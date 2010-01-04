package org.futurepages.test.mockiness;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import javax.servlet.http.HttpSession;

public class HttpSessionMockFactory {

	private static HttpSessionMockFactory instance;

	private HttpSessionMockFactory() {}

	public static HttpSessionMockFactory getInstance() {
		if (instance == null) {
			instance = new HttpSessionMockFactory();
		}
		return instance;
	}
	
	private HttpSession strictMock() {
		HttpSession mock = createStrictMock(HttpSession.class);
		return mock;
	}
	
	public HttpSession createGetAttibute(String name, Object object){
		HttpSession mock = strictMock();
		expect(mock.getAttribute(name)).andReturn(object);
		replay(mock) ;
		return mock;
	}
	
	public HttpSession createWithSetAtribute(String name, Object object){
		return createWithSetAtribute(1, name, object);
	}
	
	public HttpSession createWithSetAtribute(int num,String name, Object object){
		HttpSession mock = strictMock();
		for (int i = 0; i < num; i++) {
			mock.setAttribute(name, object);
		}
		replay(mock) ;
		return mock;
	}

}
