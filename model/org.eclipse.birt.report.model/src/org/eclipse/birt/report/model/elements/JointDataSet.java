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
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.interfaces.IJointDataSetModel;

/**
 * Represents a joint data set. A joint data set is a data set joined by several
 * data sets which can come from different data sources.
 * 
 * @see org.eclipse.birt.report.model.elements.DataSet
 */

public class JointDataSet extends ReferenceableElement
		implements
			IJointDataSetModel
{

	/**
	 * Default constructor.
	 */

	public JointDataSet( )
	{
		super( );
	}

	/**
	 * Constructs this joint data set with a required name.
	 * 
	 * @param theName
	 *            the required name
	 */

	public JointDataSet( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferenceableElement#adjustDeliveryPath(org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */

	protected void adjustDeliveryPath( NotificationEvent ev )
	{
		ev.setDeliveryPath( NotificationEvent.ELEMENT_CLIENT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitJointDataSet( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.JOINT_DATA_SET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.birt.report.model.core.Module)
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the report design of the joint data set
	 * 
	 * @return an API handle for this element
	 */

	public JointDataSetHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new JointDataSetHandle( module, this );
		}
		return (JointDataSetHandle) handle;
	}

}
