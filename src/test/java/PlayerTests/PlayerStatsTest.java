package PlayerTests;

import models.Player;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;

public class PlayerStatsTest {
    private Player player;

    @Before
    public void init(){
        player = new Player("1");
        player.setLevel(100);
    }

    @Test
    public void increaseSpeedTest(){
        double oldSpeed = player.getSpeed();
        player.increSpeed(100.0);
        assertEquals(oldSpeed + 75.0, player.getSpeed());
    }

    @Test
    public void increasePowerTest(){
        double oldPower = player.getPower();
        player.increPower(20);
        assertEquals(oldPower + 15, player.getPower());
    }

    @Test
    public void increaseStrengthTest(){
        double oldStrength = player.getStrength();
        player.increStrength(10);
        assertEquals(oldStrength + 7.5, player.getStrength());
    }

    @Test
    public void totalStatsTest(){
        double power = 250;
        double strength = 330;
        double speed = 120;
        double total = (power + speed + strength) * .75 + 3;

        player.increPower(power);
        player.increStrength(strength);
        player.increSpeed(speed);

        assertEquals(total, player.getTotalStats());
    }

    @Test
    public void statGainLevel50Test(){
        player.setLevel(50);
        double expected = .625;

        System.out.println("Lvl 50 stat gain: " + expected);
        assertEquals(expected, player.calcStatGain());
    }

    @Test
    public void statGainLeve200Test(){
        player.setLevel(200);
        double expected = 1.0;

        System.out.println("Lvl 200 stat gain: " + expected);
        assertEquals(expected, player.calcStatGain());
    }

    @Test
    public void statGainLevel300Test(){
        player.setLevel(300);
        double expected = 1.5;

        System.out.println("Lvl 300 stat gain: " + expected);
        assertEquals(expected, player.calcStatGain());
    }

    @Test
    public void statGainLevel400Test(){
        player.setLevel(400);
        double expected = 2.0;

        System.out.println("Lvl 400 stat gain: " + expected);
        assertEquals(expected, player.calcStatGain());
    }

    @Test
    public void statGainLevel500Test(){
        player.setLevel(500);
        double expected = 2.5;

        System.out.println("Lvl 500 stat gain: " + expected);
        assertEquals(expected, player.calcStatGain());
    }

    @Test
    public void statIncreLevel100Test(){
        player.setLevel(100);
        System.out.println("Old Speed " + player.getSpeed());
        player.increSpeed(20);
        System.out.println("New Speed " + player.getSpeed());

        assertEquals(16.0, player.getSpeed());
    }

    @Test
    public void statIncreLevel50Test(){
        player.setLevel(50);
        System.out.println("Old Speed " + player.getSpeed());
        player.increSpeed(20);
        System.out.println("New Speed " + player.getSpeed());

        assertEquals(20 * .625 + 1, player.getSpeed());
    }

    @Test
    public void statIncreLevelTwoTest(){
        player.setLevel(2);
        System.out.println("Old Speed " + player.getSpeed());
        player.increSpeed(20);
        System.out.println("New Speed " + player.getSpeed());

        double expected = 20 * .505 + 1;
        System.out.println(expected);
        assertEquals(expected, player.getSpeed());
    }

    @Test
    public void statIncreLevelThreeTest(){
        player.setLevel(3);
        System.out.println("Old Speed " + player.getSpeed());
        player.increSpeed(20);
        System.out.println("New Speed " + player.getSpeed());

        double expected = (20 * player.calcStatGain()) + 1.0;
        double rounded = player.round(expected);

        assertEquals(rounded, player.getSpeed());
    }

    @Test
    public void speedPercentageTest(){
        player.setPower(500);
        player.setSpeed(300);
        player.setStrength(200);

        String actual = player.getSpeedPercentage();

        assertEquals("30", actual);
    }

    @Test
    public void speedPercentage2Test(){
        player.setPower(1000);
        player.setSpeed(353);
        player.setStrength(187);

        String actual = player.getSpeedPercentage();

        assertEquals("22.92", actual);
    }

    @Test
    public void powerPercentageTest(){
        player.setPower(321);
        player.setSpeed(192);
        player.setStrength(584);

        String actual = player.getPowerPercentage();

        assertEquals("29.26", actual);
    }

    @Test
    public void powerPercentage2Test(){
        player.setPower(1);
        player.setSpeed(321);
        player.setStrength(584);

        String actual = player.getPowerPercentage();

        assertEquals("0.11", actual);
    }

    @Test
    public void strengthPercentageTest(){
        player.setPower(534);
        player.setSpeed(321.73);
        player.setStrength(888.32);

        String actual = player.getStrengthPercentage();

        assertEquals("50.93", actual);
    }

    @Test
    public void strengthPercentage2Test(){
        player.setPower(1534.33);
        player.setSpeed(9321.73);
        player.setStrength(888.32);

        String actual = player.getStrengthPercentage();

        assertEquals("7.56", actual);
    }

    @Test
    public void legendaryEffectTest(){
        player.setSpeed(100.0);
        player.setPower(100.0);
        player.setStrength(100.0);

        player.applyLegendaryEffect();

        assertEquals(105 + 33.333, player.getSpeed(), .01);
        assertEquals(105 + 33.333, player.getPower(), .01);
        assertEquals(105 + 33.333, player.getStrength(), .01);
    }

    @Test
    public void legendaryEffectTest2(){
        player.setSpeed(531.952);
        player.setPower(97.42);
        player.setStrength(9324.841);

        player.applyLegendaryEffect();

        assertEquals(558.5496 + 33.333, player.getSpeed(),  .01);
        assertEquals(102.291 + 33.333, player.getPower(), .01);
        assertEquals(9791.08 + 33.333, player.getStrength(),.01);
    }

    @Test
    public void legendaryEffectTest3(){
        player.setSpeed(531.952);
        player.setPower(97.42);
        player.setStrength(9324.841);

        double oldTotal = player.getTotalStats();
        player.applyLegendaryEffect();
        double newTotal = player.getTotalStats();

        double diff = newTotal - oldTotal;

        assertEquals(diff, oldTotal *.05 + 99.999,  .01);

    }

    @Test
    public void legendaryEffectTest4(){
        player.setSpeed(38472.7324);
        player.setPower(32597.428);
        player.setStrength(19324.241);

        double oldTotal = player.getTotalStats();
        player.applyLegendaryEffect();
        double newTotal = player.getTotalStats();

        double diff = newTotal - oldTotal;

        assertEquals(diff, oldTotal *.05 + 99.999, .01);

    }
}
