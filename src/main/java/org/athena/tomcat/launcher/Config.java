package org.athena.tomcat.launcher;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

public abstract class Config {
	
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	
	protected static final String CURRENT_DIR = ".";
	
	protected static String projectHome;
	
	protected static String projectName;
	
	protected static int port;
	
	protected static int shutdownPort;
	
	protected static String contextPath;
	
	protected static String webapp;
	
	static {
		try {
			initProject();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	protected static void initProject() throws Exception {
		projectHome = System.getProperty("project.home");
		if (projectHome == null){
			System.setProperty("project.home", CURRENT_DIR);
			projectHome = CURRENT_DIR;
		}
		
		projectName = System.getProperty("project.name", "WEB");
		port = Integer.parseInt(System.getProperty("start.port", "8080"));
		shutdownPort = Integer.parseInt(System.getProperty("stop.port", "8005"));
		contextPath = System.getProperty("contextPath", "/");
		webapp = System.getProperty("webapp", "webapp");
		
		logger.info("project home is {}", projectHome);
		logger.info("project name is {}", projectName);
		logger.info("start port is {}" , port);
		logger.info("shutdown port is {}", shutdownPort);
		logger.info("context path is {}", contextPath);
		logger.info("webapp is {}", webapp);
		
		
		File logbackFile = new File(projectHome + "/conf/logback.xml");
		
		if (logbackFile.exists()){
			System.setProperty("logback.configurationFile", logbackFile.getAbsolutePath());
			LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
	        JoranConfigurator configurator = new JoranConfigurator();
	        configurator.setContext(lc);
	        lc.reset();
	        configurator.doConfigure(logbackFile.getAbsolutePath());
		}else {
			System.err.println("Can not found logback configuration in the conf directory");
			throw new Exception("Can not found logback configuration in the conf directory");
		}
		
	}

}
