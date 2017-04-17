package com.forj.fwm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * https://github.com/steveliles/jetty-embedded-spring-mvc
 *
 */
public class ScratchPad {

	
	public static void startServer(String[] args) throws Exception {
		Server server = new Server(8080);
		
		WebAppContext ctx = new WebAppContext();
		ctx.setContextPath("/");
		ctx.setWar("src/main/webapp/");
		HandlerCollection hc = new HandlerCollection();
		hc.setHandlers(new Handler[] {ctx});
		
		server.setHandler(hc);
		server.setStopAtShutdown(true);
		server.start();
		
		server.join();
		
		// server.removeBean(o);
		// server.addBean(o);
	}
}
