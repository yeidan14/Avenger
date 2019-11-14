/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;

import com.mycompany.segudoprevio.dao.exceptions.NonexistentEntityException;
import com.mycompany.segudoprevio.dao.exceptions.PreexistingEntityException;
import com.mycompany.segudoprevio.dto.Clasificacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.segudoprevio.dto.Pelicula;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class ClasificacionJpaController implements Serializable {

    public ClasificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clasificacion clasificacion) throws PreexistingEntityException, Exception {
        if (clasificacion.getPeliculaList() == null) {
            clasificacion.setPeliculaList(new ArrayList<Pelicula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pelicula> attachedPeliculaList = new ArrayList<Pelicula>();
            for (Pelicula peliculaListPeliculaToAttach : clasificacion.getPeliculaList()) {
                peliculaListPeliculaToAttach = em.getReference(peliculaListPeliculaToAttach.getClass(), peliculaListPeliculaToAttach.getId());
                attachedPeliculaList.add(peliculaListPeliculaToAttach);
            }
            clasificacion.setPeliculaList(attachedPeliculaList);
            em.persist(clasificacion);
            for (Pelicula peliculaListPelicula : clasificacion.getPeliculaList()) {
                Clasificacion oldClasificacionOfPeliculaListPelicula = peliculaListPelicula.getClasificacion();
                peliculaListPelicula.setClasificacion(clasificacion);
                peliculaListPelicula = em.merge(peliculaListPelicula);
                if (oldClasificacionOfPeliculaListPelicula != null) {
                    oldClasificacionOfPeliculaListPelicula.getPeliculaList().remove(peliculaListPelicula);
                    oldClasificacionOfPeliculaListPelicula = em.merge(oldClasificacionOfPeliculaListPelicula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClasificacion(clasificacion.getId()) != null) {
                throw new PreexistingEntityException("Clasificacion " + clasificacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clasificacion clasificacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clasificacion persistentClasificacion = em.find(Clasificacion.class, clasificacion.getId());
            List<Pelicula> peliculaListOld = persistentClasificacion.getPeliculaList();
            List<Pelicula> peliculaListNew = clasificacion.getPeliculaList();
            List<Pelicula> attachedPeliculaListNew = new ArrayList<Pelicula>();
            for (Pelicula peliculaListNewPeliculaToAttach : peliculaListNew) {
                peliculaListNewPeliculaToAttach = em.getReference(peliculaListNewPeliculaToAttach.getClass(), peliculaListNewPeliculaToAttach.getId());
                attachedPeliculaListNew.add(peliculaListNewPeliculaToAttach);
            }
            peliculaListNew = attachedPeliculaListNew;
            clasificacion.setPeliculaList(peliculaListNew);
            clasificacion = em.merge(clasificacion);
            for (Pelicula peliculaListOldPelicula : peliculaListOld) {
                if (!peliculaListNew.contains(peliculaListOldPelicula)) {
                    peliculaListOldPelicula.setClasificacion(null);
                    peliculaListOldPelicula = em.merge(peliculaListOldPelicula);
                }
            }
            for (Pelicula peliculaListNewPelicula : peliculaListNew) {
                if (!peliculaListOld.contains(peliculaListNewPelicula)) {
                    Clasificacion oldClasificacionOfPeliculaListNewPelicula = peliculaListNewPelicula.getClasificacion();
                    peliculaListNewPelicula.setClasificacion(clasificacion);
                    peliculaListNewPelicula = em.merge(peliculaListNewPelicula);
                    if (oldClasificacionOfPeliculaListNewPelicula != null && !oldClasificacionOfPeliculaListNewPelicula.equals(clasificacion)) {
                        oldClasificacionOfPeliculaListNewPelicula.getPeliculaList().remove(peliculaListNewPelicula);
                        oldClasificacionOfPeliculaListNewPelicula = em.merge(oldClasificacionOfPeliculaListNewPelicula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = clasificacion.getId();
                if (findClasificacion(id) == null) {
                    throw new NonexistentEntityException("The clasificacion with id " + id + " no longer exists.");
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
            Clasificacion clasificacion;
            try {
                clasificacion = em.getReference(Clasificacion.class, id);
                clasificacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clasificacion with id " + id + " no longer exists.", enfe);
            }
            List<Pelicula> peliculaList = clasificacion.getPeliculaList();
            for (Pelicula peliculaListPelicula : peliculaList) {
                peliculaListPelicula.setClasificacion(null);
                peliculaListPelicula = em.merge(peliculaListPelicula);
            }
            em.remove(clasificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clasificacion> findClasificacionEntities() {
        return findClasificacionEntities(true, -1, -1);
    }

    public List<Clasificacion> findClasificacionEntities(int maxResults, int firstResult) {
        return findClasificacionEntities(false, maxResults, firstResult);
    }

    private List<Clasificacion> findClasificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clasificacion.class));
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

    public Clasificacion findClasificacion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clasificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getClasificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clasificacion> rt = cq.from(Clasificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
