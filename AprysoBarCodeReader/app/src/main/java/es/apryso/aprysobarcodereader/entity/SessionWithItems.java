package es.apryso.aprysobarcodereader.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Relation;

import java.util.List;

public class SessionWithItems {

    @Embedded
    public SessionEntity session;

    @Relation(parentColumn = "id", entityColumn = "sessionId", entity = SessionEntryEntity.class)
    public List<SessionEntryEntity> entryList;
}
