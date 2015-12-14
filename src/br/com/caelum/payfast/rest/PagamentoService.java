package br.com.caelum.payfast.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Essa classe serve só para configurar a path (caminho raíz)
 * da aplicação. É necessária para inicializar o JAX-RS
 * 
 * @author tca85
 *
 */
@ApplicationPath("/")
public class PagamentoService extends Application { }