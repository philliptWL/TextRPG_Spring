package io.github.philliptwl.textrpg_spring;

public abstract class Ability {
    abstract String type();
    abstract int cost();
    abstract String getName();
    abstract double getDamage();
    abstract double getHeal();
    abstract double getEvasion();
}
