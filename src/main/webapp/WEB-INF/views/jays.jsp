<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body>
	<c:forEach items="${st}" var = "thing">
		${thing.getfName()}
	</c:forEach>
</body>
</html>
