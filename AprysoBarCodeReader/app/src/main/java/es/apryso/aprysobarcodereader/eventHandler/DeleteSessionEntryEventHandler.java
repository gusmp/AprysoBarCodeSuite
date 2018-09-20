package es.apryso.aprysobarcodereader.eventHandler;

import android.view.View;

import es.apryso.aprysobarcodereader.activity.R;
import es.apryso.aprysobarcodereader.adapter.SessionManagerExpandableListAdapter;
import es.apryso.aprysobarcodereader.dao.DataBase;
import es.apryso.aprysobarcodereader.entity.SessionEntryEntity;
import es.apryso.aprysobarcodereader.utils.PopUpUtils;


public class DeleteSessionEntryEventHandler implements View.OnClickListener {

    private SessionEntryEntity sessionEntryEntity;
    private SessionManagerExpandableListAdapter adapter;

    public DeleteSessionEntryEventHandler(SessionEntryEntity sessionEntryEntity, SessionManagerExpandableListAdapter adapter) {
        this.sessionEntryEntity = sessionEntryEntity;
        this.adapter = adapter;
    }


    @Override
    public void onClick(View view) {

        DataBase.getInstance(view.getContext()).sessionEntryDao().delete(this.sessionEntryEntity);
        PopUpUtils.showPopup(view.getContext(), view.getContext().getString(R.string.session_manager_entry_delete_msg));
        adapter.updateData();
    }

}
