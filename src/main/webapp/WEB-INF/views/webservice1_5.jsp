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
		<script src="/s/webservice1_5.js"></script>
				
	</head>
	<body>
		<nav class="navbar navbar-toggleable-md navbar-inverse bg-inverse fixed-top">
			<div class="navbar navbar-default" id="navbars">
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
	
    	<div id="outerbox" class="container">
    		<div class="row">
    			<span class="col-sm-4 text-center">
    				<div class="input-group">
      					<input type="text" class="form-control" id="myInput" title="Npc" placeholder="Search Npc...">
      					<span class="input-group-btn">
        					<button class="btn btn-secondary" type="button" onclick="searchvalue(document.getElementById('myInput').value);">Go!</button>
      					</span>
    				</div>
    				<div id="searchResults">Search&nbsp;Results</div>
    				<div>
						<div id="listcontainer" style="overflow: auto">
					    </div>
    				</div>
    			</span>
    			<span class="col-sm-8 text-center">
					<div class="row"><div id="cname"></div></div>
					<div class="row"><div id="name"></div></div>
					<div class="row"><img id="image"/></div>
					<div class="row"><div id="description"></div></div>
    			</span>
    		</div>
    	</div>
	</body>
</html>

