package de.unibi.agbi.biodwh2.hgnc.etl;

import de.unibi.agbi.biodwh2.core.Workspace;
import de.unibi.agbi.biodwh2.core.etl.GraphExporter;
import de.unibi.agbi.biodwh2.core.model.graph.Graph;
import de.unibi.agbi.biodwh2.hgnc.HGNCDataSource;
import de.unibi.agbi.biodwh2.hgnc.model.Gene;

public class HGNCGraphExporter extends GraphExporter<HGNCDataSource> {
    @Override
    protected boolean exportGraph(final Workspace workspace, final HGNCDataSource dataSource, final Graph graph) {
        for (Gene gene : dataSource.genes)
            createNodeFromModel(graph, gene);
        return true;
    }
}
