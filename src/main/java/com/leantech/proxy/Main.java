/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leantech.proxy;

import java.io.IOException;

/**
 *
 * @author miguelangel
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Proxy proxy = new Proxy(8080);
        proxy.start();
    }

}
