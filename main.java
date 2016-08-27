import java.util.concurrent.atomic.AtomicInteger;


public class main {
	public static Passengers passenger;
	public static Car car;
	public static controller controller;
	public static int nPassengers;
	public static int m =23,n=3,c=2;
	public static AtomicInteger seatSize;
	
	public static void main(String args[])
	{
		if(args.length==0)
		{
			m =23;
			n=3;
			c=2;
			seatSize= new AtomicInteger(5);
		}
		else
		{
			m = Integer.parseInt(args[0]);
			n = Integer.parseInt(args[1]);
			c = Integer.parseInt(args[2]);
			seatSize = new AtomicInteger(Integer.parseInt(args[3]));
			
		}
		
		
		for(int i=1;i<=m;i++)
		{
			passenger = new Passengers("Passenger ", i);
			passenger.start();
			nPassengers++;
			
		}
		
		for(int i=1;i<=c;i++)
		{
			controller = new controller("Controller ", i);
			controller.start();
		}
		
		
		for(int i=1;i<=n;i++)
		{
			car = new Car("Car ", i, seatSize);
			car.start();
		}
		
		

		
		
		
	}

}
