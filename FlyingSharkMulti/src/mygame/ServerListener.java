/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
import mygame.*;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.util.ArrayList;

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
            boolean bool = false;
            int countI = 0;
            System.out.println("-------------------------");
            System.out.println("----------------"+ this.serverMain.players.size() );
            System.out.println("----------------"+ this.serverMain.players );
            if(this.serverMain.players.size()>0){
                for(PlayerAlex alix: this.serverMain.players){
                    
                    System.out.println("--premier test fait");
                    if (alix.getId().equals(helloMessage.getPlayers().get(0).getId())){
                        bool = true;
                        this.serverMain.players.get(countI).setPos(helloMessage.getPlayers().get(0).getPos());
                        this.serverMain.players.get(countI).setDirection(helloMessage.getPlayers().get(0).getDirection());
                    }
                    ++countI;
                }
            } else{
                System.out.println("-----22222222--------");
                this.serverMain.players = new ArrayList<PlayerAlex>(10);
                System.out.println(helloMessage.getPlayers());
                this.serverMain.players.add(new PlayerAlex(helloMessage.getPlayers().get(0).getId()));
                System.out.println("-------  yess  ---------");
                bool = true;
            }
            if (!bool) {
                System.out.println("-------3333333FFFF---------");
                this.serverMain.players.add(new PlayerAlex(this.serverMain.players, helloMessage.getPlayers().get(0).getId()));
                
            }
            
            System.out.println("----END  E>ND  END22--------");
        } // else....
    }
}
