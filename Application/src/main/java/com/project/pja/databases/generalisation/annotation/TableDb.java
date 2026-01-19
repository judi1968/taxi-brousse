package com.project.pja.databases.generalisation.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Définir l'annotation
@Retention(RetentionPolicy.RUNTIME) // Indique quand l'annotation est disponible (exécution, compilation, ou source)
@Target({ElementType.TYPE}) // Spécifie où l'annotation peut être appliquée
public @interface TableDb {
    String name() default ""; // Déclare un élément avec une valeur par défaut (ici c'est un string vide)

    boolean isTable() default false;
    // int level() default 1; // Un autre élément avec une valeur par défaut
}
