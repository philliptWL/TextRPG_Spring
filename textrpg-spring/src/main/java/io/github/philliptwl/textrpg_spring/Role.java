package io.github.philliptwl.textrpg_spring;

public interface Role {
    double getHealth();
    int getResource();
    String type();
    void resetHealth();
    void resetResource();
    int useAbility();
    Weapon getWeapon();
    Ability getAbility();
    String useWeapon();
    void setHealth(double health);
    void setResource(int resource);
}