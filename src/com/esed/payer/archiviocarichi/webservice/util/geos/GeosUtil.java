package com.esed.payer.archiviocarichi.webservice.util.geos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.esed.payer.archiviocarichi.webservice.exception.ConfigurazioneException;
import com.esed.payer.archiviocarichi.webservice.exception.ValidazioneException;
import com.seda.commons.properties.tree.PropertiesNodeException;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.compatibility.SystemVariable;
import com.seda.payer.commons.geos.Bollettino;
import com.seda.payer.commons.geos.DatiAnagrafici;
import com.seda.payer.commons.geos.DatiCreditore;
import com.seda.payer.commons.geos.Documento;
import com.seda.payer.commons.geos.Flusso;
import com.seda.payer.commons.geos.Tributo;
import com.seda.payer.pgec.webservice.commons.dati.ConfigPagamento;
import com.seda.payer.pgec.webservice.commons.dati.ConfigPagamentoRequest;
import com.seda.payer.pgec.webservice.commons.dati.ConfigPagamentoResponse;
import com.seda.payer.pgec.webservice.commons.source.CommonsSOAPBindingStub;
import com.seda.payer.pgec.webservice.commons.source.CommonsServiceLocator;


public class GeosUtil {
	
	public static int lenEuro = 10;
	private static PropertiesTree configuration = null;
	private  Properties config = null;
	
	
	/**
	 * @implNote
	 * Inizializzazione della configurazione e valorizzazione del campo
	 * {@code configuration}
	 * 
	 * 
	 * @throws PropertiesNodeException
	 * @throws Exception
	 */
	public static void initConfiguration() throws PropertiesNodeException,Exception  {
		SystemVariable sv = new SystemVariable();
		String rootPath = sv.getSystemVariableValue("GEOSREST_WEB_ROOT");
		sv = null;

		if (rootPath != null) {
			configuration = new PropertiesTree(rootPath);
		}
	}
	
	



	public static void setConfiguration() throws Exception {
		if(configuration == null) {
			try {
				initConfiguration();
				if(configuration == null) {
					throw new ConfigurazioneException("Errore in GeosUtil getConfiguration (null)");
				}
			} catch (PropertiesNodeException e) {
				e.printStackTrace();
				throw new ConfigurazioneException("Errore in GeosUtil getConfiguration");
			}
		}
	}
	
	public static PropertiesTree getConfiguration() {
		return configuration;
	}

	public static String getUrlRestGeos(String cutecute) throws ConfigurazioneException {
		PropertiesTree config = getConfiguration();
		String key = String.format("%s.wsRest.GEOS", cutecute); 
		String urlGeos = config.getProperty(key);
		if(urlGeos == null || urlGeos.trim().length()== 0) {
			throw new ConfigurazioneException("Manca configurazione per Url WS REST GEOS");
		}
		return urlGeos.trim();
	}
	
	public static String leftZeroPad(String s, int minLen, int maxLen)
	{
		if(s.length() < minLen)
		{
			int zeri = minLen - s.length();
			return String.format("%0" + zeri + "d", 0) + s;
		}
		if(s.length() > maxLen)
		{
			return s.substring(s.length() - maxLen);
		}
		return s;
	}
	
	public static Flusso extractFlusso(ResultSet resultSet) throws SQLException, ConfigurazioneException, ValidazioneException {
		String cuteCute = resultSet.getString("FLU_CUTECUTE");
		String ente = resultSet.getString("FLU_ENTE"); 
		String tipoStampaFlu = "B";
		boolean ibanPostale = false;
		String docIban = resultSet.getString("DOC_IBAN");
		System.out.println("docIban= " + docIban);
		if (docIban == null || docIban.trim().length() < 10) {
			throw new ValidazioneException("Documento non stampabile per iban non valido");
		} else {
			if (docIban.substring(5,10).equals("07601")){
	        	ibanPostale = true;
	        	System.out.println("ibanPostale: "+ibanPostale);
	        	System.out.println("docIban.substring" + docIban.substring(5,10).equals("07601"));
	        }
		}
        String tipoIban = ibanPostale ? "POSTE" : "STANDARD";
        String tipoTemplate = getTipoTemplate(resultSet.getString("FLU_CUTECUTE"),resultSet.getString("FLU_ENTE"),resultSet.getString("DOC_TIPOLOGIA_SERVIZIO"), tipoIban);
        String tipoStampa = tipoTemplate.replace("_","");
        if (tipoStampa.equalsIgnoreCase("STANDARD"))
        	tipoStampaFlu = "B";
        if (tipoStampa.equalsIgnoreCase("POSTE"))
        	tipoStampaFlu = "P";
        //PGNTACWS-1 - inizio
        String flagMultibeneficiario = "";
        try {
        	flagMultibeneficiario = resultSet.getString("FLAG_MULTIBEN"); 
        } catch (Exception ex) {
        	ex.printStackTrace();
        } 
        if (flagMultibeneficiario!=null && flagMultibeneficiario.trim().equals("Y")) {
        	if(ibanPostale==false) {
        		tipoStampaFlu = "B";
        	}else {
        		tipoStampaFlu = "P";
        	}
        }
        //PGNTACWS-1 - fine
        String idFlusso = resultSet.getString("FLU_ID_FLUSSO"); //TODO da verificare se serve
		
        //17022021 GG Tk 2021021588000051 - inizio
        //Flusso flu = new Flusso(cuteCute, ente, tipoStampaFlu, idFlusso);
        Flusso flu = new Flusso(cuteCute, ente, tipoStampaFlu, idFlusso, "PPA");
        //17022021 GG Tk 2021021588000051 - fine
		
		return flu;
	}
	
	/**
	 * Il template da utilizzare per il flusso.
	 * @return una stringa "STANDARD_" oppure "POSTE_" 
	 * @throws ConfigurazioneException 
	 * @throws ValidazioneException 
	 **/
	public static String getTipoTemplate(String codiceUtente, String ente,	String servizio, String tipoIban) throws ConfigurazioneException, ValidazioneException {
		PropertiesTree config = getConfiguration();
		String templateKey = String.format("template.%s.%s.%s.%s", codiceUtente,ente,servizio,tipoIban); 
		String tipoTemplate = config.getProperty(templateKey);

		if("POSTE".equals(tipoIban))
			tipoTemplate = "POSTE_";
	    else
			tipoTemplate = "STANDARD_";
		
		return tipoTemplate;
	}
	
	public static Documento extractDoc(ResultSet resultSet) throws SQLException {
		
		String impostaServizio = resultSet.getString("TIPO_SERVIZIO");
		System.out.println("resultSet.getString(\"DOC_IMPORTO_TOTALE\") = " + resultSet.getString("DOC_IMPORTO_TOTALE"));
		String importoInCent = leftZeroPad(resultSet.getString("DOC_IMPORTO_TOTALE"),4,6);
		String causaleDocumento = resultSet.getString("DOC_CAUSALE");
		//Tk2021021588000051 - inizio
		//la causale deve contenere la causale del documento e solo qualora non specificata l'imposta servizio
		String oggettoPagamento = resultSet.getString("OGG_PAGAMENTO");
		String causale = oggettoPagamento;
		if (causale == null || causale.trim().length()==0) {
			causale = causaleDocumento;
		}
		else
			System.out.println("oggettoPagamento = " + oggettoPagamento);
		//Tk2021021588000051 - fine
		
		System.out.println("causale =" + causale);
		//Tk2021021588000051 - fine
		
		System.out.println("importoInCent = " + importoInCent);
		
		Documento doc = new Documento(impostaServizio, importoInCent, causale);

		return doc;
	}
	
	public static DatiAnagrafici extractAna(ResultSet resultSet) throws SQLException {
		String denominazione = resultSet.getString("DEB_NOME_COGN_RAG_SOC");
		String codFis = resultSet.getString("DEB_CODICE_FISCALE");
		String cap="";
		String indirizzo = resultSet.getString("DEB_INDIRIZZO");
		if(indirizzo.contains("|")) {
			  String[] capIndirizzo = indirizzo.split("\\|");
			  if(capIndirizzo[1].length()>0) {
					System.out.println("capIndirizzo[1] (PARTE CAP)" + capIndirizzo[1]);
					cap = capIndirizzo[1];
					indirizzo=capIndirizzo[0];
				}else {
					System.out.println("Cap DEB_CAP " + cap);
					cap = resultSet.getString("DEB_CAP");
					indirizzo=capIndirizzo[0];
				}
			}else {
				cap = resultSet.getString("DEB_CAP");
			}
		System.out.println("cap - " + cap);
		String citta = resultSet.getString("DEB_COMUNE");
		String siglaProv = resultSet.getString("DEB_PROVINCIA");
		
		DatiAnagrafici ana = new DatiAnagrafici(denominazione, codFis, indirizzo, cap, citta, siglaProv);
		
		return ana;
	}
	
	public static DatiCreditore extractCre(ResultSet resultSet,String dbSchemaCodSocieta) throws SQLException, ConfigurazioneException,Exception {
		String codFiscEnte = resultSet.getString("DOC_CFISCALE_ENTE");
		String descEnte = resultSet.getString("DOC_DESCRIZIONE_ENTE");
		if(descEnte.contains("/"))
			descEnte = descEnte.split("/")[0].trim();
		String cBill = getCbill(resultSet,dbSchemaCodSocieta);
		

		DatiCreditore cre = new DatiCreditore(codFiscEnte, descEnte, cBill);

		//QF: 2021-06-25 presa informazioni come da spontaneo
		cre.Denominazione1 = descEnte;
		cre.Denominazione2 = resultSet.getString("DOC_DESCRIZIONE_SERVIZIO");
		cre.Denominazione3 = descEnte;
		
		
		return cre;
	}
	
	public static String getBarcodeParameter1(ResultSet resultSet, String numAvviso) throws SQLException {
		String cutecute = resultSet.getString("FLU_CUTECUTE");
		String codFiscEnte=resultSet.getString("DOC_CFISCALE_ENTE");
		String auxDigit= numAvviso.substring(0, 1);
		String applicationCode = numAvviso.substring(1, 3);
		
		String barcodePar = getBarcodeParameter(cutecute, codFiscEnte, auxDigit, applicationCode);
		return barcodePar;
	}
	
	public static String getCbill(ResultSet resultSet,String dbSchemaCodSocieta) throws SQLException, ConfigurazioneException, Exception {
		ConfigPagamento configPagamento;
		PropertiesTree config = getConfiguration();
		String cutecute = resultSet.getString("FLU_CUTECUTE");
		String societa = resultSet.getString("FLU_SOCIETA");
		String codFiscEnte = resultSet.getString("DOC_CFISCALE_ENTE");
		String chiaveEnte = resultSet.getString("DOC_CHIAVE_ENTE");
		String codTipologiaServizio = resultSet.getString("DOC_TIPOLOGIA_SERVIZIO");
		String key = String.format("cbill.%s.%s.%s", cutecute,societa ,codFiscEnte.trim()); 
		String codiceCbill = config.getProperty(key);
		
		if(codiceCbill==null || codiceCbill.trim().length()==0) {
			
			String commonsWsUrl = config.getProperty("webServices.url.commons");
			
			configPagamento = recuperaListaFunzioniEnte(commonsWsUrl,societa,cutecute, chiaveEnte, "WEB", codTipologiaServizio,dbSchemaCodSocieta);
			
			if(configPagamento == null) {
		      configPagamento = recuperaListaFunzioniEnte(commonsWsUrl,societa,cutecute, chiaveEnte, "PSP", codTipologiaServizio,dbSchemaCodSocieta);
			}
			
			if(configPagamento!=null)
			  codiceCbill = configPagamento.getCbillStampaAvvisoPagoPa(); 
			
		}
		
		if(codiceCbill==null || codiceCbill.trim().length()==0)
			throw new ConfigurazioneException("Manca configurazione Codice Cbill: " + key);
		
		if(codiceCbill.trim().length()>10) {
			throw new ConfigurazioneException("Configurazione Codice Cbill maggiore di 10 caratteri: " + key);
		}
		
		codiceCbill = codiceCbill.trim();
		
		return codiceCbill;
	}
	
	public static String getCodiceAutorizzazione(ResultSet resultSet,String dbSchemaCodSocieta) throws SQLException, ConfigurazioneException,Exception {
		ConfigPagamento configPagamento;
		PropertiesTree config = getConfiguration();
		String cutecute = resultSet.getString("FLU_CUTECUTE");
		String societa = resultSet.getString("FLU_SOCIETA");
		String ente = resultSet.getString("FLU_ENTE");
		String chiaveEnte = resultSet.getString("DOC_CHIAVE_ENTE");
		String codTipologiaServizio = resultSet.getString("DOC_TIPOLOGIA_SERVIZIO");
		String key = String.format("autorizzazione.%s.%s.%s", cutecute,societa ,ente); 
		String codiceAut = config.getProperty(key);
		
		if(codiceAut==null || codiceAut.trim().length()==0) {
			
			
			String commonsWsUrl = config.getProperty("webServices.url.commons");
			
			configPagamento = recuperaListaFunzioniEnte(commonsWsUrl,societa,cutecute, chiaveEnte, "WEB", codTipologiaServizio,dbSchemaCodSocieta);
			
			if(configPagamento == null) {
		      configPagamento = recuperaListaFunzioniEnte(commonsWsUrl,societa,cutecute, chiaveEnte, "PSP", codTipologiaServizio,dbSchemaCodSocieta);
			}
			
			if(configPagamento!=null)
				codiceAut = configPagamento.getAutorizzazioneStampaAvvisoPagoPa(); 

		}
		
		if(codiceAut==null || codiceAut.trim().length()==0)
			throw new ConfigurazioneException("Manca configurazione Codice codiceAut: " + key);

	
		codiceAut = codiceAut.trim();
		return codiceAut;
	}
	
	/**
	 * In riferimento alla Stringa Barcode.<br/>
	 * dobbiamo mettere una parte di questa stringa (quella da 13 bite) su un file properties (quello del batch)
	 * L'IdDominio, corrisponde al codice fiscale presente sull'Ente che si trova sulla tabella PYENTTB.
	 * 
//	La chiave per andare a prendere il valore di questa stringa in base al "numero avviso pagoPa" di appartenenza,
//	dovrà essere:
//	                CUTECUTE.IDDOMINIO.AUX_DIGIT.APPLICATION_CODE = ciamammacomemdsaifjsdlfjld
//	 
//	Esempio:
//	                000P4.01234567890.0.01 = 01234567890123
*	AUX_DIGIT e Application code sono i primi 3 caratteri del Numero Avviso PagoPa che hai "in canna" (idbollettino)
*/
	public static String getBarcodeParameter(String cutecute, String codFiscEnte, String auxDigit, String applicationCode) {
		PropertiesTree config = getConfiguration();
		codFiscEnte=codFiscEnte.trim();
		String key = String.format("barcode.%s.%s.%s.%s", cutecute, codFiscEnte, auxDigit, applicationCode);
		return config.getProperty(key);
	}
	
	public static  Tributo extractTrib(ResultSet resultSet) throws SQLException {
		String anno = String.valueOf(resultSet.getInt("TR_ANNO_TRIBUTO"));
		String codice = resultSet.getString("TR_COD_TRIBUTO");
		String importoInCent = leftZeroPad( resultSet.getString("TR_IMP_TRIBUTO"),11,11);
		String note = resultSet.getString("TR_NOTE");
		
		Tributo trib = new Tributo(anno, codice, importoInCent, note);

		return trib;
	}
	
	public static  Bollettino extractBoll(ResultSet resultSet,String dbSchemaCodSocieta) throws SQLException, ConfigurazioneException {
		try {
			StringBuilder importoRata = new StringBuilder(resultSet.getString("AR_IMPORTO_BOLL"));
			System.out.println("importoRata da DB= " + importoRata);
			importoRata.insert(importoRata.length()-2, "+");
			String tipoboll = "";
			String intestazioneCCP = resultSet.getString("FLU_CCP_INTEST")==null?"":resultSet.getString("FLU_CCP_INTEST");
			if(intestazioneCCP.contains("/")){
				intestazioneCCP = intestazioneCCP.split("/")[0].trim();
			}
			String numeroCCP = resultSet.getString("FLU_CCP");
			String docIban = resultSet.getString("DOC_IBAN")==null?"":resultSet.getString("DOC_IBAN").trim();
			if (docIban.length()>0) {
				if (docIban.length()==27 && docIban.startsWith("IT")) {
					numeroCCP = docIban.substring(docIban.length()-12,docIban.length());
				}
			}
			String numeroIUV = resultSet.getString("AR_CODICE_IUV");
			int numBollettino = resultSet.getInt("AR_PROGRESSIVO_BOLLETTINO");  
			String numeroAvvisoPagoPa = resultSet.getString("AR_NUMERO_AVVISO");
			String dataScadenzaIta = resultSet.getString("AR_DATA_SCADENZA").replace(".","").trim();
			System.out.println("importoRata = " + importoRata);
			String importoInCent = importoRata.substring(importoRata.length()-11);
			//String autorizzazioneCCP = resultSet.getString("FLU_CCP");
			String autorizzazioneCCP = getCodiceAutorizzazione(resultSet,dbSchemaCodSocieta);
			String importoCentesimi = leftZeroPad(resultSet.getString("AR_IMPORTO_BOLL"),11,11);
			String barcodePagoPa = String.format("(415)%s(8020)%s(3902)%s", getBarcodeParameter1(resultSet,resultSet.getString("AR_NUMERO_AVVISO")),resultSet.getString("AR_NUMERO_AVVISO"),importoCentesimi);
			String qrCcodePagoPa = resultSet.getString("AR_CODICE_QRCODE");
			String codeline4Boll = resultSet.getString("FLU_TYPE");
			
			Bollettino boll = new Bollettino(tipoboll, intestazioneCCP, numeroCCP, numeroIUV, numBollettino, numeroAvvisoPagoPa, dataScadenzaIta, importoInCent, autorizzazioneCCP, barcodePagoPa, qrCcodePagoPa, codeline4Boll); 

			return boll;
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
			// TODO: handle exception
			throw new ConfigurazioneException(e);
		}
		
	}
	
	public static  Bollettino extractBollUltimo(ResultSet resultSet,String dbSchemaCodSocieta) throws SQLException, ConfigurazioneException, Exception{
		StringBuilder importoRataTotale = new StringBuilder(resultSet.getString("DOC_IMPORTO_TOTALE"));
		importoRataTotale.insert(importoRataTotale.length()-2, "+");
		String tipoboll = "";
		String intestazioneCCP = resultSet.getString("FLU_CCP_INTEST")==null?"":resultSet.getString("FLU_CCP_INTEST");
		if(intestazioneCCP.contains("/")){
			intestazioneCCP = intestazioneCCP.split("/")[0].trim();
		}
		String numeroCCP = resultSet.getString("FLU_CCP");
		String docIban = resultSet.getString("DOC_IBAN")==null?"":resultSet.getString("DOC_IBAN").trim();
		if (docIban.length()>0) {
			if (docIban.length()==27 && docIban.startsWith("IT")) {
				numeroCCP = docIban.substring(docIban.length()-12,docIban.length());
			}
		}
		String numeroIUV = resultSet.getString("DOC_CODICE_IUV");
		String numeroAvvisoPagoPa = resultSet.getString("DOC_NUM_AVVISO_PAGO_PA");
		String dataPrimaScadenzaIta = resultSet.getString("DOC_DATA_SCADENZA").replace(".","").trim();
		String importoInCent = importoRataTotale.substring(importoRataTotale.length()-11);
		//String autorizzazioneCCP = resultSet.getString("FLU_CCP");
		String autorizzazioneCCP = getCodiceAutorizzazione(resultSet,dbSchemaCodSocieta);
		String importoCentesimi = leftZeroPad(resultSet.getString("DOC_IMPORTO_TOTALE"),11,11);
		String barcodePagoPa = String.format("(415)%s(8020)%s(3902)%s", getBarcodeParameter1(resultSet,resultSet.getString("DOC_NUM_AVVISO_PAGO_PA")),resultSet.getString("DOC_NUM_AVVISO_PAGO_PA"),importoCentesimi);
		String qrCcodePagoPa = resultSet.getString("DOC_CODICE_QRCODE");
		String codeline4Boll = resultSet.getString("FLU_TYPE");
		
		Bollettino boll = new Bollettino(tipoboll, intestazioneCCP, numeroCCP, numeroIUV, numeroAvvisoPagoPa, dataPrimaScadenzaIta, importoInCent, autorizzazioneCCP, barcodePagoPa, qrCcodePagoPa, codeline4Boll);

		return boll;
	}
	
	public static boolean sameFlusso(ResultSet resultSet, Flusso flu) throws SQLException {
		boolean same = flu.idFlusso.equals(resultSet.getString("FLU_ID_FLUSSO"));

		return same;
	}
	
	public static boolean sameBoll(ResultSet resultSet, Bollettino bol) throws SQLException {
		boolean same = bol.ProgressivoBoll == resultSet.getInt("AR_PROGRESSIVO_BOLLETTINO")	&& 
		               bol.AvvisoPagoPa.equals(resultSet.getString("AR_NUMERO_AVVISO"));
		return same;
	}
	
	
	// inizio-PAGONET-368
	
	
	// istanzio il pgec
	public static CommonsSOAPBindingStub getCommonsSOAPBindingStub(String dbSchemaCodSocieta,String commonsWsUrl) throws Exception {
		// we initialize commons serviceLocator
		CommonsServiceLocator serviceLocator = new CommonsServiceLocator();
		serviceLocator.setCommonsPortEndpointAddress(commonsWsUrl);

		// we initialize commons stub
		CommonsSOAPBindingStub binding = (CommonsSOAPBindingStub)serviceLocator.getCommonsPort(); //new URL(commonsWsUrl));
		
		binding.clearHeaders();
		binding.setHeader("","dbSchemaCodSocieta",dbSchemaCodSocieta);	
		
		return binding;
	}
	
	
	public static com.seda.payer.pgec.webservice.commons.dati.ConfigPagamento 
						recuperaListaFunzioniEnte(
			String commonsWsUrl, 
			String codiceSocieta,
			String codiceUtente,
			String chiaveEnte, 
			String canalePagamento, 
			String codTipologiaServizio,
			String dbSchemaSocieta) throws Exception {
		
		CommonsSOAPBindingStub commonsWs = getCommonsSOAPBindingStub(dbSchemaSocieta, commonsWsUrl);
		com.seda.payer.pgec.webservice.commons.dati.ConfigPagamento configPagamento = null;
    	ConfigPagamentoRequest configPagamentoRequest = new ConfigPagamentoRequest(codiceSocieta, codiceUtente, chiaveEnte, canalePagamento);
    	ConfigPagamentoResponse configPagamentoResponse = commonsWs.recuperaListaFunzioniEnte(configPagamentoRequest);
    	//In caso di errore, configPagamentoResponse.getRetCode() vale "01" altrimenti "00"
    	if (configPagamentoResponse != null && configPagamentoResponse.getRetCode()!=null) {
    		if (!configPagamentoResponse.getRetCode().equals("00")) {
    			//error(" - errore in recuperaListaFunzioniEnte: " + configPagamentoResponse.getRetMessage());
        		throw new Exception(" - errore in recuperaListaFunzioniEnte: " + configPagamentoResponse.getRetMessage());
        	} else {
        		if (configPagamentoResponse.getListConfigPagamento() != null){
        			//TODO. Si potrebbe nel caso della sendRT prendere il primo configPagamento a prescindere da codTipologiaServizio ....
        			//      e nel caso non fare andare in errore se codTipologiaServizio non e' presente
    	    		for(int i = 0; i < configPagamentoResponse.getListConfigPagamento().length; i++){
    					if (configPagamentoResponse.getListConfigPagamento(i).getCodTipologiaServizio().equals(codTipologiaServizio)) {
    						configPagamento = configPagamentoResponse.getListConfigPagamento(i);
    					}
    				}
    	    	}
        	}
    	}
    	return configPagamento;
    }
	
	// fine-PAGONET-368


}