/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.chart.factory;

import java.util.Locale;

import com.ibm.icu.util.ULocale;

/**
 * Provides services for externalization of static text messages rendered in a
 * chart. The chart title and axis titles are presently externalizable. Custom
 * series types may choose to make use of the externalization service provided
 * by this interface.
 *
 * Note that the externalization service needs to be implemented by a target
 * host service. This service is provided by the BIRT reporting context.
 */
public interface IMessageLookup {

	/**
	 * Defines a separator for a fully externalized message reference containing a
	 * key on the LHS and a value on the RHS separated by the key separator.
	 */
	char KEY_SEPARATOR = '=';

	/**
	 * Retrieves an externalized text message value from a message base file
	 * associated with the report design.
	 *
	 * @param sKey The key for which an externalized message is looked up.
	 * @param lcl  The locale for which an externalized message file is retrieved.
	 *
	 * @return An externalized message for the specified key and locale.
	 * @deprecated use {@link #getMessageValue(String, ULocale)} instead.
	 */
	@Deprecated
	String getMessageValue(String sKey, Locale lcl);

	/**
	 * Retrieves an externalized text message value from a message base file
	 * associated with the report design.
	 *
	 * @param sKey The key for which an externalized message is looked up.
	 * @param lcl  The locale for which an externalized message file is retrieved.
	 *
	 * @return An externalized message for the specified key and locale.
	 * @since 2.1
	 */
	String getMessageValue(String sKey, ULocale lcl);
}
