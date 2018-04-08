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
import retrofit2.http.Query;

public interface CampusCartApi {

    @POST("items")
    Observable<JSONObject> postItem(@Body Item item);

    @GET("items")
    Observable<List<Item>> getItems();

    @GET("orders")
    Observable<List<Order>> getOrders();

    @GET("items")
    Observable<List<Item>> getItemsByIds(@Query("ids") String ids);

    @GET("orders/{id}")
    Observable<Order> getOrderByOrderId(@Path("id") long id);

    @GET("items/{id}")
    Observable<Item> getItemById(@Path("id") long id);

    @POST("orders")
    Observable<JSONObject> patchOrder(@Body Order order);
}
