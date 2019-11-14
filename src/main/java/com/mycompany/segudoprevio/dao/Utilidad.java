/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.dao;


import com.mycompany.segudoprevio.controller.Conexion;
//import com.mycompany.segudoprevio.dto.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Daniel
 */
public class Utilidad {
    
    
//    
//     public Persona Buscaruser(String documento) {
//       Connection cn=Conexion.getConexion2();
//       Persona u=new Persona();
//        try {
//            String sql = "SELECT * FROM persona"; 
//            PreparedStatement pstm = cn.prepareStatement(sql);
//            ResultSet rs = pstm.executeQuery();
//            
//            while(rs.next()){
//             if(rs.getString("Documento").equals(documento)){
//                
//                String doc=rs.getString(1);
//                String nom=rs.getString(2);
//                String ape=rs.getString(3);
//                String email=rs.getString(4);
//                String edad=rs.getString(5);
//                String nacio=rs.getString(6);
//             
//               
//               u.setDocumento(doc);
//               u.setNombres(nom);
//               u.setApellidos(ape);
//               u.setEmail(email);
//               u.setNacionalidad(nacio);
//               u.setEdad(edad);
//               
//              
//               
//           
//             }
//            }
//            
//        } catch (Exception e) {
//        }
//        return u;
//    }
    
}
