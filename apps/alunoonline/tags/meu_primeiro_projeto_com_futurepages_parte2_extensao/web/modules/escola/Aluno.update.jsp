<%@taglib uri="futurepagesApp" prefix="fpg" %>

<div align="center">
    <h2>Editar Aluno</h2>
    <br/>
    <br/>
    <fpg:hasError>
        <!-- imprime estre pedaço somente se houver erro na página -->
        <div style="color: red; border:solid 1px red">
            <fpg:error/>
        </div>
    </fpg:hasError>
    <br />
    <br />
    <a href="<fpg:contextPath />">Voltar ao inínio</a>
    <br />
    <br />
    <form method="post" action="<fpg:modulePath module="escola" />/Aluno.update.fpg">
        <fpg:bean value="aluno">
            <input name="id" value="${aluno.id}" type="hidden" />
            Nome: <input id="nomeCompleto" name="nomeCompleto" value="${aluno.nomeCompleto}" />
            <br />
            <br />
            Matricula: <input id="matricula" name="matricula" value="${aluno.matricula}" />
        </fpg:bean>
        <br />
        <br />
        <fpg:Select list="turmas" name="turma" selected="${turma}" showAttr="nome" />
        <br />
        <br />
        <input type="submit" value="Atualizar" />

    </form>
</div>
