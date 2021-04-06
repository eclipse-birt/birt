/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.Rule;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the cube element.
 */

public class TabularCubeParserTest extends BaseTestCase {

	private final String FILE_NAME = "CubeParserTest.xml"; //$NON-NLS-1$
	private final String FILE_NAME_EXTENDS = "CubeParserTest_3.xml"; //$NON-NLS-1$

	private final String PARSE_TEST_FILE = "CubeParserTest_5.xml"; //$NON-NLS-1$

	/**
	 * 
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(PARSE_TEST_FILE);
		assertNotNull(designHandle);

		// cube
		TabularCubeHandle cube = (TabularCubeHandle) designHandle.findCube("testCube"); //$NON-NLS-1$
		assertEquals(designHandle.findDataSet("firstDataSet"), cube.getDataSet()); //$NON-NLS-1$
		assertTrue(cube.autoPrimaryKey());
		ExpressionHandle expressionHandle = cube.getACLExpression();
		Expression value = (Expression) expressionHandle.getValue();
		assertEquals("ACL expression", value.getStringExpression()); //$NON-NLS-1$
		assertEquals(IExpressionType.JAVASCRIPT, value.getType());

		// filter
		Iterator iter = cube.filtersIterator();
		FilterConditionHandle filterConditionHandle = (FilterConditionHandle) iter.next();
		assertEquals("filter expression", filterConditionHandle.getExpr()); //$NON-NLS-1$
		// join condition
		iter = cube.joinConditionsIterator();
		DimensionConditionHandle cubeJoinConditionHandle = (DimensionConditionHandle) iter.next();
		assertEquals(design.findOLAPElement("testHierarchy"), cubeJoinConditionHandle.getHierarchy().getElement()); //$NON-NLS-1$
		MemberHandle conditionMemberHandle = cubeJoinConditionHandle.getJoinConditions();
		assertEquals(3, conditionMemberHandle.getListValue().size());
		DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionMemberHandle.getAt(0);
		assertEquals("cubeKey", joinCondition.getCubeKey()); //$NON-NLS-1$
		assertEquals("key", joinCondition.getHierarchyKey()); //$NON-NLS-1$
		assertEquals(designHandle.findLevel("testDimension/testLevel"), joinCondition.getLevel()); //$NON-NLS-1$
		joinCondition = (DimensionJoinConditionHandle) conditionMemberHandle.getAt(1);
		assertEquals("cubeKey2", joinCondition.getCubeKey()); //$NON-NLS-1$
		assertEquals("key2", joinCondition.getHierarchyKey()); //$NON-NLS-1$
		joinCondition = (DimensionJoinConditionHandle) conditionMemberHandle.getAt(2);
		assertEquals("cubeKey4", joinCondition.getCubeKey()); //$NON-NLS-1$
		assertEquals("key4", joinCondition.getHierarchyKey()); //$NON-NLS-1$
		cubeJoinConditionHandle = (DimensionConditionHandle) iter.next();
		assertNull(cubeJoinConditionHandle.getHierarchy());
		assertEquals("nonExistingHierarchy", cubeJoinConditionHandle.getHierarchyName()); //$NON-NLS-1$
		conditionMemberHandle = cubeJoinConditionHandle.getJoinConditions();
		assertEquals(1, conditionMemberHandle.getListValue().size());

		PropertyHandle propHandle = cube.getPropertyHandle(TabularCubeHandle.DIMENSIONS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, cube.getContentCount(TabularCubeHandle.DIMENSIONS_PROP));

		// dimension
		DimensionHandle dimension = (DimensionHandle) propHandle.getContent(0);
		assertEquals(dimension, cube.getContent(TabularCubeHandle.DIMENSIONS_PROP, 0));
		assertEquals("testDimension", dimension.getName()); //$NON-NLS-1$
		assertTrue(dimension.isTimeType());
		expressionHandle = dimension.getACLExpression();
		value = (Expression) expressionHandle.getValue();
		assertEquals("ACL expression", value.getStringExpression()); //$NON-NLS-1$
		assertEquals(IExpressionType.JAVASCRIPT, value.getType());
		propHandle = dimension.getPropertyHandle(DimensionHandle.HIERARCHIES_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, dimension.getContentCount(DimensionHandle.HIERARCHIES_PROP));

		// hierarchy
		TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) propHandle.getContent(0);
		assertEquals(hierarchy, dimension.getContent(DimensionHandle.HIERARCHIES_PROP, 0));
		// test getDefaultHierarchy in dimension
		assertEquals(hierarchy, dimension.getDefaultHierarchy());
		assertEquals("testHierarchy", hierarchy.getName()); //$NON-NLS-1$
		assertEquals(designHandle.findDataSet("secondDataSet"), hierarchy.getDataSet()); //$NON-NLS-1$

		TabularLevelHandle level = (TabularLevelHandle) hierarchy.getLevel("testLevel"); //$NON-NLS-1$
		assertNotNull(level);

		propHandle = cube.getPropertyHandle(TabularCubeHandle.DIMENSIONS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, cube.getContentCount(TabularCubeHandle.DIMENSIONS_PROP));

		// filter
		iter = hierarchy.filtersIterator();
		filterConditionHandle = (FilterConditionHandle) iter.next();
		assertEquals("filter expression", filterConditionHandle.getExpr()); //$NON-NLS-1$
		List primaryKeys = hierarchy.getPrimaryKeys();
		assertEquals(3, primaryKeys.size());
		assertEquals("key", primaryKeys.get(0)); //$NON-NLS-1$
		assertEquals("key2", primaryKeys.get(1)); //$NON-NLS-1$
		assertEquals("key4", primaryKeys.get(2)); //$NON-NLS-1$
		propHandle = hierarchy.getPropertyHandle(TabularHierarchyHandle.LEVELS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, hierarchy.getContentCount(TabularHierarchyHandle.LEVELS_PROP));

		// level
		level = (TabularLevelHandle) propHandle.getContent(0);
		assertEquals(level, hierarchy.getContent(TabularHierarchyHandle.LEVELS_PROP, 0));
		assertEquals("testLevel", level.getName()); //$NON-NLS-1$
		assertEquals("column1", level.getColumnName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, level.getDataType());
		assertEquals(DesignChoiceConstants.INTERVAL_TYPE_PREFIX, level.getInterval());
		assertEquals(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH, level.getDateTimeLevelType());
		assertEquals("mmm", level.getDateTimeFormat()); //$NON-NLS-1$
		assertEquals(3.0, level.getIntervalRange(), 0.00);
		assertEquals("Jan", level.getIntervalBase()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC, level.getLevelType());
		expressionHandle = level.getACLExpression();
		value = (Expression) expressionHandle.getValue();
		assertEquals("ACL expression", value.getStringExpression()); //$NON-NLS-1$
		assertEquals(IExpressionType.JAVASCRIPT, value.getType());
		expressionHandle = level.getMemberACLExpression();
		value = (Expression) expressionHandle.getValue();
		assertEquals("member ACL expression", value.getStringExpression()); //$NON-NLS-1$
		assertEquals(IExpressionType.JAVASCRIPT, value.getType());

		iter = level.staticValuesIterator();
		RuleHandle rule = (RuleHandle) iter.next();
		assertEquals("rule expression", rule.getRuleExpression()); //$NON-NLS-1$
		assertEquals("display expression", rule.getDisplayExpression()); //$NON-NLS-1$
		rule = (RuleHandle) iter.next();
		assertEquals("rule expression2", rule.getRuleExpression()); //$NON-NLS-1$
		assertEquals("display expression2", rule.getDisplayExpression()); //$NON-NLS-1$
		iter = level.attributesIterator();

		LevelAttributeHandle attribute = (LevelAttributeHandle) iter.next();
		assertEquals("var1", attribute.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, attribute.getDataType());
		attribute = (LevelAttributeHandle) iter.next();
		assertEquals("var2", attribute.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, attribute.getDataType());

		ActionHandle action = level.getActionHandle();
		assertEquals("http://localhost:8080/bluehero", action.getURI()); //$NON-NLS-1$

		FormatValueHandle format = level.getFormat();
		assertNotNull(format);
		assertEquals("testLevelFormatCategory", format.getCategory()); //$NON-NLS-1$
		assertEquals("testLevelFormatPattern", format.getPattern()); //$NON-NLS-1$
		assertEquals(TEST_LOCALE, format.getLocale());

		// measure group
		propHandle = cube.getPropertyHandle(TabularCubeHandle.MEASURE_GROUPS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, cube.getContentCount(TabularCubeHandle.MEASURE_GROUPS_PROP));
		MeasureGroupHandle measureGroup = (MeasureGroupHandle) propHandle.getContent(0);
		assertEquals(measureGroup, cube.getContent(TabularCubeHandle.MEASURE_GROUPS_PROP, 0));
		assertEquals("testMeasureGroup", measureGroup.getName()); //$NON-NLS-1$

		propHandle = measureGroup.getPropertyHandle(MeasureGroupHandle.MEASURES_PROP);

		// measure
		MeasureHandle measure = (MeasureHandle) propHandle.getContent(0);
		assertEquals("testMeasure", measure.getName()); //$NON-NLS-1$
		assertEquals("column", measure.getMeasureExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.MEASURE_FUNCTION_MIN, measure.getFunction());
		assertFalse(measure.isCalculated());
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, measure.getDataType());
		expressionHandle = measure.getACLExpression();
		value = (Expression) expressionHandle.getValue();
		assertEquals("ACL expression", value.getStringExpression()); //$NON-NLS-1$
		assertEquals(IExpressionType.JAVASCRIPT, value.getType());

		action = measure.getActionHandle();
		assertEquals("http://localhost:8080/bluehero", action.getURI()); //$NON-NLS-1$

		format = measure.getFormat();
		assertNotNull(format);
		assertEquals("testMeasureFormatCategory", format.getCategory()); //$NON-NLS-1$
		assertEquals("testMeasureFormatPattern", format.getPattern()); //$NON-NLS-1$
		assertNull(format.getLocale());

		// test alignment
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_JUSTIFY, level.getAlignment());
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_JUSTIFY, measure.getAlignment());

		// test isVisible
		assertFalse(measure.isVisible());

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
		TabularCubeHandle cube = (TabularCubeHandle) designHandle.findCube("testCube"); //$NON-NLS-1$
		cube.setDataSet(designHandle.findDataSet("secondDataSet")); //$NON-NLS-1$
		cube.setName(namePrix + cube.getName());
		cube.setAutoPrimaryKey(false);

		ExpressionHandle expressionHandle = cube.getACLExpression();
		expressionHandle.setValue(new Expression("new " + expressionHandle.getStringValue(), IExpressionType.CONSTANT)); //$NON-NLS-1$

		PropertyHandle propHandle = cube.getPropertyHandle(TabularCubeHandle.DIMENSION_CONDITIONS_PROP);
		propHandle.removeItem(1);
		DimensionCondition condition = new DimensionCondition();
		DimensionConditionHandle structHandle = cube.addDimensionCondition(condition);
		DimensionJoinConditionHandle joinConditionHandle = structHandle.addJoinCondition(new DimensionJoinCondition());
		joinConditionHandle.setCubeKey("addCubeKey"); //$NON-NLS-1$
		joinConditionHandle.setHierarchyKey("addHierarchyKey"); //$NON-NLS-1$
		joinConditionHandle.setLevel("testDimension/noLevel"); //$NON-NLS-1$
		structHandle = (DimensionConditionHandle) propHandle.get(0);
		MemberHandle memberHandle = structHandle.getMember(DimensionCondition.HIERARCHY_MEMBER);
		memberHandle.setValue(valuePrix + "hierarchy"); //$NON-NLS-1$
		structHandle.removeJoinCondition(1);

		// dimension
		cube.add(TabularCubeHandle.DIMENSIONS_PROP, factory.newTabularDimension(null));
		DimensionHandle dimension = (DimensionHandle) cube.getContent(TabularCubeHandle.DIMENSIONS_PROP, 0);
		dimension.setName(namePrix + dimension.getName());
		dimension.setTimeType(false);
		dimension.setDefaultHierarchy(factory.newTabularHierarchy("testDefaultHierarchy")); //$NON-NLS-1$
		expressionHandle = dimension.getACLExpression();
		expressionHandle.setValue(new Expression("new " + expressionHandle.getStringValue(), IExpressionType.CONSTANT)); //$NON-NLS-1$

		// hierarchy
		dimension.add(DimensionHandle.HIERARCHIES_PROP, factory.newTabularHierarchy(null));
		TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension
				.getContent(DimensionHandle.HIERARCHIES_PROP, 0);
		hierarchy.setName(namePrix + hierarchy.getName());
		hierarchy.setDataSet(designHandle.findDataSet("firstDataSet")); //$NON-NLS-1$
		propHandle = hierarchy.getPropertyHandle(TabularHierarchyHandle.PRIMARY_KEYS_PROP);
		propHandle.removeItem("key2"); //$NON-NLS-1$
		propHandle.addItem(valuePrix + "key"); //$NON-NLS-1$

		// level
		hierarchy.add(TabularHierarchyHandle.LEVELS_PROP, factory.newTabularLevel(dimension, null));
		TabularLevelHandle level = (TabularLevelHandle) hierarchy.getContent(TabularHierarchyHandle.LEVELS_PROP, 0);
		level.setName(namePrix + level.getName());
		level.setColumnName(valuePrix + level.getColumnName());
		level.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING);
		level.setDateTimeLevelType(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER);
		level.setDateTimeFormat("mm"); //$NON-NLS-1$
		// level.setInterval( DesignChoiceConstants.INTERVAL_MONTH );
		level.setIntervalRange(5);
		level.setIntervalBase(valuePrix + level.getIntervalBase());
		level.setLevelType(DesignChoiceConstants.LEVEL_TYPE_MIRRORED);
		expressionHandle = level.getACLExpression();
		expressionHandle.setValue(new Expression("new " + expressionHandle.getStringValue(), IExpressionType.CONSTANT)); //$NON-NLS-1$
		expressionHandle = level.getMemberACLExpression();
		expressionHandle.setValue(new Expression("new " + expressionHandle.getStringValue(), IExpressionType.CONSTANT)); //$NON-NLS-1$
		propHandle = level.getPropertyHandle(LevelHandle.STATIC_VALUES_PROP);
		propHandle.removeItem(0);
		Rule rule = new Rule();
		rule.setProperty(Rule.DISPLAY_EXPRE_MEMBER, "new display expression"); //$NON-NLS-1$
		rule.setProperty(Rule.RULE_EXPRE_MEMBER, "new rule expression"); //$NON-NLS-1$
		propHandle.insertItem(rule, 0);
		propHandle = level.getPropertyHandle(LevelHandle.ATTRIBUTES_PROP);
		propHandle.removeItem(propHandle.get(1));

		LevelAttribute config = new LevelAttribute();
		config.setName("var3"); //$NON-NLS-1$
		config.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN);
		propHandle.insertItem(config, 0);

		Action action = StructureFactory.createAction();
		level.setAction(action);

		FormatValue levelFormat = StructureFactory.newFormatValue();
		levelFormat.setCategory("testLevelFormatCategory"); //$NON-NLS-1$
		levelFormat.setPattern("testLevelFormatPattern"); //$NON-NLS-1$
		levelFormat.setLocale(TEST_LOCALE);
		level.setFormat(levelFormat);

		// measure group
		cube.add(TabularCubeHandle.MEASURE_GROUPS_PROP, factory.newTabularMeasureGroup(null));
		MeasureGroupHandle measureGroup = (MeasureGroupHandle) cube.getContent(TabularCubeHandle.MEASURE_GROUPS_PROP,
				0);
		measureGroup.setName(namePrix + measureGroup.getName());

		// measure
		measureGroup.add(MeasureGroupHandle.MEASURES_PROP, factory.newTabularMeasure(null));
		MeasureHandle measure = (MeasureHandle) measureGroup.getContent(MeasureGroupHandle.MEASURES_PROP, 0);
		measure.setName(namePrix + measure.getName());
		measure.setMeasureExpression(valuePrix + measure.getMeasureExpression());
		measure.setFunction(DesignChoiceConstants.MEASURE_FUNCTION_COUNT);
		measure.setCalculated(true);
		measure.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN);
		expressionHandle = measure.getACLExpression();
		expressionHandle.setValue(new Expression("new " + expressionHandle.getStringValue(), IExpressionType.CONSTANT)); //$NON-NLS-1$

		action = StructureFactory.createAction();
		measure.setAction(action);

		FormatValue measureFormat = StructureFactory.newFormatValue();
		measureFormat.setCategory("testMeasureFormatCategory"); //$NON-NLS-1$
		measureFormat.setPattern("testMeasureFormatPattern"); //$NON-NLS-1$
		measureFormat.setLocale(TEST_LOCALE);
		measure.setFormat(measureFormat);

		// test alignment
		level.setAlignment(DesignChoiceConstants.TEXT_ALIGN_CENTER);
		measure.setAlignment(DesignChoiceConstants.TEXT_ALIGN_CENTER);

		// isVisible
		measure.setVisible(true);

		save();
		assertTrue(compareFile("CubeParserTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testCommand() throws Exception {
		openDesign(FILE_NAME);
		assertNotNull(designHandle);

		// cube
		TabularCubeHandle cube = (TabularCubeHandle) designHandle.findCube("testCube"); //$NON-NLS-1$
		try {
			cube.setName(null);
			fail();
		} catch (NameException e) {

		}

		IDesignElement clonedCube = cube.copy();
		assertNotNull(clonedCube);
		designHandle.rename(clonedCube.getHandle(design));
		designHandle.getSlot(IReportDesignModel.CUBE_SLOT).add(clonedCube.getHandle(design));

		// save

		save();
		assertTrue(compareFile("CubeParserTest_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests cases for cube1 extends another cube2.
	 * 
	 * <ul>
	 * <li>if one new access control is added to the cube1. Access controls on cube2
	 * will be copies to cube1 first.
	 * <li>if one new value access control is added to the level in the cube1. Value
	 * access controls on the level cube2 will be copies to the level of cube1
	 * first.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testExtends() throws Exception {
		openDesign(FILE_NAME_EXTENDS);
		assertNotNull(designHandle);

		// cube

		libraryHandle = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		TabularCubeHandle cube = (TabularCubeHandle) libraryHandle.findCube("testCube"); //$NON-NLS-1$

		TabularCubeHandle newCube = (TabularCubeHandle) designHandle.getElementFactory().newElementFrom(cube,
				"Customer Cube"); //$NON-NLS-1$

		designHandle.getCubes().add(newCube);

		save();
		assertTrue(compareFile("TabularCubeParserTest_extends_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testMergeDimensionConditions() throws Exception {
		openDesign("CubeParserTest_4.xml"); //$NON-NLS-1$

		TabularCubeHandle cube = (TabularCubeHandle) designHandle.findCube("testCube"); //$NON-NLS-1$

		assertNotNull(cube.findDimensionCondition("testHierarchy")); //$NON-NLS-1$
		assertEquals(cube.findDimensionCondition("testHierarchy").getStructure(), //$NON-NLS-1$
				cube.findDimensionCondition((HierarchyHandle) design.findOLAPElement("testHierarchy").getHandle(design)) //$NON-NLS-1$
						.getStructure());

		save();
		assertTrue(compareFile("CubeParserTest_golden_3.xml")); //$NON-NLS-1$
	}

	/**
	 * When we delete an element, we will remove the structure whose member refers
	 * the deleted element. However, this feature only works for the first level
	 * structure. It does not work for the second structure.
	 * 
	 * @throws Exception
	 */
	public void testClearStructureForElementRemove() throws Exception {
		openDesign(FILE_NAME);
		LevelHandle levelHandle = designHandle.findLevel("testDimension/testLevel"); //$NON-NLS-1$

		levelHandle.dropAndClear();

		save();
		assertTrue(compareFile("CubeParserTest_golden_4.xml")); //$NON-NLS-1$
	}

	private static void checkNotificationStatus(MyListener listener) {
		assertEquals(NotificationEvent.PROPERTY_EVENT, listener.getEventType());
		assertEquals(TabularCubeHandle.ACCESS_CONTROLS_PROP, listener.getPropName());

		listener.reset();
	}

	private static class MyListener implements Listener {

		private String propName = null;
		private int eventType = -1;

		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			eventType = ev.getEventType();

			assert eventType == NotificationEvent.PROPERTY_EVENT;

			propName = ((PropertyEvent) ev).getPropertyName();
		}

		MyListener() {

		}

		String getPropName() {
			return propName;
		}

		int getEventType() {
			return eventType;
		}

		void reset() {
			propName = null;
			eventType = -1;
		}
	}
}
