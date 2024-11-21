package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public abstract class ObjetsDuJeu {

    protected double vx, vy;

    protected Image image;


    protected double x;

/*
    public double getGaucheEcran() {
        return xEcran;
    }

    public double getDroiteEcran() {
        return xEcran + w;
    }*/

    // protected double xEcran;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    protected double y;


    protected double ax, ay;

    protected double w, h;

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    /**
     * Dessine l'objet sur l'écran.
     * <p>
     * À redéfinir dans les sous-classes.
     */
    protected void draw(GraphicsContext context, Camera camera){
        debogger(context,camera);
        double xEcran=camera.calculerEcranX(x);
        context.drawImage(image,xEcran,y);
    }


    /**
     * Met à jour la vitesse selon l'accélération et
     * la position selon la vitesse.
     */
    protected void updatePhysique(double deltaTemps) {
        vx += deltaTemps * ax;
        vy += deltaTemps * ay;
        x += deltaTemps * vx;
        y += deltaTemps * vy;
    }

    protected boolean sortDeLaScene(Camera camera) {
        boolean sorti = false;
        if (getBas() < 0 || getDroite()-camera.getX() < 0 || getHaut() > Main.HAUTEUR_FENETRE || getGauche()-camera.getX() > Main.LARGEUR_FENETRE
        || getDroite()>Main.LONGUEUR_NIVEAU) {
            sorti = true;
        }
        return sorti;
    }

    /*
    protected boolean estEnCollisionEcran(ObjetsDuJeu autreObjet) {
        return (this.x < autreObjet.xEcran + autreObjet.w &&
                this.x + this.w > autreObjet.xEcran&&
                this.y < autreObjet.y + autreObjet.h &&
                this.y + this.h > autreObjet.y);
    }*/

    public boolean estEnCollision(ObjetsDuJeu autreObjet) {
        return (this.x < autreObjet.x + autreObjet.w &&
                this.x + this.w > autreObjet.x &&
                this.y < autreObjet.y + autreObjet.h &&
                this.y + this.h > autreObjet.y);
    }


    protected void debogger(GraphicsContext context,Camera camera) {
        double xEcran=camera.calculerEcranX(x);
        if (Main.enModeDeboggage) {
            context.setStroke(Color.YELLOW);
            context.strokeLine(xEcran, y, xEcran + w, y);
            context.strokeLine(xEcran, y + h, xEcran + w, y + h);
            context.strokeLine(xEcran, y, xEcran, y + h);
            context.strokeLine(xEcran + w, y, xEcran + w, y + h);
        }
    }

    /**
     * Par défaut, une nouvelle sous-classe de ObjetDuJeu ne va
     * rien faire d'autre que subir les lois de la physique
     */
    public void update(double deltaTemps) {
    }

    public double getHaut() {
        return y;
    }

    public double getBas() {
        return y + h;
    }

    public double getGauche() {
        return x;
    }

    public double getDroite() {
        return x + w;
    }


}
