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
public class Restore implements Ability{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
