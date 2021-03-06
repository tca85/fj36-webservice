package br.com.caelum.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Message Driven Bean
 * 
 * @author tca85
 *
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/TOPICO.LIVRARIA"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic") })
public class FinanceiroMDB implements MessageListener {

	@Override
	public void onMessage(Message msg) {
		
		try {
			TextMessage message = (TextMessage) msg;
			System.out.printf("Gerando notas para %s\n", message.getText());
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}