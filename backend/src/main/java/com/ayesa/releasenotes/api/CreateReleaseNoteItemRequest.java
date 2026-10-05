package com.ayesa.releasenotes.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReleaseNoteItemRequest(
        @NotNull(message = "applicationId is required") Long applicationId,
        @NotBlank(message = "title is required") @Size(max = 200, message = "title must have at most 200 characters") String title,
        @Size(max = 2_000, message = "description must have at most 2000 characters") String description
) {
}
