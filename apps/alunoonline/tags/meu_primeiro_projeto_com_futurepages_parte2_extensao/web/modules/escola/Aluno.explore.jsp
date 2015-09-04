<%@taglib uri="futurepagesApp" prefix="fpg" %>

<script type="text/javascript">
    function confirmaExclusao(id, nomeCompleto, matricula) {
        if(confirm("Deseja realmente apagar o aluno " + nomeCompleto + "\n(matricula: " + matricula + ")")) {
            document.location = '<fpg:modulePath module="escola" />/Aluno.delete.fpg?id=' + id;
        }
    }
</script>

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
                    <td><strong>Editar</strong></td>
					<td><strong>Apagar</strong></td>
				</tr>
				<fpg:loop var="aluno">
					<tr>
						<td>${aluno.id}</td>
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
				</fpg:loop>
			</table>
		</fpg:isEmpty>
	</fpg:list>
	<br/>
	<br/>
</div>
