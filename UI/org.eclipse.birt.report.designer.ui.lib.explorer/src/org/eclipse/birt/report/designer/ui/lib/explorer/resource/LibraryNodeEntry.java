
package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.core.model.views.outline.LibraryNode;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.swt.graphics.Image;

public class LibraryNodeEntry extends ReportResourceEntry
{

	private LibraryNode library;
	private ResourceEntry parent;
	private INodeProvider provider;

	public LibraryNodeEntry( LibraryNode library, ResourceEntry parent )
	{
		this.library = library;
		this.parent = parent;
		this.provider = ProviderFactory.createProvider( library );
	}

	public String getDisplayName( )
	{
		return provider.getNodeDisplayName( library );
	}

	public Image getImage( )
	{
		return provider.getNodeIcon( library );
	}

	public String getName( )
	{
		return provider.getNodeDisplayName( library );
	}

	public ResourceEntry getParent( )
	{
		return parent;
	}

	public boolean equals( Object object )
	{
		if ( object == null )
			return false;
		if ( !( object instanceof LibraryNodeEntry ) )
			return false;
		if ( object == this )
			return true;
		else
		{
			LibraryNodeEntry temp = (LibraryNodeEntry) object;
			if ( temp.library.getReportDesignHandle( ).getID( ) == this.library.getReportDesignHandle( )
					.getID( )
					&& DEUtil.isSameString( temp.library.getReportDesignHandle( )
							.getModule( )
							.getFileName( ),
							this.library.getReportDesignHandle( )
									.getModule( )
									.getFileName( ) ) )
				return true;
		}
		return false;
	}

	public int hashCode( )
	{
		String fileName = this.library.getReportDesignHandle( )
				.getModule( )
				.getFileName( );
		return (int) this.library.getReportDesignHandle( ).getID( )
				* 7
				+ ( fileName == null ? 0 : fileName.hashCode( ) );
	}

	public Object getReportElement( )
	{
		return library;
	}

}
