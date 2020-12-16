package loc.aliar.model.game;

import loc.aliar.model.colony.Colony;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class LifeGameTest {

    @Mock
    private Colony colony;

    @Mock
    private Properties properties;

    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(properties.getAge()).thenReturn(1);
        when(properties.getStepDelay()).thenReturn(0);
        when(properties.getStepCount()).thenReturn(1);
        when(properties.isExpanded()).thenReturn(false);

        game = new LifeGame(colony);
        game.setProperties(properties);
    }

    @ParameterizedTest
    @MethodSource("stepsShouldBeSameAsAgeProvider")
    void stepsShouldBeSameAsAge(int age, int stepCount) {
        when(properties.getAge()).thenReturn(age);
        when(properties.getStepCount()).thenReturn(stepCount);

        game.run();

        try {
            verify(colony, times(age)).evolve();
        } catch (ExecutionException | InterruptedException e) {
            fail("Game has been interrupted", e);
        }
    }

    private static Stream<Arguments> stepsShouldBeSameAsAgeProvider() {
        return Stream.of(
                Arguments.arguments(1, 2),
                Arguments.arguments(2, 1),
                Arguments.arguments(2, 2),
                Arguments.arguments(0, 2)
        );
    }

    @Test
    void stepsShouldBeAtMostOneWhenColonyIsEmpty() {
        when(properties.getAge()).thenReturn(10);
        when(colony.isEmpty()).thenReturn(true);

        game.run();
        try {
            verify(colony, atMost(1)).evolve();
        } catch (ExecutionException | InterruptedException e) {
            fail("Game has been interrupted", e);
        }
    }

    @Test
    void colonyShouldBeFilledIfItEmpty() {
        when(colony.isEmpty()).thenReturn(true);

        game.run();

        InOrder order = inOrder(colony);
        order.verify(colony).isEmpty();
        order.verify(colony).randomFill();
    }

    @Test
    void colonyShouldNotFillIfNotEmpty() {
        when(colony.isEmpty()).thenReturn(false);

        game.run();
        verify(colony, atLeastOnce()).isEmpty();
        verify(colony, never()).randomFill();
    }

    @Test
    void colonyShouldNotBeExpanded() {
        when(properties.isExpanded()).thenReturn(true);
        game.run();
        verify(colony).fit();
    }

    @Test
    void colonyShouldBeExpanded() {
        when(properties.isExpanded()).thenReturn(false);
        game.run();
        verify(colony, never()).fit();
    }

    @Test
    void shouldCallGameEvents() {
        when(colony.fit()).thenReturn(true);
        when(properties.isExpanded()).thenReturn(true);

        Game.GameListener gameListener = mock(Game.GameListener.class);
        game.setGameListener(gameListener);

        game.run();

        InOrder order = inOrder(gameListener);
        order.verify(gameListener).onStart();
        order.verify(gameListener).onFieldChange(anyInt(), anyInt());
        order.verify(gameListener).onStep(anyInt());
        order.verify(gameListener).onStop();
    }

    @Test
    void shouldSetRunningToFalse() {
        Whitebox.setInternalState(game, "isRunning", true);
        game.stop();
        assertEquals(Whitebox.getInternalState(game, "isRunning"), false);
    }

    @Test
    void shouldCallOnStopEvent() {
        Game.GameListener gameListener = mock(Game.GameListener.class);
        game.setGameListener(gameListener);
        Whitebox.setInternalState(game, "isRunning", true);

        game.stop();
        verify(gameListener).onStop();
    }

    @Test
    void shouldCallColonyClear() {
        game.clear();
        verify(colony).clear();
    }

    @Test
    void shouldCallOnClearEvent() {
        Game.GameListener gameListener = mock(Game.GameListener.class);
        game.setGameListener(gameListener);

        game.clear();
        verify(gameListener).onClear();
    }
}