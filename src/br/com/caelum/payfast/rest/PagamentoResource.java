package br.com.caelum.payfast.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import br.com.caelum.payfast.modelo.Pagamento;
import br.com.caelum.payfast.modelo.Transacao;
import br.com.caelum.payfast.oauth2.TokenDAO;

/**
 * Gerenciamento de pagamentos através do JAX-RS
 * utilizando a implementação RestEasy do Jboss
 * 
 * @author tca85
 *
 */
@Path("/pagamentos")
@Singleton
public class PagamentoResource {
	private Map<Integer, Pagamento> repositorio = new HashMap<>();
	private Integer idPagamento = 1;
	
	/**
	 * Injeta o TokenDAO através do CDI, para ser usado para a autenticação oAuth 2.0
	 */
	@Inject
	private TokenDAO tokenDAO;
	
	/**
	 * A autenticação oAuth 2.0 exige um HttpServletRequest injetado
	 */
	@Inject 
	private HttpServletRequest request;
 
	//---------------------------------------------------------------------------------------------
	/**
	 * Em um projeto real, o repositório seria o banco de dados
	 * nesse caso estamos usando o construtor para popular um pagamento
	 * no repositório
	 */
	public PagamentoResource() {
		Pagamento pagamento = new Pagamento();
		pagamento.setId(idPagamento++);
		pagamento.setValor(BigDecimal.TEN);
		pagamento.comStatusCriado();
		repositorio.put(pagamento.getId(), pagamento);
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Recebe uma requisição via GET e devolve um JSON ou XML
	 * 
	 * Por padrão, será devolvido em XML quando requisitado direto na URL pelo browser
	 * 
	 * http://localhost:8080/fj36-webservice/pagamentos/1
	 * 
	 * Para testar o retorno via JSON:
	 * curl -i -H "Accept: application/json" http://localhost:8080/fj36-webservice/pagamentos/1
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Pagamento buscaPagamento(@PathParam("id") Integer id){
		return repositorio.get(id);
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Recebe uma transação financeira para criar um pagamento a partir de seus dados
	 * 
	 * curl -i -H "Content-type: application/json" -X POST -d '{"valor":"39.9", "titular":"Fulano"}' localhost:8080/fj36-webservice/pagamentos
	 * 
	 * @param transacao
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response criarPagamento(Transacao transacao) throws URISyntaxException{
		
		// Valida a access token da requisição antes de cadastrar um novo pagamento
		// se o código for inválido, devolve uma resposta com o código de erro 401 (unauthorized)
		Response unauthorized = Response.status(Status.UNAUTHORIZED).build();
		
		try {
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request);
			
			String accessToken = oauthRequest.getAccessToken();
			
			if (tokenDAO.existeAccessToken(accessToken)) {
				Pagamento pagamento = new Pagamento();
				
				pagamento.setId(idPagamento++);
				pagamento.setValor(transacao.getValor());
				
				pagamento.comStatusCriado();
				
				// após isso, ele retorna as urls utilizando o conceito de HATEOAS:
				//
				// {"id":2,"status":"CRIADO","valor":39.9,
				//   "links":[
				//       {"rel":"confirmar","uri":"/pagamentos/2","method":"PUT"},
				//       {"rel":"cancelar","uri":"/pagamentos/2","method":"DELETE"}
				//    ]
				//  }
				
				repositorio.put(pagamento.getId(), pagamento);
				
				System.out.println("Pagamento criado " + pagamento);
				
				return Response.created(new URI("/pagamentos" + pagamento.getId()))
						       .entity(pagamento)
						       .type(MediaType.APPLICATION_JSON_TYPE)
						       .build();				
			}else{
				return unauthorized;
			}
			
		} catch (OAuthSystemException | OAuthProblemException e) {
			return unauthorized;
		}
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * A chamada do método criarPagamento retorna a lista dos métodos
	 * possíveis a serem utilizados. Nesse caso, estamos tratanto o PUT
	 * 
	 *  {"rel":"confirmar","uri":"/pagamentos/2","method":"PUT"}
	 *  
	 *  curl -i -H "Content-type: application/json" -X PUT http://localhost:8080/fj36-webservice/pagamentos/2
	 * 
	 * @param pagamentoId
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Pagamento confirmarPagamento(@PathParam("id") Integer pagamentoId){
		Pagamento pagamento = repositorio.get(pagamentoId);
		pagamento.comStatusConfirmado();
		
		System.out.println("Pagamento confirmado " + pagamento);
		
		return pagamento;
	}	
	//---------------------------------------------------------------------------------------------
}
