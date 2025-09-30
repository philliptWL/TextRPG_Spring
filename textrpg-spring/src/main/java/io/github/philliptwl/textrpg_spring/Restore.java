package io.github.philliptwl.textrpg_spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@AllArgsConstructor
public class Restore extends Ability{

    private final String name = "Restore";
    private final double restore = 50.0;

    @Override
    public String type() {
        return "heal";
    }

    @Override
    public int cost() {
        return 40;
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
        return restore;
    }

    @Override
    public double getEvasion() {
        return 0;
    }
}
