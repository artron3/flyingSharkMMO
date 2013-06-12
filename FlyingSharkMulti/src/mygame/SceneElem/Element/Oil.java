/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.SceneElem.Element;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.PQTorus;

/**
 *
 * @author cailloux
 */
public class Oil   implements PhysicsCollisionListener {
   float x, y, z;

    PQTorus  wall;
    BoxCollisionShape brick;
    float bLength = 10.8f;
    float bWidth = 10.4f;
    float bHeight = 10.4f;
    //Materials
    Material matBullet;
    OilCollision oilCollision;
    
    public Oil(float x, float y, float z, Node rootNode, BulletAppState bulletAppState, AssetManager assetManager) {
        
        brick = new BoxCollisionShape(new Vector3f( bLength, bHeight, bWidth));
         matBullet = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //brick.scaleTextureCoordinates(new Vector2f(1f, .5f));
        matBullet.setColor("Color", ColorRGBA.LightGray);
        matBullet.setColor("GlowColor", ColorRGBA.Cyan);  
        oilCollision = new OilCollision(brick, 10.01f);
        oilCollision.setGravity(Vector3f.ZERO);
        oilCollision.clearForces();
        //model = (Node) assetManager.loadModel(TEXTURE);
       
        Spatial model = new Node();// new PQTorus(z, z, z, bWidth, steps, radialSamples)
        wall = new PQTorus(3,8f, 2f, .6f, 38, 5);//Box(Vector3f.ZERO, bLength, bHeight, bWidth);
        model =  (new Geometry("brick", wall));
        model.setMaterial(matBullet);
        //model.setLocalScale(0.5f); //make bigger/smaller model
        model.addControl(oilCollision);
       // oilCollision.setPhysicsLocation(new Vector3f(-170, 15, 28));
        rootNode.attachChild(model);
        bulletAppState.getPhysicsSpace().add(oilCollision);
        oilCollision.setGravity(new Vector3f(0.0f, 0.0f, 0.0f));
        oilCollision.clearForces();
        oilCollision.setPhysicsLocation(new Vector3f(x, y, z));
        
        
        
       /* matBullet = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        float startpt = bLength / 4 - x;
        brick = new Box(Vector3f.ZERO, bLength, bHeight, bWidth);
        brick.scaleTextureCoordinates(new Vector2f(1f, .5f));
       // for (int j = 0; j < 15; j++) {
         //   for (int i = 0; i < 4; i++) {
                Vector3f vt = new Vector3f(1 * bLength * 2 + startpt, bHeight + y, z);
                addBrick(vt, rootNode, bulletAppState);
 //           }
            startpt = -startpt;
            y += 1.01f * bHeight;
       // }*/
    }
    
    
    private void addBrick(Vector3f ori, Node rootNode, BulletAppState bulletAppState) {
       /* Geometry reBoxg = new Geometry("brick", brick);
        reBoxg.setMaterial(matBullet);
        reBoxg.setLocalTranslation(ori);
        reBoxg.addControl(new RigidBodyControl(1.5f));
        reBoxg.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(reBoxg);
        bulletAppState.getPhysicsSpace().addCollisionListener(reBoxg);*/
    }

    public void collision(PhysicsCollisionEvent event) {
        System.out.println("22222");
        throw new UnsupportedOperationException("Not supported yet.");

    }

    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        System.out.println("33333333");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void physicsTick(PhysicsSpace space, float tpf) {
        System.out.println("44444");
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
