import java.rmi.*;
import java.io.Serializable;
import java.util.concurrent.CyclicBarrier;

public class Barriere implements Serializable
{
	
	public final CyclicBarrier gate;
	
	public Barriere(int nbThreads){
		
		gate = new CyclicBarrier(nbThreads);
	}
	
	public CyclicBarrier getBarrier(){
		return gate;
	}
	
}
