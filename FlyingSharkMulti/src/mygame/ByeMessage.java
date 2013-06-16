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
public class ByeMessage extends AbstractMessage {

    private String hello;       // custom message data
    private PlayerAlex players;

    public ByeMessage() {
    }    // empty constructor

    public ByeMessage(PlayerAlex players2) {
        this.players = players2;
    }

    public PlayerAlex getPlayers() {
        return players;
    }

    public void setPlayers(PlayerAlex players) {
        this.players = players;
    }

    public void UpdtAl(PlayerAlex players2) {
        this.players = players2;
    }
}
