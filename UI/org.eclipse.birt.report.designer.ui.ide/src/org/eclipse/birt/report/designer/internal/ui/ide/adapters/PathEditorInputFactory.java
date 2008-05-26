/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.ui.editors.IPathEditorInputFactory;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;

/**
 * Adapter factory for <code>IEditorInput</code> based on the local file system
 * path.
 */
public class PathEditorInputFactory implements IAdapterFactory
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
	 * java.lang.Class)
	 */
	public Object getAdapter( Object adaptableObject, Class adapterType )
	{
		if ( IPathEditorInputFactory.class.equals( adapterType ) )
		{

			return new IPathEditorInputFactory( ) {

				/*
				 * (non-Javadoc)
				 * 
				 * @seeorg.eclipse.birt.report.designer.ui.editors.
				 * IPathEditorInputFactory
				 * #create(org.eclipse.core.runtime.IPath)
				 */
				public IEditorInput create( IPath path )
				{
					IFileStore fileStore = EFS.getLocalFileSystem( )
							.getStore( path );

					return new FileStoreEditorInput( fileStore );
				}
			};
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList( )
	{
		return new Class[]{
			IPathEditorInputFactory.class
		};
	}
}
