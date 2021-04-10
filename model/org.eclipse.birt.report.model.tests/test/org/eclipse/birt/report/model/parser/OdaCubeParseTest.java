
package org.eclipse.birt.report.model.parser;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.OdaLevelAttributeHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.OdaLevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.Rule;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.OdaCubeHandle;
import org.eclipse.birt.report.model.api.olap.OdaHierarchyHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests case for parsing and writing ODA OLAP elements.
 * 
 */

public class OdaCubeParseTest extends BaseTestCase {

	private final String FILE_NAME = "OdaCubeParserTest.xml"; //$NON-NLS-1$

	/**
	 * 
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(FILE_NAME);
		assertNotNull(designHandle);

		// cube

		OdaCubeHandle cube = (OdaCubeHandle) designHandle.findCube("testCube"); //$NON-NLS-1$

		// filter
		Iterator iter = cube.filtersIterator();
		FilterConditionHandle filterConditionHandle = (FilterConditionHandle) iter.next();
		assertEquals("filter expression", filterConditionHandle.getExpr()); //$NON-NLS-1$

		PropertyHandle propHandle = cube.getPropertyHandle(OdaCubeHandle.DIMENSIONS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, cube.getContentCount(OdaCubeHandle.DIMENSIONS_PROP));

		// dimension
		DimensionHandle dimension = (DimensionHandle) propHandle.getContent(0);
		assertEquals(dimension, cube.getContent(OdaCubeHandle.DIMENSIONS_PROP, 0));
		assertEquals("testDimension", dimension.getName()); //$NON-NLS-1$
		assertTrue(dimension.isTimeType());
		propHandle = dimension.getPropertyHandle(DimensionHandle.HIERARCHIES_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, dimension.getContentCount(DimensionHandle.HIERARCHIES_PROP));

		// hierarchy
		OdaHierarchyHandle hierarchy = (OdaHierarchyHandle) propHandle.getContent(0);
		assertEquals(hierarchy, dimension.getContent(DimensionHandle.HIERARCHIES_PROP, 0));
		// test getDefaultHierarchy in dimension
		// null is currently returned for all unresolved reference elements
		// assertEquals( hierarchy, dimension.getDefaultHierarchy( ) );
		assertEquals("testHierarchy", hierarchy.getName()); //$NON-NLS-1$

		propHandle = cube.getPropertyHandle(OdaCubeHandle.DIMENSIONS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, cube.getContentCount(OdaCubeHandle.DIMENSIONS_PROP));

		// filter
		iter = hierarchy.filtersIterator();
		filterConditionHandle = (FilterConditionHandle) iter.next();
		assertEquals("filter expression", filterConditionHandle.getExpr()); //$NON-NLS-1$

		propHandle = hierarchy.getPropertyHandle(OdaHierarchyHandle.LEVELS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, hierarchy.getContentCount(OdaHierarchyHandle.LEVELS_PROP));

		// level
		LevelHandle level = (LevelHandle) propHandle.getContent(0);
		assertEquals(level, hierarchy.getContent(OdaHierarchyHandle.LEVELS_PROP, 0));
		assertEquals("testLevel", level.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, level.getDataType());
		assertEquals("none", level.getInterval()); //$NON-NLS-1$
		assertEquals(3.0, level.getIntervalRange(), 0.00);
		assertEquals("Jan", level.getIntervalBase()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC, level.getLevelType());
		assertEquals("2", level.getDefaultValue()); //$NON-NLS-1$
		iter = level.staticValuesIterator();
		RuleHandle rule = (RuleHandle) iter.next();
		assertEquals("rule expression", rule.getRuleExpression()); //$NON-NLS-1$
		assertEquals("display expression", rule.getDisplayExpression()); //$NON-NLS-1$
		rule = (RuleHandle) iter.next();
		assertEquals("rule expression2", rule.getRuleExpression()); //$NON-NLS-1$
		assertEquals("display expression2", rule.getDisplayExpression()); //$NON-NLS-1$
		iter = level.attributesIterator();

		OdaLevelAttributeHandle attribute = (OdaLevelAttributeHandle) iter.next();
		assertEquals("var1", attribute.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, attribute.getDataType());
		assertEquals("native var1", attribute.getNativeName()); //$NON-NLS-1$
		assertEquals(10, attribute.getNativeDataType().intValue());

		attribute = (OdaLevelAttributeHandle) iter.next();
		assertEquals("var2", attribute.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, attribute.getDataType());
		assertEquals("native var2", attribute.getNativeName()); //$NON-NLS-1$
		assertEquals(2, attribute.getNativeDataType().intValue());

		// measure group
		propHandle = cube.getPropertyHandle(OdaCubeHandle.MEASURE_GROUPS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, cube.getContentCount(OdaCubeHandle.MEASURE_GROUPS_PROP));
		MeasureGroupHandle measureGroup = (MeasureGroupHandle) propHandle.getContent(0);
		assertEquals(measureGroup, cube.getContent(OdaCubeHandle.MEASURE_GROUPS_PROP, 0));
		assertEquals("testMeasureGroup", measureGroup.getName()); //$NON-NLS-1$

		propHandle = measureGroup.getPropertyHandle(MeasureGroupHandle.MEASURES_PROP);

		// measure
		MeasureHandle measure = (MeasureHandle) propHandle.getContent(0);
		assertEquals("testMeasure", measure.getName()); //$NON-NLS-1$
		assertEquals("column", measure.getMeasureExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.MEASURE_FUNCTION_MIN, measure.getFunction());
		assertFalse(measure.isCalculated());

		// test alignment
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_JUSTIFY, level.getAlignment());
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_JUSTIFY, measure.getAlignment());

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		openDesign(FILE_NAME);
		assertNotNull(designHandle);
		String namePrix = "new"; //$NON-NLS-1$
		String valuePrix = "updated "; //$NON-NLS-1$
		ElementFactory factory = designHandle.getElementFactory();

		// cube
		OdaCubeHandle cube = (OdaCubeHandle) designHandle.findCube("testCube"); //$NON-NLS-1$
		cube.setName(namePrix + cube.getName());

		// dimension
		cube.add(OdaCubeHandle.DIMENSIONS_PROP, factory.newOdaDimension(null));
		DimensionHandle dimension = (DimensionHandle) cube.getContent(OdaCubeHandle.DIMENSIONS_PROP, 0);
		dimension.setName(namePrix + dimension.getName());
		dimension.setTimeType(false);
		dimension.setDefaultHierarchy(factory.newOdaHierarchy("testDefaultHierarchy")); //$NON-NLS-1$

		// hierarchy
		dimension.add(DimensionHandle.HIERARCHIES_PROP, factory.newOdaHierarchy(null));
		OdaHierarchyHandle hierarchy = (OdaHierarchyHandle) dimension.getContent(DimensionHandle.HIERARCHIES_PROP, 0);
		hierarchy.setName(namePrix + hierarchy.getName());

		// level
		hierarchy.add(OdaHierarchyHandle.LEVELS_PROP, factory.newOdaLevel(dimension, null));
		LevelHandle level = (LevelHandle) hierarchy.getContent(OdaHierarchyHandle.LEVELS_PROP, 0);
		level.setName(namePrix + level.getName());
		level.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING);
		// level.setInterval( DesignChoiceConstants.INTERVAL_MONTH );
		level.setIntervalRange(5);
		level.setDefaultValue("10"); //$NON-NLS-1$
		level.setIntervalBase(valuePrix + level.getIntervalBase());
		level.setLevelType(DesignChoiceConstants.LEVEL_TYPE_MIRRORED);

		PropertyHandle propHandle = level.getPropertyHandle(LevelHandle.STATIC_VALUES_PROP);
		propHandle.removeItem(0);
		Rule rule = new Rule();
		rule.setProperty(Rule.DISPLAY_EXPRE_MEMBER, "new display expression"); //$NON-NLS-1$
		rule.setProperty(Rule.RULE_EXPRE_MEMBER, "new rule expression"); //$NON-NLS-1$
		propHandle.insertItem(rule, 0);
		propHandle = level.getPropertyHandle(LevelHandle.ATTRIBUTES_PROP);
		propHandle.removeItem(propHandle.get(1));

		OdaLevelAttribute attribute = new OdaLevelAttribute();
		attribute.setName("var3"); //$NON-NLS-1$
		attribute.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN);
		attribute.setNativeDataType(new Integer(100));
		attribute.setNativeName("new native name 3"); //$NON-NLS-1$

		propHandle.insertItem(attribute, 0);

		// measure group
		cube.add(OdaCubeHandle.MEASURE_GROUPS_PROP, factory.newOdaMeasureGroup(null));
		MeasureGroupHandle measureGroup = (MeasureGroupHandle) cube.getContent(OdaCubeHandle.MEASURE_GROUPS_PROP, 0);
		measureGroup.setName(namePrix + measureGroup.getName());

		// measure
		measureGroup.add(MeasureGroupHandle.MEASURES_PROP, factory.newOdaMeasure(null));
		MeasureHandle measure = (MeasureHandle) measureGroup.getContent(MeasureGroupHandle.MEASURES_PROP, 0);
		measure.setName(namePrix + measure.getName());
		measure.setMeasureExpression(valuePrix + measure.getMeasureExpression());
		measure.setFunction(DesignChoiceConstants.MEASURE_FUNCTION_COUNT);
		measure.setCalculated(true);

		// test alignment
		level.setAlignment(DesignChoiceConstants.TEXT_ALIGN_CENTER);
		measure.setAlignment(DesignChoiceConstants.TEXT_ALIGN_CENTER);

		save();
		assertTrue(compareFile("OdaCubeParserTest_golden.xml")); //$NON-NLS-1$
	}

}
