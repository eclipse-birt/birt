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

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;

public class BackgroundImageCellEditor extends DialogCellEditor
{

	private static final String[] IMAGE_TYPES = new String[]{
			".bmp",
			".jpg",
			".jpeg",
			".jpe",
			".jfif",
			".gif",
			".png",
			".tif",
			".tiff",
			".ico",
			".svg"
	};

	private static final int defaultStyle = SWT.SINGLE;

	public BackgroundImageCellEditor( Composite parent )
	{
		super( parent );
		setStyle( defaultStyle );
	}

	public BackgroundImageCellEditor( Composite parent, int style )
	{
		super( parent, style );
	}

	protected Object openDialogBox( Control cellEditorWindow )
	{
		String extensions[] = new String[]{
			"*.bmp;*.jpg;*.jpeg;*.jpe;*.jfif;*.gif;*.png;*.tif;*.tiff;*.ico;*.svg"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		};
		FileDialog fd = new FileDialog( cellEditorWindow.getShell( ), SWT.OPEN );
		fd.setFilterExtensions( extensions );
		Object value = getValue( );
		if ( value != null )
		{
			fd.setFileName( (String) value );
		}

		String file = fd.open( );
		if ( file != null )
		{
			if ( checkExtensions( IMAGE_TYPES, file ) == false )
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "EmbeddedImagesNodeProvider.FileNameError.Title" ),
						Messages.getString( "EmbeddedImagesNodeProvider.FileNameError.Message" ) );
			}
		}
		return file;
	}

	private boolean checkExtensions( String fileExt[], String fileName )
	{
		for ( int i = 0; i < fileExt.length; i++ )
		{
			String ext = fileExt[i].substring( fileExt[i].lastIndexOf( '.' ) );
			if ( fileName.toLowerCase( ).endsWith( ext.toLowerCase( ) ) )
			{
				return true;
			}
		}
		return false;
	}
}
