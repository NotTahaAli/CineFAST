package com.l230954.cinefast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SnacksAdapter extends ArrayAdapter<Snack> {

    Context context;
    ArrayList<Snack> snacks;

    public SnacksAdapter(@NonNull Context context, @NonNull ArrayList<Snack> objects) {
        super(context, 0, objects);
        this.context = context;
        this.snacks = objects;
    }

    @Override
    public int getCount() {
        return snacks.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Snack snack = getItem(position);
        assert snack != null;
        convertView = LayoutInflater.from(context).inflate(R.layout.snack_line_item, parent, false);

        ImageView ivSnackImage = convertView.findViewById(R.id.ivSnackImage);
        ImageView ivRemoveBtn = convertView.findViewById(R.id.ivRemoveBtn);
        ImageView ivAddBtn = convertView.findViewById(R.id.ivAddBtn);
        TextView tvSnackName = convertView.findViewById(R.id.tvSnackName);
        TextView tvSnackDetails = convertView.findViewById(R.id.tvSnackDetails);
        TextView tvSnackPrice = convertView.findViewById(R.id.tvSnackPrice);
        TextView tvSnackQuantity = convertView.findViewById(R.id.tvSnackQuantity);

        ivSnackImage.setImageResource(snack.imageId);
        tvSnackName.setText(snack.name);
        tvSnackDetails.setText(snack.description);
        tvSnackPrice.setText(CurrencyHelper.formatCurrency(snack.price));
        tvSnackQuantity.setText(String.valueOf(snack.quantity));

        ivAddBtn.setOnClickListener(v -> {
            snack.quantity++;
            tvSnackQuantity.setText(String.valueOf(snack.quantity));
        });

        ivRemoveBtn.setOnClickListener(v->{
            if (snack.quantity > 0) {
                snack.quantity--;
                tvSnackQuantity.setText(String.valueOf(snack.quantity));
            }
        });

        return convertView;
    }
}
