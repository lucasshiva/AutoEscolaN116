package br.com.senai.autoescola.n116.instructors;

import br.com.senai.autoescola.n116.instructors.create.CreateInstructorHandler;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorRequest;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorResponse;
import br.com.senai.autoescola.n116.instructors.getById.GetInstructorByIdHandler;
import br.com.senai.autoescola.n116.instructors.getById.GetInstructorByIdRequest;
import br.com.senai.autoescola.n116.instructors.getById.GetInstructorByIdResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/instructors")
public class InstructorsController {
    private final CreateInstructorHandler createInstructorHandler;
    private final GetInstructorByIdHandler getInstructorByIdHandler;

    public InstructorsController(
            CreateInstructorHandler createInstructorHandler,
            GetInstructorByIdHandler getInstructorByIdHandler) {
        this.createInstructorHandler = createInstructorHandler;
        this.getInstructorByIdHandler = getInstructorByIdHandler;
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

    @GetMapping("/{id}")
    public ResponseEntity<GetInstructorByIdResponse> getById(@PathVariable Long id) {
        var response = getInstructorByIdHandler.handle(new GetInstructorByIdRequest(id));
        return ResponseEntity.ok(response);
    }
}
