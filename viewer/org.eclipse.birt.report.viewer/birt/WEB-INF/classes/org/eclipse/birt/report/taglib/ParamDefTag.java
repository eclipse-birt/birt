/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.ParameterDataTypeConverter;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.taglib.component.ParamDefField;
import org.eclipse.birt.report.taglib.component.ViewerField;
import org.eclipse.birt.report.taglib.util.BirtTagUtil;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * This tag is used to generate html code for report parameter.
 * 
 */
public class ParamDefTag extends BodyTagSupport {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1255870121526790060L;

	/**
	 * Parameter Definiation
	 */
	private ParamDefField param;

	/**
	 * Associated parameterPage tag object
	 */
	private RequesterTag requesterTag;

	/**
	 * Viewer supported attributes
	 */
	private ViewerField viewer;

	/**
	 * Current report parameter definition
	 */
	private ParameterDefinition paramDef;

	/**
	 * Input Options information
	 */
	private InputOptions options;

	/**
	 * Current isLocale setting
	 */
	private boolean isLocale = false;

	/**
	 * Current locale setting
	 */
	private Locale locale;

	/**
	 * Current time zone setting.
	 */
	private TimeZone timeZone;

	/**
	 * Current parameter format pattern
	 */
	private String pattern;

	/**
	 * value string
	 */
	private String valueString;

	/**
	 * value string list
	 */
	private List valueStringList;

	/**
	 * display text string
	 */
	private String displayTextString;

	/**
	 * parameter group object name
	 */
	private String groupObjName;

	/**
	 * Whether imported js/style files
	 */
	private static final String IMPORT_FILES_ATTR = "IMPORT_FILES_FLAG"; //$NON-NLS-1$
	private static final String ATTR_ID = "ID_"; //$NON-NLS-1$
	private static final String ATTR_PARAM = "PARAM_"; //$NON-NLS-1$

	/**
	 * Initialize pageContext
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#setPageContext(javax.servlet.jsp.PageContext)
	 */
	public void setPageContext(PageContext context) {
		super.setPageContext(context);
		param = new ParamDefField();
	}

	/**
	 * When reach the end tag, fire this operation
	 * 
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		try {
			if (__validate()) {
				// included in parameterpage tag
				this.requesterTag = (RequesterTag) TagSupport.findAncestorWithClass(this, RequesterTag.class);
				if (requesterTag != null) {
					this.viewer = requesterTag.viewer;
					if (this.viewer.isCustom()) {
						__beforeEndTag();
						__process();
					}
				}
			}
		} catch (Exception e) {
			__handleException(e);
		}
		return super.doEndTag();
	}

	/**
	 * validate the tag
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean __validate() throws Exception {
		if (!param.validate())
			return false;

		// validate parameter id if valid
		Pattern p = Pattern.compile("^\\w+$"); //$NON-NLS-1$
		Matcher m = p.matcher(param.getId());
		if (!m.find()) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_INVALID_ATTR_ID));
		}

		// validate parameter id if unique
		if (pageContext.findAttribute(ATTR_ID + param.getId()) != null) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_ATTR_ID_DUPLICATE));
		}

		// validate parameter name if unique
		if (this.requesterTag == null) {
			// the whole page scope
			if (pageContext.findAttribute(ATTR_PARAM + param.getName()) != null) {
				throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_PARAM_NAME_DUPLICATE,
						new String[] { param.getName() }));
			}
		} else {
			// the form scope
			if (this.requesterTag.getParameters().get(param.getName()) != null)
				throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_PARAM_NAME_DUPLICATE,
						new String[] { param.getName() }));
		}

		return true;
	}

	/**
	 * Handle event before doEndTag
	 */
	protected void __beforeEndTag() {
		// Save parameter id
		pageContext.setAttribute(ATTR_ID + param.getId(), param.getId());

		// Save parameter name
		if (this.requesterTag == null) {
			pageContext.setAttribute(ATTR_PARAM + param.getName(), param.getName());
		}
	}

	/**
	 * process tag function
	 * 
	 * @throws Exception
	 */
	protected void __process() throws Exception {
		if (viewer == null)
			return;

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		this.locale = BirtTagUtil.getLocale(request, viewer.getLocale());
		this.timeZone = BirtTagUtil.getTimeZone(request, viewer.getTimeZone());

		// Create Input Options
		this.options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, request);
		options.setOption(InputOptions.OPT_LOCALE, this.locale);
		options.setOption(InputOptions.OPT_TIMEZONE, this.timeZone);
		options.setOption(InputOptions.OPT_RTL, Boolean.valueOf(viewer.getRtl()));

		// get report parameter definition list
		Collection paramDefList = viewer.getParameterDefList();
		if (paramDefList == null) {
			// initialize engine context
			BirtReportServiceFactory.getReportService().setContext(pageContext.getServletContext(), options);

			// get report design handle
			IViewerReportDesignHandle designHandle = BirtTagUtil.getDesignHandle(request, viewer);
			viewer.setReportDesignHandle(designHandle);

			paramDefList = BirtReportServiceFactory.getReportService().getParameterDefinitions(designHandle, options,
					false);
			viewer.setParameterDefList(paramDefList);
		}

		// find current parameter definition object
		this.paramDef = BirtUtility.findParameterDefinition(paramDefList, param.getName());
		if (paramDef == null)
			return;

		// data type
		String dataType = ParameterDataTypeConverter.convertDataType(paramDef.getDataType());

		// pattern format
		this.pattern = param.getPattern();
		if (this.pattern == null)
			this.pattern = paramDef.getPattern();

		if ("true".equalsIgnoreCase(param.getIsLocale())) //$NON-NLS-1$
			this.isLocale = true;
		else
			this.isLocale = false;

		// handle parameter value
		if (param.getValue() != null) {
			if (param.getValue() instanceof String) {
				// convert parameter value to object
				Object valueObj = DataUtil.validateWithPattern(param.getName(), dataType, this.pattern,
						(String) param.getValue(), locale, timeZone, isLocale);
				if (this.paramDef.isMultiValue())
					param.setValue(new Object[] { valueObj });
				else
					param.setValue(valueObj);
			} else if (this.paramDef.isMultiValue() && param.getValue() instanceof String[]) {
				// handle multi-value parameter
				String[] sValues = (String[]) param.getValue();
				Object[] values = new Object[sValues.length];
				for (int i = 0; i < sValues.length; i++) {
					Object valueObj = DataUtil.validateWithPattern(param.getName(), dataType, this.pattern, sValues[i],
							locale, timeZone, isLocale);
					values[i] = valueObj;
				}
				param.setValue(values);
			}
		} else {
			Object defaultValue = BirtReportServiceFactory.getReportService()
					.getParameterDefaultValue(viewer.getReportDesignHandle(), param.getName(), options);
			if (this.paramDef.isMultiValue()) {
				if (!(defaultValue instanceof Object[])) {
					defaultValue = new Object[] { defaultValue };
				}
			}

			param.setValue(defaultValue);
		}

		// handle value string
		if (this.paramDef.isMultiValue()) {
			// handle multi-value parameter
			this.valueStringList = new ArrayList();
			Object[] values = (Object[]) param.getValue();
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					String value = DataUtil.getDisplayValue(values[i], timeZone);
					this.valueStringList.add(value);
				}
			}
		} else {
			this.valueString = DataUtil.getDisplayValue(param.getValue(), timeZone);
			if (this.valueString == null)
				this.valueString = ""; //$NON-NLS-1$
		}

		// handle parameter display text
		this.displayTextString = param.getDisplayText();
		if (this.displayTextString == null) {
			Object obj = param.getValue();
			if (obj != null) {
				if (obj instanceof Object[]) {
					Object[] objs = (Object[]) obj;
					if (objs.length > 0)
						obj = objs[0];
					else
						obj = null;
				}

				this.displayTextString = DataUtil.getDisplayValue(dataType, this.pattern, obj, locale, timeZone);
			}
		}
		if (this.displayTextString == null)
			this.displayTextString = ""; //$NON-NLS-1$

		// handle title
		if (param.getTitle() == null)
			param.setTitle(this.displayTextString);

		// cache parameter value
		requesterTag.addParameter(param.getName(), param.getValue());

		if (paramDef.isHidden()) {
			// handle hidden parameter
			__handleHidden();
		} else {
			// handle parameter section output
			switch (paramDef.getControlType()) {
			case IScalarParameterDefn.TEXT_BOX:
				__handleTextBox();
				break;
			case IScalarParameterDefn.LIST_BOX:
				__handleListBox();
				break;
			case IScalarParameterDefn.RADIO_BUTTON:
				__handleRadioButton();
				break;
			case IScalarParameterDefn.CHECK_BOX:
				__handleCheckBox();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Handle output hidden type parameter
	 * 
	 * @throws Exception
	 */
	protected void __handleHidden() throws Exception {
		JspWriter writer = pageContext.getOut();

		String encParamId = ParameterAccessor.htmlEncode(param.getId());
		String encParamName = ParameterAccessor.htmlEncode(param.getName());

		boolean isNullValue = param.getValue() == null;

		// parameter hidden value control
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + encParamId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		if (!isNullValue) {
			writer.write(" name=\"" + encParamName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.valueString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		writer.write(" >\n"); //$NON-NLS-1$

		// display text hidden object
		String displayTextId = encParamId + "_displayText"; //$NON-NLS-1$
		String displayTextName = ParameterAccessor.PREFIX_DISPLAY_TEXT + encParamName;
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + displayTextId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		if (!isNullValue) {
			writer.write(" name=\"" + displayTextName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.displayTextString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		writer.write(" >\n"); //$NON-NLS-1$
	}

	/**
	 * Handle output general definitions for a control
	 * 
	 * @throws Exception
	 */
	protected void __handleGeneralDefinition() throws Exception {
		JspWriter writer = pageContext.getOut();

		if (param.getTitle() != null)
			writer.write(" title=\"" + param.getTitle() + "\" "); //$NON-NLS-1$ //$NON-NLS-2$

		if (param.getCssClass() != null)
			writer.write(" class=\"" + param.getCssClass() + "\" "); //$NON-NLS-1$ //$NON-NLS-2$

		if (param.getStyle() != null)
			writer.write(" style=\"" + param.getStyle() + "\" "); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Handle output Text Box type parameter
	 * 
	 * @throws Exception
	 */
	protected void __handleTextBox() throws Exception {
		JspWriter writer = pageContext.getOut();

		String encParamId = ParameterAccessor.htmlEncode(param.getId());
		String encParamName = ParameterAccessor.htmlEncode(param.getName());

		boolean isNullValue = param.getValue() == null;

		// display text hidden object
		String displayTextId = encParamId + "_displayText"; //$NON-NLS-1$
		String displayTextName = ParameterAccessor.PREFIX_DISPLAY_TEXT + encParamName;
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + displayTextId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		if (paramDef.isRequired() || !isNullValue) {
			writer.write(" name=\"" + displayTextName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.displayTextString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		writer.write(" >\n"); //$NON-NLS-1$

		// parameter value hidden object
		String valueId = encParamId + "_value"; //$NON-NLS-1$
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + valueId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		writer.write(" name=\"" + encParamName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.valueString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write(" >\n"); //$NON-NLS-1$

		// isLocale hidden object
		String isLocaleId = encParamId + "_islocale"; //$NON-NLS-1$
		writer.write("<input type=\"hidden\" id=\"" + isLocaleId + "\" value=\"" //$NON-NLS-1$ //$NON-NLS-2$
				+ encParamName + "\" >\n"); //$NON-NLS-1$

		// set parameter pattern format
		String patternId = encParamId + "_pattern"; //$NON-NLS-1$
		String patternName = encParamName + "_format"; //$NON-NLS-1$
		if (param.getPattern() != null) {
			writer.write("<input type = 'hidden' id=\"" + patternId + "\" \n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(param.getPattern()) + "\">\n"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// onchange script
		writer.write("\n<script language=\"JavaScript\">\n"); //$NON-NLS-1$
		writer.write("function handleParam" + encParamId + "( )\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write("{\n"); //$NON-NLS-1$
		writer.write("var inputCtl = document.getElementById(\"" + encParamId //$NON-NLS-1$
				+ "\");\n"); //$NON-NLS-1$
		writer.write("var valCtl = document.getElementById(\"" + valueId //$NON-NLS-1$
				+ "\");\n"); //$NON-NLS-1$
		writer.write("var displayCtl = document.getElementById(\"" //$NON-NLS-1$
				+ displayTextId + "\");\n"); //$NON-NLS-1$
		writer.write("var localeCtl = document.getElementById(\"" //$NON-NLS-1$
				+ isLocaleId + "\");\n"); //$NON-NLS-1$
		writer.write("var patternCtl = document.getElementById(\"" //$NON-NLS-1$
				+ patternId + "\");\n"); //$NON-NLS-1$
		writer.write("displayCtl.value=inputCtl.value;\n"); //$NON-NLS-1$
		writer.write("valCtl.value=inputCtl.value;\n"); //$NON-NLS-1$
		writer.write("localeCtl.name='" + ParameterAccessor.PARAM_ISLOCALE + "';\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write("if( patternCtl ) patternCtl.name=\"" + patternName + "\";\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write("}\n"); //$NON-NLS-1$
		writer.write("</script>\n"); //$NON-NLS-1$

		String controlType = paramDef.concealValue() ? "PASSWORD" : "TEXT"; //$NON-NLS-1$ //$NON-NLS-2$
		if (paramDef.isRequired()) {
			writer.write("<input type=\"" + controlType + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" id=\"" + encParamId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
			__handleGeneralDefinition();
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.displayTextString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" onchange=\"handleParam" + encParamId + "( )\""); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" >\n"); //$NON-NLS-1$
		} else {
			String nullValueId = encParamId + "_null"; //$NON-NLS-1$
			String radioTextValueId = encParamId + "_radio_input"; //$NON-NLS-1$
			String radioNullValueId = encParamId + "_radio_null"; //$NON-NLS-1$

			// onclick script
			writer.write("\n<script language=\"JavaScript\">\n"); //$NON-NLS-1$
			writer.write("function switchParam" + encParamId + "( flag )\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("{\n"); //$NON-NLS-1$
			writer.write("var inputCtl = document.getElementById(\"" + encParamId //$NON-NLS-1$
					+ "\");\n"); //$NON-NLS-1$
			writer.write("var displayCtl = document.getElementById(\"" //$NON-NLS-1$
					+ displayTextId + "\");\n"); //$NON-NLS-1$
			writer.write("var nullCtl = document.getElementById(\"" + nullValueId //$NON-NLS-1$
					+ "\");\n"); //$NON-NLS-1$
			writer.write("var radioTextCtl = document.getElementById(\"" + radioTextValueId //$NON-NLS-1$
					+ "\");\n"); //$NON-NLS-1$
			writer.write("var radioNullCtl = document.getElementById(\"" + radioNullValueId //$NON-NLS-1$
					+ "\");\n"); //$NON-NLS-1$
			writer.write("if( flag ) \n"); //$NON-NLS-1$
			writer.write("{\n"); //$NON-NLS-1$
			writer.write("	radioTextCtl.checked=true;\n"); //$NON-NLS-1$
			writer.write("	radioNullCtl.checked=false;\n"); //$NON-NLS-1$
			writer.write("	inputCtl.disabled=false;\n"); //$NON-NLS-1$
			writer.write("	nullCtl.name='';\n"); //$NON-NLS-1$
			writer.write("	displayCtl.name='" + displayTextName + "';\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("}\n"); //$NON-NLS-1$
			writer.write("else\n"); //$NON-NLS-1$
			writer.write("{\n"); //$NON-NLS-1$
			writer.write("	radioTextCtl.checked=false;\n"); //$NON-NLS-1$
			writer.write("	radioNullCtl.checked=true;\n"); //$NON-NLS-1$
			writer.write("	inputCtl.disabled=true;\n"); //$NON-NLS-1$
			writer.write("	nullCtl.name='" + ParameterAccessor.PARAM_ISNULL + "';\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("	displayCtl.name='';\n"); //$NON-NLS-1$
			writer.write("}\n"); //$NON-NLS-1$
			writer.write("}\n"); //$NON-NLS-1$
			writer.write("</script>\n"); //$NON-NLS-1$

			// Null Value hidden object
			writer.write("<input type=\"hidden\" value=\"" //$NON-NLS-1$
					+ encParamName + "\" id=\"" + nullValueId + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			if (isNullValue)
				writer.write(" name=\"" //$NON-NLS-1$
						+ ParameterAccessor.PARAM_ISNULL + "\""); //$NON-NLS-1$
			writer.write(" >\n"); //$NON-NLS-1$

			writer.write("<input type=\"radio\" id=\"" + radioTextValueId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" onclick=\"switchParam" + encParamId + "( true )\""); //$NON-NLS-1$ //$NON-NLS-2$
			if (!isNullValue)
				writer.write(" checked "); //$NON-NLS-1$
			writer.write(" >\n"); //$NON-NLS-1$

			writer.write("<input type=\"" + controlType + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" id=\"" + encParamId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
			__handleGeneralDefinition();
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.displayTextString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" onchange=\"handleParam" + encParamId + "( )\""); //$NON-NLS-1$ //$NON-NLS-2$
			if (isNullValue)
				writer.write(" disabled = 'true' "); //$NON-NLS-1$
			writer.write(" >\n"); //$NON-NLS-1$

			writer.write("<input type=\"radio\" id=\"" + radioNullValueId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" onclick=\"switchParam" + encParamId + "( false )\""); //$NON-NLS-1$ //$NON-NLS-2$
			if (isNullValue)
				writer.write(" checked "); //$NON-NLS-1$
			writer.write(" >"); //$NON-NLS-1$
			writer.write("<label id=\"" + (radioNullValueId + "_label") + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			writer.write(" title=\"" + IBirtConstants.NULL_VALUE_DISPLAY + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" for=\"" + radioNullValueId + "\">"); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(IBirtConstants.NULL_VALUE_DISPLAY);
			writer.write("</label>"); //$NON-NLS-1$
			writer.write("</input>\n"); //$NON-NLS-1$
		}
	}

	/**
	 * Handle output List Box type parameter
	 * 
	 * @throws Exception
	 */
	protected void __handleListBox() throws Exception {
		if (paramDef.getGroup() != null && paramDef.getGroup().cascade()) {
			JspWriter writer = pageContext.getOut();

			// Only import necessary files once.
			if (pageContext.findAttribute(IMPORT_FILES_ATTR) == null) {
				String baseURL = "/webcontent/"; //$NON-NLS-1$
				if (viewer.getBaseURL() != null) {
					baseURL = viewer.getBaseURL() + baseURL;
				} else {
					baseURL = ((HttpServletRequest) pageContext.getRequest()).getContextPath() + baseURL;
				}

				// style files
				writer.write("\n<LINK REL=\"stylesheet\" HREF=\"" + baseURL //$NON-NLS-1$
						+ "birt/styles/style.css\" TYPE=\"text/css\">\n"); //$NON-NLS-1$

				// lib files
				BirtTagUtil.writeExtScripts(writer, baseURL + "birt/ajax/", //$NON-NLS-1$
						new String[] { "lib/prototype.js", //$NON-NLS-1$
								"lib/head.js", //$NON-NLS-1$
								"utility/Debug.js", //$NON-NLS-1$
								"utility/Constants.js", //$NON-NLS-1$
								"utility/BirtUtility.js", //$NON-NLS-1$
								"utility/BirtPosition.js", //$NON-NLS-1$
								"core/BirtSoapRequest.js", //$NON-NLS-1$
								"core/BirtEvent.js", //$NON-NLS-1$
								"taglib/CascadingParameter.js", //$NON-NLS-1$
								"taglib/ParameterGroup.js", //$NON-NLS-1$
								"taglib/ParameterDefinition.js", //$NON-NLS-1$
								"taglib/SoapResponseHelper.js", //$NON-NLS-1$
								"taglib/ProgressBar.js" }); //$NON-NLS-1$

				// create ProgressBar div
				this.__createProgressBar(baseURL);

				BirtTagUtil.writeScript(writer, "var progressBar = new ProgressBar( \"progressBar\",\"mask\" );" + //$NON-NLS-1$
						"Constants.nullValue = \"" + IBirtConstants.NULL_VALUE + "\";\n" //$NON-NLS-1$ //$NON-NLS-2$
				);

				pageContext.setAttribute(IMPORT_FILES_ATTR, Boolean.TRUE);
			}

			this.groupObjName = "group_" + this.viewer.getId() + "_" + paramDef.getGroup().getName();//$NON-NLS-1$ //$NON-NLS-2$
			if (pageContext.findAttribute(this.groupObjName) == null) {
				writer.write("<script  language=\"JavaScript\">var " + this.groupObjName //$NON-NLS-1$
						+ " = new ParameterGroup( );</script>\n"); //$NON-NLS-1$
				pageContext.setAttribute(this.groupObjName, Boolean.TRUE);
			}

			// get parameter list from cascading group
			Collection selectionList = getParameterSelectionListForCascadingGroup();
			__handleCommonListBox(selectionList);
			__handleCascadingListBox();
		} else {
			// get parameter list
			Collection selectionList = BirtReportServiceFactory.getReportService()
					.getParameterSelectionList(viewer.getReportDesignHandle(), options, param.getName());

			if (this.paramDef.isMultiValue())
				__handleMultiListBox(selectionList);
			else
				__handleCommonListBox(selectionList);
		}

	}

	/**
	 * Create Progress bar div
	 * 
	 * @param baseURL
	 * @throws Exception
	 */
	protected void __createProgressBar(String baseURL) throws Exception {
		JspWriter writer = pageContext.getOut();

		writer.write("<DIV ID=\"mask\" STYLE=\"display:none;position:absolute;z-index:200\">\n"); //$NON-NLS-1$
		writer.write("</DIV>\n"); //$NON-NLS-1$

		writer.write("<DIV ID=\"progressBar\" STYLE=\"display:none;position:absolute;z-index:300\">\n"); //$NON-NLS-1$
		writer.write("<TABLE WIDTH=\"250px\" CLASS=\"birtviewer_progressbar\" CELLSPACING=\"10px\">\n"); //$NON-NLS-1$
		writer.write("	<TR>\n"); //$NON-NLS-1$
		writer.write("		<TD ALIGN=\"center\">\n"); //$NON-NLS-1$
		writer.write("			<B>" //$NON-NLS-1$
				+ BirtResources.getMessage("birt.viewer.progressbar.prompt") //$NON-NLS-1$
				+ "</B>\n"); //$NON-NLS-1$
		writer.write("		</TD>\n"); //$NON-NLS-1$
		writer.write("	</TR>\n"); //$NON-NLS-1$
		writer.write("	<TR>\n"); //$NON-NLS-1$
		writer.write("		<TD ALIGN=\"center\">\n"); //$NON-NLS-1$
		writer.write("			<IMG SRC=\"" + baseURL //$NON-NLS-1$
				+ "birt/images/Loading.gif\" ALT=\"Progress Bar Image\"/>\n"); //$NON-NLS-1$
		writer.write("		</TD>\n"); //$NON-NLS-1$
		writer.write("	</TR>\n"); //$NON-NLS-1$
		writer.write("	<TR>\n"); //$NON-NLS-1$
		writer.write("		<TD ALIGN=\"center\">\n"); //$NON-NLS-1$
		writer.write("			<DIV ID=\"cancelTaskButton\" STYLE=\"display:block\">\n"); //$NON-NLS-1$
		writer.write("				<TABLE WIDTH=\"100%\">\n"); //$NON-NLS-1$
		writer.write("					<TR>\n"); //$NON-NLS-1$
		writer.write("						<TD ALIGN=\"center\">\n"); //$NON-NLS-1$
		writer.write("							<INPUT TYPE=\"BUTTON\" VALUE=\"" //$NON-NLS-1$
				+ BirtResources.getMessage("birt.viewer.dialog.cancel") //$NON-NLS-1$
				+ "\" \n"); //$NON-NLS-1$
		writer.write("									TITLE=\"" //$NON-NLS-1$
				+ BirtResources.getMessage("birt.viewer.dialog.cancel") //$NON-NLS-1$
				+ "\" \n"); //$NON-NLS-1$
		writer.write("									CLASS=\"birtviewer_progressbar_button\"/>\n"); //$NON-NLS-1$
		writer.write("						</TD>\n"); //$NON-NLS-1$
		writer.write("					</TR>\n"); //$NON-NLS-1$
		writer.write("				</TABLE>\n"); //$NON-NLS-1$
		writer.write("			</DIV>\n"); //$NON-NLS-1$
		writer.write("		</TD>\n"); //$NON-NLS-1$
		writer.write("	</TR>\n"); //$NON-NLS-1$
		writer.write("</TABLE>\n"); //$NON-NLS-1$
		writer.write("</DIV>\n"); //$NON-NLS-1$
		writer.write("<INPUT TYPE=\"HIDDEN\" ID=\"taskid\" VALUE=''/>\n"); //$NON-NLS-1$
	}

	/**
	 * Handle Multi-value List Box type parameter
	 * 
	 * @param selectionList
	 * 
	 * @throws Exception
	 */
	protected void __handleMultiListBox(Collection selectionList) throws Exception {
		JspWriter writer = pageContext.getOut();

		String encParamId = ParameterAccessor.htmlEncode(param.getId());
		String encParamName = ParameterAccessor.htmlEncode(param.getName());

		String containerId = encParamId + "_container"; //$NON-NLS-1$
		String displayTextName = ParameterAccessor.PREFIX_DISPLAY_TEXT + encParamName;

		// function for handling select onchange
		String content = "function handleParam" + encParamId + "( oCtl )\n" + //$NON-NLS-1$ //$NON-NLS-2$
				"{\n" + //$NON-NLS-1$
				"  if( !oCtl ) return;\n" + //$NON-NLS-1$
				"  var container = document.getElementById(\"" + containerId + "\");\n" + //$NON-NLS-1$ //$NON-NLS-2$
				" while( container.childNodes.length > 0)\n" + //$NON-NLS-1$
				"{\n" + //$NON-NLS-1$
				"  container.removeChild(container.firstChild);\n" + //$NON-NLS-1$
				"}\n" + //$NON-NLS-1$
				"\n" + //$NON-NLS-1$
				"  var options = oCtl.options;\n" + //$NON-NLS-1$
				"  for( var i = 0; i < options.length; i++ )\n" + //$NON-NLS-1$
				"  {\n" + //$NON-NLS-1$
				"    if( !options[i].selected ) continue;\n" + //$NON-NLS-1$
				"\n" + //$NON-NLS-1$
				"    var text = options[i].text;\n" + //$NON-NLS-1$
				"    var value = options[i].value;\n" + //$NON-NLS-1$

				// null value
				"\n" + //$NON-NLS-1$
				"  if( value == '" + IBirtConstants.NULL_VALUE + "')\n" + //$NON-NLS-1$ //$NON-NLS-2$
				"    {\n" + //$NON-NLS-1$
				"      var oInput = document.createElement( 'input' );\n" + //$NON-NLS-1$
				"      oInput.type = 'hidden';\n" + //$NON-NLS-1$
				"      oInput.name = '" + ParameterAccessor.PARAM_ISNULL + "';\n" + //$NON-NLS-1$ //$NON-NLS-2$
				"      oInput.value = \"" + encParamName + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$
				"      container.appendChild( oInput );\n" + //$NON-NLS-1$
				"    }\n" + //$NON-NLS-1$

				// parameter value
				"\n" + //$NON-NLS-1$
				"    var oInput = document.createElement( 'input' );\n" + //$NON-NLS-1$
				"    oInput.type = 'hidden';\n" + //$NON-NLS-1$
				"    oInput.name = \"" + encParamName + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$
				"    oInput.value = value;\n" + //$NON-NLS-1$
				"    container.appendChild( oInput );\n" + //$NON-NLS-1$

				// display text
				"\n" + //$NON-NLS-1$
				"    var oInput = document.createElement( 'input' );\n" + //$NON-NLS-1$
				"    oInput.type = 'hidden';\n" + //$NON-NLS-1$
				"    oInput.name = \"" + displayTextName + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$
				"    oInput.value = text;\n" + //$NON-NLS-1$
				"    container.appendChild( oInput );\n" + //$NON-NLS-1$
				"  }\n"; //$NON-NLS-1$

		// isLocale
		if (this.isLocale) {
			content += "\n" + //$NON-NLS-1$
					"  var oInput = document.createElement( 'input' );\n" + //$NON-NLS-1$
					"  oInput.type = 'hidden';\n" + //$NON-NLS-1$
					"  oInput.name = \"" + ParameterAccessor.PARAM_ISLOCALE + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$
					"  oInput.value = \"" + encParamName + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$
					"  container.appendChild( oInput );\n"; //$NON-NLS-1$
		}

		content += "}\n"; //$NON-NLS-1$
		BirtTagUtil.writeScript(writer, content);

		String onChange = "handleParam" + encParamId + "( this )"; //$NON-NLS-1$ //$NON-NLS-2$

		// parameter container
		writer.write("<div id=\"" + containerId + "\" style=\"display:none;\"></div>"); //$NON-NLS-1$ //$NON-NLS-2$

		// select control
		writer.write("<select "); //$NON-NLS-1$
		writer.write(" id=\"" + encParamId + "\""); //$NON-NLS-1$//$NON-NLS-2$
		__handleGeneralDefinition();
		writer.write(" onchange=\"" + onChange + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write(" multiple='true'"); //$NON-NLS-1$
		writer.write(" >\n"); //$NON-NLS-1$

		makeOption(writer, selectionList, this.valueStringList);

		writer.write("</select>\n"); //$NON-NLS-1$

		BirtTagUtil.writeScript(writer, "var selectCtl = document.getElementById(\"" //$NON-NLS-1$
				+ encParamId + "\");\n" + //$NON-NLS-1$
				"if( selectCtl.options.length > 8 )\n" + //$NON-NLS-1$
				"  selectCtl.size = 8;\n" + //$NON-NLS-1$
				"else\n" + //$NON-NLS-1$
				"  selectCtl.size = selectCtl.options.length;\n" + //$NON-NLS-1$
				"handleParam" + encParamId + "( selectCtl );\n" //$NON-NLS-1$ //$NON-NLS-2$
		);
	}

	/**
	 * @param writer
	 * @param items
	 * @throws IOException
	 */
	private void makeOption(JspWriter writer, Collection items, List selectedItems) throws IOException {
		boolean nullValueFound = false;
		// blank item
		if (!paramDef.isRequired()) {
			BirtTagUtil.writeOption(writer, "", "", DataUtil.contain( //$NON-NLS-1$//$NON-NLS-2$
					this.valueStringList, "", true)); //$NON-NLS-1$
		}

		// selection list
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			ParameterSelectionChoice selectionItem = (ParameterSelectionChoice) iter.next();

			Object value = selectionItem.getValue();
			try {
				// try convert value to parameter definition data type
				value = DataUtil.convert(value, paramDef.getDataType());
			} catch (Exception e) {
				value = null;
			}

			// Convert parameter value using standard format
			String displayValue = DataUtil.getDisplayValue(value, timeZone);
			if (value == null) {
				nullValueFound = true;
			}

			// If label is null or blank, then use the format parameter
			// value for display
			String label = selectionItem.getLabel();
			if (label == null || label.length() <= 0)
				label = DataUtil.getDisplayValue(null, this.pattern, value, this.locale, this.timeZone);

			label = label != null ? label : ""; //$NON-NLS-1$

			BirtTagUtil.writeOption(writer, label, (displayValue == null) ? IBirtConstants.NULL_VALUE : displayValue,
					DataUtil.contain(selectedItems, displayValue, true));
		}

		// null value item
		if (!paramDef.isRequired() && !nullValueFound) {
			BirtTagUtil.writeOption(writer, IBirtConstants.NULL_VALUE_DISPLAY, IBirtConstants.NULL_VALUE,
					DataUtil.contain(selectedItems, null, true));
		}
	}

	/**
	 * Handle Common List/Combo Box type parameter( not cascading parameter )
	 * 
	 * @param selectionList
	 * 
	 * @throws Exception
	 */
	protected void __handleCommonListBox(Collection selectionList) throws Exception {
		JspWriter writer = pageContext.getOut();

		String encParamId = ParameterAccessor.htmlEncode(param.getId());
		String encParamName = ParameterAccessor.htmlEncode(param.getName());

		String displayTextId = encParamId + "_displayText"; //$NON-NLS-1$
		String displayTextName = ParameterAccessor.PREFIX_DISPLAY_TEXT + encParamName;

		boolean isSelected = false;
		boolean isNullValue = param.getValue() == null;
		String valueId = encParamId + "_value"; //$NON-NLS-1$
		String nullValueId = encParamId + "_null"; //$NON-NLS-1$

		String radioSelectId = encParamId + "_radio_select"; //$NON-NLS-1$
		String radioTextId = encParamId + "_radio_input"; //$NON-NLS-1$
		String inputTextId = encParamId + "_input"; //$NON-NLS-1$

		String isLocaleId = encParamId + "_islocale"; //$NON-NLS-1$
		String patternId = encParamId + "_pattern"; //$NON-NLS-1$
		String patternName = encParamName + "_format"; //$NON-NLS-1$

		if (!paramDef.mustMatch()) {
			BirtTagUtil.writeScript(writer,
					// function for updating controls status
					"function updateParam" + encParamId + "( flag )\n" + //$NON-NLS-1$ //$NON-NLS-2$
							"{\n" + //$NON-NLS-1$
							"var radioSelectCtl = document.getElementById(\"" + radioSelectId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( radioSelectCtl ) radioSelectCtl.checked = flag;\n" + //$NON-NLS-1$
							"var radioTextCtl = document.getElementById(\"" + radioTextId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( radioTextCtl ) radioTextCtl.checked = !flag;\n" + //$NON-NLS-1$
							"var selectCtl = document.getElementById(\"" + encParamId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( selectCtl ) selectCtl.disabled = !flag;\n" + //$NON-NLS-1$
							"var inputCtl = document.getElementById(\"" + inputTextId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( inputCtl ) inputCtl.disabled = flag;\n" + //$NON-NLS-1$

							// If input parameter in text field,enable locale control
							"var localeCtl = document.getElementById(\"" + isLocaleId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( localeCtl )\n" + //$NON-NLS-1$
							"{\n" + //$NON-NLS-1$
							"  if( flag )\n" + //$NON-NLS-1$
							"    localeCtl.name = '';\n" + //$NON-NLS-1$
							"  else\n" + //$NON-NLS-1$
							"    localeCtl.name = \"" //$NON-NLS-1$
							+ ParameterAccessor.PARAM_ISLOCALE + "\";\n" + //$NON-NLS-1$
							"}\n" + //$NON-NLS-1$

							"if( flag )\n" + //$NON-NLS-1$
							"{\n" + //$NON-NLS-1$
							"  if( selectCtl.selectedIndex >= 0 )\n" + //$NON-NLS-1$
							"    handleParam" + encParamId + "( selectCtl.options[selectCtl.selectedIndex] );\n" + //$NON-NLS-1$ //$NON-NLS-2$
							"  else\n" + //$NON-NLS-1$
							"  {\n" + //$NON-NLS-1$
							"    var nullCtl = document.getElementById(\"" + nullValueId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"    if( nullCtl ) nullCtl.name=\"" + ParameterAccessor.PARAM_ISNULL //$NON-NLS-1$
							+ "\";\n" + //$NON-NLS-1$
							"    var valCtl = document.getElementById(\"" + valueId //$NON-NLS-1$
							+ "\");\n" + //$NON-NLS-1$
							"    if( valCtl ) valCtl.name = '';\n" + //$NON-NLS-1$
							"    if( valCtl ) valCtl.value = '';\n" + //$NON-NLS-1$
							"    var displayCtl = document.getElementById(\"" //$NON-NLS-1$
							+ displayTextId + "\");\n" + //$NON-NLS-1$
							"    if( displayCtl ) displayCtl.value = '';\n" + //$NON-NLS-1$
							"    if( displayCtl ) displayCtl.name = '';\n" + //$NON-NLS-1$
							"  }\n" + //$NON-NLS-1$
							"}\n" + //$NON-NLS-1$
							"else\n" + //$NON-NLS-1$
							"{\n" + //$NON-NLS-1$
							"  handleTextParam" + encParamId + "( );\n" + //$NON-NLS-1$ //$NON-NLS-2$
							"}\n" + //$NON-NLS-1$

							"}\n" + //$NON-NLS-1$

							// function for handling text input
							"function handleTextParam" + encParamId + "( )\n" + //$NON-NLS-1$ //$NON-NLS-2$
							"{\n" + //$NON-NLS-1$
							"var inputCtl = document.getElementById(\"" + inputTextId //$NON-NLS-1$
							+ "\");\n" + //$NON-NLS-1$

							"var valCtl = document.getElementById(\"" + valueId //$NON-NLS-1$
							+ "\");\n" + //$NON-NLS-1$
							"if( valCtl ) valCtl.name = \"" + encParamName + "\";\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( valCtl ) valCtl.value = inputCtl.value;\n" + //$NON-NLS-1$

							"var displayCtl = document.getElementById(\"" //$NON-NLS-1$
							+ displayTextId + "\");\n" + //$NON-NLS-1$
							"if( displayCtl ) displayCtl.name = \"" + displayTextName + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$
							"if( displayCtl ) displayCtl.value = inputCtl.value;\n" + //$NON-NLS-1$

							"var nullCtl = document.getElementById(\"" + nullValueId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( nullCtl ) nullCtl.name='';\n" + //$NON-NLS-1$

							"var localeCtl = document.getElementById(\"" + isLocaleId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( localeCtl ) localeCtl.name = \"" + ParameterAccessor.PARAM_ISLOCALE + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$

							"}\n" + //$NON-NLS-1$

							"function changeTextParam" + encParamId + "( )\n" + //$NON-NLS-1$ //$NON-NLS-2$
							"{\n" + //$NON-NLS-1$
							"var patternCtl = document.getElementById(\"" + patternId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
							"if( patternCtl ) patternCtl.name = \"" + patternName + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$
							"  handleTextParam" + encParamId + "( );\n" + //$NON-NLS-1$ //$NON-NLS-2$
							"}\n"); //$NON-NLS-1$
		}

		BirtTagUtil.writeScript(writer,
				// onchange script
				"function handleParam" + encParamId + "( option )\n" + //$NON-NLS-1$ //$NON-NLS-2$
						"{\n" + //$NON-NLS-1$
						"if( !option ) return;\n" + //$NON-NLS-1$

						"var valCtl = document.getElementById(\"" + valueId //$NON-NLS-1$
						+ "\");\n" + //$NON-NLS-1$
						"var displayCtl = document.getElementById(\"" //$NON-NLS-1$
						+ displayTextId + "\");\n" + //$NON-NLS-1$
						"var nullCtl = document.getElementById(\"" + nullValueId + "\");\n" + //$NON-NLS-1$//$NON-NLS-2$
						"var label = option.text;\n" + //$NON-NLS-1$
						"var value = option.value;\n" + //$NON-NLS-1$
						"if( value == \"" + IBirtConstants.NULL_VALUE + "\")\n" + //$NON-NLS-1$//$NON-NLS-2$
						"{\n" + //$NON-NLS-1$
						"  if( nullCtl ) nullCtl.name=\"" + ParameterAccessor.PARAM_ISNULL //$NON-NLS-1$
						+ "\";\n" + //$NON-NLS-1$
						"  if( valCtl ) valCtl.name = '';\n" + //$NON-NLS-1$
						"  if( valCtl ) valCtl.value = '';\n" + //$NON-NLS-1$
						"  if( displayCtl ) displayCtl.value = '';\n" + //$NON-NLS-1$
						"  if( displayCtl ) displayCtl.name = '';\n" + //$NON-NLS-1$
						"}\n" + //$NON-NLS-1$
						"else\n" + //$NON-NLS-1$
						"{\n" + //$NON-NLS-1$
						"  if( nullCtl ) nullCtl.name='';\n" + //$NON-NLS-1$
						"  if( valCtl ) valCtl.name = \"" + encParamName + "\";\n" + //$NON-NLS-1$//$NON-NLS-2$
						"  if( valCtl ) valCtl.value = value;\n" + //$NON-NLS-1$
						"  if( displayCtl ) displayCtl.name = \"" + displayTextName + "\";\n" + //$NON-NLS-1$ //$NON-NLS-2$
						"  if( displayCtl ) displayCtl.value = label;\n" + //$NON-NLS-1$
						"}\n" + //$NON-NLS-1$
						"}\n"//$NON-NLS-1$
		);

		String onChange = "handleParam" + encParamId + "( this.options[this.selectedIndex] )"; //$NON-NLS-1$ //$NON-NLS-2$
		if (!paramDef.mustMatch()) {
			String onClick = "updateParam" + encParamId + "( true )"; //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("<input type=\"radio\" "); //$NON-NLS-1$
			writer.write(" id=\"" + radioSelectId + "\""); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" onclick=\"" + onClick + "\""); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" >\n"); //$NON-NLS-1$
		}

		// select control
		writer.write("<select "); //$NON-NLS-1$
		writer.write(" id=\"" + encParamId + "\""); //$NON-NLS-1$//$NON-NLS-2$
		__handleGeneralDefinition();
		writer.write(" onchange=\"" + onChange + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write(" >\n"); //$NON-NLS-1$

		// blank item
		if (!paramDef.isRequired()) {
			if (param.getValue() != null && DataUtil.getString(param.getValue()).length() <= 0) {
				isSelected = true;
			}
			BirtTagUtil.writeOption(writer, "", "", isSelected);
		}

		boolean nullValueFound = false;
		for (Iterator iter = selectionList.iterator(); iter.hasNext();) {
			ParameterSelectionChoice selectionItem = (ParameterSelectionChoice) iter.next();

			Object value = selectionItem.getValue();
			try {
				// try convert value to parameter definition data type
				value = DataUtil.convert(value, paramDef.getDataType());
			} catch (Exception e) {
				value = null;
			}

			// Convert parameter value using standard format
			String displayValue = DataUtil.getDisplayValue(value, timeZone);

			// If label is null or blank, then use the format parameter
			// value for display
			String label = selectionItem.getLabel();
			if (label == null || label.length() <= 0)
				label = DataUtil.getDisplayValue(null, this.pattern, value, this.locale, this.timeZone);

			if (value == null) {
				nullValueFound = true;
				if (label == null) {
					label = IBirtConstants.NULL_VALUE_DISPLAY;
				}
			}

			label = label != null ? label : ""; //$NON-NLS-1$
			boolean selected = false;
			if (DataUtil.equals(displayValue, DataUtil.getDisplayValue(param.getValue(), timeZone))) {
				selected = true;
				isSelected = true;
				writer.write(" selected"); //$NON-NLS-1$
				if (param.getDisplayText() == null) {
					this.displayTextString = label;
				} else {
					label = param.getDisplayText();
				}
			}

			BirtTagUtil.writeOption(writer, label, (displayValue == null) ? IBirtConstants.NULL_VALUE : displayValue,
					selected);
		}

		String defaultValueText = null;
		if (!isSelected) {
			Object defaultValue = BirtReportServiceFactory.getReportService()
					.getParameterDefaultValue(viewer.getReportDesignHandle(), param.getName(), options);
			if (defaultValue == null) {
				isNullValue = true;
			} else {
				isNullValue = false;
				defaultValueText = DataUtil.getDisplayValue(defaultValue, timeZone);
				if (this.valueString.equalsIgnoreCase(defaultValueText) || paramDef.mustMatch()) {
					if (defaultValueText != null)
						this.valueString = defaultValueText;

					String defaultDisplayText = DataUtil.getDisplayValue(null, this.pattern, defaultValue, locale,
							this.timeZone);
					if (defaultDisplayText != null)
						this.displayTextString = defaultDisplayText;

					BirtTagUtil.writeOption(writer, this.displayTextString, this.valueString, true);
					isSelected = true;
				}
			}
		}

		// null value item
		if (!paramDef.isRequired() && !nullValueFound) {
			BirtTagUtil.writeOption(writer, IBirtConstants.NULL_VALUE_DISPLAY, IBirtConstants.NULL_VALUE, isNullValue);
			isSelected = true;
		}

		writer.write("</select>\n"); //$NON-NLS-1$

		if (!paramDef.mustMatch()) {
			// isLocale hidden object
			writer.write("<input type = 'hidden' "); //$NON-NLS-1$
			writer.write(" id=\"" + isLocaleId + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + encParamName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" >\n"); //$NON-NLS-1$

			// set parameter pattern format
			if (param.getPattern() != null) {
				writer.write("<input type = 'hidden' id=\"" + patternId + "\""); //$NON-NLS-1$ //$NON-NLS-2$
				writer.write(" value=\"" + ParameterAccessor.htmlEncode(param.getPattern()) + "\">\n"); //$NON-NLS-1$//$NON-NLS-2$
			}

			String onClick = "updateParam" + encParamId + "( false );"; //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("<input type=\"radio\" "); //$NON-NLS-1$
			writer.write(" id=\"" + radioTextId + "\""); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" onclick=\"" + onClick + "\""); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" >\n"); //$NON-NLS-1$

			writer.write("<input type=\"text\" "); //$NON-NLS-1$
			writer.write(" id=\"" + inputTextId + "\""); //$NON-NLS-1$//$NON-NLS-2$
			if (!isSelected) {
				writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.displayTextString) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			}
			writer.write(" onchange=\"changeTextParam" + encParamId + "( )\""); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" >\n"); //$NON-NLS-1$

			// initialize controls
			writer.write(
					"<script language=\"JavaScript\">updateParam" + encParamId + "(" + isSelected + ");</script>\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		// display text hidden object
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + displayTextId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		if (!isNullValue) {
			writer.write(" name=\"" + displayTextName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.displayTextString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$

		}
		writer.write(" >\n"); //$NON-NLS-1$

		// parameter value hidden object
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + valueId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		if (!isNullValue) {
			writer.write(" name=\"" + encParamName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.valueString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		writer.write(" >\n"); //$NON-NLS-1$

		// Null Value hidden object
		if (!paramDef.isRequired()) {
			writer.write("<input type=\"hidden\" value=\"" //$NON-NLS-1$
					+ encParamName + "\" id=\"" + nullValueId + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			if (isNullValue)
				writer.write(" name=\"" //$NON-NLS-1$
						+ ParameterAccessor.PARAM_ISNULL + "\""); //$NON-NLS-1$
			writer.write(" >\n"); //$NON-NLS-1$
		}

		if (!isSelected && paramDef.mustMatch()) {
			writer.write("\n<script language=\"JavaScript\">\n"); //$NON-NLS-1$
			writer.write("var selectCtl = document.getElementById(\"" //$NON-NLS-1$
					+ encParamId + "\");\n"); //$NON-NLS-1$
			writer.write("if( selectCtl.selectedIndex >= 0 )\n"); //$NON-NLS-1$
			writer.write("{\n"); //$NON-NLS-1$
			if (defaultValueText != null) {
				writer.write("  selectCtl.value = \"" + defaultValueText //$NON-NLS-1$
						+ "\";\n"); //$NON-NLS-1$
			}
			writer.write("  handleParam" + encParamId + "( selectCtl.options[selectCtl.selectedIndex] );\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("}\n"); //$NON-NLS-1$
			writer.write("</script>\n"); //$NON-NLS-1$
		}
	}

	/**
	 * Handle Cascading List Box type parameter
	 * 
	 * @throws Exception
	 */
	protected void __handleCascadingListBox() throws Exception {
		String encParamId = ParameterAccessor.htmlEncode(param.getId());
		String encParamName = ParameterAccessor.htmlEncode(param.getName());
		String inputTextId = encParamId + "_input"; //$NON-NLS-1$

		JspWriter writer = pageContext.getOut();

		BirtTagUtil.writeScript(writer,
				"var param = new ParameterDefinition(\"" + encParamId + "\",\"" + encParamName + "\");\n" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"param.setRequired(" + paramDef.isRequired() + ");\n" + //$NON-NLS-1$ //$NON-NLS-2$
						this.groupObjName + ".addParameter( param );\n" //$NON-NLS-1$
		);
		ParameterGroupDefinition group = (ParameterGroupDefinition) paramDef.getGroup();
		int index = group.getParameters().indexOf(paramDef);

		// if it is the last cascading parameter, return
		if (index == group.getParameterCount() - 1)
			return;

		String casObj = "cas" + encParamId; //$NON-NLS-1$
		String namesObj = "names_" + encParamId; //$NON-NLS-1$
		writer.write("\n<script language=\"JavaScript\">\n"); //$NON-NLS-1$
		writer.write("var " + namesObj + " = new Array( " + (index + 2) + " );\n"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		for (int i = 0; i < index + 2; i++) {
			ParameterDefinition param = (ParameterDefinition) group.getParameters().get(i);
			writer.write(namesObj + "[" + i + "] = \"" //$NON-NLS-1$ //$NON-NLS-2$
					+ ParameterAccessor.htmlEncode(param.getName()) + "\";\n"); //$NON-NLS-1$
		}
		writer.write("var " + casObj + " = new CascadingParameter( \"" + this.viewer.getId() + "\", param, " + namesObj //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ", " + this.groupObjName + " );\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write("var selectCtl = document.getElementById(\"" + encParamId + "\");\n"); //$NON-NLS-1$//$NON-NLS-2$
		writer.write("selectCtl.onchange = function( ) { \n"); //$NON-NLS-1$
		writer.write("var selectCtl = document.getElementById(\"" + encParamId //$NON-NLS-1$
				+ "\");\n"); //$NON-NLS-1$
		writer.write("handleParam" + encParamId //$NON-NLS-1$
				+ "( selectCtl.options[selectCtl.selectedIndex] );\n"); //$NON-NLS-1$
		writer.write("progressBar.setHandler(" + casObj + ");\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write(casObj + ".process( ); };\n"); //$NON-NLS-1$
		writer.write("var inputCtl = document.getElementById(\"" + inputTextId + "\");\n"); //$NON-NLS-1$//$NON-NLS-2$
		writer.write("if( inputCtl )\n"); //$NON-NLS-1$
		writer.write("{\n"); //$NON-NLS-1$
		writer.write("inputCtl.onchange = function( ) { \n"); //$NON-NLS-1$
		writer.write("handleTextParam" + encParamId + "( );\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write("progressBar.setHandler(" + casObj + ");\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write(casObj + ".process( ); };\n"); //$NON-NLS-1$
		writer.write("}\n"); //$NON-NLS-1$
		writer.write("</script>\n"); //$NON-NLS-1$
	}

	/**
	 * Get parameter selection list from cascading parameter
	 * 
	 * @return
	 * @throws ReportServiceException
	 */
	private Collection getParameterSelectionListForCascadingGroup() throws ReportServiceException {

		ParameterGroupDefinition group = (ParameterGroupDefinition) paramDef.getGroup();
		int index = group.getParameters().indexOf(paramDef);
		Object[] groupKeys = new Object[index];
		for (int i = 0; i < index; i++) {
			ParameterDefinition def = (ParameterDefinition) group.getParameters().get(i);
			String parameterName = def.getName();
			groupKeys[i] = requesterTag.getParameters().get(parameterName);
		}
		return BirtReportServiceFactory.getReportService()
				.getSelectionListForCascadingGroup(viewer.getReportDesignHandle(), group.getName(), groupKeys, options);
	}

	/**
	 * Handle output Radio Button type parameter
	 * 
	 * @throws Exception
	 */
	protected void __handleRadioButton() throws Exception {
		Collection selectionList = BirtReportServiceFactory.getReportService()
				.getParameterSelectionList(viewer.getReportDesignHandle(), this.options, param.getName());
		if (selectionList == null || selectionList.size() <= 0)
			return;

		JspWriter writer = pageContext.getOut();

		String encParamId = ParameterAccessor.htmlEncode(param.getId());
		String encParamName = ParameterAccessor.htmlEncode(param.getName());

		String displayTextId = encParamId + "_displayText"; //$NON-NLS-1$
		String displayTextName = ParameterAccessor.PREFIX_DISPLAY_TEXT + encParamName;

		String nullValueId = encParamId + "_null"; //$NON-NLS-1$
		String radioNullValueId = encParamId + "_radio_null"; //$NON-NLS-1$

		String radioName = encParamId + "_radio"; //$NON-NLS-1$
		String valueId = encParamId + "_value"; //$NON-NLS-1$
		boolean isChecked = false;

		// onclick script
		writer.write("\n<script language=\"JavaScript\">\n"); //$NON-NLS-1$
		writer.write("function handleParam" + encParamId + "( e )\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write("{\n"); //$NON-NLS-1$
		writer.write("var obj;\n"); //$NON-NLS-1$
		writer.write("if( window.event )\n"); //$NON-NLS-1$
		writer.write("{\n"); //$NON-NLS-1$
		writer.write("  obj = window.event.srcElement;\n"); //$NON-NLS-1$
		writer.write("}\n"); //$NON-NLS-1$
		writer.write("else\n"); //$NON-NLS-1$
		writer.write("{\n"); //$NON-NLS-1$
		writer.write("  if( e ) obj = e.target;\n"); //$NON-NLS-1$
		writer.write("}\n"); //$NON-NLS-1$
		writer.write("if( !obj ) return;\n"); //$NON-NLS-1$

		writer.write("var valCtl = document.getElementById(\"" + valueId //$NON-NLS-1$
				+ "\");\n"); //$NON-NLS-1$
		writer.write("var displayCtl = document.getElementById(\"" //$NON-NLS-1$
				+ displayTextId + "\");\n"); //$NON-NLS-1$
		writer.write("var nullCtl = document.getElementById(\"" + nullValueId + "\");\n"); //$NON-NLS-1$//$NON-NLS-2$
		writer.write("if( obj.id == \"" + radioNullValueId + "\")\n"); //$NON-NLS-1$//$NON-NLS-2$
		writer.write("{\n"); //$NON-NLS-1$
		writer.write("  if( nullCtl ) nullCtl.name=\"" + ParameterAccessor.PARAM_ISNULL //$NON-NLS-1$
				+ "\";\n"); //$NON-NLS-1$
		writer.write("  valCtl.name = '';\n"); //$NON-NLS-1$
		writer.write("  valCtl.value = '';\n"); //$NON-NLS-1$
		writer.write("  displayCtl.value = '';\n"); //$NON-NLS-1$
		writer.write("  displayCtl.name = '';\n"); //$NON-NLS-1$
		writer.write("}\n"); //$NON-NLS-1$
		writer.write("else\n"); //$NON-NLS-1$
		writer.write("{\n"); //$NON-NLS-1$
		writer.write("  if( nullCtl ) nullCtl.name='';\n"); //$NON-NLS-1$
		writer.write("  valCtl.name = \"" + encParamName + "\";\n"); //$NON-NLS-1$//$NON-NLS-2$
		writer.write("  valCtl.value = obj.value;\n"); //$NON-NLS-1$
		writer.write("  var labelCtl = document.getElementById( obj.id + \"_label\");\n"); //$NON-NLS-1$
		writer.write("  displayCtl.value = labelCtl.innerHTML;\n"); //$NON-NLS-1$
		writer.write("  displayCtl.name = \"" + displayTextName + "\";\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write("}\n"); //$NON-NLS-1$

		writer.write("}\n"); //$NON-NLS-1$
		writer.write("</script>\n"); //$NON-NLS-1$

		String onClick = "handleParam" + encParamId + "( event )"; //$NON-NLS-1$ //$NON-NLS-2$

		int index = 0;
		for (Iterator iter = selectionList.iterator(); iter.hasNext();) {
			ParameterSelectionChoice selectionItem = (ParameterSelectionChoice) iter.next();

			Object value = selectionItem.getValue();
			try {
				// try convert value to parameter definition data type
				value = DataUtil.convert(value, paramDef.getDataType());
			} catch (Exception e) {
				value = null;
			}

			// Convert parameter value using standard format
			String displayValue = DataUtil.getDisplayValue(value, timeZone);
			if (displayValue == null)
				continue;

			// If label is null or blank, then use the format parameter
			// value for display
			String label = selectionItem.getLabel();
			if (label == null || label.length() <= 0)
				label = DataUtil.getDisplayValue(null, this.pattern, value, this.locale, timeZone);

			label = label != null ? ParameterAccessor.htmlEncode(label) : ""; //$NON-NLS-1$
			String ctlId = encParamId + "_" //$NON-NLS-1$
					+ index;

			writer.write("<input type=\"radio\" "); //$NON-NLS-1$
			writer.write(" name=\"" + radioName + "\""); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" id=\"" + ctlId + "\""); //$NON-NLS-1$//$NON-NLS-2$
			__handleGeneralDefinition();
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(displayValue) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" onclick=\"" + onClick + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			if (displayValue.equalsIgnoreCase(DataUtil.getDisplayValue(param.getValue(), timeZone))) {
				isChecked = true;
				writer.write(" checked"); //$NON-NLS-1$
				if (param.getDisplayText() == null) {
					this.displayTextString = label;
				} else {
					label = param.getDisplayText();
				}
			}
			writer.write(" >"); //$NON-NLS-1$
			writer.write("<label id=\"" + (ctlId + "_label") + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			writer.write(" title=\"" + label + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" for=\"" + ctlId + "\">"); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(label);
			writer.write("</label>"); //$NON-NLS-1$
			writer.write("</input>\n"); //$NON-NLS-1$

			index++;
		}

		// allow Null value
		if (!paramDef.isRequired()) {
			boolean isNullValue = param.getValue() == null;

			// Null Value hidden object
			writer.write("<input type=\"hidden\" value=\"" //$NON-NLS-1$
					+ encParamName + "\" id=\"" + nullValueId + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			if (isNullValue)
				writer.write(" name=\"" //$NON-NLS-1$
						+ ParameterAccessor.PARAM_ISNULL + "\""); //$NON-NLS-1$
			writer.write(" >\n"); //$NON-NLS-1$

			writer.write("<input type=\"radio\" id=\"" + radioNullValueId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" name=\"" + radioName + "\""); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(" onclick=\"" + onClick + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			if (isNullValue)
				writer.write(" checked "); //$NON-NLS-1$
			writer.write(" >\n"); //$NON-NLS-1$
			writer.write("<label id=\"" + (radioNullValueId + "_label") + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			writer.write(" title=\"" + IBirtConstants.NULL_VALUE_DISPLAY + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" for=\"" + radioNullValueId + "\">"); //$NON-NLS-1$//$NON-NLS-2$
			writer.write(IBirtConstants.NULL_VALUE_DISPLAY);
			writer.write("</label>"); //$NON-NLS-1$
			writer.write("</input>"); //$NON-NLS-1$
		}

		// display text hidden object
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + displayTextId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		if (isChecked) {
			writer.write(" name=\"" + displayTextName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.displayTextString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$

		}
		writer.write(" >\n"); //$NON-NLS-1$

		// parameter value hidden object
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + valueId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		if (isChecked) {
			writer.write(" name=\"" + encParamName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.valueString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		writer.write(" >\n"); //$NON-NLS-1$
	}

	/**
	 * Handle output Check Box type parameter
	 * 
	 * @throws Exception
	 */
	protected void __handleCheckBox() throws Exception {
		JspWriter writer = pageContext.getOut();

		String encParamId = ParameterAccessor.htmlEncode(param.getId());
		String encParamName = ParameterAccessor.htmlEncode(param.getName());

		Boolean bl = (Boolean) param.getValue();
		boolean value = bl != null ? bl.booleanValue() : false;

		// parameter hidden value control
		String valueId = encParamId + "_value"; //$NON-NLS-1$
		writer.write("<input type=\"hidden\" "); //$NON-NLS-1$
		writer.write(" id=\"" + valueId + "\" "); //$NON-NLS-1$//$NON-NLS-2$
		writer.write(" name=\"" + encParamName + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write(" value=\"" + ParameterAccessor.htmlEncode(this.valueString) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write(" >\n"); //$NON-NLS-1$

		String valCtl = "document.getElementById('" + valueId + "')"; //$NON-NLS-1$ //$NON-NLS-2$
		String inputCtl = "document.getElementById('" + encParamId + "')"; //$NON-NLS-1$ //$NON-NLS-2$
		String onClick = "var value = 'false';if( " + inputCtl + ".checked ) value='true';" + valCtl //$NON-NLS-1$//$NON-NLS-2$
				+ ".value = value;"; //$NON-NLS-1$

		writer.write("<input type=\"checkbox\" "); //$NON-NLS-1$
		if (param.getId() != null)
			writer.write(" id=\"" + encParamId + "\""); //$NON-NLS-1$//$NON-NLS-2$
		__handleGeneralDefinition();
		writer.write(" onclick=\"" + onClick + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		if (value)
			writer.write(" checked "); //$NON-NLS-1$

		writer.write(" >"); //$NON-NLS-1$
	}

	/**
	 * Handle Exception
	 * 
	 * @param e
	 * @throws JspException
	 */
	protected void __handleException(Exception e) throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.write("<font color='red'>"); //$NON-NLS-1$
			writer.write(e.getMessage());
			writer.write("</font>"); //$NON-NLS-1$
		} catch (IOException err) {
			throw new JspException(err);
		}
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		param.setId(id);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		param.setName(name);
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		param.setPattern(pattern);
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		param.setValue(value);
	}

	/**
	 * @param displayText the displayText to set
	 */
	public void setDisplayText(String displayText) {
		param.setDisplayText(displayText);
	}

	/**
	 * @param isLocale the isLocale to set
	 */
	public void setIsLocale(String isLocale) {
		param.setIsLocale(isLocale);
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		param.setTitle(title);
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		param.setCssClass(cssClass);
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		param.setStyle(style);
	}
}
