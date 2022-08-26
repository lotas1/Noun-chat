package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.university.chat.R;
import com.university.chat.data.model.ChatModel;
import com.university.chat.ui.adapter.RecyclerViewAdapterChat;
import com.university.chat.ui.viewModel.GeneralChatViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GeneralChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout linearLayoutUser, linearLayoutAdmin;
    private TextView textViewMessage, textViewGroupInfoMessage, textViewUsernameReply, textViewMessageReply, textViewGroupName, textViewPinMessage;
    private ImageView imageViewGroupInfoEdit, imageViewFullDisplay, imageViewDeletePinMessage;
    private ImageButton imageButtonCloseReplyChat, imageButtonSendUserData, imageButtonChatImageChooser, imageButtonDeleteChatImage, imageButtonScrollDown;
    private LinearLayout linearLayoutReplyMessageGeneralChat, linearLayoutPinMessageParentLayout;
    private ConstraintLayout constraintLayoutImageFullDisplayParentLayout;
    private EditText editTextUserMessage;
    private RecyclerView recyclerViewChatData;
    private RecyclerViewAdapterChat recyclerViewAdapterChat;
    private MenuItem item1;
    private Query queryBan, queryUser, queryChatMessages;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference groupRef, userRef, chatRef;
    boolean isUserBan, isUserAdmin;
    private String groupName, groupKey, groupImage, username, replyUsername, replyMessage, messageKey, textToCopy, messageUserId, chatImage, highlightedMessage;

    int usersCount;
    private boolean isGroupMute;
    private static final int PICK_IMAGE = 100;
    private boolean isChatImageSelected, isReplyChatSelected = false;
    private Uri uriChatImage;
    private GeneralChatViewModel generalChatViewModel;
    private StorageReference storageRef, chatImageStorageRef;
    private FirebaseStorage storage;
    private UploadTask uploadTask;
    private int replyPosition;//, pinMessagePosition = 0;
    private String pinMessagePosition;
    private ActionMode actionMode;
    private Map<String, Object> replyMap;
    private int highlightedPosition = -10;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_chat);
        // retrieve an instance of your database
        database = FirebaseDatabase.getInstance();
        // location reference in database
        groupRef = database.getReference("Groups");
        userRef = database.getReference("Users");
        chatRef = database.getReference();
        // instance of firebase storage
        storage = FirebaseStorage.getInstance();
        // get current user details
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Create a storage reference from our app
        storageRef = storage.getReference();
        // instantiate view model
        generalChatViewModel = new ViewModelProvider(this).get(GeneralChatViewModel.class);

        sharedPref = GeneralChatActivity.this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // instantiate views
        toolbar = findViewById(R.id.toolbar_general_chat);
        linearLayoutUser = findViewById(R.id.linearLayout_user_generalChat);
        linearLayoutAdmin = findViewById(R.id.linearLayout_adminOnly_generalChat);
        textViewMessage = findViewById(R.id.textView_message_generalChat);
        item1 = toolbar.getMenu().findItem(R.id.muteGroup);
        editTextUserMessage = findViewById(R.id.edittext_inputMessage_generalChat);
        imageButtonSendUserData = findViewById(R.id.imageButton_sendMessage_generalChat);
        recyclerViewChatData = findViewById(R.id.recyclerView_generalChat);
        imageButtonChatImageChooser = findViewById(R.id.imageButton_imageChooser_generalChat);
        imageViewFullDisplay = findViewById(R.id.imageView_viewSelectedImage_generalChat);
        constraintLayoutImageFullDisplayParentLayout = findViewById(R.id.constrainLayout_imageViewDisplay_parentLayout);
        imageButtonDeleteChatImage = findViewById(R.id.imageButton_delete_Message_generalChat);
        linearLayoutReplyMessageGeneralChat = findViewById(R.id.linearLayout_replyMessage_generalChat);
        imageButtonCloseReplyChat = findViewById(R.id.imageButton_CloseReply_generalChat);
        textViewUsernameReply = findViewById(R.id.textView_usernameReply_generalChat);
        textViewMessageReply = findViewById(R.id.textView_messageReply_generalChat);
        linearLayoutPinMessageParentLayout = findViewById(R.id.linearLayout_PinMessageParentLayout_generalChat);
        textViewPinMessage = findViewById(R.id.textView_PinMessage_generalChat);
        imageViewDeletePinMessage = findViewById(R.id.imageView_ClosePinMessage_generalChat);
        imageButtonScrollDown = findViewById(R.id.imageButton_scrollDown_generalChat);

        // instance of bundle
        Bundle b = getIntent().getExtras();

        // variables holds value for is admin only
        //isAdminOnly = b.getBoolean("adminOnly");
        isUserBan = b.getBoolean("isUserBan");
        isUserAdmin = b.getBoolean("isUserAdmin");
        groupName = b.getString("groupName");
        usersCount = b.getInt("usersCount");
        groupKey = b.getString("groupKey");

        // close activity on click
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        // on click opens all users list.
        toolbar.setOnClickListener(v -> {
            // check if user is admin
            if (isUserAdmin) {
                // navigate to user list.
                Intent intent = new Intent(GeneralChatActivity.this, GroupMemberActivity.class);
                startActivity(intent);
            }
        });

        // set toolbar title
        toolbar.setTitle(groupName);
        toolbar.setSubtitle(String.valueOf(usersCount).concat(" members"));

        // toolbar menu navigation
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.groupInfo) {
                // display group info or description.
                groupInfo();
            } else if (item.getItemId() == R.id.muteGroup) {
                // check if user is admin
                if (isUserAdmin) {
                    if (isGroupMute) {
                        // mute group
                        muteOrUnMuteGroup("UnMute", "Do you want to un-mute group?", "UnMute", false);
                    } else {
                        //UnMute group.
                        muteOrUnMuteGroup("Mute", "Do you want to mute group?", "Mute", true);
                    }
                } else {
                    // notify user not admin
                    showAlertDialog(GeneralChatActivity.this, "Admin Feature", "Only admin can access this features.");
                }
            } else if (item.getItemId() == R.id.deleteGroup) {
                // check if user is admin
                if (isUserAdmin) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Delete Group")
                            .setMessage("Do you want to delete group?")
                            .setNegativeButton("cancel", (dialog, which) -> {

                            })
                            .setPositiveButton("delete", (dialog, which) -> {
                                // delete group
                                deleteGroup(groupImage);
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // notify user not admin
                    showAlertDialog(GeneralChatActivity.this, "Admin Feature", "Only admin can access this features.");
                }
            }
            return false;
        });

        // prevent user from sending images in chat
        if (!isUserAdmin) {
            // set visibility gone for image chat view temporal display gone on starting of the application.
            imageButtonChatImageChooser.setVisibility(View.GONE);
            // set visibility to gone for closing image view of pin message for those who are not admin
            imageViewDeletePinMessage.setVisibility(View.GONE);
        }

        // init onCreate
        init(GeneralChatActivity.this);

        /** close reply chat if not needed again by the user**/
        imageButtonCloseReplyChat.setOnClickListener(v -> {
            isReplyChatSelected = false;
            linearLayoutReplyMessageGeneralChat.setVisibility(View.GONE);
        });

        // observer for reply message in chat
        generalChatViewModel.getUserReplyInfo().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> map) {
                replyMap = map;
                actionMode = startSupportActionMode(actionModeCallback);
            }
        });

        // observer for getting replied message position
        generalChatViewModel.getReplyPositionLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer replyPosition) {
                // scroll to the reply message position.
                recyclerViewChatData.smoothScrollToPosition(replyPosition);
            }
        });

        // on click for choosing image to send along user message
        imageButtonChatImageChooser.setOnClickListener(v -> {
            imageChooser();

        });
        // on click delete image selected for sending in chat messages.
        imageButtonDeleteChatImage.setOnClickListener(v -> {
            // remove chat image and set isChatImageChooser & isChatImageSelected to false
            constraintLayoutImageFullDisplayParentLayout.setVisibility(View.GONE);
            isChatImageSelected = false;
        });

        // onclick sends sends user message to realtime firebase
        imageButtonSendUserData.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editTextUserMessage.getText())) {

            } else {
                if (user != null) {
                    String message = editTextUserMessage.getText().toString();
                    if (isChatImageSelected) {
                        // sends user message with image attached.
                        sendMessageWithImage(GeneralChatActivity.this, uriChatImage, message);
                    } else {
                        // send user message without image attached.
                        sendMessageWithOutImage(message);
                        //if (BadWordsFilter.filterText(message)){
                        //    Toast.makeText(this, "This message was blocked because a bad word was found.", Toast.LENGTH_SHORT).show();
                        //}else{
                        //    // send user message without image attached.
                        //    sendMessageWithOutImage(message);
                        //}

                    }
                }
            }
        });

        // scroll down to last position of recycler view
        imageButtonScrollDown.setOnClickListener(v -> recyclerViewChatData.smoothScrollToPosition(recyclerViewChatData.getAdapter().getItemCount()-1));

    }

    // contextual toolbar
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_action_bar_chat, menu);
            // if selected message to be deleted is not sent by the current user disable visibility of delete menu item
            // Only admin can delete all messages.
            if (!isUserAdmin) {
                MenuItem shareItem1 = menu.findItem(R.id.pinMessage);
                shareItem1.setVisible(false);
                if (!messageUserId.equals(user.getUid())) {
                    MenuItem shareItem2 = menu.findItem(R.id.delete);
                    shareItem2.setVisible(false);
                }
            }
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.reply) {
                // updates ui with the selected reply message
                selectedReplyMessage(replyMap);
                // reset highlighted position to remove highlight from row.
                highlightedPosition = -10;
                recyclerViewAdapterChat.notifyDataSetChanged();
                mode.finish(); // Action picked, so close the CAB
                return true;
            } else if (item.getItemId() == R.id.delete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GeneralChatActivity.this);
                builder.setTitle("Delete message")
                        .setMessage("Are you sure you want to delete this message for everyone?")
                        .setNegativeButton("cancel", (dialog, which) -> {

                        })
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // deletes selected message
                            deleteSelectedMessage(chatImage);

                            // reset highlighted position to remove highlight from row.
                            highlightedPosition = -10;
                            recyclerViewAdapterChat.notifyDataSetChanged();
                            mode.finish(); // Action picked, so close the CAB

                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            } else if (item.getItemId() == R.id.pinMessage) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GeneralChatActivity.this);
                builder.setTitle("Pin Message message")
                        .setMessage("Pin this message in the group?")
                        .setNegativeButton("cancel", (dialog, which) -> {

                        })
                        .setPositiveButton("Pin", (dialog, which) -> {
                            // pin selected message
                            pinMessage(highlightedMessage);
                            //recyclerViewAdapterChat.notifyDataSetChanged();
                            mode.finish(); // Action picked, so close the CAB

                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (item.getItemId() == R.id.copy) {
                setClipboard(GeneralChatActivity.this, textToCopy);
                Toast.makeText(GeneralChatActivity.this, "Message copied", Toast.LENGTH_SHORT).show();
                // reset highlighted position to remove highlight from row.
                highlightedPosition = -10;
                recyclerViewAdapterChat.notifyDataSetChanged();
                mode.finish(); // Action picked, so close the CAB
                return true;
            }
            return false;
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // reset highlighted position to remove highlight from row (on close of contextual action bar).
            highlightedPosition = -10;
            recyclerViewAdapterChat.notifyDataSetChanged();
            actionMode = null;

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // get current position of recycler view
        int pagePosition = ((LinearLayoutManager) recyclerViewChatData.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        //store position locally in shared preferences.
        editor.putInt(groupKey, pagePosition);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerViewAdapterChat.stopListening();
    }

    private void selectedReplyMessage(Map<String, Object> map) {
        isReplyChatSelected = true;
        replyUsername = (String) map.get("replyUsername");
        replyMessage = (String) map.get("replyMessage");
        replyPosition = (int) map.get("replyPosition");
        // set reply layout visibility VISIBLE
        linearLayoutReplyMessageGeneralChat.setVisibility(View.VISIBLE);
        textViewUsernameReply.setText(replyUsername);
        textViewMessageReply.setText(replyMessage);
    }

    private void pinMessage(String message) {
        groupRef.child(groupKey).child(getStringResource(R.string.pinMessage)).setValue(message);
        groupRef.child(groupKey).child(getStringResource(R.string.pinMessagePosition)).setValue(highlightedPosition);
    }

    private void readPinMessage() {
        groupRef.child(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(getStringResource(R.string.pinMessage)).exists()) {
                    // set visible the parent layout for pin message.
                    linearLayoutPinMessageParentLayout.setVisibility(View.VISIBLE);
                    // get pin message info.
                    pinMessagePosition = String.valueOf(snapshot.child(getStringResource(R.string.pinMessagePosition)).getValue());
                    String pinMessage = Objects.requireNonNull(snapshot.child(getStringResource(R.string.pinMessage)).getValue()).toString();
                    // update pin message info to ui
                    textViewPinMessage.setText(pinMessage);
                } else {
                    // set Gone the parent layout for pin message if pin message does not exist.
                    linearLayoutPinMessageParentLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // delete pin message
        imageViewDeletePinMessage.setOnClickListener(v -> deletePinMessage());
    }

    private void deletePinMessage() {
        groupRef.child(groupKey).child(getStringResource(R.string.pinMessage)).removeValue();
        groupRef.child(groupKey).child(getStringResource(R.string.pinMessagePosition)).removeValue();
    }

    private void deleteSelectedMessage(String chatImage) {
        // create instance of firebase database & database reference.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // reference for News/NounUpdate path.
        DatabaseReference myRef = database.getReference(groupKey);
        if (chatImage != null) {
            // delete group image first
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(chatImage);
            storageReference.delete().addOnSuccessListener(unused -> {
                // delete message
                myRef.child(messageKey).removeValue();
            });
        } else {
            // delete message
            myRef.child(messageKey).removeValue();
        }
    }

    private void sendMessageWithImage(Context context, Uri uri, String messageChat) {
        // instance of progress dialog
        ProgressDialog progressBar = new ProgressDialog(context);
        // Create a reference to group profile pics
        chatImageStorageRef = storageRef.child(groupKey).child(uri.getLastPathSegment());
        uploadTask = chatImageStorageRef.putFile(uri);

        uploadTask.addOnFailureListener(e -> {
            // update user on failure
            showAlertDialog(context, "Error", "Check your network");
        }).addOnProgressListener(snapshot -> {
            // update ui with progress bar
            progressBar.setCancelable(false);
            progressBar.setMessage("Sending...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();
        }).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return chatImageStorageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // gets image download url
                    Uri downloadUri = task.getResult();

                    String uid, key;
                    uid = user.getUid();
                    key = chatRef.push().getKey();

                    Map<String, Object> map = new HashMap<>();
                    map.put(getStringResource(R.string.username), username);
                    map.put(getStringResource(R.string.message), messageChat);
                    map.put(getStringResource(R.string.time), getDate());
                    map.put(getStringResource(R.string.userId), uid);
                    map.put(getStringResource(R.string.key), key);
                    map.put(getStringResource(R.string.image), downloadUri.toString());
                    if (isReplyChatSelected) {
                        map.put(getStringResource(R.string.replyUsername), replyUsername);
                        map.put(getStringResource(R.string.replyMessage), replyMessage);
                        map.put(getStringResource(R.string.replyPosition), replyPosition);
                    }
                    assert key != null;
                    chatRef.child(groupKey).child(key).setValue(map).addOnSuccessListener(unused -> {
                        // Connecting Adapter class with the Recycler view
                        // Function to tell the app to start getting data from database
                        // refreshes adapter for new messages.
                        recyclerViewChatData.setAdapter(recyclerViewAdapterChat);
                        recyclerViewAdapterChat.startListening();
                    });
                    map.clear();
                    // update last message sender.
                    groupRef.child(groupKey).child(getStringResource(R.string.sender)).setValue(username);
                    groupRef.child(groupKey).child(getStringResource(R.string.lastMessage)).setValue(messageChat);
                    // clear view.
                    editTextUserMessage.setText(null);
                    // remove chat image and set isChatImageChooser & isChatImageSelected to false
                    constraintLayoutImageFullDisplayParentLayout.setVisibility(View.GONE);
                    isChatImageSelected = false;
                    linearLayoutReplyMessageGeneralChat.setVisibility(View.GONE);
                    isReplyChatSelected = false;
                    replyUsername = null;
                    replyMessage = null;
                    replyPosition = 0;
                    // dismiss progress bar
                    progressBar.dismiss();

                } else {
                    // Handle failures
                    // ...
                }
            });
        });
    }

    private void sendMessageWithOutImage(String message) {
        String uid, key;

        uid = user.getUid();
        key = chatRef.push().getKey();

        Map<String, Object> map = new HashMap<>();
        map.put(getStringResource(R.string.username), username);
        map.put(getStringResource(R.string.message), message);
        map.put(getStringResource(R.string.time), getDate());
        map.put(getStringResource(R.string.userId), uid);
        map.put(getStringResource(R.string.key), key);
        if (isReplyChatSelected) {
            map.put(getStringResource(R.string.replyUsername), replyUsername);
            map.put(getStringResource(R.string.replyMessage), replyMessage);
            map.put(getStringResource(R.string.replyPosition), replyPosition);
        }
        assert key != null;
        chatRef.child(groupKey).child(key).setValue(map)
                .addOnSuccessListener(unused -> {
                    // Connecting Adapter class with the Recycler view
                    // Function to tell the app to start getting data from database
                    // refreshes adapter for new messages.
                    recyclerViewChatData.setAdapter(recyclerViewAdapterChat);
                    recyclerViewAdapterChat.startListening();
                });
        map.clear();
        // update last message sender for ui update of group list.
        groupRef.child(groupKey).child(getStringResource(R.string.sender)).setValue(username);
        groupRef.child(groupKey).child(getStringResource(R.string.lastMessage)).setValue(message);
        // clear view.
        editTextUserMessage.setText(null);
        linearLayoutReplyMessageGeneralChat.setVisibility(View.GONE);
        isReplyChatSelected = false;
        replyUsername = null;
        replyMessage = null;
        replyPosition = 0;
    }

    private void chatRecyclerView(Context context) {
        // To display the Recycler view linearly
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerViewChatData.setLayoutManager(layoutManager);
        // TODO recyclerViewChatData.setHasFixedSize(true);

        // firebase location path
        queryChatMessages = FirebaseDatabase.getInstance().getReference(groupKey);
        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<ChatModel> options = new FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(queryChatMessages, ChatModel.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        recyclerViewAdapterChat = new RecyclerViewAdapterChat(options, GeneralChatActivity.this) {

            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolderSent holder, int position, @NonNull ChatModel model) {
                super.onBindViewHolder(holder, position, model);
                // on click
                holder.itemView.setOnClickListener(v -> {
                    // remove highlighted area on click.
                    if (highlightedPosition >= 0) {
                        // close contextual action bar
                        actionMode.finish();
                        highlightedPosition = -10;
                        notifyDataSetChanged();
                    }

                });
                // on long click on item view.
                holder.itemView.setOnLongClickListener(v -> {

                    // remove any previous contextual action bar created for highlighted position.
                    if (highlightedPosition >= 0) {
                        // close contextual action bar
                        actionMode.finish();
                    }
                    // get chat image if any
                    if (model.getImage() != null) {
                        chatImage = model.getImage();
                    } else {
                        chatImage = null;
                    }
                    // get highlighted message for pinning
                    highlightedMessage = model.getMessage();
                    // get message user id
                    messageUserId = model.getUserId();
                    // gets position selected by user to highlight
                    highlightedPosition = holder.getLayoutPosition();
                    // get selected message push key for deleting message purpose
                    messageKey = model.getKey();
                    // get message text for copy
                    textToCopy = model.getMessage();
                    // get user message info a user wants to reply to.
                    generalChatViewModel.setUserReplyInfo(model.getUsername(), model.getMessage(), holder.getLayoutPosition());
                    notifyDataSetChanged();
                    return false;
                });

                // checks for position selected for highlight and highlight it.
                if (highlightedPosition == position) {
                    // disable reply parent layout from clicking
                    holder.cardViewReplyInfo.setClickable(false);
                    // set highlighted row background to grey
                    holder.itemView.setBackgroundColor(context.getResources().getColor(com.university.theme.R.color.dark_grey));
                } else {
                    // set highlighted row background to selectableItemBackground
                    TypedValue outValue = new TypedValue();
                    context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                    holder.itemView.setBackgroundResource(outValue.resourceId);

                    // if position highlighted, first click on parent reply layout disables it from being clickable.
                    if (highlightedPosition >= 0) {
                        // disable reply parent layout from clicking
                        holder.cardViewReplyInfo.setClickable(false);
                    }
                }
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                // Connecting Adapter class with the Recycler view
                // Function to tell the app to start getting data from database
                //recyclerViewChatData.setAdapter(recyclerViewAdapterChat);
                // read recycler page position from shared preferences.
                int recyclerPagePosition = sharedPref.getInt(groupKey, 0);
                if (recyclerPagePosition != 0) {
                    // scroll to last position of recycler view by the user.
                    recyclerViewChatData.scrollToPosition(recyclerPagePosition);
                    //store position locally in shared preferences.
                    editor.putInt(groupKey, 0);
                    editor.apply();
                }
            }


        };
        // Connecting Adapter class with the Recycler view
        // Function to tell the app to start getting data from database
        recyclerViewChatData.setAdapter(recyclerViewAdapterChat);
        recyclerViewAdapterChat.startListening();


    }

    private void init(Context context) {
        // set visibility for pin message gone on start of application
        //linearLayoutPinMessageParentLayout.setVisibility(View.GONE);
        // on click on pin message recycler view scrolls to the pin message position
        linearLayoutPinMessageParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewChatData.smoothScrollToPosition(Integer.parseInt(pinMessagePosition));
            }
        });
        // read pin message from firebase
        readPinMessage();
        // set reply layout visibility gone by default
        linearLayoutReplyMessageGeneralChat.setVisibility(View.GONE);
        // for recycler view updating user chat.
        chatRecyclerView(context);
        // read groups from firebase.
        groupRef.child(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get group mute status
                if (snapshot.child(getStringResource(R.string.adminOnly)).exists()) {
                    isGroupMute = (boolean) snapshot.child(getStringResource(R.string.adminOnly)).getValue();
                    // checks isGroupMute and update user messaging ui.
                    checkIsGroupAdminOnly(isGroupMute);
                }
                // get group image string url (if any)
                if (snapshot.child(getStringResource(R.string.groupImage)).exists()) {
                    groupImage = snapshot.child(getStringResource(R.string.groupImage)).getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef.child(user.getUid()).child(getStringResource(R.string.username)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username = snapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // compare the requestCode with the
        // PICK_IMAGE constant
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                uriChatImage = data.getData();
                // stores true if image is selected
                isChatImageSelected = true;
                constraintLayoutImageFullDisplayParentLayout.setVisibility(View.VISIBLE);
                imageViewFullDisplay.setImageURI(uriChatImage);
            } else {
                // stores false if image is not selected
                isChatImageSelected = false;
                constraintLayoutImageFullDisplayParentLayout.setVisibility(View.GONE);
            }

        }
    }

    private void deleteGroup(String groupImages) {
        // check if group has image before deleting group
        if (groupImages != null) {
            // delete group image first
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(groupImages);
            storageReference.delete().addOnSuccessListener(unused -> {
                // File deleted successfully
                // delete group
                groupRef.child(groupKey).removeValue();
                // delete group chat messages.
                chatRef.child(groupKey).removeValue();

                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error: can't delete group", Toast.LENGTH_SHORT).show();
            });
        } else {
            // delete group
            groupRef.child(groupKey).removeValue();
            // delete group chat messages.
            chatRef.child(groupKey).removeValue();
            finish();
        }

    }

    private void groupInfo() {
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.group_info_custom_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        // instantiate views
        textViewGroupInfoMessage = customLayout.findViewById(R.id.textView_groupInfo);
        imageViewGroupInfoEdit = customLayout.findViewById(R.id.imageView_edit_groupInfo);
        textViewGroupName = customLayout.findViewById(R.id.textView_groupName_groupInfo);

        // check if user is admin
        if (!isUserAdmin) {
            // notify user not admin and set edit group button visibility to GONE
            imageViewGroupInfoEdit.setVisibility(View.GONE);
        }

        // read data(group info) from firebase
        queryUser = FirebaseDatabase.getInstance().getReference("Groups");
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String groupName = snapshot.child(groupKey).child(getStringResource(R.string.groupName)).getValue().toString();
                textViewGroupName.setText(groupName);
                if (snapshot.child(groupKey).child(getStringResource(R.string.groupInfo)).exists()) {
                    // get group info or description from database and update user ui.
                    String groupInfo = snapshot.child(groupKey).child(getStringResource(R.string.groupInfo)).getValue().toString();
                    textViewGroupInfoMessage.setText(groupInfo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // on click on edit image icon open group info edit activity.
        imageViewGroupInfoEdit.setOnClickListener(v -> {
            Intent intent = new Intent(GeneralChatActivity.this, GroupInfoEditActivity.class);
            intent.putExtra("groupKey", groupKey);
            startActivity(intent);
        });
        // show dialog
        dialog.show();

    }

    private void checkIsGroupAdminOnly(boolean isGroupMute) {
        // checks if the group isAminOnly or not(for all users to chat)
        if (isGroupMute) {
            // update appbar menu
            item1.setTitle("UnMute");
            // checks if user is an admin and not also ban for violation
            if (isUserAdmin) {
                // checks if user is not ban for violation
                if (isUserBan) {
                    linearLayoutUser.setVisibility(View.GONE);
                    // updates user for rule violation
                    textViewMessage.setText("For Violation of rules you have been ban from sending messages");
                    linearLayoutAdmin.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutUser.setVisibility(View.VISIBLE);
                    linearLayoutAdmin.setVisibility(View.GONE);
                }
            } else {
                linearLayoutUser.setVisibility(View.GONE);
                linearLayoutAdmin.setVisibility(View.VISIBLE);
            }

        } else {
            // update appbar menu
            item1.setTitle("Mute");
            // checks if user is not ban for violation
            if (isUserBan) {
                linearLayoutUser.setVisibility(View.GONE);
                // updates user for rule violation
                textViewMessage.setText("For Violation of rules you have been ban from sending messages");
                linearLayoutAdmin.setVisibility(View.VISIBLE);
            } else {
                linearLayoutUser.setVisibility(View.VISIBLE);
                linearLayoutAdmin.setVisibility(View.GONE);
            }

        }
    }

    // this function is triggered when the Select Image Button is clicked
    private void imageChooser() {
        // create an instance of the intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
    }

    private void muteOrUnMuteGroup(String title, String message, String positiveButton, boolean muteorunmute) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton("cancel", (dialog, which) -> {

                })
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    groupRef.child(groupKey).child(getStringResource(R.string.adminOnly)).setValue(muteorunmute);

                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("ok", (dialog, which) -> {

                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // returns string from resources
    private String getStringResource(int string) {
        return getResources().getString(string);
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    // clears textInput layout error
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout) {
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }

    // checks if autocomplete text view is empty.
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return TextUtils.isEmpty(autoCompleteTextView.getEditableText());
    }

    // return date in string.
    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d-hh:mmaaa");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }
}