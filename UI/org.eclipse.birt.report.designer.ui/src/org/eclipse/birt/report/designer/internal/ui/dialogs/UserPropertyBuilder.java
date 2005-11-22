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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 * 
 */

public class UserPropertyBuilder extends BaseDialog
{

	public static final int USER_PROPERTY = 0;

	public static final int NAMED_EXPRESSION = 1;

	private static final String PROPERTY_TITLE = "New User Property";
	private static final String EXPRESSION_TITLE = "New Named Expression";

	private static final String ERROR_MSG_NAME_IS_REQUIRED = "Name is required.";
	private static final String ERROR_MSG_NAME_DUPLICATED = "The name has been used.";

	private static final String LABEL_NAME = "Property Name:";

	private static final String LABEL_TYPE = "Property Type:";

	private static final String LABEL_DEFAULT_VALUE = "Default Value:";

	private static final Image ERROR_ICON = ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );

	private static PropertyType[] PROPERTY_TYPES;

	private static PropertyType EXPRESSION_TYPE;

	static
	{
		IMetaDataDictionary dictionary = DesignEngine.getMetaDataDictionary( );
		ArrayList typeList = new ArrayList( ( (MetaDataDictionary) DesignEngine.getMetaDataDictionary( ) ).getPropertyTypes( ) );
		EXPRESSION_TYPE = dictionary.getPropertyType( PropertyType.EXPRESSION_TYPE );
		typeList.remove( EXPRESSION_TYPE );
		PROPERTY_TYPES = (PropertyType[]) typeList.toArray( new PropertyType[0] );
	}

	private DesignElementHandle input;

	private int style;

	private Text nameEditor, defaultValueEditor;
	private Combo typeChooser;
	private CLabel messageLine;

	public UserPropertyBuilder( int style )
	{
		super( UIUtil.getDefaultShell( ), "" );
		switch ( this.style = style )
		{
			case USER_PROPERTY :
				setTitle( PROPERTY_TITLE );
				break;
			case NAMED_EXPRESSION :
				setTitle( EXPRESSION_TITLE );
				break;
		}
	}

	protected boolean initDialog( )
	{
		switch ( style )
		{
			case USER_PROPERTY :
				typeChooser.setText( new UserPropertyDefn( ).getType( )
						.getDisplayName( ) );
				break;
			case NAMED_EXPRESSION :
		}
		checkName( );
		return super.initDialog( );
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		GridLayout layout = new GridLayout( 2, false );
		layout.marginHeight = layout.marginWidth = 10;
		composite.setLayout( layout );

		new Label( composite, SWT.NONE ).setText( LABEL_NAME );
		nameEditor = new Text( composite, SWT.BORDER | SWT.SINGLE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = 200;
		nameEditor.setLayoutData( gd );
		nameEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkName( );
			}
		} );

		messageLine = new CLabel( composite, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		messageLine.setLayoutData( gd );

		switch ( style )
		{
			case USER_PROPERTY :
				new Label( composite, SWT.NONE ).setText( LABEL_TYPE );
				typeChooser = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
				typeChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				for ( int i = 0; i < PROPERTY_TYPES.length; i++ )
				{
					typeChooser.add( PROPERTY_TYPES[i].getDisplayName( ), i );
				}
				break;
			case NAMED_EXPRESSION :
				new Label( composite, SWT.NONE ).setText( LABEL_DEFAULT_VALUE );
				Composite subComposite = new Composite( composite, SWT.NONE );
				subComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				subComposite.setLayout( UIUtil.createGridLayoutWithoutMargin( 2,
						false ) );

				defaultValueEditor = new Text( subComposite, SWT.BORDER
						| SWT.SINGLE );
				defaultValueEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				Button button = new Button( subComposite, SWT.PUSH );
				button.setText( "..." );
				button.setLayoutData( new GridData( ) );

				button.addSelectionListener( new SelectionAdapter( ) {

					public void widgetSelected( SelectionEvent e )
					{
						ExpressionBuilder builder = new ExpressionBuilder( UIUtil.getDefaultShell( ),
								defaultValueEditor.getText( ) );
						builder.setExpressionProvier( new ExpressionProvider( input.getModuleHandle( ),
								DEUtil.getDataSetList( input ) ) );

						if ( builder.open( ) == OK )
						{
							defaultValueEditor.setText( UIUtil.convertToGUIString( builder.getResult( ) ) );
						}
					}

				} );

		}
		return composite;
	}

	protected void okPressed( )
	{
		UserPropertyDefn def = new UserPropertyDefn( );
		def.setName( nameEditor.getText( ).trim( ) );
		switch ( style )
		{
			case USER_PROPERTY :
				def.setType( PROPERTY_TYPES[typeChooser.getSelectionIndex( )] );
				break;
			case NAMED_EXPRESSION :
				def.setType( EXPRESSION_TYPE );				
				break;
		}
		setResult( def );
		super.okPressed( );
	}

	public void setInput( DesignElementHandle handle )
	{
		input = handle;
	}

	private void checkName( )
	{
		String errorMessage = null;
		String name = nameEditor.getText( ).trim( );
		if ( name.length( ) == 0 )
		{
			errorMessage = ERROR_MSG_NAME_IS_REQUIRED;
		}
		else if ( input.getPropertyHandle( name ) != null )
		{
			errorMessage = ERROR_MSG_NAME_DUPLICATED;
		}
		if ( errorMessage != null )
		{
			messageLine.setText( errorMessage );
			messageLine.setImage( ERROR_ICON );
		}
		else
		{
			messageLine.setText( "" );
			messageLine.setImage( null );
		}
		getOkButton( ).setEnabled( errorMessage == null );
	}
}
