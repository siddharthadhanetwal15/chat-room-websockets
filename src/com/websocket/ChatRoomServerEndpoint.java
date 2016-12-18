package com.websocket;

import sun.org.mozilla.javascript.internal.json.JsonParser;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.json.*;

import static java.lang.System.out;

/**
 * Created by siddhartha.dhanetwal on 12/18/2016.
 */
@ServerEndpoint("/chatroomServerEndpoint")
public class ChatRoomServerEndpoint {
    static Set<Session> chatRoomUsers= Collections.synchronizedSet(new HashSet<Session>());
    @OnOpen
    public void handleOpen(Session userSession)
    {
        chatRoomUsers.add(userSession);
        out.println("Client is now Connected");
    }
    @OnMessage
    public String handleMessage(String message, Session userSession) throws IOException {
        String uname=(String)userSession.getUserProperties().get("username");
        if(uname==null)
        {
            userSession.getUserProperties().put("username",message);
            userSession.getBasicRemote().sendText(buildJSONData("system" , "you are now connected as"+message));
        }
        else{
            Iterator<Session> iterator=chatRoomUsers.iterator();
            while (iterator.hasNext())
            {
                iterator.next().getBasicRemote().sendText(buildJSONData(uname,message));
            }
        }
        out.println("received message from client="+message);
        String replyMsg="echo "+message;
        out.println("send to client="+message);
        return replyMsg;
    }
    @OnClose
    public void handleClose(Session userSession)
    {
        chatRoomUsers.remove(userSession);
        out.println("Client is now disconnected");
    }
    private String buildJSONData(String uname, String message){
        JsonObject jsonObject = Json.createObjectBuilder().add("message",uname+":"+message).build();
        StringWriter sw=new StringWriter();
        try(JsonWriter jw=Json.createWriter(sw)){
            jw.write(jsonObject);
        }
        return sw.toString();
    }
}
