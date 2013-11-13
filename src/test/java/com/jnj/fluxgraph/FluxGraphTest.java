package com.jnj.fluxgraph;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.GraphTest;
import com.tinkerpop.blueprints.util.io.gml.GMLReaderTestSuite;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReaderTestSuite;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReaderTestSuite;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Test suite for Datomic graph implementation.
 *
 * @author Davy Suvee (http://datablend.be)
 */
public class FluxGraphTest extends GraphTest {

    private FluxGraph currentGraph;

    public void testDatomicBenchmarkTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new FluxBenchmarkTestSuite(this));
        printTestPerformance("FluxBenchmarkTestSuite", this.stopWatch());
    }

    public void testVertexTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new VertexTestSuite(this));
        printTestPerformance("VertexTestSuite", this.stopWatch());
    }

    /*

        testSetEdgeLabelNullShouldThrowIllegalArgumentException
            in
        EdgeTestSuite

        expects that an IllegalArgumentException will be thrown but, FluxGraph catches it

        TODO: Decide whether to change fluxgraph to throw these exceptions

    public void testEdgeTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new EdgeTestSuite(this));
        printTestPerformance("EdgeTestSuite", this.stopWatch());
    }*/

    public void testGraphTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphTestSuite(this));
        printTestPerformance("GraphTestSuite", this.stopWatch());
    }

    public void testQueryTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphQueryTestSuite(this));
        printTestPerformance("QueryTestSuite", this.stopWatch());
    }

    /*
        Line 160 in testNoConcurrentModificationException in KeyIndexableGraphTestSuite
        breaks FluxGraph because of a change in datomic to throw exceptions on unsupported
        schema changes

        It's still Key indexable but, indexes must be specified on attributes at the time of
        definition (as attribute definitions cannot be changed)

        See: ## Changed in 0.8.3561
        At: http://downloads.datomic.com/0.8.4143/changes

     public void testKeyIndexableGraphTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new KeyIndexableGraphTestSuite(this));
        printTestPerformance("KeyIndexableGraphTestSuite", this.stopWatch());
    }*/

    public void testGraphMLReaderTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphMLReaderTestSuite(this));
        printTestPerformance("GraphMLReaderTestSuite", this.stopWatch());
    }

    public void testGraphSONReaderTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphSONReaderTestSuite(this));
        printTestPerformance("GraphSONReaderTestSuite", this.stopWatch());
    }

    public void testGMLReaderTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GMLReaderTestSuite(this));
        printTestPerformance("GMLReaderTestSuite", this.stopWatch());
    }

    public Graph generateGraph(String graphDirectoryName) {
        return generateGraph();
    }

    public Graph generateGraph() {
        this.currentGraph = new FluxGraph("datomic:mem://tinkerpop" + UUID.randomUUID());
        return this.currentGraph;
    }

    public void doTestSuite(final TestSuite testSuite) throws Exception {
        for (Method method : testSuite.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println("Testing " + method.getName() + "...");
                method.invoke(testSuite);
                try {
                    if (this.currentGraph != null)
                        //this.currentGraph.clear();
                        this.currentGraph.shutdown();
                } catch (Exception e) {
                }
            }
        }
    }

}
