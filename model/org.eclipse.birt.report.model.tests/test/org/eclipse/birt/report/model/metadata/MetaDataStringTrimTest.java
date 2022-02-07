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

package org.eclipse.birt.report.model.metadata;

import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;

/**
 * Tests meta trim string operation.
 * 
 */
public class MetaDataStringTrimTest extends AbstractMetaTest {

	/**
	 * Tests parser the input trim string value and convert it into number. Then
	 * trim the input string according to the option value.
	 * 
	 * @throws Exception
	 */
	public void testTrimString() throws Exception {
		loadMetaData(MetaDataStringTrimTest.class.getResourceAsStream("input/TrimStringRomTest.def")); //$NON-NLS-1$

		IElementDefn elemDefn = MetaDataDictionary.getInstance()
				.getElement(ReportDesignConstants.REPORT_DESIGN_ELEMENT);
		List<IElementPropertyDefn> propertyList = elemDefn.getProperties();

		// no trim
		PropertyDefn propDefn = (PropertyDefn) propertyList.get(0);
		assertEquals("noTrim", propDefn.getName()); //$NON-NLS-1$
		assertEquals(" test ", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("  ", propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$ //$NON-NLS-2$

		// trim space
		propDefn = (PropertyDefn) propertyList.get(1);
		assertEquals("trimSpace", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", propDefn.validateValue(design, null, "")); //$NON-NLS-1$ //$NON-NLS-2$

		// trimEmptyToNull
		propDefn = (PropertyDefn) propertyList.get(2);
		assertEquals("trimEmptyToNull", propDefn.getName()); //$NON-NLS-1$
		assertEquals(" test ", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("   ", propDefn.validateValue(design, null, "   ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "")); //$NON-NLS-1$

		// trimSpace and trimEmptyToNull
		propDefn = (PropertyDefn) propertyList.get(3);
		assertEquals("trimEmptyAndNull", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "   ")); //$NON-NLS-1$
		assertNull(propDefn.validateValue(design, null, "")); //$NON-NLS-1$

		// trim option has no value
		propDefn = (PropertyDefn) propertyList.get(4);
		assertEquals("noTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "   ")); //$NON-NLS-1$
		assertNull(propDefn.validateValue(design, null, "")); //$NON-NLS-1$

		// html type and trim option is noTrim
		propDefn = (PropertyDefn) propertyList.get(5);
		assertEquals(IPropertyType.HTML_TYPE_NAME, propDefn.getType().getName());
		assertEquals("htmlNoTrim", propDefn.getName()); //$NON-NLS-1$
		assertEquals(" test ", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$

		// html type and trim option has no value.
		propDefn = (PropertyDefn) propertyList.get(6);
		assertEquals(IPropertyType.HTML_TYPE_NAME, propDefn.getType().getName());
		assertEquals("htmlNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "   ")); //$NON-NLS-1$

		// resource key type and trim option is noTrim
		propDefn = (PropertyDefn) propertyList.get(7);
		assertEquals(IPropertyType.RESOURCE_KEY_TYPE_NAME, propDefn.getType().getName());
		assertEquals("resourceKeyNoTrim", propDefn.getName()); //$NON-NLS-1$
		assertEquals(" test ", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$

		// resource key type and trim option has no value.
		propDefn = (PropertyDefn) propertyList.get(8);
		assertEquals(IPropertyType.RESOURCE_KEY_TYPE_NAME, propDefn.getType().getName());
		assertEquals("resourceKeyNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals(" test ", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("   ", propDefn.validateValue(design, null, "   ")); //$NON-NLS-1$ //$NON-NLS-2$

		// uri type and trim option is no trim
		propDefn = (PropertyDefn) propertyList.get(9);
		assertEquals(IPropertyType.URI_TYPE_NAME, propDefn.getType().getName());
		assertEquals("uriNoTrim", propDefn.getName()); //$NON-NLS-1$
		assertEquals(" test ", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$

		// uri type and trim option is no trim
		propDefn = (PropertyDefn) propertyList.get(10);
		assertEquals(IPropertyType.URI_TYPE_NAME, propDefn.getType().getName());
		assertEquals("uriNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "   ")); //$NON-NLS-1$

		// literal string type and trim option is no trim
		propDefn = (PropertyDefn) propertyList.get(11);
		assertEquals(IPropertyType.LITERAL_STRING_TYPE_NAME, propDefn.getType().getName());
		assertEquals("literalStringTrimSpace", propDefn.getName()); //$NON-NLS-1$
		assertEquals("", propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$ //$NON-NLS-2$

		// literal string type and trim option has no value.
		propDefn = (PropertyDefn) propertyList.get(12);
		assertEquals(IPropertyType.LITERAL_STRING_TYPE_NAME, propDefn.getType().getName());
		assertEquals("literalStringNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals(" test ", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("   ", propDefn.validateValue(design, null, "   ")); //$NON-NLS-1$ //$NON-NLS-2$

		// expression type and trim option is trimSpace.
		propDefn = (PropertyDefn) propertyList.get(13);
		assertEquals(IPropertyType.EXPRESSION_TYPE_NAME, propDefn.getType().getName());
		assertEquals("expressionStringTrimSpace", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", ((Expression) propDefn.validateValue(design, null, " test ")).getStringExpression()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", ((Expression) propDefn.validateValue(design, null, "  ")).getStringExpression()); //$NON-NLS-1$ //$NON-NLS-2$

		// expression type and trim option has no value.
		propDefn = (PropertyDefn) propertyList.get(14);
		assertEquals(IPropertyType.EXPRESSION_TYPE_NAME, propDefn.getType().getName());
		assertEquals("expressionStringNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals("  ", ((Expression) propDefn.validateValue(design, null, "  ")).getStringExpression()); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "")); //$NON-NLS-1$

		// member Key type and trim option is trimSpace.
		propDefn = (PropertyDefn) propertyList.get(15);
		assertEquals(IPropertyType.MEMBER_KEY_NAME, propDefn.getType().getName());
		assertEquals("memberKeyTrimSpace", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$ //$NON-NLS-2$

		// member key type and trim option has no value.
		propDefn = (PropertyDefn) propertyList.get(16);
		assertEquals(IPropertyType.MEMBER_KEY_NAME, propDefn.getType().getName());
		assertEquals("memberKeyNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$
		assertNull(propDefn.validateValue(design, null, "")); //$NON-NLS-1$

		// name type and trim option is trimSpace.
		propDefn = (PropertyDefn) propertyList.get(17);
		assertEquals(IPropertyType.NAME_TYPE_NAME, propDefn.getType().getName());
		assertEquals("nameTrimSpace", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$ //$NON-NLS-2$

		// name type and trim option has no value.
		propDefn = (PropertyDefn) propertyList.get(18);
		assertEquals(IPropertyType.NAME_TYPE_NAME, propDefn.getType().getName());
		assertEquals("nameNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$
		assertNull(propDefn.validateValue(design, null, "")); //$NON-NLS-1$

		// script type and trim option is trimSpace.
		propDefn = (PropertyDefn) propertyList.get(19);
		assertEquals(IPropertyType.SCRIPT_TYPE_NAME, propDefn.getType().getName());
		assertEquals("scriptTrimSpace", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$ //$NON-NLS-2$

		// script type and trim option has no value.
		propDefn = (PropertyDefn) propertyList.get(20);
		assertEquals(IPropertyType.SCRIPT_TYPE_NAME, propDefn.getType().getName());
		assertEquals("scriptNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertEquals("  ", propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(propDefn.validateValue(design, null, "")); //$NON-NLS-1$

		// string type and trim option is trimSpace.
		propDefn = (PropertyDefn) propertyList.get(21);
		assertEquals(IPropertyType.STRING_TYPE_NAME, propDefn.getType().getName());
		assertEquals("stringTrimSpace", propDefn.getName()); //$NON-NLS-1$
		assertEquals("test", propDefn.validateValue(design, null, " test ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$ //$NON-NLS-2$

		// string type and trim option has no value.
		propDefn = (PropertyDefn) propertyList.get(22);
		assertEquals(IPropertyType.STRING_TYPE_NAME, propDefn.getType().getName());
		assertEquals("stringNoTrimOptionValue", propDefn.getName()); //$NON-NLS-1$
		assertNull(propDefn.validateValue(design, null, "  ")); //$NON-NLS-1$
		assertNull(propDefn.validateValue(design, null, "")); //$NON-NLS-1$

	}
}
