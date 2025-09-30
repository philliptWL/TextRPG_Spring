package io.github.philliptwl.textrpg_spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@AllArgsConstructor
public class Evade extends Ability {

    private final String name = "Evade";
    private final double rate = 1.0;

    @Override
    public String type() {
        return "evasion";
    }

    @Override
    public int cost() {
        return 30;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getDamage() {
        return 0;
    }

    @Override
    public double getHeal() {
        return 0;
    }

    @Override
    public double getEvasion() {
        return 0;
    }
}
