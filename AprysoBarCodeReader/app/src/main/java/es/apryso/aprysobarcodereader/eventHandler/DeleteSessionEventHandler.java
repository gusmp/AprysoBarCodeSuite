package es.apryso.aprysobarcodereader.eventHandler;

import android.view.View;

import es.apryso.aprysobarcodereader.activity.R;
import es.apryso.aprysobarcodereader.adapter.SessionManagerExpandableListAdapter;
import es.apryso.aprysobarcodereader.dao.DataBase;
import es.apryso.aprysobarcodereader.entity.SessionEntity;
import es.apryso.aprysobarcodereader.utils.PopUpUtils;


public class DeleteSessionEventHandler implements View.OnClickListener {

    private SessionEntity sessionEntity;
    private SessionManagerExpandableListAdapter adapter;

    public DeleteSessionEventHandler(SessionEntity sessionEntity, SessionManagerExpandableListAdapter adapter) {
        this.sessionEntity = sessionEntity;
        this.adapter = adapter;
    }


    @Override
    public void onClick(View view) {

        DataBase.getInstance(view.getContext()).sessionDao().delete(this.sessionEntity);
        PopUpUtils.showPopup(view.getContext(), view.getContext().getString(R.string.session_manager_group_delete_msg));
        adapter.updateData();
    }

}
