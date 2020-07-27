package com.unq.dapp0.c1.comprandoencasa.services;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.unq.dapp0.c1.comprandoencasa.services")
public class ServicesArchitectureTests {
    @ArchTest
    public static final ArchRule runtimeExceptionsShouldAlwaysBeAtTheirSpecificPackages =
            classes().that().areAssignableFrom(RuntimeException.class)
                    .should().resideInAPackage("com.unq.dapp0.c1.comprandoencasa.services.exceptions")
                    .because("Exceptions should reside in their own specific subpackages.");

}
