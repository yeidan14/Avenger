/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;

import com.mycompany.segudoprevio.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.segudoprevio.dto.Proceso;
import com.mycompany.segudoprevio.dto.Empleado;
import com.mycompany.segudoprevio.dto.Concepto;
import com.mycompany.segudoprevio.dto.Liquidacion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class LiquidacionJpaController implements Serializable {

    public LiquidacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Liquidacion liquidacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proceso numproceso = liquidacion.getNumproceso();
            if (numproceso != null) {
                numproceso = em.getReference(numproceso.getClass(), numproceso.getId());
                liquidacion.setNumproceso(numproceso);
            }
            Empleado codempleado = liquidacion.getCodempleado();
            if (codempleado != null) {
                codempleado = em.getReference(codempleado.getClass(), codempleado.getCodigo());
                liquidacion.setCodempleado(codempleado);
            }
            Concepto codconcepto = liquidacion.getCodconcepto();
            if (codconcepto != null) {
                codconcepto = em.getReference(codconcepto.getClass(), codconcepto.getCodconcepto());
                liquidacion.setCodconcepto(codconcepto);
            }
            em.persist(liquidacion);
            if (numproceso != null) {
                numproceso.getLiquidacionList().add(liquidacion);
                numproceso = em.merge(numproceso);
            }
            if (codempleado != null) {
                codempleado.getLiquidacionList().add(liquidacion);
                codempleado = em.merge(codempleado);
            }
            if (codconcepto != null) {
                codconcepto.getLiquidacionList().add(liquidacion);
                codconcepto = em.merge(codconcepto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Liquidacion liquidacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Liquidacion persistentLiquidacion = em.find(Liquidacion.class, liquidacion.getId());
            Proceso numprocesoOld = persistentLiquidacion.getNumproceso();
            Proceso numprocesoNew = liquidacion.getNumproceso();
            Empleado codempleadoOld = persistentLiquidacion.getCodempleado();
            Empleado codempleadoNew = liquidacion.getCodempleado();
            Concepto codconceptoOld = persistentLiquidacion.getCodconcepto();
            Concepto codconceptoNew = liquidacion.getCodconcepto();
            if (numprocesoNew != null) {
                numprocesoNew = em.getReference(numprocesoNew.getClass(), numprocesoNew.getId());
                liquidacion.setNumproceso(numprocesoNew);
            }
            if (codempleadoNew != null) {
                codempleadoNew = em.getReference(codempleadoNew.getClass(), codempleadoNew.getCodigo());
                liquidacion.setCodempleado(codempleadoNew);
            }
            if (codconceptoNew != null) {
                codconceptoNew = em.getReference(codconceptoNew.getClass(), codconceptoNew.getCodconcepto());
                liquidacion.setCodconcepto(codconceptoNew);
            }
            liquidacion = em.merge(liquidacion);
            if (numprocesoOld != null && !numprocesoOld.equals(numprocesoNew)) {
                numprocesoOld.getLiquidacionList().remove(liquidacion);
                numprocesoOld = em.merge(numprocesoOld);
            }
            if (numprocesoNew != null && !numprocesoNew.equals(numprocesoOld)) {
                numprocesoNew.getLiquidacionList().add(liquidacion);
                numprocesoNew = em.merge(numprocesoNew);
            }
            if (codempleadoOld != null && !codempleadoOld.equals(codempleadoNew)) {
                codempleadoOld.getLiquidacionList().remove(liquidacion);
                codempleadoOld = em.merge(codempleadoOld);
            }
            if (codempleadoNew != null && !codempleadoNew.equals(codempleadoOld)) {
                codempleadoNew.getLiquidacionList().add(liquidacion);
                codempleadoNew = em.merge(codempleadoNew);
            }
            if (codconceptoOld != null && !codconceptoOld.equals(codconceptoNew)) {
                codconceptoOld.getLiquidacionList().remove(liquidacion);
                codconceptoOld = em.merge(codconceptoOld);
            }
            if (codconceptoNew != null && !codconceptoNew.equals(codconceptoOld)) {
                codconceptoNew.getLiquidacionList().add(liquidacion);
                codconceptoNew = em.merge(codconceptoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = liquidacion.getId();
                if (findLiquidacion(id) == null) {
                    throw new NonexistentEntityException("The liquidacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Liquidacion liquidacion;
            try {
                liquidacion = em.getReference(Liquidacion.class, id);
                liquidacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The liquidacion with id " + id + " no longer exists.", enfe);
            }
            Proceso numproceso = liquidacion.getNumproceso();
            if (numproceso != null) {
                numproceso.getLiquidacionList().remove(liquidacion);
                numproceso = em.merge(numproceso);
            }
            Empleado codempleado = liquidacion.getCodempleado();
            if (codempleado != null) {
                codempleado.getLiquidacionList().remove(liquidacion);
                codempleado = em.merge(codempleado);
            }
            Concepto codconcepto = liquidacion.getCodconcepto();
            if (codconcepto != null) {
                codconcepto.getLiquidacionList().remove(liquidacion);
                codconcepto = em.merge(codconcepto);
            }
            em.remove(liquidacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Liquidacion> findLiquidacionEntities() {
        return findLiquidacionEntities(true, -1, -1);
    }

    public List<Liquidacion> findLiquidacionEntities(int maxResults, int firstResult) {
        return findLiquidacionEntities(false, maxResults, firstResult);
    }

    private List<Liquidacion> findLiquidacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Liquidacion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Liquidacion findLiquidacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Liquidacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getLiquidacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Liquidacion> rt = cq.from(Liquidacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
