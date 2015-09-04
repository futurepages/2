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

    <form method="post" action="<fpg:modulePath module="escola" />/Aluno.create.fpg">
        <fpg:bean value="aluno">
            Nome: <input id="nomeCompleto" name="nomeCompleto" value="${aluno.nomeCompleto}" />
            <br/><br/>
            Matrícula: <input id="cpf" name="cpf" value="${aluno.cpf}" />
        </fpg:bean>
        <br/>
        <br/>
        <input type="submit" value="Enviar" />
    </form>
</div>