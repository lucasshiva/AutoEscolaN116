package br.com.senai.autoescola.n116.instructors;

import br.com.senai.autoescola.n116.instructors.create.CreateInstructorHandler;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorRequest;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorResponse;
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
@RequestMapping("/instructors")
public class InstructorsController {
    private final CreateInstructorHandler createInstructorHandler;

    public InstructorsController(CreateInstructorHandler createInstructorHandler) {
        this.createInstructorHandler = createInstructorHandler;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CreateInstructorResponse> createNew(
            @RequestBody @Valid CreateInstructorRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var response = createInstructorHandler.handle(request);
        URI uri = uriBuilder
                .path("/instructors/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
