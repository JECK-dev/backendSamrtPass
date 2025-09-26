package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.Tag;
import com.smartpass.smartpassbackend.repository.PlazaRepository;
import com.smartpass.smartpassbackend.repository.TagRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    private final PlazaRepository plazaRepository;

    public List<Tag> obtenerTodos() {
        return tagRepository.findAll();
    }

    public Optional<Tag> obtenerPorId(Long id) {
        return tagRepository.findById(id);
    }

    public List<Tag> obtenerDisponibles() {
        return tagRepository.findByDisponibleTrue();
    }

    public List<Tag> obtenerOcupados() {
        return tagRepository.findByDisponibleFalse();
    }

    public Tag guardar(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag actualizar(Long numTag, Tag tag) {
        Tag existente = tagRepository.findById(numTag).orElse(null);
        if (existente != null) {
            // numTag no se actualiza porque es el ID (PK)
            existente.setDisponible(tag.getDisponible());
            existente.setPlaza(tag.getPlaza());
            existente.setFechaRegistro(tag.getFechaRegistro());
            existente.setFechaAsignacion(tag.getFechaAsignacion());
            return tagRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Long numTag) {
        tagRepository.deleteById(numTag);
    }


    public TagService(TagRepository tagRepository, PlazaRepository plazaRepository) {
        this.tagRepository = tagRepository;
        this.plazaRepository = plazaRepository;
    }

    /**
     * Cargar tags desde JSON (enviado por frontend)
     */
    public List<Tag> cargarTagsDesdeJson(List<Tag> tags) {
        return tags.stream().map(tag -> {
                    // numTag ya viene desde el JSON
                    tag.setDisponible(true);
                    tag.setFechaRegistro(LocalDateTime.now());
                    tag.setFechaAsignacion(null);

                    // validamos que exista la plaza
                    Integer idPlaza = tag.getPlaza().getNumPlaza();
                    tag.setPlaza(plazaRepository.findById(idPlaza)
                            .orElseThrow(() -> new RuntimeException("Plaza no encontrada con id: " + idPlaza)));

                    return tag;
                }).map(tagRepository::save)
                .collect(Collectors.toList());
    }

    /**
     * Opción para guardar todos en lote
     */
    public List<Tag> cargarTagsDesdeJsonBatch(List<Tag> tags) {
        tags.forEach(tag -> {
            tag.setDisponible(true);
            tag.setFechaRegistro(LocalDateTime.now());
            tag.setFechaAsignacion(null);

            Integer idPlaza = tag.getPlaza().getNumPlaza();
            tag.setPlaza(plazaRepository.findById(idPlaza)
                    .orElseThrow(() -> new RuntimeException("Plaza no encontrada con id: " + idPlaza)));
        });

        return tagRepository.saveAll(tags);
    }




}