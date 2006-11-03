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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IResourceKeyDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * The descriptor to control resource keys
 */
public class ResourceKeyDescriptor extends PropertyDescriptor
{

	private Text text;

	private Button btnBrowse, btnReset;

	private String oldValue;

	public ResourceKeyDescriptor( boolean isFormStyle )
	{
		setFormStyle( isFormStyle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PropertyDescriptor#resetUIData()
	 */
	public void load( )
	{
		if ( !provider.isEnable( ) )
		{
			text.setEnabled( false );
			btnBrowse.setEnabled( false );
			btnReset.setEnabled( false );
			return;
		}
		else
		{
			text.setEnabled( true );
			btnBrowse.setEnabled( true );
			btnReset.setEnabled( true );
		}
		oldValue = getDescriptorProvider( ).load( ).toString( );

		String baseName = provider.getBaseName( );
		if ( baseName == null )
		{
			btnBrowse.setEnabled( false );
		}
		else
		{
			URL resource = provider.getResourceURL( );
			String path = null;
			try
			{
				if ( resource != null )
				{
					path = FileLocator.resolve( resource ).getPath( );
				}

			}
			catch ( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace( );
			}
			if ( resource == null || path == null ||!new File( path ).exists( ) )
			{
				btnBrowse.setEnabled( false );
			}
			else
			{
				btnBrowse.setEnabled( true );
			}
		}
		text.setEnabled( baseName != null );

		text.setText( DEUtil.resolveNull( oldValue ) );
	}

	public Control getControl( )
	{
		return innerParent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl( Composite parent )
	{
		innerParent = new Composite( parent, 0 );
		innerParent.setLayout( new GridLayout( 3, false ) );
		if ( isFormStyle( ) )
			text = FormWidgetFactory.getInstance( ).createText( innerParent,
					"",
					SWT.READ_ONLY );
		else
			text = new Text( innerParent, SWT.BORDER | SWT.READ_ONLY );
		text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		btnBrowse = FormWidgetFactory.getInstance( ).createButton( innerParent,
				SWT.PUSH,
				isFormStyle( ) );
		btnBrowse.setText( provider.getBrowserText( ) ); //$NON-NLS-1$

		btnBrowse.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleBrowserSelectedEvent( );
			}
		} );

		btnReset = FormWidgetFactory.getInstance( ).createButton( innerParent,
				SWT.PUSH,
				isFormStyle( ) );
		btnReset.setText( provider.getResetText( ) ); //$NON-NLS-1$

		btnReset.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleSelectedEvent( null );
			};

		} );

		return innerParent;
	}

	private void handleSelectedEvent( String newValue )
	{
		if ( "".equals( newValue ) )//$NON-NLS-1$
		{
			newValue = null;
		}

		try
		{
			save( newValue );
			text.setText( DEUtil.resolveNull( newValue ) );
		}
		catch ( SemanticException e )
		{
			text.setText( DEUtil.resolveNull( oldValue ) );
			WidgetUtil.processError( text.getShell( ), e );
		}

	}

	public void save( Object obj ) throws SemanticException
	{
		getDescriptorProvider( ).save( obj );

	}

	protected void handleBrowserSelectedEvent( )
	{
		ResourceEditDialog dlg = new ResourceEditDialog( btnBrowse.getShell( ),
				Messages.getString( "ResourceKeyDescriptor.title.SelectKey" ) ); //$NON-NLS-1$

		dlg.setResourceURL( provider.getResourceURL( ) );

		if ( dlg.open( ) == Window.OK )
		{
			handleSelectedEvent( (String) dlg.getResult( ) );
		}
	}

	IResourceKeyDescriptorProvider provider;

	private Composite innerParent;

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof IResourceKeyDescriptorProvider )
			this.provider = (IResourceKeyDescriptorProvider) provider;
	}

	public String getStringValue( )
	{
		return text.getText( );
	}

	public void setStringValue( String value )
	{
		text.setText( value );
	}

	public void setHidden( boolean isHidden )
	{
		WidgetUtil.setExcludeGridData( text, isHidden );
	}

	public void setVisible( boolean isVisible )
	{
		text.setVisible( isVisible );
	}

	public void setInput( Object input )
	{
		super.setInput( input );
		getDescriptorProvider( ).setInput( input );
	};

}