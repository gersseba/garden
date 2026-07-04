package com.gersseba.garden.model;

/**
 * In-memory mock plant entry used by the My Plants screen until Room persistence is wired up.
 */
public class MockPlant {

    public final long id;
    public final String name;

    public MockPlant(long id, String name) {
        this.id = id;
        this.name = name;
    }
}

