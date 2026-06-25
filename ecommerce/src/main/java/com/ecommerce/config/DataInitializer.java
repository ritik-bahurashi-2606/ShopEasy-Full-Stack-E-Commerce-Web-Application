package com.ecommerce.config;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            List<Product> products = Arrays.asList(
                createProduct("Apple iPhone 15", "Latest iPhone with A16 Bionic chip, 48MP camera system, Dynamic Island, and all-day battery life.", 79999.0, "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400", "Electronics", "Apple", 50, 4.8),
                createProduct("Samsung Galaxy S24", "Flagship Android phone with Snapdragon 8 Gen 3, 200MP camera, and stunning 6.8-inch AMOLED display.", 74999.0, "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400", "Electronics", "Samsung", 45, 4.7),
                createProduct("Sony WH-1000XM5", "Industry-leading noise cancelling wireless headphones with up to 30-hour battery life and crystal clear hands-free calling.", 24999.0, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400", "Electronics", "Sony", 100, 4.9),
                createProduct("MacBook Air M2", "Supercharged by M2 chip, impossibly thin design, with a brilliant Liquid Retina display and all-day battery life.", 114999.0, "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400", "Electronics", "Apple", 30, 4.9),
                createProduct("Nike Air Max 270", "The Nike Air Max 270 features Nike's biggest heel Air unit yet for a super-soft ride that feels as plush as it looks.", 8999.0, "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", "Footwear", "Nike", 200, 4.6),
                createProduct("Levi's 501 Original Jeans", "The original straight leg jeans. A timeless classic made with 100% cotton for a comfortable, durable fit.", 3499.0, "https://images.unsplash.com/photo-1542272454315-4c01d7abdf4a?w=400", "Clothing", "Levi's", 300, 4.5),
                createProduct("Instant Pot Duo 7-in-1", "7-in-1 multi-use programmable pressure cooker, slow cooker, rice cooker, steamer, saute, yogurt maker and warmer.", 6999.0, "https://images.unsplash.com/photo-1585515320310-259814833e62?w=400", "Kitchen", "Instant Pot", 150, 4.7),
                createProduct("The Alchemist - Paulo Coelho", "A magical story about following your dreams. One of the best-selling books of all time with millions of copies sold worldwide.", 299.0, "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400", "Books", "HarperCollins", 500, 4.8),
                createProduct("Yoga Mat Premium", "Non-slip, eco-friendly yoga mat with alignment lines. Perfect for all yoga styles and meditation.", 1499.0, "https://images.unsplash.com/photo-1601925228116-cb27fae97e16?w=400", "Sports", "YogaFit", 200, 4.6),
                createProduct("LEGO Star Wars Millennium Falcon", "Build and display the iconic Star Wars Millennium Falcon with over 1,300 pieces. Perfect for ages 9 and up.", 5999.0, "https://images.unsplash.com/photo-1585366119957-e9730b6d0f60?w=400", "Toys", "LEGO", 75, 4.9),
                createProduct("Nescafe Gold Blend Coffee", "Rich and smooth instant coffee. Made from 100% Arabica beans roasted to perfection. 200g jar.", 699.0, "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=400", "Grocery", "Nescafe", 1000, 4.4),
                createProduct("Fitbit Charge 6", "Advanced fitness tracker with built-in GPS, heart rate monitoring, stress management tools, and 7-day battery life.", 12999.0, "https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=400", "Electronics", "Fitbit", 80, 4.5)
            );
            productRepository.saveAll(products);
            System.out.println("✅ Sample products loaded successfully!");
        }
    }

    private Product createProduct(String name, String desc, Double price, String img, String category, String brand, int stock, double rating) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setImageUrl(img);
        p.setCategory(category);
        p.setBrand(brand);
        p.setStock(stock);
        p.setRating(rating);
        return p;
    }
}