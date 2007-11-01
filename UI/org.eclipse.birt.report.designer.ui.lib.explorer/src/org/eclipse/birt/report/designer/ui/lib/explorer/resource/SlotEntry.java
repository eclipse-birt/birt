
package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.swt.graphics.Image;

public class SlotEntry extends ReportResourceEntry
{

	private SlotHandle slot;
	private ResourceEntry parent;
	private INodeProvider provider;

	public SlotEntry( SlotHandle slot, ResourceEntry parent )
	{
		this.slot = slot;
		this.parent = parent;
		this.provider = ProviderFactory.createProvider( slot );
	}

	public String getDisplayName( )
	{
		return provider.getNodeDisplayName( slot );
	}

	public Image getImage( )
	{
		return provider.getNodeIcon( slot );
	}

	public String getName( )
	{
		return provider.getNodeDisplayName( slot );
	}

	public ResourceEntry getParent( )
	{
		return parent;
	}

	public boolean equals( Object object )
	{
		if ( object == null )
			return false;
		if ( !( object instanceof SlotEntry ) )
			return false;
		if ( object == this )
			return true;
		else
		{
			SlotEntry temp = (SlotEntry) object;
			if ( temp.slot.getSlotID( ) == this.slot.getSlotID( )
					&& temp.slot.getElement( ).getID( ) == this.slot.getElement( )
							.getID( )
					&& DEUtil.isSameString( temp.slot.getModule( )
							.getFileName( ), this.slot.getModule( )
							.getFileName( ) ) )
				return true;
		}
		return false;
	}

	public int hashCode( )
	{
		String fileName = this.slot.getModule( ).getFileName( );
		return (int) ( slot.getElement( ).getID( ) * 7 + slot.getSlotID( ) )
				* 7
				+ ( fileName == null ? 0 : fileName.hashCode( ) );
	}

	public Object getReportElement( )
	{
		return slot;
	}
}
