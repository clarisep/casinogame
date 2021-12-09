package com.slots.game.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Promotion {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    private int id;
    private int counter;


    public int getCounter() {
        return counter;
    }

    public void setCounter(final int counter) {
        this.counter = counter;
    }

}
