/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.controller;



import com.mycompany.segudoprevio.dao.EstadoJpaController;
import com.mycompany.segudoprevio.dao.GeneroJpaController;
import com.mycompany.segudoprevio.dao.HeroeJpaController;
import com.mycompany.segudoprevio.dto.Estado;
import com.mycompany.segudoprevio.dto.Genero;
import com.mycompany.segudoprevio.dto.Heroe;
import java.io.IOException;

import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Daniel
 */
public class Agregar extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String heroe = request.getParameter("heroe");        
        String fnacimiento = request.getParameter("fnacimiento");
        Date fn = Date.valueOf(fnacimiento);
        String faparicion = request.getParameter("faparicion");
        Date fi = Date.valueOf(faparicion);
        String genero = request.getParameter("genero");
        String estado = request.getParameter("estado");
        String arma = request.getParameter("arma");
        String descripcion = request.getParameter("descripcion") ;      

       
        Conexion con = Conexion.getConexion();
        HeroeJpaController per = new HeroeJpaController(con.getBd());
         EstadoJpaController e=new EstadoJpaController(con.getBd());
         GeneroJpaController g=new GeneroJpaController(con.getBd());
         
         Heroe  h=new Heroe();
         Genero gen=g.findGenero(genero);
         Estado est=e.findEstado(estado);
         h.setNombre(nombre);
         h.setHeroe(heroe);
         h.setFechanacimiento(fn);
         h.setFechaaparicion(fi);
         h.setGenero(gen);
         h.setEstado(est);
         h.setArma(arma);
         h.setDescripcion(descripcion);

        try {
          
                per.create(h);
                String registrado = "registrado";
                request.setAttribute("registrado", registrado);
                request.getRequestDispatcher("Agregar.jsp").forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(Agregar.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("erroruser.jsp");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
