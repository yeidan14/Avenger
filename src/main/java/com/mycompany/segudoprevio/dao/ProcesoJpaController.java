/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;

import com.mycompany.segudoprevio.dao.exceptions.IllegalOrphanException;
import com.mycompany.segudoprevio.dao.exceptions.NonexistentEntityException;
import com.mycompany.segudoprevio.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.segudoprevio.dto.Liquidacion;
import com.mycompany.segudoprevio.dto.Proceso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class ProcesoJpaController implements Serializable {

    public ProcesoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proceso proceso) throws PreexistingEntityException, Exception {
        if (proceso.getLiquidacionList() == null) {
            proceso.setLiquidacionList(new ArrayList<Liquidacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Liquidacion> attachedLiquidacionList = new ArrayList<Liquidacion>();
            for (Liquidacion liquidacionListLiquidacionToAttach : proceso.getLiquidacionList()) {
                liquidacionListLiquidacionToAttach = em.getReference(liquidacionListLiquidacionToAttach.getClass(), liquidacionListLiquidacionToAttach.getId());
                attachedLiquidacionList.add(liquidacionListLiquidacionToAttach);
            }
            proceso.setLiquidacionList(attachedLiquidacionList);
            em.persist(proceso);
            for (Liquidacion liquidacionListLiquidacion : proceso.getLiquidacionList()) {
                Proceso oldNumprocesoOfLiquidacionListLiquidacion = liquidacionListLiquidacion.getNumproceso();
                liquidacionListLiquidacion.setNumproceso(proceso);
                liquidacionListLiquidacion = em.merge(liquidacionListLiquidacion);
                if (oldNumprocesoOfLiquidacionListLiquidacion != null) {
                    oldNumprocesoOfLiquidacionListLiquidacion.getLiquidacionList().remove(liquidacionListLiquidacion);
                    oldNumprocesoOfLiquidacionListLiquidacion = em.merge(oldNumprocesoOfLiquidacionListLiquidacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProceso(proceso.getId()) != null) {
                throw new PreexistingEntityException("Proceso " + proceso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proceso proceso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proceso persistentProceso = em.find(Proceso.class, proceso.getId());
            List<Liquidacion> liquidacionListOld = persistentProceso.getLiquidacionList();
            List<Liquidacion> liquidacionListNew = proceso.getLiquidacionList();
            List<String> illegalOrphanMessages = null;
            for (Liquidacion liquidacionListOldLiquidacion : liquidacionListOld) {
                if (!liquidacionListNew.contains(liquidacionListOldLiquidacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Liquidacion " + liquidacionListOldLiquidacion + " since its numproceso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Liquidacion> attachedLiquidacionListNew = new ArrayList<Liquidacion>();
            for (Liquidacion liquidacionListNewLiquidacionToAttach : liquidacionListNew) {
                liquidacionListNewLiquidacionToAttach = em.getReference(liquidacionListNewLiquidacionToAttach.getClass(), liquidacionListNewLiquidacionToAttach.getId());
                attachedLiquidacionListNew.add(liquidacionListNewLiquidacionToAttach);
            }
            liquidacionListNew = attachedLiquidacionListNew;
            proceso.setLiquidacionList(liquidacionListNew);
            proceso = em.merge(proceso);
            for (Liquidacion liquidacionListNewLiquidacion : liquidacionListNew) {
                if (!liquidacionListOld.contains(liquidacionListNewLiquidacion)) {
                    Proceso oldNumprocesoOfLiquidacionListNewLiquidacion = liquidacionListNewLiquidacion.getNumproceso();
                    liquidacionListNewLiquidacion.setNumproceso(proceso);
                    liquidacionListNewLiquidacion = em.merge(liquidacionListNewLiquidacion);
                    if (oldNumprocesoOfLiquidacionListNewLiquidacion != null && !oldNumprocesoOfLiquidacionListNewLiquidacion.equals(proceso)) {
                        oldNumprocesoOfLiquidacionListNewLiquidacion.getLiquidacionList().remove(liquidacionListNewLiquidacion);
                        oldNumprocesoOfLiquidacionListNewLiquidacion = em.merge(oldNumprocesoOfLiquidacionListNewLiquidacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proceso.getId();
                if (findProceso(id) == null) {
                    throw new NonexistentEntityException("The proceso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proceso proceso;
            try {
                proceso = em.getReference(Proceso.class, id);
                proceso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proceso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Liquidacion> liquidacionListOrphanCheck = proceso.getLiquidacionList();
            for (Liquidacion liquidacionListOrphanCheckLiquidacion : liquidacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proceso (" + proceso + ") cannot be destroyed since the Liquidacion " + liquidacionListOrphanCheckLiquidacion + " in its liquidacionList field has a non-nullable numproceso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proceso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proceso> findProcesoEntities() {
        return findProcesoEntities(true, -1, -1);
    }

    public List<Proceso> findProcesoEntities(int maxResults, int firstResult) {
        return findProcesoEntities(false, maxResults, firstResult);
    }

    private List<Proceso> findProcesoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proceso.class));
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

    public Proceso findProceso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proceso.class, id);
        } finally {
            em.close();
        }
    }

    public int getProcesoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proceso> rt = cq.from(Proceso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
