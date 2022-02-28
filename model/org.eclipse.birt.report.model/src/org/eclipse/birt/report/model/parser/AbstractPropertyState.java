/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.DateFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.elements.structures.TimeFormatValue;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.AbstractScalarParameter;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextDataItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ObjectDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.SimpleEncryptionHelper;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * Parses the abstract property. The XML file is like:
 *
 * <pre>
 *
 *              &lt;property-tag name=&quot;propName&quot;&gt;property value&lt;/property-tag&gt;
 * </pre>
 *
 * The supported tags are:
 * <ul>
 * <li>property,
 * <li>expression,
 * <li>xml,
 * <li>method,
 * <li>structure,
 * <li>list-property,
 * <li>text-property,
 * <li>html-property
 * </ul>
 * This class parses the "name" attribute and keeps it. Other attributes are
 * parsed by the inherited classes.
 */

public class AbstractPropertyState extends AbstractParseState {

	/**
	 * The design file parser handler.
	 */

	protected ModuleParserHandler handler = null;

	/**
	 * The element holding this property.
	 */

	protected DesignElement element = null;

	/**
	 * The element property name or structure member name.
	 */

	protected String name = null;

	/**
	 * The hash code for the name member. Used for performance tuning.
	 */

	protected int nameValue = -1;

	/**
	 * The library which the element reference is using.
	 */

	// protected String library = null;
	/**
	 * The structure which holds this property as a member.
	 */

	protected IStructure struct = null;

	/**
	 * Whether the property of this state is defined.
	 */

	protected boolean valid = true;

	/**
	 * Whether the property is empty.
	 */
	protected boolean isEmpty = false;

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this property to parse is a property of one element.
	 *
	 * @param theHandler the design file parser handler
	 * @param element    the element which holds this property
	 */

	public AbstractPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		handler = theHandler;
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		// library = attrs.getValue( DesignSchemaConstants.LIBRARY_ATTRIB );
		//
		name = attrs.getValue(DesignSchemaConstants.NAME_ATTRIB);
		if (StringUtil.isBlank(name)) {
			DesignParserException e = new DesignParserException(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED);
			handler.getErrorHandler().semanticError(e);
			valid = false;
			return;
		}

		nameValue = name.toLowerCase().hashCode();

		isEmpty = Boolean.parseBoolean(attrs.getValue(DesignSchemaConstants.IS_EMPTY_ATTRIB));

		super.parseAttrs(attrs);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	@Override
	public XMLParserHandler getHandler() {
		return handler;
	}

	/**
	 * Sets the member of a structure.
	 *
	 * @param struct   the structure that contains the member to set
	 * @param propName the property in which the structure appears
	 * @param member   the structure member name
	 * @param value    the value parsed from the XML file
	 */

	void setMember(IStructure struct, String propName, String member, Object value) {
		// Ensure that the member is defined.

		StructureDefn structDefn = (StructureDefn) struct.getDefn();
		assert structDefn != null;

		StructPropertyDefn memberDefn = (StructPropertyDefn) structDefn.getMember(member);
		if (memberDefn == null) {
			DesignParserException e = new DesignParserException(new String[] { member },
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY);
			RecoverableError.dealUndefinedProperty(handler, e);

			valid = false;
			return;
		}

		Object valueToSet = value;

		if (memberDefn.isEncryptable()) {
			if (handler.versionNumber < VersionUtil.VERSION_3_2_15) {
				IEncryptionHelper helper = SimpleEncryptionHelper.getInstance();
				if (helper != SimpleEncryptionHelper.getInstance()) {
					valueToSet = SimpleEncryptionHelper.getInstance().decrypt((String) valueToSet);
					valueToSet = helper.encrypt((String) valueToSet);
				}

			}
		}

		doSetMember(struct, propName, memberDefn, valueToSet);
	}

	protected void doSetMember(IStructure struct, String propName, StructPropertyDefn memberDefn, Object valueToSet) {
		// Validate the value.

		try {
			Object propValue = memberDefn.validateXml(handler.getModule(), element, valueToSet);
			struct.setProperty(memberDefn, propValue);
		} catch (PropertyValueException ex) {
			ex.setElement(element);
			ex.setPropertyName(propName + "." + memberDefn.getName()); //$NON-NLS-1$
			handleMemberValueException(ex, memberDefn);
			valid = false;
		}
	}

	/**
	 * Sets the value of a property with a string parsed from the XML file. Performs
	 * any required semantic checks.
	 *
	 * @param propName property name
	 * @param value    value string from the XML file
	 */

	protected void setProperty(String propName, Object value) {
		assert propName != null;

		if (propName.equalsIgnoreCase(IDesignElementModel.NAME_PROP)
				|| propName.equalsIgnoreCase(IDesignElementModel.EXTENDS_PROP)) {
			DesignParserException e = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_INVALID_PROPERTY_SYNTAX);
			handler.getErrorHandler().semanticError(e);
			valid = false;
			return;
		}

		// Avoid overridden properties that may cause structure change.

		if (element.isVirtualElement()) {
			if (element instanceof Cell) {
				if (ICellModel.COL_SPAN_PROP.equalsIgnoreCase(propName)
						|| ICellModel.ROW_SPAN_PROP.equalsIgnoreCase(propName)
						|| ICellModel.DROP_PROP.equalsIgnoreCase(propName)
						|| ICellModel.COLUMN_PROP.equalsIgnoreCase(propName)) {
					PropertyValueException e = new PropertyValueException(element, propName, value,
							PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN);

					handler.getErrorHandler().semanticWarning(e);
					valid = false;
					return;
				}
			}
		}

		// The property definition is not found, including user
		// properties.

		ElementPropertyDefn propDefn = element.getPropertyDefn(propName);
		if (propDefn == null) {
			// if element is extensible, then pass this value to the extension
			if (element instanceof ExtendedItem) {
				((ExtendedItem) element).getExtensibilityProvider().handleUndefinedProperty(propName, value);
				return;
			}
			DesignParserException e = new DesignParserException(new String[] { propName },
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY);
			RecoverableError.dealUndefinedProperty(handler, e);
			valid = false;
			return;
		}

		Object valueToSet = value;

		if (propDefn.isEncryptable()) {
			if (handler.versionNumber < VersionUtil.VERSION_3_2_15) {
				IEncryptionHelper helper = SimpleEncryptionHelper.getInstance();
				if (handler.versionNumber == VersionUtil.VERSION_0) {
					valueToSet = helper.encrypt((String) valueToSet);
				} else if (helper != SimpleEncryptionHelper.getInstance()) {
					valueToSet = SimpleEncryptionHelper.getInstance().decrypt((String) valueToSet);
					valueToSet = helper == null ? valueToSet : helper.encrypt((String) valueToSet);
				}
				element.setEncryptionHelper(propDefn, SimpleEncryptionHelper.ENCRYPTION_ID);

			}
		}
		doSetProperty(propDefn, valueToSet);
	}

	protected void doSetProperty(PropertyDefn propDefn, Object valueToSet) {
		// Validate the value.

		try {
			Object propValue = propDefn.validateXml(handler.getModule(), element, valueToSet);
			element.setProperty(propDefn, propValue);
		} catch (PropertyValueException ex) {
			// if this property is extensible and value is invalid, then pass
			// this value to the extension to do some work, maybe compatibility,
			// maybe still invalid and fire a warning, maybe something else
			if (element instanceof ExtendedItem && propDefn.isExtended()) {
				((ExtendedItem) element).getExtensibilityProvider().handleInvalidPropertyValue(propDefn.getName(),
						valueToSet);
				return;
			}
			ex.setElement(element);
			ex.setPropertyName(propDefn.getName());
			handlePropertyValueException(ex);
			valid = false;
		}
	}

	/**
	 * Process the property value exception if the value of a property is invalid.
	 *
	 * @param e the property value exception
	 */

	protected void handlePropertyValueException(PropertyValueException e) {
		String propName = e.getPropertyName();

		if (isRecoverableError(e.getInvalidValue(), e.getErrorCode(), e.getElement().getPropertyDefn(propName))) {
			RecoverableError.dealInvalidPropertyValue(handler, e);
		} else {
			handler.getErrorHandler().semanticError(e);
		}
	}

	/**
	 * Process the property value exception if the value of a member is invalid.
	 *
	 * @param e          the property value exception
	 * @param memberDefn the member definition
	 */

	private void handleMemberValueException(PropertyValueException e, StructPropertyDefn memberDefn) {
		if (isRecoverableError(e.getInvalidValue(), e.getErrorCode(), memberDefn)) {
			RecoverableError.dealInvalidMemberValue(handler, e, struct, memberDefn);
		} else {
			handler.getErrorHandler().semanticError(e);
		}
	}

	/**
	 * Checks whether the given exception is an error that the parser can recover.
	 *
	 * @param invalidValue
	 * @param errorCode    the error code of the property value exception
	 * @param propDefn     the definition of the exception. Can be an element
	 *                     property definition or a member definition.
	 * @return return <code>true</code> if it is a recoverable error, otherwise
	 *         <code>false</code>.
	 */

	private boolean isRecoverableError(Object invalidValue, String errorCode, IPropertyDefn propDefn) {

		if (PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE.equalsIgnoreCase(errorCode)
				|| PropertyValueException.DESIGN_EXCEPTION_NON_POSITIVE_VALUE.equalsIgnoreCase(errorCode)
				|| PropertyValueException.DESIGN_EXCEPTION_UNIT_NOT_ALLOWED.equalsIgnoreCase(errorCode)
				|| PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED.equalsIgnoreCase(errorCode)) {
			return true;
		}

		if (PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE.equals(errorCode) && (this.struct != null
				&& struct instanceof HideRule && HideRule.FORMAT_MEMBER.equals(propDefn.getName()))) {
			return true;
		}

		// choice 'any' is removed from column-data-type since 3.2.17(design
		// file version)/2.3.2(birt release version)
		if (PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND.equalsIgnoreCase(errorCode)) {
			IChoiceSet columnDataTypeSet = propDefn.getChoices();
			if (columnDataTypeSet != null
					&& DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE.equalsIgnoreCase(columnDataTypeSet.getName())) {
				if (DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals(invalidValue)) {
					return true;
				}
			}
		}

		if (PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE.equalsIgnoreCase(errorCode)
				|| PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND.equalsIgnoreCase(errorCode)) {
			if ((element instanceof TextDataItem
					&& ITextDataItemModel.CONTENT_TYPE_PROP.equalsIgnoreCase(propDefn.getName())) || (element instanceof AbstractScalarParameter
					&& IAbstractScalarParameterModel.DATA_TYPE_PROP.equalsIgnoreCase(propDefn.getName()))) {
				return true;
			}

			if (IStyleModel.PAGE_BREAK_AFTER_PROP.equalsIgnoreCase(propDefn.getName())
					|| IStyleModel.PAGE_BREAK_BEFORE_PROP.equalsIgnoreCase(propDefn.getName())
					|| IStyleModel.PAGE_BREAK_INSIDE_PROP.equalsIgnoreCase(propDefn.getName())) {
				return true;
			}

			ObjectDefn objDefn = ((PropertyDefn) propDefn).definedBy();
			if (objDefn instanceof StructureDefn) {
				// DateTimeFormatValue.CATEGORY_MEMBER
				// NumberFormatValue.CATEGORY_MEMBER
				// StringFormatValue.CATEGORY_MEMBER
				// DateFormatValue.CATEGORY_MEMBER
				// TimeFormatValue.CATEGORY_MEMBER

				String structureName = objDefn.getName();
				if (DateTimeFormatValue.FORMAT_VALUE_STRUCT.equals(structureName)
						|| NumberFormatValue.FORMAT_VALUE_STRUCT.equals(structureName)
						|| StringFormatValue.FORMAT_VALUE_STRUCT.equals(structureName)
						|| TimeFormatValue.FORMAT_VALUE_STRUCT.equals(structureName)
						|| DateFormatValue.FORMAT_VALUE_STRUCT.equals(structureName)) {
					if (FormatValue.CATEGORY_MEMBER.equalsIgnoreCase(propDefn.getName())) {
						return true;
					}
				}

				if ((DataSetParameter.STRUCT_NAME.equalsIgnoreCase(structureName)
						&& DataSetParameter.DATA_TYPE_MEMBER.equalsIgnoreCase(propDefn.getName()))
						|| (OdaDataSetParameter.STRUCT_NAME.equalsIgnoreCase(structureName)
								&& OdaDataSetParameter.DATA_TYPE_MEMBER.equalsIgnoreCase(propDefn.getName()))) {
					return true;
				}

				// MapRule.OPERATOR_MEMBER
				// HighlightRule.OPERATOR_MEMBER

				if (MapRule.STRUCTURE_NAME.equals(objDefn.getName())
						|| HighlightRule.STRUCTURE_NAME.equals(objDefn.getName())) {
					if (StyleRule.OPERATOR_MEMBER.equals(propDefn.getName())
							|| StyleRule.OPERATOR_MEMBER.equals(propDefn.getName())) {
						return "any".equalsIgnoreCase(invalidValue.toString()); //$NON-NLS-1$
					}
				}
			}
		}

		return false;
	}

	/**
	 * Sets the value of the attribute "name". This method is used when the specific
	 * state is defined. When the generic state jumps to the specific one, the
	 * <code>parseAttrs</code> will not be called. So the value of the attribute
	 * "name" should be set by the generic state.
	 *
	 * @param name the name to set
	 */

	protected void setName(String name) {
		this.name = name;

		if (this.name != null) {
			nameValue = this.name.toLowerCase().hashCode();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#jumpTo()
	 */

	@Override
	public final AbstractParseState jumpTo() {
		// If this state can not be parsed properly, any states in it are
		// ignored.

		if (!valid) {
			return new AnyElementState(handler);
		}

		AbstractParseState state;

		// general jump to
		state = generalJumpTo();
		if (state != null) {
			return state;
		}

		// version conditional jump to
		if (!handler.isCurrentVersion) {
			state = versionConditionalJumpTo();
			if (state != null) {
				return state;
			}
		}

		// super jump to
		return super.jumpTo();
	}

	/**
	 * Jumps to the specified state that the current state needs to go.
	 *
	 * @return the other state.
	 */

	protected AbstractParseState versionConditionalJumpTo() {
		return null;
	}

	/**
	 * Jumps to the specified state that the current state needs to go when some
	 * version controlled condition is satisfied.
	 *
	 * @return the other state.
	 */

	protected AbstractParseState generalJumpTo() {
		return null;
	}

	/**
	 * De-escapes characters in the CDATA. Two characters are needed to convert:
	 *
	 * <ul>
	 * <li>&amp; to &
	 * <li>]]&gt; to ]]>
	 * </ul>
	 *
	 * @param value
	 * @return the escaped string
	 */
	protected String deEscape(String value) {
		if (value == null) {
			return null;
		}

		String retValue = value.replaceAll("]]&gt;", "]]>"); //$NON-NLS-1$ //$NON-NLS-2$
		retValue = retValue.replace("&amp;", "&"); //$NON-NLS-1$ //$NON-NLS-2$

		return retValue;
	}

	/**
	 * Checks if the list property is element property and can be inherited or it is
	 * a style property.
	 *
	 * @return <true> if the list property is element property and can be inherited
	 *         or it is a style property, else return <false>.
	 */
	protected boolean supportIsEmpty() {
		if (isEmpty && struct == null) {
			ElementPropertyDefn defn = element.getPropertyDefn(name);
			if (defn != null && ModelUtil.canInherit(defn)) {
				return true;
			}
		}
		return false;
	}
}
