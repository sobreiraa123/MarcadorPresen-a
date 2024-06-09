package com.example.presenca.repository;

import com.example.presenca.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {
}
