/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.defaultServer.exceptions;

/**
 *
 * @author martin
 */
public class NullListenersException extends Exception {

    /**
     * Creates a new instance of <code>NullListenersException</code> without
     * detail message.
     */
    public NullListenersException() {
    }

    /**
     * Constructs an instance of <code>NullListenersException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NullListenersException(String msg) {
        super(msg);
    }
}
