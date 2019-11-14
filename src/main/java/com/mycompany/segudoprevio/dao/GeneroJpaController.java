/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;

import com.mycompany.segudoprevio.dao.exceptions.NonexistentEntityException;
import com.mycompany.segudoprevio.dao.exceptions.PreexistingEntityException;
import com.mycompany.segudoprevio.dto.Genero;
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
public class GeneroJpaController implements Serializable {

    public GeneroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Genero genero) throws PreexistingEntityException, Exception {
        if (genero.getHeroeList() == null) {
            genero.setHeroeList(new ArrayList<Heroe>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Heroe> attachedHeroeList = new ArrayList<Heroe>();
            for (Heroe heroeListHeroeToAttach : genero.getHeroeList()) {
                heroeListHeroeToAttach = em.getReference(heroeListHeroeToAttach.getClass(), heroeListHeroeToAttach.getId());
                attachedHeroeList.add(heroeListHeroeToAttach);
            }
            genero.setHeroeList(attachedHeroeList);
            em.persist(genero);
            for (Heroe heroeListHeroe : genero.getHeroeList()) {
                Genero oldGeneroOfHeroeListHeroe = heroeListHeroe.getGenero();
                heroeListHeroe.setGenero(genero);
                heroeListHeroe = em.merge(heroeListHeroe);
                if (oldGeneroOfHeroeListHeroe != null) {
                    oldGeneroOfHeroeListHeroe.getHeroeList().remove(heroeListHeroe);
                    oldGeneroOfHeroeListHeroe = em.merge(oldGeneroOfHeroeListHeroe);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGenero(genero.getId()) != null) {
                throw new PreexistingEntityException("Genero " + genero + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Genero genero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero persistentGenero = em.find(Genero.class, genero.getId());
            List<Heroe> heroeListOld = persistentGenero.getHeroeList();
            List<Heroe> heroeListNew = genero.getHeroeList();
            List<Heroe> attachedHeroeListNew = new ArrayList<Heroe>();
            for (Heroe heroeListNewHeroeToAttach : heroeListNew) {
                heroeListNewHeroeToAttach = em.getReference(heroeListNewHeroeToAttach.getClass(), heroeListNewHeroeToAttach.getId());
                attachedHeroeListNew.add(heroeListNewHeroeToAttach);
            }
            heroeListNew = attachedHeroeListNew;
            genero.setHeroeList(heroeListNew);
            genero = em.merge(genero);
            for (Heroe heroeListOldHeroe : heroeListOld) {
                if (!heroeListNew.contains(heroeListOldHeroe)) {
                    heroeListOldHeroe.setGenero(null);
                    heroeListOldHeroe = em.merge(heroeListOldHeroe);
                }
            }
            for (Heroe heroeListNewHeroe : heroeListNew) {
                if (!heroeListOld.contains(heroeListNewHeroe)) {
                    Genero oldGeneroOfHeroeListNewHeroe = heroeListNewHeroe.getGenero();
                    heroeListNewHeroe.setGenero(genero);
                    heroeListNewHeroe = em.merge(heroeListNewHeroe);
                    if (oldGeneroOfHeroeListNewHeroe != null && !oldGeneroOfHeroeListNewHeroe.equals(genero)) {
                        oldGeneroOfHeroeListNewHeroe.getHeroeList().remove(heroeListNewHeroe);
                        oldGeneroOfHeroeListNewHeroe = em.merge(oldGeneroOfHeroeListNewHeroe);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = genero.getId();
                if (findGenero(id) == null) {
                    throw new NonexistentEntityException("The genero with id " + id + " no longer exists.");
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
            Genero genero;
            try {
                genero = em.getReference(Genero.class, id);
                genero.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genero with id " + id + " no longer exists.", enfe);
            }
            List<Heroe> heroeList = genero.getHeroeList();
            for (Heroe heroeListHeroe : heroeList) {
                heroeListHeroe.setGenero(null);
                heroeListHeroe = em.merge(heroeListHeroe);
            }
            em.remove(genero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Genero> findGeneroEntities() {
        return findGeneroEntities(true, -1, -1);
    }

    public List<Genero> findGeneroEntities(int maxResults, int firstResult) {
        return findGeneroEntities(false, maxResults, firstResult);
    }

    private List<Genero> findGeneroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Genero.class));
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

    public Genero findGenero(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Genero.class, id);
        } finally {
            em.close();
        }
    }

    public int getGeneroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Genero> rt = cq.from(Genero.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
