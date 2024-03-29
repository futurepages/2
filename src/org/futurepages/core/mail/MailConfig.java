package org.futurepages.core.mail;

import org.futurepages.core.config.Params;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.emails.Email;

/**
 * Configuração dos valores padrão dos parâmetros de Configuração do Email.
 * 
 * @author leandro
 */
public class MailConfig {

    public static void initialize() throws Exception {
        try{
            String  EMAIL_HOST_NAME =       Params.get("EMAIL_HOST_NAME");
            String  EMAIL_DEFAULT_PORT =    Params.get("EMAIL_DEFAULT_PORT");
            boolean EMAIL_SSL_CONNECTION =	Params.get("EMAIL_SSL_CONNECTION").equals("true");
            boolean MAIL_SMTP_STARTTLS =	Params.get("MAIL_SMTP_STARTTLS_ENABLE").equals("true");
            String MAIL_SMTP_SSL_PROTOCOLS =	Params.get("MAIL_SMTP_SSL_PROTOCOLS");
            String  EMAIL_USER_NAME =		Params.get("EMAIL_USER_NAME");
            String  EMAIL_USER_PASSWORD =	Params.get("EMAIL_USER_PASSWORD");
            String  EMAIL_FROM =			Params.get("EMAIL_FROM");
            String  EMAIL_FROM_NAME =		Params.get("EMAIL_FROM_NAME");
            
			String  EMAIL_CHARSET = (String) ReflectionUtil.staticField(Email.class, Params.get("EMAIL_CHARSET"));

            Email.setDefaultHostName(EMAIL_HOST_NAME);
            Email.setDefaultPort(EMAIL_DEFAULT_PORT);
            Email.setSSLConnection(EMAIL_SSL_CONNECTION);
            Email.setMailSmtpStarttlsEnable(MAIL_SMTP_STARTTLS);
            Email.setMailSmtpSslProtocols(MAIL_SMTP_SSL_PROTOCOLS);
            Email.setDefaultAuthentication(EMAIL_USER_NAME, EMAIL_USER_PASSWORD);
            Email.setDefaultFrom(EMAIL_FROM, EMAIL_FROM_NAME);
            Email.setDefaultCharset(EMAIL_CHARSET);
        }
        catch(Exception ex){
            throw new Exception("Erro ao configurar serviço de email. ("+ex.getMessage()+")");
        }
    }
}