/*************************************************************************************
 * Copyright (c) 2024 Thomas Gutmann.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Gutmann - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.Map;

import org.eclipse.birt.report.engine.api.ITaskOption;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * Emitter service to handle the emitter configuration options
 *
 * @since 4.18
 *
 */
public class EmitterServices {

	/**
	 * Convert an Object to a boolean, with quite a few options about the class of
	 * the Object.
	 *
	 * @param options      The task options to extract the value from.
	 * @param birtContent  The leaf node to look for UserProperties
	 * @param name         The name of the value to extract from options.
	 * @param defaultValue Value to return if value is null.
	 * @return true if value in some way represents a boolean TRUE value.
	 */
	public static boolean booleanOption(ITaskOption options, IContent birtContent, String name, boolean defaultValue) {
		boolean result = defaultValue;
		Object value = null;

		IElement currentElement = birtContent;

		while ((currentElement != null) && (value == null)) {
			if (currentElement instanceof IContent) {
				Object designObject = ((IContent) currentElement).getGenerateBy();
				if (designObject instanceof ReportElementDesign) {
					Map<String, Expression> userProperties = ((ReportElementDesign) designObject).getUserProperties();
					if (userProperties != null) {
						Expression expression = userProperties.get(name);
						if (expression instanceof Expression.Constant) {
							Expression.Constant constant = (Expression.Constant) expression;
							value = constant.getValue();
						}
					}
				}
			}
			if (value == null) {
				currentElement = currentElement.getParent();
			}
		}
		if ((value == null) && (birtContent != null)) {
			Map<String, Expression> userProperties = birtContent.getReportContent().getDesign().getUserProperties();
			if (userProperties != null) {
				Expression expression = userProperties.get(name);
				if (expression instanceof Expression.Constant) {
					Expression.Constant constant = (Expression.Constant) expression;
					value = constant.getValue();
				}
			}
		}

		if ((value == null) && (options != null)) {
			value = options.getOption(name);
		}

		if (birtContent != null && birtContent.getReportContent() != null && value == null) {
			value = getReportDesignConfiguration(birtContent.getReportContent(), name);
		}

		if (value != null) {
			result = booleanOption(value, defaultValue);
		}

		return result;
	}

	/**
	 * Convert an Object to a boolean, with quite a few options about the class of
	 * the Object.
	 *
	 * @param options       The task options to extract the value from.
	 * @param reportContent The report node to look for UserProperties
	 * @param name          The name of the value to extract from options.
	 * @param defaultValue  Value to return if value is null.
	 * @return true if value in some way represents a boolean TRUE value.
	 */
	public static boolean booleanOption(ITaskOption options, IReportContent reportContent, String name,
			boolean defaultValue) {
		boolean result = defaultValue;
		Object value = null;

		if (reportContent != null) {
			Map<String, Expression> userProperties = reportContent.getDesign().getUserProperties();
			if (userProperties != null) {
				Expression expression = userProperties.get(name);
				if (expression instanceof Expression.Constant) {
					Expression.Constant constant = (Expression.Constant) expression;
					value = constant.getValue();
				}
			}
		}

		if ((value == null) && (options != null)) {
			value = options.getOption(name);
		}

		if (reportContent != null && value == null) {
			value = getReportDesignConfiguration(reportContent, name);
		}

		if (value != null) {
			result = booleanOption(value, defaultValue);
		}

		return result;
	}

	/**
	 * Search for an emitter option and return it as a string
	 *
	 * @param options      The task options to extract the value from.
	 * @param birtContent  The leaf node to look for UserProperties
	 * @param name         The name of the value to extract from options.
	 * @param defaultValue Value to return if value is null.
	 * @return a string, or the defaultValue
	 */
	public static String stringOption(ITaskOption options, IContent birtContent, String name, String defaultValue) {
		String result = defaultValue;
		Object value = null;

		IElement currentElement = birtContent;

		while ((currentElement != null) && (value == null)) {
			if (currentElement instanceof IContent) {
				Object designObject = ((IContent) currentElement).getGenerateBy();
				if (designObject instanceof ReportElementDesign) {
					Map<String, Expression> userProperties = ((ReportElementDesign) designObject).getUserProperties();
					if (userProperties != null) {
						Expression expression = userProperties.get(name);
						if (expression instanceof Expression.Constant) {
							Expression.Constant constant = (Expression.Constant) expression;
							value = constant.getValue();
						}
					}
				}
			}
			if (value == null) {
				currentElement = currentElement.getParent();
			}
		}
		if ((value == null) && (birtContent != null)) {
			Map<String, Expression> userProperties = birtContent.getReportContent().getDesign().getUserProperties();
			if (userProperties != null) {
				Expression expression = userProperties.get(name);
				if (expression instanceof Expression.Constant) {
					Expression.Constant constant = (Expression.Constant) expression;
					value = constant.getValue();
				}
			}
		}

		if ((value == null) && (options != null)) {
			value = options.getOption(name);
		}

		if (birtContent != null && birtContent.getReportContent() != null && value == null) {
			value = getReportDesignConfiguration(birtContent.getReportContent(), name);
		}

		if (value != null) {
			result = value.toString();
		}

		return result;
	}

	/**
	 * Search for an emitter option and return it as a string
	 *
	 * @param options       The task options to extract the value from.
	 * @param reportContent The report
	 * @param name          The name of the value to extract from options.
	 * @param defaultValue  Value to return if value is null.
	 * @return a string, or the defaultValue
	 */
	public static String stringOption(ITaskOption options, IReportContent reportContent, String name,
			String defaultValue) {
		String result = defaultValue;
		Object value = null;

		if (reportContent != null) {
			Map<String, Expression> userProperties = reportContent.getDesign().getUserProperties();
			if (userProperties != null) {
				Expression expression = userProperties.get(name);
				if (expression instanceof Expression.Constant) {
					Expression.Constant constant = (Expression.Constant) expression;
					value = constant.getValue();
				}
			}
		}

		if ((value == null) && (options != null)) {
			value = options.getOption(name);
		}

		if (reportContent != null && value == null) {
			value = getReportDesignConfiguration(reportContent, name);
		}

		if (value != null) {
			result = value.toString();
		}

		return result;
	}

	/**
	 * Search for an emitter option and return it as an integer
	 *
	 * @param options      The task options to extract the value from.
	 * @param birtContent  The leaf node to look for UserProperties
	 * @param name         The name of the value to extract from options.
	 * @param defaultValue Value to return if value is null.
	 * @return an integer, or the defaultValue
	 */
	public static int integerOption(ITaskOption options, IContent birtContent, String name, int defaultValue) {
		int result = defaultValue;
		Object value = null;

		IElement currentElement = birtContent;

		while ((currentElement != null) && (value == null)) {
			if (currentElement instanceof IContent) {
				Object designObject = ((IContent) currentElement).getGenerateBy();
				if (designObject instanceof ReportElementDesign) {
					Map<String, Expression> userProperties = ((ReportElementDesign) designObject).getUserProperties();
					if (userProperties != null) {
						Expression expression = userProperties.get(name);
						if (expression instanceof Expression.Constant) {
							Expression.Constant constant = (Expression.Constant) expression;
							value = constant.getValue();
						}
					}
				}
			}
			if (value == null) {
				currentElement = currentElement.getParent();
			}
		}
		if ((value == null) && (birtContent != null)) {
			Map<String, Expression> userProperties = birtContent.getReportContent().getDesign().getUserProperties();
			if (userProperties != null) {
				Expression expression = userProperties.get(name);
				if (expression instanceof Expression.Constant) {
					Expression.Constant constant = (Expression.Constant) expression;
					value = constant.getValue();
				}
			}
		}

		if ((value == null) && (options != null)) {
			value = options.getOption(name);
		}

		if (birtContent != null && birtContent.getReportContent() != null && value == null) {
			value = getReportDesignConfiguration(birtContent.getReportContent(), name);
		}

		if (value instanceof Number) {
			result = ((Number) value).intValue();
		} else if (value != null) {
			try {
				result = Integer.parseInt(value.toString());
			} catch (Exception ex) {
			}
		}

		return result;
	}

	/**
	 * Convert an Object to a boolean, with quite a few options about the class of
	 * the Object.
	 *
	 * @param value        A value that can be of any type.
	 * @param defaultValue Value to return if value is null.
	 * @return true if value in some way represents a boolean TRUE value.
	 */
	public static boolean booleanOption(Object value, boolean defaultValue) {
		if (value != null) {
			if (value instanceof Boolean) {
				return ((Boolean) value).booleanValue();
			}
			if (value instanceof Number) {
				return ((Number) value).doubleValue() != 0.0;
			}
			if (value != null) {
				return Boolean.parseBoolean(value.toString());
			}
		}
		return defaultValue;
	}

	/**
	 * Get the boolean value of a user property
	 *
	 * @param propertyName user property name
	 * @param defaultValue default of the user property
	 * @return the user property result of boolean
	 */
	public static boolean booleanOption(String propertyName, boolean defaultValue) {
		return booleanOption(null, propertyName, defaultValue);
	}

	/**
	 * Get the boolean value of a user property
	 *
	 * @param birtContent  element of the report
	 * @param propertyName user property name
	 * @param defaultValue default of the user property
	 * @return the user property result of boolean
	 */
	public static boolean booleanOption(IContent birtContent, String propertyName, boolean defaultValue) {
		Object value = getUserProperty(birtContent, propertyName);
		return booleanOption(value, defaultValue);
	}

	/**
	 * Get the string value of a user property
	 *
	 * @param birtContent  element of the report
	 * @param propertyName user property name
	 * @param defaultValue default of the user property
	 * @return the user property result as string
	 */
	public static String stringOption(IContent birtContent, String propertyName, boolean defaultValue) {
		String stringValue = null;
		Object value = getUserProperty(birtContent, propertyName);
		if (value != null) {
			stringValue = value.toString();
		}
		return stringValue;
	}

	/**
	 * Get the object of the user property
	 *
	 * @param birtContent report element, e.g. a IListContent
	 * @param propName    property name
	 * @return user property object independent of the type
	 */
	protected static Object getUserProperty(IContent birtContent, String propName) {
		Object value = null;

		Map<String, Object> userprops = birtContent.getUserProperties();
		if (userprops != null) {
			value = userprops.get(propName);
		}
		if (value == null) {
			ReportItemDesign designElem = (ReportItemDesign) birtContent.getGenerateBy();
			if (designElem != null) {
				Map<String, Expression> designUserprops = designElem.getUserProperties();
				if (designUserprops != null) {
					Expression expression = designUserprops.get(propName);
					if (expression instanceof Expression.Constant) {
						Expression.Constant constant = (Expression.Constant) expression;
						value = constant.getValue();
					}
				}
			}
		}
		if (value == null) {
			Map<String, Expression> designUserprops = birtContent.getReportContent().getDesign().getUserProperties();
			if (designUserprops != null) {
				Expression expression = designUserprops.get(propName);
				if (expression instanceof Expression.Constant) {
					Expression.Constant constant = (Expression.Constant) expression;
					value = constant.getValue();
				}
			}
		}
		return value;
	}

	/*
	 * Read the configuration from the report design if no user property is set
	 */
	private static Object getReportDesignConfiguration(IReportContent reportContent, String name) {
		Object value = null;

		if (name.equalsIgnoreCase(DocEmitter.WORD_MARGIN_PADDING_WRAPPED_TABLE)) {
			value = reportContent.getDesign().getReportDesign().getWordWrapTableForMarginPadding();

		} else if (name.equalsIgnoreCase(DocEmitter.WORD_MARGIN_PADDING_COMBINE)) {
			value = reportContent.getDesign().getReportDesign().getWordCombineMarginPadding();

		} else if (name.equalsIgnoreCase(DocEmitter.WORD_HEADER_FOOTER_WRAPPED_TABLE)) {
			value = reportContent.getDesign().getReportDesign().getWordWrapTableForHeaderFooter();

		} else if (name.equalsIgnoreCase(DocEmitter.WORD_ADD_EMPTY_PARAGRAPH_FOR_LIST_CELL)) {
			value = reportContent.getDesign().getReportDesign().getWordListCellAddEmptyPara();

		}
		return value;
	}

}
