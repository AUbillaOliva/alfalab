package cl.alfa.alfalab.api;

import java.util.ArrayList;

import cl.alfa.alfalab.models.AuthUser;
import cl.alfa.alfalab.models.LoginData;
import cl.alfa.alfalab.models.Message;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ApiService {

    public interface OrderService {
        @GET("orders")
        Call<ArrayList<Orders>> getOrders();
    }

    public interface DeleteOrderService {
        @DELETE("orders/{number}")
        Call<ResponseBody> deleteOrder(@Path("number") int id, @Header("x-auth-token") String token);
    }

    public interface PostOrderService {
        @POST("orders")
        Call<Orders> postOrder(@Body Orders order, @Header("x-auth-token") String token);
    }

    public interface GetUserService {
        @GET("auth")
        Call<AuthUser> getUser(@Header("x-auth-token") String token);
    }

    public interface UpdateOrderService {
        @POST("orders/{number}")
        Call<Orders> updateOrder(@Path("number") int id, @Body Orders order, @Header("x-auth-token") String token);
    }

    public interface DeliveredService {
        @GET("delivered")
        Call<ArrayList<Orders>> getDeliveries();
    }

    public interface DeleteDelivery {
        @DELETE("delivered/{number}")
        Call<ResponseBody> deleteDelivery(@Path("number") int id, @Header("x-auth-token") String token);
    }

    public interface SetDeliveredService {
        @POST("delivered")
        Call<Orders> setDelivered(@Body Orders order, @Header("x-auth-token") String token);
    }

    public interface GetAuthService {
        @POST("auth")
        Call<ResponseBody> getAuth(@Body LoginData loginData);
    }

    public interface RefreshTokenService {
        @POST("auth/refresh/{token}")
        Call<ResponseBody> refreshToken(@Path("token") String token);
    }

    public interface SignUpService {
        @POST("users")
        Call<ResponseBody> signUp(@Body User user);
    }

    public interface UpdateUserService {
        @PUT("users")
        Call<ResponseBody> updateUser(@Header("x-auth-token") String token, @Body User newUser);
    }

    public interface MessagesService {
        @GET("messages")
        Call<ArrayList<Message>> getMessages(@Header("x-auth-token") String token);
    }

    public interface PostMessageService {
        @POST("messages")
        Call<Message> postMessage(@Body Message message, @Header("x-auth-token") String token);
    }

    public interface UpdateMessageService {
        @POST("messages/{id}")
        Call<Message> updateMessage(@Body Message message, @Path("id") String id, @Header("x-auth-token") String token);
    }

    public interface DeleteMessageService {
        @DELETE("messages/{id}")
        Call<ResponseBody> deleteMessage(@Path("id") String id, @Header("x-auth-token") String token);
    }

}
