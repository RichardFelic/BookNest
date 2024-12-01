package com.richardfeliciano.booknest.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexResponse(
        @JsonProperty("results") List<BookRecord> results
) {

}
