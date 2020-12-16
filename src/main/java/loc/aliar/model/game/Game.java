package loc.aliar.model.game;

import loc.aliar.model.colony.Colony;

public interface Game extends Runnable {

    void stop();

    void clear();

    Colony getColony();

    Properties getProperties();

    void setProperties(Properties properties);

    void setGameListener(GameListener gameListener);

    interface GameListener {

        void onStart();

        void onStep(int step);

        void onStop();

        void onClear();

        void onFieldChange(int width, int height);
    }
}
