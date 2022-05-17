package br.ufs.dcomp.ExemploRabbitMQ.dual;

import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmissorDual extends Thread {
	private Channel channel;
	private static final String EXCHANGE_NAME = "mensagens";
	private static String QUEUE_NAME = "minha-fila";
	
	// Tenho que receber a QUEUE_NAME aqui para saber
	// para quem a mensagem será enviada
	public EmissorDual(Channel channel) {
		this.channel = channel;
	}
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		try {
			while(true) {
				System.out.println("[x] Digite uma mensagem informando o destinatario com @: ");
				String mensagem = sc.nextLine();
				
				// Checando para quem ele deseja enviar
				if (!mensagem.matches("^@.*")) {
					// Recebo o usuario que desejo enviar
					QUEUE_NAME = mensagem;
					
					// (queue-name, durable, exclusive, auto-delete, params);
					channel.queueDeclare(QUEUE_NAME, false, false, false, null);

					// (exchange, routingKey, props, message-body );
					channel.basicPublish("", QUEUE_NAME, null, mensagem.getBytes("UTF-8"));
					
					System.out.println(" [x] Mensagem enviada: '" + mensagem + "'");
				} else {
					channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	
					// (exchange, routingKey, props, message-body );
					channel.basicPublish(EXCHANGE_NAME, "", null, mensagem.getBytes("UTF-8"));
					System.out.println(" [x] Mensagem enviada: '" + mensagem + "'");
				}	
				
				System.out.println(" [*] Esperando recebimento de mensagens...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
