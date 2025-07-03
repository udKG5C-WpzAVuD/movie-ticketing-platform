package com.example.movieticketingplatform.model.domain;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

@Component
public class PdfReportGenerator {
    public byte[] generatePdfByteArray(String reportType, LocalDate startDate, LocalDate endDate,
                                       ReportData reportData, JSONObject chartData, String chartImage)
            throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // 使用iTextAsian提供的中文字体
            BaseFont baseFont = BaseFont.createFont(
                    "STSong-Light",
                    "UniGB-UCS2-H",
                    BaseFont.EMBEDDED
            );

            // 使用这个中文字体创建所有文本元素
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font sectionFont = new Font(baseFont, 14, Font.BOLD);
            Font normalFont = new Font(baseFont, 12, Font.NORMAL);

            // 使用中文字体创建标题
            Paragraph title = new Paragraph(String.format("%s - 运营报告", startDate), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // 使用中文字体创建内容
            document.add(new Paragraph("报告类型: " + reportType, normalFont));
            document.add(new Paragraph("日期范围: " + startDate + " 至 " + endDate, normalFont));
            document.add(Chunk.NEWLINE);

            // 添加核心数据
            document.add(new Paragraph("核心数据:", sectionFont));
            document.add(new Paragraph("新注册用户: " + reportData.getNewUserCount(), normalFont));
            document.add(new Paragraph("订单总量: " + reportData.getOrderCount(), normalFont));
            document.add(new Paragraph("总票房: " + reportData.getTotalRevenue()*100, normalFont));
            document.add(new Paragraph("上架电影总数: " + reportData.getMovieCount(), normalFont));

            document.add(Chunk.NEWLINE);

            // 如果有图表图片，添加到PDF
            if (chartImage != null && !chartImage.isEmpty()) {
                document.add(new Paragraph("数据图表:", sectionFont));

                // 解码Base64图片数据
                String imageData = chartImage.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(imageData);

                // 创建图片对象
                Image image = Image.getInstance(imageBytes);

                // 设置图片大小（例如宽度为页面宽度的80%，高度按比例缩放）
                float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
                float width = documentWidth * 0.8f;
                float height = image.getHeight() * (width / image.getWidth());

                image.scaleToFit(width, height);
                image.setAlignment(Image.ALIGN_CENTER);

                document.add(image);
            }

        } catch (DocumentException | IOException e) {
            throw new IOException("Failed to generate PDF", e);
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }
}