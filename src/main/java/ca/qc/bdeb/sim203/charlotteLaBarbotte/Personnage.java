package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;


public class Personnage extends ObjetsDuJeu {
    private final double HAUTEUR_IMAGE = 90;

    private final double LARGEUR_IMAGE = 102;

    private final double ACCELERATION = 1000;

    private final double VITESSE_MAXIMALE = 300;


    public void setNbdeVies(double nbdeVies) {
        this.nbdeVies = nbdeVies;
    }

    public double getVIES_MAX() {
        return VIES_MAX;
    }

    private final double VIES_MAX=4;

    private double nbdeVies = 4;

    public boolean isEstMort() {
        return estMort;
    }

    private  boolean estMort=false;

    Camera camera;

    private boolean estSurExtremiteGauche() {
        return getGauche() < 0;
    }

    private boolean estSurExtremiteHaute() {
        return getHaut() < 0;
    }

    private boolean estSurExtremiteBasse() {
        return getBas() > Main.HAUTEUR_FENETRE;
    }


    public boolean isFiniNiveau() {
        return finiNiveau;
    }

    public void setFiniNiveau(boolean finiNiveau) {
        this.finiNiveau = finiNiveau;
    }

    private boolean finiNiveau=false;


    public boolean isEstAlafinDuNIveau() {
        return estAlafinDuNIveau;
    }

    private boolean estAlafinDuNIveau = false;


    private Image charlotteAvant = new Image("code/charlotte-avant.png");
    private Image charlotte = new Image("code/charlotte.png");

    private Image charlotteBlesse = new Image("code/charlotte-outch.png");

    public Personnage(Camera camera) {
        this.camera=camera;
        y = Main.HAUTEUR_FENETRE / 2 - HAUTEUR_IMAGE;
        h = HAUTEUR_IMAGE;
        w = LARGEUR_IMAGE;

    }
    public void perdreVie(){
        if (!estMort){
            nbdeVies-=1;
        }
        if(nbdeVies==0){
            estMort=true;
        }
    }
    public  void commenceNiveau(){
        x=0;
        y = Main.HAUTEUR_FENETRE / 2 - HAUTEUR_IMAGE;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        double xEcran = camera.calculerEcranX(x);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);
        if (right) {
            context.drawImage(charlotteAvant, xEcran, y);
        } else {
            context.drawImage(charlotte, xEcran, y);
        }
        debogger(context,camera);
    }


    public void drawBlessee(GraphicsContext context, double deltaTemps, Camera camera) {
        double xEcran = camera.calculerEcranX(x);
        if (Main.enModeDeboggage) {
            debogger(context,camera);
        }
        if (deltaTemps <= 0.25 || (deltaTemps > 0.50 && deltaTemps <= 0.75) ||
                (deltaTemps > 1 && deltaTemps <= 1.25) || (deltaTemps > 1.50 && deltaTemps <= 1.75)) {
            context.drawImage(charlotteBlesse, xEcran, y);
        }
    }

    public void testFinNiveau() {
        if (x>Main.LONGUEUR_NIVEAU-(Main.LARGEUR_FENETRE*0.8)) {
            estAlafinDuNIveau = true;
        } else {
            estAlafinDuNIveau = false;
        }
    }


    private void entreeUtilisateur() {
        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);
        boolean down = Input.isKeyPressed(KeyCode.DOWN);
        boolean up = Input.isKeyPressed(KeyCode.UP);
        if (left && !estSurExtremiteGauche()) {
            ax = -ACCELERATION;
        //} else if (right && !estSurExtremiteDroite()) {
            ax = ACCELERATION;
        } else if (down && !estSurExtremiteBasse()) {
            ay = ACCELERATION;
        } else if (up && !estSurExtremiteHaute()) {
            ay = -ACCELERATION;
        } else {
            ax = 0;
            ay = 0;
        }
    }

    private void amortissementVitesse(double deltaTemps) {
        int signeVitesse = vx > 0 ? 1 : -1;
        double vitesseAmortissementX = -signeVitesse * 500;
        vx += deltaTemps * vitesseAmortissementX;
        int nouveauSigneVitesse = vx > 0 ? 1 : -1;
        if (nouveauSigneVitesse != signeVitesse) {
            vx = 0;
        }
        if (vx > VITESSE_MAXIMALE)
            vx = VITESSE_MAXIMALE;
        else if (vx < -VITESSE_MAXIMALE)
            vx = -VITESSE_MAXIMALE;
        int signeVitessey = vy > 0 ? 1 : -1;
        double vitesseAmortissementy = -signeVitessey * 500;
        vy += deltaTemps * vitesseAmortissementy;
        int nouveauSigneVitessey = vy > 0 ? 1 : -1;
        if (nouveauSigneVitessey != signeVitessey) {
            vy = 0;
        }
        if (vy > VITESSE_MAXIMALE)
            vy = VITESSE_MAXIMALE;
        else if (vy < -VITESSE_MAXIMALE)
            vy = -VITESSE_MAXIMALE;
    }

    private void regarderExtremites() {
        if (estSurExtremiteGauche()) {
            vx = 0;
            ax = 0;
        }
       // if (estSurExtremiteDroite()) {
            vx = 0;
            ax = 0;
        if (estSurExtremiteHaute()) {
            vy = 0;
            ay = 0;
        }
        if (estSurExtremiteBasse()) {
            vy = 0;
            ay = 0;
        }
    }

    @Override
    public void update(double deltaTemps) {
        testFinNiveau();
        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);
        boolean down = Input.isKeyPressed(KeyCode.DOWN);
        boolean up = Input.isKeyPressed(KeyCode.UP);


        if (left) {
            ax = -ACCELERATION;
        } else if (right) {
            ax = ACCELERATION;
        } else {
            ax = -vx * 10;


        }
        if (up) {
            ay = -ACCELERATION;
        } else if (down) {
            ay = ACCELERATION;
        } else {
            ay = -vy * 10;
        }
        if (vx > VITESSE_MAXIMALE) {
            vx = VITESSE_MAXIMALE;
        } else if (vx < -VITESSE_MAXIMALE) {
            vx = -VITESSE_MAXIMALE;
        }
        if (vy > VITESSE_MAXIMALE) {
            vy = VITESSE_MAXIMALE;
        } else if (vy < -VITESSE_MAXIMALE) {
            vy = -VITESSE_MAXIMALE;
        }
        updatePhysique(deltaTemps);
         if(getGauche()-camera.getX()<0){
            x=camera.getX();
        }
         if(getDroite()>Main.LONGUEUR_NIVEAU){
             finiNiveau=true;
            x=Main.LONGUEUR_NIVEAU-w;
        }
        System.out.println(x+w);
         y = Math.min(Math.max(y, 0), Main.HAUTEUR_FENETRE - h);
    }
}
