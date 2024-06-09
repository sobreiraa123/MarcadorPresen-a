package com.example.presenca.service;

import com.example.presenca.model.Presenca;
import com.example.presenca.repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PresencaService {

    @Autowired
    private PresencaRepository presencaRepository;

    public void savePresenca(Presenca presenca) {
        presencaRepository.save(presenca);
    }
}
