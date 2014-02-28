package edu.ucsb.cs56.projects.tutorials.image_zoom;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * Created by samsung on 14-2-26.
 */
public class JFileChooserPreview {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) { }

        final JFrame frame = new JFrame();
        JButton button = new JButton("Open File Chooser");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                PreviewPane previewPane = new PreviewPane();
                chooser.setAccessory(previewPane);
                chooser.addPropertyChangeListener(previewPane);
                chooser.showDialog(frame, "OK");
            }
        });
        frame.getContentPane().add(button);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    static class PreviewPane extends JPanel implements PropertyChangeListener {
        private JLabel label;
        private int maxImgWidth;
        public PreviewPane() {
            setLayout(new BorderLayout(5,5));
            setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            add(new JLabel("Preview:"), BorderLayout.NORTH);
            label = new JLabel();
            label.setBackground(Color.WHITE);
            label.setOpaque(true);
            label.setPreferredSize(new Dimension(200, 200));
            maxImgWidth = 195;
            label.setBorder(BorderFactory.createEtchedBorder());
            add(label, BorderLayout.CENTER);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            Icon icon = null;
            if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt
                    .getPropertyName())) {
                File newFile = (File) evt.getNewValue();
                if(newFile != null) {
                    String path = newFile.getAbsolutePath();
                    if(path.endsWith(".gif") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".bmp")) {
                        try {
                            BufferedImage img = ImageIO.read(newFile);
                            float width = img.getWidth();
                            float height = img.getHeight();
                            float scale = height / width;
                            width = maxImgWidth;
                            height = (width * scale); // height should be scaled from new width
                            icon = new ImageIcon(img.getScaledInstance(Math.max(1, (int)width), Math.max(1, (int)height), Image.SCALE_SMOOTH));
                        }
                        catch(IOException e) {
                            // couldn't read image.
                        }
                    }
                }

                label.setIcon(icon);
                this.repaint();

            }
        }

    }

}
