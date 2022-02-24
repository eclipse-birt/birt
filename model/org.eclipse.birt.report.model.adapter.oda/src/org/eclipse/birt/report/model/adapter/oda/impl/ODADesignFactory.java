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

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.adapter.oda.IODADesignFactory;
import org.eclipse.datatools.connectivity.oda.design.AndExpression;
import org.eclipse.datatools.connectivity.oda.design.AxisAttributes;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.CustomData;
import org.eclipse.datatools.connectivity.oda.design.CustomFilterExpression;
import org.eclipse.datatools.connectivity.oda.design.DataAccessDesign;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSetQuery;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionRequest;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionResponse;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.DesignerStateContent;
import org.eclipse.datatools.connectivity.oda.design.DynamicFilterExpression;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.ExpressionArguments;
import org.eclipse.datatools.connectivity.oda.design.ExpressionVariable;
import org.eclipse.datatools.connectivity.oda.design.FilterExpressionType;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterUIHints;
import org.eclipse.datatools.connectivity.oda.design.OdaDesignSession;
import org.eclipse.datatools.connectivity.oda.design.OutputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetCriteria;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.datatools.connectivity.oda.design.ResultSubset;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueChoices;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueDefinition;
import org.eclipse.datatools.connectivity.oda.design.SortKey;
import org.eclipse.datatools.connectivity.oda.design.SortSpecification;
import org.eclipse.datatools.connectivity.oda.design.StaticValues;
import org.eclipse.datatools.connectivity.oda.design.ValueFormatHints;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.emf.ecore.EObject;

/**
 *
 */

class ODADesignFactory implements IODADesignFactory {

	private DesignFactory designFactory = DesignFactory.eINSTANCE;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createColumnDefinition()
	 */

	public ColumnDefinition createColumnDefinition() {
		return designFactory.createColumnDefinition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDataAccessDesign()
	 */
	public DataAccessDesign createDataAccessDesign() {
		return designFactory.createDataAccessDesign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDataElementAttributes()
	 */
	public DataElementAttributes createDataElementAttributes() {
		return designFactory.createDataElementAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDataSetDesign()
	 */
	public DataSetDesign createDataSetDesign() {
		return designFactory.createDataSetDesign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDataSetParameters()
	 */
	public DataSetParameters createDataSetParameters() {
		return designFactory.createDataSetParameters();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDataSetQuery()
	 */
	public DataSetQuery createDataSetQuery() {
		return designFactory.createDataSetQuery();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDataSourceDesign()
	 */
	public DataSourceDesign createDataSourceDesign() {
		return designFactory.createDataSourceDesign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDesignSessionRequest()
	 */
	public DesignSessionRequest createDesignSessionRequest() {
		return designFactory.createDesignSessionRequest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createOdaDesignSession()
	 */
	public OdaDesignSession createOdaDesignSession() {
		return designFactory.createOdaDesignSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createParameterDefinition()
	 */
	public ParameterDefinition createParameterDefinition() {
		return designFactory.createParameterDefinition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IODADesignFactory#createProperties
	 * ()
	 */
	public Properties createProperties() {
		return designFactory.createProperties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createResultSetColumns()
	 */
	public ResultSetColumns createResultSetColumns() {
		return designFactory.createResultSetColumns();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createResultSetDefinition()
	 */
	public ResultSetDefinition createResultSetDefinition() {
		return designFactory.createResultSetDefinition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IODADesignFactory#createResultSets
	 * ()
	 */
	public ResultSets createResultSets() {
		return designFactory.createResultSets();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDataElementUIHints()
	 */
	public DataElementUIHints createDataElementUIHints() {
		return designFactory.createDataElementUIHints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDesignerState()
	 */
	public DesignerState createDesignerState() {
		return designFactory.createDesignerState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDesignerStateContent()
	 */
	public DesignerStateContent createDesignerStateContent() {
		return designFactory.createDesignerStateContent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDynamicValuesQuery()
	 */
	public DynamicValuesQuery createDynamicValuesQuery() {
		return designFactory.createDynamicValuesQuery();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createInputElementAttributes()
	 */
	public InputElementAttributes createInputElementAttributes() {
		return designFactory.createInputElementAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createInputElementUIHints()
	 */
	public InputElementUIHints createInputElementUIHints() {
		return designFactory.createInputElementUIHints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createInputParameterAttributes()
	 */
	public InputParameterAttributes createInputParameterAttributes() {
		return designFactory.createInputParameterAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createInputParameterUIHints()
	 */
	public InputParameterUIHints createInputParameterUIHints() {
		return designFactory.createInputParameterUIHints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createOutputElementAttributes()
	 */
	public OutputElementAttributes createOutputElementAttributes() {
		return designFactory.createOutputElementAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createScalarValueChoices()
	 */
	public ScalarValueChoices createScalarValueChoices() {
		return designFactory.createScalarValueChoices();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createScalarValueDefinition()
	 */
	public ScalarValueDefinition createScalarValueDefinition() {
		return designFactory.createScalarValueDefinition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createValueFormatHints()
	 */
	public ValueFormatHints createValueFormatHints() {
		return designFactory.createValueFormatHints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IODADesignFactory#validateObject
	 * (org.eclipse.emf.ecore.EObject)
	 */

	public void validateObject(EObject eObject) {
		DesignUtil.validateObject(eObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDesignSessionResponse()
	 */

	public DesignSessionResponse createDesignSessionResponse() {
		return designFactory.createDesignSessionResponse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createStaticValues()
	 */

	public StaticValues createStaticValues() {
		return designFactory.createStaticValues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createFilterExpressionType()
	 */

	public FilterExpressionType createFilterExpressionType() {
		return designFactory.createFilterExpressionType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createAndExpression()
	 */
	public AndExpression createAndExpression() {
		return designFactory.createAndExpression();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createCustomFilterExpression()
	 */
	public CustomFilterExpression createCustomFilterExpression() {
		return designFactory.createCustomFilterExpression();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createDynamicFilterExpression()
	 */
	public DynamicFilterExpression createDynamicFilterExpression() {
		return designFactory.createDynamicFilterExpression();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createExpressionArguments()
	 */
	public ExpressionArguments createExpressionArguments() {
		return designFactory.createExpressionArguments();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createExpressionVariable()
	 */
	public ExpressionVariable createExpressionVariable() {
		return designFactory.createExpressionVariable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createResultSetCriteria()
	 */
	public ResultSetCriteria createResultSetCriteria() {
		return designFactory.createResultSetCriteria();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IODADesignFactory#createSortKey ()
	 */
	public SortKey createSortKey() {
		return designFactory.createSortKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createSortSpecification()
	 */

	public SortSpecification createSortSpecification() {
		return designFactory.createSortSpecification();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IODADesignFactory#createCustomData
	 * ()
	 */
	public CustomData createCustomData() {
		return designFactory.createCustomData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createAxisAttributes()
	 */
	public AxisAttributes createAxisAttributes() {
		return designFactory.createAxisAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IODADesignFactory#
	 * createResultSubset()
	 */
	public ResultSubset createResultSubset() {
		return designFactory.createResultSubset();
	}
}
