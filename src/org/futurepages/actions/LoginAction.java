package org.futurepages.actions;

import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.admin.AuthenticationFree;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.context.Context;

public abstract class LoginAction extends AbstractAction implements AuthenticationFree  {

	public static int TIMEOUT = 30; // in Minutes

	/**
	 * Coloca o usu�rio na sess�o, alterando o tempo da sess�o para o tempo
	 * padr�o definido em {@link TIMEOUT} (tempo em minutos)
	 * @param user a ficar logado
	 */
	protected void setUserInSession(DefaultUser user) {
		LoginAction.setUserInSession(this.getHttpSession(), user);
	}

	/**
	 * Coloca o usu�rio numa nova sess�o, alterando o tempo da sess�o para o tempo
	 * padr�o definido em {@link TIMEOUT} (tempo em minutos)
	 * @param user a ficar logado
	 */
	protected void setUserInNewSession(DefaultUser user) {
		LoginAction.setUserSession(this.getHttpSession(), user, this.getRequest());
	}

	/**
	 * Altera o tempo da sess�o corrente da action
	 * 
	 * @param timeInMinutes tempo em minutos
	 */
	protected void setSessionTimeout(int timeInMinutes) {
		LoginAction.setSessionTimeout(this.getHttpSession(), timeInMinutes);
	}

	// M�TODOS EST�TICOS ////////////////////////////////////////

	/**
	 * Coloca o usu�rio na sess�o, alterando o tempo da mesma com o valor padr�o definido
	 * em {@link TIMEOUT}  (tempo em minutos)
	 */
	public static void setUserInSession(HttpSession session, DefaultUser user) {
		setUserSession(session, user, null);
	}

	/**
	 * Coloca-se o usu�rio na sess�o passada como par�metro ou numa nova sess�o, dependendo-se
	 * se 'reqToReset' for nula, sen�o cria-se uma nova sess�o associada ao request.
	 *
	 * @param session sess�o que receber� o usu�rio (caso n�o v� receber uma nova sess�o)
	 * @param user usu�rio que ficar� na sess�o
	 * @param reqToReset requisi��o que receber� uma nova sess�o, caso seja necess�rio (diferente de null)
	 */
	private static void setUserSession(HttpSession session, DefaultUser user,HttpServletRequest reqToReset) {
		if(reqToReset!=null){
			session.invalidate();
			session = reqToReset.getSession(true);
		}
		session.setAttribute(USER_KEY, user);
		setSessionTimeout(session, TIMEOUT);
	}

	/**
	 * Altera o tempo da sess�o em minutos.
	 */
	public static void setSessionTimeout(HttpSession session, int timeInMinutes) {
		session.setMaxInactiveInterval(timeInMinutes * 60);
	}

	@Override
    public boolean bypassAuthentication(String innerAction) {
       return true;
    }

 	/* LOCALE */
    public void setUserLocale(Locale loc) {
        setUserLocale(loc, session);
    }

    public static void setUserLocale(Locale loc, Context session) {

        if (!session.hasAttribute(USER_KEY)) {

            throw new IllegalStateException(
                    "Tried to set user locale, but there is no user in the session!");
        }

        session.setAttribute(LOCALE_KEY, loc);
    }

    public static void setUserLocale(String loc, Context session) {

       StringTokenizer st = new StringTokenizer(loc, "_");

       if (st.countTokens() == 1) {

          setUserLocale(new Locale(st.nextToken()), session);

       } else if (st.countTokens() == 2) {

          setUserLocale(new Locale(st.nextToken(), st.nextToken()), session);

       } else if (st.countTokens() == 3) {

          setUserLocale(new Locale(st.nextToken(), st.nextToken(), st.nextToken()), session);

       } else {

          throw new IllegalArgumentException("Bad locale: " + loc);
       }
    }

    public void setUserLocale(String loc) {
       setUserLocale(loc, session);
    }
}