package dev.country.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse {
    String responseCode;
    String responseMessage;
    List<?> data = new ArrayList<>();
}