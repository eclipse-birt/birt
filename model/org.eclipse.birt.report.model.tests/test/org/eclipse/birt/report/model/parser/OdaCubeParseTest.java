
package org.eclipse.birt.report.model.parser;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.AccessControlHandle;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.ValueAccessControlHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.Rule;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.OdaCubeHandle;
import org.eclipse.birt.report.model.api.olap.OdaHierarchyHandle;
import org.eclipse.birt.report.model.elements.interfaces.IAccessControlModel;
import org.eclipse.birt.report.model.elements.interfaces.IValueAccessControlModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests case for parsing and writing ODA OLAP elements.
 * 
 */

public class OdaCubeParseTest extends BaseTestCase
{

	private final String FILE_NAME = "OdaCubeParserTest.xml"; //$NON-NLS-1$

	/**
	 * 
	 * @throws Exception
	 */

	public void testParser( ) throws Exception
	{
		openDesign( FILE_NAME );
		assertNotNull( designHandle );

		// cube

		OdaCubeHandle cube = (OdaCubeHandle) designHandle.findCube( "testCube" ); //$NON-NLS-1$

		// filter
		Iterator iter = cube.filtersIterator( );
		FilterConditionHandle filterConditionHandle = (FilterConditionHandle) iter
				.next( );
		assertEquals( "filter expression", filterConditionHandle.getExpr( ) ); //$NON-NLS-1$

		// access controls

		// access controls on cube.

		Iterator iter1 = cube.accessControlsIterator( );
		AccessControlHandle accessControl = (AccessControlHandle) iter1.next( );

		assertEquals( "cube user1; cube user2", accessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.USER_NAMES_PROP )
				.getStringValue( ) );
		assertEquals( "cube role1; cube role2", accessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.ROLES_PROP )
				.getStringValue( ) );
		assertEquals( DesignChoiceConstants.ACCESS_PERMISSION_DISALLOW,
				accessControl.getPermission( ) );

		PropertyHandle propHandle = cube
				.getPropertyHandle( OdaCubeHandle.DIMENSIONS_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, cube.getContentCount( OdaCubeHandle.DIMENSIONS_PROP ) );

		// dimension
		DimensionHandle dimension = (DimensionHandle) propHandle.getContent( 0 );
		assertEquals( dimension, cube.getContent(
				OdaCubeHandle.DIMENSIONS_PROP, 0 ) );
		assertEquals( "testDimension", dimension.getName( ) ); //$NON-NLS-1$
		assertTrue( dimension.isTimeType( ) );
		propHandle = dimension
				.getPropertyHandle( DimensionHandle.HIERARCHIES_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, dimension
				.getContentCount( DimensionHandle.HIERARCHIES_PROP ) );

		// hierarchy
		OdaHierarchyHandle hierarchy = (OdaHierarchyHandle) propHandle
				.getContent( 0 );
		assertEquals( hierarchy, dimension.getContent(
				DimensionHandle.HIERARCHIES_PROP, 0 ) );
		// test getDefaultHierarchy in dimension
		assertEquals( hierarchy, dimension.getDefaultHierarchy( ) );
		assertEquals( "testHierarchy", hierarchy.getName( ) ); //$NON-NLS-1$

		// access controls on hierarchy.

		iter1 = hierarchy.accessControlsIterator( );
		accessControl = (AccessControlHandle) iter1.next( );

		assertEquals( "hierarchy user1; hierarchy user2", accessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.USER_NAMES_PROP )
				.getStringValue( ) );
		assertEquals( "hierarchy role1; hierarchy role2", accessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.ROLES_PROP )
				.getStringValue( ) );
		assertEquals( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW,
				accessControl.getPermission( ) );

		propHandle = cube.getPropertyHandle( OdaCubeHandle.DIMENSIONS_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, cube.getContentCount( OdaCubeHandle.DIMENSIONS_PROP ) );

		// filter
		iter = hierarchy.filtersIterator( );
		filterConditionHandle = (FilterConditionHandle) iter.next( );
		assertEquals( "filter expression", filterConditionHandle.getExpr( ) ); //$NON-NLS-1$

		propHandle = hierarchy
				.getPropertyHandle( OdaHierarchyHandle.LEVELS_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, hierarchy
				.getContentCount( OdaHierarchyHandle.LEVELS_PROP ) );

		// level
		LevelHandle level = (LevelHandle) propHandle.getContent( 0 );
		assertEquals( level, hierarchy.getContent(
				OdaHierarchyHandle.LEVELS_PROP, 0 ) );
		assertEquals( "testLevel", level.getName( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, level
				.getDataType( ) );
		assertEquals( "week", level.getInterval( ) ); //$NON-NLS-1$
		assertEquals( 3.0, level.getIntervalRange( ), 0.00 );
		assertEquals( "Jan", level.getIntervalBase( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC, level
				.getLevelType( ) );
		iter = level.staticValuesIterator( );
		RuleHandle rule = (RuleHandle) iter.next( );
		assertEquals( "rule expression", rule.getRuleExpression( ) ); //$NON-NLS-1$
		assertEquals( "display expression", rule.getDisplayExpression( ) ); //$NON-NLS-1$
		rule = (RuleHandle) iter.next( );
		assertEquals( "rule expression2", rule.getRuleExpression( ) ); //$NON-NLS-1$
		assertEquals( "display expression2", rule.getDisplayExpression( ) ); //$NON-NLS-1$
		iter = level.attributesIterator( );
		ConfigVariableHandle attribute = (ConfigVariableHandle) iter.next( );
		assertEquals( "var1", attribute.getName( ) ); //$NON-NLS-1$
		assertEquals( "mumble.jpg", attribute.getValue( ) ); //$NON-NLS-1$
		attribute = (ConfigVariableHandle) iter.next( );
		assertEquals( "var2", attribute.getName( ) ); //$NON-NLS-1$
		assertEquals( "abcdefg", attribute.getValue( ) ); //$NON-NLS-1$

		// access controls on level.

		iter1 = level.valueAccessControlsIterator( );
		ValueAccessControlHandle valueAccessControl = (ValueAccessControlHandle) iter1
				.next( );

		assertEquals( "level user1; level user2", valueAccessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.USER_NAMES_PROP )
				.getStringValue( ) );
		assertEquals( "level role1; level role2", valueAccessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.ROLES_PROP )
				.getStringValue( ) );
		assertEquals( "level value1; level value2", valueAccessControl //$NON-NLS-1$
				.getPropertyHandle( IValueAccessControlModel.VALUES_PROP )
				.getStringValue( ) );
		assertEquals( DesignChoiceConstants.ACCESS_PERMISSION_DISALLOW,
				valueAccessControl.getPermission( ) );

		// measure group
		propHandle = cube.getPropertyHandle( OdaCubeHandle.MEASURE_GROUPS_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, cube
				.getContentCount( OdaCubeHandle.MEASURE_GROUPS_PROP ) );
		MeasureGroupHandle measureGroup = (MeasureGroupHandle) propHandle
				.getContent( 0 );
		assertEquals( measureGroup, cube.getContent(
				OdaCubeHandle.MEASURE_GROUPS_PROP, 0 ) );
		assertEquals( "testMeasureGroup", measureGroup.getName( ) ); //$NON-NLS-1$
		// test getDefaultMeasureGroup in cube
		assertEquals( measureGroup, cube.getDefaultMeasureGroup( ) );
		propHandle = measureGroup
				.getPropertyHandle( MeasureGroupHandle.MEASURES_PROP );

		// measure
		MeasureHandle measure = (MeasureHandle) propHandle.getContent( 0 );
		assertEquals( "testMeasure", measure.getName( ) ); //$NON-NLS-1$
		assertEquals( "column", measure.getMeasureExpression( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.MEASURE_FUNCTION_MIN, measure
				.getFunction( ) );
		assertFalse( measure.isCalculated( ) );

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testWriter( ) throws Exception
	{
		openDesign( FILE_NAME );
		assertNotNull( designHandle );
		String namePrix = "new"; //$NON-NLS-1$
		String valuePrix = "updated "; //$NON-NLS-1$
		ElementFactory factory = designHandle.getElementFactory( );

		// cube
		OdaCubeHandle cube = (OdaCubeHandle) designHandle.findCube( "testCube" ); //$NON-NLS-1$
		cube.setName( namePrix + cube.getName( ) );
		cube.setDefaultMeasureGroup( factory
				.newOdaMeasureGroup( "testDefaultMeasureGroup" ) ); //$NON-NLS-1$

		// access controls on cube.

		AccessControlHandle accessControl = (AccessControlHandle) cube
				.accessControlsIterator( ).next( );

		accessControl.addUserName( "new cube user1" ); //$NON-NLS-1$
		accessControl.addUserName( "new cube user2" ); //$NON-NLS-1$

		accessControl.addRole( "new cube role1" ); //$NON-NLS-1$
		accessControl.addRole( "new cube role2" ); //$NON-NLS-1$

		accessControl
				.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );

		// add a new access control

		PropertyHandle propHandle = cube
				.getPropertyHandle( OdaCubeHandle.ACCESS_CONTROLS_PROP );

		accessControl = designHandle.getElementFactory( ).newAccessControl( );
		propHandle.add( accessControl );

		// dimension
		cube
				.add( OdaCubeHandle.DIMENSIONS_PROP, factory
						.newOdaDimension( null ) );
		DimensionHandle dimension = (DimensionHandle) cube.getContent(
				OdaCubeHandle.DIMENSIONS_PROP, 0 );
		dimension.setName( namePrix + dimension.getName( ) );
		dimension.setTimeType( false );
		dimension.setDefaultHierarchy( factory
				.newOdaHierarchy( "testDefaultHierarchy" ) ); //$NON-NLS-1$

		// hierarchy
		dimension.add( DimensionHandle.HIERARCHIES_PROP, factory
				.newOdaHierarchy( null ) );
		OdaHierarchyHandle hierarchy = (OdaHierarchyHandle) dimension
				.getContent( DimensionHandle.HIERARCHIES_PROP, 0 );
		hierarchy.setName( namePrix + hierarchy.getName( ) );

		// access controls on hierarchy.

		accessControl = (AccessControlHandle) hierarchy
				.accessControlsIterator( ).next( );

		accessControl.addUserName( "new hierarchy user1" ); //$NON-NLS-1$
		accessControl.addUserName( "new hierarchy user2" ); //$NON-NLS-1$

		accessControl.addRole( "new hierarchy role1" ); //$NON-NLS-1$
		accessControl.addRole( "new hierarchy role2" ); //$NON-NLS-1$

		accessControl
				.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_DISALLOW );

		// add a new access control

		propHandle = hierarchy
				.getPropertyHandle( OdaHierarchyHandle.ACCESS_CONTROLS_PROP );

		accessControl = designHandle.getElementFactory( ).newAccessControl( );
		propHandle.add( accessControl );

		// level
		hierarchy.add( OdaHierarchyHandle.LEVELS_PROP, factory
				.newOdaLevel( null ) );
		LevelHandle level = (LevelHandle) hierarchy.getContent(
				OdaHierarchyHandle.LEVELS_PROP, 0 );
		level.setName( namePrix + level.getName( ) );
		level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING );
		level.setInterval( DesignChoiceConstants.INTERVAL_MONTH );
		level.setIntervalRange( 5 );
		level.setIntervalBase( valuePrix + level.getIntervalBase( ) );
		level.setLevelType( DesignChoiceConstants.LEVEL_TYPE_MIRRORED );
		propHandle = level.getPropertyHandle( LevelHandle.STATIC_VALUES_PROP );
		propHandle.removeItem( 0 );
		Rule rule = new Rule( );
		rule.setProperty( Rule.DISPLAY_EXPRE_MEMBER, "new display expression" ); //$NON-NLS-1$
		rule.setProperty( Rule.RULE_EXPRE_MEMBER, "new rule expression" ); //$NON-NLS-1$
		propHandle.insertItem( rule, 0 );
		propHandle = level.getPropertyHandle( LevelHandle.ATTRIBUTES_PROP );
		propHandle.removeItem( propHandle.get( 1 ) );
		ConfigVariable config = new ConfigVariable( );
		config.setName( "var3" ); //$NON-NLS-1$
		config.setValue( "var3 value" ); //$NON-NLS-1$
		propHandle.insertItem( config, 0 );

		// access controls on hierarchy.

		ValueAccessControlHandle valueAccess = (ValueAccessControlHandle) level
				.valueAccessControlsIterator( ).next( );

		valueAccess.addUserName( "new level user1" ); //$NON-NLS-1$
		valueAccess.addUserName( "new level user2" ); //$NON-NLS-1$

		valueAccess.addRole( "new level role1" ); //$NON-NLS-1$
		valueAccess.addRole( "new level role2" ); //$NON-NLS-1$

		valueAccess.addValue( "new level value1" ); //$NON-NLS-1$
		valueAccess.addValue( "new level value2" ); //$NON-NLS-1$

		valueAccess
				.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );

		// add a new value access control

		propHandle = level
				.getPropertyHandle( LevelHandle.VALUE_ACCESS_CONTROLS_PROP );

		valueAccess = designHandle.getElementFactory( ).newValueAccessControl( );
		propHandle.add( valueAccess );

		// measure group
		cube.add( OdaCubeHandle.MEASURE_GROUPS_PROP, factory
				.newOdaMeasureGroup( null ) );
		MeasureGroupHandle measureGroup = (MeasureGroupHandle) cube.getContent(
				OdaCubeHandle.MEASURE_GROUPS_PROP, 0 );
		measureGroup.setName( namePrix + measureGroup.getName( ) );

		// measure
		measureGroup.add( MeasureGroupHandle.MEASURES_PROP, factory
				.newOdaMeasure( null ) );
		MeasureHandle measure = (MeasureHandle) measureGroup.getContent(
				MeasureGroupHandle.MEASURES_PROP, 0 );
		measure.setName( namePrix + measure.getName( ) );
		measure.setMeasureExpression( valuePrix
				+ measure.getMeasureExpression( ) );
		measure.setFunction( DesignChoiceConstants.MEASURE_FUNCTION_COUNT );
		measure.setCalculated( true );

		save( );
		
		assertTrue( compareFile( "OdaCubeParserTest_golden.xml" ) ); //$NON-NLS-1$
	}

}
