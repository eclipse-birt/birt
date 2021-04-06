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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;

/**
 * Represents a styled element that can be referenced using an element
 * reference. This element maintains a cached set of back-references to the
 * "clients" so that changes can be automatically propagated.
 * 
 */

public abstract class ReferencableStyledElement extends StyledElement implements IReferencable, IReferencableElement {

	private IReferencableElement adapter = null;

	/**
	 * Default constructor.
	 */

	public ReferencableStyledElement() {
		adapter = new ReferenceableElementAdapter(this);
	}

	/**
	 * Constructs the ReferenceableElement with the element name.
	 * 
	 * @param theName the element name
	 */

	public ReferencableStyledElement(String theName) {
		super(theName);
		adapter = new ReferenceableElementAdapter(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.IReferencableElement#doClone(org.eclipse
	 * .birt.report.model.elements.strategy.CopyPolicy)
	 */

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		ReferencableStyledElement element = (ReferencableStyledElement) super.doClone(policy);
		element.adapter = (IReferencableElement) ((ReferenceableElementAdapter) adapter).clone();
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#addClient(org
	 * .eclipse.birt.report.model.core.DesignElement, java.lang.String)
	 */

	public void addClient(DesignElement client, String propName) {
		adapter.addClient(client, propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#dropClient(org
	 * .eclipse.birt.report.model.core.DesignElement)
	 */

	public void dropClient(DesignElement client) {
		adapter.dropClient(client, (String) null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#dropClient(org
	 * .eclipse.birt.report.model.core.DesignElement, java.lang.String)
	 */

	public void dropClient(DesignElement client, String propName) {
		adapter.dropClient(client, propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#getClientList()
	 */

	public List<BackRef> getClientList() {
		return adapter.getClientList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IReferencable#hasReferences()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#hasReferences()
	 */

	public boolean hasReferences() {
		return adapter.hasReferences();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#broadcast(org
	 * .eclipse.birt.report.model.api.activity.NotificationEvent,
	 * org.eclipse.birt.report.model.core.Module)
	 */

	public void broadcast(NotificationEvent ev, Module module) {
		super.broadcast(ev, module);
		broadcastToClients(ev, module);
	}

	/**
	 * Broadcasts the event to clients.
	 * 
	 * @param ev     the event to broadcast
	 * @param module the module
	 */

	protected void broadcastToClients(NotificationEvent ev, Module module) {
		((ReferenceableElementAdapter) adapter).broadcastToClients(ev, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.core.IReferencableElement#
	 * updateClientReferences()
	 */

	public void updateClientReferences() {
		adapter.updateClientReferences();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#clearClients()
	 */

	public void clearClients() {
		adapter.clearClients();
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
		adapter.addClient(struct, memberName);
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
		adapter.dropClient(struct, memberName);
	}
}