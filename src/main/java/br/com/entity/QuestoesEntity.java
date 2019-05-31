package br.com.entity;

import org.jsoup.Jsoup;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questoes", schema = "teste")
public class QuestoesEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String enunciado;
    private String enunciadoFormatado;
    private String resposta;
    private String respostaFormatada;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "id_questao")
    private List<OpcoesEntity> opcoesList;


    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "id_disciplina")
    private DisciplinaEntity disciplinaEntity;


    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "id_serie")
    private SerieEntity serieEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String titulo) {
        this.enunciado = titulo;
        setEnunciadoFormatado(titulo);
    }

    public List<OpcoesEntity> getOpcoesList() {
        return opcoesList;
    }

    public void setOpcoesList(List<OpcoesEntity> opcoesList) {
        this.opcoesList = opcoesList;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
        setRespostaFormatada(resposta);
    }

    public DisciplinaEntity getDisciplinaEntity() {
        return disciplinaEntity;
    }

    public void setDisciplinaEntity(DisciplinaEntity disciplinaEntity) {
        this.disciplinaEntity = disciplinaEntity;
    }

    public SerieEntity getSerieEntity() {
        return serieEntity;
    }

    public void setSerieEntity(SerieEntity serieEntity) {
        this.serieEntity = serieEntity;
    }

    public String getEnunciadoFormatado() {
        return enunciadoFormatado;
    }

    private void setEnunciadoFormatado(String enunciadoFormatado) {
        this.enunciadoFormatado = Jsoup.parse(enunciadoFormatado).text();
    }

    public String getRespostaFormatada() {
        return respostaFormatada;
    }

    private void setRespostaFormatada(String respostaFormatado) {
        this.respostaFormatada = Jsoup.parse(respostaFormatado).text();
    }
}
