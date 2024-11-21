package ca.qc.bdeb.sim203.charlotteLaBarbotte;

import javafx.scene.canvas.GraphicsContext;

public class Projectile extends  ObjetsDuJeu{

    @Override
    public void draw(GraphicsContext context,Camera camera) {
            if(Main.enModeDeboggage){
                debogger(context,camera);
            }
    }
    public void calculerForceElectrique(PoissonEnnemi ennemi){

    }

}
