package com.ngbsn.domain.repository;

import com.ngbsn.domain.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {
}
