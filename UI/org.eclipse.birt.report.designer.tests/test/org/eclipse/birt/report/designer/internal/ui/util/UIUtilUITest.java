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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.tests.ITestConstants;
import org.eclipse.birt.report.designer.testutil.BirtUITestCase;

/**
 * UI tests for UIUtil
 */

public class UIUtilUITest extends BirtUITestCase {

	public void testIsReportEditorActivated() throws Exception {
		openEditor();
		assertTrue(UIUtil.isReportEditorActivated());
		closeEditor();
		assertFalse(UIUtil.isReportEditorActivated());
	}

	/*
	 * Class under test for ReportEditor getActiveReportEditor()
	 */
	public void testGetActiveReportEditor() throws Exception {
		openEditor();
		assertTrue(UIUtil.getActiveReportEditor() != null);
		assertTrue(UIUtil.getActiveReportEditor(true) != null);
		assertTrue(UIUtil.getActiveReportEditor(false) != null);
		closeEditor();
		assertTrue(UIUtil.getActiveReportEditor() == null);
		assertTrue(UIUtil.getActiveReportEditor(true) == null);
		assertTrue(UIUtil.getActiveReportEditor(false) == null);
	}

	public void testGetLayoutEditPartViewer() throws Exception {
		openEditor();
		assertTrue(UIUtil.getLayoutEditPartViewer() != null);
		closeEditor();
		assertTrue(UIUtil.getLayoutEditPartViewer() == null);
	}

	public void testGetDefaultShell() {
		// assertEquals( Display.getDefault( ).getActiveShell( ),
		// UIUtil.getDefaultShell( ) );
	}

	public void testGetDefaultProject() {
		assertNotNull(UIUtil.getDefaultProject());
		assertEquals(ITestConstants.TEST_PROJECT_NAME, UIUtil.getDefaultProject().getName());
	}

	public void testContainElement() throws Throwable {
//		showPerspective( );
//		openEditor( );
//
//		AbstractMultiPageEditor editor = UIUtil.getActiveReportEditor( );
//		ContentOutlinePage outlinePage = (ContentOutlinePage) editor.getSite( )
//				.getPage( );
//		TreeViewer outlineTreeViewer = (TreeViewer) PrivateAccessor.getField( outlinePage,
//				"treeViewer" );
//
//		ReportDesignHandle reportHandle = (ReportDesignHandle) SessionHandleAdapter.getInstance( )
//				.getReportDesignHandle( );
//
//		DataSourceHandle dataSource = reportHandle.getElementFactory( )
//				.newOdaDataSource( null, null );
//		DataSetHandle dataSet = reportHandle.getElementFactory( )
//				.newScriptDataSet( null );
//		dataSet.setDataSource( dataSource.getName( ) );
//		ScalarParameterHandle param1 = reportHandle.getElementFactory( )
//				.newScalarParameter( "P1" );
//		ScalarParameterHandle param2 = reportHandle.getElementFactory( )
//				.newScalarParameter( "P2" );
//		ParameterGroupHandle paramGroup = reportHandle.getElementFactory( )
//				.newParameterGroup( null );
//		paramGroup.addElement( param2, ParameterGroupHandle.PARAMETERS_SLOT );
//
//		reportHandle.getDataSources( ).add( dataSource );
//		reportHandle.getDataSets( ).add( dataSet );
//		reportHandle.getParameters( ).add( param1 );
//		reportHandle.getParameters( ).add( paramGroup );
//
//		LabelHandle label1 = reportHandle.getElementFactory( ).newLabel( null );
//		LabelHandle label2 = reportHandle.getElementFactory( ).newLabel( null );
//		TextItemHandle text = reportHandle.getElementFactory( )
//				.newTextItem( null );
//		ListHandle list = reportHandle.getElementFactory( ).newList( null );
//		ListGroupHandle listGroup = reportHandle.getElementFactory( )
//				.newListGroup( );
//		listGroup.getHeader( ).add( label2 );
//		list.getDetail( ).add( text );
//		list.getGroups( ).add( listGroup );
//
//		reportHandle.getBody( ).add( label1 );
//		reportHandle.getBody( ).add( list );
//
//		getView( OUTLINE_ID ).setFocus( );
//		outlineTreeViewer.refresh( );
//
//		try
//		{
//			assertTrue( UIUtil.containElement( outlineTreeViewer, label1 ) );
//			assertTrue( UIUtil.containElement( outlineTreeViewer, label2 ) );
//			assertTrue( UIUtil.containElement( outlineTreeViewer, text ) );
//			assertTrue( UIUtil.containElement( outlineTreeViewer, list ) );
//			assertTrue( UIUtil.containElement( outlineTreeViewer, listGroup ) );
//
//			assertTrue( UIUtil.containElement( outlineTreeViewer, dataSource ) );
//			assertTrue( UIUtil.containElement( outlineTreeViewer, dataSet ) );
//			assertTrue( UIUtil.containElement( outlineTreeViewer, param1 ) );
//			assertTrue( UIUtil.containElement( outlineTreeViewer, paramGroup ) );
//			assertTrue( UIUtil.containElement( outlineTreeViewer, param2 ) );
//		}
//		catch ( Throwable e )
//		{
//			throw e;
//		}
//		finally
//		{
//			closeEditor( );
//		}
	}
}
