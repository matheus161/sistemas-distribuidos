/*
 * ALUNOS:
 * 		ERICK SANTOS RESENDE
 * 		MATHEUS LIMA PINHEIRO 
 * */

package chat_exercicio_04;

import com.rabbitmq.client.Channel;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Emissor extends Thread {
	private Channel channel;
	private static final String EXCHANGE_NAME = "mensagens";
	private static String QUEUE_NAME = "minha-fila";
	private static String NOME_RECEPTOR = "";
	
	public Emissor(Channel channel, String QUEUE_NAME) {
		this.channel = channel;
		this.NOME_RECEPTOR = QUEUE_NAME;
	}
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		try {
			while(true) {
				String mensagem = sc.nextLine();
				
				// Checando para quem ele deseja enviar
				if (mensagem.matches("^@.*")) {					
					// Checando espaços em branco na mensagem
					String blank_space = " ";
					Pattern pattern = Pattern.compile(blank_space);
					
					/*
					* Aqui, atraves da instancia de Pattern, chamando o método * matcher() 
					* e passando a fonte de busca
					*/
					Matcher match = pattern.matcher(mensagem);
					int localizacao = 0;
					
					// Ligando o motor 
					while(match.find()) {
						//Obtendo o inicio do que foi encontrado
						localizacao = (match.start());
					}
					
					QUEUE_NAME = mensagem.substring(1, localizacao);
					String end_message = NOME_RECEPTOR + ": " + mensagem;
					
					// (queue-name, durable, exclusive, auto-delete, params);
					channel.queueDeclare(QUEUE_NAME, false, false, false, null);
					
					// (exchange, routingKey, props, message-body );
					channel.basicPublish("", QUEUE_NAME, null, end_message.getBytes("UTF-8"));
					
					System.out.println(end_message);
				} else { // Enviando para todos
					String end_message = NOME_RECEPTOR + ": " + mensagem;
					channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
					// (exchange, routingKey, props, message-body );
					channel.basicPublish(EXCHANGE_NAME, "", null, mensagem.getBytes("UTF-8"));
					
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
