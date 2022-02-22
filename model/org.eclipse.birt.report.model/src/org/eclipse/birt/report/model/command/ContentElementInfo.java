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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Describes where to send out the event.
 *
 */

public class ContentElementInfo {

	private DesignElement element;

	private PropertyDefn propDefn;

	private List<Step> path = null;

	private boolean enablePath = false;

	/**
	 * Constructor.
	 *
	 * @param element  the element
	 * @param propDefn the property definition
	 */

	public ContentElementInfo(DesignElement element, PropertyDefn propDefn) {
		this.element = element;
		this.propDefn = propDefn;
	}

	/**
	 * Constructor.
	 *
	 * @param enablePath <code>true</code> to enable path trace
	 */

	public ContentElementInfo(boolean enablePath) {
		this.enablePath = enablePath;
		path = new ArrayList<>();
	}

	/**
	 * Returns the event destination.
	 *
	 * @return the element
	 */

	public DesignElement getElement() {
		return element;
	}

	/**
	 * Returns the property name of the target event.
	 *
	 * @return the property name
	 */

	public String getPropName() {
		if (propDefn != null) {
			return propDefn.getName();
		}

		if (path.isEmpty()) {
			return null;
		}

		Step topStep = path.get(path.size() - 1);
		propDefn = topStep.stepPropDefn;
		return propDefn.getName();

	}

	/**
	 * Adds one step.
	 *
	 * @param stepPropDefn
	 * @param index
	 */

	public void pushStep(PropertyDefn stepPropDefn, int index) {
		if (enablePath) {
			path.add(new Step(stepPropDefn, index));
		}
	}

	/**
	 * Sets the top container that is not content element.
	 *
	 * @param topElement
	 */

	public void setTopElement(DesignElement topElement) {
		assert !(topElement instanceof ContentElement);
		element = topElement;
	}

	/**
	 * Returns the iterator for the steps.
	 *
	 * @return the list of the step
	 */

	public List<Step> stepIterator() {
		if (path == null) {
			return Collections.emptyList();
		}

		return path;
	}

	/**
	 * Copies the path from the target to this info.
	 *
	 * @param target
	 */

	public void copyPath(ContentElementInfo target) {
		if (target == null) {
			return;
		}

		path = target.path;
	}

	/**
	 *
	 *
	 */

	static class Step {

		/**
		 * The property definition or member definition.
		 */
		protected PropertyDefn stepPropDefn;

		/**
		 * The index position where the content resides in the list.
		 */
		protected int index = -1;

		/**
		 * @param propDefn
		 * @param index
		 */

		Step(PropertyDefn propDefn, int index) {
			this.stepPropDefn = propDefn;
			this.index = index;
		}
	}
}
