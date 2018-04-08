package com.parthiv.user.api;

import com.parthiv.user.model.Item;
import com.parthiv.user.model.Order;
import com.parthiv.user.model.User;

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

    @POST("orders")
    Observable<JSONObject> postOrder(@Body Order order);

    @GET("items")
    Observable<List<Item>> getItems();

    @GET("users/{id}/orders")
    Observable<List<Order>> getOrdersByUserId(@Path("id") long id);

    @GET("orders/{id}")
    Observable<Order> getOrderByOrderId(@Path("id") long id);

    @GET("items")
    Observable<List<Item>> getItemsByIds(@Query("ids") String ids);

    @GET("items/{id}")
    Observable<Item> getItemById(@Path("id") long id);

    @POST("users")
    Observable<User> postUser(@Body User user);
}
