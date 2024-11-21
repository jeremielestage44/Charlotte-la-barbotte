package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EtoileDeMer extends Projectile{

    private Image etoileDeMer;


    public EtoileDeMer() {
        w=36;
        h=35;
        etoileDeMer = new Image("code/etoile.png");
        vx=800;
    }

    public void draw(GraphicsContext context, Camera camera){
        super.draw(context,camera);
        double xEcran=camera.calculerEcranX(x);
        context.drawImage(etoileDeMer,xEcran,y);
    }
    public void update(double deltatemps){
        super.updatePhysique(deltatemps);
    }


}
