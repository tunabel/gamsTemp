package application.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageReq {

    @JsonProperty("currentpage")
    private int currentPage = 0;
    @JsonProperty("numberofrecord")
    private int numberRecord = 5;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNumberRecord() {
        return numberRecord;
    }
}
