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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.SearchKey;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * The test case for structure factory method.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testNewStructure()}</td>
 * <td>Creates the new Structure.</td>
 * <td>Structure are created successfully.</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class StructureFactoryTest extends BaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		designHandle = new SessionHandle(ULocale.getDefault()).createDesign();
		design = (ReportDesign) designHandle.getModule();
	}

	/**
	 * 
	 * Tests nativeName property in OdaDataSetParam and OdaResultSetColumn
	 * structure.
	 * 
	 * change type of nativename property from 'string' to 'literalstring'.
	 * 
	 * @throws SemanticException
	 */

	public void testLiteralString() throws SemanticException {
		OdaDataSetHandle dataSet = designHandle.getElementFactory().newOdaDataSet("dataset", null); //$NON-NLS-1$
		OdaDataSetParameter setParameter = StructureFactory.createOdaDataSetParameter();
		setParameter.setName("param1");//$NON-NLS-1$
		setParameter.setNativeName("");//$NON-NLS-1$
		OdaDataSetParameterHandle paramHandle = (OdaDataSetParameterHandle) dataSet
				.getPropertyHandle(OdaDataSetHandle.PARAMETERS_PROP).addItem(setParameter);
		assertEquals("param1", paramHandle.getName());//$NON-NLS-1$
		assertEquals("", paramHandle.getNativeName());//$NON-NLS-1$
		OdaResultSetColumn column = StructureFactory.createOdaResultSetColumn();
		column.setNativeName("");//$NON-NLS-1$
		column.setColumnName("column1");//$NON-NLS-1$
		OdaResultSetColumnHandle columnHandle = (OdaResultSetColumnHandle) dataSet
				.getPropertyHandle(OdaDataSetHandle.RESULT_SET_PROP).addItem(column);
		assertEquals("column1", columnHandle.getColumnName());//$NON-NLS-1$
		assertEquals("", columnHandle.getNativeName());//$NON-NLS-1$

	}

	/**
	 * Test the generic create element factory method.Create every type of element
	 * which was defined in reportDesignConstents.
	 * 
	 */

	public void testNewStructure() {
		ComputedColumn computedColumn = StructureFactory.createComputedColumn();
		assertNotNull(computedColumn);

		ConfigVariable configVar = StructureFactory.createConfigVar();
		assertNotNull(configVar);

		EmbeddedImage embeddedImage = StructureFactory.createEmbeddedImage();
		assertNotNull(embeddedImage);

		FilterCondition filterCondition = StructureFactory.createFilterCond();
		assertNotNull(filterCondition);

		HideRule hide = StructureFactory.createHideRule();
		assertNotNull(hide);

		IncludeScript includeScript = StructureFactory.createIncludeScript();
		assertNotNull(includeScript);

		IncludedLibrary includeLibrary = StructureFactory.createIncludeLibrary();
		assertNotNull(includeLibrary);

		ParamBinding paramBinding = StructureFactory.createParamBinding();
		assertNotNull(paramBinding);

		PropertyMask propertyMask = StructureFactory.createPropertyMask();
		assertNotNull(propertyMask);

		ResultSetColumn resultSetColumn = StructureFactory.createResultSetColumn();
		assertNotNull(resultSetColumn);

		SearchKey searchKey = StructureFactory.createSearchKey();
		assertNotNull(searchKey);

		SelectionChoice selectionChoice = StructureFactory.createSelectionChoice();
		assertNotNull(selectionChoice);

		SortKey sortKey = StructureFactory.createSortKey();
		assertNotNull(sortKey);

		ColumnHint columnHint = StructureFactory.createColumnHint();
		assertNotNull(columnHint);

		HighlightRule highlightRule = StructureFactory.createHighlightRule();
		assertNotNull(highlightRule);

		MapRule mapRule = StructureFactory.createMapRule();
		assertNotNull(mapRule);

		TOC toc = StructureFactory.createTOC();
		assertNotNull(toc);

		toc = StructureFactory.createTOC("toc"); //$NON-NLS-1$
		assertNotNull(toc);

	}

	/**
	 * Test the specified element factory method for bound data column.
	 * 
	 */

	public void testNewBoundDataColumns() throws SemanticException {
		TextItemHandle textHandle = designHandle.getElementFactory().newTextItem("text1"); //$NON-NLS-1$

		designHandle.getBody().add(textHandle);

		ComputedColumn column1 = StructureFactory.newComputedColumn(textHandle, "new_column"); //$NON-NLS-1$
		assertEquals("new_column", column1.getName()); //$NON-NLS-1$

		column1.setExpression("new expression"); //$NON-NLS-1$

		textHandle.addColumnBinding(column1, false);

		column1 = StructureFactory.newComputedColumn(textHandle, "new_column"); //$NON-NLS-1$
		assertEquals("new_column_1", column1.getName()); //$NON-NLS-1$

		column1.setExpression("new expression1"); //$NON-NLS-1$
		textHandle.addColumnBinding(column1, false);

		column1 = StructureFactory.newComputedColumn(textHandle, "new_column"); //$NON-NLS-1$
		assertEquals("new_column_2", column1.getName()); //$NON-NLS-1$
	}

}
