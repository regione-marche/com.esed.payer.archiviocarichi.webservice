package com.esed.payer.archiviocarichi.webservice.listener;

import javax.servlet.ServletContextEvent;
import com.seda.compatibility.SystemVariable;
import com.seda.j2ee5.listener.spi.ApplicationListenerHandler;
import com.esed.payer.archiviocarichi.webservice.config.*;

public class ApplicationListener extends ApplicationListenerHandler {
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.listener.spi.ApplicationListenerHandler#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) { }
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.listener.spi.ApplicationListenerHandler#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		String rootPath = event.getServletContext().getInitParameter(PrintStrings.CONFIG_FILE.format());
		if (rootPath==null) {
			SystemVariable sv = new SystemVariable();
			rootPath=sv.getSystemVariableValue(PrintStrings.ROOT.format());
			sv=null;
			System.out.println("ApplicationListener - Path file di configurazione letto da variabile: " + rootPath); //GG
		} 
		if (rootPath!=null) {
			configurePropertiesTree(PrintStrings.TREE_CONTEXT_NAME.format(), rootPath);
			// initialize log4j for this application context
//			configureLogger(PrintStrings.LOGGER_CONTEXT_NAME.format(), 
//				propertiesTree().getProperties(PrintStrings.LOGGER_PROPERTIES_NAME.format()));
			System.out.println("ApplicationListener - Caricamento configurazioni");	//GG
		}
	}
}