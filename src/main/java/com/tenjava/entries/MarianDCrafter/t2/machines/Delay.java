package com.tenjava.entries.MarianDCrafter.t2.machines;

/**
 * Represents a delay.
 */
public enum Delay {

    /**
     * One second, which are 20 ticks.
     */
    SECOND(20),
    /**
     * One minute, which are 1200 ticks.
     */
    MINUTE(1200),
    /**
     * One hour, which are 72000 ticks.
     */
    HOUR(72000);

    private int ticks;

    Delay(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Returns the ticks every delay.
     * @return the ticks every delay
     */
    public int getTicks() {
        return ticks;
    }

}
