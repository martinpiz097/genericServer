/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.defaultServer.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.defaultServer.exceptions.NullListenersException;
import org.martin.defaultServer.interfaces.ClientListener;
import org.martin.defaultServer.listeners.ClientDisconnectedListener;
import org.martin.defaultServer.listeners.NewClientListener;
import org.martin.defaultServer.system.SysInfo;

/**
 *
 * @author martin
 */
public class Server {
    private final ServerSocket serverSock;
    private final List<Client> clientsConnected;
    private static Server server;
    private NewClientListener ncListener;
    private ClientDisconnectedListener cliDisListener;
    private boolean isServerOnline;
    
    static{
        try {
            server = new Server();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Server getServer(){
        return server;
    }
    
    public static void restartServer(){
        try {
            for (Client cli : server.getClientsConnected()) {
                cli.stopClient();
            }
            server.serverSock.close();
            server = null;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Server() throws IOException{
        serverSock = new ServerSocket(SysInfo.DEFAULT_PORT);
        clientsConnected = new LinkedList<>();
        isServerOnline = true;
    }
    
    public void setNcListener(NewClientListener ncListener){
        this.ncListener = ncListener;
    }
    
    public void setClientDisconnetedListener(ClientDisconnectedListener cdListener){
        this.cliDisListener = cdListener;
    }
    
    public void addClient(Client client){
        clientsConnected.add(client);
    }
    
    public List<Client> getClientsConnected(){
        return clientsConnected;
    }
    
    public void suspendServer(){
        isServerOnline = false;
    }
    
    public void resumeServer(){
        if (!isServerOnline)
            isServerOnline = !isServerOnline;
    }
    
    public void disconnectClient(Client client){
        cliDisListener.onClientDisconnected(client);
    }
    
    
    public void start() throws IOException, InterruptedException, NullListenersException {
        if (ncListener == null || cliDisListener == null){
            String msg = ncListener == null && cliDisListener == null ? 
                    "Both listeners are null!" : (ncListener == null ? "NewClientListener is null!"
                    : "ClientDisconnectedListener is null!");
            throw new NullListenersException(msg);
        }
        
        while (true) {            
            while (isServerOnline) {
                ncListener.onClientConnected(serverSock.accept());
                Thread.sleep(SysInfo.DEFAULT_SLEEP_TIME);
            }
        }
        
    }
    
    public static void main(String[] args) {
        try {
            Server server = Server.getServer();
            server.start();
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullListenersException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
