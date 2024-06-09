package com.example.presenca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Presenca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroAluno;
    private String nome;
    private String disciplina;
    private String docente;
    private LocalDateTime dataHora;
    private String curso;


    public Presenca() {
    }

    public Presenca(String numeroAluno, String nome, String disciplina, String docente, LocalDateTime dataHora, String curso) {
        this.numeroAluno = numeroAluno;
        this.nome = nome;
        this.disciplina = disciplina;
        this.docente = docente;
        this.dataHora = dataHora;
        this.curso = curso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroAluno() {
        return numeroAluno;
    }

    public void setNumeroAluno(String numeroAluno) {
        this.numeroAluno = numeroAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    @Override
public String toString() {
    return numeroAluno + "," + nome + "," + disciplina + "," + docente + "," + dataHora.toString() + "," + curso;
}

}
