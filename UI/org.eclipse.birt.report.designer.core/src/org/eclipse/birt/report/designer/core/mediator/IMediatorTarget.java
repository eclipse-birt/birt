/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
