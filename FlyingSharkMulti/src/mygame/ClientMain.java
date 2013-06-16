/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.GUI.GUI;
import mygame.Playable.BombControl;
import mygame.Playable.Player;
import mygame.Playable.PlayerOnLine;
import mygame.SceneElem.Element.Base;
import mygame.SceneElem.Element.Oil;
import mygame.SceneElem.Element.OilCollision;
import mygame.SceneElem.World;

/**
 *
 * @author Chedly
 */
public class ClientMain extends SimpleApplication implements ClientStateListener,
    ActionListener, PhysicsCollisionListener {

    public static void main(String[] args) {
    ClientMain app = new ClientMain();
    app.start(JmeContext.Type.Display); // standard display type
  }
    private Client myClient;

    private Player player;
    private PlayerOnLine enemy;
    private World world;
    private Base baseAly;
    private Base baseEnemy;
    private ArrayList<Oil> oils = new ArrayList<Oil>(10);
    private Oil oil;
    private BulletAppState bulletAppState;
    ChaseCamera chaseCam;
    //bullet
    Sphere bullet;
    SphereCollisionShape bulletCollisionShape;
    //explosion
    FilterPostProcessor fpp;
    Material matBullet;
    //INTERFACE 2D ON SCREEN
    GUI guiViseur;
    GUI guiOil;
    GUI guiOilStocked;
    Integer lastValueOilStocked = 0;
    ArrayList<GUI> oilArray = new ArrayList<GUI>(10);
    BitmapText hudText; // display speed
    ArrayList<GUI> guiRadarsPlayersPos;
    private ArrayList<PlayerOnLine> enemys = new ArrayList<PlayerOnLine>(30);
    float time = 0;
    int X = 0;
    int radarPosX;
    int radarPosY;
    float w;
    
    @Override
    public void simpleInitApp() {
        try {
            myClient = Network.connectToServer("localhost", 8000);
            myClient.start();
            myClient.addClientStateListener(this);
            Serializer.registerClass(HelloMessage.class);
            myClient.addMessageListener(new ClientListener(), HelloMessage.class);
                
            //Message message = new HelloMessage("Hello World!");
            //myClient.send(message);
            
        
            bulletAppState = new BulletAppState();
            bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
            stateManager.attach(bulletAppState);
            w = 0;

            setupKeys();
            prepareBullet();
            world = new World(assetManager, bulletAppState, rootNode, getCamera(), viewPort);
            baseAly = new Base(-100, 20, -850, assetManager, bulletAppState, rootNode);
            baseEnemy = new Base(-100, 20, 1200, assetManager, bulletAppState, rootNode);

            Oil oil = new Oil(-170.0f, 17.1f, 28.0f, rootNode, bulletAppState, getAssetManager());

            player = new Player(assetManager, bulletAppState, rootNode, 0);

            enemy = new PlayerOnLine(-140, 15, 510, assetManager, bulletAppState, rootNode);
            enemys.add(enemy);
            enemy = new PlayerOnLine(-180, 15, 410, assetManager, bulletAppState, rootNode);
            enemys.add(enemy);
            enemy = new PlayerOnLine(-140, 15, 410, assetManager, bulletAppState, rootNode);
            enemys.add(enemy);
            enemy = new PlayerOnLine(-100, 15, 620, assetManager, bulletAppState, rootNode);
            enemys.add(enemy);
            enemy = new PlayerOnLine(-160, 15, 380, assetManager, bulletAppState, rootNode);
            enemys.add(enemy);
            enemy = new PlayerOnLine(-185, 15, 550, assetManager, bulletAppState, rootNode);
            enemys.add(enemy);
            enemy = new PlayerOnLine(-170, -135, 10, assetManager, bulletAppState, rootNode);
            enemys.add(enemy);

            setupChaseCamera();
            guiViseur = new GUI("Interface/viseur.png",
                    4 * settings.getWidth() / 9, 7 * settings.getHeight() / 19, 1 * settings.getWidth() / 9,
                    2 * settings.getHeight() / 9,
                    assetManager, settings, guiNode);
            for (int i = 1; i <= 10; ++i) {
                guiOilStocked = new GUI("Interface/oil_" + i + ".png",
                        1 * settings.getWidth() / 12, 1 * settings.getHeight() / 12, 1 * settings.getWidth() / 9,
                        3 * settings.getHeight() / 9,
                        assetManager, settings, guiNode);
                guiOilStocked.pic.setPosition(-200f, -200f);
                oilArray.add(guiOilStocked);
            }
            guiOil = new GUI("Interface/oilGround.png",
                    1 * settings.getWidth() / 12, 1 * settings.getHeight() / 12, 1 * settings.getWidth() / 9, 3 * settings.getHeight() / 9,
                    assetManager, settings, guiNode);

            radarPosX = settings.getWidth() - 110;
            radarPosY = 100;
            GUI guiRadar = new GUI("Interface/radar.png",
                    settings.getWidth() - 210, 10, 200, 200,
                    assetManager, settings, guiNode);


            guiRadarsPlayersPos = new ArrayList<GUI>(20);
            guiRadar = new GUI("Interface/radarAerodromAly.png",
                    settings.getWidth() - 105, 80, 30, 30,
                    assetManager, settings, guiNode);
            guiRadarsPlayersPos.add(guiRadar);
            guiRadar = new GUI("Interface/radarAerodromEnemy.png",
                    settings.getWidth() - 105, 80, 30, 30,
                    assetManager, settings, guiNode);
            guiRadarsPlayersPos.add(guiRadar);


            for (int i = 0; i < (enemys.size() / 2); ++i) {
                guiRadar = new GUI("Interface/radarEnemy.png",
                        settings.getWidth() - 105, 80, 7, 7,
                        assetManager, settings, guiNode);
                guiRadarsPlayersPos.add(guiRadar);
            }
            for (int i = (enemys.size() / 2); i < (enemys.size()); ++i) {
                guiRadar = new GUI("Interface/radarAly.png",
                        settings.getWidth() - 105, 80, 7, 7,
                        assetManager, settings, guiNode);
                guiRadarsPlayersPos.add(guiRadar);
            }
            guiOil = new GUI("Interface/oilGround.png",
                    1 * settings.getWidth() / 12, 1 * settings.getHeight() / 12, 1 * settings.getWidth() / 9, 3 * settings.getHeight() / 9,
                    assetManager, settings, guiNode);


            hudText = new BitmapText(guiFont, false);
            hudText.setSize(51f);      // font size
            hudText.setColor(ColorRGBA.Blue);                             // font color
            hudText.setText("0 KM");             // the text
            hudText.setLocalTranslation(settings.getWidth() - 169, 278, 0); // position
            guiNode.attachChild(hudText);

            guiRadar = new GUI("Interface/radarPlayer.png",
                    radarPosX - 11, radarPosY - 11, 20, 20,
                    assetManager, settings, guiNode);
            guiRadarsPlayersPos.add(guiRadar);
            guiRadar = new GUI("Interface/radarLook.png",
                    radarPosX - 11, radarPosY - 11, 8, 8,
                    assetManager, settings, guiNode);
            guiRadarsPlayersPos.add(guiRadar);

            GUI guiLife;
            guiLife = new GUI("Interface/LifeCurrent.png",
                    30, settings.getHeight() - 80, 105, 55, assetManager, settings, guiNode);
            guiRadarsPlayersPos.add(guiLife);
            guiLife = new GUI("Interface/LifeBackground.png",
                    30, settings.getHeight() - 80, 105, 55, assetManager, settings, guiNode);

        } catch (IOException ex) {
            System.out.println("---------00000000-------------");
            System.out.println("-------------------------------");
            System.out.println("--------ERROR CLIEN%T---------");
            System.out.println("--------ERROR CLIEN%T---------");
            System.out.println("-------------------------------");
            System.out.println("-------------------------------");
            Logger.getLogger("  ::::::  "+ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        try {
            myClient = Network.connectToServer("localhost", 8000);
       
        myClient.start();
        myClient.addClientStateListener(this);
        Serializer.registerClass(HelloMessage.class);
        myClient.addMessageListener(new ClientListener(), HelloMessage.class);

//        Message message = new HelloMessage("Hello World!");
  //      myClient.send(message);
        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("---------   "+w+"  ------------");
            System.out.println("-------------------------------");
            System.out.println("--------ERROR CLIEN%T---------");
            System.out.println("--------ERROR CLIEN%T---------");
            System.out.println("-------------------------------");
        }
        
        if (null != cam && player.game) {
            player.update(tpf, cam);
        }

        Integer oilStocked = Math.round(player.fuelStocked / (player.MAXFUEL / 10));
        if (lastValueOilStocked != oilStocked) {
            if (oilStocked != 10) {
                oilArray.get(oilStocked).pic.setPosition(-200f, -200f);
            }
            oilArray.get(oilStocked - 1).pic.setPosition(1 * settings.getWidth() / 12, 1 * settings.getHeight() / 12);
        }
        if (oilStocked.equals(0)) {
            oilArray.get(0).pic.setPosition(-200f, -200f);
        }
        hudText.setText(Math.round(player.currentSpeed * 51) + " KM");

        time += tpf;
        if (time > .3f) {
            X += time * 60;
            time = 0;
            // if(Math.random() < 0.3)
            oil = new Oil(-170 + X, 25, 28, rootNode, bulletAppState, getAssetManager());
        }
        enemys.get(1).update(tpf, rootNode, bulletAppState);
        enemys.get(2).update(tpf, rootNode, bulletAppState);
        enemys.get(3).update(tpf, rootNode, bulletAppState);
        enemys.get(4).update(tpf, rootNode, bulletAppState);
        enemys.get(5).update(tpf, rootNode, bulletAppState);
        enemys.get(6).update(tpf, rootNode, bulletAppState);
        enemys.get(0).update(tpf, rootNode, bulletAppState);
        // radar

        guiRadarsPlayersPos.get(0).pic.setPosition((baseAly.oilCollision2.getPhysicsLocation().x - player.character.getPhysicsLocation().x) / 800 * 100 + radarPosX,
                (baseAly.oilCollision2.getPhysicsLocation().z - player.character.getPhysicsLocation().z) / 800 * -100 + radarPosY);
        guiRadarsPlayersPos.get(1).pic.setPosition((baseEnemy.oilCollision2.getPhysicsLocation().x - player.character.getPhysicsLocation().x) / 800 * 100 + radarPosX,
                (baseEnemy.oilCollision2.getPhysicsLocation().z - player.character.getPhysicsLocation().z) / 800 * -100 + radarPosY);

        w += 0.1f;
        // while ((float)Math.abs(guiRadarsPlayersPos.get(enemys.size() + 2).pic.getLocalRotation().getZ() - (cam.getRotation()).getZ()) < 0.1f  ) {
        System.out.println(Math.abs(guiRadarsPlayersPos.get(enemys.size() + 2).pic.getLocalRotation().getZ() - (cam.getRotation()).getZ()));
        if (guiRadarsPlayersPos.get(enemys.size() + 2).pic.getLocalRotation().getZ() > cam.getRotation().getZ()) {
            guiRadarsPlayersPos.get(enemys.size() + 2).pic.setLocalRotation(new Quaternion(new float[]{0f, 0f, (guiRadarsPlayersPos.get(enemys.size() + 2).pic.getLocalRotation().getZ() - (cam.getRotation()).getZ()) * FastMath.PI - 1 * (float) Math.cos(cam.getDirection().getZ() * FastMath.TWO_PI) + (float) Math.cos(cam.getDirection().getX() * FastMath.TWO_PI)}));
        } else {
            guiRadarsPlayersPos.get(enemys.size() + 2).pic.setLocalRotation(new Quaternion(new float[]{0f, 0f, -(guiRadarsPlayersPos.get(enemys.size() + 2).pic.getLocalRotation().getZ() - (cam.getRotation()).getZ()) * FastMath.PI - 1 * (float) Math.cos(cam.getDirection().getZ() * FastMath.TWO_PI) + (float) Math.cos(cam.getDirection().getX() * FastMath.TWO_PI)}));
        }
        w += 0.1f;



        //guiRadarsPlayersPos.get(enemys.size()+2).pic.setLocalRotation(new Quaternion(new float[]{0f, 0f, w}));//-1*(float) Math.cos( cam.getDirection().getZ() *  FastMath.TWO_PI)  + (float) Math.cos(cam.getDirection().getX()  * FastMath.TWO_PI)} ));

        guiRadarsPlayersPos.get(enemys.size() + 3).pic.setPosition(radarPosX + (float) Math.cos(cam.getRotation().getX() * (float) Math.PI * 2),
                radarPosY + (float) Math.cos(cam.getRotation().getZ() * (float) Math.PI * 2));

        for (int i = 2; i < enemys.size() + 2; ++i) {
            if (enemys.get(i - 2).character.life > 0) {
                guiRadarsPlayersPos.get(i).pic.setPosition((enemys.get(i - 2).character.getPhysicsLocation().x - player.character.getPhysicsLocation().x) / 800 * 100 + radarPosX,
                        (enemys.get(i - 2).character.getPhysicsLocation().z - player.character.getPhysicsLocation().z) / 800 * -100 + radarPosY);
            } else {
                guiRadarsPlayersPos.get(i).pic.setPosition(-300f, -300f);
            }
        }
        guiRadarsPlayersPos.get(enemys.size() + 4).pic.setWidth(player.character.life * 1.05f);

        baseAly.oilCollision2.setPhysicsLocation(new Vector3f(-100, 20, -850));
        baseEnemy.oilCollision2.setPhysicsLocation(new Vector3f(-100, 20, 1200));
        baseAly.oilCollision2.clearForces();
        baseEnemy.oilCollision2.clearForces();
        System.out.println(baseEnemy);
        System.out.println(baseAly);
        System.out.println(baseAly.oilCollision2.getLinearVelocity());
        System.out.println(baseAly.oilCollision2.getObjectId());
    }
    public void clientConnected(Client c) {

        
    }

    public void clientDisconnected(Client c, DisconnectInfo info) {
       
        System.out.println("CLIENT Disconnected");
        System.out.println("CLIENT Disconnected");
        System.out.println("CLIENT Disconnected");
        System.out.println("CLIENT Disconnected");
        System.out.println("CLIENT Disconnected");
    }
      @Override
  public void destroy() {
      
          System.out.println("-------------------------------");
          System.out.println("-------------------------------");
          System.out.println("--------ERROR CLIEN%T---------");
          System.out.println("--------ERROR CLIEN%T---------");
          System.out.println("-------------------------------");
          System.out.println("-------------------------------");
if(myClient!=null)      myClient.close();
      super.destroy();
  }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void setupKeys() {
        inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(this, "wireframe");
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("CharUp", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("CharDown", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("CharSpace", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("CharShoot", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("CharHighter", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("CharLower", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addListener(this, "CharHighter");
        inputManager.addListener(this, "CharLower");
        inputManager.addListener(this, "CharLeft");
        inputManager.addListener(this, "CharRight");
        inputManager.addListener(this, "CharUp");
        inputManager.addListener(this, "CharDown");
        inputManager.addListener(this, "CharSpace");
        inputManager.addListener(this, "CharShoot");
    }

    private void prepareBullet() {
        bullet = new Sphere(32, 32, 0.4f, true, false);
        bullet.setTextureMode(Sphere.TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(0.1f);
        matBullet = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matBullet.setColor("Color", ColorRGBA.Green);
        matBullet.setColor("GlowColor", ColorRGBA.Magenta);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    private void setupChaseCamera() {
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, player.model, inputManager);
        chaseCam.setLookAtOffset(new Vector3f(0.0f, 8.0f, 0.0f));
    }

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("CharShoot") && !value) {
            bulletControl();
            // gui.pic.removeFromParent();
        }
        player.actionControl(binding, value, tpf, cam);
    }

    private void bulletControl() {
        /*shootingChannel.setAnim("Dodge", 0.1f);
         * shootingChannel.setLoopMode(LoopMode.DontLoop);*/
        Geometry bulletg = new Geometry("bullet", bullet);
        bulletg.setMaterial(matBullet);
        bulletg.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        bulletg.setLocalTranslation(player.character.getPhysicsLocation().add(cam.getDirection().mult(14)));
        RigidBodyControl bulletControl = new BombControl(bulletCollisionShape, 1);
        bulletControl.setGravity(new Vector3f(0.0f, 0.0f, 0.001f));
        bulletControl.setCcdMotionThreshold(0.1f);
        bulletControl.setFriction(0f);
        bulletControl.setLinearVelocity(cam.getDirection().mult(500).add(new Vector3f(1.0f, 15.0f, 1.0f)));
        bulletg.addControl(bulletControl);
        rootNode.attachChild(bulletg);
        bulletAppState.getPhysicsSpace().add(bulletControl);

        /*
         */
    }

    public void collision(PhysicsCollisionEvent event) {
        if (event.getObjectA() instanceof BombControl) {
            final Spatial node = event.getNodeA();
            world.effect.killAllParticles();
            world.effect.setLocalTranslation(node.getLocalTranslation());
            world.effect.emitAllParticles();
        } else if (event.getObjectB() instanceof BombControl) {
            final Spatial node = event.getNodeB();
            world.effect.killAllParticles();
            world.effect.setLocalTranslation(node.getLocalTranslation());
            world.effect.emitAllParticles();
        }

        if (event.getObjectA() instanceof OilCollision) {
            if (event.getObjectB() instanceof BombControl) {
                event.getObjectA().detachDebugShape();
                player.fuelStocked = Math.min(player.fuelStocked + 150, player.MAXFUEL);
            }

        } else if (event.getObjectB() instanceof OilCollision) {
            if (event.getObjectA() instanceof BombControl) {
                player.fuelStocked = Math.min(player.fuelStocked + 150, player.MAXFUEL);
            }

        }
    }
}
