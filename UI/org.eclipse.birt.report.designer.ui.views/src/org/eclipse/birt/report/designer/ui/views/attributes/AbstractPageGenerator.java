
package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

/**
 * The abstract page generator implemention providing some helper methods.
 */
public class AbstractPageGenerator extends CategoryPageGenerator
{

	protected HashMap<CTabItem, Object> itemMap = new HashMap<CTabItem, Object>( );

	/**
	 * Creates a new tab and place it as last.
	 * 
	 * @param index
	 * @param itemKey
	 */
	protected void createTabItem( int index, String itemKey )
	{
		if ( tabFolder.getItemCount( ) <= index )
		{
			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
			tabItem.setText( itemKey );
			itemMap.put( tabItem, null );
		}
	}

	/**
	 * Creates a new tab after the given preceding tab.
	 * 
	 * @param itemKey
	 * @param precedingItemKey
	 */
	protected void createTabItem( String itemKey, String precedingItemKey )
	{
		if ( existTabItem( itemKey ) )
			return;
		CTabItem tabItem = new CTabItem( tabFolder,
				SWT.NONE,
				getItemIndex( precedingItemKey ) + 1 );
		tabItem.setText( itemKey );
		itemMap.put( tabItem, null );
	}

	/**
	 * Returns the index of the tab with given key.
	 * 
	 * @param title
	 * @return 0-based index. -1 means no tab found.
	 */
	public int getItemIndex( String itemKey )
	{
		if ( itemKey == null )
			return -1;
		CTabItem[] items = tabFolder.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].getText( ).equals( itemKey ) )
				return i;
		}
		return -1;
	}

	/**
	 * Checks if a tab with given key exists.
	 * 
	 * @param itemKey
	 * @return
	 */
	public boolean existTabItem( String itemKey )
	{
		CTabItem[] items = tabFolder.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].getText( ).equals( itemKey ) )
				return true;
		}
		return false;
	}

	/**
	 * Removes the tab with given key.
	 * 
	 * @param itemKey
	 */
	public void removeTabItem( String itemKey )
	{
		CTabItem[] items = tabFolder.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].getText( ).equals( itemKey ) )
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

	public void dispose( )
	{
		if ( itemMap != null && itemMap.values( ) != null )
		{
			Iterator<Object> iter = itemMap.values( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Object value = iter.next( );
				if ( value instanceof AttributePage )
				{
					( (AttributePage) value ).dispose( );
				}
			}
		}
	}

	@Override
	public void createTabItems( List input )
	{
		super.createTabItems( input );
		AbstractPageGenerator adaptable = new AbstractPageGenerator( );
		IPageGeneratorHelper helper = (IPageGeneratorHelper) ElementAdapterManager
				.getAdapter( adaptable, IPageGeneratorHelper.class );
		if ( helper != null )
		{
			String[] tabKeys = helper.createTabItems( input );
			for ( String tabKey : tabKeys )
			{
				if ( !existTabItem( tabKey ) )
				{
					int index = this.tabFolder.getItemCount( );
					String precedingItemKey = this.tabFolder
							.getItem( index - 1 ).getText( );
					createTabItem( tabKey, precedingItemKey );
					TabPage page = helper.buildTabContent( tabKey );
					CTabItem item = tabFolder.getItem( getItemIndex( tabKey ) );
					setPageInput( page );
					refresh( tabFolder, page, true );
					item.setControl( page.getControl( ) );
					itemMap.put( item, page );
				}
				else
				{
					CTabItem item = tabFolder.getItem( getItemIndex( tabKey ) );
					setPageInput( itemMap.get( item ) );
					refresh( tabFolder, itemMap.get( item ), false );
				}
			}
		}
	}

}
