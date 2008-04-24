/*************************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.RenameInputDialog;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

/**
 * Deals with the themes node
 * 
 * 
 */
public class ThemesNodeProvider extends DefaultNodeProvider
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
				Messages.getString( "ThemesNodeProvider.action.New" ) ) ); //$NON-NLS-1$
		super.createContextMenu( sourceViewer, object, menu );

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
		return THEMES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(java.lang.Object)
	 */
	public String getIconName( Object model )
	{
		return IReportGraphicConstants.ICON_NODE_THEMES;
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
		if ( ReportDesignConstants.THEME_ITEM.equals( type ) )
		{
			Theme theme = new Theme( ReportPlugin.getDefault( )
					.getCustomName( ReportDesignConstants.THEME_ITEM ) );

			INameHelper nameHelper = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getModule( )
					.getNameHelper( );

			nameHelper.makeUniqueName( theme );
			nameHelper.dropElement( theme );

			RenameInputDialog inputDialog = new RenameInputDialog( Display.getCurrent( ).getActiveShell( ),
					Messages.getString( "NewThemeDialog.DialogTitle" ), //$NON-NLS-1$
					Messages.getString( "NewThemeDialog.DialogMessage" ), //$NON-NLS-1$
					theme.getName( ),
					null );

			inputDialog.create( );

			if ( inputDialog.open( ) == Window.OK )
			{
				return factory.newTheme( inputDialog.getValue( ).trim( ) );
			}
			return null;
		}
		return super.createElement( type );
	}
	
	public Object[] getChildren( Object model )
	{
		return ( (SlotHandle) model ).getElementHandle( )
				.getModuleHandle( )
				.getVisibleThemes( IAccessControl.NATIVE_LEVEL )
				.toArray( );
	}
}