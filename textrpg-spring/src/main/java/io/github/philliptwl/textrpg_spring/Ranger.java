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
public class Ranger implements Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double health;
    private Weapon weapon;
    private int resource;
    private Ability ability;

    public Ranger(double health, Bow bow, int resource, Ability ability) {
        this.health = health;
        this.weapon = bow;
        this.resource = resource;
        this.ability = ability;
    }

    @Override
    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public Ability getAbility() {
        return ability;
    }

    @Override
    public String useWeapon() {
        return "Knock and Draw " + getWeapon().name();
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public void setResource(int resource) {
        this.resource = resource;
    }

    public void setWeapon(Bow weapon) {
        this.weapon = weapon;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public int getResource() {
        return resource;
    }

    @Override
    public String type() {
        return "Ranger";
    }

    @Override
    public void resetHealth() {
        health = 100.0;
    }

    @Override
    public void resetResource() {
        resource = 100;
    }

    @Override
    public int useAbility(){
        return resource - ability.cost();
    }
}
