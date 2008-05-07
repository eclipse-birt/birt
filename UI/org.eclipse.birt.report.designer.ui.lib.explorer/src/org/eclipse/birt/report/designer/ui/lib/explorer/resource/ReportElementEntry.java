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

package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.swt.graphics.Image;

/**
 * This class is a representation of resource entry for report element.
 */
public class ReportElementEntry extends ReportResourceEntry
{

	/** The element in report. */
	private final Object element;

	/** The parent entry. */
	private final ResourceEntry parent;

	/** The node provider for the element. */
	private final INodeProvider provider;

	/**
	 * Constructs a resource entry for the specified report element.
	 * 
	 * @param element
	 *            the specified report element.
	 * @param parent
	 *            the parent entry.
	 */
	public ReportElementEntry( Object element, ResourceEntry parent )
	{
		this.element = element;
		this.parent = parent;
		this.provider = ProviderFactory.createProvider( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return provider.getNodeDisplayName( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry#getImage()
	 */
	public Image getImage( )
	{
		return provider.getNodeIcon( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry#getName()
	 */
	public String getName( )
	{
		return provider.getNodeDisplayName( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry#getParent()
	 */
	public ResourceEntry getParent( )
	{
		return parent;
	}

	@Override
	public boolean equals( Object object )
	{
		if ( !( object instanceof ReportElementEntry ) )
		{
			return false;
		}

		if ( object == this
				|| ( (ReportElementEntry) object ).element == element )
		{
			return true;
		}
		else if ( element != null
				&& element.equals( ( (ReportElementEntry) object ).element ) )
		{
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry#getReportElement()
	 */
	public Object getReportElement( )
	{
		return element;
	}
}
