import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;


public class Car extends Thread{
	
	
	private String name;
	public volatile int count =0;
	private Thread t;
	public static AtomicInteger seats = new AtomicInteger();
	public  static Vector<Object> WaitingCars = new Vector<Object>();
	public  static Vector<Object> FullCars = new Vector<Object>();
	public static boolean carAv = false;
	private static int tempSeats = main.seatSize.get();
	public static boolean departure;
	public static AtomicInteger PassengersLeft = new AtomicInteger(0);
	public static long time = System.currentTimeMillis();
	public static int car = 0;

	
	public Car(String name, int num, AtomicInteger seat)
	{
		seats = seat;
		this.name = name;
		count = num;
		departure = false;	
	}
	
	
	public synchronized void notifyCar()
	{	
		if(WaitingCars.size()>0)
		{
			synchronized (WaitingCars.elementAt(0)) {
	            WaitingCars.elementAt(0).notify();
	         }
	         WaitingCars.removeElementAt(0);
		}
	}
	
	public synchronized void notifyFullCar()
	{	
		if(FullCars.size()>0)
		{
			synchronized (FullCars.elementAt(0)) {
	            FullCars.elementAt(0).notify();
	         }
	         FullCars.removeElementAt(0);
		}
	}
	
	
	
	public void carAvaliable()
	{
		loop:
			while(main.nPassengers!=PassengersLeft.get())
			{
				Object convey = new Object();
				synchronized(convey){
					 try{
						 	if(carAv==false)
						 	{
						 		
						 		WaitingCars.addElement(convey);
				                //System.out.println("["+(System.currentTimeMillis()-time)+"]" + "Car " + this.count + " is waiting.");
				                if(Passengers.counter.get()==main.seatSize.get() || Passengers.left.get()==0)
				            	{
				            		Car.carAv=true;
				            		main.car.notifyCar();
				            	}
				                convey.wait();
				                if(PassengersLeft.get()>=main.nPassengers)
				                {
				                	break loop;
				                }
				                //System.out.println(PassengersLeft);
				                seats.set(tempSeats);
				                carAv=true;
				                if(WaitingCars.size()==1)
				                {
				        
				                	carAv=false;
				                	
				                }
						 	}
						 
			            	if(carAv == true)
			            	{		            		
			            		carAv=false;
			            		loop2:
			            		while(seats.get()>0)
			            		{
			            			if(!Passengers.WaitingPassengers.isEmpty())
			            			{
				            			seats.decrementAndGet();
				            			main.passenger.setWaitForCarfalse();
				        				main.passenger.sitInCar(this.count);
				            			System.out.println("-----------" + this.name + this.count + " has " + seats.get() + " seats left" + "------------");            			
				        				PassengersLeft.incrementAndGet();        				
				        				if(Passengers.WaitingPassengers.size()==0 && main.nPassengers>=PassengersLeft.get() )
						            	{
						            		System.out.println("["+(System.currentTimeMillis()-time)+"]" + this.name + this.count + " is asking for permission to Depart.");
						            		seats.set(tempSeats);						       		
						            		FullCars.addElement(convey);
						            		main.controller.notifyController();
						            		convey.wait();		
						            		System.out.println("["+(System.currentTimeMillis()-time)+"]" + this.name + this.count + " rides around park and drops passengers");
						            		//notifyCar();
						            		try {
												t.sleep((long)(Math.random() * 1000));
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}	
						            		if(PassengersLeft.get()>main.nPassengers)
						            			break loop;	
						            		break loop2;
						            	}	
			            			}
			            		}
			            	}	
			            	if(seats.get()==0)
			            	{
			            		System.out.println("["+(System.currentTimeMillis()-time)+"]" + this.name + this.count + " has " + seats + " seats left : finish");
			            		System.out.println("["+(System.currentTimeMillis()-time)+"]" + this.name + this.count + " is waiting for controller to Depart. ");
			            		notifyCar();
			            		main.controller.notifyController();
			            		FullCars.addElement(convey);		            		
			            		convey.wait();
			            		System.out.println("["+(System.currentTimeMillis()-time)+"]" + this.name + this.count + " rides around park and drops passengers.");          		
			            		WaitingCars.addElement(convey);
			            		//notifyCar();
			            	}			            	     
			            }catch(InterruptedException e){
			                e.printStackTrace();
			            }
				}
				
			}
			System.out.println("["+(System.currentTimeMillis()-time)+"]" + this.name + this.count + " leaves because no more passengers");
			car++;
			main.controller.notifyController();
			notifyCar();
	}
	
	public void run()
	{	
		carAvaliable();	
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
