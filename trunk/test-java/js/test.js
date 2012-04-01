function validate(obj) {
	var es = obj.elements;
	for (var i = 0; i < es.length; i++) {
		if (es[i].type != "text") {
			continue;
		}
		
		var defValue = Number(es[i].defaultValue);
		var newValue = Number(es[i].value);
		
		if (isNaN(defValue)) {
			defValue = es[i].defaultValue.toLowerCase();
			newValue = es[i].value.toLowerCase();
			alert(defValue + " - " + newValue);
		}
		
		if (newValue != defValue) {
			alert(defValue + " != " + newValue);
		}
	}
}