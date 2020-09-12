package cl.alfa.alfalab.utils;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;

public class GenericViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> holder = new SparseArray<>();

    public GenericViewHolder(@NonNull View itemView, final RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        super(itemView);
        deepScan((ViewGroup)itemView);
        itemView.setOnClickListener(v -> {
            if(recyclerViewOnClickListenerHack != null)
                recyclerViewOnClickListenerHack.onClickListener(v, getLayoutPosition());
        });
        itemView.setOnLongClickListener(v -> {
            if(recyclerViewOnClickListenerHack != null)
                recyclerViewOnClickListenerHack.onLongPressClickListener(v, getLayoutPosition());
            return false;
        });
    }

    private void deepScan(ViewGroup v) {
        if (v.getChildCount() == 0)
            return;
        for(int i=0; i< v.getChildCount(); ++i) {
            final View nextChild = v.getChildAt(i);
            put(nextChild);
            if (nextChild instanceof ViewGroup)
                deepScan((ViewGroup)nextChild);
        }
    }

    public <T extends View> T get(Integer id){
        return (T) holder.get(id);
    }

    private void put(View view){
        put(view, view.getId());
    }

    private void put(View view, Integer id){
        if (id == -1) return;
        holder.put(id, view);
    }

    public void clear(){
        holder.clear();
    }

}
