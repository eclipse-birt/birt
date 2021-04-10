
package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression test case description:
 * <p>
 * Description: Delete data source, then edit the invalid data set, unexpected
 * exception is thrown out.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create a data source and related data set.
 * <li>Delete the data source.
 * <li>Edit the invalid data set.
 * <li>Unexpected exception isthrown out.
 * </ol>
 * Test description:
 * <p>
 * Through Model API to delete DataSource.
 * </p>
 */

public class Regression_121857 extends BaseTestCase {

	/**
	 * @throws Exception
	 */
	public void test_regression_121857() throws Exception {
		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		OdaDataSourceHandle datasource = factory.newOdaDataSource("dsource", //$NON-NLS-1$
				"org.eclipse.birt.report.data.oda.jdbc"); //$NON-NLS-1$
		OdaDataSetHandle dataset = factory.newOdaDataSet("dset", //$NON-NLS-1$
				"org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"); //$NON-NLS-1$
		dataset.setDataSource("dsource"); //$NON-NLS-1$

		designHandle.getDataSources().add(datasource);
		designHandle.getDataSets().add(dataset);

		datasource.drop();
		assertNull(designHandle.findDataSource("dsource")); //$NON-NLS-1$
		assertNull(dataset.getDataSource());
		assertEquals("dsource", dataset.getDataSourceName()); //$NON-NLS-1$

	}

}