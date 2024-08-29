package game.entities.sportsman;

import game.entities.MobileEntity;
import game.enums.Gender;


public class Sportsman extends MobileEntity {
    private final String name;
    private final double age;
    private final Gender gender;
    private double acceleration;

    public Sportsman(String name, double age, Gender gender, double acceleration, double maxSpeed) {
        super(0, acceleration,maxSpeed);
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }
    //region Getters & setters
    public String getName() {
        return name;
    }

    public double getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }
    
}
