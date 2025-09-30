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
public class Enemy extends Role{

    private double health;
    private Weapon weapon;
    private int resource;
    private Ability ability;


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
