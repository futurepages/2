<%@tag pageEncoding="UTF-8" %>
<%@taglib uri="futurepagesApp" prefix="fpg"%>

<%@attribute name="aluno" type="modules.escola.beans.Aluno"  required="true"%>
<%@attribute name="green" type="java.lang.Boolean"  required="true"%>

<tr ${green? 'style="color:green"':'style="color:red"'}>
	<td>${aluno.id}</td>
	<td style="text-align: center"><img src="${params.UPLOADS_URL_PATH}/alunos/${aluno.id}.jpg" style="width: 24px;"/></td>
	<td>${aluno.nomeCompleto}</td>
	<td>${aluno.matricula}</td>
	<td>${aluno.turma.nome}</td>
	<td>
		<div align="center">
			<a href="<fpg:modulePath module="escola"/>/Aluno.fpg?type=update&id=${aluno.id}">editar</a>
		</div>
	</td>
	<td>
		<div align="center">
			<a href="javascript:confirmaExclusao(${aluno.id}, '${aluno.nomeCompleto}', '${aluno.matricula}');" >apagar</a>
		</div>
	</td>
</tr>