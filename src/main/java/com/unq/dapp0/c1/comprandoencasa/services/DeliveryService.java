package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtHome;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtShop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingListEntry;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Turn;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import com.unq.dapp0.c1.comprandoencasa.repositories.DeliveryRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShoppingListEntryRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.TurnRepository;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.DeliveryDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.EmailServiceExceptions;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.MissingFieldsForDeliveryCreationException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.NotAnActiveShoppingListException;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDeliveryCreationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDeliveryDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ShoppingListEntryRepository shoppingListEntryRepository;

    @Autowired
    private TurnRepository turnRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private MailService mailService;

    private static final Logger log = LogManager.getLogger(DeliveryService.class);

    @Transactional
    public void delete(ShopDelivery delivery) {
        this.deliveryRepository.delete(delivery);
    }

    @Transactional
    public void notifyMailDeliveries() {
        List<String> exceptions = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.now().plusHours(2).minusMinutes(15);
        LocalDateTime endDate = LocalDateTime.now().plusHours(2).plusMinutes(15);
        List<ShopDelivery> deliveryList = this.deliveryRepository.getAllActiveScheduledDeliveriesBetween(startDate, endDate);
        for (ShopDelivery delivery : deliveryList) {
            try {
                sendMailReminder((DeliveryAtShop) delivery);
            } catch (MessagingException exception) {
                exceptions.add(exception.getLocalizedMessage());
            }
        }
        if (!exceptions.isEmpty()) {
            throw new EmailServiceExceptions(exceptions);
        }
    }

    private void sendMailReminder(DeliveryAtShop delivery) throws MessagingException {
        String to = delivery.getUser().getEmail();
        String subject = "Reminder: You have a scheduled turn for " + delivery.getShop().getName();
        String body = "Hello, dear customer! This is a reminder for your appointment at "
                + delivery.getShop().getName() +
                " to pick up your purchase. Your turn is in 2 hours, at " +
                delivery.getTurn().getTime().toLocalTime().toString() +
                ", don't be late! Best regards, Comprando en Casa.";
        mailService.sendASynchronousMail(to, subject, body);
    }

    @Transactional
    public List<ShopDeliveryDTO> doPurchase(Long userId, Collection<ShopDeliveryCreationDTO> deliveries) {
        User user = this.userService.findUserById(userId);
        List<ShopDelivery> newDeliveries = new ArrayList<>();
        List<Shop> shops = new ArrayList<>();
        for (ShopDeliveryCreationDTO deliveryDTO : deliveries) {
            Shop shop = this.shopService.findShopById(deliveryDTO.shopId);
            newDeliveries.add(createNewDelivery(user, shop, deliveryDTO));
            shops.add(shop);
        }
        user.finishPurchase();
        List<Turn> allTurns = getAllTurns(newDeliveries);
        this.turnRepository.saveAll(allTurns);
        this.deliveryRepository.saveAll(newDeliveries);
        this.shopService.saveAll(shops);
        this.userService.save(user);
        this.sendMailForAllNewDeliveries(newDeliveries);
        return ShopDeliveryDTO.createDeliveries(newDeliveries);
    }

    private void sendMailForAllNewDeliveries(List<ShopDelivery> newDeliveries) {
        for (ShopDelivery delivery : newDeliveries) {
            sendMailForNewDelivery(delivery);
        }
    }

    private void sendMailForNewDelivery(ShopDelivery delivery) {
        String to = delivery.getUser().getEmail();
        String subject = "Here's your ticket for your purchase at " + delivery.getShop().getName();
        String body = "<p>Hello, dear customer! This is a verification mail for your purchase at "
                + delivery.getShop().getName() +
                " </p>" +
                listTicketElements(delivery.getProducts())
                +
                "<p>Best regards, Comprando en Casa.</p>";
        try {
            mailService.sendASynchronousMail(to, subject, body);
        } catch (MessagingException exception) {
            log.error("Error while sending checkout email " + exception.getMessage());
        }
    }

    private String listTicketElements(List<ShoppingListEntry> products) {
        StringBuilder message = new StringBuilder();
        for (ShoppingListEntry entry : products) {
            Product product = entry.getProduct();
            message.append(" <p>").append(product.getName()).append(" of brand ").append(product.getBrand()).append(" x ").append(entry.getQuantity()).append("</p> ");
        }
        return message.toString();
    }

    private List<Turn> getAllTurns(List<ShopDelivery> deliveries) {
        List<Turn> turns = new ArrayList<>();
        for (ShopDelivery delivery : deliveries) {
            if (delivery instanceof DeliveryAtShop) {
                turns.add(((DeliveryAtShop) delivery).getTurn());
            }
        }
        return turns;
    }

    private ShopDelivery createNewDelivery(User user, Shop shop, ShopDeliveryCreationDTO deliveryDTO) {
        List<ShoppingListEntry> products = findEntries(deliveryDTO.shoppingEntryIds);
        if (deliveryDTO.turn != null) {
            Turn turn = new Turn(shop, deliveryDTO.turn);
            DeliveryAtShop delivery = new DeliveryAtShop(shop, products, user, turn);
            shop.addDelivery(delivery);
            user.addNewDelivery(delivery);
            return delivery;
        } else if (deliveryDTO.locationId != null && deliveryDTO.dateOfDelivery != null) {
            ShoppingList activeList = user.getActiveShoppingList();
            if (activeList.getId() == null) {
                throw new NotAnActiveShoppingListException(user.getId());
            }
            Location location = activeList.getDeliveryLocation();
            DeliveryAtHome delivery = new DeliveryAtHome(shop, products, user, location, deliveryDTO.dateOfDelivery);
            shop.addDelivery(delivery);
            user.addNewDelivery(delivery);
            return delivery;
        } else {
            throw new MissingFieldsForDeliveryCreationException(user, deliveryDTO);
        }
    }

    private List<ShoppingListEntry> findEntries(Collection<Long> shoppingEntryIds) {
        List<ShoppingListEntry> entries = new ArrayList<>();
        this.shoppingListEntryRepository.findAllById(shoppingEntryIds).forEach(entries::add);
        return entries;
    }

    @Transactional
    public List<ShopDelivery> getUserActiveDeliveries(Long userId) {
        User user = userService.findUserById(userId);
        return user.getActiveDeliveries();
    }

    @Transactional
    public List<ShopDelivery> getUserHistoricDeliveries(Long userId) {
        User user = userService.findUserById(userId);
        return user.getHistoricDeliveries();
    }

    @Transactional
    public ShopDelivery confirmDeliveryReception(Long userId, Long deliveryId) {
        User user = userService.findUserById(userId);
        ShopDelivery delivery = findDeliveryForUser(user, deliveryId);
        Shop shop = delivery.getShop();
        user.confirmDeliveryReception(delivery);
        shop.confirmDeliveryReception(delivery);
        this.userService.save(user);
        return delivery;
    }

    private ShopDelivery findDeliveryForUser(User user, Long deliveryId) {
        Optional<ShopDelivery> deliveryOptional = user.getActiveDeliveries().stream()
                .filter(shopDelivery -> shopDelivery.getId().equals(deliveryId)).findFirst();
        if (deliveryOptional.isPresent()){
            return deliveryOptional.get();
        } else {
            throw new DeliveryDoesntExistException(deliveryId);
        }
    }

    @Transactional
    public ShopDelivery cancelDelivery(Long userId, Long deliveryId) {
        User user = userService.findUserById(userId);
        ShopDelivery delivery = findDeliveryForUser(user, deliveryId);
        Shop shop = delivery.getShop();
        user.cancelDelivery(delivery);
        shop.cancelDelivery(delivery);
        this.userService.save(user);
        this.shopService.save(shop);
        this.deliveryRepository.delete(delivery);
        return delivery;
    }
}
