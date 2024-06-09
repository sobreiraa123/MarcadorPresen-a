package com.example.presenca.controller;

import com.example.presenca.model.Presenca;
import com.example.presenca.service.PresencaService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PresencaController {

    @Autowired
    private PresencaService presencaService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/marcar-presenca-estudante")
    public String marcarPresencaEstudante() {
        return "marcar-presenca-estudante";
    }

    @PostMapping("/salvar-presenca-estudante")
    public String salvarPresencaEstudante(@RequestParam("numeroAluno") String numeroAluno, @RequestParam("nome") String nome, @RequestParam("disciplina") String disciplina, @RequestParam("docente") String docente, @RequestParam("dataHora") String dataHora, @RequestParam("curso") String curso, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dataHoraConvertida = LocalDateTime.parse(dataHora, formatter);
        Presenca presenca = new Presenca(numeroAluno, nome, disciplina, docente, dataHoraConvertida, curso);
        presencaService.savePresenca(presenca);
        
        
        try {
            String qrCodeImage = generateQRCode(presenca.toString());
            model.addAttribute("qrCodeImage", qrCodeImage);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            model.addAttribute("qrCodeImage", "Erro ao gerar QR Code");
        }

        return "presenca-sucesso";
    }

    @GetMapping("/marcar-presenca-docente")
    public String marcarPresencaDocente() {
        return "marcar-presenca-docente";
    }

    @PostMapping("/salvar-presenca-docente")
    public String salvarPresencaDocente(@RequestParam("nome") String nome, @RequestParam("disciplina") String disciplina, @RequestParam("dataHora") String dataHora, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dataHoraConvertida = LocalDateTime.parse(dataHora, formatter);
        Presenca presenca = new Presenca(null, nome, disciplina, nome, dataHoraConvertida, null);
        presencaService.savePresenca(presenca);

        
        try {
            String qrCodeImage = generateQRCode(presenca.toString());
            model.addAttribute("qrCodeImage", qrCodeImage);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            model.addAttribute("qrCodeImage", "Erro ao gerar QR Code");
        }

        return "presenca-sucesso";
    }

    @GetMapping("/ler-qr")
    public String lerQr() {
        return "ler-qr";
    }

    @PostMapping("/processar-qr")
    public String processarQr(@RequestParam("file") MultipartFile file, Model model) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            String qrCodeData = result.getText();

            
            String[] data = qrCodeData.split(",");
            if (data.length == 6) {
                Presenca presenca = new Presenca(data[0], data[1], data[2], data[3], LocalDateTime.parse(data[4]), data[5]);
                presencaService.savePresenca(presenca);
                model.addAttribute("qrCodeData", qrCodeData);
            } else {
                model.addAttribute("qrCodeData", "Formato de dados QR inválido");
            }
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
            model.addAttribute("qrCodeData", "Erro ao ler QR Code");
        }

        return "result";
    }

    private String generateQRCode(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(image, "PNG", pngOutputStream);
        String base64Image = Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());

        return base64Image;
    }
}
