package de.dzmitry_lamaka.lesaratesttask.ui;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import de.dzmitry_lamaka.lesaratesttask.R;
import de.dzmitry_lamaka.lesaratesttask.network.data.Product;

public class TrendProductsActivity extends AppCompatActivity implements LifecycleRegistryOwner {
    private static final int NUMBER_OF_ROWS = 2;
    private static final int PRODUCTS_PER_PAGE = 48;
    @NonNull
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    @Nullable
    private TrendProductsViewModel viewModel;
    @Nullable
    private ProductsAdapter adapter;
    @Nullable
    private RecyclerView recyclerView;
    @Nullable
    private ProgressBar progressBar;
    @Nullable
    private LiveData<State> stateObservable;
    @NonNull
    private final Observer<State> observer = new Observer<State>() {
        @Override
        public void onChanged(@Nullable final State state) {
            if (state == null || recyclerView == null || progressBar == null) {
                return;
            }
            if (state.isLoaded()) {
                onLoaded(state.getProducts());
            } else if (state.isLoading()) {
                onLoading();
            } else {
                onError(state.getErrorMessage());
            }
        }
    };
    @NonNull
    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (adapter == null || stateObservable == null || viewModel == null) {
                return;
            }
            final State state = stateObservable.getValue();
            if (state != null && state.isLoading()) {
                return;
            }
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            final int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            if (adapter.getItemCount() - firstVisiblePosition <= PRODUCTS_PER_PAGE / NUMBER_OF_ROWS) {
                viewModel.loadNextPage();
            }
        }
    };

    private void onLoaded(@Nullable final List<Product> products) {
        if (products == null || adapter == null || progressBar == null || recyclerView == null) {
            return;
        }
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.addProducts(products);
    }

    private void onLoading() {
        if (progressBar == null || recyclerView == null || adapter == null) {
            return;
        }
        if (adapter.hasProducts()) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void onError(@Nullable final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_products);
        recyclerView = (RecyclerView) findViewById(android.R.id.list);
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(onScrollListener);
            recyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_ROWS));
            adapter = new ProductsAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        progressBar = (ProgressBar) findViewById(android.R.id.progress);
        stateObservable = getViewModel().getState();
        stateObservable.observe(this, observer);
        getViewModel().loadNextPage();
    }

    @NonNull
    private TrendProductsViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(TrendProductsViewModel.class);
        }
        return viewModel;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }
}
