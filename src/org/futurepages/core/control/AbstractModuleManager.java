package org.futurepages.core.control;

import org.futurepages.core.config.Params;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.consequence.Forward;
import org.futurepages.core.consequence.Redirect;
import org.futurepages.util.ModuleUtil;
import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.DynAction;
import org.futurepages.core.ajax.json.JSONGenericRenderer;
import org.futurepages.core.consequence.Chain;
import org.futurepages.util.Is;

public abstract class AbstractModuleManager extends AbstractApplicationManager {

	protected String webPath;
    protected String moduleId;
    private boolean withPrettyURL = false;
    
    public AbstractModuleManager() {
        this.moduleId = AbstractModuleManager.moduleId(this.getClass());

        withPrettyURL = Params.get("PRETTY_URL").equals("true");
        
        webPathIt();
    }

    protected void webPathIt() {
        if(withPrettyURL){
            this.webPath =  moduleId + "/";
        }
        else{
            this.webPath = Params.MODULES_PATH + "/" + moduleId + "/";
        }
    }

	private String modulePath(String moduleId){
        if(withPrettyURL){
            return moduleId + "/";
        }
        else{
            return Params.MODULES_PATH + "/" + moduleId + "/";
        }
	}

    @Override
    public ActionConfig action(String act, Class<? extends Object> actionClass) {
        return super.action(webPath + act, actionClass);
    }

	public ActionConfig globalAction(String act, Class<? extends Object> actionClass) {
        return super.action(act, actionClass);
    }


    @Override
    public ActionConfig action(Class<? extends Object> actionClass) {
        return this.action(actionClass.getSimpleName(),actionClass);
    }
    
	public ActionConfig ajaxAction(String act, Class<? extends AjaxAction> actionClass) {
        return super.action(webPath + act, actionClass)
				.ajaxSuccess(new JSONGenericRenderer())
				.ajaxError(new JSONGenericRenderer())
		;
    }

	public ActionConfig ajaxAction(Class<? extends AjaxAction> actionClass) {
        return super.action(webPath +
						actionClass.getSimpleName(),actionClass)
							.ajaxSuccess(new JSONGenericRenderer())
							.ajaxError(new JSONGenericRenderer())
		;
    }

	/**
	 *
	 * @param actionClass that implements DynAction
	 * @return actionConfig para DynAction => fwIn("dyn/Action.jsp") on SUCCESS and ERROR
	 */
	public ActionConfig dynAction(Class<? extends DynAction> actionClass) {
        return action(actionClass)
				.on(SUCCESS,fwIn("dyn/"+actionClass.getSimpleName().substring(3)+".jsp"))
				.on(ERROR,fwIn("dyn/"+actionClass.getSimpleName().substring(3)+".jsp"))
		;
    }
	
    protected Consequence fwIn(String page) {
        return (new Forward(prettyCorrect(page) + webPath + page));
    }

    protected Consequence rdIn(String page) {
        return (new Redirect(prettyCorrect(page) + webPath + page));
    }

    protected Consequence rdIn(String page, boolean redirectParams) {
        return (new Redirect(prettyCorrect(page) + webPath + page, redirectParams));
    }

    protected Consequence fwd(String moduleId, String page){
        String path = Params.MODULES_PATH+"/";
        if(withPrettyURL){
            if(!page.contains(".")){ //.page , .jsp
                path = "";
            }
        }
        return (new Forward(path +  moduleId + "/"+page));
    }

    protected Consequence redir(String moduleId, String page){
        return (new Redirect(prettyCorrect(page) + moduleId + "/"+page));
    }


    protected Consequence chain(String moduleId, String actionName, String innerAction) {
        return addChain(modulePath(moduleId), actionName, innerAction);
    }

	protected Consequence chain(String moduleId, String actionName){
		return chain(moduleId, actionName, null);
	}

    protected Consequence chainRoot(String actionName, String innerAction) {
        return addChain("/", actionName, innerAction);
    }

    protected Consequence chainRoot(String actionName) {
        return chainRoot(actionName, null);
    }


    protected Consequence chainIn(String actionName, String innerAction) {
        return addChain(webPath, actionName, innerAction);
    }

	protected Consequence chainIn(String actionName) {
        return chainIn(actionName, null);
    }

	private Consequence addChain(String path, String actionName, String innerAction){
		String actionPath = path + actionName;
		innerAction = (!Is.empty(innerAction) ? innerAction : null);
		return addChain(new Chain(actionPath, innerAction));
	}
	
    public static String moduleId(Class klass){
           return ModuleUtil.moduleId(klass);
    }

    private String prettyCorrect(String page) {
        if(withPrettyURL){
            if(page.contains(".")){ //.page , .jsp
                return Params.MODULES_PATH+"/";
            }
        }
        return "";
    }
}