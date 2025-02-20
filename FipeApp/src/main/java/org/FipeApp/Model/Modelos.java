package org.FipeApp.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Modelos(
        @JsonAlias("codigo") String id, @JsonAlias("nome") String name){}

