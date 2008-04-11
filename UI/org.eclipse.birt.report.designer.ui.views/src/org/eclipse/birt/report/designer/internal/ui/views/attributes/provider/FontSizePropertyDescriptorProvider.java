
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;

public class FontSizePropertyDescriptorProvider extends
		PropertyDescriptorProvider
{

	public FontSizePropertyDescriptorProvider( String property, String element )
	{
		super( property, element );
	}

	public String getDefaultUnit( )
	{

		String unit = null;
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement( input );
		if ( handle != null )
			unit = handle.getPropertyHandle( getProperty( ) ).getDefaultUnit( );
		return unit;
	}

	public boolean isLocalValue( )
	{
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement( input );
		if ( handle != null )
			return handle.getPropertyHandle( getProperty( ) ).isLocal( );
		return false;
	}

}
