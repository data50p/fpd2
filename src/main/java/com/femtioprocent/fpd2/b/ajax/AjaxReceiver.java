package com.femtioprocent.fpd2.b.ajax;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base class for a receiver of Ajax call.
 * 
 */
public abstract class AjaxReceiver {

    /**
     * Annotate java parameters for automatic binding from/to JSONObject key/value pairs. 
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    static @interface AjaxParam {
        String name() default ""; // then try to use java param/field name if possible. Possible when returning AjaxParm. Not when
                                  // it is an argument parameter.
    }

    /**
     * Annotate java parameters for automatic binding from/to the JSONObject itself.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    static @interface AjaxParamJSONObject {
    }
    
    public AjaxReceiver() {
    }
    
    /**
     * Override this or create a method with name perform_FOOBAR where FOOBAR matches Ajax perform parameter value.
     * @param arg the string send by ajax call in javascript on a web page.
     * @return
     */
    public String perform(String arg) {
        return "";
    }
}
