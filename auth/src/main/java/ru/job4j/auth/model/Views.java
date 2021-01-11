package ru.job4j.auth.model;

public final class Views {
    public interface EmpOnlyData {}

    public interface AccOnlyData {}

    public interface EmpFull extends EmpOnlyData {}

    public interface AccFull extends AccOnlyData {}

    public interface EmpFullAndAccData extends EmpFull, AccOnlyData {}

    public interface EmpDataAndAccFull extends EmpOnlyData, AccFull {}
}
