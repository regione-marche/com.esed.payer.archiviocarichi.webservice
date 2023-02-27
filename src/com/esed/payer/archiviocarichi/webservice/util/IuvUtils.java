package com.esed.payer.archiviocarichi.webservice.util;

import java.sql.Connection;
import java.util.Calendar;

import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.ConfigurazioneIUV;
import com.seda.payer.core.dao.ArchivioCarichiDao;

public class IuvUtils {
	
	public static String[] calcolaIuv(String codiceEnte, ConfigurazioneIUV configurazioneIUV, Connection connection, String schema) throws Exception {
		String[] res = new String[]{"",""};
		String auxDigit=configurazioneIUV.getAuxDigit();
		if(auxDigit.equals("0")){
			res=calcolaIuv_AuxDigit_0(codiceEnte,configurazioneIUV,connection,schema);
		}else if(auxDigit.equals("1")){
			res=calcolaIuv_AuxDigit_1(codiceEnte,configurazioneIUV,connection,schema);
	    }else if(auxDigit.equals("2")){
	    	res=calcolaIuv_AuxDigit_2(codiceEnte,configurazioneIUV,connection,schema);
	    }else if(auxDigit.equals("3")){
	    	res=calcolaIuv_AuxDigit_3(codiceEnte,configurazioneIUV,connection,schema);
	    }  
	
		return res;
	}
	
	
	private static String[] calcolaIuv_AuxDigit_0(String codiceEnte, ConfigurazioneIUV configurazione, Connection connection, String schema) throws Exception {
		String[] res = new String[]{"",""};
		String iuv="";
		String numeroAvviso="";
		String auxDigit="0";
		try{
			ArchivioCarichiDao archivioCarichiDao=new ArchivioCarichiDao(connection, schema);
			String strYearDayOfYear = getAAGGG();
			String progressivo=archivioCarichiDao.getProgressivoIuv(codiceEnte, "0", Integer.parseInt(strYearDayOfYear)); 
			String applicationCode=String.format("%02d",Integer.parseInt(configurazione.getApplicationCode()));
			// calcolo la lunghezza del progressivo e non devo superare i 13 caratteri tra progressivo e AAGGG
			int length_progressivo=String.valueOf(progressivo).length();
			if(length_progressivo>8) throw new Exception("Il progressivo ottenuto per il calcolo dell Iuv con auxDigit 0 è maggiore del massimo consentito");
			String iuvBase = strYearDayOfYear.concat(String.format("%08d",Integer.parseInt(progressivo)));
			/*concat*/
			String strForCheckDigit = auxDigit.concat(applicationCode).concat(iuvBase);
			long check_digit = Long.parseLong(strForCheckDigit)% 93;
			String iuvCheckDigit = String.format("%02d", check_digit);
			iuv=iuvBase.concat(iuvCheckDigit);	//codice iuv da 15
			numeroAvviso=auxDigit.concat(applicationCode).concat(iuv);
			res[0]=numeroAvviso;
			res[1]=iuv;
		}catch(NumberFormatException e){
			throw e;
		}
		return res;
	}
	
	
	private static String[] calcolaIuv_AuxDigit_1(String codiceEnte, ConfigurazioneIUV configurazione, Connection connection, String schema) throws Exception {
		String[] res = new String[]{"",""};
		String iuv="";
		String numeroAvviso="";
		String auxDigit="1";
		try{
			ArchivioCarichiDao archivioCarichiDao=new ArchivioCarichiDao(connection, schema);
			String strYearDayOfYear = getAAGGG();
			String progressivo=archivioCarichiDao.getProgressivoIuv(codiceEnte, "1", Integer.parseInt(strYearDayOfYear));
			// calcolo la lunghezza del progressivo e non devo superare i 17 caratteri tra progressivo e AAGGG
			int length_progressivo=String.valueOf(progressivo).length();
			if(length_progressivo>12) throw new Exception("Il progressivo ottenuto per il calcolo dell Iuv con auxDigit 1 è maggiore del massimo consentito");
			String iuvBase = strYearDayOfYear.concat(String.format("%012d",Integer.parseInt(progressivo)));
			iuv=iuvBase;	//codice iuv da 17
			numeroAvviso=auxDigit.concat(iuvBase); 
			res[0]=numeroAvviso;
			res[1]=iuv;
		}catch(NumberFormatException e){
			throw e;
		}
		return res;
	}
	
	
	private static String[] calcolaIuv_AuxDigit_2(String codiceEnte, ConfigurazioneIUV configurazione, Connection connection, String schema) throws Exception {
		String[] res = new String[]{"",""};
		String iuv="";
		String numeroAvviso="";
		String auxDigit="2";
		try{
			ArchivioCarichiDao archivioCarichiDao=new ArchivioCarichiDao(connection, schema);
			String strYearDayOfYear = getAAGGG();
			String progressivo=archivioCarichiDao.getProgressivoIuv(codiceEnte, "2", Integer.parseInt(strYearDayOfYear));
			// calcolo la lunghezza del progressivo e non devo superare i 15 caratteri tra progressivo e AAGGG
			int length_progressivo=String.valueOf(progressivo).length();
			if(length_progressivo>10) throw new Exception("Il progressivo ottenuto per il calcolo dell Iuv con auxDigit 2 è maggiore del massimo consentito");
			String iuvBase = strYearDayOfYear.concat(String.format("%010d",Integer.parseInt(progressivo)));
			/*concat*/
			String strForCheckDigit = auxDigit.concat(iuvBase);
			long check_digit = Long.parseLong(strForCheckDigit)% 93;
			String iuvCheckDigit = String.format("%02d", check_digit);
			iuv=iuvBase.concat(iuvCheckDigit);	//codice iuv da 17
			numeroAvviso=auxDigit.concat(iuv);
			res[0]=numeroAvviso;
			res[1]=iuv;
		}catch(NumberFormatException e){
			throw e;
		}
		return res;
	}
	
	private static String[] calcolaIuv_AuxDigit_3(String codiceEnte, ConfigurazioneIUV configurazione, Connection connection, String schema) throws Exception {
		String[] res = new String[]{"",""};
		String iuv="";
		String numeroAvviso="";
		String auxDigit="3";
		try{
			ArchivioCarichiDao archivioCarichiDao=new ArchivioCarichiDao(connection, schema);
			String strYearDayOfYear = getAAGGG();
			String progressivo=archivioCarichiDao.getProgressivoIuv(codiceEnte, "3", Integer.parseInt(strYearDayOfYear));
		 	String segregationCode=String.format("%02d",Integer.parseInt(configurazione.getSegregationCode()));
		 	String carattereServizio = configurazione.getCarattereServizio();
		  	// calcolo la lunghezza del progressivo e non devo superare i 13 caratteri tra progressivo e AAGGG
		 	int length_progressivo=String.valueOf(progressivo).length();
		 	if(carattereServizio!= null && carattereServizio.trim().length()>0){
		 		carattereServizio = String.format("%02d",Integer.parseInt(carattereServizio));
		 		System.out.print("CarattereServizio presente nel calcolo iuv: " + carattereServizio);
		 		if(length_progressivo>6) throw new Exception("Il progressivo ottenuto per il calcolo dell Iuv con auxDigit 3 è maggiore del massimo consentito");
		 		String iuvBase = carattereServizio.concat(strYearDayOfYear).concat(String.format("%06d",Integer.parseInt(progressivo)));
		 		String strForCheckDigit = auxDigit.concat(segregationCode).concat(iuvBase);
				long check_digit = Long.parseLong(strForCheckDigit)% 93;
				String iuvCheckDigit = String.format("%02d", check_digit);
				iuv=segregationCode.concat(iuvBase).concat(iuvCheckDigit); //codice iuv da 17
		 	} else {
			 	if(length_progressivo>8) throw new Exception("Il progressivo ottenuto per il calcolo dell Iuv con auxDigit 3 è maggiore del massimo consentito");
			 	String iuvBase = strYearDayOfYear.concat(String.format("%08d",Integer.parseInt(progressivo)));
				/*concat*/
				String strForCheckDigit = auxDigit.concat(segregationCode).concat(iuvBase);
				long check_digit = Long.parseLong(strForCheckDigit)% 93;
				String iuvCheckDigit = String.format("%02d", check_digit);
				iuv=segregationCode.concat(iuvBase).concat(iuvCheckDigit);	//codice iuv da 17
		 	}
			numeroAvviso=auxDigit.concat(iuv);
			res[0]=numeroAvviso;
			res[1]=iuv;
			System.out.println(iuv);
				 	
		}catch(NumberFormatException e){
			throw e;
		}
		return res;
	}
	
	
	
	private static String getAAGGG() {
		String yearDayOfYear = "";
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int dayOfYear= now.get(Calendar.DAY_OF_YEAR);
		String strYear = String.valueOf(year).substring(2);
		String strDayOfYear = String.valueOf(dayOfYear+500);
		yearDayOfYear = strYear.concat(strDayOfYear);
		return yearDayOfYear;
	}
}
