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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

import junit.framework.TestCase;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.re.DummyCubeCursor;
import org.eclipse.birt.report.item.crosstab.core.re.DummyDimensionCursor;
import org.eclipse.birt.report.item.crosstab.core.re.DummyEdgeCursor;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class TestColumnWalker extends TestCase implements ICrosstabConstants {

	private IDesignEngine engine;
	private ModuleHandle module;

	protected void setUp() throws Exception {
		super.setUp();
		ThreadResources.setLocale(ULocale.ENGLISH);

		if (engine == null) {
			engine = new DesignEngine(new DesignConfig());
		}

		SessionHandle sh = engine.newSessionHandle(ULocale.getDefault());
		ReportDesignHandle rdh = sh.createDesign();
		module = rdh.getModuleHandle();
	}

	private void baseTestColumnWalker(CrosstabReportItemHandle handle) {
		try {
			ColumnWalker cw = new ColumnWalker(handle, (EdgeCursor) createSimpleCubeCursor().getOrdinateEdge().get(0));

			System.out.println("Start:============================================"); //$NON-NLS-1$

			while (cw.hasNext()) {
				ColumnEvent ce = cw.next();

				System.out.println(ce);
			}

			System.out.println("End:============================================="); //$NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testColumnWalker1() {
		baseTestColumnWalker(CrosstabSamples.createCrosstab1(module));
	}

	public void testColumnWalker2() {
		baseTestColumnWalker(CrosstabSamples.createCrosstab2(module));
	}

	public void testColumnWalker3() {
		baseTestColumnWalker(CrosstabSamples.createCrosstab3(module));
	}

	public void testColumnWalker4() {
		baseTestColumnWalker(CrosstabSamples.createCrosstab4(module));
	}

	public void testColumnWalker5() {
		baseTestColumnWalker(CrosstabSamples.createCrosstab5(module));
	}

	protected CubeCursor createSimpleCubeCursor() {
		DummyDimensionCursor ddc1 = new DummyDimensionCursor(2);
		DummyDimensionCursor ddc2 = new DummyDimensionCursor(2);
		DummyDimensionCursor ddc3 = new DummyDimensionCursor(2);

		DummyEdgeCursor dec = new DummyEdgeCursor(8);
		dec.addDimensionCursor(ddc1);
		dec.addDimensionCursor(ddc2);
		dec.addDimensionCursor(ddc3);

		DummyCubeCursor dcc = new DummyCubeCursor();
		dcc.addOrdinateEdgeCursor(dec);

		return dcc;
	}

}
