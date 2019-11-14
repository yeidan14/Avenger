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
import com.mycompany.segudoprevio.dto.Estado;
import com.mycompany.segudoprevio.dto.Genero;
import com.mycompany.segudoprevio.dto.Heroe;
import com.mycompany.segudoprevio.dto.Pelicula;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class HeroeJpaController implements Serializable {

    public HeroeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Heroe heroe) {
        if (heroe.getPeliculaList() == null) {
            heroe.setPeliculaList(new ArrayList<Pelicula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado estado = heroe.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getId());
                heroe.setEstado(estado);
            }
            Genero genero = heroe.getGenero();
            if (genero != null) {
                genero = em.getReference(genero.getClass(), genero.getId());
                heroe.setGenero(genero);
            }
            List<Pelicula> attachedPeliculaList = new ArrayList<Pelicula>();
            for (Pelicula peliculaListPeliculaToAttach : heroe.getPeliculaList()) {
                peliculaListPeliculaToAttach = em.getReference(peliculaListPeliculaToAttach.getClass(), peliculaListPeliculaToAttach.getId());
                attachedPeliculaList.add(peliculaListPeliculaToAttach);
            }
            heroe.setPeliculaList(attachedPeliculaList);
            em.persist(heroe);
            if (estado != null) {
                estado.getHeroeList().add(heroe);
                estado = em.merge(estado);
            }
            if (genero != null) {
                genero.getHeroeList().add(heroe);
                genero = em.merge(genero);
            }
            for (Pelicula peliculaListPelicula : heroe.getPeliculaList()) {
                peliculaListPelicula.getHeroeList().add(heroe);
                peliculaListPelicula = em.merge(peliculaListPelicula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Heroe heroe) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Heroe persistentHeroe = em.find(Heroe.class, heroe.getId());
            Estado estadoOld = persistentHeroe.getEstado();
            Estado estadoNew = heroe.getEstado();
            Genero generoOld = persistentHeroe.getGenero();
            Genero generoNew = heroe.getGenero();
            List<Pelicula> peliculaListOld = persistentHeroe.getPeliculaList();
            List<Pelicula> peliculaListNew = heroe.getPeliculaList();
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getId());
                heroe.setEstado(estadoNew);
            }
            if (generoNew != null) {
                generoNew = em.getReference(generoNew.getClass(), generoNew.getId());
                heroe.setGenero(generoNew);
            }
            List<Pelicula> attachedPeliculaListNew = new ArrayList<Pelicula>();
            for (Pelicula peliculaListNewPeliculaToAttach : peliculaListNew) {
                peliculaListNewPeliculaToAttach = em.getReference(peliculaListNewPeliculaToAttach.getClass(), peliculaListNewPeliculaToAttach.getId());
                attachedPeliculaListNew.add(peliculaListNewPeliculaToAttach);
            }
            peliculaListNew = attachedPeliculaListNew;
            heroe.setPeliculaList(peliculaListNew);
            heroe = em.merge(heroe);
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getHeroeList().remove(heroe);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getHeroeList().add(heroe);
                estadoNew = em.merge(estadoNew);
            }
            if (generoOld != null && !generoOld.equals(generoNew)) {
                generoOld.getHeroeList().remove(heroe);
                generoOld = em.merge(generoOld);
            }
            if (generoNew != null && !generoNew.equals(generoOld)) {
                generoNew.getHeroeList().add(heroe);
                generoNew = em.merge(generoNew);
            }
            for (Pelicula peliculaListOldPelicula : peliculaListOld) {
                if (!peliculaListNew.contains(peliculaListOldPelicula)) {
                    peliculaListOldPelicula.getHeroeList().remove(heroe);
                    peliculaListOldPelicula = em.merge(peliculaListOldPelicula);
                }
            }
            for (Pelicula peliculaListNewPelicula : peliculaListNew) {
                if (!peliculaListOld.contains(peliculaListNewPelicula)) {
                    peliculaListNewPelicula.getHeroeList().add(heroe);
                    peliculaListNewPelicula = em.merge(peliculaListNewPelicula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = heroe.getId();
                if (findHeroe(id) == null) {
                    throw new NonexistentEntityException("The heroe with id " + id + " no longer exists.");
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
            Heroe heroe;
            try {
                heroe = em.getReference(Heroe.class, id);
                heroe.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The heroe with id " + id + " no longer exists.", enfe);
            }
            Estado estado = heroe.getEstado();
            if (estado != null) {
                estado.getHeroeList().remove(heroe);
                estado = em.merge(estado);
            }
            Genero genero = heroe.getGenero();
            if (genero != null) {
                genero.getHeroeList().remove(heroe);
                genero = em.merge(genero);
            }
            List<Pelicula> peliculaList = heroe.getPeliculaList();
            for (Pelicula peliculaListPelicula : peliculaList) {
                peliculaListPelicula.getHeroeList().remove(heroe);
                peliculaListPelicula = em.merge(peliculaListPelicula);
            }
            em.remove(heroe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Heroe> findHeroeEntities() {
        return findHeroeEntities(true, -1, -1);
    }

    public List<Heroe> findHeroeEntities(int maxResults, int firstResult) {
        return findHeroeEntities(false, maxResults, firstResult);
    }

    private List<Heroe> findHeroeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Heroe.class));
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

    public Heroe findHeroe(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Heroe.class, id);
        } finally {
            em.close();
        }
    }

    public int getHeroeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Heroe> rt = cq.from(Heroe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
