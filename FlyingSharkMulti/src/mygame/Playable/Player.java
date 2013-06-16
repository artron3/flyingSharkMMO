/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Playable;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.PlayerAlex;
import mygame.SceneElem.Element.Base;

/**
 *
 * @author cailloux
 */
public class Player {
    
    //character
    public PlayerCollision character;
    public Node model = new Node();
    public Float MAXFUEL = 1000.0f;
    String TEXTURE = "Models/HoverTank/Tank2.mesh.xml";  ///Batman.mesh.xml"; //
    public Float fuelStocked ;
    public Float acceleRate = 4.0f;    // vitesse du joueur
    public Float SPEEDMAX = 13.0f;      //speed max du joueur
    public Float MINSTALLEDSPEED = 2.5f; // vitesse min avant décrochage
    public Float currentSpeed =0f;  // vitesse actuel du joueur
    public Float fallSpeed = 9.8f; // utilisé pour retirer la gravité
    public boolean game = false;
    public int faction ;
    Vector3f walkDirection = new Vector3f();
    public Integer ID;
    
    Spatial missile;
     Node rootNode;
    BulletAppState bulletAppState;
    AssetManager assetManager;
    //camera
    boolean left = false, right = false, up = false, down = false, higth = false;
    boolean low = false;
    boolean isInit = false;
    Camera cam;
    
    float UPSPEED = 0.25f;
    
    public Player (AssetManager assetManager, BulletAppState bulletAppState,
            Node rootNode, Camera cam) {
    
        ID= 20;
        faction =  (int) Math.random()*10000;
        this.cam = cam;
        this.bulletAppState = bulletAppState;
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.fuelStocked = MAXFUEL;
        SphereCollisionShape capsule = new SphereCollisionShape(1f);
        character = new PlayerCollision(capsule, 1.3f);
        model = (Node) assetManager.loadModel(TEXTURE);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"));
        mat.setColor("Color", new ColorRGBA(0f, 191f, 255f, 0.17f)); // purple
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        model.setMaterial(mat);
       
        model.setQueueBucket(Bucket.Transparent);
        //model.setLocalScale(0.5f); make bigger/smaller model
        model.addControl(character);
        character.setPhysicsLocation(new Vector3f(-140, 5, -10));
        rootNode.attachChild(model);
        bulletAppState.getPhysicsSpace().add(character);
        missile = assetManager.loadModel("Models/SpaceCraft/Rocket.mesh.xml");
        character.setFallSpeed(0f);

    }
    
    public void updateServ(PlayerAlex alex){
        if (!isInit){
            character.setPhysicsLocation(alex.getPos());
            character.setViewDirection(alex.getDirection());
            faction = alex.getFaction();
            isInit = true;
        }
    }
    
     public void actionControl(String binding, boolean value, float tpf, Camera cam) {
        if (binding.equals("CharLeft")) {
           if (value) {
                left = true;
            } else {
                left = false;
            }
        } else if (binding.equals("CharRight")) {
            System.out.println(" Y LOCATION:" + character.getPhysicsLocation().getY());
            System.out.println(" X LOCATION:" + character.getPhysicsLocation().getX());
            System.out.println(" Z LOCATION:" + character.getPhysicsLocation().getZ());
            System.out.println(" --------------------------------- ");
            System.out.println(" --------------------------------- ");
            if (value) {
                right = true;
            } else {
                right = false;
            }
        } else if (binding.equals("CharUp")) {
            if (value&& fuelStocked>0) {
                up = true;
            } else {
                up = false;
                game = true;
            }
        } else if (binding.equals("CharDown")) {
            System.out.println("X = " + cam.getDirection().x);
            System.out.println("X = " + cam.getDirection().x);
            System.out.println("Y = " + cam.getDirection().y);
            System.out.println("Y = " + cam.getDirection().y);
            System.out.println("Z = " + cam.getDirection().z);
            System.out.println("Z = " + cam.getDirection().z);
            if (value) {
                down = true;
            } else {
                down = false;
            }
        } else if (binding.equals("CharLower")) {
            if (value) {
                low = true;
            } else {
                low = false;
                acceleRate = Math.max(acceleRate - 0.5f, 0);
            }
        } else if (binding.equals("CharHighter")) {
            if (value) {
                higth = true;
            } else {
                higth = false;
                acceleRate = Math.min(acceleRate + 0.5f, SPEEDMAX);
            }
        } else if (binding.equals("CharSpace")) {
            System.out.println(" Y LOCATION:" + character.getPhysicsLocation().getY());
            System.out.println(" X LOCATION:" + character.getPhysicsLocation().getX());
            System.out.println(" Z LOCATION:" + character.getPhysicsLocation().getZ());
            System.out.println(" --------------------------------- ");
            System.out.println(" --------------------------------- ");
            
            
            Vector3f pos = character.getPhysicsLocation().clone();
            Quaternion rot = cam.getRotation().clone();
            Vector3f dir = rot.getRotationColumn(2);
            
            missile.scale(0.5f);
            missile.rotate(0, FastMath.PI, 0);
            missile.updateGeometricState();
            BoundingBox box = (BoundingBox) missile.getWorldBound();
            final Vector3f extent = box.getExtent(null);

            BoxCollisionShape boxShape = new BoxCollisionShape(extent);

            missile.setName("Missile");
            missile.rotate(rot);
            missile.setLocalTranslation(pos.addLocal(0, extent.y * 4.5f, 0));
            missile.setLocalRotation(cam.getRotation());
            // missile.setShadowMode(RenderQueue.ShadowMode.Cast);
            RigidBodyControl control = new BombControl(assetManager, boxShape, 20);
            control.setLinearVelocity(dir.mult(100));
            control.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
            missile.addControl(control);


            rootNode.attachChild(missile);
            bulletAppState.getPhysicsSpace().add(missile);
        } else if (binding.equals("CharShoot") && !value) {
              
        System.out.println("X = "+ cam.getDirection().x);
        System.out.println("Y = "+ cam.getDirection().y);
        System.out.println("Z = "+ cam.getDirection().z);
       /* System.out.println("currentspeed = "+ currentSpeed);
        System.out.println("fuelStocked = "+ fuelStocked);
        System.out.println("accelerate = "+ acceleRate);*/
        }
    }
     
      public void update(float tpf) {
        
        Vector3f camDir = cam.getDirection().clone().multLocal(0.1f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.1f);
        camDir.y = 0;
        camLeft.y = 0;
          walkDirection.set(0.00f, 0.000f, 0.00f);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
          //      currentSpeed += tpf;
            //    currentSpeed = Math.min(currentSpeed , acceleRate);
                currentSpeed = Math.min(currentSpeed + (((currentSpeed+tpf)*100)/(fuelStocked*(SPEEDMAX))), acceleRate);
         //       currentSpeed = Math.min(currentSpeed + (acceleRate - currentSpeed) / (SPEEDMAX), acceleRate);

            walkDirection.addLocal(camDir);
                fuelStocked -= currentSpeed / 300 ;
                if(fuelStocked<=0)
                    up = false;
        }
        else{
            currentSpeed -= 1.3f*tpf;
            currentSpeed = Math.max(currentSpeed, 0f);
            if(currentSpeed> 0.4f)
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        if (walkDirection.length() == 0) {
        } else {
            character.setViewDirection(walkDirection);
            /*System.out.println("camLeft X" + camLeft.getX());
            System.out.println("camLeft Y" + camLeft.getY());
            System.out.println("camLeft Z" + camLeft.getZ());*/
        }
        if(currentSpeed >= MINSTALLEDSPEED ){
            character.setFallSpeed(0.0f);
            
          if((currentSpeed)*50 > character.getPhysicsLocation().getY()+10f){
            //if(character.getPhysicsLocation().getY() < 600.0f) //EMPECHE LE JOUEUR DE DEPASSER LES 600 EN HAUTEUR
              walkDirection.addLocal(new Vector3f(0.0f, UPSPEED* 1/4, 0f));  
            //
                //.setZ(600);
          }
          if((currentSpeed)*50 < (character.getPhysicsLocation().getY()-10f)){
           // if(character.getPhysicsLocation().getY() < 600.0f) //EMPECHE LE JOUEUR DE DEPASSER LES 600 EN HAUTEUR
              walkDirection.addLocal(new Vector3f(0.0f, -UPSPEED* 1/4, 0f));  
            //
                //walkDirection.setZ(0);
          }
        }
        if(currentSpeed< MINSTALLEDSPEED){
            
            character.setFallSpeed(Math.min(character.getFallSpeed() + 4f*tpf, 20f));
        }
          
      /* if(acceleRate >= MINSTALLEDSPEED &&
                ((acceleRate - MINSTALLEDSPEED)*50 < character.getPhysicsLocation().getY())){
          
          walkDirection.addLocal(new Vector3f(0.0f, ((acceleRate - MINSTALLEDSPEED)*50 - character.getPhysicsLocation().getY()) *tpf, 0f));  
        }*/
        character.setWalkDirection(walkDirection.mult(currentSpeed + 2.2f));
       /* model.setLocalRotation(new Matrix3f(
                a*a+(1-a*a)*Math.cos(angle), a*b*(1-Math.cos(teta))-c*Sin, tpf,
                                            tpf, tpf, tpf,
                                            tpf, tpf, tpf));*/
    }      
}
