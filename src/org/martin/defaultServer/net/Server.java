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
import org.martin.defaultServer.listeners.ClientDisconnectedListener;
import org.martin.defaultServer.listeners.NewClientListener;
import org.martin.defaultServer.system.SysInfo;

/**
 *
 * @author martin
 */
public class Server extends Thread{
    private final ServerSocket serverSock;
    private final List<Client> clientsConnected;
    private static Server server;
    private NewClientListener ncListener;
    private ClientDisconnectedListener cliDisListener;
    private boolean isServerOnline;
    
    /*static{
        try {
            server = new Server();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    public static void newServer() throws IOException{
        server = new Server();
    }
    
    public static void newServer(int port) throws IOException{
        server = new Server(port);
    }
    
    public static Server getServer(){
        return server;
    }
    
    
    public static void restartServer(){
        try {
            for (Client cli : server.getClientsConnected())
                server.cliDisListener.onClientDisconnected(cli);

            server.serverSock.close();
            server.stop();
            int port = server.serverSock.getLocalPort();
            server = new Server(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Server() throws IOException{
        this(SysInfo.DEFAULT_PORT);
    }
        
    private Server(int port) throws IOException{
        serverSock = new ServerSocket(port);
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
    
    public void run() {
        if (ncListener == null || cliDisListener == null){
            try {
                String msg = ncListener == null && cliDisListener == null ?
                        "Both listeners are null!" : (ncListener == null ? "NewClientListener is null!"
                        : "ClientDisconnectedListener is null!");
                throw new NullListenersException(msg);
            } catch (NullListenersException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Se usan dos while para que al momento de suspenderse el servidor se siga teniendo en ejecucion
        // pero sin interactuar con la red.
        System.out.println("Server started!");
        while (true) {
            try {
                while (isServerOnline) {
                    System.out.println("Waiting for clients.....");
                    ncListener.onClientConnected(serverSock.accept());
                    System.out.println("Client connected");
                }
                Thread.sleep(100);
                System.out.println("Server suspended");
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
//    
//    public static void main(String[] args) {
//        try {
//            Server.newServer();
//            Server server = Server.getServer();
//            server.setNcListener(new NewClientListener() {
//                @Override
//                public void onClientConnected(Socket sockClient) {
//                    System.out.println("se conecto un cliente");
//                }
//            });
//            server.setClientDisconnetedListener(new ClientDisconnectedListener() {
//                @Override
//                public void onClientDisconnected(Client client) {
//                    System.out.println("Cliente "+client.getId()+" desconectado");
//                }
//            });
//            server.start();
//            Socket sock1 = new Socket("localhost", SysInfo.DEFAULT_PORT);
//            Socket sock2 = new Socket("localhost", SysInfo.DEFAULT_PORT);
//            server.suspendServer();
//            Server.restartServer();
//            
//        } catch (IOException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
