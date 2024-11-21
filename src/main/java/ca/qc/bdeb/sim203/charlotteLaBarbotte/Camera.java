package ca.qc.bdeb.sim203.charlotteLaBarbotte;

public class Camera {

    public double getX() {
        return x;
    }


    public void setX(double x) {
        this.x = x;
    }

    private double x;

    public double calculerEcranX(double xMonde) {
        return xMonde - x;

    }


    public void update(double deltaTemps,double vitesseCharlotte) {//vitesse
        x += deltaTemps * vitesseCharlotte;
    }

    public void suivre(Personnage personnage,double deltaTemps, double vitesseCharlotte) {
        if ( !personnage.isEstAlafinDuNIveau() && personnage.getGauche()>Main.LARGEUR_FENETRE/5+x){
            update(deltaTemps,vitesseCharlotte);
        }
    }
}
