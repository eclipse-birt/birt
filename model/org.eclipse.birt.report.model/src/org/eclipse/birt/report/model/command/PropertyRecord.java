/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.LayoutRecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ExtensionPropertyDefinitionEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.validators.ValidationExecutor;

/**
 * Changes the value of a property. The caller must have previously validated
 * that the property name is valid, that the new value is valid and so on.
 */

public class PropertyRecord extends SimpleRecord
{

	/**
	 * The design element, that is, a report element. All report element classes
	 * derives from DesignElement
	 */

	private DesignElement element = null;

	/**
	 * The property definition. When create the PropertyEvent, the property name
	 * should be stored in it, so the listener can check it to, say, whether to
	 * refresh the UI or not.
	 */

	private ElementPropertyDefn propDefn = null;

	/**
	 * The old value of the property
	 */

	private Object oldValue = null;

	/**
	 * The new value of the property
	 */

	private Object newValue = null;

	/**
	 * Constructor.
	 * 
	 * @param propertyOwner
	 *            the report element that has the property
	 * @param name
	 *            the name of the property to change
	 * @param value
	 *            the new value
	 */

	public PropertyRecord( DesignElement propertyOwner, String name,
			Object value )
	{
		assert propertyOwner != null;
		element = propertyOwner;
		assert name != null;
		propDefn = element.getPropertyDefn( name );
		assert propDefn != null;
		assert !propDefn.isIntrinsic( );
		newValue = value;
		oldValue = propertyOwner.getLocalProperty( null, propDefn );
	}

	/**
	 * Constructor.
	 * 
	 * @param propertyOwner
	 *            the element that has the property to set
	 * @param prop
	 *            the definition of the property to set
	 * @param value
	 *            the new value
	 */

	public PropertyRecord( DesignElement propertyOwner,
			ElementPropertyDefn prop, Object value )
	{
		assert propertyOwner != null;
		propDefn = prop;
		assert propDefn != null;

		// The intrinsic property can also be set with property record. For
		// example,
		// the "unit" property in ReportDesign.

		element = propertyOwner;
		newValue = value;
		oldValue = propertyOwner.getLocalProperty( null, propDefn );

		label = ModelMessages.getMessage(
				MessageConstants.CHANGE_PROPERTY_MESSAGE, new String[]{propDefn
						.getDisplayName( )} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		if ( eventTarget != null )
			return eventTarget.getElement( );

		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		// Use the same notification for the done/redone and undone states.

		if ( element instanceof ExtendedItem )
		{
			IReportItem extElement = ( (ExtendedItem) element )
					.getExtendedElement( );
			// assert extElement != null;

			if ( extElement != null && extElement.refreshPropertyDefinition( ) )
			{
				return new ExtensionPropertyDefinitionEvent( element );
			}
		}

		// if the element works like properties, return property event for the
		// top element.

		if ( eventTarget != null )
		{
			return new PropertyEvent( eventTarget.getElement( ), eventTarget
					.getPropName( ) );
		}

		return new PropertyEvent( element, propDefn.getName( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		Object value = undo ? oldValue : newValue;
		element.setProperty( propDefn, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.IValidatable#getValidators()
	 */
	public List getValidators( )
	{
		return ValidationExecutor.getValidationNodes( element, propDefn
				.getTriggerDefnSet( ), false );
	}

	/**
	 * Returns the definition of the property whose value is changed.
	 * 
	 * @return the property definition
	 */

	public ElementPropertyDefn getPropDefn( )
	{
		return propDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List getPostTasks( )
	{
		List retValue = new ArrayList( );
		retValue.addAll( super.getPostTasks( ) );

		if ( !( element instanceof Cell ) )
			return retValue;

		String propName = propDefn.getName( );

		if ( !ICellModel.COL_SPAN_PROP.equalsIgnoreCase( propName )
				&& !ICellModel.ROW_SPAN_PROP.equalsIgnoreCase( propName )
				&& !ICellModel.COLUMN_PROP.equalsIgnoreCase( propName )
				&& !ICellModel.DROP_PROP.equalsIgnoreCase( propName ) )
			return retValue;

		ReportItem compoundElement = LayoutUtil.getCompoundContainer( element );
		if ( compoundElement == null )
			return retValue;

		retValue.add( new LayoutRecordTask( compoundElement.getRoot( ),
				compoundElement ) );
		return retValue;
	}
}