package br.com.senai.autoescola.n116.students;

import br.com.senai.autoescola.n116.students.create.CreateStudentHandler;
import br.com.senai.autoescola.n116.students.create.CreateStudentRequest;
import br.com.senai.autoescola.n116.students.create.CreateStudentResponse;
import br.com.senai.autoescola.n116.students.delete.DeleteStudentHandler;
import br.com.senai.autoescola.n116.students.delete.DeleteStudentRequest;
import br.com.senai.autoescola.n116.students.getById.GetStudentByIdHandler;
import br.com.senai.autoescola.n116.students.getById.GetStudentByIdRequest;
import br.com.senai.autoescola.n116.students.getById.GetStudentByIdResponse;
import br.com.senai.autoescola.n116.students.list.ListStudentsHandler;
import br.com.senai.autoescola.n116.students.list.ListStudentsRequest;
import br.com.senai.autoescola.n116.students.list.ListStudentsResponse;
import br.com.senai.autoescola.n116.students.update.UpdateStudentHandler;
import br.com.senai.autoescola.n116.students.update.UpdateStudentRequest;
import br.com.senai.autoescola.n116.students.update.UpdateStudentResponse;
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
    private final GetStudentByIdHandler getStudentByIdHandler;
    private final UpdateStudentHandler updateStudentHandler;

    public StudentsController(
            CreateStudentHandler createStudentHandler,
            DeleteStudentHandler deleteStudentHandler,
            ListStudentsHandler listStudentsHandler,
            GetStudentByIdHandler getStudentByIdHandler,
            UpdateStudentHandler updateStudentHandler
    ) {
        this.createStudentHandler = createStudentHandler;
        this.deleteStudentHandler = deleteStudentHandler;
        this.listStudentsHandler = listStudentsHandler;
        this.getStudentByIdHandler = getStudentByIdHandler;
        this.updateStudentHandler = updateStudentHandler;
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

    @GetMapping("/{id}")
    public ResponseEntity<GetStudentByIdResponse> findById(@PathVariable Long id) {
        var result = getStudentByIdHandler.handle(new GetStudentByIdRequest(id));
        return ResponseEntity.ok(result);
    }

    @PutMapping("{id}")
    public ResponseEntity<UpdateStudentResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateStudentRequest request
    ) {
        var result = updateStudentHandler.handle(id, request);
        return ResponseEntity.ok(result);
    }
}
