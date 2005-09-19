/*******************************************************************************
 * Copyright (C) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.dialogs;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JDBCDriverInformation;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JarFile;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JdbcToolKit;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * A dialog to manage Jdbc drivers.
 */

public class JdbcDriverManagerDialog extends Dialog
{

	private TableViewer jarViewer, driverViewer;

	private Button addButton, restoreButton, deleteButton, editButton;

	private Map jarMap, driverMap;

	/**
	 * list of jars to be copied and deleted when okPressed
	 */
	private List jarsToBeCopied,jarsToBeDeleted;
	
	private static final int btnWidth = 90;
	private static final int btnHeight = 24;

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public JdbcDriverManagerDialog( Shell parentShell )
	{
		super( parentShell );

		setShellStyle( SWT.CLOSE
				| SWT.TITLE
				| SWT.BORDER
				| SWT.APPLICATION_MODAL
				| SWT.RESIZE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#create()
	 */
	public void create( )
	{
		super.create( );

		Point pt = getShell( ).computeSize( -1, -1 );
		pt.y = Math.max( pt.y, 400 );
		getShell( ).setSize( pt );
		getShell( ).setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.title" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		TabFolder tabFolder = new TabFolder( composite, SWT.TOP );
		tabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		// add pages
		addTabPages( tabFolder );

		initialize( );

		return composite;
	}

	/**
	 * Add jarPage and driverPage
	 * @param tabFolder parent Composite
	 */
	private void addTabPages( TabFolder tabFolder )
	{
		addJarPage( tabFolder );
		addDriverPage( tabFolder );
	}
	
	/**
	 * add Jar Page to the Dialog
	 */
	private void addJarPage( TabFolder tabFolder )
	{
		final Composite page = new Composite( tabFolder, SWT.NONE );

		GridLayout layout = new GridLayout( );
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.verticalSpacing = 5;
		page.setLayout( layout );
		page.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		final Table table = new Table( page, SWT.BORDER | SWT.FULL_SELECTION );

		GridData data = new GridData( GridData.FILL_BOTH );
		table.setLayoutData( data );

		table.setHeaderVisible( true );
		table.setLinesVisible( true );

		TableLayout tableLayout = new TableLayout( );
		table.setLayout( tableLayout );

		final TableColumn column0 = new TableColumn( table, SWT.NONE );
		column0.setWidth( 20 );

		final TableColumn column1 = new TableColumn( table, SWT.NONE );
		column1.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.jarColumnFileName" ) ); //$NON-NLS-1$
		column1.setWidth( 150 );
		column1.addSelectionListener( new SelectionListener( ) {
			private boolean asc = false;
			
			public void widgetSelected( SelectionEvent e )
			{
				sortJar( 1, asc );
				asc = !asc;
			}
			public void widgetDefaultSelected( SelectionEvent e )
			{
				widgetSelected( e );
			}
		} );

		final TableColumn column2 = new TableColumn( table, SWT.NONE );
		column2.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.jarColumnLocation" ) ); //$NON-NLS-1$
		column2.setWidth( 280 );
		column2.addSelectionListener( new SelectionListener( ) {
			private boolean asc = false;

			public void widgetSelected( SelectionEvent e )
			{
				sortJar( 2, asc );
				asc = !asc;
			}
			public void widgetDefaultSelected( SelectionEvent e )
			{
				widgetSelected( e );
			}
		} );

		jarViewer = new TableViewer( table );
		jarViewer.setContentProvider( new IStructuredContentProvider( ) {

			public Object[] getElements( Object inputElement )
			{
				if ( inputElement instanceof Map )
				{
					return ( (Map) inputElement ).entrySet( ).toArray( );
				}

				return new Object[0];
			}

			public void inputChanged( Viewer viewer, Object oldInput,
					Object newInput )
			{
			}

			public void dispose( )
			{
			}
		} );

		jarViewer.setSorter( null );

		jarViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				updateJarButtons( );
			}
		} );

		Label lb = new Label( page, SWT.NONE );
		lb.setText( JdbcPlugin.getResourceString( "driverManagerDialog.message.NotInODADirectory" ) ); //$NON-NLS-1$

		lb = new Label( page, SWT.NONE );
		lb.setText( JdbcPlugin.getResourceString( "driverManagerDialog.message.FileNotExist" ) ); //$NON-NLS-1$

		lb = new Label( page, SWT.NONE );
		lb.setText( JdbcPlugin.getResourceString( "driverManagerDialog.message.FileRestored" ) ); //$NON-NLS-1$

		Composite buttons = new Composite( page, SWT.NONE );
		buttons.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
		layout = new GridLayout( );
		layout.numColumns = 4;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout( layout );

		addButton = new Button( buttons, SWT.PUSH );
		addButton.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.Add" ) ); //$NON-NLS-1$
		data = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
		data.widthHint = btnWidth;
		data.heightHint = btnHeight;
		addButton.setLayoutData( data );
		addButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				addJar( );
			}
		} );

		restoreButton = new Button( buttons, SWT.PUSH );
		restoreButton.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.Restore" ) ); //$NON-NLS-1$
		data = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
		data.widthHint = btnWidth;
		data.heightHint = btnHeight;
		restoreButton.setLayoutData( data );
		restoreButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				restoreJar( );
			}
		} );

		deleteButton = new Button( buttons, SWT.PUSH );
		deleteButton.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.Delete" ) ); //$NON-NLS-1$
		data = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
		data.widthHint = btnWidth;
		data.heightHint = btnHeight;
		deleteButton.setLayoutData( data );
		deleteButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				deleteJar( );
			}
		} );

		TabItem tabItem = new TabItem( tabFolder, SWT.NONE );
		tabItem.setControl( page );
		tabItem.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.JarFile" ) ); //$NON-NLS-1$
	}
	
	/**
	 * Add Driver Page to the Dialog
	 * @param tabFolder tab Composite
	 */
	private void addDriverPage( TabFolder tabFolder )
	{
		Composite page = new Composite( tabFolder, SWT.NONE );

		GridLayout layout = new GridLayout( );
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.verticalSpacing = 5;
		page.setLayout( layout );
		page.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		final Table table = new Table( page, SWT.BORDER | SWT.FULL_SELECTION );

		GridData data = new GridData( GridData.FILL_BOTH );
		table.setLayoutData( data );

		table.setHeaderVisible( true );
		table.setLinesVisible( true );

		TableLayout tableLayout = new TableLayout( );
		table.setLayout( tableLayout );

		TableColumn column0 = new TableColumn( table, SWT.NONE );
		column0.setWidth( 20 );

		TableColumn column1 = new TableColumn( table, SWT.NONE );
		column1.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.driverColumnClassName" ) ); //$NON-NLS-1$
		column1.setWidth( 300 );
		column1.addSelectionListener(new SelectionListener( ) {
			private boolean asc = false;

			public void widgetSelected( SelectionEvent e )
			{
				sortDriver( 1, asc );
				asc = !asc;
			}
			public void widgetDefaultSelected( SelectionEvent e )
			{
				widgetSelected( e );
			}
		} );

		TableColumn column2 = new TableColumn( table, SWT.NONE );
		column2.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.driverColumnDisplayName" ) ); //$NON-NLS-1$
		column2.setWidth( 100 );
		column2.addSelectionListener( new SelectionListener( ) {
			private boolean asc = false;

			public void widgetSelected( SelectionEvent e )
			{
				sortDriver( 2, asc );
				asc = !asc;
			}
			public void widgetDefaultSelected( SelectionEvent e )
			{
				widgetSelected( e );
			}
		} );

		TableColumn column3 = new TableColumn( table, SWT.NONE );
		column3.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.driverColumnTemplate" ) ); //$NON-NLS-1$
		column3.setWidth( 100 );
		column3.addSelectionListener( new SelectionListener( ) {
			private boolean asc = false;

			public void widgetSelected( SelectionEvent e )
			{
				sortDriver( 3, asc );
				asc = !asc;
			}
			public void widgetDefaultSelected( SelectionEvent e )
			{
				widgetSelected( e );
			}
		} );

		driverViewer = new TableViewer( table );
		driverViewer.setContentProvider( new IStructuredContentProvider( ) {

			public Object[] getElements( Object inputElement )
			{
				if ( inputElement instanceof Map )
				{
					return ( (Map) inputElement ).entrySet( ).toArray( );
				}
				return new Object[0];
			}

			public void inputChanged( Viewer viewer, Object oldInput,
					Object newInput )
			{
			}

			public void dispose( )
			{
			}
		} );

		driverViewer.setSorter( null );

		driverViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				updateDriverButtons( );
			}
		} );
		driverViewer.addDoubleClickListener( new IDoubleClickListener( ) {

			public void doubleClick( DoubleClickEvent event )
			{
				editDriver( );
			}
		} );

		Composite buttons = new Composite( page, SWT.NONE );
		buttons.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
		layout = new GridLayout( );
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout( layout );

		editButton = new Button( buttons, SWT.PUSH );
		editButton.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.Edit" ) ); //$NON-NLS-1$
		data = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
		data.widthHint = btnWidth;
		data.heightHint = btnHeight;
		editButton.setLayoutData( data );
		editButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				editDriver( );
			}
		} );

		TabItem tabItem = new TabItem( tabFolder, SWT.NONE );
		tabItem.setControl( page );
		tabItem.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.Drivers" ) ); //$NON-NLS-1$
	}
	
	/**
	 * initialize jarMap,driverMap and Viewer
	 */
	private void initialize( )
	{
		jarMap = new HashMap( );
		driverMap = new HashMap( );
		jarsToBeCopied = new ArrayList( );
		jarsToBeDeleted = new ArrayList( );
		
		updateJarMap( );
		updateDriverMap( );

		refreshJarViewer( );
		refreshDriverViewer( );

		updateJarButtons( );
		updateDriverButtons( );
	}
	
	/**
	 * update jarMap, read setting from preference store. Jar files under ODA
	 * driver directory will be displayed.
	 */
	private void updateJarMap( )
	{
		File jarPath = JarFile.getDriverLocation( );
		
		if ( jarPath != null && jarPath.exists( ) && jarPath.isDirectory( ) )
		{
			File[] jars = jarPath.listFiles( new FileFilter( ) {
				Map deletedJars = Utility.getPreferenceStoredMap( JdbcPlugin.DELETED_JAR_MAP_PREFERENCE_KEY );

				public boolean accept( File pathname )
				{
					if ( pathname.exists( )
							&& pathname.isFile( )
							&& ( !deletedJars.containsKey( pathname.getName( ) ) )
							&& ( pathname.getName( ).endsWith( ".jar" ) || pathname.getName( ).endsWith( ".zip" ) ) ) //$NON-NLS-1$
					{
						return true;
					}

					return false;
				}
			} );

			for ( int i = 0; i < jars.length; i++ )
				jarMap.put( jars[i].getName( ),
						new JarFile( jars[i].getAbsolutePath( ), "", false ) );
			
		}
		
//		Set entrySet = deletedJars.entrySet( );
//		Iterator iterator = entrySet.iterator( );
//		while ( iterator.hasNext( ) )
//		{
//			Map.Entry entry = (Map.Entry) iterator.next( );
//			if ( !( (JarFile) entry.getValue( ) ).isToBeDeleted() )
//				jarMap.put( entry.getKey( ), entry.getValue( ) );
//		}
		
		jarMap.putAll( Utility.getPreferenceStoredMap( JdbcPlugin.JAR_MAP_PREFERENCE_KEY ));
		
		checkJarState( );
	}
	
	/**
	 * Carry out sort operation against certain driver column
	 * @param columnIndex the column based on which the sort operation would be carried out 
	 * @param asc the sort direction
	 */	
	private void sortDriver( final int columnIndex, final boolean asc )
	{
		TableItem[] tableItems = driverViewer.getTable( ).getItems( );

		sort( columnIndex, asc, tableItems );
		String[][] records = mapTableItemsTo2DArray( tableItems );

		driverViewer.getTable( ).removeAll( );
		TableItem tableItem;
		for ( int i = 0; i < tableItems.length; i++ )
		{
			tableItem = new TableItem( driverViewer.getTable( ), SWT.NONE );
			tableItem.setText( records[i] );
		}
	}

	/**
	 * Carry out sort operation against certain jar column
	 * @param columnIndex the column based on which the sort operation would be carried out 
	 * @param asc the sort direction
	 */	
	private void sortJar( final int columnIndex, final boolean asc )
	{
		TableItem[] tableItems = jarViewer.getTable( ).getItems( );

		sort( columnIndex, asc, tableItems );
		String[][] records = mapTableItemsTo2DArray( tableItems );

		jarViewer.getTable( ).removeAll( );
		TableItem tableItem;
		for ( int i = 0; i < tableItems.length; i++ )
		{
			tableItem = new TableItem( jarViewer.getTable( ), SWT.NONE );
			tableItem.setText( records[i] );
		}
	}
	
	/**
	 * Carry out sort operation against certain column
	 * @param columnIndex the column based on which the sort operation would be carried out 
	 * @param asc the sort direction
	 */	
	private void sort( final int columnIndex, final boolean asc,
			TableItem[] items )
	{
		TableItem[] tableItems = items;

		Arrays.sort( tableItems, new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				TableItem it1 = (TableItem) o1;
				TableItem it2 = (TableItem) o2;
				int result = 0;
				if ( asc )
				{
					result = it1.getText( columnIndex )
							.compareTo( it2.getText( columnIndex ) );
				}
				else
				{
					result = it2.getText( columnIndex )
							.compareTo( it1.getText( columnIndex ) );
				}
				return result;
			}
		} );
	}
	
	/**
	 * Map TableItems to a 2-dimension array
	 * @param tableItems
	 * @return
	 */
	private String[][] mapTableItemsTo2DArray( TableItem[] tableItems )
	{
		String[][] records = new String[tableItems.length][driverViewer.getTable( ).getColumnCount( )];
		
		for ( int i = 0; i < tableItems.length; i++)
		{	
			for ( int j = 0; j < driverViewer.getTable( ).getColumnCount( ); j++ )
			{
				records[i][j] = tableItems[i].getText(j);
			}
		}
		return records;
	}
	
	/**
	 * refresh jarViewer
	 */
	private void refreshJarViewer( )
	{
		//TODO: a temporary solution for table refresh error
		jarViewer.setInput( null );
		jarViewer.setInput( jarMap );

		for ( int i = 0; i < jarViewer.getTable( ).getItemCount( ); i++ )
		{
			TableItem ti = jarViewer.getTable( ).getItem( i );

			Object element = ti.getData( );

			String c0 = "", c1 = "", c2 = ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			if ( element instanceof Map.Entry )
			{
				Map.Entry entry = (Map.Entry) element;
				JarFile jarInfo = (JarFile) entry.getValue( );
				c0 = jarInfo.getState( );
				c1 = (String) entry.getKey( );
				c2 = jarInfo.getFilePath( );
			}

			ti.setText( 0, c0 );
			ti.setText( 1, c1 );
			ti.setText( 2, c2 );
		}

	}
	
	/**
	 * check if the jar exist in the oda driver directory or exist in the disk.
	 * x - not exist in the oda dirctory. <br>* - not exist in the disk.
	 */
	private void checkJarState( )
	{
		for ( Iterator itr = jarMap.values( ).iterator( ); itr.hasNext( ); )
		{
			JarFile jarInfo = (JarFile) itr.next( );

			jarInfo.checkJarState();
		}
	}
	
	/**
	 * update driverMap
	 */
	private void updateDriverMap( )
	{
		// get drivers from ODADir
		List driverList = JdbcToolKit.getJdbcDriversFromODADir( OdaJdbcDriver.Constants.DATA_SOURCE_ID );
		
		// add drivers in to be copied Jars
		List fileList = new ArrayList( );
		for ( int i = 0; i < jarsToBeCopied.size( ); i++ )
		{
			fileList.add(new File( ( (JarFile) jarsToBeCopied.get( i ) ).getFilePath( ) ) );
		}
		driverList.addAll( JdbcToolKit.getJdbcDriverFromFile( fileList ) );
		
		// remove drivers in to be deleted Jars
		fileList.clear( );
		for ( int i = 0; i < jarsToBeDeleted.size( ); i++ )
		{
			fileList.add( new File( ( (JarFile) jarsToBeDeleted.get( i ) ).getFilePath( ) ) );

		}
		driverList.removeAll( JdbcToolKit.getJdbcDriverFromFile( fileList ) );
		
		driverMap.clear( );
		for ( Iterator itr = driverList.iterator( ); itr.hasNext( ); )
		{
			JDBCDriverInformation info = (JDBCDriverInformation) itr.next( );

			if ( !driverMap.containsKey( info.toString( ) ) )
			{
				driverMap.put( info.toString( ), new String[]{
						( info.getDisplayName( ) == null ) ? ""
								: ( info.getDisplayName( ) ),
						( info.getUrlFormat( ) == null ) ? ""
								: ( info.getUrlFormat( ) )
				} ); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	/**
	 * refresh driverViewer
	 */
	private void refreshDriverViewer( )
	{
		//TODO: a temporary solution for table refresh error
		driverViewer.setInput( null );
		driverViewer.setInput(driverMap);

		for ( int i = 0; i < driverViewer.getTable( ).getItemCount( ); i++ )
		{
			TableItem ti = driverViewer.getTable( ).getItem( i );

			Object element = ti.getData( );

			String c1 = "", c2 = "", c3 = ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			if ( element instanceof Map.Entry )
			{
				Map.Entry entry = (Map.Entry) element;

				c1 = entry.getKey( ).toString( );

				String[] vals = (String[]) entry.getValue( );

				c2 = vals[0];
				c3 = vals[1];
			}

			ti.setText( 0, "" ); //$NON-NLS-1$
			ti.setText( 1, c1 );
			ti.setText( 2, c2 );
			ti.setText( 3, c3 );
		}
	}
	
	/**
	 * Update states of jar buttons
	 */
	private void updateJarButtons( )
	{
		restoreButton.setEnabled( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( )
				&& JarFile.ODA_FILE_NOT_EXIST_TOKEN.equals( jarViewer.getTable( )
						.getItem( jarViewer.getTable( ).getSelectionIndex( ) )
						.getText( ) ) );

		deleteButton.setEnabled( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( ) );
	}

	/**
	 * Update states of driver buttons
	 */
	private void updateDriverButtons( )
	{
		editButton.setEnabled( driverViewer.getTable( ).getSelectionIndex( ) >= 0
				&& driverViewer.getTable( ).getSelectionIndex( ) < driverViewer.getTable( )
						.getItemCount( ) );
	}
	
	/**
	 * actions after add button is click in jarPage
	 */
	private void addJar( )
	{
		FileDialog dlg = new FileDialog( getShell( ) );
		dlg.setFilterExtensions( new String[]{
			"*.jar", "*.zip" //$NON-NLS-1$, $NON-NLS-2$
			} );

		if ( dlg.open( ) != null )
		{
			String fn = dlg.getFilterPath( )
					+ File.separator
					+ dlg.getFileName( );

			if ( jarMap.containsKey( dlg.getFileName( ) )
					&& !( (JarFile) jarMap.get( dlg.getFileName( ) ) ).getState( )
							.equals( JarFile.ODA_FILE_NOT_EXIST_TOKEN ) )
			{
				// duplicate,can not add a driver
				ExceptionHandler.openErrorMessageBox( JdbcPlugin.getResourceString( "driverManagerDialog.text.DriverError" ),
						JdbcPlugin.getResourceString( "driverManagerDialog.error.CanNotAddDriver" ) );
				return;
			}

			JarFile jarInfo = new JarFile( fn, "", false );

			jarsToBeCopied.add( jarInfo );
			jarMap.put( dlg.getFileName( ), jarInfo );

			refreshJarViewer( );

			updateJarButtons( );
			
			JdbcToolKit.resetJdbcDriverNames();
			
			updateDriverMap( );

			refreshDriverViewer( );

			updateDriverButtons( );
		}
	}

	/**
	 * actions after restore button is click in jarPage
	 */
	private void restoreJar( )
	{
		//restore jar.
		if ( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( ) )
		{
			Map.Entry fn = (Map.Entry) jarViewer.getTable( ).getSelection( )[0].getData( );

//			( (JarFile) fn.getValue( ) ).copyJarToODADir();
			
			( (JarFile) fn.getValue( ) ).setRestored( );
			jarsToBeCopied.add( (JarFile) fn.getValue( ) );
			
			checkJarState( );

			refreshJarViewer( );

			updateJarButtons( );

			updateDriverMap( );

			refreshDriverViewer( );

			updateDriverButtons( );
		}
	}

	/**
	 * actions after delete button is click in jarPage
	 */
	private void deleteJar( )
	{
		if ( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( ) )
		{
			int idx = jarViewer.getTable( ).getSelectionIndex( );

			Map.Entry fn = (Map.Entry) jarViewer.getTable( ).getSelection( )[0].getData( );

			( (JarFile) fn.getValue( ) ).setToBeDeleted(true);

			Utility.putPreferenceStoredMapValue( JdbcPlugin.DELETED_JAR_MAP_PREFERENCE_KEY,
					fn.getKey( ).toString( ),
					fn.getValue( ) );
//			((JarFile) fn.getValue( )).deleteJarFromODADir();
			if ( jarsToBeCopied.contains( (JarFile) fn.getValue( ) ) )
			{
				jarsToBeCopied.remove( (JarFile) fn.getValue( ) );
			}
			
			jarsToBeDeleted.add((JarFile) fn.getValue( ));
			
			jarMap.remove( fn.getKey( ) );

			jarViewer.getTable( ).remove( idx );

			jarViewer.refresh( );

			if ( idx >= jarViewer.getTable( ).getItemCount( ) )
			{
				idx--;
			}

			jarViewer.getTable( ).select( idx );

			refreshJarViewer( );

			updateJarButtons( );

			JdbcToolKit.resetJdbcDriverNames();

			updateDriverMap( );

			refreshDriverViewer( );

			updateDriverButtons( );
		}
	}
	
	/**
	 * actions of Edit Driver in Driver Page
	 */
	private void editDriver( )
	{
		if ( driverViewer.getTable( ).getSelectionIndex( ) >= 0
				&& driverViewer.getTable( ).getSelectionIndex( ) < driverViewer.getTable( )
						.getItemCount( ) )
		{
			EditJdbcDriverDialog dlg = new EditJdbcDriverDialog( getShell( ) );

			Object obj = driverViewer.getTable( )
					.getItem( driverViewer.getTable( ).getSelectionIndex( ) )
					.getData( );

			if ( obj instanceof Map.Entry )
			{
				dlg.setDriverClassName( ( (Map.Entry) obj ).getKey( )
						.toString( ) );
				dlg.setDisplayName( ( (String[]) ( (Map.Entry) obj ).getValue( ) )[0] );
				dlg.setUrlTemplate( ( (String[]) ( (Map.Entry) obj ).getValue( ) )[1] );
			}

			if ( dlg.open( ) == Window.OK )
			{
				if ( obj instanceof Map.Entry )
				{
					( (String[]) ( (Map.Entry) obj ).getValue( ) )[0] = dlg.getDisplayName( );
					( (String[]) ( (Map.Entry) obj ).getValue( ) )[1] = dlg.getUrlTemplate( );

					driverMap.put( ( (Map.Entry) obj ).getKey( ),
							( (Map.Entry) obj ).getValue( ) );
				}

				refreshDriverViewer( );

				updateDriverButtons( );
			}
		}
	}
	
    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
     */
	protected void cancelPressed( )
	{
		JdbcToolKit.resetJdbcDriverNames( );
		super.cancelPressed( );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
    	Utility.setPreferenceStoredMap( JdbcPlugin.JAR_MAP_PREFERENCE_KEY,
				jarMap );
		Utility.setPreferenceStoredMap( JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY,
				driverMap );
		for ( int i = 0; i < jarsToBeCopied.size( ); i++ )
		{
			JarFile jar = (JarFile) jarsToBeCopied.get( i );
			jar.copyJarToODADir( );
		}
		for ( int i = 0; i < jarsToBeDeleted.size( ); i++ )
		{
			JarFile jar = (JarFile) jarsToBeDeleted.get( i );
			jar.deleteJarFromODADir( );
		}
		super.okPressed( );
	}
	
	/**
	 * EditJdbcDriverDialog
	 */
	class EditJdbcDriverDialog extends Dialog
	{

		private Label classNameLabel;
		private Text displayNameText, templateText;

		private String className = ""; //$NON-NLS-1$
		private String displayName = ""; //$NON-NLS-1$
		private String template = ""; //$NON-NLS-1$

		/**
		 * The constructor.
		 * 
		 * @param parentShell
		 */
		public EditJdbcDriverDialog( Shell parentShell )
		{
			super( parentShell );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.window.Window#create()
		 */
		public void create( )
		{
			super.create( );

			Point pt = getShell( ).computeSize( -1, -1 );
			pt.x = Math.max( pt.x, 400 );
			getShell( ).setSize( pt );
			getShell( ).setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.EditDriverTitle" ) ); //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
		 */
		protected Control createDialogArea( Composite parent )
		{
			Composite composite = (Composite) super.createDialogArea( parent );

			GridLayout layout = new GridLayout( 2, false );
			layout.marginHeight = 10;
			layout.marginWidth = 10;
			layout.verticalSpacing = 5;
			composite.setLayout( layout );

			Label lb = new Label( composite, SWT.NONE );
			lb.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.EditDriverClassName" ) ); //$NON-NLS-1$

			classNameLabel = new Label( composite, SWT.NONE );
			classNameLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			classNameLabel.setText( className );

			lb = new Label( composite, SWT.NONE );
			lb.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.EditDriverDisplayName" ) ); //$NON-NLS-1$

			displayNameText = new Text( composite, SWT.BORDER );
			displayNameText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			displayNameText.setText( displayName );
			displayNameText.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					displayName = displayNameText.getText( );
				}
			} );

			lb = new Label( composite, SWT.NONE );
			lb.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.EditDriverTemplate" ) ); //$NON-NLS-1$

			templateText = new Text( composite, SWT.BORDER );
			templateText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			templateText.setText( template );
			templateText.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					template = templateText.getText( );
				}
			} );

			return composite;
		}

		void setDriverClassName( String name )
		{
			className = name;

			if ( classNameLabel != null )
			{
				classNameLabel.setText( name );
			}
		}

		void setDisplayName( String name )
		{
			displayName = name;

			if ( displayNameText != null )
			{
				displayNameText.setText( name );
			}
		}

		void setUrlTemplate( String name )
		{
			template = name;

			if ( templateText != null )
			{
				templateText.setText( name );
			}
		}

		String getDisplayName( )
		{
			return displayName == null ? "" : displayName; //$NON-NLS-1$
		}

		String getUrlTemplate( )
		{
			return template == null ? "" : template; //$NON-NLS-1$
		}

	}
}