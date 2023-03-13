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
 * group defined in table item
 *
 * @see TableItemDesign
 */
public class TableGroupDesign extends GroupDesign {
	public TableGroupDesign() {
		// modify bug161912
		// header = new TableBandDesign( );
		// footer = new TableBandDesign( );
	}

	@Override
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitTableGroup(this, value);
	}

}
