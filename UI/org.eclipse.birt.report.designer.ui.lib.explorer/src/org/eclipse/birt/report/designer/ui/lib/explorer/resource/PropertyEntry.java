
package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.swt.graphics.Image;

public class PropertyEntry extends ReportResourceEntry
{

	private PropertyHandle property;
	private ResourceEntry parent;
	private INodeProvider provider;

	public PropertyEntry( PropertyHandle property, ResourceEntry parent )
	{
		this.property = property;
		this.parent = parent;
		this.provider = ProviderFactory.createProvider( property );
	}

	public String getDisplayName( )
	{
		return provider.getNodeDisplayName( property );
	}

	public Image getImage( )
	{
		return provider.getNodeIcon( property );
	}

	public String getName( )
	{
		return provider.getNodeDisplayName( property );
	}

	public ResourceEntry getParent( )
	{
		return parent;
	}

	public boolean equals( Object object )
	{
		if ( object == null )
			return false;
		if ( !( object instanceof PropertyEntry ) )
			return false;
		if ( object == this )
			return true;
		else
		{
			PropertyEntry temp = (PropertyEntry) object;
			if ( temp.property.getDefn( )
					.getName( )
					.equals( this.property.getDefn( ).getName( ) )
					&& temp.property.getElement( ).getID( ) == this.property.getElement( )
							.getID( )
					&& DEUtil.isSameString( temp.property.getModule( )
							.getFileName( ), this.property.getModule( )
							.getFileName( ) ) )
				return true;
		}
		return false;
	}

	public int hashCode( )
	{
		String fileName = this.property.getModule( ).getFileName( );
		return (int) ( property.getElement( ).getID( ) * 7 + property.getDefn( )
				.getName( )
				.hashCode( ) )
				* 7
				+ ( fileName == null ? 0 : fileName.hashCode( ) );
	}

	public Object getReportElement( )
	{
		return property;
	}
}
