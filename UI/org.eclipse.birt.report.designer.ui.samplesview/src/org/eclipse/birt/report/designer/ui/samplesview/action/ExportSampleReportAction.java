/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.samplesview.action;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.samplesview.util.PlaceResources;
import org.eclipse.birt.report.designer.ui.samplesview.view.ReportExamples;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TreeItem;

public class ExportSampleReportAction extends Action
{

	private static final String[] REPORTDESIGN_FILENAME_PATTERN = new String[]{
		"*.rptdesign" //$NON-NLS-1$
	};

	private static final String ACTION_TEXT = Messages.getString( "SampleReportsView.Action.exportSampleReport" );

	private ReportExamples composite;

	public ExportSampleReportAction( ReportExamples composite )
	{
		super( ACTION_TEXT );
		setToolTipText( Messages.getString( "SampleReportsView.Action.exportSampleReport.toolTipText" ) );
		this.composite = composite;
	}

	public void run( )
	{
		Object selectedElement = ( (TreeItem) composite.getSelectedElement( ) ).getData( );
		if ( selectedElement == null
				|| !( selectedElement instanceof ReportDesignHandle ) )
		{
			return;
		}

		String filename = ( (ReportDesignHandle) selectedElement ).getFileName( );
		String reportName = filename.substring( filename.lastIndexOf( "/" ) + 1 );
		final FileDialog saveDialog = new FileDialog( composite.getShell( ),
				SWT.SAVE );
		saveDialog.setFilterExtensions( REPORTDESIGN_FILENAME_PATTERN ); //$NON-NLS-1$
		saveDialog.setFileName( reportName );
		if ( saveDialog.open( ) == null )
			return;

		PlaceResources.copy( composite.getShell( ),
				saveDialog.getFilterPath( ),
				saveDialog.getFileName( ),
				filename );
	}
}
