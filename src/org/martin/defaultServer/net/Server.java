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
import org.martin.defaultServer.system.ServerMessages;
import org.martin.defaultServer.system.SysInfo;

/**
 *
 * @author martin
 */
public class Server extends Thread{
    private final ServerSocket serverSock;
    private final List<Client> clientsConnected;
    
    private NewClientListener ncListener;
    private ClientDisconnectedListener cliDisListener;
    
    private final Clock clock;
    private boolean isServerOnline;
    private boolean clockEnable;
    
    private static Server server;
    
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
        clockEnable = true;
        clock = Clock.getInstance();
    }
    
    public void setNcListener(NewClientListener ncListener){
        this.ncListener = ncListener;
    }
    
    public void setClientDisconnetedListener(ClientDisconnectedListener cdListener){
        this.cliDisListener = cdListener;
    }
    
    public void addClient(Client client){
        if (client.getState() != State.RUNNABLE)
            client.start();
        clientsConnected.add(client);
    }
    
    public void addClient(Socket sockClient) throws IOException{
        addClient(new Client(sockClient));
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
    
    public void enableClock(boolean enable){
        clockEnable = enable;
    }
    
    public void disconnectClient(Client client){
        cliDisListener.onClientDisconnected(client);
    }
    
    public void showServerMessage(String msg){
        System.out.println(clockEnable ? clock.getCurrentTime()+msg : msg);
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
        showServerMessage(ServerMessages.STARTED);
        while (true) {
            try {
                while (isServerOnline) {
                    showServerMessage(ServerMessages.WAITING_CLIENTS);
                    ncListener.onClientConnected(serverSock.accept());
                    showServerMessage(ServerMessages.CLIENT_CONNECTED);
                }
                Thread.sleep(100);
                showServerMessage(ServerMessages.SUSPENDED);
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
      
//    public static void main(String[] args) throws InterruptedException {
//        try {
//            Server.newServer();
//            Server server = Server.getServer();
//            server.setNcListener(new NewClientListener() {
//                @Override
//                public void onClientConnected(Socket sockClient) {
//                    try {
//                        new ObjectOutputStream(sockClient.getOutputStream())
//                                .writeObject("xd");
//                    } catch (IOException ex) {
//                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//                    }
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
//            Client client = new Client(new Socket("localhost", SysInfo.DEFAULT_PORT));
//            client.setOwnListener(new ClientListener() {
//                @Override
//                public void onClientInExecution() {
//                    try {
//                        System.out.println(client.getReceivedObject());
//                    } catch (IOException ex) {
//                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            });
//            client.start();
//            server.suspendServer();
//            Server.restartServer();
//            
//        } catch (IOException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
