package edu.wildlifesecurity.backend;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import com.sixlegs.png.PngImage;

import edu.wildlifesecurity.framework.IImageDecoder;

public class PngDecoder implements IImageDecoder {

	@Override
	public Mat decode(byte[] array) {
		try {
			PngImage decoder = new PngImage();
			BufferedImage img;
			img = decoder.read(new ByteArrayInputStream(array), true);
			Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
			mat.put(0, 0, ((DataBufferByte)img.getRaster().getDataBuffer()).getData());
			
			return mat;
		} catch (IOException e) {
			System.out.println("Error in PngDecoder: Could not parse png image. " + e.getMessage());
			return null;
		}
	}

}
