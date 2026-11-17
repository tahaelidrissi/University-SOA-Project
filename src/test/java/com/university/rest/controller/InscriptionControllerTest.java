package com.university.rest.controller;

import com.university.model.Inscription;
import com.university.rest.service.InscriptionService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InscriptionController.class)
class InscriptionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InscriptionService inscriptionService;

    @MockBean
    private RuntimeService runtimeService;

    private static final String BODY = "{\"matriculeEtudiant\":\"E1\",\"coursId\":3}";

    @Test
    void post_demarreLeProcessusBpmn_etRepond202() throws Exception {
        ProcessInstance instance = mock(ProcessInstance.class);
        when(instance.getId()).thenReturn("pi-42");
        when(runtimeService.startProcessInstanceByKey(eq("processusInscription"), anyMap()))
                .thenReturn(instance);

        mvc.perform(post("/api/inscriptions").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("EN_TRAITEMENT"))
                .andExpect(jsonPath("$.processInstanceId").value("pi-42"));

        verify(runtimeService).startProcessInstanceByKey(
                eq("processusInscription"),
                eq(Map.<String, Object>of("matriculeEtudiant", "E1", "coursId", 3L)));
    }

    @Test
    void post_erreurMoteur_renvoie500() throws Exception {
        when(runtimeService.startProcessInstanceByKey(eq("processusInscription"), anyMap()))
                .thenThrow(new RuntimeException("moteur indisponible"));

        mvc.perform(post("/api/inscriptions").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("moteur indisponible"));
    }

    @Test
    void save_creeLInscription_etRepond201() throws Exception {
        when(inscriptionService.creerInscription("E1", 3L)).thenReturn(new Inscription(10L, "E1", 3L));

        mvc.perform(post("/api/inscriptions/save").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.statut").value("ACTIVE"));
    }

    @Test
    void save_doublon_renvoie400() throws Exception {
        when(inscriptionService.creerInscription("E1", 3L))
                .thenThrow(new RuntimeException("Étudiant déjà inscrit à ce cours"));

        mvc.perform(post("/api/inscriptions/save").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Étudiant déjà inscrit à ce cours"));
    }

    @Test
    void get_inscriptionInexistante_renvoie404() throws Exception {
        when(inscriptionService.getInscription(99L)).thenThrow(new RuntimeException("Inscription non trouvée"));

        mvc.perform(get("/api/inscriptions/99")).andExpect(status().isNotFound());
    }

    @Test
    void lister_renvoieLaListe() throws Exception {
        when(inscriptionService.listerInscriptions()).thenReturn(Collections.singletonList(new Inscription(1L, "E1", 3L)));

        mvc.perform(get("/api/inscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matriculeEtudiant").value("E1"));
    }

    @Test
    void delete_annuleLInscription() throws Exception {
        mvc.perform(delete("/api/inscriptions/1")).andExpect(status().isOk());
        verify(inscriptionService).annulerInscription(1L);
    }
}
