package org.eclipse.birt.report.designer.ui.samples.nls;
//Just Sample For Newbie as Me
import java.util.Scanner;

public class InputOutputandScanner {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Name = ");
		String name = scan.nextLine();
		
		System.out.print("Your Height on Centimeter ");
		String stringHeight = scan.nextLine();
		
		System.out.print("Your Weight On KiloGram ");
		String stringWeight = scan.nextLine();
		
		scan.close();
		
		Double height = Double.parseDouble(stringHeight)/100; //This is a BMI Calculate
		Double weight = Double.parseDouble(stringWeight);
		
		Double bmi = weight/(height*height);
		System.out.println("Your Name is "+name);
		System.out.println("Your Height is "+stringHeight+" cm");
		System.out.println("Your Weight is "+stringWeight+" kg");
		System.out.println("Yours BMI is "+bmi);

	}

}
