package com.esed.payer.archiviocarichi.webservice.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.seda.commons.validator.Validation;
import com.seda.commons.validator.ValidationException;

public class GenericsDateNumbers {
	
	/*********************************** OPERAZIONI DATE - CALENDAR **************************/
	
	public static Calendar getMinDate()
	{
		Timestamp timestamp = Timestamp.valueOf("1000-01-01 00:00:00.000");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp.getTime());

		//Calendar cal = Calendar.getInstance();
		//cal.set(1970,0,1,0,0,0);
		return cal;
	}
	
	public static Calendar getCurrentDate()
	{
		return Calendar.getInstance();
	}
	
	public static String calendarToString (Calendar calendar, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String formatCalendar="";
		
		if(calendar!=null){
			formatCalendar = dateFormat.format(calendar.getTime());
		}
				
		return formatCalendar;
	}
	
	/**
	 * Converte una data stringa da un formato ad un altro
	 * @param sData
	 * @param sFormatoOld
	 * @param sFormatoNew
	 * @return
	 */
	public static String formatData(String sData, String sFormatoOld, String sFormatoNew)
	{
		if (sData != null && !sData.equals(""))
		{
			SimpleDateFormat formatDateTimeOld = new SimpleDateFormat(sFormatoOld);
			SimpleDateFormat formatDateTimeNew = new SimpleDateFormat(sFormatoNew);
	    	try
	    	{
				Date date = formatDateTimeOld.parse(sData);
				
				return formatDateTimeNew.format(date);
	    	}
	    	catch (Exception e) {}
		}
		return "";
	}
	
	/***
	 * Ritorna un Calendar da una data passata nel formato passato come parametro
	 * @param sDataDDMMYYYY
	 * @return
	 */
	public static Calendar getCalendarFromDateString(String sData, String sDateFormat)
	{
		if (sData != null && !sData.equals(""))
		{
			try
			{
				SimpleDateFormat formatDateTime = new SimpleDateFormat(sDateFormat);
				Date date = formatDateTime.parse(sData);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				return cal;
			} 
			catch (Exception e) {
				return getMinDate();
			}
		}
		
		return getMinDate();
	}
	
	//inizio LP PG22XX05
	public static Calendar getCalendarOrNullFromDateString(String sData, String sDateFormat)
	{
		if (sData != null && !sData.equals(""))
		{
			try
			{
				SimpleDateFormat formatDateTime = new SimpleDateFormat(sDateFormat);
				Date date = formatDateTime.parse(sData);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				return cal;
			} 
			catch (Exception e) {
				return null;
			}
		}
		
		return null;
	}	
	//fine LP PG222XX05
	public static Calendar getCalendarFromDate(java.util.Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static java.util.Date getDateFromCalendar(Calendar cal) {
		if (cal == null)
			return null;
		return cal.getTime();
	}

	
/*********************************** OPERAZIONI NUMERI **************************/
	
	public static Double stringToDouble(String arg) {
			return new Double(Double.valueOf(arg).doubleValue() / 100D);
	}
	 
	public static Double bigDecimalToDouble(BigDecimal arg) {
			return new Double(arg.doubleValue() / 100D);
	}
	
	public static String formatDecimalNumber(BigDecimal bdValue)
	{
		DecimalFormat dcFormat = getDecimalFormat();
		bdValue = bdValue.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return dcFormat.format(bdValue);
	}
	
	public static DecimalFormat getDecimalFormat()
	{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(); 
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');

		DecimalFormat dcFormat = new DecimalFormat("#0.00", symbols);
		return dcFormat;
	}
	//inizio LP PG210130
	public static boolean isIbanPostale(String codiceIban) {
		String abiPosteItaliane = "07601";
		try {
			//              1          2 
			//01 23 4 56789 01234 567890123456
		    //IT 60 X 05428 11101 000000123456
			//IT 54 P 07601 01200 000011763117
			return codiceIban.substring(5, 10).equals(abiPosteItaliane);
		} catch (Exception e) {
		}
		return false;
	}
	//fine LP PG210130
}
