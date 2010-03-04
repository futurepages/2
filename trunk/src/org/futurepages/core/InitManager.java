package org.futurepages.core;

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
import org.futurepages.filters.FileUploadFilter;
import org.futurepages.filters.InjectionFilter;
import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.core.i18n.LocaleManager;

/**
 * ApplicationManager que gerencia a A��o Inicial e os filtros Globais
 * Carrega a Action Inicial da Aplica��o
 */
public class InitManager extends AbstractApplicationManager{
    
    @Override
    public final void loadActions() {
            //Filtros Globais
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

            //A��o Inicial Padr�o
            try {
                Class initActionClass = Class.forName(Params.get("INIT_ACTION"));
                action(Params.get("START_PAGE_NAME"), initActionClass).on(SUCCESS, fwd(Params.get("START_CONSEQUENCE")));
            } catch (ClassNotFoundException ex) {
                System.out.println("[::initManager::] A classe de A��o Inicial da Aplica��o n�o foi encontrada.");
                ex.printStackTrace();
            }
    }
    
    @Override
    public void loadLocales(){
         //Por enquanto o futurepages n�o contempla internacionaliza��o.
         LocaleManager.add(new Locale("pt","BR"));
    }
    
    @Override
    public void loadFormatters() {
        FormatterManager.addFormatter("cpfCnpj"		 	   , new CPFCNPJFormatter());
        FormatterManager.addFormatter("date"         	   , new DateFormatter());
        FormatterManager.addFormatter("dateTime"     	   , new DateTimeFormatter());
        FormatterManager.addFormatter("elapsedTime"   	   , new ElapsedTimeFormatter());
        FormatterManager.addFormatter("html"        	   , new HTMLFormatter());
        FormatterManager.addFormatter("javascript"         , new JavascriptFormatter());
        FormatterManager.addFormatter("literalDate" 	   , new LiteralDateFormatter());
        FormatterManager.addFormatter("literalDateTime"    , new LiteralDateTimeFormatter());
        FormatterManager.addFormatter("literalDayOfWeek"   , new LiteralDayOfWeekFormatter());
        FormatterManager.addFormatter("anniversary"        , new AnniversaryFormatter());
        FormatterManager.addFormatter("anniversaryAbbr"    , new AnniversaryAbbrFormatter());
        FormatterManager.addFormatter("literalAnniversary" , new LiteralAnniversaryFormatter());
        FormatterManager.addFormatter("money"              , new MoneyFormatter());
        FormatterManager.addFormatter("float"        	   , new FloatFormatter());
        FormatterManager.addFormatter("seoURL"	     	   , new SEOURLFormatter());
        FormatterManager.addFormatter("noSpecials"  	   , new NoSpecialsFormatter());
        FormatterManager.addFormatter("textarea"		   , new TextAreaFormatter());
        FormatterManager.addFormatter("uppercase"		   , new UpperCaseFormatter());
    }
}