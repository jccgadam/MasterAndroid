package Category;

import User.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 9/7/15.
 */
public class Category {
        private Long cId;

        private String name;

        private int followerNumber;

        private String createTime;

        private String createDate;

        private Set<User> users = new HashSet<User>();

//        private Set<Question> questions = new HashSet<>();

        public Set<User> getUsers() {
            return users;
        }

        public void setUsers(Set<User> users) {
            this.users = users;
        }

//        public Set<Question> getQuestions() {
//            return questions;
//        }
//
//        public void setQuestions(Set<Question> questions) {
//            this.questions = questions;
//        }

        public int getFollowerNumber() {
            return followerNumber;
        }

        public void setFollowerNumber(int followerNumber) {
            this.followerNumber = followerNumber;
        }

        public Long getcId() {
            return cId;
        }

        public void setcId(Long cId) {
            this.cId = cId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }

