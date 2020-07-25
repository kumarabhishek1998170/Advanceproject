<%@page import="java.util.HashMap"%>
<%
HashMap userDetails=(HashMap)session.getAttribute("userDetails");
if(userDetails!=null){
	String temail=request.getParameter("temail");
	String email=(String)userDetails.get("email");
	db.DbConnect db=new db.DbConnect();
    java.util.HashMap tUserDetails=db.getUserByEmail(temail);
    if(tUserDetails!=null){
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PeopleTalk</title>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="css/custom.css" rel="stylesheet">
	
	<script language="Javascript" src="js/jquery.js"></script>
	<script type="text/JavaScript" src='js/state.js'></script>
  </head>
  <body data-spy="scroll" data-target="#my-navbar">
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="profile.jsp">PeopleTalk</a>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><div class="navbar-text"><p>Welcome: <%= userDetails.get("name")%>  </p></div></li>
					<li><a href="profile.jsp">Home</a></li>
					<li><a href="Logout">Logout</a><li>
				</ul>			
			</div>
		</div>
	</nav>
	<%
    String msg=(String)session.getAttribute("msg");
    if(msg!=null){
%>
        <div class="panel panel-primary">
            <div class="panel-heading text-center">
                <p><%=msg%></p>
            </div>
        </div>
<%    
        session.setAttribute("msg",null);
    }
%>
		<div class="container">
			<div class="row">
				<div class="col-lg-4">
					<img src="GetPhoto?email=<%=tUserDetails.get("email")%>" height=130 width=100>
				</div>
				<div class="col-lg-4">
					<div class="form-group">
					</br>
						<label for="email" class="control-label">Name: <font color="grey"><%=tUserDetails.get("name")%></font></label><br>
						<label for="gender" class="control-label">Gender: <font color="grey"><%=tUserDetails.get("gender")%></font></label><br>
						<label for="phone" class="control-label">Phone: <font color="grey"><%=tUserDetails.get("phone")%></font></label><br>
					</div>
				</div>
				<div class="col-lg-4">
					<div class="form-group">
					</br>
						<label for="name" class="control-label">Email:<font color="grey"> <%=tUserDetails.get("email")%></font></label><br>
						<label for="dob" class="control-label">Date of Birth: <font color="grey"><%=tUserDetails.get("dob")%></font></label><br>
						<label for="address" class="control-label">Address: <font color="grey"><%=tUserDetails.get("area")%>,<%=tUserDetails.get("city")%>,<%=tUserDetails.get("state")%></font></label><br>
					</div>
				</div>
			</div>
		</div>
		</br>
		<div class="container text-center">
			<div class="panel panel-default">
				<div class="panel-body text-center">
					<form action="SendMessage" data-toggle="validator" method='post' enctype='multipart/form-data' class="form-horizontal">
						<div class="form-group">
							<label for="message" class="col-lg-2 control-label">Message:</label>
								<div class="col-lg-4">
									<textarea id="message" name="message" class="form-control" rows="5" cols="50" required></textarea>
								</div>
						</div><!--end form group-->
							<div class="form-group">
							<label for="filetosend" class="col-lg-2 control-label">File to Send:</label>
								<div class="col-lg-4">
									<input type="file" name="ufile" class="form-control" id="filetosend"/>
								</div>
								<div class="col-lg-2">
								<input type='hidden' name='temail' value='<%=tUserDetails.get("email")%>'/>
									<button type="submit" class="btn btn-primary">Send</button>
								</div>
						</div><!--end form group-->
					</form>
				</div>
			</div>
		</div>
		<div class="container text-center">
			<div class="panel panel-default">
				<div class="panel-body text-center">
					<div class="row">
						<div class="col-lg-6">
							<div class="panel panel-default">
								<div class="panel-heading text-center">
									<h5><%=userDetails.get("name")%>'s  Messages</h5>
								</div>
								<div class="panel-body text-left">
								<% 
								java.util.ArrayList<java.util.HashMap> allMessage=db.getMessages(email,temail);
								java.util.Iterator i=allMessage.iterator();
					            while(i.hasNext()){
					                HashMap message=(HashMap)i.next();
								%>
									<p><%=message.get("message") %></p>
									<div class="row">
										<font size="1">
										<div class="form-group">
											<div class="col-lg-2">
											<% 
											String fn=(String)message.get("filename"); 
											if( !fn.equals("")){
											%>
												<label for="ufile" class="control-label">File: <a href='DownloadFile?fname=<%=fn %>&pid=<%=message.get("pid")%>'> <%=fn %> </a></label>
											<%} %>
											</div>
											<div class="col-lg-2">
												<label for="date-time" class="control-label">Date:<%=message.get("datetime") %></label>
											</div>
										</div>
										</font>
									</div>
									<hr>
									<%} %>
								</div>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="panel panel-default">
								<div class="panel-heading text-center">
									<h5><%=tUserDetails.get("name")%>'s Messages</h5>
								</div>
								<div class="panel-body text-left">
									<% 
								 allMessage=db.getMessages(temail,email);
								 i=allMessage.iterator();
					            while(i.hasNext()){
					                HashMap message=(HashMap)i.next();
								%>
									<p><%=message.get("message") %></p>
									<div class="row">
										<font size="1">
										<div class="form-group">
											<div class="col-lg-2">
											<% 
											String fn=(String)message.get("filename"); 
											if( !fn.equals("")){
											%>
												<label for="ufile" class="control-label">File: <a href='DownloadFile?fname=<%=fn %>&pid=<%=message.get("pid")%>'> <%=fn %> </a></label>
											<%} %>
											</div>
											<div class="col-lg-2">
												<label for="date-time" class="control-label">Date:<%=message.get("datetime") %></label>
											</div>
										</div>
										</font>
									</div>
									<hr>
									<%} %>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	<hr>
	
	<!--footer-->
	
	<div class="navbar navbar-inverse navbar-fixed-bottom">
		<div class="container">
			<div class="navbar-text pull-left">
				<p>Design and Develop by INCAPP</p>
			</div>
		</div>
	</div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/jquery-2.2.2.min.js"></script>
    <script src="js/validator.js"></script>
  </body>
</html>
<%
    }
}else{
	session.setAttribute("msg", "Plz login First!");
    response.sendRedirect("home.jsp");
}
%>