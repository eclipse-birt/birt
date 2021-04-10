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

package org.eclipse.birt.report.model.command;

import java.util.List;

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Records a change to the back reference of an element.
 * 
 * @see org.eclipse.birt.report.model.core.ReferenceableElement
 */

public class ElementBackRefRecord extends BackRefRecord {

	/**
	 * The element is referred by <code>reference</code>.
	 */

	protected IReferencableElement referred = null;

	private DesignElement target;

	/**
	 * Constructor.
	 * 
	 * @param module    the module
	 * @param referred  the element to change
	 * @param reference the element that refers to another element.
	 * @param propName  the property name. The type of the property must be
	 *                  <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *                  must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *                  <code>DesignElement.STYLE_PROP</code>
	 */

	public ElementBackRefRecord(Module module, IReferencableElement referred, DesignElement reference,
			String propName) {
		super(module, reference, propName);
		this.referred = referred;

		assert referred != null;

		target = reference;
	}

	/**
	 * Constructor.
	 * 
	 * @param module    the module
	 * @param referred  the element to change
	 * @param reference the element that refers to another element.
	 * @param propName  the property name. The type of the property must be
	 *                  <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *                  must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *                  <code>DesignElement.STYLE_PROP</code>
	 * @param memberRef the member reference that refers to a structure member
	 */

	public ElementBackRefRecord(Module module, IReferencableElement referred, Structure reference, String propName) {
		super(module, reference, propName);
		this.referred = referred;

		assert referred != null;

		target = reference.getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		if (undo) {
			if (reference instanceof DesignElement) {
				DesignElement tmpElement = (DesignElement) reference;
				ElementPropertyDefn propDefn = tmpElement.getPropertyDefn(propName);

				// To add client is done in resolving element reference.
				if (IStyledElementModel.STYLE_PROP.equals(propName)) {
					tmpElement.getStyle(module);
				} else if (IModuleModel.THEME_PROP.equals(propName)) {
					((Module) tmpElement).getTheme(module);
				} else
					tmpElement.getLocalProperty(module, propDefn);
			} else {
				// try to resolve the element reference for the structure
				// member.

				((Structure) reference).getLocalProperty(module, propName);
			}
		} else {
			unresolveBackRef(module, reference, referred, propName);
		}
	}

	/**
	 * Removes the back reference that established by a structure member value.
	 */

	private static void removeBackRefOfStructMember(Module module, Object reference, IReferencableElement referred,
			String propName) {
		Structure struct = (Structure) reference;
		Object value = struct.getLocalProperty(module, propName);

		assert value instanceof ElementRefValue;

		ElementRefValue refValue = (ElementRefValue) value;
		refValue.unresolved(refValue.getName());

		referred.dropClient(struct, propName);
	}

	/**
	 * Removes the back reference that established by a element property value.
	 */

	private static void removeElementRefOfProperty(Module module, Object reference, IReferencableElement referred,
			String propName) {
		DesignElement tmpElement = (DesignElement) reference;

		Object value = tmpElement.getLocalProperty(module, propName);
		if (value instanceof ElementRefValue) {
			ElementRefValue refValue = (ElementRefValue) value;
			refValue.unresolved(refValue.getName());

			referred.dropClient(tmpElement);
		} else if (value instanceof List) {
			List<Object> listValue = (List) value;
			for (int i = 0; i < listValue.size(); i++) {
				ElementRefValue item = (ElementRefValue) listValue.get(i);
				if (item.getElement() == referred) {
					item.unresolved(item.getName());
					referred.dropClient(tmpElement);
					break;
				}
			}
		}
	}

	public DesignElement getTarget() {
		return target;
	}

	/**
	 * 
	 * @param module
	 * @param reference
	 * @param referred
	 * @param propName
	 */
	static void unresolveBackRef(Module module, Object reference, IReferencableElement referred, String propName) {
		if (reference instanceof DesignElement) {
			removeElementRefOfProperty(module, reference, referred, propName);
		} else {
			removeBackRefOfStructMember(module, reference, referred, propName);
		}
	}
}