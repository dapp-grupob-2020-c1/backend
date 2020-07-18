package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Turn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TurnDTO {

    public Long id;
    public Long shopId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime time;

    public TurnDTO(){}

    public TurnDTO(Turn turn) {
        this.id = turn.getId();
        this.shopId = turn.getShop().getId();
        this.time = turn.getTime();
    }

    public static List<TurnDTO> createPublicTurns(List<Turn> turns) {
        List<TurnDTO> returnList = new ArrayList<>();
        for (Turn turn : turns){
            returnList.add(new TurnDTO(turn));
        }
        return returnList;
    }
}
