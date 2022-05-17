package threads_exemplos;

public class MinhaThreadRunnable implements Runnable{
	private String nome;
	private Object notificar;
	
	public MinhaThreadRunnable(String nome, Object notificar) {
		this.nome = nome;
		this.notificar = notificar;
	}
	
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.printf("%s %d%n", this.nome, i);
			
			synchronized (this.notificar) {
				this.notificar.notifyAll();
			}
		}
		
		System.out.printf("FIM %s%n", this.nome);
	}
}

