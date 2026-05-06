package br.com.senai.autoescola.n116.lessons;

import br.com.senai.autoescola.n116.lessons.schedule.ScheduleLessonHandler;
import br.com.senai.autoescola.n116.lessons.schedule.ScheduleLessonRequest;
import br.com.senai.autoescola.n116.lessons.schedule.ScheduleLessonResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/lessons")
public class LessonsController {
    private final ScheduleLessonHandler scheduleLessonHandler;

    public LessonsController(ScheduleLessonHandler scheduleLessonHandler) {
        this.scheduleLessonHandler = scheduleLessonHandler;
    }

    @PostMapping
    public ResponseEntity<ScheduleLessonResponse> schedule(
            @RequestBody @Valid ScheduleLessonRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var result = scheduleLessonHandler.schedule(request);
        var uri = uriBuilder.path("/lessons/{id}").buildAndExpand(result.lessonId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }
}
