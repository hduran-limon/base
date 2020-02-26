package com.udg.envio_de_fotos.Adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.udg.envio_de_fotos.R;

import java.util.List;

public class EnviarAdapter extends RecyclerView.Adapter<EnviarAdapter.ViewHolderFotos> {


    private  List<Bitmap> fotosBitmap;

    public EnviarAdapter(List<Bitmap> fotosBitmap) {
        this.fotosBitmap = fotosBitmap;
    }

    @Override
    public EnviarAdapter.ViewHolderFotos onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_galeria, null, false);
        return new EnviarAdapter.ViewHolderFotos(view);

    }

    @Override
    public void onBindViewHolder(EnviarAdapter.ViewHolderFotos holder, int position) {
            holder.foto.setImageBitmap(fotosBitmap.get(position));
    }

    @Override
    public int getItemCount() {
        return fotosBitmap.size();
    }

    public class ViewHolderFotos extends RecyclerView.ViewHolder {
        private ImageView foto;

        public ViewHolderFotos(View itemView) {
            super(itemView);
            this.foto = foto;
            foto=(ImageView)itemView.findViewById(R.id.imgT);
        }
    }
}
