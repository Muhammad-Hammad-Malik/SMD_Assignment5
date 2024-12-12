package x21l_5388_com.example.assignment5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private final Context context;
    private final List<Post> postList;
    private final PostsAPI postsApi;


    public PostsAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postsApi = retrofit.create(PostsAPI.class);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.postTitle.setText(post.getTitle());
        holder.postDescription.setText(post.getBody());


        holder.deleteButton.setOnClickListener(v -> {
            int postId = post.getId();
            deletePost(postId, position);
        });


        holder.updateButton.setOnClickListener(v -> showUpdateDialog(post, position));

        holder.viewCommentsButton.setOnClickListener(v ->{
            Intent I = new Intent(context, CommentsActivity.class);
            CommentsActivity.postID = post.getId();
            context.startActivity(I);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    private void deletePost(int postId, int position) {
        Call<Void> call = postsApi.deletePost(postId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    postList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PostsAdapter", "Error deleting post: " + t.getMessage());
                Toast.makeText(context, "Error deleting post", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showUpdateDialog(Post post, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Post");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_post, null);
        EditText titleInput = dialogView.findViewById(R.id.editPostTitle);
        EditText bodyInput = dialogView.findViewById(R.id.editPostBody);


        titleInput.setText(post.getTitle());
        bodyInput.setText(post.getBody());

        builder.setView(dialogView);


        builder.setPositiveButton("Update", (dialog, which) -> {
            String newTitle = titleInput.getText().toString().trim();
            String newBody = bodyInput.getText().toString().trim();

            if (!newTitle.isEmpty() && !newBody.isEmpty()) {
                post.setTitle(newTitle);
                post.setBody(newBody);
                updatePost(post, position);
            } else {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void updatePost(Post post, int position) {
        Call<Post> call = postsApi.updatePost(post.getId(), post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList.set(position, response.body());
                    notifyItemChanged(position);
                    Toast.makeText(context, "Post updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to update post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("PostsAdapter", "Error updating post: " + t.getMessage());
                Toast.makeText(context, "Error updating post", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView postTitle, postDescription;
        Button deleteButton, viewCommentsButton, updateButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDescription = itemView.findViewById(R.id.postDescription);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            viewCommentsButton = itemView.findViewById(R.id.viewCommentsButton);
            updateButton = itemView.findViewById(R.id.updateButton);
        }
    }
}
