package ru.job4j.cinema.repository.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Place;
import ru.job4j.cinema.repository.interfaces.FilmSessionsPlaceRepository;

import java.util.Collection;

@Slf4j
@Service
public class Sql2oFilmSessionsPlaceRepository implements FilmSessionsPlaceRepository {
    private final Sql2o sql2o;

    public Sql2oFilmSessionsPlaceRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Place> getPlaces(int sessionId) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT
                        :sessionId as session_id,
                        rows.row_number,
                        places.place_number,
                        NOT EXISTS (
                            SELECT 1 FROM tickets t
                            WHERE t.session_id = :sessionId
                              AND t.row_number = rows.row_number
                              AND t.place_number = places.place_number
                        ) AS free
                    FROM
                        film_sessions fs
                        JOIN halls h ON h.id = fs.halls_id,
                        generate_series(1, h.row_count) AS rows(row_number),
                        generate_series(1, h.place_count) AS places(place_number)
                    WHERE
                        fs.id = :sessionId
                    ORDER BY
                        rows.row_number,
                        places.place_number;
                    """;

            return connection.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .setColumnMappings(Place.COLUMN_MAPPING)
                    .executeAndFetch(Place.class);
        }
    }
}
