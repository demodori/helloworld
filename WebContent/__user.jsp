<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*"%>
<html>
<head><title>HelloWorld</title></head>
<body>

<form action="DBReadWrite" method="post">
	ＩＤ:<input type="text" name="id" value="" /><br />
	ＰＷ:<input type="text" name="password" value="" /><br />
	概要:<input type="text" name="description" value="" /><br />
	<br />
	
	<input type="submit" name="btReg" value="　登　録　"><br />
</form>
<br />

<% out.println("日本語POST確認：" + request.getParameter("description")); %><br />
<br />

●登録済一覧<br />
<table border="1">
	<tr>
		<th>ID</th>
		<th>PASSWORD</th>
		<th>概要</th>
	</tr>
<%
List userList = (List)request.getAttribute("userList");
for (int i = 0, n = userList.size(); i < n; i++) {
	Map user = (Map)userList.get(i);
	out.println("<tr>");
	out.println("<td>" + user.get("id") + "</td>");
	out.println("<td>" + user.get("password") + "</td>");
	out.println("<td>" + user.get("description") + "</td>");
	out.println("<tr />");
}
%>
</table>
<br />

<a href="/helloworld/index.html">戻る</a><br />
</body>
</html>