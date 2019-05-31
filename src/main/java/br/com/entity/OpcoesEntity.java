package br.com.entity;

import javax.persistence.*;

@Entity
@Table(name = "opcoes", schema = "teste")
public class OpcoesEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String textoResposta;
    private String flResposta;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextoResposta() {
        return textoResposta;
    }

    public void setTextoResposta(String descricao) {
        this.textoResposta = descricao;
    }

    public boolean getFlResposta() {
        return flResposta.equals("S");
    }

    public void setFlResposta(String flResposta) {
        if(new Boolean(flResposta)){
            flResposta = "S";
        }
        else {
            flResposta = "N";
        }
        this.flResposta = flResposta;
    }
}
