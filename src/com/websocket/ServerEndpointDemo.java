package com.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import static java.lang.System.out;

/**
 * Created by siddhartha.dhanetwal on 12/18/2016.
 */
@ServerEndpoint("/serverendpointdemo")
public class ServerEndpointDemo {
    @OnOpen
    public void handleOpen()
    {
        out.println("Client is now Connected");
    }
    @OnMessage
    public String handleMessage(String message)
    {
        out.println("received message from client="+message);
        String replyMsg="echo "+message;
        out.println("send to client="+message);
        return replyMsg;
    }
    @OnClose
    public void handleClose()
    {
        out.println("Client is now disconnected");
    }
    @OnError
    public void handleError(Throwable t)
    {
        t.printStackTrace();
    }
}
