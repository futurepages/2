package org.futurepages.core.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.futurepages.core.session.SessionEventListener;
import org.futurepages.core.session.SessionListenerManager;


public class SessionListener implements HttpSessionListener{

	public void sessionCreated(HttpSessionEvent event) {
		for(SessionEventListener sl : SessionListenerManager.getInstance().getListeners()){
			sl.onCreate(event.getSession());
		}
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		for(SessionEventListener sl : SessionListenerManager.getInstance().getListeners()){
			sl.onDestroy(event.getSession());
		}
	}
}