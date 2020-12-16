package loc.aliar.model.colony;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Colony {

    int DEFAULT_HEIGHT = 10;
    int DEFAULT_WIDTH = 10;

    void evolve() throws ExecutionException, InterruptedException;

    boolean fit();

    Colony clear();

    boolean isEmpty();

    Colony randomFill();

    Colony revertCell(int i, int j);

    int neighbours(int i, int j);

    List<? extends List<Boolean>> getField();

    int getHeight();

    void setHeight(int height);

    int getWidth();

    void setWidth(int width);

    Death getDeath();

    void setDeath(Death death);

    Life getLife();

    void setLife(Life life);
}
