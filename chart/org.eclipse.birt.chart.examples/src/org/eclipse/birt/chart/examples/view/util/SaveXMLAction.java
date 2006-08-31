/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.util;

import java.io.File;
import org.eclipse.birt.chart.examples.view.ChartExamples;
import java.io.FileOutputStream;
import java.io.IOException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

public class SaveXMLAction extends Action
{

	private Composite cmp;

	public SaveXMLAction( Tools tool, Composite parent )
	{
		super( );
		this.cmp = parent;
		String id = tool.group + '.' + tool.name;
		setId( id );

		setText( "Save" ); //$NON-NLS-1$
		setToolTipText( "Save XML Source" ); //$NON-NLS-1$
		setDescription( "Save XML Source to the designated directory" ); //$NON-NLS-1$
	}

	/**
	 * When the action is invoked, pop up a File Dialog to designate the
	 * directory.
	 */
	public void run( )
	{
		Chart cm = ChartExamples.getChartModel( );
		if ( cm != null )
		{
			final FileDialog saveDialog = new FileDialog( cmp.getShell( ),
					SWT.SAVE );
			saveDialog.setFilterExtensions( new String[]{
				"*.chart"} ); //$NON-NLS-1$
			try
			{
				saveDialog.open( );
				String name = saveDialog.getFileName( );
				if ( name != null && name != "" ) //$NON-NLS-1$
				{
					Serializer serializer = null;
					final File file = new File( saveDialog.getFilterPath( ),
							name );
					if ( file.exists( ) )
					{
						MessageBox box = new MessageBox( cmp.getShell( ),
								SWT.ICON_WARNING | SWT.YES | SWT.NO );
						box.setText( "Save XML Source" ); //$NON-NLS-1$
						box.setMessage( "The XML source already exists in the directory. \nDo you want to replace it?" ); //$NON-NLS-1$
						if ( box.open( ) != SWT.YES )
						{
							return;
						}
					}

					serializer = SerializerImpl.instance( );
					try
					{
						serializer.write( cm, new FileOutputStream( file ) );
					}
					catch ( IOException ioe )
					{
						ioe.printStackTrace( );
					}
				}
			}
			catch ( Throwable e )
			{
				e.printStackTrace( );
			}
		}
	}
}
