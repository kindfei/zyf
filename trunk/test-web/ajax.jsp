<html>
<script language="javascript">

/*
* Returns an new XMLHttpRequest object, or false if the browser
* doesn't support it
*/
var availableSelectList;

function newXMLHttpRequest() {
	var xmlreq = false;
	// Create XMLHttpRequest object in non-Microsoft browsers
	if (window.XMLHttpRequest) {
		xmlreq = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		try {
			// Try to create XMLHttpRequest in later versions
			// of Internet Explorer
			xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e1) {
			// Failed to create required ActiveXObject
			try {
				// Try version supported by older versions
				// of Internet Explorer
				xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e2) {
				// Unable to create an XMLHttpRequest by any means
				xmlreq = false;
			}
		}
	}
	return xmlreq;
}

/*
* Returns a function that waits for the specified XMLHttpRequest
* to complete, then passes it XML response to the given handler function.
* req - The XMLHttpRequest whose state is changing
* responseXmlHandler - Function to pass the XML response to
*/
function getReadyStateHandler(req, responseXmlHandler) {
	// Return an anonymous function that listens to the XMLHttpRequest instance
	return function () {
		// If the request's status is "complete"
		if (req.readyState == 4) {
			// Check that we received a successful response from the server
			if (req.status == 200) {
				// Pass the XML payload of the response to the handler function.
				responseXmlHandler(req.responseXML);
			} else {
				// An HTTP problem has occurred
				alert("HTTP error "+req.status+": "+req.statusText);
			}
		}
	}
}

function search(searchKey) {
	var form = document.forms[0];
	var keyValue = document.getElementById("getCities").value;
	keyValue = keyValue.replace(/^\s*|\s*$/g,"");
	if (keyValue.length > 0) {
		availableSelectList = document.getElementById("searchResult");
		var req = newXMLHttpRequest();
		req.onreadystatechange = getReadyStateHandler(req, update);
		req.open("POST","<%=request.getContextPath()%>/searchCity.do", true);
		req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		req.send("getCities="+keyValue);
	}
}

function update(cartXML) {
	var countries = cartXML.getElementsByTagName("cities")[0];
	var country = countries.getElementsByTagName("city");
	availableSelectList.innerHTML = '';
	for (var i = 0; i < country.length ; i++) {
		var ndValue = country[i].firstChild.nodeValue;
		availableSelectList.innerHTML += ndValue+"\n";
	}
}
</script>
	<input type="textbox" name="getCities" size="20" onkeyup="search(this);" style="width:300px;" autocomplete="off">
	<div align="left" id="searchResult" style="width:300px;border:#000000;"></div>
</html>
