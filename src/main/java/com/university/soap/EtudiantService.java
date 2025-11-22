package com.university.soap;

import com.university.model.Etudiant;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface EtudiantService {

    @WebMethod
    Etudiant creerEtudiant(
            @WebParam(name = "nom") String nom,
            @WebParam(name = "prenom") String prenom,
            @WebParam(name = "matricule") String matricule,
            @WebParam(name = "filiere") String filiere,
            @WebParam(name = "annee") int annee,
            @WebParam(name = "email") String email
    );

    @WebMethod
    Etudiant getEtudiant(@WebParam(name = "matricule") String matricule);

    @WebMethod
    Etudiant modifierEtudiant(
            @WebParam(name = "matricule") String matricule,
            @WebParam(name = "nom") String nom,
            @WebParam(name = "prenom") String prenom,
            @WebParam(name = "filiere") String filiere,
            @WebParam(name = "annee") int annee,
            @WebParam(name = "email") String email
    );

    @WebMethod
    boolean supprimerEtudiant(@WebParam(name = "matricule") String matricule);

    @WebMethod
    List<Etudiant> listerEtudiants();

    @WebMethod
    boolean verifierExistence(@WebParam(name = "matricule") String matricule);
}