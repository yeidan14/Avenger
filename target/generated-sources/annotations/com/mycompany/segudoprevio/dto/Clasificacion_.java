package com.mycompany.segudoprevio.dto;

import com.mycompany.segudoprevio.dto.Pelicula;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-11-14T08:02:08")
@StaticMetamodel(Clasificacion.class)
public class Clasificacion_ { 

    public static volatile SingularAttribute<Clasificacion, String> descripcion;
    public static volatile ListAttribute<Clasificacion, Pelicula> peliculaList;
    public static volatile SingularAttribute<Clasificacion, String> id;

}