package algonquin.cst2335.leesandroid;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Dao
@Database(entities = {ChatMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {
    public abstract ChatMessageDAO cmDAO();
}
