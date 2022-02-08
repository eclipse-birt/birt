/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.util;

import java.util.Comparator;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;

import com.ibm.icu.text.Collator;

/**
 * A text based comparator is used to reorder the elements.
 * 
 * @see IStructuredContentProvider
 * @see StructuredViewer
 */
public class AlphabeticallyComparator implements Comparator<Object> {

	private boolean ascending = true;

	/**
	 * Compare the two objects
	 * 
	 * @param o1 object1
	 * @param 02 object2
	 * @return the compare result
	 */
	public int compare(Object o1, Object o2) {
		String name1 = null;
		String name2 = null;

		if (o1 instanceof DesignElementHandle && o2 instanceof DesignElementHandle) {
			name1 = ((DesignElementHandle) o1).getDisplayLabel();
			name2 = ((DesignElementHandle) o2).getDisplayLabel();
			if (name1 == null) {
				name1 = ((DesignElementHandle) o1).getName();
			}
			if (name2 == null) {
				name2 = ((DesignElementHandle) o2).getName();
			}

		}
		if (o1 instanceof IChoice && o2 instanceof IChoice) {
			name1 = ((IChoice) o1).getDisplayName();
			name2 = ((IChoice) o2).getDisplayName();
			if (name1 == null) {
				name1 = ((IChoice) o1).getName();
			}
			if (name2 == null) {
				name2 = ((IChoice) o2).getName();
			}

		}
		if (name1 == null) {
			name1 = o1.toString();
		}
		if (name2 == null) {
			name2 = o2.toString();
		}

		if (name1 == null) {
			name1 = "";//$NON-NLS-1$
		}
		if (name2 == null) {
			name2 = "";//$NON-NLS-1$
		}

		if (ascending) {
			return Collator.getInstance().compare(name1, name2);
		} else {
			return Collator.getInstance().compare(name2, name1);
		}

	}

	/**
	 * Set order of this sort True: Ascending False: Deascending
	 * 
	 * @param ascending
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}
