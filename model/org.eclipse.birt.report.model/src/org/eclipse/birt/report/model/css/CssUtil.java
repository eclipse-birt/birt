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

package org.eclipse.birt.report.model.css;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;

/**
 *
 * Converts some CSS objects, such as <code>LexicalUnit</code>,
 * <code>Selector</code> to the string representations.
 */

public final class CssUtil {

	/**
	 * Gets the string output for a lexical unit implementation of the Flute
	 * package.
	 *
	 * @param lu the lexical unit of Flute implementation
	 * @return the string output of the lexical unit
	 */

	public static String toString(LexicalUnit lu) {
		StringBuilder sb = new StringBuilder();
		toString(sb, lu);
		return sb.toString();
	}

	private static void toString(StringBuilder sb, LexicalUnit lu) {
		int type = lu.getLexicalUnitType();
		switch (type) {
		case LexicalUnit.SAC_OPERATOR_COMMA:
			sb.append(","); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_PLUS:
			sb.append("+"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_MINUS:
			sb.append("-"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_MULTIPLY:
			sb.append("*"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_SLASH:
			sb.append("/"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_MOD:
			sb.append("%"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_EXP:
			sb.append("^"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_LT:
			sb.append("<"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_GT:
			sb.append(">"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_LE:
			sb.append("<="); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_GE:
			sb.append(">="); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_OPERATOR_TILDE:
			sb.append("~"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_INHERIT:
			sb.append("inherit"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_INTEGER:
			sb.append(String.valueOf(lu.getIntegerValue()));
			break;
		case LexicalUnit.SAC_REAL:
			sb.append(trimFloat(lu.getFloatValue()));
			break;
		case LexicalUnit.SAC_EM:
		case LexicalUnit.SAC_EX:
		case LexicalUnit.SAC_PIXEL:
		case LexicalUnit.SAC_INCH:
		case LexicalUnit.SAC_CENTIMETER:
		case LexicalUnit.SAC_MILLIMETER:
		case LexicalUnit.SAC_POINT:
		case LexicalUnit.SAC_PICA:
		case LexicalUnit.SAC_PERCENTAGE:
		case LexicalUnit.SAC_DEGREE:
		case LexicalUnit.SAC_GRADIAN:
		case LexicalUnit.SAC_RADIAN:
		case LexicalUnit.SAC_MILLISECOND:
		case LexicalUnit.SAC_SECOND:
		case LexicalUnit.SAC_HERTZ:
		case LexicalUnit.SAC_KILOHERTZ:
		case LexicalUnit.SAC_DIMENSION:
			sb.append(trimFloat(lu.getFloatValue())).append(lu.getDimensionUnitText());
			break;
		case LexicalUnit.SAC_URI:
			sb.append("url(").append(lu.getStringValue()).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case LexicalUnit.SAC_COUNTER_FUNCTION:
			sb.append(lu.toString());
			break;
		case LexicalUnit.SAC_COUNTERS_FUNCTION:
			sb.append(lu.toString());
			break;
		case LexicalUnit.SAC_RGBCOLOR:
			sb.append("rgb("); //$NON-NLS-1$
			toStringParameters(sb, lu);
			sb.append(")"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_IDENT:
			sb.append(lu.getStringValue());
			break;
		case LexicalUnit.SAC_STRING_VALUE:
			sb.append("\"").append(lu.getStringValue()).append("\""); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case LexicalUnit.SAC_ATTR:
			sb.append("attr("); //$NON-NLS-1$
			toStringParameters(sb, lu);
			sb.append(")"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_RECT_FUNCTION:
			sb.append("rect("); //$NON-NLS-1$
			toStringParameters(sb, lu);
			sb.append(")"); //$NON-NLS-1$
			break;
		case LexicalUnit.SAC_UNICODERANGE:
			sb.append(lu.getStringValue());
			break;
		case LexicalUnit.SAC_SUB_EXPRESSION:
			sb.append(lu.getStringValue());
			break;
		case LexicalUnit.SAC_FUNCTION:
			sb.append(lu.getFunctionName());
			sb.append("("); //$NON-NLS-1$
			toStringParameters(sb, lu);
			sb.append(")"); //$NON-NLS-1$
			break;
		}
	}

	private static void toStringParameters(StringBuilder sb, LexicalUnit lu) {
		LexicalUnit parameters = lu.getParameters();
		sb.append(toString(parameters));
		for (LexicalUnit nextLexicalUnit = parameters
				.getNextLexicalUnit(); nextLexicalUnit != null; nextLexicalUnit = nextLexicalUnit
						.getNextLexicalUnit()) {
			sb.append(toString(nextLexicalUnit));
		}
	}

	/**
	 * Trims the float value. If the value is an integer, method will return value
	 * that has "#.##" format(9->9.00).
	 *
	 * @param f the float value to handle
	 * @return the trimmed float value string
	 */

	private static String trimFloat(float f) {
		String s = String.valueOf(f);
		return (f - (int) f != 0) ? s : s.substring(0, s.length() - 2);
	}

	/**
	 * Converts a selector to a string representation.
	 *
	 * @param selector the selector to handle
	 * @return the string representation of the selector
	 */

	public static String toString(Selector selector) {
		if (selector == null) {
			return null;
		}

		switch (selector.getSelectorType()) {
		case Selector.SAC_CONDITIONAL_SELECTOR:
			assert selector instanceof ConditionalSelector;
			return toString(((ConditionalSelector) selector).getSimpleSelector())
					+ toString(((ConditionalSelector) selector).getCondition());

		case Selector.SAC_ELEMENT_NODE_SELECTOR:
			String localName = ((ElementSelector) selector).getLocalName();
			return (localName != null) ? localName : "*"; //$NON-NLS-1$

		// for pseudo selector, need to append ":" before the selector
		// name.

		case Selector.SAC_PSEUDO_ELEMENT_SELECTOR:
			localName = ((ElementSelector) selector).getLocalName();
			return (localName != null) ? ":" + localName : ":*"; //$NON-NLS-1$ //$NON-NLS-2$

		case Selector.SAC_DESCENDANT_SELECTOR:
			assert selector instanceof DescendantSelector;
			return toString(((DescendantSelector) selector).getAncestorSelector())
					+ toString(((DescendantSelector) selector).getSimpleSelector());

		case Selector.SAC_CHILD_SELECTOR:
			assert selector instanceof DescendantSelector;
			return toString(((DescendantSelector) selector).getAncestorSelector())
					+ toString(((DescendantSelector) selector).getSimpleSelector());

		case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
			assert selector instanceof SiblingSelector;
			return toString(((SiblingSelector) selector).getSelector())
					+ toString(((SiblingSelector) selector).getSiblingSelector());
		}
		return selector.toString();
	}

	/**
	 * Converts a condition to a string representation.
	 *
	 * @param condition the condition to handle
	 * @return the string representation of the condition
	 */

	static String toString(Condition condition) {
		if (condition == null) {
			return null;
		}
		switch (condition.getConditionType()) {
		case Condition.SAC_AND_CONDITION:
			assert condition instanceof CombinatorCondition;
			return toString(((CombinatorCondition) condition).getFirstCondition())
					+ toString(((CombinatorCondition) condition).getSecondCondition());

		case Condition.SAC_ATTRIBUTE_CONDITION:
			assert condition instanceof AttributeCondition;
			String value = ((AttributeCondition) condition).getValue();
			if (value != null) {
				return "[" //$NON-NLS-1$
						+ ((AttributeCondition) condition).getLocalName() + "=\"" + value + "\"]"; //$NON-NLS-1$//$NON-NLS-2$
			}
			return "[" + ((AttributeCondition) condition).getLocalName() //$NON-NLS-1$
					+ "]"; //$NON-NLS-1$

		case Condition.SAC_ID_CONDITION:
			assert condition instanceof AttributeCondition;
			return "#" + ((AttributeCondition) condition).getValue(); //$NON-NLS-1$

		case Condition.SAC_LANG_CONDITION:
			assert condition instanceof LangCondition;
			return ":lang(" + ((LangCondition) condition).getLang() //$NON-NLS-1$
					+ ")"; //$NON-NLS-1$

		case Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION:
			assert condition instanceof AttributeCondition;
			return "[" + ((AttributeCondition) condition).getLocalName() //$NON-NLS-1$
					+ "~=\"" //$NON-NLS-1$
					+ ((AttributeCondition) condition).getValue() + "\"]"; //$NON-NLS-1$

		case Condition.SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
			assert condition instanceof AttributeCondition;
			return "[" + ((AttributeCondition) condition).getLocalName() //$NON-NLS-1$
					+ "|=\"" //$NON-NLS-1$
					+ ((AttributeCondition) condition).getValue() + "\"]"; //$NON-NLS-1$

		case Condition.SAC_CLASS_CONDITION:
			assert condition instanceof AttributeCondition;
			return "." + ((AttributeCondition) condition).getValue(); //$NON-NLS-1$

		case Condition.SAC_PSEUDO_CLASS_CONDITION:
			assert condition instanceof AttributeCondition;
			return ":" + ((AttributeCondition) condition).getValue(); //$NON-NLS-1$

		}
		return condition.toString();
	}

}
