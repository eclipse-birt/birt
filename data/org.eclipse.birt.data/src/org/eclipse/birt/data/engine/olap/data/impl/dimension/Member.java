
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;

/**
 * Describes a level member which is located at a level.
 */

public class Member implements IComparableStructure
{
	private static IStructureCreator creator = null;
	public Object[] keyValues;
	public Object[] attributes;

	public Object[] getFieldValues( )
	{
		Object[][] objects = new Object[2][];
		objects[0] = keyValues;
		objects[1] = attributes;
		return ObjectArrayUtil.convert( objects );
		
	}

	public int compareTo( Object o )
	{
		Member other = (Member) o;
		for ( int i = 0; i < keyValues.length; i++ )
		{
			int result = ( (Comparable) keyValues[i] ).compareTo( other.keyValues[i] );
			if ( result != 0 )
			{
				return result;
			}
		}
		return 0;
	}
	
	public static IStructureCreator getCreator( )
	{
		if ( creator == null )
		{
			creator = new LevelMemberCreator( );
		}
		return creator;
	}
}

class LevelMemberCreator implements IStructureCreator
{

	public IStructure createInstance( Object[] fields )
	{
		Member result = new Member( );
		Object[][] objects = ObjectArrayUtil.convert( fields );
		
		result.keyValues = objects[0];
		result.attributes = objects[1];
		return result;
	}
}