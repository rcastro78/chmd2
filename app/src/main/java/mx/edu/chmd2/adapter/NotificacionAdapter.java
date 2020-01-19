package mx.edu.chmd2.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mx.edu.chmd2.R;
import mx.edu.chmd2.modelos.Notificacion;

public class NotificacionAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Notificacion> items = new ArrayList<>();
    Notificacion notificacion;
    ViewHolder holder=new ViewHolder();
    String TAG="NotificacionAdapter";
    ArrayList<String> seleccionados = new ArrayList<String>();
    Typeface tf,tfBold;

    public NotificacionAdapter(Activity activity, ArrayList<Notificacion> items) {
        this.activity = activity;
        this.items = items;
        tf = Typeface.createFromAsset(activity.getAssets(),"fonts/GothamRoundedBook_21018.ttf");
        tfBold = Typeface.createFromAsset(activity.getAssets(),"fonts/GothamRoundedBold_21016.ttf");
    }

    @Override
    public int getCount() {
        try {
            return items.size();
        }catch (Exception e){
            return 0;
        }
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

        notificacion = items.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_notificacion, null);
            holder = new ViewHolder();
            holder.itemNombre = convertView.findViewById(R.id.itemNombre);
            holder.itemRecibido = convertView.findViewById(R.id.itemRecibido);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemNombre.setTypeface(tfBold);
        holder.itemRecibido.setTypeface(tf);
        holder.itemNombre.setText(notificacion.getTitulo());
        holder.itemRecibido.setText(notificacion.getRecibida());

        return convertView;
    }

    static class ViewHolder {
        TextView itemNombre,itemRecibido;
    }

}
