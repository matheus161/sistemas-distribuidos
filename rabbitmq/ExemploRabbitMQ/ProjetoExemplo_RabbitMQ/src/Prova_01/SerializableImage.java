package Prova_01;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class SerializableImage implements Serializable {
	private static final long serialVersionUID = 1L;
	private byte[] image;
	private byte[] imageName;
	
	public SerializableImage(BufferedImage buferredImage, byte[] name) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(buferredImage, "jpg", byteArrayOutputStream);
		byteArrayOutputStream.flush();
		
		this.image = byteArrayOutputStream.toByteArray();
		this.imageName = name;
	}
	
	public byte[] getImage() {
		return this.image;
	}
	
	public byte[] getImageName() {
		return this.imageName;
	}
}
