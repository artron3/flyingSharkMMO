/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.SceneElem.Element;

import com.bulletphysics.dynamics.RigidBody;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author cailloux
 */
public class Base {
    public BaseCollision oilCollision2;

    public Base(int x, int y, int z, AssetManager assetManager, BulletAppState bulletAppState, Node rootNode) {
        
        BoxCollisionShape brick2 = new BoxCollisionShape(new Vector3f(500, 90, 120));
        Spatial model2 = new Node();// new PQTorus(z, z, z, bWidth, steps, radialSamples)
        Material matBullet = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //brick2.scaleTextureCoordinates(new Vector2f(1f, .5f));
        matBullet.setColor("Color", ColorRGBA.LightGray);
        matBullet.setColor("GlowColor", ColorRGBA.Cyan);
        this.oilCollision2 = new BaseCollision(brick2, 0.0f);
        this.oilCollision2.setGravity(Vector3f.ZERO);
        this.oilCollision2.clearForces();
        //model2 = (Node) assetManager.loadModel(TEXTURE);

        //Spatial model = new Node();// new PQTorus(z, z, z, bWidth, steps, radialSamples)
        Box wall = new Box(500, 90, 120);//Box(Vector3f.ZERO, bLength, bHeight, bWidth);
        model2 = (new Geometry("brick2", wall));
        model2.setMaterial(matBullet);
        //model2.setLocalScale(0.5f); //make bigger/smaller model2
        model2.addControl(this.oilCollision2);
        // oilCollision2.setPhysicsLocation(new Vector3f(-170, 15, 28));
        rootNode.attachChild(model2);
        bulletAppState.getPhysicsSpace().add(this.oilCollision2);
        this.oilCollision2.setGravity(new Vector3f(0.0f, 0.0f, 0.0f));
        this.oilCollision2.clearForces();
        this.oilCollision2.setPhysicsLocation(new Vector3f(x,y,z));//20, -15, 900));
    }
    
    
}
