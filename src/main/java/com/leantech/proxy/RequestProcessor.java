/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leantech.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author miguelangel
 */
public abstract class RequestProcessor implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RequestProcessor.class);
    private final Socket clientSocket;

    public RequestProcessor(Socket socket) {
        this.clientSocket = socket;
    }

    abstract void process(String request);

    public String extractHost(String request) {
        Scanner scanner = new Scanner(request);
        String line = "";

        while (scanner.hasNextLine() && !line.startsWith("Host:")) {
            line = scanner.nextLine();
        }

        return line.split(" ")[1];
    }

    /**
     * Sends the request to the remote server and sends the response back to the
     * client.
     */
    @Override
    public void run() {
        LOG.info("New request arrived");
        String request = null;

        try (InputStream clientReader = clientSocket.getInputStream();
                OutputStream clientWriter = clientSocket.getOutputStream()) {

            // buffer large enough to handle simple requests/responses
            byte[] buffer = new byte[4096];
            clientReader.read(buffer);

            int port = 80;
            request = new String(buffer);
            String host = extractHost(request);
            process(request);

            try (
                    Socket remoteSocket = new Socket(host, port);
                    InputStream remoteReader = remoteSocket.getInputStream();
                    OutputStream remoteWriter = remoteSocket.getOutputStream()) {

                // send request to server
                remoteWriter.write(buffer);

                buffer = new byte[1024 * 1024];
                int len;

                // read response and send to the client
                while ((len = remoteReader.read(buffer)) > 0) {
                    clientWriter.write(buffer, 0, len);
                }
            }
        } catch (IOException ex) {
            LOG.error("Error processing request: \n" + request, ex);
        }
    }

}
