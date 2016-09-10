package org.futurepages.annotations;

import javax.persistence.TemporalType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation para beans com os valores a serem tratados na geração do código
 * @author Jorge
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleMakeEntity {
    String name() default "";
}