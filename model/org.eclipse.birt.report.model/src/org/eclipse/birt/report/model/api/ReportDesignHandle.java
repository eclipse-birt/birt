/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;

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

public class ReportDesignHandle extends ReportDesignHandleImpl implements IReportDesignModel {

	/**
	 * Constructs a handle with the given design. The application generally does not
	 * create handles directly. Instead, it uses one of the navigation methods
	 * available on other element handles.
	 * 
	 * @param design the report design
	 */

	public ReportDesignHandle(ReportDesign design) {
		super(design);
	}

}
