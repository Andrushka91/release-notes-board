package com.ayesa.releasenotes.service;

import com.ayesa.releasenotes.api.ApplicationResponse;
import com.ayesa.releasenotes.api.CreateReleaseNoteItemRequest;
import com.ayesa.releasenotes.api.ReleaseNoteItemResponse;
import com.ayesa.releasenotes.domain.ApplicationEntity;
import com.ayesa.releasenotes.domain.ApplicationRepository;
import com.ayesa.releasenotes.domain.ReleaseNoteItem;
import com.ayesa.releasenotes.domain.ReleaseNoteItemRepository;
import com.ayesa.releasenotes.domain.ReleaseStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReleaseNotesService {
    private final ApplicationRepository applicationRepository;
    private final ReleaseNoteItemRepository itemRepository;

    public ReleaseNotesService(ApplicationRepository applicationRepository, ReleaseNoteItemRepository itemRepository) {
        this.applicationRepository = applicationRepository;
        this.itemRepository = itemRepository;
    }

    public List<ApplicationResponse> listApplications() {
        return applicationRepository.findAllByOrderByNameAsc().stream()
                .map(this::toApplicationResponse)
                .toList();
    }

    public List<ReleaseNoteItemResponse> listItems() {
        return itemRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toItemResponse)
                .toList();
    }

    @Transactional
    public ReleaseNoteItemResponse createItem(CreateReleaseNoteItemRequest request) {
        ApplicationEntity application = applicationRepository.findById(request.applicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application " + request.applicationId() + " was not found"));

        String description = request.description() == null ? null : request.description().trim();
        ReleaseNoteItem item = new ReleaseNoteItem(application, request.title().trim(), description);
        return toItemResponse(itemRepository.save(item));
    }

    @Transactional
    public ReleaseNoteItemResponse changeStatus(Long id, ReleaseStatus status) {
        ReleaseNoteItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release note item " + id + " was not found"));
        item.changeStatus(status);
        return toItemResponse(item);
    }

    private ApplicationResponse toApplicationResponse(ApplicationEntity application) {
        return new ApplicationResponse(application.getId(), application.getName());
    }

    private ReleaseNoteItemResponse toItemResponse(ReleaseNoteItem item) {
        return new ReleaseNoteItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getStatus(),
                toApplicationResponse(item.getApplication()),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
