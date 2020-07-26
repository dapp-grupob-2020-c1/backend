package com.unq.dapp0.c1.comprandoencasa.model;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.unq.dapp0.c1.comprandoencasa.ComprandoEnCasaApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

@AnalyzeClasses(packages = "com.unq.dapp0.c1.comprandoencasa.model")
public class ModelArchitectureTests {

    @ArchTest
    public static final ArchRule ruleNoComponentsOnModelObjectPackages =
            noClasses().that().areAnnotatedWith(Component.class)
                    .should().resideInAnyPackage(
                            "com.unq.dapp0.c1.comprandoencasa.model.objects",
                    "com.unq.dapp0.c1.comprandoencasa.model",
                    "com.unq.dapp0.c1.comprandoencasa.model.exceptions"
                    )
                    .because("There should not be any component on the model object package.");

    @ArchTest
    public static final ArchRule runtimeExceptionsShouldAlwaysBeAtTheirSpecificPackages =
            classes().that().areAssignableFrom(RuntimeException.class)
                    .should().resideInAPackage("com.unq.dapp0.c1.comprandoencasa.model.exceptions")
                    .because("Exceptions should reside in their own specific subpackages.");


}
