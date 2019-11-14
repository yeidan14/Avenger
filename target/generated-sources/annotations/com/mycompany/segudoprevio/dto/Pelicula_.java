package com.mycompany.segudoprevio.dto;

import com.mycompany.segudoprevio.dto.Clasificacion;
import com.mycompany.segudoprevio.dto.Heroe;
import com.mycompany.segudoprevio.dto.Planeta;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-11-14T07:02:47")
@StaticMetamodel(Pelicula.class)
public class Pelicula_ { 

    public static volatile SingularAttribute<Pelicula, Short> anolanzamiento;
    public static volatile SingularAttribute<Pelicula, Integer> id;
    public static volatile SingularAttribute<Pelicula, Clasificacion> clasificacion;
    public static volatile ListAttribute<Pelicula, Planeta> planetaList;
    public static volatile SingularAttribute<Pelicula, String> nombre;
    public static volatile ListAttribute<Pelicula, Heroe> heroeList;
    public static volatile SingularAttribute<Pelicula, Short> anosecuencia;
    public static volatile SingularAttribute<Pelicula, String> sinopsis;

}