package com.esed.payer.archiviocarichi.webservice.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaRequest;
import com.seda.data.helper.HelperException;
import com.seda.payer.core.handler.BaseDaoHandler;

public class RichiestaAvvisiDao extends BaseDaoHandler {

	public RichiestaAvvisiDao(Connection connection, String schema) {
		super(connection, schema);
	}

	public CallableStatement prepareCall512AVVIWS(String dbSchemaCodSocieta, RichiestaAvvisoPagoPaRequest in) throws Exception {
		CallableStatement callableStatement = null; 
		try {
			callableStatement = prepareCall("PY512SP_AVVI_WS");
			callableStatement.setString(1, dbSchemaCodSocieta);
			callableStatement.setString(2, in.getCodiceEnte());
			callableStatement.setString(3, in.getStampaDocumento().getNumeroDocumento());
			callableStatement.setString(4, in.getStampaDocumento().getCodiceFiscale());
			if (in.getStampaDocumento().getFlagDatiAttualizzati() != null && in.getStampaDocumento().getFlagDatiAttualizzati().equals("Y")) {
				callableStatement.setString(5,"Y");
			} else {
				callableStatement.setString(5,"N");
			}
			callableStatement.setString(6, in.getImpostaServizio());
			callableStatement.registerOutParameter(7, Types.INTEGER); // CODICE ERR
			callableStatement.registerOutParameter(8, Types.VARCHAR); // MESS ERR
		} catch (SQLException x) {
			throw new Exception(x);
		} catch (IllegalArgumentException x) {
			throw new Exception(x);
		} catch (HelperException x) {
			throw new Exception(x);
		}
		return callableStatement;
	}

}
