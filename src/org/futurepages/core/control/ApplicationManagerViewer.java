package org.futurepages.core.control;

import org.futurepages.core.filter.Filter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

/**
 * Show application manager stats
 * @author Fernando Boaglio
 *
 */
public class ApplicationManagerViewer {

	public static String STATS_PAGE_NAME= "stats";

	private String STATS_VERSION= "1.0";

	private AbstractApplicationManager applicationManager = AbstractApplicationManager.getInstance();

	public void buildApplicationManagerStats(HttpServletResponse res) throws IOException {

		int count;
        res.setContentType("text/html");

        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<h2>ApplicationManager Statistics "+STATS_VERSION+"</h2>");
        out.println("<hr/>");
        out.println("<hr/>");
        out.println(" <b>Filters</b>:"+ applicationManager.getAllFilters().size());
        out.println("<br/>");
        out.println(" <b>Global Filters</b>:"+ applicationManager.getGlobalFilters().size());
        out.println("<br/>");
        out.println(" <b>Actions</b>:"+ applicationManager.getActions().size());
        out.println("<hr/>");

        out.println("<b>Actions</b> :<br/>");
        Map<String, ActionConfig> actions = applicationManager.getActions();
        count=1;
        for(String action:  actions.keySet()) {
         out.println("action ["+(count++)+"] = "+action  + "<br/>");
        }
        out.println("<hr/>");

        out.println("<b>All filters</b>:<br/>");
        Set<Filter> allFilters = applicationManager.getAllFilters();
        count=1;
        for(Filter filter :  allFilters) {
         out.println("filter ["+(count++)+"] = "+filter + "<br/>");
        }

        out.println("<hr/>");
        out.println("<b>All global filters</b>:<br/>");
        List<Filter> allGlobalFilters = applicationManager.getGlobalFilters();
        count=1;
        for(Filter filter :  allGlobalFilters) {
         out.println("global filter ["+(count++)+"] = "+filter + "<br/>");
        }

        out.println("<hr/>");
        out.println("</html>");
	}


}