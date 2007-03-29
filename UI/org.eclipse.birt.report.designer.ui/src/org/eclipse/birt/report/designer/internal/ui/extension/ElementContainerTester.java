
package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.core.expressions.PropertyTester;

public class ElementContainerTester extends PropertyTester
{

	public ElementContainerTester( )
	{
		// TODO Auto-generated constructor stub
	}

	public boolean test( Object receiver, String property, Object[] args,
			Object expectedValue )
	{
		if ( "containerName".equals( property ) )
		{
			if ( receiver instanceof DesignElementHandle )
			{
				DesignElementHandle container = ( (DesignElementHandle) receiver ).getContainer( );
				String containerName = expectedValue.toString( );
				return container.getDefn( )
						.getDisplayName( )
						.equals( containerName );
			}
		}
		return false;
	}
}
