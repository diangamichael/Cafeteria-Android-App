package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shara on 11/5/2015.
 */
public class MenuAdapter extends ArrayAdapter<MenuItems> {

    private List<MenuItems> mList;
    private Context context;


    public MenuAdapter(List<MenuItems> mList, Context context) {
        super(context, R.layout.menuitem, mList);
        this.mList = mList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater li = ((Activity) context).getLayoutInflater();
        convertView = li.inflate(R.layout.activity_menu, parent, false);

        Log.d("App", "" + convertView.findViewById(R.id.m_price));

        TextView priceView = (TextView) convertView.findViewById(R.id.m_price);
        TextView itemView = (TextView) convertView.findViewById(R.id.m_item);
        TextView idView = (TextView) convertView.findViewById(R.id.m_id);
       // CheckBox chkboxView = (CheckBox) convertView.findViewById(R.id.m_chkbox);

        MenuItems m = mList.get(position);

        priceView.setText("$"+String.valueOf(m.getPrice()));
        itemView.setText(m.getItem());
        idView.setText(Integer.valueOf(position+1).toString()+'.');
        //chkboxView.setTag(m.getID());

        return convertView;
    }
}