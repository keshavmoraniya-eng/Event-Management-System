package com.ems.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class QRCodeGenerator {

    //Generate QR code as BufferedImage

    public BufferedImage generateQRCodeImage(String data,int width,int height) throws WriterException{
        QRCodeWriter qrCodeWriter=new QRCodeWriter();

        Map<EncodeHintType,Object> hints=new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        hints.put(EncodeHintType.MARGIN,1);

        BitMatrix bitMatrix=qrCodeWriter.encode(data, BarcodeFormat.QR_CODE,width,height,hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    //Generate QR code as BASE64 string
    public String generateQRCodeBase64(String data,int width,int height) throws WriterException, IOException {
        BufferedImage qrImage=generateQRCodeImage(data, width, height);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(qrImage,"PNG",baos);
        byte[] imageBytes=baos.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    //Generate QR code for booking reference
    public String generateBookingReference(String bookingReference){
        try {
            return generateQRCodeBase64(bookingReference,300,300);
        }catch (Exception e){
            return "QR-"+bookingReference;
        }
    }



}
