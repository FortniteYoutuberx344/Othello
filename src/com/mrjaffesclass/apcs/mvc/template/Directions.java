/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mrjaffesclass.apcs.mvc.template;

/**
 *
 * @author tonys
 */
public class Directions {

    /**
     * Gets the requested direction vector
     *
     * @param compassPoint Direction whose vector we want to retrieve
     * @return Direction vector
     */
    public static Position getVector(String compassPoint) {
        switch (compassPoint) {
            case "N":
                return new Position(0, -1);
            case "NE":
                return new Position(1, -1);
            case "E":
                return new Position(1, 0);
            case "SE":
                return new Position(1, 1);
            case "S":
                return new Position(0, 1);
            case "SW":
                return new Position(-1, 1);
            case "W":
                return new Position(-1, 0);
            case "NW":
                return new Position(-1, -1);
        }
        return null;
    }

    /**
     * Gets an array of strings representing cardinal direction points
     *
     * @return Array of direction strings
     */
    public static String[] getDirections() {
        String[] points = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        return points;
    }

}
