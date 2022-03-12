public class Note extends Post{

    public Note(User poster, String header, String UID) {
        // TODO
    }

    public Note(User poster, String header, String text, String keyword, String PEID, String UID)  {
        // TODO
    }

    @Override
    public String getText(User u){
        return this.text; //verify
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }
}
