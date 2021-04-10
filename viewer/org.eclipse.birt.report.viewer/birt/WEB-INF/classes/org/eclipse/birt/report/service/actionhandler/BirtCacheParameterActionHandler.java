/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.service.ParameterDataTypeConverter;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

import com.ibm.icu.util.ULocale;

public class BirtCacheParameterActionHandler extends AbstractBaseActionHandler {

	// provide an unique id for each config variable to avoid duplicated
	private int index = 0;

	// remember whether records parameter data type/expr..
	private Map map = new HashMap();

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtCacheParameterActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * execute action
	 */
	protected void __execute() throws Exception {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
		assert attrBean != null;

		// get design file name
		String reportDesignName = attrBean.getReportDesignName();

		// get design config file name
		String configFileName = ParameterAccessor.getConfigFileName(reportDesignName);

		if (configFileName == null) {
			handleUpdate();
			return;
		}

		// Generate the session handle
		SessionHandle sessionHandle = new DesignEngine(null).newSessionHandle(ULocale.US);

		File configFile = new File(configFileName);

		// if config file existed, then delete it
		if (configFile != null && configFile.exists() && configFile.isFile()) {
			configFile.delete();
		}

		// create a new config file
		ModuleOption options = new ModuleOption();
		options.setProperty(ModuleOption.BLANK_CREATION_KEY, Boolean.TRUE);
		ReportDesignHandle handle = sessionHandle.createDesign(configFileName, options);

		// get parameters from operation
		String displayTextParam = null;
		List locs = new ArrayList();
		Map params = new HashMap();
		Oprand[] op = this.operation.getOprand();
		if (op != null) {
			for (int i = 0; i < op.length; i++) {
				ConfigVariable configVar = new ConfigVariable();

				String paramName = op[i].getName();
				String paramValue = op[i].getValue();

				if (paramName == null || paramValue == null)
					continue;

				if (paramName.equalsIgnoreCase(ParameterAccessor.PARAM_ISLOCALE)) {
					// locale string
					locs.add(paramValue);
				}
				// if pass a null parameter
				else if (paramName.equalsIgnoreCase(ParameterAccessor.PARAM_ISNULL)) {
					ParameterDefinition parameter = attrBean.findParameterDefinition(paramValue);
					if (parameter != null) {
						// add null parameter to config file
						configVar.setName(getConfigName(ParameterAccessor.PARAM_ISNULL + "_" + parameter.getId())); //$NON-NLS-1$
						configVar.setValue(paramValue + "_" + parameter.getId()); //$NON-NLS-1$
						handle.addConfigVariable(configVar);

						// update parameter information
						updateParameterInfo(handle, parameter);
					}

					continue;
				} else if ((displayTextParam = ParameterAccessor.isDisplayText(paramName)) != null) {
					ParameterDefinition parameter = attrBean.findParameterDefinition(displayTextParam);
					if (parameter != null) {
						// add display text of select parameter
						configVar.setName(getConfigName(paramName + "_" + parameter.getId())); //$NON-NLS-1$
						configVar.setValue(paramValue);
						handle.addConfigVariable(configVar);
					}

					continue;
				} else {
					// push to parameter map
					List list = (List) params.get(paramName);
					if (list == null) {
						list = new ArrayList();
						params.put(paramName, list);
					}
					list.add(paramValue);
				}
			}
		}

		// hanlde parameters
		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String paramName = (String) entry.getKey();
			List paramValues = (List) entry.getValue();

			// find the parameter
			ParameterDefinition parameter = attrBean.findParameterDefinition(paramName);
			if (parameter == null)
				continue;

			String pattern = parameter.getPattern();
			String dataType = ParameterDataTypeConverter.convertDataType(parameter.getDataType());

			// check whether it is a locale String.
			boolean isLocale = locs.contains(paramName);

			if (parameter.isMultiValue()) {
				for (int i = 0; i < paramValues.size(); i++) {
					String paramValue = null;
					try {
						// convert parameter
						Object paramValueObj = DataUtil.validate(dataType, pattern, (String) paramValues.get(i),
								attrBean.getLocale(), attrBean.getTimeZone(), isLocale);

						paramValue = DataUtil.getDisplayValue(paramValueObj, attrBean.getTimeZone());

					} catch (Exception err) {
						paramValue = (String) paramValues.get(i);
					}

					// add parameter to config file
					if (paramValue != null) {
						ConfigVariable configVar = new ConfigVariable();
						configVar.setName(getConfigName(paramName + "_" + parameter.getId())); //$NON-NLS-1$
						configVar.setValue(paramValue);
						handle.addConfigVariable(configVar);
					}
				}
			} else {
				String paramValue = null;
				try {
					// convert parameter
					Object paramValueObj = DataUtil.validate(dataType, pattern, (String) paramValues.get(0),
							attrBean.getLocale(), attrBean.getTimeZone(), isLocale);

					paramValue = DataUtil.getDisplayValue(paramValueObj, attrBean.getTimeZone());

				} catch (Exception err) {
					paramValue = (String) paramValues.get(0);
				}

				// add parameter to config file
				if (paramValue != null) {
					ConfigVariable configVar = new ConfigVariable();
					configVar.setName(getConfigName(paramName + "_" + parameter.getId())); //$NON-NLS-1$
					configVar.setValue(paramValue);
					handle.addConfigVariable(configVar);
				}
			}

			// update parameter information
			updateParameterInfo(handle, parameter);
		}

		// save config file
		handle.save();
		handle.close();

		handleUpdate();
	}

	/**
	 * update parameter related information in config file
	 * 
	 * @param handle
	 * @param parameter
	 */
	private void updateParameterInfo(ReportDesignHandle handle, ParameterDefinition parameter) throws Exception {
		assert handle != null;
		assert parameter != null;

		String paramName = parameter.getName();
		if (map.containsKey(paramName))
			return;

		String dataType = ParameterDataTypeConverter.convertDataType(parameter.getDataType());

		// add parameter type
		ConfigVariable typeVar = new ConfigVariable();
		typeVar.setName(paramName + "_" + parameter.getId() + "_" //$NON-NLS-1$//$NON-NLS-2$
				+ IBirtConstants.PROP_TYPE + "_"); //$NON-NLS-1$
		typeVar.setValue(dataType);
		handle.addConfigVariable(typeVar);

		// add parameter value expression
		if (parameter.getValueExpr() != null) {
			ConfigVariable exprVar = new ConfigVariable();
			exprVar.setName(paramName + "_" + parameter.getId() + "_" //$NON-NLS-1$//$NON-NLS-2$
					+ IBirtConstants.PROP_EXPR + "_"); //$NON-NLS-1$
			exprVar.setValue(parameter.getValueExpr());
			handle.addConfigVariable(exprVar);
		}

		map.put(paramName, Boolean.valueOf(true));
	}

	/**
	 * Handle response
	 */
	protected void handleUpdate() {
		Data data = new Data();
		data.setConfirmation("Parameter value saved."); //$NON-NLS-1$

		UpdateData updateData = new UpdateData();
		updateData.setTarget("birtParameterDialog"); //$NON-NLS-1$
		updateData.setData(data);

		Update update = new Update();
		update.setUpdateData(updateData);
		response.setUpdate(new Update[] { update });
	}

	/**
	 * Returns the config variable name
	 */
	protected String getConfigName(String name) {
		return name + "_" + (index++); //$NON-NLS-1$
	}

	protected IViewerReportService getReportService() {
		return null;
	}
}