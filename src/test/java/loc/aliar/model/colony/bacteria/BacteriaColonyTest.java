package loc.aliar.model.colony.bacteria;

import loc.aliar.model.colony.Colony;
import loc.aliar.model.colony.Death;
import loc.aliar.model.colony.Life;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BacteriaColonyTest {

    @Mock
    private Death death;

    @Mock
    private Life life;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCallLifeAndDeathStep() throws Exception {
        Colony colony = new BacteriaColony(0, 0);
        colony.setDeath(death);
        colony.setLife(life);

        colony.evolve();

        verify(death).call();
        verify(life).call();
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldThrowExecutionException() throws Exception {
        Colony colony = new BacteriaColony(0, 0);
        when(death.call()).thenThrow(NullPointerException.class);

        colony.setDeath(death);
        colony.setLife(life);

        assertThrows(ExecutionException.class, colony::evolve);
    }

    @Test
    void shouldExpandFieldIfEdgeContainTrue() {
        Colony colony = new BacteriaColony(5, 5).revertCell(0, 2);
        assertTrue(colony.fit());
        assertEquals(colony.getHeight(), 6);
        assertEquals(colony.getWidth(), 5);

        colony = new BacteriaColony(5, 5).revertCell(4, 2);
        assertTrue(colony.fit());
        assertEquals(colony.getHeight(), 6);
        assertEquals(colony.getWidth(), 5);

        colony = new BacteriaColony(5, 5).revertCell(2, 0);
        assertTrue(colony.fit());
        assertEquals(colony.getHeight(), 5);
        assertEquals(colony.getWidth(), 6);

        colony = new BacteriaColony(5, 5).revertCell(2, 4);
        assertTrue(colony.fit());
        assertEquals(colony.getHeight(), 5);
        assertEquals(colony.getWidth(), 6);
    }

    @Test
    void shouldNotExpandFieldIfEdgeNotContainTrue() {
        Colony colony = new BacteriaColony();
        int height = colony.getHeight();
        int width = colony.getWidth();

        assertFalse(colony.fit());
        assertEquals(height, colony.getHeight());
        assertEquals(width, colony.getWidth());

        colony = new BacteriaColony(0, 0);
        assertFalse(colony.fit());
        assertEquals(colony.getHeight(), 0);
        assertEquals(colony.getHeight(), 0);

        colony = new BacteriaColony(5, 5);
        assertFalse(colony.fit());
        assertEquals(colony.getHeight(), 5);
        assertEquals(colony.getHeight(), 5);
    }

    @RepeatedTest(5)
    void shouldClearField() {
        assertTrue(new BacteriaColony()
                .randomFill()
                .clear()
                .isEmpty()
        );
        assertTrue(new BacteriaColony(5, 5)
                .randomFill()
                .clear()
                .isEmpty()
        );
    }

    @RepeatedTest(5)
    void shouldCheckEmptyField() {
        assertTrue(new BacteriaColony().isEmpty());
        assertTrue(new BacteriaColony(5, 5).isEmpty());
    }

    @RepeatedTest(5)
    void shouldRandomFillField() {
        assertFalse(new BacteriaColony(5, 5).randomFill().isEmpty());
    }

    @RepeatedTest(5)
    void shouldRevertCellValue() {
        Colony colony = new BacteriaColony(5, 5).randomFill();
        boolean val = colony.getField().get(2).get(2);
        boolean newVal = colony.revertCell(2, 2).getField().get(2).get(2);

        assertNotEquals(val, newVal);
    }

    @ParameterizedTest
    @MethodSource("shouldCalculateNeighboursQuantityProvider")
    void shouldCalculateNeighboursQuantity(LinkedList<LinkedList<Boolean>> field, int[][] neighbours) {
        Colony colony = new BacteriaColony(5, 5);
        colony.getDeath().setLoneliness(2);
        colony.getDeath().setCloseness(4);
        colony.getLife().setBirth(3);
        Whitebox.setInternalState(colony, "field", field);

        for (int i = 0; i < neighbours.length; i++) {
            for (int j = 0; j < neighbours[0].length; j++) {
                assertEquals(colony.neighbours(i, j), neighbours[i][j]);
            }
        }
    }

    private static Stream<Arguments> shouldCalculateNeighboursQuantityProvider() {
        return Stream.of(
                Arguments.arguments(
                        new LinkedList<>(Arrays.asList(
                                new LinkedList<>(Arrays.asList(true, false, true, false, true)),
                                new LinkedList<>(Arrays.asList(false, true, false, true, false)),
                                new LinkedList<>(Arrays.asList(true, false, true, false, true)),
                                new LinkedList<>(Arrays.asList(false, true, false, true, false)),
                                new LinkedList<>(Arrays.asList(true, false, true, false, true))
                        )),
                        new int[][] {
                                {1, 3, 2, 3, 1},
                                {3, 4, 4, 4, 3},
                                {2, 4, 4, 4, 2},
                                {3, 4, 4, 4, 3},
                                {1, 3, 2, 3, 1}
                        }
                ),
                Arguments.arguments(
                        new LinkedList<>(Arrays.asList(
                                new LinkedList<>(Arrays.asList(true, true, true, true, true)),
                                new LinkedList<>(Arrays.asList(true, true, true, true, true)),
                                new LinkedList<>(Arrays.asList(true, true, true, true, true)),
                                new LinkedList<>(Arrays.asList(true, true, true, true, true)),
                                new LinkedList<>(Arrays.asList(true, true, true, true, true))
                        )),
                        new int[][] {
                                {3, 5, 5, 5, 3},
                                {5, 8, 8, 8, 5},
                                {5, 8, 8, 8, 5},
                                {5, 8, 8, 8, 5},
                                {3, 5, 5, 5, 3}
                        }
                ),
                Arguments.arguments(
                        new LinkedList<>(Arrays.asList(
                                new LinkedList<>(Arrays.asList(false, false, false, false, false)),
                                new LinkedList<>(Arrays.asList(false, false, false, false, false)),
                                new LinkedList<>(Arrays.asList(false, false, false, false, false)),
                                new LinkedList<>(Arrays.asList(false, false, false, false, false)),
                                new LinkedList<>(Arrays.asList(false, false, false, false, false))
                        )),
                        new int[][] {
                                {0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0}
                        }
                )
        );
    }
}