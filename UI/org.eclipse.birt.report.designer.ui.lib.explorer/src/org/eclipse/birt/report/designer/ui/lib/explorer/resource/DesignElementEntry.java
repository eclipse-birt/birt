/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.swt.graphics.Image;

public class DesignElementEntry extends ReportResourceEntry
{

	private DesignElementHandle element;
	private ResourceEntry parent;
	private INodeProvider provider;

	public DesignElementEntry( DesignElementHandle element, ResourceEntry parent )
	{
		this.element = element;
		this.parent = parent;
		this.provider = ProviderFactory.createProvider( element );
	}

	public String getDisplayName( )
	{
		return provider.getNodeDisplayName( element );
	}

	public Image getImage( )
	{
		return provider.getNodeIcon( element );
	}

	public String getName( )
	{
		return provider.getNodeDisplayName( element );
	}

	public ResourceEntry getParent( )
	{
		return parent;
	}

	public boolean equals( Object object )
	{
		if ( object == null )
			return false;
		if ( !( object instanceof DesignElementEntry ) )
			return false;
		if ( object == this )
			return true;
		else
		{
			DesignElementEntry temp = (DesignElementEntry) object;
			if ( temp.element.getElement( ).getID( ) == this.element.getElement( )
					.getID( )
					&& DEUtil.isSameString( temp.element.getModule( )
							.getFileName( ), this.element.getModule( )
							.getFileName( ) )
					&& ( temp.element.getElement( ).getName( ) == null ? true
							: ( temp.element.getElement( ).getName( ).equals( this.element.getElement( )
									.getName( ) ) ) ) )
				return true;
		}
		return false;
	}

	public int hashCode( )
	{
		String fileName = this.element.getModule( ).getFileName( );
		return (int) ( element.getElement( ).getID( ) * 7 + ( element.getElement( )
				.getName( ) == null ? 0
				: ( element.getElement( ).getName( ).hashCode( ) ) ) )
				* 7
				+ ( fileName == null ? 0 : fileName.hashCode( ) );
	}

	public Object getReportElement( )
	{
		return element;
	}

}
