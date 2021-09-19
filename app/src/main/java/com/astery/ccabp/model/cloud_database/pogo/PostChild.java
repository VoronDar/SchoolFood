package com.astery.ccabp.model.cloud_database.pogo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class PostChild {

        @SerializedName("parentId")
        @Expose
        private int parentId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("patronymic")
        @Expose
        private String patronymic;
        @SerializedName("birth")
        @Expose
        private String birth;
        @SerializedName("schoolId")
        @Expose
        private int schoolId;
        @SerializedName("className")
        @Expose
        private String className;
        @SerializedName("isFree")
        @Expose
        private boolean isFree;
        @SerializedName("classCode")
        @Expose
        private String classCode;

        public PostChild() {
        }

        public PostChild(int parentId, String name, String lastName, int schoolId, String className, boolean isFree, String classCode) {
            super();
            this.parentId = parentId;
            this.name = name;
            this.lastName = lastName;
            this.schoolId = schoolId;
            this.className = className;
            this.isFree = isFree;
            this.classCode = classCode;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String classId) {
            this.className = classId;
        }

        public boolean isFree() {
            return isFree;
        }

        public void setFree(boolean free) {
            isFree = free;
        }

        public String getClassCode() {
            return classCode;
        }

        public void setClassCode(String classCode) {
            this.classCode = classCode;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }
    }
