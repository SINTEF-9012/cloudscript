package net.modelbased.cloudscript.examples.java;

import net.modelbased.cloudscript.kernel.*;

import net.modelbased.cloudscript.samples.sensapp.SensAppSystem;
import net.modelbased.cloudscript.samples.sensapp.platform.MongoDataBase;
import net.modelbased.cloudscript.samples.sensapp.platform.JettyServer;


public class App {
    public static void main( String[] args) {
        SensAppSystem application = new SensAppSystem();
        for(Component c: application.containeds()) {	
        	System.out.println(c);
        	System.out.println("  expects: " + c.expected());
        	for(Service s: c.offereds()) { System.out.println("  offers: " + s); }
        }
        
        MongoDataBase db = new MongoDataBase();
        System.out.println(db.toCommand());
        
        JettyServer srv = new JettyServer();
        System.out.println("Deployment Script: \n" + srv.deployAsBash());
        System.out.println("Startup Script: \n" + srv.startupAsBash());
        
    }
}
