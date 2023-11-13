package app.equalityboot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageUtil {

    public static BufferedImage compressImage(byte[] imageData, double compressionQuality) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(imageData);
        BufferedImage originalImage = ImageIO.read(inputStream);

        ByteArrayOutputStream compressedImageData = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", compressedImageData);

        return originalImage;
    }

    public static byte[] imageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String getImgData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
}
