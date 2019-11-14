/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;

import com.mycompany.segudoprevio.dao.exceptions.NonexistentEntityException;
import com.mycompany.segudoprevio.dao.exceptions.PreexistingEntityException;
import com.mycompany.segudoprevio.dto.Estado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.segudoprevio.dto.Heroe;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) throws PreexistingEntityException, Exception {
        if (estado.getHeroeList() == null) {
            estado.setHeroeList(new ArrayList<Heroe>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Heroe> attachedHeroeList = new ArrayList<Heroe>();
            for (Heroe heroeListHeroeToAttach : estado.getHeroeList()) {
                heroeListHeroeToAttach = em.getReference(heroeListHeroeToAttach.getClass(), heroeListHeroeToAttach.getId());
                attachedHeroeList.add(heroeListHeroeToAttach);
            }
            estado.setHeroeList(attachedHeroeList);
            em.persist(estado);
            for (Heroe heroeListHeroe : estado.getHeroeList()) {
                Estado oldEstadoOfHeroeListHeroe = heroeListHeroe.getEstado();
                heroeListHeroe.setEstado(estado);
                heroeListHeroe = em.merge(heroeListHeroe);
                if (oldEstadoOfHeroeListHeroe != null) {
                    oldEstadoOfHeroeListHeroe.getHeroeList().remove(heroeListHeroe);
                    oldEstadoOfHeroeListHeroe = em.merge(oldEstadoOfHeroeListHeroe);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstado(estado.getId()) != null) {
                throw new PreexistingEntityException("Estado " + estado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getId());
            List<Heroe> heroeListOld = persistentEstado.getHeroeList();
            List<Heroe> heroeListNew = estado.getHeroeList();
            List<Heroe> attachedHeroeListNew = new ArrayList<Heroe>();
            for (Heroe heroeListNewHeroeToAttach : heroeListNew) {
                heroeListNewHeroeToAttach = em.getReference(heroeListNewHeroeToAttach.getClass(), heroeListNewHeroeToAttach.getId());
                attachedHeroeListNew.add(heroeListNewHeroeToAttach);
            }
            heroeListNew = attachedHeroeListNew;
            estado.setHeroeList(heroeListNew);
            estado = em.merge(estado);
            for (Heroe heroeListOldHeroe : heroeListOld) {
                if (!heroeListNew.contains(heroeListOldHeroe)) {
                    heroeListOldHeroe.setEstado(null);
                    heroeListOldHeroe = em.merge(heroeListOldHeroe);
                }
            }
            for (Heroe heroeListNewHeroe : heroeListNew) {
                if (!heroeListOld.contains(heroeListNewHeroe)) {
                    Estado oldEstadoOfHeroeListNewHeroe = heroeListNewHeroe.getEstado();
                    heroeListNewHeroe.setEstado(estado);
                    heroeListNewHeroe = em.merge(heroeListNewHeroe);
                    if (oldEstadoOfHeroeListNewHeroe != null && !oldEstadoOfHeroeListNewHeroe.equals(estado)) {
                        oldEstadoOfHeroeListNewHeroe.getHeroeList().remove(heroeListNewHeroe);
                        oldEstadoOfHeroeListNewHeroe = em.merge(oldEstadoOfHeroeListNewHeroe);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = estado.getId();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<Heroe> heroeList = estado.getHeroeList();
            for (Heroe heroeListHeroe : heroeList) {
                heroeListHeroe.setEstado(null);
                heroeListHeroe = em.merge(heroeListHeroe);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
