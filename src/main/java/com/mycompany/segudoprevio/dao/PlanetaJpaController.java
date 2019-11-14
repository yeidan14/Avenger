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
import com.mycompany.segudoprevio.dto.Pelicula;
import com.mycompany.segudoprevio.dto.Planeta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class PlanetaJpaController implements Serializable {

    public PlanetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Planeta planeta) throws PreexistingEntityException, Exception {
        if (planeta.getPeliculaList() == null) {
            planeta.setPeliculaList(new ArrayList<Pelicula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pelicula> attachedPeliculaList = new ArrayList<Pelicula>();
            for (Pelicula peliculaListPeliculaToAttach : planeta.getPeliculaList()) {
                peliculaListPeliculaToAttach = em.getReference(peliculaListPeliculaToAttach.getClass(), peliculaListPeliculaToAttach.getId());
                attachedPeliculaList.add(peliculaListPeliculaToAttach);
            }
            planeta.setPeliculaList(attachedPeliculaList);
            em.persist(planeta);
            for (Pelicula peliculaListPelicula : planeta.getPeliculaList()) {
                peliculaListPelicula.getPlanetaList().add(planeta);
                peliculaListPelicula = em.merge(peliculaListPelicula);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPlaneta(planeta.getId()) != null) {
                throw new PreexistingEntityException("Planeta " + planeta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Planeta planeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Planeta persistentPlaneta = em.find(Planeta.class, planeta.getId());
            List<Pelicula> peliculaListOld = persistentPlaneta.getPeliculaList();
            List<Pelicula> peliculaListNew = planeta.getPeliculaList();
            List<Pelicula> attachedPeliculaListNew = new ArrayList<Pelicula>();
            for (Pelicula peliculaListNewPeliculaToAttach : peliculaListNew) {
                peliculaListNewPeliculaToAttach = em.getReference(peliculaListNewPeliculaToAttach.getClass(), peliculaListNewPeliculaToAttach.getId());
                attachedPeliculaListNew.add(peliculaListNewPeliculaToAttach);
            }
            peliculaListNew = attachedPeliculaListNew;
            planeta.setPeliculaList(peliculaListNew);
            planeta = em.merge(planeta);
            for (Pelicula peliculaListOldPelicula : peliculaListOld) {
                if (!peliculaListNew.contains(peliculaListOldPelicula)) {
                    peliculaListOldPelicula.getPlanetaList().remove(planeta);
                    peliculaListOldPelicula = em.merge(peliculaListOldPelicula);
                }
            }
            for (Pelicula peliculaListNewPelicula : peliculaListNew) {
                if (!peliculaListOld.contains(peliculaListNewPelicula)) {
                    peliculaListNewPelicula.getPlanetaList().add(planeta);
                    peliculaListNewPelicula = em.merge(peliculaListNewPelicula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = planeta.getId();
                if (findPlaneta(id) == null) {
                    throw new NonexistentEntityException("The planeta with id " + id + " no longer exists.");
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
            Planeta planeta;
            try {
                planeta = em.getReference(Planeta.class, id);
                planeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The planeta with id " + id + " no longer exists.", enfe);
            }
            List<Pelicula> peliculaList = planeta.getPeliculaList();
            for (Pelicula peliculaListPelicula : peliculaList) {
                peliculaListPelicula.getPlanetaList().remove(planeta);
                peliculaListPelicula = em.merge(peliculaListPelicula);
            }
            em.remove(planeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Planeta> findPlanetaEntities() {
        return findPlanetaEntities(true, -1, -1);
    }

    public List<Planeta> findPlanetaEntities(int maxResults, int firstResult) {
        return findPlanetaEntities(false, maxResults, firstResult);
    }

    private List<Planeta> findPlanetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Planeta.class));
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

    public Planeta findPlaneta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Planeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Planeta> rt = cq.from(Planeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
