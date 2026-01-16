package ru.job4j.cinema.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.sql2o.ResultSetHandler;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Place;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class Sql2oFilmSessionRepository implements FilmSessionRepository {
    private final Sql2o sql2o;

    public Sql2oFilmSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    private String getFindByIdSql() {
        var sql = """
                    SELECT 
                    film_sessions.*, 
                    films.name as film_name, 
                    films.year as film_year, 
                    films.description as film_description, 
                    films.file_id as film_file_id,
                    halls.name as halls_name,
                    halls.row_count as halls_row_count,
                    halls.place_count as halls_place_count,
                    (halls.row_count * halls.place_count - COALESCE(t.ticket_count, 0)) AS free_tickets
                    FROM film_sessions
                    LEFT JOIN films on films.id = film_sessions.film_id
                    LEFT JOIN halls on halls.id = film_sessions.halls_id
                    LEFT JOIN (
                        SELECT
                            session_id,
                            COUNT(*) AS ticket_count
                        FROM tickets
                        GROUP BY session_id
                    ) t ON t.session_id = film_sessions.id
                    WHERE film_sessions.id = :id                    
                    """;
        return sql;
    }

    @Override
    public Optional<FilmSession> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = getFindByIdSql();
            var query = connection.createQuery(sql);
            var filmSession = query
                    .addParameter("id", id)
                    .executeAndFetchFirst((ResultSetHandler<FilmSession>) resultSet -> {
                        FilmSession session = new FilmSession();
                        session.setId(resultSet.getInt("id"));
                        session.setFilmId(resultSet.getInt("film_id"));
                        session.setHallsId(resultSet.getInt("halls_id"));
                        session.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
                        session.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
                        session.setPrice(resultSet.getInt("price"));
                        session.setFreeTickets(resultSet.getInt("free_tickets"));

                        Film film = new Film.Builder()
                                .name(resultSet.getString("film_name"))
                                .year(resultSet.getInt("film_year"))
                                .description(resultSet.getString("film_description"))
                                .fileId(resultSet.getInt("film_file_id"))
                                .build();
                        Hall hall = new Hall();
                        hall.setName(resultSet.getString("halls_name"));
                        hall.setRowCount(resultSet.getInt("halls_row_count"));
                        hall.setPlaceCount(resultSet.getInt("halls_place_count"));

                        session.setFilm(film);
                        session.setHall(hall);

                        return session;
                    });
            return Optional.ofNullable(filmSession);
        }
    }

    private String getFindAllSql() {
        var sql = """
                    SELECT 
                        film_sessions.*, 
                        films.name as film_name,
                        films.year as film_year,
                        films.description as film_description,
                        halls.name as halls_name,
                        (halls.row_count * halls.place_count - COALESCE(t.ticket_count, 0)) AS free_tickets
                    FROM film_sessions
                    LEFT JOIN films on films.id = film_sessions.film_id
                    LEFT JOIN halls on halls.id = film_sessions.halls_id
                    LEFT JOIN (
                        SELECT
                            session_id,
                            COUNT(*) AS ticket_count
                        FROM tickets
                        GROUP BY session_id
                    ) t ON t.session_id = film_sessions.id
                    ORDER BY film_sessions.id ASC
                    """;

        return sql;
    }

    @Override
    public Collection<FilmSession> findAll() {
        try (var connection = sql2o.open()) {

            var sql = getFindAllSql();

            return connection.createQuery(sql)
                .executeAndFetch((ResultSetHandler<FilmSession>) resultSet -> {
                    FilmSession filmSession = new FilmSession();
                    filmSession.setId(resultSet.getInt("id"));
                    filmSession.setFilmId(resultSet.getInt("film_id"));
                    filmSession.setHallsId(resultSet.getInt("halls_id"));
                    filmSession.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
                    filmSession.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
                    filmSession.setPrice(resultSet.getInt("price"));
                    filmSession.setFreeTickets(resultSet.getInt("free_tickets"));

                    Film film = new Film.Builder()
                        .name(resultSet.getString("film_name"))
                        .year(resultSet.getInt("film_year"))
                        .description(resultSet.getString("film_description"))
                        .build();
                    Hall hall = new Hall();
                    hall.setName(resultSet.getString("halls_name"));

                    filmSession.setFilm(film);
                    filmSession.setHall(hall);

                    return filmSession;
                });
        }
    }
}
