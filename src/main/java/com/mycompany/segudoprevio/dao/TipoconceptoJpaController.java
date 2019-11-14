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
import com.mycompany.segudoprevio.dto.Tipoconcepto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class TipoconceptoJpaController implements Serializable {

    public TipoconceptoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoconcepto tipoconcepto) throws PreexistingEntityException, Exception {
        if (tipoconcepto.getConceptoList() == null) {
            tipoconcepto.setConceptoList(new ArrayList<Concepto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Concepto> attachedConceptoList = new ArrayList<Concepto>();
            for (Concepto conceptoListConceptoToAttach : tipoconcepto.getConceptoList()) {
                conceptoListConceptoToAttach = em.getReference(conceptoListConceptoToAttach.getClass(), conceptoListConceptoToAttach.getCodconcepto());
                attachedConceptoList.add(conceptoListConceptoToAttach);
            }
            tipoconcepto.setConceptoList(attachedConceptoList);
            em.persist(tipoconcepto);
            for (Concepto conceptoListConcepto : tipoconcepto.getConceptoList()) {
                Tipoconcepto oldTipoOfConceptoListConcepto = conceptoListConcepto.getTipo();
                conceptoListConcepto.setTipo(tipoconcepto);
                conceptoListConcepto = em.merge(conceptoListConcepto);
                if (oldTipoOfConceptoListConcepto != null) {
                    oldTipoOfConceptoListConcepto.getConceptoList().remove(conceptoListConcepto);
                    oldTipoOfConceptoListConcepto = em.merge(oldTipoOfConceptoListConcepto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoconcepto(tipoconcepto.getId()) != null) {
                throw new PreexistingEntityException("Tipoconcepto " + tipoconcepto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoconcepto tipoconcepto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoconcepto persistentTipoconcepto = em.find(Tipoconcepto.class, tipoconcepto.getId());
            List<Concepto> conceptoListOld = persistentTipoconcepto.getConceptoList();
            List<Concepto> conceptoListNew = tipoconcepto.getConceptoList();
            List<Concepto> attachedConceptoListNew = new ArrayList<Concepto>();
            for (Concepto conceptoListNewConceptoToAttach : conceptoListNew) {
                conceptoListNewConceptoToAttach = em.getReference(conceptoListNewConceptoToAttach.getClass(), conceptoListNewConceptoToAttach.getCodconcepto());
                attachedConceptoListNew.add(conceptoListNewConceptoToAttach);
            }
            conceptoListNew = attachedConceptoListNew;
            tipoconcepto.setConceptoList(conceptoListNew);
            tipoconcepto = em.merge(tipoconcepto);
            for (Concepto conceptoListOldConcepto : conceptoListOld) {
                if (!conceptoListNew.contains(conceptoListOldConcepto)) {
                    conceptoListOldConcepto.setTipo(null);
                    conceptoListOldConcepto = em.merge(conceptoListOldConcepto);
                }
            }
            for (Concepto conceptoListNewConcepto : conceptoListNew) {
                if (!conceptoListOld.contains(conceptoListNewConcepto)) {
                    Tipoconcepto oldTipoOfConceptoListNewConcepto = conceptoListNewConcepto.getTipo();
                    conceptoListNewConcepto.setTipo(tipoconcepto);
                    conceptoListNewConcepto = em.merge(conceptoListNewConcepto);
                    if (oldTipoOfConceptoListNewConcepto != null && !oldTipoOfConceptoListNewConcepto.equals(tipoconcepto)) {
                        oldTipoOfConceptoListNewConcepto.getConceptoList().remove(conceptoListNewConcepto);
                        oldTipoOfConceptoListNewConcepto = em.merge(oldTipoOfConceptoListNewConcepto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoconcepto.getId();
                if (findTipoconcepto(id) == null) {
                    throw new NonexistentEntityException("The tipoconcepto with id " + id + " no longer exists.");
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
            Tipoconcepto tipoconcepto;
            try {
                tipoconcepto = em.getReference(Tipoconcepto.class, id);
                tipoconcepto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoconcepto with id " + id + " no longer exists.", enfe);
            }
            List<Concepto> conceptoList = tipoconcepto.getConceptoList();
            for (Concepto conceptoListConcepto : conceptoList) {
                conceptoListConcepto.setTipo(null);
                conceptoListConcepto = em.merge(conceptoListConcepto);
            }
            em.remove(tipoconcepto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipoconcepto> findTipoconceptoEntities() {
        return findTipoconceptoEntities(true, -1, -1);
    }

    public List<Tipoconcepto> findTipoconceptoEntities(int maxResults, int firstResult) {
        return findTipoconceptoEntities(false, maxResults, firstResult);
    }

    private List<Tipoconcepto> findTipoconceptoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoconcepto.class));
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

    public Tipoconcepto findTipoconcepto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoconcepto.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoconceptoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoconcepto> rt = cq.from(Tipoconcepto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
