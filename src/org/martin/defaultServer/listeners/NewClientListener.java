/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.defaultServer.listeners;

import java.net.Socket;

/**
 *
 * @author martin
 */


public interface NewClientListener {
    public void onClientConnected(Socket sockClient);
}
