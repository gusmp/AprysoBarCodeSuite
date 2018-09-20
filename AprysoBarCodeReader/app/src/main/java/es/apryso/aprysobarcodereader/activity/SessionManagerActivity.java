package es.apryso.aprysobarcodereader.activity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ExpandableListView;
import android.widget.TextView;
import es.apryso.aprysobarcodereader.adapter.SessionManagerExpandableListAdapter;


public class SessionManagerActivity extends BaseActivity {

    private ExpandableListView session_manager_expandable_list_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_manager);

        this.session_manager_expandable_list_view = (ExpandableListView) findViewById(android.R.id.list);
        this.session_manager_expandable_list_view.setAdapter(new SessionManagerExpandableListAdapter(getApplicationContext(), (TextView) findViewById(R.id.empty), this));
   }

    @Override
    public void onBackPressed() {

        if (this.disableBackButton == false) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}
