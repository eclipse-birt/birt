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
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * The implementation of methods on <code>IReferencableElement</code>.
 * 
 */

public class ReferenceableElementAdapter implements IReferencableElement, Cloneable {

	/**
	 * The list of cached clients.
	 */

	protected ArrayList<BackRef> clients = new ArrayList<BackRef>();

	/**
	 * The design element.
	 */

	private DesignElement element;

	/**
	 * The constructor.
	 * 
	 * @param element the element
	 */

	public ReferenceableElementAdapter(DesignElement element) {
		this.element = element;
	}

	/**
	 * The constructor.
	 * 
	 * @param element the styled element
	 */

	public ReferenceableElementAdapter(ReferencableStyledElement element) {
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.IReferencableElement#doClone(org.eclipse
	 * .birt.report.model.elements.strategy.CopyPolicy)
	 */

	public Object clone() throws CloneNotSupportedException {
		ReferenceableElementAdapter adapter = (ReferenceableElementAdapter) super.clone();

		adapter.clients = new ArrayList<BackRef>();
		return adapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#addClient(org
	 * .eclipse.birt.report.model.core.DesignElement, java.lang.String)
	 */

	public void addClient(DesignElement client, String propName) {
		clients.add(new BackRef(client, propName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#dropClient(org
	 * .eclipse.birt.report.model.core.DesignElement)
	 */

	public void dropClient(DesignElement client) {
		dropClient(client, (String) null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#dropClient(org
	 * .eclipse.birt.report.model.core.DesignElement, java.lang.String)
	 */

	public void dropClient(DesignElement client, String propName) {

		for (int i = 0; i < clients.size(); i++) {
			BackRef ref = clients.get(i);
			if (ref.getElement() == client && (propName == null || ref.getPropertyName().equals(propName))) {
				clients.remove(i);
				return;
			}
		}
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#getClientList()
	 */

	public List<BackRef> getClientList() {
		return new ArrayList<BackRef>(clients);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#hasReferences()
	 */

	public boolean hasReferences() {
		return !clients.isEmpty();
	}

	/**
	 * Broadcasts the event to clients.
	 * 
	 * @param ev     the event to broadcast
	 * @param module the module
	 */

	public void broadcastToClients(NotificationEvent ev, Module module) {
		for (int i = 0; i < clients.size(); i++) {
			BackRef ref = clients.get(i);
			DesignElement target = ref.getElement();
			if (IDesignElementModel.EXTENDS_PROP.equalsIgnoreCase(ref.getPropertyName()))
				ev.setDeliveryPath(NotificationEvent.EXTENDS_EVENT);
			else if (IStyledElementModel.STYLE_PROP.equalsIgnoreCase(ref.getPropertyName()))
				ev.setDeliveryPath(NotificationEvent.STYLE_CLIENT);
			else
				ev.setDeliveryPath(NotificationEvent.ELEMENT_CLIENT);

			target.broadcast(ev, module);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.core.IReferencableElement#
	 * updateClientReferences()
	 */

	public void updateClientReferences() {
		// creates another list for the iterator

		Iterator<BackRef> backRefIter = new ArrayList<BackRef>(clients).iterator();

		while (backRefIter.hasNext()) {
			BackRef ref = backRefIter.next();
			DesignElement client = ref.getElement();

			Module root = client.getRoot();

			Structure struct = ref.getStructure();
			Object value = null;
			if (struct != null) {
				value = struct.getLocalProperty(root, ref.getPropertyName());
			} else {
				value = client.getLocalProperty(root, ref.getPropertyName());
			}

			if (value instanceof ElementRefValue) {
				ElementRefValue refValue = (ElementRefValue) value;
				refValue.unresolved(refValue.getName());
				dropClient(client);
			} else if (value instanceof List) {
				List<Object> valueList = (List<Object>) value;
				for (int i = 0; i < valueList.size(); i++) {
					ElementRefValue item = (ElementRefValue) valueList.get(i);
					if (item.getElement() == element) {
						item.unresolved(item.getName());
						dropClient(client);
					}
				}
			}

			// for the style, send out a event to let UI repaint the element.
			// otherwise, try to resolve it.
			if (IStyledElementModel.STYLE_PROP.equalsIgnoreCase(ref.getPropertyName()))
				client.broadcast(new StyleEvent(client));
			else
				client.resolveElementReference(root, client.getPropertyDefn(ref.getPropertyName()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#clearClients()
	 */

	public void clearClients() {
		clients = new ArrayList<BackRef>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#addClient(org
	 * .eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.CachedMemberRef,
	 * org.eclipse.birt.report.model.core.Structure)
	 */
	public void addClient(Structure struct, String memberName) {
		clients.add(new BackRef(struct, memberName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#dropClient(org
	 * .eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.CachedMemberRef,
	 * org.eclipse.birt.report.model.core.Structure)
	 */

	public void dropClient(Structure struct, String memberName) {
		for (int i = 0; i < clients.size(); i++) {
			BackRef ref = clients.get(i);
			if (ref.getStructure() == struct && (ref.getPropertyName().equalsIgnoreCase(memberName))) {
				clients.remove(i);
				return;
			}
		}
		assert false;
	}
}
