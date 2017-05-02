<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=windows-1252">
		
		<!-- link for style sheet, anything need to be changed on style need to be changed on webservice1_0.css-->
		<link rel="stylesheet" type="text/css" href="/c/webservice1_0.css">
		
		<!-- This is the link for jQuery -->
		<script type="text/javascript" src="/s/jquery-3.2.1.min.js"></script>

	    <title>Webservice 1.0</title>

		<!-- link for style sheet, anything need to be changed on style need to be changed on webservice1_0.css-->
		<link rel="stylesheet" type="text/css" href="/c/webservice1_0.css">

	</head>
	<body>
		<!--Menu-->
		<ul>
	  		<li>
	  			<a id="home" onclick="redirectIndex();" href="#">Home</a>
	  		</li>
	   		<li>
				<a href='/webservice1_0/getPrev/${CurrentIndex}'>&lt; Previous</a>
	   		</li>
	   		<li>
	   			<a href='/webservice1_0/getNext/${CurrentIndex}'>Next &gt;</a>
	   		</li>
		</ul>
		
		<!--Object display-->		
		<div class="outerbox">
			<div class="inner1 centered">
				<b id="name">${Name}</b>
				<br>		
				<img id="img" class="main" src="/webservice1_0/multimediaImage/${ImageFileName}">
				<br>
				<div id="sound" class="centered audioplayer">
					<audio id="player" autoplay controls>
						<source id="audiosrc" src="" type="audio/mp3"/>
					</audio>
				</div>
			</div>
			<div class="description centered"> 
				<p id="desc">${Description}</p>
			</div>
		</div>
		<script type="text/javascript">
			// javascript for redirecting to index while clicking Home
			function redirectIndex() { 
				window.location.assign("/");
			};
		</script>
		
	 	<script type="text/javascript">
	 		$(document).ready(function() {
	 			var soundFile = "${SoundFileName}";
	 			var audio = $("#player");
	 			if (soundFile.length > 0) {
	 				//if there is sound attached to object
	 				var soundURL = "/webservice1_0/multimediaSound/" + "${SoundFileName}";
	 				var a = new Audio(soundURL);
	 				a.currentTime = 0;
	 				a.play();
	 			} else {
	 				$("#sound").hide();
	 			}
	 			
	 			var objId = "${ObjectId}";
		 		var objClass = "${ObjectClass}";
		 		
		 		$.ajaxSetup({ cache: true });
		 		setInterval(function() {
			 		$.ajax({
		 				url: "/webservice1_0/objectIdCheck/" + "${CurrentIndex}",
		 				type: 'GET',
		 				contentType : "application/json",
		 				dataType: 'json',
		 				success: function(data) {
		 					
		 					console.log(data);
		 					console.log(data[0]);
		 					console.log(data[1]);
		 					
		 					if(data[0] != objId || data[1] != objClass){
		 						window.location.assign("/webservice1_0");
		 					}
		 					
			 			},
		 				fail: function(data) {
		 					console.log("error");
		 				}
		 			});
			 	}, 2000);
	 		});
	 	</script>
	</body>
</html>
