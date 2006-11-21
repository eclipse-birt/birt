/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.dialogs.StyleBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with the styles node
 * 
 * 
 */
public class StylesNodeProvider extends DefaultNodeProvider
{

	/**
	 * Creates the context menu for the given object.
	 * 
	 * @param menu
	 *            the menu
	 * @param object
	 *            the object
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		menu.add( new InsertAction( object,
				Messages.getString( "StylesNodeProvider.action.New" ) ) ); //$NON-NLS-1$
		super.createContextMenu( sourceViewer, object, menu );

		menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS,
				new Separator( ) );

		menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS,
				new ImportCSSStyleAction( object ) ); //$NON-NLS-1$

	}

	/**
	 * Gets the node display name of the given object.
	 * 
	 * @param object
	 *            the object
	 * @return the display name
	 */
	public String getNodeDisplayName( Object object )
	{
		return STYLES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(java.lang.Object)
	 */
	public String getIconName( Object model )
	{
		return IReportGraphicConstants.ICON_NODE_STYLES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#createElement(java.lang.String)
	 */
	protected DesignElementHandle createElement( String type ) throws Exception
	{
		// ElementFactory factory = SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( )
		// .getElementFactory( );
		DesignElementFactory factory = DesignElementFactory.getInstance( );
		if ( ReportDesignConstants.STYLE_ELEMENT.equals( type ) )
		{
			StyleHandle handle = factory.newStyle( null );
			StyleBuilder builder = new StyleBuilder( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ), handle, StyleBuilder.DLG_TITLE_NEW );
			if ( builder.open( ) == Dialog.CANCEL )
			{
				return null;
			}
			return handle;
		}
		return super.createElement( type );
	}

	public Object[] getChildren( Object model )
	{
		Object[] styles = ( (SlotHandle) model ).getElementHandle( )
				.getModuleHandle( )
				.getStyles( )
				.getContents( )
				.toArray( );
		Arrays.sort( styles, new AlphabeticallyComparator( ) );
		return styles;
	}
}