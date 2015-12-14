package br.com.caelum.jms;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;

/**
 * Envio via JMS dentro do servidor
 * 
 * --- deveria estar dentro do fj36-estoque
 * 
 * @author tca85
 *
 */
public class EnviarMensagem {
	 // contexto é código de infra, por isso deve receber injetado
	 @Inject
	 JMSContext ctx;

	  // Destination é o genérico de topico e fila
	 @Resource(lookup="java:jboss/exported/jms/TOPICO.LIVRARIA")
	 javax.jms.Destination topico;
	 
	 //--------------------------------------------------------------------------------------------
	 /**
	  * Contexto deve enviar para dentro do tópico
	  * @param msg
	  */
	 public void enviar(String msg){
		 ctx.createProducer().send(topico, msg);
	 }
	//--------------------------------------------------------------------------------------------
}