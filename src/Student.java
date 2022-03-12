public class Student extends User{

    public Student(String PID, String username) {
        super(PID, username);
    }

    public boolean answerQuestion(Post p, String response) {
        boolean eligibleUser = false;
        if (p.isPrivate){
            eligibleUser = (p.poster == this);
        }
        if (response.length() > 50 || !eligibleUser || !(p instanceof Question)){
            return false;
        }
        ((Question) p).answerQuestion(response);
        if (!this.posts.contains(p)) {
            this.posts.add(p);
        } //TODO: need to add some more when doing PE
        this.numOfPostsAnswered++;
        return true;
    }

    @Override
    public boolean endorsePost(Post p) {
        boolean eligibleUser = false;
        if (p.isPrivate){
            eligibleUser = (p.poster == this);
        }
        if (!eligibleUser){
            return false;
        }
        else {
            p.endorsementCount++;
            return true;
        }
    }

    @Override
    public boolean editPost(Post p, String newText) {
        if (!p.isPrivate || p.poster == this){
            p.editText(newText);
            if (!this.posts.contains(p)){
                this.numOfPostSubmitted++; // TODO: should we add p to posts?
            }
            return true;
        }
        return false;
    }

    @Override
    public String displayName() {
        return "Student: " + this.username + ", PID: " + this.PID;
    }
}
