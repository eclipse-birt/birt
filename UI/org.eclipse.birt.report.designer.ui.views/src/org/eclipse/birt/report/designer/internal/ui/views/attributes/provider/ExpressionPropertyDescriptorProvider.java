
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupElementFactory;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;

public class ExpressionPropertyDescriptorProvider extends
		PropertyDescriptorProvider
{

	public ExpressionPropertyDescriptorProvider( String property, String element )
	{
		super( property, element );
		// TODO Auto-generated constructor stub
	}

	public boolean isEnable( )
	{
		List list = DEUtil.getInputElements( input );
		if ( list == null || list.size( ) == 0 )
			return false;
		else
			return true;
	}

	public ExpressionProvider getExpressionProvider( )
	{
		List lst = DEUtil.getInputElements( input );
		if ( lst != null && lst.size( ) == 1 )
		{
			DesignElementHandle elementHandle = (DesignElementHandle) lst.get( 0 );
			return new ExpressionProvider( elementHandle );
		}
		else
			return null;
	}

	public boolean isReadOnly( )
	{
		if ( DEUtil.getInputElements( input ).size( ) > 0 )
		{
			ReportElementHandle handle = (ReportElementHandle) DEUtil.getInputFirstElement( input );
			GroupPropertyHandle propertyHandle = GroupElementFactory.newGroupElement( handle.getModuleHandle( ),
					DEUtil.getInputElements( input ) )
					.getPropertyHandle( getProperty( ) );
			return propertyHandle.isReadOnly( );
		}
		return false;
	}
}
