/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "concepto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Concepto.findAll", query = "SELECT c FROM Concepto c")
    , @NamedQuery(name = "Concepto.findByCodconcepto", query = "SELECT c FROM Concepto c WHERE c.codconcepto = :codconcepto")
    , @NamedQuery(name = "Concepto.findByDescripcion", query = "SELECT c FROM Concepto c WHERE c.descripcion = :descripcion")})
public class Concepto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codconcepto")
    private String codconcepto;
    @Size(max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private Tipoconcepto tipo;
    @JoinColumn(name = "codtercero", referencedColumnName = "codtercero")
    @ManyToOne
    private Tercero codtercero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codconcepto")
    private List<Liquidacion> liquidacionList;

    public Concepto() {
    }

    public Concepto(String codconcepto) {
        this.codconcepto = codconcepto;
    }

    public String getCodconcepto() {
        return codconcepto;
    }

    public void setCodconcepto(String codconcepto) {
        this.codconcepto = codconcepto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Tipoconcepto getTipo() {
        return tipo;
    }

    public void setTipo(Tipoconcepto tipo) {
        this.tipo = tipo;
    }

    public Tercero getCodtercero() {
        return codtercero;
    }

    public void setCodtercero(Tercero codtercero) {
        this.codtercero = codtercero;
    }

    @XmlTransient
    public List<Liquidacion> getLiquidacionList() {
        return liquidacionList;
    }

    public void setLiquidacionList(List<Liquidacion> liquidacionList) {
        this.liquidacionList = liquidacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codconcepto != null ? codconcepto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Concepto)) {
            return false;
        }
        Concepto other = (Concepto) object;
        if ((this.codconcepto == null && other.codconcepto != null) || (this.codconcepto != null && !this.codconcepto.equals(other.codconcepto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.segudoprevio.dto.Concepto[ codconcepto=" + codconcepto + " ]";
    }
    
}
