<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Rss Tool - create file</title>
</head>
<body>

	<form:form method="POST" action="/rsstool/createRssFile">
	   <table>
		    <tr>
		        <td>FileName</td>
		        <td><form:input path="fileName" /></td>
		    </tr>
		    <tr>
		        <td colspan="2">
		            <input type="submit" value="Submit"/>
		        </td>
		    </tr>
		</table>  
	</form:form>
    <a href="./">Back to main menu</a>
	
	
	

      
	   
</body>
</html>