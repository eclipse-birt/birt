/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import org.eclipse.birt.report.engine.ir.BandDesign;

/**
 * Provides the interface for the Band Content. All types of bands for table,
 * group, list, such as table-header/detail/footer, group-header/detail/footer,
 * list-header/detail/footer, are implemented as instances of this class.
 * <p>
 * The following types for the band content are predefined:
 * <li><code>BAND_HEADER</code></li>
 * <li><code>BAND_FOOTER</code></li>
 * <li><code>GROUP_HEADER</code></li>
 * <li><code>GROUP_FOOTER</code></li>
 * <li><code>BAND_DETAIL</code></li>
 */
public interface IBandContent extends IContainerContent {

	public static final int BAND_HEADER = BandDesign.BAND_HEADER;
	public static final int BAND_FOOTER = BandDesign.BAND_FOOTER;
	public static final int BAND_GROUP_HEADER = BandDesign.GROUP_HEADER;
	public static final int BAND_GROUP_FOOTER = BandDesign.GROUP_FOOTER;
	public static final int BAND_DETAIL = BandDesign.BAND_DETAIL;

	/**
	 * Get the type of the band content. The return result of this method is in
	 * <code>int</code> format, and the <code>int</code> value must be one of the
	 * following predefined values in class <code>IBandContent</code>:
	 * <li><code>BAND_HEADER</code></li>
	 * <li><code>BAND_FOOTER</code></li>
	 * <li><code>GROUP_HEADER</code></li>
	 * <li><code>GROUP_FOOTER</code></li>
	 * <li><code>BAND_DETAIL</code></li>
	 * 
	 * @return type of the band content.
	 */
	int getBandType();

	/**
	 * Set the type of the band content. The value of parameter
	 * <code>bandType</code> must be one of the following predefined values in class
	 * <code>IBandContent</code>:
	 * <li><code>BAND_HEADER</code></li>
	 * <li><code>BAND_FOOTER</code></li>
	 * <li><code>GROUP_HEADER</code></li>
	 * <li><code>GROUP_FOOTER</code></li>
	 * <li><code>BAND_DETAIL</code></li>
	 * 
	 * @param bandType the type of the band content.
	 */
	void setBandType(int bandType);

	/**
	 * Get the group id if the band content is in some group. This method will
	 * return <code>null</code> when the band content is not a group header and
	 * group footer.
	 * 
	 * @return the group id.
	 */
	String getGroupID();
}
