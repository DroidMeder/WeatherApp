package kg.geekteck.weatherapp.ui.savedweather;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import kg.geekteck.weatherapp.common.OnItemClick;
import kg.geekteck.weatherapp.data.models.room.CurrentWeather;
import kg.geekteck.weatherapp.databinding.ItemWeather1Binding;
import kg.geekteck.weatherapp.databinding.ItemWeatherBinding;

class ListWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CurrentWeather> list;
    ItemWeatherBinding binding;
    ItemWeather1Binding binding1;
    OnItemClick clicked;

    public ListWeatherAdapter(OnItemClick clicked) {
        this.clicked = clicked;
        list=new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            binding = ItemWeatherBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new VH1(binding);
        } else {
            binding1 = ItemWeather1Binding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new VH2(binding1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (getItemViewType(position) == 0) {
            ((VH1) holder).bind1(list.get(position));
        } else {
            ((VH2) holder).bind2(list.get(position));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked.click(list.get(position));
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void clearData() {
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<CurrentWeather> data) {
        list=data;
        notifyDataSetChanged();
    }

    protected static class VH1 extends RecyclerView.ViewHolder {
        ItemWeatherBinding binding;

        public VH1(@NonNull ItemWeatherBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        @SuppressLint("SetTextI18n")
        public void bind1(CurrentWeather news) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
            String str = String.valueOf(simpleDateFormat.format(news.getCreatedAt()));
            binding.tvDate.setText(str);
            binding.tvCityName.setText(news.getLocation());
        }
    }

    protected static class VH2 extends RecyclerView.ViewHolder {
        ItemWeather1Binding binding;

        public VH2(@NonNull ItemWeather1Binding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind2(CurrentWeather news) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
            String str = String.valueOf(simpleDateFormat.format(news.getCreatedAt()));
            binding.tvDate.setText(str);
            binding.tvCityName.setText(news.getLocation());        }
    }
}
