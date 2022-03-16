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

package org.eclipse.birt.report.model.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Unit test case for DesignElementHandle.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * <tr>
 * <td>testFindContentSlot()</td>
 * <td>Label1's container is MasterPage('My Page'), Evaluate the return value
 * masterPageHandle.findContentSlot( labelHandle )</td>
 * <td>The return value should be 0</td>
 * </tr>
 * <td></td>
 * <td>Label1's container is MasterPage('My Page'), Evaluate the return value
 * designHandle.findContentSlot( labelHandle )</td>
 * <td>The return value should be -1</td>
 * </tr>
 * <td></td>
 * <td>Move one text item within MasterPage to ReportDesign</td>
 * <td>After move, text's container should be design</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Move one text item within Table/Row/Cell to ReportDesign</td>
 * <td>After move, text's container should be design</td>
 * </tr>
 * <tr>
 * <td>testGetContanerSlot()</td>
 * <td>Get the slot handle of an element in a master page</td>
 * <td>The report element of the slot handle is the master page</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Get the slot handle of an element in a cell</td>
 * <td>The report element of the slot handle is the cell</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Get the slot handle of the design</td>
 * <td>the return slot handle is null</td>
 * </tr>
 * <tr>
 * <td>testStyle()</td>
 * <td>Gets private and shared style from a label with a shared style.</td>
 * <td>The label has both private and shared styles.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets private and shared style from a label without a shared style and no
 * private style defined on it.</td>
 * <td>The label has the private style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets private and shared style from a label without a shared style but
 * with a private style.</td>
 * <td>The label only has the private style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Sets and clears the style on the label.</td>
 * <td>The style is set correctly.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Sets the style on the label with another element.</td>
 * <td>Throws <code>StyleException</code> with an error code
 * <code>NOT_FOUND</code>.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets choices of the font family property of a style.</td>
 * <td>It has choices and the number of choices is 5.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets choices of the master-page property of a style.</td>
 * <td>It has no choices.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets client number of the style1.</td>
 * <td>It has 2 clients.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets the number of derived elements of the style1.</td>
 * <td>It has 0 derived elements.</td>
 * </tr>
 * <tr>
 * <td>testChoice</td>
 * <td>Gets choices of the font family property of a label.</td>
 * <td>It has choices and the number of choices is 5.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets choices of the text property of a label.</td>
 * <td>It has no choices.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets choices of the Data#DISTINCT_PROP property of a label.</td>
 * <td>It has no choices.</td>
 * </tr>
 * <tr>
 * <td>testExtends</td>
 * <td>Gets the extend element from labels that are in body, master-page and
 * components slots.</td>
 * <td>Returned elements match with definition in the design file.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Sets and clears the extends of an element.</td>
 * <td>The extends is set and cleared correctly.</td>
 * </tr>
 * <tr>
 * <td>testOtherMethods</td>
 * <td>Moves a label from the body slot to components slot.</td>
 * <td>The container is ReportDesign and the slot is components slot.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Tests the number of properties of a label.</td>
 * <td>The number is 52.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Drops a label from the design file.</td>
 * <td>The label cannot be find and it has no container.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Gets the display label of a label.</td>
 * <td>Display labels of different level match with the design file.</td>
 * </tr>
 * <tr>
 * <td>testPropertyMethods</td>
 * <td>Sets and gets the property value in different set/get property methods.
 * </td>
 * <td>The value can be set and gotten correctly.</td>
 * </tr>
 * <tr>
 * <td>testPropertyMethods</td>
 * <td>Sets and gets the invalid property value.</td>
 * <td>Throws <code>PropertyValueException</code>.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testCanContain()}</td>
 * <td>Whether the label can be contained in the page slot.</td>
 * <td>The return value is false.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Whether the page can be contained in the page slot before and after
 * inserting in the slot.</td>
 * <td>The return value is true before inserting. The value is false after
 * inserting.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Whether the label and grid can be contained in the freeform slot before
 * and after inserting in the slot.</td>
 * <td>The return value is true before/after inserting.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testCanContainTableHeader()}</td>
 * <td>DataSets slot in the design can contain only data-sets without any
 * data-source.</td>
 * <td>The return values are expected.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>The slot in a free-form can contain list, table, etc. But not any cell,
 * data-set, etc..</td>
 * <td>The return values are expected.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Any slot cannot contain <code>null</code> elements.</td>
 * <td>The return values are expected.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetPath()}</td>
 * <td>The path of a label in the body slot.</td>
 * <td>The return value is expected.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>The path of a style in the styles slot.</td>
 * <td>The return value is expected.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>The path of a table row.</td>
 * <td>The return value is expected.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>The path of a table cell.</td>
 * <td>The return value is expected.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>The path of a graphic master page.</td>
 * <td>The return value is expected.</td>
 * </tr>
 *
 * </table>
 */

public class DesignElementHandleTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("DesignElementHandleTest.xml"); //$NON-NLS-1$
	}

	/**
	 * Test findContentSlot method on DesignElementHandle; and the move operation of
	 * SlotHandle.
	 *
	 * @throws Exception
	 */
	public void testFindContentSlot() throws Exception {
		GraphicMasterPageHandle mHandle = (GraphicMasterPageHandle) designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		DesignElementHandle dHandle = designHandle.findElement("label1"); //$NON-NLS-1$
		assertTrue(-1 == designHandle.findContentSlot(dHandle));
		assertTrue(0 == mHandle.findContentSlot(dHandle));

		SlotHandle sHandle = mHandle.getContent();
		DesignElementHandle textHandle = designHandle.findElement("text1"); //$NON-NLS-1$
		assertTrue(textHandle.getElement().getContainer() == mHandle.getElement());
		sHandle.move(textHandle, designHandle, ReportDesign.BODY_SLOT);
		assertTrue(textHandle.getElement().getContainer() == design);

		textHandle = designHandle.findElement("text2"); //$NON-NLS-1$
		DesignElementHandle tableHandle = designHandle.findElement("table1"); //$NON-NLS-1$
		SlotHandle detailSlotOfTable = tableHandle.getSlot(TableItem.DETAIL_SLOT);
		RowHandle rowHandle = (RowHandle) detailSlotOfTable.get(0);
		SlotHandle cellsHandle = rowHandle.getCells();
		CellHandle cellHandle = (CellHandle) cellsHandle.get(0);
		assertTrue(textHandle.getElement().getContainer() == cellHandle.getElement());
		cellHandle.getContent().move(textHandle, designHandle, ReportDesign.BODY_SLOT);
		assertTrue(textHandle.getElement().getContainer() == design);

	}

	/**
	 * Test the method.
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getContainerSlotHandle}
	 *
	 * @throws Exception if errors encountered.
	 */

	public void testGetContainerSlot() throws Exception {
		DesignElementHandle eHandle = designHandle.findElement("label1"); //$NON-NLS-1$
		SlotHandle slotHandle = eHandle.getContainerSlotHandle();
		assertNotNull(slotHandle);
		assertEquals(slotHandle.getElement(), designHandle.findMasterPage("My Page")); //$NON-NLS-1$
		assertEquals(GraphicMasterPage.CONTENT_SLOT, slotHandle.getSlotID());

		eHandle = designHandle.findElement("text2"); //$NON-NLS-1$
		slotHandle = eHandle.getContainerSlotHandle();
		assertTrue(slotHandle.getElement() instanceof Cell);

		slotHandle = designHandle.getContainerSlotHandle();
		assertNull(slotHandle);
	}

	/**
	 * Tests the private style and shared style of an element. Following methods
	 * have been tested:
	 * <ul>
	 * <li>{@link org.eclipse.birt.report.model.api.DesignElementHandle#getStyle()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getPrivateStyle()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setStyle(SharedStyleHandle)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setStyleElement}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setStyleName(String)}
	 * <li>{@link org.eclipse.birt.report.model.api.DesignElementHandle#getName()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setName(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getChoices(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#clientsIterator()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#derivedIterator()}
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testStyle() throws Exception {
		// an element with a shared style.

		DesignElementHandle handle = designHandle.findElement("bodyLabel1"); //$NON-NLS-1$
		SharedStyleHandle styleHandle = handle.getStyle();
		assertNotNull(styleHandle);

		PrivateStyleHandle privateStyleHandle = (PrivateStyleHandle) handle.getPrivateStyle();
		assertNotNull(privateStyleHandle);
		assertEquals("bodyLabel1", privateStyleHandle.getName()); //$NON-NLS-1$
		assertEquals("Style1", styleHandle.getName()); //$NON-NLS-1$

		// an element without a style.

		handle = designHandle.findElement("bodyLabel2"); //$NON-NLS-1$
		styleHandle = handle.getStyle();
		assertNull(styleHandle);
		privateStyleHandle = (PrivateStyleHandle) handle.getPrivateStyle();
		assertNotNull(privateStyleHandle);
		assertEquals("bodyLabel2", privateStyleHandle.getName()); //$NON-NLS-1$

		// an element with a private style.

		handle = designHandle.findElement("bodyLabel3"); //$NON-NLS-1$
		styleHandle = handle.getStyle();
		assertNull(styleHandle);
		privateStyleHandle = (PrivateStyleHandle) handle.getPrivateStyle();
		assertNotNull(privateStyleHandle);
		assertEquals("bodyLabel3", privateStyleHandle.getName()); //$NON-NLS-1$

		// sets a shared style for bodyLabel3 by the style name.

		styleHandle = designHandle.findStyle("Style1"); //$NON-NLS-1$
		handle.setStyleName(styleHandle.getName());
		assertNotNull(handle.getStyle());

		// clears the style

		handle.setStyle(null);
		assertNull(handle.getStyle());

		// resets the style on the label

		styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		handle.setStyle(styleHandle);
		assertNotNull(handle.getStyle());
		assertEquals("My-Style", handle.getStyle().getName()); //$NON-NLS-1$

		// clears the style

		handle.setStyleElement(null);
		assertNull(handle.getStyle());

		// resets the style on the label

		styleHandle = designHandle.findStyle("Style1"); //$NON-NLS-1$
		handle.setStyle(styleHandle);
		assertNotNull(handle.getStyle());
		assertEquals("Style1", handle.getStyle().getName()); //$NON-NLS-1$

		// clears the style

		handle.setStyleElement(null);
		assertNull(handle.getStyle());

		// resets the style through another label.

		try {
			handle.setStyleElement(new Style("unknownStyle")); //$NON-NLS-1$
			fail();
		} catch (StyleException e) {
			assertEquals(StyleException.DESIGN_EXCEPTION_NOT_FOUND, e.getErrorCode());
		}

		handle.setName("newLabel"); //$NON-NLS-1$
		assertEquals("newLabel", handle.getName()); //$NON-NLS-1$

		// font family property has choices, and the number of choices is 5.

		IChoice[] choices = styleHandle.getChoices(Style.FONT_FAMILY_PROP);
		assertNotNull(choices);
		assertEquals(5, choices.length);

		// master page property has choices.

		choices = styleHandle.getChoices(Style.MASTER_PAGE_PROP);
		assertNull(choices);

		// style1 has 2 clients.

		Iterator iterator = styleHandle.clientsIterator();

		int count = 0;
		for (; iterator.hasNext(); iterator.next()) {
			count++;
		}

		assertEquals(2, count);

		Iterator iteratorDerived = styleHandle.derivedIterator();

		count = 0;
		for (; iteratorDerived.hasNext(); iteratorDerived.next()) {
			count++;
		}

		assertEquals(0, count);
	}

	/**
	 * Tests the extends of an element. Following methods have been tested:
	 * <ul>
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getExtends()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setExtends(DesignElementHandle)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setExtendsElement}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setExtendsName(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#derivedIterator()}
	 * </ul>
	 *
	 * @throws Exception if any exception
	 */

	public void testExtends() throws Exception {
		DesignElementHandle label4Handle = designHandle.findElement("bodyLabel4"); //$NON-NLS-1$
		DesignElementHandle parentHandle = label4Handle.getExtends();
		assertEquals("base", parentHandle.getName()); //$NON-NLS-1$

		label4Handle = designHandle.findElement("label3"); //$NON-NLS-1$
		parentHandle = label4Handle.getExtends();
		assertEquals("child1", parentHandle.getName()); //$NON-NLS-1$

		parentHandle = parentHandle.getExtends();
		assertEquals("base", parentHandle.getName()); //$NON-NLS-1$

		label4Handle = designHandle.findElement("label2"); //$NON-NLS-1$
		label4Handle.setExtendsName("base"); //$NON-NLS-1$
		assertEquals("base", label4Handle.getExtends().getName()); //$NON-NLS-1$

		// clears the extends.

		label4Handle.setExtendsName(null); // $NON-NLS-1$
		assertNull(label4Handle.getExtends()); // $NON-NLS-1$

		label4Handle.setExtends(parentHandle);
		assertEquals("base", label4Handle.getExtends().getName()); //$NON-NLS-1$

		// clears the extends.

		label4Handle.setExtends(null); // $NON-NLS-1$
		assertNull(label4Handle.getExtends()); // $NON-NLS-1$

		label4Handle = designHandle.findElement("label3"); //$NON-NLS-1$
		parentHandle = label4Handle.getExtends();

		label4Handle.setExtendsElement(parentHandle.getElement());

		// clears the extends.

		label4Handle.setExtendsElement(null); // $NON-NLS-1$
		assertNull(label4Handle.getExtends()); // $NON-NLS-1$

	}

	/**
	 * Tests choices of properties on an element. Following methods have been
	 * tested:
	 * <ul>
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getChoices(String)}
	 * </ul>
	 */

	public void testChoice() {
		DesignElementHandle handle = designHandle.findElement("bodyLabel1"); //$NON-NLS-1$

		// text property on a label.

		IChoice[] choices = handle.getChoices(Label.TEXT_PROP);
		assertNull(choices);

		choices = handle.getChoices(Style.FONT_FAMILY_PROP);
		assertNotNull(choices);
		assertEquals(5, choices.length);

		choices = handle.getChoices(TextItem.CONTENT_TYPE_PROP);
		assertNull(choices);
	}

	/**
	 * Tests other related methods of DesignElementHandle. Following methods have
	 * been tested:
	 * <ul>
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#dropAndClear()}
	 * <li>{@link org.eclipse.birt.report.model.api.DesignElementHandle#moveTo}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getPropertyIterator()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getModuleHandle()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getDisplayLabel()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getDisplayLabel(int)}
	 * </ul>
	 * Detail tests for DisplayLabel in
	 * {@link org.eclipse.birt.report.model.core.DesignElementTest}.
	 *
	 * @throws Exception if any exception
	 * @see org.eclipse.birt.report.model.core.DesignElementTest
	 */

	public void testOtherMethods() throws Exception {
		// move bodyLabel1 to the component slot.

		DesignElementHandle handle = designHandle.findElement("bodyLabel1"); //$NON-NLS-1$
		SlotHandle slotHandle = designHandle.getComponents();

		handle.moveTo(slotHandle.getElementHandle(), ReportDesign.COMPONENT_SLOT);
		assertEquals(handle.getModuleHandle(), handle.getContainer());
		assertEquals(ReportDesign.COMPONENT_SLOT, handle.getContainerSlotHandle().getSlotID());

		// for a label, totally it has 59 properties.

		Iterator iterator = handle.getPropertyIterator();
		int count = 0;
		for (; iterator.hasNext(); iterator.next()) {
			count++;
		}

		// now drops the bodyLabel1 from components.

		handle.dropAndClear();
		assertNull(handle.getContainer());

		handle = designHandle.findElement("bodyLabel1"); //$NON-NLS-1$
		assertNull(handle);

		handle = (LabelHandle) designHandle.findElement("bodyLabel5"); //$NON-NLS-1$

		assertEquals("bodyLabel5", handle.getDisplayLabel()); //$NON-NLS-1$

		assertEquals("bodyLabel5", handle.getDisplayLabel(DesignElement.USER_LABEL)); //$NON-NLS-1$

		assertEquals("bodyLabel5", handle.getDisplayLabel(DesignElement.SHORT_LABEL)); //$NON-NLS-1$

		assertEquals("bodyLabel5(\"Fifth Label in body.\")", handle.getDisplayLabel(DesignElement.FULL_LABEL)); //$NON-NLS-1$

	}

	/**
	 * Tests undo the operation of moving element to another container.
	 *
	 * @throws Exception
	 */
	public void testUndoMoveToMethod() throws Exception {
		openDesign("DesignElementHandleMoveTo.xml", ULocale.ENGLISH); //$NON-NLS-1$

		DesignElementHandle handle = designHandle.getElementByID(39);
		DesignElementHandle oldContianer = handle.getContainer();
		handle.moveTo(designHandle, ReportDesign.COMPONENT_SLOT);

		designHandle.getCommandStack().undo();

		handle = designHandle.getElementByID(39);
		DesignElementHandle newContainer = handle.getContainer();
		assertEquals(oldContianer, newContainer);
	}

	/**
	 * Tests for containment.
	 *
	 * @throws SemanticException
	 */

	public void testContainment() throws SemanticException {
		createDesign();

		CommandStack stack = designHandle.getCommandStack();

		ElementFactory factory = designHandle.getElementFactory();

		// Test the simple case: body slot in the design.

		FreeFormHandle section1 = factory.newFreeForm(null);
		SlotHandle slot = designHandle.getBody();
		assertEquals(slot.getCount(), 0);

		designHandle.getBody().add(section1);

		assertEquals(section1, slot.get(0));
		NameSpace ns = design.getNameHelper().getNameSpace(ReportDesign.ELEMENT_NAME_SPACE);
		assertEquals(0, ns.getCount());
		assertEquals(design, section1.getContainer());

		// Undo.

		stack.undo();
		assertEquals(0, slot.getCount());
		assertNull(section1.getContainer());

		stack.redo();
		assertEquals(1, slot.getCount());
		assertEquals(section1, slot.get(0));
		assertEquals(design, section1.getContainer());

		// Test an an element with a name.

		FreeFormHandle section2 = factory.newFreeForm("Section2");//$NON-NLS-1$
		designHandle.getBody().add(section2);

		assertEquals(2, slot.getCount());
		assertEquals(1, ns.getCount());
		assertEquals(section2, designHandle.findElement("Section2"));//$NON-NLS-1$
		assertEquals(design, section2.getContainer());

		// Duplicate name
		//
		// FreeFormHandle section3 = factory.newFreeForm( "Section2"
		// );//$NON-NLS-1$
		//
		// try
		// {
		// designHandle.getBody( ).add( section3 );
		// fail( );
		// }
		// catch ( NameException e )
		// {
		// assertEquals( section3.getElement( ), e.getElement( ) );
		// assertEquals( "Section2", e.getName( ) );//$NON-NLS-1$
		// assertEquals( e.getErrorCode( ),
		// NameException.DESIGN_EXCEPTION_DUPLICATE );
		// }
		// assertNull( section3.getContainer( ) );

		// Test a single-item slot: a section

		// Simple case: add the item.

		FreeFormHandle container1 = factory.newFreeForm(null);
		slot = section1.getReportItems();
		assertNotNull(slot);
		assertEquals(slot.getCount(), 0);

		section1.getReportItems().add(container1);

		assertEquals(slot.getCount(), 1);
		assertEquals(slot.get(0), container1);
		assertEquals(container1.getContainer(), section1);

		// Undo.

		stack.undo();
		assertEquals(slot.getCount(), 0);
		assertNull(container1.getContainer());

		stack.redo();
		assertEquals(slot.getCount(), 1);
		assertEquals(slot.get(0), container1);
		assertEquals(container1.getContainer(), section1);

		// Test an alement with a name.

		FreeFormHandle container2 = factory.newFreeForm("Sample");//$NON-NLS-1$
		slot = section2.getReportItems();
		section2.getReportItems().add(container2);

		assertEquals(slot.getCount(), 1);
		assertEquals(slot.get(0), container2);
		assertEquals(design.findElement("Sample"), container2);//$NON-NLS-1$
		assertEquals(container2.getContainer(), section2);

		// Second entry in a single-entry slot

		MasterPageHandle masterPageHandle = factory.newSimpleMasterPage("masterPage");//$NON-NLS-1$
		designHandle.getMasterPages().add(masterPageHandle);

		try {
			FreeFormHandle freeForm1 = factory.newFreeForm("form1");//$NON-NLS-1$
			FreeFormHandle freeForm2 = factory.newFreeForm("form2");//$NON-NLS-1$

			masterPageHandle.getSlot(SimpleMasterPage.PAGE_HEADER_SLOT).add(freeForm1);
			masterPageHandle.getSlot(SimpleMasterPage.PAGE_HEADER_SLOT).add(freeForm2);
			fail();
		} catch (ContentException e) {
			assertEquals(masterPageHandle.getElement(), e.getElement());
			assertEquals(e.getSlot(), FreeForm.REPORT_ITEMS_SLOT);
			assertEquals(e.getErrorCode(), ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL);
		}

		// Wrong Slot

		LabelHandle label1 = factory.newLabel(null);
		try {
			designHandle.getStyles().add(label1);
			fail();
		} catch (ContentException e) {
			assertEquals(e.getElement(), design);
			assertEquals(e.getSlot(), IReportDesignModel.STYLE_SLOT);
			assertEquals(e.getErrorCode(), ContentException.DESIGN_EXCEPTION_WRONG_TYPE);
		}

		try {
			SlotHandle sh = new SlotHandle(designHandle, 50);
			sh.add(label1);
			fail();
		} catch (ContentException e) {
			assertEquals(e.getElement(), design);
			assertEquals(e.getSlot(), 50);
			assertEquals(e.getErrorCode(), ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND);
		}

		// Test containment within a container.

		label1 = factory.newLabel("Label1");//$NON-NLS-1$
		container1.getReportItems().add(label1);

		slot = container1.getReportItems();
		assertNotNull(slot);
		assertEquals(slot.getCount(), 1);
		assertEquals(slot.get(0), label1);
		assertTrue(ns.contains("Label1"));//$NON-NLS-1$
		assertEquals(container1, label1.getContainer());

		// Not a container

		LabelHandle label2 = factory.newLabel(null);
		try {
			SlotHandle sh = new SlotHandle(label1, 0);
			sh.add(label2.getElement().getHandle(design));
			fail();
		} catch (ContentException e) {
			assertEquals(label1.getElement(), e.getElement());
			assertEquals(e.getSlot(), 0);
			assertEquals(e.getErrorCode(), ContentException.DESIGN_EXCEPTION_NOT_CONTAINER);
		}

		// Required name

		StyleHandle style = factory.newStyle("Foo"); //$NON-NLS-1$
		style.getElement().setName(null);
		designHandle.getStyles().add(style);
		// create a unique name
		assertTrue(style.getName() != null);

		style = factory.newStyle("My_Style");//$NON-NLS-1$
		designHandle.getStyles().add(style);
	}

	/**
	 * Tests setting/getting property of an element. Following methods have been
	 * tested:
	 * <ul>
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getBooleanProperty(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getDimensionProperty(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getElementProperty(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getFactoryPropertyHandle(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getFloatProperty(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getFontProperty()}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getIntProperty(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getNumberProperty(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getProperty(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getPropertyHandle(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#getStringProperty(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setFloatProperty(String, double)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setIntProperty}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setNumberProperty(String, BigDecimal)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setProperty(String, Object)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#setStringProperty(String, String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#clearProperty(String)}
	 * </ul>
	 *
	 * @throws Exception if any exception
	 */

	public void testPropertyMethods() throws Exception {
		LabelHandle handle = (LabelHandle) designHandle.findElement("bodyLabel1"); //$NON-NLS-1$
		ULocale locale = handle.getModule().getLocale();
		NumberFormatter numberFormatter = new NumberFormatter(locale);

		// to set an data set that does not exist.

		StyleHandle styleHandle = handle.getStyle();

		// the default value is true.

		assertFalse(styleHandle.getBooleanProperty(Style.CAN_SHRINK_PROP));

		DimensionHandle dimensionHandle = handle.getDimensionProperty(Label.X_PROP);
		assertNotNull(dimensionHandle);
		assertEquals("1mm", dimensionHandle.getStringValue()); //$NON-NLS-1$

		handle.setStringProperty(Label.X_PROP, "11mm"); //$NON-NLS-1$
		assertEquals("11mm", dimensionHandle.getStringValue()); //$NON-NLS-1$

		handle.setStringProperty(Label.X_PROP, numberFormatter.format(12.3) + "pc"); // $NON-NLS-1$
		assertEquals("12.3pc", dimensionHandle.getStringValue()); // $NON-NLS-1$

		ColorHandle colorHandle = styleHandle.getColorProperty(Style.COLOR_PROP);
		assertNotNull(colorHandle);
		assertEquals("red", colorHandle.getCssValue()); //$NON-NLS-1$

		// to test get element property.

		DataSetHandle dataSethandle = designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		DesignElementHandle elementHandle = dataSethandle.getElementProperty(SimpleDataSet.DATA_SOURCE_PROP);
		assertEquals("myDataSource", elementHandle.getName()); //$NON-NLS-1$

		// CAN_SHRINK_PROP has not any value.

		FactoryPropertyHandle factoryHandle = styleHandle.getFactoryPropertyHandle(Style.CAN_SHRINK_PROP);
		assertNull(factoryHandle);

		factoryHandle = styleHandle.getFactoryPropertyHandle(Style.MASTER_PAGE_PROP);
		assertNull(factoryHandle);

		factoryHandle = styleHandle.getFactoryPropertyHandle(Label.BOOKMARK_PROP);
		assertNull(factoryHandle);

		// uses width and height to test getFloatProperty.

		double floatValue = ((DimensionValue) handle.getProperty(ReportItem.WIDTH_PROP)).getMeasure();
		assertTrue(2 == floatValue);

		floatValue = ((DimensionValue) handle.getProperty(ReportItem.HEIGHT_PROP)).getMeasure();
		assertTrue(.25 == floatValue);

		// get a font handle from the label.

		FontHandle fontHandle = handle.getPrivateStyle().getFontFamilyHandle();
		assertNotNull(fontHandle);
		assertEquals("\"Time New Roman\", \"Arial\"", fontHandle.getValue()); //$NON-NLS-1$

		fontHandle = styleHandle.getFontFamilyHandle();
		assertNotNull(fontHandle);
		assertEquals("\"Time New Roman\", \"Arial\"", fontHandle.getValue()); //$NON-NLS-1$

		// uses WIDOWS_PROP to test getIntProperty.

		int intValue = styleHandle.getIntProperty(Style.WIDOWS_PROP);
		assertTrue(2 == intValue);

		intValue = handle.getIntProperty("noPropName"); //$NON-NLS-1$
		assertTrue(0 == intValue);

		// uses WIDOWS_PROP and height to test getNumberProperty.

		BigDecimal bigDecimalValue = styleHandle.getNumberProperty(Style.WIDOWS_PROP);
		assertTrue(2 == bigDecimalValue.intValue());

		// no this property, will be null.

		bigDecimalValue = handle.getNumberProperty(SimpleDataSet.DATA_SOURCE_PROP);
		assertNull(bigDecimalValue);

		// setProperty and setStringProperty.

		String fontNames = (String) handle.getProperty(Style.FONT_FAMILY_PROP);
		assertEquals("\"Time New Roman\", \"Arial\"", fontNames); //$NON-NLS-1$

		fontNames = handle.getStringProperty(Style.FONT_FAMILY_PROP);
		assertEquals("\"Time New Roman\", \"Arial\"", fontNames); //$NON-NLS-1$

		PropertyHandle propertyHandle = handle.getPropertyHandle(Style.FONT_FAMILY_PROP);
		assertEquals("\"Time New Roman\", \"Arial\"", propertyHandle.getStringValue()); //$NON-NLS-1$

		// test the label height

		String height = handle.getProperty(ReportItem.HEIGHT_PROP).toString();
		assertEquals("0.25mm", height); //$NON-NLS-1$

		height = handle.getStringProperty(ReportItem.HEIGHT_PROP).toString();
		assertEquals("0.25mm", height); //$NON-NLS-1$

		// set properties

		double heightNumber = 1.715;
		handle.setProperty(ReportItem.HEIGHT_PROP, numberFormatter.format(heightNumber) + "in"); //$NON-NLS-1$
		assertTrue(heightNumber == handle.getPropertyHandle(ReportItem.HEIGHT_PROP).getFloatValue());

		styleHandle.setIntProperty(Style.WIDOWS_PROP, 5);
		assertTrue(5 == styleHandle.getIntProperty(Style.WIDOWS_PROP));

		try {
			styleHandle.setIntProperty(Label.CUSTOM_XML_PROP, 15);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		bigDecimalValue = new BigDecimal(55);
		styleHandle.setNumberProperty(Style.WIDOWS_PROP, bigDecimalValue);
		assertTrue(55 == styleHandle.getNumberProperty(Style.WIDOWS_PROP).intValue());

		handle.setProperty(Style.FONT_FAMILY_PROP, "Song"); //$NON-NLS-1$
		assertEquals("\"Song\"", handle.getStringProperty(Style.FONT_FAMILY_PROP)); //$NON-NLS-1$

		// test the label height

		handle.setProperty(ReportItem.HEIGHT_PROP, numberFormatter.format(2.34) + "cm"); //$NON-NLS-1$
		assertEquals("2.34cm", handle.getStringProperty(ReportItem.HEIGHT_PROP)); //$NON-NLS-1$

		handle.setStringProperty(ReportItem.HEIGHT_PROP, numberFormatter.format(0.25).substring(1) + "mm"); //$NON-NLS-1$
		assertEquals("0.25mm", handle.getStringProperty(ReportItem.HEIGHT_PROP)); //$NON-NLS-1$

		handle.setProperty(ReportItem.HEIGHT_PROP, new DimensionValue(2.34, DesignChoiceConstants.UNITS_CM));

		dimensionHandle = handle.getHeight();
		assertEquals("2.34cm", dimensionHandle.getStringValue()); //$NON-NLS-1$

		handle.clearProperty(ReportItem.HEIGHT_PROP);
		assertNull(handle.getProperty(ReportItem.HEIGHT_PROP));

		// resets to the default value.

		styleHandle.clearProperty(Style.FONT_FAMILY_PROP);
		assertEquals(DesignChoiceConstants.FONT_FAMILY_SERIF, styleHandle.getProperty(Style.FONT_FAMILY_PROP));

		// to set an data set that does not exist.

		TableHandle table = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$

		// from firstDataSet to NoExistedDataSet

		table.setProperty(IReportItemModel.DATA_SET_PROP, "NoExistedDataSet"); //$NON-NLS-1$
		assertEquals("NoExistedDataSet", table //$NON-NLS-1$
				.getProperty(IReportItemModel.DATA_SET_PROP));

		// with the same unresolved status

		table.setProperty(IReportItemModel.DATA_SET_PROP, "NoExistedDataSet"); //$NON-NLS-1$
		assertEquals("NoExistedDataSet", table //$NON-NLS-1$
				.getProperty(IReportItemModel.DATA_SET_PROP));
	}

	/**
	 * Test setProperties(). The map parameter contains: 1) dimension value. 2)
	 * string value. 3) color value. 4) Choice value.
	 *
	 * @throws SemanticException
	 */

	public void testSetProperties() throws SemanticException {
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("bodyLabel1"); //$NON-NLS-1$

		Map properties = new HashMap();
		properties.put(Label.HEIGHT_PROP, "12cm"); //$NON-NLS-1$
		properties.put(Label.TEXT_PROP, "new Label1"); //$NON-NLS-1$
		properties.put(Style.BACKGROUND_COLOR_PROP, "red"); //$NON-NLS-1$
		properties.put(Style.BORDER_LEFT_STYLE_PROP, "solid"); //$NON-NLS-1$

		labelHandle.setProperties(properties);

		assertEquals("12cm", labelHandle.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("new Label1", labelHandle.getText()); //$NON-NLS-1$
		assertEquals("red", labelHandle.getStringProperty(Style.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$
		assertEquals("solid", labelHandle.getStringProperty(Style.BORDER_LEFT_STYLE_PROP)); //$NON-NLS-1$

		// if failed in process, the underlining code do not provide rollback.
	}

	/**
	 * Clear the contents slot within a GraphicMasterPage.
	 *
	 * @throws SemanticException
	 */

	public void testClearElements() throws SemanticException {
		GraphicMasterPageHandle pageHandle = (GraphicMasterPageHandle) designHandle.findMasterPage("My Page"); //$NON-NLS-1$

		assertEquals(4, pageHandle.getSlot(GraphicMasterPage.CONTENT_SLOT).getCount());
		pageHandle.clearContents(GraphicMasterPage.CONTENT_SLOT);
		assertEquals(0, pageHandle.getSlot(GraphicMasterPage.CONTENT_SLOT).getCount());

	}

	/**
	 * Clear all properties of masterpage.
	 *
	 * @throws SemanticException
	 */

	public void testClearProperties() throws SemanticException {
		GraphicMasterPageHandle pageHandle = (GraphicMasterPageHandle) designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		pageHandle.clearAllProperties();
	}

	/**
	 * Test the follwing addElementMethod.
	 * <ul>
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#addElement(DesignElementHandle, int) }
	 * </li>
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.DesignElementHandle#addElement(DesignElementHandle, int, int)}
	 * </li>
	 * </ul>
	 * Add a new Label to the end of the contents slot of a GraphicMasterPage. The
	 * label should be added and it should in the end position.
	 *
	 * @throws NameException
	 * @throws ContentException
	 */

	public void testAddElement() throws ContentException, NameException {
		// 1. addElement(DesignElementHandle, int)

		GraphicMasterPageHandle pageHandle = (GraphicMasterPageHandle) designHandle.findMasterPage("My Page"); //$NON-NLS-1$

		assertEquals(4, pageHandle.getSlot(GraphicMasterPage.CONTENT_SLOT).getCount());
		LabelHandle newLabel = new ElementFactory(design).newLabel("American Navy 1921"); //$NON-NLS-1$
		pageHandle.addElement(newLabel, GraphicMasterPage.CONTENT_SLOT);
		assertEquals(5, pageHandle.getSlot(GraphicMasterPage.CONTENT_SLOT).getCount());
		assertEquals(4, pageHandle.getSlot(GraphicMasterPage.CONTENT_SLOT).findPosn(newLabel));

		// 2. addElement(DesignElementHandle, int, int)

		ImageHandle newImage = new ElementFactory(design).newImage("Rock in deep blue sea"); //$NON-NLS-1$

		// add an image to position 3.
		pageHandle.addElement(newImage, GraphicMasterPage.CONTENT_SLOT, 3);
		assertEquals(6, pageHandle.getSlot(GraphicMasterPage.CONTENT_SLOT).getCount());
		assertEquals(3, pageHandle.getSlot(GraphicMasterPage.CONTENT_SLOT).findPosn(newImage));

	}

	/**
	 * Tests broadcast() methods through a content event.
	 *
	 * @throws Exception
	 */

	public void testBroadCast() throws Exception {
		TableHandle tableHandle = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$
		BroadCast1Listener containerListener = new BroadCast1Listener();

		RowHandle rowHandle = (RowHandle) tableHandle.getDetail().get(0);
		rowHandle.addListener(containerListener);

		rowHandle.dropAndClear();
		assertNotNull(rowHandle);

		// make sure there is no listener here.

		rowHandle = (RowHandle) tableHandle.getDetail().get(0);
		BroadCast2Listener listener2 = new BroadCast2Listener();
		rowHandle.addListener(listener2);

		rowHandle.dropAndClear();
		assertNotNull(rowHandle);
	}

	/**
	 * Tests the semantic check from handle.
	 *
	 * @throws DesignFileException if failed to open design file
	 */

	public void testSemanticCheck() throws DesignFileException {
		openDesign("DesignElementHandleTest_1.xml"); //$NON-NLS-1$
		assertEquals(1, designHandle.getErrorList().size());

		OdaDataSourceHandle dataSourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource"); //$NON-NLS-1$
		assertNotNull(dataSourceHandle);

		List list = dataSourceHandle.semanticCheck();
		assertEquals(0, list.size());

		// Register one validation listener to one table

		DesignElementHandle myTable = designHandle.findElement("myTable"); //$NON-NLS-1$
		MyListener myListener = new MyListener();
		designHandle.addValidationListener(myListener);

		designHandle.checkReport();
		list = design.getAllErrors();
		assertEquals(4, list.size());
		assertEquals(SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT, ((ErrorDetail) list.get(0)).getErrorCode());
		assertEquals(SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT, ((ErrorDetail) list.get(1)).getErrorCode());
		assertEquals(SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT, ((ErrorDetail) list.get(2)).getErrorCode());
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET, ((ErrorDetail) list.get(3)).getErrorCode());

		assertEquals(1, designHandle.getErrorList().size());
		assertEquals(3, designHandle.getWarningList().size());

		list = myTable.getSemanticErrors();
		assertEquals(1, list.size());
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET, ((ErrorDetail) list.get(0)).getErrorCode());

		list = myListener.errorList;
		assertEquals(1, list.size());
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET, ((ErrorDetail) list.get(0)).getErrorCode());

		designHandle.removeValidationListener(myListener);
	}

	/**
	 * Test isValid() and hasValidationError().
	 *
	 * @throws DesignFileException
	 */

	public void testValidAndHasValidationError() throws DesignFileException {
		openDesign("DesignElementHandleTest_1.xml"); //$NON-NLS-1$
		assertEquals(1, designHandle.getErrorList().size());

		OdaDataSourceHandle dataSourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource"); //$NON-NLS-1$
		assertNotNull(dataSourceHandle);

		List list = dataSourceHandle.semanticCheck();
		assertEquals(0, list.size());
		assertTrue(dataSourceHandle.isValid());
		assertFalse(dataSourceHandle.hasSemanticError());
		dataSourceHandle.setValid(false);
		assertFalse(dataSourceHandle.isValid());
		assertFalse(dataSourceHandle.hasSemanticError());

		// Register one validation listener to one table

		DesignElementHandle myTable = designHandle.findElement("myTable"); //$NON-NLS-1$
		designHandle.checkReport();
		assertTrue(myTable.isValid());
		assertTrue(myTable.hasSemanticError());
	}

	/**
	 * Tests canContain() method for table or list cannot directly or indirecly
	 * reside in the table header slot.
	 *
	 * @throws SemanticException
	 */

	public void testCanContainTableHeader() throws SemanticException {

		createDesign();

		ElementFactory factory = new ElementFactory(design);

		assertTrue(designHandle.canContain(ReportDesign.DATA_SET_SLOT, factory.newOdaDataSet(null, null)));
		assertFalse(designHandle.canContain(ReportDesign.BODY_SLOT, factory.newOdaDataSet(null, null)));
		assertFalse(designHandle.canContain(ReportDesign.DATA_SET_SLOT, factory.newOdaDataSource(null, null)));

		// normal cases in FreeForm

		FreeFormHandle form = factory.newFreeForm(null);
		designHandle.getBody().add(form);

		assertFalse(form.canContain(FreeForm.REPORT_ITEMS_SLOT, factory.newOdaDataSet(null, null)));
		assertFalse(form.canContain(FreeForm.REPORT_ITEMS_SLOT, factory.newCell()));
		assertTrue(form.canContain(FreeForm.REPORT_ITEMS_SLOT, factory.newList(null)));

		// test special values.

		assertFalse(form.canContain(FreeForm.REPORT_ITEMS_SLOT, (String) null));
		assertFalse(form.canContain(FreeForm.REPORT_ITEMS_SLOT, (DesignElementHandle) null));
		assertFalse(form.canContain(FreeForm.NO_SLOT, (DesignElementHandle) null));

		// table is nested in the table header slot.

		TableHandle table = factory.newTableItem(null, 1);
		RowHandle row = (RowHandle) (table.getHeader().get(0));

		// row element can contain cell element.

		assertTrue(row.canContain(TableRow.CONTENT_SLOT, ReportDesignConstants.CELL_ELEMENT));
		assertFalse(row.canContain(TableRow.NO_SLOT, factory.newCell()));

		// The row cannot be inserted into the table header

		row = factory.newTableRow();
		CellHandle cell = factory.newCell();
		cell.addElement(factory.newTableItem(null), Cell.CONTENT_SLOT);
		row.addElement(cell, TableRow.CONTENT_SLOT);
		assertFalse(table.canContain(TableItem.HEADER_SLOT, row));

		// table-header cell element can not contain table item.

		row = (RowHandle) (table.getHeader().get(0));
		cell = (CellHandle) (row.getSlot(TableRow.CONTENT_SLOT).get(0));

		assertFalse(cell.canContain(Cell.CONTENT_SLOT, ReportDesignConstants.TABLE_ITEM));
		assertFalse(cell.canContain(Cell.CONTENT_SLOT, factory.newTableItem(null)));
		assertTrue(cell.canContain(Cell.CONTENT_SLOT, factory.newFreeForm(null)));

		// table-header cell element can not contain list item.

		assertFalse(cell.canContain(Cell.CONTENT_SLOT, factory.newList(null)));
		assertFalse(cell.canContain(Cell.CONTENT_SLOT, ReportDesignConstants.LIST_ITEM));

		// table-header cell element can not contain a free-form that contains a
		// table. However, it can contain a single free-form.

		form.addElement(factory.newTableItem(null), FreeForm.REPORT_ITEMS_SLOT);
		assertFalse(cell.canContain(Cell.CONTENT_SLOT, form));
		assertTrue(cell.canContain(Cell.CONTENT_SLOT, ReportDesignConstants.FREE_FORM_ITEM));

		// add a free-from to table-header cell element. Then this freeform
		// cannot contain table items.

		form = factory.newFreeForm(null);
		cell.addElement(form, Cell.CONTENT_SLOT);
		assertFalse(form.canContain(FreeForm.REPORT_ITEMS_SLOT, factory.newTableItem(null)));
		assertFalse(form.canContain(FreeForm.REPORT_ITEMS_SLOT, ReportDesignConstants.TABLE_ITEM));

		// table is allowed to be nested in the table footer slot.

		row = (RowHandle) (table.getFooter().get(0));
		cell = (CellHandle) (row.getSlot(TableRow.CONTENT_SLOT).get(0));
		assertTrue(cell.canContain(Cell.CONTENT_SLOT, ReportDesignConstants.TABLE_ITEM));

		// table-footer cell element can contain a free-form that contains a
		// table. And, it can contain a single free-form.

		form = factory.newFreeForm(null);
		form.addElement(factory.newTableItem(null), FreeForm.REPORT_ITEMS_SLOT);
		assertTrue(cell.canContain(Cell.CONTENT_SLOT, form));
		assertTrue(cell.canContain(Cell.CONTENT_SLOT, ReportDesignConstants.FREE_FORM_ITEM));

		// add a free-from to table-footer cell element. Then this free-form
		// can contain table items.

		form = factory.newFreeForm(null);
		cell.addElement(form, Cell.CONTENT_SLOT);
		assertTrue(form.canContain(FreeForm.REPORT_ITEMS_SLOT, factory.newTableItem(null)));
		assertTrue(form.canContain(FreeForm.REPORT_ITEMS_SLOT, ReportDesignConstants.TABLE_ITEM));

	}

	/**
	 * Tests canContain() method for duplicate group names in the listing element.
	 *
	 * @throws SemanticException
	 */

	public void testCanContainGroupName() throws SemanticException {
		createDesign();
		design = (ReportDesign) designHandle.getModule();

		ElementFactory factory = new ElementFactory(design);

		TableHandle table = factory.newTableItem("table", 1); //$NON-NLS-1$

		// test two table group or list groups with same names without datasets.

		TableGroupHandle tableGroup = factory.newTableGroup();
		tableGroup.setName("Group1"); //$NON-NLS-1$

		assertTrue(table.canContain(TableItem.GROUP_SLOT, tableGroup));
		table.getGroups().add(tableGroup);

		assertFalse(table.canContain(TableItem.GROUP_SLOT, tableGroup));

		tableGroup = factory.newTableGroup();
		tableGroup.setName("Group2"); //$NON-NLS-1$
		assertTrue(table.canContain(TableItem.GROUP_SLOT, tableGroup));

		// establishes the relationship between data set and table.

		DataSourceHandle dataSource = factory.newOdaDataSource(null, null);
		DataSetHandle dataSet = factory.newOdaDataSet(null, null);
		dataSet.setDataSource(dataSource.getName());
		table.setDataSet(dataSet);
		designHandle.getDataSources().add(dataSource);
		designHandle.getDataSets().add(dataSet);

		// for a group with different names. It is OK.

		assertTrue(table.canContain(TableItem.GROUP_SLOT, tableGroup));
		table.getGroups().add(tableGroup);

		// for an existed group name, cannot be contained.

		assertFalse(table.canContain(TableItem.GROUP_SLOT, tableGroup));
		tableGroup = factory.newTableGroup();
		tableGroup.setName("Group1"); //$NON-NLS-1$

		assertFalse(table.canContain(TableItem.GROUP_SLOT, tableGroup));

		// test cases for nested tables.

	}

	/**
	 * Tests the canContain().
	 *
	 * @throws SemanticException
	 *
	 */

	public void testCanContain() throws SemanticException {
		createDesign();
		design = (ReportDesign) designHandle.getModule();

		// different type

		LabelHandle label = designHandle.getElementFactory().newLabel(null);
		label.setName("newLabel"); //$NON-NLS-1$
		assertFalse(designHandle.canContain(ReportDesign.PAGE_SLOT, label));
		assertFalse(designHandle.getMasterPages().canContain(label));

		// multicardinality used to set false. To fix bug #132316, now change
		// masterpage slot to multicardinality.

		MasterPageHandle page = designHandle.getElementFactory().newGraphicMasterPage(null);

		assertTrue(designHandle.canContain(ReportDesign.PAGE_SLOT, page));
		assertTrue(designHandle.getMasterPages().canContain(page));

		designHandle.getMasterPages().add(page);

		assertTrue(designHandle.canContain(ReportDesign.PAGE_SLOT, page));
		assertTrue(designHandle.getMasterPages().canContain(page));

		// multicardinality is true

		assertTrue(designHandle.getComponents().canContain(label));
		assertTrue(designHandle.getComponents().canContain(ReportDesignConstants.GRID_ITEM));

		assertTrue(designHandle.canContain(ReportDesign.COMPONENT_SLOT, ReportDesignConstants.GRID_ITEM));

		designHandle.getComponents().add(label);

		assertTrue(designHandle.canContain(ReportDesign.COMPONENT_SLOT, label));
		designHandle.getComponents().canContain(ReportDesignConstants.GRID_ITEM);

		// tests recursive containment is forbidden.

		TableHandle table = designHandle.getElementFactory().newTableItem("table1", 3, 1, 1, 1);
		RowHandle row = (RowHandle) table.getDetail().get(0);
		CellHandle cell = (CellHandle) row.getCells().get(0);
		assertFalse(cell.canContain(CellHandle.CONTENT_SLOT, table));
	}

	/**
	 * Tests the function to retrieve the path of an element. The path begins from
	 * the root of the design tree.
	 *
	 * @throws SemanticException
	 *
	 */

	public void testGetPath() throws SemanticException {
		DesignElementHandle element = designHandle.findElement("bodyLabel3"); //$NON-NLS-1$
		assertEquals("/report/body/label[@id=\"15\"]", element.getXPath()); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$

		// the second row in the table detail

		RowHandle row = (RowHandle) table.getDetail().get(1);
		assertEquals("/report/body/table[@id=\"22\"]/detail/row[@id=\"28\"]", //$NON-NLS-1$
				row.getXPath());

		// the first cell in the above row

		assertEquals("/report/body/table[@id=\"22\"]/detail/row[@id=\"28\"]/cell[@id=\"29\"]", //$NON-NLS-1$
				row.getCells().get(0).getXPath());

		element = designHandle.findElement("text2"); //$NON-NLS-1$
		assertEquals("/report/body/table[@id=\"18\"]/detail/row[@id=\"19\"]/cell[@id=\"20\"]/text[@id=\"21\"]", //$NON-NLS-1$
				element.getXPath());

		StyleHandle style = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		assertEquals("/report/styles/style[@id=\"4\"]", //$NON-NLS-1$
				style.getXPath());

		style = designHandle.findStyle("Style1"); //$NON-NLS-1$
		assertEquals("/report/styles/style[@id=\"5\"]", //$NON-NLS-1$
				style.getXPath());

		MasterPageHandle page = designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		assertEquals("/report/page-setup/graphic-master-page[@id=\"8\"]", //$NON-NLS-1$
				page.getXPath());
	}

	/**
	 * Test cases for DesignElementHandle.isDirectionRTL.
	 *
	 * @throws Exception
	 */

	public void testIsDirectionRTL() throws Exception {
		LabelHandle bodyLabel1 = (LabelHandle) designHandle.findElement("bodyLabel1"); //$NON-NLS-1$
		assertTrue(bodyLabel1.isDirectionRTL());

		LabelHandle bodyLabel2 = (LabelHandle) designHandle.findElement("bodyLabel2"); //$NON-NLS-1$
		assertFalse(bodyLabel2.isDirectionRTL());

		TableHandle tabl1 = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$
		assertTrue(tabl1.isDirectionRTL());
	}

	/**
	 * Tests if the report item locates in template parameter definition.
	 *
	 * @throws Exception
	 */
	public void testInTemplateParameterDefinition() throws Exception {

		openDesign("DesignElementHandleTest_2.xml");//$NON-NLS-1$

		DesignElementHandle table = designHandle.getElementByID(41);
		assertTrue(table.isInTemplateParameter());

	}

	public void testCanContainElementSharesBinding() throws Exception {
		openDesign("DesignElementHandleTest_3.xml");//$NON-NLS-1$

		TableHandle table1 = (TableHandle) designHandle.findElement("Table1");
		assertNotNull(table1);
		TableHandle table2 = (TableHandle) designHandle.findElement("Table2");
		assertNotNull(table2);
		TableHandle table3 = (TableHandle) designHandle.findElement("Table3");
		assertNotNull(table3);

		assertFalse(getCell(table1).canContain(CellHandle.CONTENT_SLOT, table2));
		assertFalse(getCell(table2).canContain(CellHandle.CONTENT_SLOT, table1));
		assertFalse(getCell(table3).canContain(CellHandle.CONTENT_SLOT, table1));
		assertTrue(getCell(table1).canContain(CellHandle.CONTENT_SLOT, table3));
	}

	private CellHandle getCell(TableHandle table) {
		RowHandle row = (RowHandle) table.getDetail().get(0);
		return (CellHandle) row.getCells().get(0);
	}

	/**
	 * The listener modifies the <code>listeners</code> of a design element. Used to
	 * test <code>broadcast</code> method in <code>DesignElement</code>.
	 */

	class BroadCast1Listener implements Listener {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */

		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev.getEventType() == NotificationEvent.CONTENT_EVENT) {
				focus.removeListener(this);
			}
		}
	}

	/**
	 * The listener does not modify the <code>listeners</code> of a design element.
	 * Used to test <code>drop</code> method in <code>DesignElementHandle</code>.
	 */

	class BroadCast2Listener implements Listener {

		protected int flag = 0;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */

		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev.getEventType() == NotificationEvent.CONTENT_EVENT) {
				flag++;
			}
		}

	}

	class MyListener implements IValidationListener {

		protected List errorList = null;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.api.validators.IValidationListener#
		 * elementValidated (org.eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.validators.ValidationEvent)
		 */
		@Override
		public void elementValidated(DesignElementHandle targetElement, ValidationEvent ev) {
			errorList = ev.getErrors();
		}
	}
}
