package br.com.caelum.estoque.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Web Service
 * 
 * 
 * http://localhost:8080/fj36-webservice/EstoqueWS?wsdl
 * 
 * Forma de versionar:
 * http://soapatterns.org/design_patterns/canonical_versioning
 * 
 * @author tca85
 *
 */
@WebService(targetNamespace="http://caelum.com.br/estoquews/v1")
@Stateless
public class EstoqueWS {
	private Map<String, ItemEstoque> repositorio = new HashMap<>();
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Cria uma quantidade fictícia para cada um dos códigos existentes
	 */
	public EstoqueWS() {
		repositorio.put("SOA", new ItemEstoque("SOA", 5));
		repositorio.put("TDD", new ItemEstoque("TDD", 1));
		repositorio.put("RES", new ItemEstoque("RES", 2));
		repositorio.put("LOG", new ItemEstoque("LOG", 4));
		repositorio.put("WEB", new ItemEstoque("WEB", 1));
		repositorio.put("ARQ", new ItemEstoque("ARQ", 2));
	}
	
	//---------------------------------------------------------------------------------------------

	/**
	 * SOA Fallacies - o correto é receber uma lista e retornar uma lista
	 * sempre limitada em uma quantidade específica. Isso se chama granularidade do serviço
	 * 
	 * Contract First - pensa primeiro no contrato (wsdl) ao invés do serviço
	 * "TDD" do JAX-WS, senão os parâmetros serão disponibilizados como arg0... entre outras coisas
	 * 
	 * @WebMethod e @WebParam mudam a mensagem SOAP de ida
	 * @WebResult muda a mensagem de retorno
	 * 
	 * Para evitar o envio do token para cada requisição, é necessário implementar
	 * um handler (pesquisar sobre isso), ou utilizar a extensão WS-Security
	 * do CXF ou AXIS
	 * 
	 * A chamada desse Web Service está dentro da classe Carrinho do fj36-livraria
	 * 
	 * @param codigos
	 * @param token
	 * @return
	 */
	@WebMethod(operationName="ItensPeloCodigo")
	@WebResult(name="ItemEstoque")
	public List<ItemEstoque> getQuantidade(@WebParam(name="codigo") List<String> codigos, 
			                               @WebParam(name="tokenUsuario", header=true) String token){
		
		if (token == null || !token.equals("TOKEN123")) {
			throw new AutorizacaoException("Não autorizado");
		}
		
		 List<ItemEstoque> itens = new ArrayList<ItemEstoque>();
		 
		 if (codigos == null || codigos.isEmpty()) {
			return itens;
		}
		 
		 for (String codigo : codigos) {
			if (repositorio.containsKey(codigo)) {
				itens.add(repositorio.get(codigo));
			}
		}
		 
		 return itens;
	}
	//---------------------------------------------------------------------------------------------
	
	/**
	 * Por padrão, todos métodos são publicados no Web Service, mas podemos excluir qualquer um
	 * deles através da anotação
	 */
	@WebMethod(exclude=true)
	public void metodoNaoDisponibilizadoWS(){
		System.out.println("método não disponibilizado");
	}
	
	//---------------------------------------------------------------------------------------------
}
