/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.library;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests the properties of library in reading and writing. This test case also
 * includes testing semantic check.
 */

public class LibraryCompoundElementTest extends BaseTestCase {

	private final static String INPUT1 = "DesignWithLibraryCompoundElement.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE = "DesignWithLibraryCompoundElement_golden.xml"; //$NON-NLS-1$
	private final static String FILE_NAME = "DesignWithElementProperty.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE_1 = "DesignWithElementProperty_golden.xml"; //$NON-NLS-1$

	/**
	 * Tests all properties and slots. Design extends an element from library.
	 * 
	 * 
	 * @throws Exception if any exception
	 */

	public void testParser() throws Exception {
		openDesign(INPUT1, ULocale.ENGLISH);

		// 1. test child element properties.

		TableHandle bodyTable = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		assertEquals("New Design Table", bodyTable.getCaption()); //$NON-NLS-1$
		assertEquals("blue", bodyTable.getStringProperty(StyleHandle.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("bolder", bodyTable.getStringProperty(StyleHandle.FONT_WEIGHT_PROP)); //$NON-NLS-1$

		// 2. test virtual element properties.

		TableRow row = (TableRow) bodyTable.getElement().getSlot(TableItem.HEADER_SLOT).getContent(0);

		GroupHandle group1 = (GroupHandle) bodyTable.getGroups().get(0);
		assertEquals("libTable1Group1", group1.getName()); //$NON-NLS-1$

		// Get property from it self.
		assertEquals("blue", row.getStringProperty(design, //$NON-NLS-1$
				Style.COLOR_PROP));

		// Get property from local style
		assertEquals("\"Arial\"", row.getStringProperty(design, StyleHandle.FONT_FAMILY_PROP)); //$NON-NLS-1$

		// Get property from virtual parent
		assertEquals("20pt", row.getStringProperty(design, RowHandle.HEIGHT_PROP)); //$NON-NLS-1$

		// 3. Table inside a cell.

		CellHandle bodyCell = (CellHandle) bodyTable.getDetail().get(1).getSlot(TableRow.CONTENT_SLOT).get(0);

		TableHandle bodyInnerTable = (TableHandle) bodyCell.getContent().get(0);
		RowHandle bodyInnerRow = (RowHandle) bodyInnerTable.getHeader().get(0);
		CellHandle bodyInnerCell = (CellHandle) bodyInnerRow.getCells().get(0);
		assertEquals(ColorPropertyType.LIME, bodyInnerCell.getElement().getLocalProperty(design, Style.COLOR_PROP));

		// test properties related to the table layout.

		assertEquals("\"Arial\"", bodyInnerCell //$NON-NLS-1$
				.getProperty(Style.FONT_FAMILY_PROP));

		// color should be aqua

		bodyCell = (CellHandle) bodyTable.getDetail().get(1).getSlot(TableRow.CONTENT_SLOT).get(1);
		assertEquals(ColorPropertyType.AQUA, bodyCell.getProperty(Style.COLOR_PROP));

		// test a lable that is a virtual element. And its virtual parent
		// extends from a library label.

		ListHandle list = (ListHandle) designHandle.findElement("list1"); //$NON-NLS-1$
		LabelHandle label = (LabelHandle) list.getDetail().get(0);

		assertEquals("base label in library", label.getText()); //$NON-NLS-1$
	}

	/**
	 * Tests writing the properties.
	 * 
	 * @throws Exception if any error found.
	 */

	public void testWriter() throws Exception {
		openDesign(INPUT1, ULocale.ENGLISH);

		// verify the overridden color in the design.

		TableHandle bodyTable = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		assertEquals("New Design Table", bodyTable.getCaption()); //$NON-NLS-1$

		RowHandle bodyRow = (RowHandle) bodyTable.getDetail().get(1);
		bodyRow.getPrivateStyle().getColor().setStringValue(ColorPropertyType.FUCHSIA);
		bodyRow.getHeight().setAbsolute(1.1);
		bodyRow.setBookmark("http://www.eclipse.org/birt"); //$NON-NLS-1$

		CellHandle bodyCell = (CellHandle) bodyRow.getCells().get(0);
		bodyCell.getPrivateStyle().getColor().setStringValue(ColorPropertyType.RED);

		TableHandle bodyInnerTable = (TableHandle) bodyCell.getContent().get(0);
		bodyInnerTable.setName("New Table"); //$NON-NLS-1$
		bodyInnerTable.setStyleName("new_style"); //$NON-NLS-1$
		RowHandle bodyInnerRow = (RowHandle) bodyInnerTable.getHeader().get(0);
		CellHandle bodyInnerCell = (CellHandle) bodyInnerRow.getCells().get(0);

		bodyInnerCell.getPrivateStyle().getColor().setStringValue(ColorPropertyType.NAVY);

		// ensure that style and name is written out.

		save();
		assertTrue(compareFile(GOLDEN_FILE));
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testParserForElementProperty() throws Exception {
		openDesign(FILE_NAME);
		Library lib = design.getLibraryWithNamespace("lib"); //$NON-NLS-1$

		TabularCubeHandle cube = (TabularCubeHandle) designHandle.findCube("designCube"); //$NON-NLS-1$
		assertEquals(lib.findDataSet("firstDataSet"), cube.getDataSet().getElement()); //$NON-NLS-1$
		// filter
		Iterator iter = cube.filtersIterator();
		FilterConditionHandle filterConditionHandle = (FilterConditionHandle) iter.next();
		assertEquals("filter expression", filterConditionHandle.getExpr()); //$NON-NLS-1$

		// access controls

		// access controls on cube.

		PropertyHandle propHandle = cube.getPropertyHandle(TabularCubeHandle.DIMENSIONS_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, cube.getContentCount(TabularCubeHandle.DIMENSIONS_PROP));

		// dimension
		DimensionHandle dimension = (DimensionHandle) propHandle.getContent(0);
		assertEquals(dimension, cube.getContent(TabularCubeHandle.DIMENSIONS_PROP, 0));
		assertEquals("testDimension", dimension.getName()); //$NON-NLS-1$
		assertTrue(dimension.isTimeType());
		propHandle = dimension.getPropertyHandle(DimensionHandle.HIERARCHIES_PROP);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(1, dimension.getContentCount(DimensionHandle.HIERARCHIES_PROP));

		// hierarchy
		TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) propHandle.getContent(0);
		assertEquals(hierarchy, dimension.getContent(DimensionHandle.HIERARCHIES_PROP, 0));
		// test getDefaultHierarchy in dimension
		assertEquals(hierarchy, dimension.getDefaultHierarchy());
		assertEquals("testHierarchy", hierarchy.getName()); //$NON-NLS-1$
		assertEquals(lib.findDataSet("secondDataSet"), hierarchy.getDataSet().getElement()); //$NON-NLS-1$

		// access controls on hierarchy.

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
		TabularLevelHandle level = (TabularLevelHandle) propHandle.getContent(0);
		assertEquals(level, hierarchy.getContent(TabularHierarchyHandle.LEVELS_PROP, 0));
		assertEquals("testLevel", level.getName()); //$NON-NLS-1$
		assertEquals("column1", level.getColumnName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, level.getDataType());
		assertEquals(DesignChoiceConstants.INTERVAL_TYPE_PREFIX, level.getInterval());
		assertEquals(3.0, level.getIntervalRange(), 0.00);
		assertEquals("Jan", level.getIntervalBase()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC, level.getLevelType());
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

		// access controls on level.

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

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testWriterForElementProperty() throws Exception {
		openDesign(FILE_NAME);
		LibraryHandle libHandle = designHandle.getLibrary("lib"); //$NON-NLS-1$
		DesignElementHandle childCube = designHandle.getElementFactory().newElementFrom(libHandle.findCube("testCube"), //$NON-NLS-1$
				"designChildCube"); //$NON-NLS-1$

		DesignElementHandle cubeHandle = designHandle.getCubes().get(0);
		try {
			cubeHandle.setProperty(ICubeModel.DIMENSIONS_PROP, null);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());

		}
		cubeHandle.drop();
		designHandle.getCubes().add(childCube);
		save();
		assertTrue(compareFile(GOLDEN_FILE_1));
	}
}
