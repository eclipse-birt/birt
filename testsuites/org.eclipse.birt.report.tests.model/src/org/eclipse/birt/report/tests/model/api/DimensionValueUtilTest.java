package org.eclipse.birt.report.tests.model.api;

import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.DimensionValueUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

@SuppressWarnings("restriction")
public class DimensionValueUtilTest extends BaseTestCase {

	public void testDoParse() throws PropertyValueException {
		DimensionValue dv = DimensionValueUtil.doParse("1,2cm", true,
				ULocale.FRENCH);
		assertEquals("1.2cm", dv.toString());

		dv = DimensionValueUtil.doParse("1.2cm", true, null);
		assertEquals("1.2cm", dv.toString());

		try {
			dv = DimensionValueUtil.doParse("1,2cm", false, ULocale.FRENCH);
			fail();
		} catch (PropertyValueException e) {
		}

		dv = DimensionValueUtil.doParse("1.2cm", false, ULocale.FRENCH);
		assertEquals("1.2cm", dv.toString());
	}
}
