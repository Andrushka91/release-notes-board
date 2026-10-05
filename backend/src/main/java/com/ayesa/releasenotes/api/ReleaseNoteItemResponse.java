package com.ayesa.releasenotes.api;

import com.ayesa.releasenotes.domain.ReleaseStatus;

import java.time.Instant;

public record ReleaseNoteItemResponse(
        Long id,
        String title,
        String description,
        ReleaseStatus status,
        ApplicationResponse application,
        Instant createdAt,
        Instant updatedAt
) {
}
