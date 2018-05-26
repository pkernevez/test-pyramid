package ch.octo.blog.transport.journeybooking;


import ch.octo.blog.transport.journeybooking.model.Journey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface JourneyRepository extends Repository<Journey, Long> {

    <S extends Journey> S save(S entity);

    Optional<Journey> findById(Long id);

    List<Journey> findAll();

    void deleteById(Long id);

    List<Journey> findByFromAndTo(String from, String to);
}
