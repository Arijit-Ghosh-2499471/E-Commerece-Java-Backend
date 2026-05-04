package com.cts.ecommerce.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

public class CartMapper {

    RowMapper<Map<String,Object>> res = (rs,c)->{
        Map<String, Object> product = new HashMap<>();
        product.put("ProductId", rs.getInt("ProductId"));
        product.put("ProductName", rs.getString("ProductName"));
        product.put("Description", rs.getString("Description"));
        product.put("Price", rs.getDouble("Price"));
        product.put("CategoryId", rs.getInt("CategoryId"));
        product.put("ImageURL", rs.getString("ImageURL"));
        product.put("UserId", rs.getInt("UserId"));
        product.put("ShoppingCartId",rs.getInt("ShoppingCartId"));
        return product;
    };

    public RowMapper<Map<String,Object>> getCartMapper(){
        return res;
    }

}
