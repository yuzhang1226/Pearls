package com.example.pearls.ui.groups;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pearls.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateGroupFragment extends Fragment {

    private EditText editGroupName;
    private EditText editGroupIntro;
    private Button btnCreateGroup;
    private TextView textMessage;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        editGroupName = view.findViewById(R.id.editGroupName);
        editGroupIntro = view.findViewById(R.id.editGroupIntro);
        btnCreateGroup = view.findViewById(R.id.btnCreateGroup);
        textMessage = view.findViewById(R.id.textMessage);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        btnCreateGroup.setOnClickListener(v -> createGroup());

        return view;
    }

    private void createGroup() {
        String groupName = editGroupName.getText().toString().trim();
        String groupIntro = editGroupIntro.getText().toString().trim();

        if (groupName.isEmpty() || groupIntro.isEmpty()) {
            textMessage.setText("Please fill in both fields");
            textMessage.setVisibility(View.VISIBLE);
            return;
        }

        // Get the current user's ID
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "";

        // Create a new group object with the provided data
        Group group = new Group();
        group.setGroupName(groupName);
        group.setIntroduction(groupIntro);
        group.setMemberNumber("1"); // Assuming the creator is the first member
        group.setPostsNumber("0"); // Initially zero posts
        group.setUserId(userId); // Set the userId of the creator

        // Save to Firestore
        db.collection("groups").add(group)
                .addOnSuccessListener(documentReference -> {
                    textMessage.setText("Group created successfully!");
                    textMessage.setVisibility(View.VISIBLE);

                    // Navigate back to the group detail page after 4 seconds
                    new Handler().postDelayed(() -> {
                        // Navigate to the group detail page using the created group ID
                        Bundle bundle = new Bundle();
                        bundle.putString("groupId", documentReference.getId());
                        GroupDetailFragment groupDetailFragment = new GroupDetailFragment();
                        groupDetailFragment.setArguments(bundle);
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_main, groupDetailFragment, null)
                                .addToBackStack(null)
                                .commit();
                    }, 4000); // Delay for 4 seconds
                })
                .addOnFailureListener(e -> {
                    textMessage.setText("Error creating group: " + e.getMessage());
                    textMessage.setVisibility(View.VISIBLE);
                });
    }
}
