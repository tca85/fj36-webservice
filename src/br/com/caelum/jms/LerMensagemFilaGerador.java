package br.com.caelum.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 
 * Faz a leitura da FILA.GERADOR, que teve um novo livro inserido após
 * concretização da compra no fj36-livraria. Essa configuração foi
 * feita no método init da ConfiguracaoCamel
 * 
 * @author tca85
 *
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/FILA.GERADOR"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class LerMensagemFilaGerador implements MessageListener {

	@Override
	public void onMessage(Message msg) {

		try {
			TextMessage message = (TextMessage) msg;
			System.out.printf("Mensagens da FILA.GERADOR %s\n", message.getText());

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}