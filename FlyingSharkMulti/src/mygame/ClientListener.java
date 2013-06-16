/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.io.IOException;
import mygame.*;

/**
 *
 * @author Chedly
 */
public class ClientListener implements MessageListener<Client> {
    public void messageReceived(Client source, Message message) {
        try{
            if (message instanceof HelloMessage) {
                // do something with the message
                HelloMessage helloMessage = (HelloMessage) message;
                System.out.println("----------------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------------");
                System.out.println("-----------"+ helloMessage.toString()+ " ----------------");
                System.out.println("-----------"+ helloMessage.players.toString()+ " ----------------");
                System.out.println("-----------"+ helloMessage.players.get(0).toString()+ " ----------------");
                System.out.println("-----------"+ helloMessage.players.get(0).id.toString()+ " ----------------");
                System.out.println("----------------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------------");
                
                //      System.out.println("Client #"+source.getId()+" received: '"+helloMessage.players.size() +"'");
                System.out.println("Client #"+source.getId()+" received: '"+helloMessage.players.get(helloMessage.players.size()-1).faction +"'");
            }
        } catch (Exception ex) {
            System.out.println(getClass().getName()+"  uuuuuuuuuu   "+ ex.toString());
        }
    }
}
