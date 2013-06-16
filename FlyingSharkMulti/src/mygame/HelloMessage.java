/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
import mygame.*;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Chedly
 */

@Serializable
public class HelloMessage extends AbstractMessage {
   
  private ArrayList<PlayerAlex> players;
  
  public HelloMessage() {}    // empty constructor

    public HelloMessage(ArrayList<PlayerAlex> players2) {
        this.players = players2;
    }

    public HelloMessage(PlayerAlex players2) {
        this.players = new ArrayList<PlayerAlex>(1);
        this.players.add(players2);
    }

    public ArrayList<PlayerAlex> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerAlex> players) {
        this.players = players;
    }
    public void UpdtAl(ArrayList<PlayerAlex> players2){
        this.players = players2;
    }

}
