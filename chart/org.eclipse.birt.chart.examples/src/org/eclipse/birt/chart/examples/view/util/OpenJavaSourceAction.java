/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.birt.chart.examples.ChartExamplesPlugin;
import org.eclipse.birt.chart.examples.view.ChartExamples;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.jface.action.Action;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;

import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.Bundle;

public class OpenJavaSourceAction extends Action
{

	private IWorkbenchWindow window;

	private static String JAVA_EXTENSION = ".java"; //$NON-NLS-1$

	public OpenJavaSourceAction( Tools tool, IWorkbenchWindow window )
	{
		super( );
		this.window = window;
		String id = tool.group + '.' + tool.name;
		setId( id );
		// action = tool.action;
		setText( "Open" ); //$NON-NLS-1$
		setToolTipText( "Open Java Source" ); //$NON-NLS-1$
		setDescription( "Open Java Source from the relevant directory" ); //$NON-NLS-1$
	}

	public void run( )
	{
		String fileName = ChartExamples.getClassName( );
		if ( fileName != null )
		{
			IFileStore fileStore = EFS.getLocalFileSystem( )
					.getStore( new Path( getPath( fileName ) ) );
			fileStore = fileStore.getChild( fileName + JAVA_EXTENSION );
			if ( !fileStore.fetchInfo( ).isDirectory( )
					&& fileStore.fetchInfo( ).exists( ) )
			{
				IEditorInput input = createEditorInput( fileStore );
				String editorId = getEditorId( fileStore );
				try
				{
					window.getActivePage( ).openEditor( input, editorId );
				}
				catch ( PartInitException e )
				{
					e.printStackTrace( );
				}
			}
		}
	}

	private String getPath( String fileName )
	{
		Bundle bundle = Platform.getBundle( ChartExamplesPlugin.ID );
		Path relativePath = new Path( "/src/org/eclipse/birt/chart/examples/view/models/" + fileName + JAVA_EXTENSION ); //$NON-NLS-1$
		URL relativeURL = FileLocator.find( bundle, relativePath, null );

		String absolutePath = null;
		try
		{
			URL absoluteURL = FileLocator.toFileURL( relativeURL );
			String tmp = absoluteURL.getPath( );
			absolutePath = tmp.substring( 0, tmp.lastIndexOf( "/" ) ); //$NON-NLS-1$		
		}
		catch ( IOException io )
		{
			io.printStackTrace( );
		}
		return absolutePath;
	}

	private IEditorInput createEditorInput( IFileStore fileStore )
	{
		IFile workspaceFile = getWorkspaceFile( fileStore );
		if ( workspaceFile != null )
			return new FileEditorInput( workspaceFile );
		return new FileStoreEditorInput( fileStore );
	}

	private IFile getWorkspaceFile( IFileStore fileStore )
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace( );
		IFile[] files = workspace.getRoot( )
				.findFilesForLocation( new Path( fileStore.toURI( ).getPath( ) ) );
		files = filterNonExistentFiles( files );
		if ( files == null || files.length == 0 )
		{
			return null;
		}
		else
		{
			return files[0];
		}
	}

	private IFile[] filterNonExistentFiles( IFile[] files )
	{
		if ( files == null )
			return null;

		int length = files.length;
		ArrayList existentFiles = new ArrayList( length );
		for ( int i = 0; i < length; i++ )
		{
			if ( files[i].exists( ) )
				existentFiles.add( files[i] );
		}
		return (IFile[]) existentFiles.toArray( new IFile[existentFiles.size( )] );
	}

	private String getEditorId( IFileStore file )
	{
		IWorkbench workbench = window.getWorkbench( );
		IEditorRegistry editorRegistry = workbench.getEditorRegistry( );
		IEditorDescriptor descriptor = editorRegistry.getDefaultEditor( file.getName( ),
				getContentType( file ) );

		// check the OS for in-place editor (OLE on Win32)
		if ( descriptor == null
				&& editorRegistry.isSystemInPlaceEditorAvailable( file.getName( ) ) )
		{
			descriptor = editorRegistry.findEditor( IEditorRegistry.SYSTEM_INPLACE_EDITOR_ID );
		}

		// check the OS for external editor
		if ( descriptor == null
				&& editorRegistry.isSystemExternalEditorAvailable( file.getName( ) ) )
		{
			descriptor = editorRegistry.findEditor( IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID );
		}

		return descriptor.getId( );
	}

	private IContentType getContentType( IFileStore fileStore )
	{
		if ( fileStore == null )
			return null;

		InputStream stream = null;
		try
		{
			stream = fileStore.openInputStream( EFS.NONE, null );
			return Platform.getContentTypeManager( )
					.findContentTypeFor( stream, fileStore.getName( ) );
		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace( );
			return null;
		}
		catch ( CoreException ce )
		{
			// Do not log FileNotFoundException (no access)
			if ( !( ce.getStatus( ).getException( ) instanceof FileNotFoundException ) )

				ce.printStackTrace( );
			return null;
		}
		finally
		{
			try
			{
				if ( stream != null )
					stream.close( );
			}
			catch ( IOException ioe )
			{
				ioe.printStackTrace( );
			}
		}
	}
}
