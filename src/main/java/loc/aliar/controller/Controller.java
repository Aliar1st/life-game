package loc.aliar.controller;

import loc.aliar.model.game.Game;

public interface Controller {

    void start();

    void stop();

    void clear();

    boolean isBusy(int i, int j);
    
    void click(int i, int j);

    void setGameListener(Game.GameListener gameListener);

    int getHeight();

    int getWidth();

    int getAge();

    int getCloseness();

    int getLoneliness();

    int getStepCount();

    int getStepDelay();

    boolean isExpanded();
    
    void setWidth(int width);
    
    void setHeight(int height);
    
    void setAge(int age);
    
    void setCloseness(int closeness);

    void setLoneliness(int loneliness);
    
    void setStepCount(int stepCount);
    
    void setStepDelay(int stepDelay);
    
    void setExpanded(boolean expanded);
}
