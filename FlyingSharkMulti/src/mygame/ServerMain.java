package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class ServerMain extends SimpleApplication implements ConnectionListener {

    public static void main(String[] args) {
          ServerMain app = new ServerMain();
    app.start(JmeContext.Type.Headless); // headless type for servers!
        System.out.println("Hello Server");
    }
    private Server myServer;
    private HashMap<Integer, PlayerAlex> conneInfo;
    private ArrayList<PlayerAlex> players;
    private float timeElapsed;
    int count=0;

    @Override
    public void simpleInitApp() {
        try {
            timeElapsed =0f;
            count =0;
            conneInfo = new HashMap<Integer, PlayerAlex>(4);
            players = new ArrayList<PlayerAlex>(4);
          myServer = Network.createServer(8000);
         myServer.start();
            System.out.println("started");
         myServer.addConnectionListener(this);
            System.out.println("connection listner addedd");
            Serializer.registerClass(PlayerAlex.class);
            Serializer.registerClass(HelloMessage.class);
            System.out.println("classe serialized");
         myServer.addMessageListener(new ServerListener(), HelloMessage.class);
            
        } catch (IOException ex) {
            System.out.println("PROBLEEEEEEEEEEEEEEM");
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        timeElapsed+=tpf;
        if(timeElapsed>0.48f){
            if(players != null){
                if(players.size()>0){
                Message msg = new HelloMessage(players);
                myServer.broadcast(msg);
            }
        }
            
            
            
            
            
            
            
            timeElapsed = 0;
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // MET A JOUR LA POSITION DU JOUEUR EN QUESTION !!!
        
    }

    public void connectionAdded(Server server, HostedConnection conn) {
       
       // if (!conneInfo.containsKey(conn.getId())){
        //    conneInfo.put(conn.getId(), new PlayerAlex(players, conn.getId()));
            players.add(new PlayerAlex(players, conn.getId()));
       // }
        System.out.println("connection added" + count);
        ++count;
    }

    public void connectionRemoved(Server server, HostedConnection conn) {
        
        System.out.println("connection removed");
    }
    @Override
  public void destroy() {
      
      myServer.close();
      super.destroy();
  }
}
