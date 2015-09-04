<%@taglib uri="futurepagesApp" prefix="fpg"%>

<div align="center">
    <h2>Listagem de Turmas</h2>
    <fpg:hasSuccess>
		<div style="text-align: center; width: 400px; color:green; border-color: green; background-color: greenyellow">
			<fpg:success />
		</div>
        <br />
        <br />
    </fpg:hasSuccess>
        <a href="<fpg:modulePath module="escola" />/Turma.fpg?type=create">Nova Turma</a>
    <br />
    <br />
    <fpg:list value="turmas">
        <fpg:isEmpty>
            Nenhuma turma cammmadastrada.
        </fpg:isEmpty>
        <fpg:isEmpty negate="true">
            <table border="1">
                <tr>
                    <td><strong>ID</strong></td>
                    <td><strong>CÓDIGO</strong></td>
                    <td><strong>NOME</strong></td>
                    <td><strong>TIPO</strong></td>
                </tr>
                <fpg:loop var="turma">
                    <tr>
                        <td>${turma.id}</td>
                        <td>${turma.codigo}</td>
                        <td>${turma.nome}</td>
                        <td>${turma.tipo.nome}</td>
                    </tr>
                </fpg:loop>
            </table>
        </fpg:isEmpty>
    </fpg:list>
    <br />
    <br />
</div>
