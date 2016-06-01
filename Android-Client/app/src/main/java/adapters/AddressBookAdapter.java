package adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import modals.CommentTemplate;
import modals.ContactTemplate;
import vitymobi.com.todobluemix.R;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class AddressBookAdapter extends BaseAdapter {

    private Context parentContext;
    private LayoutInflater mInflater;
    private ArrayList<ContactTemplate> contactsList= new ArrayList<>();


    public AddressBookAdapter(Context parentReference, ArrayList<ContactTemplate> contacts){
        this.parentContext=parentReference;
        mInflater= LayoutInflater.from(parentReference);
        contactsList.clear();;
        contactsList=  contacts;
       // this.contactsList=contacts;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            view=mInflater.inflate(R.layout.row_address_book,null);
            holder.mTxtVwContactName=(TextView)view.findViewById(R.id.txtvwContactName);
            holder.mTxtVwContactLastActivity=(TextView)view.findViewById(R.id.txtvwContactLastActivity);
            holder.mTxtVwContactEmail=(TextView)view.findViewById(R.id.txtvwContactEmail);
            holder.mTxtVwContactUserID=(TextView)view.findViewById(R.id.txtvwContactUserId);

            holder.mImgVwStatusBeacon=(ImageView)view.findViewById(R.id.imgvwStatusBeacon);
            view.setTag(holder);
        }else{
            holder =(ViewHolder)view.getTag();
        }

        holder.mTxtVwContactName.setText("Name: " + contactsList.get(i).getDISPLAY_NAME());
        holder.mTxtVwContactLastActivity.setText("Last Activity: " + contactsList.get(i).getLAST_ACTIVITY());
        holder.mTxtVwContactEmail.setText("Email: " + contactsList.get(i).getEMAIL());
        holder.mTxtVwContactUserID.setText("User ID: " + contactsList.get(i).getUSER_ID());
        if(contactsList.get(i).getUSER_STATE().equalsIgnoreCase("1"))
            holder.mImgVwStatusBeacon.setImageDrawable(parentContext.getResources().getDrawable(R.drawable.green_dot));
        else{
            holder.mImgVwStatusBeacon.setImageDrawable(parentContext.getResources().getDrawable(R.drawable.red_dot_md));
        }



        return view;
    }


    private class ViewHolder{
        TextView mTxtVwContactName;
        TextView mTxtVwContactLastActivity;
        TextView mTxtVwContactEmail;
        TextView mTxtVwContactUserID;
        ImageView mImgVwStatusBeacon;



    }
}
