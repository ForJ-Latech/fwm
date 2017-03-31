<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.forj.fwm.startup.WorldFileUtil" %>


<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=windows-1252">
		
		<script type="text/javascript">
		// javascript for redirecting to index while clicking Home
				function redirectIndex() { 
					window.location.assign("/");
					};
					
		// javascript for redirecting previous page
				function goBack(){
					indow.location.assign("/webservice1_0/getPrev");
					};
					
		
		// javascript for redirecting next page
				function goForward(){
					window.location.assign("/webservice1_0/getNext");
					};
		</script>


	    <title></title>

		<!-- link for style sheet, anything need to be changed on style need to be changed on webservice1_0.css-->
		<link rel="stylesheet" type="text/css" href="/c/webservice1_0.css">

	</head>
	<body>
		<!--Menu-->
		<ul>
	  		<li>
	  			<a class="active" onclick="redirectIndex();">Home</a>
	  		</li>
	   		<li>
	   			<a class="centered">Player Status</a>
	   		</li>
	   		<li>
				<a href='<c:url value="/webservice1_0/getPrev/${currentIndex}"></c:url>'>&lt; Previous</a>
	   		</li>
	   		<li>
	   			<a href='<c:url value="/webservice1_0/getNext/${currentIndex}"></c:url>'>Next &gt;</a>
	   		</li>
		</ul>		
		<div class="outerbox">
			<div class="inner1 centered">
				<b>${Name}</b>
				<br>		
				<img class="main" src="/multimedia/${ImageFileName}">
			</div>
			<div class="description centered"> 
				<p>${Description}</p>
			</div>

		</div>
	</body>
</html>
