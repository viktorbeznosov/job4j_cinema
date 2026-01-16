package ru.job4j.cinema.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class Film {
    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "name", "name",
            "description", "description",
            "year", "year",
            "genre_id", "genreId",
            "minimal_age", "minimalAge",
            "duration_in_minutes", "durationInMinutes",
            "file_id", "fileId"
    );

    private int id;

    private String name;

    private String description;

    private int year;

    private int genreId;

    private int minimalAge;

    private int durationInMinutes;

    private int fileId;

    private Genre genre;

    public Film() {
    }

    public static class Builder {
        private int id;
        private String name;
        private String description;
        private int year;
        private int genreId;
        private int minimalAge;
        private int durationInMinutes;
        private int fileId;
        private Genre genre;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder genreId(int genreId) {
            this.genreId = genreId;
            return this;
        }

        public Builder minimalAge(int minimalAge) {
            this.minimalAge = minimalAge;
            return this;
        }

        public Builder durationInMinutes(int durationInMinutes) {
            this.durationInMinutes = durationInMinutes;
            return this;
        }

        public Builder fileId(int fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder genre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public Film build() {
            Film film = new Film();
            film.id = this.id;
            film.name = this.name;
            film.description = this.description;
            film.year = this.year;
            film.genreId = this.genreId;
            film.minimalAge = this.minimalAge;
            film.durationInMinutes = this.durationInMinutes;
            film.fileId = this.fileId;
            film.genre = this.genre;

            return film;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getGenreId() {
        return genreId;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public int getMinimalAge() {
        return minimalAge;
    }

    public void setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Film film = (Film) o;
        return id == film.id && year == film.year && genreId == film.genreId && minimalAge == film.minimalAge && durationInMinutes == film.durationInMinutes && name.equals(film.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, year, genreId, minimalAge, durationInMinutes);
    }
}
