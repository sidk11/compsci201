
public class Planet {
	double myXVel;
	double myYVel;
	double myXPos;
	double myYPos;
	double myMass;
	String myFileName;
	
	public Planet(double xp, double yp, double xv,
            double yv, double mass, String filename)
	{
		myXPos = xp;
		myYPos = yp;
		myXVel = xv;
		myYVel = yv;
		myMass = mass;
		myFileName = filename;
		
	}
	
	public Planet(Planet p)
	{
		this.myXPos = p.myXPos;
		this.myYPos = p.myYPos;
		this.myXVel = p.myXVel;
		this.myYVel = p.myYVel;
		this.myMass = p.myMass;
		this.myFileName = p.myFileName;
	}
	
	public double calcDistance(Planet dp)
	{
		double dist;
		dist = Math.sqrt(Math.pow(myXPos-dp.myXPos,2) + Math.pow(myYPos-dp.myYPos,2));
		return dist;
	}
	
	public double calcForceExertedBy(Planet fp)
	{
		double force;
		force = (6.67*(Math.pow(10,-11)))*(myMass)*(fp.myMass)/(Math.pow(calcDistance(fp),2));
		return force;
	}
	
	public double calcForceExertedByX(Planet fxp)
	{
		double xforce;
		xforce = (calcForceExertedBy(fxp)*((fxp.myXPos - myXPos))/(calcDistance(fxp)));
		return xforce;
	}
	
	public double calcForceExertedByY(Planet fyp)
	{
		double yforce;
		yforce = (calcForceExertedBy(fyp)*((fyp.myYPos - myYPos)/(calcDistance(fyp))));
		return yforce;
	}
	
	public double calcNetForceExertedByX(Planet[] allPlanets)
	{
		double nxforce=0;
		for (int i=0; i<allPlanets.length; i++)
		{
			if(! allPlanets[i].equals(this))
			{
				nxforce += calcForceExertedByX(allPlanets[i]);
			}
		}
		return nxforce;
	}
	
	public double calcNetForceExertedByY(Planet[] allPlanets)
	{
		double nyforce=0;
		for (int j=0; j<allPlanets.length; j++)
		{
			if(! allPlanets[j].equals(this))
			{
				nyforce += calcForceExertedByY(allPlanets[j]);
			}
		}
		return nyforce;
	}

	public void update(double seconds, double xforce, double yforce)
	{
		double accx = xforce/this.myMass;
		double accy = yforce/this.myMass;
		this.myXVel = this.myXVel + (accx)*(seconds);
		this.myYVel = this.myYVel + (accy)*(seconds);
		this.myXPos = this.myXPos + (this.myXVel)*(seconds);
		this.myYPos = this.myYPos + (this.myYVel)*(seconds);
	}
	
	public void stdDraw()
	{
	StdDraw.picture(myXPos, myYPos, "images/"+myFileName);
	}
	
}

