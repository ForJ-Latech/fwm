<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
	<head>
		<meta charset="utf-8">
   		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  		<title>Search Stuff</title>
  		
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
		            	<a class="nav-link" href="">Co-Dungeon Master</a>
		          	</li>
		          	<li class="nav-item">
		            	<a class="nav-link" href="">NPCs</a>
		          	</li>
		        </ul>
			</div>
		</nav>
	
    	<div class="container outerbox"><br>    		
    		<div class="row">
    			
    			<span class="col-sm-4 text-center">
    				<div class="input-group"><br>
      					<input type="text" class="form-control" id="myInput" title="Npc" placeholder="Search...">
      					<span class="input-group-btn">
        					<button class="btn btn-secondary" type="button" onclick="searchvalue(document.getElementById('myInput').value);">Search</button>
      					</span>
    				</div>    				
    				<div id="searchResults"><strong>Search&nbsp;Results</strong>
    				</div>
    				<div>    				
						<div class="text-center"  id="listcontainer" >
						
					    </div>
    				</div>    			
    			</span>
    			<div class="line1"></div>
    			<span class="col-sm-8 text-center">
					<div class="row text-center" style="width:100%;">
					<span class="col-sm-8">
						<div class="row center" ><div id="cname"></div></div>
						<div class="row center"><font size="6"><strong><div id="name"></div></strong></font></div><br>
						<div class="row center" id="imagecontainer"></div><br>
						<div class="row"><font size="5"><div id="description"></div></font></div><br>
						</span>	
						<div class="line2"></div>				
					<span class="col-sm-4">
						<div id="rellistcontainer" class="text-center bold">
						
						</div>
					</span>
					</div>
    			</span>
    		</div>
    	
    	</div>
    	
	</body>
</html>

