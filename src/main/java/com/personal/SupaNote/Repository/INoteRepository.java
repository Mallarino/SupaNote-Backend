package com.personal.SupaNote.Repository;

import com.personal.SupaNote.Models.NoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INoteRepository extends JpaRepository<NoteModel, Long> {

    // Todas las notas de un usuario
    List<NoteModel> findByUserId(Long id);

    // Buscar por título dentro de las notas de un usuario
    List<NoteModel> findByUserIdAndTitleContainingIgnoreCase(Long id, String title);
}
