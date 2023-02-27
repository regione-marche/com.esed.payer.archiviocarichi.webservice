package com.esed.payer.archiviocarichi.webservice.util;

public class SogeiUCheckNum {
	  private String strCfNum;
	  private int pintCodUff;
	  
	  public SogeiUCheckNum(String s)
	  {
	    this.strCfNum = s;
	  }
	  
	  public String cfNum(String p)
	  {
	    String strRetcode = null;
	    if (Integer.parseInt(p.substring(0, 7)) < 8000000) {
	      strRetcode = "2";
	    } else if (this.pintCodUff > 95) {
	      strRetcode = "N";
	    } else {
	      strRetcode = "5";
	    }
	    if ((!controlCfNum(p.substring(0, 11))) && (strRetcode == "2")) {
	      strRetcode = "Q";
	    } else if (!controlCfNum(p.substring(0, 11))) {
	      strRetcode = "T";
	    }
	    return strRetcode;
	  }
	  
	  public String codificaRetCodeCfNum(char chrValore)
	  {
	    String strRetcode = null;
	    switch (chrValore)
	    {
	    case '0': 
	      strRetcode = "NUMERO DI CODICE FISCALE DEFINITIVO";
	      break;
	    case '1': 
	      strRetcode = "NUMERO DI CODICE FISCALE PROVVISORIO ASSEGNATO D'UFFICIO (DU.74 DU.75 101.75)";
	      break;
	    case '2': 
	      strRetcode = "NUMERO DI CODICE FISCALE DELLE P.N.F. OPPURE NUMERO DI PARTITA I.V.A.";
	      break;
	    case '3': 
	      strRetcode = "NUMERO DI CODICE FISCALE PROVVISORIO ASSEGNATO DALLE INTENDENZE DI FINANZE";
	      break;
	    case '4': 
	      strRetcode = "NUMERO DI CODICE FISCALE PROVVISORIO ASSEGNATO DALLE IMPOSTE DIRETTE";
	      break;
	    case '5': 
	      strRetcode = "NUMERO DI CODICE FISCALE DELLE P.N.F. NON CONTRIBUENTI I.V.A.";
	      break;
	    case '6': 
	      strRetcode = "NUMERO DI CODICE FISCALE DEFINITIVO P.F. SOPRAELEVATA DI OMOCODICE";
	      break;
	    case 'A': 
	      strRetcode = "NUMERO DI CODICE FISCALE DEFINITIVO CON CHECK DIGIT ERRATO";
	      break;
	    case 'B': 
	      strRetcode = "NUMERO DI CODICE FISCALE DEFINITIVO FORMALMENTE ERRATO";
	      break;
	    case 'I': 
	      strRetcode = "NUMERO DI CODICE FISCALE A 11 CIFRE NON ALLINEATO";
	      break;
	    case 'L': 
	      strRetcode = "NUMERO DI CODICE FISCALE A 11 CIFRE NON NUMERICO";
	      break;
	    case 'M': 
	      strRetcode = "NUMERO DI CODICE FISCALE A 11 CIFRE AVENTE LE PRIME 7 CIFRE A ZERO";
	      break;
	    case 'N': 
	      strRetcode = "NUMERO DI CODICE FISCALE A 11 CIFRE CON NUMERO DI CODICE UFFICIO ERRATO";
	      break;
	    case 'O': 
	      strRetcode = "NUMERO DI CODICE FISCALE PROVVISORIO ASSEGNATO D'UFFICIO AVENTE PROGRESSIVO NUMERICO NON AMMISSIBILE (FUORI RANGE)";
	      break;
	    case 'P': 
	      strRetcode = "NUMERO DI CODICE FISCALE PROVVISORIO ASSEGNATO D'UFFICIO (DU.75 101.75) CON CHECK DIGIT ERRATO";
	      break;
	    case 'Q': 
	      strRetcode = "NUMERO DI CODICE FISCALE DELLE P.N.F. OPPURE NUMERO DI PARTITA I.V.A. CON CHECK DIGIT ERRATO";
	      break;
	    case 'R': 
	      strRetcode = "NUMERO DI CODICE FISCALE PROVVISORIO ASSEGNATO DALLE II.FF. CON CHECK DIGIT ERRATO";
	      break;
	    case 'S': 
	      strRetcode = "NUMERO DI CODICE FISCALE PROVVISORIO ASSEGNATO DALLE II.DD. CON CHECK DIGIT ERRATO";
	      break;
	    case 'T': 
	      strRetcode = "NUMERO DI CODICE FISCALE DELLE P.N.F. NON CONTRIBUENTI I.V.A. CON CHECK DIGIT ERRATO";
	      break;
	    }
	    return strRetcode;
	  }
	  
	  public boolean controlCfNum(String s)
	  {
	    int pintTotale = 0;
	    for (int i = 1; i < 11; i += 2)
	    {
	      String strElem = getCfNum().substring(i, i + 1);
	      Integer intAppoggio = Integer.valueOf(strElem);
	      int pintAppo = intAppoggio.intValue() * 2;
	      String strS2 = String.valueOf(pintAppo);
	      for (int j = 0; j < strS2.length(); j++)
	      {
	        String strElem1 = strS2.substring(j, j + 1);
	        pintTotale += Integer.parseInt(strElem1);
	      }
	    }
	    for (int k = 0; k < 9; k += 2)
	    {
	      String strElem2 = getCfNum().substring(k, k + 1);
	      pintTotale += Integer.parseInt(strElem2);
	    }
	    String strElem = getCfNum().substring(10, 11);
	    Integer intAppoggio = Integer.valueOf(strElem);
	    int pintUltimoCarattere = intAppoggio.intValue();
	    if (pintTotale % 10 == 0) {
	      return pintTotale % 10 == pintUltimoCarattere;
	    }
	    return 10 - pintTotale % 10 == pintUltimoCarattere;
	  }
	  
	  public boolean controllaCfNum()
	  {
	    char c = trattCfNum().charAt(0);
	    switch (c)
	    {
	    case '0': 
	      return true;
	    case '1': 
	      return true;
	    case '2': 
	      return true;
	    case '3': 
	      return true;
	    case '4': 
	      return true;
	    case '5': 
	      return true;
	    case 'A': 
	      return false;
	    case 'B': 
	      return false;
	    case 'I': 
	      return false;
	    case 'L': 
	      return false;
	    case 'M': 
	      return false;
	    case 'N': 
	      return false;
	    case 'O': 
	      return false;
	    case 'P': 
	      return false;
	    case 'Q': 
	      return false;
	    case 'R': 
	      return false;
	    case 'S': 
	      return false;
	    case 'T': 
	      return false;
	    }
	    return false;
	  }
	  
	  public String getCfNum()
	  {
	    String s = new String(this.strCfNum);
	    return s;
	  }
	  
	  public void setCfNum(String s1)
	  {
	    this.strCfNum = new String(s1);
	  }
	  
	  public String trattCfNum()
	  {
	    for (int i = 0; i < 11; i++) {
	      if (!Character.isDigit(this.strCfNum.charAt(i))) {
	        return "L";
	      }
	    }
	    try
	    {
	      this.pintCodUff = Integer.parseInt(this.strCfNum.substring(7, 10));
	    }
	    catch (RuntimeException exc)
	    {
	      return "L";
	    }
	    if (this.strCfNum.substring(0, 7) == "0000000") {
	      return "M";
	    }
	    if (this.pintCodUff == 0)
	    {
	      if ((Integer.parseInt(this.strCfNum.substring(0, 7)) > 0) && (Integer.parseInt(this.strCfNum.substring(0, 7)) < 273961)) {
	        return "1";
	      }
	      if (((Integer.parseInt(this.strCfNum.substring(0, 7)) > 131072) && (Integer.parseInt(this.strCfNum.substring(0, 7)) < 1072480)) || 
	        ((Integer.parseInt(this.strCfNum.substring(0, 7)) > 1500000) && (Integer.parseInt(this.strCfNum.substring(0, 7)) < 1828637)) || (
	        (Integer.parseInt(this.strCfNum.substring(0, 7)) > 2000000) && (Integer.parseInt(this.strCfNum.substring(0, 7)) < 2054096)))
	      {
	        if (controlCfNum(this.strCfNum)) {
	          return "1";
	        }
	        return "P";
	      }
	      return "O";
	    }
	    if (this.pintCodUff == 999) {
	      return cfNum(this.strCfNum);
	    }
	    if (((this.pintCodUff > 0) && (this.pintCodUff < 101)) || ((this.pintCodUff > 119) && (this.pintCodUff < 122)) || (this.pintCodUff == 888)) {
	      return cfNum(this.strCfNum);
	    }
	    if ((this.pintCodUff > 150) && (this.pintCodUff < 246))
	    {
	      if (controlCfNum(this.strCfNum)) {
	        return "3";
	      }
	      return "R";
	    }
	    if ((this.pintCodUff > 300) && (this.pintCodUff < 767))
	    {
	      if (controlCfNum(this.strCfNum.substring(0, 11))) {
	        return "4";
	      }
	      return "S";
	    }
	    if ((this.pintCodUff > 899) && (this.pintCodUff < 951))
	    {
	      if (controlCfNum(this.strCfNum.substring(0, 11))) {
	        return "4";
	      }
	      return "S";
	    }
	    return "N";
	  }
}
