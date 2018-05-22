package com.ttdev;

import com.sun.tools.xjc.XJCFacade;

public class CodeGenerator {

	public static void main(String[] args) throws Throwable {
		XJCFacade.main(new String [] {
			"-b","src/main/resources/usuario-binding.xml",
			"-p","com.ttdev.usuario",
			"-d","src/main/java",
			"src/main/resources/usuario.xsd"
		});
		System.out.println("Hecho");
	}

}
