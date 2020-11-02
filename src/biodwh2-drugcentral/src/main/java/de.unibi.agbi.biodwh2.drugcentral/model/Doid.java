package de.unibi.agbi.biodwh2.drugcentral.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unibi.agbi.biodwh2.core.model.graph.GraphProperty;
import de.unibi.agbi.biodwh2.core.model.graph.NodeLabel;

@SuppressWarnings("unused")
@JsonPropertyOrder({"id", "label", "doid", "url"})
@NodeLabel("Doid")
public final class Doid {
    @JsonProperty("id")
    @GraphProperty("id")
    public String id;
    @JsonProperty("label")
    @GraphProperty("label")
    public String label;
    @JsonProperty("doid")
    @GraphProperty("doid")
    public String doId;
    @JsonProperty("url")
    @GraphProperty("url")
    public String url;
}