<%@taglib uri="futurepagesApp" prefix="fpg"%>

<div style="text-align:center">
    <h1>${mensagemInicial}</h1>
	<h2>
	    <fpg:valueFormatter object="${momentoAtual}" formatter="fullDateLiteral"/>
	</h2>
    <br/>
    <br/>
    <a href="<fpg:modulePath module="escola" />/Turma?type=create" >Nova Turma</a>
    <br/>
    <br/>
    <a href="<fpg:modulePath module="escola" />/Turma.fpg?type=explore" >Listar Turmas</a>
    <br/>
    <br/>
    <a href="<fpg:modulePath module="escola" />/Aluno.fpg?type=create">Novo Aluno</a>
    <br/>
    <br/>
    <a href="<fpg:modulePath module="escola" />/Aluno.fpg?type=explore">Listar Alunos</a>
    <br/>
    <br/>
    <br/>
    <br/>
</div>