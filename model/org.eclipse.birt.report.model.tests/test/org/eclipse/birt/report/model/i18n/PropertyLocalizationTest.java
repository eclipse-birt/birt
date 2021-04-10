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

package org.eclipse.birt.report.model.i18n;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.SystemPropertyDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.StringTokenizer;
import com.ibm.icu.util.ULocale;

/**
 * Test for property localization.
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testSetStringProperty()}</td>
 * <td>Run through the input test file( input.txt ), find the required
 * DesignElement, and set its property using an input string. The input string
 * is locale-dependent, it represents what the user might input.</td>
 * <td>SetProperty operation should throw an Exception if isOK = "F"(fail), and
 * that now Exception if isOK = "P"(pass).</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testToDisplayStringInEn()}</td>
 * <td>ColorPropertyType.toDisplayString(...,"red" );</td>
 * <td>red</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>ColorPropertyType.toDisplayString(... , ...,"red" );</td>
 * <td>red</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>ColorPropertyType.toDisplayString(... , ...,Integer.decode( "#FF0000" )
 * );</td>
 * <td>red</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>ColorPropertyType.toDisplayString(... , ...,Integer.decode( "#FEDCBA"
 * );</td>
 * <td>#FEDCBA</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>DateTimePropertyType.toDisplayString(... , ...,formatter.parse(
 * "1981-04-29 06:10:50" );</td>
 * <td>4/29/81</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>NumberPropertyType.toDisplayString(... , ...,new BigDecimal( 123456.78d
 * );</td>
 * <td>123,456.78</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>propDefn.details = color ChoiseSet ChoicePropertyType.toDisplayString(...
 * , propDefn, "red" );</td>
 * <td>red</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>propDefn.details = color ChoiseSet ChoicePropertyType.toDisplayString(...
 * , propDefn, Integer.decode( "#FF0000" ) );</td>
 * <td>red</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>BooleanPropertyType.toDisplayString(... , ..., new Boolean( true )
 * );</td>
 * <td>true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>BooleanPropertyType.toDisplayString(... , ..., new Boolean( false
 * ));</td>
 * <td>false</td>
 * <tr>
 * 
 * <tr>
 * <td></td>
 * <td>BooleanPropertyType.toDisplayString(... , ..., "true" )) ;</td>
 * <td>true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>BooleanPropertyType.toDisplayString(... , ..., new Double( 1.01 )) ;</td>
 * <td>true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>BooleanPropertyType.toDisplayString(... , ..., new Integer( 1 ) );</td>
 * <td>true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>BooleanPropertyType.toDisplayString(... , ..., new BigDecimal( 1.01 )
 * );</td>
 * <td>true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>DimensionPropertyType.toDisplayString(... , ..., new DimensionValue(
 * 123456.78d, DimensionValue.CM_UNIT ) );</td>
 * <td>123,456.78cm</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The content is the same as in EN locale</td>
 * <td>En locale value</td>
 * </tr>
 * </table>
 * 
 */
public class PropertyLocalizationTest extends BaseTestCase {

	PropertyType propertyType = null;

	/**
	 * list of test cases.
	 */
	List<Case> cases = new ArrayList<Case>();

	/**
	 * Data Structure for test. Every instance of the class represents an internal
	 * data structure for a line in the input test file.
	 */
	static class Case {

		DesignElement element = null;
		String propName = null;
		ULocale locale = null;
		String inputString = null;
		boolean isOK = false;

		// to track error location in the input test file.
		int lineNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.prepareCases("input/input.txt"); //$NON-NLS-1$
	}

	/**
	 * Read in the input file line by line, parse the input data into an array of
	 * <code>Case<code> . The test file should follow the following format:
	 * <p>
	 * #Element Property name ULocale Input string isOK( Pass(P)/Fail(F) )
	 * ReportDesign creationDate zh_CN 1981-04-29 P
	 * 
	 * <p>
	 * Each line specifies a test case.
	 * <p>
	 * Element represents an DesignElement.
	 * <p>
	 * Property name represents property name of the element you what to set.
	 * <p>
	 * ULocale represents for a locale that you want the input string to be parsed
	 * in.
	 * <p>
	 * Input string is what you want the user to input.
	 * <p>
	 * isOK = "F" means that the setProperty() call should fail because the input
	 * string is not in the format of the locale. isOK = "P" means that the
	 * setProperty() call should success.
	 * 
	 * @param fileName
	 * @throws Exception
	 * 
	 */
	private void prepareCases(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(getResourceAStream(fileName)));
		String line = null;
		int lineNo = 0;

		while ((line = br.readLine()) != null) {
			++lineNo;

			if (line.startsWith("#") || StringUtil.isBlank(line)) //$NON-NLS-1$
				continue;

			StringTokenizer st = new StringTokenizer(line);

			if (st.countTokens() != 5)
				throw new Exception("Invalid input, lineNo = " + lineNo + " tokens = " + st.countTokens()); //$NON-NLS-1$ //$NON-NLS-2$

			String element = st.nextToken();
			String propName = st.nextToken();
			String locale = st.nextToken();
			String inputString = st.nextToken();

			// Convert "\uabcd" to unicode String
			if (inputString.startsWith("\\u")) //$NON-NLS-1$
				inputString = convertToUnicodeString(inputString);

			String isOK = st.nextToken();

			// build.
			Case newCase = new Case();

			DesignElement designElement = null;
			try {
				designElement = (DesignElement) Class.forName("org.eclipse.birt.report.model.elements." + element) //$NON-NLS-1$
						.newInstance();
			} catch (ClassNotFoundException e) {
				throw new Exception("Invalid element name, lineNo = " + lineNo, e); //$NON-NLS-1$
			}

			if ("P".equalsIgnoreCase(isOK)) //$NON-NLS-1$
			{
				newCase.isOK = true;
			} else if ("F".equalsIgnoreCase(isOK)) //$NON-NLS-1$
			{
				newCase.isOK = false;
			} else {
				throw new Exception("Only P/F allowed for 'isOK' column, lineNo = " + lineNo); //$NON-NLS-1$
			}

			int index = locale.indexOf('_');
			if (index != -1) {
				// e.g, zh_CN (language_country)
				String language = locale.substring(0, index);
				String country = locale.substring(index + 1);
				newCase.locale = new ULocale(language, country);
			} else {
				// e.g, en (language)
				newCase.locale = new ULocale(locale);
			}

			newCase.element = designElement;
			newCase.inputString = inputString;
			newCase.propName = propName;
			newCase.lineNo = lineNo;

			this.cases.add(newCase);
		}
	}

	/**
	 * Converts input string with the format "\uabcd\u1234" to java string.
	 * 
	 * @param input string to convert
	 * @return java string
	 * @throws Exception if any exception.
	 */

	private String convertToUnicodeString(String input) throws Exception {
		assert input.length() % 6 == 0;
		assert input.startsWith("\\u"); //$NON-NLS-1$

		String output = ""; //$NON-NLS-1$
		String[] chars = input.split("\\\\u"); //$NON-NLS-1$
		for (int i = 0; i < chars.length; i++) {
			if (chars[i].length() != 0)
				output += convertToUnicodeChar(chars[i]);
		}

		return output;
	}

	/**
	 * Converts input string with the format "\uabcd" to java character.
	 * 
	 * @param input string to convert, which represents one java character.
	 * @return java character
	 * @throws Exception if the input string is not a hex string.
	 */

	private String convertToUnicodeChar(String input) throws Exception {
		assert input.length() == 4;

		byte[] bb = new byte[2];

		bb[0] = (byte) Integer.parseInt(input.substring(0, 2), 16);
		bb[1] = (byte) Integer.parseInt(input.substring(2, 4), 16);

		return new String(bb, "UTF-16"); //$NON-NLS-1$

	}

	static class MockupElementHandle extends DesignElementHandle {

		DesignElement element = null;

		MockupElementHandle(ReportDesign design, DesignElement element) {
			super(design);
			this.element = element;
		}

		@Override
		public DesignElement getElement() {
			return element;
		}
	}

	/**
	 * 
	 * Run through the input test file, find the required DesignElement, and set its
	 * property using an input string. Check that the setProperty operation should
	 * throw an Exception if isOK = "F"(fail), and that now Exception if isOK =
	 * "P"(pass)
	 * <p>
	 * The test file should follow the following format:
	 * <p>
	 * #Element Property name ULocale Input string isOK( Pass(P)/Fail(F) )
	 * ReportDesign creationDate zh_CN 1981-04-29 P
	 * 
	 * <p>
	 * Each line specifies a test case.
	 * <p>
	 * Element represents an DesignElement.
	 * <p>
	 * Property name represents property name of the element you what to set.
	 * <p>
	 * ULocale represents for a locale that you want the input string to be parsed
	 * in.
	 * <p>
	 * Input string is what you want the user to input.
	 * <p>
	 * isOK = "F" means that the setProperty() call should fail because the input
	 * string is not in the format of the locale. isOK = "P" means that the
	 * setProperty() call should success.
	 * 
	 * @throws DesignFileException
	 * 
	 */
	public void testSetStringProperty() throws DesignFileException {
		Case aCase = null;
		for (int i = 0; i < cases.size(); i++) {
			aCase = (Case) cases.get(i);
			openDesign("PropertyLocalizationTest.xml", aCase.locale); //$NON-NLS-1$

			DesignElementHandle elementHandle = new MockupElementHandle(design, aCase.element);

			if (aCase.isOK) // No exception thrown.
			{
				try {
					elementHandle.setProperty(aCase.propName, aCase.inputString);
				} catch (SemanticException e) {
					fail("Case fail, lineNo : " + aCase.lineNo + "   " + e.toString()); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else
			// Should throw an Exception.
			{
				try {
					elementHandle.setProperty(aCase.propName, aCase.inputString);
					fail("Case fail, lineNo : " + aCase.lineNo); //$NON-NLS-1$
				} catch (SemanticException e) {
					assert true;
				}
			}
		}
	}

	/**
	 * Test toDisplayString in English locale.
	 * 
	 * @throws Exception
	 */
	public void testToDisplayStringInEn() throws Exception {
		openDesign("PropertyLocalizationTest.xml", ULocale.ENGLISH); //$NON-NLS-1$

		// 1. Color
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.COLOR_TYPE);
		assertEquals("Red", propertyType.toDisplayString(null, null, "red")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("Blue", propertyType.toDisplayString(null, null, "blue")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("RGB(255,0,0)", propertyType.toDisplayString(design, null, Integer.decode("#FF0000"))); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("RGB(254,220,186)", propertyType.toDisplayString(design, null, Integer.decode("#FEDCBA"))); //$NON-NLS-1$//$NON-NLS-2$

		// Customer color

		assertEquals("My Color", propertyType.toDisplayString(design, null, //$NON-NLS-1$
				"myColor1")); //$NON-NLS-1$

		// 2. DateTime
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.DATE_TIME_TYPE);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		Date date = formatter.parse("1981-04-29 06:10:50"); //$NON-NLS-1$
		assertEquals("4/29/81", propertyType.toDisplayString(null, null, date)); //$NON-NLS-1$

		// 3. Number
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.NUMBER_TYPE);
		double d1 = 123456.78d;
		BigDecimal number = new BigDecimal(d1);
		assertEquals("123,456.78", propertyType.toDisplayString(null, null, number)); //$NON-NLS-1$

		// 4. Choice
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.CHOICE_TYPE);

		PropertyDefn propDefn = new SystemPropertyDefn();
		// Colors:
		propDefn.setDetails(MetaDataDictionary.getInstance().getPropertyType(PropertyType.COLOR_TYPE).getChoices());

		assertEquals("Red", propertyType.toDisplayString( //$NON-NLS-1$
				null, propDefn, "red")); //$NON-NLS-1$

		// fontWeights:
		propDefn.setDetails(MetaDataDictionary.getInstance().getChoiceSet("fontWeight")); //$NON-NLS-1$
		assertEquals("Bold", propertyType.toDisplayString(null, propDefn, "bold")); //$NON-NLS-1$//$NON-NLS-2$

		// 5. Boolean
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.BOOLEAN_TYPE);
		assertEquals("true", propertyType.toDisplayString(null, null, new Boolean(true))); //$NON-NLS-1$
		assertEquals("false", propertyType.toDisplayString(null, null, new Boolean(false))); //$NON-NLS-1$

		// 6. Float

		// 7. Dimension
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.DIMENSION_TYPE);
		DimensionValue dimValue = new DimensionValue(123456.78d, DesignChoiceConstants.UNITS_CM);
		assertEquals("123,456.78cm", propertyType.toDisplayString(null, null, dimValue)); //$NON-NLS-1$

	}

	/**
	 * Test toDisplayString() in testing locale.
	 * 
	 * @throws ParseException
	 * 
	 * @throws DesignFileException
	 */
	public void testToDisplayStringInTestingULocale() throws ParseException, DesignFileException {
		openDesign("PropertyLocalizationTest.xml", TEST_LOCALE); //$NON-NLS-1$

		// 1. Color
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.COLOR_TYPE);
		assertEquals("\u7ea2\u8272", propertyType.toDisplayString(null, null, "red")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("\u84dd\u8272", propertyType.toDisplayString(null, null, "blue")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("RGB(255,0,0)", propertyType.toDisplayString(design, null, Integer.decode("#FF0000"))); //$NON-NLS-1$//$NON-NLS-2$

		// 2. Boolean
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.BOOLEAN_TYPE);
		assertEquals("\u771f", propertyType.toDisplayString(null, null, new Boolean(true))); //$NON-NLS-1$
		assertEquals("\u5047", propertyType.toDisplayString(null, null, new Boolean(false))); //$NON-NLS-1$

		// message.properties unrelated case.

		ThreadResources.setLocale(ULocale.CHINA);

		// 3. DateTime
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.DATE_TIME_TYPE);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		Date date = formatter.parse("1981-04-29 06:10:50"); //$NON-NLS-1$

		// The icu version change cause this to fail, change the input string
		// (previous was "81/4/29") to accommodate
		assertEquals("1981/4/29", propertyType.toDisplayString(null, null, date)); //$NON-NLS-1$

		// 4. Number
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.NUMBER_TYPE);
		double d = 123456.78d;
		BigDecimal number = new BigDecimal(d);
		assertEquals("123,456.78", propertyType.toDisplayString(null, null, number)); //$NON-NLS-1$

		// 5. Float
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.FLOAT_TYPE);
		double d1 = 123456.78d;
		Double number1 = new Double(d1);
		assertEquals("123,456.78", propertyType.toDisplayString(null, null, number1)); //$NON-NLS-1$

		// 6. Dimension
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.DIMENSION_TYPE);
		DimensionValue dimValue = new DimensionValue(123456.78d, DesignChoiceConstants.UNITS_CM);
		assertEquals("123,456.78cm", propertyType.toDisplayString(null, null, dimValue)); //$NON-NLS-1$

		dimValue = new DimensionValue(123456.78d, "mm"); //$NON-NLS-1$
		assertEquals("123,456.78mm", propertyType.toDisplayString(null, null, dimValue)); //$NON-NLS-1$
	}

	/**
	 * Test validateInputStringInEN(). Mostly cover in
	 * {@link #testSetStringProperty()}
	 * 
	 * @throws PropertyValueException
	 * @throws DesignFileException
	 */
	public void testValidateInputStringInEn() throws PropertyValueException, DesignFileException {
		openDesign("PropertyLocalizationTest.xml", ULocale.ENGLISH); //$NON-NLS-1$

		// 1.Color
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.COLOR_TYPE);

		assertEquals(Integer.decode("#FF0000"), //$NON-NLS-1$
				propertyType.validateInputString(design, null, null, "RGB( 255, 0, 0 )")); //$NON-NLS-1$

		assertEquals(Integer.decode("#FF00FF"), //$NON-NLS-1$
				propertyType.validateInputString(design, null, null, "RGB( 400, 0, 300 )")); //$NON-NLS-1$

		assertEquals(Integer.decode("#FF0000"), //$NON-NLS-1$
				propertyType.validateInputString(design, null, null, "RGB( 255%, 0%, 0% )")); //$NON-NLS-1$

		assertEquals(Integer.decode("#FF00FF"), //$NON-NLS-1$
				propertyType.validateInputString(design, null, null, "RGB( 300%, 0%, 268% )")); //$NON-NLS-1$

		assertEquals("red", propertyType.validateInputString(design, null, null, "red")); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			propertyType.validateInputString(design, null, null, "None-exist-color-name"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			// pass
		}

		// 2. DateTime
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.DATE_TIME_TYPE);
		Date date = (Date) propertyType.validateInputString(design, null, null, "04/29/81"); //$NON-NLS-1$
		assertEquals(Calendar.APRIL, date.getMonth());
		assertEquals(29, date.getDate());
		assertEquals(81, date.getYear());

		// 3. Number
		// propertyType = MetaDataDictionary.getInstance( ).getPropertyType(
		// PropertyType.NUMBER_TYPE );
		// System.out.println( propertyType.validateInputString( design, null,
		// "123,456.78" ) ); //$NON-NLS-1$
		// System.out.println( propertyType.validateInputString( design, null,
		// "1234 456,78" ) ); //$NON-NLS-1$
		// System.out.println( propertyType.validateInputString( design, null,
		// "7,328,059,002,903.26" ) ); //$NON-NLS-1$

		// 4. Choice
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.CHOICE_TYPE);
		UserPropertyDefn propDefn = new UserPropertyDefn();
		propDefn.setType(propertyType);

		// Colors:
		propDefn.setDetails(MetaDataDictionary.getInstance().getPropertyType(PropertyType.COLOR_TYPE).getChoices());

		assertEquals(IColorConstants.RED, propertyType.validateInputString(null, null, propDefn, "red")); //$NON-NLS-1$
		try {
			propertyType.validateInputString(design, null, propDefn, "None-exist-color-name"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			// pass
		}
		// fontWeights:
		propDefn.setDetails(MetaDataDictionary.getInstance().getChoiceSet("fontWeight")); //$NON-NLS-1$
		assertEquals("normal", propertyType.validateInputString(null, null, //$NON-NLS-1$
				propDefn, "normal")); //$NON-NLS-1$

		// 5. Boolean
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.BOOLEAN_TYPE);
		assertEquals(new Boolean(true), propertyType.validateInputString(null, null, null, "true")); //$NON-NLS-1$

		// 6. Float
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.FLOAT_TYPE);
		double d1 = 123456.78d;
		Double number1 = new Double(d1);
		assertEquals("123,456.78", propertyType.toDisplayString(null, null, number1)); //$NON-NLS-1$

		// For German

	}

	/**
	 * Test validateInputStringInZH_CN(). Mostly cover in
	 * {@link #testSetStringProperty()}
	 * 
	 * @throws PropertyValueException
	 */
	public void testValidateInputStringInZH_CN() throws PropertyValueException {
		ThreadResources.setLocale(TEST_LOCALE);

		// 1.Color
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.COLOR_TYPE);
		// "\u7ea2\u8272" represents ascii for "red".
		assertEquals("red", propertyType.validateInputString(null, null, null, "\u7ea2\u8272")); //$NON-NLS-1$ //$NON-NLS-2$

		// 2. Choice
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.CHOICE_TYPE);
		UserPropertyDefn propDefn = new UserPropertyDefn();
		propDefn.setType(propertyType);

		// Colors:
		propDefn.setDetails(MetaDataDictionary.getInstance().getPropertyType(PropertyType.COLOR_TYPE).getChoices());
		// Red
		assertEquals(IColorConstants.RED, propertyType.validateInputString(null, null, propDefn, "\u7ea2\u8272")); //$NON-NLS-1$

		// fontWeights:
		propDefn.setDetails(MetaDataDictionary.getInstance().getChoiceSet("fontWeight")); //$NON-NLS-1$
		assertEquals("normal", propertyType.validateInputString(null, null, //$NON-NLS-1$
				propDefn, "\u6807\u51c6")); //$NON-NLS-1$

		// 3. Boolean
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.BOOLEAN_TYPE);
		assertEquals(new Boolean(true), propertyType.validateInputString(null, null, null, "\u771f")); //$NON-NLS-1$
		assertEquals(new Boolean(false), propertyType.validateInputString(null, null, null, "\u5047")); //$NON-NLS-1$

		// message.properties unrelated cases.

		ThreadResources.setLocale(ULocale.CHINA);
		// 4. DateTime
		propertyType = MetaDataDictionary.getInstance().getPropertyType(PropertyType.DATE_TIME_TYPE);
		// The icu version change cause this to fail, change the input string
		// (previous was "81/4/29") to accommodate
		Date date = (Date) propertyType.validateInputString(null, null, null, "1981/4/29"); //$NON-NLS-1$
		assertEquals(Calendar.APRIL, date.getMonth());
		assertEquals(29, date.getDate());
		assertEquals(81, date.getYear());
	}
}
