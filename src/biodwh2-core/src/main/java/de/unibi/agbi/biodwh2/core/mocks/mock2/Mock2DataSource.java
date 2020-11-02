package de.unibi.agbi.biodwh2.core.mocks.mock2;

import de.unibi.agbi.biodwh2.core.DataSource;
import de.unibi.agbi.biodwh2.core.etl.*;
import de.unibi.agbi.biodwh2.core.mocks.mock2.etl.*;

public class Mock2DataSource extends DataSource {
    @Override
    public String getId() {
        return "Mock2";
    }

    @Override
    public Updater<Mock2DataSource> getUpdater() {
        return new Mock2Updater(this);
    }

    @Override
    public Parser<Mock2DataSource> getParser() {
        return new Mock2Parser(this);
    }

    @Override
    public GraphExporter<Mock2DataSource> getGraphExporter() {
        return new Mock2GraphExporter(this);
    }

    @Override
    public MappingDescriber getMappingDescriber() {
        return new Mock2MappingDescriber(this);
    }

    @Override
    protected void unloadData() {
    }
}