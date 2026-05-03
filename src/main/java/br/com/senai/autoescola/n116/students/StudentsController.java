package br.com.senai.autoescola.n116.students;

import br.com.senai.autoescola.n116.students.create.CreateStudentHandler;
import br.com.senai.autoescola.n116.students.create.CreateStudentRequest;
import br.com.senai.autoescola.n116.students.create.CreateStudentResponse;
import br.com.senai.autoescola.n116.students.delete.DeleteStudentHandler;
import br.com.senai.autoescola.n116.students.delete.DeleteStudentRequest;
import br.com.senai.autoescola.n116.students.list.ListStudentsHandler;
import br.com.senai.autoescola.n116.students.list.ListStudentsRequest;
import br.com.senai.autoescola.n116.students.list.ListStudentsResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/students")
public class StudentsController {
    private final CreateStudentHandler createStudentHandler;
    private final DeleteStudentHandler deleteStudentHandler;
    private final ListStudentsHandler listStudentsHandler;

    public StudentsController(
            CreateStudentHandler createStudentHandler,
            DeleteStudentHandler deleteStudentHandler,
            ListStudentsHandler listStudentsHandler
    ) {
        this.createStudentHandler = createStudentHandler;
        this.deleteStudentHandler = deleteStudentHandler;
        this.listStudentsHandler = listStudentsHandler;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateStudentResponse> create(
            @RequestBody @Valid CreateStudentRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var response = createStudentHandler.handle(request);
        URI uri = uriBuilder
                .path("/students/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        deleteStudentHandler.handle(new DeleteStudentRequest(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ListStudentsResponse> findAll(@PageableDefault Pageable pageable) {
        var result = listStudentsHandler.handle(new ListStudentsRequest(pageable));
        return ResponseEntity.ok(result);
    }
}
