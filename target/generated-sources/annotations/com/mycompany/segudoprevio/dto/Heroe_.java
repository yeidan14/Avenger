package com.mycompany.segudoprevio.dto;

import com.mycompany.segudoprevio.dto.Estado;
import com.mycompany.segudoprevio.dto.Genero;
import com.mycompany.segudoprevio.dto.Pelicula;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-11-14T08:02:08")
@StaticMetamodel(Heroe.class)
public class Heroe_ { 

    public static volatile SingularAttribute<Heroe, String> descripcion;
    public static volatile SingularAttribute<Heroe, String> heroe;
    public static volatile SingularAttribute<Heroe, Estado> estado;
    public static volatile SingularAttribute<Heroe, Date> fechanacimiento;
    public static volatile ListAttribute<Heroe, Pelicula> peliculaList;
    public static volatile SingularAttribute<Heroe, Genero> genero;
    public static volatile SingularAttribute<Heroe, String> arma;
    public static volatile SingularAttribute<Heroe, Integer> id;
    public static volatile SingularAttribute<Heroe, String> nombre;
    public static volatile SingularAttribute<Heroe, Date> fechaaparicion;

}