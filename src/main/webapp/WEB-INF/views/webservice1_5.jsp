<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
	<head>
		<meta charset="utf-8">
   		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  		<title>Search Previously Shown</title>
  		
  		<link rel="stylesheet" href="/c/bootstrap.min.css">
  		<link rel="stylesheet" type="text/css" href="/c/webservice1_5.css">	
		<script type="text/javascript" src="/s/jquery-3.2.1.min.js"></script>
		<script src="/s/bootstrap.min.js"></script>
		<script src="/s/imagesize.js"></script>
		<script src="/s/webservice1_5.js"></script>
				
	</head>
	<body>
		<nav class="navbar navbar-toggleable-sm navbar-inverse bg-inverse fixed-top">
      		<button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbars" aria-controls="navbars" aria-expanded="false" aria-label="Toggle navigation">
    			<span class="navbar-toggler-icon"></span>
 			</button>
			<div class="collapse navbar-collapse" id="navbars">
		    	<ul class="nav navbar-nav">
		          	<li class="nav-item active">
		            	<a class="nav-link" href="/">Home <span class="sr-only">(current)</span></a>
		          	</li>
		          	<li class="nav-item">
		            	<a class="nav-link" href="/webservice1_0bs">Show Players</a>
		          	</li>		          
		        </ul>
			</div>
		</nav>
    	<div class="container-fluid"><br> 
    		<!-- The main row  -->   		
    		<div class="row">
    			<!-- Left-hand search area  -->
    			<div class="col-xs-12 col-md-4 text-center">
    				<div id="searchbar" class="input-group"><br>
      					<input type="text" class="form-control" id="myInput" title="Npc" placeholder="Search...">
      					<span class="input-group-btn">
        					<button class="btn btn-secondary" type="button" onclick="searchvalue(document.getElementById('myInput').value);">Go</button>
      					</span>
    				</div>    				
    				<div class="text-center" id="searchResults"><strong>Search&nbsp;Results</strong>
    				</div>  				
					<div class="text-center" id="listcontainer" >
					</div>  			
    			</div>
    			<!-- <div class="line1"></div -->
    			<!-- The middle display for object -->
    			<div id="display" class="col-xs-12 col-md-4 text-center">
					<div class="row center">
						<div class="col-md-12">						
							<div class="row center" >
								<div class="col-md-12 text-center">
									<div id="cname">
										<div>&nbsp;&nbsp;Name, Image, Description</div>
									</div>
								</div>
							</div>
							<div class="row center">
								<div class="col-md-12 text-center">
									<strong><div id="name"></div>
									</strong>
								</div>
							</div>
							<div class="row center">
								<div class="col-md-12 text-center" id="imagecontainer">
								</div>
							</div><br>
							<!-- <div class="style2"> -->
							<div class="row center">
								<div class="col-md-12 text-center">
									<div id="description">&nbsp;&nbsp;</div>
								</div>
							</div>
						</div>
					</div>					
				</div>
    			<!-- <div class="line2"></div> -->
    			<!-- The relational list container  -->
    			<div class="col-xs-12 col-md-4">
					<div id="rellistcontainer" class="text-center bold center">			
						<div>Region/God/Group Relations</div>		
					</div>
				</div>
			</div>
    	</div>
	</body>
</html>

