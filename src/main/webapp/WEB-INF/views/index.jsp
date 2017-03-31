<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/c/index.css">
	<title> Webservice Selection </title>
	<script>
		function redirectWS1_0() {
			window.location.assign("/startwebservice1_0");
	    };
	    function redirectWS1_5() {	    	
	    	windows.location.assign("/startwebservice1_5");
	    };
	</script>
</head>
<body>
	<div align="center"><br>
	<h2> Select a webservice </h2>
	<hr>
	<form>
		<input type="button" value="Web Service 1.0" id="ws1.0" onclick="redirectWS1_0()" /> </br>
		<input type="button" value="Web Service 1.5" id="ws1.5" onclick="redirectWS1_5()" /> </br>
		<input type="button" value="Web Service 2.0" id="ws2.0" onclick="Page not found" /> </br>
	</form> 
	</div>
</body>
</html>

