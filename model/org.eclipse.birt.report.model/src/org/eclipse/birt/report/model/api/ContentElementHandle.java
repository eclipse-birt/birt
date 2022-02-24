/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 *
 */

package org.eclipse.birt.report.model.api;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 *
 *
 */

public abstract class ContentElementHandle extends DesignElementHandle {

	/**
	 * The target report element.
	 */

	protected DesignElement element;

	/**
	 * Constructs the handle for a report element with the given design and element.
	 * The application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ContentElementHandle(Module module, DesignElement element) {
		super(module);
		assert element != null;
		this.element = element;
	}

	// Implementation of an abstract method in the base class.

	@Override
	public final DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#addListener(org
	 * .eclipse.birt.report.model.api.core.Listener)
	 */

	@Override
	public final void addListener(Listener obj) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.DesignElementHandle#
	 * isTemplateParameterValue()
	 */
	@Override
	public final boolean isTemplateParameterValue() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#localize()
	 */

	@Override
	public final void localize() throws SemanticException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#removeListener(
	 * org.eclipse.birt.report.model.api.core.Listener)
	 */
	@Override
	public final void removeListener(Listener obj) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#revertToReportItem
	 * ()
	 */
	@Override
	public final void revertToReportItem() throws SemanticException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#revertToTemplate
	 * (java.lang.String)
	 */
	@Override
	public final TemplateElementHandle revertToTemplate(String name) throws SemanticException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#canTransformToTemplate
	 * ()
	 */
	@Override
	public final boolean canTransformToTemplate() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#clientsIterator()
	 */

	@Override
	public final Iterator clientsIterator() {
		return Collections.EMPTY_LIST.iterator();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#createTemplateElement
	 * (java.lang.String)
	 */

	@Override
	public final TemplateElementHandle createTemplateElement(String name) throws SemanticException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#derivedIterator()
	 */

	@Override
	public final Iterator derivedIterator() {
		return Collections.EMPTY_LIST.iterator();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getName()
	 */

	@Override
	public String getName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getPrivateStyle()
	 */
	@Override
	public final StyleHandle getPrivateStyle() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getQualifiedName()
	 */

	@Override
	public final String getQualifiedName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#setEventHandlerClass
	 * (java.lang.String)
	 */
	@Override
	public final void setEventHandlerClass(String expr) throws SemanticException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setExtends(org.
	 * eclipse.birt.report.model.api.DesignElementHandle)
	 */
	@Override
	public final void setExtends(DesignElementHandle parent) throws ExtendsException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setExtendsName(
	 * java.lang.String)
	 */
	@Override
	public final void setExtendsName(String name) throws ExtendsException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setName(java.lang
	 * .String)
	 */

	@Override
	public void setName(String name) throws NameException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#getEventHandlerClass ()
	 */

	@Override
	public String getEventHandlerClass() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getExtends()
	 */
	@Override
	public DesignElementHandle getExtends() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#setStyle(org.eclipse
	 * .birt.report.model.api.SharedStyleHandle)
	 */
	@Override
	public void setStyle(SharedStyleHandle style) throws StyleException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setStyleName(java
	 * .lang.String)
	 */
	@Override
	public void setStyleName(String name) throws StyleException {

	}
}
