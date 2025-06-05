package dev.asper.common.fsm;

public interface Stateful<E extends Enum<E>> {
    void setState(E state);
    E getState();
}
