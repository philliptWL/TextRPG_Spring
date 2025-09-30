package io.github.philliptwl.textrpg_spring;

public interface Ability {
    String type();
    int cost();
    String getName();
    double getDamage();
    double getHeal();
    double getEvasion();
}
