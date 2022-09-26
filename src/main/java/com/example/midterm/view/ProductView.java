package com.example.midterm.view;

import com.example.midterm.pojo.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Route(value = "/view")
public class ProductView extends VerticalLayout {
    private ComboBox proList;
    private TextField proName;
    private NumberField proCost, proPro, proPrice;
    private Button addPro, upPro, delPro, clearPro;
    private List<Product> products = new ArrayList<>();
    private Product product = new Product();

    public ProductView(){
        proList = new ComboBox<>();
        proList.setWidth("600px");
        proList.setLabel("Product List");
        proName = new TextField("Product Name:");
        proName.setValue("");
        proName.setWidth("600px");
        proCost = new NumberField("Product Cost:");
        proPro = new NumberField("Product Profit:");
        proPrice = new NumberField("Product Price");
        proCost.setWidth("600px");
        proPro.setWidth("600px");
        proPrice.setWidth("600px");
        proCost.setValue(0.0);
        proPro.setValue(0.0);
        proPrice.setValue(0.0);
        proPrice.setEnabled(false);
        addPro = new Button("Add Product");
        upPro = new Button("Update Product");
        delPro = new Button("Delete Product");
        clearPro = new Button("Clear Product");
        HorizontalLayout bar = new HorizontalLayout();
        bar.add(addPro, upPro, delPro, clearPro);
        add(proList, proName, proCost, proPro, proPrice, bar);

        proList.addFocusListener(event ->{
            List<Product> out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getAll")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Product>>() {
                    })
                    .block();
            this.products = out;
            ArrayList <String> name = new ArrayList<>();
            for (Product p: this.products) {
                name.add(p.getProductName());
            }
            proList.setItems(name);
        });
        proList.addValueChangeListener(event ->{
            if(event.getValue() != null){
                for (int i = 0; i < this.products.size(); i++) {
                    if(proList.getValue().equals(this.products.get(i).getProductName())){
                        this.product = this.products.get(i);
                        proName.setValue(this.product.getProductName());
                        proCost.setValue(this.product.getProductCost());
                        proPro.setValue(this.product.getProductProfit());
                        proPrice.setValue(this.product.getProductPrice());
                    }
                }
            }
        });
        proCost.addValueChangeListener(event ->{
            double price = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPrice/"+proCost.getValue()+"/"+proPro.getValue())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();
            proPrice.setValue(price);
        });
        proPro.addValueChangeListener(event ->{
            double price = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPrice/"+proCost.getValue()+"/"+proPro.getValue())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();
            proPrice.setValue(price);
        });
        addPro.addClickListener(event ->{
            double price = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPrice/"+proCost.getValue()+"/"+proPro.getValue())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();
            proPrice.setValue(price);
            boolean out = WebClient.create()
                    .post().uri("http://localhost:8080/addProduct")
                    .body(Mono.just(new Product(null, proName.getValue(), proCost.getValue(), proPro.getValue(), price)), Product.class)
                    .retrieve()
                    .bodyToMono(boolean.class)
                    .block();
            System.out.println(out);
        });
        clearPro.addClickListener(event ->{
            proName.setValue("");
            proCost.setValue(0.0);
            proPro.setValue(0.0);
            proPrice.setValue(0.0);
        });
        upPro.addClickListener(event ->{
            double price = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPrice/"+proCost.getValue()+"/"+proPro.getValue())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();
            proPrice.setValue(price);
            boolean out = WebClient.create()
                    .post().uri("http://localhost:8080/updateProduct")
                    .body(Mono.just(new Product(this.product.get_id(), proName.getValue(), proCost.getValue(), proPro.getValue(), price)), Product.class)
                    .retrieve()
                    .bodyToMono(boolean.class)
                    .block();
            System.out.println(out);
        });
        delPro.addClickListener(event ->{
            boolean out = WebClient.create()
                    .post().uri("http://localhost:8080/deleteProduct")
                    .body(Mono.just(this.product.getProductName()),String.class)
                    .retrieve()
                    .bodyToMono(boolean.class)
                    .block();
        });
    }
}
