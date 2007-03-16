
package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>IScalarParameterDefn test</b>
 * <p>
 * This case tests methods in IScalarParameterDefn API.
 */
public class IScalarParameterDefnTest extends EngineCase
{

	private String INPUT = "scalarparameters.rptdesign";
	private IGetParameterDefinitionTask paramTask = null;
	private IScalarParameterDefn scalarDefn = null;
	private String report = this.genInputFile( INPUT );

	public IScalarParameterDefnTest( String name ) throws EngineException
	{
		super( name );
	}

	public static Test Suite( )
	{
		return new TestSuite( IScalarParameterDefnTest.class );
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( INPUT, INPUT );
		IReportRunnable runnable;
		runnable = engine.openReportDesign( report );
		paramTask = engine.createGetParameterDefinitionTask( runnable );

	}

	protected void tearDown( ) throws Exception
	{
		paramTask.close( );
		engine.shutdown( );
		removeResource( );
		super.tearDown( );
	}

	/*
	 * test getDefaultValue method
	 */
	public void testgetDefaultValue( )
	{
		// string parameter
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_string" );
		assertEquals( "abc", scalarDefn.getDefaultValue( ) );
		// int parameter
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_int" );
		assertEquals( "123", scalarDefn.getDefaultValue( ) );
	}

	/*
	 * test isValueConcealed method
	 */
	public void testisValueConcealed( )
	{
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_conceal" );
		assertTrue( "IsValueConcealed method failed", scalarDefn
				.isValueConcealed( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_string" );
		assertFalse( "IsValueConcealed method failed", scalarDefn
				.isValueConcealed( ) );
	}

	/*
	 * test allowNull method
	 */
	public void testallowNull( )
	{
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_allowbn" );
		assertTrue( "AllowNull method failed", scalarDefn.allowNull( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_string" );
		assertFalse( "AllowNull method failed", scalarDefn.allowNull( ) );
	}

	/*
	 * test allowBlank method
	 */
	public void testallowBlank( )
	{
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_allowbn" );
		assertTrue( "AllowBlank method failed", scalarDefn.allowBlank( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_string" );
		assertFalse( "AllowBlank method failed", scalarDefn.allowBlank( ) );
	}

	/*
	 * test getDisplayFormat method
	 */
	public void testgetDisplayFormat( )
	{
		// string display format
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_dispform_string" );
		assertEquals( "GetDisplayFormat method failed to get string format",
				"(@@)", scalarDefn.getDisplayFormat( ) );

		// datetime display format
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_dispform_dt" );
		assertEquals( "GetDisplayFormat method failed to get datetime format",
				"Short Date", scalarDefn.getDisplayFormat( ) );

		// number display format
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_dispform_num" );
		assertEquals( "GetDisplayFormat method failed to get number format",
				"Scientific", scalarDefn.getDisplayFormat( ) );
	}

	/*
	 * test getControlType method
	 */
	public void testgetControlType( )
	{
		// text box
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_string" );
		assertEquals( "GetControlType method failed to get textbox type",
				IScalarParameterDefn.TEXT_BOX, scalarDefn.getControlType( ) );
		// list box
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_list" );
		assertEquals( "GetControlType method failed to get textbox type",
				IScalarParameterDefn.LIST_BOX, scalarDefn.getControlType( ) );
	}

	/*
	 * test getAlignment method
	 */
	public void testgetAlignment( )
	{
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_align" );
		assertEquals( "GetAlignment method failed", IScalarParameterDefn.RIGHT,
				scalarDefn.getAlignment( ) );
	}

	/*
	 * test getSelectionListType method
	 */
	public void testgetSelectionListType_static( )
	{
		// static
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_list" );
		assertEquals( "GetSelectionListType method failed to get static type",
				IScalarParameterDefn.SELECTION_LIST_STATIC, scalarDefn
						.getSelectionListType( ) );
	}

	public void testgetSelectionListType_dynamic( )
	{
		// dynamic
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_dynamic" );
		assertEquals( "GetSelectionListType method failed to get dynamic type",
				IScalarParameterDefn.SELECTION_LIST_DYNAMIC, scalarDefn
						.getSelectionListType( ) );
	}

	public void testGetSelectionListType_none( )
	{
		// none
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_string" );
		assertEquals( "GetSelectionListType method failed to get none type",
				IScalarParameterDefn.SELECTION_LIST_NONE, scalarDefn
						.getSelectionListType( ) );

	}

	/*
	 * test displayInFixedOrder method
	 */
	public void testdisplayInFixedOrder( )
	{
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_list" );
		assertTrue( "DisplayInFixedOrder should return true", scalarDefn
				.displayInFixedOrder( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_combo_sort" );
		assertFalse( "DisplayInFixedOrder should return false", scalarDefn
				.displayInFixedOrder( ) );

	}

	/*
	 * test getParameterType method
	 */
	public void testgetDataType( )
	{
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_string" );
		assertEquals( "GetDataType method failed to get string type",
				IScalarParameterDefn.TYPE_STRING, scalarDefn.getDataType( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_boolean" );
		assertEquals( "GetDataType method failed to get string type",
				IScalarParameterDefn.TYPE_BOOLEAN, scalarDefn.getDataType( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_datetime_format" );
		assertEquals( "GetDataType method failed to get string type",
				IScalarParameterDefn.TYPE_DATE_TIME, scalarDefn.getDataType( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_int" );
		assertEquals( "GetDataType method failed to get integer type",
				IScalarParameterDefn.TYPE_INTEGER, scalarDefn.getDataType( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_dispform_num" );
		assertEquals( "GetDataType method failed to get string type",
				IScalarParameterDefn.TYPE_DECIMAL, scalarDefn.getDataType( ) );

	}

	/*
	 * test allowNewValues method
	 */
	public void testallowNewValues( )
	{
		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_list" );
		assertFalse( "AllowNewValues method should return true for listbox",
				scalarDefn.allowNewValues( ) );

		scalarDefn = (IScalarParameterDefn) paramTask
				.getParameterDefn( "p_combo_sort" );
		assertTrue( "AllowNewValues method should return false for combobox",
				scalarDefn.allowNewValues( ) );
	}
}
