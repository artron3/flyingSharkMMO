/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Playable;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import mygame.Game;

/**
 *
 * @author cailloux
 */
public class PlayerOnLine {
    
    //character
    public PlayerCollision character;
    public Node model = new Node();
    public Float MAXFUEL = 100000.0f;
    String TEXTURE = "Models/HoverTank/Tank2.mesh.xml";  ///Batman.mesh.xml"; //
    public Float fuelStocked;
    public Float acceleRate = 4.0f;    // vitesse du joueur
    public Float SPEEDMAX = 13.0f;      //speed max du joueur
    public Float MINSTALLEDSPEED = 2.5f; // vitesse min avant décrochage
    public Float currentSpeed = 0f;  // vitesse actuel du joueur
    public Float fallSpeed = 9.8f; // utilisé pour retirer la gravité
    Vector3f walkDirection = new Vector3f();
    //camera
    boolean left = false, right = false, up = false, down = false, higth = false;
    boolean low = false;
    float UPSPEED = 0.25f;
    
    
        Material matBullet;
    //bullet
    Sphere bullet;
    SphereCollisionShape bulletCollisionShape;
    private float time;
        
    public PlayerOnLine(int x, int y, int z, AssetManager assetManager, BulletAppState bulletAppState,
            Node rootNode) {
        this.fuelStocked = MAXFUEL;
        BoxCollisionShape capsule = new BoxCollisionShape(new Vector3f(5f,2f,3f));
        character = new PlayerCollision(capsule, 1.3f);
        model = (Node) assetManager.loadModel(TEXTURE);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"));
        mat.setColor("Color", new ColorRGBA(250f, 0, 0, 1f)); // purple
        // mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        model.setMaterial(mat);
        
        model.setQueueBucket(Bucket.Transparent);
        //model.setLocalScale(0.5f); make bigger/smaller model
        model.addControl(character);
        character.setPhysicsLocation(new Vector3f(x, y+150, z));
        character.setViewDirection(character.getViewDirection().mult(new Vector3f(1f,1f, -1f)));
        rootNode.attachChild(model);
        bulletAppState.getPhysicsSpace().add(character);
        
        
        bullet = new Sphere(32, 32, 0.4f, true, false);
        bullet.setTextureMode(Sphere.TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(0.1f);
        matBullet = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matBullet.setColor("Color", ColorRGBA.Green);
        matBullet.setColor("GlowColor", ColorRGBA.Magenta);
    }

    
    
    public void update(float tpf, Node rootNode, BulletAppState bulletAppState) {
        
        this.time += tpf;
        
        if(this.time>1f && this.character.life>0){
            this.time=0f;
        Geometry bulletg = new Geometry("bullet", bullet);
        bulletg.setMaterial(matBullet);
        bulletg.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        bulletg.setLocalTranslation(character.getPhysicsLocation().add(character.getViewDirection().mult(14)));
        RigidBodyControl bulletControl = new BombControl(bulletCollisionShape, 0.1f);
        bulletControl.setGravity(new Vector3f(0.0f, 0.0f, 0.001f));
        bulletControl.setCcdMotionThreshold(0.1f);
        bulletControl.setFriction(0f);
        bulletControl.setLinearVelocity(character.getViewDirection().mult(500));
        bulletg.addControl(bulletControl);
        rootNode.attachChild(bulletg);
        bulletAppState.getPhysicsSpace().add(bulletControl);
        }
        
    }
}
