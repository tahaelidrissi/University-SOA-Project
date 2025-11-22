package com.university.soap;

import com.university.model.Enseignant;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface EnseignantService {

    @WebMethod
    Enseignant ajouterEnseignant(
            @WebParam(name = "nom") String nom,
            @WebParam(name = "prenom") String prenom,
            @WebParam(name = "specialite") String specialite,
            @WebParam(name = "email") String email
    );

    @WebMethod
    Enseignant getEnseignant(@WebParam(name = "id") Long id);

    @WebMethod
    Enseignant modifierEnseignant(
            @WebParam(name = "id") Long id,
            @WebParam(name = "nom") String nom,
            @WebParam(name = "prenom") String prenom,
            @WebParam(name = "specialite") String specialite,
            @WebParam(name = "disponible") boolean disponible,
            @WebParam(name = "email") String email
    );

    @WebMethod
    boolean supprimerEnseignant(@WebParam(name = "id") Long id);

    @WebMethod
    List<Enseignant> listerEnseignants();

    @WebMethod
    boolean verifierDisponibilite(@WebParam(name = "id") Long id);
}