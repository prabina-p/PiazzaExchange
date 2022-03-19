import java.util.HashMap;
import java.util.ArrayList;

public class Forest {

    // Connects the InternalNode with their specific keywords in the hashmap
    private HashMap<String, InternalNode> forest;

    protected class InternalNode {

        String key; // The key of the internal node
        ArrayList<Post> posts; // The posts with this specific keyword
        ArrayList<InternalNode> children; // The children of this key

        /**
         * A constructor that initializes the InternalNode instance variables.
         *
         * @param key      Node's key
         */
        public InternalNode(String key, Post post) {
            this.key = key.toLowerCase();
            this.posts = new ArrayList<>();
            this.posts.add(post);
            this.children = new ArrayList<>();
        }

        /**
         * A constructor that initializes InternalNode variables. Note: This constructor is
         * used when you want to add a key with no related information yet. In this
         * case, you must create an empty ArrayList for the node.
         *
         * @param key Node's key
         */
        public InternalNode(String key) {
            this.key = key.toLowerCase();
            this.posts = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        /**
         * Return the key
         *
         * @return The key
         */
        public String getKey() {
            return this.key;
        }


        /**
         * Return the linked list of the node
         *
         * @return The linked list of the node
         */
        public ArrayList<Post> getPosts() {
            return this.posts;
        }

        public ArrayList<InternalNode> getChildren() {
            return this.children;
        }

        public void addChildren(InternalNode node) {
            this.children.add(node);
        }

        public void setChildren(ArrayList<InternalNode> children) {
            this.children = children;
        }

        public boolean removeChildren(InternalNode node) {
            return this.children.remove(node);
        }

        /**
         * Setter for the linked list of the node
         *
         * @param newPosts New linked list
         */
        public void setPostsList(ArrayList<Post> newPosts) {
            this.posts = newPosts;
        }

        /**
         * Append new data to the end of the existing linked list of the node
         *
         * @param data New data to be appended
         */
        public void addNewPost(Post data) {
            this.posts.add(data);
        }

        /**
         * Remove 'data' from the linked list of the node and return true. If the linked
         * list does not contain the value 'data', return false.
         *
         * @param data Info to be removed
         * @return True if data was found, false otherwise
         */
        public boolean removePost(Post data) {
            return this.posts.remove(data);
        }
    }

    /**
     * Constructor that initialize the instance variable of the forest
     */
    public Forest() {
        this.forest = new HashMap<>();
    }

    /**
     * Insert the specific key into the forest with InternalNode with empty posts
     *
     * @param key the key of the internal node
     */
    public void insert(String key) {
        String lowerKey = key.toLowerCase();
        InternalNode node = new InternalNode(lowerKey);
        this.forest.put(lowerKey, node);
    }

    /**
     * Insert the specific key and value pairs into the forest
     *
     * @param post insert the post according to the post's key
     */
    public void insert(Post post) {
        String postKeyWd = post.getKeyword().toLowerCase();
        InternalNode nodeWKey = this.nodeLookUp(postKeyWd);
        if (nodeWKey == null){
            nodeWKey = new InternalNode(postKeyWd, post);
            this.forest.put(postKeyWd, nodeWKey);
//            nodeWKey.addNewPost(post); // TODO: VERIFY
        }
        else {
            nodeWKey.addNewPost(post);
        }
    }

    /**
     * Helper method. Returns the node with the given key. 
     * If the key doesn’t exist in the forest, return null.
     * 
     * @param key querying the internal node with this specific key
     */
    public InternalNode nodeLookUp(String key) {
        key = key.toLowerCase();
        return this.forest.get(key);
    }

    /**
     * Get the posts that relate to a specific key. If the key does
     * not exist in the forest, throw IllegalArgumentException
     *
     * @param key the key
     * @return the Arraylist of posts
     */
    public ArrayList<Post> getPosts(String key) {
        if (this.nodeLookUp(key.toLowerCase()) == null){
            throw new IllegalArgumentException();
        }
        return this.nodeLookUp(key.toLowerCase()).getPosts();
    }

    /**
     * add Connection of more than one internal nodes by their keys
     *
     * @param parent the parent node's key
     * @param children the array of children node's keys
     */
    public void addConnection(String parent, String[] children) {
        InternalNode parentNode = nodeLookUp(parent.toLowerCase());
        if (parentNode == null) {
            this.insert(parent);
            parentNode = nodeLookUp(parent);
        }
        ArrayList<InternalNode> childrenList = new ArrayList<>();
        for (String key: children) {
            key = key.toLowerCase();
            InternalNode node = nodeLookUp(key);
            if (node != null) {
                childrenList.add(node);
            } else {
                this.insert(key);
                childrenList.add(nodeLookUp(key));
            }
        }
        parentNode.setChildren(childrenList);
    }

    /**
     * add connection for one internal nodes by their keys
     *
     * @param parent the key of the parent key
     * @param child the key of the child key
     */
    public void addConnection(String parent, String child) {
        parent = parent.toLowerCase();
        InternalNode parentNode = nodeLookUp(parent);
        if (parentNode == null) {
            this.insert(parent);
            parentNode = nodeLookUp(parent);
        }
        child = child.toLowerCase();
        InternalNode node = nodeLookUp(child);
        if (node != null) {
            parentNode.addChildren(node);
        } else {
            this.insert(child);
            parentNode.addChildren(nodeLookUp(child));
        }
    }

    /**
     * query the connection between the internal node by traversing the edge
     * of the forest
     *
     * @param key the initial start point of
     * @return the children of that specific node
     */
    public String[] queryConnection(String key) {
        String keyLowerCase = key.toLowerCase();
        InternalNode queryNode = this.nodeLookUp(keyLowerCase);
        if (queryNode == null){
            return null;
        }
        ArrayList<Forest.InternalNode> childrenNode = queryNode.getChildren();
        String[] keys = new String[childrenNode.size()];
        for (int i = 0; i < childrenNode.size(); i++){
            keys[i] = childrenNode.get(i).getKey();
        }
        return keys;
    }

    /**
     * Delete the specific value (post) from the forest
     *
     * @param post delete the post according to the post's key
     * @return whether the deletion was successful
     */
    public boolean delete(Post post) {
        String postKeyWd = post.getKeyword().toLowerCase();
        InternalNode postNode = nodeLookUp(postKeyWd);
        if (postNode == null){
            return false;
        }
        return this.forest.get(postKeyWd).removePost(post);
//        return postNode.removePost(post);

    }
}
