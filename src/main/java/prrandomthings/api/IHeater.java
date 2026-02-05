package prrandomthings.api;

public interface IHeater {
    boolean isActive();
    IHeater DEAD_HEATER=()->false;
}
