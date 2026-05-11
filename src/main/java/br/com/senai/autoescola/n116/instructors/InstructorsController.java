package br.com.senai.autoescola.n116.instructors;

import br.com.senai.autoescola.n116.instructors.create.CreateInstructorHandler;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorRequest;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorResponse;
import br.com.senai.autoescola.n116.instructors.delete.DeleteInstructorHandler;
import br.com.senai.autoescola.n116.instructors.getById.GetInstructorByIdHandler;
import br.com.senai.autoescola.n116.instructors.getById.GetInstructorByIdRequest;
import br.com.senai.autoescola.n116.instructors.getById.GetInstructorByIdResponse;
import br.com.senai.autoescola.n116.instructors.list.ListInstructorsHandler;
import br.com.senai.autoescola.n116.instructors.list.ListInstructorsRequest;
import br.com.senai.autoescola.n116.instructors.list.ListInstructorsResponse;
import br.com.senai.autoescola.n116.instructors.update.UpdateInstructorHandler;
import br.com.senai.autoescola.n116.instructors.update.UpdateInstructorRequest;
import br.com.senai.autoescola.n116.instructors.update.UpdateInstructorResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/instructors")
@SecurityRequirement(name = "bearer-key")
public class InstructorsController {
	private final CreateInstructorHandler createInstructorHandler;
	private final GetInstructorByIdHandler getInstructorByIdHandler;
	private final DeleteInstructorHandler deleteInstructorHandler;
	private final ListInstructorsHandler listInstructorsHandler;
	private final UpdateInstructorHandler updateInstructorHandler;

	public InstructorsController(
			CreateInstructorHandler createInstructorHandler,
			GetInstructorByIdHandler getInstructorByIdHandler,
			DeleteInstructorHandler deleteInstructorHandler,
			ListInstructorsHandler listInstructorsHandler,
			UpdateInstructorHandler updateInstructorHandler
	) {
		this.createInstructorHandler = createInstructorHandler;
		this.getInstructorByIdHandler = getInstructorByIdHandler;
		this.deleteInstructorHandler = deleteInstructorHandler;
		this.listInstructorsHandler = listInstructorsHandler;
		this.updateInstructorHandler = updateInstructorHandler;
	}

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	@PreAuthorize("hasAnyRole('ADMIN')")
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
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<GetInstructorByIdResponse> getById(@PathVariable Long id) {
		var response = getInstructorByIdHandler.handle(new GetInstructorByIdRequest(id));
		return ResponseEntity.ok(response);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<ListInstructorsResponse> findAll(@ParameterObject @PageableDefault Pageable pageable) {
		var result = listInstructorsHandler.handle(new ListInstructorsRequest(pageable));
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		deleteInstructorHandler.handle(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<UpdateInstructorResponse> update(
			@PathVariable Long id,
			@RequestBody @Valid UpdateInstructorRequest request
	) {
		var result = updateInstructorHandler.handle(id, request);
		return ResponseEntity.ok(result);
	}
}
