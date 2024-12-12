package x21l_5388_com.example.assignment5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context context;
    private final List<Comment> commentList;


    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        Comment comment = commentList.get(position);

        holder.commenterName.setText(comment.getName());
        holder.commenterEmail.setText(comment.getEmail());
        holder.commentBody.setText(comment.getBody());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    // ViewHolder class
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commenterName, commenterEmail, commentBody;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commenterName = itemView.findViewById(R.id.commenterName);
            commenterEmail = itemView.findViewById(R.id.commenterEmail);
            commentBody = itemView.findViewById(R.id.commentBody);

        }
    }
}
