/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.MenuStylesKeyType;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * The class defines a dialog to edit menu style properties.
 * 
 * @since 2.5
 */

public class MenuStylesDialog extends TrayDialog implements Listener
{

	private EMap<String, String> fPropertiesMap;
	private Combo fComboStyle;
	private Table fTable;
	private Button fBtnAdd;
	private Button fBtnRemove;
	private TableViewer fTableViewer;
	private ArrayList<String[]> fCurrentAttrList;
	private MenuStylesKeyType fCurrentStyleKeyType;

	private static final String[] COLUMNS = new String[]{
			"Properties", "Value" //$NON-NLS-1$ //$NON-NLS-2$
	};

	/**
	 * @param shell
	 */
	public MenuStylesDialog( Shell shell, EMap<String, String> propertiesMap )
	{
		super( shell );
		setHelpAvailable( false );
		setShellStyle( SWT.DIALOG_TRIM | SWT.RESIZE );
		fPropertiesMap = propertiesMap;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.HYPERLINK_OPTIONS );
		getShell( ).setText( Messages.getString("MenuStylesDialog.title") ); //$NON-NLS-1$
		Composite c = (Composite) super.createDialogArea( parent );
		placeComponents( c );
		initTableContents( );
		populateUIValues( );
		initListeners( c );
		updateButtonStatus( );
		return c;
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		// Just create OK by default
		createButton( parent,
				IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL,
				true );
	}

	/**
	 * @param c
	 */
	private void placeComponents( Composite c )
	{
		GridLayout gl = (GridLayout) c.getLayout( );
		gl.numColumns = 4;

		Label labelStyle = new Label( c, SWT.NONE );
		labelStyle.setText( Messages.getString("MenuStylesDialog.Label.Style") ); //$NON-NLS-1$

		fComboStyle = new Combo( c, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		fComboStyle.setLayoutData( gd );

		new Label( c, SWT.NONE );

		fTable = new Table( c, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION );
		fTable.setLinesVisible( true );
		fTable.setHeaderVisible( true );

		gd = new GridData( GridData.FILL_BOTH );
		gd.horizontalSpan = 3;
		gd.verticalSpan = 16;
		fTable.setLayoutData( gd );

		fTableViewer = new TableViewer( fTable );
		fTableViewer.setUseHashlookup( true );

		fBtnAdd = new Button( c, SWT.NONE );
		fBtnAdd.setText( Messages.getString("MenuStylesDialog.Btn.Add") ); //$NON-NLS-1$

		fBtnRemove = new Button( c, SWT.NONE );
		fBtnRemove.setText( Messages.getString("MenuStylesDialog.Btn.Remove") ); //$NON-NLS-1$
	}

	private void initTableContents( )
	{
		// Initialize table columns.
		TableColumn column = new TableColumn( fTable, SWT.LEFT );
		column.setText( getI18NMessage( "Column." + COLUMNS[0] ) ); //$NON-NLS-1$
		column.setWidth( 80 );

		column = new TableColumn( fTable, SWT.LEFT );
		column.setText( getI18NMessage( "Column." + COLUMNS[1] ) ); //$NON-NLS-1$
		column.setWidth( 120 );

		fTableViewer.setColumnProperties( COLUMNS );
		fTableViewer.setContentProvider( new PropertiesContentProvider( ) );
		fTableViewer.setLabelProvider( new PropertiesLabelProvider( ) );
		fTableViewer.setCellEditors( getCellEditors( fTable ) );
		fTableViewer.setCellModifier( new PropertyCellModifier( fTableViewer ) );
	}

	private void populateUIValues( )
	{
		List<String> displayKeys = new ArrayList<String>( MenuStylesKeyType.VALUES.size( ) );
		for ( MenuStylesKeyType key : MenuStylesKeyType.VALUES )
		{
			displayKeys.add( getI18NMessage( "Style." + key.getName( ) ) ); //$NON-NLS-1$
		}
		fComboStyle.setItems( displayKeys.toArray( new String[]{} ) );

		fComboStyle.select( 0 );

		// Update table.
		switchProperties( MenuStylesKeyType.MENU );
	}

	private void updateButtonStatus( )
	{
		int index = fTable.getSelectionIndex( );
		fBtnRemove.setEnabled( index >= 0 );
	}

	private void initListeners( Composite c )
	{
		c.addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				// Only save when it's needed
				updateProperties( MenuStylesKeyType.get( fComboStyle.getSelectionIndex( ) ) );
			}

		} );

		fComboStyle.addListener( SWT.Selection, this );
		fBtnAdd.addListener( SWT.Selection, this );
		fBtnRemove.addListener( SWT.Selection, this );

		fTable.addListener( SWT.Resize, this );
		fTable.addListener( SWT.Selection, this );
		fTable.addListener( SWT.KeyDown, this );
	}

	private void updateProperties( MenuStylesKeyType menuStylesKeyType )
	{
		fPropertiesMap.put( menuStylesKeyType.getName( ),
				serializeProperties( fCurrentAttrList ) );
	}

	private String getI18NMessage( String key )
	{
		return Messages.getString( "MenuStyleDialog." + key ); //$NON-NLS-1$
	}

	/**
	 * Set input object of table viewer.
	 * 
	 * @param input
	 */
	protected void setTableVeiwerInput( Object input )
	{
		fTableViewer.setInput( input );
		fTableViewer.refresh( );
		updateButtonStatus( );
	}

	private List<String[]> deserializeProperties( String properties )
	{
		String[] attributes = properties.split( ";" ); //$NON-NLS-1$
		fCurrentAttrList = new ArrayList<String[]>( );
		for ( String v : attributes )
		{
			int index = v.indexOf( ':' );
			if ( index < 0 )
			{
				continue;
			}
			String[] element = new String[2];
			element[0] = v.substring( 0, index );
			element[1] = v.substring( index + 1 );
			fCurrentAttrList.add( element );
		}

		return fCurrentAttrList;
	}

	private String serializeProperties( List<String[]> attrMap )
	{
		if ( attrMap == null || attrMap.size( ) == 0 )
		{
			return ""; //$NON-NLS-1$
		}

		StringBuilder sb = new StringBuilder( );
		int i = 0;
		for ( String[] v : attrMap )
		{
			if ( v[0] == null || "".equals( v[0] ) ) //$NON-NLS-1$
			{
				continue;
			}

			if ( i != 0 )
			{
				sb.append( ";" ); //$NON-NLS-1$
			}
			sb.append( v[0] );
			sb.append( ":" ); //$NON-NLS-1$
			sb.append( v[1] );
			i++;
		}

		return sb.toString( );
	}

	private CellEditor[] getCellEditors( Table table )
	{
		CellEditor[] editors = new CellEditor[COLUMNS.length];
		editors[0] = new TextCellEditor( table ) {

			@Override
			protected void keyReleaseOccured( KeyEvent keyEvent )
			{
				super.keyReleaseOccured( keyEvent );
				if ( keyEvent.character == '\r' )
				{
					fTableViewer.editElement( fTableViewer.getElementAt( fTable.getSelectionIndex( ) ),
						1 );
				}

			}
		};
		editors[1] = new TextCellEditor( table );
		return editors;
	}

	static class PropertiesContentProvider implements
			IStructuredContentProvider
	{

		public Object[] getElements( Object inputElement )
		{
			if ( inputElement instanceof List )
				return ( (List<String[]>) inputElement ).toArray( );
			return null;
		}

		public void dispose( )
		{
			// TODO Auto-generated method stub

		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
			// TODO Auto-generated method stub

		}

	}

	/**
	 * EffectsTableCellModifier
	 */
	static class PropertyCellModifier implements ICellModifier
	{

		private TableViewer fTableViewer;

		public PropertyCellModifier( TableViewer tableViewer )
		{
			fTableViewer = tableViewer;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
		 * java.lang.String)
		 */
		public boolean canModify( Object element, String property )
		{
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object,
		 * java.lang.String)
		 */
		public Object getValue( Object element, String property )
		{
			String[] ele = ( (String[]) element );
			int index = Arrays.asList( COLUMNS ).indexOf( property );
			return ele[index];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
		 * java.lang.String, java.lang.Object)
		 */
		public void modify( Object element, String property, Object value )
		{
			int index = Arrays.asList( COLUMNS ).indexOf( property );
			String[] data = (String[]) ( (TableItem) element ).getData( );
			data[index] = (String) value;
			fTableViewer.update( element, null );
			fTableViewer.refresh( );
		}
	}

	/**
	 * EffectLabelProvider
	 */
	static class PropertiesLabelProvider extends LabelProvider implements
			ITableLabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int)
		 */
		public String getColumnText( Object element, int columnIndex )
		{
			if ( element instanceof String[] )
			{
				if ( columnIndex < ( (String[]) element ).length )
				{
					return ( (String[]) element )[columnIndex];
				}
			}
			return null;
		}

	}

	public void handleEvent( Event event )
	{
		Object source = event.widget;
		if ( source == fComboStyle )
		{
			updateProperties( fCurrentStyleKeyType );
			switchProperties( MenuStylesKeyType.get( fComboStyle.getSelectionIndex( ) ) );
		}
		else if ( source == fBtnAdd )
		{
			doAdd( );
		}
		else if ( source == fBtnRemove )
		{
			doRemove( );
		}
		else if ( source == fTable )
		{
			if ( event.type == SWT.Resize )
			{
				int totalWidth = 0;
				int valuewidth = 0;
				int i = 0;
				for ( TableColumn tc : fTable.getColumns( ) )
				{
					totalWidth += tc.getWidth( );
					if ( i == 1 )
					{
						valuewidth = tc.getWidth( );
					}
					i++;
				}
				valuewidth += ( fTable.getClientArea( ).width - totalWidth );
				fTable.getColumn( 1 ).setWidth( valuewidth );
			}
			else if ( event.type == SWT.Selection )
			{
				updateButtonStatus( );
			}
			else if ( event.type == SWT.KeyDown )
			{
				if ( event.character == ' ' )
				{
					fTableViewer.editElement( fTableViewer.getElementAt( fTable.getSelectionIndex( ) ),
							0 );
				}
			}
		}
	}

	/**
	 * 
	 */
	private void doAdd( )
	{
		final String[] ele = new String[]{
				"", "" //$NON-NLS-1$ //$NON-NLS-2$
		};
		fCurrentAttrList.add( ele );
		fTableViewer.refresh( );

		Display.getDefault( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				fTableViewer.editElement( ele, 0 );
			}
		} );
	}

	/**
	 * 
	 */
	private void doRemove( )
	{
		int index = fTable.getSelectionIndex( );
		if ( index < 0 )
		{
			return;
		}

		fCurrentAttrList.remove( index );
		fTableViewer.refresh( );
	}

	private void switchProperties( MenuStylesKeyType menuStylesKeyType )
	{
		fCurrentStyleKeyType = menuStylesKeyType;
		setTableVeiwerInput( deserializeProperties( fPropertiesMap.get( menuStylesKeyType.getName( ) ) ) );
	}
}
