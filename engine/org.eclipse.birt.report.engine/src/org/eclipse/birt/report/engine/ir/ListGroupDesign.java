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

package org.eclipse.birt.report.engine.ir;

/**
 * List group
 * 
 */
public class ListGroupDesign extends GroupDesign {
	public ListGroupDesign() {
		// modify bug161912
		// header = new ListBandDesign( );
		// footer = new ListBandDesign( );
	}

	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitListGroup(this, value);
	}

}
