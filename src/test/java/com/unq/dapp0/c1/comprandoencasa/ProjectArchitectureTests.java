package com.unq.dapp0.c1.comprandoencasa;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.unq.dapp0.c1.comprandoencasa")
public class ProjectArchitectureTests {

    @ArchTest
    public static final ArchRule applicationShouldBeAtTheTopmostPackage =
            classes().that().areAnnotatedWith(SpringBootApplication.class)
                    .should().resideInAPackage("com.unq.dapp0.c1.comprandoencasa")
                    .because("The spring application needs to be at the topmost package so every component is scanned.");

    @ArchTest
    public static final ArchRule ruleModelObjectsAsEntitiesShouldBeOnTheirPackage =
            classes().that().areAnnotatedWith(Entity.class)
                    .should().resideInAPackage("com.unq.dapp0.c1.comprandoencasa.model.objects")
                    .because("Model objects persisted in the database should be at their designated package.");

    @ArchTest
    public static final ArchRule ruleRepositoryObjectsShouldBeOnTheirPackage =
            classes().that().areAnnotatedWith(Repository.class)
                    .should().resideInAPackage("com.unq.dapp0.c1.comprandoencasa.repositories")
                    .because("Repositories should be at their designated package.");

    @ArchTest
    public static final ArchRule ruleServiceObjectsShouldBeOnTheirPackage =
            classes().that().areAnnotatedWith(Service.class)
                    .should().resideInAnyPackage(
                    "com.unq.dapp0.c1.comprandoencasa.services",
                    "com.unq.dapp0.c1.comprandoencasa.services.security")
                    .because("Services should be at their designated package.");

    @ArchTest
    public static final ArchRule ruleWebserviceObjectsShouldBeOnTheirPackage =
            classes().that().areAnnotatedWith(RestController.class).and().areNotAnnotatedWith(SpringBootApplication.class)
                    .should().resideInAnyPackage(
                    "com.unq.dapp0.c1.comprandoencasa.webservices",
                    "com.unq.dapp0.c1.comprandoencasa.webservices.security"
            )
                    .because("Webservices should be at their designated package.");

    @ArchTest
    public static final ArchRule allExceptionClassesShouldHaveExceptionOnTheirNames =
            classes().that().areAssignableFrom(RuntimeException.class)
                    .or().areAssignableFrom(ResponseStatusException.class)
                    .should().haveSimpleNameEndingWith("Exception")
                    .because("All exception derived classes must have the descriptor Exception on their names.");

}
