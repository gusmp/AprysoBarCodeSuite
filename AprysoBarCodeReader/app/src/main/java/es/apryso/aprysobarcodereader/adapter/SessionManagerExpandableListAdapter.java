package es.apryso.aprysobarcodereader.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.apryso.aprysobarcodereader.activity.BaseActivity;
import es.apryso.aprysobarcodereader.activity.R;
import es.apryso.aprysobarcodereader.dao.DataBase;
import es.apryso.aprysobarcodereader.entity.SessionEntryEntity;
import es.apryso.aprysobarcodereader.entity.SessionWithItems;
import es.apryso.aprysobarcodereader.eventHandler.AddReadEventHandler;
import es.apryso.aprysobarcodereader.eventHandler.DeleteSessionEntryEventHandler;
import es.apryso.aprysobarcodereader.eventHandler.DeleteSessionEventHandler;
import es.apryso.aprysobarcodereader.eventHandler.SendSessionEventHandler;
import es.apryso.aprysobarcodereader.eventHandler.SubstractReadEventHandler;

public class SessionManagerExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private TextView emptyView;
    private BaseActivity baseActivity;
    private List<SessionWithItems> sessionList;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public SessionManagerExpandableListAdapter(Context context, TextView emptyView, BaseActivity baseActivity) {
        this.context = context;
        this.emptyView = emptyView;
        this.baseActivity = baseActivity;
        updateData();
    }

    public void updateData() {
        this.sessionList = DataBase.getInstance(this.context).sessionDao().loadAllSessionsWithItems();
        notifyDataSetChanged();

        if (this.sessionList.size() == 0) {
            this.emptyView.setVisibility(View.VISIBLE);
        } else {
            this.emptyView.setVisibility(View.GONE);
        }
    }

    /*
    private View getGroupView(ExpandableListView listView, int groupPosition) {

        long packedPosition = ExpandableListView.getPackedPositionForGroup(groupPosition);
        int flatPosition = listView.getFlatListPosition(packedPosition);
        int first = listView.getFirstVisiblePosition();

        if ( (flatPosition - first)  >= 0 )
            return listView.getChildAt(flatPosition - first);
        else
            return null;
    }
    */


    @Override
    public View getChildView(int sessionIndex, int sessionEntryIndex, boolean isLastChild, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.session_manager_entry_layout, null);
        }

        SessionEntryEntity see = this.sessionList.get(sessionIndex).entryList.get(sessionEntryIndex);

        TextView session_manager_entry_reads = (TextView) view.findViewById(R.id.session_manager_entry_reads);
        /*
        TextView session_manager_group_reads = null;
        View groupView = getGroupView((ExpandableListView) parent, sessionIndex);
        if (groupView != null) {
            session_manager_group_reads = (TextView) groupView.findViewById(R.id.session_manager_group_reads);
        }
        */

        ((TextView) view.findViewById(R.id.session_manager_entry_content)).setText(see.content);
        ((TextView) view.findViewById(R.id.session_manager_entry_barcode_format)).setText(see.barcodeFormat);
        session_manager_entry_reads.setText(String.valueOf(see.numberOfItems));
        ((TextView) view.findViewById(R.id.session_manager_entry_date)).setText(sdf.format(see.timestamp));


        ((Button) view.findViewById(R.id.session_manager_entry_delete)).setOnClickListener(
                new DeleteSessionEntryEventHandler(see, this));

        ((Button) view.findViewById(R.id.session_manager_entry_add)).setOnClickListener(
                new AddReadEventHandler(see, session_manager_entry_reads, (ExpandableListView) parent, sessionIndex));

        ((Button) view.findViewById(R.id.session_manager_entry_subtract)).setOnClickListener(
                new SubstractReadEventHandler(see, session_manager_entry_reads, (ExpandableListView) parent, sessionIndex));


        return view;
    }

    @Override
    public boolean isChildSelectable(int sessionIndex, int sessionEntryIndex) {
        return false;
    }

    @Override
    public int getChildrenCount(int sessionIndex) {
        return this.sessionList.get(sessionIndex).entryList.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public Object getChild(int sessionIndex, int sessionEntryIndex) {
        return this.sessionList.get(sessionIndex).entryList.get(sessionEntryIndex);
    }

    @Override
    public long getChildId(int sessionIndex, int sessionEntryIndex) {
        return this.sessionList.get(sessionIndex).entryList.get(sessionEntryIndex).id;
    }


    @Override
    public Object getGroup(int i) {
        return this.sessionList.get(i).session;
    }

    @Override
    public long getGroupId(int i) {
        return this.sessionList.get(i).session.id;
    }

    @Override
    public int getGroupCount() {
        return this.sessionList.size();
    }


    @Override
    public View getGroupView(int sessionIndex, boolean isExpanded, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.session_manager_group_layout, null);
        }

        ((TextView) view.findViewById(R.id.session_manager_group_title)).
                setText(sdf.format(this.sessionList.get(sessionIndex).session.timestamp));

        int total = 0;
        for(SessionEntryEntity e : this.sessionList.get(sessionIndex).entryList) {
            total += e.numberOfItems;
        }

        ((TextView) view.findViewById(R.id.session_manager_group_reads)).setText(String.valueOf(total));


        ((Button) view.findViewById(R.id.session_manager_group_delete)).setOnClickListener(
                new DeleteSessionEventHandler(this.sessionList.get(sessionIndex).session, this));

        ((Button) view.findViewById(R.id.session_manager_group_send)).setOnClickListener(
                new SendSessionEventHandler(this.sessionList.get(sessionIndex).session.id, this, this.context, this.baseActivity));


        return view;
    }

}
