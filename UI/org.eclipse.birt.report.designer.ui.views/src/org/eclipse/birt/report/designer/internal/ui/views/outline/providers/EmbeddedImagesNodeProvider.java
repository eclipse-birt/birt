/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.graphics.BirtImageLoader;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertEmbeddedImageAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

/**
 * Node provider for embedded images resources node
 */

public class EmbeddedImagesNodeProvider extends DefaultNodeProvider
{

	// private static final String SUPPORTED_IMAGE_FILE_EXTS = Messages
	// .getString( "ImageBuilderDialog.FileDialog.FilterMessage" );
	// //$NON-NLS-1$
	private static String[] EXTENSIONS = new String[]{
			".bmp", ".jpg", //$NON-NLS-1$ //$NON-NLS-2$
			".jpeg", ".jpe", ".jfif", ".gif", ".png", ".tif", ".tiff", ".ico", ".svg"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

	private static String[] ALLEXTENSIONS = new String[]{
		"*.bmp;*.jpg;*.jpeg;*.jpe;*.jfif;*.gif;*.png;*.tif;*.tiff;*.ico;*.svg"}; //$NON-NLS-1$

	public Object[] getChildren( Object model )
	{
		return ( (EmbeddedImageNode) model ).getChildren( );
	}

	public String getIconName( Object model )
	{
		return IReportGraphicConstants.ICON_NODE_IMAGES;
	}

	public String getNodeDisplayName( Object model )
	{
		return IMAGES;
	}

	public Object getParent( Object model )
	{
		return ( (EmbeddedImageNode) model ).getReportDesignHandle( );
	}

	/**
	 * Creates the context menu for the given object.
	 * 
	 * @param object
	 *            the object
	 * @param menu
	 *            the menu
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		menu.add( new InsertEmbeddedImageAction( object,
				Messages.getString( "EmbeddedImagesNodeProvider.action.New" ) ) );//$NON-NLS-1$
		super.createContextMenu( sourceViewer, object, menu );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#performRequest(java.lang.Object,
	 *      org.eclipse.gef.Request)
	 */
	public boolean performRequest( Object model, Request request )
			throws Exception
	{
		if ( request.getType( ).equals( IRequestConstants.REQUEST_TYPE_INSERT ) )
		{
			FileDialog fileChooser = new FileDialog( UIUtil.getDefaultShell( ),
					SWT.OPEN );
			fileChooser.setText( Messages.getString( "ImageBuilder.Chooser.Title" ) ); //$NON-NLS-1$
			fileChooser.setFilterExtensions( ALLEXTENSIONS );

			// fileChooser.setFilterNames( new
			// String[]{SUPPORTED_IMAGE_FILE_EXTS
			// + " (gif, jpeg, png, ico, bmp)" //$NON-NLS-1$
			// } );

			CommandStack stack = SessionHandleAdapter.getInstance( )
					.getCommandStack( );

			try
			{
				String fullPath = fileChooser.open( );

				String fileName = fileChooser.getFileName( );
				if ( fullPath == null || "".equalsIgnoreCase( fullPath ) ) //$NON-NLS-1$
				{
					return false;
				}

				if ( checkExtensions( fileName ) == false )
				{
					ExceptionUtil.openError( Messages.getString( "EmbeddedImagesNodeProvider.FileNameError.Title" ), //$NON-NLS-1$
							Messages.getString( "EmbeddedImagesNodeProvider.FileNameError.Message" ) ); //$NON-NLS-1$
					return false;
				}

				stack.startTrans( Messages.getString( "EmbeddedImagesNodeProvider.stackMsg.insert" ) );//$NON-NLS-1$

				BirtImageLoader imageLoader = new BirtImageLoader( );
				imageLoader.save( SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( ), fullPath, fileName );

				stack.commit( );
				return true;

			}
			catch ( Throwable e )
			{
				stack.rollback( );
				ExceptionUtil.handle( e );
			}
		}
		return false;
	}

	private boolean checkExtensions( String fileName )
	{
		fileName = fileName.toLowerCase( );
		for ( int i = 0; i < EXTENSIONS.length; i++ )
		{
			if ( fileName.endsWith( EXTENSIONS[i] ) )
			{
				return true;
			}
		}
		return false;
	}
}