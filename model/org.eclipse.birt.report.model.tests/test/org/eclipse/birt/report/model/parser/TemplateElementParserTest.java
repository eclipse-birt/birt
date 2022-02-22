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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateDataSetHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.TemplateParameterDefinitionHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.TemplateException;
import org.eclipse.birt.report.model.api.command.TemplateTransformEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.XMLParserException;

import com.ibm.icu.util.ULocale;

/**
 * The test case of all the issues about the templates.
 *
 * @see org.eclipse.birt.report.model.api.TemplateParameterDefinitionHandle
 * @see org.eclipse.birt.report.model.api.TemplateElementHandle
 * @see org.eclipse.birt.report.model.api.TemplateReportItemHandle
 * @see org.eclipse.birt.report.model.api.TemplateDataSetHandle
 */

public class TemplateElementParserTest extends BaseTestCase {

	/**
	 * This file is to test the parser.
	 */

	private final String parserFileName = "TemplateElementParserTest.xml"; //$NON-NLS-1$

	/**
	 * This file is to test the design file exception during the parser.
	 */

	private final String errorFileName = "TemplateElementParserTest_1.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests the parser result of the input file.
	 * <p>
	 * <ul>
	 * <li>case 1 -- get the template parameter definition by name from the module
	 * handle.
	 * <li>case 2 -- get the template parameter definition by name from the
	 * namespace in the module
	 * <li>case 3 -- all the properties of the template definitions are right
	 * <li>case 4 -- the default elements in the template definitions are not put
	 * into the namespace
	 * <li>case 5 -- get the template data set, template report item from the module
	 * and the data set namespace or element namespace
	 * <li>case 6 -- all the properties of the template report items and template
	 * data sets are right
	 * <li>case 7 -- check the reference between template definitions and template
	 * elements
	 * </ul>
	 *
	 *
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(parserFileName);
		assertNotNull(designHandle);

		// get the template definitions from the design handle

		TemplateParameterDefinitionHandle templateLabelParam = design
				.findTemplateParameterDefinition("templateLabelParam")//$NON-NLS-1$
				.handle(design);
		assertNotNull(templateLabelParam);
		TemplateParameterDefinitionHandle templateDataSetParam = design
				.findTemplateParameterDefinition("templateDataSetParam")//$NON-NLS-1$
				.handle(design);
		assertNotNull(templateDataSetParam);

		// get the template definition from the namespace

		NameSpace ns = design.getNameHelper().getNameSpace(ReportDesign.TEMPLATE_PARAMETER_NAME_SPACE);
		assertEquals(templateLabelParam.getElement(), ns.getElement("templateLabelParam")); //$NON-NLS-1$
		assertEquals(templateDataSetParam.getElement(), ns.getElement("templateDataSetParam")); //$NON-NLS-1$

		// get the properties for the template definitions

		assertEquals("templateLabelParam", templateLabelParam.getName());//$NON-NLS-1$

		assertEquals(DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_LABEL, templateLabelParam.getAllowedType());
		assertEquals("click here to create label", templateLabelParam //$NON-NLS-1$
				.getDescription());
		assertEquals("description key", templateLabelParam.getDescriptionKey()); //$NON-NLS-1$
		LabelHandle defaultLabel = (LabelHandle) templateLabelParam.getDefaultElement();
		assertNotNull(defaultLabel);
		assertEquals("base", defaultLabel.getName()); //$NON-NLS-1$
		assertEquals("6mm", defaultLabel.getX().getStringValue());//$NON-NLS-1$
		assertEquals("0.5mm", defaultLabel.getY().getStringValue());//$NON-NLS-1$
		assertEquals("0.25mm", defaultLabel.getHeight().getStringValue());//$NON-NLS-1$
		assertEquals("1mm", defaultLabel.getWidth().getStringValue());//$NON-NLS-1$

		assertEquals("templateDataSetParam", templateDataSetParam.getName());//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_DATA_SET, templateDataSetParam.getAllowedType());
		assertEquals("click here to create data set", templateDataSetParam //$NON-NLS-1$
				.getDescription());
		assertEquals("description key", templateDataSetParam //$NON-NLS-1$
				.getDescriptionKey());

		ScriptDataSetHandle defaultDataSet = (ScriptDataSetHandle) templateDataSetParam.getDefaultElement();
		assertNotNull(defaultDataSet);
		assertEquals("firstDataSet", defaultDataSet.getName()); //$NON-NLS-1$
		assertEquals("myDataSource", defaultDataSet.getDataSource().getName()); //$NON-NLS-1$
		assertEquals("open script", defaultDataSet.getOpen()); //$NON-NLS-1$
		assertEquals("close script", defaultDataSet.getClose()); //$NON-NLS-1$

		// the default elements in template definitions are not put into the
		// namespace, so the names can be used by other real report items or
		// data sets

		assertFalse(defaultLabel == designHandle.findElement(defaultLabel.getName()));
		assertNotNull(designHandle.findElement(defaultLabel.getName()));
		assertFalse(defaultDataSet == designHandle.findDataSet(defaultDataSet.getName()));
		assertNotNull(designHandle.findDataSet(defaultDataSet.getName()));

		// get the template label and template data set from the module; get the
		// template label from the element namespace and get the template data
		// set from the data set namespace

		TemplateDataSetHandle templateDataSet = designHandle.findTemplateDataSet("templateDataSet"); //$NON-NLS-1$
		assertNotNull(templateDataSet);
		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) designHandle.findElement("templateLabel"); //$NON-NLS-1$
		assertNotNull(templateLabel);
		assertEquals(templateDataSet.getElement(),
				design.getNameHelper().getNameSpace(ReportDesign.DATA_SET_NAME_SPACE).getElement("templateDataSet")); //$NON-NLS-1$
		assertEquals(templateLabel.getElement(),
				design.getNameHelper().getNameSpace(ReportDesign.ELEMENT_NAME_SPACE).getElement("templateLabel"));//$NON-NLS-1$

		// test the property values of template label and template data set

		assertEquals("templateDataSet", templateDataSet.getName());//$NON-NLS-1$
		ElementRefValue refTemplateParam = (ElementRefValue) templateDataSet.getElement().getProperty(design,
				TemplateDataSetHandle.REF_TEMPLATE_PARAMETER_PROP);
		assertNotNull(refTemplateParam);
		assertEquals(templateDataSetParam.getElement(), refTemplateParam.getElement());
		assertEquals(templateDataSetParam.getName(),
				templateDataSet.getProperty(TemplateDataSetHandle.REF_TEMPLATE_PARAMETER_PROP));
		assertEquals(DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_DATA_SET, templateDataSet.getAllowedType());
		assertEquals("click here to create data set", templateDataSet //$NON-NLS-1$
				.getDescription());

		assertEquals("description key", templateDataSet.getDescriptionKey()); //$NON-NLS-1$
		assertEquals(defaultDataSet, templateDataSet.getDefaultElement());

		assertEquals("templateLabel", templateLabel.getName()); //$NON-NLS-1$
		assertTrue(templateLabel.visibilityRulesIterator().hasNext());
		assertEquals("all", ((StructureHandle) templateLabel //$NON-NLS-1$
				.visibilityRulesIterator().next()).getMember(HideRule.FORMAT_MEMBER).getStringValue());
		assertEquals("1+1=3", ((StructureHandle) templateLabel //$NON-NLS-1$
				.visibilityRulesIterator().next()).getMember(HideRule.VALUE_EXPR_MEMBER).getStringValue());

		// test display name and display name ID

		assertEquals("display name key", templateLabel.getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("display name", templateLabel.getDisplayName()); //$NON-NLS-1$

		// tests user properties.

		List userProps = templateLabel.getUserProperties();
		assertEquals(2, userProps.size());

		UserPropertyDefn userPropDefn = (UserPropertyDefn) userProps.get(0);
		assertEquals("myProp1", userPropDefn.getName()); //$NON-NLS-1$
		assertEquals(IPropertyType.STRING_TYPE, userPropDefn.getTypeCode());
		assertEquals("world", templateLabel.getProperty("myProp1")); //$NON-NLS-1$ //$NON-NLS-2$

		userPropDefn = (UserPropertyDefn) userProps.get(1);
		assertEquals("myProp2", userPropDefn.getName()); //$NON-NLS-1$
		assertEquals(IPropertyType.CHOICE_TYPE, userPropDefn.getTypeCode());
		assertEquals("always", templateLabel.getProperty("myProp2")); //$NON-NLS-1$ //$NON-NLS-2$

		refTemplateParam = (ElementRefValue) templateLabel.getElement().getProperty(design,
				TemplateDataSetHandle.REF_TEMPLATE_PARAMETER_PROP);
		assertNotNull(refTemplateParam);
		assertEquals(templateLabelParam.getElement(), refTemplateParam.getElement());
		assertEquals(templateLabelParam.getName(),
				templateLabel.getProperty(TemplateDataSetHandle.REF_TEMPLATE_PARAMETER_PROP));
		assertEquals(DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_LABEL, templateLabel.getAllowedType());
		assertEquals("click here to create label", templateLabel //$NON-NLS-1$
				.getDescription());

		assertEquals("description key", templateLabel.getDescriptionKey()); //$NON-NLS-1$
		assertEquals(defaultLabel, templateLabel.getDefaultElement());

		// checks the back ref of the template definitions

		TemplateParameterDefinition templateParam = (TemplateParameterDefinition) templateDataSetParam.getElement();
		assertEquals(1, templateParam.getClientList().size());
		assertEquals(templateDataSet.getElement(), ((BackRef) templateParam.getClientList().get(0)).getElement());

		templateParam = (TemplateParameterDefinition) templateLabelParam.getElement();
		assertEquals(1, templateParam.getClientList().size());
		assertEquals(templateLabel.getElement(), ((BackRef) templateParam.getClientList().get(0)).getElement());
	}

	/**
	 * Tests the writer.
	 *
	 * @throws Exception
	 */

	public void testWriter() throws Exception {
		openDesign(parserFileName);
		assertNotNull(designHandle);

		// get the template definitions from the design handle

		TemplateParameterDefinitionHandle templateLabelParam = design
				.findTemplateParameterDefinition("templateLabelParam") //$NON-NLS-1$
				.handle(design);
		assertNotNull(templateLabelParam);

		// set the properties for the template definitions

		templateLabelParam.setName("new templateLabelParam"); //$NON-NLS-1$
		templateLabelParam.setDescription("click here to create new label"); //$NON-NLS-1$
		templateLabelParam.setDescriptionKey("new description key"); //$NON-NLS-1$

		// set properties for the template elements

		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) designHandle.findElement("templateLabel"); //$NON-NLS-1$
		assertNotNull(templateLabel);
		templateLabel.setName("new templateLabel"); //$NON-NLS-1$
		templateLabel.setProperty(TemplateReportItem.REF_TEMPLATE_PARAMETER_PROP, "new templateLabelParam"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("TemplateElementParserTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests the design file parser exception if the template elements refer a wrong
	 * type template definition. Test the semantic errors.
	 *
	 * @throws Exception
	 */

	public void testErrors() throws Exception {

		try {
			openDesign(errorFileName);
			fail();
		} catch (DesignFileException e) {
			assertEquals(DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR, e.getErrorCode());
			assertEquals(2, e.getExceptionList().size());
			assertTrue(e.getExceptionList().get(0) instanceof XMLParserException);
			XMLParserException exception = (XMLParserException) e.getExceptionList().get(0);
			assertTrue(exception.getException() instanceof DesignParserException);
			DesignParserException cause = (DesignParserException) exception.getException();
			assertEquals(DesignParserException.DESIGN_EXCEPTION_MISSING_TEMPLATE_PARAMETER_DEFAULT,
					cause.getErrorCode());

			assertTrue(e.getExceptionList().get(1) instanceof XMLParserException);
			exception = (XMLParserException) e.getExceptionList().get(1);
			assertTrue(exception.getException() instanceof DesignParserException);
			cause = (DesignParserException) exception.getException();
			assertEquals(DesignParserException.DESIGN_EXCEPTION_INCONSISTENT_TEMPLATE_ELEMENT_TYPE,
					cause.getErrorCode());
		}

	}

	/**
	 * Tests creates template element from a report item or data set.
	 *
	 * @throws Exception
	 *
	 */

	public void testCreateTemplate() throws Exception {
		openDesign(parserFileName);
		assertNotNull(designHandle);

		// case 1 -- create a template label from a real label in body slot

		ActivityStack stack = design.getActivityStack();

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("base"); //$NON-NLS-1$
		assertNotNull(labelHandle);
		String name = "label"; //$NON-NLS-1$
		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) labelHandle.createTemplateElement(name);

		// the real label is replaced by the template label

		save();
		compareFile("TemplateElementParserTest_golden_1.xml"); //$NON-NLS-1$

		// there is a template definition added into design for the new created
		// template label

		TemplateParameterDefinition templateParam = design
				.findTemplateParameterDefinition("NewTemplateParameterDefinition"); //$NON-NLS-1$
		assertNotNull(templateParam);

		assertEquals(templateLabel, designHandle.findElement(name));
		assertNotNull(templateLabel);
		labelHandle = (LabelHandle) designHandle.findElement("base"); //$NON-NLS-1$
		assertNull(labelHandle);

		// stack undo, then the template label disppear and label replaced it

		stack.undo();
		templateLabel = (TemplateReportItemHandle) designHandle.findElement(name);
		assertNull(templateLabel);
		labelHandle = (LabelHandle) designHandle.findElement("base"); //$NON-NLS-1$
		assertNotNull(labelHandle);

		// TODO the definition disappears

		// stack redo, then the label is replaced by the template label

		stack.redo();
		templateLabel = (TemplateReportItemHandle) designHandle.findElement(name);
		assertNotNull(templateLabel);
		labelHandle = (LabelHandle) designHandle.findElement("base"); //$NON-NLS-1$
		assertNull(labelHandle);

		// case 2 -- create a template label from a real label in the cell

		GridHandle gridHandle = designHandle.getElementFactory().newGridItem("grid", 1, 1); //$NON-NLS-1$
		assertNotNull(gridHandle);
		CellHandle cellHandle = gridHandle.getCell(1, 1);
		assertNotNull(cellHandle);
		labelHandle = designHandle.getElementFactory().newLabel("cellLabel"); //$NON-NLS-1$
		assertNotNull(labelHandle);
		cellHandle.getContent().add(labelHandle);
		designHandle.getBody().add(gridHandle);
		MyListener listener = new MyListener();
		cellHandle.addListener(listener);
		assertEquals(cellHandle, labelHandle.getContainer());

		// create a template label from the cell label and the name of the
		// template label is the same as that of the replaced cell label; the
		// cell receives the template transformation event and all the parameter
		// values in event is right

		templateLabel = (TemplateReportItemHandle) labelHandle.createTemplateElement(labelHandle.getName());
		assertEquals(templateLabel, designHandle.findElement(labelHandle.getName()));
		assertNotNull(templateLabel);
		assertEquals(NotificationEvent.TEMPLATE_TRANSFORM_EVENT, listener.event.getEventType());
		assertEquals(cellHandle, listener.focus);
		TemplateTransformEvent ttEvent = (TemplateTransformEvent) listener.event;
		assertEquals(labelHandle.getElement(), ttEvent.getFrom());
		assertEquals(templateLabel.getElement(), ttEvent.getTo());
		assertEquals(Cell.CONTENT_SLOT, ttEvent.getSlot());
		assertEquals(cellHandle.getElement(), ttEvent.getTarget());

		// case 3 -- create a template data set from the real data set

		name = "templateDataSet"; //$NON-NLS-1$
		TemplateDataSetHandle templateDataSet;
		DataSetHandle dataSetHandle = designHandle.getElementFactory().newScriptDataSet("dataSet"); //$NON-NLS-1$

		// the data set has not been in the tree, there is an error

		try {
			templateDataSet = (TemplateDataSetHandle) dataSetHandle.createTemplateElement(name);
			fail();
		} catch (SemanticException e) {
			assertEquals(TemplateException.DESIGN_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN, e.getErrorCode());
		}

		// add the data set to the report design and its name is duplicate with
		// one which has been in the design, but the template command rename it
		// to get a unique name

		designHandle.getDataSets().add(dataSetHandle);
		assertNotNull(designHandle.findTemplateDataSet(name));

		templateDataSet = (TemplateDataSetHandle) dataSetHandle.createTemplateElement(name);
		assertNotNull(templateDataSet);
		assertEquals(templateDataSet.getElement(), designHandle.findTemplateDataSet("templateDataSet1")); //$NON-NLS-1$

		// styles can not create template element

		StyleHandle style = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$
		designHandle.getStyles().add(style);
		try {
			style.createTemplateElement("page"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(TemplateException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE, e.getErrorCode());
		}

		// libraries not supporte template elements

		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle((ULocale) null);
		libraryHandle = session.createLibrary();
		labelHandle = libraryHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$
		libraryHandle.getComponents().add(labelHandle);
		try {
			labelHandle.createTemplateElement(null);
			fail();
		} catch (SemanticException e) {
			assertEquals(TemplateException.DESIGN_EXCEPTION_TEMPLATE_ELEMENT_NOT_SUPPORTED, e.getErrorCode());
		}

	}

	/**
	 * Tests the parser about the name issue: the items in the template definition
	 * needs no name. That is to say, even if data set is name-required, the data
	 * set that resides in any template definition can have no name. Such a file can
	 * be correctly opened.
	 *
	 * @throws Exception
	 */
	public void testParserWithNoName() throws Exception {
		openDesign("TemplateElementParserTest_4.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);
	}

	/**
	 * Tests transform a tempalte element to a real report item or data set.
	 *
	 * @throws Exception
	 */

	public void testTransformTemplate() throws Exception {
		openDesign(parserFileName);
		assertNotNull(designHandle);

		// read the template data set and template label from the file

		TemplateDataSetHandle templateDataSet = designHandle.findTemplateDataSet("templateDataSet"); //$NON-NLS-1$
		assertNotNull(templateDataSet);
		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) designHandle.findElement("templateLabel"); //$NON-NLS-1$
		assertNotNull(templateLabel);

		// create a real label and data set

		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$
		ScriptDataSetHandle dataSet = designHandle.getElementFactory().newScriptDataSet("dataSet"); //$NON-NLS-1$

		ActivityStack stack = design.getActivityStack();

		// transform the template data set

		templateDataSet.transformToDataSet(dataSet);
		assertEquals(dataSet.getElement(), designHandle.findDataSet(dataSet.getName()));
		assertNull(designHandle.findTemplateDataSet(templateDataSet.getName()));
		assertNull(templateDataSet.getContainer());
		assertEquals("templateDataSetParam", dataSet.getElement() //$NON-NLS-1$
				.getTemplateParameterElement(design).getName());

		// stack undo,

		stack.undo();
		assertNull(designHandle.findDataSet(dataSet.getName()));
		assertEquals(templateDataSet, designHandle.findTemplateDataSet(templateDataSet.getName()));
		assertEquals(designHandle, templateDataSet.getContainer());

		// set the template definition in the template data set to null, then
		// the transform fails

		try {
			templateDataSet.setProperty(TemplateDataSetHandle.REF_TEMPLATE_PARAMETER_PROP, null);
			templateDataSet.transformToDataSet(dataSet);
			fail();
		} catch (SemanticException e) {
			assertEquals(TemplateException.DESIGN_EXCEPTION_TRANSFORM_TO_DATA_SET_FORBIDDEN, e.getErrorCode());
		}

		// transform the template label

		MyListener listener = new MyListener();
		templateLabel.addListener(listener);
		templateLabel.transformToReportItem(label);

		// the template label has been transformed to a real label, then it must
		// not be send the event

		assertNull(listener.event);
		assertNull(templateLabel.getContainer());
		assertEquals(label.getElement(), designHandle.findElement(label.getName()));
		assertNull(designHandle.findElement(templateLabel.getName()));
		assertNull(templateLabel.getContainer());
		assertEquals("templateLabelParam", label.getElement() //$NON-NLS-1$
				.getTemplateParameterElement(design).getName());
	}

	/**
	 * Tests a report item or data set based on template definition to be reverted
	 * to a template element.
	 *
	 * @throws Exception
	 */

	public void testRevertToTemplate() throws Exception {
		openDesign("TemplateElementParserTest_3.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		// get the template definition based label and data set

		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		assertNotNull(label);
		assertEquals("templateLabelParam", label.getElement() //$NON-NLS-1$
				.getTemplateParameterElement(design).getName());

		ScriptDataSetHandle dataSet = (ScriptDataSetHandle) designHandle.findDataSet("dataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);
		assertEquals("templateDataSetParam", dataSet.getElement().getTemplateParameterElement(design).getName()); //$NON-NLS-1$

		// revert the label and data set to a template element

		TemplateElementHandle templateHandle;
		templateHandle = label.revertToTemplate("templateLabel"); //$NON-NLS-1$
		assertNotNull(templateHandle);
		assertTrue(templateHandle instanceof TemplateReportItemHandle);
		assertEquals("templateLabelParam", templateHandle.getElement() //$NON-NLS-1$
				.getTemplateParameterElement(design).getName());

		templateHandle = dataSet.revertToTemplate("templateDatSet"); //$NON-NLS-1$
		assertNotNull(templateHandle);
		assertTrue(templateHandle instanceof TemplateDataSetHandle);
		assertEquals("templateDataSetParam", templateHandle.getElement() //$NON-NLS-1$
				.getTemplateParameterElement(design).getName());
	}

	/**
	 * Tests complex template issues.
	 *
	 * @throws Exception
	 */

	public void testComplexCreateTemplate() throws Exception {
		openDesign(this.parserFileName);
		GridHandle gridHandle = designHandle.getElementFactory().newGridItem("grid", 3, 1); //$NON-NLS-1$
		CellHandle cellHandle = gridHandle.getCell(1, 1);
		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$
		cellHandle.addElement(labelHandle, Cell.CONTENT_SLOT);
		designHandle.addElement(gridHandle, ReportDesign.BODY_SLOT);
		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) labelHandle
				.createTemplateElement("templateLabel3"); //$NON-NLS-1$
		assertNotNull(templateLabel);

		// if a child element in a compound element is a template element, then
		// the compound element can be transformed to a template report
		// item, too.

		TemplateReportItemHandle templateGrid = (TemplateReportItemHandle) gridHandle
				.createTemplateElement("templateGrid"); //$NON-NLS-1$
		assertNotNull(templateGrid);

	}

	/**
	 * Tests the destroying the useless TemplateParameterDefinition when calling
	 * semanticCheck of report design.
	 *
	 * @throws Exception
	 *
	 */

	public void testDestroyTemplateParameterDefinition() throws Exception {
		designHandle = (new DesignEngine(new DesignConfig())).newSessionHandle((ULocale) null).createDesign();
		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$
		assertNotNull(labelHandle);

		ContainerSlot templateParams = designHandle.getElement()
				.getSlot(ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT);
		assertEquals(0, templateParams.getCount());

		designHandle.getBody().add(labelHandle);
		TemplateElementHandle templateLabel = labelHandle.createTemplateElement("templateLabel"); //$NON-NLS-1$
		assertNotNull(templateLabel);
		assertEquals(designHandle, templateLabel.getRoot());
		assertEquals(1, templateParams.getCount());
		templateLabel.drop();
		designHandle.checkReport();
		assertEquals(0, templateParams.getCount());

	}

	/**
	 * Tests checkAdd() in TemplateCommand about the handler of
	 * refTemplateParameter.
	 *
	 * @throws Exception
	 */

	public void testCheckAdd() throws Exception {
		openDesign(parserFileName);
		assertNotNull(designHandle);

		// add a label which defines a template param can be found in the design

		LabelHandle label = designHandle.getElementFactory().newLabel(null);
		label.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, "templateLabelParam"); //$NON-NLS-1$
		designHandle.getBody().add(label);
		assertEquals(designHandle, label.getContainer());
		assertEquals("templateLabelParam", label.getProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP)); //$NON-NLS-1$

		// add a table which defines a template param can be found but the kind
		// is inconsistent

		TableHandle table = designHandle.getElementFactory().newTableItem(null);
		table.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, "templateLabelParam"); //$NON-NLS-1$
		designHandle.getBody().add(table);
		assertEquals(designHandle, table.getContainer());
		assertNull(table.getProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP));

		// add a label which defines a template param can not be found

		label = designHandle.getElementFactory().newLabel(null);
		label.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, "NonexisttemplateLabelParam"); //$NON-NLS-1$
		designHandle.getBody().add(label);
		assertEquals(designHandle, label.getContainer());
		assertNull(label.getProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP));

	}

	/**
	 * Tests canTransformToTemplate(). Now support template for data set and part of
	 * report items. Auto text does not support template.
	 *
	 * @throws Exception
	 */

	public void testCanTransformToTemplateForReportItem() throws Exception {
		openDesign(parserFileName);
		SimpleMasterPageHandle masterPage = (SimpleMasterPageHandle) designHandle.getMasterPages().get(0);
		AutoTextHandle autoText = designHandle.getElementFactory().newAutoText("test"); //$NON-NLS-1$
		masterPage.getPageHeader().dropAndClear(0);
		masterPage.getPageHeader().add(autoText);
		assertFalse(autoText.canTransformToTemplate());

	}

	/**
	 * Implements a listener.
	 */

	class MyListener implements Listener {

		NotificationEvent event = null;
		DesignElementHandle focus = null;

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.model.api.core.Listener#elementChanged(org.eclipse.
		 * birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */

		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			this.event = ev;
			this.focus = focus;
		}

	}
}
