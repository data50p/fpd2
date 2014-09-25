/**
 * Ajax Transmitter
 *
 * A function to send an Ajax request. This is a bit special expecting a servlet to decode the
 * argument in the query string to locata a Java class and call a specific method.
 * Arg is supposed to be a JSON string with parameters of said function in the Java method.
 *
 * If perform is '' then arg and return value type is simple string. Else it is JSON. 
 * 
 */

function AjaxTransmitter(receiver, performer) {
	var seq = 0;
	
	this.send = function(arg, resultCallback) {
	     var argS = typeof arg === 'string' && performer == '' ?
		            encodeURIComponent(arg) :
		            encodeURIComponent(JSON.stringify(arg));

		$.ajax({
			type:       'post',
			cache:      false,
			async:      true,
			timeout:    5000,
			url:        "ajax/dispatchAjax.jsp?seq=" + ++seq + "&receiver=" + receiver + "&perform=" + performer + "&arg=" + argS
			
		}).done(function(data) {
   		 	var trimmed = data == null ? "" : $.trim(data);
   		 	var result = null;
   		 	if ( performer == '' ) {
   		 		// return value is plain text
   		 		result = trimmed;
   		 	} else {
   		 		// return value is a JSON encoded string
   		 		result = JSON && JSON.parse(trimmed) || $.parseJSON(trimmed)
   		 	}
   		 	//console.log("AJAX done: " + receiver + ' ' + performer +  " -> " + tresponsText);
   		 	if ( performer == '' && result.match("^"+"[{]") == "{")	    				 
   		 		resultCallback("Wrong result! Please retry operation");
   		 	else
   		 		resultCallback(result);		       
		});
	}
}
