package x21l_5388_com.example.assignment5;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostsAPI {
    @GET("posts")
    Call<List<Post>> getPosts();
    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Path("id") int id, @Body Post post);
    @GET("posts/{postId}/comments")
    Call<List<Comment>> getCommentsForPost(@Path("postId") int postId);
}
