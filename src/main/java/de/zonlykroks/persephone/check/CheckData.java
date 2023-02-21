package de.zonlykroks.persephone.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckData {

    String name() default "";
    String checkType() default "";
    boolean experimental() default false;
    int setbackVl() default 10;
    boolean setback() default false;

    boolean damage() default false;
    float damageAmount() default 0.5F;
}
