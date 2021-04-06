/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content;

/**
 * Provides interfaces for Group Content.
 */
public interface IGroupContent extends IContainerContent {
	/**
	 * Return the indication that the group header is repeated or not.
	 * 
	 * @return if the group header is repeated.
	 *         <p>
	 *         <code>true</code>, the group header is repeated. <code>false</code>,
	 *         the group header is not repeated.
	 */
	boolean isHeaderRepeat();

	/**
	 * Set the value if the group header is repeated. if <code>repeat</code> is
	 * true, the group header needs to be repeat. Or, <code>repeat</code> is false,
	 * the group header does not need to be repeat.
	 * 
	 * @param repeat if the group header is repeated.
	 */
	void setHeaderRepeat(boolean repeat);

	/**
	 * Get the header of the group.
	 * 
	 * @return the header of the group. Return <code>null</code> if the group does
	 *         not have a header.
	 */
	IBandContent getHeader();

	/**
	 * Get the footer of the group.
	 * 
	 * @return the footer of the group. Return <code>null</code> if the group does
	 *         not have a header.
	 */
	IBandContent getFooter();

	/**
	 * Get the unique id of the group.
	 * 
	 * @return the unique id of the group.
	 */
	String getGroupID();

	/**
	 * Set the unique id of the group.
	 * 
	 * @param groupId the id of the group.
	 */
	void setGroupID(String groupId);

	/**
	 * Get the level of the group. The default level of the group is <code>0</code>
	 * 
	 * @return the level of the group.
	 */
	int getGroupLevel();
}
