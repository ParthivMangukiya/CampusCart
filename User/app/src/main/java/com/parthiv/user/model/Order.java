package com.parthiv.user.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder()
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "uid", "otp"})
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class Order {
    private long id;
    private long uid;
    private String username;
    private int otp;
    private String status;
    private double amount;
    private int color;
    private List<Long> items;
}