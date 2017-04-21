<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
  		<link rel="stylesheet" type="text/css" href="/c/webservice1_5.css">	
		
		<script type="text/javascript" src="/s/jquery-3.2.1.min.js">
		</script>
		<script src="/s/bootstrap.min.js"></script>
		<script type="text/javascript" src="/s/webservice1_5.js" />
				
	</head>
	<body>
	    	<ul>
				
       			<li><a class="active" id="home" onclick="redirectIndex();" href="#" >Home</a></li>
				<li><a>Co-Dungeon Master</a></li>
				<li><a>NPCs</a></li>
							
     	
    		</ul>
    		
    		<div class="outerbox">		    			
     			<div class="box1" align="center">
     			<br>
     			
    				<input type="text" id="myInput" onkeyup="searchvalue(this.value)" placeholder="Search Npc.." title="Npc">
     			</div>
     			<div class="box2" id="listcontainer">     			
					
     			</div>
    		</div>
	</body>
</html>

