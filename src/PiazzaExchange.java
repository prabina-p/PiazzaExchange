import javafx.geometry.Pos;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PiazzaExchange {

    String courseID; // unique identifier for the course (Eg:DSC30)
    Instructor instructor; // instructor of this course
    ArrayList<User> users; // stores users enrolled in this course
    ArrayList<Post> posts; // stores all posts created in this course
    ArrayList<Post> unanswered; // stores unanswered posts(optional)
    String status; // status of the course (active/inactive)
    boolean selfEnroll; // whether the self-enrollment option is enabled
    private Forest keywordForest; // stores keywords and their corresponding posts in the structure of forest (mentioned in later sections)

    private static final String STATS_STRING = "%s submitted %d posts, answered %d posts, received %d endorsements\n";

    /**
     * Constructor of the PiazzaExchange.
     *
     * @param instructor the instructor of this class
     * @param courseID The course ID
     * @param selfEnroll whether the class allow self enrolling or not.
     */
    public PiazzaExchange(Instructor instructor, String courseID, boolean selfEnroll) {
        this.instructor = instructor;
        this.courseID = courseID;
        this.selfEnroll = selfEnroll;
        this.status = "inactive";
        this.users = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.unanswered = new ArrayList<>();
        this.keywordForest = new Forest();
    }

    //is there a reason why we don't combine these two constructors?
    //by default, selfEnroll is false, and we're setting DSC30 as default courseID

    /**
     * Constructor of the PiazzaExchange with a roster of user.
     *
     * @param instructor the instructor who create this piazza
     * @param roster the list of Users that will be included in this piazza
     */
    public PiazzaExchange(Instructor instructor, ArrayList<User> roster) {
        this.instructor = instructor;
        this.courseID = "DSC30";
        this.selfEnroll = false;
        this.status = "inactive";
        this.users = roster;
        for (User user : roster){
            user.courses.add(this);
        }
        this.posts = new ArrayList<>();
        this.unanswered = new ArrayList<>();
        this.keywordForest = new Forest();
    }

    public Forest getKeywordForest() {
        return this.keywordForest;
    }

    /**
     * Query for the top two endorsed posts
     *
     * @return two posts that has the highest endorsed
     */
    public Post[] computeTopTwoEndorsedPosts() {
        Post[] top2Endorsed = new Post[2];
        if (this.posts.size() == 0){
            top2Endorsed[0] = null;
            top2Endorsed[1] = null;
        }
        else if (this.posts.size() == 1){
            top2Endorsed[0] = posts.get(0);
            top2Endorsed[1] = null;
        }
        Post num1Post = null;
        int num1 = 0;
        Post num2Post = null;
        int num2 = 0;
        for (Post post : posts){
            int curCount = post.endorsementCount;
            if (curCount >= num1){
                num2 = num1;
                num2Post = num1Post;
                num1Post = post;
                num1 = curCount;
            }
            else if (curCount >= num2){
                num2Post = post;
                num2 = curCount;
            }
        }
        top2Endorsed[0] = num1Post;
        top2Endorsed[1] = num2Post;
        return top2Endorsed;
    }

    
    /* helper method for getTopStudentContributions() */

    public int getStudentContributions(User u) {
        return u.numOfPostSubmitted + u.numOfPostsAnswered + u.numOfEndorsement;
    }

    /**
     * get recent-30 day stats(including today), where index i corresponds to ith day away from current day
     *
     * @return integer array with the daily post status
     */
    public int[] computeDailyPostStats() {
        int[] last30DaysArr = new int[30];
        int numOfPostsPerDay;
        LocalDate curDate = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            numOfPostsPerDay = 0;
            for (int j = 0; j < this.posts.size(); j++) {
                LocalDate thisPostDate = this.posts.get(j).getDate();
                if (ChronoUnit.DAYS.between(thisPostDate, curDate) == i){
                    numOfPostsPerDay++;
                }
            }
            last30DaysArr[i] = numOfPostsPerDay;
        }
        return last30DaysArr;
    }

    /**
     * get the month post stats for the last 12 month
     *
     * @return integer array that indicates the monthly status.
     */
    public int[] computeMonthlyPostStats(){
        int[] monthlyPostsArr = new int[12];
        int numOfPostsPerMonths;
        LocalDate curDate = LocalDate.now();
        for (int i = 0; i < 12; i++){
            numOfPostsPerMonths = 0;
            for (int j = 0; j < this.posts.size(); j++){
                LocalDate thisPostDate = this.posts.get(j).getDate();
                if (ChronoUnit.MONTHS.between(thisPostDate, curDate) == i){
                    numOfPostsPerMonths++;
                }
            }
            monthlyPostsArr[i] = numOfPostsPerMonths;
        }
        return monthlyPostsArr;
    }

    /**
     * Activate the pizza. This action should be done by instructor only.
     *
     * @param u the instructor who wish to activate the class
     * @return successfulness of the action call
     */
    public boolean activatePiazza(User u){
        if (u instanceof Instructor && this.status.equals("inactive")) { // TODO: equals or == ?
            this.status = "active";
            return true;
        }
        return false;
    }

    /**
     * Activate the pizza. This action should be done by instructor only.
     *
     * @param u the instructor who wish to activate the class
     * @return successfulness of the action call
     */
    public boolean deactivatePiazza(User u){
        if (u instanceof Instructor && this.status.equals("active")) {
            this.status = "inactive";
            this.selfEnroll = false;
            return true;
        }
        return false;
    }

    /**
     * Enroll the user to this PiazzaExchange. If the self enroll is disabled, only
     * instructor and tutor can request a new enrollment action.
     *
     * @param requester the requester of enrollment
     * @param u the user to enroll
     * @return successfulness of the action call
     */
    public boolean enrollUserToDatabase(User requester, User u){
        if (this.status.equals("active") && !this.users.contains(u)){
            if (this.selfEnroll){
                this.users.add(u);
                u.courses.add(this);
                return true;
            }
            else if (requester instanceof Instructor || requester instanceof Tutor){
                this.users.add(u);
                u.courses.add(this);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    /**
     * Enroll this user to PiazzaExchange
     *
     * @param u the user to enroll
     * @return successfulness of the action call
     */
    public boolean enrollUserToDatabase(User u){
        return this.enrollUserToDatabase(u, u);
    }

    ////////////// BEGIN BENCHMARKED METHOD /////////////

    /**
     * Given a specific posts, add this post to the database
     *
     * @param u The user that initiate this add-post action
     * @param p the post that we are going to add to the database
     * @throws OperationDeniedException when the action is not allowed
     */
    public void addPostToDatabase(User u, Post p) throws OperationDeniedException {
        if (!this.users.contains(u) || this.status.equals("inactive")){
            throw new OperationDeniedException();
        }
        this.posts.add(p);
        u.posts.add(p);
        u.numOfPostSubmitted++;
        if (p instanceof Question) {
            this.unanswered.add(p);
        }
    }

    /**
     * Get the post posted by this user that has the specific keyword
     *
     * @param u the poster of the post
     * @param keyword the keywords that we are going to query on
     * @return the post array that contains every single post that has the keyword
     */
    public Post[] retrievePost(User u, String keyword){
        ArrayList<Post> userPostsArr = new ArrayList<>();;
        for (Post post: u.posts){
            if (post.getKeyword().equals(keyword)){ //TODO: equals or == ?
                userPostsArr.add(post);
            }
        }
        Post[] thisUserPosts = new Post[userPostsArr.size()];
        for (int i = 0; i < thisUserPosts.length; i++){
            thisUserPosts[i] = userPostsArr.get(i);
        }
        return thisUserPosts;
    }

    /**
     * Get the post that has the specific keyword
     *
     * @param keyword the keyword that we are searching on
     * @return the post array that contains every single post that has the keyword
     */
    public Post[] retrievePost(String keyword){
        ArrayList<Post> userPostsArr = new ArrayList<>();;
        for (Post post : this.posts){
            if (post.getKeyword().equals(keyword)){
                userPostsArr.add(post);
            }
        }
        Post[] thisUserPosts = new Post[userPostsArr.size()];
        for (int i = 0; i < thisUserPosts.length; i++){
            thisUserPosts[i] = userPostsArr.get(i);
        }
        return thisUserPosts;
    }

    /**
     * Get the post with specific poster
     *
     * @param u the poster of posts
     * @return the post array that contains every single post that has specified poster u
     */
    public Post[] retrievePost(User u) {
        ArrayList<Post> userPostsArr = u.posts;
        Post[] thisUserPosts = new Post[userPostsArr.size()];
        for (int i = 0; i < thisUserPosts.length; i++){
            thisUserPosts[i] = userPostsArr.get(i);
        }
        return thisUserPosts;
    }

    /**
     * delete the post from the PE. User should be Instructor
     * return whether the post got successfully deleted or not
     *
     * @param u the user who initiate this action
     * @param p the post to delete
     * @return whether the action is successful
     * @throws OperationDeniedException when the action is denied
     */
    public boolean deletePostFromDatabase(User u, Post p) throws OperationDeniedException {
        if (!(u instanceof Instructor)){
            throw new OperationDeniedException();
        }
        if (this.posts.contains(p)){ //
            this.posts.remove(p); // TODO: need to worry abt updating post? perhaps not @1058
            this.unanswered.remove(p);
            return true;
        }
        return false;
    }

    /**
     * Compute the most urgent question in the unanswered post DS
     * for future answering.
     *
     * @return the Post with the highest urgency rating
     */
    public Post computeMostUrgentQuestion() {
        int mostUrgent = 0;
        Post mostUrgentPost = null;
        if (this.unanswered.size() == 0){
            return null;
        }
        for (Post post : this.unanswered){
            int curUrgency = post.calculatePriority();
            if (curUrgency >= mostUrgent){
                mostUrgent = curUrgency;
                mostUrgentPost = post;
            }
        }
        return mostUrgentPost;
    }

    /**
     * Compute the top K urgent question from the unanswered post DS
     *
     * @param k the number of unanswered post that we want to have
     * @return the post array that is sorted by the urgency of the post
     * @throws OperationDeniedException when the operation is denied
     */
    public Post[] computeTopKUrgentQuestion(int k) throws OperationDeniedException{
        if (k > this.unanswered.size()){
            throw new OperationDeniedException();
        }
        ArrayList<Post> unansweredQuestionsCopy = new ArrayList<>(unanswered);
        ArrayList<Post> topKUrgentQuestions = new ArrayList<>();
        for (int i = 0; i < k; i++){
            int PostWMostUrgencyI = 0;
            for (int j = 1; j < unansweredQuestionsCopy.size(); j++){
                int curUrgency = posts.get(j).calculatePriority();
                if (curUrgency > unansweredQuestionsCopy.get(PostWMostUrgencyI).calculatePriority()){
                    PostWMostUrgencyI = j;
                }
            }
            topKUrgentQuestions.add(unansweredQuestionsCopy.remove(PostWMostUrgencyI));
        }
        Post[] topKUrgencyQues = new Post[k];
        for (int i = 0; i < k; i++){
            topKUrgencyQues[i] = topKUrgentQuestions.get(i);
        }
        return topKUrgencyQues;
    }

    /**
     * answer the post. Removed from the unanswered post DataStructure
     *
     * @param u the answerer of the post
     * @param p the post that u is trying to answer
     * @param response the response to be appended to the post as an answer
     * @return the Post instance of the answered post
     * @throws OperationDeniedException when the operation is denied
     */
    public Post answerQuestion(User u, Post p, String response) throws OperationDeniedException{
        if (!posts.contains(p)){
            throw new OperationDeniedException();
        }
        if (p instanceof Question){
            boolean questionAnswered = u.answerQuestion(p, response);
            if (questionAnswered) {
                unanswered.remove(p);
            }
            return p;
        }
        return null;
    }

    ////////////// END BENCHMARKED METHOD /////////////

    /**
     *
     * @param u
     * @return
     */
    public String viewStats(User u){
        String stats = "";
        if (u instanceof Student){
            stats += String.format(STATS_STRING, u.username, u.numOfPostSubmitted, u.numOfPostsAnswered, u.numOfEndorsement);
        }
        else if (u instanceof Instructor || u instanceof Tutor){
            for (User user : this.users){
                if (user instanceof Student){
                    stats += String.format(STATS_STRING, user.username, user.numOfPostSubmitted, user.numOfPostsAnswered, user.numOfEndorsement);
                }
            }
        }
        return stats.trim();
    }

    /**
     * Retrieve all the posts from this piazza
     *
     * @param u the user who initiate this action
     * @return the posts array that contains every single post
     *      in this piazza
     */
    public Post[] retrieveLog(User u){
        Post[] postsArr = new Post[this.posts.size()];
        for (int i = 0; i < postsArr.length; i++){
            postsArr[i] = this.posts.get(i);
        }
        return postsArr;
    }

    //If the length > 10, students only be able to access the first 10 posts right?

    /**
     * Retrieve posts log with specified length
     *
     * @param u the user who initiate this action
     * @param length of the posts that is allowed to fetch
     * @return the posts array that satisfy the conditions
     */
    public Post[] retrieveLog(User u, int length){
        if (u instanceof Student){
            if (length > 10){
                length = 10;
            }
        }
        if (length > this.posts.size()){
            length = this.posts.size();
        }
        Post[] postsArr = new Post[length];
        for (int i = 0; i < postsArr.length; i++){
            postsArr[i] = posts.get(i);
        }
        return postsArr;
    }

    private String[] getEleMultipleIndex(String[] arr, int[] indexes) {
        String[] output = new String[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            output[i] = arr[indexes[i]];
        }
        return output;
    }

    /**
     * Helper method that initialize the semantic connection
     * of the keywordForest.
     */
    private void initializeForest() {
        String[] allKeywords = new String[]{"tree", "midterm", "heap", "heap sort", "in place",
                "bst", "linked list", "queue",
                "priority queue", "SLL", "DLL", "hash table", "collision", "element", "hash function", "bloom filters",
                 "probing", "linear probing", "quadratic probing", "double hashing", "rehash", "chaining"};
        int[][] childrenIndex = {
                {2, 5}, // i = 0
                {5, 6},
                {7, 3},
                {4},
                {},
                {}, // i = 5
                {9, 10},
                {8},
                {},
                {},
                {}, // i = 10
                {12, 13, 14, 15},
                {16},
                {},
                {19, 20, 21},
                {}, // i = 15
                {17, 18},
                {},
                {},
                {},
                {}, // i = 20
                {}
        };
        for (int i = 0; i < allKeywords.length; i++) {
            keywordForest.addConnection(allKeywords[i], getEleMultipleIndex(allKeywords, childrenIndex[i]));
        }
    }

    /**
     * Sort by the title, return the first k occurrences of the posts with the keyword
     * Forest of tree and storing key using HashMap.
     *
     * @param keyword The keyword that we initiate the starting point of the search
     * @param k the number of similar post that we are querying
     */
    public Post[] computeKSimilarPosts(String keyword, int k) {
        String keyWdLowerCase = keyword.toLowerCase();
        this.initializeForest();
        String[] childrenKeyWd = keywordForest.queryConnection(keyWdLowerCase);
        Post[] kSimilarPosts= new Post[k];
        int kCount = 0;
        for (int i = 0; i < childrenKeyWd.length; i++){
            Forest.InternalNode childNode = keywordForest.nodeLookUp(childrenKeyWd[i]);
            for (Post post : childNode.getPosts()){
                if (kCount == k) {
                    break;
                }
                kSimilarPosts[kCount] = post;
                kCount++;
            }
        }
        return kSimilarPosts;
    }

    /**
     * Sort by the title, return the first k occurrences of the posts with the keyword
     * Forest of tree of BST and store key using HashMap.
     */
    public Post[] computeKSimilarPosts(String keyword, int k, int level) {
        // TODO; not needed for checkpoint
        return null;
    }

    /**
     * describes basic course info, user and post status
     * NOT GRADED, for your own debugging purposes
     */
    public String toString(){
        return this.courseID;
    }
}
