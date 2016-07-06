<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<style type="text/css">     
    select {
        width:228px;
        height:30px;
    }
</style>
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">

	$(document).ready(function() {
		
		$('#product').change(function(event) {
			var $product = $("select#product").val();
			$.get('ActionServlet', {
				productName : $product
			}, function(responseJson) {
				var $select = $('#version');
				$select.find('option').remove();
				$.each(responseJson, function(key, value) {
					$('<option>').text(value).appendTo($select);
				});
			});
		});
		
		
		$('#product1').change(function(event) {
			var $product = $("select#product1").val();
			$.get('ActionServlet', {
				productName : $product
			}, function(responseJson) {
				var $select = $('#version1');
				$select.find('option').remove();
				$.each(responseJson, function(key, value) {
					$('<option>').text(value).appendTo($select);
				});
			});
		});
		
		
		
	});     
</script>

<!-- Modal -->
<div class="modal fade bs-example-modal-sm" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Query Bugzilla</h4>
      </div>
      <div class="modal-body">
       <form class="margin-base-vertical" method="post" action="GetDetailData">
					<p class="input-group ">
						<span class="input-group-addon"><span class="glyphicon glyphicon-cloud"></span></span>
							<select name="product" id="product">
							<option>Product</option>
							<option value="cloud">cloud</option>
							<option value="vpx">vpx</option>
							<option value="vSphere">vSphere</option>
							</select>
					</p>

          <p class="input-group ">
						<span class="input-group-addon"><span class="glyphicon glyphicon-envelope"></span></span>
						<input type="text" required="true" required class="form-control input-sm" name="email" id="email" placeholder="nitins" />
					</p>

          <p class="input-group ">
						<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
						
      						 <select name="version" id="version">
             				<option>Fixed By</option>
							</select>
							</p>

		  <p class="text-center">
						<button type="submit" class="btn btn-success btn-lg" onclick="this.value='Submitting ..';this.disabled='disabled'; this.form.submit();">Submit</button>
					</p>
					

				</form>
      </div>
      
    </div>
  </div>
</div>

<!-- Nav bar -->

	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="/bug-villa/home.jsp"><img
					src="images/CRT.jpg" height=50 width=100></a>
			</div>
			<ul class="nav navbar-nav">
				<li class="active"><a href="/bug-villa/home.jsp"><span
						class="glyphicon glyphicon-home" aria-hidden="true"></span> Home</a></li>
						<li>
			<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target=".bs-example-modal-sm">
 Individual Search
</button>

</li>
		
			
</ul>
			<form class="navbar-form pull-right"  method="post" action="GetData">
				<div class="form-group">
					 <select name="product" id="product1">
							<option>Product</option>
							<option value="cloud">cloud</option>
							<option value="vpx">vpx</option>
							<option value="vSphere">vSphere</option>
							
							</select>
				</div>
				<div class="form-group">
					<select name="version" id="version1">
             				<option>Fixed By</option>
							</select>
				</div>

				<button type="submit" onclick="this.value='Submitting ..';this.disabled='disabled'; this.form.submit();" class="btn btn-danger">Search</button>
			</form>
		</div>
	</nav>
</body>
</html>