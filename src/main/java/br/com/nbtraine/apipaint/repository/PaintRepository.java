
package br.com.nbtraine.apipaint.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.nbtraine.apipaint.entity.Paint;

/**
 * PaintRepository
 */
public interface PaintRepository extends JpaRepository<Paint, Long> {

    Optional<Paint> findByName(String name);
}