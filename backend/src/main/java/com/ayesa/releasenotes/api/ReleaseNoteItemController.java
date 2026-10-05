package com.ayesa.releasenotes.api;

import com.ayesa.releasenotes.service.ReleaseNotesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ReleaseNoteItemController {
    private final ReleaseNotesService releaseNotesService;

    public ReleaseNoteItemController(ReleaseNotesService releaseNotesService) {
        this.releaseNotesService = releaseNotesService;
    }

    @GetMapping
    public List<ReleaseNoteItemResponse> listItems() {
        return releaseNotesService.listItems();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReleaseNoteItemResponse createItem(@Valid @RequestBody CreateReleaseNoteItemRequest request) {
        return releaseNotesService.createItem(request);
    }

    @PatchMapping("/{id}/status")
    public ReleaseNoteItemResponse changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        return releaseNotesService.changeStatus(id, request.status());
    }
}
