package de.unibi.agbi.biodwh2.core.io.obo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * OBO Header definition
 * <p/>
 * http://purl.obolibrary.org/obo/oboformat/spec.html#3.2
 */
public final class OboHeader extends OboStructure {
    private static final Set<String> RESERVED_TOKENS = new HashSet<>(
            Arrays.asList("format-version", "data-version", "auto-generated-by", "data-version", "date", "idspace",
                          "default-namespace", "format-version", "import", "ontology", "owl-axioms", "synonymtypedef",
                          "remark", "saved-by", "subsetdef", "treat-xrefs-as-equivalent", "property_value",
                          "treat-xrefs-as-genus-differentia", "treat-xrefs-as-has-subclass", "treat-xrefs-as-is_a",
                          "treat-xrefs-as-relationship", "treat-xrefs-as-reverse-genus-differentia"));

    private final Set<String> unreservedTokens;

    OboHeader() {
        super();
        unreservedTokens = new HashSet<>();
    }

    void cacheUnreservedTokens() {
        unreservedTokens.addAll(getKeys());
        unreservedTokens.removeAll(RESERVED_TOKENS);
    }

    /**
     * UnreservedToken ':'  [  ws ] UnquotedString
     */
    public String[] getUnreservedTokenKeys() {
        return unreservedTokens.toArray(new String[0]);
    }

    /**
     * cardinality 0-1
     *
     * @return TVP
     */
    public String getFormatVersion() {
        return getFirst("format-version");
    }

    /**
     * cardinality *
     *
     * @return TVP[]
     */
    public String[] getDataVersions() {
        return get("data-version");
    }

    /**
     * cardinality *
     *
     * @return (IRI | filepath)[]
     */
    public String[] getImports() {
        return get("import");
    }

    /**
     * cardinality *
     *
     * @return (Subset-ID ws QuotedString)[]
     */
    public String[] getSubsetDefs() {
        return get("subsetdef");
    }

    /**
     * cardinality *
     *
     * @return (SynonymType-ID ws QuotedString [ ws SynonymScope ])[]
     */
    public String[] getSynonymTypeDefs() {
        return get("synonymtypedef");
    }

    /**
     * cardinality 0-1
     *
     * @return DD:MM:YYYY ws hh:mm
     */
    public String getDate() {
        return getFirst("date");
    }

    /**
     * cardinality 0-1
     *
     * @return TVP
     */
    public String getSavedBy() {
        return getFirst("saved-by");
    }

    /**
     * cardinality 0-1
     *
     * @return TVP
     */
    public String getAutoGeneratedBy() {
        return getFirst("auto-generated-by");
    }

    /**
     * cardinality 0-1
     *
     * @return OBONamespace
     */
    public String getDefaultNamespace() {
        return getFirst("default-namespace");
    }

    /**
     * cardinality *
     *
     * @return TVP[]
     */
    public String[] getRemarks() {
        return get("remark");
    }

    /**
     * cardinality 0-1
     *
     * @return TVP
     */
    public String getOntology() {
        return getFirst("ontology");
    }

    /**
     * cardinality *
     *
     * @return TVP
     */
    public String[] getOWLAxioms() {
        return get("owl-axioms");
    }

    /**
     * cardinality *
     *
     * @return (IDPrefix ws Rel-ID ws Class-ID)[]
     */
    public String[] treatXrefsAsGenusDifferentia() {
        return get("treat-xrefs-as-genus-differentia");
    }

    /**
     * cardinality *
     *
     * @return (IDPrefix ws Rel-ID ws Class-ID)[]
     */
    public String[] treatXrefsAsReverseGenusDifferentia() {
        return get("treat-xrefs-as-reverse-genus-differentia");
    }

    /**
     * cardinality *
     *
     * @return (IDPrefix Rel-ID)[]
     */
    public String[] treatXrefsAsRelationship() {
        return get("treat-xrefs-as-relationship");
    }

    /**
     * cardinality *
     *
     * @return IDPrefix[]
     */
    public String[] treatXrefsAsEquivalent() {
        return get("treat-xrefs-as-equivalent");
    }

    /**
     * cardinality *
     *
     * @return IDPrefix[]
     */
    public String[] treatXrefsAsIsA() {
        return get("treat-xrefs-as-is_a");
    }

    /**
     * cardinality *
     *
     * @return IDPrefix[]
     */
    public String[] treatXrefsAsHasSubclass() {
        return get("treat-xrefs-as-has-subclass");
    }

    /**
     * cardinality *
     *
     * @return (IDPrefix ws IRI [ ws QuotedString ])[]
     */
    public String[] getIdspaces() {
        return get("idspace");
    }

    /**
     * cardinality *
     *
     * @return (Rel-ID ( QuotedString XSD-Type | ID) {WhiteSpaceChar} [ QualifierBlock ] {WhiteSpaceChar} [ HiddenComment ])[]
     */
    public String[] getPropertyValues() {
        return get("property_value");
    }
}
