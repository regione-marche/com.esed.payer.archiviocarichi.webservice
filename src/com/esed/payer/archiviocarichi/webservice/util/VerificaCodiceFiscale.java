package com.esed.payer.archiviocarichi.webservice.util;

import java.util.*;

public class VerificaCodiceFiscale {
	
	public static boolean verificaCodiceFiscale(String codiceFiscale) {
		if(codiceFiscale == null)
			return false;
		codiceFiscale = ClearString(codiceFiscale);
		if(codiceFiscale.length() != 16 && codiceFiscale.length() != 11)
			return false;
		return controlloCodiceFiscale_PartitaIva_NomeEmpty(codiceFiscale, false);
	}
	
	public static boolean verificaPartitaIva(String partitaIva) {
		if(partitaIva == null)
			return false;
		partitaIva = ClearString(partitaIva);
		if(partitaIva.length() != 11)
			return false;	
		return controlloCodiceFiscale_PartitaIva_NomeEmpty(partitaIva, true);
	}

	public static boolean verificaCodiceFiscale(Debitore debitore) {
		String codFiscaleCalcolato = calcoloCodiceFiscale(debitore);
//		String codFiscaleCalcolato2 = calcoloCodiceFiscale2(debitore);
//		if(!codFiscaleCalcolato2.equals(codFiscaleCalcolato))
//			codFiscaleCalcolato = codFiscaleCalcolato2;
		if (!codFiscaleCalcolato.equalsIgnoreCase(debitore.getCodiceFiscale())) {
			if (!verificaCodiciFiscaliOmocodia(codFiscaleCalcolato, debitore.getCodiceFiscale()))
				if (!verifica2CodiciFiscaliOmocodia(codFiscaleCalcolato, debitore.getCodiceFiscale()))
					return false;
		}
		return true;
	}
	
	private static boolean controlloCodiceFiscale_PartitaIva_NomeEmpty(String codFiscalePIVA, boolean bPIVA) {
		//il controllo required viene effettuato dal validatore
		String sCF_PI = codFiscalePIVA.trim();
		
		/*
		//PG170060 12042017 - inizio
		if (sCF_PI.length() >= 5 && sCF_PI.substring(0, 5).equals("CFKO0")){
			if(sCF_PI.length()==16 && isNumeric(sCF_PI.substring(6)))
				return "";
			
			else
				return labelCampo + ": Codice fiscale non valido.";	
		}
		//PG170060 12042017 - fine
		*/
		if (sCF_PI.length() > 0) {
			//lunghezza 11: controllo partita iva
			if (sCF_PI.length() == 11)  {
				if(bPIVA) {
					if (!IsInt64(sCF_PI))
						return false;
					//controllo completo partita iva
					boolean esitoPartitaIva = false;
					try {
						esitoPartitaIva = controllaPIVA(sCF_PI);
					} catch (Exception ex) {			
						return false;
					}		
					return esitoPartitaIva;
				} else {
				    SogeiUCheckNum checkCfNum = new SogeiUCheckNum(sCF_PI.toUpperCase());
				    return (checkCfNum.controllaCfNum()) && (checkCfNum.trattCfNum().charAt(0) != '2') && (checkCfNum.trattCfNum().charAt(0) != '5');
				}
				
			} else {
				//controllo codice fiscale
				try {			
					UCheckDigit uCodFiscal = new UCheckDigit(sCF_PI);
					//SogeiUCheckDigit uCodFiscal = new SogeiUCheckDigit(sCF_PI.trim().toUpperCase());
					return uCodFiscal.controllaCorrettezza();
				}
				catch (Exception ex) {			
					return false;
				}
			}
		}
		return true;
	}
	/*
	private static boolean isNumeric(String str)   {  
	  try {  
	    Double.parseDouble(str);  
	  } catch(NumberFormatException nfe) {  
	    return false;  
	  }  
	  return true;  
	}
	*/	
	//metodo per il controllo della partita IVA
	private static boolean controllaPIVA(String pi) {
	    int i, c, s;
	    s = 0;
	    for(i = 0; i <= 9; i += 2 )
	    	s += pi.charAt(i) - '0';
	    for(i = 1; i <= 9; i += 2 ) {
	       c = 2 * (pi.charAt(i) - '0');
	       if(c > 9)  c = c - 9;
	       s += c;
	    }
	    return !((10 - s % 10) % 10 != pi.charAt(10) - '0' || pi.equals("00000000000"));
	}
	
	public static boolean IsInt64(String sValue) {
		try {
			Long.valueOf(sValue);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/*
	 * @param cognome Il cognome della persona
	 * @param nome Il nome della persona
	 * @param sesso Il sesso della persona M o F
	 * @param data La data di nascita della persona
	 * @param comune Il codice del comune di nascita della persona
	 * @return String - il codice fiscale calcolato in base a parametri in
	 *         input. Se uno dei parametri in input è null o stringa vuota
	 *         restituisce una stringa vuota.	 */

	private static String calcoloCodiceFiscale2(Debitore debitore) {
		return CalcolaCodiceFiscale.calcolaCf(debitore.getCognome(),
										debitore.getNome(),
										debitore.getSesso(),
										debitore.getDataNascita().getTime(),
										debitore.getComuneNascitaBelfiore());
										
	}

	
	private static String calcoloCodiceFiscale(Debitore debitore) {
		String codiceFiscale = "";
		
		try {
			Lettere lettere = new Lettere();
			
			/*
			 * Cognome
			Sono necessari 3 caratteri per rappresentare il cognome, e sono la prima la seconda 
			e la terza consonante del cognome.
			E' possibile che le consonanti siano meno di tre, in questo caso è possibile 
			aggiungere le vocali nell'ordine in cui compaiono nel cognome.
			Per cognomi più corti di 3 caratteri, è possibile sostituire il carattere mancante con la lettera X.
			Chiaramente se ci sono cognomi con più parti, è necessario rimuovere gli spazi 
			e considerare tutto come un cognome unico.
			*/
			
			lettere.elaboraParola(debitore.getCognome().toUpperCase());
			
			if (lettere.consonanti.size() >= 3)
				codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(1)) + String.valueOf(lettere.consonanti.get(2));
			else 
			{
				int lettereMancanti = 3;
				//meno di 3 consonanti
				if (lettere.consonanti.size() == 2)
				{
					codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(1));
					lettereMancanti = 1;
				}
				else if (lettere.consonanti.size() == 1)
				{
					codiceFiscale += String.valueOf(lettere.consonanti.get(0));
					lettereMancanti = 2;
				}
				
				if (lettere.vocali.size() >= lettereMancanti)
				{
					//ho abbastanza vocali per completare la stringa di 3
					for (int i=0; i<lettereMancanti; i++)
						codiceFiscale += String.valueOf(lettere.vocali.get(i));
				}
				else
				{
					//non ho abbastanza vocali, riempio con le X
					for (int i=0; i<lettere.vocali.size(); i++)
						codiceFiscale += String.valueOf(lettere.vocali.get(i));
					for (int i=0; i<(lettereMancanti - lettere.vocali.size()); i++)
						codiceFiscale += "X";
				}
			}
			
			/*
			 * Nome
				Per il nome il discorso è analogo con la particolarità che se il nome è composto da 4 o più consonanti 
				vengono prese nell'ordine la prima, la terza e la quarta.
				Anche qui potremmo trovarci nella situazione di un numero di consonanti 
				minore di 3 e allo stesso modo si aggiungo le vocali.
				Ripetiamo anche qui che se il nome è più corto di 3 lettere è possibile 
				sostituire i caratteri mancanti con delle X.
				Se il nome fosse composto da più nomi, bisogna considerarlo tutto assieme.
			 */
			lettere.elaboraParola(debitore.getNome().toUpperCase());
			
			if (lettere.consonanti.size() > 3)
				codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(2)) + String.valueOf(lettere.consonanti.get(3));
			else if (lettere.consonanti.size() == 3)
				codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(1)) + String.valueOf(lettere.consonanti.get(2));
			else 
			{
				int lettereMancanti = 3;
				//meno di 3 consonanti
				if (lettere.consonanti.size() == 2)
				{
					codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(1));
					lettereMancanti = 1;
				}
				else if (lettere.consonanti.size() == 1)
				{
					codiceFiscale += String.valueOf(lettere.consonanti.get(0));
					lettereMancanti = 2;
				}
				
				if (lettere.vocali.size() >= lettereMancanti)
				{
					//ho abbastanza vocali per completare la stringa di 3
					for (int i=0; i<lettereMancanti; i++)
						codiceFiscale += String.valueOf(lettere.vocali.get(i));
				}
				else
				{
					//non ho abbastanza vocali, riempio con le X
					for (int i=0; i<lettere.vocali.size(); i++)
						codiceFiscale += String.valueOf(lettere.vocali.get(i));
					for (int i=0; i<(lettereMancanti - lettere.vocali.size()); i++)
						codiceFiscale += "X";
				}
			}
	
			/*
			 * Anno di nascita
				Per l'anno vengono prese semplicemente le ultime due cifre.
			 */
			String sYear = String.valueOf(debitore.getDataNascita().get(Calendar.YEAR));
			codiceFiscale += sYear.substring(2);
			
			/*
			 * Per quanto riguarda il mese c'è una tabella di conversione. 
			 * Ad ogni mese corrisponde una lettera dell'alfabeto:
			 */
		
			int iMonth = debitore.getDataNascita().get(Calendar.MONTH) + 1;
			switch (iMonth)
			{
				case 1: codiceFiscale += "A"; break;
				case 2: codiceFiscale += "B"; break;
				case 3: codiceFiscale += "C"; break;
				case 4: codiceFiscale += "D"; break;
				case 5: codiceFiscale += "E"; break;
				case 6: codiceFiscale += "H"; break;
				case 7: codiceFiscale += "L"; break;
				case 8: codiceFiscale += "M"; break;
				case 9: codiceFiscale += "P"; break;
				case 10: codiceFiscale += "R"; break;
				case 11: codiceFiscale += "S"; break;
				case 12: codiceFiscale += "T"; break;
			}
			
			/*
			 * Giorno
				In questo caso è sufficiente riportare il numero del giorno, 
				con il particolare che per le donne questo numero dev'essere aumentato di 40
			 */
			String sesso = debitore.getSesso();
			int iDay = debitore.getDataNascita().get(Calendar.DAY_OF_MONTH);
			if (sesso.equals("F"))
				iDay += 40;
			
			codiceFiscale += formatNumToString(2, String.valueOf(iDay));
			
			/*
			 * Comune di nascita
			E' composto da quattro caratteri alfanumerici. 
			E' il codice del comune rilevato dai volumi dei codici dei comuni italiani. (Codice Belfiore)
			 */
			codiceFiscale += debitore.getComuneNascitaBelfiore();
			
			codiceFiscale += getCodiceControlloCodiceFiscale(codiceFiscale);
			
			return codiceFiscale;
			
		} catch (Exception e) {
			return "";
		}
	}
	
	private static String formatNumToString(int iLenght, String sNumToFormat) {
		String formattedString = sNumToFormat;

		while(formattedString.length() < iLenght) 
		{
			formattedString = "0" + formattedString;
		}
		return formattedString;
	}
	
	private static String getCodiceControlloCodiceFiscale(String codiceFiscale) {
		/* Si comincia con il prendere i caratteri del codice fiscale fin qui calcolato che sono 15, 
		 * si prendono quelli in posizione pari e si convertono con i 
		 * numeri corrispondenti della prima tabella. Tutti questi numeri vengono sommati.
		 */
		int iTotalePari = 0;
		for (int i=1; i<14; i=i+2)
		{
			char c = codiceFiscale.charAt(i);
			switch (c) {
				case (int)'0': iTotalePari += 0; break;
				case (int)'1': iTotalePari += 1; break;
				case (int)'2': iTotalePari += 2; break;
				case (int)'3': iTotalePari += 3; break;
				case (int)'4': iTotalePari += 4; break;
				case (int)'5': iTotalePari += 5; break;
				case (int)'6': iTotalePari += 6; break;
				case (int)'7': iTotalePari += 7; break;
				case (int)'8': iTotalePari += 8; break;
				case (int)'9': iTotalePari += 9; break;
				case (int)'A': iTotalePari += 0; break;
				case (int)'B': iTotalePari += 1; break;
				case (int)'C': iTotalePari += 2; break;
				case (int)'D': iTotalePari += 3; break;
				case (int)'E': iTotalePari += 4; break;
				case (int)'F': iTotalePari += 5; break;
				case (int)'G': iTotalePari += 6; break;
				case (int)'H': iTotalePari += 7; break;
				case (int)'I': iTotalePari += 8; break;
				case (int)'J': iTotalePari += 9; break;
				case (int)'K': iTotalePari += 10; break;
				case (int)'L': iTotalePari += 11; break;
				case (int)'M': iTotalePari += 12; break;
				case (int)'N': iTotalePari += 13; break;
				case (int)'O': iTotalePari += 14; break;
				case (int)'P': iTotalePari += 15; break;
				case (int)'Q': iTotalePari += 16; break;
				case (int)'R': iTotalePari += 17; break;
				case (int)'S': iTotalePari += 18; break;
				case (int)'T': iTotalePari += 19; break;
				case (int)'U': iTotalePari += 20; break;
				case (int)'V': iTotalePari += 21; break;
				case (int)'W': iTotalePari += 22; break;
				case (int)'X': iTotalePari += 23; break;
				case (int)'Y': iTotalePari += 24; break;
				case (int)'Z': iTotalePari += 25; break;
			}
		}
			
		/*Allo stesso modo con i caratteri dispari che devono essere convertiti 
		 * pero' utilizzando la seconda tabella e vengono tutti sommati.
		 */
		int iTotaleDispari = 0;
		for (int i=0; i<15; i=i+2)
		{
			char c = codiceFiscale.charAt(i);
			switch (c) {
				case (int)'0': iTotaleDispari += 1; break;
				case (int)'1': iTotaleDispari += 0; break;
				case (int)'2': iTotaleDispari += 5; break;
				case (int)'3': iTotaleDispari += 7; break;
				case (int)'4': iTotaleDispari += 9; break;
				case (int)'5': iTotaleDispari += 13; break;
				case (int)'6': iTotaleDispari += 15; break;
				case (int)'7': iTotaleDispari += 17; break;
				case (int)'8': iTotaleDispari += 19; break;
				case (int)'9': iTotaleDispari += 21; break;
				case (int)'A': iTotaleDispari += 1; break;
				case (int)'B': iTotaleDispari += 0; break;
				case (int)'C': iTotaleDispari += 5; break;
				case (int)'D': iTotaleDispari += 7; break;
				case (int)'E': iTotaleDispari += 9; break;
				case (int)'F': iTotaleDispari += 13; break;
				case (int)'G': iTotaleDispari += 15; break;
				case (int)'H': iTotaleDispari += 17; break;
				case (int)'I': iTotaleDispari += 19; break;
				case (int)'J': iTotaleDispari += 21; break;
				case (int)'K': iTotaleDispari += 2; break;
				case (int)'L': iTotaleDispari += 4; break;
				case (int)'M': iTotaleDispari += 18; break;
				case (int)'N': iTotaleDispari += 20; break;
				case (int)'O': iTotaleDispari += 11; break;
				case (int)'P': iTotaleDispari += 3; break;
				case (int)'Q': iTotaleDispari += 6; break;
				case (int)'R': iTotaleDispari += 8; break;
				case (int)'S': iTotaleDispari += 12; break;
				case (int)'T': iTotaleDispari += 14; break;
				case (int)'U': iTotaleDispari += 16; break;
				case (int)'V': iTotaleDispari += 10; break;
				case (int)'W': iTotaleDispari += 22; break;
				case (int)'X': iTotaleDispari += 25; break;
				case (int)'Y': iTotaleDispari += 24; break;
				case (int)'Z': iTotaleDispari += 23; break;
			}
		}

		/*I valori ottenuti vengono a loro volta sommati e il totale viene diviso per 26.*/
		int resto = (iTotalePari + iTotaleDispari) % 26;
		
		/*Il resto della divisione dev'essere convertito usando l'ultima tabella.
		* Il carattere corrispondente è il codice di controllo!
		*/
		switch (resto)
		{
			case 0: return "A";
			case 1: return "B"; 
			case 2: return "C"; 
			case 3: return "D"; 
			case 4: return "E"; 
			case 5: return "F"; 
			case 6: return "G"; 
			case 7: return "H"; 
			case 8: return "I"; 
			case 9: return "J"; 
			case 10: return "K"; 
			case 11: return "L"; 
			case 12: return "M"; 
			case 13: return "N"; 
			case 14: return "O"; 
			case 15: return "P"; 
			case 16: return "Q"; 
			case 17: return "R"; 
			case 18: return "S"; 
			case 19: return "T";
			case 20: return "U"; 
			case 21: return "V"; 
			case 22: return "W"; 
			case 23: return "X"; 
			case 24: return "Y"; 
			case 25: return "Z"; 
		}

		return "";
	}
	
	private static boolean verificaCodiciFiscaliOmocodia(String codFiscaleCalcolato, String codiceFiscaleInserito) {
		int[] aPos = new int[]{15,14,13,11,10,8,7};
		
		int iIndex = 0;
		
		//elimino l'ultimo carattere
		while (codFiscaleCalcolato != null && iIndex < 7)
		{
			codFiscaleCalcolato = codFiscaleCalcolato.substring(0,15);
			codFiscaleCalcolato = calcolaCodFiscaleOmocode(codFiscaleCalcolato, aPos[iIndex]);
			if (codFiscaleCalcolato != null)
			{
				if (codFiscaleCalcolato.equalsIgnoreCase(codiceFiscaleInserito))
					return true;
			}
			iIndex++;
		}
		
		return false;
	}
	
	private static boolean verifica2CodiciFiscaliOmocodia(String codFiscaleCalcolato, String codiceFiscaleInserito) {
		int[] aPos = new int[]{15,14,13,11,10,8,7};
		
		int iIndex = 0;
		
		//elimino l'ultimo carattere
		String appo = codiceFiscaleInserito.substring(0, 15);
		
		while (iIndex < 7)
		{
			String carattereSostituto = "";
			char c = appo.charAt(aPos[iIndex] - 1);
			try {
				Integer.valueOf(String.valueOf(c));
			} catch (NumberFormatException ex) {
				//Se non è numerico significa trattasi di omocodia
				switch (c) {
					case 'L': carattereSostituto = "0"; break;
					case 'M': carattereSostituto = "1"; break;
					case 'N': carattereSostituto = "2"; break;
					case 'P': carattereSostituto = "3"; break;
					case 'Q': carattereSostituto = "4"; break;
					case 'R': carattereSostituto = "5"; break;
					case 'S': carattereSostituto = "6"; break;
					case 'T': carattereSostituto = "7"; break;
					case 'U': carattereSostituto = "8"; break;
					case 'V': carattereSostituto = "9"; break;
				}
			}
			if (carattereSostituto.length() > 0) {
				appo = appo.substring(0, aPos[iIndex] - 1) + 
						carattereSostituto + 
						appo.substring(aPos[iIndex], appo.length());
			}
			iIndex++;
		}
		appo += getCodiceControlloCodiceFiscale(appo);
		if (appo.equalsIgnoreCase(codFiscaleCalcolato))
			return true;
		else 
			return false;
	}
	
	private static String calcolaCodFiscaleOmocode(String codFiscalePrecedente, int posizioneSostituzione) {
		String carattereSostituto = "";
		char c = codFiscalePrecedente.charAt(posizioneSostituzione - 1);
		switch (c) {
			case (int)'0': carattereSostituto = "L"; break;
			case (int)'1': carattereSostituto = "M"; break;
			case (int)'2': carattereSostituto = "N"; break;
			case (int)'3': carattereSostituto = "P"; break;
			case (int)'4': carattereSostituto = "Q"; break;
			case (int)'5': carattereSostituto = "R"; break;
			case (int)'6': carattereSostituto = "S"; break;
			case (int)'7': carattereSostituto = "T"; break;
			case (int)'8': carattereSostituto = "U"; break;
			case (int)'9': carattereSostituto = "V"; break;
		}
		if (carattereSostituto.length() == 0)
			return null;
		else
		{
			String codFiscaleOmocode = codFiscalePrecedente.substring(0, posizioneSostituzione - 1) + 
										carattereSostituto + 
										codFiscalePrecedente.substring(posizioneSostituzione, codFiscalePrecedente.length());
			
			codFiscaleOmocode += getCodiceControlloCodiceFiscale(codFiscaleOmocode);
			return codFiscaleOmocode;
		}
	}
	
	public static String ClearString(String appo) {
		String ret = (appo != null ? appo.trim() : "");
		if(ret.length() > 0) {
			ret = ret.replaceAll("\t", " ");
			ret = ret.replaceAll("\r", " ");
			ret = ret.replaceAll("\n", " ");
			int k = ret.indexOf("  ");
			while (k != -1) {
				ret = ret.substring(0, k + 1) + ret.substring(k + 1);
				k = ret.indexOf("  ");
			}
			ret = ret.trim();
		}			
		return ret;
	}
}

class Lettere {
	List<Character> consonanti;
	List<Character> vocali;
	
	//data una parola divide le consonanti e le vocali
	void elaboraParola(String parola)
	{
		parola = parola.toUpperCase();
		consonanti = new ArrayList<Character>();
		vocali = new ArrayList<Character>();
		
		List<Character> listVocali = new ArrayList<Character>(Arrays.asList('A','E','I','O','U'));
		List<Character> listConsonanti = new ArrayList<Character>(Arrays.asList('B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Y','Z'));
		for (int i=0; i<parola.length(); i++)
		{
			Character c =parola.charAt(i);
			if (listVocali.contains(c))
				vocali.add(c);
			else if (listConsonanti.contains(c))
				consonanti.add(c);
		}
	}
}

@SuppressWarnings("rawtypes")
class UCheckDigit {
	private String strCodFisc;
	private static Vector vctCd;
	private static Vector vctCp;
	private static Vector vctNd;
	private static Vector vctNp;
	private static Vector vctVm; // vector dei mesi
	
	// private static Vector ceck;
	private static String ritorno;
	
	public UCheckDigit (String g ) {
		strCodFisc = g;
		
		creaCarattereDispari();
		creaCaratterePari();
		creaNumeroDispari();
		creaNumeroPari();
		creaMese();	
	}

	public boolean controllaCheckDigit( ) {
		int intAppoggio = 0;
		char chrCarattereEsaminato;
		
		//Ciclo di conteggio dei valori sui primi 15 caratteri del codice fiscale
		for (int i=0; i<15; i++) {
			chrCarattereEsaminato = getCodFisc().charAt(i);
			String strElem = getCodFisc().substring(i,i+1);
			int intResto = (i % 2);
			switch(intResto) {
				case 0:
					if(Character.isDigit(chrCarattereEsaminato) == false)
						intAppoggio += getVectCarDisp(strElem);
					else
						intAppoggio += getVectNumDisp(strElem);
					break;
				case 1:
					if(Character.isDigit(chrCarattereEsaminato) == false)
						intAppoggio += getVectCarPari(strElem);
					else
						intAppoggio += getVectNumPari(strElem);
					break;
				default:
					break;
			}
		}
		
		// Estraggo il carattere di controllo
		String ceckdigit = getCodFisc().substring(15,16);
		return (intAppoggio % 26) == getVectCarPari(ceckdigit);
	}

	public boolean controllaCorrettezza() {
		if (controllaCorrettezzaChar() == '0')
			return true;
		else
			return false;
	}

	public char controllaCorrettezzaChar () {
	@SuppressWarnings("unused")
		boolean bolLettera = false;
		for (int i = 0; i < 6; i++) {
			if(!(Character.isLetter(strCodFisc.charAt(i)))) // controllo dei primi 6
				return '2'; // caratteri alfabetici
		}
		for (int i = 6; i < 8; i++) {
			if (!(Character.isDigit(strCodFisc.charAt(i)))) // controllo dell'anno
				return '2';
		}
		
		if (!((strCodFisc.charAt(8) != 'A') || (strCodFisc.charAt(8) != 'B') || // controllo del mese
			(strCodFisc.charAt(8) != 'C') || (strCodFisc.charAt(8) != 'D') ||
			(strCodFisc.charAt(8) != 'E') || (strCodFisc.charAt(8) != 'H') ||
			(strCodFisc.charAt(8) != 'L') || (strCodFisc.charAt(8) != 'M') ||
			(strCodFisc.charAt(8) != 'P') || (strCodFisc.charAt(8) != 'R') ||
			(strCodFisc.charAt(8) != 'S') || (strCodFisc.charAt(8) != 'T'))
		) return '2';
		
		for (int i = 9; i < 11; i++) {
			if (!(Character.isDigit(strCodFisc.charAt(i)))) // controllo dell'anno
				return '2';
		}
		int intGiorno = Integer.parseInt(strCodFisc.substring(9,11)); // controllo formale del giorno
		if(intGiorno > 31)
			intGiorno -= 40;
		if(intGiorno <1 || intGiorno > 31)
			return '2';
		
		String strElem = strCodFisc.substring(8,9); // lettera del mese
		String strMese = String.valueOf(getVectMese(strElem)); // valore della lettera del mese
		
		if (strMese.length() == 1) // se mese ha una sola cifra
			strMese = "0" + strMese; // viene aggiunto uno zero
		
		String strAnno = strCodFisc.substring(6,8);
		
		String strGiorno = String.valueOf(intGiorno); // se giorno ha una sola cifra
		if (strGiorno.length() == 1) // viene aggiunto uno zero
			strGiorno = "0" + strGiorno;
		
		String data = strGiorno + strMese + strAnno; // controllo dell'intera data
		
		if (!(controllaData(data)))
			return'2' ;
		if (((strCodFisc.charAt(11) != 'A') && (strCodFisc.charAt(11) != 'B') &&
			// controllo del 1° carattere
			(strCodFisc.charAt(11) != 'C') && (strCodFisc.charAt(11) != 'D') &&
			// del codice catastale
			(strCodFisc.charAt(11) != 'E') && (strCodFisc.charAt(11) != 'F') &&
			(strCodFisc.charAt(11) != 'G') && (strCodFisc.charAt(11) != 'H') &&
			(strCodFisc.charAt(11) != 'I') && (strCodFisc.charAt(11) != 'L') &&
			(strCodFisc.charAt(11) != 'M') && (strCodFisc.charAt(11) != 'Z'))
		) return '2';

		for(int i = 6; i < 8; i++) {
			if (!(Character.isDigit(strCodFisc.charAt(i))))
				if ((strCodFisc.charAt(i) != 'L') && (strCodFisc.charAt(i) != 'M') && // controllo del 1° carattere
					(strCodFisc.charAt(i) != 'N') && (strCodFisc.charAt(i) != 'P') && // del codice catastale
					(strCodFisc.charAt(i) != 'Q') && (strCodFisc.charAt(i) != 'R') &&
					(strCodFisc.charAt(i) != 'S') && (strCodFisc.charAt(i) != 'T') &&
					(strCodFisc.charAt(i) != 'U') && (strCodFisc.charAt(i) != 'V'))
					return '3';
		}
		
		for (int i = 9; i < 11; i++) {
			if (!(Character.isDigit(strCodFisc.charAt(i))))
				if((strCodFisc.charAt(i) != 'L') && (strCodFisc.charAt(i) != 'M') && // controllo del 1° carattere
					(strCodFisc.charAt(i) != 'N') && (strCodFisc.charAt(i) != 'P') && // del codice catastale
					(strCodFisc.charAt(i) != 'Q') && (strCodFisc.charAt(i) != 'R') &&
					(strCodFisc.charAt(i) != 'S') && (strCodFisc.charAt(i) != 'T') &&
					(strCodFisc.charAt(i) != 'U') && (strCodFisc.charAt(i) != 'V'))
					return '3';
		}
		
		for (int i = 12; i < 15; i++) {
			if(!(Character.isDigit(strCodFisc.charAt(i)))) {
				bolLettera = true;
				if((strCodFisc.charAt(i) != 'L') && (strCodFisc.charAt(i) != 'M') && // controllo del 1° carattere
					(strCodFisc.charAt(i) != 'N') && (strCodFisc.charAt(i) != 'P') && // del codice catastale
					(strCodFisc.charAt(i) != 'Q') && (strCodFisc.charAt(i) != 'R') &&
					(strCodFisc.charAt(i) != 'S') && (strCodFisc.charAt(i) != 'T') &&
					(strCodFisc.charAt(i) != 'U') && (strCodFisc.charAt(i) != 'V'))
					return '3';
			}
		}
	
		if(bolLettera = false) {
			int intNumeroCodCat = Integer.parseInt(strCodFisc.substring(12, 15));
		
			if(intNumeroCodCat == 000)
				return '2';
		
			if((strCodFisc.charAt(11) == 'M') && (intNumeroCodCat > 399)) // se lettera M le 3 cifre
				return '2'; // del cod. cat. non > di 399
		}
		
		if(controllaCheckDigit())
			return '0';
		return '1';
	}

	public int controllaCorrettezzaInt() {
		return ((char)controllaCorrettezzaChar() - 48);
	}

	public static boolean controllaData(String s) {
	//controllo l'anno dopo averlo estrapolato dalla stringa
		try {
			String strAnno = s.substring(4, s.length());
			if((s.length() == 8) || (s.length() == 6)) {
				if (s.length() == 6)
					strAnno = "19" + s ;
				if (Integer.parseInt(strAnno) < 1870)
					return false;
			} else			
					return false;
			
			//Estrapolazione mese e giorno
			String strMese = s.substring(2,4);
			String strGiorno = s.substring(0,2);
			
			//Trasformazione delle stringhe in interi
			int intMese = Integer.parseInt(strMese);
			int intGiorno = Integer.parseInt(strGiorno);
			int intAnno = Integer.parseInt(strAnno);
			
			//controlli di ammissibilità sul giorno e sul mese
			if((intMese > 12) || (intGiorno > 31) || (intMese < 1) || (intGiorno < 1))
				return false;
			
			//controllo mese
			switch (intMese) {
				case 2: //febbraio
					boolean bisestile = false;
					if(intAnno % 4 == 0) {
						if (intAnno % 400 == 0) {
							if (intAnno % 1000 == 0)
								bisestile = true;
						} else
							bisestile = true;
					}
					if((bisestile && (intGiorno > 29)) || (!bisestile && (intGiorno > 28)))
						return false;
					break;
				case 4: //aprile
				case 6: //giugno
				case 9: //settembre
				case 11: //novembre
					if (intGiorno > 30)
						return false;
					break;
				default:
					break;
			}
			//se arrivo a questo punto vuol dire che la data è corretta
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	/**
	* Tabella (di tipo Vector) dei valori dei caratteri dispari.
	*/
	@SuppressWarnings({ "unchecked", "static-access" })
	public Vector creaCarattereDispari() {
		this.vctCd= new Vector();
	    //valore dei caratteri dispari
		vctCd.addElement("B");
		vctCd.addElement("A");
		vctCd.addElement("K");
		vctCd.addElement("P");
		vctCd.addElement("L");
		vctCd.addElement("C");
		vctCd.addElement("Q");
		vctCd.addElement("D");
		vctCd.addElement("R");
		vctCd.addElement("E");
		vctCd.addElement("V");
		vctCd.addElement("O");
		vctCd.addElement("S");
		vctCd.addElement("F");
		vctCd.addElement("T");
		vctCd.addElement("G");
		vctCd.addElement("U");
		vctCd.addElement("H");
		vctCd.addElement("M");
		vctCd.addElement("I");
		vctCd.addElement("N");
		vctCd.addElement("J");
		vctCd.addElement("W");
		vctCd.addElement("Z");
		vctCd.addElement("Y");
		vctCd.addElement("X");
		return vctCd;
	}
	/**
	* Tabella (di tipo Vector) dei valori dei caratteri pari.
	*/
	@SuppressWarnings({ "unchecked", "static-access" })
	public Vector creaCaratterePari( ) {
		this.vctCp = new Vector();
		//valore dei caratteri pari
		vctCp.addElement("A");
		vctCp.addElement("B");
		vctCp.addElement("C");
		vctCp.addElement("D");
		vctCp.addElement("E");
		vctCp.addElement("F");
		vctCp.addElement("G");
		vctCp.addElement("H");
		vctCp.addElement("I");
		vctCp.addElement("J");
		vctCp.addElement("K");
		vctCp.addElement("L");
		vctCp.addElement("M");
		vctCp.addElement("N");
		vctCp.addElement("O");
		vctCp.addElement("P");
		vctCp.addElement("Q");
		vctCp.addElement("R");
		vctCp.addElement("S");
		vctCp.addElement("T");
		vctCp.addElement("U");
		vctCp.addElement("V");
		vctCp.addElement("W");
		vctCp.addElement("X");
		vctCp.addElement("Y");
		vctCp.addElement("Z");
		return vctCp;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	public Vector creaMese ( ) {
		this.vctVm = new Vector();		
		vctVm.addElement(" ");
		vctVm.addElement("A"); // gennaio
		vctVm.addElement("B"); // febbraio
		vctVm.addElement("C"); // marzo
		vctVm.addElement("D"); // aprile
		vctVm.addElement("E"); // maggio
		vctVm.addElement("H"); // giugno
		vctVm.addElement("L"); // luglio
		vctVm.addElement("M"); // agosto
		vctVm.addElement("P"); // settembre
		vctVm.addElement("R"); // ottobre
		vctVm.addElement("S"); // novembre
		vctVm.addElement("T"); // dicembre
		return vctVm;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	public Vector creaNumeroDispari ( ) {
		this.vctNd = new Vector();
		//valore dei numeri dispari
		vctNd.addElement("1");
		vctNd.addElement("0");
		vctNd.addElement(" ");
		vctNd.addElement(" ");
		vctNd.addElement(" ");
		vctNd.addElement("2");
		vctNd.addElement(" ");
		vctNd.addElement("3");
		vctNd.addElement(" ");
		vctNd.addElement("4");
		vctNd.addElement(" ");
		vctNd.addElement(" ");
		vctNd.addElement(" ");
		vctNd.addElement("5");
		vctNd.addElement(" ");
		vctNd.addElement("6");
		vctNd.addElement(" ");
		vctNd.addElement("7");
		vctNd.addElement(" ");
		vctNd.addElement("8");
		vctNd.addElement(" ");
		vctNd.addElement("9");
		return vctNd;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	public Vector creaNumeroPari( ) {
		this.vctNp = new Vector();
		//valore dei numeri pari
		vctNp.addElement("0");
		vctNp.addElement("1");
		vctNp.addElement("2");
		vctNp.addElement("3");
		vctNp.addElement("4");
		vctNp.addElement("5");
		vctNp.addElement("6");
		vctNp.addElement("7");
		vctNp.addElement("8");
		vctNp.addElement("9");
		return vctNp;
	}

	public String getCodFisc() {
		String s = new String(this.strCodFisc);
		return s;
	}

	@SuppressWarnings("static-access")
	public String getRitorno ( ) {
		String s = new String(this.ritorno);
		return s /*null*/;
	}
	/**
	* Calcola valore del carattere dispari.
	*/
	@SuppressWarnings("static-access")
	public int getVectCarDisp (String elem ) {
		return this.vctCd.indexOf(elem);
	}
	/**
	* Calcola valore del carattere pari.
	* @return int
	*/
	@SuppressWarnings("static-access")
	public int getVectCarPari (String elem) {
		return this.vctCp.indexOf(elem);
	}
	
	@SuppressWarnings("static-access")
	public int getVectMese(String stringa) {
		return this.vctVm.indexOf(stringa);
	}
	/**
	* Calcola valore del numero dispari.
	* @return int
	*/
	@SuppressWarnings("static-access")
	public int getVectNumDisp (String elem) {
		return this.vctNd.indexOf(elem);
	}
	/**
	*Calcola valore del numero pari.
	* @return int
	*/
	@SuppressWarnings("static-access")
	public int getVectNumPari (String elem) {
		return this.vctNp.indexOf(elem);
	}
	/**
	* Inizializzazione di un codice fiscale come stringa.
	* @param s java.lang.String
	*/
	public void setCodFisc(String s) {
		this.strCodFisc = new String(s);
		return;
	}
}
