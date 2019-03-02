/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.util;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.input.InputFileOpener;
import org.eclipse.birt.data.oda.pojo.querymodel.ClassColumnMappings;
import org.eclipse.birt.data.oda.pojo.querymodel.Column;
import org.eclipse.birt.data.oda.pojo.querymodel.ConstantParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.FieldSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.OneColumnMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;
import org.eclipse.birt.data.oda.pojo.util.PojoQueryParser;
import org.eclipse.datatools.connectivity.oda.OdaException;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class PojoQueryParserTest {

	@SuppressWarnings("nls")
	@Test
    public void testBasicParse( ) throws IOException, OdaException
	{
		InputStream in = InputFileOpener.openFile( InputFileOpener.SIMPLE_QUERY_FILE );
		
		String text = InputFileOpener.fromInputStream( in );
		
		PojoQuery query = PojoQueryParser.parse( text ); 
		
		assertEquals( query.getVersion( ), "1.0" );
		assertEquals( query.getDataSetClass( ), "org.eclipse.birt.data.oda.pojo.input.TeacherDataSet" );
		assertEquals( query.getAppContextKey( ), "TeacherDataSet_Key" );
		
		IColumnsMapping[] mappings = query.getColumnsMappings( );
		assertEquals( mappings.length, 5 );
		
		ClassColumnMappings ccm = ( ClassColumnMappings) mappings[0];
		IColumnsMapping[] internals = ccm.getColumnsMappings( );
		assertTrue( internals.length == 2 );
		assertEqualMappingSource( ccm.getSource( ), 
				new MethodSource("getDean", null));
		assertEqualColumnsMapping( internals[0], 
				new OneColumnMapping( new MethodSource("getDeanID", null), new Column("Dean ID", "Integer", 1)) );
		assertEqualColumnsMapping( internals[1], 
				new OneColumnMapping( new MethodSource("getDeanName", null), new Column("Dean Name", "String", 2)) );
		
		ccm = ( ClassColumnMappings) mappings[1];
		internals = ccm.getColumnsMappings( );
		assertTrue( internals.length == 2 );
		assertEqualMappingSource( ccm.getSource( ), 
				new MethodSource("getStudents", new IMethodParameter[]{
						new ConstantParameter( "18", Constants.PARAM_TYPE_int),
						new VariableParameter( "sex", Constants.PARAM_TYPE_boolean ),
						new ConstantParameter( null, Constants.PARAM_TYPE_String)
				}));
		assertEqualColumnsMapping( internals[0], 
				new OneColumnMapping( new MethodSource("getStudentID", null), new Column("Student ID", "Integer", 3)) );
		assertEqualColumnsMapping( internals[1], 
				new OneColumnMapping( new MethodSource("getStudentName", null), new Column("Student Name", "String", 4)) );
		
		OneColumnMapping ocm = ( OneColumnMapping) mappings[2];
		assertEqualColumnsMapping( ocm, 
				new OneColumnMapping( new MethodSource("getTeacherID", null), new Column("Teacher ID", "Integer", 5)) );

		ocm = ( OneColumnMapping) mappings[3];
		assertEqualColumnsMapping( ocm, 
				new OneColumnMapping( new MethodSource("getTeacherName", null), new Column("Teacher Name", "String", 6)) );
		ocm = ( OneColumnMapping) mappings[4];
		assertEqualColumnsMapping( ocm, 
				new OneColumnMapping( new FieldSource("age"), new Column("Age", "Integer", 7)) );
		
		in.close( );
	}


	
	public static void assertEqualMappingSource( IMappingSource s1, IMappingSource s2 )
	{
		assert s1 != null && s2 != null;
		assertTrue( s1.equals( s2 ) );
	}
	
	public static void assertEqualColumn( Column c1, Column c2 )
	{
		assert c1 != null && c2 != null;
		assertEquals( c1.getName( ), c2.getName( ));
		assertEquals( c1.getOdaType( ), c2.getOdaType( ));
		assertEquals( c1.getIndex( ), c2.getIndex( ));
	}
	
	public static void assertEqualColumnsMapping( IColumnsMapping cm1, IColumnsMapping cm2 )
	{
		assert cm1 != null && cm2 != null;
		assertEquals( cm1.getClass( ), cm2.getClass( ) );
		assertEqualMappingSource( cm1.getSource( ), cm2.getSource( ) );
		if ( cm1 instanceof OneColumnMapping )
		{
			assertEqualColumn( ( (OneColumnMapping) cm1 ).getMappedColumn( ), ((OneColumnMapping) cm2 ).getMappedColumn( ));
			return;
		}
		else if ( cm1 instanceof ClassColumnMappings )
		{
			IColumnsMapping[] internals1 = ((ClassColumnMappings)cm1).getColumnsMappings( );
			IColumnsMapping[] internals2 = ((ClassColumnMappings)cm1).getColumnsMappings( );
			assertEquals( internals1.length, internals2.length );
			for ( int i = 0; i < internals1.length; i++ )
			{
				assertEqualColumnsMapping( internals1[i], internals2[i]);
			}
			return;
		}
		//Should never goes here
		assertTrue( false );
	}
}
