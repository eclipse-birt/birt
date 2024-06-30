/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.mediator;

/**
 * IMediatorTarget
 */
public interface IMediatorTarget {

	/**
	 * Adds the dispose listener. The target should notify the listener once it's
	 * disposed. The target should ensure adding a duplicate listener has no effect.
	 *
	 * @param listener
	 */
	void addDisposeListener(ITargetDisposeListener listener);

	/**
	 * Removes the dispose listener.
	 *
	 * @param listener
	 */
	void removeDisposeListener(ITargetDisposeListener listener);

	/**
	 * ITargetDisposeListener
	 */
	public interface ITargetDisposeListener {

		void dispose(IMediatorTarget target);
	}
}
