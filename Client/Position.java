/*
  Daniel Hedeblom
  Borås Högskola
  Utvecklingsingenjör Elektronik & Data
*/

class Position
{
    Character.Direction drawType;
    int y,x;


    public Position()
    {
	//System.out.println("Skapar position.");
    }

    public Position(int s_x, int s_y)
    {
	//System.out.println("Skapar position.");
	x = s_x;
	y = s_y;
    }

    public void setX(int s_x)
    {
	x = s_x;
    }

    public int getX()
    {
	return x;
    }

    public void setY(int s_y)
    {
	y = s_y;
    }

    public int getY()
    {
	return y;
    }

    public String toString()
    {
	return "(" + x + "," + y + ")";
    }

    public void setPosition(int s_x, int s_y)
    {
	y = s_y;
	x = s_x;
    }

    public double getDistance()
    {
	return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    public Position add(Position a)
    {
	setPosition(x + a.getX(), y + a.getY());
	return this;
    }

    public Position sub(Position a)
    {
	setPosition(x - a.getX(), y - a.getY());
	return this;
    }
}
