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

package org.eclipse.birt.report.designer.internal.ui.dnd;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 *  
 */

public class InsertInLayoutUtilTest extends BaseTestCase {

	static class LayoutExtendsUtil extends InsertInLayoutUtil {

		public static DesignElementHandle performInsertDataSetColumn(ResultSetColumnHandle model, Object target,
				Object targetParent) throws SemanticException {
			return InsertInLayoutUtil.performInsertDataSetColumn(model, target, targetParent);
		}
	}

	private static final String FILE_NAME = "../internal/ui/dnd/DndTest.rptdesign";
	private static final String DATA_SET_1_NAME = "Data Set";
	private static final String TABLE1_NAME = "Table";
	private static final String LIST1_NAME = "List";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.testutil.BaseTestCase#getLoadFile()
	 */
	protected String getLoadFile() {
		return FILE_NAME;
	}

	// private ElementFactory getElementFactory( )
	// {
	// return getReportDesignHandle( ).getElementFactory( );
	// }

	private DataSetHandle getDataSet1() {
		return getReportDesignHandle().findDataSet(DATA_SET_1_NAME);
	}

	private ListHandle getListHandle() {
		return (ListHandle) getReportDesignHandle().findElement(LIST1_NAME);
	}

	private TableHandle getTableHandle() {
		return (TableHandle) getReportDesignHandle().findElement(TABLE1_NAME);
	}

	private TableGroupHandle getTableGroup() {
		return (TableGroupHandle) getTableHandle().getGroups().get(0);
	}

	private ListGroupHandle getListGroup() {
		return (ListGroupHandle) getListHandle().getGroups().get(0);
	}

	private CellHandle getCell(int row, int column) {
		return (CellHandle) HandleAdapterFactory.getInstance().getTableHandleAdapter(getTableHandle()).getCell(row,
				column);
	}

	public void testOpenFile() {
		assertTrue("test data set 1", getDataSet1() != null);
		assertTrue("test data set 1", getDataSet1().getDataSource() != null);
		assertTrue("test table 1", getTableHandle() != null);
		assertTrue("test list 1", getListHandle() != null);
		assertTrue("test table group", getTableGroup() != null);
		assertTrue("test list group", getListGroup() != null);
	}

	public void testPerformInsertDataSetColumn() throws SemanticException {
//		DataSetManager.setCurrentInstance( DataSetManager.newInstance( ) );
//
//		DataSetItemModel[] columnModels = DataSetManager.getCurrentInstance( )
//				.getColumns( getDataSet1( ), false );
//
//		Object target = null;
//		Object targetParent = null;
//		String keyExp = null;
//
//		//Test GroupKeySetRule
//		//Test table group
//		target = getCell( 2, 1 );
//		targetParent = getTableHandle( );
//		assertTrue( "test table group key expression",
//				getTableGroup( ).getKeyExpr( ) == null );
//		assertTrue( "test table data set",
//				getTableHandle( ).getDataSet( ) == null );
//		for ( int i = 0; i < columnModels.length; i++ )
//		{
//			if ( i == 0 )
//			{
//				keyExp = columnModels[i].getDataSetColumnName( );
//			}
//			LayoutExtendsUtil.performInsertDataSetColumn( columnModels[i],
//					target,
//					targetParent );
//		}
//
//		assertTrue( "test table group key expression",
//				getTableGroup( ).getKeyExpr( ).equals( keyExp ) );
//		assertTrue( "test table data set",
//				getTableHandle( ).getDataSet( ) == getDataSet1( ) );
//
//		//Test list group
//		target = new ListBandProxy( getListGroup( ).getSlot( ListGroup.HEADER_SLOT ) );
//		targetParent = getListHandle( );
//		assertTrue( "test list group key expression",
//				getListGroup( ).getKeyExpr( ) == null );
//		assertTrue( "test list data set",
//				getListHandle( ).getDataSet( ) == null );
//		for ( int i = 0; i < columnModels.length; i++ )
//		{
//			if ( i == 0 )
//			{
//				keyExp = columnModels[i].getDataSetColumnName( );
//			}
//			LayoutExtendsUtil.performInsertDataSetColumn( columnModels[i],
//					target,
//					targetParent );
//		}
//
//		assertTrue( "test list group key expression",
//				getListGroup( ).getKeyExpr( ).equals( keyExp ) );
//		assertTrue( "test list data set",
//				getListHandle( ).getDataSet( ) == getDataSet1( ) );
	}
}
