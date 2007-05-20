
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.internal.script;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.impl.DataRequestSessionImpl;


/**
 * 
 */

public class DataRequestSessionTest extends TestCase
{
	private DataRequestSession session = null;
	
	public void setUp() throws BirtException
	{
		this.session = new DataRequestSessionImpl( new DataSessionContext(DataEngineContext.DIRECT_PRESENTATION));
	}
	
	public void testGetReferableBindings( ) throws AdapterException, DataException
	{
		IBinding binding1 = new Binding( "b1", new ScriptExpression("dimension[\"dim1\"][\"level1\"]"));
		IBinding binding2 = new Binding( "b2", new ScriptExpression("dimension.dim1.level2 + 1"));
		IBinding binding3 = new Binding( "b3", new ScriptExpression("dimension[\"dim1\"][\"level3\"] + dimension.dim1.level2"));
		IBinding binding4 = new Binding( "b4", new ScriptExpression("data.b1"));
		IBinding binding5 = new Binding( "b5", new ScriptExpression("dimension.dim1.level1 + 25"));
		IBinding binding6 = new Binding( "b6", new ScriptExpression("data.b4 + 1"));
		IBinding binding7 = new Binding( "b7", new ScriptExpression("measure[\"abc\"]"));
		binding7.addAggregateOn( "dimension[\"dim1\"][\"level2\"]" );
		binding7.addAggregateOn( "dimension[\"dim1\"][\"level1\"]" );
		binding7.addAggregateOn( "dimension[\"dim2\"][\"level1\"]" );
		
		IBinding binding71 = new Binding( "b71", new ScriptExpression("measure[\"abc\"]"));
		binding71.addAggregateOn( "dimension[\"dim1\"][\"level1\"]" );
		binding71.addAggregateOn( "dimension[\"dim1\"][\"level2\"]" );
		binding71.addAggregateOn( "dimension[\"dim2\"][\"level1\"]" );
		
		IBinding binding8 = new Binding( "b8", new ScriptExpression("dimension[\"dim2\"][\"level1\"]"));
		
		ICubeQueryDefinition query = new CubeQueryDefinition( "query");
		query.addBinding( binding1 );
		query.addBinding( binding2 );
		query.addBinding( binding3 );
		query.addBinding( binding4 );
		query.addBinding( binding5 );
		query.addBinding( binding6 );
		query.addBinding( binding7 );
		query.addBinding( binding71 );
		query.addBinding( binding8 );
		
		IEdgeDefinition columnEdge = query.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dim1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "hier1" );
		hier1.createLevel( "level1" );
		hier1.createLevel( "level2" );
		hier1.createLevel( "level3" );
		
		
		IEdgeDefinition rowEdge = query.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dim2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "hier2" );
		hier2.createLevel( "level1" );
		
		String targetLevel1 = "dimension[\"dim1\"][\"level1\"]";
		List l1 = this.session.getCubeQueryUtil( ).getReferableBindings( targetLevel1, query, true );
		assertTrue( l1.size( ) == 3 );
		assertTrue( l1.contains( binding1 ));
		assertTrue( l1.contains( binding4 ));
		assertTrue( l1.contains( binding7 ));
		
		String targetLevel2 = "dimension[\"dim1\"][\"level1\"]";
		List l2 = this.session.getCubeQueryUtil( ).getReferableBindings( targetLevel2, query, false );
		assertTrue( l2.size( ) == 5 );
		assertTrue( l2.contains( binding1 ));
		assertTrue( l2.contains( binding4 ));
		assertTrue( l2.contains( binding5 ));
		assertTrue( l2.contains( binding6 ));
		assertTrue( l2.contains( binding7 ));
	
		String targetLevel3 = "dimension[\"dim1\"][\"level2\"]";
		List l3 = this.session.getCubeQueryUtil( ).getReferableBindings( targetLevel3, query, false );
		assertTrue( l3.size( ) == 2 );
		assertTrue( l3.contains( binding2 ));
		assertTrue( l3.contains( binding71 ));
	}
	
	public void testGetReferencedLevels() throws DataException, AdapterException
	{
		IBinding binding1 = new Binding( "b1", new ScriptExpression("dimension[\"dim1\"][\"level1\"]"));
		IBinding binding2 = new Binding( "b2", new ScriptExpression("dimension.dim1.level2 + 1"));
		IBinding binding3 = new Binding( "b3", new ScriptExpression("dimension[\"dim1\"][\"level3\"] + dimension.dim1.level2"));
		IBinding binding4 = new Binding( "b4", new ScriptExpression("data.b1"));
		IBinding binding5 = new Binding( "b5", new ScriptExpression("dimension.dim1.level1 + 25"));
		IBinding binding6 = new Binding( "b6", new ScriptExpression("data.b4 + 1"));
		IBinding binding7 = new Binding( "b7", new ScriptExpression("measure[\"abc\"]"));
		binding7.addAggregateOn( "dimension[\"dim1\"][\"level2\"]" );
		binding7.addAggregateOn( "dimension[\"dim1\"][\"level1\"]" );
		binding7.addAggregateOn( "dimension[\"dim2\"][\"level1\"]" );
		
		IBinding binding71 = new Binding( "b71", new ScriptExpression("measure[\"abc\"]"));
		binding71.addAggregateOn( "dimension[\"dim1\"][\"level1\"]" );
		binding71.addAggregateOn( "dimension[\"dim1\"][\"level2\"]" );
		binding71.addAggregateOn( "dimension[\"dim2\"][\"level1\"]" );
		
		IBinding binding8 = new Binding( "b8", new ScriptExpression("dimension[\"dim2\"][\"level1\"]"));
		
		ICubeQueryDefinition query = new CubeQueryDefinition( "query");
		query.addBinding( binding1 );
		query.addBinding( binding2 );
		query.addBinding( binding3 );
		query.addBinding( binding4 );
		query.addBinding( binding5 );
		query.addBinding( binding6 );
		query.addBinding( binding7 );
		query.addBinding( binding71 );
		query.addBinding( binding8 );
		
		IEdgeDefinition columnEdge = query.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dim1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "hier1" );
		hier1.createLevel( "level1" );
		hier1.createLevel( "level2" );
		hier1.createLevel( "level3" );
		
		
		IEdgeDefinition rowEdge = query.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dim2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "hier2" );
		hier2.createLevel( "level1" );

		List l1 = this.session.getCubeQueryUtil( ).getReferencedLevels( "dimension[\"dim1\"][\"level1\"]", "data[\"b1\"]", query );
		assertTrue( l1.size( ) == 0 );
		
		List l2 = this.session.getCubeQueryUtil( ).getReferencedLevels( "dimension[\"dim1\"][\"level1\"]", "data[\"b7\"]", query );
		assertTrue( l2.size( ) == 1 );
		assertTrue( ((ILevelDefinition)l2.get(0)).getName( ).equals( "level1" ));
		
		List l3 = this.session.getCubeQueryUtil( ).getReferencedLevels( "dimension[\"dim2\"][\"level1\"]", "data[\"b71\"]", query );
		assertTrue( l3.size( ) == 2 );
		assertTrue( ((ILevelDefinition)l3.get(0)).getName( ).equals( "level1" ));
		assertTrue( ((ILevelDefinition)l3.get(1)).getName( ).equals( "level2" ));
	}
	
	public void tearDown()
	{
		this.session.shutdown( );
	}
}
