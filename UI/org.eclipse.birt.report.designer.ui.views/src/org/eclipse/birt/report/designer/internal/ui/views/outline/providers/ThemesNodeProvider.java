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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.RenameInputDialog;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ExtendElementAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

/**
 * Deals with the themes node
 */
public class ThemesNodeProvider extends DefaultNodeProvider
{

	protected static final String NEW_THEME_ACTION_ID = "org.eclipse.birt.report.designer.internal.ui.action.NewThemeAction"; //$NON-NLS-1$

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
		if ( object instanceof SlotHandle
				&& ( (SlotHandle) object ).getElementHandle( ) instanceof LibraryHandle )
		{
			ExtendElementAction newThemeAction = new ExtendElementAction( this,
					NEW_THEME_ACTION_ID,
					object,
					Messages.getString( "ThemesNodeProvider.action.New" ), //$NON-NLS-1$
					ReportDesignConstants.THEME_ITEM );
			menu.add( newThemeAction );
		}

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
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName
	 * (java.lang.Object)
	 */
	public String getIconName( Object model )
	{
		return IReportGraphicConstants.ICON_NODE_THEMES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider
	 * #createElement(java.lang.String)
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

			Module module = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getModule( );
			NameExecutor executor = new NameExecutor( module, theme );
			executor.makeUniqueName( );
			executor.dropElement( );

			RenameInputDialog inputDialog = new RenameInputDialog( Display.getCurrent( )
					.getActiveShell( ),
					Messages.getString( "NewThemeDialog.DialogTitle" ), //$NON-NLS-1$
					Messages.getString( "NewThemeDialog.DialogMessage" ), //$NON-NLS-1$
					theme.getName( ),
					ChoiceSetFactory.getThemes( ),
					IHelpContextIds.NEW_THEME_DIALOG_ID );

			inputDialog.create( );

			if ( inputDialog.open( ) == Window.OK )
			{
				return factory.newTheme( inputDialog.getResult( )
						.toString( )
						.trim( ) );
			}
			return null;
		}
		return super.createElement( type );
	}

	public Object[] getChildren( Object model )
	{
		List<Object> list = new ArrayList<Object>( );
		list.addAll( ( (SlotHandle) model ).getElementHandle( )
				.getModuleHandle( )
				.getVisibleThemes( IAccessControl.NATIVE_LEVEL ) );
		return list.toArray( );
	}
}