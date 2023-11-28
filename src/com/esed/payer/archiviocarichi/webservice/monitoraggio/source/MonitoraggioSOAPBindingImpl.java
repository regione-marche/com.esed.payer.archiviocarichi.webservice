/**
 * MonitoraggioSOAPBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.esed.payer.archiviocarichi.webservice.monitoraggio.source;

import java.sql.Connection;
import java.util.List;

//import com.esed.payer.archiviocarichi.webservice.exception.ValidazioneException;
import com.esed.payer.archiviocarichi.webservice.handler.WebServiceHandler;
import com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.MonitoraggioTransazioniResponse;
import com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.ResponseType;
import com.seda.data.dao.DAOHelper;
import com.seda.payer.core.bean.DettaglioTransazione;
import com.seda.payer.core.dao.DettaglioTransazioneDao;
public class MonitoraggioSOAPBindingImpl extends WebServiceHandler implements com.esed.payer.archiviocarichi.webservice.monitoraggio.source.MonitoraggioInterface{

	private Connection connection = null;
	private DettaglioTransazioneDao dettaglioTransazioniDao = null; 
	
	@Override
	public com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.MonitoraggioTransazioniResponse monitoraggioTransazioni(com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.MonitoraggioTransazioniRequest in) throws java.rmi.RemoteException, com.esed.payer.archiviocarichi.webservice.srv.FaultType {
    
		MonitoraggioTransazioniResponse response = new MonitoraggioTransazioniResponse();
    	   	
//        this.codiceSocieta = codiceSocieta;
//        this.codiceUtente = codiceUtente;
//        this.codiceEnte = codiceEnte;
//        this.tipoUfficio = tipoUfficio;
//        this.codiceUfficio = codiceUfficio;
//        this.numeroDocumento = numeroDocumento;
//        this.numeroBollettinoPagoPA = numeroBollettinoPagoPA;
    	debug("com.esed.payer.archiviocarichi.webservice.monitoraggio - monitoraggioTransazioni - inizio");
//    	VariazioneEcResponse response = new VariazioneEcResponse(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), "", "", in.getDocumento());
    	try {
    		dbSchemaCodSocieta = in.getCodiceUtente();
    		this.connection = getConnectionDifferito(dbSchemaCodSocieta);
    		dettaglioTransazioniDao = new DettaglioTransazioneDao(this.connection, getSchemaDifferito(dbSchemaCodSocieta));
	    	        	
		    List<DettaglioTransazione> listDetTra = dettaglioTransazioniDao.listMonTransazioni(in.getCodiceSocieta(),in.getCodiceUtente(),in.getCodiceEnte(),in.getTipoUfficio(),in.getCodiceUfficio(),in.getNumeroDocumento(),in.getNumeroBollettinoPagoPA());

		    com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.DettaglioTransazione[] listDetTraWS = generateItemFromResponse(listDetTra);
		    response.setListDettaglioTransazione(listDetTraWS);

		    ResponseType resp = new ResponseType();
		    if(listDetTra!=null && listDetTra.size()>0){
		    	resp.setRetCode("00");
	    		resp.setRetMessage("Operazione effettuata con successo");
		    	
		    }
	    		else{
	    			resp.setRetCode("04");
		    		resp.setRetMessage("Nessuna transazione individuata");
    		}
	    			
	    			
	    			
	    	response.setResponse(resp);
	    		    	
    	} catch (Exception e) {
			error("com.esed.payer.archiviocarichi.webservice.monitoraggio - monitoraggioTransazioni failed, generic error due to: ", e);
//			response.setCodiceEsito("01");
//			response.setMessaggioEsito("Errore generico");
		} finally {
			closeConnection();
		}
    	debug("com.esed.payer.archiviocarichi.webservice.monitoraggio - monitoraggioTransazioni - fine");

    	
    	//TODO da inserire in questo punto la logica implementativa

    	
    	//TODO da inserire in questo punto la logica implementativa
    	return response;
    }
	
	private void closeConnection() {
    	if (this.connection != null)
    		DAOHelper.closeIgnoringException(this.connection);
	}
	private com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.DettaglioTransazione[] generateItemFromResponse(List<DettaglioTransazione> listDetTra) {
		com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.DettaglioTransazione[] listResponse = new com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.DettaglioTransazione[listDetTra.size()];   
		int itemIdx = 0;
		for (DettaglioTransazione item : listDetTra) {
			com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.DettaglioTransazione dt = new com.esed.payer.archiviocarichi.webservice.monitoraggio.dati.DettaglioTransazione();
			dt.setCodiceTransazione(item.getCodiceTransazione());
			dt.setDataTransazione(item.getDataTransazione());
			dt.setDataPagamento(item.getDataPagamento());
			dt.setCodiceAutorizzazioneBanca(item.getCodiceAutorizzazioneBanca());
			dt.setCodiceIdentificativoBancaIUR(item.getCodiceIdentificativoBancaIUR());
			dt.setCodiceIUV(item.getCodiceIUV());
			dt.setImportoTotaleTransazione(item.getImportoTotaleTransazione());
			dt.setTipologiaBollettino(item.getTipologiaBollettino());
			dt.setTipologiaServizio(item.getTipologiaServizio());
			dt.setNumeroBollettinoPagoPA(item.getNumeroBollettinoPagoPA());
			dt.setImportoBollettino(item.getImportoBollettino());
			dt.setNumeroContoCorrente(item.getNumeroContoCorrente());
			dt.setIntestatarioContoCorrente(item.getIntestatarioContoCorrente());
			dt.setNumeroDocumento(item.getNumeroDocumento());
			dt.setCodiceFiscale(item.getCodiceFiscale());
			dt.setDenominazione(item.getDenominazione());
			dt.setFlagRendicontazione(item.getFlagRendicontazione());
			dt.setFlagRiconciliazione(item.getFlagRiconciliazione());

			listResponse[itemIdx++] = dt;
		}
		return listResponse;
	}


}
