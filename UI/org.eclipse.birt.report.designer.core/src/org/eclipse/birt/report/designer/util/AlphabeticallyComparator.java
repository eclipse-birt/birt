/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.util;

import java.text.Collator;
import java.util.Comparator;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * A text based comparator is used to reorder the elements.
 * 
 * @see IStructuredContentProvider
 * @see StructuredViewer
 */
public class AlphabeticallyComparator implements Comparator
{

	private boolean ascending = true;

	public int compare( Object o1, Object o2 )
	{
		String name1;
		String name2;

		if ( o1 instanceof DesignElementHandle
				&& o2 instanceof DesignElementHandle )
		{
			name1 = ( (DesignElementHandle) o1 ).getDisplayLabel( );
			name2 = ( (DesignElementHandle) o2 ).getDisplayLabel( );
			if ( name1 == null )
			{
				name1 = ( (DesignElementHandle) o1 ).getName( );
			}
			if ( name2 == null )
			{
				name2 = ( (DesignElementHandle) o2 ).getName( );
			}

		}
		if ( o1 instanceof IChoice
				&& o2 instanceof IChoice) 
		{
			name1 = ( (IChoice) o1 ).getDisplayName();
			name2 = ( (IChoice) o2 ).getDisplayName( );
			if ( name1 == null )
			{
				name1 = ( (IChoice) o1 ).getName( );
			}
			if ( name2 == null )
			{
				name2 = ( (IChoice) o2 ).getName( );
			}

		}
		else
		{
			name1 = o1.toString( );
			name2 = o2.toString( );
		}

		if ( name1 == null )
		{
			name1 = "";//$NON-NLS-1$
		}
		if ( name2 == null )
		{
			name2 = "";//$NON-NLS-1$
		}
		
		if ( ascending )
		{
			return Collator.getInstance( ).compare( name1, name2 );
		}
		else
		{
			return Collator.getInstance( ).compare( name2, name1 );
		}		
		 
	}

	/**
	 * Set order of this sort
	 * True: Ascending
	 * False: Deascending
	 * @param ascending
	 */
	public void setAscending( boolean ascending )
	{
		this.ascending = ascending;
	}
}