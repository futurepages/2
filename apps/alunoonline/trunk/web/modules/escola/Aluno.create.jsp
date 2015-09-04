<%@taglib uri="futurepagesApp" prefix="fpg"%>

<div style="text-align:center">
    <h1>Novo Aluno</h1>
    <br/>
    <br/>
    <fpg:hasError>
        <div style="color: red; border:solid 1px red">
            <fpg:error/>
        </div>
    </fpg:hasError>
        <br/>

    <form method="post" action="<fpg:modulePath module="escola" />/Aluno.create.fpg" enctype="multipart/form-data">
        Nome: <input id="nomeCompleto" name="nomeCompleto" value="${aluno.nomeCompleto}" />
        <br/><br/>
        Matrícula: <input id="matricula" name="matricula" value="${aluno.matricula}" />
        <br />
        <br />
        <fpg:Select list="turmas" name="turma" selected="${aluno.turma!=null?aluno.turma.id:0}" showAttr="nome" />
	    <br />
	    <br />
	    Arquivo de Foto 3x4: <input type="file" name="foto"/>
	    <br />
        <br />
        <input type="submit" value="Enviar" />
    </form>
</div>
