
package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

public class AbstractPageGenerator extends CategoryPageGenerator
{

	protected CTabFolder tabFolder;
	protected List input;
	protected HashMap itemMap = new HashMap( );

	protected void createTabItem( int index, String itemKey )
	{
		if ( tabFolder.getItemCount( ) <= index )
		{
			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
			tabItem.setText( itemKey ); //$NON-NLS-1$
			itemMap.put( tabItem, null );
		}
	}

	protected void setPageInput( Object object )
	{
		if ( object instanceof AttributePage )
		{
			( (AttributePage) object ).setInput( input );
		}
	}

	protected void refresh( Composite parent,Object object,boolean init)
	{
		if ( object instanceof AttributePage )
		{
			if ( init )
			{
				( (AttributePage) object ).buildUI( parent );
				((Composite)( (AttributePage) object ).getControl( )).layout( );
			}
			( (AttributePage) object ).refresh( );
		}
	}
}
