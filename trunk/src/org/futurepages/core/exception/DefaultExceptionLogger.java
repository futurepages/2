package org.futurepages.core.exception;

import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.action.AsynchronousManager;
import org.futurepages.core.action.Manipulable;
import org.futurepages.core.control.Controller;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.exceptions.FuturepagesServletException;
import org.futurepages.exceptions.ServletErrorException;
import org.futurepages.json.JSONGenericRenderer;
import org.futurepages.util.DateUtil;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.StringUtils;
import org.futurepages.util.The;


public class DefaultExceptionLogger implements ExceptionLogger, Manipulable{

    private static final DefaultExceptionLogger INSTANCE = new DefaultExceptionLogger();

    public static DefaultExceptionLogger getInstance() {
        return INSTANCE;
    }

	private DefaultExceptionLogger() {}

	public void execute(Throwable throwable) {
		execute(throwable, ExceptionLogType.SILENT_EXCEPTION.name(),null);
	}

	public String execute(Throwable throwable, String errorType, HttpServletRequest req) {
		String numeroProtocolo = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		InvocationChain chain = Controller.getInstance().getChain();
		String exceptionId =  StringUtils.concat("[",errorType.toUpperCase(),"] ",numeroProtocolo);
        log(exceptionId , "  ("  , DateUtil.viewDateTime(new Date()) , ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		if(chain!=null){
			if(req == null){
				req =  chain.getAction().getRequest();
			}
		}
						 
		throwable.printStackTrace();

		System.out.println();
		
		if(req!=null){
			log(">[url    ]  ", req.getRequestURL().toString(), (req.getQueryString()!=null?"?"+req.getQueryString():""));
			log(">[referer]  ", req.getHeader("referer"));
			log(">[from   ]  ", AbstractAction.getIpsFromRequest(req));
			log(">[browser]  ", req.getHeader("user-agent"));
			log(">[proxy  ]  ", req.getHeader("Proxy-Authorization"));
			if(AbstractAction.isLogged(req)){
			log(">[user   ]  ", AbstractAction.loggedUser(req).getLogin());
			}
			log(">[method ]  ", req.getMethod());

			System.out.print(">[request]  ");
			for(Object key : req.getParameterMap().keySet()){
				System.out.print(The.concat(key.toString(), ": ",
											The.implodedArray(req.getParameterValues(key.toString()),",","'"),
											";"
											)
								 );
			}
			System.out.println();

			log(">[session]  id: ", req.getSession().getId() , "; ",
				"creation: ",DateUtil.viewDateTime(new Date(req.getSession().getCreationTime())), "; ",
				"last access: ",DateUtil.viewDateTime(new Date(req.getSession().getLastAccessedTime())), "; ",
				"max inative interval: ",String.valueOf(req.getSession().getMaxInactiveInterval()/60), " minutes;"
			); //TODO - informacoes de tempo da sessao
			System.out.print(">[session]  ");
			Enumeration ralist = req.getSession().getAttributeNames();
			while(ralist.hasMoreElements()){
				String name=(String)ralist.nextElement();
				String toStringValue = req.getSession().getAttribute(name).toString();

				System.out.print(The.concat(name, ": '", toStringValue, "';" ));
			}
			System.out.println();

			if (req.getCookies() != null) {
				System.out.print(">[cookies]  (" + req.getCookies().length + ") ");
				for (Cookie cookie : req.getCookies()) {
					System.out.print(The.concat(cookie.getName(), ": '", EncodingUtil.decodeUrl(cookie.getValue()), "'; "));
				}
				System.out.println();
			}
		}

		log("\n",exceptionId," <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		return numeroProtocolo;
	}

	public String execute(Throwable throwable, InvocationChain chain, HttpServletRequest req, boolean status500) throws ServletException {

		String actionType = null;
		if(chain!=null){
			if(AsynchronousManager.isAsynchronousAction(chain)){
				actionType = DYN_EXCEPTION;
			}else{
				actionType = EXCEPTION;
			}
		}


		if(status500){
			Throwable ex;
			if(throwable.getCause() != null){
				ex = throwable.getCause();
			}else{
				ex = throwable;
			}
			if(ex instanceof ServletErrorException){
				throw (ServletErrorException) ex;
			}
			String protocolNumber = execute(ex,ExceptionLogType.SERVLET_500.name(),req);
			throw new FuturepagesServletException(protocolNumber, actionType, ex);
		}else if(chain!=null){
			String protocolNumber = execute(throwable, actionType, chain.getAction().getRequest());
			chain.getAction().getOutput().setValue(EXCEPTION, new Exception(protocolNumber,throwable));
		}		
		return actionType;
	}

	private void log(String... strs){
		System.out.println(StringUtils.concat(strs));
	}
	
	private enum ExceptionLogType {
		EXCEPTION, SERVLET_500, DYN_EXCEPTION, SILENT_EXCEPTION;
	}
}

