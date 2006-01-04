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

import java.net.URL;

import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.PaletteImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

/**
 * MarkerIconDialog is invoked when the user chooses "icon" from Marker Type
 * Combo box.
 */
public class MarkerIconDialog implements SelectionListener, ModifyListener
{

	private transient Button btnURL;

	private transient Button btnLocal;

	private transient Button btnAdd;

	private transient Button btnPreview;

	private transient Button btnRemove;

	private transient Button btnOK;

	private transient Button btnCancel;

	private Composite inputArea;

	private IconCanvas previewCanvas;

	private transient Button btnBrowse;

	final private static int URI_TYPE = 0;

	final private static int LOCAL_TYPE = 1;

	private int selectedType = -1;

	private Text uriEditor;

	private List iconList;

	private transient Shell shell;

	private transient boolean applyMarkerIcon = false;

	private transient Palette iconPalette;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            shell of LineSeriesAttributeComposite
	 * @param iconPalette
	 *            retrieved from LineSeries
	 */
	public MarkerIconDialog( Shell parent, Palette iconPalette )
	{
		super( );
		this.iconPalette = iconPalette;

		shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		shell.setLayout( new GridLayout( ) );
		shell.setSize( 600, 420 ); // Fixed dialog size, cannot be resized.
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

	/**
	 * Place dialog composite.
	 * 
	 * @param shell
	 *            shell of MarkerIconDialog
	 */
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
		btnOK.setEnabled( iconList.getSelectionCount( ) > 0 );

		btnCancel = new Button( btnComposite, SWT.NONE );
		btnCancel.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		btnCancel.setText( Messages.getString( "Shared.Lbl.Cancel" ) ); //$NON-NLS-1$
		btnCancel.addSelectionListener( this );
	}

	/**
	 * Selection Area locates in the top of the dialog.
	 * 
	 * @param parent
	 *            dialog composite
	 */
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

	/**
	 * List Area locates in the left middle of the dialog.
	 * 
	 * @param parent
	 *            dialog composite
	 */
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

		// Input Area is various depending on the selection (URI or Local).
		inputArea = new Composite( listArea, SWT.NONE );
		GridData gdInputArea = new GridData( GridData.FILL_BOTH
				| GridData.HORIZONTAL_ALIGN_BEGINNING );
		inputArea.setLayoutData( gdInputArea );
		inputArea.setLayout( gl );

		iconList = new List( listArea, SWT.SINGLE
				| SWT.BORDER
				| SWT.V_SCROLL
				| SWT.H_SCROLL );
		GridData gdIconList = new GridData( GridData.FILL_HORIZONTAL );
		gdIconList.heightHint = 120;
		iconList.setLayoutData( gdIconList );
		iconList.addSelectionListener( this );

		if ( iconPalette != null )
		{
			for ( int i = 0; i < iconPalette.getEntries( ).size( ); i++ )
			{
				iconList.add( ( (Image) iconPalette.getEntries( ).get( i ) ).getURL( ) );
			}
		}

		btnRemove = new Button( listArea, SWT.NONE );
		btnRemove.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		btnRemove.setText( Messages.getString( "MarkerIconDialog.Lbl.Remove" ) ); //$NON-NLS-1$
		btnRemove.addSelectionListener( this );
	}

	/**
	 * Preview Area locates in the right middle of the dialog.
	 * 
	 * @param composite
	 *            dialog composite
	 */
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

	/**
	 * Switch in the Selection Area (URI or Local).
	 * 
	 * @param type
	 *            0: URI_TYPE; 1: LOCAL_TYPE
	 */
	private void switchTo( int type )
	{
		if ( type == selectedType )
		{
			// If the selected type is same with the current type,
			// Do nothing.
			return;
		}

		// Clear the current Input Area contents.
		selectedType = type;
		Control[] controls = inputArea.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			controls[i].dispose( );
		}

		// Rearrange the layout and contents of Input Area.
		switch ( type )
		{
			case URI_TYPE :
				swtichToURIType( );
				break;
			case LOCAL_TYPE :
				swtichToListType( );
				break;
		}
		inputArea.layout( );
	}

	private void swtichToURIType( )
	{
		Label title = new Label( inputArea, SWT.NONE );
		title.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		title.setText( Messages.getString( "MarkerIconDialog.Lbl.EnterURL" ) ); //$NON-NLS-1$

		uriEditor = new Text( inputArea, SWT.SINGLE | SWT.BORDER );
		uriEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		uriEditor.addModifyListener( this );

		Composite innerComp = new Composite( inputArea, SWT.NONE );
		innerComp.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		GridLayout gl = new GridLayout( 2, false );
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.verticalSpacing = 2;
		innerComp.setLayout( gl );

		btnAdd = new Button( innerComp, SWT.PUSH );
		btnAdd.setText( Messages.getString( "MarkerIconDialog.Lbl.Add" ) ); //$NON-NLS-1$
		btnAdd.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		btnAdd.setEnabled( false );
		btnAdd.addSelectionListener( this );

		btnPreview = new Button( innerComp, SWT.PUSH );
		btnPreview.setText( Messages.getString( "MarkerIconDialog.Lbl.Preview" ) ); //$NON-NLS-1$
		btnPreview.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		btnPreview.setEnabled( false );
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

	private void updateButton( )
	{
		btnOK.setEnabled( iconList.getSelectionCount( ) != 0 );
	}

	/**
	 * Preview the image when it is a local image file.
	 * 
	 * @param fullPath
	 *            Image absolute path without "file:///"
	 */
	private void preview( String uri )
	{
		try
		{
			previewCanvas.loadImage( new URL( uri ) );
		}
		catch ( Exception e )
		{
			WizardBase.displayException( e );
		}
	}

	/**
	 * If there is no palette associated with the marker, create a new palette.
	 * Otherwise, add the icon into the palette.
	 * 
	 */
	private void addIconToPalette( )
	{
		if ( iconPalette == null )
		{
			iconPalette = PaletteImpl.create( ImageImpl.create( iconList.getSelection( )[0] ) );
		}
		else
		{
			iconPalette.getEntries( )
					.add( ImageImpl.create( iconList.getSelection( )[0] ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.widget.equals( btnOK ) )
		{
			applyMarkerIcon = true;
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
			switchTo( LOCAL_TYPE );
		}
		else if ( e.widget.equals( btnAdd ) )
		{
			uriEditor.setText( uriEditor.getText( ).trim( ) );
			String path = uriEditor.getText( );
			iconList.add( path );
			iconList.select( iconList.indexOf( path ) );
			addIconToPalette( );
			updateButton( );
		}
		else if ( e.widget.equals( btnPreview ) )
		{
			uriEditor.setText( uriEditor.getText( ).trim( ) );
			String path = uriEditor.getText( );
			preview( path );
		}
		else if ( e.widget.equals( iconList ) )
		{
			String path = iconList.getSelection( )[0];
			preview( path );
			updateButton( );
		}
		else if ( e.widget.equals( btnBrowse ) )
		{
			FileDialog fileChooser = new FileDialog( shell, SWT.OPEN );
			fileChooser.setText( Messages.getString( "MarkerIconDialog.Chooser.Title" ) ); //$NON-NLS-1$
			fileChooser.setFilterExtensions( new String[]{
					"*.gif", "*.jpg", "*.png" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} );

			try
			{
				String path = fileChooser.open( );
				if ( path != null )
				{
					if ( iconList.indexOf( path ) != -1 )
					{
						return;
					}
					path = new StringBuffer( "file:///" ).append( path ).toString( ); //$NON-NLS-1$
					preview( path );
					iconList.add( path );
					iconList.select( iconList.indexOf( path ) );
					addIconToPalette( );
				}
			}
			catch ( Throwable ex )
			{
				ex.printStackTrace( );
			}
			updateButton( );
		}
		else if ( e.widget.equals( btnRemove ) )
		{
			if ( iconList.getSelectionCount( ) != 0 )
			{
				iconPalette.getEntries( )
						.remove( iconList.getSelectionIndex( ) );
				previewCanvas.clear( );
				iconList.remove( iconList.getSelectionIndex( ) );
			}
			updateButton( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent event )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		if ( e.widget.equals( uriEditor ) )
		{
			btnAdd.setEnabled( trimString( uriEditor.getText( ) ) != null );
			btnPreview.setEnabled( trimString( uriEditor.getText( ) ) != null );
		}
	}

	/**
	 * Trim a string. Removes leading and trailing blanks. If the resulting
	 * string is empty, normalizes the string to an null string.
	 * 
	 * @param value
	 *            the string to trim
	 * @return the trimmed string, or null if the string is empty
	 */

	private static String trimString( String value )
	{
		if ( value == null )
			return null;
		value = value.trim( );
		if ( value.length( ) == 0 )
			return null;
		return value;
	}

	/**
	 * 
	 * @return If the user clicks "OK", returns true. Otherwise, returns false.
	 */
	public boolean applyMarkerIcon( )
	{
		return applyMarkerIcon;
	}

	/**
	 * 
	 * @return Returns an icon palette to Line series.
	 */
	public Palette getIconPalette( )
	{
		return iconPalette;
	}
}
