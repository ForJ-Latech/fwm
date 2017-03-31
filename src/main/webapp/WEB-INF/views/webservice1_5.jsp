<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<script>
		
			function redirectIndex() 
			{
				window.location.assign("/");
	    		};
		</script>
		<style> 
			ul {
    			list-style-type: none;
    			margin: 0;
    			padding: 0;
    			overflow: hidden;
    			background-color: dimgrey;    			
				}

			li {
    			float: left;
    			border-right:1px solid #bbb;
				}

			

			li a {
					display: block;
					color: white;
    				text-align: center;
   					padding: 14px 16px;
    				text-decoration: none;
					}

			.active {
    					background-color: limegreen;
    				}

			.div2 	{
    				width: 1150px;
    				height: 500px;    
    				padding: 50px;
   					border: 3px solid blue;
   				
   					position: absolute;
    				left: 1%;
   					top : 10%
				}
				
		 img 	{
		 		display :block;
		 		margin: auto;
		 		position: auto;	
		 		box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
		 		 		
		 	}
			
		body 	{
				background-color: lightgrey;
			}
		</style>
	</head>
	<body>
		<ul>
  			<li><a class="active" href="redirectIndex();" >Home</a></li>
   			<li><a>Co-Dungeon Master</a></li>
			</ul>
		
		
		</div>
		
		<div class="div2">
			
				
		
		<p>
			
		</p>
	</div>
	</body>
</html>

