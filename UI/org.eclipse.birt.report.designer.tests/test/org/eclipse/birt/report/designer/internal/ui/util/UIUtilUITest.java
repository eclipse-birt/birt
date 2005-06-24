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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewPage;
import org.eclipse.birt.report.designer.tests.ITestConstants;
import org.eclipse.birt.report.designer.testutil.BirtUITestCase;
import org.eclipse.birt.report.designer.testutil.PrivateAccessor;
import org.eclipse.birt.report.designer.ui.views.data.DataView;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * UI tests for UIUtil
 */

public class UIUtilUITest extends BirtUITestCase
{

	public void testIsReportEditorActivated( ) throws Exception
	{
		openEditor( );
		assertTrue( UIUtil.isReportEditorActivated( ) );
		closeEditor( );
		assertFalse( UIUtil.isReportEditorActivated( ) );
	}

	/*
	 * Class under test for ReportEditor getActiveReportEditor()
	 */
	public void testGetActiveReportEditor( ) throws Exception
	{
		openEditor( );
		assertTrue( UIUtil.getActiveReportEditor( ) != null );
		assertTrue( UIUtil.getActiveReportEditor( true ) != null );
		assertTrue( UIUtil.getActiveReportEditor( false ) != null );
		closeEditor( );
		assertTrue( UIUtil.getActiveReportEditor( ) == null );
		assertTrue( UIUtil.getActiveReportEditor( true ) == null );
		assertTrue( UIUtil.getActiveReportEditor( false ) == null );
	}

	public void testGetLayoutEditPartViewer( ) throws Exception
	{
		openEditor( );
		assertTrue( UIUtil.getLayoutEditPartViewer( ) != null );
		closeEditor( );
		assertTrue( UIUtil.getLayoutEditPartViewer( ) == null );
	}

	public void testGetDefaultShell( )
	{
		assertEquals( Display.getCurrent( ).getActiveShell( ),
				UIUtil.getDefaultShell( ) );
	}

	public void testGetDefaultProject( )
	{
		assertNotNull( UIUtil.getDefaultProject( ) );
		assertEquals( ITestConstants.TEST_PROJECT_NAME,
				UIUtil.getDefaultProject( ).getName( ) );
	}

	public void testContainElement( ) throws Throwable
	{
		showPerspective( );
		openEditor( );
		getView( DATA_EXPLORER_ID ).setFocus( );
		DataViewPage dataViewPage = (DataViewPage) ( (DataView) getView( DATA_EXPLORER_ID ) ).getCurrentPage( );
		TreeViewer dataTreeViewer = (TreeViewer) PrivateAccessor.getField( dataViewPage,
				"treeViewer" );

		ContentOutlinePage outlinePage = (ContentOutlinePage) PrivateAccessor.getField( UIUtil.getActiveReportEditor( ),
				"outlinePage" );
		TreeViewer outlineTreeViewer = (TreeViewer) PrivateAccessor.getField( outlinePage,
				"treeViewer" );

		ReportDesignHandle reportHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		DataSourceHandle dataSource = reportHandle.getElementFactory( )
				.newOdaDataSource( null );
		DataSetHandle dataSet = reportHandle.getElementFactory( )
				.newScriptDataSet( null );
		ScalarParameterHandle param1 = reportHandle.getElementFactory( )
				.newScalarParameter( "P1" );
		ScalarParameterHandle param2 = reportHandle.getElementFactory( )
				.newScalarParameter( "P2" );
		ParameterGroupHandle paramGroup = reportHandle.getElementFactory( )
				.newParameterGroup( null );
		paramGroup.addElement( param2, ParameterGroupHandle.PARAMETERS_SLOT );

		reportHandle.getDataSources( ).add( dataSource );
		reportHandle.getDataSets( ).add( dataSet );
		reportHandle.getParameters( ).add( param1 );
		reportHandle.getParameters( ).add( paramGroup );

		LabelHandle label1 = reportHandle.getElementFactory( ).newLabel( null );
		LabelHandle label2 = reportHandle.getElementFactory( ).newLabel( null );
		TextItemHandle text = reportHandle.getElementFactory( )
				.newTextItem( null );
		ListHandle list = reportHandle.getElementFactory( ).newList( null );
		ListGroupHandle listGroup = reportHandle.getElementFactory( )
				.newListGroup( );
		listGroup.getHeader( ).add( label2 );
		list.getDetail( ).add( text );
		list.getGroups( ).add( listGroup );

		reportHandle.getBody( ).add( label1 );
		reportHandle.getBody( ).add( list );

		getView( DATA_EXPLORER_ID ).setFocus( );
		dataTreeViewer.refresh( );
		getView( OUTLINE_ID ).setFocus( );
		outlineTreeViewer.refresh( );

		try
		{
			assertTrue( UIUtil.containElement( outlineTreeViewer, label1 ) );
			assertTrue( UIUtil.containElement( outlineTreeViewer, label2 ) );
			assertTrue( UIUtil.containElement( outlineTreeViewer, text ) );
			assertTrue( UIUtil.containElement( outlineTreeViewer, list ) );
			assertTrue( UIUtil.containElement( outlineTreeViewer, listGroup ) );

			assertTrue( UIUtil.containElement( dataTreeViewer, dataSource ) );
			assertTrue( UIUtil.containElement( dataTreeViewer, dataSet ) );
			assertTrue( UIUtil.containElement( dataTreeViewer, param1 ) );
			assertTrue( UIUtil.containElement( dataTreeViewer, paramGroup ) );
			assertTrue( UIUtil.containElement( dataTreeViewer, param2 ) );
			
			assertFalse( UIUtil.containElement( outlineTreeViewer, dataSource ) );
			assertFalse( UIUtil.containElement( outlineTreeViewer, dataSet ) );
			assertFalse( UIUtil.containElement( outlineTreeViewer, param1 ) );
			assertFalse( UIUtil.containElement( outlineTreeViewer, paramGroup ) );
			assertFalse( UIUtil.containElement( outlineTreeViewer, param2 ) );

			assertFalse( UIUtil.containElement( dataTreeViewer, label1 ) );
			assertFalse( UIUtil.containElement( dataTreeViewer, label2 ) );
			assertFalse( UIUtil.containElement( dataTreeViewer, text ) );
			assertFalse( UIUtil.containElement( dataTreeViewer, list ) );
			assertFalse( UIUtil.containElement( dataTreeViewer, listGroup ) );
			
		}
		catch ( Throwable e )
		{
			throw e;
		}
		finally
		{
			closeEditor( );
		}
	}
}
