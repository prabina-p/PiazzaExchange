import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Post implements Comparable<Post> {

    String UID; // unique identifier for each post
    String parentPEID; // course ID that the post is created
    int endorsementCount; // number of endorsements of the post (any type of User can contribute to this)
    boolean endorsedByCourseStaff; // whether a course staff endorses the post (Only Tutor and Instructor can change this)
    private String header; // header of the post
    protected String text; // responsible for the content both Note and Question subclass
    boolean isPrivate; // whether a post is created private
    User poster; // user that creates the post
    LocalDate date; // date when the post is created
    int priority; // the urgency, in a decimal number, for the post to be answered by an instructor/tutor. The higher, the larger extent of priority/urgency
    private String keyword; // keyword associated with this post

    /**
     * Constructor for Post
     *
     * @param poster the poster of the post
     * @param header the header of the post
     */
    public Post(User poster, String header, String UID) {
        this.UID = UID;
        this.poster = poster;
        this.header = header;
        this.text = "";
        this.keyword = null;
        this.parentPEID = null;
        this.endorsementCount = 0;
        this.endorsedByCourseStaff = false;
        this.isPrivate = false;
        this.date = LocalDate.now();
        this.priority = 0;
    }

    /**
     * Overloaded constructor for Post
     */
    public Post(User poster, String header, String text, String keyword, String PEID, String UID) {
        this.poster = poster;
        this.header = header;
        this.text = text;
        this.keyword = keyword;
        this.UID = UID;
        this.parentPEID = PEID;
        this.date = LocalDate.now();
        this.priority = 0;
        this.endorsementCount = 0;
        this.endorsedByCourseStaff = false;
        this.isPrivate = false;
    }

    /**
     * Getter method of the keyword of the post
     * @return the keyword of the post
     */
    public String getKeyword() {
        return this.keyword;
    }

    /**
     * Getter method of the text of the post
     * @return the text of the post
     */
    public abstract String getText(User u) throws OperationDeniedException;

    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Set the date of the post to the provided new date
     * 
     * @param newDate the new date we are setting the post to
     */
    public void setDate(LocalDate newDate) {
        this.date = newDate;
    }

    public User getPoster() {
        return this.poster;
    }

    public void editText(String text) {
        this.text = text;
    }

    public String toString() {
        String necessaryInfo;
        necessaryInfo = this.text;
        return necessaryInfo; // TODO: change it later
    }

    /**
     * Compare two posts by their priority
     *
     * @param other the other post that we are comparing
     * @return whether this post is larger than the other post
     */
    public int compareTo(Post other){
        int thisPriority = this.calculatePriority();
        int otherPriority = other.calculatePriority();
        if (thisPriority < otherPriority){
            return -1;
        }
        else if (thisPriority == otherPriority){
            return 0;
        }
        else {
            return 1;
        }
    }

    public int calculatePriority() {
        int updatedPriority = this.endorsementCount+(int)(this.date.until(LocalDate.now()).getDays()/3.0);
        this.priority = updatedPriority;
        return this.priority;
    }




}
