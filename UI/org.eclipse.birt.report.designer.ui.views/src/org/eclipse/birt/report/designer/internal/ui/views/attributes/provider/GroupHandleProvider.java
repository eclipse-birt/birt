/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.GroupModelProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

/**
 * Group FormHandleProvider, provides Filter sensitive data and processes.
 */
public class GroupHandleProvider extends AbstractFormHandleProvider
{

	/**
	 * The current selections in outline or Editor.
	 */
	protected List contentInput;

	/**
	 * Column properties.
	 */
	private String[] columnKeys = new String[]{
			GroupHandle.GROUP_NAME_PROP, GroupHandle.KEY_EXPR_PROP
	};

	/**
	 * Column widths.
	 */
	private static int[] columnWidth = new int[]{
			250, 250
	};

	/**
	 * Model processor, provide data process of Group model.
	 */
	private GroupModelProvider modelAdapter = new GroupModelProvider( );

	/**
	 * The display name of columns.
	 */
	private String[] columnNames;

	/**
	 * Column editors for the Filter form.
	 */
	private TextCellEditor[] editors;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getColumnNames()
	 */
	public String[] getColumnNames( )
	{
		if ( columnNames == null )
		{
			columnNames = modelAdapter.getColumnNames( columnKeys );
		}
		return columnNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getTitle()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "GroupHandleProvider.Label.Groups" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getEditors(org.eclipse.swt.widgets.Table)
	 */
	public CellEditor[] getEditors( Table table )
	{
		// if ( editors == null )
		// {
		// editors = new TextCellEditor[columnKeys.length];
		// editors[0] = new TextCellEditor( table );
		// editors[1] = new TextCellEditor( table );
		// }
		return new CellEditor[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doMoveItem(int, int)
	 */
	public boolean doMoveItem( int oldPos, int newPos )
			throws SemanticException
	{
		return modelAdapter.moveItem( contentInput.get( 0 ), oldPos, newPos );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doDeleteItem(int)
	 */
	public boolean doDeleteItem( int pos ) throws SemanticException
	{
		return modelAdapter.deleteItem( contentInput.get( 0 ), pos );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doAddItem(int)
	 */
	public boolean doAddItem( int pos )
	{
		return UIUtil.createGroup( (DesignElementHandle) contentInput.get( 0 ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doEditItem(int)
	 */
	public boolean doEditItem( int pos )
	{
		ListingHandle listingHandle = (ListingHandle) contentInput.get( 0 );
		GroupHandle groupHandle = (GroupHandle) listingHandle.getGroups( )
				.get( pos );
		GroupDialog dialog = new GroupDialog( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ), GroupDialog.GROUP_DLG_TITLE_EDIT );
		dialog.setInput( groupHandle );
		// dialog.setDataSetList( DEUtil.getDataSetList( listingHandle ) );
		return dialog.open( ) == Dialog.OK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText( Object element, int columnIndex )
	{
		String key = columnKeys[columnIndex];
		return modelAdapter.getText( element, key );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getImagePath(java.lang.Object, int)
	 */
	public Image getImage( Object element, int columnIndex )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object inputElement )
	{
		if ( inputElement instanceof List )
		{
			contentInput = (List) inputElement;
		}
		else
		{
			contentInput = new ArrayList( );
			contentInput.add( inputElement );
		}

		return modelAdapter.getElements( contentInput );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify( Object element, String property )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue( Object element, String property )
	{
		int index = Arrays.asList( columnNames ).indexOf( property );

		String columnText = getColumnText( element, index );
		return columnText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#modify(java.lang.Object, java.lang.String,
	 * java.lang.Object)
	 */
	public boolean modify( Object data, String property, Object value )
			throws NameException, SemanticException
	{
		int index = Arrays.asList( columnNames ).indexOf( property );
		String key = columnKeys[index];

		String strValue;
		if ( value instanceof Integer )
		{
			int intValue = ( (Integer) value ).intValue( );
			if ( intValue == -1 )
			{
				CCombo combo = (CCombo) editors[index].getControl( );
				strValue = combo.getText( );
			}
			else
			{
				String[] choices = modelAdapter.getChoiceSet( contentInput.get( 0 ),
						columnKeys[index] );
				strValue = choices[intValue];
			}
		}
		else
			strValue = (String) value;
		return modelAdapter.setStringValue( contentInput.get( 0 ),
				data,
				key,
				strValue );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getColumnWidths()
	 */
	public int[] getColumnWidths( )
	{
		return columnWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider
	 * #needRefreshed(org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public boolean needRefreshed( NotificationEvent event )
	{
		if ( event instanceof ContentEvent )
		{
			return true;
		}

		if ( event instanceof PropertyEvent )
		{
			String propertyName = ( (PropertyEvent) event ).getPropertyName( );
			if ( GroupHandle.GROUP_NAME_PROP.equals( propertyName )
					|| GroupHandle.KEY_EXPR_PROP.equals( propertyName ) )
				return true;
		}
		return false;
	}

	public boolean isAddEnable( )
	{
		if ( ( (ReportItemHandle) DEUtil.getInputFirstElement( super.input ) ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			return false;
		else
		{
			if ( DEUtil.isLinkedElement( (ReportItemHandle) DEUtil.getInputFirstElement( super.input ) ) )
				return false;
			return true;
		}
	}

	public boolean isEditEnable( )
	{
		return true;
	}

	public boolean isDeleteEnable( )
	{
		if ( ( (ReportItemHandle) DEUtil.getInputFirstElement( super.input ) ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			return false;
		else
		{
			if ( DEUtil.isLinkedElement( (ReportItemHandle) DEUtil.getInputFirstElement( super.input ) ) )
				return false;
			return true;
		}
	}

	public boolean isUpEnable( )
	{
		if ( ( (ReportItemHandle) DEUtil.getInputFirstElement( super.input ) ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			return false;
		else
		{
			if ( DEUtil.isLinkedElement( (ReportItemHandle) DEUtil.getInputFirstElement( super.input ) ) )
				return false;
			return true;
		}
	}

	public boolean isDownEnable( )
	{
		if ( ( (ReportItemHandle) DEUtil.getInputFirstElement( super.input ) ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			return false;
		else
		{
			if ( DEUtil.isLinkedElement( (ReportItemHandle) DEUtil.getInputFirstElement( super.input ) ) )
				return false;
			return true;
		}
	}
}