package org.futurepages.util;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.futurepages.core.control.Controller;
import org.futurepages.core.exception.DefaultExceptionLogger;

/**
 *
 * @author Thiago Rabelo
 */
public class BrowserDetection {
	
	private static final Pattern browserDetect;
	private static final Pattern engineDetect;

	static {
		browserDetect = Pattern.compile("(Chrome|CriOS)|Chromium|Firefox|Opera|Safari|rekonq|Epiphany|MSIE");
		engineDetect = Pattern.compile("AppleWebKit|Gecko|Presto|Trident");
	}
	
	private String userAgent;
	
	private String browser = null;
	//private String version;
	private String engine  = null;
	private Boolean mobile  = null;
	
	public BrowserDetection(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Só vale para quem está usando Chain com Actions do Futurepages ativamente
	 */
	public static BrowserDetection getInstance(HttpServletRequest req) {
			BrowserDetection bd = null;
			bd = (BrowserDetection) req.getSession().getAttribute("_browserDetectionInstance");
			if(bd==null){
				bd = new BrowserDetection(req.getHeader("user-agent"));
				req.getSession().setAttribute("_browserDetectionInstance", bd);
			}
			return bd;
	}

	/**
	 * Só vale para quem está usando Chain com Actions do Futurepages ativamente
	 */
	public static BrowserDetection getInstance(){
		Controller controller = Controller.getInstance();
		if(controller !=null && controller.getChain()!=null){
			return getInstance(controller.getChain().getAction().getRequest());
		}else{
			DefaultExceptionLogger.getInstance().execute(new IllegalStateException("Browser couldn't be detected using getInstance() without a chain. Use getIntance(request)!"));
			return new BrowserDetection(""); //just to not break the page
		}
	}
	
	protected void detectBrowser() {
		Matcher bd = browserDetect.matcher(userAgent);
		HashSet<String> bdMatches = new HashSet<String>();

		while (bd.find()) {
			bdMatches.add(bd.group());
		}

		if (bdMatches.size() > 0) {
			if (bdMatches.contains("Firefox")) {
				browser = "firefox";
			} else if (bdMatches.contains("Opera")) {
				browser = "Opera";
			} else if (bdMatches.contains("MSIE")) {
				browser = "ie";
			} else if (bdMatches.contains("Safari")) {
				if ((bdMatches.contains("Chrome") || bdMatches.contains("CriOS")) && !bdMatches.contains("Epiphany")) {
					browser = "chrome";
				} else if (bdMatches.contains("Chromium") && !bdMatches.contains("Epiphany")) {
					browser = "chromium";
				} else if (bdMatches.contains("rekonq")) {
					browser = "rekonq";
				} else if (bdMatches.contains("Epiphany")) {
						browser = "epiphany";
				} else {
					browser = "safari";					
				}
			} else {
				browser = "unknow_browser";
			}
		} else {
			browser = "unknow_browser";
		}
	}

	protected void detectEngine() {
		Matcher ed = engineDetect.matcher(userAgent);
		HashSet<String> edMatches = new HashSet<String>();
		
		while (ed.find()) {
			edMatches.add(ed.group());
		}
	
		if (edMatches.size() > 0) {
			if (edMatches.contains("AppleWebKit")) {
				engine = "webkit";
			} else if (edMatches.contains("Gecko") && !edMatches.contains("AppleWebKit")) {
				engine = "gecko";
			} else if (edMatches.contains("Presto")) {
				engine = "presto";
			} else if (edMatches.contains("Trident")) {
				engine = "trident";
			} else {
				engine = "unknow_engine";
			}
		} else {
			engine = "unknow_engine";
		}
	}

	protected void detectMobile() {
		mobile = (userAgent.contains("Mobile") || userAgent.contains("Mobi") ||
		          userAgent.contains("Mob") || userAgent.contains("mobile") ||
		          userAgent.contains("mobi") || userAgent.contains("mob"));
	}
	
	public String getBrowser() {
		if (browser == null) {
			detectBrowser();
		}
		
		return browser;
	}
	
	public String getEngine() {
		if (engine == null) {
			detectEngine();
		}
		
		return engine;
	}
	
	public boolean getMobile() {
		if (mobile == null) {
			detectMobile();
		}
		
		return mobile.booleanValue();
	}
}
