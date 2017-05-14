package co.firetools.copperink.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import co.firetools.copperink.behaviors.Model;
import co.firetools.copperink.clients.PostClient;
import co.firetools.copperink.clients.UserClient;
import co.firetools.copperink.db.DBContract;

public class Post implements Model {
    public final static long   DEFAULT_OID = -1;
    public final static String UNSYNCED_ID = "__unsynced__";

    public final static String STATUS_QUEUED = "queued";
    public final static String STATUS_POSTED = "posted";
    public final static String STATUS_ERROR  = "error";

    public final static int SYNC_COMPLETE  = 0;
    public final static int SYNC_TO_CREATE = 1;
    public final static int SYNC_TO_UPDATE = 2;

    private String  id;
    private String  status;
    private String  content;
    private String  accountId;
    private String  authorId;
    private String  imageUrl;
    private long    oid;
    private long    postAt;
    private boolean synced;


    /**
     * Post Constructor - for server response
     */
    @JsonCreator
    public Post(
            @JsonProperty("id")         String  id,
            @JsonProperty("status")     String  status,
            @JsonProperty("content")    String  content,
            @JsonProperty("author_id")  String  authorId,
            @JsonProperty("account_id") String  accountId,
            @JsonProperty("image")      String  imageUrl,
            @JsonProperty("post_at")    long    postAt) {
        this(id, status, content, authorId, accountId, imageUrl, postAt, true);
    }


    /**
     * Post constructor - for local creation
     */
    public Post(String content, String accountId, String imagePath, long postAt) {
        this(UNSYNCED_ID, "queued", content, UserClient.getUser().getID(), accountId, imagePath, postAt, false);
    }


    /**
     * Post constructor - for local updates
     */
    public Post(String id, String status, String content, String authorId, String accountId, String imageUrl, long postAt, boolean synced) {
        this(DEFAULT_OID, id, status, content, authorId, accountId, imageUrl, postAt, synced);
    }


    /**
     * Post constructor - load from DB
     */
    public Post(long oid, String id, String status, String content, String authorId, String accountId, String imageUrl, long postAt, boolean synced) {
        this.id        = id;
        this.oid       = oid;
        this.status    = status;
        this.content   = content;
        this.authorId  = authorId;
        this.accountId = accountId;
        this.imageUrl  = imageUrl;
        this.postAt    = postAt;
        this.synced    = synced;
    }



    /**
     * Getters
     */
    public String  getID()        { return id;        }
    public String  getContent()   { return content;   }
    public String  getStatus()    { return status;    }
    public String  getImageUrl()  { return imageUrl;  }
    public String  getAccountID() { return accountId; }
    public String  getAuthorID()  { return authorId;  }
    public long    getPostAt()    { return postAt;    }
    public long    getOID()       { return oid;       }
    public boolean isSynced()     { return synced;    }


    /**
     * Get sync status
     */
    public int getSyncStatus() {
        if (synced)
            return SYNC_COMPLETE;
        else if (id.equals(UNSYNCED_ID))
            return SYNC_TO_CREATE;
        else
            return SYNC_TO_UPDATE;
    }


    /**
     * Pretty print datetime
     */
    public String prettyPostTime() {
        return PostClient.dateToString(getPostAt());
    }

    /**
     * Return appropriate contract
     */
    @Override
    public Contract getContract() {
        return new DBContract.PostTable();
    }



    /**
     * Serializes the Post object into a
     * JSON String using the Jackson library
     */
    public static String serialize(Post post) throws JsonProcessingException {
        // Serialize using private/public fields only,
        // ignore everything else (Setters, Getters, etc.)
        return new ObjectMapper()
            .setVisibility(PropertyAccessor.ALL,   JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .writeValueAsString(post);
    }


    /**
     * Deserializes JSON string back into an Post
     * object using the Jackson library
     */
    public static Post deserialize(String json) throws IOException {
        return new ObjectMapper().readValue(json, Post.class);
    }

    public static JSONObject prepareForRequest(Post post) throws JSONException {
        JSONObject jsonPost = new JSONObject();
        jsonPost.put("content",    post.getContent());
        jsonPost.put("post_at",    post.getPostAt());
        jsonPost.put("account_id", post.getAccountID());
        jsonPost.put("image",      post.getImageUrl());
        return jsonPost;
    }

}
