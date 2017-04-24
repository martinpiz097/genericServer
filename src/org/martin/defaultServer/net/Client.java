/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.defaultServer.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.defaultServer.interfaces.ClientListener;
import org.martin.defaultServer.interfaces.Communicable;
import org.martin.defaultServer.system.SysInfo;

/**
 *
 * @author martin
 */
public class Client extends Thread implements Communicable{
    private final Socket sockClient;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private ClientListener ownListener;
    private boolean isRunning;
    private List listCliObjects; // Lista para manejar objetos como un objeto
                                // usuario o lo que el programador necesite.
    
    public Client(Socket sockClient) throws IOException {
        this(sockClient, new ObjectOutputStream(sockClient.getOutputStream()), 
                new ObjectInputStream(sockClient.getInputStream()));
    }
    
    public Client(Socket sockClient, ObjectOutputStream output, ObjectInputStream input) {
        this.sockClient = sockClient;
        this.output = output;
        this.input = input;
        isRunning = true;
        listCliObjects = new LinkedList();
        setName("Client "+getId());
    }
    
//    public void stopClient(){
//        isRunning = false;
//    }
    
    public void addObjectToList(Object obj){
        listCliObjects.add(obj);
    }

    public ClientListener getOwnListener() {
        return ownListener;
    }

    public void setOwnListener(ClientListener ownListener) {
        this.ownListener = ownListener;
    }
    
    public List getListCliObjects() {
        return listCliObjects;
    }

    public void setListCliObjects(List listCliObjects) {
        this.listCliObjects = listCliObjects;
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
    public void run() {
        ownListener.onClientInExecution();
    }

}
