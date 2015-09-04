<%@page import="org.futurepages.util.html.HtmlMapChars,org.futurepages.core.config.Params" %>

<%@include file="template/layout_BEGIN.jsp" %>

<% exception = (Exception) pageContext.findAttribute("exception");

if(exception==null){
	exception = (request.getParameter("id")!=null? new Exception(request.getParameter("id")) : null );
}
%>


<div id="head">
	<h1>Falha Inesperada</h1>
</div>
<div id="body">

	<p>A sua requisição provocou uma operação inesperada na aplicação,
		fazendo com que seu resultado fosse suspenso para previnir maiores danos.
	</p>
	<br/>

	<h2 style="color:red">Falha Inesperada <%=exception.getMessage()%></h2>

		<% if(Params.devMode() && exception.getCause()!=null) { %>
		<p class="small">
		<small style="color:red">
		<%=exception.getCause().getClass().getName() %>
		<br/>
		<%=HtmlMapChars.htmlValue(exception.getCause().getMessage()) %>
		</small>
		</p>
		<%}%>
		<br/>
		<br/>
</div>

<%@include file="template/layout_END.jsp" %>