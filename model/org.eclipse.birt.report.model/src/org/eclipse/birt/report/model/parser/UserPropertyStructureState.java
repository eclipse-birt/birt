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

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.UserChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses one user-defined property and adds the property definition into the
 * element, if the element allows user-defined properties and the definition is
 * valid.
 */

public class UserPropertyStructureState extends StructureState {

	/**
	 * Constructs the state of the structure which is user property.
	 * 
	 * @param theHandler the design parser handler
	 * @param element    the element holding this user property
	 * @param theList    the list of user properties
	 */

	UserPropertyStructureState(ModuleParserHandler theHandler, DesignElement element, ArrayList theList) {
		super(theHandler, element);

		this.propDefn = new UserPropertyDefn();
		this.struct = (IStructure) propDefn;
		this.list = theList;

		this.name = DesignElement.USER_PROPERTIES_PROP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();

		if (ParserSchemaConstants.PROPERTY_TAG == tagValue)
			return new UserPropertyState(handler, element, propDefn, struct);
		if (ParserSchemaConstants.LIST_PROPERTY_TAG == tagValue)
			return new ChoiceStructureListState(handler, element, propDefn, struct);
		if (ParserSchemaConstants.TEXT_PROPERTY_TAG == tagValue)
			return new TextPropertyState(handler, element, struct);
		if (ParserSchemaConstants.EXPRESSION_TAG == tagValue)
			return new UserExpressionState(handler, element, propDefn, struct);

		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.StructureState#parseAttrs(org.xml
	 * .sax.Attributes)
	 */
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		lineNumber = handler.getCurrentLineNo();

		if (struct == null) {
			assert propDefn != null;

			// If the structure has its specific state, the structure will be
			// created by the specific state.

			struct = createStructure((StructureDefn) propDefn.getStructDefn());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		try {
			((UserPropertyDefn) struct).checkUserPropertyDefn(handler.getModule(), element);
			element.addUserPropertyDefn((UserPropertyDefn) struct);
		} catch (UserPropertyException e) {
			handler.getErrorHandler().semanticError(e);
		} catch (MetaDataException e) {
			handler.getErrorHandler()
					.semanticError(new UserPropertyException(element, ((UserPropertyDefn) struct).getName(),
							UserPropertyException.DESIGN_EXCEPTION_INVALID_DEFINITION, e));
		}
	}

	/**
	 * Convenience class for the inner classes used to parse parts of the
	 * ReportElement tag.
	 */

	class InnerParseState extends AbstractParseState {

		public XMLParserHandler getHandler() {
			return handler;
		}
	}

	/**
	 * Parses the value choices of use-defined properties, which will read the
	 * choice and store it as <code>UserChoice</code>.
	 */

	class ChoiceStructureListState extends ListPropertyState {

		/**
		 * Constructs the choice structure list state with the parser handler, element
		 * that holds the user property definition, property definition of user
		 * properties and the current handled user property definition.
		 * 
		 * @param theHandler
		 * @param element
		 * @param propDefn
		 * @param struct
		 */

		ChoiceStructureListState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
				IStructure struct) {
			super(theHandler, element, propDefn, struct);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(
		 * org.xml.sax.Attributes)
		 */

		public void parseAttrs(Attributes attrs) throws XMLParserException {
			String name = attrs.getValue(DesignSchemaConstants.NAME_ATTRIB);
			if (StringUtil.isBlank(name)) {
				handler.getErrorHandler()
						.semanticError(new DesignParserException(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				valid = false;
				return;
			}

			if (!UserPropertyDefn.CHOICES_MEMBER.equalsIgnoreCase(name)) {
				DesignParserException e = new DesignParserException(new String[] { name },
						DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY);
				RecoverableError.dealUndefinedProperty(handler, e);
				valid = false;
				return;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		public AbstractParseState startElement(String tagName) {
			int tagValue = tagName.toLowerCase().hashCode();
			if (ParserSchemaConstants.STRUCTURE_TAG == tagValue)
				return new ChoiceStructureState(list);

			return super.startElement(tagName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end() throws SAXException {
			UserChoice[] choiceArray = new UserChoice[list.size()];
			list.toArray(choiceArray);

			((UserPropertyDefn) struct).setChoices(choiceArray);
		}
	}

	/**
	 * Parses one user-defined choice of the user-defined property.
	 */

	class ChoiceStructureState extends InnerParseState {

		/**
		 * Choice list.
		 */

		ArrayList choices = null;

		/**
		 * User choice to handle.
		 */

		UserChoice choice = new UserChoice(null, null);

		/**
		 * Constructor.
		 * 
		 * @param theChoices
		 */

		ChoiceStructureState(ArrayList theChoices) {
			this.choices = theChoices;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		public AbstractParseState startElement(String tagName) {
			int tagValue = tagName.toLowerCase().hashCode();
			if (ParserSchemaConstants.PROPERTY_TAG == tagValue)
				return new ChoicePropertyState(choice);

			if (ParserSchemaConstants.TEXT_PROPERTY_TAG == tagValue)
				return new ChoiceTextPropertyState(choice);

			return super.startElement(tagName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end() throws SAXException {
			choices.add(choice);
		}

	}

	/**
	 * Parses the member of the user-defined choice( <code>UserChoice</code>), that
	 * is name, display name key, display name.
	 */

	class ChoicePropertyState extends InnerParseState {

		UserChoice choice = null;
		String choiceName = null;

		ChoicePropertyState(UserChoice theChoice) {
			this.choice = theChoice;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(
		 * org.xml.sax.Attributes)
		 */

		public void parseAttrs(Attributes attrs) throws XMLParserException {
			choiceName = attrs.getValue(DesignSchemaConstants.NAME_ATTRIB);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end() throws SAXException {
			String value = text.toString();

			if (Choice.NAME_PROP.equalsIgnoreCase(choiceName))
				choice.setName(value);
			else if (UserChoice.VALUE_PROP.equalsIgnoreCase(choiceName)) {
				UserPropertyDefn propDefn = (UserPropertyDefn) struct;
				Object objValue = value;

				if (propDefn.getTypeCode() != IPropertyType.CHOICE_TYPE) {
					try {
						objValue = propDefn.validateValue(handler.getModule(), element, value);
					} catch (PropertyValueException e) {
						handler.getErrorHandler().semanticError(new UserPropertyException(element, name,
								UserPropertyException.DESIGN_EXCEPTION_INVALID_CHOICE_VALUE));
						return;
					}
				}
				choice.setValue(objValue);
			} else
				assert false;
		}
	}

	/**
	 * Parses the member of the user-defined choice( <code>UserChoice</code>), that
	 * is name, display name key, display name.
	 */

	class ChoiceTextPropertyState extends InnerParseState {

		UserChoice choice = null;
		String displayNamePropName = null;
		String resourceKeyValue = null;

		ChoiceTextPropertyState(UserChoice theChoice) {
			this.choice = theChoice;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(
		 * org.xml.sax.Attributes)
		 */

		public void parseAttrs(Attributes attrs) throws XMLParserException {
			displayNamePropName = attrs.getValue(DesignSchemaConstants.NAME_ATTRIB);
			resourceKeyValue = attrs.getValue(DesignSchemaConstants.KEY_ATTRIB);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end() throws SAXException {
			String value = text.toString();

			if (UserChoice.DISPLAY_NAME_PROP.equalsIgnoreCase(displayNamePropName)) {
				choice.setDisplayName(value);
				choice.setDisplayNameKey(resourceKeyValue);
			}
		}

	}

	/**
	 * Parses the one property of user-define property definition.
	 */

	static class UserPropertyState extends PropertyState {

		UserPropertyState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
				IStructure struct) {
			super(theHandler, element, propDefn, struct);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end() throws SAXException {
			String value = text.toString();

			// if the property tag is to set the type of a user-defined
			// property, then we implement it like this.

			if (UserPropertyDefn.TYPE_MEMBER.equalsIgnoreCase(name)) {
				MetaDataDictionary dictionary = MetaDataDictionary.getInstance();
				if (StringUtil.isBlank(value)) {
					value = IPropertyType.STRING_TYPE_NAME;
				}

				PropertyType typeDefn = dictionary.getPropertyType(value);
				if (typeDefn == null) {
					handler.getErrorHandler()
							.semanticError(new UserPropertyException(element, ((UserPropertyDefn) struct).getName(),
									UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE));
					return;
				}

				((UserPropertyDefn) struct).setType(typeDefn);
			} else if (UserPropertyDefn.DEFAULT_MEMBER.equalsIgnoreCase(name)) {
				try {
					Object defaultValue = ((UserPropertyDefn) struct).validateValue(handler.getModule(), element,
							value);

					((UserPropertyDefn) struct).setDefault(defaultValue);

				} catch (PropertyValueException e) {
					handler.getErrorHandler()
							.semanticError(new UserPropertyException(element, name,
									UserPropertyException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE, e,
									new String[] { value, ((UserPropertyDefn) struct).getType().getName() }));
				}

			} else {
				super.end();
			}
		}
	}

	static class UserExpressionState extends ExpressionState {

		UserExpressionState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
				IStructure struct) {
			super(theHandler, element, propDefn, struct);
		}

		protected void doEnd(Object value) {
			if (UserPropertyDefn.DEFAULT_MEMBER.equals(name)) {
				if (((UserPropertyDefn) struct).allowExpression()) {
					Expression expr = new Expression(value, exprType);
					((UserPropertyDefn) struct).setDefault(expr);
				} else {
					handler.getErrorHandler()
							.semanticError(new UserPropertyException(element, ((UserPropertyDefn) struct).getName(),
									UserPropertyException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE));
				}
				return;
			}
			super.doEnd(value);
		}
	}
}
