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

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Deals with theme node
 * 
 */
public class ThemeNodeProvider extends DefaultNodeProvider
{

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
		if ( canContain( object ) )
		{
			menu.add( new InsertAction( object, Messages
					.getString( "StylesNodeProvider.action.New" ) ) ); //$NON-NLS-1$
		}

		super.createContextMenu( sourceViewer, object, menu );

		if(canContain(object))
		{
			menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS,
					new Separator( ) );
	
			menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS,
					new ImportCSSStyleAction( object ) ); //$NON-NLS-1$
		}
	}

	public String getNodeDisplayName( Object model )
	{
		return ( (ThemeHandle) model ).getDisplayLabel( );
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
		StyleHandle handle = factory.newStyle( null );
		if ( ProviderFactory.createProvider( handle ).performRequest( handle,
				new Request( IRequestConstants.REQUEST_TYPE_EDIT ) ) )
		{
			return handle;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.views.INodeProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren( Object model )
	{
		if ( model instanceof ThemeHandle )
		{
			return super.getChildrenBySlotHandle( ( (ThemeHandle) model )
					.getStyles( ) );
		}
		return super.getChildren( model );
	}

	private boolean canContain( Object object )
	{
		if ( object instanceof ThemeHandle )
		{
			return ( (ThemeHandle) object ).canContain(
					LibraryHandle.THEMES_SLOT,
					ReportDesignConstants.STYLE_ELEMENT );
		}
		return true;
	}
}