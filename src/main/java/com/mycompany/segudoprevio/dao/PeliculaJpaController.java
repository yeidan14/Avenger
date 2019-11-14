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
import com.mycompany.segudoprevio.dto.Clasificacion;
import com.mycompany.segudoprevio.dto.Planeta;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.segudoprevio.dto.Heroe;
import com.mycompany.segudoprevio.dto.Pelicula;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class PeliculaJpaController implements Serializable {

    public PeliculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pelicula pelicula) {
        if (pelicula.getPlanetaList() == null) {
            pelicula.setPlanetaList(new ArrayList<Planeta>());
        }
        if (pelicula.getHeroeList() == null) {
            pelicula.setHeroeList(new ArrayList<Heroe>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clasificacion clasificacion = pelicula.getClasificacion();
            if (clasificacion != null) {
                clasificacion = em.getReference(clasificacion.getClass(), clasificacion.getId());
                pelicula.setClasificacion(clasificacion);
            }
            List<Planeta> attachedPlanetaList = new ArrayList<Planeta>();
            for (Planeta planetaListPlanetaToAttach : pelicula.getPlanetaList()) {
                planetaListPlanetaToAttach = em.getReference(planetaListPlanetaToAttach.getClass(), planetaListPlanetaToAttach.getId());
                attachedPlanetaList.add(planetaListPlanetaToAttach);
            }
            pelicula.setPlanetaList(attachedPlanetaList);
            List<Heroe> attachedHeroeList = new ArrayList<Heroe>();
            for (Heroe heroeListHeroeToAttach : pelicula.getHeroeList()) {
                heroeListHeroeToAttach = em.getReference(heroeListHeroeToAttach.getClass(), heroeListHeroeToAttach.getId());
                attachedHeroeList.add(heroeListHeroeToAttach);
            }
            pelicula.setHeroeList(attachedHeroeList);
            em.persist(pelicula);
            if (clasificacion != null) {
                clasificacion.getPeliculaList().add(pelicula);
                clasificacion = em.merge(clasificacion);
            }
            for (Planeta planetaListPlaneta : pelicula.getPlanetaList()) {
                planetaListPlaneta.getPeliculaList().add(pelicula);
                planetaListPlaneta = em.merge(planetaListPlaneta);
            }
            for (Heroe heroeListHeroe : pelicula.getHeroeList()) {
                heroeListHeroe.getPeliculaList().add(pelicula);
                heroeListHeroe = em.merge(heroeListHeroe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pelicula pelicula) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pelicula persistentPelicula = em.find(Pelicula.class, pelicula.getId());
            Clasificacion clasificacionOld = persistentPelicula.getClasificacion();
            Clasificacion clasificacionNew = pelicula.getClasificacion();
            List<Planeta> planetaListOld = persistentPelicula.getPlanetaList();
            List<Planeta> planetaListNew = pelicula.getPlanetaList();
            List<Heroe> heroeListOld = persistentPelicula.getHeroeList();
            List<Heroe> heroeListNew = pelicula.getHeroeList();
            if (clasificacionNew != null) {
                clasificacionNew = em.getReference(clasificacionNew.getClass(), clasificacionNew.getId());
                pelicula.setClasificacion(clasificacionNew);
            }
            List<Planeta> attachedPlanetaListNew = new ArrayList<Planeta>();
            for (Planeta planetaListNewPlanetaToAttach : planetaListNew) {
                planetaListNewPlanetaToAttach = em.getReference(planetaListNewPlanetaToAttach.getClass(), planetaListNewPlanetaToAttach.getId());
                attachedPlanetaListNew.add(planetaListNewPlanetaToAttach);
            }
            planetaListNew = attachedPlanetaListNew;
            pelicula.setPlanetaList(planetaListNew);
            List<Heroe> attachedHeroeListNew = new ArrayList<Heroe>();
            for (Heroe heroeListNewHeroeToAttach : heroeListNew) {
                heroeListNewHeroeToAttach = em.getReference(heroeListNewHeroeToAttach.getClass(), heroeListNewHeroeToAttach.getId());
                attachedHeroeListNew.add(heroeListNewHeroeToAttach);
            }
            heroeListNew = attachedHeroeListNew;
            pelicula.setHeroeList(heroeListNew);
            pelicula = em.merge(pelicula);
            if (clasificacionOld != null && !clasificacionOld.equals(clasificacionNew)) {
                clasificacionOld.getPeliculaList().remove(pelicula);
                clasificacionOld = em.merge(clasificacionOld);
            }
            if (clasificacionNew != null && !clasificacionNew.equals(clasificacionOld)) {
                clasificacionNew.getPeliculaList().add(pelicula);
                clasificacionNew = em.merge(clasificacionNew);
            }
            for (Planeta planetaListOldPlaneta : planetaListOld) {
                if (!planetaListNew.contains(planetaListOldPlaneta)) {
                    planetaListOldPlaneta.getPeliculaList().remove(pelicula);
                    planetaListOldPlaneta = em.merge(planetaListOldPlaneta);
                }
            }
            for (Planeta planetaListNewPlaneta : planetaListNew) {
                if (!planetaListOld.contains(planetaListNewPlaneta)) {
                    planetaListNewPlaneta.getPeliculaList().add(pelicula);
                    planetaListNewPlaneta = em.merge(planetaListNewPlaneta);
                }
            }
            for (Heroe heroeListOldHeroe : heroeListOld) {
                if (!heroeListNew.contains(heroeListOldHeroe)) {
                    heroeListOldHeroe.getPeliculaList().remove(pelicula);
                    heroeListOldHeroe = em.merge(heroeListOldHeroe);
                }
            }
            for (Heroe heroeListNewHeroe : heroeListNew) {
                if (!heroeListOld.contains(heroeListNewHeroe)) {
                    heroeListNewHeroe.getPeliculaList().add(pelicula);
                    heroeListNewHeroe = em.merge(heroeListNewHeroe);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pelicula.getId();
                if (findPelicula(id) == null) {
                    throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.");
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
            Pelicula pelicula;
            try {
                pelicula = em.getReference(Pelicula.class, id);
                pelicula.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.", enfe);
            }
            Clasificacion clasificacion = pelicula.getClasificacion();
            if (clasificacion != null) {
                clasificacion.getPeliculaList().remove(pelicula);
                clasificacion = em.merge(clasificacion);
            }
            List<Planeta> planetaList = pelicula.getPlanetaList();
            for (Planeta planetaListPlaneta : planetaList) {
                planetaListPlaneta.getPeliculaList().remove(pelicula);
                planetaListPlaneta = em.merge(planetaListPlaneta);
            }
            List<Heroe> heroeList = pelicula.getHeroeList();
            for (Heroe heroeListHeroe : heroeList) {
                heroeListHeroe.getPeliculaList().remove(pelicula);
                heroeListHeroe = em.merge(heroeListHeroe);
            }
            em.remove(pelicula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pelicula> findPeliculaEntities() {
        return findPeliculaEntities(true, -1, -1);
    }

    public List<Pelicula> findPeliculaEntities(int maxResults, int firstResult) {
        return findPeliculaEntities(false, maxResults, firstResult);
    }

    private List<Pelicula> findPeliculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pelicula.class));
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

    public Pelicula findPelicula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pelicula.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeliculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pelicula> rt = cq.from(Pelicula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
