package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class PoissonEnnemi extends ObjetsDuJeu {
    private final Random random = new Random();

    public double getCHARGE_ENNEMI() {
        return CHARGE_ENNEMI;
    }

    private final double CHARGE_ENNEMI=-100;
    private final double ACCELERATION = 500;
    private final Image poissonEnnemi;
    private double plageHauteur = (4.0 / 5.0 - 1.0 / 5.0) * Main.HAUTEUR_FENETRE;
    private int nbPoisson = random.nextInt(5) + 1;
    private String nomPoissonEnnemi = "code/poisson" + nbPoisson + ".png";
    private double intervalle;


    public PoissonEnnemi(int numeroNiveau,Camera camera) {
        x = Main.LARGEUR_FENETRE+camera.getX();
        y = 1.0 / 5.0 * Main.HAUTEUR_FENETRE + random.nextDouble() * plageHauteur;
        h = 50 + random.nextInt(71);
        double proportion = 120.0 / 104.0;
        w = h * proportion;
        ax = -ACCELERATION;
        calculerVX(numeroNiveau);
        vy = (random.nextInt(101) - 50) * 2;
        calculerIntervalle(numeroNiveau);
        poissonEnnemi = new Image(nomPoissonEnnemi, w, h, true, true);
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        double xEcran=camera.calculerEcranX(x);
        if(Main.enModeDeboggage){
            super.debogger(context,camera);
        }
        context.drawImage(poissonEnnemi, xEcran, y);

    }

    @Override
    public void update(double deltaTemps) {

        updatePhysique(deltaTemps);
    }

    private void calculerVX(int numeroNiveau) {
        vx = -(100 * Math.pow(numeroNiveau, 0.33) + 200);
    }

    public double calculerIntervalle(int numeroNiveau) {
        intervalle = 0.75 + 1 / Math.sqrt(numeroNiveau);
        return intervalle;
    }
}