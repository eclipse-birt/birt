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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 * 
 */
public class ExtendedPropertyEditorComposite extends Composite
		implements
			SelectionListener
{

	private transient LinkedHashMap propMap = null;

	private transient Table table = null;

	private transient TableColumn tcKey = null;

	private transient TableColumn tcValue = null;

	private transient TableEditor editorValue = null;

	private transient Text txtNewKey = null;

	private transient Button btnAdd = null;

	private transient Button btnRemove = null;

	private transient Composite cmpDlgButtons = null;

	private transient Button btnAccept = null;

	private transient Button btnCancel = null;

	private transient Color color;

	private transient Chart chart;

	public ExtendedPropertyEditorComposite( Composite parent, int style,
			Chart chart )
	{
		super( parent, style );
		this.chart = chart;
		init( );
		placeComponents( );
		color = new Color( Display.getCurrent( ), 100, 200, 100 );
	}

	private void init( )
	{
		propMap = getExtendedProperties( );
		if ( propMap == null )
		{
			propMap = new LinkedHashMap( 20 );
		}
		// this.propMapBackup = (LinkedHashMap) props.clone( );
	}

	private void placeComponents( )
	{
		GridLayout glContent = new GridLayout( );
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		this.setLayout( glContent );

		table = new Table( this, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER );
		GridData gdTable = new GridData( GridData.FILL_BOTH );
		table.setLayoutData( gdTable );
		table.setHeaderVisible( true );
		table.setLinesVisible( true );

		tcKey = new TableColumn( table, SWT.CENTER );
		tcKey.setWidth( 186 );
		tcKey.setText( Messages.getString( "PropertyEditorDialog.Lbl.Key" ) ); //$NON-NLS-1$

		tcValue = new TableColumn( table, SWT.LEFT );
		tcValue.setWidth( 186 );
		tcValue.setText( Messages.getString( "PropertyEditorDialog.Lbl.Value" ) ); //$NON-NLS-1$

		editorValue = new TableEditor( table );
		editorValue.setColumn( 1 );
		editorValue.grabHorizontal = true;
		editorValue.minimumWidth = 30;

		table.addSelectionListener( this );

		// Layout for buttons panel
		GridLayout glButtons = new GridLayout( );
		glButtons.numColumns = 3;
		glButtons.horizontalSpacing = 5;
		glButtons.verticalSpacing = 5;
		glButtons.marginWidth = 0;
		glButtons.marginHeight = 0;

		Composite cmpButtons = new Composite( this, SWT.NONE );
		GridData gdCMPButtons = new GridData( GridData.FILL_HORIZONTAL );
		cmpButtons.setLayoutData( gdCMPButtons );
		cmpButtons.setLayout( glButtons );

		txtNewKey = new Text( cmpButtons, SWT.SINGLE | SWT.BORDER );
		GridData gdTXTNewKey = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTNewKey.grabExcessHorizontalSpace = true;
		txtNewKey.setLayoutData( gdTXTNewKey );

		btnAdd = new Button( cmpButtons, SWT.PUSH );
		GridData gdBTNAdd = new GridData( GridData.HORIZONTAL_ALIGN_END );
		gdBTNAdd.grabExcessHorizontalSpace = false;
		btnAdd.setLayoutData( gdBTNAdd );
		btnAdd.setText( Messages.getString( "PropertyEditorDialog.Lbl.Add" ) ); //$NON-NLS-1$
		btnAdd.addSelectionListener( this );

		btnRemove = new Button( cmpButtons, SWT.PUSH );
		GridData gdBTNRemove = new GridData( GridData.HORIZONTAL_ALIGN_END );
		gdBTNRemove.grabExcessHorizontalSpace = false;
		btnRemove.setLayoutData( gdBTNRemove );
		btnRemove.setText( Messages.getString( "PropertyEditorDialog.Lbl.Remove" ) ); //$NON-NLS-1$
		btnRemove.addSelectionListener( this );

		// Layout for Dialog button composite
		GridLayout glDlgButtons = new GridLayout( );
		glDlgButtons.numColumns = 2;
		glDlgButtons.horizontalSpacing = 5;
		glDlgButtons.verticalSpacing = 5;
		glDlgButtons.marginHeight = 0;
		glDlgButtons.marginWidth = 0;

		cmpDlgButtons = new Composite( this, SWT.NONE );
		GridData gdCMPDlgButtons = new GridData( GridData.FILL_HORIZONTAL );
		cmpDlgButtons.setLayoutData( gdCMPDlgButtons );
		cmpDlgButtons.setLayout( glDlgButtons );

		btnAccept = new Button( cmpDlgButtons, SWT.PUSH );
		GridData gdBTNAccept = new GridData( GridData.FILL_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_END );
		btnAccept.setLayoutData( gdBTNAccept );
		btnAccept.setText( Messages.getString( "Shared.Lbl.OK" ) ); //$NON-NLS-1$
		btnAccept.addSelectionListener( this );

		btnCancel = new Button( cmpDlgButtons, SWT.PUSH );
		GridData gdBTNCancel = new GridData( );
		btnCancel.setLayoutData( gdBTNCancel );
		btnCancel.setText( Messages.getString( "Shared.Lbl.Cancel" ) ); //$NON-NLS-1$
		btnCancel.addSelectionListener( this );

		populateTable( );
	}

	private void populateTable( )
	{
		Iterator keys = propMap.keySet( ).iterator( );
		while ( keys.hasNext( ) )
		{
			Object oKey = keys.next( );
			Object oValue = propMap.get( oKey );

			String[] sProperty = new String[2];
			sProperty[0] = oKey.toString( );
			sProperty[1] = oValue.toString( );

			TableItem tiProp = new TableItem( table, SWT.NONE );
			tiProp.setBackground( color );
			tiProp.setText( sProperty );
		}
		if ( table.getItemCount( ) > 0 )
		{
			table.select( 0 );
		}
		else
		{
			txtNewKey.forceFocus( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnAdd ) )
		{
			String sKey = txtNewKey.getText( );
			if ( sKey.length( ) > 0 && !propMap.containsKey( sKey ) )
			{
				propMap.put( sKey, "" ); //$NON-NLS-1$
				String[] sProperty = new String[2];
				sProperty[0] = sKey;
				sProperty[1] = ""; //$NON-NLS-1$

				TableItem tiProp = new TableItem( table, SWT.NONE );
				tiProp.setBackground( color );
				tiProp.setText( sProperty );
			}
		}
		else if ( e.getSource( ).equals( btnRemove ) )
		{
			if ( table.getSelection( ).length != 0 )
			{
				propMap.remove( table.getSelection( )[0].getText( 0 ) );
				table.remove( table.getSelectionIndex( ) );
				Control editor = editorValue.getEditor( );
				if ( editor != null )
				{
					editor.dispose( );
				}
			}
		}
		else if ( e.getSource( ).equals( btnAccept ) )
		{
			for ( int i = 0; i < table.getItemCount( ); i++ )
			{
				propMap.put( table.getItem( i ).getText( 0 ), table.getItem( i )
						.getText( 1 ) );
			}
			if ( editorValue.getEditor( ) != null
					&& !editorValue.getEditor( ).isDisposed( ) )
			{
				propMap.put( table.getSelection( )[0].getText( 0 ),
						( (Text) editorValue.getEditor( ) ).getText( ) );
			}
			chart.getExtendedProperties( ).clear( );
			chart.getExtendedProperties( )
					.addAll( createExtendedProperties( propMap ) );
			getShell( ).close( );
		}
		else if ( e.getSource( ).equals( btnCancel ) )
		{
			// propMap = propMapBackup;
			getShell( ).close( );
		}
		else if ( e.getSource( ).equals( table ) )
		{
			Control oldEditor = editorValue.getEditor( );
			if ( oldEditor != null )
				oldEditor.dispose( );

			// Identify the selected row
			TableItem item = (TableItem) e.item;
			if ( item == null )
				return;

			// The control that will be the editor must be a child of the Table
			Text newEditor = new Text( table, SWT.NONE );
			newEditor.setText( item.getText( 1 ) );
			newEditor.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					Text text = (Text) editorValue.getEditor( );
					editorValue.getItem( ).setText( 1, text.getText( ) );
				}
			} );
			newEditor.selectAll( );
			newEditor.setFocus( );
			editorValue.setEditor( newEditor, item, 1 );
		}
	}

	public void dispose( )
	{
		super.dispose( );
		if ( color != null && !color.isDisposed( ) )
		{
			color.dispose( );
		}
	}

	private LinkedHashMap getExtendedProperties( )
	{
		LinkedHashMap propMap = new LinkedHashMap( );
		Object[] oArr = chart.getExtendedProperties( ).toArray( );
		if ( oArr.length > 0 )
		{
			for ( int i = 0; i < oArr.length; i++ )
			{
				ExtendedProperty property = ( (ExtendedProperty) oArr[i] );
				propMap.put( property.getName( ), property.getValue( ) );
			}
		}
		return propMap;
	}

	private Vector createExtendedProperties( LinkedHashMap props )
	{
		Vector v = new Vector( );
		if ( props == null || props.size( ) == 0 )
		{
			return v;
		}
		Iterator keys = props.keySet( ).iterator( );
		while ( keys.hasNext( ) )
		{
			Object oKey = keys.next( );
			Object oValue = props.get( oKey );

			String[] sProperty = new String[2];
			sProperty[0] = oKey.toString( );
			sProperty[1] = oValue.toString( );

			ExtendedProperty property = AttributeFactory.eINSTANCE.createExtendedProperty( );
			property.setName( oKey.toString( ) );
			property.setValue( props.get( oKey.toString( ) ).toString( ) );
			property.eAdapters( ).addAll( chart.eAdapters( ) );
			v.add( property );
		}
		return v;
	}
}