package ItemTests;


import models.Item;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ItemTest {

    @Test
    public void generateRollTest(){
        for(int i = 0; i < 10000; i++){
            int roll = Item.generateNumber();
            assertTrue(roll > 0 && roll <= 100);
        }
    }

    @Test
    public void playerBracketTest(){
        assertEquals(0, Item.getLevelBracket(42));
    }
    @Test
    public void playerBracketTest2(){
        assertEquals(2, Item.getLevelBracket(100));
    }

    @Test
    public void playerBracketTest3(){
        assertEquals(7, Item.getLevelBracket(352));
    }

    @Test
    public void levelMultiplierTest(){
        assertEquals(25, Item.getLevelMultiplier(258));
    }

    @Test
    public void levelMultiplierTest2(){
        assertEquals(10, Item.getLevelMultiplier(148));
    }

    @Test
    public void levelMultiplierTest3(){
        assertEquals(10, Item.getLevelMultiplier(100));
    }

    @Test
    public void levelMultiplierTest4(){
        assertEquals(50, Item.getLevelMultiplier(484));
    }

    @Test
    public void levelMultiplierTest5(){
        assertEquals(70, Item.getLevelMultiplier(555));
    }

    @Test
    public void levelMultiplierTest6(){
        assertEquals(1, Item.getLevelMultiplier(26));
    }

    @Test
    public void itemLowerBoundStatTest(){
        int level = 222;
        assertEquals(3000, Item.getLowerBoundStat(level));
    }

    @Test
    public void itemLowerBoundStatTest2(){
        int level = 76;
        assertEquals(50, Item.getLowerBoundStat(level));
    }

    @Test
    public void itemLowerBoundStatTest3(){
        int level = 558;
        assertEquals(33000, Item.getLowerBoundStat(level));
    }

    @Test
    public void itemUpperBoundStatTest(){
        int level = 222;
        assertEquals(5000, Item.getUpperBoundStat(level));
    }

    @Test
    public void itemUpperBoundStatTest2(){
        int level = 76;
        assertEquals(500, Item.getUpperBoundStat(level));
    }

    @Test
    public void itemUpperBoundStatTest3(){
        int level = 558;
        assertEquals(42000, Item.getUpperBoundStat(level));
    }

    @Test
    public void itemLowerBoundLevelTest(){
        assertEquals(150, Item.getLowerBoundLevel(183));
    }

    @Test
    public void itemLowerBoundLevelTest2(){
        assertEquals(50, Item.getLowerBoundLevel(69));
    }

    @Test
    public void itemLowerBoundLevelTest3(){
        assertEquals(100, Item.getLowerBoundLevel(100));
    }

    @Test
    public void itemUpperBoundLevelTest(){
        assertEquals(200, Item.getUpperBoundLevel(183));
    }

    @Test
    public void itemUpperBoundLevelTest2(){
        assertEquals(100, Item.getUpperBoundLevel(69));
    }

    @Test
    public void itemUpperBoundLevelTest3(){
        assertEquals(150, Item.getUpperBoundLevel(100));
    }

    @Test
    public void itemUpperBoundLevelTest4(){
        assertEquals(300, Item.getUpperBoundLevel(250));
    }
}