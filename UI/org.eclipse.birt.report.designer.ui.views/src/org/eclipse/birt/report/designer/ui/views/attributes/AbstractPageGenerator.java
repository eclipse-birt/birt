
package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

public class AbstractPageGenerator extends CategoryPageGenerator
{

	protected HashMap<CTabItem, Object> itemMap = new HashMap<CTabItem, Object>( );

	protected void createTabItem( int index, String itemKey )
	{
		if ( tabFolder.getItemCount( ) <= index )
		{
			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
			tabItem.setText( itemKey );
			itemMap.put( tabItem, null );
		}
	}

	protected void createTabItem( String itemKey, String getItemIndex )
	{
		if ( existTabItem( itemKey ) )
			return;
		CTabItem tabItem = new CTabItem( tabFolder,
				SWT.NONE,
				getItemIndex( getItemIndex ) + 1 );
		tabItem.setText( itemKey );
		itemMap.put( tabItem, null );
	}

	public int getItemIndex( String title )
	{
		if ( title == null )
			return -1;
		CTabItem[] items = tabFolder.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].getText( ).equals( title ) )
				return i;
		}
		return -1;
	}

	public boolean existTabItem( String title )
	{
		CTabItem[] items = tabFolder.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].getText( ).equals( title ) )
				return true;
		}
		return false;
	}

	public void removeTabItem( String title )
	{
		CTabItem[] items = tabFolder.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].getText( ).equals( title ) )
			{
				itemMap.remove( items[i] );
				items[i].dispose( );
			}
		}
	}

	protected void setPageInput( Object object )
	{
		if ( object instanceof TabPage )
		{
			( (TabPage) object ).setInput( input );
		}
	}

	protected void refresh( Composite parent, Object object, boolean init )
	{
		if ( object instanceof TabPage )
		{
			if ( init )
			{
				( (TabPage) object ).buildUI( parent );
				( (Composite) ( (TabPage) object ).getControl( ) ).layout( );
			}
			( (TabPage) object ).refresh( );
			showPropertiesPage( );
		}
	}

}
