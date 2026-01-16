package ru.job4j.cinema.dto;

public class PlaceDto {
    private Integer sessionId;
    private Integer rowNumber;
    private Integer placeNumber;

    public PlaceDto() {

    }

    public PlaceDto(Integer sessionId, Integer rowNumber, Integer placeNumber) {
        this.sessionId = sessionId;
        this.rowNumber = rowNumber;
        this.placeNumber = placeNumber;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(Integer placeNumber) {
        this.placeNumber = placeNumber;
    }

    @Override
    public String toString() {
        return "SeatDto{"
                + "sessionId=" + sessionId
                + ", rowNumber=" + rowNumber
                + ", placeNumber=" + placeNumber
                + '}';
    }
}