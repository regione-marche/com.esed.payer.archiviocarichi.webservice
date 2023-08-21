package com.esed.payer.archiviocarichi.webservice.config;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum PropKeys {
	DEFAULT_NODE,
	//inizio LP PG200070
	//JNDI_CONTEXT,
	//JNDI_PROVIDER,
	//fine LP PG200070
	differitoCatalogInitialContextFactory,
	differitoCatalogJndiAlias,
	differitoCatalogSchema,
	auxDigitConfig,
	applicationCodeConfig,
	urlWSRestGeos
	 //inizio GG 20210909
	, generatorePdf
	, urlWSRestPdf
	 //fine GG 20210909
	// inizio SR 20230108
	, username
	, password
	, idEnte
	// fine SR 20230108
	;

    private static ResourceBundle rb;

    public String format( Object... args ) {
        synchronized(PropKeys.class) {
            if(rb==null)
            	rb = ResourceBundle.getBundle(PropKeys.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
}