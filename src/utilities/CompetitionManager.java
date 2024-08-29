/*
Name:MohammedGhara,ID:213078116
Name:MosabKhalaf,ID:325103687
 */
package utilities;

import game.enums.*;
import game.arena.WinterArena;
import game.competition.SkiCompetition;
import game.entities.sportsman.Skier;
import game.entities.sportsman.WinterSportsman;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Interface for the Prototype design pattern.
 */
interface Prototype {
    Prototype clone();
}

/**
 * Singleton class to manage the competition.
 * Implements Observer to update the GUI and Prototype for cloning.
 */
public class CompetitionManager implements Observer, Prototype {

    private static CompetitionManager instance = null;
    private WinterArena arena;
    private List<WinterSportsman> competitors;
    private ExecutorService executorService;
    private int threadPoolSize;
    private CompetitorState currentState;


    private CompetitionManager() {
        competitors = new ArrayList<>();
    }


    public static CompetitionManager getInstance() {
        if (instance == null) {
            instance = new CompetitionManager();
        }
        return instance;
    }


    public CompetitionManager getCompetitionManager() {
        return instance;
    }

    @Override
    public Prototype clone() {
        try {
            return (Prototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Factory Method to create a WinterArena or SummerArena based on the type.
     *
     * @param type the type of arena (winter or summer).
     * @param length the length of the arena.
     * @param surface the surface of the arena.
     * @param weather the weather condition in the arena.
     * @return a WinterArena or SummerArena instance.
     */
    public WinterArena createArena(String type, int length, SnowSurface surface, WeatherCondition weather) {
        if (type.equalsIgnoreCase("winter")) {
            return new WinterArena(length, surface, weather);
        } else if (type.equalsIgnoreCase("summer")) {
            return new WinterArena(length, surface, weather);
        }
        throw new IllegalArgumentException("Unknown arena type: " + type);
    }

    /**
     * Builder Pattern to assemble a SkiCompetition.
     *
     * @param numberOfCompetitors the number of competitors in the competition.
     * @param arena the arena where the competition will take place.
     * @param discipline the discipline of the competition.
     * @param league the league of the competition.
     * @param gender the gender of the competitors.
     * @return a SkiCompetition instance.
     */
    public SkiCompetition buildSkiCompetition(int numberOfCompetitors, WinterArena arena, Discipline discipline, League league, Gender gender) {
        SkiCompetition skiCompetition = new SkiCompetition(arena, numberOfCompetitors, discipline, league, gender);
        WinterSportsman prototypeCompetitor = new Skier("Default", 25, gender, 10, 30, discipline);

        for (int i = 0; i < numberOfCompetitors; i++) {
            WinterSportsman clonedCompetitor = (WinterSportsman) prototypeCompetitor.clone();
            clonedCompetitor.setNumber(i + 1);
            clonedCompetitor.setColor(game.enums.Color.RED); // Example customization
            skiCompetition.addCompetitor(clonedCompetitor);
        }

        return skiCompetition;
    }

    /**
     * Initializes the thread pool with a specified number of threads.
     *
     * @param threadPoolSize the size of the thread pool.
     */
    public void initializeThreadPool(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Starts the race by submitting competitors to the thread pool.
     */
    public void startRace() {
        for (WinterSportsman competitor : competitors) {
            executorService.submit(() -> {
                try {
                    competitor.run(); // assuming run is the method that executes the race logic
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }

    /**
     * Decorator Pattern to customize competitors with additional attributes.
     *
     * @param competitor the original competitor.
     * @param color the color to be added to the competitor.
     * @param extraAcceleration the extra acceleration to be added.
     * @return the customized WinterSportsman.
     */
    public WinterSportsman customizeCompetitor(WinterSportsman competitor, Color color, double extraAcceleration) {
        competitor = new ColorDecorator(competitor, color);
        competitor = new AccelerationDecorator(competitor, extraAcceleration);
        return competitor;
    }

    /**
     * Observer Pattern method to update the GUI based on state changes.
     *
     * @param o the Observable object.
     * @param arg an argument passed to the notifyObservers method.
     */
    @Override
    public void update(Observable o, Object arg) {
        WinterSportsman sportsman = (WinterSportsman) o;
        game.enums.CompetitorState state = sportsman.getState();

    }

    /**
     * State Pattern to manage the states of competitors.
     *
     * @param sportsman the competitor whose state is to be set.
     * @param state the new state of the competitor.
     */
    public void setCompetitorState(WinterSportsman sportsman, CompetitorState state) {
        this.currentState = state;
        sportsman.notifyObservers();
    }

    // Example methods to handle states
    public void handleActiveState(WinterSportsman sportsman) {
        setCompetitorState(sportsman, new ActiveState());
    }

    public void handleInjuredState(WinterSportsman sportsman) {
        setCompetitorState(sportsman, new InjuredState());
    }

    public void handleDisabledState(WinterSportsman sportsman) {
        setCompetitorState(sportsman, new DisabledState());
    }

    public void handleCompletedState(WinterSportsman sportsman) {
        setCompetitorState(sportsman, new CompletedState());
    }
}


/**
 * Decorator Pattern base class for adding functionality to WinterSportsman.
 */
class CompetitorDecorator extends WinterSportsman {
    protected WinterSportsman decoratedCompetitor;

    /**
     * Constructor for CompetitorDecorator.
     *
     * @param decoratedCompetitor the competitor to be decorated.
     */
    public CompetitorDecorator(WinterSportsman decoratedCompetitor) {
        super(decoratedCompetitor.getName(), decoratedCompetitor.getAge(), decoratedCompetitor.getGender(),
                decoratedCompetitor.getAcceleration(), decoratedCompetitor.getMaxSpeed(), decoratedCompetitor.getDiscipline());
        this.decoratedCompetitor = decoratedCompetitor;
    }

    /**
     * Sets the acceleration for the decorated competitor.
     *
     * @param acceleration the new acceleration value.
     */
    public void setAcceleration(double acceleration) {
        decoratedCompetitor.setAcceleration(acceleration);
    }
}


class ColorDecorator extends CompetitorDecorator {
    /**
     * Constructor for ColorDecorator.
     *
     * @param decoratedCompetitor the competitor to be decorated.
     * @param color the color to be added to the competitor.
     */
    public ColorDecorator(WinterSportsman decoratedCompetitor, Color color) {
        super(decoratedCompetitor);
        // Logic to add color to the competitor
    }
}

class AccelerationDecorator extends CompetitorDecorator {
    private double extraAcceleration;


    public AccelerationDecorator(WinterSportsman decoratedCompetitor, double extraAcceleration) {
        super(decoratedCompetitor);
        this.extraAcceleration = extraAcceleration;
    }


    @Override
    public double getAcceleration() {
        return super.getAcceleration() + extraAcceleration;
    }
}

// State Pattern Classes

interface CompetitorState {
    void handleState(WinterSportsman sportsman);
}


class ActiveState implements CompetitorState {
    @Override
    public void handleState(WinterSportsman sportsman) {
    }
}


class InjuredState implements CompetitorState {
    @Override
    public void handleState(WinterSportsman sportsman) {
    }
}


class DisabledState implements CompetitorState {
    @Override
    public void handleState(WinterSportsman sportsman) {
    }
}

/**
 * Concrete State representing the completed state of a competitor.
 */
class CompletedState implements CompetitorState {
    @Override
    public void handleState(WinterSportsman sportsman) {
    }
}
