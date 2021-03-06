package mygame;



import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.serializers.Vector3Serializer;
import com.jme3.renderer.Camera;
import mygame.Faction;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import mygame.Playable.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Alexandre
 * Date: 04/05/13
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
@Serializable 
public class PlayerAlex extends AbstractMessage{
    private int faction;
    private Vector3f pos;
    private Vector3f direction;
    private ArrayList <Vector3f> bulletsPos;
    private ArrayList <Vector3f> bulletsDir;
    private Integer id;

    public PlayerAlex() {
    }    // empty constructor

    public PlayerAlex(ArrayList<PlayerAlex> players, Integer id) {
        this.id = id;
        bulletsDir = new ArrayList<Vector3f>(30);
        bulletsPos = new ArrayList<Vector3f>(30);
        pos = new Vector3f();
        direction = new Vector3f();

        if (null != players) {
            if (players.size() > 0) {
                if ((players.size() / 2) == 0) {
                    faction = 0;
                } else {
                    faction = 1;
                }
            } else {
                faction = 0;
            }
        }

    }

    
    
        public PlayerAlex( Integer id) {
        this.id = id;
        bulletsDir = new ArrayList<Vector3f>(30);
        bulletsPos = new ArrayList<Vector3f>(30);
        pos = new Vector3f();
        direction = new Vector3f();

                faction = 0;
   }
        
    public PlayerAlex( Camera cam, Integer playerId) {
        this.setPos(cam.getLocation());
        this.setDirection(cam.getDirection());
        bulletsDir = new ArrayList<Vector3f>(30);
        bulletsPos = new ArrayList<Vector3f>(30);
        this.id = playerId;
    }
    
    public void setCam(Camera cam, Integer playerId){
        
        this.setPos(cam.getLocation());
        this.setDirection(cam.getDirection());
        bulletsDir = new ArrayList<Vector3f>(30);
        bulletsPos = new ArrayList<Vector3f>(30);
        this.id = playerId;
    }

    
    
public void updatee (Vector3f posX, Vector3f dirX, ArrayList<Vector3f> bPos, 
            ArrayList<Vector3f> bDire){
    this.pos = posX;
    this.direction = dirX;
    
    this.bulletsDir = bDire;
    this.bulletsPos = bPos;
        
    }

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public ArrayList<Vector3f> getBulletsPos() {
        return bulletsPos;
    }

    public void setBulletsPos(ArrayList<Vector3f> bulletsPos) {
        this.bulletsPos = bulletsPos;
    }

    public ArrayList<Vector3f> getBulletsDir() {
        return bulletsDir;
    }

    public void setBulletsDir(ArrayList<Vector3f> bulletsDir) {
        this.bulletsDir = bulletsDir;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }




}
