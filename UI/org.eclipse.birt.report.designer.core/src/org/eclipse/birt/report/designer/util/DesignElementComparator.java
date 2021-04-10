/*******************************************************************************
 * Copyright (c) 2004 - 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.util;

import java.util.Comparator;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import com.ibm.icu.text.Collator;

/**
 * The comparator for design element
 */
public class DesignElementComparator implements Comparator {

	private boolean ascending = true;
	private int ret = 0;

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
			name1 = ((DesignElementHandle) o1).getDefn().getName();
			name2 = ((DesignElementHandle) o2).getDefn().getName();

			if (ascending) {
				ret = Collator.getInstance().compare(name1, name2);
			} else {
				ret = Collator.getInstance().compare(name2, name1);
			}
			if (ret != 0) {
				return ret;
			}

			// if ret == 0
			AlphabeticallyComparator comparator = new AlphabeticallyComparator();
			return comparator.compare(o1, o2);

		}

		return 0;
	}

}
