package io.github.philliptwl.textrpg_spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sword implements Weapon{

    private String name;
    private double damage;

    @Override
    public String name() {
        return name;
    }

    @Override
    public double damage() {
        return damage;
    }

    @Override
    public String attackType() {
        return "swing";
    }

    @Override
    public double useWeapon(double health) {
        return health - damage;
    }

    @Override
    public double setWeaponDamage(double damage) {
        return this.damage = damage;
    }
}
