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

package org.eclipse.birt.report.model.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IllegalContentInfo;
import org.eclipse.birt.report.model.api.extension.UndefinedPropertyInfo;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * New cases for report item extension should be added here.
 * <p>
 * Tests the cases for the peer extension enhancement: slot definition.
 */

public class PeerExtensionTest extends BaseTestCase {

	private static final String HEADER_PROP = "header"; //$NON-NLS-1$
	private static final String DETAIL_PROP = "detail"; //$NON-NLS-1$
	private static final String FOOTER_PROP = "footer"; //$NON-NLS-1$
	private static final String TESTING_BOX_NAME = "TestingBox"; //$NON-NLS-1$
	private static final String FILE_NAME = "PeerExtensionTest.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_1 = "PeerExtensionTest_1.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_2 = "PeerExtensionTest_2.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_3 = "PeerExtensionTest_3.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_5 = "PeerExtensionTest_5.xml";//$NON-NLS-1$
	private static final String FILE_NAME_6 = "PeerExtensionTest_6.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_7 = "PeerExtensionTest_7.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_10 = "PeerExtensionTest_10.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_11 = "PeerExtensionTest_11.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_9 = "PeerExtensionTest_9.xml"; //$NON-NLS-1$
	private static final String POINTS_PROP_NAME = "points"; //$NON-NLS-1$

	private static final String TESTING_TABLE_NAME = "TestingTable"; //$NON-NLS-1$

	/**
	 * The extension do not have its own model.
	 */
	private static final String FILE_NAME_4 = "PeerExtensionTest_4.xml"; //$NON-NLS-1$

	/**
	 * The extension validation should be called on the nested extended items.
	 */
	private static final String FILE_NAME_14 = "PeerExtensionTest_14.xml"; //$NON-NLS-1$

	private static final String FILE_NAME_15 = "PeerExtensionTest_15.xml"; //$NON-NLS-1$

	/**
	 * Test cases for the allowExpression in extension elements.
	 */
	private static final String FILE_NAME_16 = "PeerExtensionTest_16.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ThreadResources.setLocale(ULocale.ENGLISH);
	}

	/**
	 * Test initializeReportItem method. when element has virtual parent, also can
	 * be initialized.
	 *
	 * @throws Exception
	 */
	public void testVirtualExtension() throws Exception {
		openDesign(FILE_NAME_5);

		ExtendedItemHandle handle = (ExtendedItemHandle) designHandle.findElement("newHeaderMatrix"); //$NON-NLS-1$
		assertNotNull(handle);
	}

	/**
	 * Tests the parser for the extension and the TestPeer--implementation of IPeer.
	 */
	public void testExtensionMeta() {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		assertTrue(dd.getExtensions().size() >= 2);

		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(TESTING_BOX_NAME);
		assertNotNull(extDefn);
		assertEquals("TestingBox", extDefn.getDisplayName()); //$NON-NLS-1$
		assertNull(extDefn.getDisplayNameKey());
		assertEquals(TESTING_BOX_NAME, extDefn.getName());
		assertEquals(MetaDataConstants.REQUIRED_NAME, extDefn.getNameOption());
		assertEquals(true, extDefn.allowsUserProperties());
		assertEquals(TESTING_BOX_NAME, extDefn.getName());
		assertEquals(extDefn.getXmlName(),
				((ElementDefn) dd.getElement(ReportDesignConstants.EXTENDED_ITEM)).getXmlName());

		// test the list property type
		PropertyDefn propDefn = (PropertyDefn) extDefn.getProperty(POINTS_PROP_NAME);
		assertEquals(PropertyType.LIST_TYPE, propDefn.getTypeCode());
		assertEquals(PropertyType.FLOAT_TYPE, propDefn.getSubTypeCode());

		// get the slot property definitions
		assertTrue(extDefn.isContainer());
		assertEquals(0, extDefn.getSlotCount());
		// header slot
		PropertyDefn slotPropertyDefn = (PropertyDefn) extDefn.getProperty(HEADER_PROP);
		assertEquals(IPropertyType.ELEMENT_TYPE, slotPropertyDefn.getTypeCode());
		assertEquals("Element.TestingBox.slot.header", slotPropertyDefn.getDisplayNameID()); //$NON-NLS-1$
		assertEquals("defaultHeader", slotPropertyDefn.getDisplayName()); //$NON-NLS-1$
		assertFalse(slotPropertyDefn.isList());
		List<IElementDefn> allowedElements = slotPropertyDefn.getAllowedElements(false);
		assertEquals(3, allowedElements.size());
		assertTrue(allowedElements.contains(dd.getElement(ReportDesignConstants.LABEL_ITEM)));
		assertTrue(allowedElements.contains(dd.getElement(ReportDesignConstants.GRID_ITEM)));
		assertTrue(allowedElements.contains(dd.getElement("TestingMatrix"))); //$NON-NLS-1$
		// detail slot
		slotPropertyDefn = (PropertyDefn) extDefn.getProperty(DETAIL_PROP);
		assertEquals("Element.TestingBox.slot.detail", slotPropertyDefn.getDisplayNameID()); //$NON-NLS-1$
		assertEquals("defaultDetail", slotPropertyDefn.getDisplayName()); //$NON-NLS-1$
		assertTrue(slotPropertyDefn.isList());
		assertEquals(4, slotPropertyDefn.getAllowedElements(false).size());
		assertTrue(slotPropertyDefn.getAllowedElements().size() > 4);
		// footer slot
		slotPropertyDefn = (PropertyDefn) extDefn.getProperty(FOOTER_PROP);
		assertEquals("footer", slotPropertyDefn.getName()); //$NON-NLS-1$
		assertEquals("Element.TestingBox.slot.footer", slotPropertyDefn.getDisplayNameID()); //$NON-NLS-1$
		assertEquals("defaultFooter", slotPropertyDefn.getDisplayName()); //$NON-NLS-1$
		assertFalse(slotPropertyDefn.isList());

	}

	/**
	 *
	 * @throws Exception
	 */
	public void testParser() throws Exception {
		openDesign(FILE_NAME, ULocale.US);
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		assertNotNull(extendedItem);

		// test the list property

		List<?> points = (List<?>) extendedItem.getProperty(POINTS_PROP_NAME);
		assertEquals(3, points.size());
		assertEquals(13.1, ((Double) points.get(0)).doubleValue(), 0.001);
		assertEquals(14, ((Double) points.get(1)).doubleValue(), 0.001);
		assertEquals(15.678, ((Double) points.get(2)).doubleValue(), 0.001);

		// test header slot: value and the content element is added to namespace
		// and id-map
		Object slotPropertyVaiue = extendedItem.getProperty(HEADER_PROP);
		ExtendedItemHandle contentExtendedItem = (ExtendedItemHandle) slotPropertyVaiue;
		assertEquals("headerMatrix", contentExtendedItem.getName()); //$NON-NLS-1$
		assertEquals(contentExtendedItem, designHandle.findElement("headerMatrix")); //$NON-NLS-1$
		assertEquals(contentExtendedItem, designHandle.getElementByID(contentExtendedItem.getID()));
		// it is a single slot, can not contain any item
		PropertyHandle propHandle = extendedItem.getPropertyHandle(HEADER_PROP);
		assertTrue(extendedItem.getPropertyDefn(HEADER_PROP)
				.canContain(MetaDataDictionary.getInstance().getElement(ReportDesignConstants.LABEL_ITEM)));
		assertFalse(propHandle.canContain(ReportDesignConstants.LABEL_ITEM));

		// test detail slot
		propHandle = extendedItem.getPropertyHandle(DETAIL_PROP);
		TableHandle table = (TableHandle) propHandle.get(0);
		assertEquals("testTable", table.getName()); //$NON-NLS-1$
		// get the cell content slot of the table detail row
		// TODO getCell(int, int)
		SlotHandle slot = table.getDetail().get(0).getSlot(0).get(0).getSlot(0);
		contentExtendedItem = (ExtendedItemHandle) slot.get(0);
		assertEquals("detailBox", contentExtendedItem.getName()); //$NON-NLS-1$

		// test footer slot
		propHandle = extendedItem.getPropertyHandle(FOOTER_PROP);
		GridHandle grid = (GridHandle) propHandle.get(0);
		assertEquals("footerGrid", grid.getName()); //$NON-NLS-1$

		openDesign(FILE_NAME_4);
		extendedItem = (ExtendedItemHandle) designHandle.findElement("testTable"); //$NON-NLS-1$
		assertNotNull(extendedItem);

		assertNotNull(extendedItem.getReportItem());
		assertEquals(TESTING_TABLE_NAME, extendedItem.getExtensionName());
		assertNotNull(extendedItem.getDefn());

		ExtensionPropertyDefn propDefn = (ExtensionPropertyDefn) extendedItem.getPropertyDefn("customComments"); //$NON-NLS-1$
		assertFalse(propDefn.hasOwnModel());
	}

	/**
	 * Tests the writer for peer extension slot.
	 *
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		openDesign(FILE_NAME, ULocale.US);
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		assertNotNull(extendedItem);

		// add some items to detail slot and then save
		PropertyHandle propHandle = extendedItem.getPropertyHandle(DETAIL_PROP);
		LabelHandle label = designHandle.getElementFactory().newLabel("addLabel"); //$NON-NLS-1$
		propHandle.add(label);
		IDesignElement clonedExtendedItem = extendedItem.copy();
		designHandle.rename(clonedExtendedItem.getHandle(design));
		propHandle.paste(clonedExtendedItem);

		// add a testing table
		ExtendedItemHandle extendedTable = designHandle.getElementFactory().newExtendedItem("testExtendedTable", //$NON-NLS-1$
				"TestingTable"); //$NON-NLS-1$
		extendedTable.setProperty(TableItem.DATA_SET_PROP, "tableDataSet"); //$NON-NLS-1$
		extendedTable.setProperty(IStyleModel.COLOR_PROP, IColorConstants.RED);
		extendedTable.setProperty("usage", "testusagevalue"); //$NON-NLS-1$//$NON-NLS-2$
		designHandle.getBody().add(extendedTable);

		save();
		assertTrue(compareFile("PeerExtensionTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the property search with support of extension slot.
	 *
	 * @throws Exception
	 */
	public void testPropertySearch() throws Exception {
		openDesign(FILE_NAME_1);
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		assertNotNull(extendedItem);

		// the extended-item in the header slot of outer extension
		// the testing-box-header should have no effects.

		ExtendedItemHandle contentExtendedItem = (ExtendedItemHandle) designHandle.findElement("headerMatrix"); //$NON-NLS-1$
		assertEquals(IColorConstants.BLACK, contentExtendedItem.getStringProperty(IStyleModel.COLOR_PROP));

		// test the table in the extended detail slot, use the default color

		TableHandle table = (TableHandle) designHandle.findElement("testTable"); //$NON-NLS-1$
		assertEquals(extendedItem, table.getContainer());
		assertEquals(IColorConstants.BLACK, extendedItem.getStringProperty(IStyleModel.COLOR_PROP));

		// local properties in table
		assertEquals(DesignChoiceConstants.FONT_FAMILY_FANTASY, table.getStringProperty(IStyleModel.FONT_FAMILY_PROP));
		assertEquals(DesignChoiceConstants.FONT_SIZE_LARGER, table.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		// TODO:table properties get from the extended-item detail slot selector
		// assertEquals( DesignChoiceConstants.FONT_WEIGHT_BOLD, table
		// .getStringProperty( IStyleModel.FONT_WEIGHT_PROP ) );
		// assertEquals( DesignChoiceConstants.FONT_STYLE_ITALIC, table
		// .getStringProperty( IStyleModel.FONT_STYLE_PROP ) );

		// test the label in the contained extended-item header slot

		LabelHandle label = (LabelHandle) designHandle.findElement("testLabel"); //$NON-NLS-1$
		assertEquals(IColorConstants.BLACK, label.getStringProperty(IStyleModel.COLOR_PROP));
		assertEquals(DesignChoiceConstants.FONT_FAMILY_FANTASY, label.getStringProperty(IStyleModel.FONT_FAMILY_PROP));
		assertEquals(DesignChoiceConstants.FONT_SIZE_LARGER, label.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_NORMAL, label.getStringProperty(IStyleModel.FONT_WEIGHT_PROP));
		assertEquals(DesignChoiceConstants.FONT_STYLE_NORMAL, label.getStringProperty(IStyleModel.FONT_STYLE_PROP));
	}

	/**
	 * Tests the content commands.
	 *
	 * @throws Exception
	 */
	public void testCommand() throws Exception {
		openDesign(FILE_NAME, ULocale.US);
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		assertNotNull(extendedItem);

		// test the list property operations
		PropertyHandle points = extendedItem.getPropertyHandle(POINTS_PROP_NAME);
		try {
			// add an invalid float
			points.addItem("p16"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
		points.removeItem(1);
		assertEquals(2, points.getListValue().size());
		points.addItem("18.9"); //$NON-NLS-1$

		// test header slot
		PropertyHandle propHandle = extendedItem.getPropertyHandle(HEADER_PROP);
		ExtendedItemHandle contentExtendedItem = (ExtendedItemHandle) propHandle.get(0);
		// it is a single slot, can not contain any item
		LabelHandle label = designHandle.getElementFactory().newLabel("label1"); //$NON-NLS-1$
		try {
			propHandle.add(label);
			fail();
		} catch (SemanticException e) {
			// pass
		}
		contentExtendedItem.drop();
		assertEquals(0, propHandle.getContentCount());
		propHandle.add(label);
		assertEquals(1, propHandle.getContentCount());
		assertEquals(extendedItem, label.getContainer());

		// test detail slot
		propHandle = extendedItem.getPropertyHandle(DETAIL_PROP);
		TableHandle table = (TableHandle) propHandle.get(0);
		TableGroupHandle tableGroup = designHandle.getElementFactory().newTableGroup();
		table.getGroups().add(tableGroup);
		assertEquals(table, tableGroup.getContainer());
		ListHandle list = designHandle.getElementFactory().newList("list"); //$NON-NLS-1$
		propHandle.add(list);
		assertEquals(2, propHandle.getContentCount());
		// add element to detail directly
		save();
		assertTrue(compareFile("PeerExtensionTest_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 *
	 * @throws Exception
	 */
	public void testParserErrorRecover() throws Exception {
		openDesign(FILE_NAME_2);
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		assertNotNull(extendedItem);
		assertEquals("nonExistingExtension", extendedItem.getExtensionName()); //$NON-NLS-1$
		// rom-defined properties still read properly
		assertEquals("1.2mm", extendedItem.getStringProperty(ExtendedItem.X_PROP)); //$NON-NLS-1$
		assertEquals("11.2mm", extendedItem.getStringProperty(ExtendedItem.Y_PROP)); //$NON-NLS-1$
		assertEquals("firstDataSet", extendedItem.getStringProperty(ExtendedItem.DATA_SET_PROP)); //$NON-NLS-1$

		// the table in the extended item is not parsed to the tree
		assertNull(designHandle.findElement("testTable")); //$NON-NLS-1$

		// add a label to the body
		LabelHandle label = designHandle.getElementFactory().newLabel("testLabel"); //$NON-NLS-1$
		assertEquals("testLabel1", label.getName()); //$NON-NLS-1$
		designHandle.getBody().add(label);

		save();
		assertTrue(compareFile("PeerExtensionTest_golden_2.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests an extension has 'extensionName' property even if it does not extend
	 * from 'ExtendedItem'.
	 *
	 * @throws Exception
	 */
	public void testExtensionNameProp() throws Exception {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		assertTrue(dd.getExtensions().size() >= 3);

		ElementDefn extendedCell = (ElementDefn) dd.getExtension("TestingTable"); //$NON-NLS-1$
		assertNotNull(extendedCell);
		assertEquals(dd.getElement(ReportDesignConstants.REPORT_ITEM), extendedCell.getParent());

		PropertyDefn extensionName = (PropertyDefn) extendedCell.getProperty(ExtendedItem.EXTENSION_NAME_PROP);
		assertNotNull(extensionName);
	}

	/**
	 *
	 * @throws Exception
	 */
	public void testActionHandleInExtension() throws Exception {
		openDesign(FILE_NAME_3);
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		assertNotNull(extendedItem);
		PropertyDefn actionDefn = (PropertyDefn) extendedItem.getPropertyDefn(IImageItemModel.ACTION_PROP);
		assertNotNull(actionDefn);

		ImageHandle image = (ImageHandle) designHandle.findElement("testImage"); //$NON-NLS-1$
		ActionHandle imageAction = image.getActionHandle();
		assertNotNull(imageAction);
		String actionString = ModuleUtil.serializeAction(imageAction);

		ActionHandle extendedAction = ModuleUtil.deserializeAction(actionString, extendedItem);
		assertNotNull(extendedAction);
		assertNotNull(extendedItem.getProperty(IImageItemModel.ACTION_PROP));
		assertEquals(extendedItem, extendedAction.getElementHandle());

	}

	/**
	 * Tests the error handler of extension loader.
	 *
	 * @throws Exception
	 */
	public void testExtensionLoaderErrorHandler() throws Exception {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		assertTrue(dd.getExtensions().size() >= 2);
		assertNull(dd.getExtension("wrongTestExtension")); //$NON-NLS-1$
	}

	protected static final String TESTING_TABLE = "TestingTable"; //$NON-NLS-1$
	protected static final String TESTING_TABLE1 = "TestingTable1"; //$NON-NLS-1$

	protected static final String TABLE = "Table";//$NON-NLS-1$

	/**
	 * Tests extension allowed units.
	 *
	 * @throws Exception
	 */
	public void testExtensionAllowedUnits() throws Exception {

		// Test get allowed units in metadata.

		MetaDataDictionary dd = MetaDataDictionary.getInstance();

		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(TESTING_TABLE);
		IPropertyDefn defn = extDefn.getProperty("width"); //$NON-NLS-1$
		IChoiceSet set = defn.getAllowedUnits();
		assertNotNull(set.findChoice("in"));//$NON-NLS-1$
		assertNotNull(set.findChoice("cm"));//$NON-NLS-1$
		assertNull(set.findChoice("mm"));//$NON-NLS-1$
		assertNull(set.findChoice("pt"));//$NON-NLS-1$

		set = dd.getElement(TABLE).findProperty("width").getAllowedUnits(); //$NON-NLS-1$

		assertNotNull(set.findChoice("in"));//$NON-NLS-1$
		assertNotNull(set.findChoice("cm"));//$NON-NLS-1$
		assertNotNull(set.findChoice("mm"));//$NON-NLS-1$
		assertNotNull(set.findChoice("pt"));//$NON-NLS-1$

		// Test 'getPropertyDefn' method in DesignElementHandle class.

		openDesign(FILE_NAME_4);
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("testTable"); //$NON-NLS-1$
		defn = extendedItem.getPropertyDefn("width"); //$NON-NLS-1$
		set = defn.getAllowedUnits();

		assertNotNull(set.findChoice("in"));//$NON-NLS-1$
		assertNotNull(set.findChoice("cm"));//$NON-NLS-1$
		assertNull(set.findChoice("mm"));//$NON-NLS-1$
		assertNull(set.findChoice("pt"));//$NON-NLS-1$
	}

	/**
	 * Tests IReportItem :: getPredefinedStyles about the property search.
	 *
	 * @throws Exception
	 */
	public void testPredefinedStyles() throws Exception {
		openDesign(FILE_NAME_6);

		StyleHandle style = designHandle.findStyle("testing-box"); //$NON-NLS-1$

		// test item in box-header: it defines no local style and style values,
		// then get value from selector defined in ElementDefn(testing-matrix)
		DesignElementHandle extendedItem = designHandle.findElement("headerMatrix"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_400,
				extendedItem.getStringProperty(IStyleModel.FONT_WEIGHT_PROP));
		assertEquals(DesignChoiceConstants.FONT_SIZE_X_SMALL,
				extendedItem.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		// other properties are not set, while its value equals to that set in
		// the container
		assertFalse(extendedItem.getPropertyHandle(IStyleModel.FONT_FAMILY_PROP).isSet());
		assertEquals(style.getStringProperty(IStyleModel.FONT_FAMILY_PROP),
				extendedItem.getStringProperty(IStyleModel.FONT_FAMILY_PROP));
		assertFalse(extendedItem.getPropertyHandle(IStyleModel.FONT_STYLE_PROP).isSet());
		assertEquals(style.getStringProperty(IStyleModel.FONT_STYLE_PROP),
				extendedItem.getStringProperty(IStyleModel.FONT_STYLE_PROP));
		assertFalse(extendedItem.getPropertyHandle(IStyleModel.COLOR_PROP).isSet());
		assertEquals(style.getStringProperty(IStyleModel.COLOR_PROP),
				extendedItem.getStringProperty(IStyleModel.COLOR_PROP));
		assertFalse(extendedItem.getPropertyHandle(IStyleModel.FONT_VARIANT_PROP).isSet());
		assertEquals(style.getStringProperty(IStyleModel.FONT_VARIANT_PROP),
				extendedItem.getStringProperty(IStyleModel.FONT_VARIANT_PROP));

		// test item in box-detail, it defines custom predefined styles:
		// testing-box-detail and testPredefinedStyle
		extendedItem = designHandle.findElement("detailMatrix"); //$NON-NLS-1$
		// property from default selector:testing-matrix
		assertEquals(DesignChoiceConstants.FONT_SIZE_X_SMALL,
				extendedItem.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_400,
				extendedItem.getStringProperty(IStyleModel.FONT_WEIGHT_PROP));
		// property value get from testing-box-detail: color and font-style
		assertEquals(DesignChoiceConstants.FONT_STYLE_OBLIQUE,
				extendedItem.getStringProperty(IStyleModel.FONT_STYLE_PROP));
		assertEquals(IColorConstants.RED, extendedItem.getStringProperty(IStyleModel.COLOR_PROP));

		// font-variant is not set
		assertTrue(extendedItem.getPropertyHandle(IStyleModel.FONT_FAMILY_PROP).isSet());
		assertFalse(extendedItem.getPropertyHandle(IStyleModel.FONT_VARIANT_PROP).isSet());
		assertEquals(style.getStringProperty(IStyleModel.FONT_VARIANT_PROP),
				extendedItem.getStringProperty(IStyleModel.FONT_VARIANT_PROP));

		// test item in box-detail and defines its named style("My Style")
		extendedItem = designHandle.findElement("detailMatrixOne"); //$NON-NLS-1$
		// property form named style
		assertEquals(DesignChoiceConstants.FONT_SIZE_SMALL, extendedItem.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		// property value get from testing-box-detail: color and font-style
		assertEquals(DesignChoiceConstants.FONT_STYLE_OBLIQUE,
				extendedItem.getStringProperty(IStyleModel.FONT_STYLE_PROP));
		assertEquals(IColorConstants.RED, extendedItem.getStringProperty(IStyleModel.COLOR_PROP));

		// no value from testing-matrix default selector
		// font-variant is not set
		assertTrue(extendedItem.getPropertyHandle(IStyleModel.FONT_FAMILY_PROP).isSet());
		assertFalse(extendedItem.getPropertyHandle(IStyleModel.FONT_VARIANT_PROP).isSet());
		assertEquals(style.getStringProperty(IStyleModel.FONT_VARIANT_PROP),
				extendedItem.getStringProperty(IStyleModel.FONT_VARIANT_PROP));

	}

	/**
	 * When changes the style defined in IReportItem.getPredefinedStyles, the style
	 * event will be sent to the specified element.
	 *
	 * @throws Exception
	 */

	public void testPredefinedStylesBroadCast() throws Exception {
		openDesign(FILE_NAME_6);

		StyleHandle style = designHandle.findStyle("testPredefinedStyle"); //$NON-NLS-1$

		DesignElementHandle detailMaxtrix = designHandle.findElement("detailMatrix"); //$NON-NLS-1$

		MyListener styleEventListener = new MyListener();
		detailMaxtrix.addListener(styleEventListener);

		style.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_DOTTED);

		assertEquals(2, styleEventListener.getEventCount());

		style.setStringProperty(StyleHandle.COLOR_PROP, ColorPropertyType.RED);

		assertEquals(4, styleEventListener.getEventCount());
	}

	/**
	 * Test getLocalProperty in ExtendedItem. If a extension property is element
	 * reference type and is not resolved, we will try to resolve it everty time.
	 *
	 * @throws Exception
	 */
	public void testResolveForExtensionProperty() throws Exception {
		openDesign(FILE_NAME_7);

		// originally reference is not resolved for no cube exists
		DesignElementHandle extendedItem = designHandle.findElement("testTable"); //$NON-NLS-1$
		ElementRefValue value = (ElementRefValue) extendedItem.getElement().getProperty(design, "cube"); //$NON-NLS-1$
		assertEquals("testCube", value.getName()); //$NON-NLS-1$
		assertFalse(value.isResolved());

		// add the cube and test again
		CubeHandle cube = designHandle.getElementFactory().newTabularCube("testCube"); //$NON-NLS-1$
		designHandle.getCubes().add(cube);
		value = (ElementRefValue) extendedItem.getElement().getProperty(design, "cube"); //$NON-NLS-1$
		assertEquals("testCube", value.getName()); //$NON-NLS-1$
		assertEquals(cube.getElement(), value.getTargetElement());
		assertTrue(value.isResolved());
	}

	/**
	 * Test client reference in extended item.
	 *
	 * @throws Exception
	 */

	public void testBackRef() throws Exception {
		openDesign("PeerExtensionTest_8.xml");//$NON-NLS-1$
		DesignElementHandle extendedItem = designHandle.findElement("testTable");//$NON-NLS-1$

		DesignElementHandle cubeHandle = designHandle.getCubes().get(0);

		extendedItem.setProperty("cube", "Customer Cube");//$NON-NLS-1$//$NON-NLS-2$

		Iterator<?> iterator = cubeHandle.clientsIterator();
		assertTrue(iterator.hasNext());
		DesignElementHandle client = (DesignElementHandle) iterator.next();
		assertEquals("testTable", client.getName());//$NON-NLS-1$
		assertFalse(iterator.hasNext());
	}

	/**
	 * Tests IReportItem :: getFunctions.
	 *
	 * @throws Exception
	 */

	public void testGetMethods() throws Exception {
		openDesign(FILE_NAME);

		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		assertNotNull(extendedItem);

		List<?> retList = extendedItem.getMethods("onRender"); //$NON-NLS-1$
		assertFalse(retList.isEmpty());
		IMethodInfo method = (IMethodInfo) retList.get(0);
		assertEquals("getMethod1", method.getName()); //$NON-NLS-1$
		assertEquals("java.lang.String", method.getReturnType()); //$NON-NLS-1$

		retList = extendedItem.getMethods("onPrepare"); //$NON-NLS-1$
		assertNull(retList);

		retList = extendedItem.getMethods("onCreate"); //$NON-NLS-1$
		assertNotNull(retList);

		method = (IMethodInfo) retList.get(0);
		assertEquals("performOnCreate", method.getName()); //$NON-NLS-1$
		assertEquals("java.lang.String", method.getReturnType()); //$NON-NLS-1$

		Iterator<IArgumentInfoList> arguList = method.argumentListIterator();
		IArgumentInfoList argus = (IArgumentInfoList) arguList.next();
		IArgumentInfo argu = (IArgumentInfo) argus.argumentsIterator().next();

		IClassInfo clazz = argu.getClassType();
		assertEquals("java.lang.Boolean", clazz.getName()); //$NON-NLS-1$
	}

	/**
	 * Tests newElementFrom and writer for extended item with element properties.
	 *
	 * @throws Exception
	 */
	public void testElementProperty() throws Exception {
		openDesign(FILE_NAME_1);
		designHandle.includeLibrary("LibraryWithElementProperty.xml", "lib"); //$NON-NLS-1$ //$NON-NLS-2$

		LibraryHandle lib = designHandle.getLibrary("lib"); //$NON-NLS-1$
		DesignElementHandle libBox = lib.findElement("LibraryBox"); //$NON-NLS-1$

		DesignElementHandle designBox = designHandle.getElementFactory().newElementFrom(libBox, "designBox"); //$NON-NLS-1$
		designHandle.getBody().add(designBox);

		save();
		assertTrue(compareFile("PeerExtensionTest_golden_3.xml")); //$NON-NLS-1$

	}

	public void testDataBindingRef() throws Exception {
		openDesign(FILE_NAME_10);

		ExtendedItemHandle matrix1 = (ExtendedItemHandle) designHandle.findElement("myMatrix1"); //$NON-NLS-1$
		assertNotNull(matrix1);

		TableHandle table1 = (TableHandle) designHandle.findElement("myTable1"); //$NON-NLS-1$
		assertNotNull(table1);

		matrix1.setDataBindingReference(table1);
		assertEquals("Data Set", matrix1 //$NON-NLS-1$
				.getProperty(ReportItemHandle.DATA_SET_PROP));

		Iterator<?> columns = matrix1.columnBindingsIterator();
		ComputedColumnHandle column = (ComputedColumnHandle) columns.next();
		verifyColumnValues(column);

		DataItemHandle data1 = (DataItemHandle) designHandle.findElement("myData1"); //$NON-NLS-1$
		assertNotNull(data1);

		data1.setDataBindingReference(matrix1);
		assertEquals("Data Set", matrix1 //$NON-NLS-1$
				.getProperty(ReportItemHandle.DATA_SET_PROP));

		columns = data1.columnBindingsIterator();
		column = (ComputedColumnHandle) columns.next();
		verifyColumnValues(column);
	}

	private void verifyColumnValues(ComputedColumnHandle column) {
		assertEquals("CUSTOMERNUMBER", column.getName()); //$NON-NLS-1$
		assertEquals("dataSetRow[\"CUSTOMERNUMBER\"]", column.getExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, column.getDataType());
	}

	/**
	 * @throws Exception
	 */

	public void testMultiView() throws Exception {
		openDesign("PeerExtensionMultiViewTest.xml"); //$NON-NLS-1$

		// cases for parser

		ExtendedItemHandle table1 = (ExtendedItemHandle) designHandle.findElement("MyTable1"); //$NON-NLS-1$
		MultiViewsHandle view1 = (MultiViewsHandle) table1.getProperty("multiViews"); //$NON-NLS-1$
		assertNotNull(view1);

		List<?> views = view1.getListProperty(MultiViewsHandle.VIEWS_PROP);
		assertEquals(2, views.size());

		ExtendedItemHandle box1 = (ExtendedItemHandle) views.get(0);
		assertEquals("firstDataSet", box1.getDataSet().getName()); //$NON-NLS-1$

		// the data related properties are read only.

		PropertyHandle prop = box1.getPropertyHandle(ReportItemHandle.DATA_SET_PROP);
		assertTrue(prop.isReadOnly());

		prop = box1.getPropertyHandle(ExtendedItemHandle.FILTER_PROP);
		assertTrue(prop.isReadOnly());

		// cases for writer

		ExtendedItemHandle table2 = designHandle.getElementFactory().newExtendedItem("table2", "TestingTable"); //$NON-NLS-1$ //$NON-NLS-2$
		designHandle.getBody().add(table2);
		table2.setDataSet(designHandle.findDataSet("firstDataSet")); //$NON-NLS-1$

		MultiViewsHandle view2 = designHandle.getElementFactory().newMultiView();
		table2.getPropertyHandle(TableHandle.MULTI_VIEWS_PROP).add(view2);

		ExtendedItemHandle box3 = designHandle.getElementFactory().newExtendedItem("box3", "TestingBox"); //$NON-NLS-1$//$NON-NLS-2$

		view2.add(MultiViewsHandle.VIEWS_PROP, box3);

		assertEquals("firstDataSet", box3.getDataSet().getName()); //$NON-NLS-1$

		save();
		assertTrue(compareFile("PeerExtensionMultiViewTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests the compatibility framework for extended items.
	 *
	 * @throws Exception
	 */
	public void testParserCompatibility() throws Exception {
		openDesign(FILE_NAME_11);

		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		assertNotNull(extendedHandle);
		assertEquals("1.1", extendedHandle.getExtensionVersion()); //$NON-NLS-1$
		extendedHandle.setExtensionVersion("1.2"); //$NON-NLS-1$
		assertEquals("1.2", extendedHandle.getExtensionVersion()); //$NON-NLS-1$

		// test property map: contains
		Map<String, UndefinedPropertyInfo> propMap = extendedHandle.getUndefinedProperties();

		// invalid simple property
		String propName = "shape"; //$NON-NLS-1$
		UndefinedPropertyInfo propInfor = (UndefinedPropertyInfo) propMap.get(propName);
		assertEquals("circle", propInfor.getValue()); //$NON-NLS-1$
		assertEquals("1.1", propInfor.getExtensionVersion()); //$NON-NLS-1$
		// parser compatibility set is to 'cube'
		assertEquals("cube", extendedHandle.getStringProperty("shape")); //$NON-NLS-1$//$NON-NLS-2$

		// invalid simple list property
		propName = "points"; //$NON-NLS-1$
		propInfor = (UndefinedPropertyInfo) propMap.get(propName);
		List<?> valueList = (List<?>) propInfor.getValue();
		assertEquals(3, valueList.size());
		assertEquals("13.1", valueList.get(0)); //$NON-NLS-1$
		assertEquals("ttt", valueList.get(1)); //$NON-NLS-1$
		assertEquals("15.678", valueList.get(2)); //$NON-NLS-1$

		// undefined property
		propName = "noProp"; //$NON-NLS-1$
		propInfor = (UndefinedPropertyInfo) propMap.get(propName);
		assertEquals("123", propInfor.getValue()); //$NON-NLS-1$

		// test illegal children
		Map<String, List<IllegalContentInfo>> illegalChildrenMap = extendedHandle.getIllegalContents();

		// detail slot has three
		propName = "detail"; //$NON-NLS-1$
		List<?> illegalChildren = (List<?>) illegalChildrenMap.get(propName);
		assertEquals(3, illegalChildren.size());
		IllegalContentInfo info = (IllegalContentInfo) illegalChildren.get(0);
		// the content has not been inserted to the tree
		DesignElementHandle content = info.getContent();
		assertEquals("testData", content.getName()); //$NON-NLS-1$
		assertNull(content.getContainer());
		assertNull(designHandle.findElement(content.getName()));
		assertEquals(1, info.getIndex());
		info = (IllegalContentInfo) illegalChildren.get(1);
		assertEquals("testData_1", info.getContent().getName()); //$NON-NLS-1$
		assertEquals(3, info.getIndex());
		info = (IllegalContentInfo) illegalChildren.get(2);
		assertEquals("extend_item", info.getContent().getName()); //$NON-NLS-1$
		assertTrue(info.getContent() instanceof ExtendedItemHandle);
		assertEquals(5, info.getIndex());

		// header slot has one illegal child
		propName = "header"; //$NON-NLS-1$
		illegalChildren = (List<?>) illegalChildrenMap.get(propName);
		assertEquals(1, illegalChildren.size());
		info = (IllegalContentInfo) illegalChildren.get(0);
		assertEquals("testData_2", info.getContent().getName()); //$NON-NLS-1$
		assertEquals(0, info.getIndex());

		// test design with inheritance
		extendedHandle = (ExtendedItemHandle) designHandle.findElement("testBox_1"); //$NON-NLS-1$
		propMap = extendedHandle.getUndefinedProperties();
		assertEquals(1, propMap.size());

		// parent corrects the illegal contents and so child inherits the
		// layout-structure
		illegalChildrenMap = extendedHandle.getIllegalContents();
		assertTrue(illegalChildrenMap.isEmpty());
		assertTrue(extendedHandle.getContent("header", 0) instanceof LabelHandle); //$NON-NLS-1$
	}

	/**
	 * Tests extension style definition.
	 *
	 * @throws Exception
	 */

	public void testExtensionStyleDefn() throws Exception {
		openDesign("PeerExtensionTest_12.xml");//$NON-NLS-1$
		ExtendedItemHandle matrix = (ExtendedItemHandle) designHandle.getElementByID(10l);
		ExtendedItemHandle box = (ExtendedItemHandle) designHandle.getElementByID(20l);

		StyleHandle style = box.getPrivateStyle();
		assertNull(style);
		style = matrix.getPrivateStyle();
		assertNotNull(style);
		assertNotNull(style.getDefn().getProperty(StyleHandle.FONT_FAMILY_PROP));
	}

	/**
	 * Tests useOwnSearch cases:
	 *
	 * <ul>
	 * <li>No local value, uses container values. tests getFactoryProperty(),
	 * getProperty() and ReportItem.getProperty().
	 * <li>Has local value. tests getFactoryProperty(), getProperty() and
	 * ReportItem.getProperty().
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testUseOwnSearch() throws Exception {
		openDesign("PeerExtensionTest_13.xml");//$NON-NLS-1$
		ExtendedItemHandle table1 = (ExtendedItemHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		ExtendedItemHandle table2 = (ExtendedItemHandle) designHandle.findElement("table2"); //$NON-NLS-1$

		assertNull(table1.getFactoryPropertyHandle(StyleHandle.COLOR_PROP));
		assertEquals("red", table2.getFactoryPropertyHandle( //$NON-NLS-1$
				StyleHandle.COLOR_PROP).getStringValue());

		assertEquals("blue", table1.getReportItem().getProperty( //$NON-NLS-1$
				StyleHandle.COLOR_PROP));
		assertEquals("blue", table1.getProperty(StyleHandle.COLOR_PROP)); //$NON-NLS-1$

		assertEquals("red", table2.getReportItem().getProperty( //$NON-NLS-1$
				StyleHandle.COLOR_PROP));

		assertNull(table1.getFactoryPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP));

		List<?> rules = (List<?>) table1.getReportItem().getProperty(StyleHandle.HIGHLIGHT_RULES_PROP);
		assertEquals(1, rules.size());

		rules = table1.getListProperty(StyleHandle.HIGHLIGHT_RULES_PROP);
		assertEquals(1, rules.size());

		rules = (List<?>) table2.getFactoryPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).getValue();
		assertEquals(1, rules.size());
		rules = (List<?>) table2.getReportItem().getProperty(StyleHandle.HIGHLIGHT_RULES_PROP);
		assertEquals(1, rules.size());
	}

	/**
	 * Tests the property of the extension item which locates in the multiviews.
	 *
	 * @throws Exception
	 */

	public void testExtensionMultiViewProperty() throws Exception {
		openDesign("ExtensionMultiViewPropertyTest.xml"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("MyTable1"); //$NON-NLS-1$
		assertEquals("red", table.getStringProperty(IStyleModel.COLOR_PROP)); //$NON-NLS-1$

		ExtendedItemHandle box = (ExtendedItemHandle) table.getCurrentView();
		assertEquals("black", box.getStringProperty(IStyleModel.COLOR_PROP)); //$NON-NLS-1$
	}

	/**
	 * The nexted testing box should also be validated.
	 *
	 * @throws Exception
	 */

	public void testValidationOnNestedExtendedItem() throws Exception {
		openDesign(FILE_NAME_14);

		designHandle.checkReport();

		List<?> errors = design.getAllExceptions();
		List<ExtendedElementException> extendedErrors = new ArrayList<>();

		for (int i = 0; i < errors.size(); i++) {
			SemanticException error = (SemanticException) errors.get(i);
			if (!(error instanceof ExtendedElementException)) {
				continue;
			}

			extendedErrors.add((ExtendedElementException) error);
		}

		assertEquals(3, extendedErrors.size());
		assertEquals("testBox", extendedErrors.get(0).getElement().getName()); //$NON-NLS-1$
		assertEquals("detailBox", extendedErrors.get(1).getElement().getName()); //$NON-NLS-1$
		assertEquals("detailBox1", extendedErrors.get(2).getElement().getName());//$NON-NLS-1$
	}

	/**
	 * Tests the cases for user-properties in ExtendedItem.
	 *
	 * @throws Exception
	 */
	public void testUserProperty() throws Exception {
		openDesign(FILE_NAME_15);
		ExtendedItemHandle itemHandle = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$

		UserPropertyDefn userDefn = (UserPropertyDefn) itemHandle.getUserProperties().get(0);
		String propName = "myProp1"; //$NON-NLS-1$
		assertEquals(propName, userDefn.getName());
		assertEquals("property1 value", itemHandle.getStringProperty(propName)); //$NON-NLS-1$

		// change the property value
		itemHandle.setProperty(propName, "new value"); //$NON-NLS-1$

		// add another user-property and set value
		userDefn = new UserPropertyDefn();
		propName = "prop2"; //$NON-NLS-1$
		userDefn.setName(propName);
		userDefn.setType(MetaDataDictionary.getInstance().getPropertyType(IPropertyType.INTEGER_TYPE));
		itemHandle.addUserPropertyDefn(userDefn);
		itemHandle.setProperty(propName, "3"); //$NON-NLS-1$

		// save out and check golden file
		save();
		assertTrue(compareFile("PeerExtensionTest_golden_4.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests multiple level inheritance for the overridden property. Used
	 * "TestingTable" and "TestingTable1" as examples.
	 *
	 * @throws Exception
	 */

	public void testMultipleInheritance() throws Exception {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();

		IElementDefn tmpDefn = dd.getExtension(TESTING_TABLE1);
		IPropertyDefn tmpPropDefn = tmpDefn.getProperty(ReportItemHandle.WIDTH_PROP);

		IChoiceSet tmpSet = tmpPropDefn.getAllowedUnits();
		IChoice[] tmpChoices = tmpSet.getChoices();
		assertEquals(2, tmpChoices.length);

		tmpPropDefn = tmpDefn.getProperty(StyleHandle.COLOR_PROP);
		assertTrue(((ElementPropertyDefn) tmpPropDefn).enableContextSearch());
	}

	/**
	 * Tests the case that we set up the structure context for the extended
	 * structure type property when clone the extended element.
	 *
	 * @throws Exception
	 */
	public void testCloneWithExtendedStructureProp() throws Exception {
		openDesign(FILE_NAME_9);

		ExtendedItemHandle itemHandle = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		DesignElementHandle copiedHandle = itemHandle.copy().getHandle(design);
		designHandle.rename(copiedHandle);
		designHandle.getBody().paste(copiedHandle);

		Structure action = (Structure) copiedHandle.getElement().getProperty(design, "action"); //$NON-NLS-1$
		assertNotNull(action.getContext());
	}

	/**
	 * Tests the case that we set up the structure context for the extended
	 * structure type property when clone the extended element.
	 *
	 * @throws Exception
	 */

	public void testAllowExpressionProperties() throws Exception {
		openDesign(FILE_NAME_16);
		ExtendedItemHandle itemHandle = (ExtendedItemHandle) designHandle.findElement("action1"); //$NON-NLS-1$
		ExpressionHandle exprHandle = itemHandle.getExpressionProperty("test1"); //$NON-NLS-1$
		assertEquals("1+1in", exprHandle.getStringExpression()); //$NON-NLS-1$
		assertEquals(ExpressionType.JAVASCRIPT, exprHandle.getType());

		exprHandle = itemHandle.getExpressionProperty("test2"); //$NON-NLS-1$
		assertEquals("50", exprHandle.getStringExpression()); //$NON-NLS-1$
		assertEquals(ExpressionType.CONSTANT, exprHandle.getType());

		exprHandle = itemHandle.getExpressionProperty("test1"); //$NON-NLS-1$
		exprHandle.setExpression(new DimensionValue(11, DesignChoiceConstants.UNITS_EM));
		exprHandle.setType(ExpressionType.CONSTANT);

		itemHandle.setProperty("test2", new Expression("30+20", //$NON-NLS-1$//$NON-NLS-2$
				ExpressionType.JAVASCRIPT));

		save();
		assertTrue(compareFile("PeerExtensionTest_golden_16.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests element reference type list property.
	 *
	 * @throws Exception
	 */

	public void testElementRefListProperty() throws Exception {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();

		IElementDefn elementDefn = dd.getExtension(TESTING_TABLE1);
		IPropertyDefn tmpDefn = elementDefn.getProperty("elementRefList"); //$NON-NLS-1$
		assertTrue(tmpDefn instanceof PropertyDefn);
		PropertyDefn propDefn = (PropertyDefn) tmpDefn;
		assertEquals(IPropertyType.LIST_TYPE, propDefn.getTypeCode());
		assertEquals(IPropertyType.ELEMENT_REF_TYPE, propDefn.getSubTypeCode());
		assertEquals(TABLE, propDefn.getTargetElementType().getName());

		ExtendedItemHandle testTable1 = createDesign().getElementFactory().newExtendedItem("Test", TESTING_TABLE1);
		PropertyHandle propHandle = testTable1.getPropertyHandle("elementRefList"); //$NON-NLS-1$
		TableHandle[] tables = new TableHandle[3];
		for (int i = 0; i < tables.length; i++) {
			tables[i] = designHandle.getElementFactory().newTableItem("table1");
			designHandle.getBody().add(tables[i]);
			propHandle.addItem(tables[i]);
		}
		propHandle.addItem("NonexistElement"); //$NON-NLS-1$

		assertEquals(4, propHandle.getItems().size());
		for (int i = 0; i < 3; i++) {
			assertEquals(tables[i], propHandle.getItems().get(i));
		}
		assertNull(propHandle.getItems().get(3));
	}

	private static class MyListener implements Listener {

		private int eventCount = 0;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.api.core.Listener#elementChanged(org
		 * .eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */
		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev instanceof StyleEvent) {
				eventCount++;
			}
		}

		/**
		 * @return the eventCount
		 */

		int getEventCount() {
			return eventCount;
		}

	}
}
