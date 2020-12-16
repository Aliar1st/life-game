package loc.aliar.model.colony.bacteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BacteriaLifeTest {

    private BacteriaColony colony;
    private BacteriaLife bacteriaLife;

    @BeforeEach
    void setUp() {
        colony = mock(BacteriaColony.class);
        when(colony.getHeight()).thenReturn(5);
        when(colony.getWidth()).thenReturn(5);

        bacteriaLife = new BacteriaLife(colony);
        bacteriaLife.setBirth(3);
    }

    @ParameterizedTest
    @MethodSource("shouldCalculateFieldChangeProvider")
    void shouldCalculateFieldLifeChange(List<List<Boolean>> field, int[][] neighbours, boolean[][] result) {

        when(colony.getField()).thenReturn(field);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                when(colony.neighbours(i, j)).thenReturn(neighbours[i][j]);
            }
        }

        assertArrayEquals(result, bacteriaLife.call());
    }

    private static Stream<Arguments> shouldCalculateFieldChangeProvider() {
        return Stream.of(
                Arguments.arguments(
                        Arrays.asList(
                                Arrays.asList(true, false, true, false, true),
                                Arrays.asList(false, true, false, true, false),
                                Arrays.asList(true, false, true, false, true),
                                Arrays.asList(false, true, false, true, false),
                                Arrays.asList(true, false, true, false, true)
                        ), new int[][]{
                                {1, 3, 2, 3, 1},
                                {3, 4, 4, 4, 3},
                                {2, 4, 4, 4, 2},
                                {3, 4, 4, 4, 3},
                                {1, 3, 2, 3, 1},
                        }, new boolean[][]{
                                {false, true, false, true, false},
                                {true, false, false, false, true},
                                {false, false, false, false, false},
                                {true, false, false, false, true},
                                {false, true, false, true, false},
                        }),
                Arguments.arguments(
                        Arrays.asList(
                                Arrays.asList(true, true, true, true, true),
                                Arrays.asList(true, true, true, true, true),
                                Arrays.asList(true, true, true, true, true),
                                Arrays.asList(true, true, true, true, true),
                                Arrays.asList(true, true, true, true, true)
                        ), new int[][]{
                                {3, 5, 5, 5, 3},
                                {5, 8, 8, 8, 5},
                                {5, 8, 8, 8, 5},
                                {5, 8, 8, 8, 5},
                                {3, 5, 5, 5, 3},
                        }, new boolean[][]{
                                {false, false, false, false, false},
                                {false, false, false, false, false},
                                {false, false, false, false, false},
                                {false, false, false, false, false},
                                {false, false, false, false, false},
                        }),
                Arguments.arguments(
                        Arrays.asList(
                                Arrays.asList(false, false, false, false, false),
                                Arrays.asList(false, false, false, false, false),
                                Arrays.asList(false, false, false, false, false),
                                Arrays.asList(false, false, false, false, false),
                                Arrays.asList(false, false, false, false, false)
                        ), new int[][]{
                                {0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0},
                        }, new boolean[][]{
                                {false, false, false, false, false},
                                {false, false, false, false, false},
                                {false, false, false, false, false},
                                {false, false, false, false, false},
                                {false, false, false, false, false},
                        })
        );
    }
}