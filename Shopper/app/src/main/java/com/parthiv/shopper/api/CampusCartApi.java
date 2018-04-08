package com.parthiv.shopper.api;

import com.parthiv.shopper.model.Item;
import com.parthiv.shopper.model.Order;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CampusCartApi {

    @POST("items")
    Observable<JSONObject> postItem(@Body Item item);

    @GET("items")
    Observable<List<Item>> getItems();

    @GET("orders")
    Observable<List<Order>> getOrders();

    @GET("items/{id}")
    Observable<Item> getItemById(@Path("id") long id);

}
