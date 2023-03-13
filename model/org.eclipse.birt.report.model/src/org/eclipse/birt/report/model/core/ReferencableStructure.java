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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefPropertyType;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * Represents a structure that can be referenced using a property of name type.
 * This structure maintains a cached set of back-references to the "clients" so
 * that changes can be automatically propagated.
 *
 */

public abstract class ReferencableStructure extends Structure implements IReferencable {

	/**
	 * Name of the "libReference" property.
	 */

	public static final String LIB_REFERENCE_MEMBER = "libReference"; //$NON-NLS-1$

	/**
	 * The list of cached clients.
	 */

	protected ArrayList<BackRef> clients = new ArrayList<>();

	/**
	 * The library reference of this structure. It consists of namespace and name of
	 * the referred structure.
	 */

	protected StructRefValue libReference = null;

	/**
	 * The list of the cached reference structures.
	 */

	protected ArrayList<Structure> clientStructures = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.core.IStructure#isReferencable()
	 */

	@Override
	public boolean isReferencable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IReferencable#addClient(org.eclipse
	 * .birt.report.model.core.DesignElement, java.lang.String)
	 */
	@Override
	public void addClient(DesignElement client, String propName) {
		clients.add(new BackRef(client, propName));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IReferencable#dropClient(org.eclipse
	 * .birt.report.model.core.DesignElement)
	 */
	@Override
	public void dropClient(DesignElement client) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getElement() == client) {
				clients.remove(i);
				return;
			}
		}
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IReferencable#getClientList()
	 */

	@Override
	public List<BackRef> getClientList() {
		return clients;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.core.IReferencable#hasReferences()
	 */

	@Override
	public boolean hasReferences() {
		return !clients.isEmpty();
	}

	/**
	 * Sends the event to all clients in addition to the routing for a design
	 * element.
	 *
	 * @param ev the event to send
	 */

	public void broadcast(NotificationEvent ev) {
		ev.setDeliveryPath(NotificationEvent.STRUCTURE_CLIENT);
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).getElement().broadcast(ev);
		}
	}

	/**
	 * Checks whether the member of the input name is the referencable member or
	 * not.
	 *
	 * @param memberName the member name to check
	 * @return true if the member with the given name is referencable, otherwise
	 *         false
	 */

	public abstract boolean isReferencableProperty(String memberName);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	@Override
	protected Object getIntrinsicProperty(String propName) {
		if (LIB_REFERENCE_MEMBER.equalsIgnoreCase(propName)) {
			return this.libReference;
		}
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getProperty(org.eclipse.
	 * birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	@Override
	public Object getProperty(Module module, PropertyDefn propDefn) {
		assert propDefn != null;

		// return local value first

		Object value = getLocalProperty(module, propDefn);
		if (value != null) {
			return value;
		}

		// first, read property values in local ; second, check the library
		// reference and load the property values from it; third, read default
		// in ROM

		if (libReference != null) {
			ReferencableStructure refStruct = libReference.getTargetStructure();
			if (refStruct != null) {
				Module root = null;
				if (module != null) {
					root = module.getLibraryWithNamespace(libReference.getLibraryNamespace(),
							IAccessControl.DIRECTLY_INCLUDED_LEVEL);
				}
				value = refStruct.getProperty(root, propDefn);
				if (value != null) {
					return value;
				}
			}
		}

		return propDefn.getDefault();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getLocalProperty(org.eclipse
	 * .birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	@Override
	public Object getLocalProperty(Module module, PropertyDefn propDefn) {

		// try to resolve the "libReference" first

		if (libReference != null && !libReference.isResolved()) {
			PropertyDefn libRefDefn = (PropertyDefn) getDefn().getMember(LIB_REFERENCE_MEMBER);
			assert libRefDefn != null;
			StructRefPropertyType type = (StructRefPropertyType) libRefDefn.getType();
			type.resolve(module, libRefDefn, libReference);
			if (libReference.isResolved()) {
				libReference.getTargetStructure().addClientStructure(this);
			}
		}

		return super.getLocalProperty(module, propDefn);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String propName, Object value) {
		if (LIB_REFERENCE_MEMBER.equalsIgnoreCase(propName)) {
			updateReference(libReference, (StructRefValue) value);
			libReference = (StructRefValue) value;
		} else {
			assert false;
		}
	}

	/**
	 * Implements to cache a back-pointer from a referenced structure.
	 *
	 * @param oldRef the old reference, if any
	 * @param newRef the new reference, if any
	 */

	protected void updateReference(StructRefValue oldRef, StructRefValue newRef) {
		ReferencableStructure target;

		// Drop the old reference. Clear the back pointer from the referenced
		// element to this element.

		if (oldRef != null) {
			target = oldRef.getTargetStructure();
			if (target != null) {
				target.dropClientStructure(this);
			}
		}

		// Add the new reference. Cache a back pointer from the referenced
		// element to this element. Include the property name so we know which
		// property to adjust it the target is deleted.

		if (newRef != null) {
			target = newRef.getTargetStructure();
			if (target != null) {
				target.addClientStructure(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt
	 * .report.model.core.Module, org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	public List<SemanticException> validate(Module module, DesignElement element) {
		List<SemanticException> errors = new ArrayList<>();

		// if the library reference is un-resolved, fire an error

		StructRefValue ref = (StructRefValue) getLocalProperty(module, LIB_REFERENCE_MEMBER);

		if (ref != null && !ref.isResolved()) {

			errors.add(new SemanticError(element,
					new String[] { getDefn().getName(), getReferencableProperty(),
							libReference.getQualifiedReference() },
					SemanticError.DESIGN_EXCEPTION_INVALID_LIBRARY_REFERENCE));
			return errors;
		}

		return errors;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */

	@Override
	protected Object clone() throws CloneNotSupportedException {
		ReferencableStructure struct = (ReferencableStructure) super.clone();
		struct.libReference = null;
		struct.clients = new ArrayList<>();
		struct.clientStructures = new ArrayList<>();

		if (libReference == null) {
			return struct;
		}

		// retrieve the member value from the lib reference

		Iterator<IPropertyDefn> propIter = getDefn().propertiesIterator();
		while (propIter.hasNext()) {
			PropertyDefn prop = (PropertyDefn) propIter.next();

			// if the structure has the local value already or the member is
			// "libReference", then return

			if (struct.getLocalProperty(null, prop) != null || LIB_REFERENCE_MEMBER.equals(prop.getName())) {
				continue;
			}

			StructRefValue libRef = this.libReference;
			while (libRef != null) {
				ReferencableStructure libStructure = libRef.getTargetStructure();
				if (libStructure == null) {
					struct.libReference = new StructRefValue(libReference.getLibraryNamespace(),
							libReference.getName());
					return struct;
				}

				Object value = libStructure.getLocalProperty(null, prop);
				if (value != null) {
					struct.setProperty(prop, value);
					break;
				}

				libRef = (StructRefValue) libStructure.getLocalProperty(null, LIB_REFERENCE_MEMBER);
			}
		}

		return struct;

	}

	/**
	 * Adds a client structure of this.
	 *
	 * @param struct the structure that refers this struct
	 */

	public void addClientStructure(Structure struct) {
		clientStructures.add(struct);
	}

	/**
	 * Drops one of the client of this structure.
	 *
	 * @param struct the client to drop
	 */

	public void dropClientStructure(Structure struct) {
		assert clientStructures.contains(struct);
		clientStructures.remove(struct);
	}

	/**
	 * Returns all the client structures.
	 *
	 * @return all the client structures
	 */

	public List<Structure> getClientStructures() {
		return clientStructures;
	}
}
