/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Playable;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;

/**
 *
 * @author cailloux
 */
public class Player {
    
    //character
    public CharacterControl character;
    public Node model = new Node();
    public Float MAXFUEL = 100000.0f;
    String TEXTURE = "Models/HoverTank/Tank2.mesh.xml";  ///Batman.mesh.xml"; //
    public Float fuelStocked ;
    public Float acceleRate = 4.0f;    // vitesse du joueur
    public Float SPEEDMAX = 13.0f;      //speed max du joueur
    public Float MINSTALLEDSPEED = 2.5f; // vitesse min avant décrochage
    public Float currentSpeed =0f;  // vitesse actuel du joueur
    public Float fallSpeed = 9.8f; // utilisé pour retirer la gravité
    
    Vector3f walkDirection = new Vector3f();
    //camera
    boolean left = false, right = false, up = false, down = false, higth = false;
    boolean low = false;
    
    float UPSPEED = 0.25f;
    
    public Player (AssetManager assetManager, BulletAppState bulletAppState,
            Node rootNode) {
        this.fuelStocked = MAXFUEL;
        SphereCollisionShape capsule = new SphereCollisionShape(1f);
        character = new CharacterControl(capsule, 2.1f);
        model = (Node) assetManager.loadModel(TEXTURE);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"));
        mat.setColor("Color", new ColorRGBA(0f, 191f, 255f, 0.17f)); // purple
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        model.setMaterial(mat);
       
        model.setQueueBucket(Bucket.Transparent);
        //model.setLocalScale(0.5f); make bigger/smaller model
        model.addControl(character);
        character.setPhysicsLocation(new Vector3f(-140, 15, -10));
        rootNode.attachChild(model);
        bulletAppState.getPhysicsSpace().add(character);
    }
    
     public void actionControl(String binding, boolean value, float tpf, Camera cam) {
        if (binding.equals("CharLeft")) {
            if (value) {
                left = true;
            } else {
                left = false;
            }
        } else if (binding.equals("CharRight")) {
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
            }
        } else if (binding.equals("CharDown")) {
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
        } else if (binding.equals("CharShoot") && !value) {
          /*  
        System.out.println("X = "+ character.getWalkDirection().x);
        System.out.println("Y = "+ character.getWalkDirection().y);
        System.out.println("Z = "+ character.getWalkDirection().z);*/
       /* System.out.println("currentspeed = "+ currentSpeed);
        System.out.println("fuelStocked = "+ fuelStocked);
        System.out.println("accelerate = "+ acceleRate);*/
        
        }
    }
     
      public void update(float tpf, Camera cam) {
        
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
                currentSpeed += tpf;
                currentSpeed = Math.min(currentSpeed , acceleRate);
            walkDirection.addLocal(camDir);
                fuelStocked -= currentSpeed / 10 ;
                if(fuelStocked<=0)
                    up = false;
        }
        else{
            currentSpeed -= tpf;
            currentSpeed -= tpf;
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
            
            character.setFallSpeed(0.8f);
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

    public CharacterControl getCharacter() {
        return character;
    }

    public void setCharacter(CharacterControl character) {
        this.character = character;
    }

    public Node getModel() {
        return model;
    }

    public void setModel(Node model) {
        this.model = model;
    }

     
      
}
