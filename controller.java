import java.util.Vector;


public class controller extends Thread{

	
	private String name;
	private Thread t;
	public volatile int count =0;
	public  static Vector<Object> WaitingCon = new Vector<Object>();
	public static long time = System.currentTimeMillis();
	
	public controller(String name, int num)
	{
		this.name = name;
		count = num;
	}
	
	public synchronized void notifyController()
	{	
		if(WaitingCon.size()>0)
		{
			synchronized (WaitingCon.elementAt(0)) {
	            WaitingCon.elementAt(0).notify();
	           
	         }
	         WaitingCon.removeElementAt(0);
	     
		}
	}
	
	@SuppressWarnings("static-access")
	public void checkForFullCar()
	{
		loop:
			while(true)
			{
				Object convey = new Object();
				synchronized(convey){
					try {

						
						WaitingCon.addElement(convey);
						
						if(Car.FullCars.size()>0){
							main.car.notifyFullCar();
						}
						convey.wait();
						if(Car.FullCars.size()>0){
							main.car.notifyFullCar();
						}
						System.out.println("["+(System.currentTimeMillis()-time)+"]" + this.name + this.count + ": checking for tickets to give permission to a car.");
						
						try {
							t.sleep((long)(Math.random() * 1000));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						
						main.car.notifyFullCar();
						if(Car.PassengersLeft.get()==main.nPassengers && Car.FullCars.isEmpty() )
						{
							System.out.println("["+(System.currentTimeMillis()-time)+"]" + "Controller " + this.count+ ": leaves");
							notifyController();
							break loop;
						}
						WaitingCon.addElement(convey);	
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
			while(Car.car!=0)
			{
				main.car.notifyCar();
				Car.car--;
			}
	}
	
	public void run()
	{
		checkForFullCar();
	}
	
	
	public void start()
	{
		System.out.println("Starting " + name + " "+ count);
		if(t== null)
		{
			t = new Thread(this, name);
			t.start();
		}
	}
}
