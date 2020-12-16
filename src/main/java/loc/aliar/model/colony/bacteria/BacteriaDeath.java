package loc.aliar.model.colony.bacteria;

import loc.aliar.model.colony.Colony;
import loc.aliar.model.colony.Death;

class BacteriaDeath implements Death {

    private int closeness = DEFAULT_CLOSENESS;
    private int loneliness = DEFAULT_LONELINESS;

    private final Colony colony;

    BacteriaDeath(Colony colony) {
        this.colony = colony;
    }

    @Override
    public boolean[][] call() {
        boolean[][] changed =
                new boolean[colony.getHeight()][colony.getWidth()];

        for (int i = 0; i < colony.getHeight(); i++) {
            for (int j = 0; j < colony.getWidth(); j++) {
                int neighbours = colony.neighbours(i, j);
                changed[i][j] =
                        colony.getField().get(i).get(j)
                                && (neighbours < loneliness
                                || neighbours > closeness);
            }
        }
        return changed;
    }

    @Override
    public int getCloseness() {
        return closeness;
    }

    @Override
    public void setCloseness(int closeness) {
        this.closeness = closeness;
    }

    @Override
    public int getLoneliness() {
        return loneliness;
    }

    @Override
    public void setLoneliness(int loneliness) {
        this.loneliness = loneliness;
    }
}
