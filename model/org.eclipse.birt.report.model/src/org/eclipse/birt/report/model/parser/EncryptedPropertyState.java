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

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.SimpleEncryptionHelper;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.EncryptionUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses the "encrypted-property" tag. The tag may give the property value of
 * the element or the member of the structure.
 */

public class EncryptedPropertyState extends PropertyState {

	/**
	 * 
	 */
	protected String encryptionID = null;

	/**
	 * 
	 * @param theHandler
	 * @param element
	 */
	EncryptedPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * 
	 * @param theHandler
	 * @param element
	 * @param propDefn
	 * @param struct
	 */
	EncryptedPropertyState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element);

		this.propDefn = propDefn;
		this.struct = struct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		super.parseAttrs(attrs);
		encryptionID = attrs.getValue(DesignSchemaConstants.ENCRYPTION_ID_ATTRIB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */
	public void end() throws SAXException {
		String value = text.toString();

		PropertyDefn propDefn = null;
		if (struct != null) {
			StructureDefn structDefn = (StructureDefn) struct.getDefn();
			assert structDefn != null;

			propDefn = (StructPropertyDefn) structDefn.getMember(name);
		} else {
			propDefn = element.getPropertyDefn(name);
		}

		if (propDefn == null) {
			DesignParserException e = new DesignParserException(new String[] { name },
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY);
			RecoverableError.dealUndefinedProperty(handler, e);

			valid = false;
			return;
		}

		if (!EncryptionUtil.canEncrypt(propDefn)) {
			DesignParserException e = new DesignParserException(new String[] { propDefn.getName() },
					DesignParserException.DESIGN_EXCEPTION_PROPERTY_IS_NOT_ENCRYPTABLE);
			handler.getErrorHandler().semanticError(e);
			valid = false;
			return;
		}

		String valueToSet = StringUtil.trimString(value);
		if (null == valueToSet)
			return;

		// do some backward-compatibility
		if (handler.versionNumber < VersionUtil.VERSION_3_2_15) {
			IEncryptionHelper helper = null;
			String encryption = null;
			if (struct != null)
				helper = SimpleEncryptionHelper.getInstance();
			else {
				encryption = encryptionID == null ? SimpleEncryptionHelper.ENCRYPTION_ID : encryptionID;
				helper = MetaDataDictionary.getInstance().getEncryptionHelper(encryption);
			}
			if (helper != SimpleEncryptionHelper.getInstance()) {
				valueToSet = SimpleEncryptionHelper.getInstance().decrypt(valueToSet);
				valueToSet = helper == null ? valueToSet : helper.encrypt(valueToSet);

				// set encryption id
				if (struct == null)
					element.setEncryptionHelper((ElementPropertyDefn) propDefn, encryption);
			} else {
				if (struct == null)
					element.setEncryptionHelper((ElementPropertyDefn) propDefn, encryption);
			}
		}

		if (struct != null) {
			// do some special for property binding value
			if (struct instanceof PropertyBinding) {
				PropertyBinding propBinding = (PropertyBinding) struct;
				propBinding.setEncryption(encryptionID);
			}
			doSetMember(struct, propDefn.getName(), (StructPropertyDefn) propDefn,
					convertToExpression(propDefn, valueToSet));
			return;
		}

		// set encryption id
		if (encryptionID != null) {
			element.setEncryptionHelper(name, encryptionID);
		}
		doSetProperty(propDefn, convertToExpression(propDefn, valueToSet));
	}

	/**
	 * Converts input value to expression. If the property type is expression and
	 * the exprType is not null, the String value will be converted to Expression.
	 * 
	 * @param defn       property definition.
	 * @param valueToSet the value.
	 * @return the converted object.
	 */
	private Object convertToExpression(PropertyDefn defn, String valueToSet) {
		if (defn.allowExpression()) {
			return new Expression(valueToSet, ExpressionType.CONSTANT);
		}
		return valueToSet;
	}
}
