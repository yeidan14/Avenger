/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dto;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "heroe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Heroe.findAll", query = "SELECT h FROM Heroe h")
    , @NamedQuery(name = "Heroe.findById", query = "SELECT h FROM Heroe h WHERE h.id = :id")
    , @NamedQuery(name = "Heroe.findByNombre", query = "SELECT h FROM Heroe h WHERE h.nombre = :nombre")
    , @NamedQuery(name = "Heroe.findByHeroe", query = "SELECT h FROM Heroe h WHERE h.heroe = :heroe")
    , @NamedQuery(name = "Heroe.findByFechanacimiento", query = "SELECT h FROM Heroe h WHERE h.fechanacimiento = :fechanacimiento")
    , @NamedQuery(name = "Heroe.findByFechaaparicion", query = "SELECT h FROM Heroe h WHERE h.fechaaparicion = :fechaaparicion")
    , @NamedQuery(name = "Heroe.findByArma", query = "SELECT h FROM Heroe h WHERE h.arma = :arma")})
public class Heroe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 50)
    @Column(name = "heroe")
    private String heroe;
    @Column(name = "fechanacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechanacimiento;
    @Column(name = "fechaaparicion")
    @Temporal(TemporalType.DATE)
    private Date fechaaparicion;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 50)
    @Column(name = "arma")
    private String arma;
    @JoinTable(name = "participacion", joinColumns = {
        @JoinColumn(name = "idheroe", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "idpelicula", referencedColumnName = "id")})
    @ManyToMany
    private List<Pelicula> peliculaList;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne
    private Estado estado;
    @JoinColumn(name = "genero", referencedColumnName = "id")
    @ManyToOne
    private Genero genero;

    public Heroe() {
    }

    public Heroe(Integer id) {
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

    public String getHeroe() {
        return heroe;
    }

    public void setHeroe(String heroe) {
        this.heroe = heroe;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public Date getFechaaparicion() {
        return fechaaparicion;
    }

    public void setFechaaparicion(Date fechaaparicion) {
        this.fechaaparicion = fechaaparicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArma() {
        return arma;
    }

    public void setArma(String arma) {
        this.arma = arma;
    }

    @XmlTransient
    public List<Pelicula> getPeliculaList() {
        return peliculaList;
    }

    public void setPeliculaList(List<Pelicula> peliculaList) {
        this.peliculaList = peliculaList;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
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
        if (!(object instanceof Heroe)) {
            return false;
        }
        Heroe other = (Heroe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.segudoprevio.dto.Heroe[ id=" + id + " ]";
    }
    
}
