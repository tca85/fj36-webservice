package br.com.caelum.payfast.rest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.caelum.payfast.modelo.Pagamento;

/**
 * Gerenciamento de pagamentos através do JAX-RS
 * 
 * @author tca85
 *
 */
@Path("/pagamentos")
@Singleton
public class PagamentoResource {
	private Map<Integer, Pagamento> repositorio = new HashMap<>();
	private Integer idPagamento = 1;

	//---------------------------------------------------------------------------------------------
	/**
	 * em um projeto real, o repositório seria o banco de dados
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
}
