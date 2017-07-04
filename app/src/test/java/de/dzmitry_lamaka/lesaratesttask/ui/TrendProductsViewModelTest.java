package de.dzmitry_lamaka.lesaratesttask.ui;

import android.arch.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import de.dzmitry_lamaka.lesaratesttask.network.data.Product;
import de.dzmitry_lamaka.lesaratesttask.network.ProductsLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TrendProductsViewModelTest {
    @Mock
    private ProductsLoader productsLoader;
    @Mock
    private MutableLiveData<State> mutableLiveData;
    @Captor
    private ArgumentCaptor<State> stateCaptor;

    private TrendProductsViewModel subject;

    @Before
    public void setUp() throws Exception {
        subject = new TrendProductsViewModel(productsLoader, mutableLiveData);
    }

    @Test
    public void loadNextPage() throws Exception {
        subject.loadNextPage();
        verify(mutableLiveData).setValue(stateCaptor.capture());
        verify(productsLoader).loadNextPage();
        final State captured = stateCaptor.getValue();
        assertThat(captured.isLoaded()).isFalse();
        assertThat(captured.isError()).isFalse();
        assertThat(captured.isLoading()).isTrue();
        assertThat(captured.getErrorMessage()).isNull();
        assertThat(captured.getProducts()).isNull();
    }

    @Test
    public void onLoaded() throws Exception {
        final List<Product> products = new ArrayList<>();
        products.add(mock(Product.class));
        products.add(mock(Product.class));
        subject.onLoaded(products);
        verify(mutableLiveData).setValue(stateCaptor.capture());
        final State captured = stateCaptor.getValue();
        assertThat(captured.isLoaded()).isTrue();
        assertThat(captured.isError()).isFalse();
        assertThat(captured.isLoading()).isFalse();
        assertThat(captured.getErrorMessage()).isNull();
        assertThat(captured.getProducts()).hasSize(2);
    }

    @Test
    public void onError() throws Exception {
        subject.onError("Error");
        verify(mutableLiveData).setValue(stateCaptor.capture());
        final State captured = stateCaptor.getValue();
        assertThat(captured.isLoaded()).isFalse();
        assertThat(captured.isError()).isTrue();
        assertThat(captured.isLoading()).isFalse();
        assertThat(captured.getErrorMessage()).isEqualTo("Error");
        assertThat(captured.getProducts()).isNull();
    }

}
