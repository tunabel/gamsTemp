package application.model.requestdto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginationRequestDto {

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
