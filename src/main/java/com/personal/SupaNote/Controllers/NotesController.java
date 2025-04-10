package com.personal.SupaNote.Controllers;

import com.personal.SupaNote.Models.NoteModel;
import com.personal.SupaNote.Models.UserModel;
import com.personal.SupaNote.Repository.IUserRepository;
import com.personal.SupaNote.Services.NotesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

    @Autowired
    NotesServices notesServices;

    @GetMapping()
    public ResponseEntity<List<NoteModel>> getUserNotes(){
        List<NoteModel> notes = notesServices.getNotesByUser();
        return ResponseEntity.ok(notes);
    }


    @PostMapping
    public ResponseEntity<NoteModel> createNote(@RequestBody NoteModel note) {
        NoteModel createdNote = notesServices.createNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteModel> updateNote(
            @PathVariable Long id,
            @RequestBody NoteModel request) {
        NoteModel updatedNote = notesServices.updateNoteById(id, request);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        String message = notesServices.deleteNoteById(id);
        return ResponseEntity.ok(message);
    }


}
