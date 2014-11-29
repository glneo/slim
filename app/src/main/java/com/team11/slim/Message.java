package com.team11.slim;

import java.io.Serializable;
import java.util.Vector;

/**
 * This class represents a message to be sent over the wire within SLim.
 * It can be expanded to include additional data if necessary.
 *
 */
enum MessageType
{
    Text, NewUser, UpdateList
}

public class Message implements Serializable
{
    private static final long serialVersionUID = 8899974910957383782L;
    public Peer peer = null;
    public String messageText = null;
    public MessageType type = null;
    public Vector<Peer> userList = null;
    public boolean translateThis = false;

    public Message(Peer peer, MessageType type, String messageText)
    {
        this.peer = peer;
        this.type = type;
        this.messageText = messageText;
    }
}