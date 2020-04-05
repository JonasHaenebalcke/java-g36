package main;

import domein.GebruikerController;

public class StartUp {
public static void main(String[] args) {
	GebruikerController dc = new GebruikerController();
	
	dc.GeefGebruikersList().forEach(g -> System.out.println(g.toString()));
	
}
}
