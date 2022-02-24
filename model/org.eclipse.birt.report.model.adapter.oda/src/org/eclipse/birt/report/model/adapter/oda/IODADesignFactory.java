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

package org.eclipse.birt.report.model.adapter.oda;

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

/**
 * The wrapper class for ODA DesignFactory.
 *
 */

public interface IODADesignFactory {

	ColumnDefinition createColumnDefinition();

	DataAccessDesign createDataAccessDesign();

	DataElementAttributes createDataElementAttributes();

	DataSetDesign createDataSetDesign();

	DataSetParameters createDataSetParameters();

	DataSetQuery createDataSetQuery();

	DataSourceDesign createDataSourceDesign();

	DesignSessionRequest createDesignSessionRequest();

	OdaDesignSession createOdaDesignSession();

	ParameterDefinition createParameterDefinition();

	Properties createProperties();

	ResultSets createResultSets();

	ResultSetColumns createResultSetColumns();

	ResultSetDefinition createResultSetDefinition();

	InputParameterAttributes createInputParameterAttributes();

	InputElementAttributes createInputElementAttributes();

	DesignerState createDesignerState();

	DesignerStateContent createDesignerStateContent();

	DataElementUIHints createDataElementUIHints();

	ScalarValueChoices createScalarValueChoices();

	ScalarValueDefinition createScalarValueDefinition();

	DynamicValuesQuery createDynamicValuesQuery();

	InputElementUIHints createInputElementUIHints();

	InputParameterUIHints createInputParameterUIHints();

	OutputElementAttributes createOutputElementAttributes();

	ValueFormatHints createValueFormatHints();

	void validateObject(org.eclipse.emf.ecore.EObject eObject);

	DesignSessionResponse createDesignSessionResponse();

	StaticValues createStaticValues();

	FilterExpressionType createFilterExpressionType();

	ResultSetCriteria createResultSetCriteria();

	AndExpression createAndExpression();

	SortSpecification createSortSpecification();

	SortKey createSortKey();

	CustomFilterExpression createCustomFilterExpression();

	ExpressionVariable createExpressionVariable();

	DynamicFilterExpression createDynamicFilterExpression();

	ExpressionArguments createExpressionArguments();

	CustomData createCustomData();

	AxisAttributes createAxisAttributes();

	ResultSubset createResultSubset();
}
