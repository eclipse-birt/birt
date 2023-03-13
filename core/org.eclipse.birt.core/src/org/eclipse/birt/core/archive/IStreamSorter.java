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

package org.eclipse.birt.core.archive;

import java.util.ArrayList;

public interface IStreamSorter {
	/**
	 * Sort the streams.
	 *
	 * @param streamNameList - the stream name list to be sorted. All of the entries
	 *                       are strings.
	 * @return the sorted stream name list. All of the entries are strings.
	 */
	ArrayList sortStream(ArrayList streamNameList);
}
