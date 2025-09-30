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
public class Enemy implements Role{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double health;
    private Weapon weapon;
    private int resource;
    private Ability ability;

    public Enemy(double health, Weapon weapon, int resource, Ability ability) {
        this.health = health;
        this.weapon = weapon;
        this.resource = resource;
        this.ability = ability;
    }

//    public double getHealth() {
//        return health;
//    }
//    public void setHealth(double health) {
//        this.health = health;
//    }

    @Override
    public void setResource(int resource) {
        this.resource = resource;
    }

    @Override
    public int getResource() {
        return resource;
    }

    @Override
    public String type() {
        return "Enemy";
    }

    @Override
    public void resetHealth() {

    }

    @Override
    public void resetResource() {

    }

    @Override
    public int useAbility() {
        return 0;
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
        return "";
    }
}
