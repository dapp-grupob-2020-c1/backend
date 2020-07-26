package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Repository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.unq.dapp0.c1.comprandoencasa.repositories")
public class RepositoriesArchitectureTests {

    @ArchTest
    public static final ArchRule repositoriesShouldAlwaysBeInterfaces =
            classes().that().areAnnotatedWith(Repository.class)
                    .should().beInterfaces()
                    .because("Repositories should implement Spring Data JPA persistence solutions.");

    @ArchTest
    public static final ArchRule allRepositoryClassesShouldHaveRepositoryOnTheirNames =
            classes().that().areAnnotatedWith(Repository.class)
                    .should().haveSimpleNameEndingWith("Repository")
                    .because("All repository classes must have the descriptor Repository on their names.");

}
