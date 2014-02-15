package org.athena.tomcat.launcher;

import java.io.File;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatStarter extends Config{
	
	private static Logger logger = LoggerFactory.getLogger(TomcatStarter.class);
	
	private static Tomcat tomcat;
	
	public void start() throws Exception {
		File webAppDirectory = new File(webapp);
		
		tomcat = new Tomcat();
		tomcat.setBaseDir(projectHome);
		tomcat.setPort(port);
		tomcat.addWebapp(contextPath, webAppDirectory.getAbsolutePath());
		tomcat.getServer().setPort(shutdownPort);
		
		
		logger.info("start " + projectName + "...");
		tomcat.start();
		logger.info(projectName + " started ...");
		tomcat.getServer().await();
		registryShutdownListener();
		
	}
	
	private void registryShutdownListener() {
		Runtime.getRuntime().addShutdownHook(new TomcatShutdownListener());
	}
	
	static class TomcatShutdownListener extends Thread {
		
		@Override
		public void run() {
			if (tomcat != null) {
				try {
					tomcat.stop();
				} catch (LifecycleException e) {
					logger.error("stop tomcat error: ", e);
					return;
				}
				logger.info("tomcat server stopped.");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		TomcatStarter starter = new TomcatStarter();
		starter.start();
	}

}
