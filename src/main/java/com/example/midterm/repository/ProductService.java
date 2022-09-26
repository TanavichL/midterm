package com.example.midterm.repository;

import com.example.midterm.pojo.Product;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @RabbitListener(queues = "AddProductQueue")
    public boolean addProduct(Product p){
        try {
            productRepository.save(p);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    @RabbitListener(queues = "UpdateProductQueue")
    public boolean updateProduct(Product p){
        try{
            productRepository.save(p);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
    @RabbitListener(queues = "DeleteProductQueue")
    public boolean deleteProduct(Product p){
        try{
            productRepository.delete(p);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    @RabbitListener(queues = "GetAllProductQueue")
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    @RabbitListener(queues = "GetNameProductQueue")
    public Product getProductByName(String name){
        return productRepository.findByName(name);
    }
}
