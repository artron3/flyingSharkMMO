/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import mygame.GUI.GUI;
import mygame.Playable.Player;
import mygame.Playable.BombControl;
import mygame.SceneElem.*;
import mygame.SceneElem.Element.Oil;
import mygame.SceneElem.Element.OilCollision;

public class Game extends SimpleApplication implements 
        ActionListener, PhysicsCollisionListener {

    
    public static void main(String[] args) {
        Game app = new Game();
        app.start();
    }
    private Player player;
    private World world;
    private  ArrayList<Oil> oils = new ArrayList<Oil>(10);
    
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
    Integer lastValueOilStocked =0;
    ArrayList<GUI> oilArray = new ArrayList<GUI>(10);
    BitmapText hudText; // display speed
    

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        setupKeys();
        prepareBullet();
        world = new World( assetManager, bulletAppState, rootNode, getCamera(), viewPort);

        Oil oil = new Oil(-170.0f, 17.1f, 28.0f, rootNode, bulletAppState, getAssetManager());  
        
        player = new Player(assetManager, bulletAppState, rootNode);
        setupChaseCamera();
        guiViseur = new GUI("Interface/viseur.png",
                4 * settings.getWidth() / 9, 7 * settings.getHeight() / 19, 1 * settings.getWidth() / 9, 2 * settings.getHeight() / 9,
                assetManager, settings, guiNode);
        for (int i= 1; i<=10; ++i){
            guiOilStocked = new GUI("Interface/oil_"+i+".png",
                1 * settings.getWidth() / 12, 1 * settings.getHeight() / 12, 1 * settings.getWidth() / 9, 3 * settings.getHeight() / 9,
                assetManager, settings, guiNode);
            guiOilStocked.pic.setPosition(-200f, -200f);
            oilArray.add(guiOilStocked);
        }
        guiOil = new GUI("Interface/oilGround.png",
                1 * settings.getWidth() /12, 1 * settings.getHeight() / 12, 1 * settings.getWidth() / 9, 3 * settings.getHeight() / 9,
                assetManager, settings, guiNode);
        
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(51f);      // font size
        hudText.setColor(ColorRGBA.Blue);                             // font color
        hudText.setText("0 KM");             // the text
        hudText.setLocalTranslation(settings.getWidth()-200, 100, 0); // position
        guiNode.attachChild(hudText);
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(null != cam)
        player.update(tpf, cam);
        else
            System.out.println(cam.toString());
       // if (bulletAppState.getPhysicsSpace().)
       // guiOil.pic.setHeight(player.fuelStocked/1000 * 200);
        Integer oilStocked =  Math.round(player.fuelStocked/(player.MAXFUEL/10));
        if(lastValueOilStocked != oilStocked){
            if (oilStocked != 10) {
                oilArray.get(oilStocked).pic.setPosition(-200f, -200f);
            }
            oilArray.get(oilStocked-1).pic.setPosition(1 * settings.getWidth() / 12, 1 * settings.getHeight() / 12);
        }
        if(oilStocked.equals(0))
            oilArray.get(0).pic.setPosition(-200f, -200f);
        hudText.setText(Math.round(player.currentSpeed *51)+ " KM");
            
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
        System.out.println(this);
    }

    private void setupChaseCamera() {
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, player.getModel(), inputManager);
        chaseCam.setLookAtOffset(new Vector3f(0.0f, 8.0f, 0.0f));
    }

    public void onAction(String binding, boolean value, float tpf) {
       if (binding.equals("CharShoot") && !value) {
            bulletControl();
            Oil oil = new Oil(-170, 25, 28, rootNode, bulletAppState, getAssetManager());  
           // gui.pic.removeFromParent();
        oils.add(oil);
        }
       player.actionControl(binding, value, tpf, cam);
    }

    private void bulletControl() {
        /*shootingChannel.setAnim("Dodge", 0.1f);
        shootingChannel.setLoopMode(LoopMode.DontLoop);*/
        Geometry bulletg = new Geometry("bullet", bullet);
        bulletg.setMaterial(matBullet);
        bulletg.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        bulletg.setLocalTranslation(player.character.getPhysicsLocation().add(cam.getDirection().mult(17)));
        RigidBodyControl bulletControl = new BombControl(bulletCollisionShape, 1);
        bulletControl.setGravity(new Vector3f(0.0f, 0.0f, 0.001f));
        bulletControl.setCcdMotionThreshold(0.1f);
        bulletControl.setLinearVelocity(cam.getDirection().mult(500).add(new Vector3f(1.0f, 15.0f, 1.0f)));
        bulletg.addControl(bulletControl);
        rootNode.attachChild(bulletg);
        bulletAppState.getPhysicsSpace().add(bulletControl);
    }

    public void collision(PhysicsCollisionEvent event) {
        if (event.getObjectA() instanceof BombControl ){
            final Spatial node = event.getNodeA();
            world.effect.killAllParticles();
            world.effect.setLocalTranslation(node.getLocalTranslation());
            world.effect.emitAllParticles();
            System.out.println("CCCCCC");
        } else if (event.getObjectB() instanceof BombControl) {
            final Spatial node = event.getNodeB();
            world.effect.killAllParticles();
            world.effect.setLocalTranslation(node.getLocalTranslation());
            world.effect.emitAllParticles();
            System.out.println("BBBBBBBB");
        }
        
        if (event.getObjectA() instanceof OilCollision) {
            if (event.getObjectB() instanceof BombControl){
            System.out.println("DETECTED COLL3333");
            System.out.println("DETECTED COLL3333");
            event.getObjectA().detachDebugShape();
            player.fuelStocked = Math.min(player.fuelStocked +150, player.MAXFUEL);
            }
            
        }else if (event.getObjectB() instanceof OilCollision) {
            if (event.getObjectA() instanceof BombControl ){
            System.out.println("DETECTED COLL44444444");
            System.out.println("DETECTED COLL44444444");
            player.fuelStocked = Math.min(player.fuelStocked + 150, player.MAXFUEL);
            }
            
        }
    }
}

