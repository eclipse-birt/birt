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
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

public class BackgroundImageCellEditor extends DialogCellEditor
{

	private static final String[] IMAGE_TYPES = new String[]{
			".bmp", //$NON-NLS-1$
			".jpg", //$NON-NLS-1$
			".jpeg", //$NON-NLS-1$
			".jpe", //$NON-NLS-1$
			".jfif", //$NON-NLS-1$
			".gif", //$NON-NLS-1$
			".png", //$NON-NLS-1$
			".tif", //$NON-NLS-1$
			".tiff", //$NON-NLS-1$
			".ico", //$NON-NLS-1$
			".svg" //$NON-NLS-1$
	};

	private static final int defaultStyle = SWT.SINGLE;

	private Text text;

	public BackgroundImageCellEditor( Composite parent )
	{
		super( parent );
		setStyle( defaultStyle );
	}

	public BackgroundImageCellEditor( Composite parent, int style )
	{
		super( parent, style );
	}

	protected Control createContents( Composite cell )
	{

		Color bg = cell.getBackground( );
		Composite composite = new Composite( cell, getStyle( ) );
		composite.setBackground( bg );
		composite.setLayout( new FillLayout( ) );

		text = new Text( composite, SWT.NONE );
		text.setBackground( bg );
		text.setFont( cell.getFont( ) );

		text.addKeyListener( new KeyAdapter( ) {

			// hook key pressed - see PR 14201
			public void keyPressed( KeyEvent e )
			{
				if ( e.character == '\u001b' )
				{ // Escape character
					fireCancelEditor( );
				}
				else if ( e.character == '\r' )
				{ // Return key
					doSetValue( text.getText( ) );
					fireApplyEditorValue( );
					deactivate( );
				}
			}
		} );

		text.addTraverseListener( new TraverseListener( ) {

			public void keyTraversed( TraverseEvent e )
			{
				if ( e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN )
				{
					e.doit = false;
				}
			}
		} );

		text.addFocusListener( new FocusAdapter( ) {

			public void focusLost( FocusEvent e )
			{
				doSetValue( text.getText( ) );
				BackgroundImageCellEditor.this.focusLost( );
			}
		} );

		text.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				markDirty( );
			}

		} );

		return composite;
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
				ExceptionHandler.openErrorMessageBox( Messages.getString( "EmbeddedImagesNodeProvider.FileNameError.Title" ), //$NON-NLS-1$
						Messages.getString( "EmbeddedImagesNodeProvider.FileNameError.Message" ) ); //$NON-NLS-1$
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

	protected void updateContents( Object value )
	{
		if ( text == null )
			return;
		if ( value != null )
			text.setText( value.toString( ) );
	}

	protected void doSetFocus( )
	{
		text.setFocus( );
	}

}
