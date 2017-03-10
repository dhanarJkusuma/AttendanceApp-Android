package com.dhanarjkusuma.sarihusada.sarihusada.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.ListDataOverflowInterface;

import java.util.Collections;
import java.util.List;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class AdapterPeserta extends RecyclerView.Adapter<AdapterPeserta.CustomHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Peserta> peserta = Collections.emptyList();
    private Peserta current;
    private ListDataOverflowInterface listDataOverflowInterface;

    public AdapterPeserta(Context context, List<Peserta> peserta, ListDataOverflowInterface listDataOverflowInterface) {
        this.context = context;
        this.peserta = peserta;
        this.inflater = LayoutInflater.from(context);
        this.listDataOverflowInterface = listDataOverflowInterface;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_peserta, parent, false);
        CustomHolder holder = new CustomHolder(view);
        return holder;
    }

    public void addItem(Peserta peserta){
        this.peserta.add(peserta);
        notifyDataSetChanged();
    }

    public void addBulk(List<Peserta> peserta){
        this.peserta.addAll(peserta);
        notifyDataSetChanged();
    }

    public void clearItem(){
        this.peserta.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        current = peserta.get(position);
        holder.name.setText(current.getName());
        holder.alamat.setText(current.getAlamat());
        holder.overFlowMenu.setOnClickListener(new AdapterPesertaOverflow(context, current, listDataOverflowInterface));
        holder.cardPeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDataOverflowInterface.detail(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return peserta.size();
    }

    class CustomHolder extends RecyclerView.ViewHolder{
        CardView cardPeserta;
        TextView name;
        TextView alamat;
        ImageView overFlowMenu;
        public CustomHolder(View itemView) {
            super(itemView);
            cardPeserta = (CardView) itemView.findViewById(R.id.cardPeserta);
            name = (TextView) itemView.findViewById(R.id.name);
            alamat = (TextView) itemView.findViewById(R.id.alamat);
            overFlowMenu = (ImageView) itemView.findViewById(R.id.pesertaOverflow);
        }
    }

}
