
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<html>
	<head>
  		<link rel="stylesheet" type="text/css" href="/c/webservice1_5.css">	
			
		<script type="text/javascript">
			// javascript for redirecting to index while clicking Home
			function redirectIndex() { 
				window.location.assign("/");
			};
			function searchvalue(){				
				window.location.assign(window.location + "/" + searchvalue ):
			}
		</script>	
		
				
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
     			
    				<input type="text" id="myInput" onkeyup="searchvalue()" placeholder="Search Npc.." title="Npc">
    				<div>
     				<!-- -------------------C:out and C:foreach for JSTL --> 
     				   <c:forEach items="${found}" var = "thing">	
     				    <br/>
						<a href="/loadNpc/${thing.getID()}"> ${thing.getfName()} </a>
					  </c:forEach> 	    					
					<!-- ------------------------------------------------- -->
     				</div>
     			</div>
     			<div class="box2">     			
     			<p style="text-align:center;"> <strong>${npc.getfName()} </strong> </p> <br>
     			<img id="img" src="/multimedia/${npc.getImageFileName()}"><br>
     			${npc.getDescription()}
     			</div>
    		</div>
	</body>
</html>

