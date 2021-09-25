package com.example.FM_SHOP.Api;

import com.example.FM_SHOP.model.OrderSingle;
import com.example.FM_SHOP.model.Product;
import com.example.FM_SHOP.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("/register")
    @FormUrlEncoded
    Call<String> registerUser(@Field("name") String name,
                              @Field("email") String email,
                              @Field("password") String password);

    @POST("/login")
    @FormUrlEncoded
    Call<User> USER_CALL(@Field("email") String email,
                         @Field("password") String password);

    @POST("/vetify")
    @FormUrlEncoded
    Call<String> forgot(@Field("email") String email);

    @POST("/confirm")
    @FormUrlEncoded
    Call<String> reset(@Field("code") String code,
                       @Field("newPassword") String confirmPassword);

    @POST("/product/add")
    @FormUrlEncoded
    Call<String> addProduct(@Field("name") String name,
                            @Field("type") String type,
                            @Field("amount") int amount,
                            @Field("price") double price,
                            @Field("image") String image);


    @POST("/product/upload")
    @Multipart
    Call<String> uploadImage(@Part MultipartBody.Part photo);

    @GET("product/list")
    Call<List<Product>> productList();

    @POST("/product/update")
    @FormUrlEncoded
    Call<String> updateProduct(@Field("id") String id,
                               @Field("name") String name,
                               @Field("type") String type,
                               @Field("amount") int amount,
                               @Field("price") double price,
                               @Field("image") String image);

    @POST("/product/delete")
    @FormUrlEncoded
    Call<String> deleteProduct(@Field("id") String id);

    @POST("/cart/add")
    @FormUrlEncoded
    Call<String> insertCart(@Field("nameProduct") String nameProduct,
                            @Field("imageProduct") String imageProduct,
                            @Field("amountProduct") String amountProduct,
                            @Field("priceProduct") String priceProduct,
                            @Field("nameUser") String nameUser,
                            @Field("status") int status);

    @GET("cart/list")
    Call<List<OrderSingle>> LIST_CART();


    @POST("cart/update")
    @FormUrlEncoded
    Call<String> CALL_UPDATE(@Field("id") String id,
                             @Field("status") int status);


}
