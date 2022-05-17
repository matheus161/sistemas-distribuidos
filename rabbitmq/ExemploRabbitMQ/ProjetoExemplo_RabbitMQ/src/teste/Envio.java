package teste;

import com.rabbitmq.client.Channel;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Envio extends Thread {
	private Channel channel;
	private String nome;
	public Envio(Channel channel, String nome) {
		this.channel = channel;
		this.nome = nome;
		}
	private static String EXCHANGE_NAME = "mensagens";
	private static String QUEUE_NAME = "";
	
	public void run() {
		try {
			while(true) {
				Scanner scan = new Scanner(System.in);
				String mensagem = scan.nextLine();
				
				// enviando para todos
				if(mensagem.matches("^@.*")) {
					
					//encontrando o espaço em branco na mensagem
					String queremosisso = " ";
					Pattern p = Pattern.compile(queremosisso);
					
					/*
					* Aqui, atraves da instancia de Pattern, chamando o método * matcher() e passando a fonte de busca
					*/
					
					Matcher m = p.matcher(mensagem);
					int localizacao = 0;
					//Ligando o motor
					while (m.find()) {
					
					//Obtendo o inicio do que foi encontrado
					localizacao = (m.start());
					}
					
					QUEUE_NAME = mensagem.substring(1, localizacao);
					String mensagemparaenvio = nome + ": " + mensagem;
					channel.queueDeclare(QUEUE_NAME, false, false, false, null);
					channel.basicPublish("", QUEUE_NAME, null, mensagemparaenvio.getBytes("UTF-8"));
					System.out.println(mensagemparaenvio);
				} else {
					// (exchange, routingKey, props, message-body );
					String mensagemparaenvio = nome + ": " + mensagem;
					channel.basicPublish(EXCHANGE_NAME, "", null, mensagemparaenvio.getBytes("UTF-8"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
