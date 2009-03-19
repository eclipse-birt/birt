/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.property.AbstractDescriptionPropertyPage;
import org.eclipse.birt.report.designer.data.ui.util.ControlProvider;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * Property page to edit output columns for all oda data sets.
 */
public final class OutputColumnsPage extends AbstractDescriptionPropertyPage
		implements
			Listener
{

	private transient PropertyHandleTableViewer viewer = null;
	// to indicate the status of dataset handle
	private transient boolean modelChanged = true;

	private String originalAlias = ""; //$NON-NLS-1$
	private String originalDisplayName = ""; //$NON-NLS-1$
	private String originalDisplayNameKey = ""; //$NON-NLS-1$

	private static String[] cellLabels = new String[]{
			Messages.getString( "dataset.editor.title.name" ),//$NON-NLS-1$
			Messages.getString( "dataset.editor.title.type" ),//$NON-NLS-1$
			Messages.getString( "dataset.editor.title.alias" ), //$NON-NLS-1$
			Messages.getString( "dataset.editor.title.displayName" ), //$NON-NLS-1$
			Messages.getString( "dataset.editor.title.displayNameKey" ) //$NON-NLS-1$
	};

	private static String[] dialogLabels = new String[]{
		Messages.getString( "dataset.editor.inputDialog.name" ),//$NON-NLS-1$
		Messages.getString( "dataset.editor.inputDialog.type" ),//$NON-NLS-1$
		Messages.getString( "dataset.editor.inputDialog.alias" ), //$NON-NLS-1$
		Messages.getString( "dataset.editor.inputDialog.displayName" ), //$NON-NLS-1$
		Messages.getString( "dataset.editor.inputDialog.displayNameKey" ) //$NON-NLS-1$
};

	private static String[] cellProperties = new String[]{
			"name", "dataTypeDisplayName", "alias", "realDisplayName", "displayNameKey"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	};

	/**
	 * The constructor.
	 */
	public OutputColumnsPage( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents( Composite parent )
	{
		viewer = new PropertyHandleTableViewer( parent, false, true, false );

		TableColumn column = new TableColumn( viewer.getViewer( ).getTable( ),
				SWT.LEFT );
		column.setText( cellLabels[0] ); //$NON-NLS-1$
		column.setWidth( 100 );
		column = new TableColumn( viewer.getViewer( ).getTable( ), SWT.LEFT );
		column.setText( cellLabels[1] ); //$NON-NLS-1$
		column.setWidth( 100 );
		column = new TableColumn( viewer.getViewer( ).getTable( ), SWT.LEFT );
		column.setText( cellLabels[2] ); //$NON-NLS-1$
		column.setWidth( 100 );
		column = new TableColumn( viewer.getViewer( ).getTable( ), SWT.LEFT );
		column.setText( cellLabels[3] ); //$NON-NLS-1$
		column.setWidth( 100 );
		column = new TableColumn( viewer.getViewer( ).getTable( ), SWT.LEFT );
		column.setText( cellLabels[4] ); //$NON-NLS-1$
		column.setWidth( 150 );

		// TODO Add support to helpText after release 1
		// column = new TableColumn( outputColumnsViewer.getViewer( ).getTable(
		// ), SWT.LEFT );
		// column.setText( Messages.getString( "dataset.editor.title.helpText" )
		// ); //$NON-NLS-1$
		// column.setWidth( 100 );

		viewer.getViewer( )
				.setContentProvider( new OutputColumnsContentProvider( ) );
		viewer.getViewer( )
				.setLabelProvider( new OutputColumnsLabelProvider( ) );
		addListeners( );
		( (DataSetHandle) getContainer( ).getModel( ) ).addListener( this );
		return viewer.getControl( );
	}

	private void addListeners( )
	{
		viewer.getNewButton( ).addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				doEdit( );
			}
		} );

		viewer.getViewer( ).getTable( ).addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
			{
				doEdit( );
			}
		} );
	}

	private void doEdit( )
	{
		int index = viewer.getViewer( ).getTable( ).getSelectionIndex( );
		if ( index == -1 )
			return;

		DataSetViewData data = (DataSetViewData) viewer.getViewer( )
				.getTable( )
				.getItem( index )
				.getData( );
		originalAlias = data.getAlias( );
		originalDisplayName = data.getRealDisplayName( );
		originalDisplayNameKey = data.getDisplayNameKey( );

		doEdit( data );
	}

	private void doEdit( DataSetViewData data )
	{
		OutputColumnInputDialog dlg = new OutputColumnInputDialog( data );

		if ( dlg.open( ) == Window.OK )
		{
			viewer.getViewer( ).update( data, null );
			updateMessage( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#pageActivated()
	 */
	public void pageActivated( )
	{
		if ( modelChanged )
		{
			updateOutputColumns( );
			modelChanged = false;
		}
		setPageProperties( );
		getContainer( ).setMessage( Messages.getString( "dataset.editor.outputColumns" ), //$NON-NLS-1$
				IMessageProvider.NONE );
		viewer.getViewer( ).getTable( ).select( 0 );
	}

	private void setPageProperties( )
	{
		viewer.getNewButton( )
				.setEnabled( viewer.getViewer( ).getInput( ) != null
						&& ( (DataSetViewData[]) viewer.getViewer( ).getInput( ) ).length != 0 );
		viewer.getNewButton( )
				.setText( Messages.getString( "PropertyHandleTableViewer.Button.Edit" ) );//$NON-NLS-1$
		viewer.getNewButton( )
				.setToolTipText( Messages.getString( "OutputColumnPage.toolTipText.Edit" ) );//$NON-NLS-1$

		viewer.getEditButton( ).setVisible( false );
		viewer.getRemoveButton( ).setVisible( false );
		viewer.getUpButton( ).setVisible( false );
		viewer.getDownButton( ).setVisible( false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#canLeave()
	 */
	public boolean canLeave( )
	{
		if ( isValid( ) )
		{
			saveOutputColumns( );
			if( this.modelChanged )
			{
				( (DataSetEditor) this.getContainer( ) ).updateDataSetDesign( this );
				this.modelChanged = false;
			}
			return super.canLeave( );
		}
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#performOk()
	 */
	public boolean performOk( )
	{
		if ( isValid( ) )
		{
			saveOutputColumns( );
			( (DataSetHandle) getContainer( ).getModel( ) ).removeListener( this );
			if( this.modelChanged )
			{
				( (DataSetEditor) this.getContainer( ) ).updateDataSetDesign( this );
				this.modelChanged = false;
			}
			return super.performOk( );
		}
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractDescriptionPropertyPage#getPageDescription()
	 */
	public String getPageDescription( )
	{
		return Messages.getString( "OutputColumnsPage.description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#getToolTip()
	 */
	public String getToolTip( )
	{
		return Messages.getString( "OutputColumnPage.OutputColumns.Tooltip" ); //$NON-NLS-1$
	}

	/**
	 * update output columns page
	 * 
	 */
	protected final void updateOutputColumns( )
	{
		clearOutputColumns( );
		try
		{
			populateOutputColums( );
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * populate output columns
	 * 
	 * @throws BirtException
	 */
	private void populateOutputColums( ) throws BirtException
	{
		ClassLoader oldContextLoader = Thread.currentThread( )
				.getContextClassLoader( );
		ClassLoader parentLoader = oldContextLoader;
		if ( parentLoader == null )
			parentLoader = this.getClass( ).getClassLoader( );
		ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader( parentLoader,
				( (DataSetEditor) getContainer( ) ).getHandle( )
						.getModuleHandle( ) );
		
		ModuleHandle handle = ( (DataSetEditor) getContainer( ) ).getHandle( )
				.getModuleHandle( );
		DataSessionContext context;
		if ( handle instanceof ReportDesignHandle )
		{

			Map dataSetMap = new HashMap( );
			Map dataSourceMap = new HashMap( );
			try
			{
				EngineConfig ec = new EngineConfig( );
				ec.getAppContext( )
						.put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
								newContextLoader );
				ReportEngine engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( ec );
				DataSetUIUtil.clearPropertyBindingMap( ( (DataSetEditor) getContainer( ) ).getHandle( ),
						dataSetMap,
						dataSourceMap );

				DummyEngineTask engineTask = new DummyEngineTask( engine,
						new ReportEngineHelper( engine ).openReportDesign( (ReportDesignHandle) handle ),
						handle );

				DataRequestSession session = engineTask.getDataSession( );

				engineTask.run( );
				DataSetViewData[] viewDatas = DataSetProvider.getCurrentInstance( )
						.populateAllOutputColumns( ( (DataSetEditor) getContainer( ) ).getHandle( ),
								session );
				if ( viewDatas == null )
				{
					viewDatas = ( (DataSetEditor) getContainer( ) ).getCurrentItemModel( false,
							false );
				}
				viewer.getViewer( ).setInput( viewDatas );
				engineTask.close( );
				engine.destroy( );
			}
			finally
			{
				DataSetUIUtil.resetPropertyBinding( ( (DataSetEditor) getContainer( ) ).getHandle( ),
						dataSetMap,
						dataSourceMap );
			}
		}
		else
		{
			context = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
					( (DataSetEditor) getContainer( ) ).getHandle( )
							.getModuleHandle( ) );
			DataRequestSession session = DataRequestSession.newSession( context );

			DataSetViewData[] viewDatas = DataSetProvider.getCurrentInstance( )
					.populateAllOutputColumns( ( (DataSetEditor) getContainer( ) ).getHandle( ),
							session );
			if ( viewDatas == null )
			{
				viewDatas = ( (DataSetEditor) getContainer( ) ).getCurrentItemModel( false,
						false );
			}
			viewer.getViewer( ).setInput( viewDatas );
			session.shutdown( );
		}
		
		Thread.currentThread( ).setContextClassLoader( oldContextLoader );
	}

	/**
	 * clear output columns table viewer
	 * 
	 */
	protected final void clearOutputColumns( )
	{
		viewer.getViewer( ).getTable( ).removeAll( );
	}

	/**
	 * 
	 * 
	 */
	private void saveOutputColumns( )
	{
		PropertyHandle handle = ( (DataSetHandle) getContainer( ).getModel( ) ).getPropertyHandle( DataSetHandle.COLUMN_HINTS_PROP );
		if ( viewer != null
				&& viewer.getViewer( ) != null
				&& viewer.getViewer( ).getInput( ) != null )
		{
			// Iterate through all the cached model items
			DataSetViewData[] items = (DataSetViewData[]) viewer.getViewer( )
					.getInput( );
			String columnName = null;
			boolean found = false;
			for ( int n = 0; n < items.length; n++ )
			{
				columnName = items[n].getName( );
				// Find this column name in the handle
				found = false;
				Iterator iter = handle.iterator( );
				if ( iter != null )
				{
					while ( iter.hasNext( ) && !found )
					{
						ColumnHintHandle hint = (ColumnHintHandle) iter.next( );
						if ( hint.getColumnName( ).equals( columnName ) )
						{
							found = true;
							// Update the display name
							if ( items[n].getRealDisplayName( ) == null
									|| items[n].getRealDisplayName( )
											.trim( )
											.length( ) == 0 )
							{
								hint.setDisplayName( null );
							}
							else
							{
								hint.setDisplayName( items[n].getRealDisplayName( ) );
							}
							if ( items[n].getDisplayNameKey( ) == null
									|| items[n].getDisplayNameKey( )
											.trim( )
											.length( ) == 0 )
							{
								hint.setDisplayNameKey( null );
							}
							else
							{
								hint.setDisplayNameKey( items[n].getDisplayNameKey( ) );
							}
							if ( items[n].getAlias( ) == null
									|| items[n].getAlias( ).trim( ).length( ) == 0 )
							{
								hint.setAlias( null );
							}
							else
							{
								hint.setAlias( items[n].getAlias( ) );
							}
							if ( items[n].getHelpText( ) == null
									|| items[n].getHelpText( ).trim( ).length( ) == 0 )
							{
								hint.setHelpText( null );
							}
							else
							{
								hint.setHelpText( items[n].getHelpText( ) );
							}
						}
					}
				}
				// If not found then create a new item and add it in
				if ( !found && isColumnHintRequired( items[n] ) )
				{
					ColumnHint hint = new ColumnHint( );
					hint.setProperty( ColumnHint.COLUMN_NAME_MEMBER, columnName );
					if ( items[n].getRealDisplayName( ) != null
							&& items[n].getRealDisplayName( ).trim( ).length( ) > 0 )
					{
						hint.setProperty( ColumnHint.DISPLAY_NAME_MEMBER,
								items[n].getRealDisplayName( ) );
					}
					if ( items[n].getDisplayNameKey( ) != null
							&& items[n].getDisplayNameKey( ).trim( ).length( ) > 0 )
					{
						hint.setProperty( ColumnHint.DISPLAY_NAME_ID_MEMBER,
								items[n].getDisplayNameKey( ) );
					}
					if ( items[n].getAlias( ) != null
							&& items[n].getAlias( ).trim( ).length( ) > 0 )
					{
						hint.setProperty( ColumnHint.ALIAS_MEMBER,
								items[n].getAlias( ) );
					}
					if ( items[n].getHelpText( ) != null
							&& items[n].getHelpText( ).trim( ).length( ) > 0 )
					{
						hint.setProperty( ColumnHint.HELP_TEXT_MEMBER,
								items[n].getHelpText( ) );

					}
					try
					{
						handle.addItem( hint );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
				}
			}
		}
	}

	/**
	 * The alias name is unique or not
	 * 
	 * @param newValue
	 * @return
	 */
	private boolean isUnique( String newValue )
	{

		DataSetViewData[] items = null;
		if ( viewer == null || viewer.getViewer( ) == null )
		{
			items = DataSetProvider.getCurrentInstance( )
					.getColumns( ( (DataSetEditor) getContainer( ) ).getHandle( ),
							false,
							true,
							true );
		}
		else
		{
			items = (DataSetViewData[]) viewer.getViewer( ).getInput( );
		}

		for ( int i = 0; i < items.length; i++ )
		{
			if ( ( items[i].getAlias( ) != null && items[i].getAlias( )
					.equals( newValue ) )
					|| ( items[i].getName( ) != null && items[i].getName( )
							.equals( newValue ) ) )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * validates that the new name/alias from a column hint or a new custom
	 * column doesn't conflict with existing column names or aliases
	 * 
	 */
	private boolean isValid( )
	{
		boolean validate = true;
		String newColumnNameOrAlias;
		DataSetViewData[] items = null;

		if ( viewer == null || viewer.getViewer( ) == null )
		{
			items = DataSetProvider.getCurrentInstance( )
					.getColumns( ( (DataSetEditor) getContainer( ) ).getHandle( ),
							false,
							true,
							true );
		}
		else
		{
			items = (DataSetViewData[]) viewer.getViewer( ).getInput( );
		}
		for ( int i = 0; items != null && i < items.length && validate; i++ )
		{
			newColumnNameOrAlias = items[i].getAlias( );
			if ( newColumnNameOrAlias != null
					&& newColumnNameOrAlias.length( ) > 0 )
			{
				for ( int n = 0; n < items.length; n++ )
				{
					if ( i == n )
						continue;
					if ( ( items[n].getName( ) != null && items[n].getName( )
							.equals( newColumnNameOrAlias ) )
							|| ( items[n].getAlias( ) != null && items[n].getAlias( )
									.equals( newColumnNameOrAlias ) ) )
					{
						validate = false;
						getContainer( ).setMessage( Messages.getFormattedString( "dataset.editor.error.columnOrAliasNameAlreadyUsed", //$NON-NLS-1$
								new Object[]{
										newColumnNameOrAlias,
										new Integer( n + 1 )
								} ),
								IMessageProvider.ERROR );

						break;
					}
				}
			}
		}
		return validate;
	}

	/**
	 * update message
	 * 
	 */
	private void updateMessage( )
	{
		if ( isValid( ) )
			getContainer( ).setMessage( Messages.getString( "dataset.editor.outputColumns" ), //$NON-NLS-1$
					IMessageProvider.NONE );
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	private boolean isColumnHintRequired( DataSetViewData model )
	{
		return !( ( model.getRealDisplayName( ) == null || model.getRealDisplayName( )
				.trim( )
				.length( ) == 0 )
				&& ( model.getDisplayNameKey( ) == null || model.getDisplayNameKey( )
						.trim( )
						.length( ) == 0 )
				&& ( model.getAlias( ) == null || model.getAlias( )
						.trim( )
						.length( ) == 0 ) && ( model.getHelpText( ) == null || model.getHelpText( )
				.trim( )
				.length( ) == 0 ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( focus.equals( getContainer( ).getModel( ) ) )
		{
			modelChanged = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#performCancel()
	 */
	public boolean performCancel( )
	{
		if ( viewer != null
				&& viewer.getViewer( ) != null
				&& viewer.getViewer( ).getInput( ) != null )
		{
			// Update the cached list from the model
			// We have to do this because we are directly updating the column
			// names in the cached list
			DataSetProvider manager = DataSetProvider.getCurrentInstance( );
			manager.updateModel( ( (DataSetEditor) getContainer( ) ).getHandle( ),
					(DataSetViewData[]) viewer.getViewer( ).getInput( ) );
		}

		( (DataSetHandle) getContainer( ).getModel( ) ).removeListener( this );
		return super.performCancel( );
	}

	private class OutputColumnInputDialog extends PropertyHandleInputDialog
	{

		DataSetViewData data = null;

		protected OutputColumnInputDialog( Object structureOrHandle )
		{
			super( structureOrHandle );

			data = (DataSetViewData) structureOrHandle;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.data.ui.dataset.PropertyHandleInputDialog#createCustomControls(org.eclipse.swt.widgets.Composite)
		 */
		protected void createCustomControls( Composite parent )
		{
			try
			{
				createCells( parent );
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
			}
		}

		private void createCells( Composite parent )
				throws IllegalArgumentException, IntrospectionException,
				IllegalAccessException, InvocationTargetException
		{
			for ( int i = 0; i < cellProperties.length; i++ )
			{
				ControlProvider.createLabel( parent, dialogLabels[i] );

				if ( i == 4 )
				{
					createResourceCell( parent, i );
					continue;
				}

				createTextCell( parent, i );
			}
		}

		private void createTextCell( Composite parent, final int index )
				throws IllegalArgumentException, IntrospectionException,
				IllegalAccessException, InvocationTargetException
		{
			final Text tx = ControlProvider.createText( parent,
					(String) Utility.getProperty( data, cellProperties[index] ) );
			tx.setLayoutData( ControlProvider.getGridDataWithHSpan( 2 ) );

			if ( index < 2 )// disable name and dataTypeName
			{
				tx.setEditable( false );
			}
			else
			{
				tx.addModifyListener( new ModifyListener( ) {

					public void modifyText( ModifyEvent e )
					{
						try
						{
							if ( index == 2 )
							{
								boolean isUniqueName = isUnique( tx.getText( ) );
								if ( !isUniqueName )
									updateStatus( getMiscStatus( IStatus.ERROR,
											Messages.getString( "OutputColumnPage.OutputColumns.DuplicatedName" ) ) ); //$NON-NLS-1$
								else
									updateStatus( getOKStatus( ) );
							}
							Object txText = tx.getText( );
							if ( tx.getText( ).trim( ).length( ) == 0 )
								txText = null;

							Utility.setProperty( data,
									cellProperties[index],
									txText );
						}
						catch ( Exception e1 )
						{
							ExceptionHandler.handle( e1 );
						}
					}

				} );
			}

			if ( index == 2 )
			{
				tx.setFocus( );
			}
		}

		private void createResourceCell( Composite parent, final int index )
				throws IllegalArgumentException, IntrospectionException,
				IllegalAccessException, InvocationTargetException
		{
			final Text tx = ControlProvider.createText( parent,
					(String) Utility.getProperty( data, cellProperties[index] ) );
			tx.setLayoutData( ControlProvider.getGridDataWithHSpan( 1 ) );
			tx.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					try
					{
						Utility.setProperty( data,
								cellProperties[index],
								tx.getText( ) );
					}
					catch ( Exception e1 )
					{
						ExceptionHandler.handle( e1 );
					}
				}

			} );

			SelectionAdapter listener = new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent event )
				{
					ResourceEditDialog dlg = new ResourceEditDialog( getShell( ),
							Messages.getString( "ResourceKeyDescriptor.title.SelectKey" ) ); //$NON-NLS-1$

					dlg.setResourceURL( getResourceURL( ) );

					if ( dlg.open( ) == Window.OK )
					{
						tx.setText( (String) dlg.getResult( ) );
					}
				}
			};
			Button bt = new Button( parent, SWT.PUSH );
			bt.setText( "..." ); //$NON-NLS-1$
			bt.addSelectionListener( listener );
			if ( getBaseName( ) == null )
				bt.setEnabled( false );
		}

		private URL getResourceURL( )
		{
			return SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.findResource( getBaseName( ),
							IResourceLocator.MESSAGE_FILE );
		}

		private String getBaseName( )
		{
			return SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getIncludeResource( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.data.ui.dataset.PropertyHandleInputDialog#rollback()
		 */
		protected void rollback( )
		{
			data.setAlias( originalAlias );
			data.setRealDisplayName( originalDisplayName );
			data.setDisplayNameKey( originalDisplayNameKey );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.data.ui.dataset.PropertyHandleInputDialog#validateSemantics(java.lang.Object)
		 */
		protected IStatus validateSemantics( Object structureOrHandle )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.data.ui.dataset.PropertyHandleInputDialog#validateSyntax(java.lang.Object)
		 */
		protected IStatus validateSyntax( Object structureOrHandle )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.data.ui.dataset.PropertyHandleInputDialog#getTitle()
		 */
		protected String getTitle( )
		{
			if ( (Object) data instanceof Structure )
			{
				return Messages.getString( "OutputColumnPage.title.OutputColumnInputDialog.NewOutputColumn" ); //$NON-NLS-1$
			}
			return Messages.getString( "OutputColumnPage.title.OutputColumnInputDialog.EditOutputColumn" );//$NON-NLS-1$
		}

	}

}

class OutputColumnsContentProvider implements IStructuredContentProvider
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object inputElement )
	{
		return (DataSetViewData[]) inputElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{

	}
}

class OutputColumnsLabelProvider implements ITableLabelProvider
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
	 *      int)
	 */
	public Image getColumnImage( Object element, int columnIndex )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 */
	public String getColumnText( Object element, int columnIndex )
	{
		String value = null;
		DataSetViewData item = (DataSetViewData) element;
		switch ( columnIndex )
		{
			case 0 :
			{
				value = item.getName( );
				break;
			}
			case 1 :
			{
				value = DataSetViewData.getDataTypeDisplayName( item.getDataTypeName( ) );
				break;
			}
			case 2 :
			{
				value = item.getAlias( );
				break;
			}
			case 3 :
			{
				value = item.getRealDisplayName( );
				break;
			}
			case 4 :
			{
				value = item.getDisplayNameKey( );
				break;
			}
		}
		return ( ( value == null ) ? "" : value );//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener( ILabelProviderListener listener )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
	public boolean isLabelProperty( Object element, String property )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener( ILabelProviderListener listener )
	{

	}
}