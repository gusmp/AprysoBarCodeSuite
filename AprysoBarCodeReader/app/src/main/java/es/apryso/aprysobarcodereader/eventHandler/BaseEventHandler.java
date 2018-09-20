package es.apryso.aprysobarcodereader.eventHandler;

import android.view.View;
import android.widget.ExpandableListView;


public abstract class BaseEventHandler {

    protected View getGroupView(ExpandableListView listView, int groupPosition) {

        long packedPosition = ExpandableListView.getPackedPositionForGroup(groupPosition);
        int flatPosition = listView.getFlatListPosition(packedPosition);
        int first = listView.getFirstVisiblePosition();

        if ( (flatPosition - first)  >= 0 )
            return listView.getChildAt(flatPosition - first);
        else
            return null;
    }
}
