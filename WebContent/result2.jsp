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
	
	$(function () {
		  $('[data-toggle="tooltip"]').tooltip()
		})
	
	$('table td').each(function()
			{
		$(this).text( $.trim( $(this).text() ) )
						 
			});
	
	
	
	
	
	
	$("tr").not(':first').hover(
			  function () {
			    $(this).css("background","yellow");
			  }, 
			  function () {
			    $(this).css("background","");
			  }
			);
	
	function exportTableToCSV($table, filename) {
        var $headers = $table.find('tr:has(th)')
            ,$rows = $table.find('tr:has(td)')
            // Temporary delimiter characters unlikely to be typed by keyboard
            // This is to avoid accidentally splitting the actual contents
            ,tmpColDelim = String.fromCharCode(11) // vertical tab character
            ,tmpRowDelim = String.fromCharCode(0) // null character
            // actual delimiter characters for CSV format
            ,colDelim = '","'
            ,rowDelim = '"\r\n"';
            // Grab text from table into CSV formatted string
            var csv = '"';
            csv += formatRows($headers.map(grabRow));
            csv += rowDelim;
            csv += formatRows($rows.map(grabRow)) + '"';
            // Data URI
            var csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);
        $(this)
            .attr({
            'download': filename
                ,'href': csvData
                //,'target' : '_blank' //if you want it to open in a new window
        });
        
        //------------------------------------------------------------
        // Helper Functions 
        //------------------------------------------------------------
        // Format the output so it has the appropriate delimiters
        function formatRows(rows){
            return rows.get().join(tmpRowDelim)
                .split(tmpRowDelim).join(rowDelim)
                .split(tmpColDelim).join(colDelim);
        }
        // Grab and format a row from the table
        function grabRow(i,row){
             
            var $row = $(row);
            //for some reason $cols = $row.find('td') || $row.find('th') won't work...
            var $cols = $row.find('td'); 
            if(!$cols.length) $cols = $row.find('th');  
            return $cols.map(grabCol)
                        .get().join(tmpColDelim);
        }
        // Grab and format a column from the table 
        function grabCol(j,col){
            var $col = $(col),
                $text = $col.text();
            return $text.replace('"', '""'); // escape double quotes
        }
    }
	// This must be a hyperlink
    $("#export").click(function (event) {
        // var outputFile = 'export'
        var outputFile = window.prompt("Enter the name of File") || 'export';
        outputFile = outputFile.replace('.csv','') + '.csv'
         
        // CSV
        exportTableToCSV.apply(this, [$('#dvData>table'), outputFile]);
        
        // IF CSV, don't do event.preventDefault() or return false
        // We actually need this to be a typical hyperlink
    });
	
	
	
	
	
	
	
	  
     
      
		});




</script>


 <%@ include file="/navbar/head.jsp" %>
 <br><br><br><br>
 <div class="btn btn-warning" data-toggle="tooltip" data-placement="right" title="Export the data in Excel">
                <a href="#" id ="export" role='button'>Export CSV
                </a>
            </div>
 
          
            
<center><h3><strong><em><c:out value="${email}"/></em></strong><small><strong> report in product</strong> </small> <strong><em><c:out value="${product}"/></em></strong><small><strong>   for fixed by</strong> </small>  <strong><em><c:out value="${fixBy}"/></em></strong> </h3></center>

 <div id="dvData">
        <table class="table table-striped table-bordered " id="report">
          
            <tr class="danger">
                <th><a href="#" data-toggle="tooltip" title="Bug Numbers">Bug</a></th>
                <th><a href="#" data-toggle="tooltip" title="Fixed By Person">Asignee</a></th>
                <th><a href="#" data-toggle="tooltip" title="Base Bug Number">Base</a></th>
                
                <c:forEach items="${branchs}" var="branch" >		
                 <th><a href="#" data-toggle="tooltip" title="Code checked-in Branch"><c:out value="${branch}"/></a></th>
     				</c:forEach>
            </tr>
            
            
            <c:forEach var="bug" items="${bugList}" varStatus="loop">
            
                <tr class="${loop.index % 2 == 0 ? 'even' : 'odd'}">
               
                    <td><a target="_blank" href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bug.bugNo}"><c:out  value="${bug.bugNo}" /></a></td>
                    <td><c:out value= "${bug.assignee }"></c:out></td>
                    <td><a target="_blank"  href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bug.base.bugNo}"><c:out  value="${bug.base.bugNo}" /></a></td>
               
               
               
             <c:forEach items="${branchs}" var="branch">
             <td>
             
             
             <c:forEach items="${bug.childs}" var="child">
        
             <c:choose>
    <c:when test="${child.branchs.contains(branch)}">
        <a target="_blank" href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${child.bugNo}"><c:out  value="${child.bugNo}"/></a>
    </c:when>
    
    
       </c:choose>
			
			</c:forEach>
			
			
			
<c:forEach items="${bug.baseChilds}" var="bchild">
        
             <c:choose>
    <c:when test="${bchild.branchs.contains(branch)&& bchild.bugNo!=bug.bugNo}">
        <a target="_blank"  href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bchild.bugNo}"><c:out  value="${bchild.bugNo}"/></a>
    </c:when>
    
    
       </c:choose>
			
			</c:forEach>
			
<c:choose>
<c:when test="${bug.base.branchs.contains(branch)}">
        <a target="_blank" href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bug.base.bugNo}"><c:out  value="${bug.base.bugNo}" /></a>
    </c:when>
    </c:choose>
					
			
			<c:choose>
<c:when test="${bug.branchs.contains(branch)}">
       <a target="_blank"  href="https://bugzilla.eng.vmware.com/show_bug.cgi?id=${bug.bugNo}"><c:out  value="${bug.bugNo}" /></a>
    </c:when>
    </c:choose>
			
              </td> 
                 
             </c:forEach>
     			
                    </tr>
                   
                  
         </c:forEach>
         
    
		               
            </table>
            </div>
         

     

   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>

</body>
</html>