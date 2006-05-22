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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverInfo;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JDBCDriverInformation;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JarFile;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JdbcToolKit;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
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

import com.ibm.icu.text.Collator;

/**
 * A dialog to manage Jdbc drivers.
 */

public class JdbcDriverManagerDialog extends Dialog
{

	private TableViewer jarViewer, driverViewer;

	private Button addButton, restoreButton, deleteButton, editButton;

	private Map jarMap, driverMap;
	private TabFolder tabFolder;
	
	/**
	 * a flag indicate whether the jar page has been changed
	 */
	private boolean jarChanged = false;
	private static boolean driverChanged = false;
	
	/**
	 * list of jars to be copied and deleted when okPressed
	 * will be reset everytime after pressing ok button
	 */
	private Hashtable jarsToBeCopied,jarsToBeDeleted;
	
	/**
	 * list of jars to be copied or deleted at runtime
	 * will be reset everytime after hitting driver tab
	 */
	private Hashtable jarsToBeCopiedRuntime, jarsToBeDeletedRuntime;
	
	private static final int btnWidth = 90;
	private static final int btnHeight = 24;
	
	private Comparator collator = Collator.getInstance( );
	
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
		tabFolder = new TabFolder( composite, SWT.TOP );
		tabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		// add pages
		addTabPages( tabFolder );
		tabFolder.addSelectionListener(new SelectionListener( ) {

			public void widgetSelected( SelectionEvent event )
			{
				int index = tabFolder.getSelectionIndex();
				if ( index == 1 )
				{
					refreshDriverPage( );
				}
			}

			public void widgetDefaultSelected(SelectionEvent e)
			{
				
			}
			
		});
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
	 * @param tabFolder
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
		jarsToBeCopied = new Hashtable( );
		jarsToBeDeleted = new Hashtable( );
		jarsToBeCopiedRuntime = new Hashtable( );
		jarsToBeDeletedRuntime = new Hashtable( );
		
		updateJarMap( );
		updateDriverMapInit( );

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
						new JarFile( jars[i].getName( ),
								jars[i].getAbsolutePath( ),
								"",
								false ) );
			
		}
		
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
		driverViewer.setComparator( new ViewerComparator( new Comparator( ) {

			/*
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			public int compare( Object o1, Object o2 )
			{
				String source = (String) o1;
				String target = (String) o2;
				int result = 0;
				if ( columnIndex == 1 )
					result = this.compareStr( getDriverClassName( source ),
							getDriverClassName( target ) );
				else if ( columnIndex == 2 )
					result = this.compareStr( getDisplayName( source ),
							getDisplayName( target ) );
				else if ( columnIndex == 3 )
					result = this.compareStr( getUrlTemplate( source ),
							getUrlTemplate( target ) );

				if ( !asc )
					return result;
				else
					return result *= -1;
			}

			/**
			 * @param o1
			 * @param o2
			 * @return
			 */
			private int compareStr( Object o1, Object o2 )
			{
				return collator.compare( o1, o2 );
			}
		} ) );

		refreshDriver( );
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	private String getDriverClassName( String source )
	{
		int index = source.lastIndexOf( "=" );
		if ( index != -1
				&& driverMap.containsKey( source.substring( 0, index ) ) )
			return source.substring( 0, index );
		else
			return source;
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	private String getDisplayName( String source )
	{
		DriverInfo driverInfo = (DriverInfo) driverMap.get( getDriverClassName( source ) );
		return driverInfo.getDisplayName( );
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	private String getUrlTemplate( String source )
	{
		DriverInfo driverInfo = (DriverInfo) driverMap.get( getDriverClassName( source ) );
		return driverInfo.getUrlTemplate( );
	}	
	
	/**
	 * Carry out sort operation against certain jar column
	 * @param columnIndex the column based on which the sort operation would be carried out 
	 * @param asc the sort direction
	 */	
	private void sortJar( final int columnIndex, final boolean asc )
	{
		jarViewer.setComparator( new ViewerComparator( new Comparator( ) {

			/*
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			public int compare( Object o1, Object o2 )
			{
				String source = (String) o1;
				String target = (String) o2;
				int result = 0;
				if ( columnIndex == 1 )
					result = this.compareStr( getFileName( source ),
							getFileName( target ) );
				else if ( columnIndex == 2 )
					result = this.compareStr( getFilePath( source ),
							getFilePath( target ) );

				if ( !asc )
					return result;
				else
					return result *= -1;
			}

			/**
			 * @param o1
			 * @param o2
			 * @return
			 */
			private int compareStr( Object o1, Object o2 )
			{
				return collator.compare( o1, o2 );
			}
		} ) );
		
		refreshJar( );
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	private String getFileName( String source )
	{
		int index = source.lastIndexOf( "=" );
		if ( index != -1 && jarMap.containsKey( source.substring( 0, index ) ) )
			return source.substring( 0, index );
		else
			return source;
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	private String getFilePath( String source )
	{
		JarFile jarFile = (JarFile) jarMap.get( getFileName( source ) );
		return jarFile.getFilePath( );
	}
	
	/**
	 * refresh jarViewer
	 */
	private void refreshJarViewer( )
	{
		//TODO: a temporary solution for table refresh error
		jarViewer.setInput( null );
		jarViewer.setInput( jarMap );
		
		refreshJar( );
	}
	
	/**
	 * refresh jar
	 *
	 */
	private void refreshJar( )
	{
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
	 * initialize driverMap
	 */
	private void updateDriverMapInit( )
	{
		// get drivers from ODADir
		updateDriverMap( JdbcToolKit.getJdbcDriversFromODADir( OdaJdbcDriver.Constants.DATA_SOURCE_ID ) );
	}
	
	/**
	 * update driverMap at runtime
	 */
	private void updateDriverMapRuntime( )
	{
		if ( jarsToBeCopiedRuntime.equals( jarsToBeDeletedRuntime ) )
			return;
		
		// add drivers in to be copied Jars
		List fileList = new ArrayList( );
		Iterator jarsCopyIterator = jarsToBeCopiedRuntime.values().iterator();
		while(jarsCopyIterator.hasNext())
		{
			fileList.add(new File( ( (JarFile) jarsCopyIterator.next(  ) ).getFilePath( ) ) );
		}
		JdbcToolKit.addToDriverList( fileList );

		// remove drivers in to be deleted Jars
		fileList.clear( );
		
		Iterator jarsDeleteIterator = jarsToBeDeletedRuntime.values( ).iterator( );
		while ( jarsDeleteIterator.hasNext( ) )
		{
			fileList.add( new File( ( (JarFile) jarsDeleteIterator.next( ) ).getFilePath( ) ) );
		}
		JdbcToolKit.removeFromDriverList( fileList );

		resetRuntimeJars( );
		updateDriverMap(JdbcToolKit.getDriverList());
	}
	
	/**
	 * reset runtime maps
	 *
	 */
	private void resetRuntimeJars( )
	{
		jarsToBeCopiedRuntime.clear( );
		jarsToBeDeletedRuntime.clear( );
	}
	
	/**
	 * 
	 * @param driverList
	 */
	private void updateDriverMap(List driverList )
	{
		driverMap.clear( );
		for ( Iterator itr = driverList.iterator( ); itr.hasNext( ); )
		{
			JDBCDriverInformation info = (JDBCDriverInformation) itr.next( );

			if ( !driverMap.containsKey( info.toString( ) ) )
			{
				driverMap.put( info.toString( ),
						new DriverInfo( info.toString( ),
								info.getDisplayName( ),
								info.getUrlFormat( ) ) );
			}
		}
	}
	
	/**
	 * refresh driverViewer
	 */
	private void refreshDriverViewer( )
	{
		// TODO: a temporary solution for table refresh error
		driverViewer.setInput( null );
		driverViewer.setInput( driverMap );

		refreshDriver( );
	}
	
	/**
	 * refresh driver
	 *
	 */
	private void refreshDriver( )
	{
		for ( int i = 0; i < driverViewer.getTable( ).getItemCount( ); i++ )
		{
			TableItem ti = driverViewer.getTable( ).getItem( i );

			Object element = ti.getData( );

			String c1 = "", c2 = "", c3 = ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			if ( element instanceof Map.Entry )
			{
				Map.Entry entry = (Map.Entry) element;
				DriverInfo driverInfo = (DriverInfo) entry.getValue( );

				c1 = entry.getKey( ).toString( );
				c2 = driverInfo.getDisplayName( );
				c3 = driverInfo.getUrlTemplate( );
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
	 * refresh the Driver Page when driver page is selected or 'OK' button is
	 * pressed.
	 */
	private void refreshDriverPage( )
	{
		if ( jarChanged
				&& ( jarsToBeCopiedRuntime.size( ) > 0 || jarsToBeDeletedRuntime.size( ) > 0 ) )
		{
			updateDriverMapRuntime( );
			refreshDriverViewer( );
			updateDriverButtons( );
			jarChanged = false;
		}
	}
	
	/**
	 * actions after add button is click in jarPage
	 */
	private void addJar( )
	{
		jarChanged=true;
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
				MessageDialog.openError( null,
						JdbcPlugin.getResourceString( "driverManagerDialog.text.DriverError" ),
						JdbcPlugin.getResourceString( "driverManagerDialog.error.CanNotAddDriver" ) );
				return;
			}
			
			JarFile jarInfo = new JarFile( dlg.getFileName( ), fn, "", false );
			
			if ( jarsToBeDeleted.containsKey( dlg.getFileName( ) ) )
			{
				jarsToBeDeleted.remove( dlg.getFileName( ) );
			}
			else
			{
				jarsToBeCopied.put( dlg.getFileName( ), jarInfo );
			}
			
			if ( jarsToBeDeletedRuntime.containsKey( dlg.getFileName( ) ) )
			{
				jarsToBeDeletedRuntime.remove( dlg.getFileName( ) );
			}
			else
			{
				jarsToBeCopiedRuntime.put( dlg.getFileName( ), jarInfo );
			}
			
			jarMap.put( dlg.getFileName( ), jarInfo );

			refreshJarViewer( );

			updateJarButtons( );
		}
	}

	/**
	 * actions after restore button is click in jarPage
	 */
	private void restoreJar( )
	{
		jarChanged=true;

		//restore jar.
		if ( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( ) )
		{
			Map.Entry fn = (Map.Entry) jarViewer.getTable( ).getSelection( )[0].getData( );

			( (JarFile) fn.getValue( ) ).setRestored( );
			jarsToBeCopied.put( ( (JarFile) fn.getValue( ) ).getFileName( ),
					(JarFile) fn.getValue( ) );
			jarsToBeCopiedRuntime.put( ( (JarFile) fn.getValue( ) ).getFileName( ),
					(JarFile) fn.getValue( ) );			
			
			( (JarFile) fn.getValue( ) ).checkJarState( );

			refreshJarViewer( );

			updateJarButtons( );
		}
	}

	/**
	 * actions after delete button is click in jarPage
	 */
	private void deleteJar( )
	{
		jarChanged=true;

		if ( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( ) )
		{
			int idx = jarViewer.getTable( ).getSelectionIndex( );

			Map.Entry fn = (Map.Entry) jarViewer.getTable( ).getSelection( )[0].getData( );
			JarFile jarFile = (JarFile) fn.getValue( );

			jarFile.setToBeDeleted( true );

			if ( jarsToBeCopied.containsKey( jarFile.getFileName() ) )
			{
				jarsToBeCopied.remove( jarFile.getFileName( ) );
			}
			// "" and "*" should be put into jarsToBeDeleted
			// "+" should've been handled already in jarsToBeCopied
			// "x" should be deleted from the viewer alone
			else if ( !( jarFile.getState( )
					.indexOf( JarFile.ODA_FILE_NOT_EXIST_TOKEN ) != -1 ) )
			{
				jarsToBeDeleted.put( jarFile.getFileName( ), jarFile );
			}
			
			if ( jarsToBeCopiedRuntime.containsKey( jarFile.getFileName( ) ) )
			{
				jarsToBeCopiedRuntime.remove( jarFile.getFileName( ) );
			}
			// "" and "*" should be put into jarsToBeDeletedRuntime
			// "+" should've been handled already in jarsToBeCopied
			// "x" should be deleted from the viewer alone
			else if ( !( jarFile.getState( )
					.indexOf( JarFile.ODA_FILE_NOT_EXIST_TOKEN ) != -1 ) )
			{
				jarsToBeDeletedRuntime.put( jarFile.getFileName( ), jarFile );
			}
			
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

			DriverInfo driverInfo = (DriverInfo) ( (Map.Entry) obj ).getValue( );
			if ( obj instanceof Map.Entry )
			{
				dlg.setDriverClassName( ( (Map.Entry) obj ).getKey( )
						.toString( ) );
				dlg.setDisplayName( driverInfo.getDisplayName( ) );
				dlg.setUrlTemplate( driverInfo.getUrlTemplate( ) );
			}

			if ( dlg.open( ) == Window.OK )
			{
				if ( obj instanceof Map.Entry
						&& ( !dlg.getDisplayName( )
								.trim( )
								.equals( driverInfo.getDisplayName( ).trim( ) ) || !dlg.getUrlTemplate( )
								.trim( )
								.equals( driverInfo.getUrlTemplate( ).trim( ) ) ) )
				{
					driverInfo.setDisplayName( dlg.getDisplayName( ) );
					driverInfo.setUrlTemplate( dlg.getUrlTemplate( ) );

					driverMap.put( ( (Map.Entry) obj ).getKey( ), driverInfo );
					Utility.setPreferenceStoredMap( JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY,
							driverMap );
					driverChanged = true;
				}
				refreshDriverViewer( );
				updateDriverButtons( );
			}
		}
	}
	
	/**
	 * check if reset preferences is needed
	 * 
	 * @return
	 */
	public static boolean needResetPreferences( )
	{
		return driverChanged;
	}
	
	/**
	 * reset DriverChanged Status back to false
	 * 
	 */
	public static void resetDriverChangedStatus( )
	{
		driverChanged = false;
	}
	
	
    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
     */
	protected void cancelPressed( )
	{
		super.cancelPressed( );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		if ( !jarChanged
				&& jarsToBeCopied.size( ) == 0 && jarsToBeDeleted.size( ) == 0
				&& jarsToBeCopiedRuntime.size( ) == 0
				&& jarsToBeDeletedRuntime.size( ) == 0 )
		{
			super.okPressed( );
		}
		else
		{
			if ( jarsToBeDeleted.values( ).iterator( ).hasNext( ) )
			{
				MessageDialog.openInformation( getShell( ),
						JdbcPlugin.getResourceString( "driverManagerDialog.fileDelete.title" ),
						JdbcPlugin.getResourceString( "driverManagerDialog.fileDelete.text" ) );
			}

			okPressedProcess( );
		}
	}
	
	/**
	 * processes after pressing ok button
	 *
	 */
	private void okPressedProcess( )
	{
		Utility.setPreferenceStoredMap( JdbcPlugin.JAR_MAP_PREFERENCE_KEY,
				jarMap );
		Utility.setPreferenceStoredMap( JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY,
				driverMap );

		Iterator jarsCopyIterator = jarsToBeCopied.values( ).iterator( );
		while ( jarsCopyIterator.hasNext( ) )
		{
			JarFile jar = (JarFile) jarsCopyIterator.next( );
			jar.copyJarToODADir( );
			Utility.removeMapEntryFromPreferenceStoredMap( JdbcPlugin.DELETED_JAR_MAP_PREFERENCE_KEY,
					jar.getFileName( ) );
		}

		Iterator jarsDeleteIterator = jarsToBeDeleted.values( ).iterator( );
		while ( jarsDeleteIterator.hasNext( ) )
		{
			JarFile jar = (JarFile) jarsDeleteIterator.next( );
			jar.deleteJarFromODADir( );
			Utility.putPreferenceStoredMapValue( JdbcPlugin.DELETED_JAR_MAP_PREFERENCE_KEY,
					jar.getFileName( ),
					jar );
		}

		refreshDriverPage( );
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

		/**
		 * 
		 * @param name
		 */
		void setDriverClassName( String name )
		{
			className = name;

			if ( classNameLabel != null )
			{
				classNameLabel.setText( name );
			}
		}

		/**
		 * 
		 * @param name
		 */
		void setDisplayName( String name )
		{
			displayName = name;

			if ( displayNameText != null )
			{
				displayNameText.setText( name );
			}
		}

		/**
		 * 
		 * @param name
		 */
		void setUrlTemplate( String name )
		{
			template = name;

			if ( templateText != null )
			{
				templateText.setText( name );
			}
		}

		/**
		 * 
		 * @return
		 */
		String getDisplayName( )
		{
			return displayName == null ? "" : displayName; //$NON-NLS-1$
		}

		/**
		 * 
		 * @return
		 */
		String getUrlTemplate( )
		{
			return template == null ? "" : template; //$NON-NLS-1$
		}
	}
}