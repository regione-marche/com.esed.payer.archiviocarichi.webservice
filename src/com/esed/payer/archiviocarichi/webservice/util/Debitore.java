package com.esed.payer.archiviocarichi.webservice.util;

import java.io.Serializable;
import java.util.Calendar;

public class Debitore implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String tipoDebitore; /* G=persona giuridica F=persona fisica

	/* DATI PERSONA FISICA */
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String sesso;
	private Calendar dataNascita;
	private String comuneNascitaBelfiore;

	
	/* DATI PERSONA GIURIDICA */
	private String ragioneSociale;
	private String partitaIVA;

	public void setTipoDebitore(String tipoDebitore) {
		this.tipoDebitore = tipoDebitore;
	}
	public String getTipoDebitore() {
		return tipoDebitore;
	}

	public String getCognome() {
		return cognome != null ? cognome : "";
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome != null ? nome : "";
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCodiceFiscale() {
		return codiceFiscale != null ? codiceFiscale.toUpperCase() : "";
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getSesso() {
		return sesso != null ? sesso : "M";
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public Calendar getDataNascita() {
		return dataNascita != null ? dataNascita : GenericsDateNumbers.getMinDate();
	}
	public void setDataNascita(Calendar dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getComuneNascitaBelfiore() {
		return comuneNascitaBelfiore != null ? comuneNascitaBelfiore : "";
	}
	public void setComuneNascitaBelfiore(String comuneNascitaBelfiore) {
		this.comuneNascitaBelfiore = comuneNascitaBelfiore;
	}
	public String getPartitaIVA() {
		return partitaIVA != null ? partitaIVA : "";
	}
	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}
	public String getRagioneSociale() {
		return ragioneSociale != null ? ragioneSociale : "";
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
}
