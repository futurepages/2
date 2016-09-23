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
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleMakeAttribute {

    String nameOnForm() default "";

    String nameOnExplore() default "";

    boolean showOnExplore() default true;

    boolean showOnForm() default true;

    TemporalType useCalendarLike() default TemporalType.DATE;

    boolean createSelect() default false;

    boolean searchParam() default false;


}