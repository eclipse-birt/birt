/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetHandleAdapter;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.VariableElement;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IVariableElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.StyleUtil;

import com.ibm.icu.util.ULocale;

/**
 * Represents the overall report design. The report design defines a set of
 * properties that describe the design as a whole like author, base and comments
 * etc.
 * <p>
 *
 * Besides properties, it also contains a variety of elements that make up the
 * report. These include:
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Content Item</th>
 * <th width="40%">Description</th>
 *
 * <tr>
 * <td>Code Modules</td>
 * <td>Global scripts that apply to the report as a whole.</td>
 * </tr>
 *
 * <tr>
 * <td>Parameters</td>
 * <td>A list of Parameter elements that describe the data that the user can
 * enter when running the report.</td>
 * </tr>
 *
 * <tr>
 * <td>Data Sources</td>
 * <td>The connections used by the report.</td>
 * </tr>
 *
 * <tr>
 * <td>Data Sets</td>
 * <td>Data sets defined in the design.</td>
 * </tr>
 *
 * <tr>
 * <td>Color Palette</td>
 * <td>A set of custom color names as part of the design.</td>
 * </tr>
 *
 * <tr>
 * <td>Styles</td>
 * <td>User-defined styles used to format elements in the report. Each style
 * must have a unique name within the set of styles for this report.</td>
 * </tr>
 *
 * <tr>
 * <td>Page Setup</td>
 * <td>The layout of the master pages within the report.</td>
 * </tr>
 *
 * <tr>
 * <td>Components</td>
 * <td>Reusable report items defined in this design. Report items can extend
 * these items. Defines a "private library" for this design.</td>
 * </tr>
 *
 * <tr>
 * <td>Body</td>
 * <td>A list of the visual report content. Content is made up of one or more
 * sections. A section is a report item that fills the width of the page. It can
 * contain Text, Grid, List, Table, etc. elements</td>
 * </tr>
 *
 * <tr>
 * <td>Scratch Pad</td>
 * <td>Temporary place to move report items while restructuring a report.</td>
 * </tr>
 *
 * <tr>
 * <td>Translations</td>
 * <td>The list of externalized messages specifically for this report.</td>
 * </tr>
 *
 * <tr>
 * <td>Images</td>
 * <td>A list of images embedded in this report.</td>
 * </tr>
 *
 * </table>
 *
 * <p>
 * Module allow to use the components defined in <code>Library</code>.
 * <ul>
 * <li>User can call {@link #includeLibrary(String, String)}to include one
 * library.
 * <li>User can create one report item based on the one in library, and add it
 * into design file.
 * <li>User can use style, data source, and data set, which are defined in
 * library, in design file.
 * </ul>
 *
 * <pre>
 *                      // Include one library
 *
 *                      ReportDesignHandle designHandle = ...;
 *                      designHandle.includeLibrary( &quot;libA.rptlibrary&quot;, &quot;LibA&quot; );
 *                      LibraryHandle libraryHandle = designHandle.getLibrary(&quot;LibA&quot;);
 *
 *                      // Create one label based on the one in library
 *
 *                      LabelHandle labelHandle = (LabelHandle) libraryHandle.findElement(&quot;companyNameLabel&quot;);
 *                      LabelHandle myLabelHandle = (LabelHandle) designHandle.getElementFactory().newElementFrom( labelHandle, &quot;myLabel&quot; );
 *
 *                      // Add the new label into design file
 *
 *                      designHandle.getBody().add(myLabelHandle);
 *
 * </pre>
 *
 * @see org.eclipse.birt.report.model.elements.ReportDesign
 */
class ReportDesignHandleImpl extends LayoutModuleHandle implements IReportDesignModel {

	/**
	 * Constructs a handle with the given design. The application generally does not
	 * create handles directly. Instead, it uses one of the navigation methods
	 * available on other element handles.
	 *
	 * @param design the report design
	 */

	public ReportDesignHandleImpl(ReportDesign design) {
		super(design);
	}

	/**
	 * Returns the script called at the end of the Factory after closing the report
	 * document (if any). This is the last method called in the Factory.
	 *
	 * @return the script
	 */

	public String getAfterFactory() {
		return getStringProperty(AFTER_FACTORY_METHOD);
	}

	/**
	 * Returns the script called after starting a presentation time action.
	 *
	 * @return the script
	 */

	public String getAfterRender() {
		return getStringProperty(AFTER_RENDER_METHOD);
	}

	/**
	 * Returns the base directory to use when computing relative links from this
	 * report. Especially used for searching images, library and so.
	 *
	 * @return the base directory
	 */

	public String getBase() {
		return module.getStringProperty(module, BASE_PROP);
	}

	/**
	 * Returns the script called at the start of the Factory after the initialize( )
	 * method and before opening the report document (if any).
	 *
	 * @return the script
	 */

	public String getBeforeFactory() {
		return getStringProperty(BEFORE_FACTORY_METHOD);
	}

	/**
	 * Returns the script called before starting a presentation time action.
	 *
	 * @return the script
	 */

	public String getBeforeRender() {
		return getStringProperty(BEFORE_RENDER_METHOD);
	}

	/**
	 * Returns a slot handle to work with the sections in the report's Body slot.
	 * The order of sections within the slot determines the order in which the
	 * sections print.
	 *
	 * @return A handle for working with the report sections.
	 */

	public SlotHandle getBody() {
		return getSlot(BODY_SLOT);
	}

	/**
	 * Returns the refresh rate when viewing the report.
	 *
	 * @return the refresh rate
	 */

	public int getRefreshRate() {
		return getIntProperty(REFRESH_RATE_PROP);
	}

	/**
	 * Returns a slot handle to work with the scratched elements within the report,
	 * which are no longer needed or are in the process of rearranged.
	 *
	 * @return A handle for working with the scratched elements.
	 */

	public SlotHandle getScratchPad() {
		return getSlot(SCRATCH_PAD_SLOT);
	}

	/**
	 * Returns the list of all the included script file of the libraries. Each one
	 * is the instance of <code>IncludeScriptHandle</code>
	 *
	 * @return the iterator of included scripts.
	 */

	public Iterator includeLibraryScriptsIterator() {
		List<Library> libList = module.getAllLibraries();
		List<Object> includeLibScriptList = new ArrayList<Object>();
		if (libList != null) {
			for (Library lib : libList) {
				PropertyHandle propHandle = lib.getHandle(lib).getPropertyHandle(INCLUDE_SCRIPTS_PROP);

				Iterator scriptIter = propHandle.iterator();
				while (scriptIter.hasNext()) {
					includeLibScriptList.add(scriptIter.next());
				}
			}
		}
		return includeLibScriptList.iterator();
	}

	/**
	 * Sets the script called at the end of the Factory after closing the report
	 * document (if any). This is the last method called in the Factory.
	 *
	 * @param value the script to set.
	 */

	public void setAfterFactory(String value) {
		try {
			setStringProperty(AFTER_FACTORY_METHOD, value);
		} catch (SemanticException e) {
			assert false;
		}
	}

	/**
	 * Sets the script called after starting a presentation time action.
	 *
	 * @param value the script to set.
	 */

	public void setAfterRender(String value) {
		try {
			setStringProperty(AFTER_RENDER_METHOD, value);
		} catch (SemanticException e) {
			assert false;
		}
	}

	/**
	 * Sets the base directory to use when computing relative links from this
	 * report. Especially used for searching images, library and so.
	 *
	 * @param base the base directory to set
	 */

	public void setBase(String base) {
		try {
			setProperty(BASE_PROP, base);
		} catch (SemanticException e) {
			assert false;
		}
	}

	/**
	 * Sets the script called at the start of the Factory after the initialize( )
	 * method and before opening the report document (if any).
	 *
	 * @param value the script to set.
	 */

	public void setBeforeFactory(String value) {
		try {
			setStringProperty(BEFORE_FACTORY_METHOD, value);
		} catch (SemanticException e) {
			assert false;
		}
	}

	/**
	 * Sets the script called before starting a presentation time action.
	 *
	 * @param value the script to set.
	 */

	public void setBeforeRender(String value) {
		try {
			setStringProperty(BEFORE_RENDER_METHOD, value);
		} catch (SemanticException e) {
			assert false;
		}
	}

	/**
	 * Sets the refresh rate when viewing the report.
	 *
	 * @param rate the refresh rate
	 */

	public void setRefreshRate(int rate) {
		try {
			setIntProperty(REFRESH_RATE_PROP, rate);
		} catch (SemanticException e) {
			assert false;
		}
	}

	/**
	 * Returns a slot handle to work with the styles within the report. Note that
	 * the order of the styles within the slot is unimportant.
	 *
	 * @return A handle for working with the styles.
	 *
	 */
	@Override
	public SlotHandle getStyles() {
		return getSlot(IInternalReportDesignModel.STYLE_SLOT);
	}

	/**
	 * Gets all CSS styles sheet
	 *
	 * @return each item is <code>CssStyleSheetHandle</code>
	 */
	public List<CssStyleSheetHandle> getAllCssStyleSheets() {
		ReportDesign design = (ReportDesign) getElement();
		List<CssStyleSheetHandle> allStyles = new ArrayList<CssStyleSheetHandle>();
		List<CssStyleSheet> csses = design.getCsses();
		for (CssStyleSheet sheet : csses) {
			allStyles.add(sheet.handle(getModule()));
		}
		return allStyles;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#importCssStyles(org.
	 *      eclipse.birt.report.model.api.css.CssStyleSheetHandle, java.util.List)
	 */

	@Override
	public void importCssStyles(CssStyleSheetHandle stylesheet, List selectedStyles) {
		if (stylesheet == null || selectedStyles == null || selectedStyles.isEmpty()) {
			// do nothing now.
			return;
		}

		ActivityStack stack = module.getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.IMPORT_CSS_STYLES_MESSAGE));
		for (Object selectedStyle : selectedStyles) {
			if (!(selectedStyle instanceof SharedStyleHandle)) {
				continue;
			}
			SharedStyleHandle style = (SharedStyleHandle) selectedStyle;
			if (stylesheet.findStyle(style.getName()) != null) {
				// Copy CssStyle to Style
				SharedStyleHandle newStyle = StyleUtil.transferCssStyleToSharedStyle(module, style);
				if (newStyle == null) {
					continue;
				}
				module.makeUniqueName(newStyle.getElement());
				try {
					addElement(newStyle, IInternalReportDesignModel.STYLE_SLOT);
				} catch (ContentException | NameException e) {
					assert false;
				}
			}
		}
		stack.commit();
	}

	/**
	 * Sets the resource key of the display name.
	 *
	 * @param displayNameKey the resource key of the display name
	 * @throws SemanticException if the display name resource-key property is locked
	 *                           or not defined on this design.
	 */

	public void setDisplayNameKey(String displayNameKey) throws SemanticException {
		setStringProperty(DISPLAY_NAME_ID_PROP, displayNameKey);
	}

	/**
	 * Gets the resource key of the display name.
	 *
	 * @return the resource key of the display name
	 */

	public String getDisplayNameKey() {
		return getStringProperty(DISPLAY_NAME_ID_PROP);
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the display name
	 * @throws SemanticException if the display name property is locked or not
	 *                           defined on this design.
	 */

	public void setDisplayName(String displayName) throws SemanticException {
		setStringProperty(DISPLAY_NAME_PROP, displayName);
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */

	public String getDisplayName() {
		return getStringProperty(DISPLAY_NAME_PROP);
	}

	/**
	 * Sets the design icon/thumbnail file path.
	 *
	 * @param iconFile the design icon/thumbnail file path to set
	 * @throws SemanticException if the property is locked or not defined on this
	 *                           design.
	 */

	public void setIconFile(String iconFile) throws SemanticException {
		setStringProperty(ICON_FILE_PROP, iconFile);
	}

	/**
	 * Gets the design icon/thumbnail file path.
	 *
	 * @return the design icon/thumbnail file path
	 */

	public String getIconFile() {
		return getStringProperty(ICON_FILE_PROP);
	}

	/**
	 * Sets the design cheat sheet file path.
	 *
	 * @param cheatSheet the design cheat sheet file path to set
	 * @throws SemanticException if the property is locked or not defined on this
	 *                           design.
	 */

	public void setCheatSheet(String cheatSheet) throws SemanticException {
		setStringProperty(CHEAT_SHEET_PROP, cheatSheet);
	}

	/**
	 * Gets the design cheat sheet file path.
	 *
	 * @return the design cheat sheet file path
	 */

	public String getCheatSheet() {
		return getStringProperty(CHEAT_SHEET_PROP);
	}

	/**
	 * Sets the thumbnail image encoded in ISO-8859-1.
	 *
	 * @param data the thumbnail image to set
	 * @throws SemanticException if the property is locked or not defined on this
	 *                           design.
	 */

	public void setThumbnail(byte[] data) throws SemanticException {
		String toSet = null;

		try {
			if (data != null) {
				toSet = new String(data, CHARSET);
			}
		} catch (UnsupportedEncodingException e) {
			assert false;
		}

		setStringProperty(THUMBNAIL_PROP, toSet);
	}

	/**
	 * Gets the thumbnail image encoded in ISO-8859-1.
	 *
	 * @return the thumbnail image
	 */

	public byte[] getThumbnail() {
		return ((ReportDesign) module).getThumbnail();
	}

	/**
	 * Deletes the thumbnail image in the design.
	 *
	 * @throws SemanticException if the property is locked or not defined on this
	 *                           design.
	 */

	public void deleteThumbnail() throws SemanticException {
		clearProperty(THUMBNAIL_PROP);
	}

	/**
	 * Gets all bookmarks defined in this module.
	 *
	 * @return All bookmarks defined in this module.
	 */
	public List<String> getAllBookmarks() {
		// bookmark value in row, report item and listing group are the same
		// now.

		List<?> bookmarks = ((ReportDesign) module).collectPropValues(BODY_SLOT,
				IInternalReportItemModel.BOOKMARK_PROP);

		List<String> resultList = new ArrayList<String>();
		Iterator<?> iterator = bookmarks.iterator();
		while (iterator.hasNext()) {
			Expression expr = (Expression) iterator.next();
			resultList.add(expr.getStringExpression());
		}
		return resultList;
	}

	/**
	 * Gets all TOCs defined in this module.
	 *
	 * @return All TOCs defined in this module.
	 */
	public List<String> getAllTocs() {
		List<?> tocs = ((ReportDesign) module).collectPropValues(BODY_SLOT, IInternalReportItemModel.TOC_PROP);

		// TODO merge with IGroupElementModel.TOC_PROP.

		List<String> resultList = new ArrayList<String>();
		Iterator<?> iterator = tocs.iterator();
		while (iterator.hasNext()) {
			TOC toc = (TOC) iterator.next();
			resultList.add(toc.getStringProperty(module, TOC.TOC_EXPRESSION));
		}
		return resultList;
	}

	/**
	 * Gets report items which holds a template definition, that is, report item in
	 * body slot and page slot. Notice, nested template items is excluded.
	 *
	 * @return report items which holds a template definition, nested template items
	 *         is excluded.
	 */

	public List<DesignElementHandle> getReportItemsBasedonTempalates() {
		ArrayList<DesignElementHandle> rtnList = new ArrayList<DesignElementHandle>();
		ArrayList<DesignElement> tempList = new ArrayList<DesignElement>();

		List contents = new ContainerContext(getElement(), BODY_SLOT).getContents(module);
		contents.addAll(new ContainerContext(getElement(), PAGE_SLOT).getContents(module));

		findTemplateItemIn(contents.iterator(), tempList);

		for (Iterator<DesignElement> iter = tempList.iterator(); iter.hasNext();) {
			rtnList.add(iter.next().getHandle(module));
		}

		return Collections.unmodifiableList(rtnList);
	}

	/**
	 * Auxilary method, help to find design element which holds and template
	 * definition.
	 *
	 * @param contents the contents to search.
	 * @param addTo    The list to add to.
	 */
	private void findTemplateItemIn(Iterator<?> contents, List<DesignElement> addTo) {
		for (; contents.hasNext();) {
			DesignElement e = (DesignElement) contents.next();
			if (e.isTemplateParameterValue(module)) {
				addTo.add(e);
				continue;
			}

			LevelContentIterator children = new LevelContentIterator(module, e, 1);

			findTemplateItemIn(children, addTo);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getCubes()
	 */
	@Override
	public SlotHandle getCubes() {
		return getSlot(CUBE_SLOT);
	}

	/**
	 * Gets the layout preference of this report design. It can be one of the
	 * following:
	 *
	 * <ul>
	 * <li><code>DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT
	 * </code>
	 * <li><code>
	 * DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT</code>
	 * </ul>
	 *
	 * @return layout preference of report design
	 */
	public String getLayoutPreference() {
		return getStringProperty(LAYOUT_PREFERENCE_PROP);
	}

	/**
	 * Sets the layout preference of this report design. The input layout can be one
	 * of the following:
	 * <ul>
	 * <li><code>DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT
	 * </code>
	 * <li><code>
	 * DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT</code>
	 * </ul>
	 *
	 * @param layout the layout to set
	 * @throws SemanticException if value is invalid
	 */
	public void setLayoutPreference(String layout) throws SemanticException {
		setStringProperty(LAYOUT_PREFERENCE_PROP, layout);
	}

	/**
	 * Returns the iterator over all included css style sheets. Each one is the
	 * instance of <code>IncludedCssStyleSheetHandle</code>
	 *
	 * @return the iterator over all included css style sheets.
	 */
	@SuppressWarnings("rawtypes")
	public Iterator includeCssesIterator() {
		PropertyHandle propHandle = getPropertyHandle(IInternalReportDesignModel.CSSES_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Gets <code>IncludedCssStyleSheetHandle</code> by file name.
	 *
	 * @deprecated
	 * @param fileName the file name
	 * @return the includedCssStyleSheet handle.
	 */
	@Deprecated
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByFileName(String fileName) {

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findIncludedCssStyleSheetHandleByFileName(fileName);

	}

	/**
	 * Gets <code>IncludedCssStyleSheetHandle</code> by properties.
	 *
	 * @param fileName
	 * @param externalCssURI
	 * @param useExternalCss
	 * @return the includedCssStyleSheet handle.
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByProperties(String fileName,
			String externalCssURI, boolean useExternalCss) {

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findIncludedCssStyleSheetHandleByProperties(fileName, externalCssURI, useExternalCss);

	}

	/**
	 * Gets <code>CssStyleSheetHandle</code> by file name.
	 *
	 * @deprecated
	 * @param fileName the file name.
	 *
	 * @return the cssStyleSheet handle.
	 */
	@Deprecated
	public CssStyleSheetHandle findCssStyleSheetHandleByFileName(String fileName) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findCssStyleSheetHandleByFileName(fileName);

	}

	/**
	 * Gets <code>CssStyleSheetHandle</code> by file name.
	 *
	 * @param fileName
	 * @param externalCssURI
	 * @param useExternalCss
	 * @return the cssStyleSheet handle.
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByProperties(String fileName, String externalCssURI,
			boolean useExternalCss) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findCssStyleSheetHandleByProperties(fileName, externalCssURI, useExternalCss);

	}

	/**
	 * Includes one css with the given css file name. The new css will be appended
	 * to the css list.
	 *
	 * @param sheetHandle css style sheet handle
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list.
	 */

	public void addCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCss(sheetHandle);
	}

	/**
	 * Includes one css with the given css file name. The new css will be appended
	 * to the css list.
	 *
	 * @deprecated
	 * @param fileName css file name
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list.
	 */

	@Deprecated
	public void addCss(String fileName) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCss(fileName);
	}

	/**
	 * Includes one css with the given css file name. The new css will be appended
	 * to the css list.
	 *
	 * @param fileName       CSS file name
	 * @param externalCssURI external CSS URI
	 * @param useExternalCss use external CSS
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list.
	 */
	public void addCssByProperties(String fileName, String externalCssURI, boolean useExternalCss)
			throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCssbyProperties(fileName, externalCssURI, useExternalCss);
	}

	/**
	 * Includes one CSS structure with the given IncludedCssStyleSheet. The new css
	 * will be appended to the CSS list.
	 *
	 * @param cssStruct the CSS structure
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list.
	 */

	public void addCss(IncludedCssStyleSheet cssStruct) throws SemanticException {
		if (cssStruct == null) {
			return;
		}

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCss(cssStruct);
	}

	/**
	 * Renames both <code>IncludedCssStyleSheet</code> and <code>CSSStyleSheet<code>
	 * to newFileName.
	 *
	 * @deprecated
	 * @param handle      the includedCssStyleSheetHandle
	 * @param newFileName the new file name
	 * @throws SemanticException
	 */
	@Deprecated
	public void renameCss(IncludedCssStyleSheetHandle handle, String newFileName) throws SemanticException {

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.renameCss(handle, newFileName);
	}

	/**
	 * Renames both <code>IncludedCssStyleSheet</code> and <code>CSSStyleSheet<code>
	 * to newFileName.
	 *
	 * @param handle         the includedCssStyleSheetHandle
	 * @param fileName       the file name
	 * @param externalCssURI external CSS URI
	 * @param useExternalCss use external CSS
	 * @throws SemanticException
	 */
	public void renameCssByProperties(IncludedCssStyleSheetHandle handle, String fileName, String externalCssURI,
			boolean useExternalCss) throws SemanticException {

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.renameCssByProperties(handle, fileName, externalCssURI, useExternalCss);
	}

	/**
	 * Checks css can be renamed or not.
	 *
	 * @deprecated
	 * @param handle      the included css style sheet handle.
	 * @param newFileName the new file name.
	 * @return <code>true</code> can be renamed.else return <code>false</code>
	 * @throws SemanticException
	 */
	@Deprecated
	public boolean canRenameCss(IncludedCssStyleSheetHandle handle, String newFileName) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canRenameCss(handle, newFileName);
	}

	/**
	 * Checks css can be renamed or not.
	 *
	 * @param handle         the included css style sheet handle.
	 * @param fileName       the file name
	 * @param externalCssURI external CSS URI
	 * @param useExternalCss use external CSS
	 * @return <code>true</code> can be renamed.else return <code>false</code>
	 * @throws SemanticException
	 */
	public boolean canRenameCssByProperties(IncludedCssStyleSheetHandle handle, String fileName, String externalCssURI,
			boolean useExternalCss) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canRenameCssByProperties(handle, fileName, externalCssURI, useExternalCss);
	}

	/**
	 * Drops the given css style sheet of this design file.
	 *
	 * @param sheetHandle the css to drop
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet
	 *             </code>    structure list. Or it maybe because that the given
	 *                           css is not found in the design. Or that the css has
	 *                           descedents in the current module
	 */

	public void dropCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.dropCss(sheetHandle);
	}

	/**
	 * Check style sheet can be droped or not.
	 *
	 * @param sheetHandle
	 * @return <code>true</code> can be dropped.else return <code>false</code>
	 */

	public boolean canDropCssStyleSheet(CssStyleSheetHandle sheetHandle) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canDropCssStyleSheet(sheetHandle);
	}

	/**
	 * Check style sheet can be added or not.
	 *
	 * @param sheetHandle
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public boolean canAddCssStyleSheet(CssStyleSheetHandle sheetHandle) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheet(sheetHandle);
	}

	/**
	 * Check style sheet can be added or not.
	 *
	 * @deprecated
	 * @param fileName
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	@Deprecated
	public boolean canAddCssStyleSheet(String fileName) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheet(fileName);
	}

	/**
	 * Check style sheet can be added or not.
	 *
	 * @param fileName
	 * @param externalCssURI
	 * @param useExternalCss
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public boolean canAddCssStyleSheetByProperties(String fileName, String externalCssURI, boolean useExternalCss) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheetByProperties(fileName, externalCssURI, useExternalCss);
	}

	/**
	 * Reloads the css with the given css file path. If the css already is included
	 * directly, reload it. If the css is not included, exception will be thrown.
	 *
	 * @param sheetHandle css style sheet handle.
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list. Or it
	 *                           maybe because that the given css is not found in
	 *                           the design. Or that the css has descedents in the
	 *                           current module
	 */

	public void reloadCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.reloadCss(sheetHandle);
	}

	/**
	 * Gets Bidi orientation value. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>BIDI_DIRECTION_LTR</code>
	 * <li><code>BIDI_DIRECTION_RTL</code>
	 * </ul>
	 *
	 * @return the Bidi orientation value
	 *
	 */

	public String getBidiOrientation() {
		return getStringProperty(BIDI_ORIENTATION_PROP);
	}

	/**
	 * Sets Bidi orientation value. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>BIDI_DIRECTION_LTR</code>
	 * <li><code>BIDI_DIRECTION_RTL</code>
	 * </ul>
	 *
	 * @param bidiOrientation orientation value to be set
	 * @throws SemanticException
	 */

	public void setBidiOrientation(String bidiOrientation) throws SemanticException {
		setStringProperty(BIDI_ORIENTATION_PROP, bidiOrientation);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#isDirectionRTL()
	 */

	@Override
	public boolean isDirectionRTL() {
		return DesignChoiceConstants.BIDI_DIRECTION_RTL.equals(getBidiOrientation());
	}

	/**
	 * Returns <code>true</code> if the ACL feature is enable; otherwise false. By
	 * default, it is <code>false</code>.
	 *
	 * @return the flag to control whether to enable ACL
	 *
	 */

	public boolean isEnableACL() {

		return getBooleanProperty(ENABLE_ACL_PROP);
	}

	/**
	 * Sets the flag to control whether to enable ACL.
	 *
	 * @param enableACL true if to enable ACL, otherwise false
	 * @throws SemanticException if the property is locked by masks
	 *
	 */

	public void setEnableACL(boolean enableACL) throws SemanticException {
		setBooleanProperty(ENABLE_ACL_PROP, enableACL);
	}

	/**
	 * Returns the ACL expression associated with the design instance.
	 *
	 * @return the expression in string
	 *
	 */

	public String getACLExpression() {
		return getStringProperty(ACL_EXPRESSION_PROP);
	}

	/**
	 * Sets the ACL expression associated with the design instance.
	 *
	 * @param expr the expression in string
	 * @throws SemanticException if the property is locked by masks
	 *
	 */

	public void setACLExpression(String expr) throws SemanticException {
		setStringProperty(ACL_EXPRESSION_PROP, expr);
	}

	/**
	 * Returns <code>true</code> (the default), the design's ACL is automatically
	 * propagated to all its directly contained child elements and are added to
	 * their ACLs. Otherwise <code>false</code>.
	 *
	 * @return the flag to control whether to cascade ACL
	 *
	 */

	public boolean cascadeACL() {

		return getBooleanProperty(CASCADE_ACL_PROP);
	}

	/**
	 * Sets the flag to control whether to cascade ACL
	 *
	 * @param cascadeACL <code>true</code> (the default), a design's ACL is
	 *                   automatically propagated to all its directly contained
	 *                   child elements and are added to their ACLs. Otherwise
	 *                   <code>false</code>.
	 * @throws SemanticException if the property is locked by masks
	 *
	 */

	public void setCascadeACL(boolean cascadeACL) throws SemanticException {
		setBooleanProperty(CASCADE_ACL_PROP, cascadeACL);
	}

	/**
	 * Gets the image DPI of the report design. This property can ensure image in
	 * report design may be displayed as same size at design time as at run time.
	 *
	 * @return the value of image DPI.
	 */
	public int getImageDPI() {
		return getIntProperty(IMAGE_DPI_PROP);
	}

	/**
	 * Sets the image DPI of the report design. This property can ensure image in
	 * report design may be displayed as same size at design time as at run time.
	 *
	 * @param imageDPI the value of image DPI.
	 * @throws SemanticException if the property is locked by masks
	 */
	public void setImageDPI(int imageDPI) throws SemanticException {
		setIntProperty(IMAGE_DPI_PROP, imageDPI);
	}

	/**
	 * Gets the script of onPageStart method.
	 *
	 * @return the script of onPageStart method.
	 */
	public String getOnPageStart() {
		return getStringProperty(ON_PAGE_START_METHOD);
	}

	/**
	 * Sets the script of onPageStart method.
	 *
	 * @param onPageStart the script of onPageStart method.
	 * @throws SemanticException if the property is locked by masks.
	 */
	public void setOnPageStart(String onPageStart) throws SemanticException {
		setStringProperty(ON_PAGE_START_METHOD, onPageStart);
	}

	/**
	 * Gets the script of onPageEnd method.
	 *
	 * @return the script of onPageEnd method.
	 */
	public String getOnPageEnd() {
		return getStringProperty(ON_PAGE_END_METHOD);
	}

	/**
	 * Sets the script of onPageEnd method.
	 *
	 * @param onPageEnd the script of onPageEnd method.
	 * @throws SemanticException if the property is locked by masks.
	 */
	public void setOnPageEnd(String onPageEnd) throws SemanticException {
		setStringProperty(ON_PAGE_END_METHOD, onPageEnd);
	}

	/**
	 * Gets the pageVariables list value which contains
	 * <code>VariableElementHandle</code>.
	 *
	 * @return the page variables list value.
	 */
	public List<VariableElementHandle> getPageVariables() {
		return getListProperty(PAGE_VARIABLES_PROP);
	}

	/**
	 * Gets the <VariableElementHandle> according to the input page variable name.
	 *
	 * @param pageVariableName the page variable name.
	 * @return the <VariableElementHandle> according to the input page variable name
	 *
	 */
	public VariableElementHandle getPageVariable(String pageVariableName) {
		if (pageVariableName == null) {
			return null;
		}
		VariableElement element = ((ReportDesign) module).findVariableElement(pageVariableName);
		if (element == null) {
			return null;
		}
		return element.handle(module);
	}

	/**
	 * Sets the page variable value.
	 *
	 * @param pageVariableName the page variable name.
	 * @param value            the page variable value.
	 * @throws SemanticException
	 */
	public void setPageVariable(String pageVariableName, Expression value) throws SemanticException {
		if (pageVariableName == null) {
			return;
		}
		VariableElementHandle handle = getPageVariable(pageVariableName);
		if (handle == null) {
			ElementFactory factory = getElementFactory();

			handle = factory.newVariableElement();
			handle.setVariableName(pageVariableName);
			handle.setExpressionProperty(IVariableElementModel.VALUE_PROP, value);
			add(PAGE_VARIABLES_PROP, handle);
			return;
		}

		handle.setExpressionProperty(IVariableElementModel.VALUE_PROP, value);
	}

	/**
	 * Adds data variable that user defined on the report design.
	 *
	 * @param variable the variable
	 * @throws SemanticException
	 */

	public void addVariable(VariableElementHandle variable) throws SemanticException {
		add(DATA_OBJECTS_PROP, variable);
	}

	/**
	 * Removes the given data variable.
	 *
	 * @param variable the variable
	 * @throws SemanticException
	 */

	public void dropVariable(VariableElementHandle variable) throws SemanticException {
		variable.drop();
	}

	/**
	 * Gets all variable.
	 *
	 * @return the list of variable. Each item is an instance of
	 *         <code>VariableElementHandle</code>.
	 */
	public List<VariableElementHandle> getAllVariables() {
		PropertyHandle propHandle = getPropertyHandle(DATA_OBJECTS_PROP);
		if (propHandle == null) {
			return Collections.emptyList();
		}
		return propHandle.getListValue();
	}

	/**
	 * Gets the locale of the report design.
	 *
	 * @return the locale of the report design.
	 */
	public ULocale getLocale() {
		return (ULocale) getProperty(LOCALE_PROP);
	}

	/**
	 * Sets the locale of the report design.
	 *
	 * @param locale the locale of the report design.
	 * @throws SemanticException
	 */
	public void setLocale(ULocale locale) throws SemanticException {
		setProperty(LOCALE_PROP, locale);
	}

	/**
	 * Gets the list of the included css style sheets that set the external URI. The
	 * css style might be included by the design handle itself and the theme which
	 * the design refers. Each item in the list is instance of
	 * <code>IncludedCssStyleSheetHandle</code>.
	 *
	 * @return list of all the included css style sheet that set the external URI
	 */
	public List<IncludedCssStyleSheetHandle> getAllExternalIncludedCsses() {
		List<IncludedCssStyleSheetHandle> ret = new ArrayList<>();
		List<IncludedCssStyleSheetHandle> values = getNativeStructureList(CSSES_PROP);

		// first, look css style sheet in the design itself
		if (values != null && !values.isEmpty()) {
			for (IncludedCssStyleSheetHandle sheetHandle : values) {
				if (sheetHandle.getExternalCssURI() != null || sheetHandle.isUseExternalCss()) {
					ret.add(sheetHandle);
				}
			}
		}

		// second, collect the theme referred by the design and included
		// libraries
		List<ThemeHandle> themeList = new ArrayList<>();
		ThemeHandle themeHandle = getTheme();
		if (themeHandle != null) {
			themeList.add(themeHandle);
		}
		List<LibraryHandle> libs = getAllLibraries();
		if (libs != null) {
			for (LibraryHandle libHandle : libs) {
				themeHandle = libHandle.getTheme();
				if (themeHandle != null && !themeList.contains(themeHandle)) {
					themeList.add(themeHandle);
				}
			}
		}

		// third, look external csses in all the collected themes
		for (int i = 0; i < themeList.size(); i++) {
			themeHandle = themeList.get(i);
			Iterator<Object> iter = themeHandle.getPropertyHandle(IAbstractThemeModel.CSSES_PROP).iterator();
			while (iter.hasNext()) {
				IncludedCssStyleSheetHandle sheetHandle = (IncludedCssStyleSheetHandle) iter.next();
				if (sheetHandle.getExternalCssURI() != null || sheetHandle.isUseExternalCss()) {
					ret.add(sheetHandle);
				}
			}
		}

		return ret;
	}

	/**
	 * Caches values for all elements, styles, etc. The caller must guarantee this
	 * method runs in single thread and have no synchronization issue. Whenever the
	 * user changes element values, should recall this method.
	 */

	public synchronized void cacheValues() {
		module.cacheValues();
	}

	/**
	 * Gets the flatten element by the original name.
	 *
	 * @param elementHandle the handle of a flatten element once in the same
	 *                      namespace
	 * @param originalName  the original name of the element
	 *
	 * @return the flatten element handle, or null if not found
	 */
	public DesignElementHandle getFlattenElement(DesignElementHandle elementHandle, String originalName) {
		DesignElement flatternElement = null;
		if (elementHandle != null) {
			flatternElement = ((ReportDesign) module).getFlattenElement(elementHandle.getElement(), originalName);
		}

		if (flatternElement != null) {
			return flatternElement.getHandle(module);
		}

		return null;
	}

	/**
	 * Gets the on-prepare script of the report design. Startup phase. No data
	 * binding yet. The design of an element can be changed here.
	 *
	 * @return the on-prepare script of the report design
	 */

	public String getOnPrepare() {
		return getStringProperty(ON_PREPARE_METHOD);
	}

	/**
	 * Sets the on-prepare script of the report design.
	 *
	 * @param script the script to set
	 * @throws SemanticException if the method is locked.
	 */
	public void setOnPrepare(String script) throws SemanticException {
		setProperty(ON_PREPARE_METHOD, script);
	}

	/**
	 * Gets the client-initialize script of the report design. The design can load
	 * java script libraries by the property.
	 *
	 * @return the client-initialize script of the report design
	 */

	public String getClientInitialize() {
		return getStringProperty(CLIENT_INITIALIZE_METHOD);
	}

	/**
	 * Sets the client-initialize script of the report design.
	 *
	 * @param script the script to set
	 * @throws SemanticException if the method is locked.
	 */
	public void setClientInitialize(String script) throws SemanticException {
		setProperty(CLIENT_INITIALIZE_METHOD, script);
	}

	/**
	 * Gets the language of the report design.
	 *
	 * @return the language of the report design
	 */

	public String getLanguage() {
		return getStringProperty(LANGUAGE_PROP);
	}

	/**
	 * Sets the language of the report design.
	 *
	 * @param language the language to set
	 * @throws SemanticException if the method is locked.
	 */

	public void setLanguage(String language) throws SemanticException {
		setProperty(LANGUAGE_PROP, language);
	}

	/**
	 * Get the configuration for the excel handling of forced auto column width
	 *
	 * @return the configuration of forced auto column width
	 */
	public boolean getExcelForceAutoColWidths() {
		return getBooleanProperty(EXCEL_FORCE_AUTO_COL_WIDTHS);
	}

	/**
	 * Set the auto column width usage for the excel output
	 *
	 * @param forceAutoColWidths auto columns widths calculation to be used
	 * @throws SemanticException
	 */
	public void setExcelForceAutoColWidths(boolean forceAutoColWidths) throws SemanticException {
		setBooleanProperty(EXCEL_FORCE_AUTO_COL_WIDTHS, forceAutoColWidths);
	}

	/**
	 * Get the configuration for the excel handling of single sheet result
	 *
	 * @return the configuration of single sheet result
	 */
	public boolean getExcelSingleSheet() {
		return getBooleanProperty(EXCEL_SINGLE_SHEET);
	}

	/**
	 * Set the single sheet usage for the excel output
	 *
	 * @param singleSheet single sheet to be used
	 * @throws SemanticException
	 */
	public void setExcelSingleSheet(boolean singleSheet) throws SemanticException {
		setBooleanProperty(EXCEL_SINGLE_SHEET, singleSheet);
	}

	/**
	 * Get the configuration for the excel handling of disabled grouping
	 *
	 * @return the configuration of disabled grouping
	 */
	public boolean getExcelDisableGrouping() {
		return getBooleanProperty(EXCEL_DISABLE_GROUPING);
	}

	/**
	 * Set the deactivation of the grouping of the excel output
	 *
	 * @param disableGrouping disable grouping
	 * @throws SemanticException
	 */
	public void setExcelDisableGrouping(boolean disableGrouping) throws SemanticException {
		setBooleanProperty(EXCEL_DISABLE_GROUPING, disableGrouping);
	}

	/**
	 * Get the configuration for the excel handling to display grid lines
	 *
	 * @return the configuration to display grind lines
	 */
	public boolean getExcelDisplayGridlines() {
		return getBooleanProperty(EXCEL_DISPLAY_GRIDLINES);
	}

	/**
	 * Set the display of grid line for the excel output
	 *
	 * @param displayGridlines display grid lines
	 * @throws SemanticException
	 */
	public void setExcelDisplayGridlines(boolean displayGridlines) throws SemanticException {
		setBooleanProperty(EXCEL_DISPLAY_GRIDLINES, displayGridlines);
	}

	/**
	 * Get the display of the excel auto filter
	 *
	 * @return the configuration to display the auto filter
	 */
	public boolean getExcelAutoFilter() {
		return getBooleanProperty(EXCEL_AUTO_FILTER);
	}

	/**
	 * Set the display of the excel auto filter
	 *
	 * @param enableAutoFilter enable auto filter
	 * @throws SemanticException
	 */
	public void setExcelAutoFilter(boolean enableAutoFilter) throws SemanticException {
		setBooleanProperty(EXCEL_AUTO_FILTER, enableAutoFilter);
	}

	/**
	 * Get the configuration for the forced recalculation
	 *
	 * @return the configuration to display grind lines
	 */
	public boolean getExcelForceRecalculation() {
		return getBooleanProperty(EXCEL_FORCE_RECALCULATION);
	}

	/**
	 * Set the forced recalculation of excel
	 *
	 * @param forceRecalculation enable the forced recalculation
	 * @throws SemanticException
	 */
	public void setExcelForceRecalculation(boolean forceRecalculation) throws SemanticException {
		setBooleanProperty(EXCEL_FORCE_RECALCULATION, forceRecalculation);
	}

	/**
	 * Get the configuration for the excel image scaling to cell dimension
	 *
	 * @return the configuration to use the image scaling
	 */
	public boolean getExcelImageScaling() {
		return getBooleanProperty(EXCEL_IMAGE_SCALING_CELL_DIMENSION);
	}

	/**
	 * Set the display of grid line for the excel output
	 *
	 * @param enableImageScaling enable image scaling
	 * @throws SemanticException
	 */
	public void setExcelImageScaling(boolean enableImageScaling) throws SemanticException {
		setBooleanProperty(EXCEL_IMAGE_SCALING_CELL_DIMENSION, enableImageScaling);
	}

	/**
	 * Get the configuration for additional page break of single sheet
	 *
	 * @return the configuration for additional page break of single sheet
	 */
	public boolean getExcelSingleSheetPageBreak() {
		return getBooleanProperty(EXCEL_SINGLE_SHEET_WITH_PAGE_BREAK);
	}

	/**
	 * Set an additional page break to single sheet
	 *
	 * @param singleSheetPageBreak add the single sheet page break
	 * @throws SemanticException
	 */
	public void setExcelSingleSheetPageBreak(boolean singleSheetPageBreak) throws SemanticException {
		setBooleanProperty(EXCEL_SINGLE_SHEET_WITH_PAGE_BREAK, singleSheetPageBreak);
	}

	/**
	 * Get the configuration if data streaming of XLSX is enabled
	 *
	 * @return the configuration  if data streaming of XLSX is enabled
	 */
	public boolean getExcelStreamingXlsx() {
		return getBooleanProperty(EXCEL_STREAMING_XLSX);
	}

	/**
	 * Set the output method to data streaming
	 *
	 * @param streamingXlsx enable the XLSX streaming
	 * @throws SemanticException
	 */
	public void setExcelStreamingXlsx(boolean streamingXlsx) throws SemanticException {
		setBooleanProperty(EXCEL_STREAMING_XLSX, streamingXlsx);
	}

	/**
	 * Get the configuration to display the report header and footer
	 *
	 * @return the configuration to display the report header and footer
	 */
	public boolean getExcelStructuredHeader() {
		return getBooleanProperty(EXCEL_STRUCTURED_HEADER);
	}

	/**
	 * Set the display of report header and footer on excel sheet
	 *
	 * @param structuredHeader display report header and footer at excel sheet
	 * @throws SemanticException
	 */
	public void setExcelStructuredHeader(boolean structuredHeader) throws SemanticException {
		setBooleanProperty(EXCEL_STRUCTURED_HEADER, structuredHeader);
	}

	/**
	 * Get the configuration for the sheet wide
	 *
	 * @return the configuration for the sheet wide
	 */
	public int getExcelPrintPagesWide() {
		return getIntProperty(EXCEL_PRINT_PAGES_WIDE);
	}

	/**
	 * Set the configuration to set the sheet wide to fit the page
	 *
	 * @param printPagesWide the page wide
	 * @throws SemanticException
	 */
	public void setExcelPrintPagesWide(int printPagesWide) throws SemanticException {
		setIntProperty(EXCEL_PRINT_PAGES_WIDE, printPagesWide);
	}

	/**
	 * Get the configuration for the sheet high
	 *
	 * @return the configuration for the sheet high
	 */
	public int getExcelPrintPagesHigh() {
		return getIntProperty(EXCEL_PRINT_PAGES_HIGH);
	}

	/**
	 * Set the configuration to set the sheet high to fit the page
	 *
	 * @param printPagesHigh the page high
	 * @throws SemanticException
	 */
	public void setExcelPrintPagesHigh(int printPagesHigh) throws SemanticException {
		setIntProperty(EXCEL_PRINT_PAGES_HIGH, printPagesHigh);
	}

	/**
	 * Get the configuration for the page scale
	 *
	 * @return the configuration for the page scale
	 */
	public int getExcelPrintScale() {
		return getIntProperty(EXCEL_PRINT_SCALE);
	}

	/**
	 * Set the configuration to set the page scale
	 *
	 * @param printScale the page scale
	 * @throws SemanticException
	 */
	public void setExcelPrintScale(int printScale) throws SemanticException {
		setIntProperty(EXCEL_PRINT_SCALE, printScale);
	}

	/**
	 * Get the configuration for the used template file
	 *
	 * @return the configuration for the used template file
	 */
	public String getExcelTemplateFile() {
		return getStringProperty(EXCEL_TEMPLATE_FILE);
	}

	/**
	 * Set the configuration for the template file
	 *
	 * @param templateFile template file
	 * @throws SemanticException
	 */
	public void setExcelTemplateFile(String templateFile) throws SemanticException {
		setStringProperty(EXCEL_TEMPLATE_FILE, templateFile);
	}

	/**
	 * Get the configuration for the used PDF version
	 *
	 * @return the configuration for the used PDF version
	 */
	public String getPdfVersion() {
		return getStringProperty(PDF_VERSION);
	}

	/**
	 * Set the configuration for the template file
	 *
	 * @param pdfVersion PDF version number
	 * @throws SemanticException
	 */
	public void setPdfVersion(String pdfVersion) throws SemanticException {
		setStringProperty(PDF_VERSION, pdfVersion);
	}

	/**
	 * Get the configuration for the used template file
	 *
	 * @return the configuration for used PDF conformance
	 */
	public String getPdfConformance() {
		return getStringProperty(PDF_CONFORMANCE);
	}

	/**
	 * Set the configuration for used PDF conformance
	 *
	 * @param pdfConformance PDF conformance
	 * @throws SemanticException
	 */
	public void setPdfConformance(String pdfConformance) throws SemanticException {
		setStringProperty(PDF_CONFORMANCE, pdfConformance);
	}

	/**
	 * Get the configuration for the PDF/UA conformance
	 *
	 * @return the configuration for the PDF/UA conformance
	 */
	public String getPdfUAConformance() {
		return getStringProperty(PDF_UA_CONFORMANCE);
	}

	/**
	 * Set the configuration for the PDF/UA conformance
	 *
	 * @param pdfUAConformance PDF/UA conformance
	 * @throws SemanticException
	 */
	public void setPdfUAConformance(String pdfUAConformance) throws SemanticException {
		setStringProperty(PDF_UA_CONFORMANCE, pdfUAConformance);
	}

	/**
	 * Get the configuration for the used PDF color type
	 *
	 * @return the configuration for the used PDF color type
	 */
	public String getPdfIccColorType() {
		return getStringProperty(PDF_ICC_COLOR_TYPE);
	}

	/**
	 * Set the configuration for the ICC color type
	 *
	 * @param iccColorType ICC color type
	 * @throws SemanticException
	 */
	public void setPdfIccColorType(String iccColorType) throws SemanticException {
		setStringProperty(PDF_ICC_COLOR_TYPE, iccColorType);
	}

	/**
	 * Get the configuration for the used external color profile
	 *
	 * @return the configuration for the used external color profile
	 */
	public String getPdfIccColorProfileExternal() {
		return getStringProperty(PDF_ICC_PROFILE_EXTERNAL);
	}

	/**
	 * Set the configuration for the external color profile
	 *
	 * @param iccProfileExternal external color profile
	 * @throws SemanticException
	 */
	public void setPdfIccColorProfileExternal(String iccProfileExternal) throws SemanticException {
		setStringProperty(PDF_ICC_PROFILE_EXTERNAL, iccProfileExternal);
	}

	/**
	 * Get the configuration to prepend document(s) to the PDF document
	 *
	 * @return the configuration to prepend document(s) to the PDF document
	 */
	public String getPdfDocumentsPrepend() {
		return getStringProperty(PDF_DOCUMENTS_PREPEND);
	}

	/**
	 * Set the configuration to prepend document(s) to the PDF document
	 *
	 * @param prependDocuments document(s) to prepend
	 * @throws SemanticException
	 */
	public void setPdfDocumentsPrepend(String prependDocuments) throws SemanticException {
		setStringProperty(PDF_DOCUMENTS_PREPEND, prependDocuments);
	}

	/**
	 * Get the configuration to append document(s) to the PDF document
	 *
	 * @return the configuration to append document(s) to the PDF document
	 */
	public String getPdfDocumentsAppend() {
		return getStringProperty(PDF_DOCUMENTS_APPEND);
	}

	/**
	 * Set the configuration to append document(s) to the PDF document
	 *
	 * @param appendDocuments document(s) to append
	 * @throws SemanticException
	 */
	public void setPdfDocumentsAppend(String appendDocuments) throws SemanticException {
		setStringProperty(PDF_DOCUMENTS_APPEND, appendDocuments);
	}

	/**
	 * Get the configuration for the fallback font of PDF/A
	 *
	 * @return the configuration for the fallback font of PDF/A
	 */
	public String getPdfAFontFallback() {
		return getStringProperty(PDFA_FONT_FALLBACK);
	}

	/**
	 * Set the configuration for the fallback font of PDF/A
	 *
	 * @param pdfaFontFallback PDF/A fallback font
	 * @throws SemanticException
	 */
	public void setPdfAFontFallback(String pdfaFontFallback) throws SemanticException {
		setStringProperty(PDFA_FONT_FALLBACK, pdfaFontFallback);
	}

	/**
	 * Get the configuration for the used CIDSet embed option
	 *
	 * @return the configuration for the used CIDSet embed option
	 */
	public String getPdfFontCidEmbed() {
		return getStringProperty(PDF_FONT_CID_SET);
	}

	/**
	 * Set the configuration for the CIDSet embed option
	 *
	 * @param embedCID font embed CIDSet
	 * @throws SemanticException
	 */
	public void setPdfFontCidEmbed(String embedCID) throws SemanticException {
		setStringProperty(PDF_FONT_CID_SET, embedCID);
	}

	/**
	 * Get the configuration to the embed the title
	 *
	 * @return the configuration to the embed the title
	 */
	public String getPdfAEmbedTitle() {
		return getStringProperty(PDFA_DOCUMENT_EMBED_TITLE);
	}

	/**
	 * Set the configuration to the embed the title
	 *
	 * @param embedTitle embed title
	 * @throws SemanticException
	 */
	public void setPdfAEmbedTitle(boolean embedTitle) throws SemanticException {
		setBooleanProperty(PDFA_DOCUMENT_EMBED_TITLE, embedTitle);
	}

	/**
	 * Get the configuration to the combined usage of margin & padding for spacing
	 *
	 * @return the configuration to use the combined calculation of margin & padding
	 */
	public boolean getWordCombineMarginPadding() {
		return getBooleanProperty(WORD_COMBINE_MARGIN_PADDING);
	}

	/**
	 * Set the configuration to the combined usage of margin & padding for spacing
	 *
	 * @param combineMarginPadding use combination of margin & padding for spacing
	 * @throws SemanticException
	 */
	public void setWordCombineMarginPadding(boolean combineMarginPadding) throws SemanticException {
		setBooleanProperty(WORD_COMBINE_MARGIN_PADDING, combineMarginPadding);
	}

	/**
	 * Get the configuration if an empty paragraph is to use at the end of a list
	 * cell
	 *
	 * @return the configuration if an empty paragraph is to use at the end of a
	 *         list cell
	 */
	public boolean getWordListCellAddEmptyPara() {
		return getBooleanProperty(WORD_LIST_CELL_ADD_EMPTY_PARA);
	}

	/**
	 * Set the configuration if an empty paragraph is to use at the end of a list
	 * cell
	 *
	 * @param addEmptyPara add empty paragraph
	 * @throws SemanticException
	 */
	public void setWordListCellAddEmptyPara(boolean addEmptyPara) throws SemanticException {
		setBooleanProperty(WORD_LIST_CELL_ADD_EMPTY_PARA, addEmptyPara);
	}

	/**
	 * Get the configuration to use a wrapping table for margin and padding
	 *
	 * @return the configuration to use a wrapping table for margin and padding
	 */
	public boolean getWordWrapTableForMarginPadding() {
		return getBooleanProperty(WORD_WRAP_TABLE_FOR_MARGIN_PADDING);
	}

	/**
	 * Set the configuration to use a wrapping table for margin and padding
	 *
	 * @param wrapTable wrap table for margin and padding
	 * @throws SemanticException
	 */
	public void setWordWrapTableForMarginPadding(boolean wrapTable) throws SemanticException {
		setBooleanProperty(WORD_WRAP_TABLE_FOR_MARGIN_PADDING, wrapTable);
	}

	/**
	 * Get the configuration to use a layout table for header and footer
	 *
	 * @return the configuration to use a layout table for header and footer
	 */
	public boolean getWordWrapTableForHeaderFooter() {
		return getBooleanProperty(WORD_WRAP_TABLE_FOR_HEADER_FOOTER);
	}

	/**
	 * Set the configuration to use a layout table for header and footer
	 *
	 * @param wrapTable layout table for header and footer
	 * @throws SemanticException
	 */
	public void setWordWrapTableForHeaderFooter(boolean wrapTable) throws SemanticException {
		setBooleanProperty(WORD_WRAP_TABLE_FOR_HEADER_FOOTER, wrapTable);
	}

}
