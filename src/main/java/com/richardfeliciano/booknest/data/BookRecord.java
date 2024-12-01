package com.richardfeliciano.booknest.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookRecord(
        @JsonProperty("title") String title,
        @JsonProperty("download_count") Long downloadCount,
        @JsonProperty("authors") List<AuthorRecord> authors,
        @JsonProperty("subjects")List<String> genres,
        @JsonProperty("languages") List<String> languages
) {}
