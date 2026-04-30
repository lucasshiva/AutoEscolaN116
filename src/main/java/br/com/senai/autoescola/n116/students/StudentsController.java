package br.com.senai.autoescola.n116.students;

import br.com.senai.autoescola.n116.students.create.CreateStudentCommand;
import br.com.senai.autoescola.n116.students.create.CreateStudentHandler;
import br.com.senai.autoescola.n116.students.create.CreateStudentResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/students")
public class StudentsController {
    private final CreateStudentHandler createStudentHandler;

    public StudentsController(CreateStudentHandler createStudentHandler) {
        this.createStudentHandler = createStudentHandler;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateStudentResponse> create(
            @RequestBody @Valid CreateStudentCommand cmd,
            UriComponentsBuilder uriBuilder
    ) {
        var response = createStudentHandler.handle(cmd);
        URI uri = uriBuilder
                .path("/students/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
