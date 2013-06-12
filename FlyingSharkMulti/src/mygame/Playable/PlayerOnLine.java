/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Playable;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
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
public class PlayerOnLine {

    //character
    public CharacterControl character;
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

    public PlayerOnLine(Integer x, Integer y, Integer z, AssetManager assetManager, BulletAppState bulletAppState,
            Node rootNode) {
        this.fuelStocked = MAXFUEL;
        BoxCollisionShape capsule = new BoxCollisionShape(new Vector3f(5f,2f,3f));
        character = new CharacterControl(capsule, 1.3f);
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
        rootNode.attachChild(model);
        bulletAppState.getPhysicsSpace().add(character);
    }


    public void update(float tpf, Camera cam) {



    }
}
