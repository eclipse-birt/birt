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

package org.eclipse.birt.report.designer.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.LabelEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 */

public class DNDUtilTest extends BaseTestCase {

	private DataItemHandle[] dataItems;
	private TableHandle table;

	private void createSource() {
		dataItems = new DataItemHandle[4];
		for (int i = 0; i < dataItems.length; i++) {
			dataItems[i] = getElementFactory().newDataItem("DataItem" + (i + 1));
		}

		table = getElementFactory().newTableItem("Table1", 3, 2, 2, 2);
	}

	private void setTable() {
		try {
			getCellForSingle().addElement(dataItems[0], 0);
			getCellForMultiple().addElement(dataItems[1], 0);
			getCellForMultiple().addElement(dataItems[2], 0);
			getCellForMultiple().addElement(dataItems[3], 0);

			getReportDesignHandle().getBody().add(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dropSource() {
		for (int i = 0; i < dataItems.length; i++) {
			dataItems[i] = null;
		}

		table = null;
		// try
		// {
		// getReportDesignHandle( ).drop( );
		// }
		// catch ( SemanticException e )
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace( );
		// }
	}

	private StructuredSelection getSelection() {
		ArrayList list = new ArrayList();

		list.add(table);
		list.add(dataItems[0]);
		list.add(dataItems[1]);
		list.add(dataItems[2]);

		StructuredSelection selection = new StructuredSelection(list);
		return selection;
	}

	private ElementFactory getElementFactory() {
		return getReportDesignHandle().getElementFactory();
	}

	private CellHandle getCell() {
		return (CellHandle) ((RowHandle) (table.getHeader().get(0))).getCells().get(0);
	}

	private CellHandle getCellForMultiple() {
		return getCell(TableItem.DETAIL_SLOT, 1, 1);
	}

	private CellHandle getCellForSingle() {
		return getCell(TableItem.HEADER_SLOT, 0, 1);
	}

	private CellHandle getCell(int type, int row, int column) {
		SlotHandle rows = null;
		if (type == TableItem.HEADER_SLOT)
			rows = table.getHeader();
		else if (type == TableItem.DETAIL_SLOT)
			rows = table.getHeader();
		else if (type == TableItem.FOOTER_SLOT)
			rows = table.getHeader();
		else
			return null;
		return (CellHandle) ((RowHandle) (rows.get(row))).getCells().get(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.testutil.BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		createSource();
		setTable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.testutil.BaseTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		dropSource();
	}

	public void testCloneSource() {
		assertTrue("Clone duplicated elements", getLength(DNDUtil.cloneSource(getSelection())) == 1);

		SlotHandle listDetail = getElementFactory().newList("list").getDetail();
		try {
			listDetail.add(getElementFactory().newLabel("label"));
			listDetail.add(getElementFactory().newDataItem("data"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue("Clone list detail", getLength(DNDUtil.cloneSource(listDetail)) == 2);

	}

	private int getLength(Object obj) {
		if (obj instanceof Object[]) {
			return ((Object[]) obj).length;
		} else if (obj instanceof SlotHandle) {
			return ((SlotHandle) obj).getCount();
		} else if (obj instanceof CellHandle) {
			return ((CellHandle) obj).getContent().getCount();
		}
		return 0;
	}

	public void testHandleValidateTargetCanContain() {
		assertFalse("Validate null", DNDUtil.handleValidateTargetCanContain(null, null));

		assertTrue("Validate container", DNDUtil.handleValidateTargetCanContain(getCell(), dataItems[3]));

		assertTrue("Validate brothers", DNDUtil.handleValidateTargetCanContain(dataItems[0], dataItems[3]));

		ParameterGroupHandle groupChild = getElementFactory().newParameterGroup("child");
		ParameterGroupHandle groupParent = getElementFactory().newParameterGroup("parent");
		assertTrue("Validate parameter group", DNDUtil.handleValidateTargetCanContain(groupChild, groupParent));

		SlotHandle listDetailSlot = getElementFactory().newList("list").getDetail();
		DataItemHandle dataItem = getElementFactory().newDataItem("data");
		assertTrue("Validate list detail", DNDUtil.handleValidateTargetCanContain(listDetailSlot, dataItem));

		LabelHandle label = getElementFactory().newLabel("label");
		try {
			listDetailSlot.add(label);
			assertTrue("Validate list detail brothers",
					DNDUtil.handleValidateTargetCanContain(label, dataItem, true) == DNDUtil.CONTAIN_PARENT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testHandleValidateTargetCanContainMore() {
		assertFalse("Validate null", DNDUtil.handleValidateTargetCanContainMore(null, 0));

		assertTrue("Validate contain 1", DNDUtil.handleValidateTargetCanContainMore(getCell(), 1));
		assertTrue("Validate contain 2", DNDUtil.handleValidateTargetCanContainMore(getCell(), 2));

		SimpleMasterPageHandle masterPage = (SimpleMasterPageHandle) getReportDesignHandle().getMasterPages().get(0);

		assertTrue("Validate master page 0", DNDUtil.handleValidateTargetCanContainMore(masterPage.getPageHeader(), 0));
		assertTrue("Validate master page 1", DNDUtil.handleValidateTargetCanContainMore(masterPage.getPageHeader(), 1));
		assertFalse("Validate master page 2",
				DNDUtil.handleValidateTargetCanContainMore(masterPage.getPageHeader(), 2));
	}

	public void testHandleValidateTargetCanContainType() {
		SimpleMasterPageHandle masterPage = (SimpleMasterPageHandle) getReportDesignHandle().getMasterPages().get(0);
		assertFalse("Validate null", DNDUtil.handleValidateTargetCanContainType(null, ReportDesignConstants.TEXT_ITEM));
		assertTrue("Validate text", DNDUtil.handleValidateTargetCanContainType(masterPage.getPageHeader(),
				ReportDesignConstants.TEXT_ITEM));
		assertTrue("Validate grid", DNDUtil.handleValidateTargetCanContainType(masterPage.getPageHeader(),
				ReportDesignConstants.GRID_ITEM));
		assertTrue("Validate label", DNDUtil.handleValidateTargetCanContainType(masterPage.getPageHeader(),
				ReportDesignConstants.LABEL_ITEM));
		assertTrue("Validate data", DNDUtil.handleValidateTargetCanContainType(masterPage.getPageHeader(),
				ReportDesignConstants.DATA_ITEM));
		assertTrue("Validate image", DNDUtil.handleValidateTargetCanContainType(masterPage.getPageHeader(),
				ReportDesignConstants.IMAGE_ITEM));
		assertFalse("Validate table", DNDUtil.handleValidateTargetCanContainType(masterPage.getPageHeader(),
				ReportDesignConstants.TABLE_ITEM));
	}

	public void testHandleValidateDragInOutline() {
		assertFalse(DNDUtil.handleValidateDragInOutline(null));
		assertFalse(DNDUtil.handleValidateDragInOutline(new Object[0]));

		ArrayList list = new ArrayList();
		list.add(getElementFactory().newDataItem(""));
		list.add(getElementFactory().newLabel(""));
		list.add(getElementFactory().newTableItem(""));
		list.add(getElementFactory().newOdaDataSource(""));
		list.add(getElementFactory().newOdaDataSet(""));
		list.add(getElementFactory().newScalarParameter(""));
		list.add(getElementFactory().newParameterGroup(""));
		list.add(getElementFactory().newTableGroup());
		list.add(getElementFactory().newListGroup());
		// list.add( getElementFactory( ).newTableRow( ) );

		ListHandle listHandle = getElementFactory().newList("");
		list.add(listHandle);

		assertTrue(DNDUtil.handleValidateDragInOutline(new StructuredSelection(list)));

		assertFalse("can't copy empty list band", DNDUtil.handleValidateDragInOutline(listHandle.getHeader()));

		try {
			listHandle.getHeader().add(getElementFactory().newLabel("listband"));
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		assertTrue("can't copy content list band", DNDUtil.handleValidateDragInOutline(listHandle.getHeader()));

		list.clear();
		list.add(listHandle.getDetail());
		assertFalse("can't drag empty list detail", DNDUtil.handleValidateDragInOutline(new StructuredSelection(list)));

		list.clear();
		try {
			listHandle.getDetail().add(getElementFactory().newLabel(""));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		list.add(listHandle.getDetail());
		assertTrue("can drag not empty list detail",
				DNDUtil.handleValidateDragInOutline(new StructuredSelection(list)));

		list.clear();
		try {
			listHandle.getDetail().add(getElementFactory().newDataItem("data"));
		} catch (ContentException e) {
			e.printStackTrace();
		} catch (NameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list.add(listHandle.getDetail());
		assertTrue("can drag list detail", DNDUtil.handleValidateDragInOutline(new StructuredSelection(list)));

		list.clear();
		list.add(getElementFactory().newTableItem("").getDetail());
		assertFalse("can't drag table detail", DNDUtil.handleValidateDragInOutline(new StructuredSelection(list)));

		list.clear();
		list.add(getElementFactory().newCell());
		assertFalse("can't drag cell", DNDUtil.handleValidateDragInOutline(new StructuredSelection(list)));
	}

	public void testCopyHandles() {
		// Test copy to container
		SlotHandle container = dataItems[0].getContainerSlotHandle();
		int count = container.getCount();
		assertTrue("Cell is blank before", getCell().getContent().getCount() == 0);
		DNDUtil.copyHandles(dataItems[0], getCell(), 0);
		assertTrue("Cell has one after", getCell().getContent().getCount() == 1);
		assertTrue("copy item's container has same contents", container.getCount() == count);

	}

	public void testMoveHandles() {
		SlotHandle container = dataItems[0].getContainerSlotHandle();
		int count = container.getCount();
		assertTrue("Cell is blank before", getCell().getContent().getCount() == 0);
		DNDUtil.moveHandles(dataItems[0], getCell(), 0);
		assertTrue("Cell has one after", getCell().getContent().getCount() == 1);
		assertTrue("copy item's container has less contents than before", container.getCount() == count - 1);
	}

	public void testEditPart2Model() {
		ArrayList list = new ArrayList();
		ListHandle listHandle = getElementFactory().newList("");
		list.add(new ListBandEditPart(new ListBandProxy(listHandle.getHeader())));
		list.add(new LabelEditPart(getElementFactory().newLabel("")));

		IStructuredSelection result = InsertInLayoutUtil.editPart2Model(new StructuredSelection(list));
		for (Iterator i = result.iterator(); i.hasNext();) {
			Object obj = i.next();
			assertTrue(obj.toString(), obj instanceof DesignElementHandle || obj instanceof SlotHandle);
		}
	}

	public void testMoveHandlesParameterGroup() {
		try {
			ParameterGroupHandle groupChild = getElementFactory().newParameterGroup("child");
			groupChild.addElement(getElementFactory().newScalarParameter("c1"), ParameterGroup.PARAMETERS_SLOT);
			groupChild.addElement(getElementFactory().newScalarParameter("c2"), ParameterGroup.PARAMETERS_SLOT);
			// add to report before operation
			getReportDesignHandle().addElement(groupChild, ModuleHandle.PARAMETER_SLOT);

			ParameterGroupHandle groupParent = getElementFactory().newParameterGroup("parent");
			groupParent.addElement(getElementFactory().newScalarParameter("p1"), ParameterGroup.PARAMETERS_SLOT);
			groupParent.addElement(getElementFactory().newScalarParameter("p2"), ParameterGroup.PARAMETERS_SLOT);
			// add to report before operation
			getReportDesignHandle().addElement(groupParent, ModuleHandle.PARAMETER_SLOT);

			DNDUtil.moveHandles(groupChild, groupParent, 0);
			int childCount = groupChild.getParameters().getCount();
			int parentCount = groupParent.getParameters().getCount();
			assertTrue("parameter group paste test--child", childCount == 0);
			assertTrue("parameter group paste test--parent", parentCount == 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testMoveHandlesPosition() {
		DataItemHandle data = dataItems[0];
		String value = "aa";
		try {
			for (int i = 0; i < dataItems.length; i++) {
				dataItems[i].setResultSetColumn("0");
			}
			data.setResultSetColumn(value);
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		int pos = 2;
		DNDUtil.moveHandles(data, getCellForMultiple(), pos);
		DataItemHandle newHandle = (DataItemHandle) getCellForMultiple().getContent().get(pos);
		assertTrue("validate move position--from outside", newHandle.getResultSetColumn().equals(value));
		assertTrue("check count", getCellForMultiple().getContent().getCount() == 4);

		pos = 0;
		DNDUtil.moveHandles(newHandle, getCellForMultiple(), pos);
		newHandle = (DataItemHandle) getCellForMultiple().getContent().get(pos);
		assertTrue("validate move position--move forward", newHandle.getResultSetColumn().equals(value));
		assertTrue("check count", getCellForMultiple().getContent().getCount() == 4);

		pos = 2;
		DNDUtil.moveHandles(newHandle, getCellForMultiple(), pos);
		newHandle = (DataItemHandle) getCellForMultiple().getContent().get(pos - 1);
		assertTrue("validate move position--move backward", newHandle.getResultSetColumn().equals(value));
		assertTrue("check count", getCellForMultiple().getContent().getCount() == 4);
	}

	public void testMoveHandlesRename() {
		try {
			ParameterGroupHandle groupChild = getElementFactory().newParameterGroup("child");
			groupChild.addElement(getElementFactory().newScalarParameter("c1"), ParameterGroup.PARAMETERS_SLOT);
			groupChild.addElement(getElementFactory().newScalarParameter("c2"), ParameterGroup.PARAMETERS_SLOT);
			// add to report before operation
			getReportDesignHandle().addElement(groupChild, ModuleHandle.PARAMETER_SLOT);

			ParameterGroupHandle groupParent = getElementFactory().newParameterGroup("parent");
			groupParent.addElement(getElementFactory().newScalarParameter("p1"), ParameterGroup.PARAMETERS_SLOT);
			groupParent.addElement(getElementFactory().newScalarParameter("p2"), ParameterGroup.PARAMETERS_SLOT);
			// add to report before operation
			getReportDesignHandle().addElement(groupParent, ModuleHandle.PARAMETER_SLOT);

			DNDUtil.moveHandles(groupChild, groupParent, 0);
			SlotHandle parameters = groupParent.getParameters();
			assertTrue("validate rename", parameters.get(0).getName().equals("p1"));
			assertTrue("validate rename", parameters.get(1).getName().equals("p2"));
			assertTrue("validate rename", parameters.get(2).getName().equals("c1"));
			assertTrue("validate rename", parameters.get(3).getName().equals("c2"));
		} catch (ContentException e) {
			e.printStackTrace();
		} catch (NameException e) {
			e.printStackTrace();
		}
	}

//	public void testCopyHandlesRename( )
//	{
//		ParameterGroupHandle groupChild = getElementFactory( ).newParameterGroup( "child" );
//		try
//		{
//			groupChild.addElement( getElementFactory( ).newScalarParameter( "c1" ),
//					ParameterGroup.PARAMETERS_SLOT );
//			groupChild.addElement( getElementFactory( ).newScalarParameter( "c2" ),
//					ParameterGroup.PARAMETERS_SLOT );
//		}
//		catch ( ContentException e )
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace( );
//		}
//		catch ( NameException e )
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace( );
//		}
//		ParameterGroupHandle groupParent = getElementFactory( ).newParameterGroup( "parent" );
//
//		DNDUtil.copyHandles( groupChild, groupParent, 0 );
//		SlotHandle parameters = groupParent.getParameters( );
//		assertTrue( "validate rename", parameters.get( 0 )
//				.getName( )
//				.equals( "c11" ) );
//		assertTrue( "validate rename", parameters.get( 1 )
//				.getName( )
//				.equals( "c21" ) );
//
//		DNDUtil.copyHandles( groupChild, groupParent, 0 );
//		parameters = groupParent.getParameters( );
//		assertTrue( "validate rename", parameters.get( 2 )
//				.getName( )
//				.equals( "c12" ) );
//		assertTrue( "validate rename", parameters.get( 3 )
//				.getName( )
//				.equals( "c22" ) );
//	}

	public void testMoveHandlesSlotContent() {
		ListHandle listHandle = getElementFactory().newList("list");
		SlotHandle transfer = listHandle.getDetail();
		SlotHandle target = listHandle.getFooter();
		try {
			transfer.add(getElementFactory().newLabel("label"));
			transfer.add(getElementFactory().newDataItem("data"));
			assertTrue("move list slot handle", getLength(transfer) == 2);
			DNDUtil.moveHandles(transfer, target, -1);
			assertTrue("move list slot handle", getLength(transfer) == 0);
			assertTrue("move list slot handle", getLength(target) == 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDropSource() {
		ListHandle listHandle = getElementFactory().newList("list");
		SlotHandle transfer = listHandle.getDetail();
		try {
			transfer.add(getElementFactory().newLabel("label"));
			transfer.add(getElementFactory().newDataItem("data"));
			assertTrue("drop list slot handle", getLength(transfer) == 2);
			DNDUtil.dropSource(transfer);
			assertTrue("drop list slot handle", getLength(transfer) == 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DeleteCommand command = null;

		command = new DeleteCommand(getElementFactory().newSimpleMasterPage("master page"));
		assertFalse("can't delete master page", command.canExecute());

		command = new DeleteCommand(getElementFactory().newOdaDataSource("data source"));
		assertTrue("can delete data source", command.canExecute());

		command = new DeleteCommand(getElementFactory().newOdaDataSet("data set"));
		assertTrue("can delete data set", command.canExecute());

		command = new DeleteCommand(getElementFactory().newParameterGroup("parameter group"));
		assertTrue("can delete parameter group", command.canExecute());

		command = new DeleteCommand(getElementFactory().newScalarParameter("parameter"));
		assertTrue("can delete parameter", command.canExecute());

		command = new DeleteCommand(getElementFactory().newLabel("label"));
		assertTrue("can delete label", command.canExecute());

		command = new DeleteCommand(getElementFactory().newTableItem("table"));
		assertTrue("can delete table", command.canExecute());

		command = new DeleteCommand(getElementFactory().newTableRow());
		assertTrue("can delete table row", command.canExecute());

		command = new DeleteCommand(getElementFactory().newTableColumn());
		assertTrue("can delete table column", command.canExecute());

		command = new DeleteCommand(getElementFactory().newCell());
		assertFalse("can't delete empty table cell", command.canExecute());

		try {
			getCell().addElement(getElementFactory().newDataItem("ss"), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		command = new DeleteCommand(getCell());
		assertTrue("can delete table cell", command.canExecute());

		command = new DeleteCommand(listHandle);
		assertTrue("can delete list", command.canExecute());

		command = new DeleteCommand(listHandle.getHeader());
		assertFalse("can't delete empty list band", command.canExecute());

		try {
			listHandle.getHeader().add(getElementFactory().newLabel(""));
		} catch (ContentException e1) {
			e1.printStackTrace();
		} catch (NameException e1) {
			e1.printStackTrace();
		}
		command = new DeleteCommand(listHandle.getHeader());
		assertTrue("can delete list band", command.canExecute());

		ListGroupHandle lg = getElementFactory().newListGroup();
		try {
			listHandle.addElement(lg, ListHandle.GROUP_SLOT);
			lg.addElement(getElementFactory().newLabel("lg"), GroupHandle.HEADER_SLOT);
		} catch (ContentException e1) {
			e1.printStackTrace();
		} catch (NameException e1) {
			e1.printStackTrace();
		}
		command = new DeleteCommand(lg.getHeader());
		assertTrue("can delete list group", command.canExecute());
	}

	public void testCopyHandlesSibling() {
		// Test list slot
		ListHandle listHandle = getElementFactory().newList("list");
		TextItemHandle text = getElementFactory().newTextItem("text");
		LabelHandle label = getElementFactory().newLabel("label");
		SlotHandle detail = listHandle.getDetail();
		try {
			detail.add(label);
			detail.add(getElementFactory().newDataItem("data"));
			int position = DNDUtil.calculateNextPosition(label, DNDUtil.CONTAIN_PARENT);
			DNDUtil.copyHandles(text, label);
			assertTrue("length is right", getLength(detail) == 3);
			assertTrue("position is right", detail.get(position) instanceof TextItemHandle);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Test element handle
		try {
			text = getElementFactory().newTextItem("text2");
			int position = DNDUtil.calculateNextPosition(dataItems[2], DNDUtil.CONTAIN_PARENT);
			DNDUtil.copyHandles(text, dataItems[2]);
			assertTrue("length is right", getLength(getCellForMultiple()) == 4);
			assertTrue("position is right", getCellForMultiple().getContent().get(position) instanceof TextItemHandle);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Test multiple order
		label = getElementFactory().newLabel("label1");
		text = getElementFactory().newTextItem("text3");
		Object[] array = new Object[] { label, text };
		Object cloneElements = DNDUtil.cloneSource(array);
		DNDUtil.copyHandles(cloneElements, dataItems[0]);
		DNDUtil.copyHandles(cloneElements, dataItems[0]);
		assertTrue("multiple order", getCellForSingle().getContent().get(0) instanceof DataItemHandle);
		assertTrue("multiple order", getCellForSingle().getContent().get(1) instanceof LabelHandle);
		assertTrue("multiple order", getCellForSingle().getContent().get(2) instanceof TextItemHandle);
		assertTrue("multiple order", getCellForSingle().getContent().get(1) instanceof LabelHandle);
		assertTrue("multiple order", getCellForSingle().getContent().get(2) instanceof TextItemHandle);
	}

	public void testCheckContainerExists() {
		{
			// Check table
			CellHandle cell00 = getCell();
			DesignElementHandle row0 = table.getHeader().get(0);
			DesignElementHandle row1 = table.getHeader().get(1);
			DesignElementHandle column0 = table.getColumns().get(0);
			DesignElementHandle column1 = table.getColumns().get(1);
			assertTrue("test cell row in", DNDUtil.checkContainerExists(cell00, new Object[] { cell00, row0 }));
			assertTrue("test cell column in", DNDUtil.checkContainerExists(cell00, new Object[] { cell00, column0 }));
			assertTrue("test cell table in", DNDUtil.checkContainerExists(cell00, new Object[] { cell00, table }));
			assertFalse("test cell row out", DNDUtil.checkContainerExists(cell00, new Object[] { cell00, row1 }));
			assertFalse("test cell column out", DNDUtil.checkContainerExists(cell00, new Object[] { cell00, column1 }));
		}

		{
			// Check table group
			TableGroupHandle tg = getElementFactory().newTableGroup();
			RowHandle tgRow = getElementFactory().newTableRow();
			try {
				tg.addElement(tgRow, GroupHandle.HEADER_SLOT);
				table.addElement(tg, TableHandle.GROUP_SLOT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			assertTrue("test table group in", DNDUtil.checkContainerExists(tg, new Object[] { tg, table }));
			assertTrue("test table group row in", DNDUtil.checkContainerExists(tgRow, new Object[] { tgRow, table }));
			assertTrue("test table group row in", DNDUtil.checkContainerExists(tgRow, new Object[] { tgRow, tg }));
		}

		{
			// Check list and list group
			ListHandle list = getElementFactory().newList("list");
			ListGroupHandle lg = getElementFactory().newListGroup();
			DataItemHandle datal = getElementFactory().newDataItem("dl");
			try {
				lg.addElement(datal, GroupHandle.HEADER_SLOT);
				list.addElement(lg, ListHandle.GROUP_SLOT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			assertTrue("test list group in", DNDUtil.checkContainerExists(lg, new Object[] { lg, list }));
			assertTrue("test list group content in", DNDUtil.checkContainerExists(datal, new Object[] { datal, lg }));
			assertTrue("test list group content in", DNDUtil.checkContainerExists(datal, new Object[] { datal, list }));
		}
	}

	public void testIsInSameColumn() {
		// Test table
		CellHandle cell00 = getCell();
		DesignElementHandle row0 = table.getHeader().get(0);
		DesignElementHandle column0 = table.getColumns().get(0);
		DesignElementHandle column1 = table.getColumns().get(1);
		DesignElementHandle column3 = getElementFactory().newTableItem("2", 3).getColumns().get(0);
		assertTrue("test cell column in", DNDUtil.isInSameColumn(new Object[] { cell00, column0 }));
		assertFalse("test cell column out", DNDUtil.isInSameColumn(new Object[] { cell00, column1 }));
		assertFalse("test cell column out", DNDUtil.isInSameColumn(new Object[] { cell00, column3 }));
		assertFalse("test cell other out", DNDUtil.isInSameColumn(new Object[] { cell00, row0 }));
		assertFalse("test cell 3 out", DNDUtil.isInSameColumn(new Object[] { cell00, column0, column1 }));
		assertTrue("test cell 3 in", DNDUtil.isInSameColumn(new Object[] { cell00, column0, column0 }));

		// Test grid
		GridHandle grid = getElementFactory().newGridItem("grid", 3, 3);
		CellHandle gridCell00 = grid.getCell(0, 0);
		DesignElementHandle gc0 = grid.getColumns().get(0);
		DesignElementHandle gc1 = grid.getColumns().get(1);
		assertTrue("test grid cell column in", DNDUtil.isInSameColumn(new Object[] { gridCell00, gc0 }));
		assertFalse("test grid cell column out", DNDUtil.isInSameColumn(new Object[] { gridCell00, gc1 }));
	}
}
