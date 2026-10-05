package com.ayesa.releasenotes.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseNoteItemRepository extends JpaRepository<ReleaseNoteItem, Long> {
    List<ReleaseNoteItem> findAllByOrderByCreatedAtDesc();
}
