
package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.swt.graphics.Image;

public class EmbeddedImagesEntry extends ReportResourceEntry
{

	private EmbeddedImageNode image;
	private ResourceEntry parent;
	private INodeProvider provider;

	public EmbeddedImagesEntry( EmbeddedImageNode image, ResourceEntry parent )
	{
		this.image = image;
		this.parent = parent;
		this.provider = ProviderFactory.createProvider( image );
	}

	public String getDisplayName( )
	{
		return provider.getNodeDisplayName( image );
	}

	public Image getImage( )
	{
		return provider.getNodeIcon( image );
	}

	public String getName( )
	{
		return provider.getNodeDisplayName( image );
	}

	public ResourceEntry getParent( )
	{
		return parent;
	}

	public boolean equals( Object object )
	{
		if ( object == null )
			return false;
		if ( !( object instanceof EmbeddedImagesEntry ) )
			return false;
		if ( object == this )
			return true;
		else
		{
			EmbeddedImagesEntry temp = (EmbeddedImagesEntry) object;
			if ( temp.image.getReportDesignHandle( ).getID( ) == this.image.getReportDesignHandle( )
					.getID( )
					&& DEUtil.isSameString( temp.image.getReportDesignHandle( )
							.getModule( )
							.getFileName( ), this.image.getReportDesignHandle( )
							.getModule( )
							.getFileName( ) ) )
				return true;
		}
		return false;
	}

	public int hashCode( )
	{
		String fileName = this.image.getReportDesignHandle( )
				.getModule( )
				.getFileName( );
		return (int) this.image.getReportDesignHandle( ).getID( )
				* 7
				+ ( fileName == null ? 0 : fileName.hashCode( ) );
	}

	public Object getReportElement( )
	{
		return image;
	}

}
