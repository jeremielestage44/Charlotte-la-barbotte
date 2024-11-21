package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class Decor extends ObjetsDuJeu{

    private  Random random = new Random();

    private int typeDecor=random.nextInt(6)+1;

    private String nomDecor="code/decor"+typeDecor+".png";


    private final double ENFONCE_SOL=10;
    public Decor() {
        h=119;
        w=80;
        y=Main.HAUTEUR_FENETRE+ENFONCE_SOL-h;
        image=new Image(nomDecor);
    }


    @Override
    public void draw(GraphicsContext context, Camera camera) {
        double xEcran=camera.calculerEcranX(x);
        context.drawImage(image,xEcran,y);
    }
}
