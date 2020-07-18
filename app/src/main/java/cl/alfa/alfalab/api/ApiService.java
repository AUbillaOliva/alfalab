package cl.alfa.alfalab.api;

import java.util.ArrayList;

import cl.alfa.alfalab.models.Orders;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
        Call<Orders> postOrder(@Body Orders order);

    }

    public interface UpdateOrderService {
        @POST("orders/{number}")
        Call<ResponseBody> updateOrder(@Path("number") int id, @Body Orders order);
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

    /*public interface WordsService {
        @GET("words")
        Call<ArrayList<Word>> getWords(@Query("letter") String letter, @Query("category") String category);
    }*/

    /*public interface ExpressionsServiceCategories {
        @GET("expressions")
        Call<ArrayList<Word>> getExpressionsOfCategories(@Query("category") String category);
    }*/

    /*public interface ExpressionsCategoryService {
        @GET("expressions/categories")
        Call<ArrayList<String>> getExpressionsCategories();
    }*/

    /*public interface ThemesCategoryService {
        @GET("words/categories")
        Call<ArrayList<String>> getThemesCategories();
    }*/
}
