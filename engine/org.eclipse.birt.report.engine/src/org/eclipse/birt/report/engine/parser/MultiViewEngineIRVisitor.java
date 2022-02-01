/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * Constructs an internal representation of the report design for report
 * geenration and presentation, based on the internal representation that design
 * engine creates. The DE IR services both the designer UI and factory, and has
 * certain features that are not quite suitable for FPE use. In particular, this
 * step of the reconstruction is needed for several reasons:
 * <p>
 * <li>Style handling: DE stores all styles in an unflatten version. Factory
 * needs to reference styles where the element hierarchy has been flattened.
 * <li>Faster lookup: DE stores various properties as property name/value pairs.
 * Factory IR might store them as structure. See
 * <code>createHighlightRule()</code> for an example.
 * <li>Merging properties: DE stores custom and default properties separately.
 * In FPE, they are merged.</li>
 * <p>
 * 
 * This class visits the Design Engine's IR to create a new IR for FPE. It is
 * usually used in the "Design Adaptation" phase of report generation, which is
 * also the first step in report generation after DE loads the report in.
 * 
 * <p>
 * special consideration in styles
 * <p>
 * BIRT uses a simlar style mode with CSS, but not exactly the same. The main
 * differences are:
 * <li>text-decoration is not inheraible which simplify the CSS standard. This
 * rules makes text-decroation are usless for all the containers. As the HTML
 * treat the text-decoration inheritable in block-level element, the ENGINE must
 * remove the text-decoration from the container's styles.
 * <li>BIRT doesn't define the body style, it uses a predefined style "report"
 * as the default style.
 * 
 */
public class MultiViewEngineIRVisitor extends EngineIRVisitor {

	public MultiViewEngineIRVisitor(ReportDesignHandle handle) {
		super(handle);
	}

	public void visitTable(TableHandle handle) {
		DesignElementHandle currentView = handle.getCurrentView();
		if (currentView != null && currentView != handle) {
			currentElementId = handle.getID();
			apply(currentView);
		} else {
			super.visitTable(handle);
		}
	}

	protected void visitExtendedItem(ExtendedItemHandle handle) {
		DesignElementHandle currentView = handle.getCurrentView();
		if (currentView != null && currentView != handle) {
			currentElementId = handle.getID();
			apply(currentView);
		} else {
			super.visitExtendedItem(handle);
		}
	}
}
