package org.futurepages.core;

import org.futurepages.core.context.Context;
import org.futurepages.formatters.LiteralAnniversaryFormatter;
import org.futurepages.formatters.AnniversaryAbbrFormatter;
import org.futurepages.formatters.AnniversaryFormatter;
import java.util.Locale;

import org.futurepages.core.config.Params;
import org.futurepages.core.persistence.HibernateFilter;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.filters.ExceptionFilter;
import org.futurepages.filters.HeadTitleFilter;
import org.futurepages.formatters.CPFCNPJFormatter;
import org.futurepages.formatters.DateFormatter;
import org.futurepages.formatters.DateTimeFormatter;
import org.futurepages.formatters.ElapsedTimeFormatter;
import org.futurepages.formatters.FloatFormatter;
import org.futurepages.formatters.HTMLFormatter;
import org.futurepages.formatters.JavascriptFormatter;
import org.futurepages.formatters.LiteralDateFormatter;
import org.futurepages.formatters.LiteralDateTimeFormatter;
import org.futurepages.formatters.LiteralDayOfWeekFormatter;
import org.futurepages.formatters.MoneyFormatter;
import org.futurepages.formatters.NoSpecialsFormatter;
import org.futurepages.formatters.SEOURLFormatter;
import org.futurepages.formatters.TextAreaFormatter;
import org.futurepages.formatters.UpperCaseFormatter;
import org.futurepages.core.control.AbstractApplicationManager;
import org.futurepages.consequences.NullConsequence;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.filters.FileUploadFilter;
import org.futurepages.filters.InjectionFilter;
import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.filters.AutoRedirectDomainFilter;
import org.futurepages.formatters.CollectionSizeFormatter;
import org.futurepages.formatters.MonthFormatter;
import org.futurepages.formatters.RemainingTimeFormatter;
import org.futurepages.formatters.UrlFormatter;
import org.futurepages.json.JSONGenericRenderer;
import org.futurepages.util.Is;

/**
 * ApplicationManager que gerencia a Ação Inicial e os filtros Globais
 * Carrega a Action Inicial da Aplicação
 */
public class InitManager extends AbstractApplicationManager{
    
    @Override
    public final void loadActions() {
            //Filtros Globais
			if(!Is.empty(Params.get("AUTO_REDIRECT_DOMAIN"))){
				filter(new AutoRedirectDomainFilter(Params.get("AUTO_REDIRECT_DOMAIN")));
			}

			if(HibernateManager.isRunning()){
                filter(new HibernateFilter());
            }else{
                filter(new ExceptionFilter());
			}
            
            if(Params.get("GLOBAL_HEAD_TITLE")!=null){
                filter(new HeadTitleFilter(Params.get("GLOBAL_HEAD_TITLE")));
            }

            filter(new FileUploadFilter());
            filter(new InjectionFilter());

            on(NULL, new NullConsequence());
			on(EXCEPTION, fwd(Params.get("EXCEPTION_FILE_PATH")));
			on(DYN_EXCEPTION, fwd(Params.get("DYN_EXCEPTION_FILE_PATH")));
			on(REDIR, redir());
			on(AJAX_REDIR, ajax(new JSONGenericRenderer()));
			on(AJAX_ERROR, ajax(new JSONGenericRenderer()));
			on(REDIR_APPEND_OUTPUT, redir(true));

            //Ação Inicial Padrão
            try {
                Class initActionClass = Class.forName(Params.get("INIT_ACTION"));
                action(Params.get("START_PAGE_NAME"), initActionClass).on(SUCCESS, fwd(Params.get("START_CONSEQUENCE")));
            } catch (ClassNotFoundException ex) {
                System.out.println("[::initManager::] A classe de Ação Inicial da Aplicação não foi encontrada.");
                DefaultExceptionLogger.getInstance().execute(ex);
            }
    }
    
    @Override
    public void loadLocales(){
         //Por enquanto o futurepages não contempla internacionalização.
         LocaleManager.add(new Locale("pt","BR"));
    }
    
    @Override
    public void loadFormatters() {
        FormatterManager.addFormatter("collectionSize" 	   , new CollectionSizeFormatter());
        FormatterManager.addFormatter("cpfCnpj"		 	   , new CPFCNPJFormatter());
        FormatterManager.addFormatter("date"         	   , new DateFormatter());
        FormatterManager.addFormatter("dateTime"     	   , new DateTimeFormatter());
        FormatterManager.addFormatter("elapsedTime"   	   , new ElapsedTimeFormatter());
        FormatterManager.addFormatter("remainingTime"	   , new RemainingTimeFormatter());
        FormatterManager.addFormatter("html"        	   , new HTMLFormatter());
        FormatterManager.addFormatter("javascript"         , new JavascriptFormatter());
        FormatterManager.addFormatter("literalDate" 	   , new LiteralDateFormatter());
        FormatterManager.addFormatter("literalDateTime"    , new LiteralDateTimeFormatter());
        FormatterManager.addFormatter("literalDayOfWeek"   , new LiteralDayOfWeekFormatter());
        FormatterManager.addFormatter("anniversary"        , new AnniversaryFormatter());
        FormatterManager.addFormatter("anniversaryAbbr"    , new AnniversaryAbbrFormatter());
        FormatterManager.addFormatter("literalAnniversary" , new LiteralAnniversaryFormatter());
        FormatterManager.addFormatter("money"              , new MoneyFormatter());
        FormatterManager.addFormatter("month"              , new MonthFormatter());
        FormatterManager.addFormatter("float"        	   , new FloatFormatter());
        FormatterManager.addFormatter("seoURL"	     	   , new SEOURLFormatter());
        FormatterManager.addFormatter("noSpecials"  	   , new NoSpecialsFormatter());
        FormatterManager.addFormatter("textarea"		   , new TextAreaFormatter());
        FormatterManager.addFormatter("uppercase"		   , new UpperCaseFormatter());
		FormatterManager.addFormatter("url"                , new UrlFormatter());
    }

	@Override
	public void init(Context application) {
		application.setAttribute("params", Params.getParamsMap());
	}
}