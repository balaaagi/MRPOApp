package mrpo.balaaagi.me.mrpo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by balaaagi on 21/04/16.
 */
public class MedicineAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<MedicineDetails> medicines;
    protected LayoutInflater mInflater;
    public MedicineAdapter(Context context, ArrayList<MedicineDetails> medicines) {
//        super(context, resource, medicines);
        this.context=context;
        this.medicines=medicines;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.medicines.size();
    }

    @Override
    public Object getItem(int i) {
        return this.medicines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.list_item_forecast,parent,false);
        TextView textLabel= (TextView) rowView.findViewById(R.id.list_item_prescription_textview);
        MedicineDetails temp= (MedicineDetails) medicines.get(position);
        textLabel.setText(temp.getName()+"--------------"+temp.getDate());

        return rowView;
    }
}
