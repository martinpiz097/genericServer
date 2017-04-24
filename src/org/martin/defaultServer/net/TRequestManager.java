/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.defaultServer.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.martin.defaultServer.interfaces.Communicable;
import org.martin.defaultServer.listeners.NewClientListener;

/**
 *
 * @author martin
 */
public class TRequestManager extends Thread implements Communicable, NewClientListener{
    private final Socket sockClient;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    public TRequestManager(Socket sockClient) throws IOException {
        this(sockClient, new ObjectOutputStream(sockClient.getOutputStream()), 
                new ObjectInputStream(sockClient.getInputStream()));
    }
    
    public TRequestManager(Socket sockClient, ObjectOutputStream output, ObjectInputStream input) {
        this.sockClient = sockClient;
        this.output = output;
        this.input = input;
    }

    @Override
    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }

    @Override
    public Object getReceivedObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }
    
    @Override
    public void onClientConnected(Socket sockClient) {

    }
    
    @Override
    public void run(){
        onClientConnected(sockClient);
    }

}
