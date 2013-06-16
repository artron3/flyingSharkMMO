/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.io.IOException;
import java.util.ArrayList;
import mygame.*;
import mygame.Playable.Player;
import mygame.Playable.PlayerOnLine;

/**
 *
 * @author Chedly
 */
public class ClientListener implements MessageListener<Client> {
    public ArrayList<PlayerOnLine> players;
    public ClientMain game;
    public BulletAppState bulletAppState;
    public Player plane;
    
    public ClientListener (ClientMain main, BulletAppState bulletAppState, Player pla) {
        this.game = main;
        this.bulletAppState = bulletAppState;
        this.plane = pla;
    }
    
    public void messageReceived(Client source, Message message) {
        try{
            System.out.println("----------------------------------------------------------------------");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("----------------------------------------------------------------------");

            if (message instanceof HelloMessage) {
                // do something with the message
                HelloMessage helloMessage = (HelloMessage) message;
                ArrayList<PlayerAlex> playersTemps = new ArrayList<PlayerAlex>(10);
                //playersTemps.addAll((PlayerAlex) helloMessage.getPlayers());
                System.out.println("-----------"+ helloMessage.toString()+ " ----------------");
                System.out.println("-----------"+ helloMessage.getPlayers().toString()+ " ----------------");
                System.out.println("-----------"+ helloMessage.getPlayers().get(0).toString()+ " ----------------");
                System.out.println("-----------"+ helloMessage.getPlayers().get(0).getId().toString()+ " ----------------");
                System.out.println("----------------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------------");
                
                ArrayList<PlayerAlex> pAT = helloMessage.getPlayers();
                /*while(players.size()<helloMessage.getPlayers().size()){
                    players.add(new PlayerOnLine(pAT.get(pAT.size()-), y, z, null, null, null))
                }
                for ( PlayerOnLine player  : players){
                    while(pl)
                
            }*/
               
                for(int i=0; i< pAT.size(); ++i){
                    if(i< players.size()){
                        if(plane.ID.equals(pAT.get(i).getId())){
                            plane.character.setPhysicsLocation(pAT.get(i).getPos());
                            plane.character.setViewDirection(pAT.get(i).getDirection());
                        }else{
                        players.get(i).character.setPhysicsLocation(pAT.get(i).getPos());
                        players.get(i).character.setViewDirection(pAT.get(i).getDirection());
                        }
                    }
                }
                //      System.out.println("Client #"+source.getId()+" received: '"+helloMessage.players.size() +"'");
                //System.out.println("Client #"+source.getId()+" received: '"+helloMessage.players.get(helloMessage.players.size()-1).faction +"'");
            }
        } catch (Exception ex) {
            System.out.println(getClass().getName()+"  uuuuuuuuuu   "+ ex.toString());
        }
    }
    
    public ArrayList<PlayerOnLine> upDatenetwork(ArrayList<PlayerOnLine> players){
        
        
        return players;
    }
}
