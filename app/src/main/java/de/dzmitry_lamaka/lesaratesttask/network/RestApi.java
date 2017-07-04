package de.dzmitry_lamaka.lesaratesttask.network;

import de.dzmitry_lamaka.lesaratesttask.network.data.TrendProducts;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {
    @GET("/restapi/v1/trendproducts?app_token=this_is_an_app_token&user_token=63a12a8116814a9574842515378c93c64846fc3d0858def78388be37e127cd17&store_id=1")
    Call<TrendProducts> getTrendProducts(@Query("page_override") int pageNumber);
}
