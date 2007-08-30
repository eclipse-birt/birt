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

package org.eclipse.birt.report.designer.ui.samples.rcp.action;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.samplesview.action.IOpenSampleReportAction;
import org.eclipse.birt.report.designer.ui.samplesview.util.PlaceResources;
import org.eclipse.birt.report.designer.ui.samplesview.view.ReportExamples;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

public class RCPOpenSampleReportAction extends Action implements
		IOpenSampleReportAction,
		Listener
{

	private static final String ACTION_TEXT = Messages.getString( "SampleReportsView.Action.openSampleReport" );

	private static final String DRILL_TO_DETAILS_CATEGORY = "Drill to Details";

	private ReportExamples composite;

	public RCPOpenSampleReportAction( )
	{
		super( ACTION_TEXT );
		setToolTipText( Messages.getString( "SampleReportsView.Action.openSampleReport.toolTipText.rcp" ) );
		setEnabled( false );
	}

	public void setMainComposite( ReportExamples composite )
	{
		this.composite = composite;
		composite.addSelectedListener( this );
	}

	public void run( )
	{
		TreeItem item = (TreeItem) composite.getSelectedElement( );
		Object selectedElement = item.getData( );
		if ( selectedElement == null
				|| !( selectedElement instanceof ReportDesignHandle ) )
		{
			return;
		}

		PlaceResources.copy( composite.getShell( ),
				getDefaultLocation( ),
				item.getText( ),
				( (ReportDesignHandle) selectedElement ).getFileName( ) );

		if ( item.getParentItem( )
				.getText( )
				.equals( DRILL_TO_DETAILS_CATEGORY ) )
		{
			PlaceResources.copyDrillThroughReport( composite.getShell( ),
					getDefaultLocation( ),
					item.getText( ) );
		}

		/*
		 * Copy the inluded libraries if selecting sample report demostrate
		 * report library feature
		 */
		if ( item.getParentItem( ).getText( ).equals( "Libraries" ) )
		{
			PlaceResources.copyIncludedLibraries( composite.getShell( ),
					getDefaultLocation( ) );
		}

		/*
		 * Copy the inluded libraries if selecting sample report demostrate
		 * report library feature
		 */
		if ( item.getParentItem( ).getText( ).equals( "XML Data Source" ) )
		{
			PlaceResources.copyIncludedPng( composite.getShell( ),
					getDefaultLocation( ) );
		}
	}

	private String getDefaultLocation( )
	{
		IPath defaultPath = Platform.getLocation( );
		return defaultPath.toOSString( );
	}

	public void handleEvent( Event event )
	{
		if ( event.widget == null || !( event.widget instanceof TreeItem ) )
			setEnabled( false );
		TreeItem item = (TreeItem) event.widget;
		if ( item == null )
			super.setEnabled( false );
		Object selectedElement = item.getData( );
		if ( selectedElement == null )
			super.setEnabled( false );
		else
			super.setEnabled( selectedElement instanceof ReportDesignHandle );
	}
}
