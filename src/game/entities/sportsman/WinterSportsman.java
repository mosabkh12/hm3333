package game.entities.sportsman;

import game.arena.IArena;
import game.competition.Competitor;
import game.enums.*;
import utilities.Point;


public class WinterSportsman extends Sportsman implements Competitor {
    private final Discipline discipline;
    private Point finish;
    private IArena arena;
    private int number;
    private Color color;
    private CompetitorState State;

    public WinterSportsman(String name, double age, Gender gender, double acceleration, double maxSpeed, Discipline discipline) {
        super(name, age, gender, acceleration, maxSpeed);
        this.discipline = discipline;
        this.State=CompetitorState.ACTIVE;
        this.number=0;
        this.color = Color.WHITE;
    }
    @Override
    public void initRace() {
        this.setLocation(new Point(0,this.getLocation().getY()));
    }
    
    @Override
    public void initRace(Point p, Point f, IArena arena) {
        this.setLocation(p); 
        this.finish = f;
        this.arena = arena;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getName();
    }

    //region Getters & setters
    public Discipline getDiscipline() {
        return discipline;
    }

    @Override
    public double getAcceleration() {
        return super.getAcceleration()+ League.calcAccelerationBonus(this.getAge());
    }
    //endregion
    
	private boolean competitionInProgress() {
		boolean res = getLocation().getX() < finish.getX();
		Point p = getLocation();
		if (!res) setLocation(new Point(finish.getX(),p.getY()));
		return res;
	}
    public void setAcceleration(double acceleration) {
        super.setAcceleration(acceleration);
    }
    
	
	@Override
	public void run() {
		while (competitionInProgress()) {
			move(arena.getFriction());
            try { 
                   Thread.sleep(100);
            } catch (InterruptedException ex) {
                   ex.printStackTrace();
            }
		}
		setChanged();
		notifyObservers();
	}

    @Override
    public WinterSportsman clone() {
        try {
            return (WinterSportsman) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Color getColor() {  // Return type is now Color
        return color;
    }

    public void setColor(Color color) {  // Parameter type is now Color
        this.color = color;
    }

    public CompetitorState getState() {
        return State;
    }

    public void setState(CompetitorState state) {
        this.State = state;
    }

}






