package threads_exemplos;

/*
 * RUN() - Quando eu utilizo o run, eu não estou pedindo para o Java
 * inicilizar uma thread para mim, e sim chamando o método
 * run que eu implementei, o que seria similar a chamar um
 * método convencional
 * 
 * JOIN() - Só retorna a chamada desse método quando
 * a thread em questão acabar
 * */

public class ProgramaRunnable {
	
	public static void main(String[] args ) throws InterruptedException {
		Object notificacao = new Object();
		
		MinhaThreadRunnable threadA = new MinhaThreadRunnable("Thread A", notificacao);
		MinhaThreadRunnable threadB = new MinhaThreadRunnable("Thread B", notificacao);
		
		//new Thread(threadA).run();
		Thread a = new Thread(threadA);
		Thread b = new Thread(threadB);
		a.start();		
		b.start();
		
		for(int i = 0; i < 20; i++) {
			/* Wait() e notify() são operações que não
			 * pode acontecer em paralelo, por isso
			 * a necessidade de sicroniza-las, todo mundo
			 * quer chamar esse Objeto, vai ficar bloqueado */
			
			synchronized (notificacao) {
				// Vou esperar até no máximo 1s para finalizar
				notificacao.wait(1000);
			}
			
			// Quando alguma Thread notificar eu vou imprimir
			System.out.println("Alguma thread entrou em execução");
			
			if(!a.isAlive()  && !b.isAlive()) {
				break;
			}
		}
		
		
		System.out.println("FIM Main");
		
	}
}
