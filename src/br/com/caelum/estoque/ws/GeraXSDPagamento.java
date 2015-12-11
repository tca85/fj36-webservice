package br.com.caelum.estoque.ws;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import br.com.caelum.payfast.modelo.Pagamento;

/**
 * 
 * @author tca85
 *
 */
public class GeraXSDPagamento {

	public static void main(String[] args) {

		try {
			JAXBContext context = JAXBContext.newInstance(Pagamento.class);

			context.generateSchema(new SchemaOutputResolver() {

				@Override
				public Result createOutput(String namespaceUri,
						String suggestedFileName) throws IOException {
					return new StreamResult(new File("pagamento.xsd"));
				}
			});
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
	}
}