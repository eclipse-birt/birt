
package org.eclipse.birt.report.model.tests.matrix;

import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.elements.Style;

public class MatrixStyle implements IStyleDeclaration
{

	StyleHandle style = null;

	/**
	 * 
	 * @param name
	 */

	public MatrixStyle( StyleHandle style )
	{
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IStyleDeclaration#getName()
	 */
	public String getName( )
	{
		return style.getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IStyleDeclaration#getProperty(java.lang.String)
	 */
	public Object getProperty( String name )
	{
		FactoryPropertyHandle factoryPropHandle = style.getFactoryPropertyHandle( name );
		if (factoryPropHandle == null)
			return null;
		
		return factoryPropHandle.getValue( );
	}

}
