package com.wifi.management.gui;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        try {
            // 🌟 Built-in modern look (NO external library)
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // 🎨 Global UI improvements (modern feel)
            UIManager.put("control", new Color(245, 245, 245));
            UIManager.put("info", new Color(242, 242, 242));

            UIManager.put("Button.background", new Color(52, 152, 219));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));

            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));

            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);

            // Smooth scaling (better UI clarity)
            System.setProperty("sun.java2d.uiScale", "1.1");

        } catch (Exception e) {
            System.out.println("Failed to set Nimbus Look and Feel");
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}