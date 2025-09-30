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
public class Mage implements Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double health;
    private Weapon weapon;
    private int resource;
    private Ability ability;

    public Mage(double health,Staff staff,int resource,Ability ability) {
        this.health = health;
        this.weapon = staff;
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
        return "Empower " + getWeapon().name();
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public void setResource(int resource) {
        this.resource = resource;
    }

    public void setWeapon(Staff weapon) {
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
        return "Mage";
    }

    @Override
    public void resetHealth() {
        health = 90.0;
    }

    @Override
    public void resetResource() {
        resource = 110;
    }

    @Override
    public int useAbility(){
        return resource - ability.cost();
    }
}
