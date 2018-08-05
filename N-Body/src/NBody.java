import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NBody {
	
	public static void main(String[] args){
		double totalTime = 157788000.0;
		double dt = 25000.0;
		String pfile = "data/planets.txt";
		if (args.length > 2) 
		{
			totalTime = Double.parseDouble(args[0]);
			dt = Double.parseDouble(args[1]);
			pfile = args[2];
		}
		
		String fname= "./data/planets.txt";
		Planet[] planets = readPlanets(fname);
		double radius = readRadius(fname);
	
		
		StdDraw.setScale(-radius, radius);
		StdDraw.picture(0,0,"images/starfield.jpg");
		
		for(int l=0; l<planets.length; l++)
		{
		planets[l].stdDraw();
		}
		
		for(double t = 0.0; t < totalTime; t += dt) {
			double[] xForces = new double[planets.length];
			double[] yForces = new double[planets.length];
			
			for(int l=0; l<planets.length; l++)
			{
				xForces[l] = planets[l].calcNetForceExertedByX(planets);
				yForces[l] = planets[l].calcNetForceExertedByY(planets);
			}
			
			for(int o=0; o<planets.length; o++)
			{
				planets[o].update(dt, xForces[o], yForces[o]);
			}
			
			StdDraw.picture(0,0,"images/starfield.jpg");
			
			for (int o=0; o<planets.length; o++) {
				planets[o].stdDraw();
			}

			StdDraw.show(10);
			
			}
		System.out.printf("%d\n", planets.length);
		System.out.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) 
		{
		    System.out.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		   		              planets[i].myXPos, planets[i].myYPos, 
		                      planets[i].myXVel, planets[i].myYVel, 
		                      planets[i].myMass, planets[i].myFileName);	
		}
		
		}
		
	 	public static double readRadius(String fname){
		    try {
		        Scanner scan = new Scanner(new File(fname));
		        int number = scan.nextInt();
		        double value = scan.nextDouble();
		        scan.close();
		        return value;
		    } catch (FileNotFoundException e) {
		    	System.exit(0);
		    }
			return 0;
			
	} 
		public static Planet[] readPlanets(String fname)
		{
			try {
		        Scanner planet = new Scanner(new File(fname));
		        int number = planet.nextInt();
		        double waste = planet.nextDouble();
		        Planet[] plt = new Planet[number];
		        for(int k=0; k<number; k++)
		        {
		        	 double x = planet.nextDouble();
		        	 double y = planet.nextDouble();
		        	 double vx = planet.nextDouble();
		        	 double vy = planet.nextDouble();
		        	 double mass = planet.nextDouble();
		        	 String name = planet.next();
		        	plt[k]= new Planet (x,y,vx,vy,mass,name);
		        }
		        
		        planet.close();
		        return plt;
		    } catch (FileNotFoundException e) {
		    }
			return null;
		}
		
		
}
