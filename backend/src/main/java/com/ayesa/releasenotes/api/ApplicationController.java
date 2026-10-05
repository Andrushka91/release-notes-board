package com.ayesa.releasenotes.api;

import com.ayesa.releasenotes.service.ReleaseNotesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ReleaseNotesService releaseNotesService;

    public ApplicationController(ReleaseNotesService releaseNotesService) {
        this.releaseNotesService = releaseNotesService;
    }

    @GetMapping
    public List<ApplicationResponse> listApplications() {
        return releaseNotesService.listApplications();
    }
}
