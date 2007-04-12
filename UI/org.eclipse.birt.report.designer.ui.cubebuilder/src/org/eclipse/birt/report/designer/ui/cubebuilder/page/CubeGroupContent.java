/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.RenameInputDialog;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ListenerElementVisitor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.LevelDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.LevelPropertyDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.MeasureDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeContentProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeLabelProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.DataContentProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.VirtualField;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureGroupModel;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;

public class CubeGroupContent extends Composite implements Listener
{

	private TreeItem[] dragSourceItems = new TreeItem[1];

	class CustomDragListener implements DragSourceListener
	{

		private TreeViewer viewer;

		CustomDragListener( TreeViewer viewer )
		{
			this.viewer = viewer;
		}

		public void dragFinished( DragSourceEvent event )
		{

		};

		public void dragSetData( DragSourceEvent event )
		{
			event.data = dragSourceItems[0].getText( );
		}

		public void dragStart( DragSourceEvent event )
		{
			TreeItem[] selection = viewer.getTree( ).getSelection( );

			if ( selection.length > 0 )
			{
				if ( viewer == dataFieldsViewer )
				{
					event.doit = true;
					dragSourceItems[0] = selection[0];
				}
				else if ( viewer == groupViewer
						&& selection[0].getData( ) != null
						&& selection[0].getData( ) instanceof LevelHandle )
				{
					dragSourceItems[0] = selection[0];
				}
			}
			else
			{
				event.doit = false;
			}

		}
	}

	private TreeViewer dataFieldsViewer;

	public CubeGroupContent( Composite parent, int style )
	{
		super( parent, style );
		GridLayout layout = new GridLayout( 4, false );
		layout.marginTop = 0;
		this.setLayout( layout );
		createContent( );
	}

	private CubeBuilder builder;

	public CubeGroupContent( CubeBuilder builder, Composite parent, int style )
	{
		super( parent, style );
		this.builder = builder;
		GridLayout layout = new GridLayout( 4, false );
		layout.marginTop = 0;
		this.setLayout( layout );
		createContent( );
	}

	public void dispose( )
	{
		if ( visitor != null )
		{
			if ( input != null )
				visitor.removeListener( input );
			visitor.dispose( );
			visitor = null;
		}
		super.dispose( );
	}

	private CubeHandle input;
	private TreeViewer groupViewer;

	public void setInput( CubeHandle cube )
	{
		if ( input != null )
			getListenerElementVisitor( ).removeListener( input );
		this.input = cube;
	}
	private DataSetHandle[] datasets = new DataSetHandle[1];

	public void setInput( CubeHandle cube, DataSetHandle dataset )
	{
		this.input = cube;
		datasets[0] = dataset;
	}

	public void createContent( )
	{
		createDataField( );
		createMoveButtonsField( );
		createGroupField( );
		createOperationField( );
	}

	private void createOperationField( )
	{
		Composite operationField = new Composite( this, SWT.NONE );
		operationField.setLayout( new GridLayout( ) );
		operationField.setLayout( new GridLayout( ) );

		String[] btnTexts = new String[]{
				Messages.getString( "GroupsPage.Button.Add" ),
				Messages.getString( "GroupsPage.Button.Edit" ),
				Messages.getString( "GroupsPage.Button.Delete" ),
				Messages.getString( "GroupsPage.Button.Property" )
		};
		addBtn = new Button( operationField, SWT.PUSH );
		addBtn.setText( btnTexts[0] );
		addBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleAddEvent( );
			}

		} );

		editBtn = new Button( operationField, SWT.PUSH );
		editBtn.setText( btnTexts[1] );
		editBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				TreeSelection slections = (TreeSelection) groupViewer.getSelection( );
				Iterator iter = slections.iterator( );
				while ( iter.hasNext( ) )
				{
					Object obj = iter.next( );

					if ( obj instanceof TabularLevelHandle )
					{
						TabularLevelHandle level = (TabularLevelHandle) obj;
						LevelDialog dialog = new LevelDialog( false );
						dialog.setInput( level );
						if ( dialog.open( ) == Window.OK )
						{
							refresh( );
						};
					}
					else if ( obj instanceof TabularMeasureHandle )
					{
						TabularMeasureHandle level = (TabularMeasureHandle) obj;
						MeasureDialog dialog = new MeasureDialog( false );
						dialog.setInput( (TabularCubeHandle) input, level );
						if ( dialog.open( ) == Window.OK )
						{
							refresh( );
						};
					}
					else
					{
						RenameInputDialog inputDialog = new RenameInputDialog( getShell( ),
								Messages.getString( "RenameInputDialog.DialogTitle" ),
								Messages.getString( "RenameInputDialog.DialogMessage" ),
								( (DesignElementHandle) obj ).getName( ),
								null );
						inputDialog.create( );
						if ( inputDialog.open( ) == Window.OK )
						{
							try
							{
								( (DesignElementHandle) obj ).setName( inputDialog.getValue( )
										.trim( ) );
							}
							catch ( NameException e1 )
							{
								ExceptionHandler.handle( e1 );
							}
						}
					}
				}
				updateButtons( );
			}

		} );

		delBtn = new Button( operationField, SWT.PUSH );
		delBtn.setText( btnTexts[2] );
		delBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleDelEvent( );
			}

		} );
		propBtn = new Button( operationField, SWT.PUSH );
		propBtn.setText( btnTexts[3] );
		propBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handlePropEvent( );
			}

		} );

		int width = UIUtil.getMaxStringWidth( btnTexts, operationField );
		if ( width < 60 )
			width = 60;
		layoutButton( addBtn, width );
		layoutButton( editBtn, width );
		layoutButton( delBtn, width );
		layoutButton( propBtn, width );
		addBtn.setEnabled( false );
		editBtn.setEnabled( false );
		delBtn.setEnabled( false );
		propBtn.setEnabled( false );

		GridData data = (GridData) addBtn.getLayoutData( );
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.BOTTOM;

		data = (GridData) propBtn.getLayoutData( );
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.TOP;

	}

	private void layoutButton( Button button, int width )
	{
		GridData gd = new GridData( );
		gd.widthHint = width;
		button.setLayoutData( gd );
	}

	private void createGroupField( )
	{
		Composite groupField = new Composite( this, SWT.NONE );
		groupField.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		groupField.setLayout( new GridLayout( ) );

		Label groupLabel = new Label( groupField, SWT.NONE );
		groupLabel.setText( Messages.getString( "GroupsPage.Label.Group" ) );

		groupViewer = new TreeViewer( groupField, SWT.SINGLE
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.BORDER );
		groupViewer.getTree( )
				.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		( (GridData) groupViewer.getTree( ).getLayoutData( ) ).heightHint = 250;
		( (GridData) groupViewer.getTree( ).getLayoutData( ) ).widthHint = 250;
		groupViewer.setLabelProvider( new CubeLabelProvider( ) );
		groupViewer.setContentProvider( new CubeContentProvider( ) );
		groupViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				updateButtons( );
			}

		} );
		groupViewer.setAutoExpandLevel( 4 );
		groupViewer.getTree( ).addKeyListener( new KeyAdapter( ) {

			public void keyPressed( KeyEvent e )
			{
				if ( e.keyCode == SWT.DEL )
				{
					try
					{
						handleDelEvent( );
					}
					catch ( Exception e1 )
					{
						ExceptionHandler.handle( e1 );
					}
				}
			}
		} );

		final DragSource fieldsSource = new DragSource( groupViewer.getTree( ),
				operations );
		fieldsSource.setTransfer( types );
		fieldsSource.addDragListener( new CustomDragListener( groupViewer ) );

		DropTarget target = new DropTarget( groupViewer.getTree( ), operations );
		target.setTransfer( types );
		target.addDropListener( new DropTargetAdapter( ) {

			public void dragOver( DropTargetEvent event )
			{
				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
				if ( event.item != null )
				{
					TreeItem item = (TreeItem) event.item;
					Object element = item.getData( );
					if ( element instanceof CubeHandle
							|| element instanceof PropertyHandle )
					{
						event.detail = DND.DROP_NONE;
						return;
					}
					else
						event.detail = DND.DROP_DEFAULT;

					Object obj = (Object) dragSourceItems[0].getData( );
					ResultSetColumnHandle dataField = null;
					DataSetHandle dataset = null;
					if ( obj == null || obj instanceof DataSetHandle )
					{
						event.detail = DND.DROP_NONE;
						return;
					}

					if ( obj instanceof ResultSetColumnHandle )
					{
						dataField = (ResultSetColumnHandle) obj;
						dataset = (DataSetHandle) dataField.getElementHandle( );

						if ( element instanceof LevelHandle )
						{
							DataSetHandle temp = ( (TabularHierarchyHandle) ( (LevelHandle) element ).getContainer( ) ).getDataSet( );
							if ( temp != null
									&& dataset != null
									&& dataset != temp )
							{
								event.detail = DND.DROP_NONE;
								return;
							}

							if ( dataField != null
									&& isDateType( dataField.getDataType( ) ) )
							{
								event.detail = DND.DROP_NONE;
								return;
							}

							String dataType = ( (LevelHandle) element ).getDataType( );
							if ( dataType != null && isDateType( dataType ) )
							{
								event.detail = DND.DROP_NONE;
								return;
							}

						}
						else if ( element instanceof DimensionHandle
								|| ( element instanceof VirtualField && ( (VirtualField) element ).getType( )
										.equals( VirtualField.TYPE_LEVEL ) ) )
						{
							DimensionHandle dimension = null;
							if ( element instanceof DimensionHandle )
								dimension = (DimensionHandle) element;
							else
								dimension = (DimensionHandle) ( (VirtualField) element ).getModel( );

							DataSetHandle temp = ( (TabularHierarchyHandle) dimension.getDefaultHierarchy( ) ).getDataSet( );
							if ( temp != null
									&& dataset != null
									&& dataset != temp )
							{
								event.detail = DND.DROP_NONE;
								return;
							}
							TabularHierarchyHandle hierarchy = ( (TabularHierarchyHandle) dimension.getDefaultHierarchy( ) );
							if ( hierarchy.getContentCount( IHierarchyModel.LEVELS_PROP ) > 0 )
							{
								if ( dataField != null
										&& isDateType( dataField.getDataType( ) ) )
								{
									event.detail = DND.DROP_NONE;
									return;
								}
								LevelHandle level = (LevelHandle) hierarchy.getContent( IHierarchyModel.LEVELS_PROP,
										0 );
								String dataType = level.getDataType( );
								if ( dataType != null && isDateType( dataType ) )
								{
									event.detail = DND.DROP_NONE;
									return;
								}
							}

						}
						else if ( element instanceof MeasureGroupHandle
								|| ( element instanceof VirtualField && ( (VirtualField) element ).getType( )
										.equals( VirtualField.TYPE_MEASURE ) )
								|| element instanceof MeasureHandle )
						{
							DataSetHandle primary = ( (TabularCubeHandle) input ).getDataSet( );
							if ( primary == null || primary != dataset )
							{
								event.detail = DND.DROP_NONE;
								return;
							}
						}
					}

					if ( obj instanceof LevelHandle )
					{
						if ( !( element instanceof LevelHandle )
								|| element == obj
								|| ( (LevelHandle) obj ).getContainer( ) != ( (LevelHandle) element ).getContainer( ) )
						{
							event.detail = DND.DROP_NONE;
							return;
						}
					}

					Point pt = Display.getCurrent( ).map( null,
							groupViewer.getTree( ),
							event.x,
							event.y );
					Rectangle bounds = item.getBounds( );
					if ( pt.y < bounds.y + bounds.height / 3 )
					{
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					}
					else if ( pt.y > bounds.y + 2 * bounds.height / 3 )
					{
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					}
					else
					{
						event.feedback |= DND.FEEDBACK_SELECT;
					}
				}
				else
				{
					event.detail = DND.DROP_NONE;
				}

			}

			public void drop( DropTargetEvent event )
			{
				if ( event.data == null )
				{
					event.detail = DND.DROP_NONE;
					return;
				}

				Object obj = (Object) dragSourceItems[0].getData( );
				ResultSetColumnHandle dataField = null;
				DataSetHandle dataset = null;
				if ( obj == null || obj instanceof DataSetHandle )
				{
					event.detail = DND.DROP_NONE;
					return;
				}

				if ( obj instanceof ResultSetColumnHandle )
				{
					dataField = (ResultSetColumnHandle) obj;
					dataset = (DataSetHandle) dataField.getElementHandle( );

					if ( event.item == null || dataField == null )
					{
						event.detail = DND.DROP_NONE;
						return;
					}
					else
					{

						TreeItem item = (TreeItem) event.item;
						Point pt = Display.getCurrent( ).map( null,
								groupViewer.getTree( ),
								event.x,
								event.y );
						Rectangle bounds = item.getBounds( );

						Object element = item.getData( );
						try
						{
							if ( pt.y < bounds.y + bounds.height / 3 )
							{
								if ( element instanceof MeasureHandle )
								{
									TabularMeasureHandle measure = DesignElementFactory.getInstance( )
											.newTabularMeasure( dataField.getColumnName( ) );
									measure.setMeasureExpression( DEUtil.getExpression( dataField ) );
									measure.setDataType( dataField.getDataType( ) );
									( (MeasureHandle) element ).getContainer( )
											.add( IMeasureGroupModel.MEASURES_PROP,
													measure );
								}
								else if ( element instanceof CubeHandle
										|| element instanceof PropertyHandle )
								{
									event.detail = DND.DROP_NONE;
									return;
								}
								else if ( element instanceof LevelHandle )
								{
									if ( isDateType( dataField.getDataType( ) ) )
									{
										event.detail = DND.DROP_NONE;
										return;
									}
									int index = ( (LevelHandle) element ).getIndex( );
									TabularLevelHandle level = DesignElementFactory.getInstance( )
											.newTabularLevel( dataField.getColumnName( ) );
									level.setColumnName( dataField.getColumnName( ) );
									level.setDataType( dataField.getDataType( ) );
									( (LevelHandle) element ).getContainer( )
											.add( IHierarchyModel.LEVELS_PROP,
													level,
													index );
								}
							}
							else
							{
								if ( element instanceof MeasureHandle )
								{
									TabularMeasureHandle measure = DesignElementFactory.getInstance( )
											.newTabularMeasure( dataField.getColumnName( ) );
									measure.setMeasureExpression( DEUtil.getExpression( dataField ) );
									measure.setDataType( dataField.getDataType( ) );
									( (MeasureHandle) element ).getContainer( )
											.add( IMeasureGroupModel.MEASURES_PROP,
													measure );
								}
								else if ( element instanceof MeasureGroupHandle
										|| ( element instanceof VirtualField && ( (VirtualField) element ).getType( )
												.equals( VirtualField.TYPE_MEASURE ) ) )
								{
									MeasureGroupHandle measureGroup = null;
									if ( element instanceof MeasureGroupHandle )
										measureGroup = (MeasureGroupHandle) element;
									else
										measureGroup = (MeasureGroupHandle) ( (VirtualField) element ).getModel( );
									TabularMeasureHandle measure = DesignElementFactory.getInstance( )
											.newTabularMeasure( dataField.getColumnName( ) );
									measure.setMeasureExpression( DEUtil.getExpression( dataField ) );
									measure.setDataType( dataField.getDataType( ) );
									measureGroup.add( IMeasureGroupModel.MEASURES_PROP,
											measure );
								}
								else if ( element instanceof CubeHandle
										|| element instanceof PropertyHandle )
								{
									event.detail = DND.DROP_NONE;
									return;
								}
								else if ( element instanceof LevelHandle )
								{
									if ( isDateType( dataField.getDataType( ) ) )
									{
										event.detail = DND.DROP_NONE;
										return;
									}

									TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) ( (LevelHandle) element ).getContainer( );
									if ( hierarchy.getDataSet( ) == null
											&& dataset != null )
									{
										hierarchy.setDataSet( dataset );
									}

									int index = ( (LevelHandle) element ).getIndex( );
									TabularLevelHandle level = DesignElementFactory.getInstance( )
											.newTabularLevel( dataField.getColumnName( ) );
									level.setColumnName( dataField.getColumnName( ) );
									level.setDataType( dataField.getDataType( ) );
									( (LevelHandle) element ).getContainer( )
											.add( IHierarchyModel.LEVELS_PROP,
													level,
													index + 1 );
								}
								else if ( element instanceof DimensionHandle
										|| ( element instanceof VirtualField && ( (VirtualField) element ).getType( )
												.equals( VirtualField.TYPE_LEVEL ) ) )
								{
									DimensionHandle dimension = null;
									if ( element instanceof DimensionHandle )
										dimension = (DimensionHandle) element;
									else
										dimension = (DimensionHandle) ( (VirtualField) element ).getModel( );

									TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension.getDefaultHierarchy( );
									if ( hierarchy.getDataSet( ) == null
											&& dataset != null )
									{
										hierarchy.setDataSet( dataset );
									}
									if ( isDateType( dataField.getDataType( ) ) )
									{
										if ( hierarchy.getContentCount( IHierarchyModel.LEVELS_PROP ) > 0 )
										{
											event.detail = DND.DROP_NONE;
											return;
										}
										else
										{
											hierarchy.add( HierarchyHandle.LEVELS_PROP,
													OlapUtil.getDateLevel( dataField,
															OlapUtil.Level_Year ) );
											hierarchy.add( HierarchyHandle.LEVELS_PROP,
													OlapUtil.getDateLevel( dataField,
															OlapUtil.Level_Qtr ) );
											hierarchy.add( HierarchyHandle.LEVELS_PROP,
													OlapUtil.getDateLevel( dataField,
															OlapUtil.Level_Month ) );
											hierarchy.add( HierarchyHandle.LEVELS_PROP,
													OlapUtil.getDateLevel( dataField,
															OlapUtil.Level_Week ) );
											hierarchy.add( HierarchyHandle.LEVELS_PROP,
													OlapUtil.getDateLevel( dataField,
															OlapUtil.Level_Day ) );
											( (TabularDimensionHandle) hierarchy.getContainer( ) ).setTimeType( true );
										}
									}
									else
									{
										TabularLevelHandle level = DesignElementFactory.getInstance( )
												.newTabularLevel( dataField.getColumnName( ) );
										level.setColumnName( dataField.getColumnName( ) );
										level.setDataType( dataField.getDataType( ) );
										hierarchy.add( IHierarchyModel.LEVELS_PROP,
												level );
									}
								}
							}
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
						}
					}
				}

				if ( obj instanceof LevelHandle )
				{
					int oldIndex = ( (LevelHandle) obj ).getIndex( );
					if ( event.item == null )
					{
						event.detail = DND.DROP_NONE;
						return;
					}
					else
					{

						TreeItem item = (TreeItem) event.item;
						Point pt = Display.getCurrent( ).map( null,
								groupViewer.getTree( ),
								event.x,
								event.y );
						Rectangle bounds = item.getBounds( );

						LevelHandle element = (LevelHandle) item.getData( );
						int newIndex = element.getIndex( );
						if ( newIndex < oldIndex )
						{
							if ( pt.y < bounds.y + bounds.height / 3 )
							{
								newIndex = element.getIndex( );
							}
							else
								newIndex = element.getIndex( ) + 1;
						}
						else if ( newIndex > oldIndex )
						{
							if ( pt.y < bounds.y + bounds.height / 3 )
							{
								newIndex = element.getIndex( ) - 1;
							}
							else
								newIndex = element.getIndex( );
						}
						try
						{
							( (LevelHandle) obj ).moveTo( newIndex );
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
						}

						groupViewer.expandToLevel( ( (LevelHandle) obj ),
								AbstractTreeViewer.ALL_LEVELS );
						groupViewer.setSelection( new StructuredSelection( ( (LevelHandle) obj ) ),
								true );
					}
				}
				groupViewer.refresh( );
			}
		} );

	}

	private void createMoveButtonsField( )
	{
		Composite buttonsField = new Composite( this, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 0;
		buttonsField.setLayout( layout );

		Button addButton = new Button( buttonsField, SWT.PUSH );

		addButton.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

			public void widgetSelected( SelectionEvent e )
			{
				handleDataAddEvent( );

			}

		} );

		Button removeButton = new Button( buttonsField, SWT.PUSH );

		removeButton.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

			public void widgetSelected( SelectionEvent e )
			{
				handleDelEvent( );
			}

		} );

		addButton.setText( ">" );
		removeButton.setText( "<" );

		GridData gd = new GridData( );
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = Math.max( 25, addButton.computeSize( SWT.DEFAULT,
				SWT.DEFAULT ).x );
		gd.verticalAlignment = SWT.BOTTOM;
		addButton.setLayoutData( gd );

		gd = new GridData( );
		gd.widthHint = Math.max( 25, removeButton.computeSize( SWT.DEFAULT,
				SWT.DEFAULT ).x );
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
		removeButton.setLayoutData( gd );

	}

	private void createDataField( )
	{
		Composite dataField = new Composite( this, SWT.NONE );
		dataField.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		dataField.setLayout( new GridLayout( ) );

		Label dataLabel = new Label( dataField, SWT.NONE );
		dataLabel.setText( Messages.getString( "GroupsPage.Label.DataField" ) );
		dataFieldsViewer = new TreeViewer( dataField, SWT.SINGLE
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.BORDER );
		dataFieldsViewer.setLabelProvider( new CubeLabelProvider( ) );
		dataFieldsViewer.setContentProvider( new DataContentProvider( ) );
		dataFieldsViewer.setAutoExpandLevel( 2 );
		GridData gd = new GridData( GridData.FILL_BOTH );
		dataFieldsViewer.getTree( ).setLayoutData( gd );
		( (GridData) dataFieldsViewer.getTree( ).getLayoutData( ) ).heightHint = 250;
		( (GridData) dataFieldsViewer.getTree( ).getLayoutData( ) ).widthHint = 225;
		dataFieldsViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				updateButtons( );
			}

		} );

		final DragSource fieldsSource = new DragSource( dataFieldsViewer.getTree( ),
				operations );
		fieldsSource.setTransfer( types );
		fieldsSource.addDragListener( new CustomDragListener( dataFieldsViewer ) );

	}

	private Button addBtn;
	private Button delBtn;
	private Button propBtn;
	private int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	private Transfer[] types = new Transfer[]{
		TextTransfer.getInstance( )
	};
	private Button editBtn;

	public void load( )
	{
		if ( input != null )
		{
			if ( datasets[0] != null )
				dataFieldsViewer.setInput( datasets );
			// dataFieldsViewer.setInput( OlapUtil.getAvailableDatasets( ) );
			groupViewer.setInput( input );
			getListenerElementVisitor( ).addListener( input );
		}
	}

	private ListenerElementVisitor visitor;

	private ListenerElementVisitor getListenerElementVisitor( )
	{
		if ( visitor == null )
		{
			visitor = new ListenerElementVisitor( this );
		}
		return visitor;
	}

	protected void updateButtons( )
	{
		TreeSelection selections = (TreeSelection) groupViewer.getSelection( );
		if ( selections.size( ) == 1 )
		{
			Iterator iter = selections.iterator( );
			Object obj = iter.next( );

			TreeSelection dataSelection = (TreeSelection) dataFieldsViewer.getSelection( );
			ResultSetColumnHandle dataField = null;
			DataSetHandle dataset = null;
			if ( dataSelection.size( ) == 1
					&& dataSelection.getFirstElement( ) != null
					&& dataSelection.getFirstElement( ) instanceof ResultSetColumnHandle )
			{
				dataField = (ResultSetColumnHandle) dataSelection.getFirstElement( );
				dataset = (DataSetHandle) dataField.getElementHandle( );
			}

			/**
			 * Deal add button and del buuton.
			 */
			if ( obj instanceof DimensionHandle
					|| obj instanceof LevelHandle
					|| obj instanceof MeasureHandle
					|| obj instanceof MeasureGroupHandle
					|| obj instanceof VirtualField )
			{
				if ( obj instanceof DimensionHandle
						&& ( (DimensionHandle) obj ).isTimeType( ) )
					addBtn.setEnabled( false );
				else if ( obj instanceof VirtualField
						&& ( (VirtualField) obj ).getType( )
								.equals( VirtualField.TYPE_LEVEL ) )
				{
					DimensionHandle dimension = (DimensionHandle) ( (VirtualField) obj ).getModel( );
					if ( dimension.isTimeType( ) )
						addBtn.setEnabled( false );
					else
						addBtn.setEnabled( true );
				}
				else
					addBtn.setEnabled( true );

				if ( obj instanceof MeasureGroupHandle
						|| ( obj instanceof VirtualField && ( (VirtualField) obj ).getType( )
								.equals( VirtualField.TYPE_MEASURE ) )
						|| obj instanceof MeasureHandle )
				{
					addBtn.setEnabled( true );
				}
				else if ( dataField == null || dataset == null )
					addBtn.setEnabled( false );

				if ( obj instanceof LevelHandle )
				{
					DimensionHandle dimension = (DimensionHandle) ( (LevelHandle) obj ).getContainer( )
							.getContainer( );
					if ( dimension.isTimeType( ) )
					{
						delBtn.setEnabled( true );
					}
					else
					{
						delBtn.setEnabled( true );
					}
				}
				else if ( obj instanceof VirtualField )
				{
					delBtn.setEnabled( false );
				}
				else
				{
					delBtn.setEnabled( true );
				}
			}
			else
			{
				delBtn.setEnabled( false );
			}

			/**
			 * CubeHandle can do nothing.
			 */
			if ( obj instanceof CubeHandle )
				addBtn.setEnabled( false );
			/**
			 * CubeModel can and a group or a summary field
			 */
			else if ( obj instanceof PropertyHandle )
				addBtn.setEnabled( true );

			/**
			 * Only Level Handle has EditBtn and PropBtn.
			 */
			if ( obj instanceof LevelHandle )
			{
				refresh( );
				LevelHandle level = (LevelHandle) obj;
				if ( level != null
						&& level.attributesIterator( ) != null
						&& level.attributesIterator( ).hasNext( ) )
				{
					String name = level.getName( )
							+ "("
							+ level.getName( )
							+ ":";
					Iterator attrIter = level.attributesIterator( );
					while ( attrIter.hasNext( ) )
					{
						name += ( (LevelAttributeHandle) attrIter.next( ) ).getName( );
						if ( attrIter.hasNext( ) )
							name += ",";
					}
					name += ")";
					groupViewer.getTree( ).getSelection( )[0].setText( name );
				}
				String dataType = level.getDataType( );
				if ( dataType != null && isDateType( dataType ) )
				{
					propBtn.setEnabled( false );
					editBtn.setEnabled( false );
				}
				else
				{
					propBtn.setEnabled( true );
					editBtn.setEnabled( true );
				}

			}
			else
			{
				if ( obj instanceof DimensionHandle
						|| obj instanceof MeasureGroupHandle
						|| obj instanceof MeasureHandle )
					editBtn.setEnabled( true );
				else
					editBtn.setEnabled( false );
				propBtn.setEnabled( false );
			}
		}
		else
		{
			addBtn.setEnabled( false );
			delBtn.setEnabled( false );
			propBtn.setEnabled( false );
			editBtn.setEnabled( false );
		}

	}

	private void handleDelEvent( )
	{
		TreeSelection slections = (TreeSelection) groupViewer.getSelection( );
		Iterator iter = slections.iterator( );
		while ( iter.hasNext( ) )
		{
			Object obj = iter.next( );
			if ( obj instanceof DimensionHandle )
			{
				DimensionHandle dimension = (DimensionHandle) obj;
				try
				{
					dimension.dropAndClear( );
				}
				catch ( Exception e1 )
				{
					ExceptionHandler.handle( e1 );
				}
				refresh( );
			}
			else if ( obj instanceof LevelHandle )
			{
				LevelHandle level = (LevelHandle) obj;
				try
				{
					level.dropAndClear( );
				}
				catch ( SemanticException e1 )
				{
					ExceptionHandler.handle( e1 );
				}
				refresh( );
			}
			else if ( obj instanceof MeasureGroupHandle )
			{
				MeasureGroupHandle measureGroup = (MeasureGroupHandle) obj;
				try
				{
					measureGroup.dropAndClear( );
				}
				catch ( Exception e1 )
				{
					ExceptionHandler.handle( e1 );
				}
				refresh( );
			}
			else if ( obj instanceof MeasureHandle )
			{
				MeasureHandle measure = (MeasureHandle) obj;
				try
				{
					measure.dropAndClear( );
				}
				catch ( SemanticException e1 )
				{
					ExceptionHandler.handle( e1 );
				}
				refresh( );
			}

		}
		updateButtons( );
	}

	private void handlePropEvent( )
	{
		TreeSelection slections = (TreeSelection) groupViewer.getSelection( );
		Iterator iter = slections.iterator( );
		while ( iter.hasNext( ) )
		{
			Object obj = iter.next( );
			if ( obj instanceof TabularLevelHandle )
			{
				TabularLevelHandle level = (TabularLevelHandle) obj;
				LevelPropertyDialog dialog = new LevelPropertyDialog( );
				dialog.setInput( level );
				if ( dialog.open( ) == Window.OK )
				{
					refresh( );
				};
			}
		}
	}

	private void handleAddEvent( )
	{
		TreeSelection slections = (TreeSelection) groupViewer.getSelection( );
		Iterator iter = slections.iterator( );
		while ( iter.hasNext( ) )
		{
			Object obj = iter.next( );

			TreeSelection dataFields = (TreeSelection) dataFieldsViewer.getSelection( );
			Iterator iterator = dataFields.iterator( );
			ResultSetColumnHandle dataField = null;
			while ( iterator.hasNext( ) )
			{
				Object temp = iterator.next( );
				if ( !( temp instanceof ResultSetColumnHandle ) )
					continue;
				dataField = (ResultSetColumnHandle) temp;
			}

			if ( dataField != null )
			{
				handleDataAddEvent( );
			}
			else
			{
				try
				{
					if ( obj instanceof MeasureGroupHandle
							|| ( obj instanceof VirtualField && ( (VirtualField) obj ).getType( )
									.equals( VirtualField.TYPE_MEASURE ) ) )
					{
						MeasureGroupHandle measureGroup = null;
						if ( obj instanceof MeasureGroupHandle )
							measureGroup = (MeasureGroupHandle) obj;
						else
							measureGroup = (MeasureGroupHandle) ( (VirtualField) obj ).getModel( );
						MeasureDialog dialog = new MeasureDialog( true );
						dialog.setInput( (TabularCubeHandle) input, null );
						if ( dialog.open( ) == Window.OK )
						{
							measureGroup.add( IMeasureGroupModel.MEASURES_PROP,
									(DesignElementHandle) dialog.getResult( ) );
						}

					}
					else if ( obj instanceof MeasureHandle )
					{
						MeasureGroupHandle measureGroup = (MeasureGroupHandle) ( (MeasureHandle) obj ).getContainer( );
						MeasureDialog dialog = new MeasureDialog( true );
						dialog.setInput( (TabularCubeHandle) input, null );
						if ( dialog.open( ) == Window.OK )
						{
							measureGroup.add( IMeasureGroupModel.MEASURES_PROP,
									(DesignElementHandle) dialog.getResult( ) );
						}
					}
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
				refresh( );
			}

			if ( obj instanceof PropertyHandle )
			{
				PropertyHandle model = (PropertyHandle) obj;
				if ( model.getPropertyDefn( )
						.getName( )
						.equals( ICubeModel.DIMENSIONS_PROP ) )
				{
					DimensionHandle dimension = DesignElementFactory.getInstance( )
							.newTabularDimension( "Group" );
					try
					{
						model.getElementHandle( )
								.add( ICubeModel.DIMENSIONS_PROP, dimension );
					}
					catch ( SemanticException e1 )
					{
						ExceptionHandler.handle( e1 );
					}
					refresh( );
				}
				else if ( model.getPropertyDefn( )
						.getName( )
						.equals( ICubeModel.MEASURE_GROUPS_PROP ) )
				{
					MeasureGroupHandle measureGroup = DesignElementFactory.getInstance( )
							.newTabularMeasureGroup( "Summary Field" );
					try
					{
						model.getElementHandle( )
								.add( ICubeModel.MEASURE_GROUPS_PROP,
										measureGroup );
					}
					catch ( SemanticException e1 )
					{
						ExceptionHandler.handle( e1 );
					}
					refresh( );
				}
			}
		}
	}

	private boolean isDateType( String dataType )
	{
		return dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME )
				|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE )
				|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_TIME );
	}

	protected void handleDataAddEvent( )
	{
		TreeSelection dataFields = (TreeSelection) dataFieldsViewer.getSelection( );
		Iterator iterator = dataFields.iterator( );
		while ( iterator.hasNext( ) )
		{
			Object temp = iterator.next( );
			if ( !( temp instanceof ResultSetColumnHandle ) )
				continue;

			ResultSetColumnHandle dataField = (ResultSetColumnHandle) temp;
			DataSetHandle dataset = (DataSetHandle) dataField.getElementHandle( );
			DataSetHandle primary = ( (TabularCubeHandle) input ).getDataSet( );

			TreeSelection slections = (TreeSelection) groupViewer.getSelection( );
			Iterator iter = slections.iterator( );
			while ( iter.hasNext( ) )
			{
				Object obj = iter.next( );
				if ( obj instanceof TabularLevelHandle )
				{
					if ( isDateType( dataField.getDataType( ) ) )
						continue;
					if ( ( (TabularLevelHandle) obj ).getDataType( ) != null
							&& isDateType( ( (TabularLevelHandle) obj ).getDataType( ) ) )
						continue;

					TabularHierarchyHandle hierarchy = ( (TabularHierarchyHandle) ( (TabularLevelHandle) obj ).getContainer( ) );
					DataSetHandle dasetTemp = hierarchy.getDataSet( );
					if ( dasetTemp != null
							&& dataset != null
							&& dataset != dasetTemp )
					{
						continue;
					}

					if ( hierarchy.getDataSet( ) == null )
					{
						try
						{
							hierarchy.setDataSet( dataset );
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
						}
					}
					TabularLevelHandle level = DesignElementFactory.getInstance( )
							.newTabularLevel( dataField.getColumnName( ) );
					try
					{
						level.setColumnName( dataField.getColumnName( ) );
						level.setDataType( dataField.getDataType( ) );
						( (TabularLevelHandle) obj ).getContainer( )
								.add( IHierarchyModel.LEVELS_PROP,
										level,
										( (TabularLevelHandle) obj ).getIndex( ) + 1 );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
					refresh( );
					return;
				}
				else if ( obj instanceof DimensionHandle
						|| ( obj instanceof VirtualField && ( (VirtualField) obj ).getType( )
								.equals( VirtualField.TYPE_LEVEL ) ) )
				{
					DimensionHandle dimension = null;
					if ( obj instanceof DimensionHandle )
						dimension = (DimensionHandle) obj;
					else
						dimension = (DimensionHandle) ( (VirtualField) obj ).getModel( );

					TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension.getContent( IDimensionModel.HIERARCHIES_PROP,
							0 );

					if ( hierarchy.getContentCount( IHierarchyModel.LEVELS_PROP ) > 0 )
					{
						if ( dataField != null
								&& isDateType( dataField.getDataType( ) ) )
						{
							continue;
						}
						LevelHandle level = (LevelHandle) hierarchy.getContent( IHierarchyModel.LEVELS_PROP,
								0 );
						String dataType = level.getDataType( );
						if ( dataType != null && isDateType( dataType ) )
						{
							continue;
						}
					}

					DataSetHandle dasetTemp = hierarchy.getDataSet( );
					if ( dasetTemp != null
							&& dataset != null
							&& dataset != dasetTemp )
					{
						continue;
					}

					if ( hierarchy.getDataSet( ) == null )
					{
						try
						{
							hierarchy.setDataSet( dataset );
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
						}
					}

					try
					{
						if ( isDateType( dataField.getDataType( ) ) )
						{
							if ( hierarchy.getContentCount( IHierarchyModel.LEVELS_PROP ) > 0 )
							{
								continue;
							}
							else
							{
								hierarchy.add( HierarchyHandle.LEVELS_PROP,
										OlapUtil.getDateLevel( dataField,
												OlapUtil.Level_Year ) );
								hierarchy.add( HierarchyHandle.LEVELS_PROP,
										OlapUtil.getDateLevel( dataField,
												OlapUtil.Level_Qtr ) );
								hierarchy.add( HierarchyHandle.LEVELS_PROP,
										OlapUtil.getDateLevel( dataField,
												OlapUtil.Level_Month ) );
								hierarchy.add( HierarchyHandle.LEVELS_PROP,
										OlapUtil.getDateLevel( dataField,
												OlapUtil.Level_Week ) );
								hierarchy.add( HierarchyHandle.LEVELS_PROP,
										OlapUtil.getDateLevel( dataField,
												OlapUtil.Level_Day ) );
								( (TabularDimensionHandle) hierarchy.getContainer( ) ).setTimeType( true );
							}
						}
						else
						{
							TabularLevelHandle level = DesignElementFactory.getInstance( )
									.newTabularLevel( dataField.getColumnName( ) );
							level.setColumnName( dataField.getColumnName( ) );
							level.setDataType( dataField.getDataType( ) );
							hierarchy.add( IHierarchyModel.LEVELS_PROP, level );
						}
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
					refresh( );
					return;
				}
				else if ( dataset != null
						&& primary != null
						&& dataset == primary )
				{
					if ( obj instanceof MeasureGroupHandle
							|| ( obj instanceof VirtualField && ( (VirtualField) obj ).getType( )
									.equals( VirtualField.TYPE_MEASURE ) ) )
					{
						MeasureGroupHandle measureGroup = null;
						if ( obj instanceof MeasureGroupHandle )
							measureGroup = (MeasureGroupHandle) obj;
						else
							measureGroup = (MeasureGroupHandle) ( (VirtualField) obj ).getModel( );

						MeasureHandle measure = DesignElementFactory.getInstance( )
								.newTabularMeasure( dataField.getColumnName( ) );
						try
						{
							measure.setMeasureExpression( DEUtil.getExpression( dataField ) );
							measure.setDataType( dataField.getDataType( ) );
							measureGroup.add( IMeasureGroupModel.MEASURES_PROP,
									measure );
						}
						catch ( SemanticException e1 )
						{
							ExceptionHandler.handle( e1 );
						}
						refresh( );
						return;
					}
					else if ( obj instanceof MeasureHandle )
					{
						TabularMeasureHandle measure = DesignElementFactory.getInstance( )
								.newTabularMeasure( dataField.getColumnName( ) );
						try
						{
							measure.setMeasureExpression( DEUtil.getExpression( dataField ) );
							measure.setDataType( dataField.getDataType( ) );
							( (MeasureHandle) obj ).getContainer( )
									.add( IMeasureGroupModel.MEASURES_PROP,
											measure );
						}
						catch ( SemanticException e1 )
						{
							ExceptionHandler.handle( e1 );
						}
						refresh( );
						return;
					}
				}

			}
		}
	}

	public void refresh( )
	{
		groupViewer.refresh( true );
	}

	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( groupViewer == null || groupViewer.getControl( ).isDisposed( ) )
		{
			return;
		}
		groupViewer.refresh( );
		expandNodeAfterCreation( ev );
		getListenerElementVisitor( ).addListener( focus );

	}

	private void expandNodeAfterCreation( NotificationEvent ev )
	{
		if ( ev instanceof ContentEvent
				&& ev.getEventType( ) == NotificationEvent.CONTENT_EVENT
				&& ( (ContentEvent) ev ).getAction( ) == ContentEvent.ADD )
		{
			IDesignElement element = ( (ContentEvent) ev ).getContent( );
			if ( element != null )
			{
				final DesignElementHandle handle = element.getHandle( input.getModule( ) );
				groupViewer.expandToLevel( handle,
						AbstractTreeViewer.ALL_LEVELS );
				groupViewer.setSelection( new StructuredSelection( handle ),
						true );
			}
		}
	}
}
