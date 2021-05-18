<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="LabaPacks.UserList" %>
<%@ page import="LabaPacks.UserActiveList" %>
<%@page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%>
<%-- Импортировать JSTL-библиотеку --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Импортировать собственную библиотеку тегов --%>
<%@taglib prefix="ad" uri="http://lab9.by/tags"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%-- Если пользователь не аутентифицирован, то просмотр страницы невозможен --%>
<c:if test="${sessionScope.authUser==null}">
	<c:redirect url="/index.jsp" />
</c:if>
<html>
<head>
<title>Личный кабинет</title>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="Expires" content="Mon, 11 May 1998 0:00:00 GMT">
<meta http-equiv="Last-Modified" content="Fri, Jan 25 2099 23:59:59 GMT">
</head>
<body>
<div
		style="background-color: #ccc; border: 1px solid black; float: right; margin: 10px; margin-top: 20px; padding: 5px 0px; text-align: center; width: 150px;">
	<a href="<c:url value="/index.jsp" />">На главную</a>
</div>
	<%-- Подключить заголовок страницы --%>
	<jsp:include page="/static/header.jsp"></jsp:include>

	<div class="w3-container w3-light-blue" style="float: right;">

		<h2>Active Users</h2>

	<%
		UserActiveList userActiveList = new UserActiveList();
		List<String> names = userActiveList.getUserActive();



		if (names != null && !names.isEmpty()) {

			for (String s : names) {
				out.println("<li class=w3-hover-sand>" + s + "</li>");
			}
			out.println("</ul>");

		}
	%>


	</div>
	<%-- Вставить кнопку создания нового объявления --%>
	<my:newButton />
	<h1>Личный кабинет</h1>
	<%-- Вставить разметку 1-колоночной страницы --%>
	<my:layout1Column>
		<%-- Извлечь список объявлений пользователя --%>
		<ad:getAds range="my" var="adListing" sort="${sessionScope.sort} " dir= "${sessionScope.dir}" />
		
		<%-- Показать объявления с возможностью редактирования --%>
		<my:adListing adListing="${adListing}" editMode="true" />
		
	</my:layout1Column>
	<%-- Вставить нижний заголовок страницы --%>
	<%@ include file="/static/footer.jsp"%>
</body>
</html>