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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.BooleanPropertyType;
import org.xml.sax.SAXException;

/**
 * Represents the state to parse ScalarParameter.allowNull and
 * ScalarParameter.allowNull properties. These properties are replaced by
 * ScalarParameter.isRequired in BIRT 2.2 M5.
 *
 * <ul>
 * <li>TextDataItem: contentTypeExpr to contentType</li>
 * <li>ListGroup: groupStart to intervalBase</li>
 * </ul>
 */

class CompatibleParamAllowMumbleState extends CompatiblePropertyState {

	/**
	 * The obsolete property name.
	 */

	private String obsoletePropName;

	/**
	 * Constructs a <code>CompatibleRenamedPropertyState</code> to parse an obsolete
	 * property.
	 *
	 * @param theHandler       the parser handle
	 * @param element          the element that holds the obsolete property
	 * @param obsoletePropName the name of the obsolete property.
	 */

	public CompatibleParamAllowMumbleState(ModuleParserHandler theHandler, DesignElement element,
			String obsoletePropName) {
		super(theHandler, element);
		this.obsoletePropName = obsoletePropName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */

	@Override
	public void end() throws SAXException {
		String value = text.toString();
		if (value.length() == 0) {
			return;
		}

		Boolean blnValue = null;

		if (BooleanPropertyType.FALSE.equalsIgnoreCase(value)) {
			blnValue = Boolean.FALSE;
		} else if (BooleanPropertyType.TRUE.equalsIgnoreCase(value)) {
			blnValue = Boolean.TRUE;
		}

		// allowNull is in the position 0, allowBlank is in the position 1.

		Boolean[] blnValues = (Boolean[]) handler.tempValue.get(element);
		if (blnValues == null) {
			blnValues = new Boolean[2];
			blnValues[0] = Boolean.FALSE;
			blnValues[1] = Boolean.TRUE;
			handler.tempValue.put(element, blnValues);
		}

		if ("allowNull".equalsIgnoreCase(obsoletePropName)) { //$NON-NLS-1$
			blnValues[0] = blnValue;
		} else if ("allowBlank".equalsIgnoreCase(obsoletePropName)) { //$NON-NLS-1$
			blnValues[1] = blnValue;
		} else { // $NON-NLS-1$
			assert false;
		}
	}

}
