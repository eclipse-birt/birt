/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public ArrayList sortStream(ArrayList streamNameList);
}
