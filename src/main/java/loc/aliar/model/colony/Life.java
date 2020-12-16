package loc.aliar.model.colony;

import java.util.concurrent.Callable;

public interface Life extends Callable<boolean[][]> {

    int DEFAULT_BIRTH = 3;

    int getBirth();

    void setBirth(int birth);
}
