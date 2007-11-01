
package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.swt.graphics.Image;

public class StructureEntry extends ReportResourceEntry
{

	private StructureHandle structure;
	private ResourceEntry parent;
	private INodeProvider provider;
	private int index;

	public StructureEntry( StructureHandle structure, ResourceEntry parent,
			int index )
	{
		this.structure = structure;
		this.parent = parent;
		this.provider = createProvider( structure );
		this.index = index;
	}

	protected INodeProvider createProvider( StructureHandle structure )
	{
		if ( structure instanceof EmbeddedImageHandle )
			return ProviderFactory.createProvider( (EmbeddedImageHandle) structure );
		else if ( structure instanceof ResultSetColumnHandle )
			return ProviderFactory.createProvider( (ResultSetColumnHandle) structure );
		return ProviderFactory.createProvider( structure );
	}

	public String getDisplayName( )
	{
		return provider.getNodeDisplayName( structure );
	}

	public Image getImage( )
	{
		return provider.getNodeIcon( structure );
	}

	public String getName( )
	{
		return provider.getNodeDisplayName( structure );
	}

	public ResourceEntry getParent( )
	{
		return parent;
	}

	public boolean equals( Object object )
	{
		if ( object == null )
			return false;
		if ( !( object instanceof StructureEntry ) )
			return false;
		if ( object == this )
			return true;
		else
		{
			StructureEntry temp = (StructureEntry) object;
			if ( temp.index == this.index
					&& temp.structure.getElement( ).getID( ) == this.structure.getElement( )
							.getID( )
					&& DEUtil.isSameString( temp.structure.getModule( )
							.getFileName( ), this.structure.getModule( )
							.getFileName( ) ) )
				return true;
		}
		return false;
	}

	public int hashCode( )
	{
		String fileName = this.structure.getModule( ).getFileName( );
		return (int) ( structure.getElement( ).getID( ) * 7 + index )
				* 7
				+ ( fileName == null ? 0 : fileName.hashCode( ) );
	}

	public Object getReportElement( )
	{
		return structure;
	}
}
