
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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
import org.eclipse.birt.report.data.adapter.api.IBindingMetaInfo;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.data.adapter.api.IDimensionLevel;
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
	
	public void testGetReferredDimLevel( ) throws Exception
	{
		IDimensionLevel[] dimSet = this.session.getCubeQueryUtil( ).getReferencedDimensionLevel( "dimension[\"dim1\"][\"lvl1\"][\"attr1\"]+dimension[\"dim2\"][\"lvl2\"][\"attr2\"]" );
		assertEquals( dimSet.length, 2 );
		
		assertEquals( "dim2", dimSet[0].getDimensionName( ));
		assertEquals( "lvl2", dimSet[0].getLevelName( ));
		assertEquals( "attr2", dimSet[0].getAttributeName( ));

		
		assertEquals( "dim1", dimSet[1].getDimensionName( ));
		assertEquals( "lvl1", dimSet[1].getLevelName( ));
		assertEquals( "attr1", dimSet[1].getAttributeName( ));

	}
	public void testGetReferableBindings( ) throws AdapterException, DataException
	{
		IBinding binding1 = new Binding( "b1", new ScriptExpression("dimension[\"dim1\"][\"level1\"]"));
		int type1 = IBindingMetaInfo.DIMENSION_TYPE;
		IBinding binding2 = new Binding( "b2", new ScriptExpression("dimension.dim1.level2 + 1"));
		int type2 = IBindingMetaInfo.DIMENSION_TYPE;
		IBinding binding3 = new Binding( "b3", new ScriptExpression("dimension[\"dim1\"][\"level3\"] + dimension.dim1.level2"));
		int type3 = IBindingMetaInfo.DIMENSION_TYPE;
		IBinding binding4 = new Binding( "b4", new ScriptExpression("data.b1"));
		int type4 = IBindingMetaInfo.DIMENSION_TYPE;
		IBinding binding5 = new Binding( "b5", new ScriptExpression("dimension.dim1.level1 + 25"));
		int type5 = IBindingMetaInfo.DIMENSION_TYPE;
		IBinding binding6 = new Binding( "b6", new ScriptExpression("data.b4 + 1"));
		int type6 = IBindingMetaInfo.DIMENSION_TYPE;
		IBinding binding7 = new Binding( "b7", new ScriptExpression("measure[\"abc\"]"));
		binding7.addAggregateOn( "dimension[\"dim1\"][\"level2\"]" );
		binding7.addAggregateOn( "dimension[\"dim1\"][\"level1\"]" );
		binding7.addAggregateOn( "dimension[\"dim2\"][\"level1\"]" );
		int type7 = IBindingMetaInfo.SUB_TOTAL_TYPE;
		
		IBinding binding71 = new Binding( "b71", new ScriptExpression("measure[\"abc\"]"));
		binding71.addAggregateOn( "dimension[\"dim1\"][\"level1\"]" );
		binding71.addAggregateOn( "dimension[\"dim1\"][\"level2\"]" );
		binding71.addAggregateOn( "dimension[\"dim2\"][\"level1\"]" );
		int type71 = IBindingMetaInfo.SUB_TOTAL_TYPE;
		
		IBinding binding8 = new Binding( "b8", new ScriptExpression("dimension[\"dim2\"][\"level1\"]"));
		int type8 = IBindingMetaInfo.DIMENSION_TYPE;
		
		IBinding binding9 = new Binding( "b9", new ScriptExpression( "measure[\"abc\"]"));
		binding9.addAggregateOn( "dimension[\"dim1\"][\"level1\"]"  );
		binding9.addAggregateOn( "dimension[\"dim1\"][\"level2\"]"  );
		int type9 = IBindingMetaInfo.GRAND_TOTAL_TYPE;
		
		IBinding binding10 = new Binding( "b10", new ScriptExpression( "measure[\"abc\"]"));
		int type10 = IBindingMetaInfo.MEASURE_TYPE;
		
		IBinding binding11 = new Binding( "b11", new ScriptExpression( "measure[\"abc\"]"));
		binding11.setAggrFunction( "SUM" );
		int type11 = IBindingMetaInfo.GRAND_TOTAL_TYPE;
		
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
		assertTrue( contains( l1, binding1, type1 ));
		assertTrue( contains( l1, binding4, type4 ));
		assertTrue( contains( l1, binding7, type7 ));
		
		String targetLevel2 = "dimension[\"dim1\"][\"level1\"]";
		List l2 = this.session.getCubeQueryUtil( ).getReferableBindings( targetLevel2, query, false );
		assertTrue( l2.size( ) == 5 );
		assertTrue( contains( l2, binding1, type1 ));
		assertTrue( contains( l2, binding4, type4 ));
		assertTrue( contains( l2, binding5, type5 ));
		assertTrue( contains( l2, binding6, type6 ));
		assertTrue( contains( l2, binding7, type7 ));
	
		String targetLevel3 = "dimension[\"dim1\"][\"level2\"]";
		List l3 = this.session.getCubeQueryUtil( ).getReferableBindings( targetLevel3, query, false );
		assertTrue( l3.size( ) == 2 );
		assertTrue( contains( l3, binding2, type2 ));
		assertTrue( contains( l3, binding71, type71 ));
	
		String targetLevel4 = "dimension[\"dim1\"][\"level2\"]";
		query.addBinding( binding9 );
		List l4 = this.session.getCubeQueryUtil( ).getReferableBindings( targetLevel4, query, false );
		
		assertTrue( l4.size( ) == 3 );
		assertTrue( contains( l4, binding2, type2 ));
		assertTrue( contains( l4, binding71, type71 ));
		assertTrue( contains( l4, binding9, type9 ));
		
		String targetLevel5 = "dimension[\"dim2\"][\"level1\"]";
		query.addBinding( binding10 );
		List l5 = this.session.getCubeQueryUtil( ).getReferableBindings( targetLevel5, query, false );
		assertTrue( l5.size( )==4 );
		assertTrue( contains( l5, binding7, type7 ));
		assertTrue( contains( l5, binding71, type71 ));
		assertTrue( contains( l5, binding8, type8 ));
		assertTrue( contains( l5, binding10, type10 ));
		
		String targetLevel6 = "dimension[\"dim1\"][\"level2\"]";
		query.addBinding( binding11 );
		List l6 = this.session.getCubeQueryUtil( ).getReferableBindings( targetLevel6, query, false );
		assertTrue( l6.size( ) == 4 );
		assertTrue( contains( l6, binding2, type2 ));
		assertTrue( contains( l6, binding71, type71 ));
		assertTrue( contains( l6, binding9, type9 ));
		assertTrue( contains( l6, binding11, type11 ));
	}
	
	/**
	 * 
	 * @param l
	 * @param binding
	 * @param type
	 * @return
	 * @throws DataException
	 */
	private boolean contains ( List l, IBinding binding, int type ) throws DataException
	{
		for( int i = 0; i < l.size( ); i++ )
		{
			IBindingMetaInfo bm = (IBindingMetaInfo)l.get( i );
			if( bm.getBindingName( ).equals( binding.getBindingName( ) ) && bm.getBindingType( ) == type )
				return true;
		}
		return false;
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
	
	public void testIsValidDimensionName( )
	{
		ICubeQueryUtil cubeQueryUtil = this.session.getCubeQueryUtil( );
		String testNames[] = new String[]{
				"12345678",
				"1234.5678",
				"123abc",
				"123+^#",
				"123.456.78",
				"abcdefg",
				"abc.def",
				".123.456.",
				"..123..456",
				"123.456.?",
				"........",
				"123  456"
		};
		boolean results[] = new boolean[]{
				false,
				false,
				true,
				true,
				false,
				true,
				true,
				false,
				false,
				true,
				false,
				true
		};

		assert testNames.length == results.length;
		for ( int i = 0; i < testNames.length; i++ )
		{
			assertEquals( cubeQueryUtil.isValidDimensionName( testNames[i] ),
					results[i] );
		}
	}
	
	public void tearDown()
	{
		this.session.shutdown( );
	}
}
