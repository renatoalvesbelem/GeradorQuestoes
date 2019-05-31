package br.com.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "series")
public class SerieEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String nomeSerie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeSerie() {
        return nomeSerie;
    }

    public void setNomeSerie(String nomeSerie) {
        this.nomeSerie = nomeSerie;
    }
}
