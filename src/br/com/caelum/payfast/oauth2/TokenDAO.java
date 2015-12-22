package br.com.caelum.payfast.oauth2;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

/**
 * Utilizando oAuth 2.0
 * 
 * Resource Server - a aplicação que contém os dados ou recursos
 * que queremos acessar em nome do Resourcer Owner (usuário que tentou
 * fechar a compra no sistema da livraria).
 * 
 * 
 * @author tca85
 *
 */
@ApplicationScoped
public class TokenDAO {
	
	private List<String> accessTokens = new ArrayList<>();
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Adiciona o token de acesso
	 * 
	 * @param token
	 */
	public void adicionaAccessToken(String token){
		accessTokens.add(token);
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Verifica se o token é válido
	 * 
	 * @param token
	 * @return
	 */
	public boolean existeAccessToken(String token){
		return accessTokens.contains(token);
	}
	//---------------------------------------------------------------------------------------------
}