/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;

import com.mycompany.segudoprevio.dao.exceptions.IllegalOrphanException;
import com.mycompany.segudoprevio.dao.exceptions.NonexistentEntityException;
import com.mycompany.segudoprevio.dao.exceptions.PreexistingEntityException;
import com.mycompany.segudoprevio.dto.Empleado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.segudoprevio.dto.Liquidacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws PreexistingEntityException, Exception {
        if (empleado.getLiquidacionList() == null) {
            empleado.setLiquidacionList(new ArrayList<Liquidacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Liquidacion> attachedLiquidacionList = new ArrayList<Liquidacion>();
            for (Liquidacion liquidacionListLiquidacionToAttach : empleado.getLiquidacionList()) {
                liquidacionListLiquidacionToAttach = em.getReference(liquidacionListLiquidacionToAttach.getClass(), liquidacionListLiquidacionToAttach.getId());
                attachedLiquidacionList.add(liquidacionListLiquidacionToAttach);
            }
            empleado.setLiquidacionList(attachedLiquidacionList);
            em.persist(empleado);
            for (Liquidacion liquidacionListLiquidacion : empleado.getLiquidacionList()) {
                Empleado oldCodempleadoOfLiquidacionListLiquidacion = liquidacionListLiquidacion.getCodempleado();
                liquidacionListLiquidacion.setCodempleado(empleado);
                liquidacionListLiquidacion = em.merge(liquidacionListLiquidacion);
                if (oldCodempleadoOfLiquidacionListLiquidacion != null) {
                    oldCodempleadoOfLiquidacionListLiquidacion.getLiquidacionList().remove(liquidacionListLiquidacion);
                    oldCodempleadoOfLiquidacionListLiquidacion = em.merge(oldCodempleadoOfLiquidacionListLiquidacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleado(empleado.getCodigo()) != null) {
                throw new PreexistingEntityException("Empleado " + empleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getCodigo());
            List<Liquidacion> liquidacionListOld = persistentEmpleado.getLiquidacionList();
            List<Liquidacion> liquidacionListNew = empleado.getLiquidacionList();
            List<String> illegalOrphanMessages = null;
            for (Liquidacion liquidacionListOldLiquidacion : liquidacionListOld) {
                if (!liquidacionListNew.contains(liquidacionListOldLiquidacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Liquidacion " + liquidacionListOldLiquidacion + " since its codempleado field is not nullable.");
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
            empleado.setLiquidacionList(liquidacionListNew);
            empleado = em.merge(empleado);
            for (Liquidacion liquidacionListNewLiquidacion : liquidacionListNew) {
                if (!liquidacionListOld.contains(liquidacionListNewLiquidacion)) {
                    Empleado oldCodempleadoOfLiquidacionListNewLiquidacion = liquidacionListNewLiquidacion.getCodempleado();
                    liquidacionListNewLiquidacion.setCodempleado(empleado);
                    liquidacionListNewLiquidacion = em.merge(liquidacionListNewLiquidacion);
                    if (oldCodempleadoOfLiquidacionListNewLiquidacion != null && !oldCodempleadoOfLiquidacionListNewLiquidacion.equals(empleado)) {
                        oldCodempleadoOfLiquidacionListNewLiquidacion.getLiquidacionList().remove(liquidacionListNewLiquidacion);
                        oldCodempleadoOfLiquidacionListNewLiquidacion = em.merge(oldCodempleadoOfLiquidacionListNewLiquidacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = empleado.getCodigo();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Liquidacion> liquidacionListOrphanCheck = empleado.getLiquidacionList();
            for (Liquidacion liquidacionListOrphanCheckLiquidacion : liquidacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Liquidacion " + liquidacionListOrphanCheckLiquidacion + " in its liquidacionList field has a non-nullable codempleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
