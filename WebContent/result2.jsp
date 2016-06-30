<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/jquery-3.0.0.min.js"></script>




<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">



<link href="css/tool.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="images/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<title>Report</title>
</head>
<body>

<script type="text/javascript">
$(document).ready(function()
		{
	$('#report td').each(function()
			{
			 if($(this).text()){
				
				 $('td').css('background-color','red');
				 
			 }
			});
	
	
		});

</script>


 <%@ include file="/navbar/head.html" %>
<center><h3><c:out value="${email}"/> report in product <c:out value="${product}"/> for fixed by <c:out value="${fixBy}"/> </h3></center>


        <table id="report" align="center" border="2" cellpadding="10">
          <thead>
            <tr>
                <th>Bug</th>
                <th>Base</th>
                
                <c:forEach items="${branchs}" var="branch" >		
                 <th><c:out value="${branch}"/></th>
     				</c:forEach>
            </tr>
            </thead>
            
            
            <c:forEach var="bug" items="${bugList}" varStatus="loop">
                <tr class="${loop.index % 2 == 0 ? 'even' : 'odd'}">
               
                    <td><a target="_blank" href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bug.bugNo}"><c:out value="${bug.bugNo}" /></a></td>
                    <td><a target="_blank"  href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bug.base.bugNo}"><c:out value="${bug.base.bugNo}" /></a></td>
               
               
               
             <c:forEach items="${branchs}" var="branch">
             <td>
             
             
             <c:forEach items="${bug.childs}" var="child">
        
             <c:choose>
    <c:when test="${child.branchs.contains(branch)}">
        <a target="_blank" href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${child.bugNo}"><c:out value="${child.bugNo}"/></a>
    </c:when>
    
    
       </c:choose>
			
			</c:forEach>
			
			
			
<c:forEach items="${bug.baseChilds}" var="bchild">
        
             <c:choose>
    <c:when test="${bchild.branchs.contains(branch)&& bchild.bugNo!=bug.bugNo}">
        <a target="_blank"  href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bchild.bugNo}"><c:out value="${bchild.bugNo}"/></a>
    </c:when>
    
    
       </c:choose>
			
			</c:forEach>
			
<c:choose>
<c:when test="${bug.base.branchs.contains(branch)}">
        <a target="_blank" href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bug.base.bugNo}"><c:out value="${bug.base.bugNo}" /></a>
    </c:when>
    </c:choose>
					
			
			<c:choose>
<c:when test="${bug.branchs.contains(branch)}">
       <a target="_blank"  href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bug.bugNo}"><c:out value="${bug.bugNo}" /></a>
    </c:when>
    </c:choose>
			
              </td> 
                 
             </c:forEach>
     			
                    </tr>
         </c:forEach>
    
		               
            </table>

       
            

   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>

</body>
</html>