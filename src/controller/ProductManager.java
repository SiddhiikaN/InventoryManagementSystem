//Business logic / event handling

package controller;

import dao.ProductDAO;
import model.Product;
import java.util.List;

public class ProductManager {
    private ProductDAO dao = new ProductDAO();

    // Add new product
    public boolean addProduct(Product p) {
        return dao.addProduct(p);
    }

    // Update existing product
    public boolean updateProduct(Product p) {
        return dao.updateProduct(p);
    }

    // Delete product by id
    public boolean deleteProduct(int id) {
        return dao.deleteProduct(id);
    }

    // Get all products list
    public List<Product> getAllProducts() {
        return dao.getAllProducts();
    }
}

