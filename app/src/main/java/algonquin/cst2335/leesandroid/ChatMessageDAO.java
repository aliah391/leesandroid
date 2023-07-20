package algonquin.cst2335.leesandroid;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ChatMessageDAO {

    @Insert
    public long insertMessage(ChatMessage messageToInsert);

    @Query("Select * from ChatMessage")
    public List<ChatMessage> getAllMessages();

    @Delete
    public void deleteMessage(ChatMessage MessagesToDelete);













}


