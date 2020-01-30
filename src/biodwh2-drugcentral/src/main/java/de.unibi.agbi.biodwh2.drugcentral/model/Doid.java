package de.unibi.agbi.biodwh2.drugcentral.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = {"id", "label", "doid", "url"})

public final class Doid {
    @JsonProperty("id")
    public String id;
    @JsonProperty("label")
    public String label;
    @JsonProperty("doid")
    public String doId;
    @JsonProperty("url")
    public String url;
}