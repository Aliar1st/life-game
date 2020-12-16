package loc.aliar.controller;

import loc.aliar.model.game.Game;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BaseController implements Controller {

    private Game game;
    private final Executor executor
            = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public BaseController(Game game) {
        this.game = game;
    }

    @Override
    public void start() {
        executor.execute(game);
    }

    @Override
    public void stop() {
        executor.execute(game::stop);
    }

    @Override
    public void clear() {
        executor.execute(game::clear);
    }

    @Override
    public boolean isBusy(int i, int j) {
        return game.getColony().getField().get(i).get(j);
    }

    @Override
    public void click(int i, int j) {
        game.getColony().revertCell(i, j);
    }

    @Override
    public void setGameListener(Game.GameListener gameListener) {
        game.setGameListener(gameListener);
    }

    @Override
    public int getHeight() {
        return game.getColony().getHeight();
    }

    @Override
    public int getWidth() {
        return game.getColony().getWidth();
    }

    @Override
    public int getAge() {
        return game.getProperties().getAge();
    }

    @Override
    public int getCloseness() {
        return game.getColony().getDeath().getCloseness();
    }

    @Override
    public int getLoneliness() {
        return game.getColony().getDeath().getLoneliness();
    }

    @Override
    public int getStepCount() {
        return game.getProperties().getStepCount();
    }

    @Override
    public int getStepDelay() {
        return game.getProperties().getStepDelay();
    }

    @Override
    public boolean isExpanded() {
        return game.getProperties().isExpanded();
    }

    @Override
    public void setWidth(int width) {
        if (game.getColony().getWidth() != width) {
            game.getColony().setWidth(width);
        }
    }

    @Override
    public void setHeight(int height) {
        if (game.getColony().getHeight() != height) {
            game.getColony().setHeight(height);
        }
    }

    @Override
    public void setAge(int age) {
        game.getProperties().setAge(age);
    }

    @Override
    public void setCloseness(int closeness) {
        game.getColony().getDeath().setCloseness(closeness);
    }

    @Override
    public void setLoneliness(int loneliness) {
        game.getColony().getDeath().setLoneliness(loneliness);
    }

    @Override
    public void setStepCount(int stepCount) {
        game.getProperties().setStepCount(stepCount);
    }

    @Override
    public void setStepDelay(int stepDelay) {
        game.getProperties().setStepDelay(stepDelay);
    }

    @Override
    public void setExpanded(boolean expanded) {
        game.getProperties().setExpanded(expanded);
    }
}
