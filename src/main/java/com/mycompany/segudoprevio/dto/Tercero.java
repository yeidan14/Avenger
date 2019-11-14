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
import javax.persistence.Id;
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
@Table(name = "tercero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tercero.findAll", query = "SELECT t FROM Tercero t")
    , @NamedQuery(name = "Tercero.findByCodtercero", query = "SELECT t FROM Tercero t WHERE t.codtercero = :codtercero")
    , @NamedQuery(name = "Tercero.findByDescripcion", query = "SELECT t FROM Tercero t WHERE t.descripcion = :descripcion")})
public class Tercero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codtercero")
    private String codtercero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "codtercero")
    private List<Concepto> conceptoList;

    public Tercero() {
    }

    public Tercero(String codtercero) {
        this.codtercero = codtercero;
    }

    public Tercero(String codtercero, String descripcion) {
        this.codtercero = codtercero;
        this.descripcion = descripcion;
    }

    public String getCodtercero() {
        return codtercero;
    }

    public void setCodtercero(String codtercero) {
        this.codtercero = codtercero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Concepto> getConceptoList() {
        return conceptoList;
    }

    public void setConceptoList(List<Concepto> conceptoList) {
        this.conceptoList = conceptoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtercero != null ? codtercero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tercero)) {
            return false;
        }
        Tercero other = (Tercero) object;
        if ((this.codtercero == null && other.codtercero != null) || (this.codtercero != null && !this.codtercero.equals(other.codtercero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.segudoprevio.dto.Tercero[ codtercero=" + codtercero + " ]";
    }
    
}
