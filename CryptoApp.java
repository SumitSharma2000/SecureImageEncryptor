import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;



public class CryptoApp extends JFrame {
    private SecretKey secretKey;
    private Cipher cipher;
    private JLabel keyIcon;

    public CryptoApp() {
        setTitle("Crypto - Message and Image Encryption and Decryption Software");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Set custom UI look and feel
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);

        // Gradient background panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(0, 153, 255);
                Color color2 = new Color(0, 51, 153);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(null);
        setContentPane(mainPanel);

        // Title label
        JLabel titleLabel = new JLabel("Message and Image Encryption and Decryption Software", JLabel.CENTER);
        titleLabel.setBounds(50, 30, 500, 40);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        // Icon in the center (Key Icon)
        keyIcon = new JLabel();
        try {
            // Load the image from the file
            Image img = ImageIO.read(new File("key_image.jpg"));
            // Resize the image to fit within the bounds of the JLabel
            Image resizedImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);  // Adjust size as needed (width, height)
            // Set the resized image as an icon
            keyIcon.setIcon(new ImageIcon(resizedImg));
        } catch (IOException e) {
            keyIcon.setText("Image not found!");
        }

        // Set the bounds of the JLabel (adjust to fit the resized image)
        keyIcon.setBounds(225, 90, 150, 150);  // Make sure bounds are at least the size of the image
        // Add the JLabel to the container
        add(keyIcon);

        // Text Encryption & Decryption button
        JButton textButton = new JButton("Text Encryption & Decryption");
        textButton.setBounds(100, 290, 180, 40); // y-coordinate changed to 290
        styleButton(textButton);
        add(textButton);

        // Image Encryption & Decryption button
        JButton imageButton = new JButton("Image Encryption & Decryption");
        imageButton.setBounds(320, 290, 180, 40); // y-coordinate changed to 290
        styleButton(imageButton);
        add(imageButton);

        // Action listeners
        textButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTextEncryptionDialog();
            }
        });
        imageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showImageEncryptionDialog();
            }
        });

        try {
            secretKey = generateKey();
            cipher = Cipher.getInstance("AES");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Styling the buttons with borders, background, and hover effect
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setOpaque(true);
        // Button hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 76, 153));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
        });
    }

    // Generate a random key for encryption
    private SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    // Encrypt text
    private String encryptText(String plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt text
    private String decryptText(String encryptedText) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    // Show dialog for text encryption/decryption
    private void showTextEncryptionDialog() {
        JTextField inputField = new JTextField(20);
        JTextArea outputArea = new JTextArea(5, 20);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Enter Text:"));
        panel.add(inputField);
        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(new JLabel("Result:"));
        panel.add(new JScrollPane(outputArea));
        JFrame textFrame = new JFrame("Text Encryption & Decryption");
        textFrame.setSize(400, 200);
        textFrame.setLocationRelativeTo(null);
        textFrame.add(panel);
        textFrame.setVisible(true);
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String encryptedText = encryptText(inputField.getText());
                    outputArea.setText(encryptedText);
                } catch (Exception ex) {
                    outputArea.setText("Encryption Error!");
                }
            }
        });
        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String decryptedText = decryptText(inputField.getText());
                    outputArea.setText(decryptedText);
                } catch (Exception ex) {
                    outputArea.setText("Decryption Error!");
                }
            }
        });
    }

    // Show dialog for image encryption/decryption
    private void showImageEncryptionDialog() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Read image file into bytes
                BufferedImage img = ImageIO.read(selectedFile);
                byte[] imageBytes = imageToByteArray(img);

                // Encrypt image bytes
                byte[] encryptedBytes = encryptImage(imageBytes);
                // Save encrypted image
                saveEncryptedImage(selectedFile.getName() + "_encrypted.png", encryptedBytes);

                JOptionPane.showMessageDialog(this, "Image encrypted and saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error encrypting image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Encrypt image bytes
    private byte[] encryptImage(byte[] imageBytes) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(imageBytes);
    }

    // Save the encrypted image
    private void saveEncryptedImage(String fileName, byte[] imageBytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(imageBytes);
        }
    }

    // Convert BufferedImage to byte array
    private byte[] imageToByteArray(BufferedImage image) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("BufferedImage cannot be null");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Write the image to the ByteArrayOutputStream in PNG format
            ImageIO.write(image, "png", baos);
            baos.flush(); // Ensure all data is written to the output stream
            return baos.toByteArray(); // Return the byte array
        }
    }


    // Placeholder for image decryption
    private void decryptImageDialog() {
        // Implement image decryption here if needed
        JOptionPane.showMessageDialog(this, "Image decryption not implemented yet!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method to start the app
    public static void main(String[] args) {
        CryptoApp app = new CryptoApp();
        app.setVisible(true);
    }
}
