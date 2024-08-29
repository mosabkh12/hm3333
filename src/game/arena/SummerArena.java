package game.arena;

import game.entities.IMobileEntity;
import game.enums.WeatherCondition;
import game.enums.SnowSurface;
import utilities.ValidationUtils;

/**
 * Represents a Summer Arena for competitions.
 */
public class SummerArena implements IArena {

    private double length;
    private final SnowSurface surface;
    private final WeatherCondition condition;

    /**
     * Constructor for a generic summer arena
     *
     * @param length    the length of the arena
     * @param surface   the surface of the arena
     * @param condition the weather condition in the arena
     */
    public SummerArena(double length, SnowSurface surface, WeatherCondition condition) {
        this.length = length;
        this.surface = surface;
        this.condition = condition;
    }

    @Override
    public double getFriction() {
        return surface.getFriction(); // Use the same friction model as WinterArena
    }

    @Override
    public boolean isFinished(IMobileEntity mobileEntity) {
        ValidationUtils.assertNotNull(mobileEntity);
        return mobileEntity.getLocation().getX() >= length;
    }

    public double getLength() {
        return length;
    }

    public SnowSurface getSurface() {
        return surface;
    }

    public WeatherCondition getCondition() {
        return condition;
    }


}
