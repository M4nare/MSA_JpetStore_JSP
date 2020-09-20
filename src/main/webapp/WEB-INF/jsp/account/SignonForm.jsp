<%--

       Copyright 2010-2016 the original author or authors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.

--%>

	
<%@ include file="../common/IncludeTop.jsp"%>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
	function accountservice(){
		var formData = $("#loginForm").serialize();
		

		$.ajax({

			type:"POST",
			url:"http://localhost:8081/jpetstore/actions/Account.action?signon",
			data: formData,
			success : function(data){
				
				
				$.each(data,function(key,value) {
					alert("Success! " + value);
					window.location.href = "http://localhost:8079/jpetstore/actions/Account.action?login&id="+value;
				});
			},
			error : function(request,status,error){
      		  alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      		}
			
		});
		
		}
</script>



<div id="Catalog">
<form name="loginForm" id="loginForm" method="post">


	<p>Please enter your username and password.</p>
	<p>Username: <input type="text" name="username"></p>
	<p>Password: <input type="password" name="password"></p>
	<input type =button value="LOGIN"onclick='accountservice()'>

</form> Need a user name and password? <a href="http://localhost:8079/jpetstore/actions/Account.action?newAccountForm">
Register Now!
</a></div>

<%@ include file="../common/IncludeBottom.jsp"%>
