package com.esed.payer.archiviocarichi.webservice.handler;

import java.sql.Connection;
import java.util.Iterator;

import javax.sql.DataSource;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.server.ServletEndpointContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import com.esed.payer.archiviocarichi.webservice.config.PrintStrings;
import com.esed.payer.archiviocarichi.webservice.config.PropKeys;
import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;
import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.webservice.spi.JaxRpc10WebServiceHandler;


public abstract class WebServiceHandler extends JaxRpc10WebServiceHandler {

	protected String loggerContextName = PrintStrings.LOGGER_CONTEXT_NAME.format();
	protected String treeContextName = PrintStrings.TREE_CONTEXT_NAME.format();
	//inizio LP PG200070 
	//protected Properties env;
	//fine LP PG200070
	protected String dbSchemaCodSocieta; 
	public final String DBSCHEMACODSOCIETA = "dbSchemaCodSocieta";
	
	protected static LoggerWrapper logger = CustomLoggerManager.get(WebServiceHandler.class);
	
	@Override
	public void init(Object endPointContext) throws ServiceException {
		super.init(endPointContext);
    	//logger(loggerContextName);
    	propertiesTree(treeContextName);
    	
    	System.out.println("WebServiceHandler - init - setDbSchemaCodSocieta");	//GG
    	setDbSchemaCodSocieta(endPointContext);
    	System.out.println("WebServiceHandler - dbSchemaCodSocieta: " + dbSchemaCodSocieta);	//GG
    	
    	//inizio LP PG200070 
    	//System.out.println("WebServiceHandler - INITIAL_CONTEXT_FACTORY: " + propertiesTree().getProperty(PropKeys.JNDI_CONTEXT.format(PropKeys.DEFAULT_NODE.format())));	//GG
    	//System.out.println("WebServiceHandler - PROVIDER_URL: " + propertiesTree().getProperty(PropKeys.JNDI_PROVIDER.format(PropKeys.DEFAULT_NODE.format())));	//GG
    	//env = new Properties();
    	//env.put(Context.INITIAL_CONTEXT_FACTORY, propertiesTree().getProperty(PropKeys.JNDI_CONTEXT.format(PropKeys.DEFAULT_NODE.format())));
    	//env.put(Context.PROVIDER_URL, propertiesTree().getProperty(PropKeys.JNDI_PROVIDER.format(PropKeys.DEFAULT_NODE.format())));
    	//fine LP PG200070
	}
	
	@SuppressWarnings("unchecked")
	private void setDbSchemaCodSocieta (Object endPointContext) {
		ServletEndpointContext ctx=null;
		
		if (javax.xml.rpc.server.ServletEndpointContext.class.isInstance(endPointContext))
			ctx = (ServletEndpointContext) endPointContext;

		if (ctx != null) {
			SOAPMessageContext mc = (SOAPMessageContext)ctx.getMessageContext();
			// process SOAP header as shown in the message handler
			try {
				SOAPHeader header = mc.getMessage().getSOAPPart().getEnvelope().getHeader();

				Iterator headers = header.examineAllHeaderElements(); //header.extractHeaderElements("http://schemas.xmlsoap.org/soap/actor/next");
				while (headers.hasNext()) {
					SOAPHeaderElement he = (SOAPHeaderElement)headers.next();
					if(he.getNodeName().equals("dbSchemaCodSocieta"))
						dbSchemaCodSocieta=new String(he.getValue());
				} 
			} catch (SOAPException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the dataSource
	 */
	public DataSource getDataSourceDifferito (String dbSchemaCodSocieta) {
    	DataSource dataSource = null;
		String dataSourceName = getDataSourceNameDifferito(dbSchemaCodSocieta);
		try {	
			dataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
		} catch (ServiceLocatorException e) {
			error(e.toString());
		}
		return dataSource;
    }
	
	/**
	 * @return the dataSourceName
	 */
	public String getDataSourceNameDifferito(String dbSchemaCodSocieta) {
		return propertiesTree().getProperty(PropKeys.differitoCatalogJndiAlias.format(dbSchemaCodSocieta));
	}
	
	/**
	 * @return the connection
	 * @param dbSchemaCodSocieta Codice societa per connessione dinamica al DB
	 */
	public Connection getConnectionDifferito(String dbSchemaCodSocieta) throws JndiProxyException {
		return new JndiProxy().getSqlConnection(getInitialContextFactoryDifferito(dbSchemaCodSocieta), getDataSourceNameDifferito(dbSchemaCodSocieta), true);
	}

	/**
	 * @return the connection
	 * @param dbSchemaCodSocieta Codice societa per connessione dinamica al DB
	 * @param autoCommit Autocommit
	 */
	public Connection getConnectionDifferito(String dbSchemaCodSocieta,boolean autoCommit) throws JndiProxyException {
		return new JndiProxy().getSqlConnection(getInitialContextFactoryDifferito(dbSchemaCodSocieta), getDataSourceNameDifferito(dbSchemaCodSocieta), autoCommit);
	}

	/**
	 * @return the initialContextFactory
	 */
	public String getInitialContextFactoryDifferito(String dbSchemaCodSocieta) {
		return propertiesTree().getProperty(PropKeys.differitoCatalogInitialContextFactory.format(dbSchemaCodSocieta));
	}
	
	/**
	 * @return the schema
	 */
	public String getSchemaDifferito(String dbSchemaCodSocieta) {
		return propertiesTree().getProperty(PropKeys.differitoCatalogSchema.format(dbSchemaCodSocieta));
	}
}