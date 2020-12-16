package loc.aliar.model.game;

import loc.aliar.model.colony.Colony;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;

public class LifeGame implements Game {

    private Logger log = Logger.getLogger(LifeGame.class.getName());

    private boolean isRunning;
    private Properties properties = new Properties();
    private Game.GameListener gameListener;
    private Colony colony;

    public LifeGame(Colony colony) {
        this.colony = colony;
    }

    public LifeGame(Colony colony, int age) {
        this.colony = colony;
        properties.setAge(age);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void run() {
        if (gameListener != null) {
            gameListener.onStart();
        }
        isRunning = true;

        if (colony.isEmpty()) {
            colony.randomFill();
        }

        evolution:
        for (int i = 0; (properties.getAge() == 0 || i < properties.getAge()) && isRunning; ) {
            try {
                for (int j = 0; j < properties.getStepCount(); j++, i++) {

                    if (properties.getAge() != 0 && i >= properties.getAge()) {
                        break evolution;
                    }

                    colony.evolve();

                    if (properties.isExpanded() && colony.fit() && gameListener != null) {
                        gameListener.onFieldChange(colony.getWidth(), colony.getHeight());
                    }
                }
                if (gameListener != null) {
                    gameListener.onStep(i);
                }
                if (colony.isEmpty()) {
                    stop();
                    return;
                }

                Thread.sleep(properties.getStepDelay());
            } catch (InterruptedException
                    | ExecutionException
                    | RejectedExecutionException e) {
                log.throwing("LifeGame", "run", e);
                break;
            }
        }
        stop();
    }

    @Override
    public void stop() {
        if (isRunning) {
            isRunning = false;
            if (gameListener != null) {
                gameListener.onStop();
            }
            log.info("LifeGame has been stopped");
        }
    }

    @Override
    public void clear() {
        colony.clear();
        if (gameListener != null) {
            gameListener.onClear();
        }
    }

    @Override
    public Colony getColony() {
        return colony;
    }

    @Override
    public void setGameListener(Game.GameListener gameListener) {
        this.gameListener = gameListener;
    }
}
