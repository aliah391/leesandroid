package algonquin.cst2335.leesandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.leesandroid.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.leesandroid.databinding.SentMessageBinding;
import algonquin.cst2335.leesandroid.databinding.ReceiveMessageBinding;
public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;

   // ArrayList<String> messages = new ArrayList<>();

    private ArrayList<ChatMessage> messages = new ArrayList<>();
    public ChatRoom(){}
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);



        messages = chatModel.messages.getValue();
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());
        }

        binding.SendButton.setOnClickListener(click -> {
            String message =binding.editText.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage cm = new ChatMessage(message, currentDateandTime, true);

            messages.add(cm);
            myAdapter.notifyItemInserted(messages.size() - 1);
//            myAdapter.notifyItemRemoved(messages.size()-1);
//            myAdapter.notifyDataSetChanged();
            binding.editText.setText("");

        });

        binding.receive.setOnClickListener(click -> {
            String message =binding.editText.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage cm = new ChatMessage(message, currentDateandTime, false);

            messages.add(cm);
            myAdapter.notifyItemInserted(messages.size() - 1);
//            myAdapter.notifyItemRemoved(messages.size()-1);
//            myAdapter.notifyDataSetChanged();
            binding.editText.setText("");


        });
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(),parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding rowBinding = ReceiveMessageBinding.inflate(getLayoutInflater(),parent, false);
                    return new MyRowHolder(rowBinding.getRoot());
                }


            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");
                ChatMessage message = messages.get(position);
                holder.messageText.setText(message.getMessage());
                holder.timeText.setText(message.getTimeSent());
                
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {

                if(messages.get(position).isSentButton == true){
                    return 0;
                }
                else{ return 1;}
            }
        });

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        ArrayList<ChatMessage> messages;
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }


}