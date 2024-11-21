package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class Sardine extends Projectile{

    private Image imageSardine=new Image("code/sardines.png");


    private final double K=1000;

    private final double CHARGE_PROTON=200;

    private double forceElectriqueX;

    private double forceElectriqueY;


    public Sardine() {
        w=35;
        h=29;
        vx=300;
        vy=0;
    }

    @Override
    public void update(double deltaTemps) {
        ax=forceElectriqueX;
        ay=forceElectriqueY;
        vx += deltaTemps * ax;
        vy += deltaTemps * ay;
        if(vx>500){
            vx=500;
        }
        if(vx<300){
            vx=300;
        }
        if(vy>500){
            vy=500;
        }
        if(vy<-500){
            vy=-500;
        }
        x += deltaTemps * vx;
        y += deltaTemps * vy;
        if(getBas()>Main.HAUTEUR_FENETRE){
            y=Main.HAUTEUR_FENETRE-h;
            vy=-vy;
        }
        if(getHaut()<0){
            y=0;
            vy=-vy;
        }
        forceElectriqueY=0;
        forceElectriqueX=0;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
            super.draw(context,camera);
            double xEcran=camera.calculerEcranX(x);
            context.drawImage(imageSardine,xEcran,y);}

    public void calculerForceElectrique(PoissonEnnemi ennemi){
        double deltaX=getGauche()-ennemi.getGauche();
        double deltaY=getHaut()-ennemi.getHaut();
        double sommmePythagore=Math.pow(deltaX,2)+Math.pow(deltaY,2);
        double distance=Math.sqrt(sommmePythagore);
        if(distance<0.01){
            distance=0.01;
        }
        double proportionX = deltaX / distance;
        double proportionY = deltaY / distance;
        double forceElectrique=(K*CHARGE_PROTON*ennemi.getCHARGE_ENNEMI())/(Math.pow(distance,2));
        forceElectriqueX+=forceElectrique*proportionX;
        forceElectriqueY+=forceElectrique*proportionY;
   }
}
