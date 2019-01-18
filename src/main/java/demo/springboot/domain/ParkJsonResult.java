package demo.springboot.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParkJsonResult {
    public class ParkResultItem {
        private String name;
        private float score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }
    }
    @SerializedName("error_code")
    private Integer errorCode;
    private List<ParkResultItem> results;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public List<ParkResultItem> getResults() {
        return results;
    }

    public void setResults(List<ParkResultItem> results) {
        this.results = results;
    }
}
