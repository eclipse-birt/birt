package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test Cases for TemplateElementHandle.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * 
 * <tr>
 * <td>{@link #testTemplate()}</td>
 * </tr>
 * </table>
 * 
 */
public class TemplateElementHandleTest extends BaseTestCase {
	private ElementFactory factory = null;
	private OdaDataSourceHandle datasource = null;
	private OdaDataSetHandle dataset = null;
	private OdaDataSetHandle dataset2 = null;
	private LabelHandle label = null;

	public TemplateElementHandleTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TemplateElementHandleTest.class);
	}

	public void setUp() throws Exception {

		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = sessionHandle.createDesign();
		design = (ReportDesign) designHandle.getModule();
		factory = designHandle.getElementFactory();

	}

	/**
	 * Test create template item and revert to template
	 * 
	 * @throws Exception
	 */
	public void testTemplate() throws Exception {
		// datasource can't be converted into template report item
		datasource = factory.newOdaDataSource("dsource", "org.eclipse.birt.report.data.oda.jdbc");
		designHandle.getDataSources().add(datasource);
		try {
			datasource.createTemplateElement("T.dsource");
			fail();
		} catch (SemanticException e) {
			assertNotNull(e);
		}

		// duplicated names
		dataset = factory.newOdaDataSet("dset", "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet");
		designHandle.getDataSets().add(dataset);
		dataset2 = factory.newOdaDataSet("dset2", "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet");
		designHandle.getDataSets().add(dataset2);
		TemplateElementHandle tdataset = dataset2.createTemplateElement("dset");
		assertEquals("dset1", tdataset.getName());

		// Element which has no template definition can't be reverted to report element
		label = factory.newLabel("label");
		try {
			label.revertToTemplate("T.lable");
		} catch (SemanticException e) {
			assertNotNull(e);
		}

	}

}
