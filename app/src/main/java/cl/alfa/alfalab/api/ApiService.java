package cl.alfa.alfalab.api;

import java.util.ArrayList;
import java.util.Map;

import cl.alfa.alfalab.models.LoginData;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiService {

    public interface OrderService {
        @GET("orders")
        Call<ArrayList<Orders>> getOrders();
    }

    public interface DeleteOrderService {
        @DELETE("orders/{number}")
        Call<ResponseBody> deleteOrder(@Path("number") int id);
    }

    public interface PostOrderService {
        @POST("orders")
        Call<Orders> postOrder(@Body Orders order, @Header("x-auth-token") String token);
    }

    public interface GetUserService {
        @GET("auth")
        Call<User> getUser(@Header("x-auth-token") String token);
    }

    public interface UpdateOrderService {
        @POST("orders/{number}")
        Call<Orders> updateOrder(@Path("number") int id, @Body Orders order);
    }

    public interface DeliveredService {
        @GET("delivered")
        Call<ArrayList<Orders>> getDeliveries();
    }

    public interface DeleteDelivery {
        @DELETE("delivered/{number}")
        Call<ResponseBody> deleteDelivery(@Path("number") int id);
    }

    public interface SetDeliveredService {
        @POST("delivered")
        Call<Orders> setDelivered(@Body Orders order);
    }

    public interface GetAuthService {
        @POST("auth")
        Call<ResponseBody> getAuth(@Body LoginData loginData);
    }

    public interface SignUpService {
        @POST("users")
        Call<ResponseBody> signUp(@Body User user);
    }

}
