package com.example.n8154.app9_1;

public class Theatre {
    private String name;
    private int ID;

    public Theatre(String s, int i) {
        name = s;
        ID = i;
    }

    public void setName(String n) {
        name = n;
    }

    public void setID (int i) {
        ID = i;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public String toString() {
        return name;
    }


}
