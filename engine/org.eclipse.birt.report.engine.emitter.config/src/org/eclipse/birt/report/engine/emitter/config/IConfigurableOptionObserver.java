/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config;

import java.util.List;

import org.eclipse.birt.report.engine.api.IRenderOption;

/**
 * IOptionObserver
 */
public interface IConfigurableOptionObserver {

	/**
	 * Returns all configurable options of this emitter.
	 */
	IConfigurableOption[] getOptions();

	/**
	 * Gets called when one of the options is changed or this observer is
	 * initialized from a previous state. If it returns <code>true</code>, means the
	 * caller need sychronize the option status by calling <code>getOptions()</code>
	 * again.
	 * 
	 * @param values The option values.
	 * @return Returns <code>true</code> if the option status need be synchronized;
	 *         <code>false</code> for doing nothing.
	 */
	boolean update(IOptionValue... values);

	/**
	 * It should be called before updating the values to check whether the input
	 * values are valid. If non of the values are invalid, the returned list is
	 * empty;otherwise, error BirtException list will be returned.
	 * 
	 * @param values
	 * @return
	 */
	List validate(IOptionValue... values);

	/**
	 * @return Returns the option values based on current state.
	 */
	IOptionValue[] getOptionValues();

	/**
	 * Returns the preferred render option based on current state.
	 */
	IRenderOption getPreferredRenderOption();

}
