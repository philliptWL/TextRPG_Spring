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
public class Sword implements Weapon{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double damage;

    public Sword(String name, double damage) {
        this.name = name;
        this.damage = damage;
    }

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
