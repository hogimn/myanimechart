package com.hogimn.myanimechart.service.batch.history;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SaveBatchHistory {
    String value();
    boolean saveDirectly() default false;
}
