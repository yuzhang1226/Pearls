package com.example.pearls.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pearls.databinding.FragmentGroupsDetailsBinding;
import com.example.pearls.models.Post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailFragment extends Fragment {

    private FragmentGroupsDetailsBinding binding;
    private FirebaseFirestore db;
    private String groupId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGroupsDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            groupId = getArguments().getString("groupId");
            loadGroupDetails();
            loadGroupPosts();
        }

        return root;
    }

    // Load group details from Firestore
    private void loadGroupDetails() {
        db.collection("groups").document(groupId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                binding.groupName.setText(task.getResult().getString("group_name"));
                binding.groupIntroduction.setText(task.getResult().getString("introduction"));
                binding.memberCount.setText(String.valueOf(task.getResult().getLong("memberCount")));
            }
        });
    }

    // Load group posts from Firestore
    private void loadGroupPosts() {
        db.collection("groups").document(groupId).collection("posts")
                .orderBy("timestamp")
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        List<Post> posts = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming you have a Post constructor that takes a DocumentSnapshot
                            posts.add(new Post(document));
                        }

                        // Set up the RecyclerView
                        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        PostAdapter adapter = new PostAdapter(posts);
                        binding.postsRecyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
