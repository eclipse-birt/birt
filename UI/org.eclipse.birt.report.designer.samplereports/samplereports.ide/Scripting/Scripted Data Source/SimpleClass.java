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
import java.util.Vector;
public class SimpleClass
{
	public Vector readData(){
	//This function simulates a read from an EJB/DB
	Vector rtnV = new Vector();
	rtnV.add(new String[]{"ANG Resellers", "1952 Alpine Renault 1300","Red"});
	rtnV.add(new String[]{"AV Stores, Co.", "1969 Harley Davidson Ultimate Chopper","Blue"});
	rtnV.add(new String[]{"Alpha Cognac", "1969 Ford Falcon","Blue"});
	rtnV.add(new String[]{"American Souvenirs Inc", "1968 Ford Mustang","Green"});
	rtnV.add(new String[]{"Amica Models & Co.", "1969 Corvair Monza","Blue"});
	rtnV.add(new String[]{"Anna's Decorations, Ltd", "1969 Corvair Monza","Yellow"});
	rtnV.add(new String[]{"Anton Designs, Ltd.", "1970 Plymouth Hemi Cuda","Orange"});
	rtnV.add(new String[]{"Asian Shopping Network, Co", "1969 Dodge Charger","Plum Crazy Purple"});
	rtnV.add(new String[]{"Asian Treasures, Inc.", "1969 Corvair Monza","Red"});
	rtnV.add(new String[]{"Atelier graphique", "1972 Alfa Romeo GTA","Metalic Silver"});

	return (rtnV);
	}
	
	
  public static void main(String[] args)
   {  
	SimpleClass sc = new SimpleClass();
	Vector rtn = sc.readData();
	for( int i=0; i<rtn.size(); i++ ){
		String[] sa = (String[])rtn.get(i); 
		System.out.println(sa[0] + "--" + sa[1] + "--" + sa[2] );
	}
   }
}

