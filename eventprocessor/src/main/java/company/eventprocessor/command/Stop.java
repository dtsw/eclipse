/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package company.eventprocessor.command;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

import company.eventprocessor.Configuration;

/**
 *
 * @author Oliver Frick
 */
public class Stop {
	
	private static Properties props = Configuration.getInstance().getProperties();
	
    public static void main(String[] args) throws IOException {
        Socket s = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt(props.getProperty("shutdown_port")));
        OutputStream out = s.getOutputStream();
        System.out.println("*** sending Stop request");
        out.write(("quit\r\n").getBytes());
        out.flush();
        s.close();
    }
}
