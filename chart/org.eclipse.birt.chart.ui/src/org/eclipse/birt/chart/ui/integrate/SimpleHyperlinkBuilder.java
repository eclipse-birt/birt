/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.integrate;

import java.io.IOException;

import org.eclipse.birt.chart.integrate.SimpleActionHandle;
import org.eclipse.birt.chart.integrate.SimpleActionUtil;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class SimpleHyperlinkBuilder extends TrayDialog
{

	private Button noneRadio, uriRadio;
	private static final String RADIO_NONE = Messages.getString( "SimpleHyperlinkBuilder.Label.RadioNone" ); //$NON-NLS-1$
	private static final String RADIO_URI = Messages.getString( "SimpleHyperlinkBuilder.Label.RadioURI" ); //$NON-NLS-1$

	private static final String TITLE = Messages.getString( "SimpleHyperlinkBuilder.Title" ); //$NON-NLS-1$
	private static final String LABEL_SELECT_TYPE = Messages.getString( "SimpleHyperlinkBuilder.Label.SelectType" ); //$NON-NLS-1$
	private static final String LABEL_LOCATION = Messages.getString( "SimpleHyperlinkBuilder.Label.Location" ); //$NON-NLS-1$
	private static final String LABEL_TARGET = Messages.getString( "SimpleHyperlinkBuilder.Label.Target" ); //$NON-NLS-1$
	private static final String REQUIED_MARK = "*"; //$NON-NLS-1$

	private Composite displayArea;
	private Text locationEditor;
	private Combo targetChooser;

	private SimpleActionHandle inputHandle;

	protected SimpleHyperlinkBuilder( Shell shell )
	{
		super( shell );
		setHelpAvailable( false );
	}

	/**
	 * Returns the serialized result action.
	 * 
	 * @return the serialized result action
	 * @throws IOException
	 */
	public String getResultString( ) throws IOException
	{
		return SimpleActionUtil.serializeAction( inputHandle );
	}

	/**
	 * Set the action to edit with a serialized string
	 * 
	 * @param input
	 *            the serialized string
	 * @param handle
	 *            DesignElementHandle
	 */
	public void setInputString( String input )
	{
		setInput( SimpleActionUtil.deserializeAction( input ) );
	}

	public String getTitle( )
	{
		return getShell( ).getText( );
	}

	public void setTitle( String title )
	{
		getShell( ).setText( title );
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		setTitle( TITLE );
		createSelectionArea( composite );
		new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		displayArea = new Composite( composite, SWT.NONE );

		displayArea.setLayoutData( new GridData( 400, 300 ) );

		new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		if ( inputHandle.getTargetWindow( ).length( ) > 0 )
		{
			uriRadio.setSelection( true );
			uriRadio.notifyListeners( SWT.Selection, new Event( ) );
		}
		
		return composite;
	}

	private void createSelectionArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		composite.setLayout( new GridLayout( 2, false ) );

		new Label( composite, SWT.NONE ).setText( LABEL_SELECT_TYPE );

		noneRadio = new Button( composite, SWT.RADIO );
		noneRadio.setText( RADIO_NONE );
		noneRadio.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event event )
			{
				clearArea( );
			}
		} );

		new Label( composite, SWT.NONE );

		uriRadio = new Button( composite, SWT.RADIO );
		uriRadio.setText( RADIO_URI );
		uriRadio.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event event )
			{
				clearArea( );
				displayArea.setLayout( new GridLayout( 2, false ) );

				new Label( displayArea, SWT.NONE ).setText( REQUIED_MARK
						+ LABEL_LOCATION );
				locationEditor = new Text( displayArea, SWT.BORDER | SWT.SINGLE );
				locationEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				locationEditor.setText( inputHandle.getURI( ) );

				createTargetBar( );

				displayArea.layout( );
			}
		} );

	}

	private void createTargetBar( )
	{
		new Label( displayArea, SWT.NONE ).setText( LABEL_TARGET );
		targetChooser = new Combo( displayArea, SWT.READ_ONLY | SWT.BORDER );
		targetChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		targetChooser.setItems( new String[]{
				"_blank", "_parent", "_self", "_top" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		if ( inputHandle.getTargetWindow( ).length( ) > 0 )
		{
			targetChooser.setText( inputHandle.getTargetWindow( ) );
		}
		else
		{
			targetChooser.select( 0 );
		}
	}

	private void clearArea( )
	{
		Control[] controls = displayArea.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			controls[i].dispose( );
		}
	}

	/**
	 * Set the action to edit.
	 * 
	 * @param input
	 *            the action to edit.
	 */
	public void setInput( SimpleActionHandle input )
	{
		inputHandle = input;
	}

	protected void okPressed( )
	{
		if ( noneRadio.getSelection( ) )
		{
			inputHandle = null;
		}
		else
		{
			inputHandle.setURI( locationEditor.getText( ).trim( ) );
			inputHandle.setTargetWindow( targetChooser.getText( ) );
		}
		super.okPressed( );
	}
}
