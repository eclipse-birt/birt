/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.PaletteImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MarkerIconDialog implements SelectionListener
{

	private transient Button btnURL, btnLocal;

	private transient Button btnPreview;

	private transient Button btnRemove;

	private transient Button btnOK;

	private transient Button btnCancel;

	private Composite inputArea;

	private IconCanvas previewCanvas;

	private transient Button btnBrowse;

	final private static int URI_TYPE = 0;

	final private static int LIST_TYPE = 1;

	private int selectedType = -1;

	private Text uriEditor;

	private List iconList;

	private transient Shell shell;

	private transient boolean applyMarkerIcon = false;

	private transient URL url;

	private transient Palette iconPalette;

	/**
	 * @param parent
	 */
	public MarkerIconDialog( Shell parent, Palette iconPalette )
	{
		super( );
		this.iconPalette = iconPalette;

		shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		shell.setLayout( new GridLayout( ) );
		shell.setSize( 600, 420 );
		UIHelper.centerOnScreen( shell );
		createContents( shell );
		shell.setText( Messages.getString( "MarkerIconDialog.Title.MarkerIconSelector" ) ); //$NON-NLS-1$
		shell.layout( );
		shell.open( );

		while ( !shell.isDisposed( ) )
		{
			if ( !shell.getDisplay( ).readAndDispatch( ) )
			{
				shell.getDisplay( ).sleep( );
			}
		}
	}

	protected void createContents( Shell shell )
	{
		Composite cmpContent = new Composite( shell, SWT.NONE );
		cmpContent.setLayout( new GridLayout( ) );

		createSelectionArea( cmpContent );

		new Label( cmpContent, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite composite = new Composite( cmpContent, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );

		createListArea( composite );
		createPreviewArea( composite );

		new Label( cmpContent, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite btnComposite = new Composite( cmpContent, SWT.NONE );
		btnComposite.setLayout( new GridLayout( 2, false ) );
		btnComposite.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		btnOK = new Button( btnComposite, SWT.NONE );
		btnOK.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
		btnOK.setText( Messages.getString( "Shared.Lbl.OK" ) ); //$NON-NLS-1$
		btnOK.addSelectionListener( this );

		btnCancel = new Button( btnComposite, SWT.NONE );
		btnCancel.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		btnCancel.setText( Messages.getString( "Shared.Lbl.Cancel" ) ); //$NON-NLS-1$
		btnCancel.addSelectionListener( this );
	}

	private void createSelectionArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );

		Label label = new Label( composite, SWT.NONE );
		label.setText( Messages.getString( "MarkerIconDialog.Lbl.SelectIconFrom" ) ); //$NON-NLS-1$
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );

		Composite selectionArea = new Composite( composite, SWT.NONE );
		selectionArea.setLayout( new FillLayout( SWT.VERTICAL ) );

		btnURL = new Button( selectionArea, SWT.RADIO );
		btnURL.setText( Messages.getString( "MarkerIconDialog.Lbl.URL" ) ); //$NON-NLS-1$
		btnURL.addSelectionListener( this );

		btnLocal = new Button( selectionArea, SWT.RADIO );
		btnLocal.setText( Messages.getString( "MarkerIconDialog.Lbl.Local" ) ); //$NON-NLS-1$
		btnLocal.addSelectionListener( this );
	}

	private void createListArea( Composite parent )
	{
		Composite listArea = new Composite( parent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_BOTH
				| GridData.HORIZONTAL_ALIGN_BEGINNING );
		gd.widthHint = 300;
		gd.heightHint = 260;
		listArea.setLayoutData( gd );
		listArea.setLayout( new GridLayout( ) );

		GridLayout gl = new GridLayout( );
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		inputArea = new Composite( listArea, SWT.NONE );
		GridData gdInputArea = new GridData( GridData.FILL_BOTH
				| GridData.HORIZONTAL_ALIGN_BEGINNING );
		inputArea.setLayoutData( gdInputArea );
		inputArea.setLayout( gl );

		iconList = new List( listArea, SWT.NONE
				| SWT.SINGLE
				| SWT.BORDER
				| SWT.V_SCROLL
				| SWT.H_SCROLL );
		iconList.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		iconList.addSelectionListener( this );

		if ( iconPalette != null )
		{
			for ( int i = 0; i < iconPalette.getEntries( ).size( ); i++ )
			{
				String path = ( (Image) iconPalette.getEntries( ).get( i ) ).getURL( );
				if ( path.charAt( 0 ) == 'f' )
				{
					path = path.substring( 6, path.length( ) );
				}
				iconList.add( path );
			}
		}

//		btnRemove = new Button( listArea, SWT.NONE );
//		btnRemove.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
//		btnRemove.setText( Messages.getString( "MarkerIconDialog.Lbl.Remove" ) ); //$NON-NLS-1$
//		btnRemove.addSelectionListener( this );
	}

	private void createPreviewArea( Composite composite )
	{
		Composite previewArea = new Composite( composite, SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 250;
		gd.heightHint = 260;
		previewArea.setLayoutData( gd );
		previewArea.setLayout( new FillLayout( ) );

		previewCanvas = new IconCanvas( previewArea );
	}

	private void switchTo( int type )
	{
		if ( type == selectedType )
		{
			return;
		}
		selectedType = type;
		Control[] controls = inputArea.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			controls[i].dispose( );
		}

		clearPreview( );
		switch ( type )
		{
			case URI_TYPE :
				swtichToExprType( );
				break;
			case LIST_TYPE :
				swtichToListType( );
				break;
		}
		inputArea.layout( );
	}

	private void clearPreview( )
	{
		previewCanvas.clear( );
	}

	private void swtichToExprType( )
	{
		Label title = new Label( inputArea, SWT.NONE );
		title.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		title.setText( Messages.getString( "MarkerIconDialog.Lbl.EnterURL" ) ); //$NON-NLS-1$

		uriEditor = new Text( inputArea, SWT.SINGLE | SWT.BORDER );
		uriEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite innerComp = new Composite( inputArea, SWT.NONE );
		innerComp.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		GridLayout gl = new GridLayout( );
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.verticalSpacing = 2;
		innerComp.setLayout( gl );

		btnPreview = new Button( innerComp, SWT.PUSH );
		btnPreview.setText( Messages.getString( "MarkerIconDialog.Lbl.Preview" ) ); //$NON-NLS-1$
		btnPreview.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		btnPreview.addSelectionListener( this );
	}

	private void swtichToListType( )
	{
		Composite buttonBar = new Composite( inputArea, SWT.NONE );

		GridLayout gl = new GridLayout( );
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		buttonBar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		buttonBar.setLayout( gl );

		Label description = new Label( buttonBar, SWT.NONE );
		description.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
		description.setText( Messages.getString( "MarkerIconDialog.Lbl.Description" ) ); //$NON-NLS-1$

		btnBrowse = new Button( buttonBar, SWT.PUSH );
		btnBrowse.setText( Messages.getString( "MarkerIconDialog.Lbl.Browse" ) ); //$NON-NLS-1$
		GridData gd = new GridData( GridData.HORIZONTAL_ALIGN_END );
		gd.grabExcessHorizontalSpace = true;
		btnBrowse.setLayoutData( gd );
		btnBrowse.addSelectionListener( this );
	}

	private void preview( String fullPath )
	{
		if ( iconList.getSelectionCount( ) > 0 )
		{
			previewCanvas.loadImage( fullPath );
		}
		else
		{
			clearPreview( );
		}
	}

	private void preview( URL uri )
	{
		try
		{
			previewCanvas.loadImage( uri );
		}
		catch ( Exception e )
		{
			clearPreview( );
		}
	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.widget.equals( btnOK ) )
		{
			applyMarkerIcon = true;
			if ( iconPalette == null )
			{
				iconPalette = PaletteImpl.create( ImageImpl.create( getImagePath( ) ) );
			}
			else
			{
				iconPalette.getEntries( )
						.add( ImageImpl.create( getImagePath( ) ) );
			}

			shell.dispose( );
		}
		else if ( e.widget.equals( btnCancel ) )
		{
			shell.dispose( );
		}
		else if ( e.widget.equals( btnURL ) )
		{
			switchTo( URI_TYPE );
		}
		else if ( e.widget.equals( btnLocal ) )
		{
			switchTo( LIST_TYPE );
		}
		else if ( e.widget.equals( btnPreview ) )
		{
			uriEditor.setText( uriEditor.getText( ).trim( ) );
			try
			{
				url = new URL( uriEditor.getText( ) );
			}
			catch ( MalformedURLException ex )
			{
				ex.printStackTrace( );
			}
			preview( url );
		}
		else if ( e.widget.equals( iconList ) )
		{
			String path = iconList.getSelection( )[0];
			if ( iconList.getSelection( )[0].startsWith( "h" ) //$NON-NLS-1$
					|| iconList.getSelection( )[0].startsWith( "H" ) ) //$NON-NLS-1$
			{
				path = path.replace( '\"', '/' ); //$NON-NLS-1$
				try
				{
					url = new URL( path );
				}
				catch ( MalformedURLException ex )
				{
					ex.printStackTrace( );
				}
				preview( url );
			}
			else
			{
				preview( path );
			}
		}
		else if ( e.widget.equals( btnBrowse ) )
		{
			FileDialog fileChooser = new FileDialog( shell, SWT.OPEN );
			fileChooser.setText( Messages.getString( "MarkerIconDialog.Chooser.Title" ) ); //$NON-NLS-1$
			fileChooser.setFilterExtensions( new String[]{
					"*.gif", "*.jpg", "*.png", "*.ico", "*.bmp" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			} );
			try
			{
				String fullPath = fileChooser.open( );
				if ( fullPath != null )
				{

					if ( iconList.indexOf( fullPath ) != -1 )
					{
						return;
					}
					previewCanvas.loadImage( fullPath );
					iconList.add( fullPath );
					iconList.select( iconList.indexOf( fullPath ) );
				}
			}
			catch ( Throwable ex )
			{
				ex.printStackTrace( );
			}
		}
		else if ( e.widget.equals( btnRemove ) )
		{
			if ( iconList.getSelectionCount() != 0 )
			{
				iconPalette.getEntries( ).remove( iconList.getSelectionIndex( ) );
			}
		}
	}

	private String getImagePath( )
	{
		String path = null;
		if ( selectedType == URI_TYPE )
		{
			path = uriEditor.getText( );
		}
		else if ( selectedType == LIST_TYPE )
		{
			path = "file:\\" + iconList.getSelection( )[0]; //$NON-NLS-1$
		}
		return path;
	}

	public void widgetDefaultSelected( SelectionEvent event )
	{

	}

	public boolean applyMarkerIcon( )
	{
		return applyMarkerIcon;
	}

	public Palette getIconPalette( )
	{
		return iconPalette;
	}
}
