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

package org.eclipse.birt.report.designer.ui.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.preview.parameter.AbstractParamGroup;
import org.eclipse.birt.report.designer.ui.preview.parameter.CascadingGroup;
import org.eclipse.birt.report.designer.ui.preview.parameter.ListingParam;
import org.eclipse.birt.report.designer.ui.preview.parameter.RadioParam;
import org.eclipse.birt.report.designer.ui.preview.parameter.ScalarParam;
import org.eclipse.birt.report.designer.ui.preview.parameter.StaticTextParam;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class InputParameterDialog extends Dialog
{

	private Composite container;
	private List params;

	private Map paramValues = new HashMap( );
	private ScrolledComposite scroller;

	public InputParameterDialog( Shell parentShell, List params, Map paramValues )
	{
		super( parentShell );
		this.params = params;
		if ( paramValues != null )
			this.paramValues.putAll( paramValues );
	}

	protected void buttonPressed( int buttonId )
	{
		// TODO Auto-generated method stub
		super.buttonPressed( buttonId );
	}

	protected Control createDialogArea( Composite parent )
	{
		GridLayout gridLayout = new GridLayout( );
		gridLayout.marginWidth = gridLayout.marginHeight = 5;
		parent.setLayout( gridLayout );

		this.scroller = new ScrolledComposite( parent, SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.BORDER );
		this.scroller.setExpandHorizontal( true );
		this.scroller.setExpandVertical( true );

		scroller.setLayoutData( new GridData( GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL ) );

		createParameters( );

		return super.createDialogArea( parent );
	}

	private void createParameters( )
	{
		if ( this.container != null && !this.container.isDisposed( ) )
			this.container.dispose( );

		this.container = new Composite( this.scroller, SWT.NONE );
		this.scroller.setContent( this.container );

		this.container.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		this.container.setLayout( new GridLayout( ) );

		createParametersSection( params, this.container );

		this.container.setSize( this.container.computeSize( SWT.DEFAULT,
				SWT.DEFAULT ) );
		this.scroller.setMinSize( this.container.computeSize( SWT.DEFAULT,
				SWT.DEFAULT ) );
	}

	private void createParametersSection( List children, Composite parent )
	{
		Iterator iterator = children.iterator( );
		while ( iterator.hasNext( ) )
		{
			Object obj = iterator.next( );
			if ( obj instanceof ScalarParam )
			{
				ScalarParam param = (ScalarParam) obj;
				createParamSection( param, parent );
			}
			else if ( obj instanceof AbstractParamGroup )
			{
				AbstractParamGroup group = (AbstractParamGroup) obj;
				createParametersSection( group.getChildren( ),
						createParamGroupSection( group, parent ) );
			}
		}

	}

	private Composite createParamGroupSection( AbstractParamGroup paramGroup,
			Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( paramGroup.getHandle( ).getDisplayLabel( ) );
		group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		group.setLayout( new GridLayout( ) );
		return group;
	}

	private Composite createParamSection( ScalarParam param, Composite parent )
	{

		Composite container = new Composite( parent, SWT.NONE );
		container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		container.setLayout( layout );
		new Label( container, SWT.NONE ).setText( param.getHandle( )
				.getDisplayLabel( )
				+ ":" );

		if ( param instanceof StaticTextParam )
		{
			final StaticTextParam textParam = (StaticTextParam) param;
			Text input = new Text( container, SWT.BORDER );
			input.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			input.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					Text input = (Text) e.getSource( );
					paramValues.put( textParam.getHandle( ).getName( ),
							input.getText( ) );
				}
			} );

			if ( paramValues.containsKey( textParam.getHandle( ).getName( ) ) )
			{
				input.setText( (String) paramValues.get( textParam.getHandle( )
						.getName( ) ) );
			}

		}
		else if ( param instanceof RadioParam )
		{
			final RadioParam radioParam = (RadioParam) param;
			String value = radioParam.getDefaultValue( );

			if ( paramValues.containsKey( radioParam.getHandle( ).getName( ) ) )
			{
				value = (String) paramValues.get( radioParam.getHandle( )
						.getName( ) );
			}

			List list = radioParam.getValueList( );
			int i = 0;

			for ( Iterator iterator = list.iterator( ); iterator.hasNext( ); )
			{
				if ( i > 0 )
				{
					new Label( container, SWT.NONE );
				}

				IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
				Button button = new Button( container, SWT.RADIO );
				button.setText( choice.getLabel( ) );
				button.setData( choice.getValue( ) );
				if ( choice.getValue( ).equals( value ) )
					button.setSelection( true );
				button.addSelectionListener( new SelectionListener( ) {

					public void widgetDefaultSelected( SelectionEvent e )
					{
						// TODO Auto-generated method stub

					}

					public void widgetSelected( SelectionEvent e )
					{
						Button button = (Button) e.getSource( );
						paramValues.put( radioParam.getHandle( ).getName( ),
								button.getData( ) );
					}
				} );
				i++;
			}

		}
		else if ( param instanceof ListingParam )
		{
			final ListingParam listParam = (ListingParam) param;
			String value = listParam.getDefaultValue( );

			if ( paramValues.containsKey( listParam.getHandle( ).getName( ) ) )
			{
				value = (String) paramValues.get( listParam.getHandle( )
						.getName( ) );
				listParam.setSelectionValue( value );
			}

			Combo combo = new Combo( container, SWT.BORDER );
			combo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			List list = listParam.getValueList( );
			for ( Iterator iterator = list.iterator( ); iterator.hasNext( ); )
			{
				IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
				String label = (String) ( choice.getLabel( ) == null ? choice.getValue( )
						: choice.getLabel( ) );
				if ( label != null )
				{
					combo.add( label );
					combo.setData( label, choice.getValue( ) );
				}
			}

			//			String[] items = (String[]) listParam.getValueList( ).toArray( new String[]{});
			//
			//			for ( int i = 0; i < items.length; i++ )
			//			{
			//				items[i] = listParam.format( items[i] );
			//			}
			//			
			//			combo.setItems( items );
			int count = combo.getItemCount( );
			//			List list = listParam.getValueList( );
			for ( int i = 0; i < count; i++ )
			{
				if ( combo.getData( combo.getItem( i ) ).equals( value ) )
				{
					combo.select( i );
					break;
				}
			}

			combo.addSelectionListener( new SelectionListener( ) {

				public void widgetDefaultSelected( SelectionEvent e )
				{
					// TODO Auto-generated method stub

				}

				public void widgetSelected( SelectionEvent e )
				{
					Combo combo = (Combo) e.getSource( );
					paramValues.put( listParam.getHandle( ).getName( ),
							combo.getData( combo.getItem( combo.getSelectionIndex( ) ) ) );
					if ( listParam.getParentGroup( ) instanceof CascadingGroup )
					{
						CascadingGroup group = (CascadingGroup) listParam.getParentGroup( );
						if ( group.getPostParameter( listParam ) != null )
							try
							{
								createParameters( );
							}
							catch ( RuntimeException e1 )
							{
								// TODO Auto-generated catch block
								e1.printStackTrace( );
							}
					}
				}
			} );

		}
		return container;
	}

	protected void configureShell( Shell newShell )
	{
		super.configureShell( newShell );
		newShell.setText( "Input parameters" );
		newShell.setSize( 400, 400 );
	}

	public Map getParameters( )
	{
		return this.paramValues;
	}

}
