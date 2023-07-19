package algonquin.cst2335.leesandroid;


import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @ColumnInfo(name="message")
    String message;
    @ColumnInfo(name="timeSent")
    String timeSent;
    @ColumnInfo(name="SendorReceive")
    boolean isSentButton;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    public String getMessage(){
        return message;
    }

    public String getTimeSent(){
        return timeSent;
    }

    public boolean getIsSentButton(){
        return isSentButton;
    }

    public ChatMessage( String m, String t, boolean sent){

        message = m;
        timeSent =t;
        isSentButton = sent;
    }
    public ChatMessage(){}
}
