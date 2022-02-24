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

import org.eclipse.birt.core.data.Constants;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.data.adapter.impl.ModelAdapter;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.mozilla.javascript.Scriptable;

/**
 * Adapts a Model ODA data source handle to equivalent DtE oda data source
 * definition
 */
public class OdaDataSourceAdapter extends OdaDataSourceDesign {

	/**
	 * Creates adaptor based on Model OdaDataSourceHandle.
	 * 
	 * @param source           model handle
	 * @param propBindingScope Javascript scope in which to evaluate property
	 *                         bindings. If null, property bindings are not
	 *                         evaluated.
	 */
	public OdaDataSourceAdapter(OdaDataSourceHandle source, Scriptable propBindingScope, DataEngineContext dtCotnext,
			ModelAdapter adapter) throws BirtException {
		super(source.getQualifiedName());

		// Adapt base class properties
		DataAdapterUtil.adaptBaseDataSource(source, this);

		// Adapt extended data source elements
		// validate that a required attribute is specified
		String driverName = source.getExtensionID();
		if (driverName == null || driverName.length() == 0) {
			throw new AdapterException(ResourceConstants.DATASOURCE_EXID_ERROR, source.getName());
		}
		setExtensionID(driverName);

		// static ROM properties defined by the ODA driver extension
		Map staticProps = DataAdapterUtil.getExtensionProperties(source, source.getExtensionPropertyDefinitionList());
		if (staticProps != null && !staticProps.isEmpty()) {
			Iterator propNamesItr = staticProps.keySet().iterator();
			while (propNamesItr.hasNext()) {
				String propName = (String) propNamesItr.next();
				assert (propName != null);

				String propValue = "";
				// If property binding expression exists and the mode is not
				// UPDATE mode, use its evaluation result
				Expression expression = source.getPropertyBindingExpression(propName);
				org.eclipse.birt.data.engine.api.querydefn.ScriptExpression script = adapter
						.adaptExpression(expression);

				if (propBindingScope != null && script != null
						&& DataSessionContext.MODE_UPDATE != dtCotnext.getMode()) {
					Object value = JavascriptEvalUtil.evaluateScript(null, propBindingScope, script.getText(),
							ScriptExpression.defaultID, 0);
					propValue = (value == null ? null : value.toString());
				} else {
					propValue = (String) staticProps.get(propName);
				}
				addPublicProperty(propName, propValue);
			}
		}

		// private driver properties / private runtime data
		Iterator elmtIter = source.privateDriverPropertiesIterator();
		if (elmtIter != null) {
			while (elmtIter.hasNext()) {
				ExtendedPropertyHandle modelProp = (ExtendedPropertyHandle) elmtIter.next();
				addPrivateProperty(modelProp.getName(), modelProp.getValue());
			}
		}

		// TODO: move ModeDteApiAdpter there in future
		addPropertyConfigurationId(this, source);
	}

	/**
	 * Adds the externalized property configuration id for use by a BIRT consumer
	 * application's propertyProvider extension. Use the name not qualified name as
	 * configurationId.
	 */
	private void addPropertyConfigurationId(OdaDataSourceDesign dteSource, OdaDataSourceHandle sourceHandle)
			throws BirtException {
		String configIdValue = dteSource.getExtensionID() + Constants.ODA_PROP_CONFIG_KEY_SEPARATOR
				+ sourceHandle.getExternalConnectionName();
		dteSource.addPublicProperty(Constants.ODA_PROP_CONFIGURATION_ID, configIdValue);
	}

}
