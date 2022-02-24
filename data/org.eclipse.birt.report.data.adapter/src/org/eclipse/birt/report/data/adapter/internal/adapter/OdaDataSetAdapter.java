/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.internal.adapter;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.impl.ModelAdapter;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSetModel;
import org.mozilla.javascript.Scriptable;

/**
 * Adapts a Model Oda Data Set definition handle
 */
public class OdaDataSetAdapter extends OdaDataSetDesign {

	/**
	 * Adapts a model oda data set handle
	 *
	 * @param modelDataSet     model handle
	 * @param propBindingScope Javascript scope in which to evaluate property
	 *                         binding expressions. If null, property binding is not
	 *                         resolved
	 * @throws BirtException
	 */
	public OdaDataSetAdapter(OdaDataSetHandle modelDataSet, Scriptable propBindingScope, ModelAdapter adapter,
			DataEngineContext dtContext) throws BirtException {
		super(modelDataSet.getQualifiedName());

		// TODO: event handler

		// Adapt base class properties
		DataAdapterUtil.adaptBaseDataSet(modelDataSet, this, adapter);

		// Adapt extended data set elements

		// Set query text; if binding exists, use its result; otherwise
		// use static design
		Expression expression = modelDataSet.getPropertyBindingExpression(OdaDataSet.QUERY_TEXT_PROP);
		org.eclipse.birt.data.engine.api.querydefn.ScriptExpression script = adapter.adaptExpression(expression);

		if (propBindingScope != null && script != null && DataSessionContext.MODE_UPDATE != dtContext.getMode()) {
			String queryText = JavascriptEvalUtil
					.evaluateScript(null, propBindingScope, script.getText(), ScriptExpression.defaultID, 0).toString();
			setQueryText(queryText);
		} else {
			setQueryText(modelDataSet.getQueryText());
		}

		// type of extended data set
		setExtensionID(modelDataSet.getExtensionID());

		// result set name
		setPrimaryResultSetName(modelDataSet.getResultSetName());

		if (modelDataSet.getPropertyHandle(IOdaDataSetModel.RESULT_SET_NUMBER_PROP).isSet()) {
			setPrimaryResultSetNumber(modelDataSet.getResultSetNumber());
		}
		// static ROM properties defined by the ODA driver extension
		Map staticProps = DataAdapterUtil.getExtensionProperties(modelDataSet,
				modelDataSet.getExtensionPropertyDefinitionList());
		if (staticProps != null && !staticProps.isEmpty()) {
			Iterator propNamesItr = staticProps.keySet().iterator();
			while (propNamesItr.hasNext()) {
				String propName = (String) propNamesItr.next();
				assert (propName != null);
				String propValue;
				String bindingExpr = modelDataSet.getPropertyBinding(propName);
				if (propBindingScope != null && bindingExpr != null && bindingExpr.length() > 0) {
					propValue = JavascriptEvalUtil
							.evaluateScript(null, propBindingScope, bindingExpr, ScriptExpression.defaultID, 0)
							.toString();
				} else {
					propValue = (String) staticProps.get(propName);
				}
				addPublicProperty((String) propName, propValue);
			}
		}

		// private driver properties / private runtime data
		Iterator elmtIter = modelDataSet.privateDriverPropertiesIterator();
		if (elmtIter != null) {
			while (elmtIter.hasNext()) {
				ExtendedPropertyHandle modelProp = (ExtendedPropertyHandle) elmtIter.next();
				addPrivateProperty(modelProp.getName(), modelProp.getValue());
			}
		}
	}

}
