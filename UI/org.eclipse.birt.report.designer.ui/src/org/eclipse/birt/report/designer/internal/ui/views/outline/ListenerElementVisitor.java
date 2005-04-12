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

package org.eclipse.birt.report.designer.internal.ui.views.outline;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.core.Listener;

/**
 * Applies visitor to the report element and the children element
 *  
 */

public class ListenerElementVisitor extends DesignVisitor
{

	/**
	 * The listener
	 */

	private Listener listener;

	private boolean install = true;

	/**
	 * constructor. Sets the listener and design
	 * 
	 * @param listener
	 *            the listener value to be set
	 */

	public ListenerElementVisitor( Listener listener )
	{
		super( );
		this.listener = listener;
	}

	public void addListener( DesignElementHandle handle )
	{
		install = true;
		apply( handle );
	}

	public void removeListener( DesignElementHandle handle )
	{
		install = false;
		apply( handle );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.api.DesignVisitor#visitDesignElement(org.eclipse.birt.model.api.DesignElementHandle)
	 */
	public void visitDesignElement( DesignElementHandle obj )
	{
		if ( install )
		{
			obj.addListener( listener );
		}
		else
		{
			obj.removeListener( listener );
		}
		for ( int i = 0; i < obj.getDefn( ).getSlotCount( ); i++ )
		{
			visitContents( obj.getSlot( i ) );
		}
	}

	/**
	 * Sets the listener null.
	 */

	public void dispose( )
	{
		listener = null;
	}
}