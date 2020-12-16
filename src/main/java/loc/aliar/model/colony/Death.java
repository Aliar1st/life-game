package loc.aliar.model.colony;

import java.util.concurrent.Callable;

public interface Death extends Callable<boolean[][]> {

    int DEFAULT_CLOSENESS = 4;
    int DEFAULT_LONELINESS = 2;

    int getCloseness();

    int getLoneliness();

    void setCloseness(int closeness);

    void setLoneliness(int loneliness);
}
