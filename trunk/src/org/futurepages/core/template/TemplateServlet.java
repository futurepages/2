package org.futurepages.core.template;

import org.futurepages.exceptions.TemplateException;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe abstrata base para TemplateServlets. Possui uma implementacao padrao,
 * que e JspTemplateServlet, porem podem ser feitas outras, como
 * FreemarkerTemplateServlet ou VelocityTemplateServlet
 *
 * @author Davi Luan Carneiro
 */
public abstract class TemplateServlet extends HttpServlet {

    /**
     * Para acessar o initParam do web.xml
     */
    protected static final String TEMPLATE_MANAGER_ATTR = "TemplateManager";
    public static final String PAGE_ATTR = "Page";
    public static final String CURRENT_VIEW_ATTR = "_view";
    private static AbstractTemplateManager templateManager = null;

    /**
     * O TemplateManager sera carregado no startup do container
     */
    @Override
    public void init() throws ServletException {
        super.init();
        templateManager = createTemplateManager();
    }

    /**
     * Cria o TemplateManager. Se ja tiver sido criado, apenas retorna a
     * instancia.
     *
     * @return TemplateManager
     * @throws Exception
     *             Caso nao encontre o TemplateManager, ou ele seja de um tipo
     *             diferente.
     */
    private AbstractTemplateManager createTemplateManager() {
        if (templateManager != null) {
            return templateManager;
        }
        AbstractTemplateManager manager;
        try {
            String nameClassTemplateManager = getInitParameter(TEMPLATE_MANAGER_ATTR);
            if (nameClassTemplateManager == null || "".equals(nameClassTemplateManager)) {
                nameClassTemplateManager = TEMPLATE_MANAGER_ATTR;
            }
            Class classTemplateManager = Class.forName(nameClassTemplateManager);
            manager = (AbstractTemplateManager) classTemplateManager.newInstance();
            if (manager == null) {
                throw new TemplateException("TemplateManager not found");
            }
            manager.configurePages();
            return manager;
        } catch (Exception e) {

            //e.printStackTrace();

            throw new TemplateException(e);
        }
    }

    /**
     * Retorna o TemplateManager. Por ser estatico, torna o TemplateManager
     * disponivel a outras classes. Deve-se tomar cuidado, pois o
     * TemplateManager so sera preenchido na primeira requisicao ao
     * TemplateServlet. Antes disso, o seu valor e null.
     *
     * @return TemplateManager
     */
    public static AbstractTemplateManager getTemplateManager() {
        return templateManager;
    }

    /**
     * Permite que o TemplateManager seja setado. Nao e usado internamente,
     * porem para darmos maxima flexibilidade a quem for usar (incluindo outros
     * frameworks), incluimos este metodo.
     *
     * @param manager
     *            Instancia de TemplateManager
     */
    public static void setTemplateManager(AbstractTemplateManager manager) {
        templateManager = manager;
    }

    /**
     * Metodo utilitario que retorna o path da pagina, sem a extensao. Por
     * exemplo: WEB-INF/views/mypage
     *
     * @param request
     * @return Path da pagina
     */
    public static String extractPagePath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath.substring(0, servletPath.lastIndexOf("."));
    }

    /**
     * Processa a requisicao, obtendo o path a partir da request e
     * redirecionando para o metodo processTemplate
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            processTemplate(extractPagePath(request), createTemplateManager(), request, response, getServletContext());

        } catch (Exception e) {

            //e.printStackTrace();

            throw new TemplateException(e);
        }
    }

    /**
     * Processa o template, invocando o TemplateManager e redirecionando para o
     * local adequado. Este metodo invoca outros metodos abstratos, tornando-o
     * bem generico. Se fosse criado um FreemarkerTemplateServlet, por exemplo,
     * esta metodo provavelmente continuaria igual. Eh publico porque pode ser
     * usado na integracao com frameworks web mvc.
     *
     * @param path
     * @param request
     * @param response
     */
    public void processTemplate(String path, AbstractTemplateManager manager,
            HttpServletRequest request, HttpServletResponse response,
            ServletContext application) {
        Page page = manager.getPageForPath(path);
        if (page == null) {
            throw new TemplateException(path + " not found!");
        }

        //Tratamento necessario ao suporte de regexp
        String oldPath = page.getPath();
        page.setPath(path);

        try {
            putPageInResponse(page, request, response, application);
            executeListener(page, request, response, application);
            page.setPath(oldPath);
            if (!response.isCommitted()) {
                showPage(page, request, response, application);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("EXCEPTION-TRACE OMITIDA - "+e.getMessage());
            throw new TemplateException(e);
        }
    }

    /**
     * Responsavel por colocar o Page na camada de visao. A classe filha devera
     * implementar este metodo. O JspTemplateServlet, por exemplo, implementa-o
     * setando o Page na request. Se for algo chave-valor (provavelmente sera),
     * procure por como chave a constante PAGE_ATTR
     *
     * @param page
     * @param request
     * @param response
     */
    protected abstract void putPageInResponse(Page page,
            HttpServletRequest request, HttpServletResponse response,
            ServletContext application) throws Exception;

    /**
     * Gera a saida do page no browser. Cada implementacao tera a sua propria.
     *
     * @param page
     * @param request
     * @param response
     * @param application
     * @throws Exception
     */
    protected abstract void showPage(Page page, HttpServletRequest request,
            HttpServletResponse response, ServletContext application)
            throws Exception;

    /**
     * Executa o Listener. Esta como publico e estatico porque e usado pela
     * taglib.
     *
     * @param page
     * @param request
     * @param response
     * @param application
     * @throws Exception
     */
    public static void executeListener(Page page, HttpServletRequest request,
            HttpServletResponse response, ServletContext application)
            throws Exception {
        if (page.getListener() != null) {
            PageController listener = (PageController) page.getListener().newInstance();
            listener.processPage(page, request, response, application);
        }
    }

    /**
     * Redireciona para processRequest.
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Redireciona para processRequest.
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
