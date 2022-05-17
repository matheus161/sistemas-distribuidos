/*
 * Alunos: Erick Santos Resende e Matheus Lima Pinheiro 
 * 
 * */

package projeto_final;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rabbitmq.client.Channel;

public class CentralThread extends Thread {
	private Channel channel;
	private static final String EXCHANGE_NAME = "redemotoristas";
	private static String DIRECT_MSG = "";
	
	// Criando a thread e recebendo o channel da Central
	public CentralThread(Channel channel) {
		this.channel = channel;
	}
	
	// Fun��o que envia para todos que est�o na rede (Fanout)
	public void sendToAll(Mensagem msg) {
		try {
			// Declando fila de envio Fanout
			this.channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			
			// Serializando objeto e gravando-o
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream ous = new ObjectOutputStream(baos);
			ous.writeObject(msg);
						
			// Transformado-o em byte[]
			byte [] body = baos.toByteArray();
			
			// Enviando para a rede
			this.channel.basicPublish(EXCHANGE_NAME, "", null, body);
			System.out.println(" [x] Mensagem enviada");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Fun��o que envia para um usu�rio espec�fico (Direct)
	public void directMensage(Mensagem msg) {
		Scanner input = new Scanner(System.in);
		System.out.println("[x] Digite uma mensagem informando o destinatario com @: ");
		String mensagem = input.nextLine();
		
		// Checando para quem ele deseja enviar
		if (mensagem.matches("^@.*")) {					
			// Checando espa�os em branco na mensagem
			String blank_space = " ";
			Pattern pattern = Pattern.compile(blank_space);
					
			// Atrav�s da inst�ncia de Pattern, 
			// chamo o m�todo matcher() 
			// e passo a fonte de busca
			Matcher match = pattern.matcher(mensagem);
			int localizacao = 0;
			
			// Ligando o motor 
			while(match.find()) {
				//Obtendo a posi��o em branco
				localizacao = (match.start());
			}
			System.out.println(localizacao);
			// Declarando para quem ser� enviado a mensagem
			// Pegando da primeira letra depois do @ at� o espa�o em branco
			DIRECT_MSG = mensagem.substring(1, localizacao);
			
			// Informando a mensagem
			msg.setMensagem("Mensagem recebida da "+ msg.getMotorista() + ": " + mensagem.substring(localizacao+1));
			
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
		System.out.println(" [*] Escolha uma das op��es abaixo: ");
		System.out.println(" [*] 1. Enviar um Alerta");
		System.out.println(" [*] 2. Enviar uma mensagem direta");
		
		Mensagem msg = new Mensagem("Central");
		int opcao = -1;
		
		while(opcao != 0) {
			opcao = sc.nextInt();
			
			switch (opcao) {
			case 1:
				msg.setMensagem("Alerta recebido da Central");
				// Chamando a fun��o de envio para todos
				sendToAll(msg);
				System.out.println(" [x] Escolha uma op��o: ");
				break;
			case 2:
				// Chamando a fun��o de envio direto
				directMensage(msg);
				System.out.println(" [x] Escolha uma op��o: ");
				break;

			default:
				System.out.println("Escolha uma op��o v�lida");
				break;
			}
		}
	}
}
