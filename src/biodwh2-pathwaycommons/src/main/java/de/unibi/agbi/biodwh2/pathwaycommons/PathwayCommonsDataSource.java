package de.unibi.agbi.biodwh2.pathwaycommons;

import de.unibi.agbi.biodwh2.core.DataSource;
import de.unibi.agbi.biodwh2.core.DevelopmentState;
import de.unibi.agbi.biodwh2.core.etl.GraphExporter;
import de.unibi.agbi.biodwh2.core.etl.MappingDescriber;
import de.unibi.agbi.biodwh2.core.etl.Parser;
import de.unibi.agbi.biodwh2.core.etl.Updater;
import de.unibi.agbi.biodwh2.pathwaycommons.etl.PathwayCommonsGraphExporter;
import de.unibi.agbi.biodwh2.pathwaycommons.etl.PathwayCommonsMappingDescriber;
import de.unibi.agbi.biodwh2.pathwaycommons.etl.PathwayCommonsParser;
import de.unibi.agbi.biodwh2.pathwaycommons.etl.PathwayCommonsUpdater;

public class PathwayCommonsDataSource extends DataSource {
    @Override
    public String getId() {
        return "PathwayCommons";
    }

    @Override
    public DevelopmentState getDevelopmentState() {
        return DevelopmentState.InDevelopment;
    }

    @Override
    protected Updater<? extends DataSource> getUpdater() {
        return new PathwayCommonsUpdater(this);
    }

    @Override
    protected Parser<? extends DataSource> getParser() {
        return new PathwayCommonsParser(this);
    }

    @Override
    protected GraphExporter<? extends DataSource> getGraphExporter() {
        return new PathwayCommonsGraphExporter(this);
    }

    @Override
    public MappingDescriber getMappingDescriber() {
        return new PathwayCommonsMappingDescriber(this);
    }

    @Override
    protected void unloadData() {
    }
}