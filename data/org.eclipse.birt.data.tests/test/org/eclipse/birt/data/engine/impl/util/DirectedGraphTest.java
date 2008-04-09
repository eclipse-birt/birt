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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.data.engine.impl.util.DirectedGraph.CycleFoundException;

import junit.framework.TestCase;


public class DirectedGraphTest extends TestCase
{

	public void testValidateCycle( )
	{
		///////////////////////////////////////////
		GraphNode foundNode = null;
		DirectedGraph graph
			= new DirectedGraph(toSet(new DirectedGraphEdge[]{
					createEdge("a", "b"),
					createEdge("a", "c"),
			}));
		try
		{
			graph.validateCycle( );
		}
		catch ( CycleFoundException e )
		{
			assert false;
		}
		
		///////////////////////////////////////////
		foundNode = null;
		graph
			= new DirectedGraph(toSet(new DirectedGraphEdge[]{
					createEdge("a", "b"),
					createEdge("a", "c"),
					createEdge("a", "a"),
			}));
		try
		{
			graph.validateCycle( );
		}
		catch ( CycleFoundException e )
		{
			foundNode = e.getNode( );
		}
		assert foundNode != null && foundNode.getValue( ).equals( "a" );
		
		///////////////////////////////////////////
		foundNode = null;
		graph
			= new DirectedGraph(toSet(new DirectedGraphEdge[]{
					createEdge("a", "b"),
					createEdge("a", "c"),
					createEdge("a", "d"),
					createEdge("b", "d"),
					createEdge("b", "e"),
					createEdge("b", "f"),
					createEdge("d", "e"),
					createEdge("d", "f"),
			}));
		try
		{
			graph.validateCycle( );
		}
		catch ( CycleFoundException e )
		{
			assert false;
		}
	
		///////////////////////////////////////////
		foundNode = null;
		graph
			= new DirectedGraph(toSet(new DirectedGraphEdge[]{
					createEdge("a", "b"),
					createEdge("a", "c"),
					createEdge("a", "d"),
					createEdge("b", "d"),
					createEdge("b", "e"),
					createEdge("b", "f"),
					createEdge("d", "e"),
					createEdge("d", "f"),
					createEdge("d", "a"),
			}));
		try
		{
			graph.validateCycle( );
		}
		catch ( CycleFoundException e )
		{
			foundNode = e.getNode( );
		}
		assert foundNode != null && foundNode.getValue( ).equals( "d" );
		
		///////////////////////////////////////////
		foundNode = null;
		graph
			= new DirectedGraph(toSet(new DirectedGraphEdge[]{
					createEdge("a", "b"),
					createEdge("a", "c"),
					createEdge("a", "d"),
					createEdge("e", "a"),
					createEdge("e", "b"),
					createEdge("c", "d"),
			}));
		try
		{
			graph.validateCycle( );
		}
		catch ( CycleFoundException e )
		{
			assert false;
		}
		
		///////////////////////////////////////////
		foundNode = null;
		graph
			= new DirectedGraph(toSet(new DirectedGraphEdge[]{
					createEdge("a", "b"),
					createEdge("a", "c"),
					createEdge("a", "d"),
					createEdge("e", "a"),
					createEdge("e", "b"),
					createEdge("c", "d"),
					createEdge("c", "e"),
			}));
		try
		{
			graph.validateCycle( );
		}
		catch ( CycleFoundException e )
		{
			foundNode = e.getNode( );
		}
		assert foundNode != null && foundNode.getValue( ).equals( "c" );
		
		///////////////////////////////////////////
		foundNode = null;
		graph
			= new DirectedGraph(toSet(new DirectedGraphEdge[]{
					createEdge("a", "b"),
					createEdge("a", "c"),
					createEdge("b", "c"),
					createEdge("c", "d"),
					createEdge("d", "b"),
			}));
		try
		{
			graph.validateCycle( );
		}
		catch ( CycleFoundException e )
		{
			foundNode = e.getNode( );
		}
		assert foundNode != null && foundNode.getValue( ).equals( "d" );
	}

	private DirectedGraphEdge createEdge(String from, String to)
	{
		return new DirectedGraphEdge(
				new GraphNode(from), new GraphNode(to));
	}
	
	private Set<DirectedGraphEdge> toSet(DirectedGraphEdge[] src)
	{
		return new HashSet<DirectedGraphEdge>(Arrays.asList( src ));
	}
}
