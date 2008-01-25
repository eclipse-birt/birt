/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editor.script;

import org.eclipse.birt.report.designer.internal.ui.editors.script.JSDocumentProvider;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

/**
 * Subclass of <code>JSDocumentProvider</code>, provides an annotation model.
 */
public class ScriptDocumentProvider extends JSDocumentProvider
{
	
	/**
	 * ID key
	 */
	public static final String SUBNAME = "sub name";//$NON-NLS-1$
	/**
	 * File name key
	 */
	public static final String FILENAME = "file name";//$NON-NLS-1$
	private String id = ""; //$NON-NLS-1$
	private String fileName = ""; //$NON-NLS-1$
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.DocumentProvider#createAnnotationModel(java.lang.Object)
	 */
	protected IAnnotationModel createAnnotationModel( Object element )
			throws CoreException
	{
		return new DebugResourceMarkerAnnotationModel( ResourcesPlugin.getWorkspace( )
				.getRoot( ) );
	}
	
	/**Gets the id.
	 * @return
	 */
	public String getId( )
	{
		return id;
	}
	
	/**Sets the id.
	 * @param id
	 */
	public void setId( String id )
	{
		this.id = id;
	}
	
	/**
	 * Update the script to refesh the break point.
	 * @param annotationModel
	 */
	public void update(IAnnotationModel annotationModel)
	{
		if (!(annotationModel instanceof DebugResourceMarkerAnnotationModel))
		{
			return ;
		}
		
		DebugResourceMarkerAnnotationModel debugAnno = (DebugResourceMarkerAnnotationModel)annotationModel;
		debugAnno.disconnected( );
		debugAnno.connected( );
	}
	
	/**
	 * DebugResourceMarkerAnnotationModel
	 */
	protected class DebugResourceMarkerAnnotationModel extends ResourceMarkerAnnotationModel
	{
		public DebugResourceMarkerAnnotationModel( IResource resource )
		{
			super( resource );
		}
		
		protected boolean isAcceptable(IMarker marker)
		{
			boolean bool = super.isAcceptable( marker );
			try
			{
				return bool && getId( ).equals( marker.getAttribute( SUBNAME ) )
					&&	getFileName( ).equals( marker.getAttribute( FILENAME ) );
			}
			catch ( CoreException e )
			{
				return false;
			}
		}
		
		protected void disconnected( )
		{
			super.disconnected( );
		}
		
		protected void connected( )
		{
			super.connected( );
		}
	}

	
	/**Gets the file name.
	 * @return
	 */
	public String getFileName( )
	{
		return fileName;
	}

	
	/**Set the file name.
	 * @param fileName
	 */
	public void setFileName( String fileName )
	{
		this.fileName = fileName;
	}
}
