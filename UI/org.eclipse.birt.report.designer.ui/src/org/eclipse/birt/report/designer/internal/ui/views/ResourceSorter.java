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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Sorter for viewers that display items of type <code>IResource</code>. The
 * sorter supports two sort criteria:
 * <p>
 * <code>NAME</code>: Folders are given order precedence, followed by files.
 * Within these two groups resources are ordered by name. All name comparisons
 * are case-insensitive.
 * </p>
 * <p>
 * <code>TYPE</code>: Folders are given order precedence, followed by files.
 * Within these two groups resources are ordered by extension. All extension
 * comparisons are case-insensitive.
 * </p>
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class ResourceSorter extends ViewerSorter {

	/**
	 * Constructor argument value that indicates to sort items by name.
	 */
	public final static int NAME = 1;

	/**
	 * Constructor argument value that indicates to sort items by extension.
	 */
	public final static int TYPE = 2;

	private int criteria;

	/**
	 * Creates a resource sorter that will use the given sort criteria.
	 *
	 * @param criteria the sort criterion to use: one of <code>NAME</code> or
	 *                 <code>TYPE</code>
	 */
	public ResourceSorter(int criteria) {
		super();
		this.criteria = criteria;
	}

	/**
	 * Returns an integer value representing the relative sort priority of the given
	 * element based on its class.
	 * <p>
	 * <ul>
	 * <li>resources (<code>IResource</code>) - 2</li>
	 * <li>project references (<code>ProjectReference</code>) - 1</li>
	 * <li>everything else - 0</li>
	 * </ul>
	 * </p>
	 *
	 * @param element the element
	 * @return the sort priority (larger numbers means more important)
	 */
	protected int classComparison(Object element) {
		if (element instanceof IResource) {
			return 2;
		}
		return 0;
	}

	/*
	 * (non-Javadoc) Method declared on ViewerSorter.
	 */
	@Override
	public int compare(Viewer viewer, Object o1, Object o2) {
		// have to deal with non-resources in navigator
		// if one or both objects are not resources, returned a comparison
		// based on class.
		if (!(o1 instanceof IResource && o2 instanceof IResource)) {
			return compareClass(o1, o2);
		}
		IResource r1 = (IResource) o1;
		IResource r2 = (IResource) o2;

		if (r1 instanceof IContainer && r2 instanceof IContainer) {
			return compareNames(r1, r2);
		} else if (r1 instanceof IContainer) {
			return -1;
		} else if (r2 instanceof IContainer) {
			return 1;
		} else if (criteria == NAME) {
			return compareNames(r1, r2);
		} else if (criteria == TYPE) {
			return compareTypes(r1, r2);
		} else {
			return 0;
		}
	}

	/**
	 * Returns a number reflecting the collation order of the given elements based
	 * on their class.
	 *
	 * @param element1 the first element to be ordered
	 * @param element2 the second element to be ordered
	 * @return a negative number if the first element is less than the second
	 *         element; the value <code>0</code> if the first element is equal to
	 *         the second element; and a positive number if the first element is
	 *         greater than the second element
	 */
	protected int compareClass(Object element1, Object element2) {
		return classComparison(element1) - classComparison(element2);
	}

	/**
	 * Returns a number reflecting the collation order of the given resources based
	 * on their resource names.
	 *
	 * @param resource1 the first resource element to be ordered
	 * @param resource2 the second resource element to be ordered
	 * @return a negative number if the first element is less than the second
	 *         element; the value <code>0</code> if the first element is equal to
	 *         the second element; and a positive number if the first element is
	 *         greater than the second element
	 */
	protected int compareNames(IResource resource1, IResource resource2) {
		return collator.compare(resource1.getName(), resource2.getName());
	}

	/**
	 * Returns a number reflecting the collation order of the given resources based
	 * on their respective file extensions. Resources with the same file extension
	 * will be collated based on their names.
	 *
	 * @param resource1 the first resource element to be ordered
	 * @param resource2 the second resource element to be ordered
	 * @return a negative number if the first element is less than the second
	 *         element; the value <code>0</code> if the first element is equal to
	 *         the second element; and a positive number if the first element is
	 *         greater than the second element
	 */
	protected int compareTypes(IResource resource1, IResource resource2) {
		String ext1 = getExtensionFor(resource1);
		String ext2 = getExtensionFor(resource2);

		// Compare extensions. If they're different then return a value that
		// indicates correct extension ordering. If they're the same then
		// return a value that indicates the correct NAME ordering.
		int result = collator.compare(ext1, ext2);

		if (result != 0) { // ie.- different extensions
			return result;
		}

		return compareNames(resource1, resource2);
	}

	/**
	 * Returns the sort criteria of this sorter.
	 *
	 * @return the sort criterion: one of <code>NAME</code> or <code>TYPE</code>
	 */
	public int getCriteria() {
		return criteria;
	}

	/**
	 * Returns the extension portion of the given resource.
	 *
	 * @param resource the resource
	 * @return the file extension, possibily the empty string
	 */
	private String getExtensionFor(IResource resource) {
		String ext = resource.getFileExtension();
		return ext == null ? "" : ext; //$NON-NLS-1$
	}

	/**
	 * Sets the sort criteria of this sorter.
	 *
	 * @param criteria the sort criterion: one of <code>ResourceSorter.NAME</code>
	 *                 or <code>ResourceSorter.TYPE</code>
	 */
	public void setCriteria(int criteria) {
		this.criteria = criteria;
	}
}
