package es.apryso.aprysobarcodereader.eventHandler;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.apryso.aprysobarcodereader.activity.BaseActivity;
import es.apryso.aprysobarcodereader.activity.R;
import es.apryso.aprysobarcodereader.adapter.SessionManagerExpandableListAdapter;
import es.apryso.aprysobarcodereader.service.SendService;
import es.apryso.aprysobarcodereader.service.ServiceFactory;
import es.apryso.aprysobarcodereader.utils.PopUpUtils;

public class SendSessionEventHandler implements View.OnClickListener {

    private Long sessionId;
    private SessionManagerExpandableListAdapter adapter;
    private Context context;
    private BaseActivity baseActivity;

    public SendSessionEventHandler(Long sessionId, SessionManagerExpandableListAdapter adapter, Context context, BaseActivity baseActivity) {

        this.sessionId = sessionId;
        this.adapter = adapter;
        this.context = context;
        this.baseActivity = baseActivity;
    }


    @Override
    public void onClick(View view) {

        this.baseActivity.disableScreen();
        SendService sendService =  (SendService) ServiceFactory.getInstance(ServiceFactory.SERVICE_INSTANCE.SEND_SERVICE);
        ((Button) view).setText(this.context.getString(R.string.session_manager_group_sending));


        sendService.sendSession(this.sessionId,
                (success) -> {

                    adapter.updateData();
                    ((Button) view).setText(this.context.getString(R.string.session_manager_group_send));

                    if (success == false) {
                        PopUpUtils.showPopup(this.context, this.context.getString(R.string.session_manager_eror_sending), Toast.LENGTH_LONG);
                    }

                    this.baseActivity.enableScreen();
                },
                this.context);
    }

}
