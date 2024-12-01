package com.richardfeliciano.booknest.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorRecord(
        @JsonProperty("name") String name,
        @JsonProperty("birth_year") Integer birthYear,
        @JsonProperty("death_year") Integer deathYear
) {

}
