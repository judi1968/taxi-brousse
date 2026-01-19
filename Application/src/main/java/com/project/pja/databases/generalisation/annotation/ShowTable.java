package com.project.pja.databases.generalisation.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Définir l'annotation
@Retention(RetentionPolicy.RUNTIME) 
@Target({ ElementType.METHOD }) 
public @interface ShowTable {
    String name() default ""; 
    int numero() default 0;
}
