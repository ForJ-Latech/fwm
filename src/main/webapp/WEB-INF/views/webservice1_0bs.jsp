<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Webservice 1.0 bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/c/bootstrap.min.css" rel="stylesheet">
    
    <!-- Custom styles for this template -->
    <link href="/c/testbs.css" rel="stylesheet">
    
  </head>

  <body>
	    <nav class="navbar navbar-toggleable-md navbar-inverse bg-inverse fixed-top">
	<!--       <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
	        <span class="navbar-toggler-icon"></span>
	      </button> -->
	      <div class="navbar navbar-default" id="navbars">
	        <ul class="nav navbar-nav">
	          	<li class="nav-item active">
	            	<a class="nav-link" href="/">Home <span class="sr-only">(current)</span></a>
	          	</li>
	          	<li class="nav-item">
	            	<a class="nav-link" href="/webservice1_0bs/getPrev/${CurrentIndex}">&lt; Previous</a>
	          	</li>
	          	<li class="nav-item">
	            	<a class="nav-link" href="/webservice1_0bs/getNext/${CurrentIndex}">Next &gt;</a>
	          	</li>
	        </ul>
	      </div>
	    </nav>
    <!-- <header></header> -->
    <div id="display" class="container">

      	<div id="namebox" class="row text-center" >
	      	<div class="col-sm-4">
	      	</div>
	      	<div class="col-sm-4">
	      		<h2 id="name">${Name}</h2>	
	      	</div>
	      	<div class="col-sm-4">
	      	</div>	
      	</div>
      	
      	<div class="row text-center">
	      	<div id="image" class="col-sm-12">
	      		<img id="img" src="/webservice1_0bs/multimediaImage/${ImageFileName}">
	      	</div>
      	</div>
		<hr>
		<div class="row text-center">
			<div class="col-sm-4"></div>
	      	<div class="col-sm-4">
	      		<p id="desc">${Description}</p>
	      	</div>
	      	<div class="col-sm-4"></div>
		</div>

    </div><!-- /.container -->
<!-- 	<footer></footer> -->
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
   	<!-- This is the link for jQuery -->
	<script type="text/javascript" src="/s/jquery-3.2.1.min.js"></script>
    <script src="/s/bootstrap.min.js"></script>
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
		 				url: "/webservice1_0bs/objectIdCheck/" + "${CurrentIndex}",
		 				type: 'GET',
		 				contentType : "application/json",
		 				dataType: 'json',
		 				success: function(data) {
		 					
		 					console.log(data);
		 					console.log(data[0]);
		 					console.log(data[1]);
		 					
		 					if(data[0] != objId || data[1] != objClass){
		 						window.location.assign("/webservice1_0bs");
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
