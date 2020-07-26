package com.unq.dapp0.c1.comprandoencasa.model;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Component;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

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
