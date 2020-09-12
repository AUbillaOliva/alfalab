package cl.alfa.alfalab.adapters;

import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;

public abstract class GenericAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<T> items = new ArrayList<>();
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent, RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val, int position);

    public abstract RecyclerViewOnClickListenerHack onGetRecyclerViewOnClickListenerHack();

    protected GenericAdapter(ArrayList<T> items){
        this.items = items;
        this.mRecyclerViewOnClickListenerHack = onGetRecyclerViewOnClickListenerHack();
    }

    protected GenericAdapter(){
        this.mRecyclerViewOnClickListenerHack = onGetRecyclerViewOnClickListenerHack();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return setViewHolder(parent, mRecyclerViewOnClickListenerHack);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindData(holder,items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void addItems(ArrayList<T> savedCardItems){
        items = savedCardItems;
        this.notifyDataSetChanged();
    }

    public void addItem(T savedCardItem){
        items.add(savedCardItem);
        this.notifyDataSetChanged();
    }

    public void deleteItem(T item){
        ArrayList<T> tmpItems = new ArrayList<>();
        for(int i= 0;i<items.size();i++)
            if(item != items.get(i))
                tmpItems.add(items.get(i));
        this.items = tmpItems;
    }

    public T getItem(int position){
        return items.get(position);
    }

    public void clear(){
        this.items.clear();
        this.notifyDataSetChanged();
    }
}
