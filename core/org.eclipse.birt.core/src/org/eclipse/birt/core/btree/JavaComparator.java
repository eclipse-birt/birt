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

package org.eclipse.birt.core.btree;

import java.util.Comparator;

public class JavaComparator<K> implements Comparator<K>
{

	public int compare( K o1, K o2 )
	{
		if ( o1 instanceof Comparable )
		{
			return ( (Comparable) o1 ).compareTo( o2 );
		}
		if ( o2 instanceof Comparable )
		{
			return -( (Comparable) o2 ).compareTo( o1 );
		}
		return 0;
	}
}
