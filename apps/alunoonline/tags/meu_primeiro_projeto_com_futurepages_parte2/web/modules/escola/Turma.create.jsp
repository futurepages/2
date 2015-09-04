<%@taglib uri="futurepagesApp" prefix="fpg"%>
<div style="text-align:center" >
    <h1>Nova Turma</h1>
    <br />
    <br />
    <fpg:hasError>
        <div style="color: red; border: solid 1px red">
            <fpg:error />
        </div>
    </fpg:hasError>
    <br />

    <form method="post" action="<fpg:modulePath module="escola" />/Turma.create.fpg" >
        <fpg:bean value="turma">
            Nome: <input id="nome" name="nome" value="${turma.nome}" />
            <br />
            <br />
            Codigo: <input id="codigo" name="codigo" value="${turma.codigo}" />
        </fpg:bean>
            <br />
            <br />
        <input type="submit" value="Enviar">
    </form>

</div>
