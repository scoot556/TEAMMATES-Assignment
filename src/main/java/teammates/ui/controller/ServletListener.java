package teammates.ui.controller;
import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String rootPath = System.getProperty("C:\\Users\\scott\\SickCairns\\src\\main\\webapp\\PDF");
        ServletContext ctx = servletContextEvent.getServletContext();
        String relPath = ctx.getInitParameter("C:\\Users\\scott\\SickCairns\\src\\main\\webapp\\PDF");
        File file = new File(rootPath + File.separator + relPath);
        if(!file.exists()) file.mkdirs();
        System.out.println("File Directory created to be used for storing files" + file);
        ctx.setAttribute("FILES_DIR_FILE", file);
        ctx.setAttribute("FILES_DIR", rootPath + File.separator + relPath);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //do cleanup if needed
    }

}