package com.personal.SupaNote.Services;

import com.personal.SupaNote.Models.NoteModel;
import com.personal.SupaNote.Models.UserModel;
import com.personal.SupaNote.Repository.INoteRepository;
import com.personal.SupaNote.Repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private INoteRepository noteRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private NotesServices notesServices;

    @Test
    void shouldReturnUserNotes() {
        UserModel mockUser = UserModel.builder()
                .id(1L)
                .username("prueba")
                .email("prueba@email.com")
                .password("123")
                .notes(new ArrayList<>())
                .build();

        List<NoteModel> mockNotes = List.of(
                new NoteModel(1L, "prueba", "contenido", LocalDateTime.now(), mockUser)
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("prueba@email.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("prueba@email.com")).thenReturn(Optional.of(mockUser));
        when(noteRepository.findByUserId(1L)).thenReturn(mockNotes);

        List<NoteModel> notes = notesServices.getNotesByUser();

        assertThat(notes).isNotEmpty();
        assertThat(notes.get(0).getTitle()).isEqualTo("prueba");
    }

    @Test
    void shouldCreateNotes() {
        UserModel mockUser = UserModel.builder()
                .id(1L)
                .username("prueba")
                .email("prueba@email.com")
                .password("123")
                .notes(new ArrayList<>())
                .build();

        NoteModel mockNote = new NoteModel(null, "prueba", "contenido", LocalDateTime.now(), null);
        NoteModel savedNote = new NoteModel(1L, "prueba", "contenido", mockNote.getCreatedAt(), mockUser);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("prueba@email.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("prueba@email.com")).thenReturn(Optional.of(mockUser));
        when(noteRepository.save(any(NoteModel.class))).thenReturn(savedNote);

        NoteModel result = notesServices.createNote(mockNote);

        assertNotNull(result);
        assertEquals("prueba", result.getTitle());
        assertEquals(mockUser.getId(), result.getUser().getId());

        verify(noteRepository).save(any(NoteModel.class));
    }

    @Test
    void shouldUpdateNoteById() {
        UserModel mockUser = UserModel.builder()
                .id(1L)
                .username("prueba")
                .email("prueba@email.com")
                .password("123")
                .notes(new ArrayList<>())
                .build();

        NoteModel mockNote = new NoteModel(1L, "prueba", "contenido", LocalDateTime.now(), mockUser);

        NoteModel request = new NoteModel();
        request.setTitle("Prueba actualizada");
        request.setContent("Contenido actualizado");
        request.setCreatedAt(LocalDateTime.now());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("prueba@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("prueba@email.com")).thenReturn(Optional.of(mockUser));
        when(noteRepository.findById(1L)).thenReturn(Optional.of(mockNote));
        when(noteRepository.save(any(NoteModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NoteModel updatedNote = notesServices.updateNoteById(1L, request);

        assertEquals("Prueba actualizada", updatedNote.getTitle());
        assertEquals("Contenido actualizado", updatedNote.getContent());
    }

    @Test
    void shouldDeleteNoteById() {
        UserModel mockUser = UserModel.builder()
                .id(1L)
                .username("prueba")
                .email("prueba@email.com")
                .password("123")
                .notes(new ArrayList<>())
                .build();

        NoteModel mockNote = new NoteModel(1L, "prueba", "contenido", LocalDateTime.now(), mockUser);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("prueba@email.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("prueba@email.com")).thenReturn(Optional.of(mockUser));
        when(noteRepository.findById(1L)).thenReturn(Optional.of(mockNote));

        String result = notesServices.deleteNoteById(1L);

        verify(noteRepository, times(1)).deleteById(1L);
        assertEquals("Nota con id 1 eliminada con exito", result);
    }



}
