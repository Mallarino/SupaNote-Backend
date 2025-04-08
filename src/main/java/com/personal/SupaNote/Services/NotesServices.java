package com.personal.SupaNote.Services;

import com.personal.SupaNote.Models.NoteModel;
import com.personal.SupaNote.Models.UserModel;
import com.personal.SupaNote.Repository.INoteRepository;
import com.personal.SupaNote.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotesServices {

    @Autowired
    INoteRepository noteRepository;

    @Autowired
    IUserRepository userRepository;


    public List<NoteModel> getNotesByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return noteRepository.findByUserId(user.getId());
    }

    public List<NoteModel> getNoteByTitle(Long id, String title){
        return noteRepository.findByUserIdAndTitleContainingIgnoreCase(id, title);
    }

    public NoteModel createNote(NoteModel note){
        return noteRepository.save(note);
    }


    public NoteModel updateNoteById(Long id, NoteModel request){
        return noteRepository.findById(id).
                map(note ->{
                    note.setTitle(request.getTitle());
                    note.setContent(request.getContent());
                    note.setCreatedAt(request.getCreatedAt());
                    return noteRepository.save(note);
                }).orElseThrow(() -> new RuntimeException("Nota no encontrada"));
    }

    public String deleteNoteById(Long id){
        try {
            noteRepository.deleteById(id);
            return "Nota con id " + id + " eliminada con exito";
        } catch (Exception e){
            return "Error al eliminar la nota";
        }
    }


}
