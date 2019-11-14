/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;

import com.mycompany.segudoprevio.dao.exceptions.NonexistentEntityException;
import com.mycompany.segudoprevio.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.segudoprevio.dto.Concepto;
import com.mycompany.segudoprevio.dto.Tercero;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class TerceroJpaController implements Serializable {

    public TerceroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tercero tercero) throws PreexistingEntityException, Exception {
        if (tercero.getConceptoList() == null) {
            tercero.setConceptoList(new ArrayList<Concepto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Concepto> attachedConceptoList = new ArrayList<Concepto>();
            for (Concepto conceptoListConceptoToAttach : tercero.getConceptoList()) {
                conceptoListConceptoToAttach = em.getReference(conceptoListConceptoToAttach.getClass(), conceptoListConceptoToAttach.getCodconcepto());
                attachedConceptoList.add(conceptoListConceptoToAttach);
            }
            tercero.setConceptoList(attachedConceptoList);
            em.persist(tercero);
            for (Concepto conceptoListConcepto : tercero.getConceptoList()) {
                Tercero oldCodterceroOfConceptoListConcepto = conceptoListConcepto.getCodtercero();
                conceptoListConcepto.setCodtercero(tercero);
                conceptoListConcepto = em.merge(conceptoListConcepto);
                if (oldCodterceroOfConceptoListConcepto != null) {
                    oldCodterceroOfConceptoListConcepto.getConceptoList().remove(conceptoListConcepto);
                    oldCodterceroOfConceptoListConcepto = em.merge(oldCodterceroOfConceptoListConcepto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTercero(tercero.getCodtercero()) != null) {
                throw new PreexistingEntityException("Tercero " + tercero + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tercero tercero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tercero persistentTercero = em.find(Tercero.class, tercero.getCodtercero());
            List<Concepto> conceptoListOld = persistentTercero.getConceptoList();
            List<Concepto> conceptoListNew = tercero.getConceptoList();
            List<Concepto> attachedConceptoListNew = new ArrayList<Concepto>();
            for (Concepto conceptoListNewConceptoToAttach : conceptoListNew) {
                conceptoListNewConceptoToAttach = em.getReference(conceptoListNewConceptoToAttach.getClass(), conceptoListNewConceptoToAttach.getCodconcepto());
                attachedConceptoListNew.add(conceptoListNewConceptoToAttach);
            }
            conceptoListNew = attachedConceptoListNew;
            tercero.setConceptoList(conceptoListNew);
            tercero = em.merge(tercero);
            for (Concepto conceptoListOldConcepto : conceptoListOld) {
                if (!conceptoListNew.contains(conceptoListOldConcepto)) {
                    conceptoListOldConcepto.setCodtercero(null);
                    conceptoListOldConcepto = em.merge(conceptoListOldConcepto);
                }
            }
            for (Concepto conceptoListNewConcepto : conceptoListNew) {
                if (!conceptoListOld.contains(conceptoListNewConcepto)) {
                    Tercero oldCodterceroOfConceptoListNewConcepto = conceptoListNewConcepto.getCodtercero();
                    conceptoListNewConcepto.setCodtercero(tercero);
                    conceptoListNewConcepto = em.merge(conceptoListNewConcepto);
                    if (oldCodterceroOfConceptoListNewConcepto != null && !oldCodterceroOfConceptoListNewConcepto.equals(tercero)) {
                        oldCodterceroOfConceptoListNewConcepto.getConceptoList().remove(conceptoListNewConcepto);
                        oldCodterceroOfConceptoListNewConcepto = em.merge(oldCodterceroOfConceptoListNewConcepto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tercero.getCodtercero();
                if (findTercero(id) == null) {
                    throw new NonexistentEntityException("The tercero with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tercero tercero;
            try {
                tercero = em.getReference(Tercero.class, id);
                tercero.getCodtercero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tercero with id " + id + " no longer exists.", enfe);
            }
            List<Concepto> conceptoList = tercero.getConceptoList();
            for (Concepto conceptoListConcepto : conceptoList) {
                conceptoListConcepto.setCodtercero(null);
                conceptoListConcepto = em.merge(conceptoListConcepto);
            }
            em.remove(tercero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tercero> findTerceroEntities() {
        return findTerceroEntities(true, -1, -1);
    }

    public List<Tercero> findTerceroEntities(int maxResults, int firstResult) {
        return findTerceroEntities(false, maxResults, firstResult);
    }

    private List<Tercero> findTerceroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tercero.class));
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

    public Tercero findTercero(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tercero.class, id);
        } finally {
            em.close();
        }
    }

    public int getTerceroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tercero> rt = cq.from(Tercero.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
