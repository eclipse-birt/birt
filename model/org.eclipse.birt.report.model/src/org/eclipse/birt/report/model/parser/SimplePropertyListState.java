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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.CompatiblePropertyChangeTables;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses the "property-list" tag. The tag may give the property value of the
 * element.
 */

public class SimplePropertyListState extends AbstractPropertyState {

	/**
	 * The list to store the values.
	 */

	private List values = null;

	/**
	 * The definition of the property of this list property.
	 */

	private PropertyDefn propDefn = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.parser.AbstractPropertyState#
	 * AbstractPropertyState(DesignParserHandler theHandler, DesignElement element,
	 * )
	 */

	SimplePropertyListState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this list property to parse is a member of one
	 * structure.
	 * 
	 * @param theHandler the design parser handler
	 * @param element    the element holding this list property
	 * @param propDefn   the definition of the property which is structure list
	 * @param struct     the structure which holds this list property
	 */

	SimplePropertyListState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element);

		this.propDefn = propDefn;
		this.struct = struct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#generalJumpTo
	 * ()
	 */
	protected AbstractParseState generalJumpTo() {
		if (supportIsEmpty()) {
			AbstractPropertyState state = new EmptyListState(handler, element, struct);
			state.setName(name);
			return state;
		}

		return super.generalJumpTo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#doSetProperty
	 * (org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */

	protected void doSetProperty(PropertyDefn propDefn, Object valueToSet) {
		assert valueToSet != null;

		if (propDefn.getTypeCode() != IPropertyType.LIST_TYPE) {
			DesignParserException e = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_WRONG_SIMPLE_LIST_TYPE);
			handler.getErrorHandler().semanticError(e);
			valid = false;
			return;
		}

		// Validate the value.

		assert valueToSet instanceof List;
		List valueList = new ArrayList();

		try {

			PropertyType type = propDefn.getType();
			Object propValue = type.validateXml(handler.module, element, propDefn, valueToSet);

			valueList = (List) propValue;
		} catch (PropertyValueException ex) {
			// if this element is extensible and value is invalid, then pass
			// this value to the extension to do some work, maybe compatibility,
			// maybe still invalid and fire a warning, maybe something else
			if (element instanceof ExtendedItem && propDefn.isExtended()) {
				valueList = (List) valueToSet;
				List retValue = new ArrayList();
				for (int i = 0; i < valueList.size(); i++) {
					String item = (String) valueList.get(i);
					retValue.add(StringUtil.trimString(item));
				}
				((ExtendedItem) element).getExtensibilityProvider().handleInvalidPropertyValue(propDefn.getName(),
						retValue);
				return;
			}
			ex.setElement(element);
			ex.setPropertyName(propDefn.getName());
			handlePropertyValueException(ex);
			valid = false;
			return;
		}

		if (!valueList.isEmpty())
			element.setProperty(propDefn, valueList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#doSetMember
	 * (org.eclipse.birt.report.model.api.core.IStructure, java.lang.String,
	 * org.eclipse.birt.report.model.metadata.StructPropertyDefn, java.lang.Object)
	 */

	protected void doSetMember(IStructure struct, String propName, StructPropertyDefn memberDefn, Object valueToSet) {
		assert valueToSet != null;

		if (memberDefn.getTypeCode() != IPropertyType.LIST_TYPE) {
			DesignParserException e = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_WRONG_SIMPLE_LIST_TYPE);
			handler.getErrorHandler().semanticError(e);
			valid = false;
			return;
		}

		// Validate the value.

		assert valueToSet instanceof List;
		List valueList = new ArrayList();

		try {
			for (int i = 0; i < ((List) valueToSet).size(); i++) {
				Object item = ((List) valueToSet).get(i);
				PropertyType type = memberDefn.getSubType();
				Object propValue = type.validateXml(handler.getModule(), element, memberDefn, item);
				if (propValue != null)
					valueList.add(i, propValue);

			}
		} catch (PropertyValueException ex) {
			ex.setElement(element);
			ex.setPropertyName(propName);
			handlePropertyValueException(ex);
			valid = false;
			return;
		}

		if (!valueList.isEmpty())
			struct.setProperty(memberDefn, valueList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		if (values == null)
			return;

		if (struct != null) {
			setMember(struct, propDefn.getName(), name, values);
			return;
		}
		setProperty(name, values);

		if (propDefn.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			List propList = (List) element.getProperty(element.getRoot(), name);
			if (propList != null) {
				for (int i = 0; i < propList.size(); i++) {
					Object obj = propList.get(i);
					if (obj instanceof ElementRefValue) {
						ElementRefValue refValue = (ElementRefValue) obj;
						if (refValue.isResolved()) {
							IReferencableElement referred = refValue.getTargetElement();
							referred.addClient(element, name);
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.VALUE_TAG == tagValue)
			return new ValueState();
		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#parseAttrs
	 * (org.xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		super.parseAttrs(attrs);

		if (struct != null)
			propDefn = (PropertyDefn) struct.getDefn().getMember(name);
		else
			propDefn = element.getPropertyDefn(name);
	}

	/**
	 * Convenience class for the inner classes used to parse parts of the Report
	 * tag.
	 */

	class InnerParseState extends AbstractParseState {

		public XMLParserHandler getHandler() {
			return handler;
		}
	}

	class ValueState extends InnerParseState {

		private String exprType;

		private boolean isNull = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end() throws SAXException {
			if (values == null)
				values = new ArrayList();

			exprType = StringUtil.trimString(exprType);

			if (exprType == null) {
				exprType = CompatiblePropertyChangeTables.getDefaultExprType(element.getDefn().getName(), name,
						handler.versionNumber);
			}

			String value = text.toString();
			if (propDefn.allowExpression()) {
				if (isNull) {
					if (exprType == null)
						values.add(null);
					else
						values.add(new Expression(null, exprType));
				} else
					values.add(new Expression(value, exprType));

			} else {
				if (isNull)
					values.add(null);
				else
					values.add(value);
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(
		 * org.xml.sax.Attributes)
		 */

		public void parseAttrs(Attributes attrs) throws XMLParserException {
			super.parseAttrs(attrs);
			exprType = attrs.getValue(DesignSchemaConstants.TYPE_TAG);
			isNull = Boolean.parseBoolean(attrs.getValue(DesignSchemaConstants.IS_NULL_ATTRIB));

		}
	}
}
