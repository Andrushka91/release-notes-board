package com.ayesa.releasenotes.api;

import com.ayesa.releasenotes.domain.ReleaseStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull(message = "status is required") ReleaseStatus status) {
}
