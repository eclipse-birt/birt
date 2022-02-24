/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IJointDataSetModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Represents a joint data set. A joint data set is a data set joined by several
 * data sets which can come from different data sources.
 *
 * @see org.eclipse.birt.report.model.elements.SimpleDataSet
 */

public class JointDataSet extends DataSet implements IJointDataSetModel {

	/**
	 * Default constructor.
	 */

	public JointDataSet() {
		super();
	}

	/**
	 * Constructs this joint data set with a required name.
	 *
	 * @param theName the required name
	 */

	public JointDataSet(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitJointDataSet(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.JOINT_DATA_SET;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse
	 * .birt.report.model.core.Module)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design of the joint data set
	 *
	 * @return an API handle for this element
	 */

	public JointDataSetHandle handle(Module module) {
		if (handle == null) {
			handle = new JointDataSetHandle(module, this);
		}
		return (JointDataSetHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.DataSet#validate(org.eclipse.birt
	 * .report.model.core.Module)
	 */

	@Override
	public List<SemanticException> validate(Module module) {
		return Collections.emptyList();
	}

	/**
	 * Gets the names of the data sets in this joint data set.
	 *
	 * @param module the root module of the element
	 *
	 * @return a list of names of data sets in this joint data set.
	 */

	public List<String> getDataSetNames(Module module) {
		List<Object> dataSetsReferences = getListProperty(module, IJointDataSetModel.DATA_SETS_PROP);
		if (dataSetsReferences == null) {
			return Collections.emptyList();
		}
		List<String> results = new ArrayList<>();
		for (int i = 0; i < dataSetsReferences.size(); i++) {
			results.add(((ElementRefValue) dataSetsReferences.get(i)).getQualifiedReference());
		}
		return results;
	}
}
