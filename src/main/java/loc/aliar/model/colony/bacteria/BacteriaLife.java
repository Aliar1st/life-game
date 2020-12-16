package loc.aliar.model.colony.bacteria;

import loc.aliar.model.colony.Colony;
import loc.aliar.model.colony.Life;

class BacteriaLife implements Life {

    private int birth = DEFAULT_BIRTH;

    private final Colony colony;

    BacteriaLife(Colony colony) {
        this.colony = colony;
    }

    @Override
    public boolean[][] call() {
        boolean[][] changed =
                new boolean[colony.getHeight()][colony.getWidth()];

        for (int i = 0; i < colony.getHeight(); i++) {
            for (int j = 0; j < colony.getWidth(); j++) {
                changed[i][j] =
                        !colony.getField().get(i).get(j)
                                && colony.neighbours(i, j) == birth;
            }
        }

        return changed;
    }

    @Override
    public int getBirth() {
        return birth;
    }

    @Override
    public void setBirth(int birth) {
        this.birth = birth;
    }
}
