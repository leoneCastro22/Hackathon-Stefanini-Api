package com.stefanini.model;

import java.io.Serializable;
import java.util.Random;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_PESSOA_PERFIL")
public class PessoaPerfil implements Serializable {
	
	/**
	 * Serializacao da Classe
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ID da Tabela
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CO_SEQ_PESSOA_PERFIL")
	private Long id;
	/**
	 * ID do Perfil
	 */
	@NotNull
    @Column(name = "CO_SEQ_PERFIL",insertable = false,updatable = false)
    private Long idPerfil;
	/**
	 * ID da Pessoa
	 */
	@NotNull
    @Column(name = "CO_SEQ_PESSOA",insertable = false,updatable = false)
    private Long idPessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_SEQ_PERFIL", referencedColumnName = "CO_SEQ_PERFIL", nullable = false)
    private Perfil perfil;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_SEQ_PESSOA", referencedColumnName = "CO_SEQ_PESSOA", nullable = false)
    private Pessoa pessoa;

    public PessoaPerfil() {
		// TODO Auto-generated constructor stub
	}

    public PessoaPerfil(Perfil perfil, Pessoa pessoa) {
        this.perfil = perfil;
        this.pessoa = pessoa;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
       this.id = id;
    }
}
