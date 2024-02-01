/**
 * IntegraEcDifferitoSOAPBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.esed.payer.archiviocarichi.webservice.integraecdifferito.source;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.WebRowSet;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXB;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.apache.commons.io.FileUtils;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.esed.payer.archiviocarichi.webservice.config.PropKeys;
import com.esed.payer.archiviocarichi.webservice.exception.ConfigurazioneException;
import com.esed.payer.archiviocarichi.webservice.exception.DuplicateException;
import com.esed.payer.archiviocarichi.webservice.exception.NotFoundException;
import com.esed.payer.archiviocarichi.webservice.exception.ValidazioneException;
import com.esed.payer.archiviocarichi.webservice.handler.WebServiceHandler;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Anagrafica;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Configurazione;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.ConfigurazioneIUV;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DettaglioContabile;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DettaglioPagamento;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Documento;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DocumentoCarico;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Ruolo;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Scadenza;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Tributo;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcResponse;
import com.esed.payer.archiviocarichi.webservice.model.CaricaDebitiJppa;
import com.esed.payer.archiviocarichi.webservice.model.InviaDovutiDao;
import com.esed.payer.archiviocarichi.webservice.util.GenericsDateNumbers;
import com.esed.payer.archiviocarichi.webservice.util.IuvUtils;
import com.esed.payer.archiviocarichi.webservice.util.VerificaCodiceFiscale;
//inizio LP - mail Giorgia 20200608
//import com.esed.payer.archiviocarichi.webservice.util.geos.Bollettino;
//import com.esed.payer.archiviocarichi.webservice.util.geos.DatiAnagrafici;
//import com.esed.payer.archiviocarichi.webservice.util.geos.DatiCreditore;
//import com.esed.payer.archiviocarichi.webservice.util.geos.Flusso;
//fine LP - mail Giorgia 20200608
import com.esed.payer.archiviocarichi.webservice.util.geos.GeosUtil;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.seda.commons.string.Convert;
import com.seda.data.helper.Helper;
//inizio LP - mail Giorgia 20200608
import com.seda.payer.commons.geos.Bollettino;
import com.seda.payer.commons.geos.DatiAnagrafici;
import com.seda.payer.commons.geos.DatiCreditore;
import com.seda.payer.commons.geos.Flusso;
import com.seda.payer.commons.geos.WSRest_GEOS;
//fine LP - mail Giorgia 20200608
import com.seda.payer.core.bean.ArchivioCarichiAnagrafica;
import com.seda.payer.core.bean.ArchivioCarichiCoda;
import com.seda.payer.core.bean.ArchivioCarichiDocumento;
import com.seda.payer.core.bean.ArchivioCarichiMovimento;
import com.seda.payer.core.bean.ArchivioCarichiRuolo;
import com.seda.payer.core.bean.ArchivioCarichiTesta;
import com.seda.payer.core.bean.ArchivioCarichiTributo;
import com.seda.payer.core.bean.ConfigPagamento;
import com.seda.payer.core.bean.ConfigurazioneModello3;
import com.seda.payer.core.bean.Ente;
import com.seda.payer.core.bean.EntrateTributiPage;
import com.seda.payer.core.dao.ArchivioCarichiDao;
import com.seda.payer.core.dao.ConfigPagamentoDao;
import com.seda.payer.core.dao.ConfigurazioneModello3DaoImpl;
import com.seda.payer.core.dao.ElaborazioneFlussiDao;
import com.seda.payer.core.dao.EnteDao;
import com.seda.payer.core.dao.EntrateBancaDatiDao;
import com.seda.payer.core.dao.EstrattoContoDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.facade.dto.ConfigPagamentoDto;
import com.seda.payer.integraente.webservice.dati.RecuperaDatiBollettinoResponse;
import com.seda.payer.integraente.webservice.dati.TipoBollettino;

import io.swagger.client.model.ContribuenteDto;
import io.swagger.client.model.ContribuenteDto.TipoIdentificativoUnivocoEnum;
import io.swagger.client.model.DatoAccertamentoDto;
import io.swagger.client.model.DettaglioDovutoDto;
import io.swagger.client.model.DettaglioDovutoDto.CodiceTipoDebitoEnum;
import io.swagger.client.model.DettaglioDovutoDto.SpeseNotificaDaAttualizzareEnum;
import io.swagger.client.model.DovutoDto;
import io.swagger.client.model.DovutoDto.ContestoDovutoEnum;
import io.swagger.client.model.NumeroAvvisoDto;
import io.swagger.client.model.RispostaInviaDovutiDto;
//inizio LP - mail Giorgia 20200608
//import com.esed.payer.archiviocarichi.webservice.util.geos.WSRest_GEOS;
//fine LP - mail Giorgia 20200608
import io.swagger.client.model.TestataDovutoDto;

public class IntegraEcDifferitoSOAPBindingImpl extends WebServiceHandler implements com.esed.payer.archiviocarichi.webservice.integraecdifferito.source.IntegraEcDifferitoInterface{
	
	private ElaborazioneFlussiDao elaborazioneFlussiDao = null;
	private ArchivioCarichiDao archivioCarichiDao = null;
	private EstrattoContoDao estrattoContoDao = null;
	private ConfigPagamentoDao configPagamentoDao = null; //LP PG22XX05
	private EnteDao enteDato = null; //LP PG22XX05
	//inizio LP PG210130 Step02
	private List<String> lstEnti = new ArrayList<String>();
	//fine LP PG210130 Step02
	//inizio LP PG22XX05
	private List<String> lstFunEnti = new ArrayList<String>();
	//fine LP PG22XX05

	// inizio SR PGNTACWS-2
	private CachedRowSet ecCachedDettaglioPagamento = null;
	private CachedRowSet ecCachedDettaglioContabile = null;
	private RecuperaDatiBollettinoResponse pgResponse = null;
	private String listXmlDP = null;
	private String listXmlDC = null;
	private int progressivoFlussoPerInviaDovuti = 0;
	// fine SR PGNTACWS-2
	
	private BigDecimal[] listaImportiScadenze = null; // SR PGNTACWS-5 
	private BigDecimal maxImport = null; // SR PGNTACWS-5
	
	@Override
	public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcResponse inserimentoEC(com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcRequest in) throws java.rmi.RemoteException, com.esed.payer.archiviocarichi.webservice.srv.FaultType {
		
		// inizio SR PGNTACWS-5 arrotondamento importi
		if(in.getListScadenze() != null && in.getListScadenze().length != 0) {
			this.listaImportiScadenze = new BigDecimal[in.getListScadenze().length];
			for (int i = 0; i<in.getListScadenze().length; i++) {
				listaImportiScadenze[i] = in.getListScadenze()[i].getImpBollettinoRata();
			}			
			this.maxImport = Arrays.asList(listaImportiScadenze).stream().max(BigDecimal::compareTo).get();
		}
		// fine SR PGNTACWS-5 
		
		CachedRowSet ecCached = null;
		
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - inizio");
		StringWriter sw = new StringWriter();
		JAXB.marshal(in, sw);
		String xmlString = sw.toString();
		//inizio LP PG22XX05
		try {
			sw.close();
		} catch (IOException e1) {
		}
		sw = null;
		//fine LP PG22XX05
    	ClearInserimentoEC(in); //LP PG22XX05
    	logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - request: " + xmlString);
		InserimentoEcResponse response = new InserimentoEcResponse(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), "", "", in.getDocumento());
		boolean checkDuplicate = false;
		int numeroRecord = 0;
		Connection connection = null;
        try {
        	dbSchemaCodSocieta = in.getCodiceUtente();
        	connection = getConnectionDifferito(dbSchemaCodSocieta);
        	elaborazioneFlussiDao = new ElaborazioneFlussiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
        	archivioCarichiDao = new ArchivioCarichiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
        	configPagamentoDao = new ConfigPagamentoDao(connection, getSchemaDifferito(dbSchemaCodSocieta)); //LP PG22XX05
        	enteDato = new EnteDao(connection, getSchemaDifferito(dbSchemaCodSocieta)); //LP PG22XX05
        	
        	Configurazione configurazione = in.getConfigurazione();
        	Ruolo ruolo = in.getRuolo();
        	Documento documento = in.getDocumento();
			Anagrafica anagrafica = in.getAnagrafica();
			int progressivoFile = 0;
			
			//********SPOSTATO DA RIGA 146 - inizio******************//
			Calendar calCurrentDate = Calendar.getInstance();
			String dataFlusso = GenericsDateNumbers.calendarToString(calCurrentDate, "yyyy-MM-dd");		//data flusso nel formato AAAA-MM-GG
			String fileNameToElab =  configurazione.getIdentificativoFlusso();
			System.out.println("Nome Flusso:" + fileNameToElab);
			//Dicembre 2020 - Controllo per inserimento idFlusso su enti diversi - TK: 2020121088000104 
			Matcher matcher = Pattern.compile("[0-9]{5}_").matcher(fileNameToElab); 
			String flagVariazione = fileNameToElab.substring(fileNameToElab.length()-3, fileNameToElab.length());
			//Controllo se sono in variazione
			//
			if(flagVariazione.equals("_v")) {
				fileNameToElab = configurazione.getIdentificativoFlusso().substring(0,fileNameToElab.length()-3);
			}else {
				if(!matcher.lookingAt()){
					fileNameToElab = in.getCodiceEnte() + "_" + configurazione.getIdentificativoFlusso();
				}else {
					fileNameToElab = configurazione.getIdentificativoFlusso();
				}
				//String fileNameToElab = "AC" + in.getCodiceUtente() + GenericsDateNumbers.calendarToString(calCurrentDate, "yyyyMMddHHmmssSSS") + ".txt";	//maxlength 50
	    		//Prima verifico la presenza in archivio di flussi con stesso identificativo flusso /nome file in giornate differenti
	    		int progFlussoPerNomeAltraData = archivioCarichiDao.getProgFlussoPerNomeAltraData(fileNameToElab, java.sql.Date.valueOf(dataFlusso));
	    		if (progFlussoPerNomeAltraData > 0) 
	    			throw new ValidazioneException("identificativo flusso " + fileNameToElab + " gi presente in archivio");
			}
			configurazione.setIdentificativoFlusso(fileNameToElab);
			//********SPOSTATO DA RIGA 146 - fine******************//
			
        	//Verifica input
			logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - checkRequest");
			boolean resCheck = checkRequest(in.getCodiceUtente(), in.getCodiceEnte(), configurazione, documento, in.getListScadenze(), in.getListTributi(), anagrafica, "I");

        	if (resCheck) {
				//inizio LP PG210130
				DettaglioPagamento[] listPagamento = null;
				DettaglioContabile[] listContabile = null;
				int numeroRate = in.getListScadenze().length;
				//inizio LP PG210130 Step03
				//inizio LP Abilitazione Dati Multi Beneficiario per Tutti i CuteCute - 20211126
				//if(in.getCodiceUtente().equals("000RM")
				//   || in.getCodiceUtente().equals("000LP")
				if(true
				//fine LP Abilitazione Dati Multi Beneficiario per Tutti i CuteCute - 20211126
				  ) {
				//fine LP PG210130 Step03
				//inizio LP PG22XX05
				//Map<String, List<Tributo>> listDocTrib = otherCheck(in.getCodiceUtente(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), configurazione, documento, in.getListScadenze(), in.getListTributi());
				Map<String, List<Tributo>> listDocTrib = otherCheck(in.getCodiceUtente(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), configurazione, documento, in.getListScadenze(), in.getListTributi(), in.getRuolo().getCodiceTipologiaServizio());
				//fine LP PG22XX05
		    	try {
		    		listContabile = DocumentoCarico.preparaArchivioCarichiContabile(in.getDocumento(), in.getListScadenze(), in.getListTributi());
		    	} catch (Exception e) {
		    		throw new ValidazioneException(e.getMessage());
		    	}
		    	//inizio LP PG210130 Step02
		    	//Nota. Costruiamo i "dettaglio pagamento" a prescindere
		    	//      chi usa i "dettaglio pagamento" dovra' eventualemente  
		    	//      "scartare" quelli con importo nullo e a non usarli per
		    	//      pagamenti tipo "multi-beneficiario" se non sono almeno 2
		    	//      e/o non contengono iddominio dell'ente principale
		    	//      Il tutto per consenteri la gestione delle eventuali
		    	//      operazioni di discarico.
		    	//if(listDocTrib.size() > 1) {
		    	//fine LP PG210130 Step02
			    	try {
			    		listPagamento = DocumentoCarico.preparaArchivioCarichiPagamento(in.getDocumento(), numeroRate, listContabile, listDocTrib);
			    	} catch (Exception e) {
			    		throw new ValidazioneException(e.getMessage());
			    	}
		    	//inizio LP PG210130 Step02
		    	//}
        		//fine LP PG210130 Step02
				//inizio LP PG210130 Step03
				}
				//fine LP PG210130 Step03
				BigDecimal impo = BigDecimal.ZERO;
				int nRate = (numeroRate == 1 ? 1 : numeroRate + 1);
				if(listContabile != null && listContabile.length > 0) {
					for(int iRata = 0; iRata < nRate; iRata++) {
						impo = BigDecimal.ZERO;
						for(DettaglioContabile dettCont : listContabile) {
							if(dettCont.getNumeroRata() == iRata)
								impo = impo.add(dettCont.getImporto());
						}
						if(iRata == 0 && impo.compareTo(documento.getImpBollettinoTotaleDocumento()) != 0)
				    		throw new ValidazioneException("importo totale bollettino su documento " + GenericsDateNumbers.bigDecimalToDouble(documento.getImpBollettinoTotaleDocumento()) + " diverso da totale importo su 'dettaglio contabile' " + GenericsDateNumbers.bigDecimalToDouble(impo));
						else if(iRata != 0) {
							for(Scadenza scadenza : in.getListScadenze()) {
								if(scadenza.getNumeroRata() == iRata) {
									if(impo.compareTo(scadenza.getImpBollettinoRata()) != 0) 
							    		throw new ValidazioneException("importo rata #" + iRata + " bollettino su documento " + GenericsDateNumbers.bigDecimalToDouble(scadenza.getImpBollettinoRata()) + " diverso da importo su 'dettaglio contabile' " + GenericsDateNumbers.bigDecimalToDouble(impo));
									break;
								}
							}
						}
					}
				}
				if(listPagamento != null && listPagamento.length > 0) {
					for(int iRata = 0; iRata < nRate; iRata++) {
						impo = BigDecimal.ZERO;
						for(DettaglioPagamento dettPag : listPagamento) {
							if(dettPag.getNumeroRata() == iRata)
								impo = impo.add(dettPag.getImporto());
						}
						if(iRata == 0 && impo.compareTo(documento.getImpBollettinoTotaleDocumento()) != 0)
				    		throw new ValidazioneException("importo totale bollettino su documento " + GenericsDateNumbers.bigDecimalToDouble(documento.getImpBollettinoTotaleDocumento()) + " diverso da totale importo su 'dettaglio pagamento' " + GenericsDateNumbers.bigDecimalToDouble(impo));
						else if(iRata != 0) {
							for(Scadenza scadenza : in.getListScadenze()) {
								if(scadenza.getNumeroRata() == iRata) {
									if(impo.compareTo(scadenza.getImpBollettinoRata()) != 0) 
							    		throw new ValidazioneException("importo rata #" + iRata + " bollettino su documento " + GenericsDateNumbers.bigDecimalToDouble(scadenza.getImpBollettinoRata()) + " diverso da importo su 'dettaglio pagamento' " + GenericsDateNumbers.bigDecimalToDouble(impo));
									break;
								}
							}
						}
					}
				}
				//fine LP PG210130

        		//Avvio elaborazione
        		//Con utilizzo core
        		
				progressivoFile = elaborazioneFlussiDao.doMaxPrgressivoFlussi(in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso));
			
        		//inizio LP PG200070
	        	//IntegraEnteEcDifferitoFacadeRemoteHome serviceRemoteHomeFacade = 
        		//	(IntegraEnteEcDifferitoFacadeRemoteHome) ServiceLocator.getInstance().getRemoteHome(
        		//		env.getProperty(Context.PROVIDER_URL), env.getProperty(Context.INITIAL_CONTEXT_FACTORY), null, null,
        		//		IntegraEnteEcDifferitoFacadeRemoteHome.JNDI_NAME, IntegraEnteEcDifferitoFacadeRemoteHome.class);
        		//IntegraEnteEcDifferitoFacade facade = serviceRemoteHomeFacade.create();
        		//fine LP PG200070
	        	
				
        		//inizio LP PG200070
				//int progressivoFlusso = facade.saveLogFlussi(-1, in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), progressivoFile, "PER", 
				//		"EP", fileNameToElab, new Timestamp(System.currentTimeMillis()), 
				//		new Timestamp(System.currentTimeMillis()), "Y", "N", dbSchemaCodSocieta);
				int progressivoFlusso = saveLogFlussi(elaborazioneFlussiDao, -1, in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso),
						                              progressivoFile, "PER", "EP", fileNameToElab, new Timestamp(System.currentTimeMillis()), 
						                              new Timestamp(System.currentTimeMillis()), "Y", "N", dbSchemaCodSocieta);
				
				this.progressivoFlussoPerInviaDovuti = progressivoFlusso;
				//fine LP PG200070
				logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - progressivoFlusso: " + progressivoFlusso);
				
				boolean flagSuccessfullExecution = false;
				try {	
					String numeroBollettinoPagoPA = "";
					String identificativoUnivocoVersamento = "";
					logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - getDocumento numeroDocumento: " + documento.getNumeroDocumento());
					ArchivioCarichiDocumento docIn = prepareArchivioCarichiDocumento(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), documento.getNumeroDocumento());
					ArchivioCarichiDocumento docOut = archivioCarichiDao.getDocumento(docIn);
					if (docOut.getProgressivoFlusso() == null) { 
						String dataNotifica = GenericsDateNumbers.formatData(documento.getDataNotifica(),"dd/MM/yyyy","yyyy-MM-dd"); 
						if (dataNotifica.equals(""))
							dataNotifica = "1900-01-01";	
						if(configurazione.getFlagGenerazioneIUV().equals("Y")) {
							String[] codici = IuvUtils.calcolaIuv(in.getCodiceEnte(),configurazione.getConfigurazioneIUV(), connection, getSchemaDifferito(dbSchemaCodSocieta)); 
							numeroBollettinoPagoPA = codici[0];
							identificativoUnivocoVersamento = codici[1];
							System.out.println("Identificativo univoco versamento"+identificativoUnivocoVersamento);
							response.getDocumento().setNumeroBollettinoPagoPA(numeroBollettinoPagoPA);
							response.getDocumento().setIdentificativoUnivocoVersamento(identificativoUnivocoVersamento);
							logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - numeroBollettinoPagoPA: " + numeroBollettinoPagoPA);
							logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - identificativoUnivocoVersamento: " + identificativoUnivocoVersamento);
						} else {
							numeroBollettinoPagoPA = documento.getNumeroBollettinoPagoPA();
							identificativoUnivocoVersamento = documento.getIdentificativoUnivocoVersamento();
						}
						logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEH1");
						//EH1 Documento
						numeroRecord++;
						//Attenzione a numero bollettino pagoPA perch se non viene fornito dovrebbe essere da noi generato secondo le configurazioni, previo controllo delle configurazioni con quanto in input
						elaborazioneFlussiDao.doInsertEH1(progressivoFlusso, "EH1", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
								in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), 
								in.getImpostaServizio(), documento.getNumeroDocumento(), anagrafica.getCodiceFiscale().toUpperCase(), 
								"", "", documento.getAnnoEmissione(), documento.getNumeroEmissione(), "", 
								"",		//flagRendicontato, valori ammessi: R(rendicontato) o E(estinto) o C(condonato)
								java.sql.Date.valueOf(dataNotifica), 
								"",		//TODO da verificare se C(nota credito) o I(ingiunzione) 
								numeroBollettinoPagoPA, 	
								GenericsDateNumbers.bigDecimalToDouble(documento.getImpBollettinoTotaleDocumento()),	 
								"", 	//codiceEnteEntrate
								"",		//TODO da verificare se S(flag sospensione) o D/R(domanda rimborso) 
								"", 	//TODO da verificare se S(procedure esecutive in corso) 
								"", 
								"",		//TODO da verificare se S(cartella proveniente da maggior rateazione)		 
								in.getRuolo().getCodiceTipologiaServizio(),
								'C', documento.getIbanAccredito(), documento.getFlagFatturazioneElettronica(), identificativoUnivocoVersamento
								, documento.getCausale() //PG180020 CT
								, documento.getIbanAppoggio()==null?"":documento.getIbanAppoggio() //PG200140
								, documento.getTassonomia()==null?"":documento.getTassonomia()); //PG200360 LP
					} else {
						checkDuplicate = true;
						throw new Exception("Posizione debitoria gi presente in archivio");
					}
			
					//EH2 Scadenze
					BigDecimal maxImportNew = Arrays.asList(in.getListScadenze()).stream().map(s-> s.getImpBollettinoRata()).max(BigDecimal::compareTo).get();
					BigDecimal scarto = maxImportNew.subtract(this.maxImport);
					
					if (in.getListScadenze()!=null && in.getListScadenze().length>0) {
						for(Scadenza scadenza : in.getListScadenze()) {
							numeroRecord++;
							logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - getScadenza numeroRata: " + scadenza.getNumeroRata());
							//ArchivioCarichiScadenza scadIn = prepareArchivioCarichiScadenza(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), documento.getNumeroDocumento(), scadenza.getNumeroRata());
							//ArchivioCarichiScadenza scadOut = archivioCarichiDao.getScadenza(scadIn);
							String numeroBollettinoScadenzaPagoPA = "";
							String identificativoUnivocoVersamentoScadenza = "";
							if(configurazione.getFlagGenerazioneIUV().equals("Y")) {
								//Nel caso il bollettino sia monorata, numero avviso /iuv documento = numero avviso/iuv rata
								if (in.getListScadenze().length==1) {
									numeroBollettinoScadenzaPagoPA = numeroBollettinoPagoPA;
									identificativoUnivocoVersamentoScadenza = identificativoUnivocoVersamento;
								} else {
									String[] codiciScadenza = IuvUtils.calcolaIuv(in.getCodiceEnte(),configurazione.getConfigurazioneIUV(), connection, getSchemaDifferito(dbSchemaCodSocieta));
									numeroBollettinoScadenzaPagoPA = codiciScadenza[0] ;
									identificativoUnivocoVersamentoScadenza = codiciScadenza[1] ;
									logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - numeroBollettinoScadenzaPagoPA: " + numeroBollettinoScadenzaPagoPA);
									logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - identificativoUnivocoVersamentoScadenza: " + identificativoUnivocoVersamentoScadenza);
									//inizio LP PG210130
									scadenza.setNumeroBollettinoPagoPA(numeroBollettinoScadenzaPagoPA);
									//fine LP PG210130
								}
							} else { 
								numeroBollettinoScadenzaPagoPA = scadenza.getNumeroBollettinoPagoPA();
								identificativoUnivocoVersamentoScadenza = scadenza.getIdentificativoUnivocoVersamento();
							}
							
							
							// inizio SR PGNTACWS-5 arrotondamento importi							
							if(scadenza.getImpBollettinoRata().compareTo(this.listaImportiScadenze[scadenza.getNumeroRata()-1]) > 0) {	
								scadenza.setImpBollettinoRata(scadenza.getImpBollettinoRata().subtract(scarto));
							} else if(scadenza.getImpBollettinoRata().compareTo(this.listaImportiScadenze[scadenza.getNumeroRata()-1]) < 0) {
								BigDecimal diff = this.listaImportiScadenze[scadenza.getNumeroRata()-1].subtract(scadenza.getImpBollettinoRata());
								scadenza.setImpBollettinoRata(scadenza.getImpBollettinoRata().add(diff));
								scarto = scarto.subtract(diff);								
							}
							// fine SR PGNTACWS-5
							
							logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEH2");
							elaborazioneFlussiDao.doInsertEH2(progressivoFlusso, "EH2", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
									in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), 
									in.getImpostaServizio(), documento.getNumeroDocumento(), 
									GenericsDateNumbers.bigDecimalToDouble(scadenza.getImpBollettinoRata()), //importo dovuto tributi
									0,
									java.sql.Date.valueOf(GenericsDateNumbers.formatData(scadenza.getDataScadenzaRata(),"dd/MM/yyyy","yyyy-MM-dd")),
									scadenza.getNumeroRata(), 
									numeroBollettinoScadenzaPagoPA,		//scadenza.getNumeroBollettinoPagoPA() 
									GenericsDateNumbers.bigDecimalToDouble(scadenza.getImpBollettinoRata()), 
									0, 0, 0, 0, 0, 0, 0, 'C', identificativoUnivocoVersamentoScadenza);
						}
					}
					
					//inizio LP PG210130
					//EHD dettaglio pagamento
					if(listPagamento != null && listPagamento.length > 0) {
						for(DettaglioPagamento dettPag : listPagamento) {
							numeroRecord++;
							int numeroRata = dettPag.getNumeroRata();
							logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - dettPag numeroRata: " + numeroRata);
							boolean trovato = false;
							if(numeroRata == 0) {
								dettPag.setNumeroBollettinoPagoPA(numeroBollettinoPagoPA);
								trovato = true;
							} else {
								for(Scadenza scadenza : in.getListScadenze()) {
									if(scadenza.getNumeroRata() == numeroRata) {
									    if(scadenza.getNumeroBollettinoPagoPA() != null) {
											dettPag.setNumeroBollettinoPagoPA(scadenza.getNumeroBollettinoPagoPA());
											trovato = true;
										}
										break;
									}
								}
							}
							if(!trovato) {		
								throw new ValidazioneException("Per DettaglioPagamento non si riesce a valorizzare il numero Bollettino!");
							}
						}
						
						// inizio SR PGNTACWS-5 arrotondamento importi
						Map<String, List<DettaglioPagamento>> groupedByNumeroAvviso = Arrays.asList(listPagamento).stream()
								.collect(Collectors.groupingBy(DettaglioPagamento::getNumeroBollettinoPagoPA));
						BigDecimal[] totRateDettagli = new BigDecimal[in.getListScadenze().length];
						
						for(Scadenza scadenza : Arrays.asList(in.getListScadenze())) {
							BigDecimal importo = groupedByNumeroAvviso.get(scadenza.getNumeroBollettinoPagoPA())
									.stream()
									.map(DettaglioPagamento::getImporto)
									.reduce(BigDecimal.ZERO, BigDecimal::add);
							totRateDettagli[scadenza.getNumeroRata()-1] = importo;
						}
						
						BigDecimal maxImportDett = Arrays.asList(totRateDettagli).stream().max(BigDecimal::compareTo).get();
						BigDecimal scartoDett = maxImportDett.subtract(this.maxImport);
						// fine SR PGNTACWS-5  
						
						for(DettaglioPagamento dettPag : listPagamento) {
							// inizio SR PGNTACWS-5 arrotondamento importi
							if(dettPag.getNumeroRata() > 0) {
								BigDecimal tot = totRateDettagli[dettPag.getNumeroRata()-1];							
								if(tot.compareTo(in.getListScadenze()[dettPag.getNumeroRata()-1].getImpBollettinoRata()) > 0) {
									dettPag.setImporto(dettPag.getImporto().subtract(scartoDett));
									totRateDettagli[dettPag.getNumeroRata()-1] = totRateDettagli[dettPag.getNumeroRata()-1].subtract(scartoDett);
								} else if(tot.compareTo(in.getListScadenze()[dettPag.getNumeroRata()-1].getImpBollettinoRata()) < 0) {
									BigDecimal diff = in.getListScadenze()[dettPag.getNumeroRata()-1].getImpBollettinoRata().subtract(tot);
									dettPag.setImporto(dettPag.getImporto().add(diff));
									totRateDettagli[dettPag.getNumeroRata()-1] = totRateDettagli[dettPag.getNumeroRata()-1].add(diff);
									scartoDett = scartoDett.subtract(diff);
								}
							}
							// fine SR PGNTACWS-5 arrotondamento importi
							
							logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEHD");											
							elaborazioneFlussiDao.doInsertEHD(progressivoFlusso, "EHD", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
									in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), 
									in.getImpostaServizio(), documento.getNumeroDocumento(),
									dettPag.getNumeroBollettinoPagoPA(),
									dettPag.getIdentificativoDominio(),
									GenericsDateNumbers.bigDecimalToDouble(dettPag.getImporto()),
									dettPag.getIBANBancario(), 
									(dettPag.getIBANPostale() != null && dettPag.getIBANPostale().trim().length() > 0 ? dettPag.getIBANPostale().trim() : ""),
									dettPag.getCodiceTipologiaServizio(), //LP PG22XX05
									'C',
									//inizio SB PAGONET-537
									dettPag.getMetadatiPagoPATariTefaKey(),
									dettPag.getMetadatiPagoPATariTefaValue()
									//fine SB PAGONET-537
									);
						}
					}
					
					//EHC dettaglio contabile
					if(listContabile != null && listContabile.length > 0) {
						for(DettaglioContabile dettCont : listContabile) {
							numeroRecord++;
							int numeroRata = dettCont.getNumeroRata();
							logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - dettCont numeroRata: " + numeroRata);
							boolean trovato = false;
							if(numeroRata == 0) {
								dettCont.setNumeroBollettinoPagoPA(numeroBollettinoPagoPA);
								trovato = true;
							} else {
								for(Scadenza scadenza : in.getListScadenze()) {
									if(scadenza.getNumeroRata() == numeroRata) {
									    if(scadenza.getNumeroBollettinoPagoPA() != null) {
											dettCont.setNumeroBollettinoPagoPA(scadenza.getNumeroBollettinoPagoPA());
											trovato = true;
										}
										break;
									}
								}
							}
							if(trovato) {
								logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEHC");
								elaborazioneFlussiDao.doInsertEHC(progressivoFlusso, "EHC", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
										in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), 
										in.getImpostaServizio(), documento.getNumeroDocumento(),
										dettCont.getNumeroBollettinoPagoPA(),
										dettCont.getNumeroTributo(),
										dettCont.getIdentificativoDominio(),
										dettCont.getCodiceContabilita(),
										GenericsDateNumbers.bigDecimalToDouble(dettCont.getImporto()),
										(dettCont.getCapitolo() != null && dettCont.getCapitolo().trim().length() > 0 ? dettCont.getCapitolo().trim() : null),
										(dettCont.getArticolo() != null && dettCont.getArticolo().trim().length() > 0 ? dettCont.getArticolo().trim() : null),
										(dettCont.getAnnoCompetenza() != null && dettCont.getAnnoCompetenza().trim().length() > 0 ? dettCont.getAnnoCompetenza().trim() : null),
										'C');
							} else {
								throw new ValidazioneException("Per DettaglioContabile non si riesce a valorizzare il numero Bollettino!");
							}
						}
					}
					//fine LP PG210130
				
					//EH6 Ruolo
					//inizio LP PG200360
					//numeroRecord++;
					//fine LP PG200360
					//Attenzione perch ruolo va in aggiornamento dato che non viene cancellato in quanto potrebbe essere associato ad altri documenti
					//Se esiste aggiorno, se non esiste inserisco
					logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - getRuolo impostaServizio: " + in.getImpostaServizio());
					ArchivioCarichiRuolo ruoloIn = prepareArchivioCarichiRuolo(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio());
					ArchivioCarichiRuolo ruoloOut = archivioCarichiDao.getRuolo(ruoloIn);
					if (ruoloOut.getProgressivoFlusso() == null) {
						//inizio LP PG200360
						numeroRecord++;
						//fine LP PG200360
						logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEH6");
						elaborazioneFlussiDao.doInsertEH6(progressivoFlusso, "EH6", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
								in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), 
								in.getImpostaServizio(), ruolo.getDescrizioneImpostaServizio(), 
								ruolo.getCodiceTipologiaServizio(), ruolo.getDescrizioneTipologiaServizio(), 'C');
					} else {
						if (!ruoloOut.getCodTipologiaServizio().equals(in.getRuolo().getCodiceTipologiaServizio())) {
							throw new ValidazioneException("codiceTipologiaServizio pervenuto differente dal codice presente in archivio per stessa imposta servizio");
						}
						logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - updateRuolo");
						ruoloOut.setDescImpostaServizio(ruolo.getDescrizioneImpostaServizio());
						ruoloOut.setDescTipologiaServizio(ruolo.getDescrizioneTipologiaServizio());
						archivioCarichiDao.updateRuolo(ruoloOut);
					}
				
					//EH7 Tributi 
					if (in.getListTributi()!=null && in.getListTributi().length>0) {
						for(Tributo tributo : in.getListTributi()) {
							numeroRecord++;
							logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - getTributo codice/anno/progressivo tributo: " + tributo.getCodiceTributo()+"/"+tributo.getAnnoTributo()+"/"+tributo.getProgressivoTributo());
							//ArchivioCarichiTributo tribIn = prepareArchivioCarichiTributo(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), documento.getNumeroDocumento(), tributo.getCodiceTributo(), tributo.getAnnoTributo(), tributo.getProgressivoTributo());
							//ArchivioCarichiTributo tribOut = archivioCarichiDao.getTributo(tribIn);
								String noteTributo = tributo.getNoteTributo()==null?"":tributo.getNoteTributo();
								//inizio LP - mail Giorgia 20200608
								String codiceCapitolo = tributo.getCodiceCapitolo()==null?"":tributo.getCodiceCapitolo();
								String accertamento = tributo.getAccertamento()==null?"":tributo.getAccertamento();
								//inizio LP PG210130
								String articolo = tributo.getArticolo() == null ? "" : tributo.getArticolo();
								String idDominio = tributo.getIdentificativoDominio() == null ? "": tributo.getIdentificativoDominio();
								String ibanBancario = tributo.getIBANBancario() == null ? "": tributo.getIBANBancario();
								String ibanPostale = tributo.getIBANPostale() == null ? "": tributo.getIBANPostale();
								//fine LP PG210130
								//inizio LP PG22XX05
								String codiceTipologiaServizio = tributo.getCodiceTipologiaServizio() == null ? "" :  tributo.getCodiceTipologiaServizio();
								//fine LP PG22XX05
								
								//inizio SB PAGONET-537
								String metadatiPagoPATariTefaKey = tributo.getMetadatiPagoPATariTefaKey() == null ? "" : tributo.getMetadatiPagoPATariTefaKey();
								String metadatiPagoPATariTefaValue = tributo.getMetadatiPagoPATariTefaValue() == null ? "" : tributo.getMetadatiPagoPATariTefaValue();
								//fine SB PAGONET-537

								logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEH7");
								/*
								elaborazioneFlussiDao.doInsertEH7(progressivoFlusso, "EH7", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
										in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), documento.getNumeroDocumento(), 
										tributo.getCodiceTributo(), tributo.getAnnoTributo(), tributo.getProgressivoTributo(), 
										"", //TODO da verificare se I(imposta) o S(sanzioni) o T(interessi) o A(altro) o V(interessi di MR) 
										GenericsDateNumbers.bigDecimalToDouble(tributo.getImpTributo()), 0, noteTributo, 'C', "","");
								*/
								//inizio LP PG210130
								//elaborazioneFlussiDao.doInsertEH7(progressivoFlusso, "EH7", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
								//		in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), documento.getNumeroDocumento(), 
								//		tributo.getCodiceTributo(), tributo.getAnnoTributo(), tributo.getProgressivoTributo(), 
								//		"", //TODO da verificare se I(imposta) o S(sanzioni) o T(interessi) o A(altro) o V(interessi di MR) 
								//		GenericsDateNumbers.bigDecimalToDouble(tributo.getImpTributo()), 0, noteTributo, 'C', codiceCapitolo, accertamento);
								elaborazioneFlussiDao.doInsertEH7(progressivoFlusso, "EH7", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
										in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), documento.getNumeroDocumento(), 
										tributo.getCodiceTributo(), tributo.getAnnoTributo(), tributo.getProgressivoTributo(), 
										"", //TODO da verificare se I(imposta) o S(sanzioni) o T(interessi) o A(altro) o V(interessi di MR) 
										GenericsDateNumbers.bigDecimalToDouble(tributo.getImpTributo()), 0, noteTributo, 'C', codiceCapitolo, accertamento
										//inizio LP PG22XX05
										//, articolo, idDominio, ibanBancario, ibanPostale);
										, articolo, idDominio, ibanBancario, ibanPostale
										, codiceTipologiaServizio
										, metadatiPagoPATariTefaKey, metadatiPagoPATariTefaValue //PAGONET-537
										);
										//fine LP PG22XX05
								//fine LP PG210130
								//fine LP - mail Giorgia 20200608
						}
					}
				
					//EH8 Anagrafica 
					//inizio LP PG200360
					//numeroRecord++;
					//fine LP PG200360
					//Attenzione perch anagrafica va in aggiornamento dato che non viene cancellata in quanto potrebbe essere associata ad altri documenti
					//Se esiste aggiorno, se non esiste inserisco
					logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - getAnagrafica codiceFiscale: " + anagrafica.getCodiceFiscale().toUpperCase());
					ArchivioCarichiAnagrafica anaIn = prepareArchivioCarichiAnagrafica(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), anagrafica.getCodiceFiscale().toUpperCase());
					ArchivioCarichiAnagrafica anaOut = archivioCarichiDao.getAnagrafica(anaIn);
					String dataNascita = GenericsDateNumbers.formatData(anagrafica.getDataNascita(),"dd/MM/yyyy","yyyy-MM-dd"); 
					if (dataNascita.equals(""))
						dataNascita = "1900-01-01";
					String codiceBelfioreComuneNascita = anagrafica.getCodiceBelfioreComuneNascita()==null?"":anagrafica.getCodiceBelfioreComuneNascita();
					String email = anagrafica.getEmail()==null?"":anagrafica.getEmail();
					String emailPec = anagrafica.getEmailPec()==null?"":anagrafica.getEmailPec();
					if (anaOut.getProgressivoFlusso() == null) {
						logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEH8");
						//inizio LP PG200360
						numeroRecord++;
						//fine LP PG200360
						
						logger.debug("progressivoFlusso " + progressivoFlusso);
						logger.debug("codice utente " + in.getCodiceUtente());
						logger.debug("data flusso" + java.sql.Date.valueOf(dataFlusso));
						logger.debug("tipo servizio " + in.getTipoServizio());
						logger.debug("cod ente " +in.getCodiceEnte());
						logger.debug("imposta servizio " + in.getImpostaServizio());
						logger.debug("cf " + anagrafica.getCodiceFiscale().toUpperCase());
						logger.debug("denom " + anagrafica.getDenominazione());
						logger.debug("tipo anag " + anagrafica.getTipoAnagrafica());
						logger.debug("comune " + codiceBelfioreComuneNascita);
						logger.debug("nascita " + java.sql.Date.valueOf(dataNascita));
						logger.debug("indirizzo " +  anagrafica.getIndirizzoFiscale());
						logger.debug("belfiore " + anagrafica.getCodiceBelfioreComuneFiscale());
						logger.debug("email " + email);
						logger.debug("emailPec " + emailPec);
						
						
						elaborazioneFlussiDao.doInsertEH8(progressivoFlusso, "EH8", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), in.getTipoServizio(), 
								in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), 
								anagrafica.getCodiceFiscale().toUpperCase(), anagrafica.getDenominazione(), anagrafica.getTipoAnagrafica(),
								codiceBelfioreComuneNascita, 
								java.sql.Date.valueOf(dataNascita), "", anagrafica.getIndirizzoFiscale(), anagrafica.getCodiceBelfioreComuneFiscale(), 'C', email, emailPec);
					} else {
						anaOut.setDenominazione(anagrafica.getDenominazione());
						anaOut.setTipoAnagrafica(anagrafica.getTipoAnagrafica());
						anaOut.setCodiceBelfioreComuneNascita(anagrafica.getCodiceBelfioreComuneNascita());
						anaOut.setDataNascita(java.sql.Date.valueOf(dataNascita));
						anaOut.setIndirizzoFiscale(anagrafica.getIndirizzoFiscale());
						anaOut.setCodiceBelfioreComuneFiscale(anagrafica.getCodiceBelfioreComuneFiscale());
						anaOut.setEmail(anagrafica.getEmail());
						anaOut.setEmailPec(anagrafica.getEmailPec());
						logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - updateAnagrafica");
						archivioCarichiDao.updateAnagrafica(anaOut);
					}
					
					//EH0 Testa
					String idDominio = "";
					String auxDigit = "";
					String applicationCode = "";
					String segregationeCode = "";
					String carattereServizio = "";
					if (configurazione.getConfigurazioneIUV()!=null) {
						ConfigurazioneIUV configIUV = configurazione.getConfigurazioneIUV();
						if (configIUV.getIdentificativoDominio()!=null) idDominio = configIUV.getIdentificativoDominio();
						if (configIUV.getAuxDigit()!=null) auxDigit = configIUV.getAuxDigit();
						if (configIUV.getApplicationCode()!=null) applicationCode = configIUV.getApplicationCode();
						if (configIUV.getSegregationCode()!=null) segregationeCode = configIUV.getSegregationCode();
						if (configIUV.getCarattereServizio()!=null) carattereServizio = configIUV.getCarattereServizio();
					}
							
					//Inserisco su EH0 solo se l'identificativo flusso o non  presente o non  stato elaborato
					ArchivioCarichiTesta testaIn = prepareArchivioCarichiTesta(progressivoFlusso, in.getCodiceUtente());
					ArchivioCarichiTesta testaOut = archivioCarichiDao.getTesta(testaIn);				
					if (testaOut.getTipoRecord()==null) {
						numeroRecord++;
						logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEH0");
						elaborazioneFlussiDao.doInsertEH0(progressivoFlusso, "EH0", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), 
								"PER", //procedura di gestione 
								in.getTipoServizio(),
								'C',
								configurazione.getFlagStampaAvviso(),
								configurazione.getFlagGenerazioneIUV(),
								idDominio,
								auxDigit,
								applicationCode,
								segregationeCode,
								carattereServizio //PG200140
								);
					} else {	//TODO da valutare il da farsi nel caso di EH0 gi esistente
						testaOut.setCodiceUtente(in.getCodiceUtente());
						testaOut.setDataCreazioneFlusso(java.sql.Date.valueOf(dataFlusso));
						archivioCarichiDao.updateTesta(testaOut);
					}
					
					
					//EH9 Coda
					//Inserisco su EH9 solo se l'identificativo flusso o non  presente o non  stato elaborato
					ArchivioCarichiCoda codaIn = prepareArchivioCarichiCoda(progressivoFlusso, in.getCodiceUtente());
					ArchivioCarichiCoda codaOut = archivioCarichiDao.getCoda(codaIn);
					if (codaOut.getTipoRecord()==null) {
						numeroRecord++;
						logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - doInsertEH9");
						elaborazioneFlussiDao.doInsertEH9(progressivoFlusso, "EH9", in.getCodiceUtente(), java.sql.Date.valueOf(dataFlusso), in.getTipoServizio(), numeroRecord, 'C');
					} else {	//TODO da valutare il da farsi nel caso di EH9 gi esistente
						codaOut.setCodiceUtente(in.getCodiceUtente());
						codaOut.setDataCreazioneFlusso(java.sql.Date.valueOf(dataFlusso));
						//inizio LP PG200360
						//NOTA. Non era giusto in caso di variazione aggiornare il numero di record.
						//      Adesso che nell'operazione di cancellazione si aggiorna nel record
						//      di coda il numero record lo si pu fare.
						//fine LP PG200360
						codaOut.setNumeroRecordFlusso(codaOut.getNumeroRecordFlusso()+numeroRecord);	//TODO da verificare
						archivioCarichiDao.updateCoda(codaOut);
					}
					
					flagSuccessfullExecution = true;
	        		//inizio LP PG200070
					//facade.saveLogFlussi(progressivoFlusso, null, null, -1, null, null, null, null, new Timestamp(System.currentTimeMillis()), "N", null, dbSchemaCodSocieta);
					saveLogFlussi(elaborazioneFlussiDao, progressivoFlusso, null, null, -1, null, null, null, null,
							      new Timestamp(System.currentTimeMillis()), "N", null, dbSchemaCodSocieta);
	        		//inizio LP PG200070
					response.setCodiceEsito("00");
					response.setMessaggioEsito("Richiesta eseguita con successo");
				} catch(Exception ex) {
					if (checkDuplicate) 
						throw new DuplicateException(ex.getMessage());
					else {
						if (!flagSuccessfullExecution) {
							archivioCarichiDao.doRollbackArchivioCarichi(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), documento.getNumeroDocumento());
							archivioCarichiDao.doRollbackArchivioCarichiELG(fileNameToElab, progressivoFlusso);
						}
						throw ex;
					}
				}
        	}
        	
        	// SR PGNTACWS-2 inizio
			// SE ARRIVO QUA, L'INSERIMENTO SU PAGONET E' OK 
        	if(propertiesTree().getProperty(PropKeys.servizioJppa.format()) != null && propertiesTree().getProperty(PropKeys.servizioJppa.format()).equals("Y")) {
        		InviaDovutiDao dao = new InviaDovutiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
    			String codiceIpaComune = "";
    			String idDominioComune = in.getConfigurazione().getConfigurazioneIUV().getIdentificativoDominio();
    			codiceIpaComune = dao.getCodiceIpa(idDominioComune, in.getCodiceEnte());
    			
    			CaricaDebitiJppa jppa = new CaricaDebitiJppa();
    			String token = jppa.login(propertiesTree().getProperty(PropKeys.username.format()), propertiesTree().getProperty(PropKeys.password.format()), codiceIpaComune);
    			
    			if (codiceIpaComune.equals("")) {
    				System.err.println("Codice ipa non trovato per id dominio: " + in.getListTributi(0).getIdentificativoDominio() + "ed ente: " + in.getCodiceEnte() + ". Impossibile inviare dovuto.");
    			} else {
    				
    				this.estrattoContoDao = new EstrattoContoDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
    	
    				String listXml = "";		
    				String[] sEsito = estrattoContoDao.doCachedRowSetList(in.getCodiceUtente(), in.getCodiceEnte(), "", "",
    						in.getAnagrafica().getCodiceFiscale(), in.getTipoServizio(), "I", "", "", in.getDocumento().getNumeroDocumento(), "", "D", "", "",
    						BigDecimal.ZERO, "", "", "", "", BigDecimal.ZERO, "",
    						BigDecimal.ZERO, "", "");
    				
    				listXml = estrattoContoDao.getWebRowSetXml(EstrattoContoDao.IDX_DOLIST_LISTA);
    				if (listXml.length() != 0) {
    					ecCached = Convert.stringToWebRowSet(listXml);
    					if (ecCached.size() > 0) {
    						boolean bFoundResult = false;
    						while (ecCached.next() && !bFoundResult) {
    							String cittaEstesa = ecCached.getString(53)
    									+ (ecCached.getString(53) != null && ecCached.getString(53).trim().length() > 0 ? " " : "")
    									+ ecCached.getString(52)
    									+ (ecCached.getString(54) != null && ecCached.getString(54).trim().length() > 0 ? " (" + ecCached.getString(54) + ")" : "");
    							TipoBollettino tipoBollettino = new TipoBollettino(
    									"", 
    									ecCached.getString(10), 
    									ecCached.getString(11), 
    									"", 
    									"00010101",
    									ecCached.getString(22), 
    									ecCached.getString(5), 
    									ecCached.getString(51), 
    									cittaEstesa, 
    									"",
    									ecCached.getString(48), 
    									null, null);

    							pgResponse = new RecuperaDatiBollettinoResponse(
    									in.getCodiceUtente(), 
    									in.getCodiceEnte(),
    									in.getTipoUfficio(), 
    									in.getCodiceUfficio(), 
    									"", 
    									in.getRuolo().getCodiceTipologiaServizio(),
    									ecCached.getString(9), 
    									ecCached.getString(21), 
    									ecCached.getBigDecimal(47) == null ? 0 : ecCached.getBigDecimal(47).multiply(new BigDecimal(100)).longValue(), 
    									"",
    									"",
    									0, 
    									"",
    									0, 
    									"",
    									getCurrentDate(), 
    									isValidResult(sEsito) ? "00" : "01", 
    									sEsito[1], 
    									tipoBollettino, 
    									in.getDocumento().getIdentificativoUnivocoVersamento(),
    									ecCached.getString(50));

    							pgResponse.setTassonomia(ecCached.getString(57) == null ? "" : ecCached.getString(57));

    							try {
    								listXmlDP = estrattoContoDao.getWebRowSetXml("1"); // datiPagamento
    							} catch (Exception e) {}
    							if (listXmlDP != null && listXmlDP.length() > 0)
    								try {
    									ecCachedDettaglioPagamento = Convert.stringToWebRowSet(listXmlDP);
    								} catch (SQLException e) {}
    							try {
    								listXmlDC = estrattoContoDao.getWebRowSetXml("2"); // datiContabili
    							} catch (Exception e) {}
    							if (listXmlDC != null && listXmlDC.length() > 0) {
    								try {
    									ecCachedDettaglioContabile = Convert.stringToWebRowSet(listXmlDC);
    								} catch (SQLException e) {}

    								extendDatiContabileEPagamento(pgResponse, ecCached, ecCachedDettaglioPagamento,
    										ecCachedDettaglioContabile,
    										in.getConfigurazione().getConfigurazioneIUV().getIdentificativoDominio(), 0);
    								bFoundResult = true;
    							}
    						}
    						
    						String codiceIpaProvincia = "";
    						if(Boolean.TRUE.equals(pgResponse.getFlagMultiBeneficiario())) {
    							String idDominioProvincia = Arrays.asList(in.getListTributi())
    									.stream()
    									.filter(t -> !t.getIdentificativoDominio().equals(idDominioComune))
    									.findFirst()
    									.get()
    									.getIdentificativoDominio();
    							Ente ente = enteDato.doDetailToCodFis("", idDominioProvincia,"");
    							codiceIpaProvincia = ente.getCodIpaEnte();
    						}
    						
    						List<DovutoDto> dovutiList = new ArrayList<>();
    						
							ContribuenteDto contribuente = new ContribuenteDto(
									"", // cap
									"", // civico
									pgResponse.getAnagraficaBollettino().getCodiceFiscale_PIVA().toUpperCase(), 
									pgResponse.getAnagraficaBollettino().getCodiceFiscale_PIVA().length() >= 16 ? anagrafica.getDenominazione() : null, // cognome=nome+cognome
									anagrafica.getEmail() != null ? anagrafica.getEmail() : null, // email
									anagrafica.getIndirizzoFiscale() != null ? anagrafica.getIndirizzoFiscale() : null, // indirizzo
									"", // localita
									"", // nazione
									"", // nome
									"", // provincia
									pgResponse.getAnagraficaBollettino().getCodiceFiscale_PIVA().length() < 16 ? anagrafica.getDenominazione() : null, // ragioneSociale
									pgResponse.getAnagraficaBollettino().getCodiceFiscale_PIVA().length() < 16 ? TipoIdentificativoUnivocoEnum.GIURID : TipoIdentificativoUnivocoEnum.FIS 
									);
													
    						// Se il bollettino è multirata, invio le rate come dovuti separati
    						if (in.getListScadenze().length > 1) {
    							int progressivo = 1;
    							for(Scadenza scadenza : in.getListScadenze()) {		
    								String iuvScadenza = scadenza.getIdentificativoUnivocoVersamento() != null && !scadenza.getIdentificativoUnivocoVersamento().isEmpty() ? scadenza.getIdentificativoUnivocoVersamento() : scadenza.getNumeroBollettinoPagoPA().substring(1);
    								DatoAccertamentoDto datoAccertamento = new DatoAccertamentoDto(
											pgResponse.getAnagraficaBollettino().getAnnoDocumento(), 
											CodiceTipoDebitoEnum.fromValue(pgResponse.getTipologiaServizio()).getValue(), 
											"", // descrizione
											BigDecimal.valueOf(scadenza.getImpBollettinoRata().doubleValue() / 100D)); 
									
									DettaglioDovutoDto dettaglio = new DettaglioDovutoDto(
											pgResponse.getCausale() + ", rata " + scadenza.getNumeroRata(),
											Boolean.TRUE.equals(pgResponse.getFlagMultiBeneficiario()) ? (progressivo == 1 ? codiceIpaComune : codiceIpaProvincia) : codiceIpaComune, 
											"codiceLotto",
											CodiceTipoDebitoEnum.fromValue(pgResponse.getTipologiaServizio()),
											OffsetDateTime.parse(new Date(scadenza.getDataScadenzaRata()).toInstant().atOffset(ZoneOffset.UTC).toString(),DateTimeFormatter.ISO_OFFSET_DATE_TIME),
											OffsetDateTime.parse(Calendar.getInstance().getTime().toInstant().atOffset(ZoneOffset.UTC).toString(),DateTimeFormatter.ISO_OFFSET_DATE_TIME),
											null,
											Arrays.asList(datoAccertamento),
											"multirata",
											idDominioComune+"_"+iuvScadenza,
											scadenza.getImpBollettinoRata().doubleValue() / 100D,
											null, // BigDecimal importo spese di notifica
											null, // MarcaDaBolloDto									
											progressivo,	
											null, // List<ParametroDebitoDto>
											SpeseNotificaDaAttualizzareEnum.OFF  // SpeseNotificaDaAttualizzareEnum
											);								
	    							
	    							NumeroAvvisoDto numeroAvvisoDto = new NumeroAvvisoDto(true, scadenza.getNumeroBollettinoPagoPA(), 1);

	    							TestataDovutoDto testata = new TestataDovutoDto(
	    									contribuente, 
	    									documento.getCausale(), 
	    									idDominioComune+"_"+iuvScadenza);
	    							
	    							DovutoDto dovuto = new DovutoDto(
	    									Boolean.TRUE.equals(pgResponse.getFlagMultiBeneficiario()) ? ContestoDovutoEnum.MULTIBENEFICIARIO : ContestoDovutoEnum.MONOBENEFICIARIO,
	    									Arrays.asList(dettaglio),
	    									numeroAvvisoDto, 
	    									testata);   						
	    							dovutiList.add(dovuto);
    								progressivo++;							
    							}
    						} 
    						    					
    						// Se il bollettino è multirata o se è soluzione unica, invio un dovuto con l'importo totale.    						
    						List<DettaglioDovutoDto> dettaglioList = new ArrayList<>();
							int progressivo = 1;
    						for(Tributo tributo : Arrays.asList(in.getListTributi())){
    							
								DatoAccertamentoDto datoAccertamento = new DatoAccertamentoDto(
										tributo.getAnnoTributo(), 
										CodiceTipoDebitoEnum.fromValue(pgResponse.getTipologiaServizio()).getValue(), 
										"", // descrizione
										BigDecimal.valueOf(tributo.getImpTributo().doubleValue() / 100D)); 
								
								DettaglioDovutoDto dettaglio = new DettaglioDovutoDto(
										pgResponse.getCausale(),
										Boolean.TRUE.equals(pgResponse.getFlagMultiBeneficiario()) ? (progressivo == 1 ? codiceIpaComune : codiceIpaProvincia) : codiceIpaComune, 
										"codiceLotto",
										CodiceTipoDebitoEnum.fromValue(pgResponse.getTipologiaServizio()),
										null,
										OffsetDateTime.parse(Calendar.getInstance().getTime().toInstant().atOffset(ZoneOffset.UTC).toString(),DateTimeFormatter.ISO_OFFSET_DATE_TIME),
										null,
										Arrays.asList(datoAccertamento),
										"unica",
										idDominioComune+"_"+pgResponse.getIdentificativoUnivocoVersamento(),
										tributo.getImpTributo().doubleValue() / 100D,
										null, // BigDecimal importo spese di notifica
										null, // MarcaDaBolloDto									
										progressivo,	
										null, // List<ParametroDebitoDto>
										SpeseNotificaDaAttualizzareEnum.OFF  // SpeseNotificaDaAttualizzareEnum
										);				
											
								progressivo++;
								dettaglioList.add(dettaglio);
    						}
    								
							NumeroAvvisoDto numeroAvvisoDto = new NumeroAvvisoDto(true, pgResponse.getIdentificativoBollettino(), 1);
							TestataDovutoDto testata = new TestataDovutoDto(
									contribuente, 
									documento.getCausale(), 
									idDominioComune+"_"+pgResponse.getIdentificativoUnivocoVersamento());
							
							DovutoDto dovuto = new DovutoDto(
									Boolean.TRUE.equals(pgResponse.getFlagMultiBeneficiario()) ? ContestoDovutoEnum.MULTIBENEFICIARIO : ContestoDovutoEnum.MONOBENEFICIARIO,
									dettaglioList, 
									numeroAvvisoDto, 
									testata);   						
							
    						dovutiList.add(dovuto);
    					    						
    						RispostaInviaDovutiDto res = jppa.inviaDovuti(token, codiceIpaComune, dovutiList); 
    						if(res != null) {
    							dao.aggiornaFlagInviaDovuto(progressivoFlussoPerInviaDovuti, getSchemaDifferito(dbSchemaCodSocieta)); 							
    						}
    					}
    				}				
    				// SR PGNTACWS-2 fine
    			}
        	}
        } catch (ConfigurazioneException e) {	
			error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC failed, configuration error due to: ", e);
			response.setCodiceEsito("02");
			response.setMessaggioEsito(e.getMessage());	//"Configurazione errata"
        } catch (ValidazioneException e) {
			error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC failed, validation error due to: ", e);
			response.setCodiceEsito("04");
			response.setMessaggioEsito(e.getMessage());	//"Errore di validazione"
        } catch (DuplicateException e) {
			error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC failed, duplication error due to: ", e);
			response.setCodiceEsito("03");
			response.setMessaggioEsito(e.getMessage());	//"Posizione debitoria già presente"
        } catch (Exception e) {
			error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC failed, generic error due to: ", e);
			response.setCodiceEsito("01");
			response.setMessaggioEsito("Errore generico");
			e.printStackTrace();	
			
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
	    	if(response != null) {
	    		//inizio LP PG210130
	    		sw = new StringWriter();
	    		//fine LP PG210130
				JAXB.marshal(response, sw);
				xmlString = sw.toString();
				//inizio LP PG22XX05
				try {
					sw.close();
				} catch (IOException e1) {
				}
				//fine LP PG22XX05
				logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - response: " + xmlString);
	    	}
		}
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inserimentoEC - fine");
		return response;
    }

	//inizio LP PG200360
    @Override
	@SuppressWarnings("unused")
	//fine LP PG200360
	public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcResponse variazioneEC(com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcRequest in) throws java.rmi.RemoteException, com.esed.payer.archiviocarichi.webservice.srv.FaultType {
    	ClearVariazioneEC(in); //LP PG22XX05
    	logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - inizio");
    	VariazioneEcResponse response = new VariazioneEcResponse(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), "", "", in.getDocumento());
    	Connection connection = null;
    	StringWriter sw = new StringWriter();
		JAXB.marshal(in, sw);
		String xmlString = sw.toString();
		//inizio LP PG22XX05
		try {
			sw.close();
		} catch (IOException e1) {
		}
		sw = null;
		//fine LP PG22XX05
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - request: " + xmlString);
    	try {
    		dbSchemaCodSocieta = in.getCodiceUtente();
    		connection = getConnectionDifferito(dbSchemaCodSocieta);
        	archivioCarichiDao = new ArchivioCarichiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
        	configPagamentoDao = new ConfigPagamentoDao(connection, getSchemaDifferito(dbSchemaCodSocieta)); //LP PG22XX05
        	enteDato = new EnteDao(connection, getSchemaDifferito(dbSchemaCodSocieta)); //LP PG22XX05        	
        	//inizio LP PG200360
        	boolean bModalitaAgggiornamento = false;
			boolean bFlagStampaAvvisoVar = false;
			boolean bTassonomiaVar = false;
			boolean bImportoVar = false;
			boolean bScadenzeVar = false;
			boolean bTributiVar = false;
        	String flagElabStampaAvviso = archivioCarichiDao.getFlagElabStampaAvviso(in.getConfigurazione().getIdentificativoFlusso());
        	boolean bStampaAvvisoEseguita = (!flagElabStampaAvviso.equals("") && !flagElabStampaAvviso.equals("N"));
			String flagStampaAvviso = in.getConfigurazione().getFlagStampaAvviso().trim();
			boolean bFlagStampaAvvisoNo = (flagStampaAvviso.length() == 0 || flagStampaAvviso.equalsIgnoreCase("N"));
			bModalitaAgggiornamento = bStampaAvvisoEseguita && bFlagStampaAvvisoNo; 
			
        	if(bModalitaAgggiornamento) {
    			logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - documento con modalit 'cancellazione-inserimento con modifica solo tassonomia e/o importi'");
        		String codiceSocieta = "";
				String chiaveFlusso = "";
				String codiceTipoServizio = "EP";
				String codiceUtenteEnte = in.getCodiceEnte();
				String chiaveTomb = "C";
				DocumentoCarico docInDB = readDocumentoCarico(in.getCodiceUtente(),
						                                      in.getCodiceEnte(),
						                                      in.getTipoUfficio(),
						                                      in.getCodiceUfficio(),
						                                      codiceSocieta,
						                                      in.getDocumento().getNumeroDocumento(),
						                                      chiaveFlusso,
						                                      codiceTipoServizio,
						                                      codiceUtenteEnte,
						                                      in.getImpostaServizio(),
						                                      chiaveTomb);
				Documento documentoDB = docInDB.getDocumento();
				Documento documentoEx = in.getDocumento();
				BigDecimal oldImp = documentoDB.getImpBollettinoTotaleDocumento();
				BigDecimal newImp = documentoEx.getImpBollettinoTotaleDocumento();
				String oldTassonomia = documentoDB.getTassonomia();
				String newTassonomia = documentoEx.getTassonomia();
				if(!oldTassonomia.equalsIgnoreCase(newTassonomia) ) {
					bTassonomiaVar = true;
					documentoDB.setTassonomia(newTassonomia);
				}
				if(oldImp.compareTo(newImp) != 0) {
					bImportoVar = true;
				}
				documentoDB.setImpBollettinoTotaleDocumento(newImp);
    			logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - controllo su variazioni consentite");
				if(!documentoDB.equals(documentoEx)) {
					throw new ValidazioneException("Richiesta non ammessa ci sono modifiche non consentite sul documento");						
				}
				Ruolo ruoloDB = docInDB.getRuolo();
				Ruolo ruoloEx = in.getRuolo();
				if(!ruoloDB.equals(ruoloEx)) {
					throw new ValidazioneException("Richiesta non ammessa ci sono modifiche non consentite sul ruolo");						
				}
				Anagrafica anagraficaDB = docInDB.getAnagrafica();
				Anagrafica anagraficaEx = in.getAnagrafica();
				if(!anagraficaDB.equals(anagraficaEx)) {
					throw new ValidazioneException("Richiesta non ammessa ci sono modifiche non consentite sulla anagrafica");
				}
				Configurazione configurazioneDB = docInDB.getConfigurazione();
				Configurazione configurazioneEx = in.getConfigurazione();
				if(configurazioneEx.getFlagGenerazioneIUV().length() == 0) {
					configurazioneEx.setFlagGenerazioneIUV("N");
				}
				if(configurazioneEx.getFlagStampaAvviso().length() == 0) {
					configurazioneEx.setFlagStampaAvviso("N");
				}
				if(!configurazioneDB.getFlagStampaAvviso().equalsIgnoreCase(configurazioneEx.getFlagStampaAvviso())) {
					bFlagStampaAvvisoVar = true;
					configurazioneDB.setFlagStampaAvviso(configurazioneEx.getFlagStampaAvviso());
				}
				/*
				//NOTA: Se non si consente la modifica della configurazioneIUV allora 
				//      si potrebbe evitare il confronto assegnando il valore sul DB
				boolean bByPassConforntoConfigurazioneIUV = false;
				if(bByPassConforntoConfigurazioneIUV) {
					configurazioneEx.setConfigurazioneIUV(configurazioneDB.getConfigurazioneIUV());
				}
				*/
				if(!configurazioneDB.equals(configurazioneEx)) {
					throw new ValidazioneException("Richiesta non ammessa ci sono modifiche non consentite sulla configurazione");
				}
				/*
				//NOTA: in checkRequest si puo' imporre il test che i "iuv" non siano gia' presenti
				configurazioneEx.setFlagGenerazioneIUV("N");
				*/
				Scadenza[] scadenzeDB = docInDB.getListScadenze();
				Scadenza[] scadenzeEX = in.getListScadenze();
				if(scadenzeDB.length != scadenzeEX.length) {
					throw new ValidazioneException("Richiesta non ammessa ci sono modifiche non consentite sulle scadenze (cardinalita' diversa)");
				}
				for(int k= 0; k < scadenzeDB.length; k++) {
					BigDecimal oldImpS = scadenzeDB[k].getImpBollettinoRata();
					BigDecimal newImpS = scadenzeEX[k].getImpBollettinoRata();
					if(oldImpS.compareTo(newImpS) != 0) {
						bScadenzeVar = true;
					}
					scadenzeDB[k].setImpBollettinoRata(newImpS);
					if(!scadenzeDB[k].equals(scadenzeEX[k])) {
						throw new ValidazioneException("Richiesta non ammessa ci sono modifiche non consentite sulle scadenze (attributi variati)");
					}
				}
				Tributo[] tributiDB = docInDB.getListTributi();
				Tributo[] tributiEX = in.getListTributi();
				if(tributiDB.length != tributiEX.length) {
					throw new ValidazioneException("Richiesta non ammessa ci sono modifiche non consentite sui tributi (cardinalita' diversa)");
				}
				for(int k= 0; k < tributiDB.length; k++) {
					BigDecimal oldImpS = tributiDB[k].getImpTributo();
					BigDecimal newImpS = tributiEX[k].getImpTributo();
					if(oldImpS.compareTo(newImpS) != 0) {
						bTributiVar = true;
					}
					tributiDB[k].setImpTributo(newImpS);
					if(!tributiDB[k].equals(tributiEX[k])) {
						throw new ValidazioneException("Richiesta non ammessa ci sono modifiche non consentite sui tributi (attributi variati)");
					}
				}
        		//NOTA. si procede anche in mancaza di modifiche
        	} else if(bStampaAvvisoEseguita) {
    			logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - documento con modalit 'cancellazione-inserimento con modifiche varie e nuovo idflusso'");
        		//NOTA. si procede anche in mancaza di modifiche
        	} else {
    			logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - documento con modalit 'cancellazione-inserimento' standard");
        		//NOTA. si procede anche in mancaza di modifiche
        	}
        	//fine LP PG200360
	    	        	
        	//Verifica input
			logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - checkRequest");
        	boolean resCheck = checkRequest(in.getCodiceUtente(), in.getCodiceEnte(), in.getConfigurazione(), in.getDocumento(), in.getListScadenze(), in.getListTributi(), in.getAnagrafica() , "V");
        	if (resCheck) {
				//inizio LP PG210130
				//inizio LP PG210130 Step03
        		//inizio LP Abilitazione Dati Multi Beneficiario per Tutti i CuteCute - 20211126
				//if(in.getCodiceUtente().equals("000RM")
				//   || in.getCodiceUtente().equals("000LP")
				if(true
				//fine LP Abilitazione Dati Multi Beneficiario per Tutti i CuteCute - 20211126
				  ) {
				//fine LP PG210130 Step03
				//inizio LP PG22XX05
				//Map<String, List<Tributo>> listDocTrib = otherCheck(in.getCodiceUtente(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getConfigurazione(), in.getDocumento(), in.getListScadenze(), in.getListTributi());
				Map<String, List<Tributo>> listDocTrib = otherCheck(in.getCodiceUtente(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getConfigurazione(), in.getDocumento(), in.getListScadenze(), in.getListTributi(), in.getRuolo().getCodiceTipologiaServizio());
				//fine LP PG22XX05
				//inizio LP PG210130 Step03
				}
				//fine LP PG210130 Step03
        		//29012021 GG - inserimento verifica su stampa avviata - inizio
            	//Non  ammessa la variazione se il documento  su un flusso in stampa o stampato
            	//inizio LP PG200360
            	//ArchivioCarichiDocumento docIn = prepareArchivioCarichiDocumento(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), in.getDocumento().getNumeroDocumento());
            	//String flagStampa = archivioCarichiDao.getFlagStampa(docIn);
            	//if (!flagStampa.equals("") && !flagStampa.equals("N")) {
            	//        			throw new ValidazioneException("Variazione non ammessa per fase di stampa avviata");
            	//}
        		////29012021 GG - inserimento verifica su stampa avviata - fine
            	//fine LP PG200360
        		
        		//Calendar calCurrentDate = Calendar.getInstance();
				//String dataFlusso = GenericsDateNumbers.calendarToString(calCurrentDate, "yyyy-MM-dd");		//data flusso nel formato AAAA-MM-GG
				//String fileNameToElab = "AC" + in.getCodiceUtente() + GenericsDateNumbers.calendarToString(calCurrentDate, "yyyyMMddHHmmssSSS") + ".txt";	//maxlength 50
        		
        		//Dicembre 2020 - Controllo per inserimento idFlusso su enti diversi - TK: 2020121088000104 
        		//Aggiungo il flag "_v" al nome del flusso per distinguere la variazione
				String fileNameToElab =  in.getConfigurazione().getIdentificativoFlusso() + "_v";
				
        		in.getConfigurazione().setIdentificativoFlusso(fileNameToElab);
	        	//Nel caso risultino discarichi per il documento non sono ammesse variazioni 
		    	ArchivioCarichiMovimento movIn = prepareArchivioCarichiMovimento(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), in.getDocumento().getNumeroDocumento(), 0, "D");
		    	ArchivioCarichiMovimento movOut = archivioCarichiDao.getMovimento(movIn);
		    	if (movOut.getProgressivoFlusso() != null) {
		    		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC non ammessa per presenza discarichi su posizione debitoria");
					response.setCodiceEsito("07");
					response.setMessaggioEsito("Richiesta non ammessa per presenza discarichi su posizione debitoria");
		    	} else {
			    	//Prima cancello poi inserisco
			    	//Attualmente qualora la configurazione preveda la generazione dei codici iuv, nuovi codici iuv sono generati
		    		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - cancellazione posizione debitoria originaria");		    		
			    	CancellazioneEcRequest cancReq = new CancellazioneEcRequest(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), in.getDocumento().getNumeroDocumento());
			    	CancellazioneEcResponse cancRes = cancellazioneEC(cancReq);
			    	if (cancRes.getCodiceEsito().equals("00")) {
			    		//inizio LP PG200360
			    		//NOTA. Genero un nuovo identificativo flusso
			    		//      in questo modo forzo la creazione di una nuova struttura flusso con il solo documento variato
			    		if(bModalitaAgggiornamento || bStampaAvvisoEseguita) {
			    			String newidflusso = getNewIdentificativoFlusso(in.getCodiceUtente());
			    			in.getConfigurazione().setIdentificativoFlusso(newidflusso);
				    		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - inserimento posizione debitoria variata su nuovo flusso: '" + newidflusso + "'");
			    		} else
			    		//fine LP PG200360
			    		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - inserimento posizione debitoria variata");
				    	InserimentoEcRequest insReq = new InserimentoEcRequest(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), in.getConfigurazione(), in.getRuolo(), in.getDocumento(), in.getAnagrafica(), in.getListScadenze(), in.getListTributi());
				    	InserimentoEcResponse insRes = inserimentoEC(insReq);
				    	response.setCodiceEsito(insRes.getCodiceEsito());
				    	response.setMessaggioEsito(insRes.getMessaggioEsito());
			    	} else {
			    		response.setCodiceEsito(cancRes.getCodiceEsito());
			    		response.setMessaggioEsito(cancRes.getMessaggioEsito());
			    	}
		    	}
        	}
    	} catch (ConfigurazioneException e) {	
			error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC failed, configuration error due to: ", e);
			response.setCodiceEsito("02");
			response.setMessaggioEsito(e.getMessage());	//"Configurazione errata"
        } catch (ValidazioneException e) {
        	error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC failed, validation error due to: ", e);
			response.setCodiceEsito("04");
			response.setMessaggioEsito(e.getMessage());	//"Errore di validazione"
    	} catch (Exception e) {
    		error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC failed, generic error due to: ", e);
			response.setCodiceEsito("01");
			response.setMessaggioEsito("Errore generico");
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	if(response != null) {
	    		//inizio LP PG210130
	    		sw = new StringWriter();
	    		//fine LP PG210130
				JAXB.marshal(response, sw);
				xmlString = sw.toString();
				//inizio LP PG22XX05
				try {
					sw.close();
				} catch (IOException e1) {
				}
				sw = null;
				//fine LP PG22XX05
				logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - response: " + xmlString);
	    	}
			//fine LP PG21XX04 Leak
		}
    	logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - variazioneEC - fine");
    	return response;
    }

    @Override
	public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcResponse cancellazioneEC(com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcRequest in) throws java.rmi.RemoteException, com.esed.payer.archiviocarichi.webservice.srv.FaultType {
    	logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC - inizio");
    	System.out.println("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC - inizio");
    	System.out.println("Codice Ente: " + in.getCodiceEnte() );
    	System.out.println("Codice Utente: " + in.getCodiceUtente());
    	System.out.println("Imposta Servizio: " + in.getImpostaServizio() );
    	System.out.println("Numero Documento: " + in.getNumeroDocumento() );
    	System.out.println("Tipo Servizio: " + in.getTipoServizio() );
    	CancellazioneEcResponse response = new CancellazioneEcResponse(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), "", "", in.getNumeroDocumento());
    	Connection connection = null;
		//inizio LP PG21XX04 Leak
		CachedRowSet ecCached = null;
		//fine LP PG21XX04 Leak
		
		StringWriter sw = new StringWriter();
		JAXB.marshal(in, sw);
		String xmlString = sw.toString();
		//inizio LP PG22XX05
		try {
			sw.close();
		} catch (IOException e1) {
		}
		sw = null;
		//fine LP PG22XX05
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC - request: " + xmlString);
    	try {
    		dbSchemaCodSocieta = in.getCodiceUtente();
    		connection = getConnectionDifferito(dbSchemaCodSocieta);
        	archivioCarichiDao = new ArchivioCarichiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
        	
//    		//Verifico se esiste la posizione debitoria da cancellare
    		ArchivioCarichiDocumento docIn = prepareArchivioCarichiDocumento(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), in.getNumeroDocumento());
//			ArchivioCarichiDocumento docOut = archivioCarichiDao.getDocumento(docIn);
//			if (docOut.getProgressivoFlusso() == null) {
//				throw new NotFoundException("Posizione debitoria non presente in archivio");	
//			} else {
			//Verifico se sussistono pagamenti correlati alla posizione debitoria
    		
			String numeroAvvisoDaCancellare = archivioCarichiDao.getDocumento(docIn).getNumeroBollettinoPagoPA();
	
			estrattoContoDao = new EstrattoContoDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
			logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC - doCachedRowAcquisizioneTransazioni - verifica presenza pagamenti su posizione debitoria");
			//20220808 SB - inizio
			String[] sEsito;
			if(dbSchemaCodSocieta.equals("000P4"))
				sEsito = estrattoContoDao.doCachedRowAcquisizioneTransazioniIS(in.getCodiceUtente(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), "", in.getTipoServizio(), "", in.getNumeroDocumento(), "D", in.getImpostaServizio());
			else
				sEsito = estrattoContoDao.doCachedRowAcquisizioneTransazioni(in.getCodiceUtente(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), "", in.getTipoServizio(), "", in.getNumeroDocumento(), "D");
			//20220808 SB - fine
			if (sEsito!=null && sEsito.length>0 && sEsito[0]=="00")
    		{
				String collString = estrattoContoDao.getWebRowSetXml("0");
	    		// converto la stringa in cachedrowset
				//inizio LP PG21XX04 Leak
				//CachedRowSet ecCached = Convert.stringToWebRowSet(collString);
				ecCached = Convert.stringToWebRowSet(collString);
				//fine LP PG21XX04 Leak
				if (ecCached.size() > 0)
				{
					logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC non ammessa per presenza pagamenti su posizione debitoria");
					response.setCodiceEsito("06");
					response.setMessaggioEsito("Richiesta non ammessa per presenza pagamenti");
				} else {
					//Cancellazione posizione debitoria in assenza di pagamenti associati
					logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC - deleteDocumento - cancellazione posizione debitoria");
		        	String retMessage = archivioCarichiDao.deleteDocumento(docIn);
		        	if (retMessage.equals("")) {
						response.setCodiceEsito("00");
			    		response.setMessaggioEsito("Richiesta eseguita con successo");
			    		
			    		// inizio SR PGNTACWS-2
			        	if(propertiesTree().getProperty(PropKeys.servizioJppa.format()) != null && propertiesTree().getProperty(PropKeys.servizioJppa.format()).equals("Y")) {
				    		CaricaDebitiJppa jppa = new CaricaDebitiJppa();
							InviaDovutiDao dao = new InviaDovutiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
							String codiceIpaComune = "";
							codiceIpaComune = dao.getCodiceIpa(in.getCodiceUtente(), in.getCodiceEnte());
							
							String token = jppa.login(propertiesTree().getProperty(PropKeys.username.format()), propertiesTree().getProperty(PropKeys.password.format()), codiceIpaComune);
							jppa.cancellaDovuto(token, codiceIpaComune, numeroAvvisoDaCancellare);  						
			        	}
			        	// fine SR PGNTACWS-2
		        	} else {
		        		throw new NotFoundException("Posizione debitoria non presente in archivio");
		        	}
				}
    		} else {
    			//error
    			error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC failed, error in doCachedRowAcquisizioneTransazioni " + sEsito==null?"":(": " + sEsito[1]));
    			throw new Exception("Errore in verifica esistenza pagamenti " + sEsito==null?"":(": " + sEsito[1]));
    		}
//			}
    		
    	} catch (NotFoundException e) {
    		error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC failed, not found error due to: ", e);
			response.setCodiceEsito("05");
			response.setMessaggioEsito(e.getMessage());
    	} catch (Exception e) {
    		error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC failed, generic error due to: ", e);
			response.setCodiceEsito("01");
			response.setMessaggioEsito("Errore generico");
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(ecCached != null) {
	    			ecCached.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
	    	if(response != null) {
	    		//inizio LP PG210130
	    		sw = new StringWriter();
	    		//fine LP PG210130
				JAXB.marshal(response, sw);
				xmlString = sw.toString();
				//inizio LP PG22XX05
				try {
					sw.close();
				} catch (IOException e1) {
				}
				sw = null;
				//fine LP PG22XX05
				logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC - response: " + xmlString);
	    	}
		}
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - cancellazioneEC - fine");
    	return response;
    }

    @Override
	public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcResponse discaricoEC(com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcRequest in) throws java.rmi.RemoteException, com.esed.payer.archiviocarichi.webservice.srv.FaultType {
    	logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC - inizio");
    	DiscaricoEcResponse response = new DiscaricoEcResponse(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), "", "", in.getDocumento().getNumeroDocumento());
    	Connection connection = null;
    	StringWriter sw = new StringWriter();
		JAXB.marshal(in, sw);
		String xmlString = sw.toString();
		//inizio LP PG22XX05
		try {
			sw.close();
		} catch (IOException e1) {
		}
		sw = null;
		//fine LP PG22XX05
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC - request: " + xmlString);
		//inizio LP PG210130
		boolean bOperazioneEseguita = false;
		//fine LP PG210130
    	try {
    		dbSchemaCodSocieta = in.getCodiceUtente();
    		//inizio LP PG210130
        	//connection = getConnectionDifferito(dbSchemaCodSocieta);
        	connection = getConnectionDifferito(dbSchemaCodSocieta, false);
    		//fine LP PG210130
        	elaborazioneFlussiDao = new ElaborazioneFlussiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
        	archivioCarichiDao = new ArchivioCarichiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
        	
        	Documento documento = in.getDocumento();
			        	
	    	//Verifico presenza posizione debitoria
    		ArchivioCarichiDocumento docIn = prepareArchivioCarichiDocumento(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), in.getDocumento().getNumeroDocumento());
			ArchivioCarichiDocumento docOut = archivioCarichiDao.getDocumento(docIn);
			if (docOut.getProgressivoFlusso() == null) {
				//inizio LP PG22XX05
		    	try {
		    		if(connection != null)
		    			connection.close();
		    	} catch (SQLException e) {
		    		e.printStackTrace();
				}
		    	connection = null;
				//fine LP PG22XX05
				throw new NotFoundException("Posizione debitoria non presente in archivio");	
			} else {   	
		    	//Per ogni tributo su cui deve essere applicato il discarico deve essere aggiornato l'importo discaricato
		    	//Quindi su ogni tributo va applicato un pagamento
		    	if (in.getListTributi()!=null && in.getListTributi().length>0) {
		    		//inizio LP PG210130
		    		java.sql.Date dataFlusso = null; 
		    		//fine LP PG210130
					for(Tributo tributo : in.getListTributi()) {
						logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC - getTributo codice/anno/progressivo tributo: " + tributo.getCodiceTributo()+"/"+tributo.getAnnoTributo()+"/"+tributo.getProgressivoTributo());
						ArchivioCarichiTributo tribIn = prepareArchivioCarichiTributo(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(), documento.getNumeroDocumento(), tributo.getCodiceTributo(), tributo.getAnnoTributo(), tributo.getProgressivoTributo());
						ArchivioCarichiTributo tribOut = archivioCarichiDao.getTributo(tribIn);
						if (tribOut.getProgressivoFlusso() == null) {
							//inizio LP PG22XX05
					    	try {
					    		if(connection != null) {
				    				connection.rollback();
					    			connection.close();
					    		}
					    	} catch (SQLException e) {
					    		e.printStackTrace();
							}
					    	connection = null;
							//fine LP PG22XX05
							throw new NotFoundException("Tributo codice " + tributo.getCodiceTributo() + " anno " + tributo.getAnnoTributo() + " progressivo " + tributo.getProgressivoTributo() + " non presente in archivio");
						} else {
							BigDecimal impEuroTributoDaDiscaricare = tributo.getImpTributo().divide(new BigDecimal(100.00));
				    		//inizio LP PG210130
							//Nota. Se l'importo da discaricare e' zero non va eseguita la procedura per quel tributo.
							if(impEuroTributoDaDiscaricare.compareTo(BigDecimal.ZERO) == 0) {
								continue;
							}
							//fine LP PG210130
							//Verifico che l'importo da discaricare non sia superiore all'importo del tributo residuo in archivio
							if (impEuroTributoDaDiscaricare.compareTo(tribOut.getImpTributo().subtract(tribOut.getImpPagatoCompresiSgravi()))>0) {
								//throw new ValidazioneException("Importo da discaricare " + bigDecimalToDouble(tributo.getImpTributo()) + " per tributo codice " + tributo.getCodiceTributo() + " anno " + tributo.getAnnoTributo() + " progressivo " + tributo.getProgressivoTributo() + " superiore a importo tributo " + tribOut.getImpTributo());
								//inizio LP PG22XX05
						    	try {
						    		if(connection != null)
						    			connection.close();
						    	} catch (SQLException e) {
						    		e.printStackTrace();
								}
						    	connection = null;
								//fine LP PG22XX05
								throw new ValidazioneException("Importo da discaricare " + GenericsDateNumbers.bigDecimalToDouble(tributo.getImpTributo()) + " per tributo codice " + tributo.getCodiceTributo() + " anno " + tributo.getAnnoTributo() + " progressivo " + tributo.getProgressivoTributo() + " superiore a importo residuo tributo " + tribOut.getImpTributo().subtract(tribOut.getImpPagatoCompresiSgravi()));
							}
							//Genero un movimento per il discarico
							Calendar calCurrentDate = Calendar.getInstance();
							String dataMovimento = GenericsDateNumbers.calendarToString(calCurrentDate, "yyyy-MM-dd");	//data movimento nel formato AAAA-MM-GG
				    		//inizio LP PG210130
							if(dataFlusso == null)
								dataFlusso = (java.sql.Date) tribOut.getDataCreazioneFlusso();
				    		//fine LP PG210130
							//Selezione max progressivo movimento a parit di documento a partire da 1
							int progPagamento = archivioCarichiDao.getProgressivoPagamento(docIn);
							   
							elaborazioneFlussiDao.doInsertEH3(tribOut.getProgressivoFlusso().intValue(), "EH3", tribOut.getCodiceUtente(), (java.sql.Date)tribOut.getDataCreazioneFlusso(), tribOut.getTipoServizio(), tribOut.getCodiceEnte(), tribOut.getTipoUfficio(), tribOut.getCodiceUfficio(), 
									tribOut.getImpostaServizio(), tribOut.getNumeroDocumento(), 
									progPagamento, //int da max 4 caratteri (progressivo pagamento all'interno del documento che fa parte della chiave)
									"D", 		   //tipoMovimento: N=Pagamento, A=Abbuono, D=Discarico, S=Sgravio, E=Eccedenza, I=Rimborso in Corso, P=Preavviso,  R=Rimborso eseguito 
									java.sql.Date.valueOf(dataMovimento), 
									"D",	       //causaleMovimento: N=Pagamento, A=Abbuono, D=Discarico, S=Sgravio, E=Eccedenza, I=Rimborso in Corso, P=Preavviso,  R=Rimborso eseguito 
									"", 		   //segno: ""=pagamento, "-"=storno/annullo
									GenericsDateNumbers.bigDecimalToDouble(tributo.getImpTributo()), 
									0, 0, 0, 0, 
									0,		//numeroRateMovimentate
									0,		//primaRataMovimentata
									"C", 	//canalePagamento (A=Assegno di traenza, B=Bonifico, C=Cassa/Sportello, H=Sisal, L=Lottomatica, I=Internet, P=Poste, R=Rid/Sepa)
									"", 
									"C");
							
							//Aggiorno l'importo discaricato sul tributo
							tribOut.setImpPagatoCompresiSgravi(impEuroTributoDaDiscaricare);
							archivioCarichiDao.applicaDiscarico(tribOut);
						}
					}
					//inizio LP PG210130
					//inizio LP PG210130 Step02
					//boolean bOldVersion = true;
					//if(!bOldVersion) {
					//	remakeEC(in, docIn, dataFlusso);
					//} else {
					//	info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
					//	info("  A T T E N Z I O N E  A T T E N Z I O N E  A T T E N Z I O N E  A T T E N Z I O N E  A T T E N Z I O N E  A T T E N Z I O N E");
					//	info("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC da implementare l'aggiornamento delle tabelle EHC e EHD");
					//	info("                                                               nel caso di documento non rata unica");
		    			//	info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		    		//}
					//fine LP PG210130 Step02
		    		connection.commit();
					info("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC - Operazione Eseguita");
					//fine LP PG210130
				}
			}
			response.setCodiceEsito("00");
    		response.setMessaggioEsito("Richiesta eseguita con successo");
    		bOperazioneEseguita = true;
    	} catch (NotFoundException e) {
    		error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC failed, not found error due to: ", e);
			response.setCodiceEsito("05");
			response.setMessaggioEsito(e.getMessage());
    	} catch (ValidazioneException e) {
    		error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC failed, validation error due to: ", e);
			response.setCodiceEsito("04");
			response.setMessaggioEsito(e.getMessage());	
    	} catch (Exception e) {
    		e.printStackTrace();
    		error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC failed, generic error due to: ", e);
			response.setCodiceEsito("01");
			response.setMessaggioEsito("Errore generico");
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(connection != null) {
	        		//inizio LP PG210130
	    			if(!bOperazioneEseguita)
	    				connection.rollback();
					//fine LP PG210130
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
	    	if(response != null) {
	    		//inizio LP PG210130
	    		sw = new StringWriter();
	    		//fine LP PG210130
				JAXB.marshal(response, sw);
				xmlString = sw.toString();
				//inizio LP PG22XX05
				try {
					sw.close();
				} catch (IOException e1) {
				}
				sw = null;
				//fine LP PG22XX05
				logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC - response: " + xmlString);
	    	}
		}
    
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - discaricoEC - fine");
    	return response;
    }

    @Override
	public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaResponse richiestaAvvisoPagoPa(com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaRequest in) throws java.rmi.RemoteException, com.esed.payer.archiviocarichi.webservice.srv.FaultType {
    	logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPa - inizio");
//    	System.out.println("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPa - inizio");
//    	System.out.println("Codice Utente: " + in.getCodiceUtente());
//    	System.out.println("Tipo Servizio: " + in.getTipoServizio());
//    	System.out.println("Codice Ente: " + in.getCodiceEnte());
//    	System.out.println("Tipo Ufficio: " + in.getTipoUfficio());
//    	System.out.println("Codice Ufficio: " + in.getCodiceUfficio());
//    	System.out.println("Imposta Servizio: " + in.getImpostaServizio());
//    	System.out.println("Codice Fiscale: " + in.getStampaDocumento().getCodiceFiscale());
//    	System.out.println("Numero Documento: " + in.getStampaDocumento().getNumeroDocumento());
//    	System.out.println("Importo Totale Documento: " + in.getStampaDocumento().getImportoTotaleDocumento());
    	StringWriter sw = new StringWriter();
		JAXB.marshal(in, sw);
		String xmlString = sw.toString();
		//inizio LP PG22XX05
		try {
			sw.close();
		} catch (IOException e1) {
		}
		sw = null;
		//fine LP PG22XX05
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPA - request: " + xmlString);
    	logger.debug("Codice Utente: " + in.getCodiceUtente());
    	logger.debug("Codice Ente: " + in.getCodiceEnte());
    	logger.debug("Numero Documento: " + in.getStampaDocumento().getNumeroDocumento());
    	logger.debug("Codice Fiscale: " + in.getStampaDocumento().getCodiceFiscale());
    	logger.debug("Codice Imposta Servizio: " + in.getImpostaServizio());
    	
    	String path = "";
    	ArrayList<Flusso> listaFlussi = new ArrayList<Flusso>();
		CallableStatement callableStatement=null;
		Flusso curFlusso = null;
		ResultSet resultSet=null;
		byte[] stampaPDFDocumentoPagoPA = new byte[0];
		boolean flagDocumentoDisponibile = false;
    	Connection connection = null;
		
    	RichiestaAvvisoPagoPaResponse response = new RichiestaAvvisoPagoPaResponse(in.getCodiceUtente(), in.getTipoServizio(), in.getCodiceEnte(), in.getTipoUfficio(), in.getCodiceUfficio(), in.getImpostaServizio(),  "00", "Richiesta eseguita con successo", in.getStampaDocumento(), stampaPDFDocumentoPagoPA);
    	try {
    		dbSchemaCodSocieta = in.getCodiceUtente();
    		String schema = getSchemaDifferito(dbSchemaCodSocieta);
    		connection = getConnectionDifferito(dbSchemaCodSocieta);
    		
    		String generatorePdf = "GEOS";
	         if (propertiesTree().getProperty(PropKeys.generatorePdf.format(in.getCodiceUtente()))!=null)
	        	 generatorePdf = propertiesTree().getProperty(PropKeys.generatorePdf.format(in.getCodiceUtente()));
    		
    		GeosUtil.setConfiguration();
			callableStatement=Helper.prepareCall(connection, schema, "PY512SP_AVVI_WS");
			callableStatement.setString(1,dbSchemaCodSocieta);
			callableStatement.setString(2,in.getCodiceEnte());
			callableStatement.setString(3,in.getStampaDocumento().getNumeroDocumento());
			callableStatement.setString(4,in.getStampaDocumento().getCodiceFiscale());
			//inizio LP PG200360
			//callableStatement.registerOutParameter(5, Types.INTEGER); // CODICE ERR
			//callableStatement.registerOutParameter(6, Types.VARCHAR); // MESS ERR
			if (in.getStampaDocumento().getFlagDatiAttualizzati() != null && in.getStampaDocumento().getFlagDatiAttualizzati().equals("Y")) {
				callableStatement.setString(5,"Y");
			} else {
				callableStatement.setString(5,"N");
			}
			callableStatement.setString(6,in.getImpostaServizio());
			callableStatement.registerOutParameter(7, Types.INTEGER); // CODICE ERR
			callableStatement.registerOutParameter(8, Types.VARCHAR); // MESS ERR
			//fine LP PG200360
			callableStatement.execute();
			//inizio LP PG200360
			//logger.debug("richiestaAvvisoPagoPa - PY512SP_AVVI_WS - returnCode: " + callableStatement.getInt(5));
			//fine LP PG200360
			String targetPdfPath = "";
			//inizio LP PG200360
			//int retCodeSp = callableStatement.getInt(5);
			//String retMessageSp = callableStatement.getString(6);
			//PG22XX01_GG1 - inizio
			//int retCodeSp = callableStatement.getInt(6);
			//String retMessageSp = callableStatement.getString(7);
			int retCodeSp = callableStatement.getInt(7);
			String retMessageSp = callableStatement.getString(8);
			//PG22XX01_GG1 - fine
			logger.debug("richiestaAvvisoPagoPa - PY512SP_AVVI_WS - returnCode: " + retCodeSp);
			//fine LP PG200360
			if (retCodeSp==0){ //MAI STAMPATO ... CREA XML
				logger.debug("richiestaAvvisoPagoPa - Non esiste PDF nella basedati");
				
				resultSet= callableStatement.getResultSet();

				if (resultSet != null && resultSet.next()) {
					flagDocumentoDisponibile = true;
					Long impTotaleDocumentoIn = Long.valueOf(in.getStampaDocumento().getImportoTotaleDocumento());
					Long impTotaleDocumentoDb = Long.valueOf(resultSet.getString("DOC_IMPORTO_TOTALE"));
					logger.debug("richiestaAvvisoPagoPa - impTotaleDocumentoIn = " + impTotaleDocumentoIn);
					logger.debug("richiestaAvvisoPagoPa - impTotaleDocumentoDb = " + impTotaleDocumentoDb);
					
					if (impTotaleDocumentoIn.compareTo(impTotaleDocumentoDb)!=0)
						throw new ValidazioneException("Importo totale del documento non congruente");
					logger.debug("richiestaAvvisoPagoPa - Elaborazione resultset");
					// primo avviso con tutte le colonne Debitore/Docum/Avviso
					     targetPdfPath = resultSet.getString("FLU_FOLDER");
				         curFlusso = GeosUtil.extractFlusso(resultSet);
						 //inizio LP - mail Giorgia 20200608
				         //com.esed.payer.archiviocarichi.webservice.util.geos.Documento curDoc = GeosUtil.extractDoc(resultSet);
				         com.seda.payer.commons.geos.Documento curDoc = GeosUtil.extractDoc(resultSet);
						 //fine LP - mail Giorgia 20200608
				         
				         DatiAnagrafici curAna = GeosUtil.extractAna(resultSet);
				         
				         
				         // inizio PAGONET-368
				         //DatiCreditore curCre = GeosUtil.extractCre(resultSet);
				         DatiCreditore curCre = GeosUtil.extractCre(resultSet,dbSchemaCodSocieta);
				         // fine PAGONET-368

						 //inizio LP - mail Giorgia 20200608
				         //com.esed.payer.archiviocarichi.webservice.util.geos.Tributo curTri = GeosUtil.extractTrib(resultSet);
				         com.seda.payer.commons.geos.Tributo curTri = GeosUtil.extractTrib(resultSet);
						 //fine LP - mail Giorgia 20200608

				         Bollettino curBol = GeosUtil.extractBoll(resultSet,dbSchemaCodSocieta);
				         Bollettino curUltimoBol = GeosUtil.extractBollUltimo(resultSet,dbSchemaCodSocieta); 

				         curFlusso.addDocumento(curDoc);
				         curDoc.addDatiAnagrafici(curAna);
				         curDoc.addDatiCreditore(curCre);
			        	 curDoc.addElencoTributi(curTri);
		        		 curDoc.addDatiBollettino(curBol);
				         listaFlussi.add(curFlusso);

				         // scansione dei flussi da DB in un unico Resultset... ci sono tutte le colonne
				         while (resultSet.next()) {
				           // riga del resultset...
				        	 curTri = GeosUtil.extractTrib(resultSet);
				        	 curDoc.addElencoTributi(curTri);
				        	 if (!(GeosUtil.sameFlusso(resultSet, curFlusso) && GeosUtil.sameBoll(resultSet, curBol))) {
				        		 curBol = GeosUtil.extractBoll(resultSet,dbSchemaCodSocieta);
				        		 curDoc.addDatiBollettino(curBol);
				        	 }
				         }
				         
				         
				         //PG22XX03_SB2 - inizio
					        if(generatorePdf.equals("WSPDF")) {
					        	for(Bollettino bol : curDoc.DatiBollettino) {
					        		bol.AvvisoPagoPa = formattaCodiceAvviso(bol.AvvisoPagoPa);
					        	}
					        	curUltimoBol.AvvisoPagoPa=formattaCodiceAvviso(curUltimoBol.AvvisoPagoPa);
					        }
					      //PG22XX03_SB2 - fine
							
				         
				         //Devo aggiungere la rata 999
				         curDoc.addDatiBollettino(curUltimoBol);	//TODO da verificare
				         
				         //String targetPdfNome=dbSchemaCodSocieta+"_"+in.getStampaDocumento().getNumeroDocumento()+"_"+System.currentTimeMillis()+".pdf"; 
				         String targetPdfNome=dbSchemaCodSocieta+"_"+in.getStampaDocumento().getNumeroDocumento().replace("/", "-")+".pdf";
				         targetPdfPath=targetPdfPath+File.separator+targetPdfNome;
				         
				         
						switch (generatorePdf) {
							case "GEOS":
								logger.debug("richiestaAvvisoPagoPa - Interrogazione WS GEOS");
								String urlWsGeos = propertiesTree().getProperty(PropKeys.urlWSRestGeos.format(in.getCodiceUtente()));
								// inizio LP - mail Giorgia 20200608
								// if
								// (WSRest_GEOS.callWSRest_GEOS(curFlusso,targetPdfPath,dbSchemaCodSocieta,urlWsGeos)){
								logger.debug("urlGeos da config: " + urlWsGeos);
								if (urlWsGeos == null || urlWsGeos.trim().length() == 0) {
									urlWsGeos = GeosUtil.getUrlRestGeos(dbSchemaCodSocieta);
									logger.debug("urlGeos da getUrlRestGeos: " + urlWsGeos);
								}
								if (WSRest_GEOS.callWSRest_GEOS(curFlusso, targetPdfPath, urlWsGeos)) {
									// fine LP - mail Giorgia 20200608
									path = targetPdfPath;
								} else {
									logger.debug("richiestaAvvisoPagoPa - Errore chiamata WS GEOS");
									throw new Exception("Impossibile eseguire la stampa dell'avviso");
								}
								break;
							case "WSPDF":
								logger.debug("richiestaAvvisoPagoPa - Interrogazione WSPDF");
								String urlWsPdf = propertiesTree().getProperty(PropKeys.urlWSRestPdf.format(in.getCodiceUtente()));
								if(in.getCodiceUtente().equals("000RM")
										|| in.getCodiceUtente().equals("000LP")) {
									urlWsPdf += "RegMarche";
								}else if(in.getCodiceUtente().equals("000P6")) {
									urlWsPdf += "Bolzano";
								}
								logger.debug("urlPdf da config: " + urlWsPdf);
								if (urlWsPdf == null || urlWsPdf.trim().length() == 0) {
									throw new Exception("Mancata configurazione url WSPDF");
								}
								
								Client client = ClientBuilder.newClient();
								
								try (Response res = client.target(urlWsPdf)
										.request(MediaType.APPLICATION_JSON)
										.post(Entity.entity(curFlusso, MediaType.APPLICATION_JSON))) {
									
									if (res.getStatus() == Status.OK.getStatusCode()) {
										byte[] ba = res.readEntity(byte[].class);
										File file = new File(targetPdfPath);
										FileUtils.writeByteArrayToFile(file, ba);
									} else {
										logger.debug("richiestaAvvisoPagoPa - Errore chiamata WS PDF");
										throw new Exception("Impossibile eseguire la stampa dell'avviso");
									}
									
									path = targetPdfPath;
								}
								
								client.close();
								break;
							default:
								throw new Exception("Errata configurazione generatore PDF");
						}
					}
				} else {	//retcode diverso da zero
					if (retCodeSp==1) {
						logger.debug("richiestaAvvisoPagoPa - Trovato PDF in base dati ottico interno");
						// Il PDF esiste ed  stato creato precedentemente dalla fase batch
						resultSet= callableStatement.getResultSet();
						String pathFile = "";
						String fileName ="";
						String pagi = "";
						String pagf = "";
						if(resultSet.next()){
							flagDocumentoDisponibile = true;
							pathFile = resultSet.getString("CFO_CCFODIRO");
							fileName = resultSet.getString("DOT_DDOTDIMM");
							fileName = fileName.replace("_all_docs.pdf", "");
							pagi = resultSet.getString("DOT_NDOTPAGI");
							pagf = resultSet.getString("DOT_NDOTPAGF");
							
						}
						
						PdfReader reader = new PdfReader(pathFile+File.separator+fileName+"_all_docs.pdf");
						PdfStamper stamper;
						String fileNameTemp = fileName+"_doc_"+in.getStampaDocumento().getNumeroDocumento().replace("/", "-")+".pdf";
						reader.selectPages(String.valueOf(pagi)+"-"+String.valueOf(pagf));
						ByteArrayOutputStream tempPdf = new ByteArrayOutputStream();
						stamper = new PdfStamper(reader,tempPdf);
						stamper.close();
						reader.close();
						
						FileOutputStream fos = new FileOutputStream(pathFile+File.separator+fileNameTemp);
						fos.write(tempPdf.toByteArray());
						fos.close();
						//inizio LP PG21XX04 Leak
						tempPdf.close();
						//fine LP PG21XX04 Leak

						path = pathFile+File.separator+fileNameTemp;
					} else {
						throw new NotFoundException(retMessageSp);
					}
				}
			if (resultSet!=null) resultSet.close();
			callableStatement.close();
			if (flagDocumentoDisponibile) {
				if (in.getStampaDocumento().getFlagDatiAttualizzati()!=null && in.getStampaDocumento().getFlagDatiAttualizzati().equals("Y")) {
					response.getStampaDocumento().setPathStampaDocumento(path);
				}
				stampaPDFDocumentoPagoPA = readBytesFromFile(path);
				response.setStampaPDFDocumentoPagoPA(stampaPDFDocumentoPagoPA);
			} else {
				throw new NotFoundException("Documento non presente a sistema");
			}
    	} catch (ConfigurazioneException e) {	
    		error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPa failed, configuration error due to: ", e);
			response.setCodiceEsito("02");
			response.setMessaggioEsito(e.getMessage());	//"Configurazione errata"
        } catch (ValidazioneException e) {
        	error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPa failed, validation error due to: ", e);
			response.setCodiceEsito("04");
			response.setMessaggioEsito(e.getMessage());	//"Errore di validazione"	
    	} catch (NotFoundException e) {
    		error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPa failed, not found error due to: ", e);
			response.setCodiceEsito("03");
			response.setMessaggioEsito(e.getMessage());	//"Documento non presente a sistema"
		} catch (Exception e) {
			e.printStackTrace();
			error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPa failed, generic error due to: ", e);
			response.setCodiceEsito("01");
			response.setMessaggioEsito("Errore generico");
		} catch (Throwable e) {
			e.printStackTrace();
			error("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPa failed, generic error due to: ", e);
			response.setCodiceEsito("01");
			response.setMessaggioEsito("Errore generico");
		}finally
		{
			//inizio LP PG21XX04 Leak
            //DAOHelper.closeIgnoringException(resultSet);
			//DAOHelper.closeIgnoringException(callableStatement);
    		//closeConnection(connection);
	    	try {
	    		if(resultSet != null) {
	    			resultSet.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	try {
	    		if(callableStatement != null) {
	    			callableStatement.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
	    	if(response != null) {
	    		//inizio LP PG210130
	    		sw = new StringWriter();
	    		//fine LP PG210130
				JAXB.marshal(response, sw);
				xmlString = sw.toString();
				//inizio LP PG22XX05
				try {
					sw.close();
				} catch (IOException e1) {
				}
				sw = null;
				//fine LP PG22XX05
				logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - stampaAvvisoPagoPA - response: " + xmlString);
	    	}
		}	
    	//16022021 GG - funzionalit momentaneamente inibita - inizio
//    	response.setCodiceEsito("01");
//    	response.setMessaggioEsito("Funzionalit momentaneamente non disponibile");
    	//16022021 GG - funzionalit momentaneamente inibita - fine
    	logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - richiestaAvvisoPagoPa - fine");
		return response;
    }
    
    private boolean checkRequest(String codiceUtente, String codiceEnte, Configurazione configurazione, Documento documento, Scadenza[] listScadenze, Tributo[] listTributi, Anagrafica anagrafica, String flagOperazione) throws ConfigurazioneException, ValidazioneException, DaoException {
    	boolean check=false;
    	//Verifiche su configurazione
    	if (configurazione.getFlagGenerazioneIUV().equals("Y")) {
    		
    		//Controllo che sia fornita la configurazione per la generazione dello iuv
    		if (configurazione.getConfigurazioneIUV()==null) 
    			throw new ConfigurazioneException("configurazioneIUV obbligatoria non valorizzata");
    	
    		//Recupero la configurazione dal DB
    		ConfigurazioneModello3 configurazioneModello3 = selectConfigurazioneModello3(codiceUtente, configurazione.getConfigurazioneIUV());
    		//Controllo che la configurazione passata in input coincida con la nostra configurazione su file .properties (analoga a quanto fatto per il ws nodo per il modello 3) 
	    	String auxDigitConfig = "";
	    	if (propertiesTree().getProperty(PropKeys.auxDigitConfig.format(codiceUtente,configurazione.getConfigurazioneIUV().getIdentificativoDominio())) != null) 
				auxDigitConfig = propertiesTree().getProperty(PropKeys.auxDigitConfig.format(codiceUtente,configurazione.getConfigurazioneIUV().getIdentificativoDominio()));
	    	if (auxDigitConfig.equals("")) {
	    		System.out.println("AuxDigit non impostato su file di configurazione per codUtente "+codiceUtente+" e idDominio "+configurazione.getConfigurazioneIUV().getIdentificativoDominio());
				System.out.println("Recupero AuxDigit dal DB");
				//se non  stato impostato l'auxdigit su file di configurazione controllo se esiste la configurazione sul db
				// se chiaveEnte = "" significa che la configurazione non esiste sul DB
	    		if(!configurazioneModello3.getChiaveEnte().equals("")) {
	    			auxDigitConfig = configurazioneModello3.getAuxDigit();
	    		} else {
	    			throw new ConfigurazioneException("configurazioneIUV non correttamente valorizzata: mancata valorizzazione auxDigit");
	    		}
	    	}
	    	if (!auxDigitConfig.equals(configurazione.getConfigurazioneIUV().getAuxDigit())) {
	    		throw new ConfigurazioneException("configurazioneIUV non correttamente valorizzata: mancata corrispondenza auxDigit " 
					+ configurazione.getConfigurazioneIUV().getAuxDigit() + " in input con " + auxDigitConfig + " in configurazione");
	    	}
	    	String applicationCodeConfig = ""; 
	    	if (propertiesTree().getProperty(PropKeys.applicationCodeConfig.format(codiceUtente,configurazione.getConfigurazioneIUV().getIdentificativoDominio(),configurazione.getConfigurazioneIUV().getAuxDigit())) != null)
				applicationCodeConfig = propertiesTree().getProperty(PropKeys.applicationCodeConfig.format(codiceUtente,configurazione.getConfigurazioneIUV().getIdentificativoDominio(),configurazione.getConfigurazioneIUV().getAuxDigit()));
			
    		//se non  stato impostato l'applicationCode su file di configurazione lo recupero dal DB
	    	if(applicationCodeConfig.equals("")) {
	    		System.out.println("ApplicationCode non impostato su file di configurazione per codUtente "+codiceUtente+" e idDominio "+configurazione.getConfigurazioneIUV().getIdentificativoDominio());
				System.out.println("Recupero ApplicationCode dal DB");
				// se chiaveEnte = "" significa che la configurazione non esiste sul DB
				if(!configurazioneModello3.getChiaveEnte().equals("")) {
	    			applicationCodeConfig = configurazioneModello3.getCodiceSegregazione();
	    		}
	    	}
	    	
	    	if (auxDigitConfig.equals("0")) {
				String applicationCode = configurazione.getConfigurazioneIUV().getApplicationCode()==null?"":configurazione.getConfigurazioneIUV().getApplicationCode().trim();
				if (applicationCode.equals("")) 
					throw new ConfigurazioneException("configurazioneIUV non correttamente valorizzata: mancata valorizzazione applicationCode");
				if (!applicationCodeConfig.equals(applicationCode)) {
					throw new ConfigurazioneException("configurazioneIUV non correttamente valorizzata: mancata corrispondenza applicationCode "
							+ applicationCode + " in input con " + applicationCodeConfig + " in configurazione") ;
				}
	    	}
	    	
	    	if (auxDigitConfig.equals("3")) {
				String segregationCode = configurazione.getConfigurazioneIUV().getSegregationCode()==null?"":configurazione.getConfigurazioneIUV().getSegregationCode().trim();
				if (segregationCode.equals("")) 
					throw new ConfigurazioneException("configurazioneIUV non correttamente valorizzata: mancata valorizzazione segregationCode");
				if (!applicationCodeConfig.equals(segregationCode)) {
					throw new ConfigurazioneException("configurazioneIUV non correttamente valorizzata: mancata corrispondenza segregationCode "
							+ segregationCode + " in input con " + applicationCodeConfig + " in configurazione");
				}
			}   
    	} else {
	    	//Verifico valorizzazione numero bollettino PagoPA e codice IUV su documento e scadenze
	    	if (documento.getNumeroBollettinoPagoPA()==null || documento.getNumeroBollettinoPagoPA().trim().length()==0) 
	    		throw new ConfigurazioneException("numeroBollettinoPagoPA su documento non valorizzato");
	    	if (documento.getIdentificativoUnivocoVersamento()==null || documento.getIdentificativoUnivocoVersamento().trim().length()==0) 
	    		throw new ConfigurazioneException("identificativoUnivocoVersamento su documento non valorizzato");
	    	//In caso di iuv in input occorre verificare che non sia gi presente in base dati per ente
	    	int count = archivioCarichiDao.checkExistIUV(codiceEnte, documento.getIdentificativoUnivocoVersamento(), documento.getNumeroDocumento(), 0, flagOperazione);
	    	if (count > 0) 
	    		throw new ValidazioneException("identificativoUnivocoVersamento " + documento.getIdentificativoUnivocoVersamento() + " su documento gi presente in archivio");
	    	
	    	if (listScadenze!=null && listScadenze.length>0) {
				for(Scadenza scadenza : listScadenze) {
					if (scadenza.getNumeroBollettinoPagoPA()==null || scadenza.getNumeroBollettinoPagoPA().trim().length()==0)
						throw new ConfigurazioneException("numeroBollettinoPagoPA su scadenza con numero rata " + scadenza.getNumeroRata() + " non valorizzato");
					if (scadenza.getIdentificativoUnivocoVersamento()==null || scadenza.getIdentificativoUnivocoVersamento().trim().length()==0)
						throw new ConfigurazioneException("identificativoUnivocoVersamento su scadenza con numero rata " + scadenza.getNumeroRata() + " non valorizzato");
					//In caso di bollettino monorata occorre verificare che numero avviso/iuv documento corrisponda a numero avviso/iuv rata
					if (listScadenze.length==1) {
						if (!documento.getNumeroBollettinoPagoPA().equals(scadenza.getNumeroBollettinoPagoPA()))
							throw new ValidazioneException("numeroBollettinoPagoPA su documento monorata differente da numeroBollettinoPagoPA su unica scadenza");
						if (!documento.getIdentificativoUnivocoVersamento().equals(scadenza.getIdentificativoUnivocoVersamento()))
							throw new ValidazioneException("identificativoUnivocoVersamento su documento monorata differente da identificativoUnivocoVersamento su unica scadenza");
					}
					//In caso di iuv in input occorre verificare che non sia gi presente in base dati per ente
			    	count = archivioCarichiDao.checkExistIUV(codiceEnte, scadenza.getIdentificativoUnivocoVersamento(), documento.getNumeroDocumento(), Integer.valueOf(scadenza.getNumeroRata()), flagOperazione);
			    	if (count > 0) 
			    		throw new ValidazioneException("identificativoUnivocoVersamento " + scadenza.getIdentificativoUnivocoVersamento() + " su scadenza con numero rata " + scadenza.getNumeroRata() + " gi presente in archivio");
				}
			}
    	}
    	
    	if(flagOperazione.equalsIgnoreCase("I")) {
    		//Verifiche su valorizzazione numero documento in fase di inserimento
    		if (documento.getNumeroDocumento()==null || documento.getNumeroDocumento().trim().length()==0) 
	    		throw new ValidazioneException("numeroDocumento su documento non valorizzato");
    		//Verifiche su valorizzazione anno documento in fase di inserimento
    		if (documento.getAnnoEmissione()==null || documento.getAnnoEmissione().trim().length()==0)
    			throw new ValidazioneException("annoEmissione su documento non valorizzato");
    		else {
    			try {
    				Integer.parseInt(documento.getAnnoEmissione());
    			} catch (NumberFormatException ex) {
    				throw new ValidazioneException("annoEmissione su documento non valorizzato correttamente: <" + documento.getAnnoEmissione() +"> valore non numerico");
    			}
    		}
    	}
    	
		//inizio LP PG22XX05_LP1 - TipoAnagrafica A,D,F e M
		if(anagrafica.getTipoAnagrafica() == null || anagrafica.getTipoAnagrafica().trim().length() == 0)
			throw new ValidazioneException("tipo anagrafica non valorizzato");
		if(anagrafica.getTipoAnagrafica()!=null) {
			System.out.println("tipoAnagrafica: " + anagrafica.getTipoAnagrafica());	
			if (!anagrafica.getTipoAnagrafica().trim().equalsIgnoreCase("A")
				&& !anagrafica.getTipoAnagrafica().trim().equalsIgnoreCase("D")
				&& !anagrafica.getTipoAnagrafica().trim().equalsIgnoreCase("F")
				&& !anagrafica.getTipoAnagrafica().trim().equalsIgnoreCase("M"))
				throw new ValidazioneException("tipo anagrafica non correttamente valorizzato. Possibili valori ammessi A (Azienda), D (Ditta Individuale), F (Femmina) o M (Maschio).");
		}
		//fine LP PG22XX05_LP1 - TipoAnagrafica A,D,F e M
    	
		//Verifiche su valorizzazione codicefiscale in fase di inserimento
		if (anagrafica.getCodiceFiscale()==null || anagrafica.getCodiceFiscale().trim().length()==0)
			throw new ValidazioneException("codiceFiscale su anagrafica non valorizzato");
		//inizio LP PG22XX05  - bug validazione codicefiscale/piva
		String codiceFiscale0 = anagrafica.getCodiceFiscale();
		String codiceFiscale = VerificaCodiceFiscale.ClearString(codiceFiscale0);
		if(!codiceFiscale0.equals(codiceFiscale))
			System.out.println("In documento: " + documento.getNumeroDocumento() + " codfis prima: '" + codiceFiscale0 + "' dopo '" + codiceFiscale + "'");
		boolean bPIVA = (codiceFiscale.length() == 11);
		boolean bCodFis = (codiceFiscale.length() == 16);
		if(bPIVA) {
			//inizio LP PAGONET-351
			//if(anagrafica.getTipoAnagrafica().equalsIgnoreCase("M") || anagrafica.getTipoAnagrafica().equalsIgnoreCase("F"))
			if(anagrafica.getTipoAnagrafica().equalsIgnoreCase("M") ||
			   anagrafica.getTipoAnagrafica().equalsIgnoreCase("F") ||
			   anagrafica.getTipoAnagrafica().equalsIgnoreCase("D"))
			//fine LP PAGONET-351
				throw new ValidazioneException("partita iva non congruente con tipo anagrafica: " + anagrafica.getTipoAnagrafica());
			if(!VerificaCodiceFiscale.verificaPartitaIva(codiceFiscale))
				throw new ValidazioneException("valore partita iva non valido");
		} else if(bCodFis) {
			//inizio LP PAGONET-351
			//if(anagrafica.getTipoAnagrafica().equalsIgnoreCase("A") || anagrafica.getTipoAnagrafica().equalsIgnoreCase("D"))
			if(anagrafica.getTipoAnagrafica().equalsIgnoreCase("A"))
			//fine LP PAGONET-351
				throw new ValidazioneException("codice fiscale non congruente con tipo anagrafica: " + anagrafica.getTipoAnagrafica());
			if(!VerificaCodiceFiscale.verificaCodiceFiscale(codiceFiscale))
				throw new ValidazioneException("valore codice fiscale non valido");
			String valGG = codiceFiscale.substring(9, 11);
			int intValGG = 0;
			try {
				intValGG =  Integer.parseInt(valGG);	
			} catch (NumberFormatException e) {
				throw new ValidazioneException("valore codice fiscale non valido");
			}
			if(anagrafica.getTipoAnagrafica().equalsIgnoreCase("M") && (intValGG < 1 || intValGG > 31))
				throw new ValidazioneException("tipo anagrafica M non ammesso per codice fiscale inserito");
			if(anagrafica.getTipoAnagrafica().equalsIgnoreCase("F") && (intValGG < 41 || intValGG > 71))
				throw new ValidazioneException("tipo anagrafica F non ammesso per codice fiscale inserito");
			
			String dataNascita = anagrafica.getDataNascita();
			if(dataNascita != null && dataNascita.length() == 10) {
				String gg = dataNascita.substring(0, 2);
				int intGG = 0;
				try {
					intGG = Integer.parseInt(gg);	
				} catch (NumberFormatException e) {
					throw new ValidazioneException("valore data di nascita non valido");
				}
				if(anagrafica.getTipoAnagrafica().equalsIgnoreCase("M") && intValGG != intGG)
					throw new ValidazioneException("data di nascita non valida per codice fiscale inserito per tipo anagrafica M");
				if(anagrafica.getTipoAnagrafica().equalsIgnoreCase("F") && intValGG != (intGG + 40)) {
					throw new ValidazioneException("data di nascita non valida per codice fiscale inserito per tipo anagrafica F");
					
				}
			} 
		}
		//fine LP PG22XX05 - bug validazione codicefiscale/piva
		//inizio LP - Rimosso controllo su F e/o G
		else
			throw new ValidazioneException("CODICE FISCALE: valore non valido");
		//fine LP - Rimosso controllo su F e/o G

    	//inizio LP PG200360
    	//Verifiche su presenza tassonomia valida
    	String tassonomia = documento.getTassonomia();
    	if(tassonomia == null || tassonomia.trim().length() == 0) {
        	//inizio LP PG200360 - 20210224 tassonomia non obbligatoria
    		//throw new ValidazioneException("tassonomia non valorizzata su documento");
    		tassonomia = "";
        	//fine LP PG200360 - 20210224 tassonomia non obbligatoria
    	}
    	tassonomia = tassonomia.trim();
    	//inizio LP PG200360 - 20210224 tassonomia non obbligatoria
    	//if(!archivioCarichiDao.checkExistTassonomia(tassonomia)) {
		//	throw new ValidazioneException("la tassonomia presente sul documento: " + tassonomia + " non presnete in archivio");
        if(tassonomia.length() > 0 && !archivioCarichiDao.checkExistTassonomia(tassonomia)) {
    		throw new ValidazioneException("il codice tassonomia inserito nel documento: " + tassonomia + " non presente in archivio");
    	//fine LP PG200360 - 20210224 tassonomia non obbligatoria
    	}
    	//fine LP PG200360
    	//Verifiche su importo bollettino, non  ammesso caricamento di documenti senza importo residuo
    	if (documento.getImpBollettinoTotaleDocumento().compareTo(BigDecimal.ZERO)<=0) {
    		throw new ValidazioneException("importo totale bollettino su documento " + GenericsDateNumbers.bigDecimalToDouble(documento.getImpBollettinoTotaleDocumento()) + " non consentito");
    	}
    	
    	//Verifiche su quadratura dati
    	BigDecimal impTotScadenze = BigDecimal.ZERO;
    	if (listScadenze!=null && listScadenze.length>0) {
    		for(Scadenza scadenza : listScadenze) {
    			impTotScadenze = impTotScadenze.add(scadenza.getImpBollettinoRata());
    		}
    	}
    	//inizio LP PG200360
    	//if(!documento.getImpBollettinoTotaleDocumento().equals(impTotScadenze))
    	if(documento.getImpBollettinoTotaleDocumento().compareTo(impTotScadenze) != 0)
    	//fine LP PG200360
    		throw new ValidazioneException("importo totale bollettino su documento " + GenericsDateNumbers.bigDecimalToDouble(documento.getImpBollettinoTotaleDocumento()) + " diverso da totale importo bollettino rata sulle scadenze " + GenericsDateNumbers.bigDecimalToDouble(impTotScadenze));
    	
    	BigDecimal impTotTributi = BigDecimal.ZERO;
    	if (listTributi!=null && listTributi.length>0) {
    		for(Tributo tributo : listTributi) {
    			if (tributo.getAnnoTributo()!=null) {
    				try {
	    				Integer.parseInt(tributo.getAnnoTributo());
	    			} catch (NumberFormatException ex) {
	    				throw new ValidazioneException("annoTributo su tributo con codice <" + tributo.getCodiceTributo() + "> non valorizzato correttamente: <" + tributo.getAnnoTributo() +"> valore non numerico");
	    			}
    			}
    			impTotTributi = impTotTributi.add(tributo.getImpTributo());
    		}
    	}
    	//inizio LP PG200360
    	//if(!documento.getImpBollettinoTotaleDocumento().equals(impTotTributi))
    	if(documento.getImpBollettinoTotaleDocumento().compareTo(impTotTributi) != 0)
    	//fine LP PG200360
    		throw new ValidazioneException("importo totale bollettino su documento " + GenericsDateNumbers.bigDecimalToDouble(documento.getImpBollettinoTotaleDocumento()) + " diverso da totale importo tributo sui tributi " + GenericsDateNumbers.bigDecimalToDouble(impTotTributi));
    	
    	//inizio LP PG200360
    	if(flagOperazione.equalsIgnoreCase("I")) {
    	//fine LP PG200360    		
	    	//Verifica su identificativoFlusso
	    	//Non sono ammessi inserimenti o variazioni per identificativiFlusso per cui  stata avviata la stampa avviso
	    	String flagElabStampaAvviso = archivioCarichiDao.getFlagElabStampaAvviso(configurazione.getIdentificativoFlusso());
	    	if (!flagElabStampaAvviso.equals("") && !flagElabStampaAvviso.equals("N")) 
	    		throw new ConfigurazioneException("identificativoFlusso " + configurazione.getIdentificativoFlusso() + " non valido per fase di stampa avviata");
	    	//inizio LP PG200360
    	}
    	//fine LP PG200360    	

    	check = true;
    	return check;
    }
    	    
	//inizio LP PG21XX04 Leak
    //private void closeConnection(Connection connection) {
    //	if (connection != null)
    //		DAOHelper.closeIgnoringException(connection);
    //}
	//fine LP PG21XX04 Leak
	
	private ArchivioCarichiRuolo prepareArchivioCarichiRuolo (String codiceUtente, String tipoServizio, String codiceEnte, String tipoUfficio, String codiceUfficio, String impostaServizio) {
		ArchivioCarichiRuolo ruoloArch = new ArchivioCarichiRuolo();
		ruoloArch.setCodiceUtente(codiceUtente);
		ruoloArch.setTipoServizio(tipoServizio);
		ruoloArch.setCodiceEnte(codiceEnte);
		ruoloArch.setTipoUfficio(tipoUfficio);
		ruoloArch.setCodiceUfficio(codiceUfficio);
		ruoloArch.setImpostaServizio(impostaServizio);
		return ruoloArch;
	}
	
	private ArchivioCarichiAnagrafica prepareArchivioCarichiAnagrafica (String codiceUtente, String tipoServizio, String codiceEnte, String tipoUfficio, String codiceUfficio, String impostaServizio, String codiceFiscale) {
		ArchivioCarichiAnagrafica anaArch = new ArchivioCarichiAnagrafica();
		anaArch.setCodiceUtente(codiceUtente);
		anaArch.setTipoServizio(tipoServizio);
		anaArch.setCodiceEnte(codiceEnte);
		anaArch.setTipoUfficio(tipoUfficio);
		anaArch.setCodiceUfficio(codiceUfficio);
		anaArch.setImpostaServizio(impostaServizio);
		anaArch.setCodiceFiscale(codiceFiscale);
		return anaArch;
	}

	private ArchivioCarichiDocumento prepareArchivioCarichiDocumento (String codiceUtente, String tipoServizio, String codiceEnte, String tipoUfficio, String codiceUfficio, String impostaServizio, String numeroDocumento) {
		ArchivioCarichiDocumento docArch = new ArchivioCarichiDocumento();
		docArch.setCodiceUtente(codiceUtente);
		docArch.setTipoServizio(tipoServizio);
		docArch.setCodiceEnte(codiceEnte);
		docArch.setTipoUfficio(tipoUfficio);
		docArch.setCodiceUfficio(codiceUfficio);
		docArch.setImpostaServizio(impostaServizio);
		docArch.setNumeroDocumento(numeroDocumento);
		return docArch;
	}

	/*
	private ArchivioCarichiScadenza prepareArchivioCarichiScadenza (String codiceUtente, String tipoServizio, String codiceEnte, String tipoUfficio, String codiceUfficio, String impostaServizio, String numeroDocumento, Integer numeroRata) {
		ArchivioCarichiScadenza scadArch = new ArchivioCarichiScadenza();
		scadArch.setCodiceUtente(codiceUtente);
		scadArch.setTipoServizio(tipoServizio);
		scadArch.setCodiceEnte(codiceEnte);
		scadArch.setTipoUfficio(tipoUfficio);
		scadArch.setCodiceUfficio(codiceUfficio);
		scadArch.setImpostaServizio(impostaServizio);
		scadArch.setNumeroDocumento(numeroDocumento);
		scadArch.setNumeroRata(numeroRata);
		return scadArch;
	}
	*/
	private ArchivioCarichiTributo prepareArchivioCarichiTributo (String codiceUtente, String tipoServizio, String codiceEnte, String tipoUfficio, String codiceUfficio, String impostaServizio, String numeroDocumento, String codiceTributo, String annoTributo, Integer progressivoTributo) {
		ArchivioCarichiTributo tribArch = new ArchivioCarichiTributo();
		tribArch.setCodiceUtente(codiceUtente);
		tribArch.setTipoServizio(tipoServizio);
		tribArch.setCodiceEnte(codiceEnte);
		tribArch.setTipoUfficio(tipoUfficio);
		tribArch.setCodiceUfficio(codiceUfficio);
		tribArch.setImpostaServizio(impostaServizio);
		tribArch.setNumeroDocumento(numeroDocumento);
		tribArch.setCodiceTributo(codiceTributo);
		tribArch.setAnnoTributo(annoTributo);
		tribArch.setProgressivoTributo(progressivoTributo);
		return tribArch;
	}
	
	private ArchivioCarichiMovimento prepareArchivioCarichiMovimento (String codiceUtente, String tipoServizio, String codiceEnte, String tipoUfficio, String codiceUfficio, String impostaServizio, String numeroDocumento, Integer progressivoPagamento, String tipoMovimento) {
		ArchivioCarichiMovimento movArch = new ArchivioCarichiMovimento();
		movArch.setCodiceUtente(codiceUtente);
		movArch.setTipoServizio(tipoServizio);
		movArch.setCodiceEnte(codiceEnte);
		movArch.setTipoUfficio(tipoUfficio);
		movArch.setCodiceUfficio(codiceUfficio);
		movArch.setImpostaServizio(impostaServizio);
		movArch.setNumeroDocumento(numeroDocumento);
		movArch.setProgressivoPagamento(progressivoPagamento);
		movArch.setTipoMovimento(tipoMovimento);
		return movArch;
	}
	
	private ArchivioCarichiTesta prepareArchivioCarichiTesta (int progressivoFlusso, String codiceUtente) {
		ArchivioCarichiTesta testaArch = new ArchivioCarichiTesta();
		testaArch.setProgressivoFlusso(Long.valueOf(progressivoFlusso));
		testaArch.setCodiceUtente(codiceUtente);
		return testaArch;
	}
	
	private ArchivioCarichiCoda prepareArchivioCarichiCoda (int progressivoFlusso, String codiceUtente) {
		ArchivioCarichiCoda codaArch = new ArchivioCarichiCoda();
		codaArch.setProgressivoFlusso(Long.valueOf(progressivoFlusso));
		codaArch.setCodiceUtente(codiceUtente);
		return codaArch;
	}
	
	private ConfigurazioneModello3 selectConfigurazioneModello3 (String codiceUtente, ConfigurazioneIUV configurazioneIUV) {
		ConfigurazioneModello3DaoImpl configurazioneModello3DaoImpl;
		ConfigurazioneModello3 configurazioneModello3 = new ConfigurazioneModello3();
		configurazioneModello3.setCodiceSocieta("");
		configurazioneModello3.setCodiceUtente(codiceUtente);
		configurazioneModello3.setChiaveEnte("");
		configurazioneModello3.setCodiceIdentificativoDominio(configurazioneIUV.getIdentificativoDominio());
		configurazioneModello3.setAuxDigit(configurazioneIUV.getAuxDigit());
		configurazioneModello3.setCodiceSegregazione(configurazioneIUV.getSegregationCode());
		configurazioneModello3.setCarattereDiServizio(configurazioneIUV.getCarattereServizio());
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneModello3DaoImpl = new ConfigurazioneModello3DaoImpl(getConnectionDifferito(dbSchemaCodSocieta), getSchemaDifferito(dbSchemaCodSocieta));
			conn = getConnectionDifferito(dbSchemaCodSocieta);
			configurazioneModello3DaoImpl = new ConfigurazioneModello3DaoImpl(conn, getSchemaDifferito(dbSchemaCodSocieta));
			//fine LP PG21XX04 Leak
			configurazioneModello3 = configurazioneModello3DaoImpl.select(configurazioneModello3);
		} catch (Exception e) {
			System.out.println("Errore durante il recupero della configurazione modello 3");
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return configurazioneModello3;
	}
	
	private byte[] readBytesFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return bytesArray;
    }

	//inizio LP PG200070
	private int saveLogFlussi(ElaborazioneFlussiDao dao, int prog, String codiceUtente, java.sql.Date dataFlusso,
			                  int progressivo, String procGestione, String tipoServizio, String nomeFile,
			                  Timestamp inizioElabFlusso, Timestamp fineElabFlusso, String flussiInElab,
			                  String flagElabStampaAvviso, String dbSchemaCodSocieta) throws DaoException
	{
		int ret = -1;
		try {
			int progressivoFlusso = dao.getProgressivoFlusso(nomeFile);
			if (progressivoFlusso > 0)
			prog = progressivoFlusso;
			
			if (prog <= 0) {
				ret = dao.doInsertLogFlussi(codiceUtente, dataFlusso, progressivo, procGestione, tipoServizio, nomeFile, inizioElabFlusso, fineElabFlusso, flussiInElab, flagElabStampaAvviso);
			}
			else {
				ret= dao.doUpdateLogFlussi(prog, codiceUtente, dataFlusso, progressivo, procGestione, tipoServizio, nomeFile, inizioElabFlusso, fineElabFlusso, flussiInElab, null);
			}
		} catch (Exception ex) {
			error("saveLogFlussi failed, generic error due to: ", ex);
			throw new DaoException(ex);
		}
		return ret;
	}
	//fine LP PG200070
	
	//inizio LP PG200360
	//inizio LP PG210130
	/*
	public static class DocumentoCarico extends RequestBase {
	    private Configurazione configurazione;
	    private Ruolo ruolo;
	    private Documento documento;
	    private Anagrafica anagrafica;
	    private Scadenza[] listScadenze;
	    //inizio LP PG210130
	    private DettaglioPagamento[] listPagamento;
	    private DettaglioContabile[] listContabile;
	    //fine LP PG210130
	    private Tributo[] listTributi;
	    String provinciaNascita;
	    String provinciaFiscale;
	
	    public String getProvinciaFiscale() {
	      return provinciaFiscale;
	    }
	
	    public void setProvinciaFiscale(String provinciaFiscale) {
	      this.provinciaFiscale = provinciaFiscale;
	    }
	
	    public String getProvinciaNascita() {
	      return provinciaNascita;
	    }
	
	    public void setProvinciaNascita(String provinciaNascita) {
	      this.provinciaNascita = provinciaNascita;
	    }
	
	    public Configurazione getConfigurazione() {
	      return configurazione;
	    }
	
	    public void setConfigurazione(
	        Configurazione configurazione) {
	      this.configurazione = configurazione;
	    }
	
	    public Ruolo getRuolo() {
	      return ruolo;
	    }
	
	    public void setRuolo(Ruolo ruolo) {
	      this.ruolo = ruolo;
	    }
	
	    public Documento getDocumento() {
	      return documento;
	    }
	
	    public void setDocumento(Documento documento) {
	      this.documento = documento;
	    }
	
	    public Anagrafica getAnagrafica() {
	      return anagrafica;
	    }
	
	    public void setAnagrafica(Anagrafica anagrafica) {
	      this.anagrafica = anagrafica;
	    }
	
	    public Scadenza[] getListScadenze() {
	      return listScadenze;
	    }
	
	    public void setListScadenze(Scadenza[] listScadenze) {
	      this.listScadenze = listScadenze;
	    }
	
	    //inizio LP PG210130
	    public DettaglioPagamento[] getListPagamento() {
			return listPagamento;
		}

		public void setListPagamento(DettaglioPagamento[] listPagamento) {
			this.listPagamento = listPagamento;
		}

		public DettaglioContabile[] getListContabile() {
			return listContabile;
		}

		public void setListContabile(DettaglioContabile[] listContabile) {
			this.listContabile = listContabile;
		}
	    //fine LP PG210130

		public Tributo[] getListTributi() {
	      return listTributi;
	    }
	
	    public void setListTributi(Tributo[] listTributi) {
	      this.listTributi = listTributi;
	    }
	
	  }
	  private String dateAjust(String data) {
	    if (data == null)
	      return null;

	    data = data.replace(".", "/");
	    if (data.equals("01/01/1900"))
	      data = null;
	    return data;
	  }

	  private String trimOrNull(String s) {
	    if (s != null)
	      return s.trim();
	    return null;
	  }
	  */	
	  //fine LP PG210130

	  public DocumentoCarico readDocumentoCarico
	  (
		  String codiceUtente,
		  String codiceEnte,
		  String codiceTipoUfficio,
		  String codiceUfficio,
		  String codiceSocieta,
		  String numeroDocumento,
		  String chiaveFlusso,
		  String codiceTipoServizio,
		  String codiceUtenteEnte,
		  String codiceImpostaServizio,
		  String chiaveTomb
	  ) throws RuntimeException {
    	Connection connection = null;
		DocumentoCarico documentoCarico = new DocumentoCarico();
	    documentoCarico.setCodiceUtente(codiceUtente);
	    documentoCarico.setCodiceEnte(codiceEnte);
	    documentoCarico.setTipoUfficio(codiceTipoUfficio);
	    documentoCarico.setCodiceUfficio(codiceUfficio);
	    documentoCarico.setTipoServizio(codiceTipoServizio);

	    Ruolo ruo = new Ruolo();
	    Documento doc = new Documento();
	    Anagrafica anag = new Anagrafica();
	    ConfigurazioneIUV confIuv = new ConfigurazioneIUV();
	    Configurazione conf = new Configurazione();
	    documentoCarico.setRuolo(ruo);
	    documentoCarico.setConfigurazione(conf);
	    documentoCarico.setDocumento(doc);
	    documentoCarico.setAnagrafica(anag);

	    // dati documento e anagrafica estratti dal DAO
	    EntrateBancaDatiDao controller;
		//inizio LP PG21XX04 Leak
	    WebRowSet wrsTrib = null;
		//fine LP PG21XX04 Leak
	    try {
			connection = getConnectionDifferito(dbSchemaCodSocieta);
		    controller = new EntrateBancaDatiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
		    com.seda.payer.core.bean.Documento docDAO = controller.getDettaglioDocumento2(codiceSocieta,
		        numeroDocumento, codiceUtente, chiaveFlusso, codiceTipoServizio,
		        codiceUtenteEnte, codiceTipoUfficio, codiceUfficio, codiceImpostaServizio,
		        chiaveTomb);
		    doc.setAnnoEmissione(docDAO.getAnnoEmissione());
			//inizio LP PG210130
		    //doc.setDataNotifica(dateAjust(docDAO.getDataNotifica()));
		    doc.setDataNotifica(DocumentoCarico.dateAjust(docDAO.getDataNotifica()));
		    //fine LP PG210130
		    doc.setFlagFatturazioneElettronica(docDAO.getFlagFatturazioneElettronica());
		    doc.setIbanAccredito(docDAO.getIbanAccredito());
		    doc.setIdentificativoUnivocoVersamento(docDAO.getIdentificativoUnivocoVersamento());
		    BigDecimal appoD = docDAO.getImpBollettinoTotaleDocumento();
		    appoD = appoD.movePointRight(2);
		    doc.setImpBollettinoTotaleDocumento(appoD);
			//inizio LP PG210130
		    //doc.setNumeroBollettinoPagoPA(trimOrNull(docDAO.getNumeroBollettinoPagoPA()));
		    doc.setNumeroBollettinoPagoPA(DocumentoCarico.trimOrNull(docDAO.getNumeroBollettinoPagoPA()));
		    //fine LP PG210130
		    doc.setNumeroDocumento(docDAO.getNumeroDocumento());
			//inizio LP PG210130
		    //doc.setNumeroEmissione(trimOrNull(docDAO.getNumeroEmissione()));
		    doc.setNumeroEmissione(DocumentoCarico.trimOrNull(docDAO.getNumeroEmissione()));
		    //fine LP PG210130
		    doc.setIbanAppoggio(docDAO.getIbanAppoggio()); // PG200140
		    doc.setCausale(docDAO.getCausale());
		    doc.setTassonomia(docDAO.getTassonomia());
		    
		    String appo = docDAO.getFlagGenerazioneIUV();
		    appo = (appo.length() == 0 ? "N" : appo);
		    conf.setFlagGenerazioneIUV(appo);
		    //if(!appo.equalsIgnoreCase("N")) {
			    confIuv.setIdentificativoDominio(docDAO.getIdDominio());
			    confIuv.setAuxDigit(docDAO.getAuxDigit());
			    confIuv.setApplicationCode(docDAO.getApplCode());
			    confIuv.setSegregationCode(docDAO.getSegrCode());
			    confIuv.setCarattereServizio(docDAO.getCarattServizio());
			    conf.setConfigurazioneIUV(confIuv);
		    //}
		    appo = docDAO.getFlagStampaAvviso();
	    	appo = (appo.length() == 0 ? "N" : appo);
		    conf.setFlagStampaAvviso(appo);
		    conf.setIdentificativoFlusso(docDAO.getNomeFlusso());
	
		    anag.setCodiceFiscale(docDAO.getAnaFiscale());
		    anag.setDenominazione(docDAO.getAnaDenom());
		    anag.setTipoAnagrafica(docDAO.getAnaTipoAnag());
			//inizio LP PG210130
		    //anag.setCodiceBelfioreComuneNascita(trimOrNull(docDAO.getAnBelfNascita()));
		    anag.setCodiceBelfioreComuneNascita(DocumentoCarico.trimOrNull(docDAO.getAnBelfNascita()));
		    //fine LP PG210130

		    //inizio LP PG210130
		    //anag.setDataNascita(dateAjust(docDAO.getAnaDataNascita()));
		    anag.setDataNascita(DocumentoCarico.dateAjust(docDAO.getAnaDataNascita()));
		    //fine LP PG210130
		    anag.setIndirizzoFiscale(docDAO.getAnaIndirizzo());
			//inizio LP PG210130
		    //anag.setCodiceBelfioreComuneFiscale(trimOrNull(docDAO.getAnaFiscaleAlt()));
		    anag.setCodiceBelfioreComuneFiscale(DocumentoCarico.trimOrNull(docDAO.getAnaFiscaleAlt()));
		    //fine LP PG210130
		    anag.setEmail(docDAO.getAnaMail());
		    anag.setEmailPec(docDAO.getAnaMailPec());
		    documentoCarico.setProvinciaNascita(docDAO.getProvinciaNascita());
		    documentoCarico.setProvinciaFiscale(docDAO.getProvinciaFiscale());
	
		    ruo.setDescrizioneImpostaServizio(docDAO.getDescImpostaServizio());
		    ruo.setCodiceTipologiaServizio(docDAO.getCodTipologiaServizio());
		    ruo.setDescrizioneTipologiaServizio(docDAO.getDescTipologiaServizio());
	
		    Scadenza[] scadenze = new Scadenza[docDAO.getScad().size()];
		    int i = 0;
		    for (com.seda.payer.core.bean.Scadenza sca : docDAO.getScad()) {
		      Scadenza scadenza = new Scadenza();
		      //inizio LP PG210130
		      //scadenza.setDataScadenzaRata(dateAjust(sca.getDataScadenzaRata()));
		      //scadenza.setIdentificativoUnivocoVersamento(trimOrNull(sca.getIdentificativoUnivocoVersamento()));
		      scadenza.setDataScadenzaRata(DocumentoCarico.dateAjust(sca.getDataScadenzaRata()));
		      scadenza.setIdentificativoUnivocoVersamento(DocumentoCarico.trimOrNull(sca.getIdentificativoUnivocoVersamento()));
		      //fine LP PG210130
		      BigDecimal appoDs = sca.getImpBollettinoRata();
		      appoDs = appoDs.movePointRight(2);
		      scadenza.setImpBollettinoRata(appoDs);
		      scadenza.setNumeroBollettinoPagoPA(sca.getNumeroBollettinoPagoPA());
		      scadenza.setNumeroRata(sca.getNumeroRata());
	
		      scadenze[i++] = scadenza;
		    }
		    documentoCarico.setListScadenze(scadenze);
	
		    String listaxml = "";
		    EntrateTributiPage dto = new EntrateTributiPage();
		    
		    dto.setCodiceEnte(codiceEnte);
		    dto.setCodiceSocieta(codiceSocieta);
		    dto.setCodiceUfficio(codiceUfficio);
		    dto.setCodiceUtente(codiceUtente);
		    dto.setImpostaServizio(codiceImpostaServizio);
		    dto.setNumeroDocumento(numeroDocumento);
		    dto.setTipoUfficio(codiceTipoUfficio);
			dto = controller.getListaTributi(dto, "", 0, 0);
			listaxml = dto.getListXml();
		    ArrayList<Tributo> tributi = new ArrayList<Tributo>();
			//inizio LP PG21XX04 Leak
		    //WebRowSet wrsTrib = Convert.stringToWebRowSet(listaxml);
		    wrsTrib = Convert.stringToWebRowSet(listaxml);
			//fine LP PG21XX04 Leak
		    while (wrsTrib.next()) {
			    Tributo tributo = new Tributo();
			    tributo.setProgressivoTributo(wrsTrib.getInt(1));
			    tributo.setCodiceTributo(wrsTrib.getString(2));
			    tributo.setAnnoTributo(wrsTrib.getString(3));
			    BigDecimal appoDt = wrsTrib.getBigDecimal(4);
			    appoDt = appoDt.movePointRight(2);
			    tributo.setImpTributo(appoDt);
			    tributo.setNoteTributo(wrsTrib.getString(7));
			    tributo.setCodiceCapitolo(wrsTrib.getString(14));
			    tributo.setAccertamento(wrsTrib.getString(15));
		        //inizio LP PG210130
		        tributo.setArticolo(wrsTrib.getString(16));
		        tributo.setIdentificativoDominio(wrsTrib.getString(17));
		        tributo.setIBANBancario(wrsTrib.getString(18));
		        tributo.setIBANPostale(wrsTrib.getString(19));
		        
		        //inizio SB PAGONET-537
		        tributo.setMetadatiPagoPATariTefaKey(wrsTrib.getString(21));
		        tributo.setMetadatiPagoPATariTefaValue(wrsTrib.getString(22));
		        //fine SB PAGONET-537
		        
		        //fine LP PG210130
			    tributi.add(tributo);
			}
		    documentoCarico.setListTributi(tributi.toArray(new Tributo[tributi.size()]));
	    } catch (Exception e) {
	    	e.printStackTrace();
		    throw new RuntimeException(e);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(wrsTrib != null) {
	    			wrsTrib.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}   
	    return documentoCarico;
	  }
	
	  /*
	  boolean updateEC(
		  boolean bUpdateLog,
		  boolean bFlagStampaAvvisoVar,
		  boolean bTassonomiaVar,
		  boolean bImportoVar,
		  boolean bScadenzeVar,
		  boolean bTributiVar,
		  String codiceUtente,
		  String tipoServizio,
		  String codiceEnte,
		  String tipoUfficio,
		  String codiceUfficio,
		  String impostaServizio,
		  Documento documento,
		  Configurazione configurazione,
		  Scadenza[] scadenze,
		  Tributo[] tributi
	  ) {
		logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - inizio updateEC");
		boolean bOk = false;
		Connection connection = null;
		ElaborazioneFlussiDao elaborazioneFlussiDao = null;
		ArchivioCarichiDao archivioCarichiDao = null;
		try {
			connection = getConnectionDifferito(dbSchemaCodSocieta);
			connection.setAutoCommit(false);
			elaborazioneFlussiDao = new ElaborazioneFlussiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
        	archivioCarichiDao = new ArchivioCarichiDao(connection, getSchemaDifferito(dbSchemaCodSocieta));
			
			String fileNameToElab =  configurazione.getIdentificativoFlusso();
			int prog = elaborazioneFlussiDao.getProgressivoFlusso(fileNameToElab);
			Calendar calCurrentDate = Calendar.getInstance();
			String dataFlusso = GenericsDateNumbers.calendarToString(calCurrentDate, "yyyy-MM-dd");		//data flusso nel formato AAAA-MM-GG
			if(bUpdateLog) {
				//update log flussi
				elaborazioneFlussiDao.doUpdateLogFlussi(prog, codiceUtente, java.sql.Date.valueOf(dataFlusso), 0, "PER", "EP", fileNameToElab, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Y", "Y");
			}
			if(bFlagStampaAvvisoVar) {
				ArchivioCarichiTesta testaIn = prepareArchivioCarichiTesta(prog, codiceUtente);
				ArchivioCarichiTesta testaOut = archivioCarichiDao.getTesta(testaIn);				
				testaOut.setCodiceUtente(codiceUtente);
				testaOut.setDataCreazioneFlusso(java.sql.Date.valueOf(dataFlusso));
				testaOut.setFlagStampaAvviso(configurazione.getFlagStampaAvviso());
				archivioCarichiDao.updateTesta(testaOut);
			}
			if(bTassonomiaVar || bImportoVar) {
				//Variazione su EH1 di tassonomia e/o importototale
				ArchivioCarichiDocumento dto = new ArchivioCarichiDocumento();
				dto.setCodiceUtente(codiceUtente);
				dto.setTipoServizio(tipoServizio);
				dto.setCodiceEnte(codiceEnte);
				dto.setTipoUfficio(tipoUfficio);
				dto.setCodiceUfficio(codiceUfficio);
				dto.setImpostaServizio(impostaServizio);
				dto.setNumeroDocumento(documento.getNumeroDocumento());
				if(bImportoVar) {
					BigDecimal appoD = documento.getImpBollettinoTotaleDocumento();
					appoD = appoD.movePointLeft(2);
					dto.setImpBollettinoTotaleDocumento(appoD);
				}
				if(bTassonomiaVar) {
					dto.setTassonomia(documento.getTassonomia());
				}
				archivioCarichiDao.updateImportoTassonomiaDocumento(dto);
			}
			if(bScadenzeVar) {
				for(int k = 0; k < scadenze.length; k++) {
					//Variazione su EH2 degli importo bollettino
					ArchivioCarichiScadenza dto = new ArchivioCarichiScadenza();
					dto.setCodiceUtente(codiceUtente);
					dto.setTipoServizio(tipoServizio);
					dto.setCodiceEnte(codiceEnte);
					dto.setTipoUfficio(tipoUfficio);
					dto.setCodiceUfficio(codiceUfficio);
					dto.setImpostaServizio(impostaServizio);
					dto.setNumeroDocumento(documento.getNumeroDocumento());
					dto.setNumeroRata(scadenze[k].getNumeroRata());
					BigDecimal appoD = scadenze[k].getImpBollettinoRata(); 
					appoD = appoD.movePointLeft(2);
					dto.setImpBollettinoRata(appoD);
					archivioCarichiDao.updateImportoScadenza(dto);
				}
			}
			if(bTributiVar) {
				for(int k = 0; k < tributi.length; k++) {
					//Variazione su EH7 degli importo tributo
					ArchivioCarichiTributo dto = new ArchivioCarichiTributo();
					dto.setCodiceUtente(codiceUtente);
					dto.setTipoServizio(tipoServizio);
					dto.setCodiceEnte(codiceEnte);
					dto.setTipoUfficio(tipoUfficio);
					dto.setCodiceUfficio(codiceUfficio);
					dto.setImpostaServizio(impostaServizio);
					dto.setNumeroDocumento(documento.getNumeroDocumento());
					dto.setAnnoTributo(tributi[k].getAnnoTributo());
					dto.setCodiceTributo(tributi[k].getCodiceTributo());
					dto.setProgressivoTributo(tributi[k].getProgressivoTributo());
					BigDecimal appoD = tributi[k].getImpTributo(); 
					appoD = appoD.movePointLeft(2);
					dto.setImpTributo(appoD);
					archivioCarichiDao.updateImportoTributo(dto);
				}
			}
			logger.debug("com.esed.payer.archiviocarichi.webservice.integraecdifferito - fine updateEC");
		} catch (Exception e) {
			error("updateEC failed, generic error to: ", e);
			e.printStackTrace();
			if(connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					error("updateEC failed, generic error in roolback to: ", e1);
					e1.printStackTrace();
				}
				closeConnection(connection);
				connection = null;
			}
		} finally {
			if(connection != null) {
				try {
					connection.commit();
					bOk = true;
				} catch (SQLException e1) {
					error("updateEC failed, generic error in commit to: ", e1);
					e1.printStackTrace();
				}
				closeConnection(connection);
			}
		}
		return bOk;
	  }
      */
	  private String getNewIdentificativoFlusso(String paramCodiceUtente)
	  {
		Calendar calCurrentDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String identificativoFlussoNew = "EH" + paramCodiceUtente + dateFormat.format(calCurrentDate.getTime()) + ".txt"; 
		return identificativoFlussoNew;
	  }
	  
	//fine LP PG200360
	//inizio LP PG210130
	private Map<String, List<Tributo>> otherCheck(
			String codiceUtente,
			String codiceEnte,
			String tipoUfficio,
			String codiceUfficio,
			Configurazione configurazione,
			Documento documento,
			Scadenza[] listScadenze,
			Tributo[] listTributi
			, String codiceTipologiaServizioRuolo //LP PG22XX05
		) throws ValidazioneException {
		Map<String, List<Tributo>> listDocTrib = new HashMap<String, List<Tributo>>();
		List<Tributo> lstTrib = null;
		Map<String, List<String>> listTerneTrib = new HashMap<String, List<String>>();
		List<String> lstTerne = null;
		String idDominioTestata = null;
		String ibanBancarioTestata = documento.getIbanAccredito();
		String ibanPostaleTestata = (documento.getIbanAppoggio() != null ? documento.getIbanAppoggio().trim() : ""); 
		String idDominioLoc = "";
		String idDominioEsteso = "";
		int idxCanalePagamentoEntePrincipale = -1; //LP PG22XX05
		boolean bEntePrincipalePresente = false;
		boolean bTerneNonCongrue = false;
		int iTrib = 0;
		if(ibanPostaleTestata == null)
			ibanPostaleTestata = "";
		else {
			if(!GenericsDateNumbers.isIbanPostale(ibanPostaleTestata))
				ibanPostaleTestata = "";
		}
		if (configurazione.getConfigurazioneIUV() != null) {
			idDominioTestata = configurazione.getConfigurazioneIUV().getIdentificativoDominio();
		} else {
			try {
				idDominioTestata = archivioCarichiDao.getCodiceFiscaleEnte(codiceUtente, codiceEnte, tipoUfficio, codiceUfficio);
			} catch (DaoException e) {
			}
		}
		if(idDominioTestata != null)
			idDominioTestata = idDominioTestata.trim();
		else
			throw new ValidazioneException("documento " + documento.getNumeroDocumento() + " errore in determina codice fiscale ente");
		for (Tributo tributo: listTributi) {
			iTrib++;
    		if(tributo.getIdentificativoDominio() == null || tributo.getIdentificativoDominio().length() == 0) {
	    		if(tributo.getIBANBancario() == null || tributo.getIBANBancario().length() == 0) {
	    			tributo.setIBANBancario(ibanBancarioTestata);
	    		}
	    		if(tributo.getIBANPostale() == null || tributo.getIBANPostale().length() == 0) {
	    			tributo.setIBANPostale(ibanPostaleTestata);
	    		}
    			tributo.setIdentificativoDominio(idDominioTestata);
    		} else {
	    		if(tributo.getIBANBancario() == null || tributo.getIBANBancario().length() == 0) {
    				throw new ValidazioneException("documento " + documento.getNumeroDocumento() + " nel tributo #" + iTrib + " non e' stato valorizzato l'IBAN Bancario");
	    		}
    		}
			String idDominio = tributo.getIdentificativoDominio();
    		//inizio LP PG22XX05
			String codiceTipologiaServizio = "";
    		if(tributo.getCodiceTipologiaServizio() == null || tributo.getCodiceTipologiaServizio().length() == 0) {
    			codiceTipologiaServizio = codiceTipologiaServizioRuolo;
    			tributo.setCodiceTipologiaServizio(codiceTipologiaServizioRuolo);
    		} else {
	    		codiceTipologiaServizio = tributo.getCodiceTipologiaServizio();
	    		if(idDominioTestata.equalsIgnoreCase(idDominio) && !codiceTipologiaServizio.equalsIgnoreCase(codiceTipologiaServizioRuolo))
					throw new ValidazioneException("documento " + documento.getNumeroDocumento() + " nel tributo #" + iTrib + " il codice tipologia servizio non corrisponde a quello dichiarato nella testata del documento");
    		}
    		//fine LP PG22XX05
			idDominioLoc = idDominio;
			idDominioEsteso = idDominio;
    		//inizio LP PG22XX05
			idDominioEsteso += "_" + tributo.getCodiceTipologiaServizio();
    		//fine LP PG22XX05
			idDominioEsteso += "_" + tributo.getIBANBancario();
			idDominioEsteso += "_" + (tributo.getIBANPostale() != null ? tributo.getIBANPostale() : "");
			if(listDocTrib.containsKey(idDominio)) {
				lstTrib = listDocTrib.get(idDominio);
    			lstTrib.add(tributo);
			} else {
				lstTrib = new ArrayList<Tributo>();
				lstTrib.add(tributo);
				listDocTrib.put(idDominio, lstTrib);
				if(idDominioLoc.equals(idDominioTestata))
					bEntePrincipalePresente = true;
				//inizio LP PG22XX05
				checkEntiCode(lstEnti, codiceUtente, idDominio, documento.getNumeroDocumento(), iTrib);
				//fine LP PG22XX05
			}
			if(listTerneTrib.containsKey(idDominio)) {
				lstTerne = listTerneTrib.get(idDominio);
				if(!lstTerne.contains(idDominioEsteso)) {
		    		//inizio LP PG22XX05
					//logger.debug("documento " + documento.getNumeroDocumento() + " sono presenti tributi che hanno valori diversi degli 'iban' a parita' di iddominio (" + idDominio + ")");
					logger.debug("documento " + documento.getNumeroDocumento() + " sono presenti tributi che hanno valori diversi degli 'iban' e/o 'codice tipologia servizio' a parita' di iddominio (" + idDominio + ")");
					//fine LP PG210130
					bTerneNonCongrue = true;
				}
			} else {	
	    		//inizio LP PG22XX05
				int idxLoc = -1;
				if(!idDominio.equals(idDominioTestata))
					idxLoc = idxCanalePagamentoEntePrincipale;
				idxLoc = checkFunEntiCode(lstFunEnti, codiceUtente, codiceEnte, idDominio,  documento.getNumeroDocumento(), tributo.getCodiceTipologiaServizio(), iTrib, idxLoc);
				if(idDominio.equals(idDominioTestata))
					idxCanalePagamentoEntePrincipale = idxLoc;
	    		//fine LP PG22XX05
				lstTerne = new ArrayList<String>();
				lstTerne.add(idDominioEsteso);
				listTerneTrib.put(idDominio, lstTerne);
			}
    	}
		int quantiEnti = listDocTrib.size();
		if(!bEntePrincipalePresente) {
			throw new ValidazioneException("documento " + documento.getNumeroDocumento() + " non sono presenti tributi che si riferiscono all'ente dichiarato sulla testata");
		}
		if(bTerneNonCongrue) {
    		//inizio LP PG22XX05
			//throw new ValidazioneException("documento " + documento.getNumeroDocumento() + " sono presenti tributi che hanno valori diversi degli 'iban' a parita' di iddominio");
			throw new ValidazioneException("documento " + documento.getNumeroDocumento() + " sono presenti tributi che hanno valori diversi degli 'iban' e/o 'codice tipologia servizio' a parita' di iddominio");
			//fine LP PG210130
		}
    	if(quantiEnti > 2) {
			throw new ValidazioneException("documento " + documento.getNumeroDocumento() + " nei tributi non possono essere presenti un numero di enti maggiore di 2");
    	}
    	//inizio LP PG210130 Step02
    	//if(quantiEnti == 1) {
    	//	for (Tributo tributo: listTributi) {
    	//		tributo.setIBANBancario("");
    	//		tributo.setIBANPostale("");
    	//		tributo.setIdentificativoDominio("");
    	//	}
    	//}
    	//fine
    	return listDocTrib;
	}
	//fine LP PG210130

	//inizio LP PG210130 Step02
	private void checkEntiCode(List<String> lista, String codUtente, String idDominio, String documento, int iTrib) throws ValidazioneException
	{
		//inizio LP PG210130 Step04
		//Nota. Si  deciso di non controllare la presenze in anagrafica
		//      degli enti assocciati agli iddominio "secondari".
		boolean bVerificaPresenzaInAnagrafica = true; //LP PG22XX05 abilitato il controllo
		
		if(!bVerificaPresenzaInAnagrafica)
			return;
		//fine LP PG210130 Step04
		String keyEnte = null;
		if (!lista.contains(idDominio)) {
			try {
				//keyEnte = archivioCarichiDao.getKeyEnteEC(idDominio); //TODO. Basta iddominio ?
				Ente ente = enteDato.doDetailToCodFis(codUtente, idDominio, dbSchemaCodSocieta);
				keyEnte = (ente != null ? ente.getAnagEnte().getChiaveEnte() : null);
			} catch (Exception e) {
				e.printStackTrace();
				String mess = "documento " + documento + " in checkEntiCode errore: " + e.getMessage();
				logger.debug(mess);
				throw new ValidazioneException(mess);
			}
			if (keyEnte == null) {
				String mess = "documento " + documento + " il tributo #" + iTrib  + " ha un valore iddominio " + idDominio + " per il quale non e' presente l'anagrafica dell'ente";
				logger.debug(mess);
				throw new ValidazioneException(mess);
			} else
				lista.add(idDominio);
		} 
		return;
	}
	//fine LP PG210130 Step02

	//inizio LP PG22XX05
	private int checkFunEntiCode(List<String> lista, String codUtente, String codiceEnte, String idDominio, String documento, String codiceTipologiaServizio, int iTrib, int idxEntePrincipale) throws ValidazioneException
	{
		boolean bVerificaPresenzaFun = true;
		
		if(!bVerificaPresenzaFun)
			return -1;
		boolean bTrovato = false;
		int idxTrovato = -1;
		String coppia = idDominio + "_" + codiceTipologiaServizio;
		if (!lista.contains(coppia)) {
			try {
				Ente ente = enteDato.doDetailToCodFis("", idDominio, codUtente);
				if(ente != null) {
					String chiaveEnte = ente.getAnagEnte().getChiaveEnte();
					String codSocieta = ente.getUser().getCompany().getCompanyCode();
					String[] canalePagamento = { "WEB", "MOB", "PSP" };
					for(int ik = 0; ik < 3; ik++) {
						if(idxEntePrincipale == -1 || idxEntePrincipale == ik) {
							List<ConfigPagamento> beanRes = configPagamentoDao.doList(codSocieta, codUtente, chiaveEnte, canalePagamento[ik]);
							ConfigPagamentoDto confDto;
							if (beanRes != null) {
								for (ConfigPagamento conf : beanRes) {
									confDto = new ConfigPagamentoDto(conf);
									if(confDto.getCodTipologiaServizio().equals(codiceTipologiaServizio)) {
										bTrovato = true;
										idxTrovato = ik;
										break;
									}
								}
							}
							if(bTrovato)
								break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				String mess = "documento " + documento + " in checkFunEntiCode errore: " + e.getMessage();
				logger.debug(mess);
				throw new ValidazioneException(mess);
			}

			if (!bTrovato ) {
				String mess = "documento " + documento + " il tributo #" + iTrib  + " ha un valore codice tipologia servizio " + codiceTipologiaServizio + " per il quale non e' presente la configurazione per l'ente";
				logger.debug(mess);
				throw new ValidazioneException(mess);
			} else
				lista.add(coppia);
		} 
		return idxTrovato;
	}
	
	private void ClearInserimentoEC(InserimentoEcRequest in)
	{
		if(in.getImpostaServizio() != null) {
			String prima = in.getImpostaServizio();
			String dopo = VerificaCodiceFiscale.ClearString(prima);
			if(!prima.equals(dopo)) {
				in.setImpostaServizio(dopo);
				String mess = "imposta servizio prima: '" + prima + "' dopo: '" + dopo + "'";
				logger.debug(mess);
			}
		}
		if(in.getTipoServizio() != null) {
			String prima = in.getTipoServizio();
			String dopo = VerificaCodiceFiscale.ClearString(prima);
			if(!prima.equals(dopo)) {
				String mess = "tipologia servizio prima: '" + prima + "' dopo: '" + dopo + "'";
				logger.debug(mess);
				in.setTipoServizio(dopo);
			}
		}
		if(in.getRuolo() != null && in.getRuolo().getCodiceTipologiaServizio() != null) {
			String prima = in.getRuolo().getCodiceTipologiaServizio();
			String dopo = VerificaCodiceFiscale.ClearString(prima);
			if(!prima.equals(dopo)) {
				in. getRuolo().setCodiceTipologiaServizio(dopo);
				String mess = "tipologia servizio ruolo prima: '" + prima + "' dopo: '" + dopo + "'";
				logger.debug(mess);
			}
		}
		int k = 0;
		for (Tributo curr : in.getListTributi()) {
			k++;
			if(curr.getCodiceTipologiaServizio() != null) {
				String prima = curr.getCodiceTipologiaServizio();
				String dopo = VerificaCodiceFiscale.ClearString(prima);
				if(!prima.equals(dopo)) {
					curr.setCodiceTipologiaServizio(dopo);
					String mess = "tipologia servizio tributo #" + k + " prima: '" + prima + "' dopo: '" + dopo + "'";
					logger.debug(mess);
				}
			}
		}
		//TODO: altri attributi su cui fare la clean ?
	}

	private void ClearVariazioneEC(VariazioneEcRequest in)
	{
		if(in.getImpostaServizio() != null) {
			String prima = in.getImpostaServizio();
			String dopo = VerificaCodiceFiscale.ClearString(prima);
			if(!prima.equals(dopo)) {
				in.setImpostaServizio(dopo);
				String mess = "imposta servizio prima: '" + prima + "' dopo: '" + dopo + "'";
				logger.debug(mess);
			}
		}
		if(in.getTipoServizio() != null) {
			String prima = in.getTipoServizio();
			String dopo = VerificaCodiceFiscale.ClearString(prima);
			if(!prima.equals(dopo)) {
				String mess = "tipologia servizio prima: '" + prima + "' dopo: '" + dopo + "'";
				logger.debug(mess);
				in.setTipoServizio(dopo);
			}
		}
		if(in.getRuolo() != null && in.getRuolo().getCodiceTipologiaServizio() != null) {
			String prima = in.getRuolo().getCodiceTipologiaServizio();
			String dopo = VerificaCodiceFiscale.ClearString(prima);
			if(!prima.equals(dopo)) {
				in. getRuolo().setCodiceTipologiaServizio(dopo);
				String mess = "tipologia servizio ruolo prima: '" + prima + "' dopo: '" + dopo + "'";
				logger.debug(mess);
			}
		}
		int k = 0;
		for (Tributo curr : in.getListTributi()) {
			k++;
			if(curr.getCodiceTipologiaServizio() != null) {
				String prima = curr.getCodiceTipologiaServizio();
				String dopo = VerificaCodiceFiscale.ClearString(prima);
				if(!prima.equals(dopo)) {
					curr.setCodiceTipologiaServizio(dopo);
					String mess = "tipologia servizio tributo #" + k + " prima: '" + prima + "' dopo: '" + dopo + "'";
					logger.debug(mess);
				}
			}
		}
		//TODO: altri attributi su cui fare la clean ?
	}
	
	//fine LP PG22XX05
	
	private String formattaCodiceAvviso(String codAut) {
		int intervallo = 4;
		char separatore = ' ';
		StringBuilder formattatore = new StringBuilder(codAut);
		for(int i = 0; i < codAut.length()/intervallo; i++) {
			formattatore.insert(((i + 1) * intervallo) + i, separatore);
		}
		String codAutFormattato = formattatore.toString();
		if(codAutFormattato.charAt(codAutFormattato.length()-1) == separatore) codAutFormattato = codAutFormattato.substring(0, codAutFormattato.length()-1);

		return codAutFormattato;
	}

	// SR 10082023 inizio
	private boolean isValidResult(String[] sEsito) {
		if (sEsito != null && sEsito.length > 0 && isValidResult(sEsito[0]))
			return true;
		else
			return false;
	}

	private boolean isValidResult(String sRetCode) {
		if (sRetCode != null && (sRetCode.equals("00") || sRetCode.equals("OK")))
			return true;
		else
			return false;
	}

	private String getCurrentDate() {
		Calendar calNow = Calendar.getInstance();
		java.util.Date dateNow = calNow.getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		String strDate = df.format(dateNow);
		return strDate; 
	}
	
	private void extendDatiContabileEPagamento(RecuperaDatiBollettinoResponse pgResponse, CachedRowSet ecCached, CachedRowSet ecCachedDettaglioPagamento, CachedRowSet ecCachedDettaglioContabile, String idDominioEntePrincipale, int cosa) throws DaoException{
		String idDominioLoc = "";
    	pgResponse.setFlagMultiBeneficiario(false);
    	try {
			if(ecCachedDettaglioContabile != null && ecCachedDettaglioContabile.size() > 0) {
				List<DettaglioContabile> lstCont = new ArrayList<>();  
				if(ecCachedDettaglioContabile.first()) {
					do {
						if(matchKey(ecCached, ecCachedDettaglioContabile, cosa)) {
							DettaglioContabile dettContabile = new DettaglioContabile();
							int ind = 8;
							dettContabile.setCodiceContabilita(trimOrBlank(ecCachedDettaglioContabile.getString(++ind)));
							dettContabile.setImporto(ecCachedDettaglioContabile.getBigDecimal(++ind));
							if(dettContabile.getImporto().compareTo(BigDecimal.ZERO) == 1) {
								dettContabile.setCapitolo(trimOrBlank(ecCachedDettaglioContabile.getString(++ind)));
								dettContabile.setArticolo(trimOrBlank(ecCachedDettaglioContabile.getString(++ind)));
								dettContabile.setAnnoCompetenza(trimOrBlank(ecCachedDettaglioContabile.getString(++ind)));
								idDominioLoc = trimOrBlank(ecCachedDettaglioContabile.getString(++ind));
								lstCont.add(dettContabile);
							}	
						}
					} while(ecCachedDettaglioContabile.next());
				}
				int qCont = lstCont.size(); 
				if(qCont >= 1) {
//					com.seda.payer.integraente.webservice.dati.DettaglioContabile[] dettaglioContabile = lstCont.toArray(new com.seda.payer.integraente.webservice.dati.DettaglioContabile[qCont]);
					pgResponse.setDettaglioContabile(new com.seda.payer.integraente.webservice.dati.DettaglioContabile[qCont]);
				}
			}
			if(ecCachedDettaglioPagamento != null && ecCachedDettaglioPagamento.size() > 0) {
				int iPag = 0;
				boolean metaDati = false;
				List<com.seda.payer.integraente.webservice.dati.DettaglioPagamento> lstPag = new ArrayList<>();  
				if(ecCachedDettaglioPagamento.first()) {
					do {
						if(matchKey(ecCached, ecCachedDettaglioPagamento, cosa)) {
							com.seda.payer.integraente.webservice.dati.DettaglioPagamento dettPagamento = new com.seda.payer.integraente.webservice.dati.DettaglioPagamento();
							int ind = 8;
							dettPagamento.setIdentificativoDominio(trimOrBlank(ecCachedDettaglioPagamento.getString(++ind)));
							dettPagamento.setImporto(ecCachedDettaglioPagamento.getBigDecimal(++ind));
							if(dettPagamento.getImporto().compareTo(BigDecimal.ZERO) == 1) {
								dettPagamento.setIBANBancario(trimOrBlank(ecCachedDettaglioPagamento.getString(++ind)));
								dettPagamento.setIBANPostale(trimOrBlank(ecCachedDettaglioPagamento.getString(++ind)));
								try {
									String appo = ecCachedDettaglioPagamento.getString(++ind);
									dettPagamento.setCodiceTipologiaServizio(trimOrBlank(appo));
									if(dettPagamento.getCodiceTipologiaServizio().length() == 0)
										dettPagamento.setCodiceTipologiaServizio(pgResponse.getTipologiaServizio());
								} catch (Exception e) {
									dettPagamento.setCodiceTipologiaServizio(pgResponse.getTipologiaServizio());
								}
								dettPagamento.setMetadatiPagoPATariTefaKey(trimOrBlank(ecCachedDettaglioPagamento.getString(++ind)));
								dettPagamento.setMetadatiPagoPATariTefaValue(trimOrBlank(ecCachedDettaglioPagamento.getString(++ind)));
								metaDati=!dettPagamento.getMetadatiPagoPATariTefaKey().equals("");
								lstPag.add(dettPagamento);
								if(++iPag > 2) {
									throw new DaoException(998, "Errore in bollettino multi-beneficiario sono presenti piu' di 2 dettaglio pagamento.");
								}
							}	
						}
					} while(ecCachedDettaglioPagamento.next());
				}
				
				int qPag = lstPag.size(); 
				if(qPag >= 2 || metaDati) { 
					boolean bOk = false;
					for(com.seda.payer.integraente.webservice.dati.DettaglioPagamento dettPagamento : lstPag) {
						if(dettPagamento.getIdentificativoDominio().equals(idDominioEntePrincipale)) {
							bOk = true;
							break;
						}
					}
					if(bOk) {
						pgResponse.setFlagMultiBeneficiario(true);
						com.seda.payer.integraente.webservice.dati.DettaglioPagamento[] dettaglioPagamento = null;
						boolean bOrd = false;
						for(com.seda.payer.integraente.webservice.dati.DettaglioPagamento dettPagamento : lstPag) {
							bOrd = dettPagamento.getIdentificativoDominio().equals(idDominioEntePrincipale);
							break;
						}
						if(bOrd) {
							dettaglioPagamento = lstPag.toArray(new com.seda.payer.integraente.webservice.dati.DettaglioPagamento[qPag]);
						} else {
							dettaglioPagamento = new com.seda.payer.integraente.webservice.dati.DettaglioPagamento[qPag];
							int ik = 1;
							for(com.seda.payer.integraente.webservice.dati.DettaglioPagamento dettPagamento : lstPag) {
								dettaglioPagamento[ik] = dettPagamento;
								ik--;
							}
						}
						pgResponse.setDettaglioImporti(dettaglioPagamento); 
					} else {
						pgResponse.setFlagMultiBeneficiario(false);
					}
				}
			}
    	} catch (DaoException e) {
			e.printStackTrace();
			error(e.toString());
			throw new DaoException(e.getErrorCode(), e.getMessage());
    	} catch (Exception e) {
			e.printStackTrace();
			error(e.toString());
		}
    }
	

	public static String trimOrBlank(String s) {
		if (s != null)
			return s.trim();
		return "";
	}
	
	private boolean matchKey(CachedRowSet ecCached, CachedRowSet ecCachedDettaglio, int cosa) {
		if(cosa == 0)
			return matchKeyDoc(ecCached, ecCachedDettaglio);
		else
			return matchKeyScadenze(ecCached, ecCachedDettaglio);
	}
	
	private boolean matchKeyDoc(CachedRowSet ecCached, CachedRowSet ecCachedDettaglio) {
		boolean bdebug = false;
		if(bdebug)
			System.out.println("inizio matchKeyDoc");
		int ind = 0;
		try {
			int ind0 = 0;
			if(bdebug) {
				System.out.println("# " + ind0  + ":  1 '" + ecCached.getString(1).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  2 '" + ecCached.getString(2).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  3 '" + ecCached.getString(3).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  4 '" + ecCached.getString(4).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  6 '" + ecCached.getString(6).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  9 '" + ecCached.getString(9).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ": IMPOSTASERVIZIO '" + ecCached.getString("IMPOSTASERVIZIO").trim() + "' - '" + ecCachedDettaglio.getString("IMPOSTASERVIZIO").trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ": 10 '" + ecCached.getString(10).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ": 21 '" + ecCached.getString(21).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
			}
			if(ecCached.getString(1).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(2).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(3).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(4).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(6).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(9).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(10).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(21).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			) {
				if(bdebug)
					System.out.println("fine matchKeyDoc: true");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(bdebug)
			System.out.println("fine matchKeyDoc: false # " + ind);
		return false;
	}
	
	private boolean matchKeyScadenze(CachedRowSet ecCached, CachedRowSet ecCachedDettaglio) {
		boolean bdebug = false;
		if(bdebug)
			System.out.println("inizio matchKeyScadenze");
		int ind = 0;
		try {
			int ind0 = 0;
			if(bdebug) {
				System.out.println("# " + ind0  + ":  1 '" + ecCached.getString(1).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  2 '" + ecCached.getString(2).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  3 '" + ecCached.getString(3).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  4 '" + ecCached.getString(4).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  6 '" + ecCached.getString(6).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ": 20 '" + ecCached.getString(20).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ": IMPOSTASERVIZIO '" + ecCached.getString("IMPOSTASERVIZIO").trim() + "' - '" + ecCachedDettaglio.getString("IMPOSTASERVIZIO").trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ":  8 '" + ecCached.getString(8).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
				System.out.println("# " + ind0  + ": 11 '" + ecCached.getString(11).trim() + "' - '" + ecCachedDettaglio.getString(++ind0).trim() + "' # " + ind0);
			}
			if(ecCached.getString(1).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(2).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(3).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(4).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(6).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(20).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(8).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			   && ecCached.getString(11).trim().equals(ecCachedDettaglio.getString(++ind).trim())
			) {
				if(bdebug)
					System.out.println("fine matchKeyScadenze: true");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(bdebug)
			System.out.println("fine matchKeyScadenze: false # " + ind);
		return false;
	}	
	// SR 10082023 fine

}