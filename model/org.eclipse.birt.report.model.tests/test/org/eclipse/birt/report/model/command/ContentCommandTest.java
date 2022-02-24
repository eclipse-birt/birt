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

package org.eclipse.birt.report.model.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ElementDeletedEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Unit test for class ContentCommand.
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testMovePosition()}</td>
 * <td>Add three content to the container: A ,B ,C.Move A to C'position.</td>
 * <td>in container: B,C,A</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Move C to A's position.</td>
 * <td>in container: B,A,C</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Move B to A's position.</td>
 * <td>in container: A,B,C</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Move B to position 5 which is not existed in this container.</td>
 * <td>in container: A,C,B</td>
 * </tr>
 * <tr>
 * <td>{@link #testMoveSamePosition()}</td>
 * <td>The new position is the same as the original one.</td>
 * <td>still in the old position.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>test the moved content is not existed in this container.</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td>{@link #testMoveWrongType()}</td>
 * <td>The content is the wrong type of the 'toContainer'</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>The toContainer's slot is full</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td>{@link #testOtherMoveException()}</td>
 * <td>FromElement is not a container</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>toContainer is not a container</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Content not found in fromContainer</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td>{@link #testAdd()}</td>
 * <td>the target container is not a container</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Trying to add more than one content into a slot when slot can only hold
 * one</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>ContentException.WRONG_TYPE</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>the content type is not match with the container</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>The destination slot is not found.</td>
 * <td>exception thrown</td>
 * </tr>
 * <tr>
 * <td>{@link #testCircularContent()}</td>
 * <td>first, add content into container add container to content.</td>
 * <td>circular content exception here is expected</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Second, add the container into middContainer, add middContainer into
 * content to test if the circular content relationship can be detected.</td>
 * <td>circular content exception here is expected</td>
 * </tr>
 * <tr>
 * <td>{@link #testReferencAfterDeletion()}</td>
 * <td>Add element A, which has style and parent, into container. remove A from
 * the container. Check the reference between A and its style from both sides.
 * </td>
 * <td>The reference between A and its style is broken from both sides.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Check the reference between A and its parent.</td>
 * <td>The reference between A and its parent is broken from both sides.</td>
 * </tr>
 * <tr>
 * <td>{@link #testRemoveContainerFromContainer()}</td>
 * <td>Add container A, which contents a content with style and parent, into
 * container B. remove A from B. Check the reference.</td>
 * <td>All of the reference existed on container A and its content, and the
 * content style, parent should be broken.</td>
 * </tr>
 * <tr>
 * <td>{@link #testNormalCaseUndoAndRedo()}</td>
 * <td>Add content into a container. Undo and redo the operation</td>
 * <td>After undo and redo both of the reference status and the element status
 * can be recovered.</td>
 * </tr>
 * <tr>
 * <td>{@link #testUndoRedoAfterRemove()}</td>
 * <td>drop a content which has 'extends', 'name' property and a style.Undo and
 * redo the operation</td>
 * <td>After undo and redo both of the reference status and the element status
 * can be recovered.</td>
 * </tr>
 * <tr>
 * <td>{@link #testSendNotifications()}</td>
 * <td>Test the notification.</td>
 * <td>The event can be created and send out.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testMove()}</td>
 * <td>Test
 * {@link SlotHandle#move(DesignElementHandle, DesignElementHandle, int, int)}
 * </td>
 * <td>Move an element from one container to another at the specified position.
 * </td>
 * </tr>
 * 
 * </table>
 * 
 */

public class ContentCommandTest extends BaseTestCase {

	/**
	 * The report element to be tested.
	 */

	private DesignElement container;

	private MasterPageHandle containerHandle;

	ElementFactory factory = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle((ULocale) null);
		designHandle = sessionHandle.createDesign("myDesign"); //$NON-NLS-1$
		design = (ReportDesign) designHandle.getModule();

		factory = new ElementFactory(design);

		containerHandle = factory.newGraphicMasterPage("Master page1"); //$NON-NLS-1$
		designHandle.getMasterPages().add(containerHandle);
		container = containerHandle.getElement();
	}

	/**
	 * Tests add functions. Test Case:
	 * 
	 * <ul>
	 * <li>the target container is not a container;
	 * <li>Trying to add more than one content into a slot when slot can only hold
	 * one
	 * <li>the content type is not match with the container
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testAdd() throws Exception {
		// To test element id when adding

		Cell cell = new Cell();
		Label child = new Label();
		Label label = new Label();

		containerHandle.getSlot(0).add(label.getHandle(design));

		// Add one Label to Cell

		cell.getHandle(design).getSlot(0).add(child.getHandle(design));

		// The target container is not a container;

		assertNull(label.getHandle(design).getSlot(0));

		// Try to add more than one content into a slot when slot can only
		// hold one

		try {
			SimpleMasterPageHandle pageHandle = designHandle.getElementFactory().newSimpleMasterPage("simplepage"); //$NON-NLS-1$
			designHandle.getMasterPages().add(pageHandle);

			ContentCommand command = new ContentCommand(design,
					new ContainerContext(pageHandle.getElement(), SimpleMasterPage.PAGE_HEADER_SLOT));
			command.add(new TextItem());
			command = new ContentCommand(design,
					new ContainerContext(pageHandle.getElement(), SimpleMasterPage.PAGE_HEADER_SLOT));
			command.add(new TextItem());
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL, e.getErrorCode());
		}

		// The content type is not match with the container

		try {
			containerHandle.getSlot(0).add(new TableItem().getHandle(design));
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());
		}

		// The slot id doesn't exist.

		try {
			ContentCommand command = new ContentCommand(design, new ContainerContext(new FreeForm(), 999));
			command.add(new Label());
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * @throws NameException
	 * @throws ContentException
	 * 
	 * 
	 */
	public void testAddElementIntoCompoundSlot() throws ContentException, NameException {

		LabelHandle labelWithoutName = factory.newLabel(null);
		LabelHandle label = factory.newLabel("label1"); //$NON-NLS-1$

		try {
			designHandle.getComponents().add(labelWithoutName);
			fail();
		} catch (ContentException e) {

			assertEquals(ContentException.DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED, e.getErrorCode());
		}
		designHandle.getComponents().add(label);

	}

	/**
	 * Test removing one content from container.
	 * 
	 * @throws Exception if any exception
	 */
	public void testRemove() throws Exception {
		// Label is not a kind of container.

		try {
			ContentCommand command = new ContentCommand(design, new ContainerContext(new Label(), 0));
			command.remove(new Label());

			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_NOT_CONTAINER, e.getErrorCode());
		}

		// Slot is not found.
		// Note: This case can not be tested for assertion.

		// try
		// {
		// FreeFormHandle form =
		// designHandle.getElementFactory().newFreeForm("form1"); //$NON-NLS-1$
		// designHandle.getBody().add(form);
		//
		// ContentCommand command = new ContentCommand( design, design );
		// command.remove( form.getElement(), 999 );
		//
		// fail();
		// }
		// catch ( ArrayIndexOutOfBoundsException e )
		// {
		// }

		// Content is not found.

		try {
			ContentCommand command = new ContentCommand(design,
					new ContainerContext(new FreeForm(), FreeForm.REPORT_ITEMS_SLOT));
			command.remove(new Label());

			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND, e.getErrorCode());
		}

		// When style is removed, the clients of this style will has no style.

		SharedStyleHandle style = designHandle.getElementFactory().newStyle("style1"); //$NON-NLS-1$
		designHandle.getStyles().add(style);

		FreeFormHandle form = designHandle.getElementFactory().newFreeForm("form2"); //$NON-NLS-1$
		designHandle.getBody().add(form);
		form.setStyle(style);
		assertNotNull(form.getStyle());

		ContentCommand command = new ContentCommand(design,
				new ContainerContext(design, IReportDesignModel.STYLE_SLOT));
		command.remove(style.getElement());

		assertNull(form.getStyle());

		// When one data set is removed, the elements that refers this data
		// source will refer no data set.

		TableHandle table = designHandle.getElementFactory().newTableItem("table1"); //$NON-NLS-1$
		designHandle.getBody().add(table);

		OdaDataSetHandle dataSet = designHandle.getElementFactory().newOdaDataSet("dataSet1"); //$NON-NLS-1$
		designHandle.getDataSets().add(dataSet);

		table.setDataSet(dataSet);

		command = new ContentCommand(design, new ContainerContext(design, ReportDesign.DATA_SET_SLOT));
		command.remove(dataSet.getElement());

		assertNull(table.getDataSet());
	}

	/**
	 * Test method 'remove'.
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>Add content into a container. Undo and redo the operation Expected
	 * result:
	 * <li>After undo and redo both of the reference status and the element status
	 * can be recovered.
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testNormalCaseUndoAndRedo() throws Exception {
		DesignElementHandle contentHandle = factory.newFreeForm(null);
		ActivityStack as = design.getActivityStack();

		containerHandle.getSlot(0).add(contentHandle);
		Object obj = container.getSlot(0).getContent(0);
		assertEquals(contentHandle.getElement(), obj);

		// test the container can be retrieved from the added content
		assertEquals(container, contentHandle.getContainer());

		// test undo
		assertFalse(as.canRedo());
		assertTrue(as.canUndo());
		as.undo();
		assertEquals(0, container.getSlot(0).getCount());

		// test redo
		assertTrue(as.canRedo());
		as.redo();
		obj = container.getSlot(0).getContent(0);
		assertEquals(contentHandle.getElement(), obj);

		containerHandle.getSlot(0).dropAndClear(0);
		assertTrue(container.getSlot(0).getCount() == 0);

		// test that reference is removed from the content list
		assertNull(contentHandle.getContainer());

		// test undo
		assertFalse(as.canRedo());
		assertTrue(as.canUndo());
		as.undo();
		assertTrue(container.getSlot(0).getCount() == 1);

		// add content at index 0
		DesignElement content2 = new FreeForm();
		containerHandle.getSlot(0).add(content2.getHandle(design), 0);
		assertTrue(container.getSlot(0).getCount() == 2);
		assertEquals(content2, container.getSlot(0).getContent(0));

		//
		DesignElement content3 = new FreeForm();
		containerHandle.getSlot(0).add(content3.getHandle(design), 10);
		assertTrue(container.getSlot(0).getCount() == 3);
		assertEquals(content3, container.getSlot(0).getContent(2));

		DesignElement content4 = new FreeForm();
		containerHandle.getSlot(0).add(content4.getHandle(design), 1);
		assertTrue(container.getSlot(0).getCount() == 4);
		assertEquals(content2, container.getSlot(0).getContent(0));
		assertEquals(content4, container.getSlot(0).getContent(1));
		assertEquals(contentHandle.getElement(), container.getSlot(0).getContent(2));
		assertEquals(content3, container.getSlot(0).getContent(3));
	}

	/**
	 * Test method 'remove'.
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>Normal case
	 * <li>Drop a content which has 'extends', 'name' property and a style.Undo and
	 * redo the operation.
	 * </ul>
	 * 
	 * 
	 * @throws Exception if any exception
	 */

	public void testUndoRedoAfterRemove() throws Exception {
		// Initiate
		FreeForm content = new FreeForm();
		FreeForm parent = new FreeForm();

		parent.setName("parent"); //$NON-NLS-1$
		content.setName("innerContainer"); //$NON-NLS-1$
		Style style = new Style("style"); //$NON-NLS-1$
		ActivityStack as = design.getActivityStack();

		try {
			ContentCommand command = new ContentCommand(design, new ContainerContext(new Label(), 0));
			command.add(content);
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof ContentException);
			ContentException ex = (ContentException) e;
			assertEquals(ContentException.DESIGN_EXCEPTION_NOT_CONTAINER, ex.getErrorCode());
		}

		containerHandle.getSlot(0).add(content.getHandle(design));
		content.setStyle(style);
		content.setExtendsElement(parent);
		Object obj = container.getSlot(0).getContent(0);
		assertEquals(content, obj);
		assertEquals(style, content.getStyle());
		assertNotNull(content.getExtendsElement());

		containerHandle.getSlot(0).dropAndClear(0);
		assertTrue(container.getSlot(0).getCount() == 0);

		// test the reference was deleted from the content side

		assertNull(content.getStyle());
		assertNull(content.getExtendsElement());

		// test the reference was deleted from the style and parent side

		assertEquals(0, style.getClientList().size());

		assertEquals(0, parent.getSlot(0).getCount());

		assertEquals(0, parent.getDerived().size());

		// test undo: undo the remove content operation

		assertFalse(as.canRedo());
		assertTrue(as.canUndo());
		as.undo();
		assertTrue(container.getSlot(0).getCount() == 1);

		// verify the reference to style is recovered

		assertEquals(style, content.getStyle());
		assertEquals(content, ((BackRef) style.getClientList().get(0)).getElement());

		// verify the reference to parent is recovered

		assertEquals(parent, content.getExtendsElement());
		assertEquals(content, parent.getDescendents().get(0));

		// test undo: undo the add content operation

		assertTrue(as.canRedo());
		assertTrue(as.canUndo());
		as.undo();
		assertTrue(container.getSlot(0).getCount() == 0);

		// test redo
		assertTrue(as.canRedo());

	}

	/**
	 * Test method move.
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>FromElement is not a container
	 * <li>toContainer is not a container
	 * <li>Content not found in fromContainer
	 * </ul>
	 */

	public void testOtherMoveException() {
		MasterPage toContainer = new GraphicMasterPage();
		FreeForm content = new FreeForm();
		Label label = new Label();
		// 1
		try {
			ContentCommand command = new ContentCommand(design, new ContainerContext(new Label(), 0));

			command.move(content, new ContainerContext(toContainer, 0));
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_NOT_CONTAINER, e.getErrorCode());
		}

		// 2
		try {
			containerHandle.getSlot(0).move(content.getHandle(design), label.getHandle(design), 0);
			fail();
		} catch (ContentException e1) {
			assertEquals(ContentException.DESIGN_EXCEPTION_NOT_CONTAINER, e1.getErrorCode());
		}

		// 3
		try {
			containerHandle.getSlot(0).move(content.getHandle(design), toContainer.getHandle(design), 0);
			fail();
		} catch (ContentException e2) {
			assertEquals(ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND, e2.getErrorCode());
		}

	}

	/**
	 * Test method move.
	 * 
	 * Test Case:
	 * 
	 * <ul>
	 * <li>Add three content to the container: A ,B ,C
	 * <li>Move A to C'position. The expected result in container: B,C,A
	 * <li>Move C to A's position. The expected result in container: B,A,C
	 * <li>Move B to A's position.The expected result in container: A,B,C
	 * <li>Move B to position 5 which is not existed in this container. The expected
	 * result in container: A,C,B
	 * <li>Test undo and redo
	 * </ul>
	 * 
	 * 
	 * @throws Exception if any exception
	 */

	public void testMovePosition() throws Exception {
		Label A = new Label();
		A.setName("A"); //$NON-NLS-1$
		Label B = new Label();
		B.setName("B"); //$NON-NLS-1$
		Label C = new Label();
		C.setName("C"); //$NON-NLS-1$
		ActivityStack as = design.getActivityStack();

		containerHandle.getSlot(0).add(A.getHandle(design));
		containerHandle.getSlot(0).add(B.getHandle(design));
		containerHandle.getSlot(0).add(C.getHandle(design));

		assertEquals(A, container.getSlot(0).getContent(0));
		assertEquals(B, container.getSlot(0).getContent(1));
		assertEquals(C, container.getSlot(0).getContent(2));

		// only work on the item A.

		// A, B, C. not moved.

		containerHandle.getSlot(0).shift(A.getHandle(design), 0);
		assertEquals(A, container.getSlot(0).getContent(0));
		assertEquals(B, container.getSlot(0).getContent(1));
		assertEquals(C, container.getSlot(0).getContent(2));

		// A, B, C. not moved.

		containerHandle.getSlot(0).shift(A.getHandle(design), 1);
		assertEquals(B, container.getSlot(0).getContent(0));
		assertEquals(A, container.getSlot(0).getContent(1));
		assertEquals(C, container.getSlot(0).getContent(2));

		// B, A, C.

		containerHandle.getSlot(0).shift(A.getHandle(design), 2);
		assertEquals(B, container.getSlot(0).getContent(0));
		assertEquals(C, container.getSlot(0).getContent(1));
		assertEquals(A, container.getSlot(0).getContent(2));

		as.undo(); // return to A, B, C

		// B, C, A.

		containerHandle.getSlot(0).shift(A.getHandle(design), 3);
		assertEquals(B, container.getSlot(0).getContent(0));
		assertEquals(C, container.getSlot(0).getContent(1));
		assertEquals(A, container.getSlot(0).getContent(2));

		as.undo(); // return to A, B, C

		// only work on item B

		// B, A, C.

		containerHandle.getSlot(0).shift(B.getHandle(design), 0);
		assertEquals(B, container.getSlot(0).getContent(0));
		assertEquals(A, container.getSlot(0).getContent(1));
		assertEquals(C, container.getSlot(0).getContent(2));

		as.undo(); // return to A, B, C

		// not moved.

		containerHandle.getSlot(0).shift(B.getHandle(design), 1);
		assertEquals(A, container.getSlot(0).getContent(0));
		assertEquals(B, container.getSlot(0).getContent(1));
		assertEquals(C, container.getSlot(0).getContent(2));

		// A, B, C. not moved.

		containerHandle.getSlot(0).shift(B.getHandle(design), 2);
		assertEquals(A, container.getSlot(0).getContent(0));
		assertEquals(C, container.getSlot(0).getContent(1));
		assertEquals(B, container.getSlot(0).getContent(2));

		// A, C, B.

		containerHandle.getSlot(0).shift(B.getHandle(design), 3);
		assertEquals(A, container.getSlot(0).getContent(0));
		assertEquals(C, container.getSlot(0).getContent(1));
		assertEquals(B, container.getSlot(0).getContent(2));

		as.undo(); // return to A, B, C

		// now work on item C

		// C, A, B

		containerHandle.getSlot(0).shift(C.getHandle(design), 0);
		assertEquals(C, container.getSlot(0).getContent(0));
		assertEquals(A, container.getSlot(0).getContent(1));
		assertEquals(B, container.getSlot(0).getContent(2));

		as.undo(); // return to A, B, C

		// A, C, B

		containerHandle.getSlot(0).shift(C.getHandle(design), 1);
		assertEquals(A, container.getSlot(0).getContent(0));
		assertEquals(C, container.getSlot(0).getContent(1));
		assertEquals(B, container.getSlot(0).getContent(2));

		as.undo(); // return to A, B, C

		// only test another undo/redo

		as.redo(); // return to A, C, B

		as.undo(); // return to A, B, C

		// A, B, C, not moved.

		containerHandle.getSlot(0).shift(C.getHandle(design), 2);
		assertEquals(A, container.getSlot(0).getContent(0));
		assertEquals(B, container.getSlot(0).getContent(1));
		assertEquals(C, container.getSlot(0).getContent(2));

		// A, B, C, not moved.

		containerHandle.getSlot(0).shift(C.getHandle(design), 3);
		assertEquals(A, container.getSlot(0).getContent(0));
		assertEquals(B, container.getSlot(0).getContent(1));
		assertEquals(C, container.getSlot(0).getContent(2));
	}

	/**
	 * Test the method
	 * {@link SlotHandle#move(DesignElementHandle, DesignElementHandle, int)}.
	 * 
	 * @throws Exception
	 */
	public void testMove() throws Exception {
		// Initialization

		FreeForm toContainer = new FreeForm();
		designHandle.getBody().add(toContainer);
		GraphicMasterPage page = new GraphicMasterPage("test_page"); //$NON-NLS-1$
		GraphicMasterPageHandle pageHandle = page.handle(design);
		designHandle.getMasterPages().add(pageHandle);

		Label label1 = new Label();
		Label label2 = new Label();
		SlotHandle sHandle = pageHandle.getContent();
		sHandle.add(label1.getHandle(design));

		sHandle.move(label1.handle(design), toContainer.handle(design), FreeForm.REPORT_ITEMS_SLOT);
		assertEquals(sHandle.getCount(), 0);
		assertEquals(1, toContainer.handle(design).getReportItems().getCount());

		sHandle.add(label2.getHandle(design));
		assertEquals(sHandle.getCount(), 1);
		sHandle.move(label2.handle(design), toContainer.handle(design), FreeForm.REPORT_ITEMS_SLOT, 0);
		assertEquals(sHandle.getCount(), 0);
		assertEquals(2, toContainer.handle(design).getReportItems().getCount());
		assertEquals(label2, toContainer.handle(design).getReportItems().get(0).getElement());

		design.getActivityStack().undo();
		assertEquals(1, sHandle.getCount());
		assertEquals(1, toContainer.handle(design).getReportItems().getCount());

		design.getActivityStack().redo();
		assertEquals(sHandle.getCount(), 0);
		assertEquals(2, toContainer.handle(design).getReportItems().getCount());
		assertEquals(label2, toContainer.handle(design).getReportItems().get(0).getElement());

		Label label3 = new Label();
		sHandle.add(label3.getHandle(design));
		sHandle.move(label3.handle(design), toContainer.handle(design), FreeForm.REPORT_ITEMS_SLOT, 10);
		assertEquals(3, toContainer.handle(design).getReportItems().getCount());
		assertEquals(label3, toContainer.handle(design).getReportItems().get(2).getElement());

		Label label4 = new Label();
		sHandle.add(label4.getHandle(design));
		sHandle.move(label4.handle(design), toContainer.handle(design), FreeForm.REPORT_ITEMS_SLOT, 12);
		assertEquals(4, toContainer.handle(design).getReportItems().getCount());
		assertEquals(label4, toContainer.handle(design).getReportItems().get(3).getElement());

		Label label5 = new Label();
		sHandle.add(label5.getHandle(design));
		MyContentListener listener = new MyContentListener();
		label5.addListener(listener);
		sHandle.move(label5.handle(design), toContainer.handle(design), FreeForm.REPORT_ITEMS_SLOT, 1);
		assertEquals(5, toContainer.handle(design).getReportItems().getCount());
		assertEquals(label5, toContainer.handle(design).getReportItems().get(1).getElement());
		// the listerner is not cleared
		label5.getHandle(design).setProperty(ILabelModel.HELP_TEXT_PROP, "helptext"); //$NON-NLS-1$
		assertNotNull(listener.event);
		assertEquals(MyContentListener.CHANGE, listener.recieveChangeEvent);

		// test the event notification for move action
		libraryHandle = sessionHandle.createLibrary();
		ElementFactory factory = libraryHandle.getElementFactory();
		ThemeHandle newTheme = factory.newTheme("testTheme"); //$NON-NLS-1$
		libraryHandle.getThemes().add(newTheme);
		StyleHandle style = factory.newStyle("style"); //$NON-NLS-1$
		newTheme.addElement(style, IThemeModel.STYLES_SLOT);
		style = factory.newStyle("style_1"); //$NON-NLS-1$
		newTheme.addElement(style, IThemeModel.STYLES_SLOT);
		style = factory.newStyle("table"); //$NON-NLS-1$
		newTheme.addElement(style, IThemeModel.STYLES_SLOT);

		TableHandle table = factory.newTableItem("testTable"); //$NON-NLS-1$
		libraryHandle.getComponents().add(table);
		listener = new MyContentListener();
		table.addListener(listener);

		// move style to the fist position in the same theme
		style.moveTo(0);
		assertNull(listener.event);

		newTheme = factory.newTheme("them_one"); //$NON-NLS-1$
		libraryHandle.getThemes().add(newTheme);
		libraryHandle.setTheme(newTheme);
		style.moveTo(newTheme, IThemeModel.STYLES_SLOT);
		assertNotNull(listener.event);
	}

	/**
	 * Tests the move exception case. Test Case:
	 * 
	 * <ul>
	 * <li>The content is the wrong type of the 'toContainer'.
	 * <li>The toContainer's slot is full.
	 * </ul>
	 * 
	 * 
	 * @throws Exception if any exception
	 */

	public void testMoveWrongType() throws Exception {

		StyleHandle styleHandle = this.factory.newStyle("style1"); //$NON-NLS-1$

		this.designHandle.getStyles().add(styleHandle);

		// slot
		// is
		// full now.

		// 1
		try {
			designHandle.getStyles().move(styleHandle, designHandle, ReportDesign.BODY_SLOT);
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());
		}

		// 2
		designHandle.getMasterPages().add(factory.newSimpleMasterPage("page2")); //$NON-NLS-1$
		assertEquals(2, designHandle.getMasterPages().getCount());
		assertNotNull(designHandle.findMasterPage("page2")); //$NON-NLS-1$
	}

	/**
	 * Unit test for method 'movePosition'.
	 * <p>
	 * Test case:
	 * 
	 * <ul>
	 * <li>The new position is the same as the original one.
	 * <li>test the moved content is not existed in this container.expected
	 * exception thrown
	 * <li>The undo & redo operation are not tested here. That were done in the
	 * testMovePosition().
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testMoveSamePosition() throws Exception {

		Label label = new Label();
		FreeForm content = new FreeForm();

		try {

			containerHandle.getSlot(0).shift(label.getHandle(design), 2);
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND, e.getErrorCode());
		}

		containerHandle.getSlot(0).add(label.getHandle(design));
		containerHandle.getSlot(0).add(content.getHandle(design));
		containerHandle.getSlot(0).add(new Label().getHandle(design));
		containerHandle.getSlot(0).shift(label.getHandle(design), 3);

		assertEquals(label, container.getSlot(0).getContent(2));
		assertEquals(content, container.getSlot(0).getContent(0));

	}

	/**
	 * Unit test for circular content.
	 * 
	 * Test case:
	 * 
	 * <ul>
	 * <li>first, add content into container add container to content. An circular
	 * content exception here is expected.
	 * <li>Second, add the container into middContainer, add middContainer into
	 * content to test if the circular content relationship can be detected.
	 * <li>An circular exception is expected here.
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testCircularContent() throws Exception {

		FreeForm content = new FreeForm();
		FreeForm middContainer = new FreeForm();
		FreeForm topContainer = new FreeForm();
		FreeFormHandle topHandle = (FreeFormHandle) topContainer.getHandle(design);

		// Add content to top container

		topHandle.getSlot(0).add(content.getHandle(design));

		// Add the container to content which is circular contented relationship
		try {
			content.getHandle(design).getSlot(0).add(topContainer.getHandle(design));

			fail();

		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_RECURSIVE, e.getErrorCode());
		}

		// add the container to middContainer

		middContainer.getHandle(design).getSlot(0).add(topContainer.getHandle(design));

		// add middcontainer to content

		try {
			content.getHandle(design).getSlot(0).add(middContainer.getHandle(design));
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_RECURSIVE, e.getErrorCode());
		}

	}

	/**
	 * Unit test to test after delete an element, whether references on the deleted
	 * contented were adjusted correctly.
	 * 
	 * Test case:
	 * 
	 * <ul>
	 * <li>Add A into container.
	 * <li>Set A extends from B, Set C extends from A.
	 * <li>Set A has style.
	 * <li>Add A into container.
	 * <li>Remove A from the container.
	 * <li>Check the extends reference and style reference on A.
	 * </ul>
	 * 
	 * <p>
	 * The expected result: C extends from B. Parent reference and style reference
	 * on A are broken.
	 * 
	 * @throws Exception
	 */

	public void testReferencAfterDeletion() throws Exception {
		Label A = new Label();
		A.setName("A"); //$NON-NLS-1$
		// NameCommand nameA = new NameCommand( design, A );

		SlotHandle containerHandle = designHandle.getComponents();

		assertNotNull(containerHandle);
		// add A into container
		containerHandle.add(A.getHandle(design));

		Label B = new Label();
		B.getHandle(design).setName("B");//$NON-NLS-1$

		Label C = new Label();
		C.getHandle(design).setName("C");//$NON-NLS-1$

		Style style = new Style();
		style.getHandle(design).setName("style");//$NON-NLS-1$

		A.setExtendsElement(B);
		C.setExtendsElement(A);
		A.setStyle(style);

		// check the references on A
		assertEquals(A, containerHandle.get(0));
		assertEquals(B, A.getExtendsElement());
		assertEquals(C, A.getDerived().get(0));
		assertEquals(A, C.getExtendsElement());
		assertEquals(style, A.getStyle());

		try {
			containerHandle.dropAndClear(A.getHandle(design));
			fail();
		} catch (Exception e) {

		}

		// check the reference on A after deletions

		assertEquals(A, C.getExtendsElement());
		assertEquals(A, B.getDerived().get(0));

		assertEquals(1, style.getClientList().size());
		assertNotNull(A.getStyle());

		assertNotNull(A.getExtendsElement());

	}

	/**
	 * Test after delete an element, whether references on the deleted contented
	 * were adjusted correctly.
	 * 
	 * Test case:
	 * 
	 * <ul>
	 * <li>Drop and detach referenceable element
	 * <li>Drop referenceable element
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testElementReferenceAfterDeletionAndDetachment() throws Exception {
		// Test drop

		createDataSourceAndDataSet();

		DataSourceHandle dataSourceHandle = designHandle.findDataSource("dataSource");//$NON-NLS-1$
		DataSetHandle dataSetHandle = designHandle.findDataSet("dataSet");//$NON-NLS-1$

		dataSourceHandle.dropAndClear();

		assertEquals(null, dataSetHandle.getDataSourceName());
		assertEquals(null, dataSetHandle.getDataSource());
		Iterator iter = dataSourceHandle.clientsIterator();
		assertEquals(false, iter.hasNext());

		designHandle.getCommandStack().undo();

		assertEquals(dataSourceHandle.getName(), dataSetHandle.getDataSourceName());
		assertEquals(dataSourceHandle, dataSetHandle.getDataSource());
		iter = dataSourceHandle.clientsIterator();
		assertEquals(true, iter.hasNext());

		// Test drop and unresolve.

		createDataSourceAndDataSet();

		dataSourceHandle = designHandle.findDataSource("dataSource");//$NON-NLS-1$
		dataSetHandle = designHandle.findDataSet("dataSet");//$NON-NLS-1$

		dataSourceHandle.drop();

		assertEquals(dataSourceHandle.getName(), dataSetHandle.getDataSourceName());
		assertEquals(null, dataSetHandle.getDataSource());
		iter = dataSourceHandle.clientsIterator();
		assertEquals(false, iter.hasNext());

		designHandle.getCommandStack().undo();

		iter = dataSourceHandle.clientsIterator();
		assertEquals(dataSetHandle, iter.next());
		assertEquals(dataSourceHandle.getName(), dataSetHandle.getDataSourceName());
		assertEquals(dataSourceHandle, dataSetHandle.getDataSource());
	}

	private void createDataSourceAndDataSet() throws Exception {
		designHandle = createDesign();

		ScriptDataSourceHandle dataSourceHandle = designHandle.getElementFactory().newScriptDataSource("dataSource");//$NON-NLS-1$
		ScriptDataSetHandle dataSetHandle = designHandle.getElementFactory().newScriptDataSet("dataSet");//$NON-NLS-1$

		designHandle.getDataSources().add(dataSourceHandle);
		designHandle.getDataSets().add(dataSetHandle);

		dataSetHandle.setDataSource(dataSourceHandle.getName());
	}

	/**
	 * Test after delete a style, whether references on the deleted contented were
	 * adjusted correctly.
	 * 
	 * Test case:
	 * 
	 * <ul>
	 * <li>Drop and unresolve one style
	 * <li>Drop one style
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testStyleReferenceAfterDeletionAndDetachment() throws Exception {
		// Test drop

		createStyleAndLabel();

		StyleHandle styleHandle = designHandle.findStyle("style");//$NON-NLS-1$
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$

		styleHandle.dropAndClear();

		assertEquals(null, labelHandle.getStyle());
		assertEquals(null, labelHandle.getStringProperty(LabelHandle.STYLE_PROP));
		Iterator iter = styleHandle.clientsIterator();
		assertEquals(false, iter.hasNext());

		designHandle.getCommandStack().undo();

		assertEquals(styleHandle.getName(), labelHandle.getStringProperty(LabelHandle.STYLE_PROP));
		assertEquals(styleHandle, labelHandle.getStyle());
		iter = styleHandle.clientsIterator();
		assertEquals(true, iter.hasNext());

		// Test drop and reference

		createStyleAndLabel();

		styleHandle = designHandle.findStyle("style");//$NON-NLS-1$
		labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$

		styleHandle.drop();

		assertEquals(null, labelHandle.getStyle());
		assertEquals(styleHandle.getName(), labelHandle.getStringProperty(LabelHandle.STYLE_PROP));
		iter = styleHandle.clientsIterator();
		assertEquals(false, iter.hasNext());

		designHandle.getCommandStack().undo();

		assertEquals(styleHandle.getName(), labelHandle.getStringProperty(LabelHandle.STYLE_PROP));
		assertEquals(styleHandle, labelHandle.getStyle());
		iter = styleHandle.clientsIterator();
		assertEquals(true, iter.hasNext());
	}

	private void createStyleAndLabel() throws Exception {
		designHandle = createDesign();

		SharedStyleHandle styleHandle = designHandle.getElementFactory().newStyle("style");//$NON-NLS-1$
		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("label");//$NON-NLS-1$

		designHandle.getStyles().add(styleHandle);
		designHandle.getBody().add(labelHandle);

		labelHandle.setStyle(styleHandle);
	}

	/**
	 * This test case is used to test that after remove a container from a
	 * container, whether the references on the removed container were adjusted
	 * correctly.
	 * 
	 * Test case:
	 * <ul>
	 * <li>Set style to label. Add label to otherCont. Add otherCont to cont. Add
	 * cont to container. Remove cont from container. The expected result is: There
	 * is no content in container. There is no content in cont. There is no content
	 * in otherCont. There is no style on label. Style's clint list is empty.
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testRemoveContainerFromContainer() throws Exception {
		FreeForm cont = new FreeForm();
		FreeForm otherCont = new FreeForm();
		Label label = new Label();
		Style style = new Style();

		cont.getHandle(design).getSlot(0).add(otherCont.getHandle(design));
		otherCont.getHandle(design).getSlot(0).add(label.getHandle(design));

		containerHandle.getSlot(0).add(cont.getHandle(design));

		assertEquals(cont, container.getSlot(0).getContent(0));
		assertEquals(otherCont, cont.getSlot(0).getContent(0));

		containerHandle.getSlot(0).dropAndClear(cont.getHandle(design));
		assertEquals(0, container.getSlot(0).getCount());
		assertEquals(0, cont.getSlot(0).getCount());
		assertEquals(0, otherCont.getSlot(0).getCount());
		assertNull(label.getContainer());

		assertEquals(0, style.getClientList().size());

	}

	/**
	 * test sendNotifications().
	 * 
	 * @throws SemanticException
	 */

	public void testSendNotifications() throws SemanticException {
		MyContentListener containerListener = new MyContentListener();
		designHandle.addListener(containerListener);

		FreeFormHandle contentHandle = designHandle.getElementFactory().newFreeForm("Form1"); //$NON-NLS-1$

		FreeFormHandle contentHandle2 = designHandle.getElementFactory().newFreeForm("Form2"); //$NON-NLS-1$

		// Add content

		designHandle.getBody().add(contentHandle);
		assertEquals(MyContentListener.ADDED, containerListener.action);
		assertTrue(containerListener.event instanceof ContentEvent);
		assertEquals(contentHandle.getElement(), ((ContentEvent) containerListener.event).getContent());

		designHandle.getBody().add(contentHandle2);
		assertEquals(MyContentListener.ADDED, containerListener.action);
		assertTrue(containerListener.event instanceof ContentEvent);
		assertEquals(contentHandle2.getElement(), ((ContentEvent) containerListener.event).getContent());

		// Shift content

		designHandle.getBody().shift(contentHandle, 2);
		assertEquals(MyContentListener.SHIFTED, containerListener.action);
		assertEquals(contentHandle.getElement(), ((ContentEvent) containerListener.event).getContent());

		// Move content

		contentHandle.moveTo(designHandle, ReportDesign.COMPONENT_SLOT);
		assertEquals(MyContentListener.MOVED, containerListener.action);
		assertEquals(contentHandle.getElement(), ((ContentEvent) containerListener.event).getContent());

		// Remove content

		MyContentListener contentListener = new MyContentListener();

		// add listener to the content.
		contentHandle.addListener(contentListener);

		designHandle.getComponents().dropAndClear(contentHandle);
		assertEquals(MyContentListener.REMOVED, containerListener.action);
		assertNull(contentListener.event);
		assertEquals(contentHandle.getElement(), ((ContentEvent) containerListener.event).getContent());

		assertTrue(containerListener.event instanceof ContentEvent);
		assertEquals(containerListener.action, ContentEvent.REMOVE);
		assertTrue(containerListener.content == contentHandle.getElement());
	}

	/**
	 * 
	 * @throws SemanticException
	 */

	public void testSendNotificationBeforeDeletion() throws SemanticException {

		DataSetHandle dataSet = factory.newOdaDataSet("dataSet", null); //$NON-NLS-1$
		DataSourceHandle dataSource = factory.newOdaDataSource("dataSource", null); //$NON-NLS-1$

		designHandle.getDataSets().add(dataSet);
		designHandle.getDataSources().add(dataSource);
		dataSet.setDataSource("dataSource"); //$NON-NLS-1$

		MyContentListener contentListener = new MyContentListener();
		dataSet.addListener(contentListener);

		// When the dataSet is dropped, there are two event will be received:
		// PropertyEvent & ElementDeleteEvent.The first event received because
		// the dataSet need to adjust the element that it referred, which is
		// dataSource.
		designHandle.getDataSets().drop(dataSet);

		// the property event should be filtered when dataSet was dropped.

		assertEquals(MyContentListener.NA, contentListener.recieveChangeEvent);
		// the element delete event is received when dataSet was dropped.
		assertTrue(contentListener.event instanceof ElementDeletedEvent);

		dataSet.removeListener(contentListener);
		contentListener = new MyContentListener();

		designHandle.getDataSets().add(dataSet);
		designHandle.getDataSources().drop(dataSource);
		dataSet.addListener(contentListener);
		designHandle.getDataSets().drop(dataSet);

		// the property changed event was not received when dataSet was deleted
		// because the dataSource in dataSet can not be resolved.
		assertEquals(MyContentListener.NA, contentListener.recieveChangeEvent);
		// the element delete event is received when dataSet was dropped.
		assertTrue(contentListener.event instanceof ElementDeletedEvent);

	}

	/**
	 * Test cases:
	 * 
	 * 1. If add an compound element to the design tree, names of nest elements
	 * should also be added into the name space. This method creates new elements by
	 * using class in elements package.
	 * 
	 * @throws Exception if any error occurs
	 * 
	 */

	public void testAddCompoundElement() throws Exception {
		// new the element from the element packages.

		CascadingParameterGroup cascadingGroup = new CascadingParameterGroup("cas1"); //$NON-NLS-1$

		ScalarParameter param1 = new ScalarParameter("param1"); //$NON-NLS-1$
		cascadingGroup.add(param1, CascadingParameterGroup.PARAMETERS_SLOT);

		ScalarParameter param2 = new ScalarParameter("param2"); //$NON-NLS-1$
		cascadingGroup.add(param2, CascadingParameterGroup.PARAMETERS_SLOT);

		testCopyAndPasteCompoundElement(cascadingGroup);

	}

	/**
	 * Test cases:
	 * 
	 * 1. If add an compound element to the design tree, names of nest elements
	 * should also be added into the name space. This method creates new elements by
	 * using factory.
	 * 
	 * @throws Exception if any error occurs
	 * 
	 */

	public void testAddCompoundElementFromHandle() throws Exception {
		CascadingParameterGroupHandle cascadingGroupHandle = factory.newCascadingParameterGroup("cas1"); //$NON-NLS-1$

		cascadingGroupHandle.getParameters().add(designHandle.getElementFactory().newScalarParameter("param1")); //$NON-NLS-1$

		cascadingGroupHandle.getParameters().add(designHandle.getElementFactory().newScalarParameter("param2")); //$NON-NLS-1$

		testCopyAndPasteCompoundElement((CascadingParameterGroup) cascadingGroupHandle.getElement());
	}

	/**
	 * Test cases:
	 * 
	 * 1. with the given CascadingParameterGroup, test copy/paste twice and check
	 * changes of name spaces.
	 * 
	 * @param cacascadingGroup
	 * @throws Exception
	 */

	private void testCopyAndPasteCompoundElement(CascadingParameterGroup cacascadingGroup) throws Exception {
		CascadingParameterGroupHandle cascadingGroupHandle = (CascadingParameterGroupHandle) cacascadingGroup
				.getHandle(design);
		designHandle.getParameters().add(cascadingGroupHandle);

		IDesignElement clonedCas = cascadingGroupHandle.copy();
		CascadingParameterGroupHandle clonedCasHandle = (CascadingParameterGroupHandle) clonedCas.getHandle(design);

		designHandle.rename(clonedCasHandle);
		designHandle.getParameters().paste(clonedCasHandle);

		NameSpace ns = design.getNameHelper().getNameSpace(Module.PARAMETER_NAME_SPACE);
		assertEquals("param11", clonedCasHandle.getParameters().get(0) //$NON-NLS-1$
				.getName());
		assertEquals("param21", clonedCasHandle.getParameters().get(1) //$NON-NLS-1$
				.getName());

		assertTrue(ns.contains(clonedCasHandle.getParameters().get(0).getName()));
		assertTrue(ns.contains(clonedCasHandle.getParameters().get(1).getName()));

		clonedCas = cascadingGroupHandle.copy();
		clonedCasHandle = (CascadingParameterGroupHandle) clonedCas.getHandle(design);

		designHandle.rename(clonedCasHandle);
		designHandle.getParameters().paste(clonedCasHandle);

		ns = design.getNameHelper().getNameSpace(Module.PARAMETER_NAME_SPACE);
		assertEquals("param12", clonedCasHandle.getParameters().get(0) //$NON-NLS-1$
				.getName());
		assertEquals("param22", clonedCasHandle.getParameters().get(1) //$NON-NLS-1$
				.getName());

		assertTrue(ns.contains(clonedCasHandle.getParameters().get(0).getName()));
		assertTrue(ns.contains(clonedCasHandle.getParameters().get(1).getName()));
	}

	/**
	 * Tests receive propery event.
	 * 
	 * @throws Exception
	 */

	public void testGroupUniqueName() throws Exception {
		MyGroupListener groupListener = new MyGroupListener();
		TableHandle tableHandle = factory.newTableItem("table1"); //$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		TableGroupHandle groupHandle = factory.newTableGroup();
		groupHandle.addListener(groupListener);

		ContentCommand command = new ContentCommand(design,
				new ContainerContext(tableHandle.getElement(), IListingElementModel.GROUP_SLOT));
		command.add(groupHandle.getElement());

		assertEquals(PropertyEvent.PROPERTY_EVENT, groupListener.action);
		assertNotNull(groupListener);
		assertEquals(IGroupElementModel.GROUP_NAME_PROP, groupListener.name);

	}

	/**
	 * Tests when the label is deleted, the template definition will be cleared too.
	 * 
	 * @throws Exception
	 */

	public void testTemplateDefinition() throws Exception {
		// originally template definition slot is empty

		SlotHandle templateDefinitions = designHandle.getSlot(ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT);

		assertEquals(0, templateDefinitions.getCount());

		// add a label and then do some transform

		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$
		designHandle.getBody().add(label);
		assertEquals(designHandle, label.getRoot());

		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) label
				.createTemplateElement("templateLabel"); //$NON-NLS-1$
		assertEquals(1, templateDefinitions.getCount());

		label = designHandle.getElementFactory().newLabel("label1"); //$NON-NLS-1$
		templateLabel.transformToReportItem(label);
		assertEquals(designHandle, label.getRoot());
		assertNotNull(label.getElement().getTemplateParameterElement(design));
		assertEquals(1, templateDefinitions.getCount());

		// drop the label, which refers a template definition

		label.drop();
		assertNull(label.getRoot());
		assertEquals(0, templateDefinitions.getCount());
	}

	/**
	 * Tests the content command for extended-item with element property.
	 * 
	 * @throws Exception
	 */
	public void testElementProperty() throws Exception {
		openDesign("ContentCommandTest.xml"); //$NON-NLS-1$
		ExtendedItemHandle outExtendedItem = (ExtendedItemHandle) designHandle.findElement("testBox"); //$NON-NLS-1$
		ExtendedItemHandle innerExtendedItem = (ExtendedItemHandle) designHandle.findElement("detailBox"); //$NON-NLS-1$
		TableHandle innerTable = (TableHandle) designHandle.findElement("testTable"); //$NON-NLS-1$

		// drop out extended-item, check the contents drop too
		CommandStack stack = designHandle.getCommandStack();
		outExtendedItem.drop();
		assertNull(outExtendedItem.getContainer());
		assertNull(innerTable.getContainer());
		assertNull(innerExtendedItem.getContainer());
		assertNull(designHandle.findElement(outExtendedItem.getName()));
		assertNull(designHandle.findElement(innerTable.getName()));
		assertNull(designHandle.findElement(innerExtendedItem.getName()));
		assertNull(designHandle.getElementByID(outExtendedItem.getID()));
		assertNull(designHandle.getElementByID(innerTable.getID()));
		assertNull(designHandle.getElementByID(innerExtendedItem.getID()));

		// undo-drop, then all containment is recovered
		stack.undo();
		assertNotNull(outExtendedItem.getContainer());
		assertNotNull(innerTable.getContainer());
		assertNotNull(innerExtendedItem.getContainer());
		assertEquals(outExtendedItem, designHandle.findElement(outExtendedItem.getName()));
		assertEquals(innerTable, designHandle.findElement(innerTable.getName()));
		assertEquals(innerExtendedItem, designHandle.findElement(innerExtendedItem.getName()));
		assertEquals(outExtendedItem, designHandle.getElementByID(outExtendedItem.getID()));
		assertEquals(innerTable, designHandle.getElementByID(innerTable.getID()));
		assertEquals(innerExtendedItem, designHandle.getElementByID(innerExtendedItem.getID()));
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testRemoveReferencableElement() throws Exception {
		openDesign("ContentCommandTest_1.xml"); //$NON-NLS-1$
		TabularCubeHandle cube = (TabularCubeHandle) designHandle.findCube("Customer Cube"); //$NON-NLS-1$

		// now resolve the 'level' member in DimensionJoinCondition
		List conditions = cube.getListProperty(TabularCubeHandle.DIMENSION_CONDITIONS_PROP);
		for (int i = 0; i < conditions.size(); i++) {
			DimensionCondition dimensionCond = (DimensionCondition) conditions.get(i);
			List joinConditions = (List) dimensionCond.getProperty(design, DimensionCondition.JOIN_CONDITIONS_MEMBER);
			for (int j = 0; j < joinConditions.size(); j++) {
				DimensionJoinCondition dimensionJoinCond = (DimensionJoinCondition) joinConditions.get(j);
				dimensionJoinCond.getProperty(design, DimensionJoinCondition.LEVEL_MEMBER);
			}
		}

		cube.getDimension("Region").dropAndClear(); //$NON-NLS-1$

		save();
		assertTrue(compareFile("ContentCommandTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests remove template parameter definition element and undo the operation.
	 * 
	 * @throws Exception
	 */
	public void testRemoveTemplateParameterDefinition() throws Exception {
		openDesign("TemplatePrarmeterDefinitionTest.xml"); //$NON-NLS-1$

		ActivityStack as = design.getActivityStack();
		DesignElementHandle handle = designHandle.getElementByID(356);
		handle.dropAndClear();
		as.undo();
		save();

		assertTrue(compareFile("TemplatePrarmeterDefinitionTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testCopyAndPaste() throws Exception {
		openDesign("ContentCommandTest_2.xml"); //$NON-NLS-1$

		StyleHandle styleHandle = designHandle.findStyle("NewStyle"); //$NON-NLS-1$
		LabelHandle labelHandle = (LabelHandle) designHandle.getElementByID(7);

		// originally, only one label refers the style
		Iterator<StyleHandle> iter = styleHandle.clientsIterator();
		assertEquals(labelHandle, iter.next());
		assertFalse(iter.hasNext());

		// copy and paste the label to the same design, two label refer the
		// style
		IDesignElement copiedLabel = labelHandle.copy();
		DesignElementHandle copiedLableHandle = copiedLabel.getHandle(design);
		designHandle.getBody().paste(copiedLableHandle);
		iter = styleHandle.clientsIterator();
		assertEquals(labelHandle, iter.next());
		assertEquals(copiedLableHandle, iter.next());
		assertFalse(iter.hasNext());

		// undo the command stack, then maybe one label refers the style
		ActivityStack as = design.getActivityStack();
		as.undo();
		iter = styleHandle.clientsIterator();
		assertEquals(labelHandle, iter.next());
		assertFalse(iter.hasNext());

		// redo, one refers the style
		as.redo();
		iter = styleHandle.clientsIterator();
		assertEquals(labelHandle, iter.next());
		assertFalse(iter.hasNext());
	}

	class MyGroupListener implements Listener {

		int action = -1;
		PropertyEvent event = null;
		String name = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev instanceof PropertyEvent) {
				event = (PropertyEvent) ev;
				action = event.getEventType();
				name = event.getPropertyName();
			}
		}
	}

	class MyContentListener implements Listener {

		static final int NA = 0;
		static final int ADDED = 1;
		static final int REMOVED = 2;
		static final int SHIFTED = 3;
		static final int MOVED = 4;
		static final int CHANGE = 5;

		NotificationEvent event = null;

		int action = NA;
		int recieveChangeEvent = NA;
		IDesignElement content = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev.getEventType() == NotificationEvent.CONTENT_EVENT) {
				event = ev;
				int newAcion = ((ContentEvent) ev).getAction();
				content = ((ContentEvent) ev).getContent();

				if (action == REMOVED && newAcion == ContentEvent.ADD)
					action = MOVED;
				else {
					switch (newAcion) {
					case ContentEvent.ADD:
						action = ADDED;
						break;
					case ContentEvent.REMOVE:
						action = REMOVED;
						break;
					case ContentEvent.SHIFT:
						action = SHIFTED;
						break;
					}
				}
			} else if (ev.getEventType() == NotificationEvent.ELEMENT_DELETE_EVENT) {
				event = ev;
			} else if (ev.getEventType() == NotificationEvent.PROPERTY_EVENT) {
				event = ev;
				recieveChangeEvent = CHANGE;
			} else if (ev.getEventType() == NotificationEvent.STYLE_EVENT) {
				event = ev;
			}
		}

	}

}
