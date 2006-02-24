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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IJointDataSetModel;

/**
 * Provides API to operate a joint data set.
 * 
 * @see org.eclipse.birt.report.model.elements.JointDataSet
 */

public class JointDataSetHandle extends ReportItemHandle
		implements
			IJointDataSetModel
{

	/**
	 * Constructs a handle of the joint data set with the given design and a
	 * joint data set. The application generally does not create handles
	 * directly. Instead, it uses one of the navigation methods available on
	 * other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public JointDataSetHandle( Module module, JointDataSet element )
	{
		super( module, element );
	}

	/**
	 * Gets the names of the source data sets in this joint data set.
	 * 
	 * @return a list of names of data sets in this joint data set.
	 */

	public List getDataSetNames( )
	{
		// TODO: logic to get the name list of the source datasets.
		return null;
	}

	/**
	 * Adds a data set into this joint data set by name.
	 * 
	 * @param dataSetName
	 *            the name of the data set to be added in.
	 */

	public void addDataSet( String dataSetName )
	{
		// TODO: add the data set element reference.
	}

	/**
	 * Removes a data set from this joint data set by name.
	 * 
	 * @param dataSetName
	 *            the name of the data set to be removed.
	 */

	public void removeDataSet( String dataSetName )
	{
		// TODO: add the data set element reference.
	}

	/**
	 * Returns the iterator of joint conditions. The element in the iterator is
	 * the corresponding <code>JointConditionHandle</code> that deal with a
	 * <code>JointCondition</code>.
	 * 
	 * @return the iterator of joint condition structure list
	 */

	public Iterator jointConditionsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( JointDataSet.JOINT_CONDITONS_PROP );

		assert propHandle != null;

		return propHandle.iterator( );
	}
}
