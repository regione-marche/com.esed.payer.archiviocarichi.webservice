package com.esed.payer.archiviocarichi.webservice.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.seda.data.helper.Helper;
import com.seda.data.helper.HelperException;
import com.seda.payer.core.bean.ArchivioCarichiDocumento;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.handler.BaseDaoHandler;

public class InviaDovutiDao extends BaseDaoHandler {

	private Connection connection;
	private String schema = null;

	protected CallableStatement callableStatementGetCodiceIpa = null;
	protected CallableStatement callableStatementFlagDovutoInviato = null;
	protected CallableStatement callableStatementGetDocumento = null;

	public InviaDovutiDao(Connection connection, String schema) {
		super(connection, schema);
		this.connection = connection;
		this.schema = schema;
	}

	public String getCodiceIpa(String primoArg, String codiceEnte) throws Exception {
		ResultSet res = null;
		String codiceIpa = "";

		try {
			if (primoArg.length() <= 5) {
				// � un codice utente
				callableStatementGetCodiceIpa = Helper.prepareCall(connection, schema, "PYENTSP_SEL_INFO_CIPA2");
				callableStatementGetCodiceIpa.setString(1, primoArg); // ENT_CUTECUTE (cute cute)
				callableStatementGetCodiceIpa.setString(2, codiceEnte); // ANE_CANECENT
				callableStatementGetCodiceIpa.execute();
				res = callableStatementGetCodiceIpa.getResultSet();
				if (res.next()) {
					codiceIpa = res.getString("ENT_CENTMYCO");
				}
			} else {
				// � un codice fiscale
				callableStatementGetCodiceIpa = Helper.prepareCall(connection, schema, "PYENTSP_SEL_INFO_CIPA");
				callableStatementGetCodiceIpa.setString(1, primoArg); // ENT_CENTCFIS (idDominio)																		// "AAABBB00A00A123B"
				callableStatementGetCodiceIpa.setString(2, codiceEnte); // ANE_CANECENT
				callableStatementGetCodiceIpa.execute();
				res = callableStatementGetCodiceIpa.getResultSet();
				if (res.next()) {
					codiceIpa = res.getString("ENT_CENTMYCO");
				}
			}
			return codiceIpa;

		} catch (SQLException x) {
			throw new Exception(x);
		} catch (IllegalArgumentException x) {
			throw new Exception(x);
		} catch (HelperException x) {
			throw new Exception(x);
		} finally {
			if (callableStatementFlagDovutoInviato != null) {
				try {
					callableStatementFlagDovutoInviato.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void aggiornaFlagInviaDovuto(int progressivoFlusso, String schema) throws Exception {
		try {
			callableStatementFlagDovutoInviato = Helper.prepareCall(connection, schema, "PYEH0SP_UPD_INV");
			callableStatementFlagDovutoInviato.setInt(1, progressivoFlusso);
			callableStatementFlagDovutoInviato.setString(2, "Y");
			callableStatementFlagDovutoInviato.execute();
		} catch (SQLException x) {
			throw new Exception(x);
		} catch (IllegalArgumentException x) {
			throw new Exception(x);
		} catch (HelperException x) {
			throw new Exception(x);
		} finally {
			if (callableStatementFlagDovutoInviato != null) {
				try {
					callableStatementFlagDovutoInviato.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<ArchivioCarichiDocumento> getDocumento(ArchivioCarichiDocumento in) throws DaoException {
		List<ArchivioCarichiDocumento> listaDocumenti = new ArrayList<>();
		try {

			connection.setAutoCommit(true);				
			callableStatementGetDocumento = Helper.prepareCall(connection, schema, "PYEH1SP");	
			
			if(in != null) {
				callableStatementGetDocumento.setString(1, in.getCodiceUtente());
				callableStatementGetDocumento.setString(2, in.getCodiceEnte());
				callableStatementGetDocumento.setString(3, "");
				callableStatementGetDocumento.setString(4, "");
				callableStatementGetDocumento.setString(5, "");
				callableStatementGetDocumento.setString(6, in.getTipoServizio());
				callableStatementGetDocumento.setString(7, "");
				callableStatementGetDocumento.setString(8, "");
				callableStatementGetDocumento.setString(9, "");
				callableStatementGetDocumento.setString(10, in.getNumeroDocumento());
				callableStatementGetDocumento.setString(11, "");
				callableStatementGetDocumento.setString(12, "");
				callableStatementGetDocumento.setString(13, "");
				callableStatementGetDocumento.registerOutParameter(14, Types.CHAR);
				callableStatementGetDocumento.registerOutParameter(15, Types.VARCHAR);
			}
			
			if (callableStatementGetDocumento.execute()) {
				ResultSet rs = callableStatementGetDocumento.getResultSet();
				while (rs.next()) {
					ArchivioCarichiDocumento doc = new ArchivioCarichiDocumento();
					doc.setCodiceUtente(rs.getString(1));
					doc.setCodiceEnte(rs.getString(2));
					doc.setTipoUfficio(rs.getString(3));
					doc.setCodiceUfficio(rs.getString(4));
					doc.setTipoServizio(rs.getString(5));
					doc.setImpostaServizio(rs.getString(6));
					doc.setNumeroDocumento(rs.getString(7));
					doc.setNumeroBollettinoPagoPA(rs.getString(8));
					doc.setImpBollettinoTotaleDocumento(rs.getBigDecimal(10));
					doc.setIbanAccredito(rs.getString(11));
					doc.setIbanAppoggio(rs.getString(12));
					doc.setCodiceTipologiaServizio(rs.getString(13));
					listaDocumenti.add(doc);
				}
			}
		} catch (SQLException x) {
			throw new DaoException(x);
		} catch (IllegalArgumentException x) {
			throw new DaoException(x);
		} catch (HelperException x) {
			throw new DaoException(x);
		} finally {
		}
		return listaDocumenti;
	}
}
