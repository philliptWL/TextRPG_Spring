package io.github.philliptwl.textrpg_spring;

public abstract class Role {
    abstract double getHealth();
    abstract int getResource();
    abstract String type();
    abstract void resetHealth();
    abstract void resetResource();
    abstract int useAbility();
    abstract Weapon getWeapon();
    abstract Ability getAbility();
    abstract String useWeapon();
    abstract void setHealth(double health);
    abstract void setResource(int resource);
}