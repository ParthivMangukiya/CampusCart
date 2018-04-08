package com.parthiv.user.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder()
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "category"})
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class Item {
    private long id;
    private String name;
    private String description;
    private String category;
    private String imageUri;
    private Double price;
}