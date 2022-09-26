package com.example.midterm.controller;

import com.example.midterm.pojo.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public boolean serviceAddProduct(@RequestBody Product p){
        try{
            rabbitTemplate.convertSendAndReceive("ProductExchange", "add", p);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public boolean serviceUpdateProduct(@RequestBody Product p){
        try{
            rabbitTemplate.convertSendAndReceive("ProductExchange", "update", p);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<Product> serviceGetAllProduct(){
        try {
            Object all = rabbitTemplate.convertSendAndReceive("ProductExchange", "getall", "");
            return (List<Product>) all;
        }
        catch (Exception e){
            return null;
        }
    }
    @RequestMapping(value = "deleteProduct", method = RequestMethod.POST)
    public boolean serviceDeleteProduct(@RequestBody String name){
        try {
            Object getProduct = rabbitTemplate.convertSendAndReceive("ProductExchange", "getname", name);
            rabbitTemplate.convertSendAndReceive("ProductExchange", "delete", getProduct);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
