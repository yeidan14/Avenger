/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.mycompany.segudoprevio.controller.Conexion;
import com.mycompany.segudoprevio.dao.EmpleadoJpaController;
import com.mycompany.segudoprevio.dto.Empleado;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Daniel
 */
public class prueba {

    public static void main(String[] args) throws Exception {
        Conexion con = Conexion.getConexion();
        EmpleadoJpaController n = new EmpleadoJpaController(con.getBd());

        Date fecha2 = new Date(116, 5, 3);
        Date retiro = new Date(117, 6, 8);
        Date nacimiento = new Date(119, 6, 25);
        Empleado p = new Empleado();
        p.setCodigo("12345");
        p.setCedula("1098102072");
        p.setNombre("Prueba");
        p.setFechaingreso(fecha2);
        p.setFecharetiro(retiro);
        p.setFechanacimiento(nacimiento);
        
        n.create(p);

        System.out.println("creado");
    }

}
