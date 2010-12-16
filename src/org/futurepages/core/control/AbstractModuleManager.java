package org.futurepages.core.control;

import org.futurepages.core.config.Params;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.consequences.Forward;
import org.futurepages.consequences.Redirect;
import org.futurepages.util.ModuleUtil;
import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.DynAction;
import org.futurepages.json.JSONGenericRenderer;
import org.futurepages.consequences.Chain;
import org.futurepages.util.StringUtils;

public abstract class AbstractModuleManager extends AbstractApplicationManager {

	protected String webPath;
    protected String moduleId;

    private boolean withPrettyURL = false;
    private char innerActionSeparator;
    
    public AbstractModuleManager() {
        this.moduleId = AbstractModuleManager.moduleId(this.getClass());

        withPrettyURL = Controller.getInstance().isWithPrettyURL();
		innerActionSeparator = Controller.getInstance().getInnerActionSeparator();
		
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

	@Override
	public ActionConfig globalAction(String act, Class<? extends Object> actionClass) {
        return super.globalAction(act, actionClass);
    }


    @Override
    public ActionConfig action(Class<? extends Object> actionClass) {
        return this.action(actionClass.getSimpleName(),actionClass);
    }
    
	public ActionConfig ajaxAction(String act, Class<? extends AjaxAction> actionClass) {
        return super.action(webPath + act, actionClass)
				.on(SUCCESS, ajax(new JSONGenericRenderer()))
				.on(ERROR, ajax(new JSONGenericRenderer()))
		;
    }

	public ActionConfig ajaxAction(Class<? extends AjaxAction> actionClass) {
        return super.action(webPath + actionClass.getSimpleName() , actionClass)
						.on(SUCCESS, ajax(new JSONGenericRenderer()))
						.on(ERROR, ajax(new JSONGenericRenderer()))
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

    protected Consequence chain(String actionName) {
        return addChain(actionName);
    }

	protected Consequence chain(String moduleId, String actionName){
		return addChain(modulePath(moduleId), actionName);
	}

    protected Consequence chainIn(String actionName) {
        return addChain(modulePath(moduleId), actionName);
    }

	private Consequence addChain(String... actionPath){
		return addChain(new Chain(StringUtils.concat(actionPath)));
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

	public char getInnerActionSeparator() {
		return innerActionSeparator;
	}

	public void setInnerActionSeparator(char innerActionSeparator) {
		this.innerActionSeparator = innerActionSeparator;
	}
}