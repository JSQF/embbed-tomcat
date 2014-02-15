package org.athena.tomcat.launcher;

import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatStoper extends Config{
	
	private static Logger logger = LoggerFactory.getLogger(TomcatStoper.class);
	
	private static final int DEFAULT_SO_TIMEOUT = 30000;

	public static void main(String[] args) throws Exception {
		
		if (shutdownPort <= 0){
			logger.error("shutdown port cannot be less than zero");
			System.exit(-1);
		}
		
		Socket socket = null;
		OutputStream os = null;
		
		try{
			logger.info("send SHUTDOWN signal");
			socket = new Socket("localhost", shutdownPort);
			socket.setSoTimeout(DEFAULT_SO_TIMEOUT);
			os = socket.getOutputStream();
			os.write("SHUTDOWN".getBytes());
			os.flush();
		}catch(Exception e) {
			logger.error("send SHUTDOWN signal error", e);
			System.exit(-1);
		}finally {
			if (os != null){
				os.close();
			}
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
		logger.info("shutdown tomcat completed");
	}
}
