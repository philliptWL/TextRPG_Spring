package io.github.philliptwl.textrpg_spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Barbarian extends Role {

    private double health;
    private Weapon weapon;
    private int resource;
    private Ability ability;

    public Barbarian( double health, Sword sword, int resource, Ability ability) {
        this.health = health;
        this.weapon = sword;
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
        return "Swing " + getWeapon().name();
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public void setResource(int resource) {
        this.resource = resource;
    }

    public void setWeapon(Sword weapon) {
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
    public String type() { return "Barbarian"; }

    @Override
    public void resetHealth() {
        health = 110.0;
    }

    @Override
    public void resetResource() {
        resource = 80;
    }

    @Override
    public int useAbility(){
        return resource - ability.cost();
    }
}

