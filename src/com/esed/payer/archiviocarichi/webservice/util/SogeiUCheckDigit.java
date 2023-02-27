package com.esed.payer.archiviocarichi.webservice.util;

import java.util.Hashtable;
import java.util.Vector;

public class SogeiUCheckDigit {
	  private String strCodFisc;
	  private static Vector<String> vctCd;
	  private static Vector<String> vctCp;
	  private static Vector<String> vctNd;
	  private static Vector<String> vctNp;
	  private static Vector<String> vctVm;
	  private static Hashtable<String, Integer> ctr_giorno;
	  
	  static
	  {
	    creaCarattereDispari();
	    creaCaratterePari();
	    creaNumeroDispari();
	    creaNumeroPari();
	    creaMese();
	    crea_ctr_giorno();
	  }
	  
	  public SogeiUCheckDigit(String g)
	  {
	    this.strCodFisc = g;
	  }
	  
	  public boolean controllaCheckDigit()
	  {
	    int intAppoggio = 0;
	    for (int i = 0; i < 15; i++)
	    {
	      char chrCarattereEsaminato = getCodFisc().charAt(i);
	      String strElem = getCodFisc().substring(i, i + 1);
	      int intResto = i % 2;
	      switch (intResto)
	      {
	      case 0: 
	        if (!Character.isDigit(chrCarattereEsaminato)) {
	          intAppoggio += getVectCarDisp(strElem);
	        } else {
	          intAppoggio += getVectNumDisp(strElem);
	        }
	        break;
	      case 1: 
	        if (!Character.isDigit(chrCarattereEsaminato)) {
	          intAppoggio += getVectCarPari(strElem);
	        } else {
	          intAppoggio += getVectNumPari(strElem);
	        }
	        break;
	      }
	    }
	    String ceckdigit = getCodFisc().substring(15, 16);
	    return intAppoggio % 26 == getVectCarPari(ceckdigit);
	  }
	  
	  public boolean controllaCorrettezza()
	  {
	    if (controllaCorrettezzaChar() == '0') {
	      return true;
	    }
	    return false;
	  }
	  
	  public char controllaCorrettezzaChar()
	  {
	    boolean bolLettera = false;
	    for (int i = 0; i < 6; i++) {
	      if ((this.strCodFisc.charAt(i) != 'A') && (this.strCodFisc.charAt(i) != 'B') && (this.strCodFisc.charAt(i) != 'C') && 
	        (this.strCodFisc.charAt(i) != 'D') && (this.strCodFisc.charAt(i) != 'E') && (this.strCodFisc.charAt(i) != 'F') && 
	        (this.strCodFisc.charAt(i) != 'G') && (this.strCodFisc.charAt(i) != 'H') && (this.strCodFisc.charAt(i) != 'I') && 
	        (this.strCodFisc.charAt(i) != 'J') && (this.strCodFisc.charAt(i) != 'K') && (this.strCodFisc.charAt(i) != 'L') && 
	        (this.strCodFisc.charAt(i) != 'M') && (this.strCodFisc.charAt(i) != 'N') && (this.strCodFisc.charAt(i) != 'O') && 
	        (this.strCodFisc.charAt(i) != 'P') && (this.strCodFisc.charAt(i) != 'Q') && (this.strCodFisc.charAt(i) != 'R') && 
	        (this.strCodFisc.charAt(i) != 'S') && (this.strCodFisc.charAt(i) != 'T') && (this.strCodFisc.charAt(i) != 'U') && 
	        (this.strCodFisc.charAt(i) != 'V') && (this.strCodFisc.charAt(i) != 'W') && (this.strCodFisc.charAt(i) != 'X') && 
	        (this.strCodFisc.charAt(i) != 'Y') && (this.strCodFisc.charAt(i) != 'Z')) {
	        return '2';
	      }
	    }
	    for (int i = 6; i < 8; i++) {
	      if ((!Character.isDigit(this.strCodFisc.charAt(i))) && 
	        (this.strCodFisc.charAt(i) != 'L') && (this.strCodFisc.charAt(i) != 'M') && 
	        (this.strCodFisc.charAt(i) != 'N') && (this.strCodFisc.charAt(i) != 'P') && 
	        (this.strCodFisc.charAt(i) != 'Q') && (this.strCodFisc.charAt(i) != 'R') && 
	        (this.strCodFisc.charAt(i) != 'S') && (this.strCodFisc.charAt(i) != 'T') && 
	        (this.strCodFisc.charAt(i) != 'U') && (this.strCodFisc.charAt(i) != 'V')) {
	        return '2';
	      }
	    }
	    if ((this.strCodFisc.charAt(8) != 'A') && (this.strCodFisc.charAt(8) != 'B') && 
	      (this.strCodFisc.charAt(8) != 'C') && (this.strCodFisc.charAt(8) != 'D') && 
	      (this.strCodFisc.charAt(8) != 'E') && (this.strCodFisc.charAt(8) != 'H') && 
	      (this.strCodFisc.charAt(8) != 'L') && (this.strCodFisc.charAt(8) != 'M') && 
	      (this.strCodFisc.charAt(8) != 'P') && (this.strCodFisc.charAt(8) != 'R') && 
	      (this.strCodFisc.charAt(8) != 'S') && (this.strCodFisc.charAt(8) != 'T')) {
	      return '2';
	    }
	    for (int i = 9; i < 11; i++) {
	      if ((!Character.isDigit(this.strCodFisc.charAt(i))) && 
	        (this.strCodFisc.charAt(i) != 'L') && (this.strCodFisc.charAt(i) != 'M') && 
	        (this.strCodFisc.charAt(i) != 'N') && (this.strCodFisc.charAt(i) != 'P') && 
	        (this.strCodFisc.charAt(i) != 'Q') && (this.strCodFisc.charAt(i) != 'R') && 
	        (this.strCodFisc.charAt(i) != 'S') && (this.strCodFisc.charAt(i) != 'T') && 
	        (this.strCodFisc.charAt(i) != 'U') && (this.strCodFisc.charAt(i) != 'V')) {
	        return '2';
	      }
	    }
	    int intGiorno = trasforma_giorno(9);
	    if (intGiorno > 31) {
	      intGiorno -= 40;
	    }
	    if ((intGiorno < 1) || (intGiorno > 31)) {
	      return '2';
	    }
	    String strElem = this.strCodFisc.substring(8, 9);
	    String strMese = String.valueOf(getVectMese(strElem));
	    if (strMese.length() == 1) {
	      strMese = "0" + strMese;
	    }
	    String strAnno = String.valueOf(trasforma_giorno(6));
	    if (strAnno.length() == 1) {
	      strAnno = "0" + strAnno;
	    }
	    String strGiorno = String.valueOf(intGiorno);
	    if (strGiorno.length() == 1) {
	      strGiorno = "0" + strGiorno;
	    }
	    String data = strGiorno + strMese + strAnno;
	    if (!controllaData(data)) {
	      return '2';
	    }
	    if ((this.strCodFisc.charAt(11) != 'A') && (this.strCodFisc.charAt(11) != 'B')) {
	      if ((this.strCodFisc.charAt(11) != 'C') && (this.strCodFisc.charAt(11) != 'D')) {
	        if ((this.strCodFisc.charAt(11) != 'E') && (this.strCodFisc.charAt(11) != 'F')) {
	          if ((this.strCodFisc.charAt(11) != 'G') && (this.strCodFisc.charAt(11) != 'H')) {
	            if ((this.strCodFisc.charAt(11) != 'I') && (this.strCodFisc.charAt(11) != 'L')) {
	              if ((this.strCodFisc.charAt(11) != 'M') && (this.strCodFisc.charAt(11) != 'Z')) {
	                return '2';
	              }
	            }
	          }
	        }
	      }
	    }
	    for (int i = 12; i < 15; i++) {
	      if (!Character.isDigit(this.strCodFisc.charAt(i)))
	      {
	        bolLettera = true;
	        if ((this.strCodFisc.charAt(i) != 'L') && (this.strCodFisc.charAt(i) != 'M') && 
	          (this.strCodFisc.charAt(i) != 'N') && (this.strCodFisc.charAt(i) != 'P') && 
	          (this.strCodFisc.charAt(i) != 'Q') && (this.strCodFisc.charAt(i) != 'R') && 
	          (this.strCodFisc.charAt(i) != 'S') && (this.strCodFisc.charAt(i) != 'T') && 
	          (this.strCodFisc.charAt(i) != 'U') && (this.strCodFisc.charAt(i) != 'V')) {
	          return '3';
	        }
	      }
	    }
	    if (!bolLettera)
	    {
	      int intNumeroCodCat = Integer.parseInt(this.strCodFisc.substring(12, 15));
	      if (intNumeroCodCat == 0) {
	        return '2';
	      }
	    }
	    if (controllaCheckDigit()) {
	      return '0';
	    }
	    return '1';
	  }
	  
	  public int controllaCorrettezzaInt()
	  {
	    return controllaCorrettezzaChar() - '0';
	  }
	  
	  public boolean controllaData(String s)
	  {
	    try
	    {
	      String strAnno = s.substring(4, s.length());
	      if ((s.length() == 8) || (s.length() == 6))
	      {
	        if (s.length() == 6) {
	          strAnno = "19" + s;
	        }
	        if (Integer.parseInt(strAnno) < 1870) {
	          return false;
	        }
	      }
	      else
	      {
	        return false;
	      }
	      String strMese = s.substring(2, 4);
	      String strGiorno = s.substring(0, 2);
	      

	      int intMese = Integer.parseInt(strMese);
	      int intGiorno = Integer.parseInt(strGiorno);
	      int intAnno = Integer.parseInt(strAnno);
	      if ((intMese > 12) || (intGiorno > 31) || (intMese < 1) || (intGiorno < 1)) {
	        return false;
	      }
	      switch (intMese)
	      {
	      case 2: 
	        boolean bisestile = false;
	        if (intAnno % 4 == 0) {
	          if (intAnno % 400 == 0)
	          {
	            if (intAnno % 1000 == 0) {
	              bisestile = true;
	            }
	          }
	          else {
	            bisestile = true;
	          }
	        }
	        if (((bisestile) && (intGiorno > 29)) || ((!bisestile) && (intGiorno > 28))) {
	          return false;
	        }
	        break;
	      case 4: 
	        if (intGiorno > 30) {
	          return false;
	        }
	        break;
	      case 6: 
	        if (intGiorno > 30) {
	          return false;
	        }
	        break;
	      case 9: 
	        if (intGiorno > 30) {
	          return false;
	        }
	        break;
	      case 11: 
	        if (intGiorno > 30) {
	          return false;
	        }
	        break;
	      }
	      return true;
	    }
	    catch (Exception e) {}
	    return false;
	  }
	  
	  public static Vector creaCarattereDispari()
	  {
	    vctCd = new Vector();
	    
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
	  
	  public static Vector creaCaratterePari()
	  {
	    vctCp = new Vector();
	    
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
	  
	  public static Vector creaMese()
	  {
	    vctVm = new Vector();
	    
	    vctVm.addElement(" ");
	    vctVm.addElement("A");
	    vctVm.addElement("B");
	    vctVm.addElement("C");
	    vctVm.addElement("D");
	    vctVm.addElement("E");
	    vctVm.addElement("H");
	    vctVm.addElement("L");
	    vctVm.addElement("M");
	    vctVm.addElement("P");
	    vctVm.addElement("R");
	    vctVm.addElement("S");
	    vctVm.addElement("T");
	    
	    return vctVm;
	  }
	  
	  public static Vector creaNumeroDispari()
	  {
	    vctNd = new Vector();
	    
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
	  
	  public static Hashtable<String, Integer> crea_ctr_giorno()
	  {
	    ctr_giorno = new Hashtable();
	    ctr_giorno.put("L", Integer.valueOf(0));
	    ctr_giorno.put("M", Integer.valueOf(1));
	    ctr_giorno.put("N", Integer.valueOf(2));
	    ctr_giorno.put("P", Integer.valueOf(3));
	    ctr_giorno.put("Q", Integer.valueOf(4));
	    ctr_giorno.put("R", Integer.valueOf(5));
	    ctr_giorno.put("S", Integer.valueOf(6));
	    ctr_giorno.put("T", Integer.valueOf(7));
	    ctr_giorno.put("U", Integer.valueOf(8));
	    ctr_giorno.put("V", Integer.valueOf(9));
	    

	    return ctr_giorno;
	  }
	  
	  public static Vector creaNumeroPari()
	  {
	    vctNp = new Vector();
	    
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
	  
	  public String getCodFisc()
	  {
	    String s = new String(this.strCodFisc);
	    return s;
	  }
	  
	  public int getVectCarDisp(String elem)
	  {
	    return vctCd.indexOf(elem);
	  }
	  
	  public int getVectCarPari(String elem)
	  {
	    return vctCp.indexOf(elem);
	  }
	  
	  public int getVectMese(String stringa)
	  {
	    return vctVm.indexOf(stringa);
	  }
	  
	  public int getVectNumDisp(String elem)
	  {
	    return vctNd.indexOf(elem);
	  }
	  
	  public int getVectNumPari(String elem)
	  {
	    return vctNp.indexOf(elem);
	  }
	  
	  public void setCodFisc(String s)
	  {
	    this.strCodFisc = new String(s);
	  }
	  
	  public Hashtable<String, Integer> getCtr_giorno()
	  {
	    return ctr_giorno;
	  }
	  
	  public void setCtr_giorno(Hashtable<String, Integer> ctr_giorno)
	  {
	    this.ctr_giorno = ctr_giorno;
	  }
	  
	  public int trasforma_giorno(int c)
	  {
	    String appo = "";
	    for (int i = c; i < c + 2; i++) {
	      if (Character.isDigit(this.strCodFisc.charAt(i))) {
	        appo = appo + this.strCodFisc.charAt(i);
	      } else {
	        appo = appo + getCtr_giorno().get(String.valueOf(this.strCodFisc.charAt(i)));
	      }
	    }
	    return Integer.parseInt(appo);
	  }
}
