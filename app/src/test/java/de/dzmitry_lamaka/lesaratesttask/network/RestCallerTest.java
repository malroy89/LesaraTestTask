package de.dzmitry_lamaka.lesaratesttask.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.nio.charset.Charset;

import de.dzmitry_lamaka.lesaratesttask.network.data.Products;
import de.dzmitry_lamaka.lesaratesttask.network.data.TrendProducts;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@Config(shadows = ShadowTextUtils.class)
@RunWith(RobolectricTestRunner.class)
public class RestCallerTest {
    @Mock
    private RestApi restApi;

    private RestCaller subject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new RestCaller(restApi);
    }

    @Test
    public void getTrendProducts_whenResponseIsSuccessfull_whenBodyIsNull_shouldReturnError() throws Exception {
        final Call<TrendProducts> call = mock(Call.class);
        final Response<TrendProducts> response = Response.success(null);
        when(call.execute()).thenReturn(response);
        when(restApi.getTrendProducts(1)).thenReturn(call);
        final ApiResult<Products> result = subject.getTrendProducts(1);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getData()).isNull();
        assertThat(result.getError()).isNotEmpty();
    }

    @Test
    public void getTrendProducts_whenResponseIsSuccessfull_shouldReturnSuccessResult() throws Exception {
        final Call<TrendProducts> call = mock(Call.class);
        final TrendProducts trendProducts = mock(TrendProducts.class);
        final Response<TrendProducts> response = Response.success(trendProducts);
        when(call.execute()).thenReturn(response);
        final Products products = mock(Products.class);
        when(trendProducts.getTrendProducts()).thenReturn(products);
        when(restApi.getTrendProducts(1)).thenReturn(call);
        final ApiResult<Products> result = subject.getTrendProducts(1);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isSameAs(products);
        assertThat(result.getError()).isNull();
    }


    @Test
    public void getTrendProducts_whenResponseIsNotSuccessfull_shouldReturnError() throws Exception {
        final Call<TrendProducts> call = mock(Call.class);
        final ResponseBody responseBody = spy(ResponseBody.class);
        BufferedSource source = mock(BufferedSource.class);
        when(responseBody.source()).thenReturn(source);
        when(source.readString(any(Charset.class))).thenReturn("Error");
        final Response<TrendProducts> response = Response.error(404, responseBody);
        when(call.execute()).thenReturn(response);
        when(restApi.getTrendProducts(1)).thenReturn(call);
        final ApiResult<Products> result = subject.getTrendProducts(1);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getData()).isNull();
        assertThat(result.getError()).isEqualTo("Error");
    }
}
