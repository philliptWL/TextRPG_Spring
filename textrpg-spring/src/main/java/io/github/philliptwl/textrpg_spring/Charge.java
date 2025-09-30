package io.github.philliptwl.textrpg_spring;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Charge implements Ability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
