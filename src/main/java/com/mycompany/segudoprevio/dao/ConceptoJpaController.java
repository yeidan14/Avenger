/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;

import com.mycompany.segudoprevio.dao.exceptions.IllegalOrphanException;
import com.mycompany.segudoprevio.dao.exceptions.NonexistentEntityException;
import com.mycompany.segudoprevio.dao.exceptions.PreexistingEntityException;
import com.mycompany.segudoprevio.dto.Concepto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.segudoprevio.dto.Tipoconcepto;
import com.mycompany.segudoprevio.dto.Tercero;
import com.mycompany.segudoprevio.dto.Liquidacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class ConceptoJpaController implements Serializable {

    public ConceptoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Concepto concepto) throws PreexistingEntityException, Exception {
        if (concepto.getLiquidacionList() == null) {
            concepto.setLiquidacionList(new ArrayList<Liquidacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoconcepto tipo = concepto.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getId());
                concepto.setTipo(tipo);
            }
            Tercero codtercero = concepto.getCodtercero();
            if (codtercero != null) {
                codtercero = em.getReference(codtercero.getClass(), codtercero.getCodtercero());
                concepto.setCodtercero(codtercero);
            }
            List<Liquidacion> attachedLiquidacionList = new ArrayList<Liquidacion>();
            for (Liquidacion liquidacionListLiquidacionToAttach : concepto.getLiquidacionList()) {
                liquidacionListLiquidacionToAttach = em.getReference(liquidacionListLiquidacionToAttach.getClass(), liquidacionListLiquidacionToAttach.getId());
                attachedLiquidacionList.add(liquidacionListLiquidacionToAttach);
            }
            concepto.setLiquidacionList(attachedLiquidacionList);
            em.persist(concepto);
            if (tipo != null) {
                tipo.getConceptoList().add(concepto);
                tipo = em.merge(tipo);
            }
            if (codtercero != null) {
                codtercero.getConceptoList().add(concepto);
                codtercero = em.merge(codtercero);
            }
            for (Liquidacion liquidacionListLiquidacion : concepto.getLiquidacionList()) {
                Concepto oldCodconceptoOfLiquidacionListLiquidacion = liquidacionListLiquidacion.getCodconcepto();
                liquidacionListLiquidacion.setCodconcepto(concepto);
                liquidacionListLiquidacion = em.merge(liquidacionListLiquidacion);
                if (oldCodconceptoOfLiquidacionListLiquidacion != null) {
                    oldCodconceptoOfLiquidacionListLiquidacion.getLiquidacionList().remove(liquidacionListLiquidacion);
                    oldCodconceptoOfLiquidacionListLiquidacion = em.merge(oldCodconceptoOfLiquidacionListLiquidacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConcepto(concepto.getCodconcepto()) != null) {
                throw new PreexistingEntityException("Concepto " + concepto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Concepto concepto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Concepto persistentConcepto = em.find(Concepto.class, concepto.getCodconcepto());
            Tipoconcepto tipoOld = persistentConcepto.getTipo();
            Tipoconcepto tipoNew = concepto.getTipo();
            Tercero codterceroOld = persistentConcepto.getCodtercero();
            Tercero codterceroNew = concepto.getCodtercero();
            List<Liquidacion> liquidacionListOld = persistentConcepto.getLiquidacionList();
            List<Liquidacion> liquidacionListNew = concepto.getLiquidacionList();
            List<String> illegalOrphanMessages = null;
            for (Liquidacion liquidacionListOldLiquidacion : liquidacionListOld) {
                if (!liquidacionListNew.contains(liquidacionListOldLiquidacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Liquidacion " + liquidacionListOldLiquidacion + " since its codconcepto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getId());
                concepto.setTipo(tipoNew);
            }
            if (codterceroNew != null) {
                codterceroNew = em.getReference(codterceroNew.getClass(), codterceroNew.getCodtercero());
                concepto.setCodtercero(codterceroNew);
            }
            List<Liquidacion> attachedLiquidacionListNew = new ArrayList<Liquidacion>();
            for (Liquidacion liquidacionListNewLiquidacionToAttach : liquidacionListNew) {
                liquidacionListNewLiquidacionToAttach = em.getReference(liquidacionListNewLiquidacionToAttach.getClass(), liquidacionListNewLiquidacionToAttach.getId());
                attachedLiquidacionListNew.add(liquidacionListNewLiquidacionToAttach);
            }
            liquidacionListNew = attachedLiquidacionListNew;
            concepto.setLiquidacionList(liquidacionListNew);
            concepto = em.merge(concepto);
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getConceptoList().remove(concepto);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getConceptoList().add(concepto);
                tipoNew = em.merge(tipoNew);
            }
            if (codterceroOld != null && !codterceroOld.equals(codterceroNew)) {
                codterceroOld.getConceptoList().remove(concepto);
                codterceroOld = em.merge(codterceroOld);
            }
            if (codterceroNew != null && !codterceroNew.equals(codterceroOld)) {
                codterceroNew.getConceptoList().add(concepto);
                codterceroNew = em.merge(codterceroNew);
            }
            for (Liquidacion liquidacionListNewLiquidacion : liquidacionListNew) {
                if (!liquidacionListOld.contains(liquidacionListNewLiquidacion)) {
                    Concepto oldCodconceptoOfLiquidacionListNewLiquidacion = liquidacionListNewLiquidacion.getCodconcepto();
                    liquidacionListNewLiquidacion.setCodconcepto(concepto);
                    liquidacionListNewLiquidacion = em.merge(liquidacionListNewLiquidacion);
                    if (oldCodconceptoOfLiquidacionListNewLiquidacion != null && !oldCodconceptoOfLiquidacionListNewLiquidacion.equals(concepto)) {
                        oldCodconceptoOfLiquidacionListNewLiquidacion.getLiquidacionList().remove(liquidacionListNewLiquidacion);
                        oldCodconceptoOfLiquidacionListNewLiquidacion = em.merge(oldCodconceptoOfLiquidacionListNewLiquidacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = concepto.getCodconcepto();
                if (findConcepto(id) == null) {
                    throw new NonexistentEntityException("The concepto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Concepto concepto;
            try {
                concepto = em.getReference(Concepto.class, id);
                concepto.getCodconcepto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The concepto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Liquidacion> liquidacionListOrphanCheck = concepto.getLiquidacionList();
            for (Liquidacion liquidacionListOrphanCheckLiquidacion : liquidacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Concepto (" + concepto + ") cannot be destroyed since the Liquidacion " + liquidacionListOrphanCheckLiquidacion + " in its liquidacionList field has a non-nullable codconcepto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tipoconcepto tipo = concepto.getTipo();
            if (tipo != null) {
                tipo.getConceptoList().remove(concepto);
                tipo = em.merge(tipo);
            }
            Tercero codtercero = concepto.getCodtercero();
            if (codtercero != null) {
                codtercero.getConceptoList().remove(concepto);
                codtercero = em.merge(codtercero);
            }
            em.remove(concepto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Concepto> findConceptoEntities() {
        return findConceptoEntities(true, -1, -1);
    }

    public List<Concepto> findConceptoEntities(int maxResults, int firstResult) {
        return findConceptoEntities(false, maxResults, firstResult);
    }

    private List<Concepto> findConceptoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Concepto.class));
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

    public Concepto findConcepto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Concepto.class, id);
        } finally {
            em.close();
        }
    }

    public int getConceptoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Concepto> rt = cq.from(Concepto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
