package malenquilla.cds.common.payloads.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PaginationRequest {
    @JsonIgnore
    private static final int DEFAULT_PAGE = 0;

    @JsonIgnore
    private static final int DEFAULT_SIZE = 100;

    @Min(0)
    private int page = DEFAULT_PAGE;

    @Min(1)
    private int size = DEFAULT_SIZE;
}
