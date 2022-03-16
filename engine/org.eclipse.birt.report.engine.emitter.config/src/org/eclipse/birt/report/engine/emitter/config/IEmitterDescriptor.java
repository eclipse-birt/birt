/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.config;

import java.util.Locale;
import java.util.Map;

/**
 * This interface is a representation of emitter descriptor.
 */
public interface IEmitterDescriptor {

	/**
	 * Returns the ID of this emitter descriptor.
	 */
	String getID();

	/**
	 * Returns the display name of this emitter descriptor.
	 */
	String getDisplayName();

	/**
	 * Returns the description of this emitter.
	 */
	String getDescription();

	/**
	 * Sets the init parameters for this descriptor. This method is always called
	 * right after the instantiation and before any other method call. Note if no
	 * init parameter defined, this method may not get called. This method should
	 * only be called by the <code>IEmitterConfigurationManager</code>.
	 */
	void setInitParameters(Map params);

	/**
	 * Creates the option observer if applicable.
	 */
	IConfigurableOptionObserver createOptionObserver();

	void setLocale(Locale locale);

	/**
	 * Indicates whether this emitter is enabled
	 *
	 * @return true if this emitter is enabled
	 */
	boolean isEnabled();
}
