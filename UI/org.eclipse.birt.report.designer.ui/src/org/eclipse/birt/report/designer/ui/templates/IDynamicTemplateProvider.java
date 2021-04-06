/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.templates;

/**
 * IDynamicTemplateProvider
 * 
 * @since 2.5.3
 */
public interface IDynamicTemplateProvider extends ITemplateProvider {

	/**
	 * Signals the provider it should initialise/reinitialise itself. The provider
	 * implementation should finish this as quickly as possible and use the
	 * <code>callback</code> to notify further state change. Note this method can be
	 * called multiple times before <code>release()</code> is called, so it's up to
	 * the provider to clean up or maintain the state between the multiple calls.
	 * 
	 * @param callback
	 */
	void init(Callback callback);

	/**
	 * Callback
	 */
	public static interface Callback {

		/**
		 * Notifies the host the content of the given provider has been changed.
		 * 
		 * @param who
		 */
		void contentChanged(IDynamicTemplateProvider who);
	}
}
