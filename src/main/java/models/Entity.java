package models;

import java.util.Random;

public class Entity {
    private int level, health;
    private double speed, power, strength;
    private int weapon, armor, accessory;

    public Entity(){
        this.level = 1;
        this.health = 200;
        this.speed = 1;
        this.power = 1;
        this.strength = 1;
        this.weapon = 0;
        this.armor = 0;
        this.accessory = 0;
    }

    public Entity(int level, int health, double speed, double power, double strength) {
        this.level = level;
        this.health = health;
        this.speed = speed;
        this.power = power;
        this.strength = strength;
        this.weapon = 0;
        this.armor = 0;
        this.accessory = 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public void setStats(double power, double speed, double strength){
        setPower(power);
        setSpeed(speed);
        setStrength(strength);
    }

    public int getWeapon() {
        return weapon;
    }

    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getAccessory() {
        return accessory;
    }

    public void setAccessory(int accessory) {
        this.accessory = accessory;
    }

    public int posOrNegOne(){
        Random random = new Random();
        return random.nextInt(3) - 1;
    }

    public int calcVariance(int base, double variance){
        return (int) (Math.random() * (base * variance));
    }
}
