/*
 * Alunos: Erick Santos Resende e Matheus Lima Pinheiro 
 * 
 * */

package projeto_final;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rabbitmq.client.Channel;

public class VeiculoThread extends Thread {
	private Channel channel;
	private String motorista;
	private static final String QUEUE_NAME = "central";
	private static final String EXCHANGE_NAME = "redemotoristas";
	private static String DIRECT_MSG = "";
	
	// Preciso receber o motorista para conseguir setar meu objeto
	public VeiculoThread(Channel channel, String motorista) {
		this.channel = channel;
		this.motorista = motorista;
	}

	public void send(Mensagem msg) {
		try {			
			// Serializando objeto e gravando-o
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream ous = new ObjectOutputStream(baos);
			ous.writeObject(msg);
			
			// Transformado-o em byte[]
			byte [] body = baos.toByteArray();
			
			// Enviando via Direct
			this.channel.basicPublish("", QUEUE_NAME, null, body);
			System.out.println(" [x] Mensagem enviada");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendToAll(Mensagem msg) {
		try {
			// Declando fila de envio Fanout
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			
			// Serializando objeto e gravando-o
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream ous = new ObjectOutputStream(baos);
			ous.writeObject(msg);
						
			// Transformado-o em byte[]
			byte [] body = baos.toByteArray();
			
			channel.basicPublish(EXCHANGE_NAME, "", null, body);
			System.out.println(" [x] Mensagem enviada");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void directMensage(Mensagem msg) {
		Scanner input = new Scanner(System.in);
		System.out.println("[x] Digite uma mensagem informando o destinatario com @: ");
		String mensagem = input.nextLine();
		
		// Checando para quem ele deseja enviar
		if (mensagem.matches("^@.*")) {					
			// Checando espaços em branco na mensagem
			String blank_space = " ";
			Pattern pattern = Pattern.compile(blank_space);
					
			// Através da instância de Pattern, 
			// chamo o método matcher() 
			// e passo a fonte de busca
			Matcher match = pattern.matcher(mensagem);
			int localizacao = 0;
			
			// Ligando o motor 
			while(match.find()) {
				//Obtendo a posição em branco
				localizacao = (match.start());
			}
			
			// Declarando para quem será enviado a mensagem
			// Pegando da primeira letra depois do @ até o espaço em branco
			DIRECT_MSG = mensagem.substring(1, localizacao);
			
			// Informando a mensagem
			msg.setMensagem("Mensagem recebida de "+ msg.getMotorista() + ": " + mensagem.substring(localizacao+1));
			
			try {
				// Serializando Objeto e gravando-o
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream ous = new ObjectOutputStream(baos);
				ous.writeObject(msg);
					
				// Transformado-o em byte[]
				byte [] body = baos.toByteArray();
					
				// Enviando via Direct
				this.channel.basicPublish("", DIRECT_MSG, null, body);
				System.out.println(" [x] Mensagem enviada");
								
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Digite a mensagem no formato correto");				
		}
	}
	
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		Random gerador = new Random();
		int opcao = -1;
		
		Mensagem msg = new Mensagem(this.motorista);		
		
		// Iniciando expediente
		msg.setMensagem(msg.getMotorista() + " se conectou a central");
		send(msg);
		msg.setId(gerador.nextInt(99999999));
		msg.setMensagem(msg.getMotorista() + " de id " + msg.getId() + " iniciando expediente");		
		sendToAll(msg);
		
		// Recolhendo nome do Motorista
		System.out.println("Bem-vindo, " + msg.getMotorista() + "! Escolha uma opção: ");
		System.out.println("0. Encerrar expediente");
		System.out.println("1. Enviar posição e velocidade atuais");
		System.out.println("2. Enviar mensagem para outro Motorista");
		System.out.println("3. Enviar alerta");
		System.out.println("4. Parada programada");
		
		while (opcao != 0) {
			opcao = sc.nextInt();
			
			switch (opcao) {
			case 0: // Encerrar programa/expediente
				msg.setMensagem(msg.getMotorista() + " finalizando expediente");
				break;
			case 1: // Enviar posicao e velocidade atuais
				msg.setX(gerador.nextInt(99999999));
				msg.setY(gerador.nextInt(99999999));
				msg.setVelo(gerador.nextInt(150));
				msg.setMensagem("Status atual do(a) " + msg.getMotorista() 
				+ ": \n Velocidade: " + msg.getVelo() + " Km/h" + "\n Posicao: " + "(" + msg.getX() + "," + msg.getY() + ")");
				send(msg);
				System.out.println(" [x] Escolha uma opção: ");
				break;
			case 2: // Mensagem direta
				directMensage(msg);	
				System.out.println(" [x] Escolha uma opção: ");
				break;
			case 3: // Enviar alerta
				msg.setX(gerador.nextInt(99999999));
				msg.setY(gerador.nextInt(99999999));
				msg.setMensagem("Alerta recebido de " + msg.getMotorista()
				+ " -> Posicao atual: " + "(" + msg.getX() + "," + msg.getY() + ")");
				sendToAll(msg);
				System.out.println(" [x] Escolha uma opção: ");
				break;
			case 4: // Parada programada
				msg.setMensagem(msg.getMotorista() + " irá fazer uma parada programa dentro de " + (gerador.nextInt(5) + 1) + " minuto(os)");
				send(msg);
				System.out.println("Alerta enviado!");
				System.out.println(" [x] Escolha uma opção: ");
				break;
			default:
				System.out.println(" [x] Escolha uma opção válida: ");
				break;
			}
		}
		
	}
}
