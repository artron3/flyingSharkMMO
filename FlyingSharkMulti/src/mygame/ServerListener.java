/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
import mygame.*;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author Chedly
 */
public class ServerListener implements MessageListener<HostedConnection> {
    ServerMain serverMain;
    
    public ServerListener(ServerMain servermain){
        this.serverMain = servermain;
    }
  public void messageReceived(HostedConnection source, Message message) {
    if (message instanceof HelloMessage) {
      // do something with the message
      HelloMessage helloMessage = (HelloMessage) message;
    //  System.out.println("Server received '" +helloMessage.getSomething().getName() +"' from client #"+source.getId() );
      this.serverMain.players = helloMessage.getPlayers();
    } // else....
  }
}
