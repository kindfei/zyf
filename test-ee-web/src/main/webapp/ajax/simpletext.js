function update() {
	var name = dwr.util.getValue("name");
	SimpleText.sayHello(name, function(data) {
		dwr.util.setValue("reply", data);
	});
}

function updateWithJSONP() {
	var name = dwr.util.getValue("name");
	$.post("../../../dwr/jsonp/SimpleText/sayHello/" + name, {},
			function(data) {
				dwr.util.setValue("reply", data.reply);
			}, "jsonp");
}

function sayHi() {
	var fistName = dwr.util.getValue("firstName");
	var lasstName = dwr.util.getValue("lastName");
	SimpleText.sayHi(fistName, lasstName, function(data) {
		dwr.util.setValue("reply", data);
	});
}