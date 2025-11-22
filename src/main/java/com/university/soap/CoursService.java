package com.university.soap;

import com.university.model.Cours;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface CoursService {

    @WebMethod
    Cours creerCours(
            @WebParam(name = "titre") String titre,
            @WebParam(name = "description") String description,
            @WebParam(name = "enseignantId") Long enseignantId,
            @WebParam(name = "credits") int credits,
            @WebParam(name = "horaires") String horaires,
            @WebParam(name = "capaciteMax") int capaciteMax
    );

    @WebMethod
    Cours getCours(@WebParam(name = "id") Long id);

    @WebMethod
    Cours modifierCours(
            @WebParam(name = "id") Long id,
            @WebParam(name = "titre") String titre,
            @WebParam(name = "description") String description,
            @WebParam(name = "enseignantId") Long enseignantId,
            @WebParam(name = "credits") int credits,
            @WebParam(name = "horaires") String horaires,
            @WebParam(name = "capaciteMax") int capaciteMax
    );

    @WebMethod
    boolean supprimerCours(@WebParam(name = "id") Long id);

    @WebMethod
    List<Cours> listerCours();

    @WebMethod
    boolean verifierDisponibiliteCours(@WebParam(name = "id") Long id);

    @WebMethod
    boolean incrementerNombreInscrits(@WebParam(name = "id") Long id);

    @WebMethod
    boolean decrementerNombreInscrits(@WebParam(name = "id") Long id);
}