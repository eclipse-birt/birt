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

package org.eclipse.birt.core.script.function.general;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

/**
 * Defines a set of static methods that support a number of widely-used
 * financial functions.
 * 
 */
public class Finance
{
	private static final String DDB = "ddb";
	private static final String SLN = "sln";
	private static final String SYD = "syd";
	private static final String FV = "fv";
	private static final String PMT = "pmt";
	private static final String IPMT = "ipmt";
	private static final String PPMT = "ppmt";
	private static final String NPER = "nper";
	private static final String PV = "pv";
	private static final String RATE = "rate";
	private static final String PERCENT = "percent";
	private static final String NPV = "npv";
	private static final String IRR = "irr";
	private static final String MIRR = "mirr";
	/**
	 * The application cannot create an instance of this class
	 */
	private Finance( )
	{

	}
	
	private static double getDouble( Object o ) throws BirtException
	{
		if( o == null )
			return Double.NaN;
		return DataTypeUtil.toDouble( o );
	}
	
	private static int getInteger( Object o ) throws BirtException
	{
		if( o == null )
			return 0;
		return DataTypeUtil.toInteger( o );
	}

	/**
	 * @param Cost
	 *            the initial cost of the asset.
	 * @param salvage
	 *            the value at the end of the depreciation (sometimes called the
	 *            salvage value of the asset).
	 * @param life
	 *            the number of periods over which the asset is being
	 *            depreciated (sometimes called the useful life of the asset).
	 * @param period
	 *            the period for which you want to calculate the depreciation.
	 *            Period must use the same units as life.
	 * 
	 * @return the depreciation of an asset for a given , single period using
	 *         the double-declning balance method Remarks
	 * 
	 * The double-declining balance method computes depreciation at an
	 * accelerated rate. Depreciation is highest in the first period and
	 * decreases in successive periods. DDB uses the following formula to
	 * calculate depreciation for a period: ((cost-salvage) - total depreciation
	 * from prior periods) * (factor/life)
	 * 
	 * Change factor if you do not want to use the double-declining balance
	 * method. Use the VDB function if you want to switch to the straight-line
	 * depreciation method when depreciation is greater than the declining
	 * balance calculation.
	 */
	static double ddb( double cost, double salvage, double life,
			int period ) throws IllegalArgumentException
	{

		double rate;
		double prior = 0; /* Depreciation in prior period */
		double basis; /* The basis for this cycle, the underpreciated value */
		double depr = 0; /* The depreciation we calculate for current cycle */

		if ( life <= 0 || salvage < 0 || cost <= 0 || period <= 0
				|| Double.isNaN( life ) || Double.isNaN( salvage )
				|| Double.isNaN( cost ) || Double.isNaN( period ) )
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter in the function DDB" ) );
		}

		for ( int x = 0; x < period; x++ )
		{
			basis = cost - prior;
			depr = Math.min( basis - salvage, basis * ( 1 / life ) * 2 );
			prior += depr;
		}
		return depr;
	}

	private static class DdbScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 4)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.ddb()");
			return ddb( getDouble( arguments[0] ),
					getDouble( arguments[1] ),
					getDouble(  arguments[2] ),
					getInteger( arguments[3] ));
		}
	}
	
	/**
	 * @param cost
	 *            the initial cost of the asset.
	 * @param salvage
	 *            the value at the end of the depreciation (sometimes called the
	 *            salvage value of the asset).
	 * @param life
	 *            the number of periods over which the asset is depreciated
	 *            (sometimes called the useful life of the asset).
	 * @return the straight-line depreciation of an asset for a single period
	 */
	static double sln( double cost, double salvage, double life )
			throws IllegalArgumentException
	{
		return ( ( cost - salvage ) / life );
	}

	private static class SlnScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 3)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.sln()");
			return sln( getDouble( arguments[0] ),
					getDouble( arguments[1] ),
					getDouble(  arguments[2] ));
		}
	}
	/**
	 * @param cost
	 *            the initial cost of the asset.
	 * @param salvage
	 *            the value at the end of the depreciation (sometimes called the
	 *            salvage value of the asset).
	 * @param life
	 *            the number of periods over which the asset is depreciated
	 *            (sometimes called the useful life of the asset).
	 * @param period
	 *            the period and must use the same units as life.
	 * @return sum-of-years'-digits deprciation of an asset for a specified
	 *         period Remark
	 * 
	 * SYD is calculated as follows:
	 * SYD=(cost-salvage)*(life-per+1)*2/(life*(life+1))
	 *  
	 */
	static double syd( double cost, double salvage, double life,
			int period ) throws IllegalArgumentException
	{
		return (cost - salvage)* (life -period + 1) * 2 / (life * (life + 1));
	}

	private static class SydScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 4)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.syd()");
			return syd( getDouble( arguments[0] ),
					getDouble( arguments[1] ),
					getDouble(  arguments[2] ),
					getInteger( arguments[3] ));
		}
	}
	/**
	 * @param rate
	 *            the interest rate per period.
	 * @param nPer
	 *            the total number of payment periods in an annuity.
	 * @param pmt
	 *            the payment made each period; it cannot change over the life
	 *            of the annuity. Typically, pmt contains principal and interest
	 *            but no other fees or taxes. If pmt is omitted, you must
	 *            include the pv argument.
	 * @param pv
	 *            the present value, or the lump-sum amount that a series of
	 *            future payments is worth right now. If pv is omitted, it is
	 *            assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due
	 *            the number 0 or 1 and indicates when payments are due. If type
	 *            is omitted, it is assumed to be 0.
	 * @return the future value of an annuity based on periodic , constant
	 *         payments,and on an unvarying interest rate Make sure that you are
	 *         consistent about the units you use for specifying rate and nper.
	 * 
	 * Remarks Make sure that you are consistent about the units you use for
	 * specifying rate and nper. If you make monthly payments on a four-year
	 * loan at 12 percent annual interest, use 12%/12 for rate and 4*12 for
	 * nper. If you make annual payments on the same loan, use 12% for rate and
	 * 4 for nper. For all the arguments, cash you pay out, such as deposits to
	 * savings, is represented by negative numbers; cash you receive, such as
	 * dividend checks, is represented by positive numbers.
	 * 
	 *  
	 */
	static double fv( double rate, int nPer, double pmt, double pv,
			int due ) throws IllegalArgumentException
	{

		double fv = 0;
		int start = 0;
		int end = 0;
		if ( rate < 0 || nPer < 0 || ( due != 1 && due != 0 ) )
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter in the function FV" ) );
		}

		switch ( due )
		{
			case 1 :
				start = 1;
				end = nPer;
				break;
			case 0 :
				start = 0;
				end = nPer - 1;
				break;
			default :
				throw ( new IllegalArgumentException(
						"There exists illegal parameter" + due ) );
		}

		for ( int i = start; i <= end; i++ )
			fv += Math.pow( ( 1 + rate ), i );

		fv = fv * pmt;

		if ( pv != 0 )
		{
			fv += pv
					* Math
							.pow( ( 1 + rate ), nPer );
		}
		return ( 0 - fv );
	}

	private static class FvScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 5)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.fv()");
			return fv( getDouble( arguments[0] ),
					getInteger( arguments[1] ),
					getDouble(  arguments[2] ),
					getDouble( arguments[3] ),
					getInteger( arguments[4] ));
		}
	}
	/**
	 * @param rate
	 *            the interest rate per period.
	 * @param nPer
	 *            the total number of payment periods in an annuity.
	 * @param pv
	 *            the present value, or the lump-sum amount that a series of
	 *            future payments is worth right now. If pv is omitted, it is
	 *            assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due
	 *            the number 0 or 1 and indicates when payments are due. If type
	 *            is omitted, it is assumed to be 0.
	 * @return the payment for an annuity, based on periodic , constant
	 *         payments, and on an unvarying interest rate.
	 * 
	 * The payment returned by PMT includes principal and interest but no taxes,
	 * reserve payments, or fees sometimes associated with loans. Make sure that
	 * you are consistent about the units you use for specifying rate and nper.
	 * If you make monthly payments on a four-year loan at an annual interest
	 * rate of 12 percent, use 12%/12 for rate and 4*12 for nper. If you make
	 * annual payments on the same loan, use 12 percent for rate and 4 for nper.
	 *  
	 */
	static double pmt( double rate, int nper, double pv, double fv,
			int due ) throws IllegalArgumentException
	{
		double pmt;
		double denom;
		if ( nper <= 0 )
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter  nper=" + nper ) );
		}
		if ( due != 0 && due != 1 )
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter due=" + due ) );
		}

		if ( rate == 0 )
		{
			pmt = -( fv + pv ) / nper;
		}
		else
		{
			denom = Math.pow( ( 1 + rate ), nper );
			pmt = ( -fv - pv * denom )
					* ( rate / ( ( 1 + rate * due ) * ( denom - 1 ) ) );
		}
		return pmt;

	}

	private static class PmtScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 5)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.pmt()");
			return pmt( getDouble( arguments[0] ),
					getInteger( arguments[1] ),
					getDouble(  arguments[2] ),
					getDouble( arguments[3] ),
					getInteger( arguments[4] ));
		}
	}
	/**
	 * @param rate
	 *            the interest rate per period.
	 * @param nPer
	 *            the total number of payment periods in an annuity.
	 * @param pv
	 *            the present value, or the lump-sum amount that a series of
	 *            future payments is worth right now. If pv is omitted, it is
	 *            assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due
	 *            the number 0 or 1 and indicates when payments are due. If type
	 *            is omitted, it is assumed to be 0.
	 * @param per
	 *            the period for which you want to find the interest and must be
	 *            in the range 1 to nper.
	 * @param fv
	 *            the future value, or a cash balance you want to attain after
	 *            the last payment is made. If fv is omitted, it is assumed to
	 *            be 0 (the future value of a loan, for example, is 0).
	 * @return the interest payment for a given period of an annuity, based on
	 *         periodic , constant payments, and on an unvarying interest rate
	 * 
	 * Remarks Make sure that you are consistent about the units you use for
	 * specifying rate and nper. If you make monthly payments on a four-year
	 * loan at 12 percent annual interest, use 12%/12 for rate and 4*12 for
	 * nper. If you make annual payments on the same loan, use 12% for rate and
	 * 4 for nper. For all the arguments, cash you pay out, such as deposits to
	 * savings, is represented by negative numbers; cash you receive, such as
	 * dividend checks, is represented by positive numbers.
	 */
	static double ipmt( double rate, int per, int nPer, double pv,
			double fv, int due ) throws IllegalArgumentException
	{
		double pmt;
		double ipmt = 0;
		double principal;
		double ppmt = 0;
		if ( rate < 0 || nPer < 0 || per > nPer || ( due != 1 && due != 0 ) )
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter in the function IPMT" ) );
		}
		pmt = Finance.pmt(rate,nPer,pv,fv,due);
		principal = Math.abs( pv );

		for ( int curper = 1; curper <= per; curper++ )
		{
			if ( curper != 1 || due == 0 )
				ipmt = rate * principal;
			ppmt = pmt - ipmt;
			principal = principal - ppmt;
		}

		return ipmt;

	}
	
	private static class IpmtScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 6)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.ipmt()");
			return ipmt( getDouble( arguments[0] ),
					getInteger( arguments[1] ),
					getInteger( arguments[2] ),
					getDouble(  arguments[3] ),
					getDouble( arguments[4] ),
					getInteger( arguments[5] ));
		}
	}

	/**
	 * @param rate
	 *            the interest rate per period.
	 * @param nPer
	 *            the total number of payment periods in an annuity.
	 * @param pv
	 *            the present value, or the lump-sum amount that a series of
	 *            future payments is worth right now. If pv is omitted, it is
	 *            assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due
	 *            the number 0 or 1 and indicates when payments are due. If type
	 *            is omitted, it is assumed to be 0.
	 * @param per
	 *            the period for which you want to find the interest and must be
	 *            in the range 1 to nper.
	 * @param fv
	 *            the future value, or a cash balance you want to attain after
	 *            the last payment is made. If fv is omitted, it is assumed to
	 *            be 0 (the future value of a loan, for example, is 0).
	 * @return the principal payment for a given period of an annuity, based on
	 *         periodic, constant payments, and on an unvarying interest rate
	 * 
	 * Remark Make sure that you are consistent about the units you use for
	 * specifying rate and nper. If you make monthly payments on a four-year
	 * loan at 12 percent annual interest, use 12%/12 for rate and 4*12 for
	 * nper. If you make annual payments on the same loan, use 12% for rate and
	 * 4 for nper.
	 *  
	 */
	static double ppmt( double rate, int per, int nPer, double pv,
			double fv, int due ) throws IllegalArgumentException
	{
		double pmt;
		double ipmt = 0;
		double principal;
		double ppmt = 0;
		if ( rate < 0 || nPer < 0 || per > nPer || ( due != 1 && due != 0 ) )
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter in the function PPMT" ) );
		}
		pmt = Finance.pmt( rate, nPer, pv, fv, due );
		principal = Math.abs( pv );

		for ( int curper = 1; curper <= per; curper++ )
		{
			ppmt = pmt - ipmt;
			principal = principal - ppmt;
			ipmt = rate * principal;
		}
		return ppmt;

	}

	private static class PpmtScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 6)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.ppmt()");
			return ppmt( getDouble( arguments[0] ),
					getInteger( arguments[1] ),
					getInteger( arguments[2] ),
					getDouble(  arguments[3] ),
					getDouble( arguments[4] ),
					getInteger( arguments[5] ));
		}
	}
	/**
	 * *
	 * 
	 * @param rate
	 *            the interest rate per period.
	 * @param pv
	 *            the present value, or the lump-sum amount that a series of
	 *            future payments is worth right now. If pv is omitted, it is
	 *            assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due
	 *            the number 0 or 1 and indicates when payments are due. If type
	 *            is omitted, it is assumed to be 0.
	 * @param fv
	 *            the future value, or a cash balance you want to attain after
	 *            the last payment is made. If fv is omitted, it is assumed to
	 *            be 0 (the future value of a loan, for example, is 0).
	 * @param pmt
	 *            the payment made each period; it cannot change over the life
	 *            of the annuity. Typically, pmt contains principal and interest
	 *            but no other fees or taxes.
	 * @return the number of periods for an annuity based on periodic , constant
	 *         payments, and on an unvarying interest rate
	 */
	static double nper( double rate, double pmt, double pv, double fv,
			int due ) throws IllegalArgumentException
	{
		
        double retval = 0;
        if (rate == 0) {
            retval = -1 * (fv + pv) / pmt;
        } else {
            double r1 = rate + 1;
            double ryr = (due == 1 ? r1 : 1) * pmt / rate;
            double a1 = ((ryr - fv) < 0)
                    ? Math.log(fv - ryr)
                    : Math.log(ryr - fv);
            double a2 = ((ryr - fv) < 0)
                    ? Math.log(-pv - ryr)
                    : Math.log(pv + ryr);
            double a3 = Math.log(r1);
            retval = (a1 - a2) / a3;
        }
        return retval;
	}
	
	private static class NperScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 5)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.nper()");
			return nper( getDouble( arguments[0] ),
					getDouble( arguments[1] ),
					getDouble( arguments[2] ),
					getDouble(  arguments[3] ),
					getInteger( arguments[4] ));
		}
	}
	
	/**
	 * @param rate
	 *            the interest rate per period.
	 * @param due
	 *            the number 0 or 1 and indicates when payments are due. If type
	 *            is omitted, it is assumed to be 0.
	 * @param fv
	 *            the future value, or a cash balance you want to attain after
	 *            the last payment is made. If fv is omitted, it is assumed to
	 *            be 0 (the future value of a loan, for example, is 0).
	 * @param pmt
	 *            the payment made each period; it cannot change over the life
	 *            of the annuity. Typically, pmt contains principal and interest
	 *            but no other fees or taxes.
	 * @param nPer
	 *            the total number of payment periods in an annuity. For
	 *            example, if you get a four-year car loan and make monthly
	 *            payments, your loan has 4*12 (or 48) periods. You would enter
	 *            48 into the formula for nper.
	 * @return the present value of an annuity based on periodic, constant
	 *         payments to be paid in the future,and on an unvarying interest
	 *         rate
	 * 
	 * Remarks Make sure that you are consistent about the units you use for
	 * specifying rate and nper. If you make monthly payments on a four-year
	 * loan at 12 percent annual interest, use 12%/12 for rate and 4*12 for
	 * nper. If you make annual payments on the same loan, use 12% for rate and
	 * 4 for nper. The following functions apply to annuities:
	 */
	static double pv( double rate, int nPer, double pmt, double fv,
			int due ) throws IllegalArgumentException
	{

		double denom;
		double num;
		double pv = 0;
		if ( nPer < 0 || ( due != 1 && due != 0 ) )
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter in the function PV" ) );
		}

		if ( rate == 0 )
		{
			pv = -fv - ( pmt * nPer );
		}
		else
		{
			denom = Math.pow( ( 1 + rate ), nPer );
			num = -fv - pmt * ( 1 + ( rate * due ) ) * ( denom - 1 ) / rate;
			pv = num / denom;
		}

		return pv;
	}

	private static class PvScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 5)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.pv()");
			return pv( getDouble( arguments[0] ),
					getInteger( arguments[1] ),
					getDouble(  arguments[2] ),
					getDouble( arguments[3] ),
					getInteger( arguments[4] ));
		}
	}
	/**
	 * @param rate
	 *            the interest rate per period.
	 * @param due
	 *            the number 0 or 1 and indicates when payments are due. If type
	 *            is omitted, it is assumed to be 0.
	 * @param fv
	 *            the future value, or a cash balance you want to attain after
	 *            the last payment is made. If fv is omitted, it is assumed to
	 *            be 0 (the future value of a loan, for example, is 0).
	 * @param nPer
	 *            the total number of payment periods in an annuity. For
	 *            example, if you get a four-year car loan and make monthly
	 *            payments, your loan has 4*12 (or 48) periods. You would enter
	 *            48 into the formula for nper.
	 * @param true_pmt
	 * @param loanamt
	 * @param incr
	 * @param attempt
	 *            the number we have failed
	 * @param found
	 *            is only a tag
	 * @return The function the basis of rate()
	 */
	static private double solvePmt( int nPer, double true_pmt, double loanamt,
			double fv, int due, double rate, double incr, int attempt,
			boolean found )
	{
			double tolerance;
			double diff1;
			double diff2;

			tolerance = .0000001; /*
								   * this will provide precision past the 3rd
								   * decimal
								   */

			if ( attempt > 1000 )
			{
				found = false;
				return -1;
			}

			diff1 = true_pmt - pmt( rate + incr, nPer, loanamt, fv, due );

			if ( Math.abs( diff1 ) <= tolerance )
			{
				found = true;
				return ( rate + incr );
			}
			diff2 = true_pmt - pmt( rate, nPer, loanamt, fv, due );
			if ( Math.abs( diff2 ) <= tolerance )
			{
				found = true;
				return ( rate );
			}
			if ( diff1 * diff2 < 0 )
				incr /= 10;
			else
				rate += incr;
			return ( solvePmt( nPer, true_pmt, loanamt, fv, due, rate,
					incr, ++attempt, found ) );
	}

	/**
	 * @param due
	 *            the number 0 or 1 and indicates when payments are due. If type
	 *            is omitted, it is assumed to be 0.
	 * @param fv
	 *            the future value, or a cash balance you want to attain after
	 *            the last payment is made. If fv is omitted, it is assumed to
	 *            be 0 (the future value of a loan, for example, is 0).
	 * @param pmt
	 *            the payment made each period; it cannot change over the life
	 *            of the annuity. Typically, pmt contains principal and interest
	 *            but no other fees or taxes.
	 * @param nPer
	 *            the total number of payment periods in an annuity. For
	 *            example, if you get a four-year car loan and make monthly
	 *            payments, your loan has 4*12 (or 48) periods. You would enter
	 *            48 into the formula for nper.
	 * @param pv
	 *            the present value of the total amount that a series of future
	 *            payments is worth now.
	 * @param guess
	 *            is your guess for what the rate will be. If you omit guess, it
	 *            is assumed to be 10 percent. If RATE does not converge, try
	 *            different values for guess. RATE usually converges if guess is
	 *            between 0 and 1.
	 * @return the interest rate per period for an annuity
	 * 
	 * Remark
	 * 
	 * Make sure that you are consistent about the units you use for specifying
	 * guess and nper. If you make monthly payments on a four-year loan at 12
	 * percent annual interest, use 12%/12 for guess and 4*12 for nper. If you
	 * make annual payments on the same loan, use 12% for guess and 4 for nper.
	 *  
	 */
	static double rate( int nPer, double pmt, double pv, double fv,
			int due, double guess ) throws IllegalArgumentException
	{
			int i;
			double tmp;
			double curr_guess;
			double incr = .1;
			boolean found = true;

			double result = 0;

			if ( nPer <= 0 || ( due != 1 && due != 0 ) )
			{
				if ( nPer <= 0 )
				{
					throw ( new IllegalArgumentException(
							"There exists illegal parameter:" + nPer ) );
				}

				throw ( new IllegalArgumentException(
						"There exists illegal parameter:" + due ) );
			}

			/*
			 * Try successive guesses at interest rate, 1st with positive
			 * increment, then negative
			 */

			i = 5;
			curr_guess = guess;
			for ( i--; i > 0; i-- )
			{
				incr = .1;
				tmp = curr_guess;
				result = solvePmt( nPer, pmt, pv, fv, due, tmp, incr, 0,
							found );
				if ( result != -1 )
					return result;

				curr_guess = curr_guess / 2;
			}

			i = 4;
			curr_guess = guess * 2;
			for ( i--; i > 0; i-- )
			{ // Scale the guess up and try again.
				incr = .1;
				tmp = curr_guess;

				result = solvePmt( nPer, pmt, pv, fv, due, tmp, incr, 0, found );
				if ( result != -1 )
					return result;

				curr_guess = curr_guess * 2;
			}

			i = 5;
			curr_guess = guess;
			for ( i--; i > 0; i-- )
			{ // Scale the guess down and try again.
				incr = -.1;
				tmp = curr_guess;

				result = solvePmt( nPer, pmt, pv, fv, due, tmp, incr, 0, found );
				if ( result != -1 )
					return result;

				curr_guess = curr_guess / 2;
			}

			i = 4;
			curr_guess = guess * 2;
			for ( i--; i > 0; i-- )
			{ // Scale the guess up and try again.
				incr = -.1;
				tmp = curr_guess;

				result = solvePmt( nPer, pmt, pv, fv, due, tmp, incr, 0, found );
				if ( result != -1 )
					return result;

				curr_guess = curr_guess * 2;
			}
		throw new IllegalArgumentException(
				"User should reset a new rate guess" );
		//return result;

	}
	
	private static class RateScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 6)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.rate()");
			return rate( getInteger( arguments[0] ),
					getDouble( arguments[1] ),
					getDouble(  arguments[2] ),
					getDouble( arguments[3] ),
					getInteger( arguments[4] ),
					getDouble( arguments[5] ));
		}
	}
	/**
	 * @param denom
	 *            the denominator
	 * @param num
	 *            the numerator
	 * @param valueIfZero
	 * 			  The percent value to return if the numerator is zero. The default is null.
	 * @return the percentage of two numbers
	 */
	static double percent(double denom, double num, double valueIfZero)
	{
		if(num == 0d)
		{
			return valueIfZero;
		}
		if(denom == 0d)
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter:" + denom) );
		}
		
		return ( num / denom ) * 100;
	}

	private static class PercentScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || (arguments.length!= 2 && arguments.length!= 3))
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.percent()");
			return percent( getDouble( arguments[0] ),
					getDouble( arguments[1] ),
					arguments.length == 3?getDouble(  arguments[2] ):0);
		}
	}
	/**
	 * @param arptr
	 *            array of Doubles that specifies the name of an existing array
	 *            of cash flow values. Rule for <casharray>: Array must contain
	 *            at least one positive value (receipt) and one negative value
	 *            (payment)
	 * @param rate
	 *            the rate of discount over the length of one period.
	 * @return the net present value of a varying series of periodic cash flows,
	 *         both positive and negative, at a given interest rate
	 * 
	 * Remarks
	 * 
	 * The NPV investment begins one period before the date of the value1 cash
	 * flow and ends with the last cash flow in the list. The NPV calculation is
	 * based on future cash flows. If your first cash flow occurs at the
	 * beginning of the first period, the first value must be added to the NPV
	 * result, not included in the values arguments. For more information, see
	 * the examples below. If n the number of cash flows in the list of values,
	 * the formula for NPV is:
	 * 
	 * 
	 * NPV is similar to the PV function (present value). The primary difference
	 * between PV and NPV is that PV allows cash flows to begin either at the
	 * end or at the beginning of the period. Unlike the variable NPV cash flow
	 * values, PV cash flows must be constant throughout the investment. For
	 * information about annuities and financial functions, see PV. NPV is also
	 * related to the IRR function (internal rate of return). IRR is the rate
	 * for which NPV equals zero: NPV(IRR(...), ...) = 0.
	 */
	static double npv( double rate, double[] arptr )
			throws IllegalArgumentException
	{
		if (arptr == null)
		{
			throw ( new IllegalArgumentException(
					"There exists illegal parameter:arptr" ) );
		}
		double npv = 0;
		double r1 = 1 + rate;
		double trate = r1;
		for ( int i = 0; i < arptr.length; i++ )
		{
			npv += arptr[i] / trate;
			trate = trate * r1;
		}

		return npv;
	}

	private static class NpvScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 2)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.npv()");
			if( arguments[1] instanceof Object[] )
			{
				Object[] array = (Object[])arguments[1];
				double[] arg = new double[array.length];
				for( int i = 0; i < arg.length; i++ )
				{
					arg[i] = getDouble( array[i] );
				}
				
				return npv( getDouble( arguments[0] ),
						arg );
				
			}
			throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.type.of.argument" )+ "Finance.npv()");
		}
	}
	
	/**
	 * @param cash
	 *            array of Doubles that specifies the name of an existing array
	 *            of cash flow values. Rule for <casharray>: Array must contain
	 *            at least one positive value (receipt) and one negative value
	 *            (payment)
	 * @param intrate
	 * @param inc
	 * @param attempt
	 * @return the internal rate of return for a series of periodic cash flows,
	 *         payments and receipts, in an existing array
	 */
	private static double calcIrr( double[] cash, double intrate, double inc,
			int attempt )
	{

		double tolerance;
		double npv1;
		double npv2;
		//int numflows;
		//if ( cash != null )
		//{
			//numflows = cash.length;
		//}

		tolerance = 0.0000001; /*
							    * this will provide precision past the 3rd
							    * decimal
							    */

		//If it cannot determine a result after 1000 iterations, the function
		// fails
		if ( attempt > 1000 )
		{
			return -1;
		}

		npv1 = npv( intrate + inc, cash  );
		//printf("Int Rate %1.3f generates delta=%1.3f\n",(*intrate +
		// *inc),npv1);

		npv2 = npv( intrate, cash );
		if ( Math.abs( npv1 - npv2 ) <= tolerance )
			return ( intrate + inc );
		if ( npv2 * npv1 < 0 )
			inc /= 10;
		else
			intrate += inc;
		return ( calcIrr( cash, intrate, inc, ++attempt ) );
	}

	/**
	 * @param cash
	 *            specifies the name of an existing array of Doubles
	 *            representing cash flow values
	 * @param intrate
	 *            is a number that you guess is close to the result of IRR.
	 * 
	 * @return This function takes the initial guess and scales it up and down
	 *         to see if a solution IRR can be found. It also checks for 'more
	 *         than 1 sign change' type of errors.
	 * 
	 * Remarks
	 * 
	 * IRR is closely related to NPV, the net present value function. The rate
	 * of return calculated by IRR is the interest rate corresponding to a 0
	 * (zero) net present value. The following formula demonstrates how NPV and
	 * IRR are related
	 *  
	 */
	static double irr( double[] cash, double intrate )
			throws IllegalArgumentException
	{
		boolean arg1Positive = true; // Is the first element of array > 0?
		long arsize = 0;
		double curr_rate;
		double incr;
		double result;
		double tmp;
		int i;
		
		arsize = cash.length;
		
		tmp = cash[0];
		if ( tmp < 0 )
		{
			arg1Positive = false;
		}
		else if ( tmp > 0 )
		{
			arg1Positive = true;
		}
		else
		{
			throw new IllegalArgumentException( "BADSIGNSINARRAY" );
		}
		/*
		 * Verify that the sign of at least one element in 1st thru N-th
		 * position in array is different from the sign of the element in the
		 * 0th position.
		 */
		boolean found = false;
		for ( i = 1; i <= arsize; i++ )
		{
				tmp = cash[i - 1];
			if ( ( arg1Positive && tmp < 0 ) || ( ( !arg1Positive ) && tmp > 0 ) )
			{
				found = true;
				break;
			}
		}

		if ( !found )
		{
			throw new IllegalArgumentException( "BADSIGNSINARRAY" );
			//return -1;
		}

		i = 10;
		curr_rate = intrate;
		for ( i--; i > 0; i-- )
		{ // Scale the guess down and try again.
			incr = .1;
			tmp = curr_rate;
			if ( ( result = calcIrr( cash, tmp, incr, 0 ) ) < 0
					&& ( result != -1 ) )
				return result;

			curr_rate = curr_rate / 2;
		}

		i = 4;
		curr_rate = intrate * 2;
		for ( i--; i > 0; i-- )
		{ // Scale the guess up and try again.
			incr = .1;
			tmp = curr_rate;
			if ( ( result = calcIrr( cash, tmp, incr, 0 ) ) >= 0 )
				return result;

			curr_rate = curr_rate * 2;
		}
		i = 10;
		curr_rate = intrate;
		for ( i--; i > 0; i-- )
		{ // Scale the guess down and try again.
			incr = -.1;
			tmp = curr_rate;
			if ( ( result = calcIrr( cash, tmp, incr, 0 ) ) < 0
					&& ( result != -1 ) )
				return result;

			curr_rate = curr_rate / 2;
		}

		i = 4;
		curr_rate = intrate * 2;
		for ( i--; i > 0; i-- )
		{ // Scale the guess up and try again.
			incr = -.1;
			tmp = curr_rate;
			if ( ( result = calcIrr( cash, tmp, incr, 0 ) ) >= 0 )
				return result;

			curr_rate = curr_rate * 2;
		}
		throw new IllegalArgumentException( "NOSOLUTIONFOUND" );
	}
	
	private static class IrrScriptFunctionExecutor implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if( arguments == null || arguments.length!= 2)
				throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.number.of.argument" )+ "Finance.irr()");
			if( arguments[0] instanceof Object[] )
			{
				Object[] array = (Object[])arguments[0];
				double[] arg = new double[array.length];
				for( int i = 0; i < arg.length; i++ )
				{
					arg[i] = getDouble( array[i] );
				}
				
				return irr( arg, getDouble( arguments[1] ));
				
			}
			else if( arguments[0].getClass( ).isAssignableFrom( double[].class ) )
			{
				return irr( (double[])arguments[0], getDouble( arguments[1] ));
				
			}
			throw new BirtException( "org.eclipse.birt.core.script.general", null, Messages.getString( "invalid.type.of.argument" )+ "Finance.irr()");
		}
	}
	
	private static double neg_npv( double rate, double[] arptr )
	{
		double npv = 0;
		double r1 = 1 + rate;
		double trate = r1;
		for ( int i = 0; i < arptr.length; i++ )
		{
			if (arptr[i] < 0)
			{
				npv += arptr[i] / trate;
			}
			trate = trate * r1;
		}

		return npv;
	}
	
	private static double pos_npv( double rate, double[] arptr )
	{
		double npv = 0;
		double r1 = 1 + rate;
		double trate = r1;
		for ( int i = 0; i < arptr.length; i++ )
		{
			if (arptr[i] >= 0)
			{
				npv += arptr[i] / trate;
			}
			trate = trate * r1;
		}

		return npv;
	}

	/**
	 * @param arptr
	 *            array of Doubles that specifies the name of an existing array
	 *            of cash flow values
	 * @param frate
	 *            the interest rate you pay on the money used in the cash flows.
	 * @param rrate
	 *            the interest rate you receive on the cash flows as you
	 *            reinvest them.
	 * @return the modified internal rate of return for a series of periodic
	 *         cash flows (payments and receipts) in an existing array
	 * 
	 * Remarks
	 * 
	 * MIRR uses the order of values to interpret the order of cash flows. Be
	 * sure to enter your payment and income values in the sequence you want and
	 * with the correct signs (positive values for cash received, negative
	 * values for cash paid). If n is the number of cash flows in values, frate
	 * is the finance_rate, and rrate is the reinvest_rate.
	 * 
	 *  
	 */
	static double mirr( double[] arptr, double frate, double rrate )
			throws IllegalArgumentException
	{
		if (arptr == null)
		{
			throw new IllegalArgumentException( "ARRAYREFERR" );
		}
		int negCount = 0;
		int posCount = 0;
		for (int i = 0; i < arptr.length; i++)
		{
			if (arptr[i] >= 0)
			{
				posCount++;
			}
			else
			{
				negCount++;
			}
		}
		if (negCount == 0 || posCount == 0)
		{
			throw new IllegalArgumentException( "ARRAYREFERR" );
		}
		
		double posNpv = pos_npv(rrate, arptr);
		double negNpv = neg_npv(frate, arptr);

		double tmp = ( -posNpv * Math.pow( ( 1.0 + rrate ), arptr.length ) )
				/ ( negNpv * ( 1.0 + frate ) );

		return Math.pow( tmp , ( 1.0 / ( arptr.length - 1 ) ) ) - 1.0 ;
	}
	
	private static class MirrScriptFunctionExecutor
			implements
				IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;

		public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
		{
			if ( arguments == null
					|| arguments.length != 2 && arguments.length != 3 )
				throw new BirtException( "org.eclipse.birt.core.script.general",
						null,
						Messages.getString( "invalid.number.of.argument" )
								+ "Finance.mirr()" );
			if ( arguments[0] instanceof Object[] )
			{
				Object[] array = (Object[]) arguments[0];
				double[] arg = new double[array.length];
				for ( int i = 0; i < arg.length; i++ )
				{
					arg[i] = getDouble( array[i] );
				}

				return mirr( arg,
						getDouble( arguments[1] ),
						arguments.length == 3
								? getDouble( arguments[2] ) : 0 );

			}
			else if ( arguments[0].getClass( )
					.isAssignableFrom( double[].class ) )
			{
				return mirr( (double[]) arguments[0],
						getDouble( arguments[1] ),
						arguments.length == 3
								? getDouble( arguments[2] ) : 0 );

			}
			throw new BirtException( "org.eclipse.birt.core.script.general",
					null,
					Messages.getString( "invalid.type.of.argument" )
							+ "Finance.mirr()" );
		}
	}
	
	static IScriptFunctionExecutor getExecutor( String functionName ) throws BirtException
	{
		if( DDB.equals( functionName ))
			return new DdbScriptFunctionExecutor();
		else if( SLN.equals( functionName ) )
			return new SlnScriptFunctionExecutor();
		else if( SYD.equals( functionName ))
			return new SydScriptFunctionExecutor();
		else if( FV.equals( functionName ))
			return new FvScriptFunctionExecutor();
		else if( PMT.equals( functionName ))
			return new PmtScriptFunctionExecutor();
		else if( IPMT.equals( functionName ))
			return new IpmtScriptFunctionExecutor();
		else if( PPMT.equals( functionName ))
			return new PpmtScriptFunctionExecutor();
		else if( NPER.equals( functionName ))
			return new NperScriptFunctionExecutor();
		else if( PV.equals( functionName ))
			return new PvScriptFunctionExecutor();
		else if( RATE.equals( functionName ))
			return new RateScriptFunctionExecutor();
		else if( PERCENT.equals( functionName ))
			return new PercentScriptFunctionExecutor();
		else if( NPV.equals( functionName ))
			return new NpvScriptFunctionExecutor();
		else if( IRR.equals( functionName ))
			return new IrrScriptFunctionExecutor();
		else if( MIRR.equals( functionName ))
			return new MirrScriptFunctionExecutor();
		
		throw new BirtException( "org.eclipse.birt.core.script.function.general",
				null,
				Messages.getString( "invalid.function.name" )
						+ "Finance." + functionName );
	}
}
