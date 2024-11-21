package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class Baril extends ObjetsDuJeu{

    private Image imageBarilFerme=new Image("code/baril.png");

    private Image imageBarilOuvert=new Image("code/baril-ouvert.png");

    public final double PERIODE=3;

    public void setEstOuvert(boolean estOuvert) {
        this.estOuvert = estOuvert;
    }

    public boolean isEstOuvert() {
        return estOuvert;
    }

    private boolean estOuvert = false;

    private double tempsBaril;

    private Random random=new Random();


    public Baril (){
        w=70;
        h=83;
        x=random.nextDouble(1440,5760);
    }
    public void updateTemps(double tempsNiveau){
        tempsBaril=tempsNiveau;
    }
    public void update(double deltaTemps){
        y=(Main.HAUTEUR_FENETRE-h)/2*Math.sin((2*Math.PI*tempsBaril)/PERIODE)+(Main.HAUTEUR_FENETRE-h)/2;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
       double xEcran=camera.calculerEcranX(x);
        if(Main.enModeDeboggage){
            debogger(context,camera);
        }
        if(!estOuvert){
            context.drawImage(imageBarilFerme,xEcran,y);
        }
        else{
            context.drawImage(imageBarilOuvert,xEcran,y);
        }

    }
    public int genererProjectile(){
        int prochainProjectile= random.nextInt(2)+1;
        return prochainProjectile;
    }
}
