package ca.qc.bdeb.sim203.charlotteLaBarbotte;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {

    public static  boolean enModeDeboggage=false;
    public static final double HAUTEUR_FENETRE = 520;
    public static final double LARGEUR_FENETRE = 900;

    public static final double LONGUEUR_NIVEAU=7200;

    private Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(ecranDAcceuil(stage));
        stage.setResizable(false);
        stage.setTitle("Charlotte la Barbotte");
        stage.show();
    }

    public Scene sceneJeu(Stage stage) {
        var root = new Pane();
        var sceneJeu = new Scene(root, LARGEUR_FENETRE, HAUTEUR_FENETRE);
        var canvas = new Canvas(LARGEUR_FENETRE, HAUTEUR_FENETRE);
        root.getChildren().add(canvas);
        var context = canvas.getGraphicsContext2D();
        Partie partie = new Partie(root);
       toucheAppuyee(sceneJeu,partie,root,stage);
       var timer = new AnimationTimer() {
            long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                double deltaTemps = (now - lastTime) * 1e-9;
                lastTime = now;
                // Nettoyer le contexte
                // Mettre à jour et dessiner votre partie
                partie.update(deltaTemps);
                partie.draw(context);
            }
        };
        timer.start();
        return sceneJeu;
    }

    public void toucheAppuyee(Scene sceneJeu,Partie partie,Pane root,Stage stage){
        sceneJeu.setOnKeyPressed((e) -> {
            if(enModeDeboggage){
                if(e.getCode()==KeyCode.Q){
                    partie.deboggageEtoile();
                }
                if(e.getCode()== KeyCode.W){
                    partie.deboggageHippocampe();
                }
                if(e.getCode()==KeyCode.E){
                    partie.deboggageSardine();
                }
                if(e.getCode()==KeyCode.R){
                    partie.deboggageNbDeVies();
                }
                if(e.getCode()==KeyCode.T){
                    partie.changerNiveau(root);
                }
            }
            if (e.getCode() == KeyCode.ESCAPE || partie.isPartieTerminee()) {
                stage.setScene(ecranDAcceuil(stage));
            }
            else if (e.getCode() == KeyCode.D) {
                enModeDeboggage = !enModeDeboggage;
            }
            else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        sceneJeu.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });
    }

    public Scene ecranDAcceuil(Stage stage) {
        var root = new BorderPane();
        var scene = new Scene(root, LARGEUR_FENETRE, HAUTEUR_FENETRE);
        var imageAccueil = new ImageView("code/logo.png");
        imageAccueil.setFitWidth(400);
        imageAccueil.setFitHeight(400);
        var setup = new VBox();
        var boutons = new HBox();
        var btnJouer = new Button("Jouer !");
        var btnInfo = new Button("Infos");
        boutons.getChildren().addAll(btnJouer, btnInfo);
        boutons.setSpacing(20);
        boutons.setAlignment(Pos.CENTER);
        setup.getChildren().addAll(imageAccueil, boutons);
        setup.setAlignment(Pos.CENTER);
        root.setCenter(setup);
        root.setStyle("-fx-background-color: #2A7FFF;");
        btnInfo.setOnAction(event -> {
            stage.setScene(ecranInfo(stage));
        });
        btnJouer.setOnAction(event -> {
            stage.setScene(sceneJeu(stage));
        });
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
        return scene;
    }

    public Scene ecranInfo(Stage stage) {
        var root = new BorderPane();
        var scene = new Scene(root, LARGEUR_FENETRE, HAUTEUR_FENETRE);
        var random = new Random();
        var affichage = new VBox();
        var btnRetour = new Button("Retour");
        var nbPoisson = random.nextInt(5) + 1;
        var titre = new Text("Charlotte la Barbotte");
        affichage.setAlignment(Pos.CENTER);
        String nomPoissonEnnemi = "code/poisson" + nbPoisson + ".png";
        ImageView poissonEnnemi = new ImageView(nomPoissonEnnemi);
        affichage.setSpacing(20);
        titre.setFont(Font.font(30));
        var nomCreateurs = new Text(" Par Martial Romarik Poumale " + "\n" + " et Jérémie Lestage");
        nomCreateurs.setFont(Font.font(18));
        var description = new Text("""
                Travail remis à Nicolas Hurtubise et Georges Côté, Graphismes adaptés de https://game-icons.net/ et de 
                https://openclipart.org/. Developpe dans le cadre du cours 420-203-RE- Developpement de programmes 
                dans un environnement graphique, au Collège de Bois-de-Boulogne.""");
        description.setFont(Font.font(15));
        affichage.getChildren().addAll(titre, poissonEnnemi, nomCreateurs, description, btnRetour);
        root.setCenter(affichage);
        root.setStyle("-fx-background-color: #2A7FFF;");
        btnRetour.setOnAction(event -> {
            stage.setScene(ecranDAcceuil(stage));
        });
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.setScene(ecranDAcceuil(stage));
            }
        });
        return scene;
    }

}