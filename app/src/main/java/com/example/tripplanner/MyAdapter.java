package com.example.tripplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.model.DestinationModel;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DestinationModel> dataList;
    private OnItemClickListener onItemClickListener;

    public MyAdapter(Context context, List<DestinationModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int checkedId);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DestinationModel destinationModel = dataList.get(position);

        // Set destination info, including total price
        long basePrice = destinationModel.getPrice();
        holder.destinationInfo.setText("Base Price: $" + basePrice);

        // Set destination name
        holder.destinationName.setText(destinationModel.getName());

        // Set the image resource dynamically based on dataImage
        holder.destinationImage.setImageResource(destinationModel.getDataImage());

        // Handle item click event
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(holder.itemView, position, -1); // No checkedId
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView destinationInfo;
        public TextView destinationName;
        public ImageView destinationImage;
        public CardView cardView;
        public RadioGroup locationRadioGroup;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            destinationInfo = itemView.findViewById(R.id.recInfo); // Modify the ID accordingly
            destinationName = itemView.findViewById(R.id.recName); // Modify the ID accordingly
            destinationImage = itemView.findViewById(R.id.recImage); // Modify the ID accordingly
            cardView = itemView.findViewById(R.id.cardView1); // Adjust the ID accordingly
        }
    }
}
