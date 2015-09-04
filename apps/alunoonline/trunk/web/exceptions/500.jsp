<%@page import="org.futurepages.exceptions.*,org.futurepages.util.html.HtmlMapChars,org.futurepages.core.config.Params" %><%
FuturepagesServletException fpgEx = (exception instanceof FuturepagesServletException)?
									 (FuturepagesServletException) exception :null;
if(fpgEx==null){
	if(exception instanceof org.apache.jasper.JasperException){
		exception = exception.getCause();
	}
}
if(exception==null){
	exception = (request.getParameter("id")!=null? new Exception(request.getParameter("id")) : null );
}

 if(fpgEx==null || (!((FuturepagesServletException)fpgEx).isAsync())) { %>

<%@include file="template/layout_BEGIN.jsp" %>

<div id="head">
	<h1>Falha Interna <%if(exception!=null) { %>(500)<%}%></h1>
</div>
<div id="body">
	<p>O servidor comportou-se de maneira inesperada mediante sua requisição.</p>

	<h2>Possíveis causas:</h2>

	<ul>
		<li>O recurso solicitado não existe ou não está mais disponível;</li>
		<li>A requisição provocou uma operação ilegal;</li>
		<li>Seu navegador pode estar em modo-offline;</li>
		<li>Ausência de conectividade com a Internet;</li>
		<li>Servidor encontra-se sobrecarregado de requisições.</li>
	</ul>


	<br/>
	<%if(exception!=null){%>
	<input type="hidden" value="<%=exception.getMessage()%>" id="failNumber"/>
	<h2 style="color:red">Falha Interna <%=exception.getMessage()%></h2>

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
		<%}%>
		 <%--<%=request.getHeader("referer")%> pode ser útil um dia nesta página.--%>
</div>

<%@include file="template/layout_END.jsp" %>

<%}else{%><%=exception.getMessage()%><%}%>
