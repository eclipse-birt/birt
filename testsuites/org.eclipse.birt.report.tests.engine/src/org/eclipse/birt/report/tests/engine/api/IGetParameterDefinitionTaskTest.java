
package org.eclipse.birt.report.tests.engine.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.ICascadingParameterGroup;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>IGetParameterDefinitionTask API</b>
 * <p>
 * This test tests all methods in IGetParameterDefinitionTask API
 * 
 */
public class IGetParameterDefinitionTaskTest extends EngineCase
{

	private String name = "IGetParameterDefinitionTaskTest.rptdesign";
	private String input = getClassFolder( ) + "/" + INPUT_FOLDER + "/" + name;
	private IGetParameterDefinitionTask task=null;
	public IGetParameterDefinitionTaskTest( String name )
	{
		super( name );
	}

	/**
	 * Test suite()
	 * 
	 * @return
	 */
	public static Test suite( )
	{
		return new TestSuite( IGetParameterDefinitionTaskTest.class );
	}

	/**
	 * Test getParameterDefns() method
	 * 
	 * @throws Exception
	 */
	public void testGetParameterDefns( ) throws Exception
	{
		boolean includeParameterGroups = true;
		ArrayList params = (ArrayList) task
				.getParameterDefns( includeParameterGroups );
		assertEquals( 6, params.size( ) );
		assertTrue( params.get( 0 ) instanceof IScalarParameterDefn );
		assertTrue( params.get( 1 ) instanceof IScalarParameterDefn );
		assertTrue( params.get( 2 ) instanceof IScalarParameterDefn );
		assertTrue( params.get( 3 ) instanceof IParameterGroupDefn );
		assertTrue( params.get( 4 ) instanceof ICascadingParameterGroup );
		assertTrue( params.get( 5 ) instanceof ICascadingParameterGroup );

		includeParameterGroups = false;
		params = (ArrayList) task.getParameterDefns( includeParameterGroups );
		assertEquals( 10, params.size( ) );
		for ( int i = 0; i < 10; i++ )
		{
			assertTrue( params.get( i ) instanceof IScalarParameterDefn );
		}
		assertEquals( "p1_string", ( (IScalarParameterDefn) params.get( 0 ) )
				.getName( ) );
		assertEquals( "p42_float", ( (IScalarParameterDefn) params.get( 4 ) )
				.getName( ) );
		assertEquals( "p51", ( (IScalarParameterDefn) params.get( 5 ) )
				.getName( ) );
		assertEquals( "p61_country", ( (IScalarParameterDefn) params.get( 7 ) )
				.getName( ) );
	}

	/**
	 * Test getParameterDefn(String) method
	 * @throws Exception
	 */
	public void testGetParameterDefn() throws Exception{
		IParameterDefnBase paramDefn=task.getParameterDefn( "p1_string" );
		assertNotNull(paramDefn);
		paramDefn=task.getParameterDefn( "p2_static_dt" );
		assertNotNull(paramDefn);
		paramDefn=task.getParameterDefn( "p3_dynamic_int" );
		assertNotNull(paramDefn);
		paramDefn=task.getParameterDefn( "p4_group" );
		assertNotNull(paramDefn);
		paramDefn=task.getParameterDefn( "p41_decimal" );
		assertNotNull(paramDefn);
		paramDefn=task.getParameterDefn( "p5_CascadingGroup_single" );
		assertNotNull(paramDefn);
		paramDefn=task.getParameterDefn( "p51" );
		assertNotNull(paramDefn);
		paramDefn=task.getParameterDefn( "p6_CascadingGroup_multiple" );
		assertNotNull(paramDefn);
		paramDefn=task.getParameterDefn( "p62_customernumber" );
		assertNotNull(paramDefn);
		
		paramDefn=task.getParameterDefn( "invalid" );
		assertNull(paramDefn);
		paramDefn=task.getParameterDefn( null );
		assertNull(paramDefn);
		
	}
	
	/**
	 * Test setValue method
	 * This method is not implemented at present.
	 * @throws Exception
	 */
	public void testSetValue() throws Exception{
//		TODO:
	}
	
	/**
	 * Test getDefaultValues() method
	 * @throws Exception
	 */
	public void testGetDefaultValues() throws Exception{
		HashMap values=task.getDefaultValues( );
		assertNotNull(values);
		assertEquals(10,values.size( ));
		assertEquals("abc",values.get( "p1_string" ));
		assertEquals(null,values.get( "p2_static_dt" ));

		assertEquals("2.35",values.get( "p41_decimal" ));
		assertEquals("87.16",values.get( "p42_float" ));
		assertEquals(null,values.get( "p51" ));
		assertEquals("0",values.get( "p52" ));
		assertNull(values.get( "p61_country" ));
		assertNull(values.get( "p61_customernumber" ));
		assertNull(values.get( "p61_orderno" ));
	}
	
	/**
	 * Test getDefaultValue() method
	 * @throws Exception
	 */
	public void testGetDefaultValue() throws Exception{
//		TODO:
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		IReportRunnable reportRunnable = engine.openReportDesign( input );
		task = engine
				.createGetParameterDefinitionTask( reportRunnable );
		assertTrue(task.getErrors( ).size( )==0);
	}

	protected void tearDown( ) throws Exception
	{
		task.close( );
		super.tearDown( );
	}
}
