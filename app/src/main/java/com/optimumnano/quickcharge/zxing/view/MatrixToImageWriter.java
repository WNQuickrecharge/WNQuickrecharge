package com.optimumnano.quickcharge.zxing.view;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

public class MatrixToImageWriter {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private MatrixToImageWriter() {
	}

	public static Bitmap toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		Bitmap image = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setPixel(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static Bitmap createBitmap(String conString) {

		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = multiFormatWriter.encode(conString, BarcodeFormat.QR_CODE, 360, 360);
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
		return toBufferedImage(bitMatrix);
	}
}
