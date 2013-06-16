/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class Faction {
    private String name = "";
    private List<PlayerAlex> playerList;
    private List<Integer> planeList;

    public Faction(String name) {
        this.name = name;
        this.playerList = new ArrayList<PlayerAlex>();
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return getName();
    }
    
    public void setName() {
        this.name = name;
    }
    
    public List<Integer> getPlaneList() {
        return planeList;
    }

    public void addPlayer(Integer plane) {
        planeList.add(plane);
    }

    public List<PlayerAlex> getPlayerList() {
        return playerList;
    }

    public void addPlayer(PlayerAlex player) {
        playerList.add(player);
    }

}
