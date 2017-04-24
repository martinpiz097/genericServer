/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.defaultServer.interfaces;

import java.net.Socket;
import java.util.EventObject;

/**
 *
 * @author martin
 */
public class ClientConnectedEvent extends EventObject{
    
    private Socket sockClient;
    
    public ClientConnectedEvent(Object source) {
        super(source);
        sockClient = (Socket) source;
        listenClient();
    }

    public Socket getSource(){
        return sockClient;
    }
    
    public void listenClient(){
        new Thread(() -> {
            
        }).start();
    }
    
}
