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

package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * column definition used by table content.
 * 
 * 
 */
public interface IColumn extends IStyledElement {
	/**
	 * @return Return this column is a column header or not.
	 */
	public boolean isColumnHeader();

	/**
	 * @return Returns the width.
	 */
	public DimensionType getWidth();

	public void setWidth(DimensionType width);

	/**
	 * get the instance id of the column. the instance id is the unique id of the
	 * content.
	 * 
	 * @return
	 */
	public InstanceID getInstanceID();

	public String getVisibleFormat();

	/**
	 * @return inline style
	 */
	IStyle getInlineStyle();

	void setInlineStyle(IStyle style);

	public void setGenerateBy(Object generateBy);

	public Object getGenerateBy();

	public boolean hasDataItemsInDetail();

	public boolean isRepeated();

	public void setRepeated(boolean isRepeated);
}
