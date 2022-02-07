/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.data.engine.impl.util.DirectedGraph.CycleFoundException;

import org.junit.Test;
import static org.junit.Assert.*;

public class DirectedGraphTest {
	@Test
	public void testValidateCycle() {
		///////////////////////////////////////////
		GraphNode foundNode = null;
		DirectedGraph graph = new DirectedGraph(
				toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"), }));
		try {
			graph.validateCycle();
		} catch (CycleFoundException e) {
			fail("Should not goes here");
		}

		///////////////////////////////////////////
		foundNode = null;
		graph = new DirectedGraph(
				toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"), createEdge("a", "a"), }));
		try {
			graph.validateCycle();
		} catch (CycleFoundException e) {
			foundNode = e.getNode();
		}
		assert foundNode != null && foundNode.getValue().equals("a");

		///////////////////////////////////////////
		foundNode = null;
		graph = new DirectedGraph(toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"),
				createEdge("a", "d"), createEdge("b", "d"), createEdge("b", "e"), createEdge("b", "f"),
				createEdge("d", "e"), createEdge("d", "f"), }));
		try {
			graph.validateCycle();
		} catch (CycleFoundException e) {
			fail("Should not goes here");
		}

		///////////////////////////////////////////
		foundNode = null;
		graph = new DirectedGraph(toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"),
				createEdge("a", "d"), createEdge("b", "d"), createEdge("b", "e"), createEdge("b", "f"),
				createEdge("d", "e"), createEdge("d", "f"), createEdge("d", "a"), }));
		try {
			graph.validateCycle();
		} catch (CycleFoundException e) {
			foundNode = e.getNode();
		}
		assert foundNode != null && foundNode.getValue().equals("d");

		///////////////////////////////////////////
		foundNode = null;
		graph = new DirectedGraph(toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"),
				createEdge("a", "d"), createEdge("e", "a"), createEdge("e", "b"), createEdge("c", "d"), }));
		try {
			graph.validateCycle();
		} catch (CycleFoundException e) {
			fail("Should not goes here");
		}

		///////////////////////////////////////////
		foundNode = null;
		graph = new DirectedGraph(
				toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"), createEdge("a", "d"),
						createEdge("e", "a"), createEdge("e", "b"), createEdge("c", "d"), createEdge("c", "e"), }));
		try {
			graph.validateCycle();
		} catch (CycleFoundException e) {
			foundNode = e.getNode();
		}
		assert foundNode != null && foundNode.getValue().equals("c");

		///////////////////////////////////////////
		foundNode = null;
		graph = new DirectedGraph(toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"),
				createEdge("b", "c"), createEdge("c", "d"), createEdge("d", "b"), }));
		try {
			graph.validateCycle();
		} catch (CycleFoundException e) {
			foundNode = e.getNode();
		}
		assert foundNode != null && foundNode.getValue().equals("d");
	}

	@Test
	public void testFlattenNodesByDependency() throws Exception {
		///////////////////////////////////////////
		DirectedGraph graph = new DirectedGraph(
				toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"), }));
		try {
			GraphNode[] nodes = graph.flattenNodesByDependency();
			assert (nodes.length == 3);
			validateFlattened(nodes, graph);
		} catch (CycleFoundException e) {
			fail("Shoule not goes here");
		}

		///////////////////////////////////////////
		graph = new DirectedGraph(
				toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"), createEdge("a", "a"), }));
		try {
			GraphNode[] nodes = graph.flattenNodesByDependency();
			fail("Should not goes here");
		} catch (CycleFoundException e) {
		}

		///////////////////////////////////////////
		graph = new DirectedGraph(toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"),
				createEdge("a", "d"), createEdge("b", "d"), createEdge("b", "e"), createEdge("b", "f"),
				createEdge("d", "e"), createEdge("d", "f"), }));
		try {
			GraphNode[] nodes = graph.flattenNodesByDependency();
			assertTrue(nodes.length == 6);
			validateFlattened(nodes, graph);

		} catch (CycleFoundException e) {
			fail("Should not goes here");
		}

		///////////////////////////////////////////
		graph = new DirectedGraph(toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"),
				createEdge("a", "d"), createEdge("b", "d"), createEdge("b", "e"), createEdge("b", "f"),
				createEdge("d", "e"), createEdge("d", "f"), createEdge("d", "a"), }));
		try {
			GraphNode[] nodes = graph.flattenNodesByDependency();
			fail("Should not goes here");
		} catch (CycleFoundException e) {
		}

		///////////////////////////////////////////
		graph = new DirectedGraph(toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"),
				createEdge("a", "d"), createEdge("e", "a"), createEdge("e", "b"), createEdge("c", "d"), }));
		try {
			GraphNode[] nodes = graph.flattenNodesByDependency();
			validateFlattened(nodes, graph);
		} catch (CycleFoundException e) {
		}

		///////////////////////////////////////////
		graph = new DirectedGraph(
				toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"), createEdge("a", "d"),
						createEdge("e", "a"), createEdge("e", "b"), createEdge("c", "d"), createEdge("c", "e"), }));
		try {
			GraphNode[] nodes = graph.flattenNodesByDependency();
			fail("Should not goes here");
		} catch (CycleFoundException e) {
		}

		///////////////////////////////////////////
		graph = new DirectedGraph(toSet(new DirectedGraphEdge[] { createEdge("a", "b"), createEdge("a", "c"),
				createEdge("b", "c"), createEdge("c", "d"), createEdge("d", "b"), }));
		try {
			GraphNode[] nodes = graph.flattenNodesByDependency();
			fail("Should not goes here");
		} catch (CycleFoundException e) {
		}
	}

	private DirectedGraphEdge createEdge(String from, String to) {
		return new DirectedGraphEdge(new GraphNode(from), new GraphNode(to));
	}

	private Set<DirectedGraphEdge> toSet(DirectedGraphEdge[] src) {
		return new HashSet<DirectedGraphEdge>(Arrays.asList(src));
	}

	private void validateFlattened(GraphNode[] nodes, DirectedGraph graph) throws Exception {
		for (int i = 0; i < nodes.length; i++) {
			for (int j = i + 1; j < nodes.length; j++) {
				if (graph.isDependOn(nodes[i], nodes[j])) {
					throw new Exception(nodes[i].getValue() + "depends on " + nodes[j].getValue() + ", But its index( "
							+ i + ") is before that dependency index(" + j + ")");
				}
			}
		}
	}
}
