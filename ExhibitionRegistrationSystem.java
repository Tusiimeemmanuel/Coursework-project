
package denisbategela.exhibitionregistrationsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;

public class ExhibitionRegistrationSystem extends JFrame {
    private JTextField txtRegID, txtName, txtDepartment, txtDancingPartner, txtContact, txtEmail;
    private JLabel lblImage;
    private JButton btnRegister, btnSearch, btnUpdate, btnDelete, btnClear, btnExit, btnUpload;
    private String imagePath = "";
    
    private Connection connection;
    
    public ExhibitionRegistrationSystem() {
        initializeDatabase();
        initializeGUI();
    }
    
    private void initializeDatabase() {
    try {
        DatabaseSetup.createDatabaseAndTable();
        
        // Absolute path to the database file
        String dbPath = "C:/Users/TUSIIME/OneDrive/Documents/NetBeansProjects/mavenproject1/VUE_Exhibition.accdb";
        String url = "jdbc:ucanaccess://" + dbPath;

        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        connection = DriverManager.getConnection(url);

        System.out.println("Database connected successfully!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    private void initializeGUI() {
        setTitle("SALSA Dance Festival Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        
        formPanel.add(new JLabel("Registration ID:"));
        txtRegID = new JTextField();
        formPanel.add(txtRegID);
        
        formPanel.add(new JLabel("Participant Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        
        formPanel.add(new JLabel("University Department:"));
        txtDepartment = new JTextField();
        formPanel.add(txtDepartment);
        
        formPanel.add(new JLabel("Dancing Partner:"));
        txtDancingPartner = new JTextField();
        formPanel.add(txtDancingPartner);
        
        formPanel.add(new JLabel("Contact Number:"));
        txtContact = new JTextField();
        formPanel.add(txtContact);
        
        formPanel.add(new JLabel("Email Address:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);
        
        formPanel.add(new JLabel("ID Image:"));
        JPanel imagePanel = new JPanel(new FlowLayout());
        btnUpload = new JButton("Upload Image");
        lblImage = new JLabel("No image selected");
        imagePanel.add(btnUpload);
        imagePanel.add(lblImage);
        formPanel.add(imagePanel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnRegister = new JButton("Register");
        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnExit = new JButton("Exit");
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExit);
        
        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        addEventListeners();
        
        pack();
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void addEventListeners() {
        btnUpload.addActionListener(e -> uploadImage());
        btnRegister.addActionListener(e -> registerParticipant());
        btnSearch.addActionListener(e -> searchParticipant());
        btnUpdate.addActionListener(e -> updateParticipant());
        btnDelete.addActionListener(e -> deleteParticipant());
        btnClear.addActionListener(e -> clearForm());
        btnExit.addActionListener(e -> System.exit(0));
    }
    
    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePath = selectedFile.getAbsolutePath();
            lblImage.setText(selectedFile.getName());
            
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(image));
        }
    }
    
    private boolean validateInput() {
        if (txtRegID.getText().trim().isEmpty() ||
            txtName.getText().trim().isEmpty() ||
            txtDepartment.getText().trim().isEmpty() ||
            txtContact.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields except Dancing Partner are required!");
            return false;
        }
        
        String email = txtEmail.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!");
            return false;
        }
        
        return true;
    }
    
    private void registerParticipant() {
        if (!validateInput()) return;
        
        try {
            String sql = "INSERT INTO Participants VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, txtRegID.getText().trim());
            pstmt.setString(2, txtName.getText().trim());
            pstmt.setString(3, txtDepartment.getText().trim());
            pstmt.setString(4, txtDancingPartner.getText().trim());
            pstmt.setString(5, txtContact.getText().trim());
            pstmt.setString(6, txtEmail.getText().trim());
            pstmt.setString(7, imagePath);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Participant registered successfully!");
            clearForm();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage());
        }
    }
    
    private void searchParticipant() {
        String regID = JOptionPane.showInputDialog(this, "Enter Registration ID:");
        if (regID == null || regID.trim().isEmpty()) return;
        
        try {
            String sql = "SELECT * FROM Participants WHERE RegistrationID = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, regID.trim());
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtRegID.setText(rs.getString("RegistrationID"));
                txtName.setText(rs.getString("ParticipantName"));
                txtDepartment.setText(rs.getString("UniversityDepartment"));
                txtDancingPartner.setText(rs.getString("DancingPartner"));
                txtContact.setText(rs.getString("ContactNumber"));
                txtEmail.setText(rs.getString("EmailAddress"));
                imagePath = rs.getString("IDImagePath");
                
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        lblImage.setText(imageFile.getName());
                        ImageIcon imageIcon = new ImageIcon(imagePath);
                        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        lblImage.setIcon(new ImageIcon(image));
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Participant not found!");
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage());
        }
    }
    
    private void updateParticipant() {
        if (!validateInput()) return;
        
        try {
            String sql = "UPDATE Participants SET ParticipantName=?, UniversityDepartment=?, " +
                        "DancingPartner=?, ContactNumber=?, EmailAddress=?, IDImagePath=? " +
                        "WHERE RegistrationID=?";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, txtName.getText().trim());
            pstmt.setString(2, txtDepartment.getText().trim());
            pstmt.setString(3, txtDancingPartner.getText().trim());
            pstmt.setString(4, txtContact.getText().trim());
            pstmt.setString(5, txtEmail.getText().trim());
            pstmt.setString(6, imagePath);
            pstmt.setString(7, txtRegID.getText().trim());
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Participant updated successfully!");
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
        }
    }
    
    private void deleteParticipant() {
        String regID = txtRegID.getText().trim();
        if (regID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Registration ID!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this participant?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM Participants WHERE RegistrationID = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, regID);
                
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Participant deleted successfully!");
                clearForm();
                pstmt.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage());
            }
        }
    }
    
    private void clearForm() {
        txtRegID.setText("");
        txtName.setText("");
        txtDepartment.setText("");
        txtDancingPartner.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        lblImage.setText("No image selected");
        lblImage.setIcon(null);
        imagePath = "";
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExhibitionRegistrationSystem());
    }
}

class DatabaseSetup {
    public static void createDatabaseAndTable() {
        Connection conn = null;
        try {
            // First check if database file exists
            File dbFile = new File("C:/Users/TUSIIME/OneDrive/Documents/NetBeansProjects/mavenproject1/VUE_Exhibition.accdb");
            
            if (!dbFile.exists()) {
                System.out.println("Creating new database file...");
                // Create new database
                String dbURL = "jdbc:ucanaccess://C:/Users/TUSIIME/OneDrive/Documents/NetBeansProjects/mavenproject1/VUE_Exhibition.accdb;newdatabaseversion=V2007";
                conn = DriverManager.getConnection(dbURL);
                System.out.println("New database created successfully!");
            } else {
                System.out.println("Database file already exists, connecting...");
                String dbURL = "jdbc:ucanaccess://C:/Users/TUSIIME/OneDrive/Documents/NetBeansProjects/mavenproject1/VUE_Exhibition.accdb";
                conn = DriverManager.getConnection(dbURL);
            }
            
            // Check if table exists
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Participants", null);
            
            if (!tables.next()) {
                // Table doesn't exist, create it
                System.out.println("Creating Participants table...");
                String createTableSQL = "CREATE TABLE Participants (" +
                    "RegistrationID VARCHAR(10) PRIMARY KEY, " +
                    "ParticipantName VARCHAR(100) NOT NULL, " +
                    "UniversityDepartment VARCHAR(100) NOT NULL, " +
                    "DancingPartner VARCHAR(100), " +
                    "ContactNumber VARCHAR(15), " +
                    "EmailAddress VARCHAR(100), " +
                    "IDImagePath VARCHAR(255))";
                
                Statement stmt = conn.createStatement();
                stmt.execute(createTableSQL);
                
                // Insert sample data
                String[] sampleData = {
                    "INSERT INTO Participants VALUES ('REG001', 'John Smith', 'Computer Science', 'Sarah Johnson', '0770123456', 'john.smith@vu.ac.ug', '')",
                    "INSERT INTO Participants VALUES ('REG002', 'Mary Johnson', 'Business Administration', 'David Brown', '0770123457', 'mary.johnson@vu.ac.ug', '')",
                    "INSERT INTO Participants VALUES ('REG003', 'Peter Wilson', 'Engineering', 'Lisa Davis', '0770123458', 'peter.wilson@vu.ac.ug', '')",
                    "INSERT INTO Participants VALUES ('REG004', 'Sarah Brown', 'Medicine', 'Michael Wilson', '0770123459', 'sarah.brown@vu.ac.ug', '')",
                    "INSERT INTO Participants VALUES ('REG005', 'David Lee', 'Law', 'Emma Thompson', '0770123460', 'david.lee@vu.ac.ug', '')"
                };
                
                for (String sql : sampleData) {
                    stmt.executeUpdate(sql);
                }
                
                System.out.println("Table created successfully with sample data!");
                stmt.close();
            } else {
                System.out.println("Participants table already exists.");
            }
            
            tables.close();
            
        } catch (Exception e) {
            System.out.println("Error during database setup: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}