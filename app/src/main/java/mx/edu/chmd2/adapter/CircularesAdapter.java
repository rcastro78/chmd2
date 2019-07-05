package mx.edu.chmd2.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.edu.chmd2.R;
import mx.edu.chmd2.modelos.Circular;

public class CircularesAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Circular> items;
    Circular c;
    ViewHolder holder=new ViewHolder();
    String TAG="CircularesAdapter";

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        c = items.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.celda_circulares, null);
            holder = new ViewHolder();
            holder.lblNomCircular = convertView.findViewById(R.id.lblNomCircular);
            holder.lblEncab = convertView.findViewById(R.id.lblEncab);
            holder.lblFecha1 = convertView.findViewById(R.id.lblFecha1);
            holder.lblFecha2 = convertView.findViewById(R.id.lblFecha2);
            holder.imgCircular = convertView.findViewById(R.id.imgCircular);
            holder.fabEliminar = convertView.findViewById(R.id.fabEliminar);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lblEncab.setText(c.getEncabezado());
        holder.lblNomCircular.setText(c.getNombre());
        holder.lblFecha1.setText(c.getFecha1());
        holder.lblFecha2.setText(c.getFecha2());
        if (c.getEstado()==1){
            //TODO:Cambiar icono
        }
        holder.fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Falta la implementación
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView lblNomCircular,lblEncab,lblFecha1,lblFecha2;
        ImageView imgCircular;
        FloatingActionButton fabEliminar;

    }
}
