<%@page import="com.femtioprocent.fpd2.util.MilliTimer"%>
<%@page import="com.femtioprocent.fpd2.b.ajax.AjaxDispatcher"%>
<%@ page contentType="text/plain" %>
<%@ page language="java"%>
<%@ page import="com.femtioprocent.fpd2.b.ajax.AjaxDispatcher"%>
<%@ page import="com.femtioprocent.fpd2.b.ajax.AjaxDispatcher.DispatchResult"%>
<%@ page import="java.util.concurrent.atomic.AtomicInteger"%>

<%!/**
This JSP handles the ajax requests from the browser (which is using JavaScript in ajaxTransmitter.jsp)
It (the java code below) will call method dispatch defined in class AjaxDispatcher.

*/

    private static final Logger logger = LoggerFactory.getLogger("dispatchAjax.jsp");

    static private AtomicInteger ordinal = new AtomicInteger();
    static private AtomicInteger ok = new AtomicInteger();
    static private AtomicInteger currentCount = new AtomicInteger();%>
<%
    String result = "";

    MilliTimer mt = new MilliTimer();
    int ord = ordinal.incrementAndGet();
    
    int myCurrentCount1 = currentCount.incrementAndGet();
    
    try {
        // there are three (4) parameterts from the ajax request.
        String receiver = request.getParameter("receiver");
        String perform = request.getParameter("perform");
        String arg = request.getParameter("arg");
        String browserSeq = request.getParameter("seq"); // avoid http GET cache
        
        // I do not know why? Arg is sent as ""foobar"" instead of "foobar";
        if ( arg.length() > 0 && arg.charAt(0) == '"' )
    arg = arg.substring(1);
        if ( arg.length() > 0 && arg.charAt(arg.length()-1) == '"' )
    arg = arg.substring(0, arg.length()-1);
        
        UserSessionContext userSessionContext = PSRUserSessionContext.getContext(request);
        User user = userSessionContext.getUser();
        String cdsid = user == null ? "       ?" : ("" + user.getCdsId() + "        ").substring(0, 8);
        
        if (!userSessionContext.isLoggedOn()) {
    System.out.println("Ajax: ! " + ord + ' ' + mt.getString() + " ms " +
                       cdsid + ' ' + browserSeq + ' ' +
                       receiver + ' ' + perform + ' ' + 
                       arg + " -> " + "<User not logged in " + userSessionContext.getUser() + 
                       ">");
    result = "User is not logged in";
        } else {
    AjaxDispatcher adp = new AjaxDispatcher(user);
    DispatchResult dispatchResult = adp.dispatch(ord, receiver, perform, arg);
    result = dispatchResult.text;
    int o;
    char code;
    if ( dispatchResult.ok ) {
        o = ok.incrementAndGet();
        code = '+';
    } else {
        o = ok.get();
        code = '-';
    }
    if (AjaxDispatcher.logOnStdout())
        System.out.println("Ajax: " + code + " " + ord + ' ' + o + "(" + (ord-o) + ") " + mt.getString() + " ms " +
                           cdsid + ' ' + browserSeq + ' ' +
                           receiver + ' ' + perform + ' ' + 
                           arg + " -> " + result);
        }
    } catch (Exception e) {
        result = "" + e;
        System.err.println("Ajax: Exception! " + ord + ' ' + mt.getString() + " ms; " + result + ' ' + e);
    } finally {
        int myCurrentCount2 = currentCount.decrementAndGet();
        if (AjaxDispatcher.logOnStdout(1))
    System.out.println("AjaxCurrentCount: " + myCurrentCount1 + ' ' + myCurrentCount2 + ' ' + (myCurrentCount2-myCurrentCount1+1));        
    }
%>
<%=result%>
