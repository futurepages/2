<%@taglib prefix="fpg" uri="futurepagesApp" %>

<html>
    <head>
        <title></title>
        <style type="text/css">
            .error{
                color:red;
            }

            .message {
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="error message" style="background-color:white">

        <p>
            QUE FEIO SERVIDOR!
        </p>
        <p>
            N�o era hora de me deixar na m�o!
        </p>

        <p>${exceptionNumber}</p>

        <p>
            ${exceptionName}
        </p>

        <p>
            ${exceptionMessage}
        </p>
        </div>
    </body>
</html>