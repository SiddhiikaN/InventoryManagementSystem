package view;

import controller.ProductManager;
import model.Product;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;

public class ProductForm extends JFrame {

    private JTextField nameField;
    private JFormattedTextField quantityField;
    private JFormattedTextField priceField;

    private JTable productTable;
    private DefaultTableModel tableModel;

    private JLabel statusLabel;

    private ProductManager controller;

    public ProductForm() {
        controller = new ProductManager();

        setTitle("Inventory Management System");
        setSize(700, 500);
        setLocationRelativeTo(null); // Center window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        // Main container with vertical BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        // Input panel with vertical stacking of input rows
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        mainPanel.add(inputPanel);

        // Helper to create input rows with label and field side-by-side
        inputPanel.add(createInputRow("Name:", nameField = new JTextField(20), font));
        
        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        NumberFormatter intFormatter = new NumberFormatter(intFormat);
        intFormatter.setValueClass(Integer.class);
        intFormatter.setAllowsInvalid(false);
        intFormatter.setMinimum(0);
        quantityField = new JFormattedTextField(intFormatter);
        quantityField.setColumns(10);
        quantityField.setFont(font);
        inputPanel.add(createInputRow("Quantity:", quantityField, font));
        
        NumberFormat doubleFormat = NumberFormat.getNumberInstance();
        NumberFormatter doubleFormatter = new NumberFormatter(doubleFormat);
        doubleFormatter.setValueClass(Double.class);
        doubleFormatter.setAllowsInvalid(false);
        doubleFormatter.setMinimum(0.0);
        priceField = new JFormattedTextField(doubleFormatter);
        priceField.setColumns(10);
        priceField.setFont(font);
        inputPanel.add(createInputRow("Price:", priceField, font));

        // Buttons panel with FlowLayout centered
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        addButton.setFont(font);
        updateButton.setFont(font);
        deleteButton.setFont(font);
        refreshButton.setFont(font);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Quantity", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disable cell editing
            }
        };
        productTable = new JTable(tableModel);
        productTable.setFont(font);
        productTable.setRowHeight(25);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Product List"));
        scrollPane.setPreferredSize(new Dimension(650, 250));
        mainPanel.add(scrollPane);

        // Status bar
        statusLabel = new JLabel(" Ready");
        statusLabel.setFont(font);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        mainPanel.add(statusLabel);

        // Load data initially
        loadTable();

        // Button listeners
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        refreshButton.addActionListener(e -> loadTable());

        // Table click fills inputs
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    quantityField.setValue(Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString()));
                    priceField.setValue(Double.parseDouble(tableModel.getValueAt(selectedRow, 3).toString()));
                }
            }
        });
    }

    // Helper method to create a horizontal input row panel (label + field)
    private JPanel createInputRow(String labelText, JComponent field, Font font) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(70, 25));
        label.setFont(font);
        panel.add(label);
        field.setFont(font);
        panel.add(field);
        return panel;
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Product> products = controller.getAllProducts();

        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getQuantity(),
                    p.getPrice()
            });
        }
        statusLabel.setText(" Loaded " + products.size() + " products.");
        clearFields();
    }

    private void addProduct() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                statusLabel.setText(" Name cannot be empty.");
                return;
            }

            int quantity = ((Number) quantityField.getValue()).intValue();
            double price = ((Number) priceField.getValue()).doubleValue();

            Product product = new Product(0, name, quantity, price);
            boolean success = controller.addProduct(product);

            if (success) {
                statusLabel.setText(" Product added successfully.");
                loadTable();
            } else {
                statusLabel.setText(" Failed to add product.");
            }
        } catch (Exception ex) {
            statusLabel.setText(" Invalid input. Please check the fields.");
        }
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            statusLabel.setText(" Please select a product to update.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                statusLabel.setText(" Name cannot be empty.");
                return;
            }

            int quantity = ((Number) quantityField.getValue()).intValue();
            double price = ((Number) priceField.getValue()).doubleValue();

            Product product = new Product(id, name, quantity, price);
            boolean success = controller.updateProduct(product);

            if (success) {
                statusLabel.setText(" Product updated successfully.");
                loadTable();
            } else {
                statusLabel.setText(" Failed to update product.");
            }
        } catch (Exception ex) {
            statusLabel.setText(" Invalid input. Please check the fields.");
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            statusLabel.setText(" Please select a product to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this product?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            boolean success = controller.deleteProduct(id);

            if (success) {
                statusLabel.setText(" Product deleted successfully.");
                loadTable();
            } else {
                statusLabel.setText(" Failed to delete product.");
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        quantityField.setValue(null);
        priceField.setValue(null);
        productTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductForm form = new ProductForm();
            form.setVisible(true);
        });
    }
}
