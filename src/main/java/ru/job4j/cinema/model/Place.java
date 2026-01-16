package ru.job4j.cinema.model;

import java.util.Map;
import java.util.Objects;

public class Place {
    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "row_number", "rowNumber",
            "session_id", "sessionId",
            "place_number", "placeNumber",
            "free", "free"
    );

    private int sessionId;

    private int rowNumber;

    private int placeNumber;

    private boolean free;

    public Place() {
    }

    public Place(int sessionId, int rowNumber, int placeNumber, boolean free) {
        this.sessionId = sessionId;
        this.rowNumber = rowNumber;
        this.placeNumber = placeNumber;
        this.free = free;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Place place = (Place) o;
        return sessionId == place.sessionId && rowNumber == place.rowNumber && free == place.free;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, rowNumber, free);
    }
}
