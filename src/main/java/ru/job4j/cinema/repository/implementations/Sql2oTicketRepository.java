package ru.job4j.cinema.repository.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import ru.job4j.cinema.dto.PlaceDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.interfaces.TicketRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class Sql2oTicketRepository implements TicketRepository {
    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO tickets (session_id, row_number, place_number, user_id)
                    values (:sessionId, :rowNumber, :placeNumber, :userId)
                    """;
            var query = connection.createQuery(sql)
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);

            return Optional.of(ticket);
        } catch (DuplicateKeyException exception) {
            log.error(exception.getMessage(), exception);
            return Optional.empty();
        }
    }

    @Override
    public void book(List<PlaceDto> places, User user) throws Exception {
        String sql = """
                    INSERT INTO tickets (session_id, row_number, place_number, user_id)
                    values (:sessionId, :rowNumber, :placeNumber, :userId)
                    """;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(sql);
            for (PlaceDto place: places) {
                query.addParameter("sessionId", place.getSessionId())
                        .addParameter("rowNumber", place.getRowNumber())
                        .addParameter("placeNumber", place.getPlaceNumber())
                        .addParameter("userId", user.getId())
                        .addToBatch();
            }
            query.executeBatch();
        } catch (Sql2oException exception) {
            log.error(exception.getMessage(), exception);
            throw new Exception("Билет уже был забронирован ранее");
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var sql = "DELETE FROM tickets WHERE id = :id";
            var query = connection.createQuery(sql);
            var affectedRows = query.executeUpdate().getResult();

            return affectedRows > 0;
        }
    }

    @Override
    public Optional<Ticket> findById(int id) {
        try (var connection = sql2o.open()) {
            var ticket = connection.createQuery("SELECT * FROM tickets WHERE id = :id")
                    .addParameter("id", id)
                    .setColumnMappings(Ticket.COLUMN_MAPPING)
                    .executeAndFetchFirst(Ticket.class);
            return Optional.ofNullable(ticket);
        }
    }

    @Override
    public Collection<Ticket> findBySessionId(int sessionId) {
        try (var connection = sql2o.open()) {
            var sql = "SELECT * FROM tickets WHERE session_id = :sessionId";
            return connection.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .setColumnMappings(Ticket.COLUMN_MAPPING)
                    .executeAndFetch(Ticket.class);
        }
    }

    @Override
    public Collection<Ticket> findAll() {
        try (var connection = sql2o.open()) {
            var sql = "SELECT * FROM tickets";
            return connection.createQuery(sql)
                .setColumnMappings(Ticket.COLUMN_MAPPING)
                .executeAndFetch(Ticket.class);
        }
    }

    @Override
    public Ticket findByPlace(PlaceDto place) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM tickets WHERE session_id = :sessionId 
                    AND row_number = :rowNumber 
                    AND place_number = :placeNumber
                    """;
            return (Ticket) connection.createQuery(sql)
                    .addParameter("sessionId", place.getSessionId())
                    .addParameter("rowNumber", place.getRowNumber())
                    .addParameter("placeNumber", place.getPlaceNumber())
                    .setColumnMappings(Ticket.COLUMN_MAPPING)
                    .executeAndFetchFirst(Ticket.class);
        }
    }
}
