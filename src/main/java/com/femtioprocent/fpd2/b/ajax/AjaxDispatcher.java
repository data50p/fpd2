package com.femtioprocent.fpd2.b.ajax;

import com.femtioprocent.fpd2.b.ajax.AjaxReceiver.AjaxParam;
import com.femtioprocent.fpd2.b.ajax.AjaxReceiver.AjaxParamJSONObject;
import com.femtioprocent.fpd2.util.ToString;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This is the common dispatcher for all ajax requests. This class will by reflection lookup a specific class and call a specific method according the
 * parameters from the ajax request. The Ajax request is first taken care of in a JSP-page. This is called ajax/dispatchAjax.jsp. The jsp pages corresponding
 * java code creates an instance of this class and calls the dispatch function.
 * 
 * Javascript makes a ajax http-request and send a json value. This json value is parsed and by using annotation this dispatcher will automatically bind values
 * to the method called.
 * 
 * Thus to crate a new ajax handler in the server do the following steps:
 * 
 * <ol>
 * <li/>Create a class in package com.femtioprocent.fpd2.b.ajax and let it extend com.femtioprocent.fpd2.b.ajax.AjaxReceiver. The name
 * must be AjaxReceiver_FOOBAR where FOOBAR is replaced with the parameter receiver in function dispatch. This comes from the request.getParameter("receiver")
 * on the jsp page.
 * <li/>Create a method in this class with name perform_FOOBAZ where FOOBAZ is the parameter perform from the http ajax request. This method return type must be
 * AjaxResult.<br/>
 * Annotate the paramaters in the method with @AjaxParam(name='json_name') to bind values from ajax to java.
 * <p/>
 * The return value from the method should be a subclass of AjaxResult with fields annotated witn AjaxParam the same way as the argument.
 * </ol>
 * 
 * Example of a class created to support the dispatcher in this class.
 * 
 * <pre>
 * class AjaxReceiver_FOOBAR extends AjaxReceiver {
 *     public AjaxResult perform_FOOBAZ(@AjaxParam(name = &quot;jsonArg1&quot;) String javaArg1, @AjaxParam(name = &quot;jsonArg2&quot;) int javaArg2) {
 * 
 *         String theValue1;
 *         int theValue2;
 *         // java code doing stuff and setting the variables above
 * 
 *         return new AjaxResult() {
 *             public @AjaxParam(name = &quot;jsonReturn1&quot;)
 *             String javaReturn1 = theValue1;
 *             public @AjaxParam
 *             int javaReturn2 = theValue2;
 *         };
 *     }
 * }
 * 
 * </pre>
 * 
 */
public class AjaxDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(AjaxDispatcher.class);

    public AjaxDispatcher() {
    }

    public static class DispatchResult {
        public int ord;
        public String text;
        public boolean ok;
        public Exception ex;

        DispatchResult(int ord, String text) {
            this.ord = ord;
            this.text = text;
            ok = true;
        }

        DispatchResult(int ord, String text, boolean ok) {
            this.ord = ord;
            this.text = text;
            this.ok = ok;
        }

        DispatchResult(int ord, Exception ex) {
            this.ord = ord;
            this.ex = ex;
            ok = false;
        }

        public String toString() {
            return ToString.toString(this);
        }
    }

    /**
     * Create and populate JSONObject from annotated (@AjaxParam) fields in the sub class.
     */
    public abstract static class AjaxResult {
        
        /**
         * Reformat this AjaxResult instance as a json string.
         * @return
         */
        public JSONObject toJSON() {
            JSONObject jo = new JSONObject();
            Class<? extends AjaxResult> clazz = (Class<? extends AjaxResult>) getClass();
            Field[] fields = clazz.getFields();
            for (Field f : fields) {
                String name = f.getName(); // default name if not set by annotation
                AjaxParam anno = f.getAnnotation(AjaxParam.class);
                if (anno != null) {
                    try {
                        // Use the annotation name or the java field as a json key.
                        String jsonName = anno.name().length() == 0 ? name : anno.name();
                        Object value = f.get(this);
                        jo.put(jsonName, value);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jo;
        }
    }

    /**
     * Call a function in a class.
     * Find class and method from arguments. Create class from AjaxReceiver_<receiver>. Call method with name perform_<perform>.
     * If perform is null or length 0 then call method perform() which expect to return a plain String.
     * 
     * If perform parameter has a value the call the method perform_<FOOBAR> where <FOOBAR> is the value of perform parameter.
     * In this case the return type is a JSONObject or AjaxResult.
     * 
     *  When the return value is of type AjaxResult then call the method toJSON() to convert the object to a JSONObject.
     *  
     *  Finally convert the JSONObject to a String representation and return that value.
     *  
     *  Called from dispatchAjax.jsp
     *  
     * @param receiver used to lookup a Class
     * @param perform used to call a specific method
     * @param arg argument to the specific method
     * @return
     */
    @SuppressWarnings("unchecked")
    public DispatchResult dispatch(final int ord, final String receiver, final String perform, final String arg) {
        try {
//            if ( logOnStdout(3) )
//                logger.info("ajax_dispatcher: E " + ord + " -> " + receiver + ' ' + perform);

            // assuming the class are in same package as this base class.
            // construct the name according to the rule, and instatiate it.
            String className = getClass().getPackage().getName() + ".AjaxReceiver_" + receiver;
            Class<? extends AjaxReceiver> clazz = (Class<? extends AjaxReceiver>) Class.forName(className);
            Constructor<? extends AjaxReceiver> constructor = clazz.getConstructor();
            AjaxReceiver ajaxReceiver = constructor.newInstance();

            if (perform == null || perform.length() == 0) {
                // this is the simple kind, no perform parameter used. Just call the perform method with a simple String
                // as argument and return value.
                return new DispatchResult(ord, ajaxReceiver.perform(arg));
            }

            // Not the simple kind. Passed argument is a JSON string and return a JSON string as a value.
            JSONObject argJ = (JSONObject) new JSONTokener(arg).nextValue();
            
            // scan methods and when a proper is found, collect arguments. Use annotation to get JSON key.
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                // The rule is to call e method matching the name perform_FOOBAR where FOOBAR is one parameter from the ajax call.
                if (m.getName().equals("perform_" + perform)) {
                    Class<?> returnType = m.getReturnType();
                    
                    // Check that return type of the perform_FOOBAR method is a JSONObject or of type AjaxResult
                    if (returnType != JSONObject.class && returnType != AjaxResult.class) {
                        return new DispatchResult(ord, "Method return type wrong: " + ord + " " + receiver + ' ' + perform, false);
                    }
                    
                    // populate parameters for the called perform_FOOBAR method
                    Class<?>[] parameterTypes = m.getParameterTypes();
                    // Arrays for collecting parameters of the method call
                    Object[] paramArray = new Object[parameterTypes.length];
                    
                    // If some argument is of type User, then use and set it from the user doing the Ajax call.
                    int paramIndex = 0;
                    for (Class<?> clz : parameterTypes) {
                        try {
//                            if (clz == User.class) {
//                                paramArray[paramIndex] = user;
//                            }
                        } finally {
                            paramIndex++;
                        }
                    }
                    
                    // loop all annotations and fill in the argument array (paramArray) with the values
                    // Investigate the argumnet type and extract value from the JSON from the Ajax call 
                    Annotation[][] parameterAnnotations = m.getParameterAnnotations();
                    paramIndex = 0;
                    PA: for (Annotation[] annoArr : parameterAnnotations) {
                        try {
                            if (annoArr.length == 0) {
                                // parameter already set in paramArray (At least if it is of type User)
                                continue PA;
                            }
                            // Hopefully our annotation is one of them. Out is either type AjaxParam or AjaxParamJSONObject
                            for (Annotation a : annoArr) {
                                if (a.annotationType() == AjaxParam.class) {
                                    String ajaxName = ((AjaxParam) a).name();
                                    if ( ajaxName.length() == 0 ) { // Can't extract java parameter name
                                        return new DispatchResult(ord, "Method parameter name unknown: " + paramIndex + ' ' + ord + " " + receiver + ' ' + perform, false);
                                    }
                                    // Only int, double, String and boolean supported.
                                    Class<?> argType = parameterTypes[paramIndex];
                                    if (argType == int.class || argType == Integer.class) {
                                        int argI = argJ.getInt(ajaxName);
                                        paramArray[paramIndex] = argI;
                                    } else if (argType == double.class || argType == Double.class) {
                                        double argD = argJ.getDouble(ajaxName);
                                        paramArray[paramIndex] = argD;
                                    } else if (argType == boolean.class || argType == Boolean.class) {
                                        boolean argB = argJ.getBoolean(ajaxName);
                                        paramArray[paramIndex] =  argB;
                                    } else if (argType == String.class) {
                                        String argS = argJ.getString(ajaxName);
                                        paramArray[paramIndex] = argS;
                                    } else {
                                        return new DispatchResult(ord, "Method parameter not bound: " + paramIndex + ' ' + argType + ' ' + ord + " " + receiver + ' ' + perform, false);
                                    }
                                } else if (a.annotationType() == AjaxParamJSONObject.class) {
                                    paramArray[paramIndex] = argJ; // this includes all JSON, even the decoded in AjaxParam
                                } else {
                                    // ignore annotation
                                }
                            } // for annoArr
                        } finally {
                            paramIndex++;
                        }
                    } // for parameterAnnotations
//                    logger.info("paramArray: " + ix + ' ' + Arrays.toString(paramArray));
                    
                    // call the method. JSON domain values are bound into the args array according to the @AjaxParam
                    Object returnValue = m.invoke(ajaxReceiver, paramArray);

                    // retrieve the return value as either JSONObject or a subclass of AjaxResult
                    if (returnValue instanceof JSONObject) {
                        DispatchResult result = new DispatchResult(ord, ((JSONObject) returnValue).toString());
                        //logger.info("AJAX done: " + receiver + ' ' + perform + " -> " + result);
                        return result;
                    }
                    if (returnValue instanceof AjaxResult) {
                        DispatchResult result = new DispatchResult(ord, ((AjaxResult) returnValue).toJSON().toString());
                        //logger.info("AJAX done: " + receiver + ' ' + perform + " -> " + result);
                        return result;
                    }
                    DispatchResult result = new DispatchResult(ord, returnValue.toString());
                    //logger.info("AJAX done: " + receiver + ' ' + perform + " -> " + result);
                    return result;
                }
            }
            return new DispatchResult(ord, "Method not found: " + ord + " " + receiver + ' ' + perform, false);
        } catch (SecurityException e) {
            return new DispatchResult(ord, e);
        } catch (NoSuchMethodException e) {
            return new DispatchResult(ord, e);
        } catch (IllegalArgumentException e) {
            return new DispatchResult(ord, e);
        } catch (InstantiationException e) {
            return new DispatchResult(ord, e);
        } catch (IllegalAccessException e) {
            return new DispatchResult(ord, e);
        } catch (InvocationTargetException e) {
            return new DispatchResult(ord, e);
        } catch (ClassNotFoundException e) {
            return new DispatchResult(ord, e);
        } catch (JSONException e) {
            return new DispatchResult(ord, e);
        } finally {
//            if ( logOnStdout(3) )
//                logger.info("ajax_dispatcher: L " + ord + " -> " + receiver + ' ' + perform);
        }
    }
    
//    public static boolean logOnStdout() {
//        return RuntimeConfig.is(RuntimeItem.LogAjaxRequest);
//    }
//
//    public static boolean logOnStdout(int level) {
//        return RuntimeConfig.is(RuntimeItem.LogAjaxRequest) && RuntimeConfig.getInt(RuntimeItem.LogAjaxRequest, RuntimeSubItem.INTARG_1, 0) >= level;
//    }
}
