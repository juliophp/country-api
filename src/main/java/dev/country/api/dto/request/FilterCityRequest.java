package dev.country.api.dto.request;

public record FilterCityRequest(String country, String orderBy, String order, Integer limit) {}
