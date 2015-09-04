<%@taglib uri="futurepagesApp" prefix="fpg" %>

<div align="center">
	<h2>Listagem de Alunos</h2>
	<fpg:hasSuccess>
		<fpg:success/>
		<br/>
		<br/>
	</fpg:hasSuccess>
	<a href="<fpg:modulePath module="escola" />/Aluno.fpg?type=create">Novo Aluno</a>
	<br/>
	<br/>
    <form name="selecionaPorTurma" method="post" action="<fpg:modulePath module="escola" />/Aluno.explore.fpg">
        <fpg:Select list="turmas" name="turma" selected="${turma}" 
                    defaultText="- Todas as Turmas -" onchange="selecionaPorTurma.submit()"
                    showAttr="nome" />
    </form>
	<br/>
	<br/>
	<fpg:list value="alunos">
		<fpg:isEmpty>
		Nenhum aluno cadastrado.
		</fpg:isEmpty>
		<fpg:isEmpty negate="true">
			<table border="1">
				<tr>
					<td><strong>ID</strong></td>
					<td><strong>NOME</strong></td>
					<td><strong>MATRÍCULA</strong></td>
                    <td><strong>Turma</strong></td>
				</tr>
				<fpg:loop var="aluno">
					<tr>
						<td>${aluno.id}</td>
						<td>${aluno.nomeCompleto}</td>
						<td>${aluno.matricula}</td>
                        <td>${aluno.turma.nome}</td>
					</tr>
				</fpg:loop>
			</table>
		</fpg:isEmpty>
	</fpg:list>
	<br/>
	<br/>
</div>
