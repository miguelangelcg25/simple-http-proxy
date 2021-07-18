/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leantech.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author miguelangel
 */
public class Proxy {

    private static final Logger LOG = LoggerFactory.getLogger(Proxy.class);
    private final ServerSocket server;
    private final int port;
    private boolean run = false;
    private final ExecutorService worker;

    public Proxy(int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(this.port);
        this.worker = Executors.newCachedThreadPool();
    }

    public void start() throws IOException {
        LOG.info("Starting proxy server");
        run = true;
        
        while(run) {
            worker.submit(new HttpLoggerProcessor(server.accept()));
        }
    }
    
    public void stop() {
        this.run = false;
        try {
            server.close();
        } catch (IOException ex) {
            LOG.error("Error stopping proxy server", ex);
        }
        
        LOG.info("Proxy server stopped");
    }
    
}
