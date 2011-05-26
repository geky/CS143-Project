
package graphics;

import java.awt.Toolkit;

import javax.media.opengl.GL2;

public class Hud {
    Texture healthbackdrop, healthbar, healthcross, healthcrossflash, crosshair, gunbar, gunbackdrop,
    gunammosingle, gunammodouble, gunammomissile;

    private static final String HEALTHBACKDROP="assets/images/hud/health_backdrop.png";
    private static final String HEALTHBAR="assets/images/hud/health_bar.png";
    private static final String HEALTHCROSS = "assets/images/hud/health_cross.png";
    private static final String HEALTHCROSSFLASH = "assets/images/hud/health_cross_red.png";
    private static final String CROSSHAIR = "assets/images/hud/crosshair.png";
    private static final String GUNBAR = "assets/images/hud/gun_bar.png";
    private static final String GUNBACKDROP = "assets/images/hud/gun_backdrop.png";
    private static final String GUNAMMOSINGLE = "assets/images/hud/gun_ammo_single.png";
    private static final String GUNAMMODOUBLE = "assets/images/hud/gun_ammo_double.png";
    private static final String GUNAMMOMISSILE = "assets/images/hud/gun_ammo_missile.png";
    private final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    
    /**
     * Constructor loads all the textures   
     */
    public Hud() {   
        healthbackdrop = Texture.findOrCreateByName(HEALTHBACKDROP);
        healthbar = Texture.findOrCreateByName(HEALTHBAR);
        healthcross =  Texture.findOrCreateByName(HEALTHCROSS);
        healthcrossflash =  Texture.findOrCreateByName(HEALTHCROSSFLASH);
        gunbar = Texture.findOrCreateByName(GUNBAR);
        gunbackdrop = Texture.findOrCreateByName(GUNBACKDROP);
        gunammosingle = Texture.findOrCreateByName(GUNAMMOSINGLE);
        gunammodouble = Texture.findOrCreateByName(GUNAMMODOUBLE);
        gunammomissile = Texture.findOrCreateByName(GUNAMMOMISSILE);
        crosshair = Texture.findOrCreateByName(CROSSHAIR);
    }
    
    public void flashHealthCross(GL2 gl) {
        start2D(gl);
        
        if(healthcrossflash != null) {
            healthcrossflash.bind(gl);
        }
        gl.glBegin(GL2.GL_QUADS);
        draw(0,0,WIDTH,HEIGHT,gl);
       
        gl.glEnd();
        gl.glFlush();
        stop2D(gl);
    }
    /**
     * Draws the static elements of the HUD
     * @param gl
     */
    public void drawStaticHud(GL2 gl) {
        start2D(gl);

        if(healthbackdrop != null) {
            healthbackdrop.bind(gl);
        }

        gl.glBegin(GL2.GL_QUADS );
        draw(0,0,WIDTH,HEIGHT,gl);
        gl.glEnd();
        
        
        if(healthbar != null) {
            healthbar.bind(gl);
        }

        gl.glBegin(GL2.GL_QUADS );
        draw(0,0,WIDTH,HEIGHT,gl);
        gl.glEnd();
       
        if(healthcross != null) {
            healthcross.bind(gl);
        }

        gl.glBegin(GL2.GL_QUADS );
        draw(0,0,WIDTH,HEIGHT,gl);
        gl.glEnd();
     
        //
        if(gunbackdrop != null) {
            gunbackdrop.bind(gl);
        }

        gl.glBegin(GL2.GL_QUADS );
        draw(-WIDTH,0,WIDTH,HEIGHT,gl);
        gl.glEnd();
        
        if(gunbar != null) {
            gunbar.bind(gl);
        }

        gl.glBegin(GL2.GL_QUADS );
        draw(-WIDTH,0,WIDTH,HEIGHT,gl);
        gl.glEnd();
           
        if(gunammosingle != null) {
            gunammosingle.bind(gl);
        }

        gl.glBegin(GL2.GL_QUADS );
        draw(-WIDTH,0,WIDTH,HEIGHT,gl);
        gl.glEnd();
     //
        if(crosshair != null) {
            crosshair.bind(gl);
        }
        gl.glBegin(GL2.GL_QUADS );
        //draw(-18,20,36,36,gl);
        gl.glEnd();
        
        gl.glFlush();
        stop2D(gl);
    }
    
    /**
     * Easier way to draw 2d shapes
     * @param x x coord top left
     * @param y y coord top left
     * @param width width of image
     * @param height height of image
     * @param gl
     */
    private void draw(float x, float y, float width, float height, GL2 gl) {
        gl.glTexCoord2d(0.0, 1.0); gl.glVertex2d(x,y);
        gl.glTexCoord2d(0.0, 0.0); gl.glVertex2d(x,y-height);
        gl.glTexCoord2d(1.0, 0.0); gl.glVertex2d(x+width,y-height); 
        gl.glTexCoord2d(1.0, 1.0); gl.glVertex2d(x+width, y);
    }
    
    /**
     * Changes to 2D
     * @param gl
     */
    private void start2D(GL2 gl) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_BLEND);
        gl.glDisable(GL2.GL_LIGHTING);

        gl.glMatrixMode(GL2.GL_PROJECTION );
        gl.glPushMatrix();   // projection matrix 
        gl.glLoadIdentity();
        
        //So I don't forget -Tim
        //glOrtho(left,right,bottom,top,nearVal,farVal)
       // gl.glOrtho(-100.0f, 100.0f, -100.0f, 100.0f, -100.0f, 100.0f );
        gl.glOrtho(-WIDTH, WIDTH, -HEIGHT, HEIGHT,-100f,100f );
        
        gl.glMatrixMode(GL2.GL_MODELVIEW );
        gl.glPushMatrix(); // save our model matrix 
        gl.glLoadIdentity();
    }
    /**
     * Stops 2D and goes back to 3D
     * @param gl
     */
    private void stop2D(GL2 gl) {
        gl.glPopMatrix(); // recover model matrix
        gl.glMatrixMode(GL2.GL_PROJECTION );

        gl.glPopMatrix(); // recover projection matrix 
        gl.glMatrixMode(GL2.GL_MODELVIEW ); 

        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_LIGHTING );
        gl.glDisable(GL2.GL_BLEND);
    }
}
