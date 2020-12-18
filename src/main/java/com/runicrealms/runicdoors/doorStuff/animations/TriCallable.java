package com.runicrealms.runicdoors.doorStuff.animations;

@FunctionalInterface
public
interface TriCallable<One, Two,Three> {
    public void apply(One one, Two two,Three three);
}
