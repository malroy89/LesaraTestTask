package de.dzmitry_lamaka.lesaratesttask.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.dzmitry_lamaka.lesaratesttask.R;
import de.dzmitry_lamaka.lesaratesttask.network.data.Product;

class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    @NonNull
    private final LayoutInflater layoutInflater;
    @NonNull
    private final List<Product> products = new ArrayList<>();

    ProductsAdapter(@NonNull final Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }
    void addProducts(@NonNull final List<Product> newProducts) {
        if (newProducts.isEmpty()) {
            return;
        }
        notifyItemRangeInserted(products.size() - 1, newProducts.size());
        products.addAll(newProducts);
    }

    boolean hasProducts() {
        return !products.isEmpty();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final Product item = products.get(position);
        Glide.with(context)
                .load(item.getThumbnailPath())
                .into(holder.image);
        holder.name.setText(item.getName());
        holder.price.setText(context.getString(R.string.item_product_price, item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final ImageView image;
        @NonNull
        private final TextView name;
        @NonNull
        private final TextView price;

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            price= (TextView) itemView.findViewById(R.id.price);
            if (image == null || name == null || price == null) {
                throw new IllegalStateException("Forgot to add all required views");
            }
        }
    }

}
