package Answer;
import User.User;
import Question.Question;
/**
 * Created by root on 9/8/15.
 */
public class Answer {

        private long aId;

        private String content;

        private boolean isBest;

        private int likes;

        private int views;


        private Question q;


        private User u;

        public User getU() {
            return u;
        }

        public void setU(User u) {
            this.u = u;
        }

        public Question getQ() {
            return q;
        }

        public void setQ(Question q) {
            this.q = q;
        }

        public long getaId() {
            return aId;
        }

        public void setaId(long aId) {
            this.aId = aId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isBest() {
            return isBest;
        }

        public void setIsBest(boolean isBest) {
            this.isBest = isBest;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }
}
