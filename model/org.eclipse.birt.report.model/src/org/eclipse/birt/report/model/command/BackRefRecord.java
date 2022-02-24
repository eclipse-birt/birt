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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;

/**
 * Records a change to the back reference of an element.
 * 
 * @see org.eclipse.birt.report.model.core.ReferenceableElement
 * @see org.eclipse.birt.report.model.core.ReferencableStructure
 */

abstract public class BackRefRecord extends SimpleRecord {

	/**
	 * The element that refers to another element.
	 */

	protected Object reference = null;

	/**
	 * The property name.
	 */

	protected String propName = null;

	/**
	 * Module
	 */

	protected Module module = null;

	/**
	 * Constructor.
	 * 
	 * @param module    module
	 * @param reference the element that refers to another element.
	 * @param propName  the property name. The type of the property must be
	 *                  <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *                  must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *                  <code>DesignElement.STYLE_PROP</code>
	 */

	public BackRefRecord(Module module, DesignElement reference, String propName) {
		this.module = module;
		this.reference = reference;
		this.propName = propName;
	}

	/**
	 * Constructor.
	 * 
	 * @param module    module
	 * @param reference the element that refers to another element.
	 * @param propName  the property name. The type of the property must be
	 *                  <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *                  must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *                  <code>DesignElement.STYLE_PROP</code>
	 */

	public BackRefRecord(Module module, Structure reference, String propName) {
		this.module = module;
		this.reference = reference;
		this.propName = propName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent() {
		if (IStyledElementModel.STYLE_PROP.equals(propName))
			return new StyleEvent(getTarget());
		return new PropertyEvent(getTarget(), propName);
	}

	public DesignElement getTarget() {
		if (reference instanceof DesignElement)
			return (DesignElement) reference;

		return ((Structure) reference).getElement();
	}
}
