package io.github.philliptwl.textrpg_spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@AllArgsConstructor
public class Charge extends Ability {

    private final String name = "Charge";
    private final int damage = 20;

    @Override
    public String type() {
        return "damage";
    }

    @Override
    public int cost() {
        return 20;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getDamage() {
        return damage;
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
