/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "pelicula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pelicula.findAll", query = "SELECT p FROM Pelicula p")
    , @NamedQuery(name = "Pelicula.findById", query = "SELECT p FROM Pelicula p WHERE p.id = :id")
    , @NamedQuery(name = "Pelicula.findByNombre", query = "SELECT p FROM Pelicula p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Pelicula.findByAnolanzamiento", query = "SELECT p FROM Pelicula p WHERE p.anolanzamiento = :anolanzamiento")
    , @NamedQuery(name = "Pelicula.findByAnosecuencia", query = "SELECT p FROM Pelicula p WHERE p.anosecuencia = :anosecuencia")})
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "anolanzamiento")
    private Short anolanzamiento;
    @Column(name = "anosecuencia")
    private Short anosecuencia;
    @Lob
    @Size(max = 65535)
    @Column(name = "sinopsis")
    private String sinopsis;
    @JoinTable(name = "ubicacion", joinColumns = {
        @JoinColumn(name = "idpelicula", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "idplaneta", referencedColumnName = "id")})
    @ManyToMany
    private List<Planeta> planetaList;
    @ManyToMany(mappedBy = "peliculaList")
    private List<Heroe> heroeList;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne
    private Clasificacion clasificacion;

    public Pelicula() {
    }

    public Pelicula(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Short getAnolanzamiento() {
        return anolanzamiento;
    }

    public void setAnolanzamiento(Short anolanzamiento) {
        this.anolanzamiento = anolanzamiento;
    }

    public Short getAnosecuencia() {
        return anosecuencia;
    }

    public void setAnosecuencia(Short anosecuencia) {
        this.anosecuencia = anosecuencia;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    @XmlTransient
    public List<Planeta> getPlanetaList() {
        return planetaList;
    }

    public void setPlanetaList(List<Planeta> planetaList) {
        this.planetaList = planetaList;
    }

    @XmlTransient
    public List<Heroe> getHeroeList() {
        return heroeList;
    }

    public void setHeroeList(List<Heroe> heroeList) {
        this.heroeList = heroeList;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pelicula)) {
            return false;
        }
        Pelicula other = (Pelicula) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.segudoprevio.dto.Pelicula[ id=" + id + " ]";
    }
    
}
