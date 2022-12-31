package com.snhu.cs360.banddatabase;

import java.util.Objects;

public class Band {

    private long id;
    private String name;
    private String description;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Band(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Band(String name, String description){
        this(-1, name, description);
    }

    public Band(long id, Band band){
        this(id, band.name, band.description);
    }

    public Band(Band b) {
        this(b.id, b.name, b.description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Band band = (Band) o;
        return id == band.id && Objects.equals(name, band.name) && Objects.equals(description, band.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
