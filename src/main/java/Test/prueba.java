/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.mycompany.segudoprevio.controller.Conexion;
import com.mycompany.segudoprevio.dao.EstadoJpaController;
import com.mycompany.segudoprevio.dao.GeneroJpaController;
import com.mycompany.segudoprevio.dao.HeroeJpaController;
import com.mycompany.segudoprevio.dto.Estado;
import com.mycompany.segudoprevio.dto.Genero;
import com.mycompany.segudoprevio.dto.Heroe;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Daniel
 */
public class prueba {

    public static void main(String[] args) throws Exception {
        Conexion con = Conexion.getConexion();
         HeroeJpaController n = new HeroeJpaController(con.getBd());
         EstadoJpaController e=new EstadoJpaController(con.getBd());
         GeneroJpaController g=new GeneroJpaController(con.getBd());
        Date fecha2 = new Date(116, 5, 3);      
        Date nacimiento = new Date(119, 6, 25);
        Estado es= e.findEstado("A");       
        Genero gen=g.findGenero("A");
       
        Heroe p = new Heroe();
       
        p.setId(85);
        p.setNombre("daniel");
        p.setEstado(es);
        p.setGenero(gen);
        p.setDescripcion("andoride del planeta 1");
        p.setArma("espada");
        p.setFechanacimiento(nacimiento);
        p.setFechaaparicion(fecha2);
        p.setHeroe("samurai");
        
        n.create(p);

        System.out.println("creado");
    }

}
