package br.com.caelum.estoque.ws;

/**
 * 
 * @author tca85
 *
 */
public class ItemEstoque {
	private String codigo;
	private Integer quantidade;

	public ItemEstoque() {
		// TODO Auto-generated constructor stub
	}

	public ItemEstoque(String codigo, Integer quantidade) {
		this.codigo = codigo;
		this.quantidade = quantidade;
	}

	public String getCodigo() {
		return codigo;
	}

	public Integer getQuantidade() {
		return quantidade;
	}
}
