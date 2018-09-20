package es.apryso.aprysobarcodereader.eventHandler;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import es.apryso.aprysobarcodereader.activity.R;
import es.apryso.aprysobarcodereader.dao.DataBase;
import es.apryso.aprysobarcodereader.entity.SessionEntryEntity;
import es.apryso.aprysobarcodereader.utils.PopUpUtils;

public class SubstractReadEventHandler extends BaseEventHandler implements View.OnClickListener {

    private SessionEntryEntity sessionEntryEntity;
    private TextView session_manager_entry_reads;
    private ExpandableListView expandableListView;
    private int sessionIndex;

    public SubstractReadEventHandler(SessionEntryEntity sessionEntryEntity, TextView session_manager_entry_reads, ExpandableListView expandableListView, int sessionIndex) {

        this.sessionEntryEntity = sessionEntryEntity;
        this.session_manager_entry_reads = session_manager_entry_reads;
        this.expandableListView = expandableListView;
        this.sessionIndex = sessionIndex;
    }


    @Override
    public void onClick(View view) {

        if (sessionEntryEntity.numberOfItems == 1) {
            PopUpUtils.showPopup(view.getContext(), view.getResources().getString(R.string.session_manager_entry_low_reads));
        } else {
            sessionEntryEntity.numberOfItems--;
            DataBase.getInstance(view.getContext()).sessionEntryDao().update(this.sessionEntryEntity);

            this.session_manager_entry_reads.setText(String.valueOf(sessionEntryEntity.numberOfItems));

            View groupView = getGroupView(this.expandableListView, sessionIndex);
            if (groupView != null) {
                TextView session_manager_group_reads = (TextView) groupView.findViewById(R.id.session_manager_group_reads);
                session_manager_group_reads.setText(String.valueOf(Integer.valueOf(session_manager_group_reads.getText().toString()) - 1));
            }
        }
    }

}
