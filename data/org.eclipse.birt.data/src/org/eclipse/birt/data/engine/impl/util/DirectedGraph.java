/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A directed graph
 */
public class DirectedGraph {

	private Set<DirectedGraphEdge> edges;

	private Map<GraphNode, Set<DirectedGraphEdge>> groupedEdgesByFromNode;

	public DirectedGraph(Set<DirectedGraphEdge> edges) {
		assert edges != null;
		this.edges = edges;
	}

	/**
	 * If node2 is reachable from node1 along edges, we say nodes1 depends on node2.
	 * If node1 depends on node2 and node2 depends on node1, throw
	 * <code>CycleFoundException</code> If node1 depends on node2 and node1 does not
	 * depend on nodes2, then node2 locates before node1
	 * 
	 * @return the flattened graph nodes in terms of dependency
	 * @throws CycleFoundException
	 */
	public GraphNode[] flattenNodesByDependency() throws CycleFoundException {
		List<GraphNode> result = new ArrayList<GraphNode>();

		// In fact, it's a copy set of <code>result</code>, used to speed up search
		Set<GraphNode> flatteneds = new HashSet<GraphNode>();

		// grouped edges by from node
		Map<GraphNode, Set<DirectedGraphEdge>> groupedEdgesByFromNode = groupEdgesByFromNode();

		for (GraphNode fromNode : groupedEdgesByFromNode.keySet()) {
			if (!flatteneds.contains(fromNode)) {
				addToFlattened(fromNode, result, new HashSet<GraphNode>(), // used to check cycle
						flatteneds);
			}
		}
		return result.toArray(new GraphNode[0]);
	}

	/**
	 * 
	 * @param node1
	 * @param node2
	 * @return true if node1 depends on node2, i.e. node2 is reachable from node1
	 *         along edges; otherwise return false
	 */
	public boolean isDependOn(GraphNode node1, GraphNode node2) {
		assert node1 != null && node2 != null;

		// grouped edges by from node
		Map<GraphNode, Set<DirectedGraphEdge>> groupedEdgesByFromNode = groupEdgesByFromNode();

		Set<DirectedGraphEdge> nextEdges = groupedEdgesByFromNode.get(node1);

		if (nextEdges == null) {
			return false;
		}

		for (DirectedGraphEdge edge : nextEdges) {
			if (node2.equals(edge.getTo())) {
				return true;
			}
			if (isDependOn(edge.getTo(), node2)) {
				return true;
			}
		}

		return false;
	}

	private void addToFlattened(GraphNode node, List<GraphNode> result, Set<GraphNode> transitions,
			Set<GraphNode> flatteneds) throws CycleFoundException {
		transitions.add(node);
		Set<DirectedGraphEdge> nextEdges = groupedEdgesByFromNode.get(node);
		if (nextEdges != null) {

			//// first flatten the dependencies
			for (DirectedGraphEdge edge : nextEdges) {
				GraphNode nextNode = edge.getTo();

				if (transitions.contains(nextNode)) {
					throw new CycleFoundException(node);
				}

				if (!flatteneds.contains(nextNode)) {
					transitions.add(nextNode);

					addToFlattened(nextNode, result, transitions, flatteneds);

					transitions.remove(nextNode);
				}

			}
		}
		result.add(node);
		flatteneds.add(node);
	}

	/**
	 * validate graph has no cycle
	 * 
	 * @throws CycleFoundException if a cycle is found
	 */
	public void validateCycle() throws CycleFoundException {
		// grouped edges by from node
		Map<GraphNode, Set<DirectedGraphEdge>> groupedEdgesByFromNode = groupEdgesByFromNode();

		// nodes that are already verified as not involved in cycle
		Set<GraphNode> checkedNodes = new HashSet<GraphNode>();
		for (GraphNode fromNode : groupedEdgesByFromNode.keySet()) {
			if (!checkedNodes.contains(fromNode)) // not checked yet
			{
				// Cycle is checked during find out all reachable nodes from fromNode
				Set<GraphNode> reachables = getReachableNodes(fromNode, new HashSet<GraphNode>(),
						groupedEdgesByFromNode);
				// NO CycleFoundException catched, so fromNode and all node in
				// reachables are nodes that are not involved in cycle
				checkedNodes.addAll(reachables);
				checkedNodes.add(fromNode);
			}
		}
	}

	/**
	 * 
	 * @param fromNode
	 * @param transitions:  current checked nodes from which fromNode is reachable
	 * @param groupedEdges: grouped edges
	 * @return reachable nodes from fromNode. an empty set is returned if no
	 *         reachable nodes
	 * @throws CycleFoundException if a cylce is found
	 */
	private Set<GraphNode> getReachableNodes(GraphNode fromNode, Set<GraphNode> transitions,
			Map<GraphNode, Set<DirectedGraphEdge>> groupedEdges) throws CycleFoundException {
		Set<GraphNode> reachables = new HashSet<GraphNode>();

		// All the edges with fromNode as its from node
		Set<DirectedGraphEdge> nextEdges = groupedEdges.get(fromNode);
		if (nextEdges != null) {
			for (DirectedGraphEdge edge : nextEdges) {
				if (edge.getTo().equals(fromNode)) {
					// An edge whose both "from" and "to" nodes are fromNode
					throw new CycleFoundException(fromNode);
				}

				GraphNode reachable = edge.getTo();

				if (transitions.contains(reachable)) {
					// cycle is checked
					throw new CycleFoundException(fromNode);
				}

				// if reachables.contains(reachable) is true, that means
				// this reachable has already been processed and passed
				if (!reachables.contains(reachable)) {
					reachables.add(reachable);

					Set<GraphNode> newTransitons = new HashSet<GraphNode>(transitions);
					newTransitons.add(fromNode);

					// apply depth traverse
					reachables.addAll(getReachableNodes(reachable, newTransitons, groupedEdges));
				}
			}
		}
		return reachables;
	}

	/**
	 * @return grouped edges by from node
	 */
	private Map<GraphNode, Set<DirectedGraphEdge>> groupEdgesByFromNode() {
		if (groupedEdgesByFromNode == null) {
			groupedEdgesByFromNode = new HashMap<GraphNode, Set<DirectedGraphEdge>>();
			for (DirectedGraphEdge edge : edges) {
				Set<DirectedGraphEdge> group = groupedEdgesByFromNode.get(edge.getFrom());
				if (group == null) // this from node is first encountered
				{
					group = new HashSet<DirectedGraphEdge>();
					groupedEdgesByFromNode.put(edge.getFrom(), group);
				}
				group.add(edge);
			}
		}
		return groupedEdgesByFromNode;
	}

	/**
	 * Exception thrown when a cycle found in this graph
	 */
	@SuppressWarnings("serial")
	public static class CycleFoundException extends Exception {

		// a node involved in the circle
		private GraphNode node;

		public CycleFoundException(GraphNode node) {
			this.node = node;
		}

		public GraphNode getNode() {
			return node;
		}
	}
}
