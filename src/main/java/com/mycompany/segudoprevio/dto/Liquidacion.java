/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "liquidacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Liquidacion.findAll", query = "SELECT l FROM Liquidacion l")
    , @NamedQuery(name = "Liquidacion.findByCodmodelo", query = "SELECT l FROM Liquidacion l WHERE l.codmodelo = :codmodelo")
    , @NamedQuery(name = "Liquidacion.findByValor", query = "SELECT l FROM Liquidacion l WHERE l.valor = :valor")
    , @NamedQuery(name = "Liquidacion.findByFecha", query = "SELECT l FROM Liquidacion l WHERE l.fecha = :fecha")
    , @NamedQuery(name = "Liquidacion.findById", query = "SELECT l FROM Liquidacion l WHERE l.id = :id")})
public class Liquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codmodelo")
    private int codmodelo;
    @Column(name = "valor")
    private Long valor;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "numproceso", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proceso numproceso;
    @JoinColumn(name = "codempleado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Empleado codempleado;
    @JoinColumn(name = "codconcepto", referencedColumnName = "codconcepto")
    @ManyToOne(optional = false)
    private Concepto codconcepto;

    public Liquidacion() {
    }

    public Liquidacion(Integer id) {
        this.id = id;
    }

    public Liquidacion(Integer id, int codmodelo) {
        this.id = id;
        this.codmodelo = codmodelo;
    }

    public int getCodmodelo() {
        return codmodelo;
    }

    public void setCodmodelo(int codmodelo) {
        this.codmodelo = codmodelo;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proceso getNumproceso() {
        return numproceso;
    }

    public void setNumproceso(Proceso numproceso) {
        this.numproceso = numproceso;
    }

    public Empleado getCodempleado() {
        return codempleado;
    }

    public void setCodempleado(Empleado codempleado) {
        this.codempleado = codempleado;
    }

    public Concepto getCodconcepto() {
        return codconcepto;
    }

    public void setCodconcepto(Concepto codconcepto) {
        this.codconcepto = codconcepto;
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
        if (!(object instanceof Liquidacion)) {
            return false;
        }
        Liquidacion other = (Liquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.segudoprevio.dto.Liquidacion[ id=" + id + " ]";
    }
    
}
