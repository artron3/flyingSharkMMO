/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting;

/**
 *
 * @author cailloux
 */
public class GUI {
    public String TEXTURE;
    public Picture pic;


    public GUI(String Texture,Integer x, Integer y, Integer width, Integer  height,
            AssetManager assetManager, AppSettings settings, Node guiNode) {
        this.TEXTURE = Texture;
        pic = new Picture("HUD Picture");
        pic.setImage(assetManager, TEXTURE, true);
        pic.setWidth(width);
        pic.setHeight(height);
        pic.setPosition(x, y);
        guiNode.attachChild(pic);
    }
    
    
}
