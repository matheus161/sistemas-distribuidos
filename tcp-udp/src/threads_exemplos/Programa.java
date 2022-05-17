package threads_exemplos;

public class Programa {
	
	public static void main(String[] args ) throws InterruptedException {
		MinhaThread threadA = new MinhaThread("Thread A");
		MinhaThread threadB = new MinhaThread("Thread B");
		
		threadA.start();
		threadB.start();
		
		System.out.println("FIM Main");
	}
}
