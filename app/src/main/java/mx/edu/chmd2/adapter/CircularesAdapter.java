package mx.edu.chmd2.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mx.edu.chmd2.R;
import mx.edu.chmd2.modelos.Circular;

public class CircularesAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Circular> items,originales;
    Circular c;
    ViewHolder holder=new ViewHolder();
    String TAG="CircularesAdapter";
    ArrayList<String> seleccionados = new ArrayList<String>();
    Typeface tf,tfBold;
    public CircularesAdapter(Activity activity, ArrayList<Circular> items) {
        this.activity = activity;
        this.items = items;
        tf = Typeface.createFromAsset(activity.getAssets(),"fonts/GothamRoundedBook_21018.ttf");
        tfBold = Typeface.createFromAsset(activity.getAssets(),"fonts/GothamRoundedBold_21016.ttf");
    }




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
            holder.llContainer = convertView.findViewById(R.id.llContainer);
            holder.chkSeleccion = convertView.findViewById(R.id.chkSeleccion);

            holder.chkSeleccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    items.get(getPosition).setSelected(buttonView.isChecked());
                    if (isChecked) {
                        seleccionados.add(String.valueOf(getPosition));

                    }else{
                        seleccionados.remove(String.valueOf(getPosition));
                    }
                }
            });



            convertView.setTag(holder);
            convertView.setTag(R.id.chkSeleccion, holder.chkSeleccion);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lblEncab.setTypeface(tfBold);
        holder.lblNomCircular.setTypeface(tf);
        holder.lblFecha1.setTypeface(tf);
        holder.lblFecha2.setTypeface(tf);


        if(c.getLeida()==1){
            holder.imgCircular.setImageResource(R.drawable.leidas_azul);
            holder.llContainer.setBackgroundColor(Color.WHITE);
        }
        if(c.getLeida()==0){
            holder.imgCircular.setImageResource(R.drawable.noleidas_celeste);
            holder.llContainer.setBackgroundColor(Color.parseColor("#D4D4D4"));
        }
        if(c.getFavorita()==1){
            holder.imgCircular.setImageResource(R.drawable.appmenu06);
            holder.llContainer.setBackgroundColor(Color.WHITE);
        }
        if(c.getCompartida()==1){
            holder.imgCircular.setImageResource(R.drawable.appmenu08);
            holder.llContainer.setBackgroundColor(Color.WHITE);
        }

        if(c.getEliminada()==1){
            holder.imgCircular.setImageResource(R.drawable.basurero_azul);
            holder.llContainer.setBackgroundColor(Color.WHITE);
        }

        holder.lblEncab.setText(c.getNombre());
        holder.lblNomCircular.setText(c.getEncabezado());
        holder.lblFecha1.setText(c.getFecha1());
        //holder.lblFecha2.setText(c.getFecha2());
        holder.lblFecha2.setText("");

        holder.chkSeleccion.setTag(position);
        holder.chkSeleccion.setChecked(items.get(position).isSelected());


        return convertView;
    }


    public ArrayList<String> getSeleccionados(){
        return seleccionados;
    }

    static class ViewHolder {
        TextView lblNomCircular,lblEncab,lblFecha1,lblFecha2;
        ImageView imgCircular;
        LinearLayout llContainer;
        CheckBox chkSeleccion;


    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Circular> results = new ArrayList<Circular>();
                if (originales == null)
                    originales = items;
                if (constraint != null) {
                    if (originales != null && originales.size() > 0) {
                        for (final Circular c : originales) {
                            if (c.getNombre().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(c);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (ArrayList<Circular>) results.values;
                notifyDataSetChanged();
            }
        };

    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
