/*
 * @authors: Erick Santos Resende e Matheus Lima Pinheiro
 * */

package Prova_01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Emissor {

	private final static String QUEUE_NAME = "minha-fila";
	private byte[] imageBuffer = null;
	
	public static void main(String[] argv) throws Exception {
		Scanner scan = new Scanner(System.in);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1"); 
		factory.setUsername("guest"); 
		factory.setPassword("guest"); 
		factory.setVirtualHost("/");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		// Pegando o diretório atual
		File file = new File("");
		String directoryName = file.getAbsoluteFile().toString();
		
		// Lendo as imagens que serão usadas
		DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryName + "\\imagens\\"));
		
		// Criando um ByteOutputStream e convertendo para Byte 
		for (Path files: stream) {
			BufferedImage bImage = ImageIO.read(files.toFile());
			SerializableImage serializableImage = new SerializableImage(bImage, files.getFileName().toString().getBytes());
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream ous = new ObjectOutputStream(baos);
			ous.writeObject(serializableImage);
			
			byte [] body = baos.toByteArray();
			
			channel.basicPublish("", QUEUE_NAME, null, body);
			System.out.println(" [x] Imagem '" + files.getFileName() + " enviada'");
	    }
		
		channel.close();
		connection.close();
	}
}
