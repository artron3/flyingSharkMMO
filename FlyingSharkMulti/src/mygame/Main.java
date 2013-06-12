package mygame;

import com.jme3.system.AppSettings;


/**
 * test
 * @author normenhansen
 */
public class Main  {

    
    public static void main(String[] args) {
        Game app = new Game();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL3);
        app.start();
    }

}
