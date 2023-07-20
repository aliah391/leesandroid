package algonquin.cst2335.leesandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.leesandroid.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.leesandroid.databinding.DetailsLayoutBinding;
import algonquin.cst2335.leesandroid.databinding.SentMessageBinding;
import algonquin.cst2335.leesandroid.databinding.ReceiveMessageBinding;
public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    MessageDatabase db;
    ChatMessageDAO mDAO;


    private ArrayList<ChatMessage> messages = new ArrayList<>();
    public ChatRoom(){}
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FrameLayout fragmentLocation = findViewById(R.id.fragmentLocation);
        boolean IAmTablet = fragmentLocation !=null;
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        chatModel.selectedMessage.observe(this, (newValue)->{
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newValue);

            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.add(R.id.fragmentLocation, chatFragment);
            tx.addToBackStack("hello");
            tx.commit();

        } );


         db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
         mDAO= db.cmDAO();

        chatModel.selectedMessage.observe(this, (newMessageValue)->{});
        Executor thread0 = Executors.newSingleThreadExecutor();
        thread0.execute(() -> {

                List<ChatMessage> allMessages = mDAO.getAllMessages();
                messages.addAll(allMessages);
        });


        messages = chatModel.messages.getValue();
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());
        }

        binding.SendButton.setOnClickListener(click -> {
            String message =binding.editText.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage cm = new ChatMessage( message, currentDateandTime, true);

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->{

                    cm.id = mDAO.insertMessage(cm);

            });


            messages.add(cm);

            myAdapter.notifyDataSetChanged();
            binding.editText.setText("");

        });

        binding.receive.setOnClickListener(click -> {
            String message =binding.editText.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage cm = new ChatMessage(message, currentDateandTime, false);

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->{

                cm.id = mDAO.insertMessage(cm);

            });
            messages.add(cm);
            myAdapter.notifyItemInserted(messages.size() - 1);

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
        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recyclerview.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }
    }

    protected class MyRowHolder extends RecyclerView.ViewHolder {
//        ArrayList<ChatMessage> messages  = new ArrayList<>();
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
//

           itemView.setOnClickListener(clk ->{
               int position = getAbsoluteAdapterPosition();
               ChatMessage selected = messages.get(position);
               chatModel.selectedMessage.postValue(selected);
           });

//                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
//
//                builder.setMessage("Do you want to delete this message " + messageText.getText() );
//                builder.setTitle("Question");
//                builder.setNegativeButton("No", (dialog, cl) -> { });
//
//                builder.setPositiveButton("yes", (dialog, cl)-> {
//
//                    ChatMessage toDelete = messages.get(position);
//
//                    Executor thread1 = Executors.newSingleThreadExecutor();
//                    thread1.execute(() ->
//                    {
//                        mDAO.deleteMessage(toDelete);
//                        messages.remove(position);
//
//                        runOnUiThread(()->{
//                        myAdapter.notifyDataSetChanged();});
//
//                        Snackbar.make(messageText, "You deleted message #" + position,Snackbar.LENGTH_LONG)
//                                .setAction("Undo", c -> {
//                                    messages.add(position, toDelete);
//                                    myAdapter.notifyItemInserted(position);
//                                })
//                                .show();
//                    });
//                });
//                builder.create().show();
//
//


            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }


}