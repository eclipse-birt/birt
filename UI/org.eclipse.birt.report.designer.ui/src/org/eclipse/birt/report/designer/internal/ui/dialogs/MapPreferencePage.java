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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Preference page for map style.
 */

public class MapPreferencePage extends BaseStylePreferencePage
{

	class MapLabelProvider extends LabelProvider implements ITableLabelProvider
	{

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			return provider.getColumnText( element, columnIndex );
		}

	}

	class MapContentProvider implements IStructuredContentProvider
	{

		public Object[] getElements( Object inputElement )
		{
			Object[] elements = provider.getElements( inputElement );

			return elements;
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public void dispose( )
		{
		}

	}

	private TableViewer fTableViewer;

	private Button fAddButton;

	private Button fEditButton;

	private Button fDeleteButton;

	private Button fMoveUpButton;

	private Button fMoveDownButton;

	private MapHandleProvider provider = new MapHandleProvider( );

	private Object model;

	/**
	 * Default constructor.
	 * 
	 * @param model
	 *            the model of preference page.
	 */
	public MapPreferencePage( Object model )
	{
		super( model );
		setTitle( Messages.getString( "MapPreferencePage.displayname.Title" ) ); //$NON-NLS-1$
		setDescription( Messages.getString( "MapPreferencePage.text.Description" ) ); //$NON-NLS-1$

		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite ancestor )
	{
		super.createFieldEditors( );

		final Composite parent = new Composite( ancestor, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout( layout );

		final Table table = new Table( parent, SWT.BORDER | SWT.FULL_SELECTION );

		GridData data = new GridData( GridData.FILL_BOTH );
		table.setLayoutData( data );

		table.setHeaderVisible( true );
		table.setLinesVisible( false );

		TableLayout tableLayout = new TableLayout( );
		table.setLayout( tableLayout );

		final TableColumn column1 = new TableColumn( table, SWT.NONE );
		column1.setText( Messages.getString( "MapPreferencePage.displayname.DisplayValue" ) ); //$NON-NLS-1$

		final TableColumn column2 = new TableColumn( table, SWT.NONE );
		column2.setText( Messages.getString( "MapPreferencePage.displayname.Condition" ) ); //$NON-NLS-1$

		fTableViewer = new TableViewer( table );
		fTableViewer.setLabelProvider( new MapLabelProvider( ) );
		fTableViewer.setContentProvider( new MapContentProvider( ) );

		fTableViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				updateButtons( );
			}
		} );

		fTableViewer.addDoubleClickListener( new IDoubleClickListener( ) {

			public void doubleClick( DoubleClickEvent event )
			{
				edit( );
			}
		} );

		parent.addControlListener( new ControlAdapter( ) {

			// Resize the table columns when the parent is resized.
			public void controlResized( ControlEvent e )
			{
				Rectangle area = parent.getClientArea( );
				Point preferredSize = table.computeSize( SWT.DEFAULT,
						SWT.DEFAULT );
				int width = area.width - 2 * table.getBorderWidth( );
				if ( preferredSize.y > area.height )
				{
					Point vBarSize = table.getVerticalBar( ).getSize( );
					width -= vBarSize.x;
				}
				Point oldSize = table.getSize( );
				if ( oldSize.x > width )
				{
					column1.setWidth( width / 4 );
					column2.setWidth( width - column1.getWidth( ) );
					table.setSize( width, area.height );
				}
				else
				{
					table.setSize( width, area.height );
					column1.setWidth( width / 4 );
					column2.setWidth( width - column1.getWidth( ) );
				}
			}
		} );

		Composite buttons = new Composite( parent, SWT.NONE );
		buttons.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		layout = new GridLayout( );
		layout.numColumns = 6;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout( layout );

		fAddButton = new Button( buttons, SWT.PUSH );
		fAddButton.setText( Messages.getString( "MapPreferencePage.text.Add" ) ); //$NON-NLS-1$
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 60;
		data.heightHint = 24;
		fAddButton.setLayoutData( data );

		fAddButton.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event e )
			{
				add( );
			}
		} );

		fEditButton = new Button( buttons, SWT.PUSH );
		fEditButton.setText( Messages.getString( "MapPreferencePage.text.Edit" ) ); //$NON-NLS-1$
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 60;
		data.heightHint = 24;
		fEditButton.setLayoutData( data );
		fEditButton.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event e )
			{
				edit( );
			}
		} );

		fDeleteButton = new Button( buttons, SWT.PUSH );
		fDeleteButton.setText( Messages.getString( "MapPreferencePage.text.Delete" ) ); //$NON-NLS-1$
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 60;
		data.heightHint = 24;
		fDeleteButton.setLayoutData( data );
		fDeleteButton.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event e )
			{
				delete( );
			}
		} );

		fMoveUpButton = new Button( buttons, SWT.ARROW );
		fMoveUpButton.setAlignment( SWT.UP );
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 40;
		data.heightHint = 24;
		fMoveUpButton.setLayoutData( data );
		fMoveUpButton.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event e )
			{
				moveUp( );
			}
		} );

		fMoveDownButton = new Button( buttons, SWT.ARROW );
		fMoveDownButton.setAlignment( SWT.DOWN );
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 40;
		data.heightHint = 24;
		fMoveDownButton.setLayoutData( data );
		fMoveDownButton.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event e )
			{
				moveDown( );
			}
		} );

		fTableViewer.setInput( model );

		updateButtons( );

		Dialog.applyDialogFont( parent );

		return parent;
	}

	private void refreshTableItemView( )
	{
		for ( int i = 0; i < fTableViewer.getTable( ).getItemCount( ); i++ )
		{
			TableItem ti = fTableViewer.getTable( ).getItem( i );

			MapRuleHandle handle = (MapRuleHandle) ti.getData( );

			ti.setText( 0, provider.getColumnText( handle, 0 ) );
			ti.setText( 1, provider.getColumnText( handle, 1 ) );
		}
		
		fTableViewer.getTable().setFocus();
	}

	private void updateButtons( )
	{
		fEditButton.setEnabled( fTableViewer.getTable( ).getSelectionIndex( ) >= 0
				&& fTableViewer.getTable( ).getSelectionIndex( ) < fTableViewer.getTable( )
						.getItemCount( ) );
		fDeleteButton.setEnabled( fEditButton.getEnabled( ) );

		fMoveUpButton.setEnabled( fTableViewer.getTable( ).getSelectionIndex( ) > 0
				&& fTableViewer.getTable( ).getSelectionIndex( ) < fTableViewer.getTable( )
						.getItemCount( ) );
		fMoveDownButton.setEnabled( fTableViewer.getTable( )
				.getSelectionIndex( ) >= 0
				&& fTableViewer.getTable( ).getSelectionIndex( ) < fTableViewer.getTable( )
						.getItemCount( ) - 1 );
	}

	private void add( )
	{
		MapRuleBuilderDialog builder = new MapRuleBuilderDialog( getShell( ),
				Messages.getString( "MapPreferencePage.text.Title" ), //$NON-NLS-1$
				provider );

		builder.updateHandle( null, fTableViewer.getTable( ).getItemCount( ) );

		if ( builder.open( ) == Window.OK )
		{
			fTableViewer.add( builder.getHandle( ) );

			updateButtons( );

			refreshTableItemView( );
		}
	}

	private void edit( )
	{
		if ( fTableViewer.getTable( ).getSelectionIndex( ) >= 0
				&& fTableViewer.getTable( ).getSelectionIndex( ) < fTableViewer.getTable( )
						.getItemCount( ) )
		{
			MapRuleBuilderDialog builder = new MapRuleBuilderDialog( getShell( ),
					Messages.getString( "MapPreferencePage.text.Title" ), //$NON-NLS-1$
					provider );

			MapRuleHandle handle = (MapRuleHandle) fTableViewer.getTable( )
					.getItem( fTableViewer.getTable( ).getSelectionIndex( ) )
					.getData( );

			builder.updateHandle( handle, fTableViewer.getTable( )
					.getItemCount( ) );

			if ( builder.open( ) == Window.OK )
			{
				updateButtons( );

				refreshTableItemView( );
			}
		}
	}

	private void delete( )
	{
		if ( fTableViewer.getTable( ).getSelectionIndex( ) >= 0
				&& fTableViewer.getTable( ).getSelectionIndex( ) < fTableViewer.getTable( )
						.getItemCount( ) )
		{
			int idx = fTableViewer.getTable( ).getSelectionIndex( );

			try
			{
				provider.doDeleteItem( idx );

				fTableViewer.getTable( ).remove( idx );
				fTableViewer.refresh( );

				if ( idx >= fTableViewer.getTable( ).getItemCount( ) )
				{
					idx--;
				}

				fTableViewer.getTable( ).select( idx );

				refreshTableItemView( );
			}
			catch ( Exception e )
			{
				WidgetUtil.processError( getShell( ), e );
			}

			updateButtons( );
		}
	}

	private void moveUp( )
	{
		int index = fTableViewer.getTable( ).getSelectionIndex( );

		try
		{
			provider.doSwapItem( index, -1 );

			Object handle = fTableViewer.getTable( ).getItem( index ).getData( );

			fTableViewer.remove( handle );
			fTableViewer.insert( handle, index - 1 );
			fTableViewer.refresh( );

			fTableViewer.getTable( ).select( index - 1 );

			refreshTableItemView( );
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		updateButtons( );
	}

	private void moveDown( )
	{
		int index = fTableViewer.getTable( ).getSelectionIndex( );

		try
		{
			provider.doSwapItem( index, 1 );

			Object handle = fTableViewer.getTable( ).getItem( index ).getData( );

			fTableViewer.remove( handle );
			fTableViewer.insert( handle, index + 1 );
			fTableViewer.refresh( );

			fTableViewer.getTable( ).select( index + 1 );

			refreshTableItemView( );

		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		updateButtons( );
	}

}