package com.runicrealms.plugin.runicdoors.door.animations;

@FunctionalInterface
public
interface QuadCallable<One, Two, Three, Four> {
    public void apply(One one, Two two, Three three, Four four);
}
