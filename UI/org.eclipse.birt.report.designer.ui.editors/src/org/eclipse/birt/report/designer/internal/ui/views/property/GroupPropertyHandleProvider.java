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

package org.eclipse.birt.report.designer.internal.ui.views.property;

import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * This handler is used to access the underlying IR model properties for the
 * property sheet page.
 */

public class GroupPropertyHandleProvider {

	private static GroupPropertyHandleProvider instance;

	/**
	 * Return a single instance.
	 * 
	 * @return instance of this
	 */
	public static GroupPropertyHandleProvider getInstance() {
		if (instance == null) {
			instance = new GroupPropertyHandleProvider();
		}
		return instance;
	}

	//
	// /**
	// * @param input
	// * @return the measure of demension.
	// */
	// public Double getMeasureFormDimension( Object input )
	// {
	// Double measure = null;
	// if ( input instanceof DimensionValue )
	// {
	// measure = new Double( ( (DimensionValue) input ).getMeasure( ) );
	// }
	// else if ( input instanceof String )
	// {
	// try
	// {
	// measure = new Double( DimensionValue.parse( (String) input )
	// .getMeasure( ) );
	// }
	// catch ( PropertyValueException e )
	// {
	// }
	// }
	// return measure;
	// }
	//
	// /**
	// * @param input
	// * @return unit of dimension.
	// */
	// public String getUnitFormDimension( Object input )
	// {
	// String unit = ""; //$NON-NLS-1$
	// if ( input instanceof DimensionValue )
	// {
	// unit = ( (DimensionValue) input ).getUnits( );
	// }
	// else if ( input instanceof String )
	// {
	// try
	// {
	// unit = DimensionValue.parse( (String) input ).getUnits( );
	// }
	// catch ( PropertyValueException e )
	// {
	// }
	// }
	// return unit;
	// }
	//
	// /**
	// * Get the value's position in the choice list it belongs to.
	// *
	// * @param o
	// * the property model to inspect
	// * @param value
	// * the specified value
	// * @return the position
	// */
	// private Object getIndexByValue( Object o, Object value )
	// {
	// int index = 0;
	//
	// Object values[] = getChoiceValues( o );
	//
	// for ( int i = 0; values != null && i < values.length; i++ )
	// {
	// if ( values[i].equals( value ) )
	// {
	// index = i;
	// }
	// }
	// return new Integer( index );
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// org.eclipse.birt.modeler.facade.IPropertyHandle#setPropertyValue(java.lang.Object)
	// */
	// public void setPropertyValue( Object id, Object value )
	// {
	// if ( isBooleanProperty( id ) )
	// {
	// int selection = ( (Integer) value ).intValue( );
	//
	// value = new Boolean( selection == 0 ? false : true );
	//
	// }
	// else if ( isColorProperty( id ) )
	// {
	// if ( value instanceof RGB )
	// {
	// RGB rgb = (RGB) value;
	// value = new Integer( ColorUtil.formRGB( rgb.red, rgb.green,
	// rgb.blue ) );
	// }
	// }
	// else if ( isFontSizeProperty( id ) )
	// {
	//
	// }
	// else if ( isChoiceProperty( id ) )
	// {
	// value = getChoiceValue( id, ( (Integer) value ).intValue( ) );
	// }
	//
	// try
	// {
	// if ( id instanceof SimpleValueHandle )
	// {
	// ( (SimpleValueHandle) id ).setValue( value );
	// }
	// }
	// catch ( SemanticException e )
	// {
	// ExceptionHandler.handle( e );
	// }
	// }

	// /**
	// * Gets the value at the specified index of the choice list.
	// *
	// * @param handle
	// * the property model to inspect
	// * @param index
	// * index of value to return
	// * @return the value at the specified index in the list
	// */
	// public Object getChoiceValue( Object handle, int index )
	// {
	// Object[] values = getChoiceValues( handle );
	// return values[index];
	// }

	/**
	 * Gets the set of choices for the property; return null if the property doesn't
	 * have choice list.
	 * 
	 * @param o the property model to inspect
	 * @return the array holds choice values
	 */
	private Object[] getChoiceValues(Object o) {
		Object[] values = null;
		if (o instanceof GroupPropertyHandle) {
			if (((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices() != null) {
				IChoice[] choices = ((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices().getChoices();
				if (choices.length > 0) {
					values = new Object[choices.length];
					for (int i = 0; i < choices.length; i++) {
						values[i] = choices[i].getName();
					}
				}
			}
		}

		return values;
	}

	/**
	 * Determines if the property has choice type value.
	 * 
	 * @param o the property model to inspect
	 * @return if a property has choice type value
	 */
	public boolean isChoiceProperty(Object o) {
		boolean choice = false;
		if (isColorProperty(o)) {
			return false;
		}
		if (o instanceof GroupPropertyHandle) {
			if (((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices() != null) {
				IChoice[] choices = ((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices().getChoices();
				if (choices.length > 0) {
					choice = true;
				}
			}
		}
		return choice;
	}

	/**
	 * Determines whether this is a style property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a style property
	 */
	public boolean isStyleProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().isStyleProperty();
		}
		return false;
	}

	/**
	 * Determines whether this is a color type property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a color type property
	 */
	public boolean isColorProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().getTypeCode() == IPropertyType.COLOR_TYPE;
		}
		return false;
	}

	/**
	 * Determines whether this is a dimension property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a dimension property
	 */
	public boolean isFontSizeProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {
			if (!((GroupPropertyHandle) o).getPropertyDefn().hasChoices()) {
				return false;
			}
			return ((GroupPropertyHandle) o).getPropertyDefn().getTypeCode() == IPropertyType.DIMENSION_TYPE;
		}
		return false;
	}

	/**
	 * Determines whether this is a dimension property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a dimension property
	 */
	public boolean isDimensionProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {

			if (isFontSizeProperty(o))
				return false;
			return ((GroupPropertyHandle) o).getPropertyDefn().getTypeCode() == IPropertyType.DIMENSION_TYPE;
		}
		return false;
	}

	/**
	 * Determines whether this is a boolean type property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a boolean type property
	 */
	public boolean isBooleanProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().getTypeCode() == IPropertyType.BOOLEAN_TYPE;
		}
		return false;
	}

	/**
	 * Determines whether this is a date-time type property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a date-time type property
	 */
	public boolean isDateTimeProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().getTypeCode() == IPropertyType.DATE_TIME_TYPE;
		}
		return false;
	}

	/**
	 * Determines whether this is an expression type property.
	 * 
	 * @param o the property model to inspect
	 * @return true if an expression property
	 */
	public boolean isExpressionProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().getTypeCode() == IPropertyType.EXPRESSION_TYPE;
		}

		return false;
	}

	/**
	 * Determines whether this is a custom style property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a custom style property
	 */
	public boolean isElementRefValue(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().getTypeCode() == IPropertyType.ELEMENT_REF_TYPE;
		}

		return false;
	}

	/**
	 * Determines whether this is a visible property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a visible property
	 */
	public boolean isReadOnly(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).isReadOnly();
		}

		return false;
	}

	/**
	 * Determines whether this is a visible property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a visible property
	 */
	public boolean isEditable(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().isEditable();
		}

		return false;
	}

	/**
	 * Get the value's position in the choice list it belongs to.
	 * 
	 * @param o     the property model to inspect
	 * @param value the specified value
	 * @return the position
	 */
	public Object getIndexByValue(Object o, Object value) {
		int index = 0;

		Object values[] = getChoiceValues(o);

		for (int i = 0; values != null && i < values.length; i++) {
			if (values[i].equals(value)) {
				index = i;
			}
		}
		return Integer.valueOf(index);
	}

	/**
	 * Determines whether this is a password property.
	 * 
	 * @param o the property model to inspect
	 * @return true if a password property
	 */
	public boolean isPassProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().isEncryptable();
		}

		return false;
	}

	public boolean isBackgroundImageProperty(Object o) {
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().getName().equals(IStyleModel.BACKGROUND_IMAGE_PROP);
		}
		return false;
	}
}