package com.example.pearls.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.pearls.databinding.FragmentGroupsBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.pearls.R;

import java.util.HashMap;
import java.util.Map;

public class GroupsFragment extends Fragment {

    private FragmentGroupsBinding binding;
    private FirebaseFirestore db;
    private String currentUserId; // Add currentUserId field

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGroupsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        currentUserId = "currentUserId"; // Replace with actual user ID retrieval

        // Setup search functionality
        binding.btnSearch.setOnClickListener(v -> {
            String queryText = binding.searchGroups.getText().toString().trim();
            if (!queryText.isEmpty()) {
                searchGroup(queryText);
            } else {
                Toast.makeText(getContext(), "Please enter a group name", Toast.LENGTH_SHORT).show();
            }
        });

        // Button to navigate to Create Group
        binding.btnCreateGroup.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.navigation_create_group);
        });

        // Load hot group discussions
        loadHotestGroupDiscussion();

        // Load recommended groups
        loadRecommendedGroups();

        // Load user-specific groups
        loadMyGroups();

        return root;
    }

    // Search Firestore for groups by name
    private void searchGroup(String queryText) {
        db.collection("groups")
                .whereEqualTo("group_name", queryText)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0); // Navigate to first found group
                        navigateToGroupDetail(document.getId());
                    } else {
                        Toast.makeText(getContext(), "No group found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error searching groups: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Load the hottest group discussions
    private void loadHotestGroupDiscussion() {
        db.collection("groups")
                .orderBy("visitorsCount", Query.Direction.DESCENDING)
                .orderBy("responsesCount", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        binding.groupName.setText(document.getString("groupName"));
                        binding.postName.setText(document.getString("postTitle"));

                        // Click listener for group name and post name
                        View.OnClickListener listener = v -> navigateToGroupDetail(document.getId());
                        binding.groupName.setOnClickListener(listener);
                        binding.postName.setOnClickListener(listener);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error loading hottest group: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Load recommended groups randomly from Firestore
    private void loadRecommendedGroups() {
        db.collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                int groupCounter = 1;
                for (DocumentSnapshot document : task.getResult()) {
                    if (groupCounter <= 6) { // Limit to 6 groups
                        setRecommendedGroupButton(groupCounter, document);
                        groupCounter++;
                    }
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error loading recommended groups: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setRecommendedGroupButton(int index, DocumentSnapshot document) {
        switch (index) {
            case 1:
                binding.btnViewGroup1.setText(document.getString("groupName"));
                binding.btnViewGroup1.setOnClickListener(v -> joinGroup(document.getId()));
                break;
            case 2:
                binding.btnViewGroup2.setText(document.getString("groupName"));
                binding.btnViewGroup2.setOnClickListener(v -> joinGroup(document.getId()));
                break;
            case 3:
                binding.btnViewGroup3.setText(document.getString("groupName"));
                binding.btnViewGroup3.setOnClickListener(v -> joinGroup(document.getId()));
                break;
            case 4:
                binding.btnViewGroup4.setText(document.getString("groupName"));
                binding.btnViewGroup4.setOnClickListener(v -> joinGroup(document.getId()));
                break;
            case 5:
                binding.btnViewGroup5.setText(document.getString("groupName"));
                binding.btnViewGroup5.setOnClickListener(v -> joinGroup(document.getId()));
                break;
            case 6:
                binding.btnViewGroup6.setText(document.getString("groupName"));
                binding.btnViewGroup6.setOnClickListener(v -> joinGroup(document.getId()));
                break;
        }
    }

    // Join a group and display a congratulatory message
    private void joinGroup(String groupId) {
        Map<String, Object> member = new HashMap<>();
        member.put("userId", currentUserId); // Use actual user ID
        db.collection("groups").document(groupId).collection("members")
                .add(member)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Congratulations! You are now part of this group.", Toast.LENGTH_SHORT).show();
                    navigateToGroupDetail(groupId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error joining group: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Load groups related to the current user
    private void loadMyGroups() {
        db.collection("groups").whereArrayContains("members", currentUserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                int groupCounter = 1;

                for (DocumentSnapshot document : task.getResult()) {
                    if (groupCounter <= 6) { // Limit to 6 groups
                        setMyGroupButton(groupCounter, document);
                        groupCounter++;
                    }
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error loading my groups: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setMyGroupButton(int index, DocumentSnapshot document) {
        switch (index) {
            case 1:
                binding.btnMyGroup1.setText(document.getString("groupName"));
                binding.btnMyGroup1.setOnClickListener(v -> navigateToGroupDetail(document.getId()));
                break;
            case 2:
                binding.btnMyGroup2.setText(document.getString("groupName"));
                binding.btnMyGroup2.setOnClickListener(v -> navigateToGroupDetail(document.getId()));
                break;
            case 3:
                binding.btnMyGroup3.setText(document.getString("groupName"));
                binding.btnMyGroup3.setOnClickListener(v -> navigateToGroupDetail(document.getId()));
                break;
            case 4:
                binding.btnMyGroup4.setText(document.getString("groupName"));
                binding.btnMyGroup4.setOnClickListener(v -> navigateToGroupDetail(document.getId()));
                break;
            case 5:
                binding.btnMyGroup5.setText(document.getString("groupName"));
                binding.btnMyGroup5.setOnClickListener(v -> navigateToGroupDetail(document.getId()));
                break;
            case 6:
                binding.btnMyGroup6.setText(document.getString("groupName"));
                binding.btnMyGroup6.setOnClickListener(v -> navigateToGroupDetail(document.getId()));
                break;
        }
    }

    // Navigate to the group detail page
    private void navigateToGroupDetail(String groupId) {
        Bundle bundle = new Bundle();
        bundle.putString("groupId", groupId);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_groupDetailsFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
