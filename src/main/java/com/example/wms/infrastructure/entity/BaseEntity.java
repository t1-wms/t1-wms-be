package com.example.wms.infrastructure.entity;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public abstract class BaseEntity {

    private LocalDate createdAt;
    private LocalDate updatedAt;
}