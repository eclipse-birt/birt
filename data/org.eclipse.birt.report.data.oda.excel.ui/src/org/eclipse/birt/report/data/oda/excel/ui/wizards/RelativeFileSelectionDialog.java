/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - support of relative file path
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.excel.ui.wizards;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.report.data.oda.excel.ui.i18n.Messages;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

public class RelativeFileSelectionDialog extends ElementTreeSelectionDialog
{

	private final static Image IMG_FOLDER = PlatformUI.getWorkbench( )
			.getSharedImages( )
			.getImage( ISharedImages.IMG_OBJ_FOLDER );

	private final static Image IMG_FILE = PlatformUI.getWorkbench( )
			.getSharedImages( )
			.getImage( ISharedImages.IMG_OBJ_FILE );

	private File rootFolder;
	private String[] extensions;
	private Button importButton;

	public RelativeFileSelectionDialog( Shell parent, File rootFolder,
			String[] extensions )
	{
		super( parent, new LabelProvider( ), new ContentProvider( extensions ) );
		setHelpAvailable( false );

		assert rootFolder != null;

		this.setValidator( new SelectionValidator( ) );
		this.setInput( rootFolder.getAbsolutePath( ) );
		this.setTitle( "SelectFile.Title" ); //$NON-NLS-1$
		this.rootFolder = rootFolder;
		this.extensions = extensions;
	}

	public URI getSelectedURI( ) throws URISyntaxException
	{
		Object[] selection = getResult( );
		if ( selection != null && selection.length > 0 )
		{
			if ( selection[0] instanceof File )
			{
				URI baseURI = rootFolder.toURI( );
				return baseURI.relativize( ( (File) selection[0] ).toURI( ) );
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createDialogArea(org
	 * .eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Control c = super.createDialogArea( parent );

		createImportFileArea( (Composite) c );

		this.getTreeViewer( ).expandToLevel( 2 );
		return c;
	}

	private void createImportFileArea( Composite parent )
	{
		importButton = new Button( parent, SWT.PUSH );
		importButton.setText( Messages.getString( "RelativeFileSelectionDialog.button.importFile" ) ); //$NON-NLS-1$
		importButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( org.eclipse.swt.events.SelectionEvent e )
			{
				FileDialog dialog = new FileDialog( getShell( ) );
				dialog.setFilterExtensions( extensions );

				String selectedLocation = dialog.open( );
				if ( selectedLocation != null )
				{
					File targetFolder = getSelectedFolder( );
					File srcFile = new File( selectedLocation );
					File targetFile = new File( targetFolder, srcFile.getName( ) );

					if ( targetFile.exists( )
							&& !MessageDialog.openConfirm( getShell( ),
									Messages.getString( "RelativeFileSelectionDialog.title.overwrite" ), //$NON-NLS-1$
									Messages.getString( "RelativeFileSelectionDialog.overwrite.msg" ) ) ) //$NON-NLS-1$
					{
						return;
					}

					importFile( targetFile, srcFile );

					refreshResource( targetFile );

					getTreeViewer( ).refresh( targetFolder );
					getTreeViewer( ).expandToLevel( targetFolder, 1 );
				}
			};
		} );
	}

	private void refreshResource( File file )
	{
		IPath resPath = Path.fromOSString( file.getAbsolutePath( ) );

		IResource[] res = ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.findFilesForLocation( resPath );

		if ( res.length == 0 )
		{
			res = ResourcesPlugin.getWorkspace( )
					.getRoot( )
					.findContainersForLocation( resPath );

			if ( res.length == 0 )
			{
				// not resources within the workspace
				return;
			}
		}

		try
		{
			final IResource[] targes = res;

			for ( IResource rc : targes )
			{
				rc.refreshLocal( IResource.DEPTH_INFINITE, null );
			}
		}
		catch ( Exception e )
		{
			// ignore
		}

	}

	private File getSelectedFolder( )
	{
		Object[] selection = getResult( );
		if ( selection != null && selection.length > 0 )
		{
			if ( selection[0] instanceof File )
			{
				File f = (File) selection[0];

				if ( f.isFile( ) )
				{
					return f.getParentFile( );
				}
				return f;
			}
		}
		return null;
	}

	private void importFile( final File target, final File src )
	{
		try
		{
			new ProgressMonitorDialog( getShell( ) ).run( true,
					false,
					new IRunnableWithProgress( ) {

						@Override
						public void run( IProgressMonitor monitor )
								throws InvocationTargetException,
								InterruptedException
						{
							monitor.beginTask( Messages.getString( "RelativeFileSelectionDialog.import.msg" ), //$NON-NLS-1$
									1 );

							try
							{
								doImport( target, src );
							}
							catch ( IOException e )
							{
								throw new InvocationTargetException( e );
							}
							finally
							{
								monitor.done( );
							}
						}
					} );
		}
		catch ( Exception e )
		{
			MessageDialog.openError( getShell( ),
					Messages.getString( "RelativeFileSelectionDialog.title.error" ), //$NON-NLS-1$
					e.getLocalizedMessage( ) );
		}
	}

	private void doImport( File target, File src ) throws IOException
	{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fcin = null;
		FileChannel fcout = null;

		try
		{
			fis = new FileInputStream( src );
			fos = new FileOutputStream( target );
			fcin = fis.getChannel( );
			fcout = fos.getChannel( );

			fcin.transferTo( 0, fcin.size( ), fcout );
		}
		finally
		{
			if ( fis != null )
			{
				fis.close( );
			}
			if ( fos != null )
			{
				fos.close( );
			}
			if ( fcin != null )
			{
				fcin.close( );
			}
			if ( fcout != null )
			{
				fcout.close( );
			}
		}
	}

	@Override
	protected void updateOKStatus( )
	{
		super.updateOKStatus( );

		importButton.setEnabled( getSelectedFolder( ) != null );
	}

	private static class ContentProvider implements ITreeContentProvider
	{

		private String[] exts;
		private final String ALL_EXT = "*.*"; //$NON-NLS-1$

		public ContentProvider( String[] extensionFilter )
		{
			if ( extensionFilter != null )
			{
				ArrayList<String> filters = new ArrayList<String>( );
				for ( int i = 0; i < extensionFilter.length; i++ )
				{
					String[] regxs = extensionFilter[i].split( ";" ); //$NON-NLS-1$

					for ( String regx : regxs )
					{
						String ext = regx.toLowerCase( Locale.US );
						if ( ALL_EXT.equals( regx ) )
							continue;
						
						filters.add( ext.substring( 1 ) );
					}
				}
				exts = filters.toArray( new String[]{} );
			}
		}

		public Object[] getChildren( Object arg0 )
		{
			if ( arg0 instanceof File )
			{
				File f = (File) arg0;
				if ( !f.isDirectory( ) )
				{
					return new File[0];
				}
				File[] result = f.listFiles( new FileFilter( ) {

					public boolean accept( File child )
					{
						if ( child.isDirectory( ) )
						{
							return true;
						}
						return filter( child.getAbsolutePath( ) );
					}
				} );
				if ( result != null )
				{
					Arrays.sort( result, new FileComparator( ) );
				}
				return result == null ? new File[0] : result;
			}
			return null;
		}

		private boolean filter( String fileName )
		{
			for ( int k = 0; exts != null && k < exts.length; k++ )
			{
				if ( fileName.toLowerCase( Locale.US ).endsWith( exts[k] ) )
					return true;
			}
			return false;
		}

		public Object getParent( Object arg0 )
		{
			return null;
		}

		public boolean hasChildren( Object arg0 )
		{
			if ( arg0 instanceof File )
			{
				File f = (File) arg0;
				return getChildren( f ).length > 0;
			}
			return false;
		}

		public Object[] getElements( Object arg0 )
		{
			if ( arg0 instanceof String )
			{
				return new Object[]{
					new File( (String) arg0 )
				};
			}
			return null;
		}

		public void dispose( )
		{

		}

		public void inputChanged( Viewer arg0, Object arg1, Object arg2 )
		{

		}

	}

	private static class LabelProvider implements ILabelProvider
	{

		public Image getImage( Object arg0 )
		{
			if ( arg0 instanceof File )
			{
				File f = (File) arg0;
				if ( f.isFile( ) )
				{
					return IMG_FILE;
				}
				return IMG_FOLDER;
			}
			return null;
		}

		public String getText( Object arg0 )
		{
			if ( arg0 instanceof File )
			{
				File f = (File) arg0;
				if ( f.getName( ).trim( ).equals( "" ) ) //$NON-NLS-1$
				{
					// For the case "File("C:\\")"
					return f.getPath( );
				}
				return f.getName( );
			}
			return ""; //$NON-NLS-1$
		}

		public void addListener( ILabelProviderListener arg0 )
		{

		}

		public void dispose( )
		{

		}

		public boolean isLabelProperty( Object arg0, String arg1 )
		{
			return false;
		}

		public void removeListener( ILabelProviderListener arg0 )
		{

		}

	}

	private static class SelectionValidator implements
			ISelectionStatusValidator
	{

		public IStatus validate( Object[] selections )
		{
			if ( selections != null && selections.length > 0 )
			{
				for ( Object o : selections )
				{
					if ( o instanceof File )
					{
						if ( ( (File) o ).isFile( ) )
						{
							return new Status( IStatus.OK,
									"org.eclipse.datatools.connectivity.oda.flatfile.ui", //$NON-NLS-1$
									IStatus.OK,
									"", //$NON-NLS-1$
									null );
						}
					}
				}
			}
			return new Status( IStatus.ERROR,
					"org.eclipse.datatools.connectivity.oda.flatfile.ui", //$NON-NLS-1$
					IStatus.ERROR,
					"", //$NON-NLS-1$
					null );
		}

	}

	public String[] getSelectedItems( )
	{
		List<String> result = new ArrayList<String>( );
		Object[] selected = this.getResult( ) == null ? new Object[0]
				: this.getResult( );
		for ( Object o : selected )
		{
			File f = (File) o;
			if ( f.isFile( ) )
			{
				URI relative = rootFolder.toURI( ).relativize( f.toURI( ) );
				result.add( relative.getPath( ) );
			}
		}

		return result.toArray( new String[0] );
	}

	public static class FileComparator implements
			Comparator<File>,
			Serializable
	{

		private static final long serialVersionUID = 1L;

		public int compare( File o1, File o2 )
		{
			if ( o1.isDirectory( ) && o2.isDirectory( ) )
			{
				return o1.getName( ).compareTo( o2.getName( ) );
			}
			else if ( o1.isFile( ) && o2.isFile( ) )
			{
				return o1.getName( ).compareTo( o2.getName( ) );
			}
			else if ( o1.isDirectory( ) && !o2.isDirectory( ) )
			{
				return -1;
			}
			else
			{
				// o1 is not a directory but o2 is a directory
				return 1;
			}
		}
	}
}
