package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.skin.CellSkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;

public class Partie {
    private Personnage personnage;
    private double xDecors;private Image iconeEtoile=new Image("code/etoile.png");

    private Image iconeHippocampe=new Image("code/hippocampe.png");

    private Image iconeSardine=new Image("code/sardines.png");



    private Pane root;

    private ArrayList<Decor> decors;

    public boolean isPartieTerminee() {
        return partieTerminee;
    }


    private  boolean partieTerminee=false;
    private Baril baril;
    private Camera camera;
    private double timerAtteint;
    private boolean personnageInvincible = false;
    private final double TEMPS_IMPACT = 2;

    private boolean estEToile;

    private boolean estSardine = false;

    private boolean estHippocampe = false;

    private final double LONGUEUR_DECOR = 80;
    private double tempsPoisson;
    private double tempsNiveau;
    private double tempsProjectile;
    private ArrayList<PoissonEnnemi> poissonsEnnemis;
    private ArrayList<Projectile> projectiles;
    private Color color;
    private Random random = new Random();

    private BarreDeVie barreDeVie;
    private int numeroNiveau;


    Partie(Pane root) {
        this.root=root;
        creerBackGround(root);
        estEToile= true;
        decors = new ArrayList<>();
        creerDecor();
        camera = new Camera();
        baril = new Baril();
        barreDeVie = new BarreDeVie();
        personnage = new Personnage(camera);
        poissonsEnnemis = new ArrayList<>();
        projectiles = new ArrayList<>();
        numeroNiveau = 1;
        color = Color.hsb(random.nextInt(170, 280), 0.84, 1);
    }
    public void afficherIconeProjectile(GraphicsContext context){
        if(estEToile){
            context.drawImage(iconeEtoile,200,5);
        }
        else if(estHippocampe){
            context.drawImage(iconeHippocampe,200,5);
        }
        else{
            context.drawImage(iconeSardine,200,5);
        }
    }
    public  void deboggageEtoile(){
        estEToile=true;
        estHippocampe=false;
        estSardine=false;
    }

    public  void deboggageHippocampe(){
        estEToile=false;
        estHippocampe=true;
        estSardine=false;
    }

    public  void deboggageSardine(){
        estEToile=false;
        estHippocampe=false;
        estSardine=true;
    }
    public void deboggageNbDeVies(){
        personnage.setNbdeVies(personnage.getVIES_MAX());
        barreDeVie.setNbDeViePoissons(personnage.getVIES_MAX());

    }

    public void deboggageChangerNiveau(){
        changerNiveau(root);
    }

    public void creerDecor() {
        decors.add(new Decor());
        while (xDecors < Main.LONGUEUR_NIVEAU) {
            double x = random.nextDouble(50, 100);
            xDecors += x + LONGUEUR_DECOR;// largeur du decor(mettre plus beau)
            var decor = new Decor();
            decor.setX(xDecors);
            decors.add(decor);
        }
    }

    public void deboggage(GraphicsContext context) {
        if (Main.enModeDeboggage) {
            context.setFont(Font.font(20));
            context.setFill(Color.WHITE);
            context.fillText("Nb Poissons: " + poissonsEnnemis.size(), 20, 70);
            context.fillText("Nb Projectiles: " + projectiles.size(), 20, 90);
            context.fillText("Position Charlotte : " + (personnage.getDroite()/ Main.LONGUEUR_NIVEAU) * 100 + "%",
                    20, 110);
        }
        }
    public void changerNiveau(Pane root){
        tempsNiveau=0;
        tempsProjectile=0;
        creerBackGround(root);
        decors.clear();
        xDecors=0;
        creerDecor();
        poissonsEnnemis.clear();
        projectiles.clear();
        numeroNiveau+=1;
        personnage.setFiniNiveau(false);
        personnage.commenceNiveau();
        camera.setX(0);
        baril = new Baril();

    }


    public void update(double deltaTemps) {
        personnage.update(deltaTemps);
        if(personnage.isFiniNiveau()){
            changerNiveau(root);
        }
        camera.suivre(personnage, deltaTemps, personnage.vx);
        tempsNiveau += deltaTemps;
        baril.updateTemps(tempsNiveau);
        if (personnage.estEnCollision(baril) && !baril.isEstOuvert()) {
            baril.setEstOuvert(true);
            int aleatoire = baril.genererProjectile();
            changerProjectile(aleatoire);

        }
        baril.update(deltaTemps);
        if (personnageInvincible) {
            timerAtteint += deltaTemps;
            if (timerAtteint >= TEMPS_IMPACT) {
                personnageInvincible = false;
            }
        } else {
            timerAtteint = 0;
        }
        double nbSecondes = 0.75 + 1 / Math.sqrt(numeroNiveau);

        if (System.currentTimeMillis() - tempsPoisson > nbSecondes * 1000) {
            tempsPoisson = System.currentTimeMillis();
            creerPoissonEnnemis(random.nextInt(5) + 1, camera);
        }
        for (int i = 0; i < poissonsEnnemis.size(); i++) {
            if (poissonsEnnemis.get(i).sortDeLaScene(camera)) {
                poissonsEnnemis.remove(poissonsEnnemis.get(i));
                i--;
            } else {
                poissonsEnnemis.get(i).update(deltaTemps);
                if (!personnageInvincible && poissonsEnnemis.get(i).estEnCollision(personnage)) {
                    barreDeVie.perdUneVie();
                    personnage.perdreVie();
                    personnageInvincible = true;
                    if(personnage.isEstMort()){
                        partieTerminee=true;
                    }
                }
                for (int k = 0; k < projectiles.size(); k++) {
                    if (i < poissonsEnnemis.size()) {
                        if (poissonsEnnemis.get(i).estEnCollision(projectiles.get(k))) {
                            poissonsEnnemis.remove(poissonsEnnemis.get(i));
                        }
                    }
                }
            }
        }
        if (Input.isKeyPressed(KeyCode.SPACE) && (tempsNiveau - tempsProjectile) >= 0.5) {
            ArrayList<Projectile> projectilesCrees = new ArrayList<>();
            if (estEToile) {
                projectilesCrees.add(new EtoileDeMer());
            } else if (estHippocampe) {
                for (int i = 0; i < 3; i++) {
                    projectilesCrees.add(new Hippocampe(personnage.getHaut()));
                }

            } else {
                projectilesCrees.add(new Sardine());
            }
            for (int i = 0; i < projectilesCrees.size(); i++) {
                projectilesCrees.get(i).setX(personnage.getGauche() + personnage.getW() / 2);
                projectilesCrees.get(i).setY(personnage.getHaut() + personnage.getH() / 2);
                projectiles.add(projectilesCrees.get(i));
            }
            tempsProjectile = tempsNiveau;
        }
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).sortDeLaScene(camera)) {
                projectiles.remove(projectiles.get(i));
                i--;
            } else {
                for(int k = 0; k < poissonsEnnemis.size(); k++) {
                    if (poissonsEnnemis.get(k).getGauche() > projectiles.get(i).getGauche()) {
                        projectiles.get(i).calculerForceElectrique(poissonsEnnemis.get(k));
                    }
                }
                projectiles.get(i).update(deltaTemps);
            }
        }

    }

    public void creerPoissonEnnemis(int nbPoisson, Camera camera) {
        for (int i = 0; i < nbPoisson; i++) {
            poissonsEnnemis.add(new PoissonEnnemi(numeroNiveau, camera));
        }
    }

    public void draw(GraphicsContext context) {
        context.clearRect(0, 0, Main.LARGEUR_FENETRE, Main.HAUTEUR_FENETRE);
        if(tempsNiveau<3){
            context.setFont(Font.font(70));
            context.fillText("Niveau "+numeroNiveau,300,Main.HAUTEUR_FENETRE/2);
        }
        if(partieTerminee){
            context.setFont(Font.font(70));
            context.setFill(Color.RED);
            context.fillText("Fin de partie",250,Main.HAUTEUR_FENETRE/2);
        }
        if (personnageInvincible) {
            personnage.drawBlessee(context, timerAtteint, camera);
        } else {
            personnage.draw(context, camera);
        }
        deboggage(context);
        barreDeVie.draw(context, camera);
        afficherIconeProjectile(context);
        baril.draw(context, camera);
        for (PoissonEnnemi poissonEnnemi : poissonsEnnemis) {
            poissonEnnemi.draw(context, camera);
        }
        for (Projectile projectile : projectiles) {
            projectile.draw(context, camera);
        }
        for (Decor decor : decors) {
            decor.draw(context, camera);
        }


    }

    public void changerProjectile(int chiffreAleatoire) {
        if (estEToile) {
            estEToile = false;
            if (chiffreAleatoire == 1) {
                estHippocampe = true;
            } else estSardine = true;
        } else if (estHippocampe) {
            estHippocampe = false;
            if (chiffreAleatoire == 1) {
                estEToile = true;
            } else estSardine = true;
        } else {
            estSardine = false;
            if (chiffreAleatoire == 1) {
                estEToile = true;
            } else estHippocampe = true;
        }

    }

    public void creerBackGround(Pane root) {
        BackgroundFill background_fill = new BackgroundFill(Color.hsb(random.nextInt(170, 280), 0.84, 1),
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        root.setBackground(background);

    }

}