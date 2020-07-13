package com.unq.dapp0.c1.comprandoencasa.model;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.unq.dapp0.c1.comprandoencasa.model.objects")
public class ModelArchitectureTests {

    @ArchTest
    public static final ArchRule rule = classes().that().areNotInterfaces().and().areNotEnums()
                .should().beAnnotatedWith(Entity.class);

}
