package co.firetools.copperink.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import co.firetools.copperink.services.UserService;

public class Post {
    public final static String UNSYNCED_ID = "__unsynced__";

    private String  id;
    private String  status;
    private String  content;
    private String  accountId;
    private String  authorId;
    private String  imageUrl;
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
            @JsonProperty("image_url")  String  imageUrl,
            @JsonProperty("post_at")    long    postAt) {
        this.id        = id;
        this.status    = status;
        this.content   = content;
        this.authorId  = authorId;
        this.accountId = accountId;
        this.imageUrl  = imageUrl;
        this.postAt    = postAt;
        this.synced    = true;
    }


    /**
     * Post constructor - for local creation
     */
    public Post(String content, String accountId, String imagePath, long postAt) {
        this.id        = UNSYNCED_ID;
        this.status    = "queued";
        this.synced    = false;
        this.authorId  = UserService.getUser().getID();
        this.accountId = accountId;
        this.content   = content;
        this.imageUrl  = imagePath;
        this.postAt    = postAt;
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
    public boolean isSynced()     { return synced;    }

}
