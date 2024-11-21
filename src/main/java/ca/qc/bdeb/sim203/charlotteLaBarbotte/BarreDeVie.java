package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BarreDeVie extends ObjetsDuJeu{


    public void setNbDeViePoissons(double nbDeViePoissons) {
        this.nbDeViePoissons = nbDeViePoissons;
    }

    private double nbDeViePoissons=4;
    public BarreDeVie() {
        w=150;
        h=30;
        x=15;
        y=10;

    }
    public void perdUneVie(){
        nbDeViePoissons-=1;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        context.setStroke(Color.WHITE);
        context.strokeLine(x, y, x + w, y);
        context.strokeLine(x, y + h, x + w, y + h);
        context.strokeLine(x, y, x, y + h);
        context.strokeLine(x + w, y, x + w, y + h);
        context.setFill(Color.WHITE);
        if(nbDeViePoissons==4){
            context.fillRect(x,y,w,h);
        }
        else if(nbDeViePoissons==3){
            context.fillRect(x,y,x+112,h);
        }
        else if(nbDeViePoissons==2){
            context.fillRect(x,y,x+75,h);
        }
        else if(nbDeViePoissons==1){
            context.fillRect(x,y,x+37,h);
        }

    }
}
