package skallaje.cafeteria_app.cs442.com;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Smruti on 10/21/2015.
 */
public class ListDataAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public ListDataAdapter(Context context, int resources)
    {
        super(context,resources);
    }

    static class  LayoutManager
    {
          TextView Name,Price;
          EditText qty;

    }

    @Override
    public void add(Object object)
    {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public  View getView(int position,View contentView,ViewGroup parent)
    {

        View row = contentView;
        final LayoutManager lm;

        if( row == null)
        {
            LayoutInflater lf = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = lf.inflate(R.layout.display_row,parent,false);
            lm = new LayoutManager();
            lm.qty= new EditText(getContext());
            lm.Name= (TextView)row.findViewById(R.id.t_name);
            lm.Price = (TextView)row.findViewById(R.id.t_price);
          //  lm.Desc = (TextView)row.findViewById(R.id.t_desc);
            row.setTag(lm);
        }
        else
        {
            lm = (LayoutManager)row.getTag();

        }

        //final DataProvider dataProvider = (DataProvider)this.getItem(position);

      //final MenuItems m = (MenuItems)this.getItem(position);


        final MenuItems m = (MenuItems) this.getItem(position);

        lm.qty.setText("");
        lm.Name.setText(m.getItem());
        lm.Price.setText('$'+String.valueOf(m.getPrice()));
        //lm.Desc.setText(m.getDesc());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lm.qty.setCursorVisible(true);
                lm.qty.setFocusableInTouchMode(true);
                lm.qty.setInputType(InputType.TYPE_CLASS_TEXT);
                lm.qty.requestFocus();

                String id = String.valueOf(m.getID());

                Toast.makeText(getContext(), "Item Selected for View", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), DescriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID", id);
                getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });
        return row;
    }

}
