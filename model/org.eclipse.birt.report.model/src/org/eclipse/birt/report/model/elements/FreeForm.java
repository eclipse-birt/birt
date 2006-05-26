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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.elements.interfaces.IFreeFormModel;

/**
 * This class represents a free-form element. A free-form holds a set of report
 * items positioned at arbitrary (x,y) positions relative to the upper left
 * corner of the free-form.
 * 
 */

public class FreeForm extends ReportItem implements IFreeFormModel
{

	/**
	 * The report items slot.
	 */

	MultiElementSlot contents = new MultiElementSlot( );

	/**
	 * Default constructor.
	 */

	public FreeForm( )
	{
	}

	/**
	 * Constructs the free form with optional name.
	 * 
	 * @param theName
	 *            the free-form name
	 */

	public FreeForm( String theName )
	{
		super( theName );
	}

	/**
	 * Makes a clone of this free form element. The cloned free form contains
	 * list of contents, which are copied from the original free form.
	 * 
	 * @return the cloned free form object.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone( ) throws CloneNotSupportedException
	{
		FreeForm form = (FreeForm) super.clone( );
		form.contents = (MultiElementSlot) contents.copy( form,
				REPORT_ITEMS_SLOT );
		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.StyledElement#cloneForTemplate()
	 */

	public Object cloneForTemplate( ) throws CloneNotSupportedException
	{
		FreeForm form = (FreeForm) super.cloneForTemplate( );
		form.contents = (MultiElementSlot) contents.cloneForTemplate( form,
				REPORT_ITEMS_SLOT );
		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert slot == REPORT_ITEMS_SLOT;
		return contents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitFreeForm( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.FREE_FORM_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle()
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the design for the free form.
	 * 
	 * @return an API handle for this element.
	 */

	public FreeFormHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new FreeFormHandle( module, this );
		}
		return (FreeFormHandle) handle;
	}
}