/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.defaultServer.listeners;

import org.martin.defaultServer.net.Client;

/**
 *
 * @author martin
 */
public interface ClientDisconnectedListener {
    public void onClientDisconnected(Client client);
}
