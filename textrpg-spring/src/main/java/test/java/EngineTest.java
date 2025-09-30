package test.java;

import io.github.philliptwl.textrpg_spring.Engine;
import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.*;

class EngineTest {
    @Test
    void moveEnemyRandomTest(){
        assertTrue(Engine.moveEnemyRandom());

    }
}