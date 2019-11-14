package com.mycompany.segudoprevio.dto;

import com.mycompany.segudoprevio.dto.Heroe;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-11-14T07:02:47")
@StaticMetamodel(Estado.class)
public class Estado_ { 

    public static volatile SingularAttribute<Estado, String> descripcion;
    public static volatile SingularAttribute<Estado, String> id;
    public static volatile ListAttribute<Estado, Heroe> heroeList;

}