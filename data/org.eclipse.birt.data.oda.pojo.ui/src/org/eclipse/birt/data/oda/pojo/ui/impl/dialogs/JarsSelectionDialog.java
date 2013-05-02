
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.ui.Activator;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.util.Utils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;



/**
 * 
 */

public class JarsSelectionDialog extends ElementTreeSelectionDialog
{


	private static final String JAR_EXT = ".jar"; //$NON-NLS-1$
	private static final String ZIP_EXT = ".zip"; //$NON-NLS-1$
	
	private final static Image IMG_FOLDER = PlatformUI.getWorkbench( )
		.getSharedImages( )
		.getImage( ISharedImages.IMG_OBJ_FOLDER );

	private final static Image IMG_FILE = PlatformUI.getWorkbench( )
		.getSharedImages( )
		.getImage( ISharedImages.IMG_OBJ_FILE );
	
	private File topDir;
	

	public JarsSelectionDialog( Shell parent, File topDir )
	{
		super( parent, new LabelProvider( ), new ContentProvider( ) );
		
		assert topDir != null;
		
		this.setValidator( new SelectionValidator( ) );
		this.setInput( topDir.getAbsolutePath( ) );	
		this.setTitle( Messages.getString( "DataSet.JarsSelectDlg.Title" ) ); //$NON-NLS-1$
		this.topDir = topDir;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea( Composite parent )
	{
		Control c = super.createDialogArea( parent );
		this.getTreeViewer( ).expandToLevel( 2 );
		return c;
	}
	
	private static class ContentProvider implements ITreeContentProvider
	{

		public Object[] getChildren( Object arg0 )
		{
			if ( arg0 instanceof File )
			{
				File f = (File)arg0;
				return JarsSelectionDialog.getChildren( f );
			}
			return null;
		}

		public Object getParent( Object arg0 )
		{
			return null;
		}

		public boolean hasChildren( Object arg0 )
		{
			if ( arg0 instanceof File )
			{
				File f = (File)arg0;
				return JarsSelectionDialog.getChildren( f ).length > 0;
			}
			return false;
		}

		public Object[] getElements( Object arg0 )
		{
			if ( arg0 instanceof String )
			{
				return new Object[]{ new File((String)arg0) };
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
				File f = (File)arg0;
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
			    File f = (File)arg0;
			    if ( f.getName( ).trim( ).equals( "" )) //$NON-NLS-1$
			    {
			    	//For the case "File("C:\\")"
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
	
	private static class SelectionValidator implements ISelectionStatusValidator
	{

		public IStatus validate( Object[] selections )
		{
			if ( selections != null && selections.length > 0 )
			{
				for ( Object o : selections )
				{
					if ( o instanceof File )
					{
						if ( ((File)o).isFile( ))
						{
							return new Status( IStatus.OK,
									Activator.PLUGIN_ID,
									IStatus.OK,
									"", //$NON-NLS-1$
									null );
						}
					}
				}
			}
			return new Status( IStatus.ERROR,
					Activator.PLUGIN_ID,
					IStatus.ERROR,
					"", //$NON-NLS-1$
					null );
		}
		
	}
	
	private static File[] getChildren( File f )
	{
		if ( !f.isDirectory( ) )
		{
			return new File[0];
		}
		File[] result 
			= f.listFiles( new FileFilter( )
				{
					public boolean accept( File child )
					{
						if ( child.isDirectory( ) )
						{
							return true;
						}
						String name = child.getName( ).toLowerCase( );
						return name.endsWith( JAR_EXT ) || name.endsWith( ZIP_EXT );
					}	
				});
		if ( result != null )
		{
			Arrays.sort( result, new Utils.FileComparator( ) );
		}
		return result == null ? new File[0] : result;
	}
	
	public String[] getSelectedItems( )
	{
		List<String> result = new ArrayList<String>( );
		Object[] selected = this.getResult( ) == null ? new Object[0] : this.getResult( );
		for ( Object o : selected )
		{
			File f = (File)o;
			if ( f.isFile( ) )
			{
				URI relative = topDir.toURI( ).relativize( f.toURI( ) );
				result.add( relative.getPath( ) );
			}
		}
		
		return result.toArray( new String[0] );
	}
}
