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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.IncludedLibraryHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScriptLibHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * This class tests the property parsing and writing. Translation is test in
 * <code>ReportDesignUserDefinedMessagesTest</code> All slots will be tested in
 * the corresponding element parse test.
 *
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: *
 * collapse" bordercolor="#111111" width="100%" id="AutoNumber5" height="99">
 * <tr>
 * <td width="33%" height="16"><b>Method </b></td>
 * <td width="33%" height="16"><b>Test Case </b></td>
 * <td width="34%" height="16"><b>Expected Result </b></td>
 * </tr>
 *
 * <tr>
 * <td width="33%" height="16">{@link #testParser()}</td>
 * <td width="33%" height="16">Test all propertyies</td>
 * <td width="34%" height="16">the correct value are returned</td>
 * </tr>
 *
 * <tr>
 * <td width="33%" height="14"></td>
 * <td width="33%" height="14">Use iterator to test the reportItems slot in
 * freeform</td>
 * <td width="34%" height="14">content can be retrieved.</td>
 * </tr>
 *
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Test the freeform extends relationship</td>
 * <td width="34%" height="16">extend relationship correct.</td>
 * </tr>
 *
 * <tr>
 * <td width="33%" height="16">{@link #testWriter()}</td>
 * <td width="33%" height="16">Set new value to properties and save it.</td>
 * <td width="34%" height="16">new value should be save into the output
 * file.</td>
 * </tr>
 *
 * <tr>
 * <td width="33%" height="16">{@link #testConfigVars()}</td>
 * <td width="33%" height="16">Test add, find, replace and drop operation on
 * Config Variables.</td>
 * <td width="34%" height="16">Check it with find method.</td>
 * </tr>
 *
 * <tr>
 * <td width="33%" height="16">{@link #testImages()}</td>
 * <td width="33%" height="16">Test add, find, replace and drop operation on
 * embedded images.</td>
 * <td width="34%" height="16">Check it with find method.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSemanticError()}</td>
 * <td>Test semantic errors with the design file input.</td>
 * <td>The errors are collected, such as the font-size is negative and there is
 * no pages in the page setup slot.</td>
 * </tr>
 * </table>
 *
 * @see org.eclipse.birt.report.model.parser.ScalarParameterParseTest
 * @see org.eclipse.birt.report.model.parser.StyleParseTest
 * @see org.eclipse.birt.report.model.parser.ComponentScratchPadTest
 * @see org.eclipse.birt.report.model.elements.ReportDesignUserDefinedMessagesTest
 */

public class ReportDesignParseTest extends BaseTestCase {

	String fileName = "ReportDesignParseTest.xml"; //$NON-NLS-1$
	String goldenFileName = "ReportDesignParseTest_golden.xml"; //$NON-NLS-1$
	String goldenFileName_2 = "ReportDesignParseTest_golden_2.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "ReportDesignParseTest_1.xml"; //$NON-NLS-1$
	String datasourceBindingsFileName = "ReportDesignParseTest_2.xml"; //$NON-NLS-1$

	String scriptLibFileName = "ReportDesignScriptLibParseTest.xml";//$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests all properties and slots.
	 *
	 * @throws Exception
	 */
	public void testParser() throws Exception {
		openDesign(fileName, ULocale.ENGLISH);

		assertEquals(1000, designHandle.getID());

		assertEquals("W.C. Fields", designHandle.getStringProperty(ReportDesign.AUTHOR_PROP)); //$NON-NLS-1$
		assertEquals("subject", designHandle.getSubject());//$NON-NLS-1$
		assertEquals("http://company.com/reportHelp.html", //$NON-NLS-1$
				design.getStringProperty(design, ReportDesign.HELP_GUIDE_PROP));
		assertEquals("Whiz-Bang Plus", design.getStringProperty(design, ReportDesign.CREATED_BY_PROP)); //$NON-NLS-1$
		assertEquals("30", design.getStringProperty(design, ReportDesign.REFRESH_RATE_PROP)); //$NON-NLS-1$
		assertEquals("c:\\", designHandle.getBase()); //$NON-NLS-1$
		assertEquals("library", designHandle.getIncludeResource()); //$NON-NLS-1$

		// title

		assertEquals("TITLE_ID", design.getStringProperty(design, ReportDesign.TITLE_ID_PROP)); //$NON-NLS-1$
		assertEquals("Sample Report", design.getStringProperty(design, ReportDesign.TITLE_PROP)); //$NON-NLS-1$

		// comments

		assertEquals("First sample report.", design.getStringProperty(design, ReportDesign.COMMENTS_PROP)); //$NON-NLS-1$

		// description

		assertEquals("DESCRIP_ID", design.getStringProperty(design, ReportDesign.DESCRIPTION_ID_PROP)); //$NON-NLS-1$
		assertEquals("This is a first sample report.", design.getStringProperty(design, ReportDesign.DESCRIPTION_PROP)); //$NON-NLS-1$

		// display name

		assertEquals("display name key", design.getStringProperty(design, ReportDesign.DISPLAY_NAME_ID_PROP)); //$NON-NLS-1$
		assertEquals("display name", design.getStringProperty(design, ReportDesign.DISPLAY_NAME_PROP)); //$NON-NLS-1$

		// icon file and cheet sheet

		assertEquals("iconFile", design.getStringProperty(design, ReportDesign.ICON_FILE_PROP)); //$NON-NLS-1$
		assertEquals("cheatSheet", design.getStringProperty(design, ReportDesign.CHEAT_SHEET_PROP)); //$NON-NLS-1$

		// event handler class

		assertEquals("on_Event", design.getStringProperty(design, ReportDesign.EVENT_HANDLER_CLASS_PROP)); //$NON-NLS-1$

		assertTrue(designHandle.newHandlerOnEachEvent());

		// layout preference
		assertEquals(DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT, designHandle.getLayoutPreference());

		// bidi

		assertEquals(DesignChoiceConstants.BIDI_DIRECTION_RTL, designHandle.getBidiOrientation());

		// ACL

		assertTrue(designHandle.isEnableACL());
		assertEquals("acl expression test", designHandle.getACLExpression()); //$NON-NLS-1$
		assertFalse(designHandle.cascadeACL());
		assertEquals(30, designHandle.getImageDPI());

		// keywords is not implemented
		// include libraries

		Iterator includeLibraries = designHandle.includeLibrariesIterator();

		IncludedLibraryHandle lib = (IncludedLibraryHandle) includeLibraries.next();
		assertEquals("LibraryA.xml", lib.getFileName()); //$NON-NLS-1$
		assertEquals("LibA", lib.getNamespace()); //$NON-NLS-1$

		lib = (IncludedLibraryHandle) includeLibraries.next();
		assertEquals("LibraryB.xml", lib.getFileName()); //$NON-NLS-1$
		assertEquals("LibB", lib.getNamespace()); //$NON-NLS-1$

		lib = (IncludedLibraryHandle) includeLibraries.next();
		assertEquals("LibraryC.xml", lib.getFileName()); //$NON-NLS-1$
		assertEquals("LibC", lib.getNamespace()); //$NON-NLS-1$

		// code-modules

		Iterator scripts = designHandle.includeScriptsIterator();
		IncludeScriptHandle script = (IncludeScriptHandle) scripts.next();
		assertEquals("script first", script.getFileName()); //$NON-NLS-1$

		script = (IncludeScriptHandle) scripts.next();
		assertEquals("script second", script.getFileName()); //$NON-NLS-1$

		assertNull(scripts.next());

		// test-config is not implemented

		// color-palette

		PropertyHandle colorPalette = designHandle.getPropertyHandle(ReportDesign.COLOR_PALETTE_PROP);
		List colors = colorPalette.getListValue();
		assertEquals(2, colors.size());
		CustomColor color = (CustomColor) colors.get(0);
		assertEquals("cus red", color.getName()); //$NON-NLS-1$
		assertEquals(111, color.getRGB());
		assertEquals("cus red key", color.getDisplayNameID()); //$NON-NLS-1$
		assertEquals("cus red display", color.getDisplayName()); //$NON-NLS-1$
		color = (CustomColor) colors.get(1);
		assertEquals("cus blue", color.getName()); //$NON-NLS-1$
		assertEquals(222, color.getRGB());
		assertEquals("cus blue key", color.getDisplayNameID()); //$NON-NLS-1$

		// config-vars
		PropertyHandle configVarHandle = designHandle.getPropertyHandle(ReportDesign.CONFIG_VARS_PROP);
		List configVars = configVarHandle.getListValue();
		assertEquals(4, configVars.size());
		ConfigVariable var = (ConfigVariable) configVars.get(0);
		assertEquals("var1", var.getName()); //$NON-NLS-1$
		assertEquals("mumble.jpg", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(1);
		assertEquals("var2", var.getName()); //$NON-NLS-1$
		assertEquals("abcdefg", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(2);
		assertEquals("var3", var.getName()); //$NON-NLS-1$
		assertEquals("", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(3);
		assertEquals("var4", var.getName()); //$NON-NLS-1$
		assertEquals(null, var.getValue());

		// images
		PropertyHandle imageHandle = designHandle.getPropertyHandle(ReportDesign.IMAGES_PROP);
		List images = imageHandle.getListValue();
		assertEquals(3, images.size());
		EmbeddedImage image = (EmbeddedImage) images.get(0);
		assertEquals("image1", image.getName()); //$NON-NLS-1$
		assertEquals("image/bmp", image.getType(design)); //$NON-NLS-1$
		assertEquals("imagetesAAA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(design))).substring(0, 11));

		image = (EmbeddedImage) images.get(1);
		assertEquals("image2", image.getName()); //$NON-NLS-1$
		assertEquals("image/gif", image.getType(design)); //$NON-NLS-1$
		assertEquals("/9j/4AAQSkZJRgA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(design))).substring(0, 15));

		image = (EmbeddedImage) images.get(2);
		assertEquals("image3", image.getName()); //$NON-NLS-1$
		assertEquals("image/bmp", image.getType(design)); //$NON-NLS-1$
		assertEquals("AAAA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(design))));

		// thumbnail

		assertTrue(new String(Base64.encodeBase64(designHandle.getThumbnail())).startsWith("thumbnailimage")); //$NON-NLS-1$

		// custom is not implemented

		assertEquals("script of initialize", designHandle.getInitialize()); //$NON-NLS-1$

		assertEquals("script of beforeFactory", designHandle.getBeforeFactory()); //$NON-NLS-1$
		assertEquals("script of afterFactory", designHandle.getAfterFactory()); //$NON-NLS-1$

		assertEquals("script of beforeRender", designHandle.getBeforeRender()); //$NON-NLS-1$
		assertEquals("script of afterRender", designHandle.getAfterRender()); //$NON-NLS-1$

		assertEquals("script of onPageStart", designHandle.getOnPageStart()); //$NON-NLS-1$
		assertEquals("script of onPageEnd", designHandle.getOnPageEnd()); //$NON-NLS-1$

		// test parser css in report design
		Iterator iterator = designHandle.includeCssesIterator();
		IncludedCssStyleSheetHandle css = (IncludedCssStyleSheetHandle) iterator.next();
		assertEquals("base.css", css.getFileName());//$NON-NLS-1$
		assertEquals("externalCss.css", css.getExternalCssURI());//$NON-NLS-1$
		IncludedCssStyleSheetHandle css1 = (IncludedCssStyleSheetHandle) iterator.next();
		assertEquals("base1.css", css1.getFileName());//$NON-NLS-1$
		assertEquals(null, css1.getExternalCssURI());
		List styles = designHandle.getAllStyles();
		assertEquals(5, styles.size());

		// Check styles in css

		assertEquals("Code", ((StyleHandle) styles.get(0)).getName());//$NON-NLS-1$
		assertEquals("CaptionFigColumn", ((StyleHandle) styles.get(1)).getName());//$NON-NLS-1$
		assertEquals("Note", ((StyleHandle) styles.get(2)).getName());//$NON-NLS-1$
		assertEquals("UILabel", ((StyleHandle) styles.get(3)).getName());//$NON-NLS-1$
		assertEquals("CodeName", ((StyleHandle) styles.get(4)).getName());//$NON-NLS-1$

		StyleHandle style = (StyleHandle) styles.get(0);
		assertEquals("left", style.getTextAlign());//$NON-NLS-1$

		List<VariableElementHandle> list = designHandle.getPageVariables();
		assertEquals(2, list.size());

		VariableElementHandle handle = list.get(0);
		assertEquals("variable1", handle.getVariableName()); //$NON-NLS-1$

		handle = list.get(1);
		assertEquals("variable2", handle.getVariableName()); //$NON-NLS-1$

		handle = designHandle.getPageVariable("variable2"); //$NON-NLS-1$
		assertEquals("variable2", handle.getVariableName()); //$NON-NLS-1$

		handle = designHandle.getPageVariable("notFound"); //$NON-NLS-1$
		assertNull(handle);

		assertEquals(ULocale.GERMAN, designHandle.getLocale());
		assertEquals("English", designHandle.getLanguage());

		// Test onPrepare
		assertEquals("script of onPrepare", designHandle.getOnPrepare()); //$NON-NLS-1$
		// Test clientInitialize
		assertEquals("script of clientInitialize", designHandle.getClientInitialize()); //$NON-NLS-1$

	}

	/**
	 * Tests design file with css file that can't be found
	 *
	 * @throws Exception
	 */

	public void testOpenBadCssFile() throws Exception {
		openDesign("ReportDesignParseTest_BadCss.xml");//$NON-NLS-1$
		List errorList = design.getAllErrors();
		assertTrue(!errorList.isEmpty());

		String errorCode = ((ErrorDetail) errorList.get(0)).getErrorCode();
		assertEquals(errorCode, CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND);
	}

	/**
	 * Tests writing the properties.
	 *
	 * @throws Exception if any error found.
	 */

	public void testWriter() throws Exception {
		openDesign(fileName, ULocale.ENGLISH);

		designHandle.setProperty(ReportDesign.AUTHOR_PROP, "Report Author"); //$NON-NLS-1$
		designHandle.setLanguage("English");
		designHandle.setSubject("Report Subject"); //$NON-NLS-1$
		designHandle.setProperty(ReportDesign.HELP_GUIDE_PROP, "Help guide"); //$NON-NLS-1$
		designHandle.setProperty(ReportDesign.CREATED_BY_PROP, "Report Creator"); //$NON-NLS-1$
		designHandle.setProperty(ReportDesign.REFRESH_RATE_PROP, "90"); //$NON-NLS-1$
		designHandle.setBase(""); //$NON-NLS-1$
		designHandle.setIncludeResource("new_message"); //$NON-NLS-1$

		designHandle.setProperty(ReportDesign.TITLE_ID_PROP, "New title id"); //$NON-NLS-1$
		designHandle.setProperty(ReportDesign.TITLE_PROP, "New title"); //$NON-NLS-1$
		designHandle.setProperty(ReportDesign.COMMENTS_PROP, "New comments"); //$NON-NLS-1$
		designHandle.setProperty(ReportDesign.DESCRIPTION_ID_PROP, "New description id"); //$NON-NLS-1$
		designHandle.setProperty(ReportDesign.DESCRIPTION_PROP, "New description"); //$NON-NLS-1$
		designHandle.setProperty(ReportDesign.EVENT_HANDLER_CLASS_PROP, "on event"); //$NON-NLS-1$

		designHandle.setNewHandlerOnEachEvent(false);

		designHandle.setInitialize("new initialize script"); //$NON-NLS-1$

		designHandle.setBeforeFactory("new beforeFactory script"); //$NON-NLS-1$
		designHandle.setAfterFactory("new afterFactory script"); //$NON-NLS-1$

		designHandle.setBeforeRender("new beforeRender script"); //$NON-NLS-1$
		designHandle.setAfterRender("new afterRender script"); //$NON-NLS-1$

		designHandle.setOnPageStart("new script of onPageStart"); //$NON-NLS-1$
		designHandle.setOnPageEnd("new script of onPageEnd"); //$NON-NLS-1$

		designHandle.setDisplayName("new display name"); //$NON-NLS-1$
		designHandle.setDisplayNameKey("new display name key"); //$NON-NLS-1$
		designHandle.setIconFile("new iconFile"); //$NON-NLS-1$
		designHandle.setCheatSheet("new cheetSheet"); //$NON-NLS-1$

		// set thumbnail

		designHandle.setThumbnail(Base64.decodeBase64("newthumbnailimageAAA" //$NON-NLS-1$
				.getBytes(IReportDesignModel.CHARSET)));

		// set layout preference
		designHandle.setLayoutPreference(DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT);

		// set external css file
		Iterator iterator = designHandle.includeCssesIterator();
		IncludedCssStyleSheetHandle css = (IncludedCssStyleSheetHandle) iterator.next();
		css.setExternalCssURI("externalCss1.css");//$NON-NLS-1$
		css.setFileName("base2.css");//$NON-NLS-1$

		// bidi

		designHandle.setBidiOrientation(DesignChoiceConstants.BIDI_DIRECTION_LTR);

		// ACL

		designHandle.setEnableACL(false);
		designHandle.setACLExpression("new acl expression test"); //$NON-NLS-1$
		designHandle.setCascadeACL(true);

		// the image DPI could not be set negative value.
		try {
			designHandle.setImageDPI(-10);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE, e.getErrorCode());
		}

		designHandle.setImageDPI(10);

		// sets the variable element value which already existed.
		Expression value = new Expression("testValue", ExpressionType.CONSTANT); //$NON-NLS-1$
		designHandle.setPageVariable("variable2", value); //$NON-NLS-1$

		// sets the variable element value which is not existed.
		value = new Expression("testValue10", ExpressionType.CONSTANT); //$NON-NLS-1$
		designHandle.setPageVariable("variable10", value); //$NON-NLS-1$

		designHandle.setLocale(ULocale.CANADA_FRENCH);

		// Test on prepare
		designHandle.setOnPrepare("new onPrepare script"); //$NON-NLS-1$
		// Test client initialize
		designHandle.setClientInitialize("new clientInitialize script"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Test config variable.
	 *
	 * @throws Exception if any error found.
	 */
	public void testConfigVars() throws Exception {
		openDesign(fileName, ULocale.ENGLISH);

		ConfigVariable configVar = new ConfigVariable();
		configVar.setName("VarToAdd"); //$NON-NLS-1$
		configVar.setValue("ValueToAdd"); //$NON-NLS-1$

		ConfigVariable newConfigVar = new ConfigVariable();
		newConfigVar.setName("VarToReplace"); //$NON-NLS-1$
		newConfigVar.setValue("ValueToReplace"); //$NON-NLS-1$

		ConfigVariable var;

		// Add new config variable and check it

		designHandle.addConfigVariable(configVar);
		var = designHandle.findConfigVariable("VarToAdd"); //$NON-NLS-1$
		assertNotNull(var);
		assertEquals("ValueToAdd", var.getValue()); //$NON-NLS-1$

		// Replace this config variable with new one

		designHandle.replaceConfigVariable(configVar, newConfigVar);
		var = designHandle.findConfigVariable("VarToAdd"); //$NON-NLS-1$
		assertNull(var);
		var = designHandle.findConfigVariable("VarToReplace"); //$NON-NLS-1$
		assertNotNull(var);
		assertEquals("ValueToReplace", var.getValue()); //$NON-NLS-1$

		// Remove this config variable and check it

		designHandle.dropConfigVariable("VarToReplace"); //$NON-NLS-1$
		var = designHandle.findConfigVariable("ValueToReplace"); //$NON-NLS-1$
		assertNull(var);

		// Add a config variable whose name is not provided

		try {
			configVar = new ConfigVariable();
			configVar.setName("   "); //$NON-NLS-1$
			configVar.setValue("value"); //$NON-NLS-1$
			designHandle.addConfigVariable(configVar);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// Add a config variable whose name exists.

		try {
			configVar = new ConfigVariable();
			configVar.setName("var1"); //$NON-NLS-1$
			configVar.setValue("value"); //$NON-NLS-1$
			designHandle.addConfigVariable(configVar);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		// Delete a config variable which doesn't exist

		try {
			designHandle.dropConfigVariable("NotExist"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode());
		}

		// Replace a non-exist config variable with new one

		try {
			configVar = new ConfigVariable();
			configVar.setName("NotExist"); //$NON-NLS-1$
			configVar.setValue("value"); //$NON-NLS-1$
			designHandle.replaceConfigVariable(configVar, newConfigVar);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode());
		}

		// Replace a config variable with invalid one

		configVar = designHandle.findConfigVariable("var1"); //$NON-NLS-1$
		designHandle.replaceConfigVariable(configVar, newConfigVar);
	}

	/**
	 * Test jar file.
	 *
	 * @throws Exception
	 */

	public void testScriptLibs() throws Exception {
		openDesign(scriptLibFileName, ULocale.ENGLISH);
		ScriptLib scriptLib = new ScriptLib();
		scriptLib.setName(null);

		try {
			designHandle.addScriptLib(scriptLib);
			fail("Not allowed set invalid value ");//$NON-NLS-1$

		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());
		}

		try {
			scriptLib.setName("a.jar");//$NON-NLS-1$
			designHandle.addScriptLib(scriptLib);
			fail("Not allowed set invalid value ");//$NON-NLS-1$

		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		scriptLib.setName("x.jar");//$NON-NLS-1$
		assertEquals(3, designHandle.getAllScriptLibs().size());

		designHandle.addScriptLib(scriptLib);

		assertEquals(4, designHandle.getAllScriptLibs().size());

		scriptLib = designHandle.findScriptLib("a.jar");//$NON-NLS-1$
		assertNotNull(scriptLib);

		designHandle.dropScriptLib(scriptLib);
		assertEquals(3, designHandle.getAllScriptLibs().size());

		designHandle.shiftScriptLibs(0, 3);
		assertEquals("c.jar", ((ScriptLibHandle) designHandle.getAllScriptLibs().get(0)).getName());//$NON-NLS-1$
		assertEquals("x.jar", ((ScriptLibHandle) designHandle.getAllScriptLibs().get(1)).getName());//$NON-NLS-1$
		assertEquals("b.jar", ((ScriptLibHandle) designHandle.getAllScriptLibs().get(2)).getName());//$NON-NLS-1$

		designHandle.dropAllScriptLibs();
		assertEquals(0, designHandle.getAllScriptLibs().size());
	}

	/**
	 * Test embedded images.
	 *
	 * @throws Exception if any error found.
	 */
	public void testImages() throws Exception {
		openDesign(fileName, ULocale.ENGLISH);

		EmbeddedImage add = new EmbeddedImage("VarToAdd", "image/bmp"); //$NON-NLS-1$//$NON-NLS-2$
		EmbeddedImage replace = new EmbeddedImage("VarToReplace", "image/gif"); //$NON-NLS-1$//$NON-NLS-2$
		EmbeddedImage image;

		// Add new image and check it

		add.setData("data".getBytes(EmbeddedImage.CHARSET)); //$NON-NLS-1$
		replace.setData("data".getBytes(EmbeddedImage.CHARSET)); //$NON-NLS-1$

		designHandle.addImage(add);
		image = designHandle.findImage("VarToAdd"); //$NON-NLS-1$
		assertNotNull(image);
		assertEquals("image/bmp", image.getType(design)); //$NON-NLS-1$

		// Replace this image with new one

		designHandle.replaceImage(add, replace);
		image = designHandle.findImage("VarToAdd"); //$NON-NLS-1$
		assertNull(image);
		image = designHandle.findImage("VarToReplace"); //$NON-NLS-1$
		assertNotNull(image);
		assertEquals("image/gif", image.getType(design)); //$NON-NLS-1$

		// Remove this image and check it

		designHandle.dropImage("VarToReplace"); //$NON-NLS-1$
		image = designHandle.findImage("ValToReplace"); //$NON-NLS-1$
		assertNull(image);

		// Add an image whose name is not provided

		try {
			add = new EmbeddedImage("  ", "value"); //$NON-NLS-1$ //$NON-NLS-2$
			designHandle.addImage(add);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// Add an image whose type is invalid

		try {
			add = new EmbeddedImage(" test ", "value"); //$NON-NLS-1$ //$NON-NLS-2$
			designHandle.addImage(add);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		// Add an image whose name exists.

		try {
			add = new EmbeddedImage("image1", "image/bmp"); //$NON-NLS-1$ //$NON-NLS-2$
			add.setData("data".getBytes(EmbeddedImage.CHARSET)); //$NON-NLS-1$
			designHandle.addImage(add);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		// Delete an image which doesn't exist

		try {
			designHandle.dropImage("NotExist"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode());
		}

		// Replace a non-exist image with new one

		try {
			add = new EmbeddedImage("NotExist", "image/bmp"); //$NON-NLS-1$ //$NON-NLS-2$
			designHandle.replaceImage(add, replace);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode());
		}

		// Replace an image with invalid one

		try {
			add = designHandle.findImage("image1"); //$NON-NLS-1$
			replace = new EmbeddedImage("replace", "wrong"); //$NON-NLS-1$//$NON-NLS-2$
			designHandle.replaceImage(add, replace);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		try {
			EmbeddedImage tmp = new EmbeddedImage("VarToAdd"); //$NON-NLS-1$
			tmp.setData("data".getBytes(EmbeddedImage.CHARSET)); //$NON-NLS-1$
			designHandle.addImage(tmp);

			tmp = new EmbeddedImage("VarToAdd", DesignChoiceConstants.IMAGE_TYPE_IMAGE_BMP); //$NON-NLS-1$
			tmp.setData("data".getBytes(EmbeddedImage.CHARSET)); //$NON-NLS-1$
			designHandle.addImage(tmp);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}
	}

	/**
	 * Checks the semantic error of ReportDesign.
	 *
	 * @throws Exception
	 */

	public void testSemanticError() throws Exception {
		openDesign(semanticCheckFileName);

		List<ErrorDetail> errors = design.getAllErrors();

		assertEquals(5, errors.size());

		int i = 0;

		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, errors.get(i++).getErrorCode());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, errors.get(i++).getErrorCode());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, errors.get(i++).getErrorCode());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, errors.get(i++).getErrorCode());
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE, errors.get(i++).getErrorCode());
	}

	/**
	 * Tests opening a report file which is of the unsupported version or invalid
	 * version.
	 */

	public void testUnsupportedVersion() {
		try {
			openDesign("UnsupportedVersionTest.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			assertEquals(DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_VERSION,
					((ErrorDetail) e.getErrorList().get(0)).getErrorCode());
		}

		try {
			openDesign("InvalidVersionTest.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			assertEquals(DesignParserException.DESIGN_EXCEPTION_INVALID_VERSION,
					((ErrorDetail) e.getErrorList().get(0)).getErrorCode());
		}
	}

	/**
	 * Tests data source parameter binding.
	 *
	 * @throws Exception
	 */

	public void testCompatibilityDatasourceParamBinding() throws Exception {
		openDesign(datasourceBindingsFileName, ULocale.ENGLISH);

		save();
		assertTrue(compareFile(goldenFileName_2));
	}

	/**
	 * Tests data source parameter binding.
	 *
	 * @throws Exception
	 */

	public void testParseDesignInJarFile() throws Exception {
		String jarFileName = copyContentToFile("input/" //$NON-NLS-1$
				+ "testRead.jar"); //$NON-NLS-1$

		SessionHandle session = DesignEngine.newSession(ULocale.getDefault());
		designHandle = session.openDesign("jar:file:" + jarFileName //$NON-NLS-1$
				+ "!/test/testRead.rptdesign"); //$NON-NLS-1$
		assertNotNull(designHandle);

		assertNotNull(designHandle.getSystemId());

		LabelHandle label = (LabelHandle) designHandle.findElement("labelfromLib"); //$NON-NLS-1$
		assertNotNull(label);
		assertEquals("a.labelfromLib", label.getElement().getExtendsName()); //$NON-NLS-1$
		assertEquals("blue", label.getProperty(IStyleModel.COLOR_PROP)); //$NON-NLS-1$

	}

	/**
	 * Tests open empty design file.
	 *
	 * @throws Exception
	 */

	public void testParseEmptyDesignFile() {
		try {
			openDesign("EmptyDesignFile.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			assertEquals(DesignFileException.DESIGN_EXCEPTION_INVALID_XML, e.getErrorCode());
		}
	}

	/**
	 * Tests open a design file will reading the line number.
	 *
	 * @throws DesignFileException
	 *
	 * @throws Exception
	 */

	public void testParseReadingLineNumber() throws DesignFileException {
		openDesign("LineNumberParseTest.xml"); //$NON-NLS-1$
		TableHandle table = (TableHandle) designHandle.findElement("table1");//$NON-NLS-1$
		assertEquals(376, designHandle.getLineNo(table));

		CellHandle cell = (CellHandle) designHandle.getElementByID(45);
		assertEquals(398, designHandle.getLineNo(cell));

		ExtendedItemHandle chart = (ExtendedItemHandle) designHandle.getElementByID(34023);
		assertEquals(403, designHandle.getLineNo(chart));

		// test embedded image
		EmbeddedImageHandle image = (EmbeddedImageHandle) designHandle.getAllImages().get(0);
		assertEquals(410, designHandle.getLineNo(image));

		// test include library
		LibraryHandle library = (LibraryHandle) designHandle.getLibraries().get(0);
		assertEquals(8, designHandle.getLineNo(library));

		// test theme property
		ThemeHandle theme = designHandle.getTheme();
		assertEquals(6, designHandle.getLineNo(theme));

		// test slot of table
		SlotHandle footer = table.getFooter();
		assertEquals(395, designHandle.getLineNo(footer));

		// test slot of design
		SlotHandle body = designHandle.getBody();
		assertEquals(375, designHandle.getLineNo(body));

		SlotHandle pages = designHandle.getMasterPages();
		assertEquals(362, designHandle.getLineNo(pages));

		SimpleMasterPageHandle page = (SimpleMasterPageHandle) designHandle.findMasterPage("Simple MasterPage"); //$NON-NLS-1$

		SlotHandle pageHeader = page.getPageHeader();
		assertEquals(364, designHandle.getLineNo(pageHeader));

		SlotHandle pageFooter = page.getPageFooter();
		assertEquals(367, designHandle.getLineNo(pageFooter));

		// test result set column
		DataSetHandle dataSetHandle = designHandle.findDataSet("Data Set"); //$NON-NLS-1$
		ResultSetColumnHandle resultSetColumnHandle = (ResultSetColumnHandle) dataSetHandle.getCachedMetaDataHandle()
				.getResultSet().getAt(0);
		assertEquals(24, designHandle.getLineNo(resultSetColumnHandle));

		CubeHandle cubeHandle = designHandle.findCube("Cube"); //$NON-NLS-1$
		PropertyHandle propHandle = cubeHandle.getPropertyHandle(ICubeModel.DIMENSIONS_PROP);
		assertEquals(419, designHandle.getLineNo(propHandle));

		HierarchyHandle hierarchy = cubeHandle.getDimension("Group") //$NON-NLS-1$
				.getDefaultHierarchy();

		StructureHandle attr = (StructureHandle) hierarchy.getLevel("Year") //$NON-NLS-1$
				.attributesIterator().next();
		assertEquals(429, designHandle.getLineNo(attr));

		attr = (StructureHandle) hierarchy.getLevel("Quarter") //$NON-NLS-1$
				.attributesIterator().next();
		assertEquals(440, designHandle.getLineNo(attr));

		attr = (StructureHandle) hierarchy.getLevel("Month") //$NON-NLS-1$
				.attributesIterator().next();
		assertEquals(451, designHandle.getLineNo(attr));

		propHandle = cubeHandle.getPropertyHandle(ICubeModel.MEASURE_GROUPS_PROP);
		assertEquals(463, designHandle.getLineNo(propHandle));

		List<VariableElementHandle> variables = designHandle.getPageVariables();
		assertEquals("report", variables.get(0).getName());
		assertEquals(477, designHandle.getLineNo(variables.get(0)));

		assertEquals("page", variables.get(1).getName());
		assertEquals(480, designHandle.getLineNo(variables.get(1)));

	}

	/**
	 * Tests the created-by in moduleOption.
	 *
	 * @throws Exception
	 */
	public void testWriterForCreatedBy() throws Exception {
		openDesign(fileName);

		// clear all properties
		designHandle.clearAllProperties();

		ModuleOption options = new ModuleOption();
		options.setProperty(ModuleOption.CREATED_BY_KEY, "createdByOption"); //$NON-NLS-1$
		design.setOptions(options);

		designHandle.setCreatedBy("created by Birt2.2"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("ReportDesignParseTest_golden_3.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests report design which contains the library which could not be found.
	 *
	 * @throws Exception
	 */
	public void testNotExistLibrary() throws Exception {
		openDesign("NotExistLibraryTest.xml"); //$NON-NLS-1$

		List errors = design.getAllErrors();

		assertEquals(1, errors.size());
		assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND, ((ErrorDetail) errors.get(0)).getErrorCode());
	}

	/**
	 * Tests the case that the report design can be opended if there is duplicate
	 * element id. TED 28431.
	 *
	 * @throws Exception
	 */

	public void testDuplicateID() throws Exception {
		openDesign("ReportDesignParseTest_3.xml"); //$NON-NLS-1$
		List<ErrorDetail> errors = design.getAllErrors();
		assertEquals(1, errors.size());
		assertEquals(DesignParserException.DESIGN_EXCEPTION_DUPLICATE_ELEMENT_ID, errors.get(0).getErrorCode());

		save();
		assertTrue(compareFile("ReportDesignParseTest_golden_1.xml")); //$NON-NLS-1$
	}
}
