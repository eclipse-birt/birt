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

package org.eclipse.birt.doc.romdoc;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;

public class DocStructure extends DocComposite {
	public DocStructure(StructureDefn struct) {
		super(struct);

		Iterator iter = struct.propertiesIterator();
		while (iter.hasNext()) {
			PropertyDefn propDefn = (PropertyDefn) iter.next();
			properties.add(new DocProperty(propDefn));
		}
		Collections.sort(properties, new DocComparator());
	}

	@Override
	public boolean isElement() {
		return false;
	}

}
