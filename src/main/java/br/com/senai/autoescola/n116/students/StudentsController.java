package br.com.senai.autoescola.n116.students;

import br.com.senai.autoescola.n116.students.create.CreateStudentHandler;
import br.com.senai.autoescola.n116.students.create.CreateStudentRequest;
import br.com.senai.autoescola.n116.students.create.CreateStudentResponse;
import br.com.senai.autoescola.n116.students.delete.DeleteStudentHandler;
import br.com.senai.autoescola.n116.students.delete.DeleteStudentRequest;
import jakarta.validation.Valid;
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

    public StudentsController(CreateStudentHandler createStudentHandler, DeleteStudentHandler deleteStudentHandler) {
        this.createStudentHandler = createStudentHandler;
        this.deleteStudentHandler = deleteStudentHandler;
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
}
