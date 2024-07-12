/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013, 2024 James Talbut and others
 *  jim-emitters@spudsoft.co.uk
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import java.util.Map;

import org.eclipse.birt.report.engine.api.ITaskOption;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;

import uk.co.spudsoft.birt.emitters.excel.framework.ExcelEmitterPlugin;

/**
 * Emitter service to handle the emitter configuration options
 *
 * @since 3.3
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

		if (value != null) {
			result = booleanOption(value, defaultValue);
		}

		if (reportContent != null && value == null) {
			value = getReportDesignConfiguration(reportContent, name);
		}

		return result;
	}

	/*
	 * Read the configuration from the report design if no user property is set
	 */
	private static Object getReportDesignConfiguration(IReportContent reportContent, String name) {
		Object value = null;

		if (name.equalsIgnoreCase(ExcelEmitter.AUTO_FILTER)) {
			value = reportContent.getDesign().getReportDesign().getExcelAutoFilter();

		} else if (name.equalsIgnoreCase(ExcelEmitter.DISABLE_GROUPING)) {
			value = reportContent.getDesign().getReportDesign().getExcelDisableGrouping();

		} else if (name.equalsIgnoreCase(ExcelEmitter.DISPLAYGRIDLINES_PROP)) {
			value = reportContent.getDesign().getReportDesign().getExcelDisplayGridlines();

		} else if (name.equalsIgnoreCase(ExcelEmitter.FORCE_RECALCULATION)) {
			value = reportContent.getDesign().getReportDesign().getExcelForceRecalculation();

		} else if (name.equalsIgnoreCase(ExcelEmitter.FORCEAUTOCOLWIDTHS_PROP)) {
			value = reportContent.getDesign().getReportDesign().getExcelForceAutoColWidths();

		} else if (name.equalsIgnoreCase(ExcelEmitter.IMAGE_SCALING_CELL_DIMENSION)) {
			value = reportContent.getDesign().getReportDesign().getExcelImageScaling();

		} else if (name.equalsIgnoreCase(ExcelEmitter.SINGLE_SHEET)) {
			value = reportContent.getDesign().getReportDesign().getExcelSingleSheet();

		} else if (name.equalsIgnoreCase(ExcelEmitter.SINGLE_SHEET_PAGE_BREAKS)) {
			value = reportContent.getDesign().getReportDesign().getExcelSingleSheetPageBreak();

		} else if (name.equalsIgnoreCase(ExcelEmitter.STREAMING_XLSX)) {
			value = reportContent.getDesign().getReportDesign().getExcelStreamingXlsx();

		} else if (name.equalsIgnoreCase(ExcelEmitter.STRUCTURED_HEADER)) {
			value = reportContent.getDesign().getReportDesign().getExcelStructuredHeader();
		}
		return value;
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
	 * Get the symbolic name for the plugin.
	 *
	 * @return the symbolic name for the plugin.
	 */
	public static String getPluginName() {
		if ((ExcelEmitterPlugin.getDefault() != null) && (ExcelEmitterPlugin.getDefault().getBundle() != null)) {
			return ExcelEmitterPlugin.getDefault().getBundle().getSymbolicName();
		}
		return "uk.co.spudsoft.birt.emitters.excel";
	}

}
