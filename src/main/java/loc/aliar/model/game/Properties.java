package loc.aliar.model.game;

import static loc.aliar.model.game.PropertiesConstant.*;

public class Properties {

    private volatile int age;
    private volatile int stepCount;
    private volatile int stepDelay;
    private volatile boolean isExpanded;

    Properties() {
        age = DEFAULT_AGE;
        stepCount = DEFAULT_STEP_COUNT;
        stepDelay = DEFAULT_STEP_DELAY;
        isExpanded = DEFAULT_EXPANDED;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getStepDelay() {
        return stepDelay;
    }

    public void setStepDelay(int stepDelay) {
        this.stepDelay = stepDelay;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "age=" + age +
                ", stepCount=" + stepCount +
                ", stepDelay=" + stepDelay +
                ", isExpanded=" + isExpanded +
                '}';
    }
}
