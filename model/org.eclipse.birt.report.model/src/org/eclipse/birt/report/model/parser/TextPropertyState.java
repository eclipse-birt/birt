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

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;
import org.eclipse.birt.report.model.metadata.ExtensionPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * Constructs the state to parse resource key property.
 */

public class TextPropertyState extends AbstractPropertyState {

	PropertyDefn propDefn = null;
	PropertyDefn keyPropDefn = null;
	String keyValue = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#
	 * AbstractPropertyState(DesignParserHandler theHandler, DesignElement element,
	 * )
	 */

	TextPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#
	 * AbstractPropertyState(DesignParserHandler theHandler, DesignElement element,
	 * String propName, IStructure struct)
	 */

	TextPropertyState(ModuleParserHandler theHandler, DesignElement element, IStructure struct) {
		super(theHandler, element);

		this.struct = struct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.
	 * Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		name = attrs.getValue(DesignSchemaConstants.NAME_ATTRIB);
		if (StringUtil.isBlank(name)) {
			handler.getErrorHandler()
					.semanticError(new DesignParserException(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
			valid = false;
			return;
		}

		nameValue = name.toLowerCase().hashCode();
		String keyName = name + IDesignElementModel.ID_SUFFIX;

		if (struct != null) {
			propDefn = (PropertyDefn) struct.getDefn().getMember(name);
			keyPropDefn = (PropertyDefn) struct.getDefn().getMember(keyName);
		} else {
			propDefn = element.getPropertyDefn(name);
			keyPropDefn = element.getPropertyDefn(keyName);
		}
		if (propDefn == null) {
			DesignParserException e = new DesignParserException(new String[] { name },
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY);
			RecoverableError.dealUndefinedProperty(handler, e);
			valid = false;
			return;
		}
		if (keyPropDefn == null) {
			DesignParserException e = new DesignParserException(new String[] { keyName },
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY);
			RecoverableError.dealUndefinedProperty(handler, e);
			valid = false;
			return;
		}

		String keyValue = attrs.getValue(DesignSchemaConstants.KEY_ATTRIB);
		if (keyValue == null)
			return;

		if (struct != null)
			setMember(struct, propDefn.getName(), keyName, keyValue);
		else
			setProperty(keyName, keyValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end() {
		String value = text.toString();

		if (struct != null)
			setMember(struct, propDefn.getName(), name, value);
		else {
			// backward compatible
			if (IReportItemModel.ALTTEXT_PROP.equals(name))
				setProperty(name, new Expression(value, ExpressionType.CONSTANT));
			else
				setProperty(name, value);
			if (!StringUtil.isBlank(keyValue))
				setProperty(name + IDesignElementModel.ID_SUFFIX, keyValue);
		}
	}

	/**
	 * @param keyValue the keyValue to set
	 */
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.parser.AbstractPropertyState#generalJumpTo()
	 */

	protected AbstractParseState generalJumpTo() {
		if (propDefn != null && (element instanceof TextItem && ITextItemModel.CONTENT_PROP.equalsIgnoreCase(name))
				&& handler.versionNumber >= VersionUtil.VERSION_3_2_16) {
			// do not handle extension xml representation property

			if (!(propDefn instanceof ExtensionPropertyDefn && ((ExtensionPropertyDefn) propDefn).hasOwnModel())) {
				CompatibleCDATATextPropertyState state = new CompatibleCDATATextPropertyState(handler, element);
				state.setName(name);
				return state;
			}
		}
		return super.generalJumpTo();

	}

}
