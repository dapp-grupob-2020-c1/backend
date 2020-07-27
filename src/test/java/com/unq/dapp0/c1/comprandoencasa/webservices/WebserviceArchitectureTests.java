package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@AnalyzeClasses(packages = "com.unq.dapp0.c1.comprandoencasa.webservices")
public class WebserviceArchitectureTests {

    @ArchTest
    public static final ArchRule responseStatusExceptionsShouldAlwaysBeAtTheirSpecificPackage =
            classes().that().areAssignableFrom(ResponseStatusException.class)
                    .should().resideInAPackage("com.unq.dapp0.c1.comprandoencasa.webservices.exceptions")
                    .because("ResponseStatusException classes have their own subpackage at webservice level.");

    @ArchTest
    public static final ArchRule allControllerClassesShouldHaveControllerOnTheirNames =
            classes().that().areAnnotatedWith(RestController.class)
                    .and().areNotAnnotatedWith(SpringBootApplication.class)
                    .should().haveSimpleNameEndingWith("Controller")
                    .because("All controller classes must have the descriptor Controller on their names.");

    @ArchTest
    public static ArchRule allPublicGetMethodsInTheWebserviceLayerShouldReturnResponseEntities =
            methods()
                    .that().areDeclaredInClassesThat().resideInAPackage("..webservices..")
                    .and().arePublic()
                    .and().areAnnotatedWith(GetMapping.class)
                    .should().haveRawReturnType(ResponseEntity.class)
                    .because("All controller methods should wrap the response objects with an API appropiate response object.");
    @ArchTest
    public static ArchRule allPublicPostMethodsInTheWebserviceLayerShouldReturnResponseEntities =
            methods()
                    .that().areDeclaredInClassesThat().resideInAPackage("..webservices..")
                    .and().arePublic()
                    .and().areAnnotatedWith(PostMapping.class)
                    .should().haveRawReturnType(ResponseEntity.class)
                    .because("All controller methods should wrap the response objects with an API appropiate response object.");
    @ArchTest
    public static ArchRule allPublicDeleteMethodsInTheWebserviceLayerShouldReturnResponseEntities =
            methods()
                    .that().areDeclaredInClassesThat().resideInAPackage("..webservices..")
                    .and().arePublic()
                    .and().areAnnotatedWith(DeleteMapping.class)
                    .should().haveRawReturnType(ResponseEntity.class)
                    .because("All controller methods should wrap the response objects with an API appropiate response object.");
    @ArchTest
    public static ArchRule allPublicPatchMethodsInTheWebserviceLayerShouldReturnResponseEntities =
            methods()
                    .that().areDeclaredInClassesThat().resideInAPackage("..webservices..")
                    .and().arePublic()
                    .and().areAnnotatedWith(PatchMapping.class)
                    .should().haveRawReturnType(ResponseEntity.class)
                    .because("All controller methods should wrap the response objects with an API appropiate response object.");

}
