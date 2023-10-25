package com.esed.payer.archiviocarichi.webservice.model;

import java.util.List;
import java.util.UUID;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.AutenticazioneApi;
import io.swagger.client.api.DovutiApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.DovutoDto;
import io.swagger.client.model.JppaLoginRequest;
import io.swagger.client.model.JppaLoginResponse;
import io.swagger.client.model.NumeroAvvisoDto;
import io.swagger.client.model.RichiestaCancellaDovutiDto;
import io.swagger.client.model.RichiestaCancellaDovutiDto.CodiceMotivoEliminazioneEnum;
import io.swagger.client.model.RichiestaInviaDovutiRestDto;
import io.swagger.client.model.RichiestaInviaDovutiRestDto.CodiceServizioEnum;
import io.swagger.client.model.RichiestaModificaDovutiDto;
import io.swagger.client.model.RichiestaModificaDovutiDto.OperazioneEnum;
import io.swagger.client.model.RispostaEliminaAvvisoPagamentoDto;
import io.swagger.client.model.RispostaInviaDovutiDto;
import io.swagger.client.model.RispostaModificaDovutiDto;

public class CaricaDebitiJppa {
		
	public String login(String username, String password, String idEnte) {
		AutenticazioneApi apiInstance = new AutenticazioneApi();
		JppaLoginRequest request = new JppaLoginRequest(); // JppaLoginRequest | request
		request.setUsername(username);
		request.setPassword(password);
		request.setIdentificativoEnte(idEnte);
		request.setIdMessaggio(UUID.randomUUID().toString());
	  
		JppaLoginResponse result = null;
		try {
			result = apiInstance.loginUsingPOST(request);
		} catch (ApiException e) {
			System.err.println("Exception when calling AutenticazioneApi#loginUsingPOST");
			e.printStackTrace();
		}
		return result != null ? result.getToken() : "";
	}

	public void inviaDovuti(String token, String codiceIpa, List<DovutoDto> dovutiList) {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth jwtToken = (ApiKeyAuth) defaultClient.getAuthentication("jwtToken");
        jwtToken.setApiKey("Bearer " + token);

		DovutiApi apiInstance = new DovutiApi();
		RichiestaInviaDovutiRestDto richiesta = new RichiestaInviaDovutiRestDto(); // RichiestaInviaDovutiRestDto | richiesta
		richiesta.setCodiceIPA(codiceIpa); 
		richiesta.setCodiceServizio(CodiceServizioEnum.PAGONET);
		richiesta.setDovuti(dovutiList);
		
		try {
		    RispostaInviaDovutiDto result = apiInstance.postInviaDovutiUsingPOST(richiesta);
		    System.out.println(result);
		} catch (ApiException e) {
		    System.err.println("Exception when calling DovutiApi#postInviaDovutiUsingPOST");
		    e.printStackTrace();
		}

	}
	
	public void cancellaDovuto(String token, String codiceIpa, String numeroAvviso) {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth jwtToken = (ApiKeyAuth) defaultClient.getAuthentication("jwtToken");
        jwtToken.setApiKey("Bearer " + token);
        
		DovutiApi apiInstance = new DovutiApi();
		RichiestaCancellaDovutiDto richiesta = new RichiestaCancellaDovutiDto(); // RichiestaCancellaDovutiDto | richiesta
		richiesta.setCodiceIPA(codiceIpa); 
		richiesta.setCodiceMotivoEliminazione(CodiceMotivoEliminazioneEnum.ELIMINAZIONE_DEBITO);
		richiesta.setCodiceServizio(CodiceServizioEnum.PAGONET.toString());
		richiesta.setDescrizioneMotivoEliminazione(CodiceMotivoEliminazioneEnum.ELIMINAZIONE_DEBITO.getValue());
		
		NumeroAvvisoDto numeroAvvisoDto = new NumeroAvvisoDto();
		numeroAvvisoDto.setVersioneNumeroAvviso(0);
		
		numeroAvvisoDto.setNumeroAvviso(numeroAvviso); 
		numeroAvvisoDto.setFlagAttivaDebito(true);
		
		richiesta.setNumeroAvviso(numeroAvvisoDto);
		
		try {
		    RispostaEliminaAvvisoPagamentoDto result = apiInstance.deleteCancellaUsingDELETE(richiesta);
		    System.out.println(result);
		} catch (ApiException e) {
		    System.err.println("Exception when calling DovutiApi#deleteCancellaUsingDELETE");
		    e.printStackTrace();
		}

	}

	public void modificaDovuto(String token, String codiceIpaComune, DovutoDto dovutoDaModificare) {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth jwtToken = (ApiKeyAuth) defaultClient.getAuthentication("jwtToken");
        jwtToken.setApiKey("Bearer " + token);
        
		DovutiApi apiInstance = new DovutiApi();
		RichiestaModificaDovutiDto richiestaModifica = new RichiestaModificaDovutiDto(); // RichiestaModificaDovutiDto | richiestaModifica
		richiestaModifica.setCodiceIPA(codiceIpaComune); 
		richiestaModifica.setCodiceServizio(CodiceServizioEnum.PAGONET.toString());
		richiestaModifica.setDovutoDto(dovutoDaModificare);
		richiestaModifica.setOperazione(OperazioneEnum.ALL);
		
		try {
		    RispostaModificaDovutiDto result = apiInstance.patchModificaUsingPATCH(richiestaModifica);
		    System.out.println(result);
		} catch (ApiException e) {
		    System.err.println("Exception when calling DovutiApi#patchModificaUsingPATCH");
		    e.printStackTrace();
		}

	}
	
}
