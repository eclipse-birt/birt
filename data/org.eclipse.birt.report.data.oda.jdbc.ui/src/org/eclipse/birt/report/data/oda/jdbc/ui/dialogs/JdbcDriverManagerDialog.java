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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JDBCDriverInformation;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JdbcToolKit;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.core.runtime.Platform;
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
import org.osgi.framework.Bundle;

/**
 * A dialog to manage Jdbc drivers.
 */

public class JdbcDriverManagerDialog extends Dialog
{

	private TableViewer jarViewer, driverViewer;

	private Button addButton, restoreButton, deleteButton, editButton;

	private Map jarMap, driverMap;

	/**
	 * The key for JAR files map property in preference store.
	 */
	public static final String JAR_MAP_PREFERENCE_KEY = "JDBC Jar List"; //$NON-NLS-1$

	/**
	 * The key for drivers map property in preference store.
	 */
	public static final String DRIVER_MAP_PREFERENCE_KEY = "JDBC Driver Map"; //$NON-NLS-1$

	/**
	 * The key for BIRT viewer drivers path property in plugin.properties.
	 */
	public static final String VIEWER_DRIVER_PATH_KEY = "birt-viewer-driver-path"; //$NON-NLS-1$

	private static final String ORIGINAL_FILE_NOT_EXIST_TOKEN = "*"; //$NON-NLS-1$

	private static final String ODA_FILE_NOT_EXIST_TOKEN = "x"; //$NON-NLS-1$

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
	 * Returns the viewer drivers directory path. <br>
	 * TODO: may change if viewer plugin provide more convenient api.
	 * 
	 * @return
	 */
	private File getViewerDriverLocation( )
	{
		//get the driver path under viewer plug-in.
		Bundle viewerBundle = Platform.getBundle( "org.eclipse.birt.report.viewer" ); //$NON-NLS-1$
		if ( viewerBundle != null )
		{
			ResourceBundle resBundle = Platform.getResourceBundle( JdbcPlugin.getDefault( )
					.getBundle( ) );
			if ( resBundle != null )
			{
				String driverPath = viewerBundle.getLocation( ).substring( 7 )
						+ resBundle.getString( VIEWER_DRIVER_PATH_KEY );

				return new File( driverPath );
			}
		}
		return null;
	}

	/**
	 * Returns the ODA dirvers directory path. <br>
	 */
	private File getDriverLocation( )
	{
		try
		{
			return OdaJdbcDriver.getDriverDirectory();
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( OdaException e )
		{
			ExceptionHandler.handle( e );
		}

		return null;
	}

	/**
	 * Copies the specified file to ODA driver path and viewer dirver path.
	 * 
	 * @param filePath
	 */
	private void doCopyJar( String filePath )
	{
		File source = new File( filePath );

		File odaDir = getDriverLocation( );
		File viewDir = getViewerDriverLocation( );

		File dest1 = null, dest2 = null;

		if ( odaDir != null )
		{
			dest1 = new File( odaDir.getAbsolutePath( )
					+ File.separator
					+ source.getName( ) );
		}
		if ( viewDir != null )
		{
			dest2 = new File( viewDir.getAbsolutePath( )
					+ File.separator
					+ source.getName( ) );
		}

		if ( source.exists( ) )
		{
			FileChannel in = null, out1 = null, out2 = null;
			try
			{
				if ( dest1 != null )
				{
					try
					{
						out1 = new FileOutputStream( dest1 ).getChannel( );
					}
					catch ( FileNotFoundException e )
					{
						//does nothing.
					}
				}
				if ( dest2 != null )
				{
					try
					{
						out2 = new FileOutputStream( dest2 ).getChannel( );
					}
					catch ( FileNotFoundException e )
					{
						//does nothing.
					}
				}

				if ( out1 != null )
				{
					in = new FileInputStream( source ).getChannel( );
					long size = in.size( );
					MappedByteBuffer buf = in.map( FileChannel.MapMode.READ_ONLY,
							0,
							size );
					out1.write( buf );
				}
				
				try
				{
					if ( in != null )
					{
						in.close( );
					}
				}
				catch ( IOException e1 )
				{
					//does nothing.
				}
				
				if ( out2 != null )
				{
					in = new FileInputStream( source ).getChannel( );
					long size = in.size( );
					MappedByteBuffer buf = in.map( FileChannel.MapMode.READ_ONLY,
							0,
							size );
					out2.write( buf );
				}

			}
			catch ( FileNotFoundException e )
			{
				//does nothing.
			}
			catch ( IOException e )
			{
				//does nothing.
			}
			finally
			{
				try
				{
					if ( in != null )
					{
						in.close( );
					}
					if ( out1 != null )
					{
						out1.close( );
					}
					if ( out2 != null )
					{
						out2.close( );
					}
				}
				catch ( IOException e1 )
				{
					//does nothing.
				}
			}
		}
	}

	/**
	 * Deletes the specified file from ODA driver path and viewer dirver path,
	 * NOTE just the file name is used.
	 * 
	 * @param filePath
	 */
	private void doDeleteJar( String filePath )
	{
		File source = new File( filePath );

		File odaDir = getDriverLocation( );
		File viewDir = getViewerDriverLocation( );

		File dest1 = null, dest2 = null;

		if ( odaDir != null )
		{
			dest1 = new File( odaDir.getAbsolutePath( )
					+ File.separator
					+ source.getName( ) );

			if ( dest1.exists( ) )
			{
				if ( !dest1.delete( ) )
				{
					dest1.deleteOnExit( );
				}
			}
		}
		if ( viewDir != null )
		{
			dest2 = new File( viewDir.getAbsolutePath( )
					+ File.separator
					+ source.getName( ) );
			if ( dest2.exists( ) )
			{
				if ( !dest2.delete( ) )
				{
					dest2.deleteOnExit( );
				}
			}
		}

	}

	private void updateJarMap( )
	{
		//read jar files under ODA driver directory.
		File jarPath = getDriverLocation( );

		if ( jarPath != null && jarPath.exists( ) && jarPath.isDirectory( ) )
		{
			File[] jars = jarPath.listFiles( new FileFilter( ) {

				public boolean accept( File pathname )
				{
					if ( pathname.exists( )
							&& pathname.isFile( )
							&& ( pathname.getName( ).endsWith( ".jar" ) ) ) //$NON-NLS-1$
					{
						return true;
					}

					return false;
				}
			} );

			for ( int i = 0; i < jars.length; i++ )
			{
				jarMap.put( jars[i].getName( ), new String[]{
						jars[i].getAbsolutePath( ), ""
				} ); //$NON-NLS-1$
			}
		}

		//read setting from preference store.
		try
		{
			String jarMap64 = JdbcPlugin.getDefault( )
					.getPreferenceStore( )
					.getString( JAR_MAP_PREFERENCE_KEY );

			if ( jarMap64 != null )
			{
				byte[] bytes = Base64.decodeBase64( jarMap64.getBytes( ) );

				ByteArrayInputStream bis = new ByteArrayInputStream( bytes );
				Object obj = new ObjectInputStream( bis ).readObject( );

				if ( obj instanceof Map )
				{
					jarMap.putAll( (Map) obj );
				}
			}

		}
		catch ( IOException e )
		{
			//ignore.
		}
		catch ( ClassNotFoundException e )
		{
			ExceptionHandler.handle( e );
		}

		checkJarState( );
	}

	/**
	 * Returns the user dirver info from preference store.
	 * 
	 * @return
	 */
	public static Map getPreferenceDriverInfo( )
	{
		String driverMap64 = JdbcPlugin.getDefault( )
				.getPreferenceStore( )
				.getString( DRIVER_MAP_PREFERENCE_KEY );

		try
		{
			if ( driverMap64 != null )
			{
				byte[] bytes = Base64.decodeBase64( driverMap64.getBytes( ) );

				ByteArrayInputStream bis = new ByteArrayInputStream( bytes );
				Object obj = new ObjectInputStream( bis ).readObject( );

				if ( obj instanceof Map )
				{
					return (Map) obj;
				}
			}
		}
		catch ( IOException e )
		{
			//ignore.
		}
		catch ( ClassNotFoundException e )
		{
			ExceptionHandler.handle( e );
		}

		return new HashMap( );
	}

	private void updateDriverMap( )
	{
		ArrayList driverList = JdbcToolKit.getJdbcDriverNames( OdaJdbcDriver.DATA_SOURCE_ID );
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

		driverMap.putAll( getPreferenceDriverInfo( ) );
	}

	private void initialize( )
	{
		jarMap = new HashMap( );
		driverMap = new HashMap( );

		updateJarMap( );
		updateDriverMap( );

		jarViewer.setInput( jarMap );
		driverViewer.setInput( driverMap );

		refreshJarViewer( );
		refreshDriverViewer( );

		updateJarButtons( );
		updateDriverButtons( );
	}

	private boolean isUnderODAPath( File f )
	{
		File odaPath = getDriverLocation( );

		File ff = new File( odaPath + File.separator + f.getName( ) );

		return ff.exists( );
	}

	private void checkJarState( )
	{
		// check if the jar exist in the oda driver directory or exist in the
		// disk.
		// x - not exist in the oda dirctory.
		// * - not exist in the disk.
		// <br>
		// TODO further work, use state flag to mark state.
		for ( Iterator itr = jarMap.values( ).iterator( ); itr.hasNext( ); )
		{
			String[] vals = (String[]) itr.next( );

			File f = new File( vals[0] );

			if ( !isUnderODAPath( f ) )
			{
				if ( f.exists( ) )
				{
					vals[1] = ODA_FILE_NOT_EXIST_TOKEN;
				}
				else
				{
					vals[1] = ODA_FILE_NOT_EXIST_TOKEN
							+ ORIGINAL_FILE_NOT_EXIST_TOKEN;
				}
			}
			else
			{
				if ( f.exists( ) )
				{
					vals[1] = ""; //$NON-NLS-1$
				}
				else
				{
					vals[1] = ORIGINAL_FILE_NOT_EXIST_TOKEN;
				}
			}
		}

	}

	private void addTabPages( TabFolder tabFolder )
	{
		addJarPage( tabFolder );
		addDriverPage( tabFolder );
	}

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

		final TableColumn column2 = new TableColumn( table, SWT.NONE );
		column2.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.jarColumnLocation" ) ); //$NON-NLS-1$
		column2.setWidth( 280 );

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
		data.widthHint = 60;
		data.heightHint = 24;
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
		data.widthHint = 60;
		data.heightHint = 24;
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
		data.widthHint = 60;
		data.heightHint = 24;
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

		TableColumn column2 = new TableColumn( table, SWT.NONE );
		column2.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.driverColumnDisplayName" ) ); //$NON-NLS-1$
		column2.setWidth( 100 );

		TableColumn column3 = new TableColumn( table, SWT.NONE );
		column3.setText( JdbcPlugin.getResourceString( "driverManagerDialog.text.driverColumnTemplate" ) ); //$NON-NLS-1$
		column3.setWidth( 100 );

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
		data.widthHint = 60;
		data.heightHint = 24;
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

	private void updateJarButtons( )
	{
		restoreButton.setEnabled( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( )
				&& ODA_FILE_NOT_EXIST_TOKEN.equals( jarViewer.getTable( )
						.getItem( jarViewer.getTable( ).getSelectionIndex( ) )
						.getText( ) ) );

		deleteButton.setEnabled( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( ) );
	}

	private void updateDriverButtons( )
	{
		editButton.setEnabled( driverViewer.getTable( ).getSelectionIndex( ) >= 0
				&& driverViewer.getTable( ).getSelectionIndex( ) < driverViewer.getTable( )
						.getItemCount( ) );

	}

	private void addJar( )
	{
		FileDialog dlg = new FileDialog( getShell( ) );
		dlg.setFilterExtensions( new String[]{
			"*.jar" //$NON-NLS-1$
			} );

		if ( dlg.open( ) != null )
		{
			String fn = dlg.getFilterPath( )
					+ File.separator
					+ dlg.getFileName( );

			if ( jarMap.containsKey( dlg.getFileName( ) ) )
			{
				//TODO propmpt the duplicate.
			}

			doCopyJar( fn );

			jarMap.put( dlg.getFileName( ), new String[]{
					fn, ""
			} ); //$NON-NLS-1$

			jarViewer.setInput( jarMap );

			refreshJarViewer( );

			updateJarButtons( );

			updateDriverMap( );

			refreshDriverViewer( );

			updateDriverButtons( );

		}
	}

	private void restoreJar( )
	{
		//restore jar.
		if ( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( ) )
		{
			Map.Entry fn = (Map.Entry) jarViewer.getTable( ).getSelection( )[0].getData( );

			doCopyJar( ( (String[]) fn.getValue( ) )[0] );

			checkJarState( );

			refreshJarViewer( );

			updateJarButtons( );

			updateDriverMap( );

			refreshDriverViewer( );

			updateDriverButtons( );
		}
	}

	private void deleteJar( )
	{
		if ( jarViewer.getTable( ).getSelectionIndex( ) >= 0
				&& jarViewer.getTable( ).getSelectionIndex( ) < jarViewer.getTable( )
						.getItemCount( ) )
		{
			int idx = jarViewer.getTable( ).getSelectionIndex( );

			Map.Entry fn = (Map.Entry) jarViewer.getTable( ).getSelection( )[0].getData( );

			jarMap.remove( fn.getKey( ) );

			jarViewer.getTable( ).remove( idx );

			jarViewer.refresh( );

			if ( idx >= jarViewer.getTable( ).getItemCount( ) )
			{
				idx--;
			}

			jarViewer.getTable( ).select( idx );

			doDeleteJar( ( (String[]) fn.getValue( ) )[0] );

			refreshJarViewer( );

			updateJarButtons( );

			updateDriverMap( );

			refreshDriverViewer( );

			updateDriverButtons( );

		}

	}

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

	private void refreshJarViewer( )
	{
		for ( int i = 0; i < jarViewer.getTable( ).getItemCount( ); i++ )
		{
			TableItem ti = jarViewer.getTable( ).getItem( i );

			Object element = ti.getData( );

			String c0 = "", c1 = "", c2 = ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			if ( element instanceof Map.Entry )
			{
				Map.Entry entry = (Map.Entry) element;

				c1 = (String) entry.getKey( );

				String[] vals = (String[]) entry.getValue( );

				c2 = vals[0];
				c0 = vals[1];
			}

			ti.setText( 0, c0 );
			ti.setText( 1, c1 );
			ti.setText( 2, c2 );
		}

	}

	private void refreshDriverViewer( )
	{
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream( );
			new ObjectOutputStream( bos ).writeObject( jarMap );

			byte[] bytes = bos.toByteArray( );

			bytes = Base64.encodeBase64( bytes );

			JdbcPlugin.getDefault( )
					.getPreferenceStore( )
					.setValue( JAR_MAP_PREFERENCE_KEY, new String( bytes ) );

			bos = new ByteArrayOutputStream( );
			new ObjectOutputStream( bos ).writeObject( driverMap );

			bytes = bos.toByteArray( );

			bytes = Base64.encodeBase64( bytes );

			JdbcPlugin.getDefault( )
					.getPreferenceStore( )
					.setValue( DRIVER_MAP_PREFERENCE_KEY, new String( bytes ) );
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
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