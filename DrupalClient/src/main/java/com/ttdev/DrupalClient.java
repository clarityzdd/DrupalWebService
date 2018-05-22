package com.ttdev;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Form;

import org.apache.cxf.jaxrs.client.WebClient;

import com.ttdev.receta.Receta;
import com.ttdev.ss.SimpleService_P1_Client;
import com.ttdev.token.Token;
import com.ttdev.usuario.Usuario;

public class DrupalClient {

	public static void main(String[] args) {
		
		WebClient client = WebClient.create("http://192.168.56.1/daw/rest");
		client.accept("application/xml");
		
		client.path("node/59");
		Receta receta = client.get(Receta.class);
		System.out.println("Titulo: " + receta.getTitle());
		
		String res = SimpleService_P1_Client.SOAPDrupal(receta.getTitle());
		
		
		client.back(true);
		client.path("user/token");
		Token token = client.type(MediaType.APPLICATION_FORM_URLENCODED).post(null,Token.class);
		//System.out.println("Token obtenido: " + token.getToken());
		
		client.back(true);
		client.path("user/login");
		client.header("x-CSRF-Token", token.getToken());
		
		Form form = new Form();	
		form.param("username", "test");	
		form.param("password", "test");
		
		Usuario usuario = client.type(MediaType.APPLICATION_FORM_URLENCODED).post(form,Usuario.class);
		
		/*
		System.out.println("sessionname: " + usuario.getSessionName());
		System.out.println("sessioid: " + usuario.getSessid());
		System.out.println("sessiotoken: " + usuario.getToken());
		*/
		
		client.back(true);
		client.path("node/59");
		
		// Cambiamos el tiempo de preparacion
		
		form = new Form();
		form.param("title","Receta editada");
		
		client.header("cookie", usuario.getSessionName()+ "=" + usuario.getSessid());
		client.replaceHeader("x-CSRF-Token", usuario.getToken());
		
		// PUT
		
		String resp = client.type(MediaType.APPLICATION_FORM_URLENCODED).put(form, String.class);
		System.out.println("Respuesta: " + resp);
		
		// Cerramos sesion
		
		client.back(true);
		client.path("/user/logout");
		client.post(null, String.class);
		
		System.out.println("Nuevo título: " + receta.getTitle());

	}

}
