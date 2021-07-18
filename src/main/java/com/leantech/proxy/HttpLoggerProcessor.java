/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leantech.proxy;

import java.net.Socket;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author miguelangel
 */
public class HttpLoggerProcessor extends RequestProcessor {
    
    private static final Logger LOG = LoggerFactory.getLogger(HttpLoggerProcessor.class);

    public HttpLoggerProcessor(Socket socket) {
        super(socket);
    }

    @Override
    void process(String request) {
        Scanner scanner = new Scanner(request);
        scanner.nextLine();
        
        LOG.info("Request Headers:");
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            
            if(line.isEmpty()) {
                break;
            }
            LOG.info(line);
        }
    }
    
}
