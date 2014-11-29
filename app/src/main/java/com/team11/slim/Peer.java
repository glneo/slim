package com.team11.slim;

import java.io.Serializable;

/**
 * This class represents a Client that is signed into SLim.
 *
 */

public class Peer implements Serializable
{
    private static final long serialVersionUID = -4950135513048568497L;
    public String name;
    public String ipAddress;

    public Peer(String name, String ipAddress)
    {
        this.name = name;
        this.ipAddress = ipAddress;
    }
}
