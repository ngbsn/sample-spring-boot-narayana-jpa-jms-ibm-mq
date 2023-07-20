package com.ngbsn.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Getter
@Setter
@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "entryIdSeq")
    @Setter(AccessLevel.NONE)
    private Long id;

    private String message;
}
