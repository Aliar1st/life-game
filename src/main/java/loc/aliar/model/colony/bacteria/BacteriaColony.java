package loc.aliar.model.colony.bacteria;

import loc.aliar.model.colony.Colony;
import loc.aliar.model.colony.Death;
import loc.aliar.model.colony.Life;

import java.util.*;
import java.util.concurrent.*;

public class BacteriaColony implements Colony {

    private static final int NEIGHBOUR_0_SHIFT = -1;
    private static final int NEIGHBOUR_1_SHIFT = 2;

    private static final int EXPAND_SIZE = 10;

    private ExecutorService executorService
            = Executors.newFixedThreadPool(8);

    private Life life = new BacteriaLife(this);
    private Death death = new BacteriaDeath(this);

    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private List<List<Boolean>> field;

    public BacteriaColony() {
        init();
    }

    public BacteriaColony(int width, int height) {
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        field = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            ArrayList<Boolean> row = new ArrayList<>();
            field.add(row);

            for (int j = 0; j < width; j++) {
                row.add(false);
            }
        }
    }

    @Override
    public void evolve() throws ExecutionException, InterruptedException {
        Future<boolean[][]> lifeFuture = executorService.submit(life);
        Future<boolean[][]> deathFuture = executorService.submit(death);

        boolean[][] lifeChanged = lifeFuture.get();
        boolean[][] deathChanged = deathFuture.get();

        //async changing
        CountDownLatch countDownLatch = new CountDownLatch(height);

        for (int i = 0; i < height; i++) {
            final int i1 = i;
            executorService.submit(() -> {
                for (int j = 0; j < width; j++) {
                    field.get(i1).set(j,
                            (lifeChanged[i1][j] || deathChanged[i1][j])
                                    != field.get(i1).get(j));
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        //        sync changing
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                field.get(i).set(j,
//                        (lifeChanged[i][j] || deathChanged[i][j])
//                                != field.get(i).get(j));
//            }
//        }
    }

    @Override
    public boolean fit() {
        if (field.size() == 0 || field.get(0).size() == 0) return false;

        boolean fit = false;

        for (boolean b : field.get(0)) {
            if (b) {
                expandNorth(EXPAND_SIZE);
                fit = true;
                break;
            }
        }

        for (List<Boolean> row : field) {
            if (row.get(width - 1)) {
                expandEast(EXPAND_SIZE);
                fit = true;
                break;
            }
        }

        for (boolean b : field.get(height - 1)) {
            if (b) {
                expandSouth(EXPAND_SIZE);
                fit = true;
                break;
            }
        }

        for (List<Boolean> lb : field) {
            if (lb.get(0)) {
                expandWest(EXPAND_SIZE);
                fit = true;
                break;
            }
        }

        return fit;
    }

    private void expandNorth(int value) {
        field.addAll(0, createRows(value, width));
        height += value;
    }

    private void expandEast(int value) {
        for (List<Boolean> row : field) {
            row.addAll(createRow(value));
        }
        width += value;
    }

    private void expandSouth(int value) {
        field.addAll(createRows(value, width));
        height += value;
    }

    private void expandWest(int value) {
        for (List<Boolean> row : field) {
            row.addAll(0, createRow(value));
        }
        width += value;
    }

    private List<Boolean> createRow(int count) {
        List<Boolean> row = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            row.add(false);
        }

        return row;
    }

    private List<List<Boolean>> createRows(int count, int width) {
        List<List<Boolean>> rows = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            List<Boolean> row = new ArrayList<>();
            rows.add(row);
            for (int j = 0; j < width; j++) {
                row.add(false);
            }
        }

        return rows;
    }

    @Override
    public Colony clear() {
        for (List<Boolean> row : field) {
            for (int j = 0; j < width; j++) {
                row.set(j, false);
            }
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        for (List<Boolean> row : field) {
            for (boolean col : row) {
                if (col) return false;
            }
        }

        return true;
    }

    @Override
    public BacteriaColony randomFill() {
        boolean isFilled = false;

        do {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    boolean tmp = ThreadLocalRandom.current().nextBoolean();
                    field.get(i).set(j, tmp);
                    if (tmp) {
                        isFilled = true;
                    }
                }
            }
        } while (!isFilled);

        return this;
    }

    @Override
    public Colony revertCell(int i, int j) {
        field.get(i).set(j, !field.get(i).get(j));
        return this;
    }

    @Override
    public int neighbours(int i, int j) {
        if (i >= height || j >= width) return -1;

        int quantity = 0;

        //determine neighbors' coordinates
        int y0 = i + NEIGHBOUR_0_SHIFT;
        int y1 = i + NEIGHBOUR_1_SHIFT;
        int x0 = j + NEIGHBOUR_0_SHIFT;
        int x1 = j + NEIGHBOUR_1_SHIFT;

        y0 = Math.max(y0, 0);
        y1 = Math.min(y1, height);
        x0 = Math.max(x0, 0);
        x1 = Math.min(x1, width);

        for (int k = y0; k < y1; k++) {
            for (int l = x0; l < x1; l++) {
                if (field.get(k).get(l) && !(k == i && l == j)) {
                    quantity++;
                }
            }
        }

        return quantity;
    }

    @Override
    public List<List<Boolean>> getField() {
        return Collections.unmodifiableList(field);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        init();
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        init();
    }

    @Override
    public Death getDeath() {
        return death;
    }

    @Override
    public void setDeath(Death death) {
        this.death = death;
    }

    @Override
    public Life getLife() {
        return life;
    }

    @Override
    public void setLife(Life life) {
        this.life = life;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // X - bacterium, O - void
                sb.append(field.get(i).get(j) ? 'X' : 'O').append(' ');
            }
            sb.append('\n');
        }

        return sb.toString();
    }
}
