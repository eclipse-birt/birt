/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.element.IList;
import org.eclipse.birt.report.model.api.ListHandle;

public class List extends Listing implements IList {

	public List(ListHandle list) {
		super(list);
	}

	public List(org.eclipse.birt.report.model.api.simpleapi.IList listImpl) {
		super(null);
		designElementImpl = listImpl;
	}

}
