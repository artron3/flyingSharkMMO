/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.SceneElem.Element;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import java.io.IOException;
import mygame.Playable.BombControl;

/**
 *
 * @author cailloux
 */
public class BaseCollision extends RigidBodyControl implements PhysicsCollisionListener, PhysicsTickListener {

    private float explosionRadius = 10;
    private PhysicsGhostObject ghostObject;
    public Boolean isDestroyed = false;
    public Integer life = 1000;

    public BaseCollision(CollisionShape shape, float mass) {
        super(shape, mass);
        createGhostObject();
    }

    public BaseCollision(AssetManager manager, CollisionShape shape, float mass) {
        super(shape, mass);
        createGhostObject();
        prepareEffect(manager);
    }

    public void setPhysicsSpace(PhysicsSpace space) {
        super.setPhysicsSpace(space);
        if (space != null) {
            space.addCollisionListener(this);
        }
    }

    private void prepareEffect(AssetManager assetManager) {
    }

    protected void createGhostObject() {
        ghostObject = new PhysicsGhostObject(new SphereCollisionShape(explosionRadius));
    }

    public void collision(PhysicsCollisionEvent event) {
        if (event.getObjectA() == this) {
            if (event.getObjectB() instanceof BombControl) {
                this.life -= 1;
                if (this.life <= 0) {
                    this.isDestroyed = true;
                }
            }
        } else if (event.getObjectB() == this) {
            if (event.getObjectA() instanceof BombControl) {
                this.life -= 1;
                if (this.life <=0)
                    this.isDestroyed = true;
            }
        }
//        throw new UnsupportedOperationException("Not supported yet.");

    }

    public void prePhysicsTick(PhysicsSpace space, float f) {
        space.removeCollisionListener(this);
    }

    public void physicsTick(PhysicsSpace space, float f) {
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        if (this.isDestroyed) {

            this.space.removeCollisionListener(this);
            this.space.remove(this);
            this.spatial.removeFromParent();
        }

    }

    /**
     * @return the explosionRadius
     */
    public float getExplosionRadius() {
        return explosionRadius;
    }

    /**
     * @param explosionRadius the explosionRadius to set
     */
    public void setExplosionRadius(float explosionRadius) {
        this.explosionRadius = explosionRadius;
        createGhostObject();
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Reading not supported.");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Saving not supported.");
    }
}
