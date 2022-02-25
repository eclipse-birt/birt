/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.swt.graphics.Image;

/**
 * This class is a representation of resource entry for report element.
 */
public class ReportElementEntry extends ReportResourceEntry {

	/** The element in report. */
	private final Object element;

	/** The parent entry. */
	private final ResourceEntry parent;

	/** The node provider for the element. */
	private final INodeProvider provider;

	/**
	 * Constructs a resource entry for the specified report element.
	 *
	 * @param element the specified report element.
	 * @param parent  the parent entry.
	 */
	public ReportElementEntry(Object element, ResourceEntry parent) {
		this.element = element;
		this.parent = parent;
		this.provider = ProviderFactory.createProvider(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry#
	 * getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return provider.getNodeDisplayName(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry#
	 * getImage()
	 */
	@Override
	public Image getImage() {
		return provider.getNodeIcon(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry#
	 * getName()
	 */
	@Override
	public String getName() {
		return provider.getNodeDisplayName(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry#
	 * getParent()
	 */
	@Override
	public ResourceEntry getParent() {
		return parent;
	}

	@Override
	public int hashCode() {
		if (element == null) {
			return 0;
		}
		return element.getClass().hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !object.getClass().equals(getClass())) {
			return false;
		}

		ReportElementEntry entry = (ReportElementEntry) object;
		Object entryElement = entry.getReportElement();

		if (entry == this || entryElement == element) {
			return true;
		} else if (element != null) {
			if (element.equals(entryElement)) {
				return true;
			} else if (parent != null && parent.equals(entry.getParent())
					&& element.getClass().equals(entryElement.getClass())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry
	 * #getReportElement()
	 */
	@Override
	public Object getReportElement() {
		return element;
	}
}
