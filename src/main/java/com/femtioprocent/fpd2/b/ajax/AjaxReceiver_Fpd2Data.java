package com.femtioprocent.fpd2.b.ajax;

import com.femtioprocent.fpd2.b.ajax.AjaxDispatcher.AjaxResult;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 * Entry point for Ajax called from PSR Data page. Used to prevent multiple users of editing the same data.
 * That is data for one ProgramUnit and an area.
 * 
 */
public class AjaxReceiver_Fpd2Data extends AjaxReceiver {
    private static final Logger logger = LoggerFactory.getLogger(AjaxReceiver_Fpd2Data.class);

    /**
     * Create the receiver for this user.
     * 
     * @param user
     */
    public AjaxReceiver_Fpd2Data() {
    }

    /**
     * Notify that a user is editing a Program Unit. Return result if somebody else is editing it.
     * 
     * How deep is the rabbit hole?<p/>
     *  The blue pill: See ajaxExample.jsp, function notifyServerNow<br/>
     *  The red pill: AjaxTransmitter.java, dispatchAjax.jsp, and AjaxDispatcher.java<br/>
     * 

     * @param puId
     * @param areaId
     * @param editAlreadyStartedSequence
     * @param argJ
     * @return
     */
    public AjaxResult perform_editEvent(@AjaxParam(name = "puId") int puId,
                                        @AjaxParam(name = "areaId") int areaId,
                                        @AjaxParam(name = "op") String op,
                                        @AjaxParam(name = "subop") String subop,
                                        @AjaxParam(name = "savedVersion") int currentSavedVersion,
                                        @AjaxParamJSONObject() JSONObject argJ) { // all params are here
        if (puId == 0 || areaId == 0)
            return getAjaxResult("", null, "");

        return getAjaxResult("", "result", "");
    }

    /**
     * Create the data to return in the Ajax call. Format later as a JSON string.
     * @param text_
     * @param nqItem_
     * @param popup_
     * @return
     */
    private AjaxResult getAjaxResult(final String text_, final String nqItem_, final String popup_) {
        //logger.info("AjaxResult: -> " + text_ + ' ' + nqItem_ + ' ' + popup_);
        return new AjaxResult() {
            public @AjaxParam String text = text_;
            public @AjaxParam String popup = popup_;
            public @AjaxParam int savedVersion = nqItem_.length();
        };
    }

    public static int getPollTime() {
    	return 21130;
    }    
}
