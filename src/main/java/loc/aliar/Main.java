package loc.aliar;


import loc.aliar.controller.BaseController;
import loc.aliar.controller.Controller;
import loc.aliar.model.colony.Colony;
import loc.aliar.model.colony.bacteria.BacteriaColony;
import loc.aliar.model.game.Game;
import loc.aliar.model.game.LifeGame;
import loc.aliar.view.Window;

public class Main {

    public static void main(String[] args) {
        Game game;

        if (args.length == 3) {
            Colony colony = new BacteriaColony(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1])
            );
            game = new LifeGame(colony, Integer.parseInt(args[2]));
        } else {
            game = new LifeGame(new BacteriaColony());
        }

        Controller controller = new BaseController(game);
        new Window(controller).setVisible(true);
    }
}



