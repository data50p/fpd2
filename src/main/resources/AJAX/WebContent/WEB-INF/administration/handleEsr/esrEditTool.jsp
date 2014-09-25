<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ page contentType="text/html; charset=utf-8"%>

<script type="text/javascript">
var enableAjax = true;
</script>
</c:if>

<script type="text/javascript">    

<%int pollTime = com.femtioprocent.fpd2.b.ajax.AjaxReceiver_FpdData.getPollTime();%>

var pollTime = <%= pollTime %>;

var notifyServerajaxSetInterval = 0;

function notifyServerInit() {
    if ( !enableAjax )
        return;

    notifyServer();
}

function notifyServer() {
	if ( !enableAjax )
		return;

    if ( notifyServerajaxSetInterval != 0 )
		return;
	
    notifyServerNow();
    notifyServerajaxSetInterval = setInterval("notifyServerNow()", pollTime);
}

var ordinal = 0;

if ( enableAjax ) {
	// This ajax service check if page is edited by other user or if the data was saved by other user
	var esrAjaxTransmitter = new AjaxTransmitter("FpdData", "editEvent");
}

function notifyServerNow() {
    if ( !enableAjax )
        return;

    var op = SCM.isSaveEnabled() && CM.isContentModified() ? "edit" : "view"; // I'm editing or only viewing data
    var cmTrigger = CM.isContentModifiedTrigger(); 
    var subop = SCM.isSaveEnabled() && cmTrigger ? "content_changed" : "";    // tell the ajax server I did start editing
    
    var argJ = {
            "puId": document.getElementById('currentProgramUnit.id').value,
            "areaId": document.getElementById('areaSaveId').value,
            "op": op,
            "subop": subop,
            "savedVersion": document.getElementById('savedVersion').value
    };
    esrAjaxTransmitter.send(argJ, function(ajaxResult) {
        if ( ajaxResult != null ) {
            // compare the savedVersion when this page was loaded (mySavedVersion) and the current from the app-server.
            var mySavedVersion = document.getElementById('savedVersion').value;
            var updatedSavedVersion = ajaxResult.savedVersion; 
            if ( ajaxResult.text.length == 0 && updatedSavedVersion > mySavedVersion ) {
                ajaxResult.text = "The page is invalid for edit, please reload";
                ajaxResult.popup = "Press Cancel to reload the page.";
                SCM.enableCancel();    		
            }
            notifyServerajaxTimeout = 0;
            // Update text on the page
            SCM.SetExtra(ajaxResult.text, ajaxResult.popup);
            if( ajaxResult.text.length > 0 )
                // there is e text info probably sombody else is editing the page
                SCM.disableSave();
        }
    });
}

var opStop = "stop";

function notifyServerStopNow() {
    if ( !enableAjax )
        return;

    var argJ = {
            "puId": document.getElementById('currentProgramUnit.id').value,
            "areaId": document.getElementById('areaSaveId').value,
            "op": opStop, // send "edit" if pressing save button, else "stop"
            "subop": "",
            "savedVersion": document.getElementById('savedVersion').value
    };
    esrAjaxTransmitter.send(argJ, function(ajaxResult) {
        notifyServerajaxTimeout = 0;
        if ( ajaxResult != null ) {
            SCM.SetExtra(ajaxResult.text, ajaxResult.popup);
        } else {
            SCM.SetExtra("", "");
        }
    });
}

if ( enableAjax ) {
    var existingHandler = window.onbeforeunload;
    window.onbeforeunload = function(event) {
        if ( notifyServerajaxSetInterval != 0 )
            clearInterval(notifyServerajaxSetInterval)
        notifyServerajaxSetInterval = 0;
        notifyServerStopNow();             // some browser (chrome!) do not send this. IE do send it.
        
        if (existingHandler)
            existingHandler(event);    
    };
}


if(typeof String.prototype.trim !== 'function') {
	  String.prototype.trim = function() {
	    return this.replace(/^\s+|\s+$/g, ''); 
	  }
}

</script>

hello world
