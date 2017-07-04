package de.dzmitry_lamaka.lesaratesttask.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import de.dzmitry_lamaka.lesaratesttask.network.data.Product;
import de.dzmitry_lamaka.lesaratesttask.network.data.Products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(shadows = ShadowLooper.class)
@RunWith(RobolectricTestRunner.class)
public class ProductsLoaderTest {
    @Mock
    private RestCaller restCaller;
    @Mock
    private Executor executor;
    @Mock
    private ProductsLoader.Listener listener;
    @Spy
    private AtomicInteger nextPage;
    @Spy
    private AtomicInteger totalPages;
    @Captor
    private ArgumentCaptor<Runnable> taskCaptor;

    private ProductsLoader subject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new ProductsLoader(restCaller, executor, nextPage, totalPages);
        subject.setListener(listener);
    }

    @Test
    public void loadNextPage_whenNextPageIsEqualToTotalPages_shouldCallOnLoadedWithEmptyList() throws Exception {
        when(nextPage.intValue()).thenReturn(100500);
        when(totalPages.intValue()).thenReturn(100500);
        subject.loadNextPage();
        verify(listener).onLoaded(Collections.<Product>emptyList());
        verify(executor, never()).execute(any(Runnable.class));
    }

    @Test
    public void loadNextPage_whenLoadedSuccessfully_shouldCallOnLoadedWithCorrectValue() throws Exception {
        nextPage.set(1);
        totalPages.set(0);
        final ApiResult apiResult = mock(ApiResult.class);
        when(apiResult.isSuccess()).thenReturn(true);
        final Products trendProducts = mock(Products.class);
        when(apiResult.getData()).thenReturn(trendProducts);
        when(trendProducts.getCurrentPage()).thenReturn(2);
        when(trendProducts.getNumberPages()).thenReturn(5);
        final List<Product> products = new ArrayList<>();
        products.add(mock(Product.class));
        products.add(mock(Product.class));
        when(trendProducts.getProducts()).thenReturn(products);
        when(restCaller.getTrendProducts(1)).thenReturn(apiResult);
        subject.loadNextPage();
        verify(executor).execute(taskCaptor.capture());
        taskCaptor.getValue().run();
        verify(listener).onLoaded(products);
        assertThat(nextPage.intValue()).isEqualTo(3);
        assertThat(totalPages.intValue()).isEqualTo(5);
    }

    @Test
    public void loadNextPage_whenLoadedUnsuccessfully_shouldCallOnError() throws Exception {
        nextPage.set(1);
        totalPages.set(0);
        final ApiResult apiResult = mock(ApiResult.class);
        when(apiResult.isSuccess()).thenReturn(false);
        when(apiResult.getError()).thenReturn("error");
        when(restCaller.getTrendProducts(1)).thenReturn(apiResult);
        subject.loadNextPage();
        verify(executor).execute(taskCaptor.capture());
        taskCaptor.getValue().run();
        verify(listener).onError("error");
        assertThat(nextPage.intValue()).isEqualTo(1);
        assertThat(totalPages.intValue()).isEqualTo(0);
    }
}
