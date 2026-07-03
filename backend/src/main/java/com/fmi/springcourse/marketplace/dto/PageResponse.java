package com.fmi.springcourse.marketplace.dto;

import java.util.Collection;

public record PageResponse<T>(Collection<T> content,
                              long totalElements,
                              int totalPages) {
}