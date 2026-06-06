package com.wifi.management.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wifi.management.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFGeneratorService {

    public static String generatePaymentReceipt(User user, String transactionId, String planName, double amount, String method) {
        // ইউজারের Downloads ফোল্ডারে সেভ করার পাথ তৈরি
        String userHome = System.getProperty("user.home");
        String fileName = "WiFi_Payment_Receipt_" + transactionId + ".pdf";
        String pdfOutputPath = userHome + File.separator + "Downloads" + File.separator + fileName;

        String currentDate = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(new Date());
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfOutputPath));
            document.open();

            // ফন্ট এবং কালার প্যালেট সেটআপ
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, new BaseColor(52, 152, 219));
            Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(46, 204, 113));
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.GRAY);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, new BaseColor(44, 62, 80));
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);

            // ১. হেডার ব্র্যান্ডিং অংশ
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);

            PdfPCell brandCell = new PdfPCell(new Phrase("🌐 WIFI MANAGER", titleFont));
            brandCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(brandCell);

            PdfPCell receiptCell = new PdfPCell(new Phrase("Payment Receipt", subTitleFont));
            receiptCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            receiptCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(receiptCell);

            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // ২. কাস্টমার এবং ট্রানজেকশন মেটা ডাটা
            PdfPTable metaTable = new PdfPTable(4);
            metaTable.setWidthPercentage(100);
            metaTable.setWidths(new float[]{1.3f, 2f, 1.5f, 1.5f});

            metaTable.addCell(createCell("Statement Date:", labelFont, Rectangle.NO_BORDER, Element.ALIGN_LEFT));
            metaTable.addCell(createCell(currentDate, valueFont, Rectangle.NO_BORDER, Element.ALIGN_LEFT));
            metaTable.addCell(createCell("Customer ID:", labelFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT));
            metaTable.addCell(createCell("#" + user.getUserId(), valueFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT));

            metaTable.addCell(createCell("Customer Name:", labelFont, Rectangle.NO_BORDER, Element.ALIGN_LEFT));
            metaTable.addCell(createCell(user.getFullName(), valueFont, Rectangle.NO_BORDER, Element.ALIGN_LEFT));
            metaTable.addCell(createCell("Transaction ID:", labelFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT));
            metaTable.addCell(createCell(transactionId, valueFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT));

            metaTable.addCell(createCell("Phone Number:", labelFont, Rectangle.NO_BORDER, Element.ALIGN_LEFT));
            metaTable.addCell(createCell(user.getPhone() != null ? user.getPhone() : "N/A", valueFont, Rectangle.NO_BORDER, Element.ALIGN_LEFT));
            metaTable.addCell(createCell("Payment Method:", labelFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT));
            metaTable.addCell(createCell(method, valueFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT));

            document.add(metaTable);
            document.add(new Paragraph("\n\n"));

            // ৩. মেইন বিলিং আইটেম টেবিল
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100);
            detailsTable.setWidths(new float[]{4f, 1.5f});

            PdfPCell th1 = createCell("Description", headerFont, Rectangle.BOX, Element.ALIGN_LEFT);
            th1.setBackgroundColor(new BaseColor(44, 62, 80));
            th1.setPadding(8);
            detailsTable.addCell(th1);

            PdfPCell th2 = createCell("Amount", headerFont, Rectangle.BOX, Element.ALIGN_RIGHT);
            th2.setBackgroundColor(new BaseColor(44, 62, 80));
            th2.setPadding(8);
            detailsTable.addCell(th2);

            PdfPCell td1 = createCell("Internet Subscription Package Plan: " + planName + " (Valid for 30 Days)", valueFont, Rectangle.BOTTOM, Element.ALIGN_LEFT);
            td1.setPadding(12);
            detailsTable.addCell(td1);

            PdfPCell td2 = createCell(amount + " BDT", valueFont, Rectangle.BOTTOM, Element.ALIGN_RIGHT);
            td2.setPadding(12);
            detailsTable.addCell(td2);

            PdfPCell totalLabel = createCell("Net Paid Amount:", labelFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT);
            totalLabel.setPadding(12);
            detailsTable.addCell(totalLabel);

            Font amountFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(46, 204, 113));
            PdfPCell totalVal = createCell(amount + " BDT", amountFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT);
            totalVal.setPadding(12);
            detailsTable.addCell(totalVal);

            document.add(detailsTable);

            // ৪. প্রফেশনাল ফুটার নোট
            Paragraph footer = new Paragraph("\n\n\n\nThank you for choosing WiFi Manager.\nThis is an electronically generated document, no signature required.", labelFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return pdfOutputPath;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PdfPCell createCell(String text, Font font, int border, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}