package co.firetools.copperink.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import co.firetools.copperink.behaviors.Model;
import co.firetools.copperink.db.DBContract;
import co.firetools.copperink.services.UserService;

public class Post implements Model {
    public final static String UNSYNCED_ID = "__unsynced__";
    public final static long   DEFAULT_OID = -1;

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
            @JsonProperty("image_url")  String  imageUrl,
            @JsonProperty("post_at")    long    postAt) {
        this(DEFAULT_OID, id, status, content, authorId, accountId, imageUrl, postAt, true);
    }


    /**
     * Post constructor - for local creation
     */
    public Post(String content, String accountId, String imagePath, long postAt) {
        this(DEFAULT_OID, UNSYNCED_ID, "queued", content, UserService.getUser().getID(), accountId, imagePath, postAt, false);
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
     * Return appropriate contract
     */
    @Override
    public Contract getContract() {
        return new DBContract.PostTable();
    }
}
