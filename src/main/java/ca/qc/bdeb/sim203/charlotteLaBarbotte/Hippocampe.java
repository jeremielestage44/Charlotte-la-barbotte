package ca.qc.bdeb.sim203.charlotteLaBarbotte;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

        public class Hippocampe extends Projectile {

            private Random random = new Random();
            private double tempsHippocampe;
            private double yInitial;
            private double amplitude;
            private double periode;

            private Image imageObjet;

            public Hippocampe(double yPersonnage) {
                w = 20;
                h = 36;
                imageObjet = new Image("code/hippocampe.png");
                vx = 500;
                tempsHippocampe = 0;
                yInitial = yPersonnage;
                double signe = (random.nextBoolean() ? 1 : -1);  // 1 pour positif, -1 pour négatif
                amplitude = signe * (random.nextDouble() * (60 - 30) + 30);
                periode = random.nextDouble() * (3.0 - 1.0 + 1.0) + 1.0;
            }

            @Override
            public void update(double deltaTemps) {
                tempsHippocampe += deltaTemps;
                y = amplitude * Math.sin(2 * Math.PI * tempsHippocampe / periode) + yInitial;
                updatePhysique(deltaTemps);
            }

            @Override
            public void draw(GraphicsContext context, Camera camera) {
                super.draw(context,camera);
                double xEcran=camera.calculerEcranX(x);
                context.drawImage(imageObjet,xEcran,y);
            }


}
